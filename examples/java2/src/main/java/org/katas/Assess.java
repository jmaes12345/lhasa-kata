package org.katas;

import static org.katas.AssessUtils.catToOutputDir;
import static org.katas.AssessUtils.countFiles;
import static org.katas.AssessUtils.foundInFiles;
import static org.katas.AssessUtils.getTestCases;
import static org.katas.SvgLocations.OUTPUT_DIRS_ALL;
import static org.katas.SvgLocations.CAT1_DIR;
import static org.katas.SvgLocations.CAT2_DIR;
import static org.katas.SvgLocations.CAT3_DIR;
import static org.katas.SvgLocations.INPUT_DIR;
import static org.katas.SvgLocations.ROOT_DIR;
import static org.katas.SvgLocations.UNCLASSIFIED_DIR;

import java.io.File;

public class Assess
{
	public static void main(String[] args) {
		String rootDir = args.length > 0 ? args[0] : null;
		if ("help".equals(rootDir) || "h".equals(rootDir) || "--help".equals(rootDir)) {
			System.out.println("""
		Run using Java 17
		To run summary test using default folders (C:/kata-svg), pass no arguments.
		To run summary test using custom folder, pass absolute path as first argument: java -jar svg-1.0-SNAPSHOT.jar C:/Test/svgClassificationFiles
		To run detailed test using default folders (C:/kata-svg): java -jar svg-1.0-SNAPSHOT.jar " " detail
		To run detailed test using custom folders: java -jar svg-1.0-SNAPSHOT.jar "C:/Test/kata files" detail
		""");
			rootDir = null;
		}

		var workingDir = rootDir != null && !rootDir.isBlank() ? rootDir : ROOT_DIR;
		System.out.println("Using 'input' and 'output' folders in parent directory: " + workingDir);

		String detailLevel = args.length > 1 ? args[1] : null;
		var summaryOnly = "summary".equalsIgnoreCase(detailLevel) || "s".equalsIgnoreCase(detailLevel);
		var detailMessage = summaryOnly ? "Running summary test..." :  "Running summary and detailed test...";
		System.out.println(detailMessage);

		var inputLocation = INPUT_DIR;
		if (rootDir != null && !rootDir.isBlank()) {
			inputLocation = rootDir + "/" + inputLocation.substring(inputLocation.indexOf("input"));
		}
		System.out.println("Reading from 'input' directory: " + inputLocation);
		var testCases = getTestCases(inputLocation);

		int expectedCat1 = testCases.stream().filter(tc -> "I".equals(tc.get(1))).toList().size();
		int expectedCat2 = testCases.stream().filter(tc -> "II".equals(tc.get(1))).toList().size();
		int expectedCat3 = testCases.stream().filter(tc -> "III".equals(tc.get(1))).toList().size();
		int expectedUnclassified = testCases.stream().filter(tc -> "unclassified".equals(tc.get(1))).toList().size();

		int foundCat1 = countFiles(CAT1_DIR, rootDir);
		int foundCat2 = countFiles(CAT2_DIR, rootDir);
		int foundCat3 = countFiles(CAT3_DIR, rootDir);
		int foundUnclassified = countFiles(UNCLASSIFIED_DIR, rootDir);

		boolean allCorrect = expectedCat1 == foundCat1 && expectedCat2 == foundCat2 && expectedCat3 == foundCat3 && expectedUnclassified == foundUnclassified;
		String message = allCorrect ? "/*** Great news, you have the correct number of files in each bucket! ***/" : "/*** Close but no carcinogenic dried leaf roll... ***/";
		System.out.printf("""

				%s
				Cat1 expected %d, found %d
				Cat2 expected %d, found %d
				Cat3 expected %d, found %d
				Unclassified expected %d, found %d
				""", message, expectedCat1, foundCat1, expectedCat2, foundCat2, expectedCat3, foundCat3, expectedUnclassified, foundUnclassified);

		System.out.println("\nRunning detailed tests...");
		testCases.forEach(tc -> detailTest(tc.get(0), tc.get(1)));
	}

	private static void detailTest(String svgFileName, String expectedCategory) {
		var outputDir = catToOutputDir(expectedCategory);
		var contents = new File(outputDir).listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));

		if (foundInFiles(contents, svgFileName)) {
			System.out.printf("SUCCESS: '%s' found in correct category: %s, dir: '%s'%n", svgFileName, expectedCategory, outputDir);
		} else {
			System.out.printf("FAILURE: '%s' not found in correct category: %s, dir: '%s'%n", svgFileName, expectedCategory, outputDir);
		}

		var wrongDirsFileFoundIn = OUTPUT_DIRS_ALL.stream()
				.filter(directory -> {
					var wrongDirContents = new File(directory).listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));
					return !outputDir.equals(directory) && foundInFiles(wrongDirContents, svgFileName);
				}).toList();

		if (!wrongDirsFileFoundIn.isEmpty()) {
			System.out.printf("'%s' had expected category: %s but was found in wrong dirs: '%s'%n", svgFileName, expectedCategory, wrongDirsFileFoundIn);
		}
	}
}