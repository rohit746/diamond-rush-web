package difftest;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Virtual time and lock-step scheduling. Prep.java redirects the game's System.currentTimeMillis,
 * Thread.sleep and Thread.start here, so a run depends only on its input script.
 *
 * Only the first thread the game starts (its main loop) runs; every Thread.sleep in it advances virtual
 * time and hands control back to the harness for one tick. Other threads (sound, platform requests) are
 * never started.
 */
public final class Clock {
    private static long now = 1_000_000_000L;
    private static Thread gameThread;
    private static final Semaphore GAME_TURN = new Semaphore(0);
    private static final Semaphore HARNESS_TURN = new Semaphore(0);

    private Clock() {
    }

    public static long now() {
        return now;
    }

    public static void sleep(long millis) throws InterruptedException {
        if (millis > 0) {
            now += millis;
        }
        if (Thread.currentThread() == gameThread) {
            HARNESS_TURN.release();
            GAME_TURN.acquire();
        }
    }

    public static void start(Thread thread) {
        if (gameThread == null) {
            gameThread = thread;
        }
    }

    /** Starts the game loop and waits for its first sleep. Returns false if it never got there. */
    static boolean launch() throws InterruptedException {
        if (gameThread == null) {
            return false;
        }
        gameThread.setDaemon(true);
        gameThread.start();
        return awaitGame();
    }

    /** Lets the game loop run until its next sleep. Returns false once it has ended or hangs. */
    static boolean step() throws InterruptedException {
        GAME_TURN.release();
        return awaitGame();
    }

    private static boolean awaitGame() throws InterruptedException {
        while (!HARNESS_TURN.tryAcquire(200, TimeUnit.MILLISECONDS)) {
            if (!gameThread.isAlive()) {
                return false;
            }
        }
        return true;
    }
}
