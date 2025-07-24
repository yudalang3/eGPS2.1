package module.heatmap.eheatmap.rectpainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import evoltree.struct.EvolNode;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.tree.ClusterParameter;
import module.heatmap.eheatmap.tree.GraphcsNode;
import module.heatmap.eheatmap.tree.LineObj;
import module.heatmap.eheatmap.tree.TreeUtility;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;

public class RectColTreePainter {

	private int globalLeafIndex;
	private double cellWidth;
	final TreeUtility treeUtility = new TreeUtility();
	private double ratio;
	private double xStartAxis;

	private List<Integer> orderMapingList;
	private FactorHandler hfactorHandler;
	private int gapSize;

	private Color defaultColor;
	private boolean treeOneMouseHasClick;

	public void painting(Graphics2D g2d, PaintingLocationParas p, ParameterModel paraModel, DataModel dataModel) {
		GraphcsNode colTreeRootNode = paraModel.getColTreeRootNode();
		if (colTreeRootNode == null) {
			float x = (float) p.getColTreeLeftTopX();
			float y = (float) p.getColTreeLeftTopY();
			g2d.drawString("No colTree data", x, y);
		}

		GraphcsNode gRootNode = colTreeRootNode;
		if (paraModel.isShouldCalculateColTreeLocation()) {

			gapSize = paraModel.getGapSize();
			int[] hGapLocations = paraModel.gethGapLocations();
			hfactorHandler = new FactorHandler(0, hGapLocations, hGapLocations.length);

			calculateLocation(gRootNode, p);
			paraModel.setShouldCalculateColTreeLocation(false);
		}

		ClusterParameter clusterPara = paraModel.getClusterPara();
		defaultColor = clusterPara.getColor();
		treeOneMouseHasClick = paraModel.isTreeOneMouseHasClick();

		orderMapingList = new ArrayList<Integer>(dataModel.getRowNames().length);

		g2d.setColor(clusterPara.getColor());
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(clusterPara.getLineWidth()));
		realDrawLine(g2d, gRootNode);
		g2d.setStroke(oldStroke);

