/*
 * Browser host for the Diamond Rush MIDlet.
 *
 * classes.js (TeaVM output: the original game bytecode plus the MIDP runtime in runtime/src) calls into the
 * global DRHost object defined here for everything platform-specific: canvas drawing, the phone screen,
 * keyboard/touch input and localStorage-backed record stores. Audio comes from the MIDI synthesizer in
 * synth.js (window.DRSynth).
 */
'use strict';

(function () {
    // ------------------------------------------------------------------ utilities

    const $ = (id) => document.getElementById(id);
    const colorCache = new Map();

    function css(rgb) {
        let value = colorCache.get(rgb);
        if (value === undefined) {
            value = '#' + (rgb | 0x1000000).toString(16).slice(1);
            colorCache.set(rgb, value);
        }
        return value;
    }

    function context(canvas) {
        const ctx = canvas.getContext('2d');
        ctx.imageSmoothingEnabled = false;
        return ctx;
    }

    function createCanvas(w, h) {
        const canvas = document.createElement('canvas');
        canvas.width = w;
        canvas.height = h;
        return canvas;
    }

    // drawRegion transforms as canvas matrices [a, b, c, d, e(w,h), f(w,h)] mapping source to destination.
    const TRANSFORMS = [
        (w, h) => [1, 0, 0, 1, 0, 0],      // NONE
        (w, h) => [1, 0, 0, -1, 0, h],     // MIRROR_ROT180
        (w, h) => [-1, 0, 0, 1, w, 0],     // MIRROR
        (w, h) => [-1, 0, 0, -1, w, h],    // ROT180
        (w, h) => [0, 1, 1, 0, 0, 0],      // MIRROR_ROT270
        (w, h) => [0, 1, -1, 0, h, 0],     // ROT90
        (w, h) => [0, -1, 1, 0, 0, w],     // ROT270
        (w, h) => [0, -1, -1, 0, h, w],    // MIRROR_ROT90
    ];

    const measureCtx = createCanvas(1, 1).getContext('2d');
    let measureFont = '';

    function roundRectPath(ctx, x, y, w, h, rx, ry) {
        rx = Math.max(0, Math.min(rx, w / 2));
        ry = Math.max(0, Math.min(ry, h / 2));
        ctx.beginPath();
        ctx.moveTo(x + rx, y);
        ctx.lineTo(x + w - rx, y);
        ctx.ellipse(x + w - rx, y + ry, rx, ry, 0, -Math.PI / 2, 0);
        ctx.lineTo(x + w, y + h - ry);
        ctx.ellipse(x + w - rx, y + h - ry, rx, ry, 0, 0, Math.PI / 2);
        ctx.lineTo(x + rx, y + h);
        ctx.ellipse(x + rx, y + h - ry, rx, ry, 0, Math.PI / 2, Math.PI);
        ctx.lineTo(x, y + ry);
        ctx.ellipse(x + rx, y + ry, rx, ry, 0, Math.PI, Math.PI * 1.5);
        ctx.closePath();
    }

    // ------------------------------------------------------------------ storage

    const memoryStore = new Map();

    function storage() {
        try {
            const s = window.localStorage;
            s.getItem('x');
            return s;
        } catch (e) {
            return null;
        }
    }

    const Synth = window.DRSynth;

    // ------------------------------------------------------------------ input

    // MIDP key codes as reported by Nokia S60 phones.
    const KEY = { UP: -1, DOWN: -2, LEFT: -3, RIGHT: -4, FIRE: -5, SOFT_LEFT: -6, SOFT_RIGHT: -7 };
    const CODE_MAP = {
        ArrowUp: KEY.UP, ArrowDown: KEY.DOWN, ArrowLeft: KEY.LEFT, ArrowRight: KEY.RIGHT,
        KeyW: KEY.UP, KeyS: KEY.DOWN, KeyA: KEY.LEFT, KeyD: KEY.RIGHT,
        Enter: KEY.FIRE, NumpadEnter: KEY.FIRE, Space: KEY.FIRE,
        KeyQ: KEY.SOFT_LEFT, KeyP: KEY.SOFT_LEFT, F1: KEY.SOFT_LEFT,
        KeyE: KEY.SOFT_RIGHT, Escape: KEY.SOFT_RIGHT, Backspace: KEY.SOFT_RIGHT, F2: KEY.SOFT_RIGHT,
        NumpadMultiply: 42,
    };
    for (let i = 0; i <= 9; i++) {
        CODE_MAP['Digit' + i] = 48 + i;
        CODE_MAP['Numpad' + i] = 48 + i;
    }
    const MIN_HOLD_MS = 70;

    let keyHandler = null;
    const held = new Map(); // MIDP key code -> { since, sources }

    function press(code, source) {
        Synth.unlock();
        let state = held.get(code);
        if (!state) {
            state = { since: performance.now(), sources: new Set(), timer: 0 };
            held.set(code, state);
            if (keyHandler) {
                keyHandler(code, true);
            }
        }
        clearTimeout(state.timer);
        state.sources.add(source);
    }

    function release(code, source) {
        const state = held.get(code);
        if (!state) {
            return;
        }
        state.sources.delete(source);
        if (state.sources.size) {
            return;
        }
        // The game samples keys about 20 times a second; make quick taps last long enough to be seen.
        const wait = Math.max(0, MIN_HOLD_MS - (performance.now() - state.since));
        state.timer = setTimeout(() => {
            if (!state.sources.size && held.get(code) === state) {
                held.delete(code);
                if (keyHandler) {
                    keyHandler(code, false);
                }
            }
        }, wait);
    }

    function releaseAll() {
        for (const [code, state] of held) {
            state.sources.clear();
            release(code, null);
        }
    }

    function midpKey(e) {
        if (e.key === '*') return 42;
        if (e.key === '#') return 35;
        const code = CODE_MAP[e.code];
        return code === undefined ? null : code;
    }

    function installKeyboard() {
        window.addEventListener('keydown', (e) => {
            if (e.ctrlKey || e.metaKey || e.altKey) {
                return;
            }
            if (e.code === 'KeyM') {
                toggleMute();
                return;
            }
            if (e.code === 'KeyF') {
                toggleFullscreen();
                return;
            }
            const code = midpKey(e);
            if (code === null) {
                return;
            }
            e.preventDefault();
            if (!e.repeat && started) {
                press(code, 'kb:' + e.code);
            }
        });
        window.addEventListener('keyup', (e) => {
            const code = midpKey(e);
            if (code !== null) {
                e.preventDefault();
                release(code, 'kb:' + e.code);
            }
        });
        window.addEventListener('blur', releaseAll);
        document.addEventListener('visibilitychange', () => {
            Synth.setHidden(document.hidden);
            if (document.hidden) {
                releaseAll();
            }
        });
    }

    function installTouchPad() {
        document.querySelectorAll('[data-key]').forEach((button) => {
            const code = parseInt(button.dataset.key, 10);
            const source = 'pad:' + button.dataset.key;
            const down = (e) => {
                e.preventDefault();
                button.setPointerCapture?.(e.pointerId);
                button.classList.add('down');
                press(code, source);
            };
            const up = (e) => {
                e.preventDefault();
                button.classList.remove('down');
                release(code, source);
            };
            button.addEventListener('pointerdown', down);
            button.addEventListener('pointerup', up);
            button.addEventListener('pointercancel', up);
            button.addEventListener('lostpointercapture', up);
            button.addEventListener('contextmenu', (e) => e.preventDefault());
        });
    }

    // ------------------------------------------------------------------ page chrome

    let started = false;
    let screenCanvas = null;

    function layout() {
        if (!screenCanvas) {
            return;
        }
        const stage = $('stage');
        const availW = stage.clientWidth;
        const availH = stage.clientHeight;
        let scale = Math.min(availW / screenCanvas.width, availH / screenCanvas.height);
        if (scale >= 2) {
            scale = Math.floor(scale); // keep pixels crisp
        }
        scale = Math.max(scale, 0.5);
        $('device').style.width = Math.round(screenCanvas.width * scale) + 'px';
        $('device').style.height = Math.round(screenCanvas.height * scale) + 'px';
    }

    function toggleMute() {
        Synth.setMuted(!Synth.isMuted());
        const button = $('mute');
        button.setAttribute('aria-pressed', String(Synth.isMuted()));
        button.textContent = Synth.isMuted() ? 'Sound off' : 'Sound on';
    }

    function toggleFullscreen() {
        if (document.fullscreenElement) {
            document.exitFullscreen?.();
        } else {
            document.documentElement.requestFullscreen?.().catch(() => {});
        }
    }

    function showMessage(title, text, actionLabel) {
        const overlay = $('overlay');
        overlay.hidden = false;
        $('overlay-title').textContent = title;
        $('overlay-text').textContent = text;
        const action = $('overlay-action');
        action.hidden = !actionLabel;
        action.textContent = actionLabel || '';
        return action;
    }

    async function boot() {
        installKeyboard();
        installTouchPad();
        window.addEventListener('resize', layout);
        $('mute').addEventListener('click', toggleMute);
        $('fullscreen').addEventListener('click', toggleFullscreen);
        $('pad-toggle').addEventListener('click', () => {
            document.body.classList.toggle('show-pad');
            layout();
        });

        showMessage('Diamond Rush', 'Loading game data…');
        let jar;
        try {
            const response = await fetch('Diamondrush.jar');
            if (!response.ok) {
                throw new Error('HTTP ' + response.status);
            }
            jar = new Int8Array(await response.arrayBuffer());
        } catch (e) {
            showMessage('Could not load the game', 'Diamondrush.jar failed to load (' + e.message + '). ' +
                'Serve this folder over HTTP, for example with ./serve.sh, rather than opening the file directly.');
            return;
        }
        DRHost._jar = jar;

        const action = showMessage('Diamond Rush', 'Click, tap or press any key to start.', 'Play');
        const begin = (e) => {
            if (started) {
                return;
            }
            if (e && e.type === 'keydown') {
                if (e.ctrlKey || e.metaKey || e.altKey) {
                    return;
                }
                // The key that starts the game is not passed on to it.
                e.preventDefault();
                e.stopImmediatePropagation();
            }
            started = true;
            Synth.unlock();
            $('overlay').hidden = true;
            window.removeEventListener('keydown', begin, true);
            $('overlay').removeEventListener('pointerdown', begin);
            window.main([]);
        };
        action.addEventListener('click', begin);
        $('overlay').addEventListener('pointerdown', begin);
        window.addEventListener('keydown', begin, true);
        action.focus();
    }

    // ------------------------------------------------------------------ the DRHost API used by classes.js

    window.DRHost = {
        boot,
        _jar: null,

        jarBytes: () => DRHost._jar,
        languages() {
            const list = [...(navigator.languages || [navigator.language || 'en'])];
            const forced = new URLSearchParams(location.search).get('lang');
            if (forced) {
                list.unshift(forced);
            }
            return list.join(',');
        },
        log: (msg) => console.log('[DR]', msg),
        fatal(msg) {
            console.error('[DR]', msg);
            showMessage('Something went wrong', msg);
        },
        exited(reason) {
            Synth.stopAll();
            showMessage('Diamond Rush', reason + ' Reload the page to play again.', 'Reload')
                .addEventListener('click', () => location.reload());
        },
        openUrl: (url) => console.info('[DR] platformRequest ignored:', url),
        vibrate(ms) {
            try {
                navigator.vibrate?.(ms);
            } catch (e) {
                // unsupported
            }
        },
        requestFrame(cb) {
            let done = false;
            const fire = () => {
                if (!done) {
                    done = true;
                    cb();
                }
            };
            requestAnimationFrame(fire);
            setTimeout(fire, 100); // keeps events flowing while the tab is hidden
        },

        // screen and input
        createScreen(w, h) {
            screenCanvas = $('screen');
            screenCanvas.width = w;
            screenCanvas.height = h;
            layout();
            return screenCanvas;
        },
        setKeyHandler(cb) {
            keyHandler = cb;
        },

        // drawing
        createCanvas,
        context,
        setColor(ctx, rgb) {
            const value = css(rgb);
            ctx.fillStyle = value;
            ctx.strokeStyle = value;
        },
        clip(ctx, x, y, w, h) {
            ctx.save();
            ctx.beginPath();
            ctx.rect(x, y, w, h);
            ctx.clip();
        },
        blitT(ctx, src, sx, sy, w, h, t, dx, dy) {
            const m = TRANSFORMS[t](w, h);
            ctx.setTransform(m[0], m[1], m[2], m[3], dx + m[4], dy + m[5]);
            ctx.drawImage(src, sx, sy, w, h, 0, 0, w, h);
            ctx.setTransform(1, 0, 0, 1, 0, 0);
        },
        line(ctx, x1, y1, x2, y2) {
            const dx = Math.abs(x2 - x1);
            const dy = -Math.abs(y2 - y1);
            const sx = x1 < x2 ? 1 : -1;
            const sy = y1 < y2 ? 1 : -1;
            let err = dx + dy;
            for (;;) {
                ctx.fillRect(x1, y1, 1, 1);
                if (x1 === x2 && y1 === y2) {
                    break;
                }
                const e2 = 2 * err;
                if (e2 >= dy) {
                    err += dy;
                    x1 += sx;
                }
                if (e2 <= dx) {
                    err += dx;
                    y1 += sy;
                }
            }
        },
        roundRect(ctx, x, y, w, h, aw, ah, fill) {
            if (fill) {
                roundRectPath(ctx, x, y, w, h, aw / 2, ah / 2);
                ctx.fill();
            } else {
                roundRectPath(ctx, x + 0.5, y + 0.5, w, h, aw / 2, ah / 2);
                ctx.lineWidth = 1;
                ctx.stroke();
            }
        },
        arc(ctx, x, y, w, h, start, arc, fill) {
            const a0 = -start * Math.PI / 180;
            const a1 = -(start + arc) * Math.PI / 180;
            ctx.beginPath();
            if (fill) {
                ctx.moveTo(x + w / 2, y + h / 2);
                ctx.ellipse(x + w / 2, y + h / 2, w / 2, h / 2, 0, a0, a1, arc > 0);
                ctx.closePath();
                ctx.fill();
            } else {
                ctx.ellipse(x + w / 2 + 0.5, y + h / 2 + 0.5, w / 2, h / 2, 0, a0, a1, arc > 0);
                ctx.lineWidth = 1;
                ctx.stroke();
            }
        },
        triangle(ctx, x1, y1, x2, y2, x3, y3) {
            ctx.beginPath();
            ctx.moveTo(x1, y1);
            ctx.lineTo(x2, y2);
            ctx.lineTo(x3, y3);
            ctx.closePath();
            ctx.fill();
        },
        text(ctx, text, x, baseline, font) {
            if (ctx.font !== font) {
                ctx.font = font;
            }
            ctx.textBaseline = 'alphabetic';
            ctx.textAlign = 'left';
            ctx.fillText(text, x, baseline);
        },
        measure(font, text) {
            if (measureFont !== font) {
                measureCtx.font = font;
                measureFont = font;
            }
            return Math.round(measureCtx.measureText(text).width);
        },
        rgbImage(argb, offset, scan, w, h, alpha) {
            const canvas = createCanvas(w, h);
            const ctx = context(canvas);
            const image = ctx.createImageData(w, h);
            const out = new Uint32Array(image.data.buffer);
            let i = 0;
            for (let y = 0; y < h; y++) {
                let p = offset + y * scan;
                for (let x = 0; x < w; x++, p++) {
                    const v = argb[p];
                    const a = alpha ? v >>> 24 : 255;
                    out[i++] = (a << 24) | ((v & 0xff) << 16) | (v & 0xff00) | ((v >> 16) & 0xff);
                }
            }
            ctx.putImageData(image, 0, 0);
            return canvas;
        },
        getRGB(canvas, out, offset, scan, x, y, w, h) {
            const data = new Uint32Array(context(canvas).getImageData(x, y, w, h).data.buffer);
            let i = 0;
            for (let row = 0; row < h; row++) {
                let p = offset + row * scan;
                for (let col = 0; col < w; col++, p++) {
                    const v = data[i++];
                    out[p] = (v & 0xff000000) | ((v & 0xff) << 16) | (v & 0xff00) | ((v >> 16) & 0xff);
                }
            }
        },

        // record stores
        storeGet(key) {
            const s = storage();
            const value = s ? s.getItem(key) : memoryStore.get(key);
            return value === undefined ? null : value;
        },
        storeSet(key, value) {
            const s = storage();
            try {
                if (s) {
                    s.setItem(key, value);
                    return;
                }
            } catch (e) {
                // quota exceeded or blocked: fall back to memory
            }
            memoryStore.set(key, value);
        },
        storeRemove(key) {
            const s = storage();
            if (s) {
                s.removeItem(key);
            }
            memoryStore.delete(key);
        },
        storeKeys(prefix) {
            const keys = new Set([...memoryStore.keys()].filter((k) => k.startsWith(prefix)));
            const s = storage();
            if (s) {
                for (let i = 0; i < s.length; i++) {
                    const key = s.key(i);
                    if (key.startsWith(prefix)) {
                        keys.add(key);
                    }
                }
            }
            return [...keys].join('\n');
        },

        // audio
        midiLoad: (bytes) => Synth.load(bytes),
        midiDuration: (id) => Synth.duration(id),
        midiPlay: (id, from, gain, onEnd) => Synth.play(id, from, gain, () => onEnd()),
        midiStop: (id) => Synth.stop(id),
        midiGain: (id, gain) => Synth.setGain(id, gain),
        midiFree: (id) => Synth.free(id),
    };
})();
