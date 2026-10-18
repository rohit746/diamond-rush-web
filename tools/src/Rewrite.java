import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 * Prepares the MIDlet's bytecode for TeaVM.
 *
 * 1. ProGuard overloaded field names by type (e.g. class i has fields "a" of
 *    type String, byte, Image, ...). The JVM keys fields by name+descriptor,
 *    TeaVM keys them by name only, so every game field is renamed to
 *    name$descriptor.
 * 2. Calls whose TeaVM classlib behaviour differs from a phone are redirected
 *    into the web runtime (dr.web.Res / dr.web.Sys).
 *
 * Usage: java -cp asm.jar:asm-commons.jar Rewrite.java <game.jar> <outDir>
 */
public class Rewrite {
    static final class ClassInfo {
        String superName;
        String[] interfaces;
        final Set<String> fields = new HashSet<>();
    }

    static final Map<String, ClassInfo> classes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path jar = Path.of(args[0]);
        Path out = Path.of(args[1]);
        Map<String, byte[]> classFiles = new HashMap<>();
        try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(jar))) {
            ZipEntry e;
            while ((e = zip.getNextEntry()) != null) {
                if (!e.isDirectory() && e.getName().endsWith(".class")) {
                    classFiles.put(e.getName(), zip.readAllBytes());
                }
            }
        }

        for (byte[] bytes : classFiles.values()) {
            new ClassReader(bytes).accept(new ClassVisitor(Opcodes.ASM9) {
                ClassInfo info;

                @Override
                public void visit(int version, int access, String name, String signature, String superName,
                        String[] interfaces) {
                    info = new ClassInfo();
                    info.superName = superName;
                    info.interfaces = interfaces;
                    classes.put(name, info);
                }

                @Override
                public FieldVisitor visitField(int access, String name, String descriptor, String signature,
                        Object value) {
                    info.fields.add(name + ' ' + descriptor);
                    return null;
                }
            }, ClassReader.SKIP_CODE);
        }

        Remapper remapper = new Remapper() {
            @Override
            public String mapFieldName(String owner, String name, String descriptor) {
                return declares(owner, name, descriptor) ? name + '$' + mangle(descriptor) : name;
            }
        };

        int redirects = 0;
        for (Map.Entry<String, byte[]> entry : classFiles.entrySet()) {
            ClassReader reader = new ClassReader(entry.getValue());
            ClassWriter writer = new ClassWriter(0);
            Redirector redirector = new Redirector(writer);
            reader.accept(new ClassRemapper(redirector, remapper), 0);
            redirects += redirector.count;
            Path target = out.resolve(entry.getKey());
            Files.createDirectories(target.getParent());
            Files.write(target, writer.toByteArray());
        }
        System.out.println("Rewrote " + classFiles.size() + " classes, " + redirects + " redirected calls");
    }

    static boolean declares(String owner, String name, String descriptor) {
        List<String> queue = new ArrayList<>();
        queue.add(owner);
        while (!queue.isEmpty()) {
            ClassInfo info = classes.get(queue.remove(queue.size() - 1));
            if (info == null) {
                continue;
            }
            if (info.fields.contains(name + ' ' + descriptor)) {
                return true;
            }
            if (info.superName != null) {
                queue.add(info.superName);
            }
            if (info.interfaces != null) {
                queue.addAll(List.of(info.interfaces));
            }
        }
        return false;
    }

    static String mangle(String descriptor) {
        StringBuilder sb = new StringBuilder();
        for (char c : descriptor.toCharArray()) {
            switch (c) {
                case '[': sb.append('A'); break;
                case '/': sb.append('_'); break;
                case ';': break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }

    static final class Redirector extends ClassVisitor {
        int count;

        Redirector(ClassVisitor next) {
            super(Opcodes.ASM9, next);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                String[] exceptions) {
            return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
                        boolean isInterface) {
                    if (owner.equals("java/lang/Class") && name.equals("getResourceAsStream")) {
                        count++;
                        super.visitMethodInsn(Opcodes.INVOKESTATIC, "dr/web/Res", "open",
                                "(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;", false);
                    } else if (owner.equals("java/lang/System") && (name.equals("getProperty") || name.equals("exit"))) {
                        count++;
                        super.visitMethodInsn(Opcodes.INVOKESTATIC, "dr/web/Sys", name, descriptor, false);
                    } else {
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                }
            };
        }
    }
}
