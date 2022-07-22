package eu.qped.java.utils.compiler;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import javax.tools.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class Com {
    private static final List<String> DEFAULT_OPTIONS = List.of("-verbose", "-Xlint", "-g");
    private static final String DEFAULT_CLASS_NAME = "TestClass";
    private static final String SUFFIX = ".java";

    public class Created {
        public final File file;
        public final String className;
        public final boolean isTrue;

        public Created(File file, String className, boolean isTrue) {
            this.file = file;
            this.className = className;
            this.isTrue = isTrue;
        }
    }


    private List<Diagnostic<? extends JavaFileObject>> protocol = new LinkedList<>();

    public boolean compileSource(File source) {
        return compileSource(source, null);
    }

    public List<Diagnostic<? extends JavaFileObject>> protocol() {
        return protocol;
    }

    public boolean compileSource(File source, List<String> options) {

        LinkedList<File> stack = new LinkedList<>();
        LinkedList<File> java = new LinkedList<>();
        stack.add(source);
        while (! stack.isEmpty()) {
            File first = stack.removeFirst();
            if (first.isDirectory()) {
                stack.addAll(0, Arrays.asList(first.listFiles()));
            } else if (Pattern.matches(".*\\.java", first.getName())) {
                java.add(first);
            }
        }

        if (java.isEmpty()) {
            return false;
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(collector, Locale.GERMANY, Charset.defaultCharset());
        try {
            fileManager.setLocation(StandardLocation.CLASS_PATH,
                    Arrays.asList(
                            source,
                            Path.of("lib/junit-jupiter-api-5.8.2.jar").toFile()
                    ));
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                    Arrays.asList(
                            source
                    ));
        }catch (Exception e) {
            e.printStackTrace();
        }


        if (Objects.isNull(options))
            options = DEFAULT_OPTIONS;

        JavaCompiler.CompilationTask task = compiler.getTask(
                new StringWriter(),
                fileManager,
                collector,
                options,
                null,
                fileManager.getJavaFileObjectsFromFiles(java)
        );

        boolean ok = task.call();
        protocol = collector.getDiagnostics();

        return ok;
    }

    public Created createClassFromString(File targetDir, String str) {
        try {
            Optional<ClassOrInterfaceDeclaration> noMethod = StaticJavaParser.parse(str).findFirst(ClassOrInterfaceDeclaration.class);
            File file;
            if (noMethod.isPresent() && noMethod.get().getFullyQualifiedName().isPresent()) {
                Path target = Paths.get(targetDir.toPath()+"/"+ noMethod.get().getFullyQualifiedName().get().replace(".","/") + SUFFIX);
                File p = target.getParent().toFile();
                if (! p.exists()) {
                    target.getParent().toFile().mkdirs();
                }
                file = target.toFile();
                write(target, str);
                return new Created(file, noMethod.get().getFullyQualifiedName().get(), true);
            } else {
                Path target = Path.of(targetDir + "/" + DEFAULT_CLASS_NAME + SUFFIX);
                file = target.toFile();
                write(target, methodToClass(str));
                return new Created(file, DEFAULT_CLASS_NAME, true);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new Created(null, null, false);
        }
    }

    private void write(Path target, String clazz) throws IOException {
        Files.write(target, clazz.getBytes());
    }

    private String methodToClass(String method) {
        return new StringBuilder().append("/**" + "public")
                .append(DEFAULT_CLASS_NAME)
                .append("*/")
                .append("\n")
                .append("import java.util.*;")
                .append("\n")
                .append("public class")
                .append(" ")
                .append(DEFAULT_CLASS_NAME)
                .append(" {")
                .append("\n")
                .append(method)
                .append("\n")
                .append("}")
                .toString();
    }

}
