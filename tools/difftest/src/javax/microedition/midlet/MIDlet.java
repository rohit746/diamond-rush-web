package javax.microedition.midlet;

import difftest.Trace;
import java.io.IOException;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public abstract class MIDlet {
    private static HashMap<String, String> properties;

    protected MIDlet() {
    }

    protected abstract void startApp() throws MIDletStateChangeException;

    protected abstract void pauseApp();

    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;

    public final void notifyDestroyed() {
        Trace.op(60);
        difftest.Harness.finished = true;
    }

    public final void notifyPaused() {
    }

    public final boolean platformRequest(String url) {
        Trace.text(61, url == null ? "" : url);
        return false;
    }

    /** Reads the original JAR's manifest (path in the difftest.jar system property). */
    public final String getAppProperty(String key) {
        if (properties == null) {
            properties = new HashMap<>();
            try (JarFile jar = new JarFile(System.getProperty("difftest.jar"))) {
                Manifest manifest = jar.getManifest();
                manifest.getMainAttributes().forEach((k, v) -> properties.put(k.toString(), v.toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.get(key);
    }
}
