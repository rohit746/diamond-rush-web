import dr.web.Host;
import dr.web.Res;
import javax.microedition.lcdui.Display;

/**
 * Browser entry point. Lives in the default package because the game's MIDlet class (GloftDIRU) does,
 * and Java cannot reference default-package classes from a named package.
 */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        try {
            Res.loadJar(Host.jarBytes().copyToJavaArray());
            Display.start();
            GloftDIRU midlet = new GloftDIRU();
            midlet.startApp();
        } catch (Throwable e) {
            e.printStackTrace();
            Host.fatal("The game failed to start: " + e);
        }
    }
}
