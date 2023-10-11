package org.lhasakata.assess;

import static org.lhasakata.StartupUtils.ROOT_DIR;
import static org.lhasakata.assess.AssessUtils.catToOutputDir;
import static org.lhasakata.assess.AssessUtils.countFiles;
import static org.lhasakata.assess.AssessUtils.foundInFiles;
import static org.lhasakata.assess.AssessUtils.getTestCases;
import static org.lhasakata.StartupUtils.OUTPUT_DIRS_ALL;
import static org.lhasakata.StartupUtils.CAT1_DIR;
import static org.lhasakata.StartupUtils.CAT2_DIR;
import static org.lhasakata.StartupUtils.CAT3_DIR;
import static org.lhasakata.StartupUtils.INPUT_FOLDER;
import static org.lhasakata.StartupUtils.UNCLASSIFIED_DIR;

import java.io.File;

import org.lhasakata.StartupUtils;

public class Assess
{
	public static void assess(String rootDir) {
		System.out.println();
		System.out.println("/**** Tests ***/");
		var workingDir = rootDir != null && !rootDir.isBlank() ? rootDir : StartupUtils.ROOT_DIR;
		ROOT_DIR = workingDir;
		System.out.println("Running tests using 'input' and 'output' folders in parent directory: " + workingDir);

		var inputLocation = workingDir + "/" + INPUT_FOLDER;
		System.out.println("Reading from 'input' directory: " + inputLocation);
		var testCases = getTestCases(inputLocation);

		int expectedCat1 = testCases.stream().filter(tc -> "I".equals(tc.get(1))).toList().size();
		int expectedCat2 = testCases.stream().filter(tc -> "II".equals(tc.get(1))).toList().size();
		int expectedCat3 = testCases.stream().filter(tc -> "III".equals(tc.get(1))).toList().size();
		int expectedUnclassified = testCases.stream().filter(tc -> "unclassified".equals(tc.get(1))).toList().size();

		int foundCat1 = countFiles(workingDir + "/" + CAT1_DIR);
		int foundCat2 = countFiles(workingDir + "/" + CAT2_DIR);
		int foundCat3 = countFiles(workingDir + "/" + CAT3_DIR);
		int foundUnclassified = countFiles(workingDir + "/" + UNCLASSIFIED_DIR);

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
		var outputDir = ROOT_DIR + "/" + catToOutputDir(expectedCategory);
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