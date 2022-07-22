package eu.qped.java.syntaxChecker;


import eu.qped.java.checkers.syntax.SyntaxCheckReport;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.compiler.Compiler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;


@ExtendWith(MockitoExtension.class)
public class SyntaxCheckerTest {

    private SyntaxChecker syntaxChecker;

    @Mock
    private Compiler compiler;

    @BeforeEach
    public void beforeEach() {
        syntaxChecker = new SyntaxChecker();
        syntaxChecker.setCompiler(compiler);
    }


    @Test
    public void checkPassTest() {

        String code = "correct code";

        Mockito.when(compiler.compileFromString(code))
                .thenReturn(true);

        syntaxChecker.setStringAnswer(code);

        SyntaxCheckReport report = syntaxChecker.check();

        Assertions.assertEquals(report.getSyntaxErrors().size(), 0);
        Assertions.assertTrue(report.isCompilable());
    }


    @Test
    public void checkFailTest() {

        String failCode = "failCode";
        syntaxChecker.setStringAnswer(failCode);

        Mockito.when(compiler.compileFromString(failCode))
                .thenReturn(false);

        Mockito.when(compiler.getCollectedDiagnostics())
                .thenReturn(List.of(
                        new Diagnostic<>() {
                            @Override
                            public Kind getKind() {
                                return Kind.ERROR;
                            }

                            @SneakyThrows
                            @Override
                            public JavaFileObject getSource() {
                                return new JavaFileObject() {
                                    @Override
                                    public Kind getKind() {
                                        return Kind.SOURCE;
                                    }

                                    @Override
                                    public boolean isNameCompatible(String simpleName, Kind kind) {
                                        return false;
                                    }

                                    @Override
                                    public NestingKind getNestingKind() {
                                        return null;
                                    }

                                    @Override
                                    public Modifier getAccessLevel() {
                                        return null;
                                    }

                                    @SneakyThrows
                                    @Override
                                    public URI toUri() {
                                        return new URI("TestClass");
                                    }

                                    @Override
                                    public String getName() {
                                        return null;
                                    }

                                    @Override
                                    public InputStream openInputStream() throws IOException {
                                        return null;
                                    }

                                    @Override
                                    public OutputStream openOutputStream() throws IOException {
                                        return null;
                                    }

                                    @Override
                                    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
                                        return null;
                                    }

                                    @Override
                                    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
                                        return failCode;
                                    }

                                    @Override
                                    public Writer openWriter() throws IOException {
                                        return null;
                                    }

                                    @Override
                                    public long getLastModified() {
                                        return 0;
                                    }

                                    @Override
                                    public boolean delete() {
                                        return false;
                                    }
                                };
                            }

                            @Override
                            public long getPosition() {
                                return 1;
                            }

                            @Override
                            public long getStartPosition() {
                                return 1;
                            }

                            @Override
                            public long getEndPosition() {
                                return 3;
                            }

                            @Override
                            public long getLineNumber() {
                                return 1;
                            }

                            @Override
                            public long getColumnNumber() {
                                return 3;
                            }

                            @Override
                            public String getCode() {
                                return "err.expected";
                            }

                            @Override
                            public String getMessage(Locale locale) {
                                return "; expected";
                            }
                        }
                ));

        SyntaxCheckReport report = syntaxChecker.check();
        Assertions.assertEquals(report.getSyntaxErrors().size(), 1);
        Assertions.assertEquals(report.getSyntaxErrors().get(0).getErrorMessage(), "; expected");
        Assertions.assertFalse(report.isCompilable());
    }

    @Test
    public void checkProjectPass() throws IOException {

        Path tempDir = Files.createTempDirectory("src");

        File class1 = File.createTempFile("Class1", ".java", tempDir.toFile());
        File class2 = File.createTempFile("Class2", ".java", tempDir.toFile());

//        compiler.setTargetProjectOrClassPath(tempDir.toUri().getPath());

        Mockito.when(compiler.compileFromProject(tempDir.toUri().getPath()))
                .thenReturn(true);

        Mockito.when(compiler.getTargetProjectOrClassPath())
                .thenReturn(tempDir.toUri().getPath());

        syntaxChecker.setTargetProject(tempDir.toUri().getPath());
        SyntaxCheckReport report = syntaxChecker.check();

        Assertions.assertEquals(report.getPath(), tempDir.toUri().getPath());


    }

}
