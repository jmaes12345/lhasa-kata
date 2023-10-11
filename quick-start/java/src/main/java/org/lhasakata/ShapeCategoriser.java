package org.lhasakata;

import static org.lhasakata.StartupUtils.INPUT_FOLDER;
import static org.lhasakata.StartupUtils.ROOT_DIR;
import static org.lhasakata.StartupUtils.checkDirectoriesExist;
import static org.lhasakata.StartupUtils.removeAllFilesFromOutputDirectory;

import java.io.File;
import java.util.List;

import org.lhasakata.assess.Assess;

import com.kitfox.svg.SVGElement;

public class ShapeCategoriser
{
	public static void main(String[] args)
	{
		String arg0 = args.length > 0 ? args[0] : null;
		String arg1 = args.length > 1 ? args[1] : null;
		help(arg0);
		testOnly(arg0, arg1);
		String workingDir = setupWorkingDir(arg0);
		checkDirectoriesExist();
		removeAllFilesFromOutputDirectory();
		
		var reader = SvgReader.getSvgReader();
		var files = SvgReader.getAllSvgs(workingDir + "/" + INPUT_FOLDER);
		for (File file : files)
		{
			List<SVGElement> elements = reader.readSvgFile(file);

			// TODO categorise file
			int category = calculateCategory(elements);
			
			//TODO copy file to correct output folder
			copyFileToTargetCategoryFolder(file, category);
		}

		// Run tests
		Assess.assess(workingDir);
	}

	static int calculateCategory(List<SVGElement> elements)
	{
		//TODO implement workflow
		return -1;
	}

	static void copyFileToTargetCategoryFolder(File file, int category)
	{
		//TODO move your files to the desired output directory,
		//  based on the calculated category
	}




	private static String setupWorkingDir(String arg0) {
		var workingDir = arg0 != null && !arg0.isBlank() ? arg0 : ROOT_DIR;
		ROOT_DIR = workingDir;
		System.out.println("Using 'input' and 'output' folders in parent directory: " + workingDir);
		return workingDir;
	}

	private static void help(String arg0) {
		if ("help".equals(arg0) || "h".equals(arg0) || "--help".equals(arg0)) {
			System.out.println("""
		Run using Java 17
		To run using default folder (C:/kata-svg), pass no arguments.
		To run using custom folder, pass absolute path as first argument: java -jar java-quickstart-1.0-SNAPSHOT.jar C:/Test/svgClassificationFiles
		To run only test using default folders (C:/kata-svg): java -jar java-quickstart-1.0-SNAPSHOT.jar test
		To run detailed test using custom folders: java -jar java-quickstart-1.0-SNAPSHOT.jar "C:/Test/kata files" test
		""");
			System.exit(0);
		}
	}

	private static void testOnly(String arg0, String arg1) {
		if ("test".equals(arg0)) {
			System.out.println("Running tests only");
			Assess.assess(null);
			System.exit(0);
		}
		else if ("test".equals(arg1)) {
			System.out.println("Running tests only");
			Assess.assess(arg0);
			System.exit(0);
		}
	}
}
