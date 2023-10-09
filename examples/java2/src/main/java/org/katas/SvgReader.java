package org.katas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGUniverse;

public class SvgReader
{
	private static SvgReader INSTANCE;
	private final SVGUniverse universe;
	private Map<String, List<SVGElement>> cachedSvgContentsByFile = new HashMap<>();
	private SvgReader() {
		this.universe = new SVGUniverse();
	}

	public static SvgReader getSvgReader() {
		if (INSTANCE == null) {
			INSTANCE = new SvgReader();
		}
		return INSTANCE;
	}

	public List<SVGElement> readSvgFile(File file) {
		var svgContents = cachedSvgContentsByFile.computeIfAbsent(file.getAbsolutePath(), key -> loadSvgFile(file));
		return svgContents;
	}

	private List<SVGElement> loadSvgFileAsResource(String filePath) {
		try (var is = getClass().getResourceAsStream(filePath))
		{
			var svgUri = universe.loadSVG(is, filePath);
			var el = universe.getElement(svgUri);
			var els = el.getChildren(null);
			return els;
		}
		catch (IOException ioe) {
			System.out.println("Could not load svg: " + filePath);
			return null;
		}
	}

	private List<SVGElement> loadSvgFile(File file) {
		try (var is = new FileInputStream(file))
		{
			var svgUri = universe.loadSVG(is, file.getName());
			var el = universe.getElement(svgUri);
			var els = el.getChildren(null);
			return els;
		}
		catch (IOException ioe) {
			System.out.println("Could not load svg: " + file.getAbsolutePath());
			return null;
		}
	}

	public static List<File> getAllSvgs(String inputLocation) {
		var inputDir = new File(inputLocation);
		if (!inputDir.exists()) {
			//inputDir.mkdirs();
			throw new IllegalStateException("Input directory does not exist: " + inputDir);
		}
		var files = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".svg"));
		if (files == null || files.length == 0) {
			throw new IllegalStateException("Input directory is empty: " + inputDir);
		}
		return List.of(files);
	}
}
