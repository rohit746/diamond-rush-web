import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Prepares game classes (a JAR or a directory of .class files) for the differential test by routing time,
 * sleeping and thread starts into difftest.Clock, and resource loading into difftest.Res (MIDP semantics).
 * Everything else is copied unchanged.
 *
 * Usage: java -cp asm.jar Prep.java <game.jar|classes-dir> <out-dir>
 */
public class Prep {
    public static void main(String[] args) throws IOException {
        Path in = Path.of(args[0]);
        Path out = Path.of(args[1]);
        Map<String, byte[]> classes = new TreeMap<>();
        if (Files.isDirectory(in)) {
            try (Stream<Path> files = Files.walk(in)) {
                for (Path p : (Iterable<Path>) files.filter(f -> f.toString().endsWith(".class"))::iterator) {
                    classes.put(in.relativize(p).toString(), Files.readAllBytes(p));
                }
            }
        } else {
            try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(in))) {
                ZipEntry e;
                while ((e = zip.getNextEntry()) != null) {
                    if (e.getName().endsWith(".class")) {
                        classes.put(e.getName(), zip.readAllBytes());
                    }
                }
            }
        }
        for (Map.Entry<String, byte[]> entry : classes.entrySet()) {
            ClassWriter writer = new ClassWriter(0);
            new ClassReader(entry.getValue()).accept(new ClassVisitor(Opcodes.ASM9, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                        String[] exceptions) {
                    return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            if (owner.equals("java/lang/System") && name.equals("currentTimeMillis")) {
                                super.visitMethodInsn(Opcodes.INVOKESTATIC, "difftest/Clock", "now", "()J", false);
                            } else if (owner.equals("java/lang/Class") && name.equals("getResourceAsStream")) {
                                super.visitMethodInsn(Opcodes.INVOKESTATIC, "difftest/Res", "open",
                                        "(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;", false);
                            } else if (owner.equals("java/lang/Thread") && name.equals("sleep") && desc.equals("(J)V")) {
                                super.visitMethodInsn(Opcodes.INVOKESTATIC, "difftest/Clock", "sleep", "(J)V", false);
                            } else if (opcode == Opcodes.INVOKEVIRTUAL && name.equals("start") && desc.equals("()V")
                                    && !owner.startsWith("javax/")) {
                                super.visitMethodInsn(Opcodes.INVOKESTATIC, "difftest/Clock", "start",
                                        "(Ljava/lang/Thread;)V", false);
                            } else {
                                super.visitMethodInsn(opcode, owner, name, desc, itf);
                            }
                        }
                    };
                }
            }, 0);
            Path target = out.resolve(entry.getKey());
            Files.createDirectories(target.getParent() == null ? out : target.getParent());
            Files.write(target, writer.toByteArray());
        }
        System.out.println("Prepared " + classes.size() + " classes");
    }
}
