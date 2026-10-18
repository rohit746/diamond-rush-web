package javax.microedition.lcdui;

import dr.web.Host;
import java.util.ArrayList;
import javax.microedition.midlet.MIDlet;

/**
 * The phone screen, plus the MIDP event thread.
 *
 * TeaVM runs Java threads as cooperative green threads on the single browser thread. Browser callbacks
 * (keys, audio timers) therefore only enqueue work; a dedicated Java thread drains the queue once per
 * animation frame and is the only thread that calls back into the game (paint, key handlers, listeners).
 * Because green threads switch only at blocking points, paint always sees a frame-consistent game state.
 */
public class Display {
    /** Diamond Rush's Nokia build targets a 320x240 landscape (QVGA E-series) screen. */
    static final int WIDTH = 320;
    static final int HEIGHT = 240;

    private static Display instance;
    static Displayable current;
    private static Graphics screenGraphics;
    private static boolean repaintPending;

    private static final ArrayList<Runnable> EVENTS = new ArrayList<>();

    private Display() {
    }

    /** Creates the on-page screen and starts the event thread. Called once by the launcher. */
    public static void start() {
        Surface screen = new Surface(Host.createScreen(WIDTH, HEIGHT), WIDTH, HEIGHT);
        screenGraphics = new Graphics(screen);
        Host.setKeyHandler((keyCode, down) -> post(() -> dispatchKey(keyCode, down)));
        Thread thread = new Thread(Display::eventLoop, "MIDP-Event");
        thread.start();
    }

    /** Queues work for the event thread; safe to call from browser callbacks. */
    public static void post(Runnable event) {
        EVENTS.add(event);
    }

    public static Display getDisplay(MIDlet midlet) {
        if (instance == null) {
            instance = new Display();
        }
        return instance;
    }

    public void setCurrent(Displayable next) {
        if (next == null || next == current) {
            return;
        }
        current = next;
        if (next instanceof Canvas) {
            ((Canvas) next).showNotify();
        }
        repaintPending = true;
    }

    public Displayable getCurrent() {
        return current;
    }

    public boolean isColor() {
        return true;
    }

    public int numColors() {
        return 1 << 16;
    }

    public int numAlphaLevels() {
        return 256;
    }

    public boolean vibrate(int duration) {
        Host.vibrate(duration);
        return true;
    }

    public boolean flashBacklight(int duration) {
        return false;
    }

    public void callSerially(Runnable runnable) {
        post(runnable);
    }

    static void requestRepaint(Canvas canvas) {
        if (canvas == current) {
            repaintPending = true;
        }
    }

    static void serviceRepaints(Canvas canvas) {
        if (repaintPending && canvas == current) {
            paintCurrent();
        }
    }

    private static void paintCurrent() {
        repaintPending = false;
        if (current instanceof Canvas) {
            screenGraphics.reset();
            try {
                ((Canvas) current).paint(screenGraphics);
            } catch (Throwable e) {
                Host.log("paint failed: " + e);
                e.printStackTrace();
            }
        }
    }

    private static void eventLoop() {
        while (true) {
            Host.nextFrame();
            if (!EVENTS.isEmpty()) {
                Runnable[] batch = EVENTS.toArray(new Runnable[0]);
                EVENTS.clear();
                for (Runnable event : batch) {
                    try {
                        event.run();
                    } catch (Throwable e) {
                        Host.log("event failed: " + e);
                        e.printStackTrace();
                    }
                }
            }
            if (repaintPending) {
                paintCurrent();
            }
        }
    }

    private static void dispatchKey(int keyCode, boolean down) {
        if (current instanceof Canvas) {
            Canvas canvas = (Canvas) current;
            if (down) {
                canvas.keyPressed(keyCode);
            } else {
                canvas.keyReleased(keyCode);
            }
        }
    }
}
