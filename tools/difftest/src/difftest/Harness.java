package difftest;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.microedition.lcdui.Display;

/**
 * Runs the MIDlet headlessly on a virtual clock with a seeded input script and writes one line per tick:
 * "<tick> <gameState> <world> <stage> <ops> <hash>", where the hash covers every drawing/platform call made
 * during that tick. Yes/no dialogs are always answered "No" so that random input cannot quit the game.
 * Two builds that log identical lines behaved identically.
 *
 * Usage: java difftest.Harness <ticks> <seed|scenario> <out.log>   (-Ddifftest.jar=Diamondrush.jar)
 * Scenarios (anaconda, knight, yeti, ending) are described in {@link Scenario}.
 */
public final class Harness {
    public static volatile boolean finished;

    // MIDP key codes
    static final int UP = -1;
    static final int DOWN = -2;
    static final int LEFT = -3;
    static final int RIGHT = -4;
    static final int FIRE = -5;
    static final int SOFT_LEFT = -6;
    static final int SOFT_RIGHT = -7;

    private static final class Event {
        final int tick;
        final int code;
        final boolean down;

        Event(int tick, int code, boolean down) {
            this.tick = tick;
            this.code = code;
            this.down = down;
        }
    }

    private Harness() {
    }

    public static void main(String[] args) throws Exception {
        int ticks = Integer.parseInt(args[0]);
        Scenario scenario = Scenario.parse(args[1]);
        Path out = Path.of(args[2]);
        System.setProperty("microedition.platform", "NokiaE71-1/300.21.012");
        System.setProperty("microedition.locale", "en");

        List<Event> events = script(scenario, ticks);
        Object midlet = Class.forName("GloftDIRU").getDeclaredConstructor().newInstance();
        Field state = gameField("gameState", "b", byte.class);
        Field world = gameField("world", "aA", int.class);
        Field stage = gameField("stage", "aB", int.class);
        Field menu = gameField("menu", "bo", int.class);
        Field menuSelection = gameField("menuSelection", "bq", int.class);
        midlet.getClass().getMethod("startApp").invoke(midlet);
        scenario.bind();

        try (PrintWriter log = new PrintWriter(Files.newBufferedWriter(out))) {
            startWatchdog(log);
            log.println("start " + Trace.take());
            boolean running = Clock.launch();
            int next = 0;
            int answerKey = 0;
            for (int tick = 0; tick < ticks && running && !finished; tick++) {
                Object canvas = Display.currentDisplayable();
                byte gameState = state.getByte(null);
                synchronized (log) {
                    scenario.steer(tick, canvas, gameState, world.getInt(canvas), stage.getInt(canvas), log);
                }
                // Menu 7 is the No/Yes question for Exit, Restart and Main menu (main menu or pause state).
                boolean dialog = (gameState == 4 || gameState == 2 || gameState == 7) && menu.getInt(canvas) == 7;
                if (answerKey != 0) {
                    Display.key(answerKey, false);
                    answerKey = 0;
                } else if (dialog) {
                    // Always answer "No" (item 0), so random input cannot end the run.
                    answerKey = menuSelection.getInt(canvas) != 0 ? UP : FIRE;
                    Display.key(answerKey, true);
                }
                for (int[] e : scenario.input(tick, canvas, gameState, world.getInt(canvas), stage.getInt(canvas))) {
                    if (!dialog || e[1] == 0) {
                        Display.key(e[0], e[1] == 1);
                    }
                }
                while (next < events.size() && events.get(next).tick == tick) {
                    Event e = events.get(next++);
                    if (!dialog || !e.down) {
                        Display.key(e.code, e.down);
                    }
                }
                Display.paintIfRequested();
                synchronized (log) {
                    log.println(tick + " " + state.getByte(null) + " " + world.getInt(canvas) + " " + stage.getInt(canvas)
                            + " " + Trace.take());
                    progress = System.currentTimeMillis();
                    currentTick = tick;
                }
                running = Clock.step();
            }
            log.println(finished ? "end midlet-destroyed" : running ? "end ticks" : "end game-thread-stopped");
        }
        Runtime.getRuntime().halt(0);
    }

    private static volatile long progress = System.currentTimeMillis();
    private static volatile int currentTick = -1;

