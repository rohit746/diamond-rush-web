import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 * Renames the obfuscated classes, fields and methods of Diamondrush.jar using a mapping file.
 *
 * Mapping lines (keys use the original obfuscated names and descriptors):
 *   class  i                 GameCanvas
 *   field  i   a   I         gameState
 *   method i   a   (I)V      setGameState
 * Text after '#' is a comment.
 *
 * Members without a mapping still get an unambiguous name derived from their type (int_aZ,
 * imageArr2_b, a_bool(...)), because ProGuard reused the same short name for unrelated members.
 * Methods that override platform methods (paint, run, startApp, ...) keep their names.
 *
 * Usage: java -cp asm.jar:asm-commons.jar Deobfuscate.java <game.jar> <mapping> <out.jar> [report.md]
 */
public class Deobfuscate {
    static final class ClassInfo {
        String name;
        String superName;
        String[] interfaces;
        final Map<String, Integer> fields = new LinkedHashMap<>();   // "name desc" -> access
        final Map<String, Integer> methods = new LinkedHashMap<>();  // "name desc" -> access
    }

    static final Map<String, ClassInfo> classes = new TreeMap<>();
    static final Map<String, String> classMap = new HashMap<>();
    static final Map<String, String> fieldMap = new HashMap<>();   // "owner name desc" -> new name
    static final Map<String, String> methodMap = new HashMap<>();  // "owner name desc" -> new name
    static final Set<String> explicit = new HashSet<>();

