package org.katas.graph;

import static org.katas.SvgLocations.INPUT_DIR;
import static org.katas.SvgReader.getAllSvgs;

import org.junit.jupiter.api.Test;
import org.katas.SvgReader;

class DecisionBuilderTest
{

	@Test
	void testSvgClassificationByGraphModel() {
		var reader = SvgReader.getSvgReader();
		var files = getAllSvgs(INPUT_DIR);

		files.forEach(file -> {
			System.out.println(file.getName());
			int category = DecisionBuilder.testSvg(reader.readSvgFile(file));
			System.out.printf("File: %s \nCategory assigned: %d\n" , file.getName(), category);
		});
	}
}