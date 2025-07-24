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

public class RectRowTreePainter {

	private int globalLeafIndex = 0;
	private double cellHeight = 40;
	private double ratio = 0.5;
	private double yStartAxis;

	final TreeUtility treeUtility = new TreeUtility();

	private List<Integer> orderMapingList;
	private int gapSize;
	private FactorHandler vfactorHandler;
	private boolean treeOneMouseHasClick;
	private boolean ifRowTreeExternally = false;

	public RectRowTreePainter() {
	}

	public void painting(Graphics2D g2d, PaintingLocationParas p, ParameterModel paraModel, DataModel dataModel) {

		GraphcsNode rowTreeRootNode = paraModel.getRowTreeRootNode();
		if (rowTreeRootNode == null) {
			float x = (float) p.getRowTreeLeftTopX();
			float y = (float) p.getRowTreeLeftTopY();
			g2d.drawString("No rowTree data", x, y);
		}

		GraphcsNode gRootNode = rowTreeRootNode;
		if (paraModel.isShouldCalculateRowTreeLocation()) {

			gapSize = paraModel.getGapSize();
			int[] vGapLocations = paraModel.getvGapLocations();
			vfactorHandler = new FactorHandler(0, vGapLocations, vGapLocations.length);

			calculateLocation(gRootNode, p);
			paraModel.setShouldCalculateRowTreeLocation(false);
		}

		orderMapingList = new ArrayList<Integer>(dataModel.getRowNames().length);
		ClusterParameter clusterPara = paraModel.getClusterPara();
		treeOneMouseHasClick = paraModel.isTreeOneMouseHasClick();
		ifRowTreeExternally = paraModel.isIfRowTreeFormExternalFile();

		g2d.setColor(clusterPara.getColor());
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(clusterPara.getLineWidth()));

		realDrawLine(g2d, gRootNode);

		g2d.setStroke(oldStroke);