    public static void main(String[] args) throws IOException {
        Path jar = Path.of(args[0]);
        Path mapping = Path.of(args[1]);
        Path out = Path.of(args[2]);

        Map<String, byte[]> classFiles = new TreeMap<>();
        try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(jar))) {
            ZipEntry e;
            while ((e = zip.getNextEntry()) != null) {
                if (!e.isDirectory() && e.getName().endsWith(".class")) {
                    classFiles.put(e.getName(), zip.readAllBytes());
                }
            }
        }
        for (byte[] bytes : classFiles.values()) {
            scan(bytes);
        }

        List<String> errors = new ArrayList<>();
        readMapping(mapping, errors);
        assignDefaults();
        checkCollisions(errors);
        if (!errors.isEmpty()) {
            errors.forEach(System.err::println);
            System.exit(1);
        }

        Remapper remapper = new Remapper() {
            @Override
            public String map(String internalName) {
                return classMap.getOrDefault(internalName, internalName);
            }

            @Override
            public String mapFieldName(String owner, String name, String descriptor) {
                String declaring = resolve(owner, name + ' ' + descriptor, true);
                return declaring == null ? name : fieldMap.get(declaring + ' ' + name + ' ' + descriptor);
            }

            @Override
            public String mapMethodName(String owner, String name, String descriptor) {
                String declaring = resolve(owner, name + ' ' + descriptor, false);
                if (declaring == null) {
                    return name;
                }
                return methodMap.getOrDefault(declaring + ' ' + name + ' ' + descriptor, name);
            }
        };

        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(out))) {
            for (byte[] bytes : classFiles.values()) {
                ClassReader reader = new ClassReader(bytes);
                ClassWriter writer = new ClassWriter(0);
                reader.accept(new ClassRemapper(writer, remapper), 0);
                zip.putNextEntry(new ZipEntry(remapper.map(reader.getClassName()) + ".class"));
                zip.write(writer.toByteArray());
                zip.closeEntry();
            }
        }

        int total = 0;
        int named = 0;
        for (ClassInfo info : classes.values()) {
            for (String member : info.fields.keySet()) {
                total++;
                named += explicit.contains("F " + info.name + ' ' + member) ? 1 : 0;
            }
            for (String member : info.methods.keySet()) {
                if (isRenamable(member.substring(0, member.indexOf(' ')))) {
                    total++;
                    named += explicit.contains("M " + info.name + ' ' + member) ? 1 : 0;
                }
            }
        }
        System.out.printf("Deobfuscated %d classes; %d of %d members have mapped names%n", classes.size(), named, total);
        if (args.length > 3) {
            writeReport(Path.of(args[3]));
        }
    }

    static void scan(byte[] bytes) {
        new ClassReader(bytes).accept(new ClassVisitor(Opcodes.ASM9) {
            ClassInfo info;

            @Override
            public void visit(int version, int access, String name, String signature, String superName,
                    String[] interfaces) {
                info = new ClassInfo();
                info.name = name;
                info.superName = superName;
                info.interfaces = interfaces;
                classes.put(name, info);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                info.fields.put(name + ' ' + descriptor, access);
                return null;
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                    String[] exceptions) {
                info.methods.put(name + ' ' + descriptor, access);
                return null;
            }
        }, ClassReader.SKIP_CODE);
    }

    /** ProGuard names are one or two ASCII letters; anything longer overrides a platform method. */
    static boolean isRenamable(String name) {
        return name.length() <= 2 && name.chars().allMatch(ch -> (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
    }

    static void readMapping(Path mapping, List<String> errors) throws IOException {
        int lineNumber = 0;
        for (String line : Files.readAllLines(mapping)) {
            lineNumber++;
            int hash = line.indexOf('#');
            String[] parts = (hash >= 0 ? line.substring(0, hash) : line).trim().split("\\s+");
            if (parts.length == 1 && parts[0].isEmpty()) {
                continue;
            }
            String where = mapping.getFileName() + ":" + lineNumber + ": ";
            switch (parts[0]) {
                case "class":
                    if (parts.length != 3 || !classes.containsKey(parts[1])) {
                        errors.add(where + "unknown class mapping: " + line);
                    } else if (classMap.containsKey(parts[1])) {
                        errors.add(where + "class mapped twice: " + line);
                    } else {
                        classMap.put(parts[1], parts[2]);
                    }
                    break;
                case "field":
                case "method": {
                    boolean field = parts[0].equals("field");
                    ClassInfo info = parts.length == 5 ? classes.get(parts[1]) : null;
                    String member = parts.length == 5 ? parts[2] + ' ' + parts[3] : null;
                    if (info == null || !(field ? info.fields : info.methods).containsKey(member)) {
                        errors.add(where + "no such " + parts[0] + ": " + line);
                    } else if (!field && !isRenamable(parts[2])) {
                        errors.add(where + "platform method cannot be renamed: " + line);
                    } else if (!explicit.add((field ? "F " : "M ") + parts[1] + ' ' + member)) {
                        errors.add(where + "mapped twice: " + line);
                    } else {
                        (field ? fieldMap : methodMap).put(parts[1] + ' ' + member, parts[4]);
                    }
                    break;
                }
                default:
                    errors.add(where + "unrecognised line: " + line);
            }
        }
    }

    static void assignDefaults() {
        for (ClassInfo info : classes.values()) {
            for (String member : info.fields.keySet()) {
                String key = info.name + ' ' + member;
                if (!fieldMap.containsKey(key)) {
                    String[] p = member.split(" ");
                    fieldMap.put(key, typeName(Type.getType(p[1])) + '_' + p[0]);
                }
            }
            for (String member : info.methods.keySet()) {
                String key = info.name + ' ' + member;
                String[] p = member.split(" ");
                if (!methodMap.containsKey(key) && isRenamable(p[0])) {
                    methodMap.put(key, p[0] + '_' + typeName(Type.getReturnType(p[1])));
                }
            }
        }
    }

    static String typeName(Type type) {
        switch (type.getSort()) {
            case Type.VOID: return "void";
            case Type.BOOLEAN: return "bool";
            case Type.ARRAY: {
                String element = typeName(type.getElementType());
                return element + "Arr" + (type.getDimensions() > 1 ? type.getDimensions() : "");
            }
            case Type.OBJECT: {
                String internal = classMap.getOrDefault(type.getInternalName(), type.getInternalName());
                String simple = internal.substring(internal.lastIndexOf('/') + 1);
                return Character.toLowerCase(simple.charAt(0)) + simple.substring(1);
            }
            default: return type.getClassName();
        }
    }

    /** Finds the game class that declares a member referenced through {@code owner}, or null. */
    static String resolve(String owner, String member, boolean field) {
        List<String> queue = new ArrayList<>();
        queue.add(owner);
        while (!queue.isEmpty()) {
            ClassInfo info = classes.get(queue.remove(0));
            if (info == null) {
                continue;
            }
            if ((field ? info.fields : info.methods).containsKey(member)) {
                return info.name;
            }
            if (info.superName != null) {
                queue.add(info.superName);
            }
            if (info.interfaces != null) {
                queue.addAll(List.of(info.interfaces));
            }
        }
        return null;
    }

    static void checkCollisions(List<String> errors) {
        Set<String> classNames = new HashSet<>();
        for (ClassInfo info : classes.values()) {
            String mapped = classMap.getOrDefault(info.name, info.name);
            if (!classNames.add(mapped)) {
                errors.add("two classes map to " + mapped);
            }
            Set<String> fieldNames = new HashSet<>();
            for (String member : info.fields.keySet()) {
                String name = fieldMap.get(info.name + ' ' + member);
                if (!fieldNames.add(name)) {
                    errors.add("duplicate field name " + mapped + "." + name);
                }
            }
            Set<String> signatures = new HashSet<>();
            for (String member : info.methods.keySet()) {
                String[] p = member.split(" ");
                String name = methodMap.getOrDefault(info.name + ' ' + member, p[0]);
                // Java source cannot overload on return type alone, so compare parameter lists only.
                String params = p[1].substring(0, p[1].indexOf(')') + 1);
                if (!signatures.add(name + params)) {
                    errors.add("duplicate method signature " + mapped + "." + name + params);
                }
            }
        }
    }

    static void writeReport(Path report) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# Diamond Rush symbol map\n\n")
          .append("Generated by `tools/src/Deobfuscate.java` from `mappings/diamondrush.map`. ")
          .append("Names like `int_aZ` or `a_bool` are type-derived placeholders for members not yet identified.\n");
        for (ClassInfo info : classes.values()) {
            String mapped = classMap.getOrDefault(info.name, info.name);
            sb.append("\n## ").append(mapped).append(" (`").append(info.name).append("`)\n\n");
            sb.append("| Kind | Obfuscated | Descriptor | Name |\n| --- | --- | --- | --- |\n");
            for (String member : info.fields.keySet()) {
                String[] p = member.split(" ");
                sb.append("| field | `").append(p[0]).append("` | `").append(p[1]).append("` | ")
                  .append(fieldMap.get(info.name + ' ' + member)).append(" |\n");
            }
            for (String member : info.methods.keySet()) {
                String[] p = member.split(" ");
                if (!isRenamable(p[0])) {
                    continue;
                }
                sb.append("| method | `").append(p[0]).append("` | `").append(p[1]).append("` | ")
                  .append(methodMap.get(info.name + ' ' + member)).append(" |\n");
            }
        }
        Files.writeString(report, sb.toString());
    }
}
