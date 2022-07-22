package eu.qped.example;

import java.io.File;

import eu.qped.framework.Checker;
import eu.qped.framework.FileInfo;
import eu.qped.framework.QfProperty;
import eu.qped.framework.qf.QfObject;

public class FileChecker implements Checker {

	@QfProperty
	private FileInfo file;

	
	@Override
	public void check(QfObject qfObject) throws Exception {
		System.out.println("Path to submitted file on local file system:");
		System.out.println(file.getSubmittedFile());
		
		if (file.getUnzipped() != null) {
			System.out.println("Submitted file was a zip file. Paht to unzipped contents:");
			printRecursive(file.getUnzipped(), "");
		}
	}

	private void printRecursive(File file, String indentation) {
		System.out.println(indentation + file.getName());
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				printRecursive(child, indentation + "    ");
			}
		}
	}

}
