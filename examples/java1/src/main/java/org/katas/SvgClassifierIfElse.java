package org.katas;

import java.awt.*;
import java.util.List;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Rect;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.Text;

public class SvgClassifierIfElse
{
	public SvgClassifierIfElse() {
	}

	public int getCategory(List<SVGElement> elements) {
		if (anyRedCircles(elements)) {
			return 3;
		}

		if (onlyBlueCircles(elements)) {
			System.out.println("only blue");
			if (largerThanDiameter(elements, 50, false)) {
				System.out.println("larger than 50");
				if (largerThanDiameter(elements, 100, false)) {
					System.out.println("larger than 100");
					return 1;
				}
				else {
					System.out.println("Smaller than 100");
					return 2;
				}
			}
			else {
				System.out.println("Smaller than 50");
				return 3;
			}
		}
		else {
			System.out.println("Not only blue circles");
			if (anyRectangles(elements)) {
				System.out.println("Rectangle exists");
				if (anyText(elements)) {
					System.out.println("Text exists");
//					if (rectangleWithColour(elements, Color.GREEN)) {
					if (rectangleWithColour(elements, new Color(0x008000))) {
						System.out.println("Green rectangle");
						return 1;
					}
					else {
						System.out.println("No Green rectangle");
						// TODO question this one
						if (elements.size() > 1) {
							System.out.println("More than one shape");
							return 1;
						}
						else {
							System.out.println("One shape");
							if (anyStraightLine(elements)) {
								System.out.println("Straight line");
							}
							else {
								System.out.println("No straight line");
								if (anyCurvedLines(elements)) {
									System.out.println("Curved line");
								}
								else {
									System.out.println("No curved lines");
								}
							}
						}
					}
				}
				else {
					System.out.println("No text");
					if (elements.size() > 1) {
						// Any straight lines
					}
					else {
						System.out.println("One shape, is a rectangle");
						return 1;
					}
				}
			}
			else {
				System.out.println("No rectangles");
				if (anyText(elements)) {
					if (textContains(elements, "lhasa")) {
						return 1;
					} else {
						return 3;
					}
				}
				else {
					return 2;
				}
			}
		}
		return -1;
	}

	private boolean anyCurvedLines(List<SVGElement> elements) {
		return true;
	}

	private boolean anyStraightLine(List<SVGElement> elements) {
		return true;
	}

	private boolean rectangleWithColour(List<SVGElement> elements, Color colour) {
		for (SVGElement o : elements) {
			if (o instanceof Rect && colour.equals(o.getPresAbsolute("fill").getColorValue())) {
				return true;
			}
		}
		return false;
	}

	private boolean textContains(List<SVGElement> elements, String searchText) {
		for (SVGElement o : elements) {
			if (o instanceof Text text) {
				var content = text.getContent();
				if (content != null && !content.isEmpty()) {
					if (content.stream().anyMatch(t -> t != null && t.toString().contains(searchText))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean anyText(List<SVGElement> elements) {
		for (SVGElement o : elements) {
			if (o instanceof Text) {
				return true;
			}
		}
		return false;
	}

	private boolean anyRectangles(List<SVGElement> elements) {
		for (SVGElement o : elements) {
			if (o instanceof Rect) {
				return true;
			}
		}
		return false;
	}

	private boolean anyRedCircles(List<SVGElement> objects) {
		for (SVGElement o : objects) {
			if (o instanceof Circle && Color.RED.equals(o.getPresAbsolute("fill").getColorValue())) {
				return true;
			}
		}
		return false;
	}

	private boolean onlyBlueCircles(List<SVGElement> objects) {
		for (SVGElement o : objects) {
			if (!(o instanceof Circle)) {
				return false;
			}

			if (!Color.BLUE.equals(o.getPresAbsolute("fill").getColorValue())) {
				return false;
			}
		}
		return true;
	}

	private boolean largerThanDiameter(List<SVGElement> objects, int diameterCeiling, boolean requireAll) {
		boolean match = false;
		for (SVGElement o : objects) {
			if (o instanceof Circle) {
				var diameter = o.getPresAbsolute("r").getIntValue();
				if (requireAll && diameter <= diameterCeiling) {
					return false;
				}
				else if (diameter > diameterCeiling) {
					match = true;
					if (!requireAll) return true;
				}
			}
			else {
				if (requireAll)
					return false;
			}
		}
		return match;
	}
}
