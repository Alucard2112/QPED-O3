package eu.qped.java.checkers.syntax;

import eu.qped.framework.CheckLevel;
import eu.qped.java.utils.compiler.Compiler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * checker for the syntax problems in java code.
 *
 * @author Mayar Hamdhash hamdash@students.uni-marburg.de
 * @version 2.0
 * @since 17.04.2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyntaxChecker implements Runnable {

    private String stringAnswer;

    private String targetProject;

    private Compiler compiler;

    @Deprecated(forRemoval = true)
    private CheckLevel level;

    @Override
    public void run() {
        System.out.println("running: " + this.getClass().getSimpleName());
    }



    private String getErrorTrigger(Diagnostic<? extends JavaFileObject> diagnostic) {

        var errorCode = "";

        try {
            errorCode = diagnostic.getSource().getCharContent(false).toString();
        } catch (IOException | NullPointerException e) { // TODO QUICK FIX NullPointerException <Diagnostic=warning: No processor claimed any of these annotations: /org.junit.jupiter.api.Test>  has no source
            // e.printStackTrace();
            return errorCode;
        }
        String[] codeSplitByLine = errorCode.split("\n");

        if (codeSplitByLine.length < 1) {
            return "";
        }

        int line = (int) diagnostic.getLineNumber();

        return codeSplitByLine[line - 1];
    }

    private List<SyntaxError> analyseDiagnostics(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        List<SyntaxError> syntaxErrors = new ArrayList<>();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            String errorTrigger = getErrorTrigger(diagnostic);
            if (errorTrigger.isBlank()) // // TODO QUICK FIX NullPointerException <Diagnostic=warning: No processor claimed any of these annotations: /org.junit.jupiter.api.Test>  has no source
                continue;

            syntaxErrors.add(
                    SyntaxError.builder()
                            .errorCode(diagnostic.getCode())
                            .fileName(diagnostic.getSource().getName())
                            .errorMessage(diagnostic.getMessage(Locale.GERMAN))
                            .startPos(diagnostic.getStartPosition())
                            .endPos(diagnostic.getEndPosition())
                            .line(diagnostic.getLineNumber())
                            .errorTrigger(errorTrigger)
                            .columnNumber(diagnostic.getColumnNumber())
                            .build()
            );

        }
        return syntaxErrors;
    }

    public SyntaxCheckReport check() {
        SyntaxCheckReport.SyntaxCheckReportBuilder resultBuilder = SyntaxCheckReport.builder();

        if (compiler == null) {
            compiler = Compiler.builder().build();
        }

        boolean compileResult;

        compiler.setCompiledStringResourcePath("src/main/resources/exam-results/src");

        if (stringAnswer != null && !stringAnswer.equals("")) {
            compileResult = compiler.compileFromString(stringAnswer);
            resultBuilder.compiledSourceType(CompiledSourceType.STRING);
            resultBuilder.codeAsString(compiler.getFullSourceCode());
        } else {
            compileResult = compiler.compileFromProject(targetProject);
            resultBuilder.compiledSourceType(CompiledSourceType.PROJECT);
        }
        resultBuilder.isCompilable(compileResult);

        List<Diagnostic<? extends JavaFileObject>> diagnostics = compiler.getCollectedDiagnostics();
        List<SyntaxError> collectedErrors = new ArrayList<>();
        if (diagnostics != null) {
            collectedErrors = analyseDiagnostics(diagnostics);
        }
        resultBuilder.syntaxErrors(collectedErrors);
        resultBuilder.path(compiler.getTargetProjectOrClassPath());
        return resultBuilder.build();
    }

    public static void main(String[] args) {
        SyntaxChecker checker = SyntaxChecker.builder().targetProject("tmp/exam-results62b874f9fb9d582f0b08d371").build();
        var report = checker.check();

        System.out.println("path: " + report.getPath());

        System.out.println(report.getSyntaxErrors().size());

    }


}
