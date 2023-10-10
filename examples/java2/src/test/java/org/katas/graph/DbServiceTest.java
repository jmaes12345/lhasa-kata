package org.katas.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.katas.graph.SvgConstants.any;
import static org.katas.graph.SvgConstants.blue;
import static org.katas.graph.SvgConstants.category;
import static org.katas.graph.SvgConstants.circle;
import static org.katas.graph.SvgConstants.color;
import static org.katas.graph.SvgConstants.decision;
import static org.katas.graph.SvgConstants.edge_No;
import static org.katas.graph.SvgConstants.edge_Yes;
import static org.katas.graph.SvgConstants.greaterThan;
import static org.katas.graph.SvgConstants.green;
import static org.katas.graph.SvgConstants.name;
import static org.katas.graph.SvgConstants.numericComparator;
import static org.katas.graph.SvgConstants.only;
import static org.katas.graph.SvgConstants.outcome;
import static org.katas.graph.SvgConstants.radius;
import static org.katas.graph.SvgConstants.rectangle;
import static org.katas.graph.SvgConstants.red;
import static org.katas.graph.SvgConstants.selection;
import static org.katas.graph.SvgConstants.shape;
import static org.katas.graph.SvgConstants.text;
import static org.katas.graph.SvgConstants.textContains;

import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.jupiter.api.Test;

class DbServiceTest
{
	@Test
	void checkConnection() {
		try (var dbService = DbService.getInstance()) {
			var db = dbService.getDb();
			var name = db.database().getName();
			assertEquals("kata1", name, "Create this database manually in remote OrientDb studio");
		}
	}

	@Test
	void buildSvgDecisionTree() {
		try (var dbService = DbService.getInstance()) {
			var db = dbService.getDb();
			var dbName = db.database().getName();
			assertEquals("kata1", dbName);

			var start = db.addVertex(SvgConstants.start);
			start.property("name", "svg1");

			var anyRedCircles = db.addVertex(decision);
			start.addEdge("entry", anyRedCircles);

			anyRedCircles.property("name", "Any red circles");
			anyRedCircles.property(selection, any);
			anyRedCircles.property(color, red);
			anyRedCircles.property(shape, circle);

			var anyRedCirclesYes = db.addVertex(outcome);
			anyRedCircles.addEdge(edge_Yes, anyRedCirclesYes);
			anyRedCirclesYes.property(category, 3);

			var onlyBlueCircles = db.addVertex(decision);
			anyRedCircles.addEdge(edge_No, onlyBlueCircles);
			onlyBlueCircles.property(name, "Only blue circles");
			onlyBlueCircles.property(selection, only);
			onlyBlueCircles.property(color, blue);
			onlyBlueCircles.property(shape, circle);

			makeOnlyBlueCirclesFalseArm(db, onlyBlueCircles);
			makeOnlyBlueCirclesTrueArm(db, onlyBlueCircles);

			db.commit();
		}
	}

	private void makeOnlyBlueCirclesFalseArm(OrientGraph db, Vertex onlyBlueCircles) {
		var anyRectangles = db.addVertex(decision);
		anyRectangles.property(name, "Any rectangles or squares");
		onlyBlueCircles.addEdge(edge_No, anyRectangles);
		anyRectangles.property(selection, any);
		anyRectangles.property(shape, rectangle);

		anyRectangles_True(db, anyRectangles);
		anyRectangles_False(db, anyRectangles);
	}

	private void anyRectangles_True(OrientGraph db, Vertex anyRectangles) {
		var anyText = anyTextNode(db);
		anyRectangles.addEdge(edge_Yes, anyText);

		var anyGreenRectangle = db.addVertex(decision);
		anyText.addEdge(edge_Yes, anyGreenRectangle);
		anyGreenRectangle.property(name, "Any green rectangle");
		anyGreenRectangle.property(selection, any);
		anyGreenRectangle.property(shape, rectangle);
		anyGreenRectangle.property(color, green);

		var anyGreenRectangleYes = db.addVertex(outcome);
		anyGreenRectangle.addEdge(edge_Yes, anyGreenRectangleYes);
		anyGreenRectangleYes.property(category, 1);
	}

	private void anyRectangles_False(OrientGraph db, Vertex anyRectangles) {
		var anyText = anyTextNode(db);
		anyRectangles.addEdge(edge_No, anyText);

		var anyTextFalse = db.addVertex(outcome);
		anyText.addEdge(edge_No, anyTextFalse);
		anyTextFalse.property(category, 2);

		var textContainsLhasa = db.addVertex(decision);
		textContainsLhasa.property("name", "Text containing the character sequence 'lhasa'");
		anyText.addEdge(edge_Yes, textContainsLhasa);
		textContainsLhasa.property(textContains, "Lhasa");

		var lhasaTextTrue = db.addVertex(outcome);
		textContainsLhasa.addEdge(edge_Yes, lhasaTextTrue);
		lhasaTextTrue.property(category, 1);

		var lhasaTextFalse = db.addVertex(outcome);
		textContainsLhasa.addEdge(edge_No, lhasaTextFalse);
		lhasaTextFalse.property(category, 3);
	}

	private Vertex anyTextNode(OrientGraph db) {
		var anyText = db.addVertex(decision);
		anyText.property(name, "Any text");
		anyText.property(selection, any);
		anyText.property(shape, text);
		return anyText;
	}

	public static void makeOnlyBlueCirclesTrueArm(OrientGraph db, Vertex onlyBlueCircles) {
		var largerThan50Diameter = db.addVertex(decision);
		largerThan50Diameter.property(name, "larger than 50 diameter");
		onlyBlueCircles.addEdge(edge_Yes, largerThan50Diameter);
		largerThan50Diameter.property(selection, any);
		largerThan50Diameter.property(radius, 50);
		largerThan50Diameter.property(numericComparator, greaterThan);

		var o50DiameterYes = db.addVertex(outcome).property(category, 3).element();
		largerThan50Diameter.addEdge(edge_No, o50DiameterYes);

		var largerThan100Diameter = db.addVertex(decision);
		largerThan50Diameter.addEdge(edge_Yes, largerThan100Diameter);
		largerThan100Diameter.property(name, "larger than 100 diameter");
		largerThan100Diameter.property(selection, any);
		largerThan100Diameter.property(radius, 100);
		largerThan100Diameter.property(numericComparator, greaterThan);

		var largerThan100DiameterYes = db.addVertex(outcome).property(category, 1).element();
		largerThan100Diameter.addEdge(edge_Yes, largerThan100DiameterYes);

		var largerThan100DiameterNo = db.addVertex(outcome).property(category, 2).element();
		largerThan100Diameter.addEdge(edge_No, largerThan100DiameterNo);
	}
}