package eu.qped.java.utils.compiler;

import eu.qped.java.utils.ExtractJavaFilesFromDirectory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Java compiler for source code as String.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compiler implements CompilerInterface {

    private static final String DEFAULT_CLASS_PATH = "TestClass.java";
    private static final String DEFAULT_CLASS_NAME = "TestClass";

    private static final String DEFAULT_DIR_PATH = "exam-results";

    private List<Diagnostic<? extends JavaFileObject>> collectedDiagnostics;

    private String compiledStringResourcePath;

    private String targetProjectOrClassPath;
    private String fileName;

    private String fullSourceCode;

    private List<String> options;

    @Override
    public boolean compileFromString(String code) {
        return compile(code);
    }

    @Override
    public boolean compileFromProject(String path) {
        setTargetProjectOrClassPath(path);
        return compile(null);
    }

    private boolean compile(String stringAnswer) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, Locale.GERMANY, Charset.defaultCharset());
        List<File> files = new ArrayList<>();

        if (stringAnswer != null && !stringAnswer.equals("")) {
            createJavaClass(writeCodeAsClass(stringAnswer));
            files.add(new File(targetProjectOrClassPath));

        } else {
            ExtractJavaFilesFromDirectory.ExtractJavaFilesFromDirectoryBuilder extractJavaFilesFromDirectoryBuilder = ExtractJavaFilesFromDirectory.builder();
            ExtractJavaFilesFromDirectory extractJavaFilesFromDirectory;
            if (targetProjectOrClassPath == null || targetProjectOrClassPath.equals("")) {
                targetProjectOrClassPath = DEFAULT_DIR_PATH;
            }
            extractJavaFilesFromDirectoryBuilder.dirPath(targetProjectOrClassPath);
            extractJavaFilesFromDirectory = extractJavaFilesFromDirectoryBuilder.build();
            files = extractJavaFilesFromDirectory.filesWithExtension("java");
            if (files.size() == 0) {
                return false;
            }
            files.forEach(System.out::println);
        }
        StringWriter stringWriter = new StringWriter();
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);

        if (options == null) {
            setDefaultOptions();
        }
        addClassFilesDestination("src/main/java/eu/qped/java/utils/compiler/compiledFiles");
        JavaCompiler.CompilationTask task = compiler.getTask(stringWriter, fileManager, diagnosticsCollector, options, null, compilationUnits);
        boolean result = task.call();

        this.setCollectedDiagnostics(diagnosticsCollector.getDiagnostics());
        return result;
    }



    public void addExternalJarsToClassPath(List<String> paths) {
        if (this.options == null) {
            setDefaultOptions();
        }
        this.options.add("cp");
        this.options.addAll(paths);
    }

    private void setDefaultOptions() {
        this.options = new ArrayList<>();
        options.add("-verbose");
        options.add("-Xlint");
        options.add("-g");
    }


    private void writeJavaFileContent(String code) {
        try (OutputStream output = Files.newOutputStream(Paths.get(targetProjectOrClassPath))) {
            output.write(code.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
    }

    private void createJavaClass(String javaFileContent) {
        try {
            // create class for Answer
            writeJavaFileContent(javaFileContent);
        } catch (Exception e) {
            LogManager.getLogger(getClass()).throwing(e);
        }
    }

    public void addClassFilesDestination(String path) {
        if (options == null) {
            setDefaultOptions();
        }
        options.add("-d");
        options.add(path);
        options.add("-s");
        options.add(path);
    }

    /**
     * @param answer the code
     * @return If the code does not contain a class, a default class is created and return it
     */
    private String writeCodeAsClass(String answer) {
        StringBuilder javaFileContent = new StringBuilder();
        boolean isClassOrInterface = answer.contains("class") || answer.contains("interface");

        if (isClassOrInterface) {
            String classDeclaration = answer.substring(answer.indexOf("class"), answer.indexOf("{"));

            String[] declarationArray = classDeclaration.split(" ");

            if (declarationArray.length < 2) {
                fileName = DEFAULT_CLASS_NAME;
                targetProjectOrClassPath = DEFAULT_CLASS_PATH;
            } else {
                fileName = declarationArray[1].trim(); // class name by student
                if (compiledStringResourcePath != null && !compiledStringResourcePath.equals("")) {
                    targetProjectOrClassPath = compiledStringResourcePath;
                    if (compiledStringResourcePath.charAt(compiledStringResourcePath.length() - 1) == '/') {
                        targetProjectOrClassPath += fileName + ".java";
                    } else {
                        targetProjectOrClassPath += "/" + fileName + ".java";
                    }
                } else {
                    targetProjectOrClassPath = fileName + ".java";
                }


            }
        } else {
            fileName = DEFAULT_CLASS_NAME;
            targetProjectOrClassPath = DEFAULT_CLASS_PATH;
        }
        if (isClassOrInterface) {
            javaFileContent.append(answer);
        } else {
            javaFileContent.append("/**" + "public")
                    .append(fileName)
                    .append("*/")
                    .append("\n")
                    .append("import java.util.*;")
                    .append("\n")
                    .append("public class")
                    .append(" ")
                    .append(fileName)
                    .append(" {")
                    .append("\n")
                    .append(answer)
                    .append("\n")
                    .append("}");
        }
        fullSourceCode = javaFileContent.toString();
        return javaFileContent.toString();
    }

    public static void main(String[] args) {
        Compiler compiler = Compiler.builder().build();
        compiler.addClassFilesDestination("src/main/java/eu/qped/java/utils/compiler/compiledFiles");
//        compiler.addSourceFilesDestination("src/main/java/eu/qped/java/utils/compiler/compiledFiles");

        compiler.setCompiledStringResourcePath("src/main/resources/exam-results");

        boolean compile = compiler.compile("import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "public class GrayCode {\n" +
                "\n" +
                "    public GrayCode() {\n" +
                "    }\n" +
                "\n" +
                "    public static List<String> grayCodeStrings(int n) {\n" +
                "        List<String> list = new ArrayList();\n" +
                "        if (n == 0) {\n" +
                "            list.add(\"\");\n" +
                "            return list;\n" +
                "        } else if (n == 1) {\n" +
                "            list.add(\"0\");\n" +
                "            list.add(\"1\");\n" +
                "            return list;\n" +
                "        } else {\n" +
                "            List<String> prev = grayCodeStrings(n - 1);\n" +
                "            list.addAll(prev);\n" +
                "\n" +
                "            for(int i = prev.size() - 1; i >= 0; --i) {\n" +
                "                String bits = \"abcccc\";\n" +
                "                list.set(i, \"0\" + bits);\n" +
                "                list.add(\"1\" + bits);\n" +
                "            }\n" +
                "\n" +
                "            return list;\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "}");

        System.out.println(compile);
    }


}
