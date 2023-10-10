package org.lhasakata;

import static org.lhasakata.StartupUtils.INPUT_FOLDER;
import static org.lhasakata.StartupUtils.checkDirectoriesExist;
import static org.lhasakata.StartupUtils.removeAllFilesFromOutputDirectory;

import java.io.File;
import java.util.List;

import com.kitfox.svg.SVGElement;

public class ShapeCategoriser
{
	public static void main(String[] args)
	{
		checkDirectoriesExist();
		removeAllFilesFromOutputDirectory();
		
		var reader = SvgReader.getSvgReader();
		var files = SvgReader.getAllSvgs(INPUT_FOLDER);
		for (File file : files)
		{
			List<SVGElement> elements = reader.readSvgFile(file);

			// TODO categorise file
			int category = calculateCategory(elements);
			
			//TODO copy file to correct output folder
			copyFileToTargetCategoryFolder(file, category);
		}
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
}
