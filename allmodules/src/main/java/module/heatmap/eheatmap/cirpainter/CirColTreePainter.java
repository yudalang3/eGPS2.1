package module.heatmap.eheatmap.cirpainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import egps2.utils.common.model.datatransfer.ThreeTuple;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.CircularParameters;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.tree.ClusterParameter;
import module.heatmap.eheatmap.tree.GraphcsNode;
import module.heatmap.eheatmap.tree.RadialObjct;
import module.heatmap.eheatmap.tree.TangentialObjct;
import module.heatmap.eheatmap.tree.TreeUtility;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;

public class CirColTreePainter {

	private int globalLeafIndex;
	final TreeUtility treeUtility = new TreeUtility();
	private double ratio;

	private List<Integer> orderMapingList;
	private FactorHandler hfactorHandler;
	private int gapSize;

	private Color defaultColor;
	private boolean treeOneMouseHasClick;
	private int r = 3;
	private CircularParameters circularParameters;
	private double startDegree = 90;
	private final double oneDegRadians = Math.PI / 180;
	private double totalDegreeForPaintingTree;

	public void painting(Graphics2D g2d, PaintingLocationParas p, ParameterModel paraModel, DataModel dataModel) {
		GraphcsNode colTreeRootNode = paraModel.getColTreeRootNode();
		if (colTreeRootNode == null) {
			float x = (float) p.getColTreeLeftTopX();
			float y = (float) p.getColTreeLeftTopY();
			g2d.drawString("No colTree data", x, y);
		}

		circularParameters = paraModel.getCircularParameters();
		GraphcsNode gRootNode = colTreeRootNode;
		if (paraModel.isShouldCalculateColTreeLocation()) {
			gapSize = paraModel.getGapSize();
			int[] hGapLocations = paraModel.gethGapLocations();
			hfactorHandler = new FactorHandler(0, hGapLocations, hGapLocations.length);
		
			totalDegreeForPaintingTree = circularParameters.getTotalDegreeForPaintingColTree();
			AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
			ThreeTuple<List<String>, List<Color[]>, List<byte[]>> colAnnoParas = annotaionParaObj.getColAnnoParas();
			List<Color[]> colAnnColors = colAnnoParas.second;
			List<byte[]> colAnnotaionIndexes = colAnnoParas.third;
			List<String> colAnnotationNames = colAnnoParas.first;

			int numOfAnnotations = colAnnotaionIndexes.size();
			int degre = circularParameters.getColEachAnnotationDegree();
			startDegree = numOfAnnotations * degre 
					+ circularParameters.getTotalSstartDeg()+ circularParameters.getTotalEextendDeg();
//					+ circularParameters.getColEachAnnotationDegreeGap();
			//System.out.println("ss\t"+startDegree);
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

		Point centerPoint = p.getCenterPoint();
		realDrawLine(g2d, gRootNode, centerPoint.x, centerPoint.y);
		g2d.setStroke(oldStroke);

		int[] array = orderMapingList.stream().mapToInt(Integer::valueOf).toArray();
		dataModel.setColOrderMaping(array);
	}
	

	private void calculateLocation(GraphcsNode gRootNode, PaintingLocationParas p) {
		globalLeafIndex = 0;
		ratio = totalDegreeForPaintingTree / treeUtility.getLongestDepthInvokeBranch(gRootNode);
		// Post-order traversal

		// This is the root
		int childCount = gRootNode.getChildCount();
		Point centerPoint = p.getCenterPoint();

		for (int i = 0; i < childCount; i++) {
			GraphcsNode g = (GraphcsNode) gRootNode.getChildAt(i);
			postOrderTraversal(centerPoint.x, centerPoint.y, g);
		}

		assignePropertiesForInnerNode(gRootNode, centerPoint.x, centerPoint.y);
	}

	private void postOrderTraversal(int x0, int y0, GraphcsNode gNode) {
		int childCount = gNode.getChildCount();
		if (childCount == 0) {
			double radicus = circularParameters.getInnerRadius() + 0.5 * circularParameters.getRingThickness()
					+ globalLeafIndex * circularParameters.getRingThickness()
					+ FactorCalculator.getIncreasedAddingFactor(hfactorHandler, globalLeafIndex) * gapSize;

			TangentialObjct tangentialObj = gNode.getTangentialObj();
			tangentialObj.setStartDeg(startDegree);
			tangentialObj.setRadicusForPaint(radicus);
			tangentialObj.setEetendDeg(ratio * gNode.getLength());

			globalLeafIndex++;
		} else {

			for (int i = 0; i < childCount; i++) {
				GraphcsNode g = (GraphcsNode) gNode.getChildAt(i);
				postOrderTraversal(x0, y0, g);
			}

			assignePropertiesForInnerNode(gNode, x0, y0);

		}

	}

	private void assignePropertiesForInnerNode(GraphcsNode gNode, int x0, int y0) {
		double radicus1 = 0, radicus2 = 0;
		double currentStartDeg = 0;
		int childCount = gNode.getChildCount();
		for (int i = 0; i < childCount; i++) {
			GraphcsNode g = (GraphcsNode) gNode.getChildAt(i);
			TangentialObjct tangentialObj = g.getTangentialObj();
			if (i == 0) {
				radicus1 = tangentialObj.getRadicusForPaint();
				currentStartDeg = tangentialObj.getStartDeg() + tangentialObj.getExtendDeg();
			} else if (i == childCount - 1) {
				radicus2 = tangentialObj.getRadicusForPaint();
			}
		}

		double brachLen = ratio * gNode.getLength();
		double radicus = 0.5 * (radicus1 + radicus2);
		TangentialObjct tangentialObj = gNode.getTangentialObj();
		tangentialObj.setRadicusForPaint(radicus);
		tangentialObj.setStartDeg(currentStartDeg);
		tangentialObj.setEetendDeg(brachLen);

		RadialObjct radialObj = gNode.getRadialObj();

		double cos = Math.cos(currentStartDeg * oneDegRadians);
		double sin = Math.sin(currentStartDeg * oneDegRadians);
		double xx1 = x0 + radicus1 * cos;
		double yy1 = y0 - radicus1 * sin;
		double xx2 = x0 + radicus2 * cos;
		double yy2 = y0 - radicus2 * sin;

		radialObj.setxInner(xx1);
		radialObj.setyInner(yy1);
		radialObj.setxOutter(xx2);
		radialObj.setyOutter(yy2);
	}

	private void realDrawLine(Graphics2D g2d, GraphcsNode gNode, int x0, int y0) {
		int childCount = gNode.getChildCount();
		int parentCount = gNode.getParentCount();

		boolean isSelectNode = false;
		if (treeOneMouseHasClick && gNode.isSelectedByUser()) {
			isSelectNode = true;
		}

		if (childCount == 0) {
			// This is leaf
			RadialObjct radialObj = gNode.getRadialObj();

			double x1 = radialObj.getxInner();
			double y1 = radialObj.getyInner();
			double x2 = radialObj.getxOutter();
			double y2 = radialObj.getyOutter();

			Line2D line = new Line2D.Double(x1, y1, x2, y2);
			g2d.draw(line);
			g2d.draw(gNode.getTangentialObj().getPaintArc(x0, y0));

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

			// g2d.drawString(gNode.getLeafName(), (float) vv.getX1(), (float) vv.getY1());
			orderMapingList.add(gNode.getOrignalOrderInMatrix());
		} else {

			if (parentCount == 0) {
				// this is root
				RadialObjct radialObj = gNode.getRadialObj();

				double x1 = radialObj.getxInner();
				double y1 = radialObj.getyInner();
				double x2 = radialObj.getxOutter();
				double y2 = radialObj.getyOutter();

				Line2D line = new Line2D.Double(x1, y1, x2, y2);
				g2d.draw(line);
				g2d.draw(gNode.getTangentialObj().getPaintArc(x0, y0));
			} else {

				g2d.draw(gNode.getTangentialObj().getPaintArc(x0, y0));

				RadialObjct radialObj = gNode.getRadialObj();

				double x1 = radialObj.getxInner();
				double y1 = radialObj.getyInner();
				double x2 = radialObj.getxOutter();
				double y2 = radialObj.getyOutter();
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

			}

			for (int i = 0; i < childCount; i++) {
				GraphcsNode g = (GraphcsNode) gNode.getChildAt(i);
				realDrawLine(g2d, g, x0, y0);
			}
		}

	}

}