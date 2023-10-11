package org.lhasakata;

import static org.junit.jupiter.api.Assertions.*;
import static org.lhasakata.assess.AssessUtils.catToOutputDir;
import static org.lhasakata.assess.AssessUtils.countFiles;
import static org.lhasakata.assess.AssessUtils.foundInFiles;
import static org.lhasakata.assess.AssessUtils.getTestCases;
import static org.lhasakata.StartupUtils.*;

import java.io.File;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lhasakata.assess.AssessUtils;

/**
 * Copyright © 2023 Lhasa Limited
 * File created: 11/10/2023 by JamesB
 * Creator : JamesB
 * Version : $$Id$$
 */
@Disabled
public class ShapeCategoriserTest
{
	@Test
	void kataTestStats() {
		var testCases = getTestCases(ROOT_DIR + "/" + INPUT_FOLDER);

		int expectedCat1 = testCases.stream().filter(tc -> "I".equals(tc.get(1))).toList().size();
		int expectedCat2 = testCases.stream().filter(tc -> "II".equals(tc.get(1))).toList().size();
		int expectedCat3 = testCases.stream().filter(tc -> "III".equals(tc.get(1))).toList().size();
		int expectedUnclassified = testCases.stream().filter(tc -> "unclassified".equals(tc.get(1))).toList().size();

		int foundCat1 = countFiles(ROOT_DIR + "/" + CAT1_DIR);
		int foundCat2 = countFiles(ROOT_DIR + "/" + CAT2_DIR);
		int foundCat3 = countFiles(ROOT_DIR + "/" + CAT3_DIR);
		int foundUnclassified = countFiles(ROOT_DIR + "/" + UNCLASSIFIED_DIR);

		boolean allCorrect = expectedCat1 == foundCat1 && expectedCat2 == foundCat2 && expectedCat3 == foundCat3 && expectedUnclassified == foundUnclassified;
		assertTrue(allCorrect, """
				Close but no carcinogenic dried leaf roll:
				Cat1 expected %d, found %d
				Cat2 expected %d, found %d
				Cat3 expected %d, found %d
				Unclassified expected %d, found %d
				""".formatted(expectedCat1, foundCat1, expectedCat2, foundCat2, expectedCat3, foundCat3, expectedUnclassified, foundUnclassified));
	}

	@ParameterizedTest(name = "{0}: Cat: {1}")
	@MethodSource
	void detailTest(String svgFileName, String expectedCategory) {
		var outputDir = catToOutputDir(expectedCategory);
		var contents = new File(outputDir).listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));

		assertTrue(foundInFiles(contents, svgFileName), String.format("File '%s' not found in correct category: %s, dir: '%s'", svgFileName, expectedCategory, outputDir));

		var wrongDirsFileFoundIn = OUTPUT_DIRS_ALL.stream()
				.filter(directory -> {
					var wrongDirContents = new File(directory).listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));
					return !outputDir.equals(directory) && foundInFiles(wrongDirContents, svgFileName);
				}).toList();
		assertTrue(wrongDirsFileFoundIn.isEmpty(), String.format("File '%s' expected category: %s but found in wrong dirs: '%s'", svgFileName, expectedCategory, wrongDirsFileFoundIn.toString()));
	}

	private static Stream<Arguments> detailTest() {
		var testCases = getTestCases(ROOT_DIR + "/" + INPUT_FOLDER);
		return testCases.stream().map(tc -> Arguments.of(tc.get(0), tc.get(1)));
	}

	@Test
	void advancedStats() {
		var confusionMatrix = new int[3][3];
		confusionMatrix[0][0] = 1;
		confusionMatrix[0][1] = 2;
		confusionMatrix[0][2] = 3;
		confusionMatrix[1][0] = 4;
		confusionMatrix[1][1] = 5;
		confusionMatrix[1][2] = 6;
		confusionMatrix[2][0] = 7;
		confusionMatrix[2][1] = 8;
		confusionMatrix[2][2] = 9;
		AssessUtils.advancedStats(confusionMatrix);
	}
}