package difftest;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * What a run plays. A number is a seeded random run (see Harness.script). A name picks a scenario that
 * reaches what random play does not:
 *
 *   anaconda, knight, yeti   On the world map of Angkor Wat, Bavaria or Siberia, the cursor is moved to
 *                            the boss stage (8, 9 or 10), which is then played with random input.
 *   ending                   In the Angkor boss stage, the Bavaria and Siberia crystals are marked as found,
 *                            the Angkor crystal is put under the player and the pickup animation is started
 *                            (as an item chest does). The game collects the crystal, shows the seal, the
 *                            ending and the credits.
 *
 * After the scripted opening (cheat menu: unlock everything, enable stage skipping), a scenario presses keys
 * by looking at the game state: skip stages with 9, confirm tallies, go to the seal and pick the world, enter
 * the boss stage from the world map. It also writes a few game fields between two ticks. Both builds see the
 * same state and get the same writes at the same tick; each write is logged as a "#" line, so the logs
 * still compare line for line.
 */
final class Scenario {
    private static final int[] PLAIN_KEYS = {
        Harness.UP, Harness.DOWN, Harness.LEFT, Harness.RIGHT, Harness.LEFT, Harness.RIGHT, Harness.UP,
        Harness.FIRE, '5', '*'
    };
    // Seeds >= 2 also skip stages (9), go back from the map to the seal (right soft key) and browse menus;
    // the pause key is left out so runs rarely quit through Exit.
    private static final int[] CHEAT_KEYS = {
        Harness.UP, Harness.DOWN, Harness.LEFT, Harness.RIGHT, Harness.LEFT, Harness.RIGHT, Harness.FIRE,
        Harness.FIRE, Harness.FIRE, '5', '*', '9', Harness.SOFT_RIGHT, '2', '8'
    };
    // Boss fights: move and use the weapon, never skip the stage or leave it.
    private static final int[] BOSS_KEYS = {
        Harness.UP, Harness.DOWN, Harness.LEFT, Harness.RIGHT, Harness.LEFT, Harness.RIGHT, Harness.UP,
        Harness.FIRE, Harness.FIRE, '5'
    };

    final long seed;
    /** Open the cheat menu, unlock everything and travel to {@link #world}. */
    final boolean cheats;
    final int world;
    /** Random keys after the opening. */
    final int[] keys;
    private final int bossStage;    // -1: no steering
    private final boolean ending;

    private Field mapNodes, mapCursorX, mapCursorY, mapTargetX, mapTargetY;
    private Field playerX, playerY, moveProgress, saveData, objectMap, objectState, objectTimers, tileRedrawTimers,
            sealLocation;
    private Method setPlayerAnim;
    private int ticksInBossStage;
    private boolean crystalPlaced;
    private Random random;
    private int start = Integer.MAX_VALUE;     // first tick of reactive input
    private int held;                          // key being held, 0 if none
    private int releaseTick;
    private int nextPress;

    private Scenario(long seed, boolean cheats, int world, int[] keys, int bossStage, boolean ending) {
        this.seed = seed;
        this.cheats = cheats;
        this.world = world;
        this.keys = keys;
        this.bossStage = bossStage;
        this.ending = ending;
    }

    static Scenario parse(String arg) {
        switch (arg) {
            case "anaconda":
                return new Scenario(101, true, 0, BOSS_KEYS, 8, false);
            case "knight":
                return new Scenario(102, true, 1, BOSS_KEYS, 9, false);
            case "yeti":
                return new Scenario(103, true, 2, BOSS_KEYS, 10, false);
            case "ending":
                return new Scenario(104, true, 0, new int[0], 8, true);
            default:
                long seed = Long.parseLong(arg);
                return new Scenario(seed, seed >= 2, (int) (seed % 3), seed >= 2 ? CHEAT_KEYS : PLAIN_KEYS, -1, false);
        }
    }

    /** True if the scenario presses keys itself after the scripted opening. */
    boolean steered() {
        return bossStage >= 0;
    }

    /** Reactive input starts at this tick. */
    void startAt(int tick) {
        start = tick;
        random = new Random(seed);
    }

    /** Resolves the fields it reads and writes; call once the game classes are loaded. */
    void bind() throws ReflectiveOperationException {
        if (bossStage < 0) {
            return;
        }
        mapNodes = Harness.gameField("mapNodes", "a", long[][].class);
        mapCursorX = Harness.gameField("mapCursorX", "dV", int.class);
        mapCursorY = Harness.gameField("mapCursorY", "dW", int.class);
        mapTargetX = Harness.gameField("mapTargetX", "dX", int.class);
        mapTargetY = Harness.gameField("mapTargetY", "dY", int.class);
        playerX = Harness.gameField("playerX", "h", int.class);
        playerY = Harness.gameField("playerY", "i", int.class);
        moveProgress = Harness.gameField("moveProgress", "j", int.class);
        saveData = Harness.gameField("saveData", "i", byte[].class);
        objectMap = Harness.gameField("objectMap", "a", byte[][].class);
        objectState = Harness.gameField("objectState", "b", int[][].class);
        objectTimers = Harness.gameField("objectTimers", "b", byte[][].class);
        tileRedrawTimers = Harness.gameField("tileRedrawTimers", "c", byte[][].class);
        sealLocation = Harness.gameField("sealLocation", "p", int.class);
        setPlayerAnim = Harness.gameMethod("setPlayerAnim", "g", void.class, int.class);
    }

