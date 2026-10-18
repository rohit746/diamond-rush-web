package difftest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/** MIDP semantics for Class.getResourceAsStream: always the MIDlet JAR, whatever the class. */
public final class Res {
    private static JarFile jar;

    private Res() {
    }

    public static InputStream open(Class<?> cls, String name) {
        try {
            if (jar == null) {
                jar = new JarFile(System.getProperty("difftest.jar"));
            }
            ZipEntry entry = jar.getEntry(name.startsWith("/") ? name.substring(1) : name);
            if (entry == null || entry.isDirectory()) {
                return null;
            }
            try (InputStream in = jar.getInputStream(entry)) {
                return new ByteArrayInputStream(in.readAllBytes());
            }
        } catch (IOException e) {
            return null;
        }
    }
}
