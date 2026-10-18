import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Compares narrowing conversions (i2b, i2s, i2c) per method between the renamed original bytecode and the
 * recompiled source. Decompilers sometimes type an int local as byte/short; `b += 24` on such a variable
 * then wraps where the original did not. Every extra narrowing in the recompiled build is listed.
 *
 * Usage: java -cp asm.jar:asm-tree.jar Narrowing.java <original-renamed.jar> <recompiled-classes-dir>
 */
public class Narrowing {
    public static void main(String[] args) throws IOException {
        Map<String, int[]> original = count(Path.of(args[0]));
        Map<String, int[]> recompiled = count(Path.of(args[1]));
        int flagged = 0;
        for (Map.Entry<String, int[]> e : recompiled.entrySet()) {
            int[] o = original.getOrDefault(e.getKey(), new int[3]);
            int[] r = e.getValue();
            if (!original.containsKey(e.getKey())) {
                System.out.println("only in recompiled: " + e.getKey());
            }
            if (r[0] > o[0] || r[1] > o[1] || r[2] > o[2]) {
                flagged++;
                System.out.printf("%-70s i2b %d->%d  i2s %d->%d  i2c %d->%d%n", e.getKey(), o[0], r[0], o[1], r[1], o[2], r[2]);
            }
        }
        System.out.println(flagged + " methods narrow more than the original");
    }

    static Map<String, int[]> count(Path path) throws IOException {
        Map<String, int[]> result = new TreeMap<>();
        if (Files.isDirectory(path)) {
            try (Stream<Path> files = Files.walk(path)) {
                for (Path p : (Iterable<Path>) files.filter(f -> f.toString().endsWith(".class"))::iterator) {
                    scan(Files.readAllBytes(p), result);
                }
            }
        } else {
            try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(path))) {
                ZipEntry e;
                while ((e = zip.getNextEntry()) != null) {
                    if (e.getName().endsWith(".class")) {
                        scan(zip.readAllBytes(), result);
                    }
                }
            }
        }
        return result;
    }

    static void scan(byte[] bytes, Map<String, int[]> result) {
        ClassNode node = new ClassNode();
        new ClassReader(bytes).accept(node, 0);
        for (MethodNode m : node.methods) {
            int[] c = new int[3];
            for (AbstractInsnNode insn : m.instructions) {
                switch (insn.getOpcode()) {
                    case Opcodes.I2B: c[0]++; break;
                    case Opcodes.I2S: c[1]++; break;
                    case Opcodes.I2C: c[2]++; break;
                    default: break;
                }
            }
            result.put(node.name + "." + m.name + m.desc, c);
        }
    }
}
