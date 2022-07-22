package eu.qped.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.qped.framework.qf.QfObject;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckerRunner {

	private static final String QF_OBJECT_FILE_PROPERTY = "file";

	private static final File QF_OBJECT_JSON_FILE = new File("qf.json");

	private final QfObject qfObject;

	private final Checker checker;

	private FileInfo fileInfo;

	private File submittedFile;
	
	private static List<File> tempFiles = new ArrayList<>();
	
	private static void cleanupTempFiles() {
		for (File file : tempFiles) {
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
		tempFiles.clear();
	}
	
	public CheckerRunner() throws JsonMappingException, JsonProcessingException, IOException {
		String qfObjectJsonString = FileUtils.readFileToString(QF_OBJECT_JSON_FILE, Charset.defaultCharset());

		ObjectMapper mapper = new ObjectMapper();

		// sem, semObj
		// syntax, syntax
		// style, style
		// checkerClass: name
		Map<String, Object> qfObjectMap = mapper.readValue(qfObjectJsonString,
				new TypeReference<Map<String, Object>>() {
				});

		//qfObject = mapper.readValue(qfObjectJsonString, new TypeReference<QfObject>(){});

		String checkerClassName = (String) qfObjectMap.get("checkerClass");
		if (checkerClassName == null) {
			throw new IllegalArgumentException("No checker class specified");
		}

		try {
			@SuppressWarnings("unchecked")
			Class<Checker> cls = (Class<Checker>) Class.forName(checkerClassName);
			this.checker = cls.getDeclaredConstructor().newInstance();

			if (qfObjectMap.containsKey(QF_OBJECT_FILE_PROPERTY)) {
				fileInfo = mapper.readValue(
						mapper.writeValueAsString(qfObjectMap.get(QF_OBJECT_FILE_PROPERTY)),
						new TypeReference<FileInfo>() {
						});

				submittedFile = File.createTempFile(fileInfo.getId(), fileInfo.getExtension());
				tempFiles.add(submittedFile);

				try (InputStream input = new URL(fileInfo.getUrl()).openStream()) {
					try (OutputStream output = new FileOutputStream(submittedFile)) {
						final int BUFFER_SIZE = 1024;
						byte[] buffer = new byte[BUFFER_SIZE];
						int bytesRead;
						while ((bytesRead = input.read(buffer, 0, BUFFER_SIZE)) != -1) {
							output.write(buffer, 0, bytesRead);
						}
					}
				}
				if (fileInfo.getMimetype().contains("application/x-zip-compressed") || fileInfo.getMimetype().contains("application/zip") ) {
					try {
						File unzipTarget = Files.createTempDirectory("exam-results").toFile();
						tempFiles.add(unzipTarget);

						ZipFile zipFile = new ZipFile(submittedFile);
						zipFile.extractAll(unzipTarget.toString());

						fileInfo.setUnzipped(unzipTarget);
					} catch (ZipException e) {
						throw new IllegalArgumentException(e);
					}
				}
				fileInfo.setSubmittedFile(submittedFile);


			} else {
				submittedFile = null;
			}


			qfObject = mapper.readValue(mapper.writeValueAsString(qfObjectMap), new TypeReference<QfObject>() {
			});

			for (Field field : cls.getDeclaredFields()) {
				QfProperty qfPropertyAnnotation = field.getDeclaredAnnotation(QfProperty.class);
				if (qfPropertyAnnotation != null) {
					if (field.getName().equals("file")) {
						if (field.getType() != FileInfo.class) {
							throw new IllegalArgumentException("The field " + field.getName() + " of checker class " + checkerClassName + " has the QfProperty annotation, but does not have the type FileInfo.");
						}
						field.setAccessible(true);
						field.set(checker, fileInfo);
					} else {
						Object propertyValue = qfObjectMap.get(field.getName());
						field.setAccessible(true);
						field.set(checker, mapper.readValue(mapper.writeValueAsString(propertyValue), field.getType()));
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Illegal checker class specified", e);
		}
	}

	public void check() throws Exception {
		checker.check(qfObject);
	}

	public static void main(String[] args) throws IOException {
		CheckerRunner runner = new CheckerRunner();
		try {
			runner.check();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			runner.getQfObject().setMessage("exceptionDuringCheck", sw.toString());
		}

		ObjectMapper mapper = new ObjectMapper();

		ObjectWriter writer = mapper.writer(new MinimalPrettyPrinter());
		writer.writeValue(QF_OBJECT_JSON_FILE, runner.getQfObject());
		cleanupTempFiles();
	}

	public QfObject getQfObject() {
		return qfObject;
	}
}
