package javax.microedition.media;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.control.VolumeControl;

/** Silent players; the harness never starts the sound thread, so only creation is observable. */
public final class Manager {
    private Manager() {
    }

    public static Player createPlayer(InputStream stream, String type) throws IOException, MediaException {
        int size = 0;
        while (stream.read() >= 0) {
            size++;
        }
        difftest.Trace.op(70, size);
        return new Silent();
    }

    private static final class Silent implements Player, VolumeControl {
        private int state = UNREALIZED;
        private int level = 100;

        public void realize() { state = REALIZED; }
        public void prefetch() { state = PREFETCHED; }
        public void start() { state = STARTED; }
        public void stop() { state = PREFETCHED; }
        public void deallocate() { state = REALIZED; }
        public void close() { state = CLOSED; }
        public long setMediaTime(long now) { return now; }
        public long getMediaTime() { return 0; }
        public int getState() { return state; }
        public long getDuration() { return 0; }
        public String getContentType() { return "audio/midi"; }
        public void setLoopCount(int count) { }
        public void addPlayerListener(PlayerListener l) { }
        public void removePlayerListener(PlayerListener l) { }
        public Control getControl(String type) { return this; }
        public Control[] getControls() { return new Control[] { this }; }
        public void setMute(boolean mute) { }
        public boolean isMuted() { return false; }
        public int setLevel(int level) { this.level = level; return level; }
        public int getLevel() { return level; }
    }
}
