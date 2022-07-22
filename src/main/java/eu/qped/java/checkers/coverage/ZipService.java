package eu.qped.java.checkers.coverage;

import eu.qped.framework.FileInfo;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ZipService {
    static String UNZIPPED_NAME = "unzipped";

    TestClass MAVEN_TEST_CLASS = (file) -> {
        return Pattern.matches("src/test/java.*\\.java$", file.getPath());
    };

    TestClass JAVA_TEST_CLASS = (file) -> {
        return Pattern.matches(".*Test\\.java$", file.getPath());
    };

    Classname MAVEN_CLASS_NAME = (file) -> {
        Pattern pattern = Pattern.compile(".*src/+(test|main)+/java/(.*)\\.java$");
        Matcher matcher = pattern.matcher(file.getPath());
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    };

    Classname JAVA_CLASS_NAME = (file) -> {
        Pattern pattern = Pattern.compile(".*/"+UNZIPPED_NAME+"\\d+/(.*)\\.java$");
        Matcher matcher = pattern.matcher(file.getPath());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    };




    @FunctionalInterface
    interface TestClass {
        boolean isTrue(File file);
    }

    @FunctionalInterface
    interface Classname {
        String parse(File file);
    }

    interface Extracted {

        List<String> testClasses();
        List<String> classes();
        List<File> files();

        Map<String, File> javafileByClassname();

        File root();
    }

    File download(String url) throws Exception;

    Extracted extract(File file, TestClass testClass, Classname classname) throws Exception;

    public Extracted extractBoth(File fileA, File fileB, TestClass testClass, Classname classname) throws Exception;


    void cleanUp();

}
