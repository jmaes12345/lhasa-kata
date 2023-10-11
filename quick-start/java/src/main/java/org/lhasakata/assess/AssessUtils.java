package org.lhasakata.assess;

import static org.lhasakata.StartupUtils.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AssessUtils
{
	public static int countFiles(String dirName) {
		var directory = new File(dirName);
		if (!directory.exists()) {
			// directory.mkdirs();
			throw new IllegalStateException("Output dir does not exist: " + dirName);
		}
		var files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));
		return files != null ? files.length : 0;
	}

	public static String catToOutputDir(String category) {
		var dir = switch (category) {
			case "I" -> CAT1_DIR;
			case "II" -> CAT2_DIR;
			case "III" -> CAT3_DIR;
			case "Unclassified" -> UNCLASSIFIED_DIR;
			default -> UNCLASSIFIED_DIR;
		};
		return ROOT_DIR + "/" + dir;
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

	public static void advancedStats(int[][] confusionMatrix) {
		double accuracy = 0.1;
		double precision = 0.2;
		double recall = 0.3;
		double f1 = 0.4;
		System.out.printf("""
				========================Evaluation Metrics========================
					# of classes:    3
					Accuracy:        %f
					Precision:       %f
					Recall:          %f
					F1 Score:        %f
					Precision, recall & F1: macro-averaged (equally weighted avg. of 3 classes)
				""", accuracy, precision, recall, f1);

		System.out.printf("""
				=========================Confusion Matrix=========================
							Cat1	Cat2	Cat3
				-----------------------------------
				Cat1	|	%d		%d		%d
				Cat2	|	%d		%d		%d
				Cat3	|	%d		%d		%d
				""", confusionMatrix[0][0], confusionMatrix[0][1], confusionMatrix[0][2],
				confusionMatrix[1][0], confusionMatrix[1][1], confusionMatrix[1][2],
				confusionMatrix[2][0], confusionMatrix[2][1], confusionMatrix[2][2]);
	}
}
