package javax.microedition.midlet;

import dr.web.Host;
import dr.web.Res;
import java.util.HashMap;

public abstract class MIDlet {
    private static HashMap<String, String> properties;

    protected MIDlet() {
    }

    protected abstract void startApp() throws MIDletStateChangeException;

    protected abstract void pauseApp();

    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;

    public final void notifyDestroyed() {
        Host.exited("The game has exited.");
    }

    public final void notifyPaused() {
    }

    public final void resumeRequest() {
    }

    public final boolean platformRequest(String url) {
        Host.openUrl(url);
        return false;
    }

    public final int checkPermission(String permission) {
        return 1;
    }

    /** Reads attributes from the JAR manifest (a phone would also merge in the JAD file). */
    public final String getAppProperty(String key) {
        if (properties == null) {
            properties = new HashMap<>();
            byte[] manifest = Res.get("META-INF/MANIFEST.MF");
            if (manifest != null) {
                String lastKey = null;
                for (String line : new String(manifest, 0, manifest.length).split("\n")) {
                    if (line.endsWith("\r")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    if (line.startsWith(" ") && lastKey != null) {
                        properties.put(lastKey, properties.get(lastKey) + line.substring(1));
                        continue;
                    }
                    int colon = line.indexOf(':');
                    if (colon > 0) {
                        lastKey = line.substring(0, colon).trim();
                        properties.put(lastKey, line.substring(colon + 1).trim());
                    }
                }
            }
        }
        return properties.get(key);
    }
}
