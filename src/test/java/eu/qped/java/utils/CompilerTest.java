package eu.qped.java.utils;

import eu.qped.java.utils.compiler.Compiler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class CompilerTest {

    private Compiler compiler;

    @BeforeEach
    void beforeAll(){
        compiler = Compiler.builder().build();
    }

    @Test
    public void compileStringTest(){
        boolean falseResult = compiler.compileFromString("private void print() {\n" +
                "        System.out.println()\n" +
                "    }");

        Assertions.assertFalse(falseResult);

        boolean trueResult = compiler.compileFromString("private void print() {\n" +
                "        System.out.println();\n" +
                "    }");

        Assertions.assertTrue(trueResult);

    }

    @Test
    public void compileProjectFalseTest() throws IOException {

        Path tempDir = Files.createTempDirectory("exam-results");
        File file = File.createTempFile("test" ,".java", tempDir.toFile());

        String falseCode = "class test {private void print() { \n System.out.println() \n  }}";
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(falseCode.getBytes());

        boolean result = compiler.compileFromProject(tempDir.toString());
        Assertions.assertFalse(result);
        Assertions.assertEquals(1, compiler.getCollectedDiagnostics().size());
        file.deleteOnExit();
    }

    @Test
    public void compileProjectTrueTest() throws IOException {
        Path tempDir = Files.createTempDirectory("exam-results");
        File file = File.createTempFile("test" ,".java", tempDir.toFile());
        File file1 = File.createTempFile("test1" ,".java", tempDir.toFile());

        String correctCode = "class test {private void print() { \n System.out.println(); \n  }}";
        String correctCode1 = "class test1 {public void print() { \n System.out.println(); \n  }}";

        FileOutputStream fos = new FileOutputStream(file);
        FileOutputStream fos1 = new FileOutputStream(file1);

        fos.write(correctCode.getBytes());
        fos1.write(correctCode1.getBytes());

        boolean result = compiler.compileFromProject(tempDir.toString());
        Assertions.assertTrue(result);
        Assertions.assertEquals(0, compiler.getCollectedDiagnostics().size());
        file.deleteOnExit();
    }

    @Test
    public void compileProjectWithoutFilesTest() throws IOException {
        Path tempDir = Files.createTempDirectory("exam-results");
        boolean result = compiler.compileFromProject(tempDir.toString());
        Assertions.assertFalse(result);
    }


}
