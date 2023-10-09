package org.katas;

import java.util.List;

public class SvgLocations
{
	public static final String ROOT_DIR = "C:/kata-svg";
	public static final String INPUT_DIR = ROOT_DIR + "/" + "input";
	public static final String OUTPUT_DIR = ROOT_DIR + "/" + "output";
	public static final String CAT1_DIR = OUTPUT_DIR + "/" + "Cat1";
	public static final String CAT2_DIR = OUTPUT_DIR + "/" + "Cat2";
	public static final String CAT3_DIR = OUTPUT_DIR + "/" + "Cat3";
	public static final String UNCLASSIFIED_DIR = OUTPUT_DIR + "/" + "Unclassified";
	public static final List<String> OUTPUT_DIRS_ALL = List.of(CAT1_DIR, CAT2_DIR, CAT3_DIR, UNCLASSIFIED_DIR);
}
