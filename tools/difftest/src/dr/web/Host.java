package dr.web;

import java.util.TreeMap;

/** Just the storage part of the browser bridge, in memory, so the runtime's RecordStore can be reused. */
public final class Host {
    private static final TreeMap<String, String> STORE = new TreeMap<>();

    private Host() {
    }

    public static String storeGet(String key) {
        return STORE.get(key);
    }

    public static void storeSet(String key, String value) {
        STORE.put(key, value);
    }

    public static void storeRemove(String key) {
        STORE.remove(key);
    }

    public static String storeKeys(String prefix) {
        StringBuilder sb = new StringBuilder();
        for (String key : STORE.keySet()) {
            if (key.startsWith(prefix)) {
                sb.append(sb.length() == 0 ? "" : "\n").append(key);
            }
        }
        return sb.toString();
    }
}
