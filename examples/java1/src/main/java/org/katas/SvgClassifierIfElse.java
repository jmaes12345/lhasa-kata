package org.katas;

import java.awt.*;
import java.util.List;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Ellipse;
import com.kitfox.svg.Line;
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
						return moreThanOneShapeTree(elements);
					}
				}
				else {
					System.out.println("No text");
					return moreThanOneShapeTree(elements);
				}
			}
			else {
				System.out.println("No rectangles");
				if (anyText(elements)) {
					System.out.println("Text exists");
					if (textContains(elements, "lhasa")) {
						return 1;
					} else {
						return 3;
					}
				}
				else {
					System.out.println("No text exists");
					return 2;
				}
			}
		}
	}

	private int moreThanOneShapeTree(List<SVGElement> elements) {
		if (elements.size() > 1) {
			System.out.println("More than one shape");
			if (anyStraightLine(elements)) {
				System.out.println("Straight line");
				if (allLineLongerThan(100, elements)) {
					return 2;
				}
				else {
					return 3;
				}
			}
			else {
				System.out.println("No straight line");
				if (anyEllipse(elements)) {
					System.out.println("Ellipse exists");
					return anyEllipseBranch(elements);
				}
				else {
					System.out.println("No ellipse");
					return noEllipseBranch(elements);
				}
			}
		}
		else {
			System.out.println("One shape, is a rectangle");
			return 1;
		}
	}

	private boolean allLineLongerThan(int length, List<SVGElement> elements) {
		for (SVGElement o : elements) {
			if (o instanceof Line line) {
				var x1 = line.getPresAbsolute("x1").getIntValue();
				var x2 = line.getPresAbsolute("x2").getIntValue();
				int xDelta = x2 - x1;
				var y1 = line.getPresAbsolute("y1").getIntValue();
				var y2 = line.getPresAbsolute("y2").getIntValue();
				int yDelta = y2 - y1;
				var svgLength = Math.hypot(xDelta, yDelta);
				if (svgLength <= length) {
					return false;
				}
			}
		}
		return true;
	}

	private int noEllipseBranch(List<SVGElement> elements) {
		if (anyShapeOpacityGreaterThan(0.5, elements)) {
			if (elements.size() > 5) {
				return 1;
			} else {
				return 2;
			}
		}
		else {
			return 3;
		}
	}

	private boolean anyShapeOpacityGreaterThan(double opacity, List<SVGElement> elements) {
		for (SVGElement o : elements) {
			var svgOpacity = o.getPresAbsolute("opacity");
			if (svgOpacity != null)
			{
				var doubleSvgOpacity = svgOpacity.getDoubleValue();
				if (doubleSvgOpacity > opacity)
				{
					return true;
				}
			}
		}
		return false;
	}

	private int anyEllipseBranch(List<SVGElement> elements) {
		if (anyEllipseHeightGreaterThanOrEqual(50, elements)) {
			return 3;
		}
		else {
			if (anyRectangleWithAreaGreaterThanOrEqual(300, elements)) {
				return 1;
			}
			else {
				if (elements.size() > 5) {
					return 2;
				} else {
					return 3;
				}
			}
		}
	}

	private boolean anyRectangleWithAreaGreaterThanOrEqual(int area, List<SVGElement> elements) {
		for (SVGElement o : elements) {
			if (o instanceof Rect rect) {
				var width = rect.getPresAbsolute("width").getIntValue();
				var height = rect.getPresAbsolute("height").getIntValue();
				var svgArea = width * height;
				return svgArea >= area;
			}
		}
		return false;
	}

	private boolean anyEllipseHeightGreaterThanOrEqual(int height, List<SVGElement> elements) {
		for (SVGElement o : elements) {
			if (o instanceof Ellipse ellipse) {
				var svgYRadius = ellipse.getPresAbsolute("ry").getIntValue();
				var svgHeight = svgYRadius * 2;
				return svgHeight >= height;
			}
		}
		return false;
	}

	private boolean anyEllipse(List<SVGElement> elements) {
		for (SVGElement o : elements) {
			if (o instanceof Ellipse) {
				return true;
			}
		}
		return false;
	}

	private boolean anyStraightLine(List<SVGElement> elements) {
		for (SVGElement o : elements) {
			if (o instanceof Line) {
				return true;
			}
		}
		return false;
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
