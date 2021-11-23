package eu.qped.umr.compiler;

import eu.qped.umr.checkers.SemanticChecker;
import eu.qped.umr.checkers.StatementsVisitor;
import eu.qped.umr.checkers.PMDStyleChecker;
import eu.qped.umr.checkers.SyntaxErrorChecker;
import eu.qped.umr.configs.*;
import eu.qped.umr.model.SyntaxError;



import javax.tools.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Compiler {
    private String input;
    private final ArrayList<SyntaxError> errors = new ArrayList<>();
    private Map<String, String> feedbacks = new HashMap<>();

    private StringBuilder javaFileContent;
    private ArrayList<String> feedbacksArray;
    private StyleConfigurator styleConfigurator;

    private StatementsVisitor statementsVisitor;
    private SemanticConfigurator semanticConfigurator;
    private final MainSettings mainSettings;

    private ArrayList<String> semanticFeedbacks;




    public Compiler(String input, MainSettings settings ,StyleConfigurator styleConfigurator,  SemanticConfigurator semanticConfigurator) {
        this.input = input;
        this.styleConfigurator = styleConfigurator;

        feedbacksArray = new ArrayList<>();
        this.semanticConfigurator = semanticConfigurator;
        this.mainSettings = settings;
        this.semanticFeedbacks = new ArrayList<>();
    }

    public Compiler ( String input ,MainSettings mainSettings) {
        this(input , mainSettings ,  null  , null);
    }


    //todo: Java Doc @later

    /**
     * die Methode konvertiert ein String-Source zu einem Java Objekt
     * Die Methode betrachtet ob es eine Klasse bzw. eine Methode geschrieben wurde.
     *
     * @return SimpleJavaFileObject
     */
    public SimpleJavaFileObject getJavaFileContentFromString() {
        javaFileContent = new StringBuilder();
        boolean isClass = input.contains("class");
        boolean isPublic = false;
        if (isClass) {
            String classDeclaration = input.substring(0, input.indexOf("class"));
            isPublic = classDeclaration.contains("public");
        }
        if (isPublic) {
            input = input.substring(input.indexOf("public") + "public".length());
        }
        if (isClass) {
            javaFileContent.append(input);
        } else {
            javaFileContent.append("/**" +
                    "* Test class" +
                    "*/" +
                    "import java.util.*;" +
                    "class TestClass {").append(input).append("}");
        }


        JavaObjectFromString javaObjectFromString = null;
        try {
            javaObjectFromString = new JavaObjectFromString("TestClass", javaFileContent.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return javaObjectFromString;
    }

    private void writeJavaFileContent() {
        try (OutputStream output = Files.newOutputStream(Paths.get("TestClass.java"))) {
            output.write(javaFileContent.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean compile() throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(diagnosticCollector, Locale.GERMANY, Charset.defaultCharset());
        JavaFileObject javaFileObjectFromString = getJavaFileContentFromString();
        Iterable<JavaFileObject> fileObjects = Collections.singletonList(javaFileObjectFromString);

        StringWriter output = new StringWriter();

        JavaCompiler.CompilationTask task = compiler.getTask(output, standardJavaFileManager, diagnosticCollector, null, null, fileObjects);

        Boolean result = task.call();
        String source = "";

        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            //todo: try-catch
            source = diagnostic.getSource().getCharContent(true).toString();
            //System.out.println(source);
            String errorSource;
            try {
                errorSource = source.substring((int) diagnostic.getStartPosition());
            } catch (StringIndexOutOfBoundsException e) {
                errorSource = source.substring((int) diagnostic.getStartPosition() + 1);
            }
            String[] splitSource = errorSource.split(";");

            Map<String, String> addProp = new HashMap<>();
            if (diagnostic.getCode().equals("compiler.err.expected")) {
                String forExpected = errorSource.split("[{]")[0];
                addProp.put("forSemExpected", forExpected);
            }

            String errorTrigger = splitSource[0];

            errors.add(new SyntaxError(diagnostic.getCode(), diagnostic.getMessage(Locale.GERMAN), diagnostic.getLineNumber(), errorTrigger, addProp, diagnostic.getStartPosition(), diagnostic.getEndPosition()));
        }


        boolean styleNeeded = Boolean.parseBoolean(mainSettings.getStyleNeeded());
        boolean semanticNeeded = Boolean.parseBoolean(mainSettings.getSemanticNeeded());

        if (result) {
            try {
                writeJavaFileContent();
                source = javaFileContent.toString();

                if (styleConfigurator == null){
                    styleConfigurator = new StyleConfigurator(null);
                }

                if (semanticConfigurator == null){
                    semanticConfigurator = new SemanticConfigurator(null);
                }

                if (styleNeeded && styleConfigurator != null){
                    PMDStyleChecker styleChecker = new PMDStyleChecker(styleConfigurator);
                    feedbacks = styleChecker.getFeedbacks();
                }


                if (semanticNeeded && semanticConfigurator != null){

                    SemanticChecker semanticChecker = new SemanticChecker(source, this.semanticConfigurator);
                    semanticFeedbacks = semanticChecker.getFeedbacks();
                }

                if (feedbacks.size() == 0) {
                    feedbacks.put("", "Good Job!, no feedback needed!");
                }
            }
            catch (Exception e){
                Logger.getInstance().log(e.getMessage());
            }

            return true;
        } else {
            //todo level
            ConfLevel confLevel = mainSettings.getSyntaxLevel();
            SyntaxErrorChecker syntaxErrorChecker = new SyntaxErrorChecker(errors, source, confLevel);
            syntaxErrorChecker.prepareFeedbackForSE();
            //feedbacks = errorAnalyser.getFeedbacks();
            feedbacksArray = syntaxErrorChecker.getFeedbacks();
            System.out.println("size: " + feedbacksArray.size());
        }
        return false;
    }


    public Map<String, String> getFeedbacks() {
        return feedbacks;
    }

    private void print(){
        for (int i = 0; i <10 ; i++) {
        }
    }



    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();


        Map<String , String> mainSettings = new HashMap<>();
        mainSettings.put("semanticNeeded" , "true");
        mainSettings.put("syntaxLevel" , "2");
        mainSettings.put("preferredLanguage" , "en");
        mainSettings.put("styleNeeded" , "true");


        MainSettings mainSettingsConf = new MainSettings(mainSettings);


        Map<String, String> styleConf = new HashMap<>();

        //styleConf.put("mainLevel" , "1");

        styleConf.put("namesLevel", "profi");
        styleConf.put("compLevel", "profi");
        //styleConf.put("basisLevel", "3");
        styleConf.put("maxClassLength", "100");
        styleConf.put("maxMethodLength", "-1");
        styleConf.put("varNamesRegEx", "-1");
        styleConf.put("methodNamesRegEx", "-1");
        styleConf.put("classNameRegEx", "-1");
        styleConf.put("maxCycloComplexity", "-1");
        styleConf.put("maxFieldsCount", "-1");




        Map<String , String> semanticConf = new HashMap<>();

        semanticConf.put("methodName" , "rec");
        semanticConf.put("recursionAllowed" , "true");
        semanticConf.put("whereLoop" , "-1");
        semanticConf.put("forLoop" , "1");
        semanticConf.put("forEachLoop" , "-1");
        semanticConf.put("ifElseStmt" , "-1");
        semanticConf.put("doWhileLoop" , "-1");
        semanticConf.put("returnType" , "undefined");

        SemanticConfigurator semanticConfigurator = new SemanticConfigurator(semanticConf);

        String code = "    private int rec(int r){\n" +
                "\n" +
                "        if (true){\n" +
                "            if (false){\n" +
                "\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        for (int i = 0; i <100 ; i++)\n" +
                "            for (int j = 0; j <100 ; j++) {\n" +
                "                for (int k = 0; k <4 ; k++) {\n" +
                "                    System.out.println();\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "\n" +
                "        for (int i = 0; i <10 ; i++) {\n" +
                "            System.out.println();\n" +
                "            for (int j = 0; j < 10; j++) {\n" +
                "                System.out.println();\n" +
                "                for (int k = 0; k < 15; k++) {\n" +
                "                    System.out.println();\n" +
                "                    for (int l = 0; l < 111; l++) {\n" +
                "                        System.out.println();\n" +
                "                        return 1;\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        if (r == 1) return 1;\n" +
                "        else return rec(r /2);\n" +
                "    }";




        StyleConfigurator styleConfigurator = new StyleConfigurator(styleConf);

        Compiler compiler = new Compiler(code , mainSettingsConf ,styleConfigurator , semanticConfigurator);
        //todo false Alarm: Here was Semicolon expected!



        //Compiler compiler = new Compiler(code, styleConfigurator, syntaxConfigurator);

        compiler.compile();






        for (String s : compiler.semanticFeedbacks){
            System.out.println(s);
        }


        /*
        for Style Errors
         */

        Map<String, String> feedbacks = compiler.getFeedbacks();

        for (Map.Entry<String, String> entry : feedbacks.entrySet()) {
            System.out.println(entry.getKey() + " feedback: " + entry.getValue());
        }

        /*
        for Syntax Errors
         */
        ArrayList<String> arrayList = compiler.getFeedbacksArray();
        for (String s : arrayList) {
            System.out.println(s);
            System.out.println("------------------");
        }
        long end = System.nanoTime() - start;
        System.out.println("Feedback generated in: " + end * Math.pow(10.0, -9.0) + " sec");

    }

    public ArrayList<String> getFeedbacksArray() {
        return feedbacksArray;
    }

    public StatementsVisitor getStatementsVisitor() {
        return statementsVisitor;
    }

    public MainSettings getMainSettings() {
        return mainSettings;
    }

    public ArrayList<String> getSemanticFeedbacks() {
        return semanticFeedbacks;
    }
}
