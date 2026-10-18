package dr.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/** JAR resources, served to the game in place of Class.getResourceAsStream (see tools/src/Rewrite.java). */
public final class Res {
    private static final HashMap<String, byte[]> FILES = new HashMap<>();

    private Res() {
    }

    /** Unpacks every non-class entry of the original MIDlet JAR. */
    public static void loadJar(byte[] jar) throws IOException {
        try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(jar))) {
            byte[] buffer = new byte[16384];
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();
                if (entry.isDirectory() || name.endsWith(".class")) {
                    continue;
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int n;
                while ((n = zip.read(buffer)) > 0) {
                    out.write(buffer, 0, n);
                }
                FILES.put(name, out.toByteArray());
            }
        }
    }

    public static byte[] get(String name) {
        return FILES.get(name.startsWith("/") ? name.substring(1) : name);
    }

    public static boolean has(String name) {
        return get(name) != null;
    }

    public static Iterable<String> names() {
        return FILES.keySet();
    }

    /** Replacement for Class.getResourceAsStream; every game class lives in the default package. */
    public static InputStream open(Class<?> cls, String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        byte[] data = get(name);
        return data == null ? null : new ByteArrayInputStream(data);
    }
}