		int[] array = orderMapingList.stream().mapToInt(Integer::valueOf).toArray();
		dataModel.setColOrderMaping(array);
	}

	private void calculateLocation(GraphcsNode gRootNode, PaintingLocationParas p) {
		globalLeafIndex = 0;
		cellWidth = p.getCellWidth();
		ratio = p.getColTreeHeight() / treeUtility.getLongestDepthInvokeBranch(gRootNode);
		xStartAxis = p.getColTreeLeftTopX();
		// Post-order traversal
		// 后续遍历

		// This is the root
		double x1 = 0;
		double x2 = 0;
		double y1 = p.getColTreeLeftTopY(), y2 = y1;
		int childCount = gRootNode.getChildCount();

		for (int i = 0; i < childCount; i++) {
			EvolNode gNode2 = gRootNode.getChildAt(i);
			GraphcsNode g = (GraphcsNode) gNode2;
			postOrderTraversal(y2, g);
		}

		LineObj horizontalLineObj = gRootNode.getHorizontalLineObj();

		for (int i = 0; i < childCount; i++) {
			GraphcsNode g = (GraphcsNode)  gRootNode.getChildAt(i);

			if (i == 0) {
				x1 = g.getVerticaLineObj().getX1();
			} else if (i == childCount - 1) {
				x2 = g.getVerticaLineObj().getX1();
			}
		}
		horizontalLineObj.setX1(x1);
		horizontalLineObj.setX2(x2);
		horizontalLineObj.setY1(y1);
		horizontalLineObj.setY2(y2);
	}

	private void postOrderTraversal(double horiY1Y2, GraphcsNode EvolNode) {
		int childCount = EvolNode.getChildCount();
		if (childCount == 0) {
			double x = 0.5 * cellWidth + globalLeafIndex * cellWidth
					+ FactorCalculator.getIncreasedAddingFactor(hfactorHandler, globalLeafIndex) * gapSize;
			x += xStartAxis;
			EvolNode.getVerticaLineObj().setX1(x);
			EvolNode.getVerticaLineObj().setX2(x);

			// in order to set x1 we need to use parent
			double y1 = horiY1Y2;
			double y2 = y1 + ratio * EvolNode.getLength();
			EvolNode.getVerticaLineObj().setY1(y1);
			EvolNode.getVerticaLineObj().setY2(y2);

			globalLeafIndex++;
		} else {
			// Vertical
			double y1 = horiY1Y2;
			double y2 = y1 + ratio * EvolNode.getLength();
			EvolNode.getVerticaLineObj().setY1(y1);
			EvolNode.getVerticaLineObj().setY2(y2);

			for (int i = 0; i < childCount; i++) {
				EvolNode gNode2 = EvolNode.getChildAt(i);
				GraphcsNode g = (GraphcsNode) gNode2;
				postOrderTraversal(y2, g);
			}
			double x1 = 0, x2 = 0;
			for (int i = 0; i < childCount; i++) {
				GraphcsNode g = (GraphcsNode) EvolNode.getChildAt(i);
				if (i == 0) {
					x1 = g.getVerticaLineObj().getX2();
				} else if (i == childCount - 1) {
					x2 = g.getVerticaLineObj().getX2();
				}
			}

			double x = 0.5 * (x1 + x2);
			EvolNode.getVerticaLineObj().setX1(x);
			EvolNode.getVerticaLineObj().setX2(x);

			EvolNode.getHorizontalLineObj().setY1(y2);
			EvolNode.getHorizontalLineObj().setY2(y2);
			EvolNode.getHorizontalLineObj().setX1(x1);
			EvolNode.getHorizontalLineObj().setX2(x2);

		}

	}

	private int r = 3;

	private void realDrawLine(Graphics2D g2d, GraphcsNode EvolNode) {
		int childCount = EvolNode.getChildCount();
		int parentCount = EvolNode.getParentCount();

		boolean isSelectNode = false;
		if (treeOneMouseHasClick && EvolNode.isSelectedByUser()) {
			isSelectNode = true;
		}

		if (childCount == 0) {
			LineObj vv = EvolNode.getVerticaLineObj();
			double x1 = vv.getX1();
			double y1 = vv.getY1();
			double x2 = vv.getX2();
			double y2 = vv.getY2();

			Line2D line = new Line2D.Double(x1, y1, x2, y2);
			g2d.draw(line);

			if (isSelectNode) {
				Ellipse2D.Double circle1 = new Ellipse2D.Double(x1 - r, y1 - r, r * 2, r * 2);
				Color oldColor = g2d.getColor();
				g2d.setColor(Color.white);
				g2d.fill(circle1);
				g2d.setColor(oldColor);
				g2d.draw(circle1);

				g2d.setColor(Color.white);
				Ellipse2D.Double circle2 = new Ellipse2D.Double(x2 - r, y2 - r, r * 2, r * 2);
				g2d.fill(circle2);
				g2d.setColor(oldColor);
				g2d.draw(circle2);

			}

			// g2d.drawString(EvolNode.getLeafName(), (float) vv.getX1(), (float) vv.getY1());
			orderMapingList.add(EvolNode.getOrignalOrderInMatrix());
		} else {

			if (parentCount == 0) {
				// this is root
				LineObj vv = EvolNode.getHorizontalLineObj();
				Line2D line = new Line2D.Double(vv.getX1(), vv.getY1(), vv.getX2(), vv.getY2());
				g2d.draw(line);
			} else {

				LineObj vv = EvolNode.getHorizontalLineObj();
				double x1 = vv.getX1();

				double y1 = vv.getY1();

				double x2 = vv.getX2();

				double y2 = vv.getY2();

				Line2D line = new Line2D.Double(x1, y1, x2, y2);
				g2d.draw(line);

				if (isSelectNode) {
					Color oldColor = g2d.getColor();
					g2d.setColor(Color.white);
					Ellipse2D.Double circle1 = new Ellipse2D.Double(x1 - r, y1 - r, r * 2, r * 2);
					g2d.fill(circle1);
					g2d.setColor(oldColor);
					g2d.draw(circle1);

					g2d.setColor(Color.white);
					Ellipse2D.Double circle2 = new Ellipse2D.Double(x2 - r, y2 - r, r * 2, r * 2);
					g2d.fill(circle2);
					g2d.setColor(oldColor);
					g2d.draw(circle2);

				}

				vv = EvolNode.getVerticaLineObj();
				line = new Line2D.Double(vv.getX1(), vv.getY1(), vv.getX2(), vv.getY2());
				g2d.draw(line);

				if (isSelectNode) {
					Color oldColor = g2d.getColor();
					g2d.setColor(Color.white);
					x1 = vv.getX1();

					y1 = vv.getY1();

					x2 = vv.getX2();

					y2 = vv.getY2();
					Ellipse2D.Double circle1 = new Ellipse2D.Double(x1 - r, y1 - r, r * 2, r * 2);
					g2d.fill(circle1);
					g2d.setColor(oldColor);
					g2d.draw(circle1);

					g2d.setColor(Color.white);
					Ellipse2D.Double circle2 = new Ellipse2D.Double(x2 - r, y2 - r, r * 2, r * 2);
					g2d.fill(circle2);
					g2d.setColor(oldColor);
					g2d.draw(circle2);
				}

			}

			for (int i = 0; i < childCount; i++) {
				EvolNode gNode2 = EvolNode.getChildAt(i);
				GraphcsNode g = (GraphcsNode) gNode2;
				realDrawLine(g2d, g);
			}
		}

	}

}
