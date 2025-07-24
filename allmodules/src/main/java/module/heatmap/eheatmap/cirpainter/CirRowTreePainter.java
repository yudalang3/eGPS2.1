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

public class CirRowTreePainter {

	private int globalLeafIndex = 0;
	private double eachDeg = 40;
	private double ratio = 0.5;

	final TreeUtility treeUtility = new TreeUtility();

	private List<Integer> orderMapingList;
	private int gapSize;
	private FactorHandler vfactorHandler;
	private Color defaultColor;
	private boolean treeOneMouseHasClick;
	private boolean ifRowTreeExternally = false;
	private int r = 3;
	private double startDegree;
	private final double oneDegRadians = Math.PI / 180;
	private double rowAnnGapFactor;
	private double availableRadius;

	public CirRowTreePainter() {
	}

	public void painting(Graphics2D g2d, PaintingLocationParas p, ParameterModel paraModel, DataModel dataModel) {

		GraphcsNode rowTreeRootNode = paraModel.getRowTreeRootNode();
		if (rowTreeRootNode == null) {
			g2d.drawString("No rowTree data", 100f, 100f);
		}

		GraphcsNode gRootNode = rowTreeRootNode;
		if (paraModel.isShouldCalculateRowTreeLocation()) {

			gapSize = paraModel.getGapSize();
			int[] vGapLocations = paraModel.getvGapLocations();
			vfactorHandler = new FactorHandler(0, vGapLocations, vGapLocations.length);

			calculateLocation(gRootNode, p, paraModel);
			paraModel.setShouldCalculateRowTreeLocation(false);
		}

		orderMapingList = new ArrayList<Integer>(dataModel.getRowNames().length);
		ClusterParameter clusterPara = paraModel.getClusterPara();
		defaultColor = clusterPara.getColor();
		treeOneMouseHasClick = paraModel.isTreeOneMouseHasClick();
		ifRowTreeExternally = paraModel.isIfRowTreeFormExternalFile();

		g2d.setColor(clusterPara.getColor());
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(clusterPara.getLineWidth()));
		// System.out.println("x0y0\t"+centerX0+"\t"+centerX0);
		Point centerPoint = p.getCenterPoint();
		realDrawLine(g2d, gRootNode, centerPoint.x, centerPoint.y, 0);

		g2d.setStroke(oldStroke);

