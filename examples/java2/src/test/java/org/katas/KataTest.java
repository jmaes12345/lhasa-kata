package org.katas;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.katas.AssessUtils.catToOutputDir;
import static org.katas.AssessUtils.countFiles;
import static org.katas.AssessUtils.foundInFiles;
import static org.katas.AssessUtils.getTestCases;
import static org.katas.SvgLocations.CAT1_DIR;
import static org.katas.SvgLocations.CAT2_DIR;
import static org.katas.SvgLocations.CAT3_DIR;
import static org.katas.SvgLocations.INPUT_DIR;
import static org.katas.SvgLocations.OUTPUT_DIRS_ALL;
import static org.katas.SvgLocations.UNCLASSIFIED_DIR;

import java.io.File;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class KataTest
{
	@Test
	void kataTestStats() {
		var testCases = getTestCases(INPUT_DIR);

		int expectedCat1 = testCases.stream().filter(tc -> "I".equals(tc.get(1))).toList().size();
		int expectedCat2 = testCases.stream().filter(tc -> "II".equals(tc.get(1))).toList().size();
		int expectedCat3 = testCases.stream().filter(tc -> "III".equals(tc.get(1))).toList().size();
		int expectedUnclassified = testCases.stream().filter(tc -> "unclassified".equals(tc.get(1))).toList().size();

		int foundCat1 = countFiles(CAT1_DIR);
		int foundCat2 = countFiles(CAT2_DIR);
		int foundCat3 = countFiles(CAT3_DIR);
		int foundUnclassified = countFiles(UNCLASSIFIED_DIR);

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
		var testCases = getTestCases(INPUT_DIR);
		return testCases.stream().map(tc -> Arguments.of(tc.get(0), tc.get(1)));
	}
}
