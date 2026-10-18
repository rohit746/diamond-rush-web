package difftest;

/** FNV-1a hash of every observable platform call the game makes during one tick. */
public final class Trace {
    private static long hash = 0xcbf29ce484222325L;
    private static int ops;

    private Trace() {
    }

    private static void mix(long value) {
        for (int i = 0; i < 8; i++) {
            hash ^= value & 0xFF;
            hash *= 0x100000001b3L;
            value >>>= 8;
        }
    }

    public static synchronized void op(int code, int... args) {
        ops++;
        mix(code);
        for (int a : args) {
            mix(a);
        }
    }

    public static synchronized void text(int code, String s, int... args) {
        op(code, args);
        mix(s.length());
        for (int i = 0; i < s.length(); i++) {
            mix(s.charAt(i));
        }
    }

    public static synchronized void data(int code, int[] values, int offset, int length) {
        op(code, offset, length);
        for (int i = 0; i < length; i++) {
            mix(values[offset + i]);
        }
    }

    /** Returns "<ops> <hash>" for the tick and starts the next one. */
    static synchronized String take() {
        String result = ops + " " + Long.toHexString(hash);
        hash = 0xcbf29ce484222325L;
        ops = 0;
        return result;
    }
}
