package org.katas;

import static org.katas.SvgLocations.CAT1_DIR;
import static org.katas.SvgLocations.CAT2_DIR;
import static org.katas.SvgLocations.CAT3_DIR;
import static org.katas.SvgLocations.INPUT_DIR;
import static org.katas.SvgLocations.OUTPUT_DIRS_ALL;
import static org.katas.SvgLocations.UNCLASSIFIED_DIR;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class SvgIfElseMain
{
	public static void main(String[] args) {
		checkDirectoriesExist();
		removeAllFilesFromOutputDirectory();

		var reader = SvgReader.getSvgReader();

		var files = SvgReader.getAllSvgs(INPUT_DIR);

		SvgClassifierIfElse cc = new SvgClassifierIfElse();
		files.forEach(file -> {
			System.out.println(file.getName());
			int category = cc.getCategory(reader.readSvgFile(file));
			outputFile(file, category);
		});

		String[] assessArgs = {};
		Assess.main(assessArgs);
	}

	private static void outputFile(File file, int category) {
		try {
			Files.copy(file.toPath(), categoryToOutputDir(category, file.getName()).toPath());
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static File categoryToOutputDir(int category, String filename) {
		if (false)
		{
			filename = insertCategoryIntoFileName(category, filename);
		}
		switch (category) {
			case 3: return new File(CAT3_DIR + "/" + filename);
			case 2: return new File(CAT2_DIR + "/" + filename);
			case 1: return new File(CAT1_DIR + "/" + filename);
			case -1:
			default: return new File(UNCLASSIFIED_DIR + "/" + filename);
		}
	}

	/**
	 * Use to add category to file name
	 */
	private static String insertCategoryIntoFileName(int category, String filename)
	{
		int lastDotIndex = filename.lastIndexOf('.');
		String nameWithoutExtension = filename.substring(0, lastDotIndex);
		String fileTypeSuffix = filename.substring(lastDotIndex);
		String categorySuffix = switch (category)
		{
			case 3 -> "-III";
			case 2 -> "-II";
			case 1 -> "-I";
			default -> "-Unclassified";
		};
		return nameWithoutExtension + categorySuffix + fileTypeSuffix;
	}

	private static void checkDirectoriesExist() {
		File inputDir = checkDirectoryExists(INPUT_DIR);
		checkDirContainsSvgs(inputDir);
		OUTPUT_DIRS_ALL.forEach(SvgIfElseMain::checkDirectoryExists);
	}

	private static void checkDirContainsSvgs(File directory) {
		var files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));
		if (files == null || files.length == 0) {
			throw new IllegalStateException("Directory contains no svgs: " + directory);
		}
	}

	private static File checkDirectoryExists(String dir) {
		var directory = new File(dir);
		if (!directory.exists()) {
//			directory.mkdirs();
			throw new IllegalStateException("Directory does not exist: " + directory);
		}
		return directory;
	}

	private static void removeAllFilesFromOutputDirectory() {
		OUTPUT_DIRS_ALL.forEach(dir -> {
			var file = new File(dir);
			if (file.exists()) {
				var contents = file.listFiles();
				if (contents != null) {
					Arrays.stream(contents).forEach(File::delete);
				}
			}
		});
	}
}