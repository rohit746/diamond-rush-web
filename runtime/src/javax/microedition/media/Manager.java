package javax.microedition.media;

import dr.web.MidiPlayer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Manager {
    public static final String TONE_DEVICE_LOCATOR = "device://tone";
    public static final String MIDI_DEVICE_LOCATOR = "device://midi";

    private Manager() {
    }

    public static String[] getSupportedContentTypes(String protocol) {
        return new String[] { "audio/midi", "audio/mid", "audio/x-midi" };
    }

    public static String[] getSupportedProtocols(String contentType) {
        return new String[0];
    }

    public static Player createPlayer(InputStream stream, String type) throws IOException, MediaException {
        if (stream == null) {
            throw new IllegalArgumentException();
        }
        if (type != null && !type.toLowerCase().contains("mid")) {
            throw new MediaException("Unsupported content type " + type);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while ((n = stream.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }
        return new MidiPlayer(out.toByteArray());
    }

    public static Player createPlayer(String locator) throws IOException, MediaException {
        throw new MediaException("Unsupported locator " + locator);
    }

    public static void playTone(int note, int duration, int volume) throws MediaException {
    }
}
