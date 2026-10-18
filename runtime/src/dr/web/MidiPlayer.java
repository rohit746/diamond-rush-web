package dr.web;

import java.util.ArrayList;
import javax.microedition.lcdui.Display;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import org.teavm.jso.typedarrays.Int8Array;

/** JSR-135 Player for MIDI data, rendered by the Web Audio synthesizer in web/host.js. */
public final class MidiPlayer implements Player, VolumeControl {
    private final byte[] data;
    private final ArrayList<PlayerListener> listeners = new ArrayList<>();
    private int state = UNREALIZED;
    private int id = -1;
    private double position;
    private int loopCount = 1;
    private int loopsLeft;
    private int playToken;
    private int level = 100;
    private boolean muted;

    public MidiPlayer(byte[] data) {
        this.data = data;
    }

    @Override
    public void realize() throws MediaException {
        checkOpen();
        if (state == UNREALIZED) {
            id = Host.midiLoad(Int8Array.copyFromJavaArray(data));
            if (id < 0) {
                throw new MediaException("Unreadable MIDI data");
            }
            state = REALIZED;
        }
    }

    @Override
    public void prefetch() throws MediaException {
        realize();
        if (state == REALIZED) {
            state = PREFETCHED;
        }
    }

    @Override
    public void start() throws MediaException {
        prefetch();
        if (state == STARTED) {
            return;
        }
        if (position >= Host.midiDuration(id)) {
            position = 0;
        }
        loopsLeft = loopCount;
        play();
        state = STARTED;
    }

    private void play() {
        int token = ++playToken;
        Host.midiPlay(id, position, gain(), () -> Display.post(() -> ended(token)));
    }

    private void ended(int token) {
        if (token != playToken || state != STARTED) {
            return;
        }
        position = 0;
        if (loopCount == -1 || --loopsLeft > 0) {
            play();
            return;
        }
        state = PREFETCHED;
        position = Host.midiDuration(id);
        fire(PlayerListener.END_OF_MEDIA, Long.valueOf(getDuration()));
    }

    @Override
    public void stop() {
        if (state == STARTED) {
            playToken++;
            position = Host.midiStop(id);
            state = PREFETCHED;
        }
    }

    @Override
    public void deallocate() {
        if (state == CLOSED) {
            return;
        }
        stop();
        // Releasing the device also rewinds: the game restarts music with prefetch() + start().
        position = 0;
        if (state == PREFETCHED) {
            state = REALIZED;
        }
    }

    @Override
    public void close() {
        if (state == CLOSED) {
            return;
        }
        stop();
        if (id >= 0) {
            Host.midiFree(id);
        }
        state = CLOSED;
        fire(PlayerListener.CLOSED, null);
    }

    @Override
    public long setMediaTime(long now) throws MediaException {
        checkOpen();
        if (state == UNREALIZED) {
            throw new IllegalStateException();
        }
        boolean playing = state == STARTED;
        stop();
        position = Math.max(0, Math.min(now / 1e6, Host.midiDuration(id)));
        if (playing) {
            play();
            state = STARTED;
        }
        return (long) (position * 1e6);
    }

    @Override
    public long getMediaTime() {
        return (long) (position * 1e6);
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public long getDuration() {
        return id < 0 ? TIME_UNKNOWN : (long) (Host.midiDuration(id) * 1e6);
    }

    @Override
    public String getContentType() {
        return "audio/midi";
    }

    @Override
    public void setLoopCount(int count) {
        if (count == 0) {
            throw new IllegalArgumentException();
        }
        loopCount = count;
    }

    @Override
    public void addPlayerListener(PlayerListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removePlayerListener(PlayerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Control getControl(String controlType) {
        if (controlType.equals("VolumeControl") || controlType.equals("javax.microedition.media.control.VolumeControl")) {
            return this;
        }
        return null;
    }

    @Override
    public Control[] getControls() {
        return new Control[] { this };
    }

    @Override
    public void setMute(boolean mute) {
        muted = mute;
        updateGain();
    }

    @Override
    public boolean isMuted() {
        return muted;
    }

    @Override
    public int setLevel(int level) {
        this.level = Math.max(0, Math.min(100, level));
        updateGain();
        return this.level;
    }

    @Override
    public int getLevel() {
        return level;
    }

    private double gain() {
        return muted ? 0 : level / 100.0;
    }

    private void updateGain() {
        if (id >= 0) {
            Host.midiGain(id, gain());
        }
    }

    private void checkOpen() {
        if (state == CLOSED) {
            throw new IllegalStateException("Player is closed");
        }
    }

    private void fire(String event, Object data) {
        for (PlayerListener listener : new ArrayList<>(listeners)) {
            listener.playerUpdate(this, event, data);
        }
    }
}
