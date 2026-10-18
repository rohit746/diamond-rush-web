package javax.microedition.lcdui;

import difftest.Trace;
import javax.microedition.midlet.MIDlet;

/** The harness drives this display: delivers keys and paints the current canvas once per tick. */
public class Display {
    static final int WIDTH = 320;
    static final int HEIGHT = 240;
    static final int SCREEN = 1;

    private static Display instance;
    static Displayable current;
    static boolean repaintPending;
    private static final Graphics SCREEN_GRAPHICS = new Graphics(SCREEN, WIDTH, HEIGHT);

    public static Display getDisplay(MIDlet midlet) {
        if (instance == null) {
            instance = new Display();
        }
        return instance;
    }

    public void setCurrent(Displayable next) {
        if (next != null && next != current) {
            current = next;
            if (next instanceof Canvas) {
                ((Canvas) next).showNotify();
            }
            repaintPending = true;
        }
    }

    public Displayable getCurrent() {
        return current;
    }

    public boolean vibrate(int duration) {
        Trace.op(50, duration);
        return true;
    }

    public boolean isColor() {
        return true;
    }

    public int numColors() {
        return 1 << 16;
    }

    public static Object currentDisplayable() {
        return current;
    }

    public static void key(int code, boolean down) {
        if (current instanceof Canvas) {
            if (down) {
                ((Canvas) current).keyPressed(code);
            } else {
                ((Canvas) current).keyReleased(code);
            }
        }
    }

    public static void paintIfRequested() {
        if (repaintPending && current instanceof Canvas) {
            repaintPending = false;
            SCREEN_GRAPHICS.reset();
            ((Canvas) current).paint(SCREEN_GRAPHICS);
        }
    }
}
