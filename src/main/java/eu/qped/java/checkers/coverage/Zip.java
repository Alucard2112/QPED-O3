package eu.qped.java.checkers.coverage;

import eu.qped.framework.FileInfo;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;


public class Zip implements ZipService {
    private static final String SCHEME = "file:";
    private static final String SUFFIX = ".zip";


    public class ZipExtracted implements ZipService.Extracted {
        private final LinkedList<String> testClasses;
        private final LinkedList<String> classes;
        private final File root;
        private final Map<String, File> files;

        public ZipExtracted(LinkedList<String> testClasses, LinkedList<String> classes, File root, Map<String, File> files) {
            this.testClasses = testClasses;
            this.classes = classes;
            this.root = root;
            this.files = files;
        }

        @Override
        public List<String> testClasses() {
            return new LinkedList<>(testClasses);
        }

        @Override
        public List<String> classes() {
            return new LinkedList<>(classes);
        }

        @Override
        public List<File> files() {
            return new LinkedList<>(files.values());
        }

        @Override
        public Map<String, File> javafileByClassname() {
            return new HashMap<>(files);
        }

        @Override
        public File root() {
            return root;
        }

        @Override
        public String toString() {
            return "ZipExtracted{" +
                    "testClasses=" + testClasses +
                    ", classes=" + classes +
                    ", files=" + files +
                    '}';
        }
    }

    private final List<File> toDelete = new LinkedList<>();

    @Override
    public File download(String url) throws Exception {
        File download = File.createTempFile("download", SUFFIX);
        toDelete.add(download);
        FileUtils.copyURLToFile(new URL(url), download);
        return download;
    }

    @Override
    public Extracted extract(File file, TestClass testClass, Classname classname) throws Exception {
        if (! isZip(file))
            return null;

        File unzipTarget = Files.createTempDirectory(ZipService.UNZIPPED_NAME).toFile();
        toDelete.add(unzipTarget);
        ZipFile zipFileA = new ZipFile(file);
        zipFileA.extractAll(unzipTarget.toString());
        return extracted(unzipTarget, testClass, classname);
    }

    private boolean isZip(File file) {
        return file.getName().endsWith(".zip");
    }


    @Override
    public Extracted extractBoth(File fileA, File fileB, TestClass testClass, Classname classname) throws Exception {
        if (! isZip(fileB) || ! isZip(fileA))
            return null;
        File unzipTarget = Files.createTempDirectory(ZipService.UNZIPPED_NAME).toFile();
        toDelete.add(unzipTarget);
        ZipFile zipFileA = new ZipFile(fileA);
        zipFileA.extractAll(unzipTarget.toString());
        ZipFile zipFileB = new ZipFile(fileB);
        zipFileB.extractAll(unzipTarget.toString());
        return extracted(unzipTarget, testClass, classname);
    }


    private Extracted extracted(File unzipTarget, TestClass testClass, Classname classname) {
        LinkedList<File> stack = new LinkedList<>();
        Map<String, File> files = new HashMap<>();
        LinkedList<String> testClasses = new LinkedList<>();
        LinkedList<String> classes = new LinkedList<>();
        stack.add(unzipTarget);

        while (!stack.isEmpty()) {
            File first = stack.removeFirst();
            if (first.isDirectory()) {
                stack.addAll(0, Arrays.asList(first.listFiles()));
            } else if (Pattern.matches(".*\\.java$", first.getName())) {
                String name = classname.parse(first);
                if (Objects.isNull(name))
                    continue;
                name = name.replace("/",".");
                files.put(name, first);
                if (testClass.isTrue(first)) {
                    testClasses.add(name);
                } else {
                    classes.add(name);
                }
            }
        }
        return new ZipExtracted(
                testClasses,
                classes,
                unzipTarget,
                files);
    }

    @Override
    public void cleanUp() {
        for (File file : toDelete) {
            if (file.isDirectory()) {
                try {
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FileUtils.delete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