    /** Ends the run with "end hang" if a tick takes more than 15 seconds (an infinite loop in either build). */
    private static void startWatchdog(PrintWriter log) {
        Thread watchdog = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                if (System.currentTimeMillis() - progress > 15000) {
                    synchronized (log) {
                        log.println("end hang after tick " + currentTick);
                        log.flush();
                    }
                    Runtime.getRuntime().halt(2);
                }
            }
        }, "watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
    }

    /** A GameCanvas field by its readable name, or by its obfuscated name and type in the original JAR. */
    static Field gameField(String name, String obfuscated, Class<?> type) throws ReflectiveOperationException {
        Field field = null;
        try {
            field = Class.forName("GameCanvas").getDeclaredField(name);
        } catch (ClassNotFoundException e) {
            for (Field f : Class.forName("i").getDeclaredFields()) {
                if (f.getName().equals(obfuscated) && f.getType() == type) {   // ProGuard reused names across types
                    field = f;
                }
            }
        }
        field.setAccessible(true);
        return field;
    }

    /** A GameCanvas method by its readable name, or by its obfuscated name and signature in the original JAR. */
    static Method gameMethod(String name, String obfuscated, Class<?> returns, Class<?>... parameters)
            throws ReflectiveOperationException {
        Method method = null;
        try {
            method = Class.forName("GameCanvas").getDeclaredMethod(name, parameters);
        } catch (ClassNotFoundException e) {
            for (Method m : Class.forName("i").getDeclaredMethods()) {
                if (m.getName().equals(obfuscated) && m.getReturnType() == returns
                        && Arrays.equals(m.getParameterTypes(), parameters)) {
                    method = m;
                }
            }
        }
        method.setAccessible(true);
        return method;
    }

    /** A fixed opening into the first stage (with the cheat menu and a trip to another world), then random play. */
    private static List<Event> script(Scenario scenario, int ticks) {
        List<Event> events = new ArrayList<>();
        Random random = new Random(scenario.seed);
        int t = 260;                                   // logo, sound and sprite loading
        t = tap(events, t, FIRE) + 40;                 // title -> main menu
        t = tap(events, t, FIRE) + 260;                // Continue / New game -> stage
        if (scenario.cheats) {
            for (int code : new int[] { '#', '4', '7', '7', '2' }) {
                t = tap(events, t, code) + 2;          // open the cheat menu
            }
            t += 20;
            // unlock stages, +100 diamonds, +25 red diamonds (opens Bavaria and Siberia), weapons, stage skipping
            for (int code : new int[] { '9', '0', '6', '6', '7', '7', '7', '7', '7', '8', '8', '#' }) {
                t = tap(events, t, code) + 4;
            }
            t = tap(events, t, FIRE) + 30;             // apply and return to the stage
            if (scenario.steered()) {
                scenario.startAt(t);                   // the scenario presses keys from here on
                events.sort((a, b) -> Integer.compare(a.tick, b.tick));
                return events;
            }
            t = tap(events, t, '9') + 150;             // skip the prologue -> stage 0-0
            t = tap(events, t, FIRE) + 300;            // use the weapon
            if (scenario.world != 0) {
                t = tap(events, t, SOFT_RIGHT) + 200;  // stage -> seal
                t = tap(events, t, scenario.world == 1 ? RIGHT : DOWN) + 40;   // Bavaria or Siberia
                t = tap(events, t, FIRE) + 300;        // -> that world's map
            }
            t = tap(events, t, FIRE) + 300;            // enter the selected stage (Angkor: use the weapon)
        }
        int[] keys = scenario.keys;
        while (t < ticks && keys.length > 0) {
            int code = keys[random.nextInt(keys.length)];
            int hold = 1 + random.nextInt(code < -4 || code > 0 ? 3 : 14);
            events.add(new Event(t, code, true));
            events.add(new Event(t + hold, code, false));
            t += hold + random.nextInt(8);
        }
        events.sort((a, b) -> Integer.compare(a.tick, b.tick));
        return events;
    }

    private static int tap(List<Event> events, int tick, int code) {
        events.add(new Event(tick, code, true));
        events.add(new Event(tick + 2, code, false));
        return tick + 2;
    }
}
