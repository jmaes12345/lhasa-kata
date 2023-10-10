package org.katas.graph;

import static org.katas.graph.SvgConstants.category;
import static org.katas.graph.SvgConstants.edge_No;
import static org.katas.graph.SvgConstants.edge_Yes;
import static org.katas.graph.SvgConstants.outcome;
import static org.katas.graph.SvgConstants.start;
import static org.katas.graph.SvgConstants.all;
import static org.katas.graph.SvgConstants.any;
import static org.katas.graph.SvgConstants.circle;
import static org.katas.graph.SvgConstants.equal;
import static org.katas.graph.SvgConstants.greaterThan;
import static org.katas.graph.SvgConstants.greaterThanOrEqual;
import static org.katas.graph.SvgConstants.lessThan;
import static org.katas.graph.SvgConstants.lessThanOrEqual;
import static org.katas.graph.SvgConstants.line;
import static org.katas.graph.SvgConstants.numericComparator;
import static org.katas.graph.SvgConstants.only;
import static org.katas.graph.SvgConstants.radius;
import static org.katas.graph.SvgConstants.rectangle;
import static org.katas.graph.SvgConstants.svg1;
import static org.katas.graph.SvgConstants.text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Line;
import com.kitfox.svg.Rect;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.ShapeElement;
import com.kitfox.svg.Text;

public class DecisionBuilder
{
	public static int testSvg(List<SVGElement> elements) {
		try {
			return testWorkflow(elements, svg1);
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static int testWorkflow(List<SVGElement> elements, String startPoint) {
		try (var dbService = DbService.getInstance()) {
			var db = dbService.getDb();
			var traversal = db.traversal();
			var startV = traversal.V().hasLabel(start).next();
			var next = startV.vertices(Direction.OUT, "entry").next();
			int category = testForOutcome(elements, next);
			return category;
		}
	}

	public static int testForOutcome(List<SVGElement> elements, Vertex v) {
		try {
			var categoryVertex = outcome.equals(v.label());
			if (categoryVertex) {
				return (int) v.property(category).value();
			}
			else {
				// Check for decision required?
				boolean match = testAllElements(elements, v);
				Vertex next;
				if (match) {
					next = v.vertices(Direction.OUT, edge_Yes).next();
				}
				else {
					next = v.vertices(Direction.OUT, edge_No).next();
				}

				if (next == null) {
					System.out.println("ERROR: Found end of traversal without an outcome");
					return -1;
				}

				return testForOutcome(elements, next);
			}
		}
		catch (Exception e) {
			System.out.println("ERROR: Found end of traversal without an outcome");
			e.printStackTrace();
			return -1;
		}
	}

	public static boolean testAllElements(List<SVGElement> elements, Vertex v) {
		// Select predicate based on vertex
		List<BiPredicate<SVGElement, Vertex>> predicate = getPredicates(v);

		// Select logic based on vertex & run predicate
		var selectionType = v.property(SvgConstants.selection);
		if (selectionType.isPresent()) {
			String selection = (String) selectionType.value();
			return switch (selection) {
				case any -> testForAny(elements, v, predicate);
				case only -> testForOnly(elements, v, predicate);
				case all -> testForAll(elements, v, predicate);
				default -> throw new IllegalStateException("Unexpected value: " + selection);
			};
		}

		return false;
	}

	private static List<BiPredicate<SVGElement, Vertex>> getPredicates(Vertex v) {
		var predicates = new ArrayList<BiPredicate<SVGElement, Vertex>>();
		if (v.property(SvgConstants.shape).isPresent()) {
			predicates.add(DecisionBuilder::testShape);
		}

		if (v.property(SvgConstants.color).isPresent()) {
			predicates.add(DecisionBuilder::testColor);
		}

		if (v.property(radius).isPresent()) {
			predicates.add(DecisionBuilder::testRadius);
		}

		return predicates;
	}

	private static boolean testRadius(SVGElement svgElement, Vertex v) {
		var svgRadius = svgElement.getPresAbsolute("r").getIntValue();
		var compareRadius = (Integer) v.property(radius).value();

		return compareValues(v, svgRadius, compareRadius);
	}

	private static boolean compareValues(Vertex v, int inputValue, int compareValue) {
		var comparator = (String) v.property(numericComparator).value();

		return switch (comparator) {
			case greaterThan -> inputValue > compareValue;
			case greaterThanOrEqual -> inputValue >= compareValue;
			case lessThan -> inputValue < compareValue;
			case lessThanOrEqual -> inputValue <= compareValue;
			case equal -> inputValue == compareValue;
			default -> false;
		};
	}

	private static boolean testForAny(List<SVGElement> elements, Vertex v, List<BiPredicate<SVGElement, Vertex>> predicates) {
		for (SVGElement element : elements) {
			if (predicates.stream().allMatch(predicate -> predicate.test(element, v))) {
				return true;
			}
		}
		return false;
	}

	private static boolean testForOnly(List<SVGElement> elements, Vertex v, List<BiPredicate<SVGElement, Vertex>> predicates) {
		for (SVGElement element : elements) {
			if (!predicates.stream().allMatch(predicate -> predicate.test(element, v))) {
				return false;
			}
		}
		return true;
	}

	public static boolean testForAll(List<SVGElement> elements, Vertex v, List<BiPredicate<SVGElement, Vertex>> predicate) {
		// TODO - Not sure if we need this
		return false;
	}


	private static boolean testColor(SVGElement svgElement, Vertex v) {
		String dbColour = (String) v.property(SvgConstants.color).value();
		Color expectedColor = mapColor(dbColour);
		Color svgColor = svgElement.getPresAbsolute("fill").getColorValue();
		return expectedColor.equals(svgColor);
	}

	public static Color mapColor(String dbString) {
		return switch (dbString) {
			case "blue" -> Color.BLUE;
			case "green" -> new Color(0, 128, 0);
			case "red" -> Color.RED;
			default -> throw new IllegalStateException("Unexpected value: " + dbString);
		};
	}

	public static boolean testShape(SVGElement el, Vertex v) {
		var shape = v.property(SvgConstants.shape);
		if (shape.isPresent()) {
			var svgShapeClassName = el.getClass().getName();
			var expectedShapeClassName = getElementClass((String) shape.value());
			return svgShapeClassName.equals(expectedShapeClassName);
		}
		return false;
	}

	public static String getElementClass(String shape) {
		Class<? extends ShapeElement> clazz = switch (shape) {
			case circle -> Circle.class;
			case rectangle -> Rect.class;
			case text -> Text.class;
			case line -> Line.class;
			default -> throw new IllegalStateException("Unexpected shape");
		};
		return clazz.getName();
	}
}