		int[] array = orderMapingList.stream().mapToInt(Integer::valueOf).toArray();
		dataModel.setRowOrderMaping(array);

//		g2d.setColor(Color.blue);
//		g2d.draw(new Rectangle2D.Double(p.getRowTreeLeftTopX(),p.getRowTreeLeftTopY(),p.getRowTreeWeidth(),p.getRowTreeHeight()));
	}

	private int r = 3;

	private void realDrawLine(Graphics2D g2d, GraphcsNode gNode) {
		int childCount = gNode.getChildCount();
		int parentCount = gNode.getParentCount();
		// g2d.setColor(defaultColor);
		boolean isSelectNode = false;

		if (treeOneMouseHasClick && gNode.isSelectedByUser()) {
			isSelectNode = true;

		}
		if (childCount == 0) {
			LineObj vv = gNode.getHorizontalLineObj();

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

//			DecimalFormat df = new DecimalFormat("##.##");
//			g2d.drawString(gNode.getLeafName()+" "+df.format(vv.getX2())+" "+df.format(vv.getY2()), (float) vv.getX2(), (float) vv.getY2());

			orderMapingList.add(gNode.getOrignalOrderInMatrix());

		} else {

			if (parentCount == 0) {
				// this is root
				LineObj vv = gNode.getVerticaLineObj();
				double xxx = vv.getX1();
				Line2D line = new Line2D.Double(xxx, vv.getY1(), xxx, vv.getY2());
				g2d.draw(line);

				if (ifRowTreeExternally) {
					double yyy = 0.5 * (vv.getY1() + vv.getY2());
					Line2D tip = new Line2D.Double(xxx - 10, yyy, xxx, yyy);
					g2d.draw(tip);
				}

			} else {
				LineObj vv = gNode.getVerticaLineObj();

				Line2D line = new Line2D.Double(vv.getX1(), vv.getY1(), vv.getX2(), vv.getY2());
				g2d.draw(line);

				if (isSelectNode) {
					Color oldColor = g2d.getColor();
					g2d.setColor(Color.white);
					double x1 = vv.getX1();

					double y1 = vv.getY1();

					double x2 = vv.getX2();

					double y2 = vv.getY2();
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

				vv = gNode.getHorizontalLineObj();
				double x1 = vv.getX1();

				double y1 = vv.getY1();

				double x2 = vv.getX2();

				double y2 = vv.getY2();

				line = new Line2D.Double(x1, y1, x2, y2);
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

//				DecimalFormat df = new DecimalFormat("##.##");
//				g2d.drawString(gNode.getName()+" "+df.format(vv.getX2())+" "+df.format(vv.getY2()), (float) vv.getX2(), (float) vv.getY2());

			}

			for (int i = 0; i < childCount; i++) {
				EvolNode gNode2 = gNode.getChildAt(i);
				GraphcsNode g = (GraphcsNode) gNode2;
				realDrawLine(g2d, g);
			}
		}

	}

	private void calculateLocation(GraphcsNode gRootNode, PaintingLocationParas p) {
		globalLeafIndex = 0;
		cellHeight = p.getCellHeight();
		double longestDepthInvokeBranch = treeUtility.getLongestDepthInvokeBranch(gRootNode);
		ratio = (p.getRowTreeWeidth() - 5) / longestDepthInvokeBranch;
		yStartAxis = p.getRowTreeLeftTopY();
		double blankLeft = p.getRowTreeLeftTopX();
		// 后续遍历

		// This is the root
		double x1 = blankLeft;
		double x2 = x1;
		double y1 = 0, y2 = 0;
		int childCount = gRootNode.getChildCount();

		for (int i = 0; i < childCount; i++) {
			EvolNode gNode2 = gRootNode.getChildAt(i);
			GraphcsNode g = (GraphcsNode) gNode2;
			postOrderTraversal(x1, g);
		}

		LineObj verticaLineObj = gRootNode.getVerticaLineObj();
		verticaLineObj.setX1(x1);
		verticaLineObj.setX2(x2);
		for (int i = 0; i < childCount; i++) {
			GraphcsNode g = (GraphcsNode) gRootNode.getChildAt(i);

			if (i == 0) {
				y1 = g.getHorizontalLineObj().getY2();
			} else if (i == childCount - 1) {
				y2 = g.getHorizontalLineObj().getY2();
			}
		}
		verticaLineObj.setY1(y1);
		verticaLineObj.setY2(y2);
	}

	private void postOrderTraversal(double vertX1X2, GraphcsNode gNode) {
		int childCount = gNode.getChildCount();
		if (childCount == 0) {
			double y = 0.5 * cellHeight + globalLeafIndex * cellHeight
					+ FactorCalculator.getIncreasedAddingFactor(vfactorHandler, globalLeafIndex) * gapSize;
			y += yStartAxis;
			gNode.getHorizontalLineObj().setY2(y);
			gNode.getHorizontalLineObj().setY1(y);

			// in order to set x1 we need to use parent
			double x1 = vertX1X2;
			double x2 = x1 + ratio * gNode.getLength();
			gNode.getHorizontalLineObj().setX1(x1);
			gNode.getHorizontalLineObj().setX2(x2);

			globalLeafIndex++;
		} else {

			// Horizontal
			double x1 = vertX1X2;
			double x2 = x1 + ratio * gNode.getLength();
			gNode.getHorizontalLineObj().setX1(x1);
			gNode.getHorizontalLineObj().setX2(x2);

			for (int i = 0; i < childCount; i++) {
				EvolNode gNode2 = gNode.getChildAt(i);
				GraphcsNode g = (GraphcsNode) gNode2;
				postOrderTraversal(x2, g);
			}
			double y1 = 0, y2 = 0;
			for (int i = 0; i < childCount; i++) {
				GraphcsNode g = (GraphcsNode) gNode.getChildAt(i);
				if (i == 0) {
					y1 = g.getHorizontalLineObj().getY2();
				} else if (i == childCount - 1) {
					y2 = g.getHorizontalLineObj().getY2();
				}
			}

			double y = 0.5 * (y1 + y2);
			gNode.getHorizontalLineObj().setY1(y);
			gNode.getHorizontalLineObj().setY2(y);

			gNode.getVerticaLineObj().setY1(y1);
			gNode.getVerticaLineObj().setY2(y2);
			gNode.getVerticaLineObj().setX1(x2);
			gNode.getVerticaLineObj().setX2(x2);

		}

	}
}
