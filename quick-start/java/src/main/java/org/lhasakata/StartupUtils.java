package org.lhasakata;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class StartupUtils
{
	static final String ROOT_DIR = "C:/kata-svg";
	static final String INPUT_FOLDER = ROOT_DIR + "/input";
	static final String OUTPUT_DIR = ROOT_DIR + "/" + "output";
	static final String CAT1_DIR = OUTPUT_DIR + "/" + "Cat1";
	static final String CAT2_DIR = OUTPUT_DIR + "/" + "Cat2";
	static final String CAT3_DIR = OUTPUT_DIR + "/" + "Cat3";
	static final String UNCLASSIFIED_DIR = OUTPUT_DIR + "/" + "Unclassified";
	static final List<String> OUTPUT_DIRS_ALL = List.of(CAT1_DIR, CAT2_DIR, CAT3_DIR, UNCLASSIFIED_DIR);
	
	private StartupUtils() {}

	static void checkDirectoriesExist() {
		File inputDir = checkDirectoryExists(INPUT_FOLDER);
		checkDirContainsSvgs(inputDir);
	}

	static void checkDirContainsSvgs(File directory) {
		var files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));
		if (files == null || files.length == 0) {
			throw new IllegalStateException("Directory contains no svgs: " + directory);
		}
	}

	static File checkDirectoryExists(String dir) {
		var directory = new File(dir);
		if (!directory.exists()) {
//			directory.mkdirs();
			throw new IllegalStateException("Directory does not exist: " + directory);
		}
		return directory;
	}

	static void removeAllFilesFromOutputDirectory() {
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
