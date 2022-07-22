package eu.qped.java.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
public class ExtractJavaFilesFromDirectory {

    private static final String DEFAULT_DIR = "exam-results";

    private String dirPath;

    public List<File> filesWithExtension(String extension) {
        List<File> allFiles = new ArrayList<>();
        List<File> filesWithJavaExtension = new ArrayList<>();
        File fileOrDirectory = new File(Objects.requireNonNullElse(dirPath, DEFAULT_DIR));
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory()) {
                allFiles.add(fileOrDirectory);
                allFiles.addAll(getFilesRecursively(fileOrDirectory));
            } else {
                allFiles.add(new File(fileOrDirectory.getPath()));
            }
        }
        for (File file : allFiles) {
            if (FilenameUtils.getExtension(String.valueOf(file)).equals(extension)) {
                filesWithJavaExtension.add(file);
            }
        }
        return filesWithJavaExtension;
    }

    private List<File> getFilesRecursively(File path) {
        List<File> files = new ArrayList<>();
        for (File file : Objects.requireNonNull(path.listFiles())) {
            if (file.isDirectory()) {
                files.add(file);
                files.addAll(getFilesRecursively(file));
            } else {
                files.add(file);
            }
        }
        return files;
    }
}