    /** Key events for this tick, as {code, 1 down / 0 up}. Call after {@link #steer}. */
    List<int[]> input(int tick, Object canvas, int gameState, int world, int stage) throws ReflectiveOperationException {
        List<int[]> events = new ArrayList<>();
        if (held != 0 && tick >= releaseTick) {
            events.add(new int[] { held, 0 });
            held = 0;
        }
        if (held != 0 || tick < start || tick < nextPress) {
            return events;
        }
        int key = 0;
        int hold = 2;
        int gap = 40;
        if (gameState == 1 && world == this.world && stage == bossStage) {
            if (!ending) {                      // fight: random moves and weapon use
                key = keys[random.nextInt(keys.length)];
                hold = 1 + random.nextInt(key < -4 || key > 0 ? 3 : 14);
                gap = random.nextInt(8);
            }
        } else if (ending && crystalPlaced) {
            // the crystal is collected; the seal, the ending and the credits play without input
        } else if (gameState == 1) {
            key = '9';                          // skip the stage
            gap = 150;
        } else if (gameState == 17 || gameState == 20 || gameState == 4 || gameState == 12) {
            key = Harness.FIRE;                 // tally, secret path message, main menu (Continue), game over
        } else if (gameState == 15) {
            key = world == this.world ? Harness.FIRE : Harness.SOFT_RIGHT;   // enter the stage, or go to the seal
            gap = 100;
        } else if (gameState == 27) {
            int location = sealLocation.getInt(canvas);
            key = location == this.world ? Harness.FIRE : sealKeyToward(location, this.world);
        }
        if (key != 0) {
            events.add(new int[] { key, 1 });
            held = key;
            releaseTick = tick + hold;
            nextPress = tick + hold + gap;
        }
        return events;
    }

    /** Called before each tick's input. */
    void steer(int tick, Object canvas, int gameState, int world, int stage, PrintWriter log)
            throws ReflectiveOperationException {
        if (bossStage < 0 || world != this.world) {
            return;
        }
        if (gameState == 15) {
            moveMapCursorToBoss(tick, canvas, log);
        }
        if (gameState == 1 && stage == bossStage) {
            ticksInBossStage++;
            if (ending && !crystalPlaced && ticksInBossStage >= 40 && moveProgress.getInt(canvas) <= 0) {
                placeCrystal(tick, canvas, log);
            }
        }
    }

    /** Puts the world map cursor on the boss stage while it is at rest; the scripted FIRE then enters it. */
    private void moveMapCursorToBoss(int tick, Object canvas, PrintWriter log) throws ReflectiveOperationException {
        long[][] nodes = (long[][]) mapNodes.get(canvas);
        int x = mapCursorX.getInt(canvas);
        int y = mapCursorY.getInt(canvas);
        if (nodes == null || x < 0 || x != mapTargetX.getInt(canvas) || y != mapTargetY.getInt(canvas)) {
            return;
        }
        for (int nx = 0; nx < nodes.length; nx++) {
            for (int ny = 0; ny < nodes[nx].length; ny++) {
                long node = nodes[nx][ny];
                // bits 6-10: stage index (empty cells are 0; the boss stage index is never 0)
                if (node != 0 && (node >>> 6 & 31) == bossStage && (nx != x || ny != y)) {
                    mapCursorX.setInt(canvas, nx);
                    mapCursorY.setInt(canvas, ny);
                    mapTargetX.setInt(canvas, nx);
                    mapTargetY.setInt(canvas, ny);
                    log.println("# " + tick + " map cursor moved to the boss stage " + world + "-" + bossStage);
                    return;
                }
            }
        }
    }

    // Constants.sealNavigation[direction][location] -> new location (-1: none). Locations 0-2 are the worlds,
    // 3 the shop; directions are up, right, down, left.
    private static final int[][] SEAL_NAVIGATION = { { -1, -1, 0, -1 }, { 1, -1, 3, -1 }, { 2, 2, -1, -1 }, { -1, 0, -1, 2 } };
    private static final int[] DIRECTION_KEYS = { Harness.UP, Harness.RIGHT, Harness.DOWN, Harness.LEFT };

    /** The key for the first step of the shortest path between two seal locations. */
    private static int sealKeyToward(int from, int to) {
        int[] firstKey = new int[4];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        boolean[] seen = new boolean[4];
        seen[from] = true;
        queue.add(from);
        while (!queue.isEmpty()) {
            int at = queue.poll();
            for (int d = 0; d < 4; d++) {
                int next = SEAL_NAVIGATION[d][at];
                if (next >= 0 && !seen[next]) {
                    seen[next] = true;
                    firstKey[next] = at == from ? DIRECTION_KEYS[d] : firstKey[at];
                    if (next == to) {
                        return firstKey[next];
                    }
                    queue.add(next);
                }
            }
        }
        return 0;
    }

    private void placeCrystal(int tick, Object canvas, PrintWriter log) throws ReflectiveOperationException {
        int x = playerX.getInt(canvas);
        int y = playerY.getInt(canvas);
        byte[] save = (byte[]) saveData.get(canvas);
        save[2] |= 6;                                          // saveData[2] bits 1, 2: Bavaria and Siberia crystals
        ((byte[][]) objectMap.get(canvas))[x][y] = 53;         // the Angkor crystal
        ((int[][]) objectState.get(canvas))[x][y] = 0;
        ((byte[][]) objectTimers.get(canvas))[x][y] = 0;
        ((byte[][]) tileRedrawTimers.get(canvas))[x][y] = 24;  // tiles are only updated while this is positive
        setPlayerAnim.invoke(canvas, 40);                       // the pickup animation applies the item at frame 13
        crystalPlaced = true;
        log.println("# " + tick + " Bavaria and Siberia crystals marked found, Angkor crystal picked up at " + x + "," + y);
    }
}
