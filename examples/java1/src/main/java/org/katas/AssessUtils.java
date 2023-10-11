package org.katas;

import static org.katas.SvgLocations.CAT1_DIR;
import static org.katas.SvgLocations.CAT2_DIR;
import static org.katas.SvgLocations.CAT3_DIR;
import static org.katas.SvgLocations.UNCLASSIFIED_DIR;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AssessUtils
{
	public static int countFiles(String dirName) {
		return countFiles(dirName, null);
	}

	public static int countFiles(String dirName, String rootDir) {
		if (rootDir != null && !rootDir.isBlank()) {
			dirName = rootDir + "/" + dirName.substring(dirName.indexOf("output"));
		}

		var directory = new File(dirName);
		if (!directory.exists()) {
			// directory.mkdirs();
			throw new IllegalStateException("Output dir does not exist: " + dirName);
		}
		var files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));
		return files != null ? files.length : 0;
	}

	public static String catToOutputDir(String category) {
		switch (category) {
			case "I":
				return CAT1_DIR;
			case "II":
				return CAT2_DIR;
			case "III":
				return CAT3_DIR;
			case "Unclassified":
			default:
				return UNCLASSIFIED_DIR;
		}
	}

	public static boolean foundInFiles(File[] files, String filename) {
		if (files == null || files.length == 0) {
			return false;
		}
		return Arrays.stream(files).anyMatch(file -> file.getName().equals(filename));
	}

	public static List<List<String>> getTestCases(String inputLocation) {
		var inputDir = new File(inputLocation);
		if (!inputDir.exists()) {
			//inputDir.mkdirs();
			throw new IllegalStateException("Input directory does not exist: " + inputDir);
		}
		var files = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));
		if (files == null || files.length == 0) {
			throw new IllegalStateException("Input directory is empty: " + inputDir);
		}

		List<List<String>> testCases = Arrays.stream(files)
				.map(file -> {
					String filename = file.getName();
					var chunks = filename.split("-");
					String cat = chunks[chunks.length - 1].split("\\.")[0].trim();
					return List.of(filename, cat);
				}).toList();

		if (testCases.isEmpty()) {
			throw new IllegalStateException("Input directory is empty: " + inputDir);
		}

		return testCases;
	}
}