		int[] array = orderMapingList.stream().mapToInt(Integer::valueOf).toArray();
		dataModel.setRowOrderMaping(array);

//		g2d.setColor(Color.blue);
//		g2d.draw(new Rectangle2D.Double(p.getRowTreeLeftTopX(),p.getRowTreeLeftTopY(),p.getRowTreeWeidth(),p.getRowTreeHeight()));
	}

	/**
	 * Note deepth is vary good for debug!
	 * 
	 * @param g2d
	 * @param gNode
	 * @param x0
	 * @param y0
	 * @param deepth
	 */
	private void realDrawLine(Graphics2D g2d, GraphcsNode gNode, int x0, int y0, int deepth) {
		int childCount = gNode.getChildCount();
		int parentCount = gNode.getParentCount();
		// g2d.setColor(defaultColor);
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

			// DecimalFormat df = new DecimalFormat("##.##");
			// g2d.drawString("Lo", (float) x2, (float) y2);
			// g2d.drawString("Li", (float) x1, (float) y1);

			orderMapingList.add(gNode.getOrignalOrderInMatrix());

		} else {

			if (parentCount == 0) {
				// this is root
				g2d.draw(gNode.getTangentialObj().getPaintArc(x0, y0));
				RadialObjct radialObj = gNode.getRadialObj();

				if (ifRowTreeExternally) {
					Line2D tip = new Line2D.Double(x0, y0, radialObj.getxInner(), radialObj.getyInner());
					g2d.draw(tip);
				}

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

//				g2d.drawString("Io", (float) x2, (float) y2);
//				g2d.drawString("Ii", (float) x1, (float) y1);

//				DecimalFormat df = new DecimalFormat("##.##");
//				g2d.drawString(gNode.getName()+" "+df.format(vv.getX2())+" "+df.format(vv.getY2()), (float) vv.getX2(), (float) vv.getY2());

			}

			for (int i = 0; i < childCount; i++) {
				GraphcsNode g = (GraphcsNode) gNode.getChildAt(i);
				realDrawLine(g2d, g, x0, y0, deepth++);
			}
		}

	}

	private void calculateLocation(GraphcsNode gRootNode, PaintingLocationParas p, ParameterModel paraModel) {

		CircularParameters circularParameters = paraModel.getCircularParameters();
		rowAnnGapFactor = circularParameters.getRowAnnGapFactor();
		
		if (paraModel.isIfPaintRowAnnotation()) {
			availableRadius = Math.min(circularParameters.getRowAnnotationRadius(), circularParameters.getInnerRadius());
		}else {
			availableRadius = circularParameters.getInnerRadius();
		}

		globalLeafIndex = 0;
		eachDeg = circularParameters.getTotalEextendDeg() / p.getNumOfRows();
		double longestDepthInvokeBranch = treeUtility.getLongestDepthInvokeBranch(gRootNode);
		final int theLengthOfTip = 4;
		ratio = (availableRadius - theLengthOfTip) / longestDepthInvokeBranch;
		startDegree = circularParameters.getTotalSstartDeg();

		Point centerPoint = p.getCenterPoint();
		// 后续遍历

		// This is the root
		int childCount = gRootNode.getChildCount();

		for (int i = 0; i < childCount; i++) {
			GraphcsNode g = (GraphcsNode) gRootNode.getChildAt(i);
			postOrderTraversal(centerPoint.x, centerPoint.y, g);
		}
		assignInternalNode(gRootNode, childCount, centerPoint.x, centerPoint.y);
	}

	private void postOrderTraversal(int x0, int y0, GraphcsNode gNode) {
		int childCount = gNode.getChildCount();
		if (childCount == 0) {
			double startDegreeForIterate = startDegree + 0.5 * eachDeg + globalLeafIndex * eachDeg
					+ FactorCalculator.getIncreasedAddingFactor(vfactorHandler, globalLeafIndex) * gapSize
							* rowAnnGapFactor;

			RadialObjct radialObj = gNode.getRadialObj();
			TangentialObjct tangentialObj = gNode.getTangentialObj();
			radialObj.setDegree(startDegreeForIterate);
			// in order to set x1 we need to use parent
			double ringRadialLen = ratio * gNode.getLength();
			double cos = Math.cos(startDegreeForIterate * oneDegRadians);
			double xx1 = x0 + availableRadius * cos;
			double xx2 = xx1 - ringRadialLen * cos;
			double sin = Math.sin(startDegreeForIterate * oneDegRadians);
			double yy1 = y0 - availableRadius * sin;
			double yy2 = yy1 + ringRadialLen * sin;

			radialObj.setxOutter(xx1);
			radialObj.setyOutter(yy1);
			radialObj.setxInner(xx2);
			radialObj.setyInner(yy2);

			tangentialObj.setRadicusForCal(availableRadius - ringRadialLen);
			// System.out.println(gNode.getLeafName()+gNode.getName()+"\tstartDegreeForIterate\t"+startDegreeForIterate);
			globalLeafIndex++;
		} else {
			for (int i = 0; i < childCount; i++) {
				GraphcsNode g = (GraphcsNode) gNode.getChildAt(i);
				postOrderTraversal(x0, y0, g);
			}

			assignInternalNode(gNode, childCount, x0, y0);

		}

	}

	private void assignInternalNode(GraphcsNode gNode, int childCount, int x0, int y0) {
		double availableRadiusTmp = 0;
		double ringRadialLen = ratio * gNode.getLength();
		double degree1 = 0, degree2 = 0;
		for (int i = 0; i < childCount; i++) {
			GraphcsNode g = (GraphcsNode) gNode.getChildAt(i);
			if (i == 0) {
				degree1 = g.getRadialObj().getDegree();
				TangentialObjct tangentialObj = g.getTangentialObj();
				availableRadiusTmp = tangentialObj.getRadicusForCal();
			} else if (i == childCount - 1) {
				degree2 = g.getRadialObj().getDegree();
			}

		}
		// System.out.println(ss+"\tdegree:\t" + degree1 +"\t"+degree2);

		double currentDegTmp = 0.5 * (degree1 + degree2);
		RadialObjct radialObj = gNode.getRadialObj();
		TangentialObjct tangentialObj = gNode.getTangentialObj();

		radialObj.setDegree(currentDegTmp);
		// in order to set x1 we need to use parent
		ringRadialLen = ratio * gNode.getLength();
		double cos = Math.cos(currentDegTmp * oneDegRadians);
		double xx1 = x0 + availableRadiusTmp * cos;
		double xx2 = xx1 - ringRadialLen * cos;
		double sin = Math.sin(currentDegTmp * oneDegRadians);
		double yy1 = y0 - availableRadiusTmp * sin;
		double yy2 = yy1 + ringRadialLen * sin;

		// radial
		radialObj.setxOutter(xx1);
		radialObj.setyOutter(yy1);
		radialObj.setxInner(xx2);
		radialObj.setyInner(yy2);

		tangentialObj.setRadicusForCal(availableRadiusTmp - ringRadialLen);
		tangentialObj.setRadicusForPaint(availableRadiusTmp);
		tangentialObj.setStartDeg(degree1);
		tangentialObj.setEetendDeg(degree2 - degree1);
	}
}
