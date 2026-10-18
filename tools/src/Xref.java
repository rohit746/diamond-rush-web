import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Cross-reference report for reverse engineering: for every method of the selected classes, lists its size,
 * callers, callees, fields read/written and string constants; for every field, its readers and writers.
 *
 * Usage: java -cp asm.jar:asm-tree.jar Xref.java <classes.jar> <out.txt> [ClassName ...]
 */
public class Xref {
    public static void main(String[] args) throws IOException {
        List<ClassNode> nodes = new ArrayList<>();
        try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(Path.of(args[0])))) {
            ZipEntry e;
            while ((e = zip.getNextEntry()) != null) {
                if (e.getName().endsWith(".class")) {
                    ClassNode node = new ClassNode();
                    new ClassReader(zip.readAllBytes()).accept(node, 0);
                    nodes.add(node);
                }
            }
        }
        Set<String> selected = new TreeSet<>(List.of(args).subList(2, args.length));
        Map<String, Set<String>> callers = new TreeMap<>();
        Map<String, Set<String>> readers = new TreeMap<>();
        Map<String, Set<String>> writers = new TreeMap<>();
        for (ClassNode c : nodes) {
            for (MethodNode m : c.methods) {
                String self = c.name + "." + m.name + shortDesc(m.desc);
                for (AbstractInsnNode insn : m.instructions) {
                    if (insn instanceof MethodInsnNode) {
                        MethodInsnNode mi = (MethodInsnNode) insn;
                        callers.computeIfAbsent(mi.owner + "." + mi.name + shortDesc(mi.desc), k -> new TreeSet<>()).add(self);
                    } else if (insn instanceof FieldInsnNode) {
                        FieldInsnNode fi = (FieldInsnNode) insn;
                        boolean write = fi.getOpcode() == Opcodes.PUTFIELD || fi.getOpcode() == Opcodes.PUTSTATIC;
                        (write ? writers : readers).computeIfAbsent(fi.owner + "." + fi.name, k -> new TreeSet<>()).add(self);
                    }
                }
            }
        }

        StringBuilder out = new StringBuilder();
        for (ClassNode c : nodes) {
            if (!selected.isEmpty() && !selected.contains(c.name)) {
                continue;
            }
            out.append("######## class ").append(c.name).append('\n');
            for (MethodNode m : c.methods) {
                String self = c.name + "." + m.name + shortDesc(m.desc);
                Set<String> callees = new TreeSet<>();
                Set<String> reads = new TreeSet<>();
                Set<String> writes = new TreeSet<>();
                Set<String> strings = new TreeSet<>();
                for (AbstractInsnNode insn : m.instructions) {
                    if (insn instanceof MethodInsnNode) {
                        MethodInsnNode mi = (MethodInsnNode) insn;
                        callees.add(simple(mi.owner) + "." + mi.name);
                    } else if (insn instanceof FieldInsnNode) {
                        FieldInsnNode fi = (FieldInsnNode) insn;
                        String name = (fi.owner.equals(c.name) ? "" : simple(fi.owner) + ".") + fi.name;
                        boolean write = fi.getOpcode() == Opcodes.PUTFIELD || fi.getOpcode() == Opcodes.PUTSTATIC;
                        (write ? writes : reads).add(name);
                    } else if (insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof String) {
                        strings.add('"' + ((String) ((LdcInsnNode) insn).cst).replace("\n", "\\n") + '"');
                    }
                }
                out.append("\n== ").append(m.name).append(shortDesc(m.desc)).append("  [").append(m.instructions.size())
                   .append(" insns]\n");
                append(out, "called by", strip(callers.getOrDefault(self, Set.of()), c.name));
                append(out, "calls", callees);
                append(out, "writes", writes);
                append(out, "reads", reads);
                append(out, "strings", strings);
            }
            out.append("\n######## fields of ").append(c.name).append('\n');
            for (var f : c.fields) {
                String key = c.name + "." + f.name;
                out.append(String.format("%-28s %-36s R:%-3d W:%-3d  writers: %s%n", f.name, f.desc,
                        readers.getOrDefault(key, Set.of()).size(), writers.getOrDefault(key, Set.of()).size(),
                        strip(writers.getOrDefault(key, Set.of()), c.name)));
            }
        }
        Files.writeString(Path.of(args[1]), out.toString());
    }

    static void append(StringBuilder out, String label, Set<String> items) {
        if (!items.isEmpty()) {
            out.append("   ").append(label).append(": ").append(String.join(", ", items)).append('\n');
        }
    }

    static Set<String> strip(Set<String> names, String owner) {
        Set<String> result = new TreeSet<>();
        for (String n : names) {
            result.add(n.startsWith(owner + ".") ? n.substring(owner.length() + 1) : n);
        }
        return result;
    }

    static String simple(String internal) {
        return internal.substring(internal.lastIndexOf('/') + 1);
    }

    /** "(Ljavax/microedition/lcdui/Graphics;II)V" -> "(Graphics,int,int)" */
    static String shortDesc(String desc) {
        StringBuilder sb = new StringBuilder("(");
        org.objectweb.asm.Type[] args = org.objectweb.asm.Type.getArgumentTypes(desc);
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            String name = args[i].getClassName();
            sb.append(name.substring(name.lastIndexOf('.') + 1));
        }
        return sb.append(')').toString();
    }
}
