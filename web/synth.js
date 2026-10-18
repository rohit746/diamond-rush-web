/*
 * A compact General MIDI synthesizer for the game's 21 MIDI clips (snd.f), on the Web Audio API.
 *
 * Clips are parsed once into absolute-time notes. Each program family gets an oscillator recipe (waveform,
 * envelope, filter), channel 10 gets a synthesized drum kit, and volume, expression, pan and pitch bend are
 * honoured. Playback keeps time with setTimeout even before audio is unlocked, so the game's end-of-media
 * events arrive on schedule either way.
 */
'use strict';

window.DRSynth = (() => {
    let ac = null;
    let master = null;
    let noise = null;
    let muted = false;
    const VOLUME = 0.8;
    const clips = new Map();
    let nextId = 1;

    function unlock() {
        const AC = window.AudioContext || window.webkitAudioContext;
        if (!AC) {
            return;
        }
        if (!ac) {
            ac = new AC();
            const compressor = ac.createDynamicsCompressor();
            compressor.threshold.value = -14;
            compressor.ratio.value = 4;
            compressor.connect(ac.destination);
            master = ac.createGain();
            master.gain.value = muted ? 0 : VOLUME;
            master.connect(compressor);
            noise = ac.createBuffer(1, ac.sampleRate * 2, ac.sampleRate);
            const data = noise.getChannelData(0);
            for (let i = 0; i < data.length; i++) {
                data[i] = Math.random() * 2 - 1;
            }
        }
        if (ac.state === 'suspended') {
            ac.resume();
        }
    }

    let hidden = false;

    function applyVolume() {
        if (master) {
            master.gain.setTargetAtTime(muted || hidden ? 0 : VOLUME, ac.currentTime, 0.02);
        }
    }

    function setMuted(value) {
        muted = value;
        applyVolume();
    }

    function setHidden(value) {
        hidden = value;
        applyVolume();
    }

    // ---- Standard MIDI File parsing into absolute-time notes

    function parse(b) {
        let p = 0;
        const tag = () => String.fromCharCode(b[p], b[p + 1], b[p + 2], b[p + 3]);
        const u32 = () => ((b[p++] << 24) | (b[p++] << 16) | (b[p++] << 8) | b[p++]) >>> 0;
        const u16 = () => (b[p++] << 8) | b[p++];
        const vlq = () => {
            let v = 0;
            let c;
            do {
                c = b[p++];
                v = (v << 7) | (c & 0x7f);
            } while (c & 0x80 && p < b.length);
            return v;
        };
        if (tag() !== 'MThd') {
            throw new Error('not a MIDI file');
        }
        p += 4;
        const headerLength = u32();
        const headerStart = p;
        u16(); // format
        const trackCount = u16();
        const division = u16();
        p = headerStart + headerLength;

        const events = [];
        let order = 0;
        for (let t = 0; t < trackCount && p + 8 <= b.length; t++) {
            const id = tag();
            p += 4;
            const length = u32();
            const end = Math.min(p + length, b.length);
            if (id !== 'MTrk') {
                p = end;
                t--;
                continue;
            }
            let tick = 0;
            let running = 0;
            while (p < end) {
                tick += vlq();
                let status = b[p];
                if (status & 0x80) {
                    p++;
                    if (status < 0xf0) {
                        running = status;
                    }
                } else {
                    status = running;
                }
                if (status === 0xff) {
                    const type = b[p++];
                    const len = vlq();
                    if (type === 0x51 && len === 3) {
                        events.push({ tick, order: order++, kind: 'tempo', value: (b[p] << 16) | (b[p + 1] << 8) | b[p + 2] });
                    }
                    p += len;
                    if (type === 0x2f) {
                        events.push({ tick, order: order++, kind: 'end' });
                        break;
                    }
                } else if (status === 0xf0 || status === 0xf7) {
                    p += vlq();
                } else if (status >= 0x80) {
                    const kind = status & 0xf0;
                    const d1 = b[p++];
                    const d2 = kind === 0xc0 || kind === 0xd0 ? 0 : b[p++];
                    events.push({ tick, order: order++, kind, ch: status & 15, d1, d2 });
                } else {
                    break;
                }
            }
            p = end;
        }
        events.sort((x, y) => x.tick - y.tick || x.order - y.order);

        const smpte = division & 0x8000;
        const ticksPerQuarter = division & 0x7fff || 480;
        let secondsPerTick = smpte ? 1 / ((256 - (division >> 8)) * (division & 0xff)) : 0.5 / ticksPerQuarter;
        const program = new Array(16).fill(0);
        const volume = new Array(16).fill(100);
        const expression = new Array(16).fill(127);
        const pan = new Array(16).fill(64);
        const bend = new Array(16).fill(0);
        const bendRange = new Array(16).fill(2);
        const rpn = new Array(16).fill(0x3fff);
        const bends = Array.from({ length: 16 }, () => []);
        const active = new Map();
        const notes = [];
        let time = 0;
        let lastTick = 0;

        const finish = (note, at) => {
            note.d = Math.max(at - note.t, 0.03);
            notes.push(note);
        };
        for (const ev of events) {
            time += (ev.tick - lastTick) * secondsPerTick;
            lastTick = ev.tick;
            const ch = ev.ch;
            if (ev.kind === 'tempo') {
                if (!smpte) {
                    secondsPerTick = ev.value / 1e6 / ticksPerQuarter;
                }
            } else if (ev.kind === 0x90 && ev.d2 > 0) {
                const key = ch * 128 + ev.d1;
                const note = {
                    t: time, d: 0, ch, key: ev.d1, vel: ev.d2, prog: program[ch],
                    vol: volume[ch], expr: expression[ch], pan: pan[ch], bend: bend[ch], bends: null,
                };
                if (!active.has(key)) {
                    active.set(key, []);
                }
                active.get(key).push(note);
            } else if (ev.kind === 0x80 || ev.kind === 0x90) {
                const list = active.get(ch * 128 + ev.d1);
                if (list && list.length) {
                    finish(list.shift(), time);
                }
            } else if (ev.kind === 0xb0) {
                const value = ev.d2;
                switch (ev.d1) {
                    case 7: volume[ch] = value; break;
                    case 10: pan[ch] = value; break;
                    case 11: expression[ch] = value; break;
                    case 100: rpn[ch] = (rpn[ch] & 0x3f80) | value; break;
                    case 101: rpn[ch] = (rpn[ch] & 0x7f) | (value << 7); break;
                    case 6: if (rpn[ch] === 0) bendRange[ch] = value; break;
                    case 121: volume[ch] = 100; expression[ch] = 127; pan[ch] = 64; bend[ch] = 0; break;
                    case 120:
                    case 123:
                        for (const [key, list] of active) {
                            if (key >> 7 === ch) {
                                list.forEach((note) => finish(note, time));
                                active.delete(key);
                            }
                        }
                        break;
                    default: break;
                }
            } else if (ev.kind === 0xc0) {
                program[ch] = ev.d1;
            } else if (ev.kind === 0xe0) {
                bend[ch] = (((ev.d2 << 7) | ev.d1) - 8192) / 8192 * bendRange[ch];
                bends[ch].push([time, bend[ch]]);
            }
        }
        for (const list of active.values()) {
            list.forEach((note) => finish(note, time + 0.1));
        }
        let duration = time;
        for (const note of notes) {
            const changes = bends[note.ch].filter(([t]) => t > note.t && t < note.t + note.d);
            note.bends = changes.length ? changes : null;
            duration = Math.max(duration, note.t + Math.min(note.d, 0.25));
        }
        notes.sort((x, y) => x.t - y.t);
        return { notes, duration };
    }

    // ---- voices

    function envGain(dest, start) {
        const g = ac.createGain();
        g.gain.setValueAtTime(0, start);
        g.connect(dest);
        return g;
    }

    function noiseSource(start, stop, nodes) {
        const src = ac.createBufferSource();
        src.buffer = noise;
        src.loop = true;
        src.start(start, Math.random() * 1.5);
        src.stop(stop);
        nodes.push(src);
        return src;
    }

    function filter(type, freq, q) {
        const f = ac.createBiquadFilter();
        f.type = type;
        f.frequency.value = Math.min(freq, ac.sampleRate / 2 - 100);
        f.Q.value = q || 0.7;
        return f;
    }

    /** Pitched percussion: a sine (or triangle) whose pitch falls quickly. */
    function thump(dest, start, amp, f0, f1, decay, nodes, wave) {
        const osc = ac.createOscillator();
        osc.type = wave || 'sine';
        osc.frequency.setValueAtTime(f0, start);
        osc.frequency.exponentialRampToValueAtTime(Math.max(f1, 20), start + decay * 0.6);
        const g = envGain(dest, start);
        g.gain.linearRampToValueAtTime(amp, start + 0.003);
        g.gain.exponentialRampToValueAtTime(0.0005, start + decay);
        osc.connect(g);
        osc.start(start);
        osc.stop(start + decay + 0.05);
        nodes.push(osc);
    }

    function hiss(dest, start, amp, decay, type, freq, q, nodes) {
        const f = filter(type, freq, q);
        const g = envGain(dest, start);
        g.gain.linearRampToValueAtTime(amp, start + 0.002);
        g.gain.exponentialRampToValueAtTime(0.0005, start + decay);
        noiseSource(start, start + decay + 0.05, nodes).connect(f);
        f.connect(g);
    }

    /** Sustained filtered noise following the note's length (sea, breath, reverse cymbal...). */
    function noiseHold(dest, start, dur, amp, type, freq, attack, release, nodes) {
        const f = filter(type, freq, 0.8);
        const g = envGain(dest, start);
        g.gain.linearRampToValueAtTime(amp, start + attack);
        g.gain.setTargetAtTime(0, start + Math.max(dur, attack), release / 3);
        noiseSource(start, start + Math.max(dur, attack) + release * 1.5, nodes).connect(f);
        f.connect(g);
    }

    function drum(dest, key, start, amp, nodes) {
        const a = amp * 0.9;
        if (key === 35 || key === 36) {
            thump(dest, start, a, 150, 45, 0.3, nodes);
        } else if (key === 38 || key === 40) {
            hiss(dest, start, a * 0.7, 0.18, 'highpass', 1200, 0.7, nodes);
            thump(dest, start, a * 0.5, 200, 120, 0.09, nodes, 'triangle');
        } else if (key === 37) {
            hiss(dest, start, a * 0.6, 0.05, 'bandpass', 2500, 2, nodes);
        } else if (key === 39) {
            hiss(dest, start, a * 0.7, 0.12, 'bandpass', 1500, 1, nodes);
        } else if (key === 41 || key === 43 || key === 45 || key === 47 || key === 48 || key === 50) {
            const f0 = 70 + (key - 41) * 14;
            thump(dest, start, a, f0, f0 * 0.6, 0.32, nodes);
        } else if (key === 42 || key === 44) {
            hiss(dest, start, a * 0.4, 0.05, 'highpass', 7000, 0.7, nodes);
        } else if (key === 46) {
            hiss(dest, start, a * 0.4, 0.3, 'highpass', 7000, 0.7, nodes);
        } else if (key === 49 || key === 52 || key === 55 || key === 57) {
            hiss(dest, start, a * 0.45, 1.2, 'highpass', 4000, 0.7, nodes);
        } else if (key === 51 || key === 53 || key === 59) {
            hiss(dest, start, a * 0.3, 0.5, 'highpass', 6000, 0.7, nodes);
        } else if (key === 54 || key === 69 || key === 70) {
            hiss(dest, start, a * 0.35, 0.08, 'highpass', 7000, 0.7, nodes);
        } else if (key === 56) {
            thump(dest, start, a * 0.4, 800, 780, 0.15, nodes, 'square');
        } else if (key >= 60 && key <= 68) {
            const f0 = 180 + (key - 60) * 40;
            thump(dest, start, a * 0.8, f0, f0 * 0.8, 0.15, nodes);
        } else if (key >= 75 && key <= 77) {
            thump(dest, start, a * 0.6, key === 75 ? 2000 : 1100, 900, 0.05, nodes);
        } else {
            hiss(dest, start, a * 0.5, 0.12, 'bandpass', 300 + key * 40, 1.5, nodes);
        }
    }

    // Instrument families by General MIDI program number.
    function instrument(p) {
        if (p < 8) return { wave: 'triangle', env: 'pluck', decay: 1.6, release: 0.15, level: 1, octave: true };
        if (p < 16) {
            const long = p === 8 || p === 9 || p === 10 || p === 11 || p === 14;
            return { wave: 'sine', env: 'pluck', decay: long ? 1.0 : 0.35, release: 0.1, level: 1, bell: long };
        }
        if (p < 24) return { wave: 'square', env: 'hold', attack: 0.01, sustain: 0.8, release: 0.08, cutoff: 3000, level: 0.5 };
        if (p < 32) return { wave: 'sawtooth', env: 'pluck', decay: 1.0, release: 0.1, cutoff: 2500, level: 0.6 };
        if (p < 40) return { wave: p < 34 ? 'triangle' : 'sawtooth', env: 'hold', attack: 0.005, sustain: 0.7, decay: 0.4, release: 0.06, cutoff: 900, level: 1.1 };
        if (p === 45) return { wave: 'triangle', env: 'pluck', decay: 0.3, release: 0.05, level: 1 };
        if (p === 46) return { wave: 'triangle', env: 'pluck', decay: 1.3, release: 0.2, level: 1, octave: true };
        if (p === 47) return { drum: 'timpani' };
        if (p === 52 || p === 53 || p === 54) return { wave: 'triangle', env: 'hold', attack: 0.08, sustain: 0.85, release: 0.25, level: 0.9, vibrato: true };
        if (p === 55) return { wave: 'sawtooth', env: 'pluck', decay: 0.35, release: 0.1, cutoff: 4000, level: 0.7 };
        if (p < 56) return { wave: 'sawtooth', env: 'hold', attack: 0.06, sustain: 0.85, release: 0.2, cutoff: 2200, level: 0.5, vibrato: true };
        if (p < 64) return { wave: 'sawtooth', env: 'hold', attack: 0.025, sustain: 0.75, release: 0.08, cutoff: 1800, level: 0.6, brass: true };
        if (p < 72) return { wave: 'square', env: 'hold', attack: 0.02, sustain: 0.8, release: 0.06, cutoff: 1800, level: 0.5, vibrato: true };
        if (p < 80) return { wave: p === 72 ? 'sine' : 'triangle', env: 'hold', attack: 0.03, sustain: 0.85, release: 0.08, level: 1, vibrato: true };
        if (p < 88) return { wave: p === 81 || p === 87 ? 'sawtooth' : 'square', env: 'hold', attack: 0.005, sustain: 0.8, release: 0.05, cutoff: 4000, level: 0.45 };
        if (p < 96) return { wave: 'sawtooth', env: 'hold', attack: 0.15, sustain: 0.8, release: 0.4, cutoff: 1500, level: 0.45 };
        if (p < 104) return { wave: 'triangle', env: p === 102 ? 'pluck' : 'hold', attack: 0.05, decay: 1.5, sustain: 0.6, release: 0.5, level: 0.6 };
        if (p < 112) return { wave: 'sawtooth', env: 'pluck', decay: 0.8, release: 0.1, cutoff: 2500, level: 0.6 };
        if (p === 112) return { wave: 'sine', env: 'pluck', decay: 0.9, release: 0.2, level: 1, bell: true };
        if (p === 113 || p === 115) return { wave: 'sine', env: 'pluck', decay: p === 113 ? 0.25 : 0.06, release: 0.03, level: 1 };
        if (p === 114) return { wave: 'triangle', env: 'pluck', decay: 0.5, release: 0.08, level: 1 };
        if (p < 120) return { drum: ['taiko', 'tom', 'synthdrum', 'revcymbal'][p - 116] };
        return { drum: ['fret', 'breath', 'sea', 'bird', 'phone', 'heli', 'applause', 'gun'][p - 120] };
    }

    function special(kind, dest, n, start, freq, amp, nodes) {
        const dur = n.d;
        switch (kind) {
            case 'timpani': thump(dest, start, amp, freq, freq * 0.97, 0.9, nodes); break;
            case 'taiko':
                thump(dest, start, amp, Math.max(60, freq), Math.max(40, freq * 0.5), 0.5, nodes);
                hiss(dest, start, amp * 0.3, 0.1, 'lowpass', 700, 0.7, nodes);
                break;
            case 'tom': thump(dest, start, amp, freq, freq * 0.7, 0.35, nodes); break;
            case 'synthdrum': thump(dest, start, amp, freq * 2, freq * 0.5, 0.3, nodes, 'triangle'); break;
            case 'revcymbal': noiseHold(dest, start, Math.max(dur, 0.3), amp * 0.5, 'highpass', 3000, Math.max(dur, 0.3), 0.05, nodes); break;
            case 'fret': hiss(dest, start, amp * 0.4, 0.1, 'bandpass', 3000, 1, nodes); break;
            case 'breath': noiseHold(dest, start, dur, amp * 0.4, 'bandpass', 1000, 0.05, 0.1, nodes); break;
            case 'sea': noiseHold(dest, start, dur, amp * 0.5, 'lowpass', 800, Math.max(0.05, dur / 2), Math.max(0.1, dur / 2), nodes); break;
            case 'applause': noiseHold(dest, start, dur, amp * 0.4, 'bandpass', 2000, 0.05, 0.3, nodes); break;
            case 'heli': noiseHold(dest, start, dur, amp * 0.5, 'lowpass', 400, 0.05, 0.2, nodes); break;
            case 'gun':
                hiss(dest, start, amp * 0.8, 0.5, 'lowpass', 2000, 0.7, nodes);
                thump(dest, start, amp * 0.6, 100, 40, 0.3, nodes);
                break;
            case 'bird': {
                const osc = ac.createOscillator();
                osc.frequency.value = freq * 2;
                const lfo = ac.createOscillator();
                lfo.frequency.value = 14;
                const depth = ac.createGain();
                depth.gain.value = freq * 0.5;
                lfo.connect(depth).connect(osc.frequency);
                const g = envGain(dest, start);
                g.gain.linearRampToValueAtTime(amp * 0.25, start + 0.02);
                g.gain.setTargetAtTime(0, start + dur, 0.03);
                osc.connect(g);
                [osc, lfo].forEach((o) => { o.start(start); o.stop(start + dur + 0.2); nodes.push(o); });
                break;
            }
            default: { // phone
                const osc = ac.createOscillator();
                osc.type = 'square';
                osc.frequency.value = freq;
                const g = envGain(dest, start);
                g.gain.linearRampToValueAtTime(amp * 0.15, start + 0.01);
                g.gain.setTargetAtTime(0, start + dur, 0.02);
                osc.connect(g);
                osc.start(start);
                osc.stop(start + dur + 0.15);
                nodes.push(osc);
            }
        }
    }

    function voice(out, n, t0, nodes) {
        const start = t0 + n.t;
        const amp = Math.pow(n.vel / 127, 1.5) * (n.vol / 127) * (n.expr / 127) * 0.32;
        if (amp < 0.0005) {
            return;
        }
        let dest = out;
        if (n.pan !== 64 && ac.createStereoPanner) {
            const panner = ac.createStereoPanner();
            panner.pan.value = Math.max(-1, Math.min(1, (n.pan - 64) / 63));
            panner.connect(out);
            dest = panner;
        }
        if (n.ch === 9) {
            drum(dest, n.key, start, amp, nodes);
            return;
        }
        const freq = 440 * Math.pow(2, (n.key - 69) / 12);
        const spec = instrument(n.prog);
        if (spec.drum) {
            special(spec.drum, dest, n, start, freq, amp, nodes);
            return;
        }

        const g = envGain(dest, start);
        let input = g;
        if (spec.cutoff) {
            const f = filter('lowpass', spec.cutoff + freq * 1.5, 0.8);
            if (spec.brass) {
                f.frequency.setValueAtTime(spec.cutoff * 0.4, start);
                f.frequency.linearRampToValueAtTime(spec.cutoff + freq * 1.5, start + 0.06);
            }
            f.connect(g);
            input = f;
        }
        const peak = amp * spec.level;
        const off = start + Math.max(n.d, 0.02);
        let end;
        if (spec.env === 'pluck') {
            g.gain.linearRampToValueAtTime(peak, start + 0.004);
            g.gain.exponentialRampToValueAtTime(0.0005, start + spec.decay);
            if (off < start + spec.decay) {
                g.gain.cancelScheduledValues(off);
                g.gain.setTargetAtTime(0, off, spec.release / 3);
                end = off + spec.release * 2;
            } else {
                end = start + spec.decay;
            }
        } else {
            const attackEnd = start + spec.attack;
            g.gain.linearRampToValueAtTime(peak, attackEnd);
            g.gain.setTargetAtTime(peak * spec.sustain, attackEnd, (spec.decay || 0.2) / 3);
            const release = Math.max(off, attackEnd + 0.001);
            g.gain.setTargetAtTime(0, release, spec.release / 3);
            end = release + spec.release * 2;
        }

        const pitched = [];
        const main = ac.createOscillator();
        main.type = spec.wave;
        main.frequency.value = freq;
        main.connect(input);
        pitched.push(main);
        if (spec.octave || spec.bell) {
            const extra = ac.createOscillator();
            extra.type = 'sine';
            extra.frequency.value = freq * (spec.bell ? 3.01 : 2);
            const level = ac.createGain();
            level.gain.value = spec.bell ? 0.25 : 0.3;
            extra.connect(level).connect(input);
            pitched.push(extra);
        }
        const all = pitched.slice();
        if (spec.vibrato && n.d > 0.25) {
            const lfo = ac.createOscillator();
            lfo.frequency.value = 5.5;
            const depth = ac.createGain();
            depth.gain.setValueAtTime(0, start);
            depth.gain.linearRampToValueAtTime(freq * 0.006, start + 0.3);
            lfo.connect(depth).connect(main.frequency);
            all.push(lfo);
        }
        for (const osc of pitched) {
            if (n.bend) {
                osc.detune.setValueAtTime(n.bend * 100, start);
            }
            if (n.bends) {
                for (const [t, semis] of n.bends) {
                    osc.detune.setValueAtTime(semis * 100, t0 + t);
                }
            }
        }
        for (const osc of all) {
            osc.start(start);
            osc.stop(end + 0.05);
            nodes.push(osc);
        }
    }

    // ---- playback API

    function load(bytes) {
        try {
            const clip = parse(new Uint8Array(bytes.buffer, bytes.byteOffset, bytes.length));
            const id = nextId++;
            clips.set(id, { clip, gain: 1, play: null });
            return id;
        } catch (e) {
            console.warn('[DR] MIDI parse failed', e);
            return -1;
        }
    }

    function silence(play, at) {
        clearTimeout(play.timer);
        if (play.out) {
            play.out.gain.cancelScheduledValues(at);
            play.out.gain.setTargetAtTime(0, at, 0.01);
            for (const node of play.nodes) {
                try {
                    node.stop(at + 0.06);
                } catch (e) {
                    // already stopped
                }
            }
            setTimeout(() => play.out.disconnect(), 200);
        }
    }

    function play(id, from, gain, onEnd) {
        const entry = clips.get(id);
        if (!entry) {
            return;
        }
        stop(id);
        entry.gain = gain;
        const clip = entry.clip;
        const remaining = Math.max(0, clip.duration - from);
        const playback = { started: performance.now() - from * 1000, out: null, nodes: [], timer: 0 };
        playback.timer = setTimeout(() => {
            if (entry.play === playback) {
                entry.play = null;
            }
            onEnd();
        }, remaining * 1000 + 20);
        entry.play = playback;
        if (ac && ac.state === 'running') {
            const out = ac.createGain();
            out.gain.value = gain;
            out.connect(master);
            playback.out = out;
            const t0 = ac.currentTime + 0.01 - from;
            for (const note of clip.notes) {
                if (note.t >= from) {
                    voice(out, note, t0, playback.nodes);
                }
            }
        }
    }

    function stop(id) {
        const entry = clips.get(id);
        if (!entry || !entry.play) {
            return 0;
        }
        const playback = entry.play;
        entry.play = null;
        silence(playback, ac ? ac.currentTime : 0);
        return Math.min((performance.now() - playback.started) / 1000, entry.clip.duration);
    }

    function setGain(id, gain) {
        const entry = clips.get(id);
        if (entry) {
            entry.gain = gain;
            if (entry.play && entry.play.out) {
                entry.play.out.gain.setTargetAtTime(gain, ac.currentTime, 0.02);
            }
        }
    }

    function stopAll() {
        for (const id of clips.keys()) {
            stop(id);
        }
    }

    return {
        unlock, load, play, stop, setGain, stopAll,
        setMuted, setHidden, isMuted: () => muted,
        duration: (id) => (clips.has(id) ? clips.get(id).clip.duration : 0),
        free: (id) => { stop(id); clips.delete(id); },
    };
})();
