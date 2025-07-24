package module.evolview.phylotree.visualization.layout;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import module.evolview.phylotree.visualization.graphics.struct.TreeDecideUtil;
import org.apache.commons.math3.util.FastMath;

import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.phylotree.visualization.graphics.struct.NodeType;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.ShowLeafPropertiesInfo;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator.LongestRoot2leafBean;

public class RectHalfCircleLayout extends RectPhyloLayout {

	private int halfWorkWidth;
	private int halfWorkRadius;
	private int halfWorkHeight;
	private Point2D.Double topCircleCenter = new Point2D.Double();
	private Point2D.Double canvasCenter = new Point2D.Double();

	private double globalRadicus;
	private double maxJudgeAngle;
	// 第一个矩形边的分割点
	private int rectH;
	// 矩形边加上半圆
	private double highBound;
	private double totalAvaliableLength;

	private Line2D.Double lineDrawUtil = new Line2D.Double();
	Arc2D.Double arc2d = new Arc2D.Double();

	public RectHalfCircleLayout(TreeLayoutProperties controller, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode, phylogeneticTreePanel);
	}

	@Override
	protected void beforeCalculate(int width, int height) {
		super.beforeCalculate(width, height);

		// 计算方式

		int workWidth = blankArea.getWorkWidth((int) currentWidth);
		int workHeight = blankArea.getWorkHeight((int) currentHeight);
		globalRadicus = 0.5 * workHeight;

		canvasCenter.setLocation(blankArea.getLeft() + 0.5 * workWidth, blankArea.getTop() + 0.5 * workHeight);
		// 黄金比例
//		halfWorkRadius = (int) (0.382 * workHeight);
		halfWorkRadius = (int) (0.3 * workHeight);


		halfWorkWidth = halfWorkRadius;
		rectH = workHeight - halfWorkRadius;
		halfWorkHeight = (int) (0.5 * rectH);
		totalAvaliableLength = rectH + FastMath.PI * halfWorkRadius;
		highBound = rectH + FastMath.PI * halfWorkRadius;
		totalAvaliableLength += rectH;

//		System.out.println(rectH + "\t" + (FastMath.PI * halfWorkRadius));

		topCircleCenter.setLocation(canvasCenter.getX(), halfWorkRadius + blankArea.getTop());

		maxJudgeAngle = FastMath.atan2(halfWorkRadius, globalRadicus);
//		startAngleInRadian = maxJudgeAngle - FastMath.PI;
//		endAngleInRadian = FastMath.PI - maxJudgeAngle;
//		totalAngleInRadian = endAngleInRadian + endAngleInRadian;

		loop = 0;
		// x axis
		LongestRoot2leafBean maxLengthOfRoot2LeafBean = GraphicTreePropertyCalculator.getMaxLengthOfRoot2Leaf(rootNode);
		double maxLengthOfRoot2Leaf = maxLengthOfRoot2LeafBean.getLength();

		canvas2logicRatio = halfWorkRadius / maxLengthOfRoot2Leaf;
		// y axis
		eachYGapLength = totalAvaliableLength / (GraphicTreePropertyCalculator.getLeafNumber(rootNode) - 1.0);

		configurateButtomScaleBarDrawProperty(maxLengthOfRoot2LeafBean.getLength(), maxLengthOfRoot2LeafBean.getLeaf());
	}

	@Override
	public void calculateForPainting(int width, int height) {
		ShowLeafPropertiesInfo showLeafPropertiesInfo = treeLayoutProperties.getShowLeafPropertiesInfo();
		if (showLeafPropertiesInfo.isNeedChange4hideLabel()) {
			int stringWidth = getMaxLengthLeafNameWidthAccording2CurrentFont();
			blankArea.setRight(blankArea.getRight() - stringWidth);
			showLeafPropertiesInfo.setNeedChange4hideLabel(false);
		} else if (showLeafPropertiesInfo.isNeedChange4showLabel()) {
			int stringWidth = getMaxLengthLeafNameWidthAccording2CurrentFont();
			blankArea.setRight(stringWidth + blankArea.getRight());
			showLeafPropertiesInfo.setNeedChange4showLabel(false);
		}

		beforeCalculate(width, height);

		GraphicsNode root = rootNode;
		// 将root的branchLength设置为0；否则会影响可视化
//		root.setDisplayedBranchLength(0);
		recursiveCalculate(root, root.getDisplayedBranchLength());

		this.scaleBarProperty.setIfDrawScaleBar(true);

		afterCalculation();
	}

	protected void recursiveCalculate(GraphicsNode node, double cumulateBranchLengthExceptCurrent) {
		double parentLineageLength = cumulateBranchLengthExceptCurrent * canvas2logicRatio;
		double lineageLength = parentLineageLength + node.getDisplayedBranchLength() * canvas2logicRatio;

		// 这个 Radicus 用来存储 lineageLength
		node.setRadicusIfNeeded(lineageLength);

		NodeType nodeType = TreeDecideUtil.decideNodeType(node, rootNode);
		switch (nodeType) {
		case LEAF:
			double currYAxisLength = loop * eachYGapLength;
			node.setDoubleVariable(currYAxisLength);
			assignValue(node, lineageLength, currYAxisLength);
			assignValue4XYParent(node, parentLineageLength, currYAxisLength);
//			System.out.println(node.getYSelf() + "\t" + node.getYParent());

			loop++;
			break;
		default:
			for (int i = 0; i < node.getChildCount(); i++) {
				recursiveCalculate((GraphicsNode) node.getChildAt(i),
						cumulateBranchLengthExceptCurrent + node.getDisplayedBranchLength());
			}
			currYAxisLength = treeLayoutProperties.averageValueOfChildrenForParent(node);
			node.setDoubleVariable(currYAxisLength);

			assignValue(node, lineageLength, currYAxisLength);
			assignValue4XYParent(node, parentLineageLength, currYAxisLength);

			if (nodeType == NodeType.ROOT) {
				node.setXParent(canvasCenter.getX());
			}
			break;
		}
	}

	public void assignValue(GraphicsNode node, double lineageLength, double currYAxisLength) {
		double xx = 0;
		double yy = 0;
		if (currYAxisLength < rectH) {
			xx = canvasCenter.getX() - lineageLength;
			yy = currentHeight - blankArea.getBottom() - currYAxisLength;
		} else if (currYAxisLength < highBound) {
			double angleRadius = (currYAxisLength - rectH) / halfWorkRadius;
			xx = topCircleCenter.getX() - lineageLength * FastMath.cos(angleRadius);
			yy = topCircleCenter.getY() - lineageLength * FastMath.sin(angleRadius);
			// 重要设置angleRadius
			node.setAngleIfNeeded(angleRadius);
		} else {
			xx = canvasCenter.getX() + lineageLength;

			yy = currentHeight - blankArea.getBottom() - (totalAvaliableLength - currYAxisLength);

		}

		node.setXSelf(xx);
		node.setYSelf(yy);
	}

	public void assignValue4XYParent(GraphicsNode node, double lineageLength, double currYAxisLength) {
		double xx = 0;
		double yy = 0;
		if (currYAxisLength < rectH) {
			xx = canvasCenter.getX() - lineageLength;
			yy = currentHeight - blankArea.getBottom() - currYAxisLength;
		} else if (currYAxisLength < highBound) {
			double angleRadius = (currYAxisLength - rectH) / halfWorkRadius;
			xx = topCircleCenter.getX() - lineageLength * FastMath.cos(angleRadius);
			yy = topCircleCenter.getY() - lineageLength * FastMath.sin(angleRadius);
		} else {
			xx = canvasCenter.getX() + lineageLength;

			yy = currentHeight - blankArea.getBottom() - (totalAvaliableLength - currYAxisLength);

		}

		node.setXParent(xx);
		node.setYParent(yy);
	}

	@Override
	public void paintGraphics(Graphics2D g2d) {

		// for debug
//		backGround(g2d);


		recursiveDraw(rootNode, g2d);
	}

	private void recursiveDraw(GraphicsNode node, Graphics2D g2d) {
		NodeType nodeType = TreeDecideUtil.decideNodeType(node, rootNode);
		switch (nodeType) {
		case LEAF:
			double xSelf = node.getXSelf();
			double ySelf = node.getYSelf();
			g2d.fillOval((int) (xSelf - 2), (int) (ySelf - 2), 4, 4);

			if (treeLayoutProperties.getShowLeafPropertiesInfo().isShowLeafLabel()) {
				g2d.drawString(node.getName(), (int) (xSelf - 2), (int) (ySelf - 2));
			}

			lineDrawUtil.setLine(xSelf, ySelf, node.getXParent(), node.getYParent());
			g2d.draw(lineDrawUtil);
			break;
		default:

			xSelf = node.getXSelf();
			ySelf = node.getYSelf();
			g2d.fillOval((int) (xSelf - 2), (int) (ySelf - 2), 4, 4);
			if (treeLayoutProperties.getShowLeafPropertiesInfo().isShowLeafLabel()) {
				g2d.drawString(node.getName(), (int) (xSelf - 2), (int) (ySelf - 2));
			}

			lineDrawUtil.setLine(xSelf, ySelf, node.getXParent(), node.getYParent());
			g2d.draw(lineDrawUtil);
			
			GraphicsNode firstNode = null;
			GraphicsNode lastNode = null;
			for (int i = 0; i < node.getChildCount(); i++) {
				GraphicsNode child = (GraphicsNode) node.getChildAt(i);

				recursiveDraw(child, g2d);
				if (firstNode == null) {
					firstNode = child;
				}
				lastNode = child;
			}

			// 这个 Radicus 用来存储 lineageLength
			drawLineOfSiblingConnection(firstNode, lastNode, node.getRadicusIfNeeded(), g2d);

			if (nodeType == NodeType.ROOT) {

			}
			break;
		}
	}

	private void drawLineOfSiblingConnection(GraphicsNode firstNode, GraphicsNode lastNode,
			double parentCurrYAxisLength, Graphics2D g2d) {
		double firstNodeXParent = firstNode.getXParent();
		double firstNodeYParent = firstNode.getYParent();
		double lastNodeXParent = lastNode.getXParent();
		double lastNodeYParent = lastNode.getYParent();

		double x1 = 0, y1 = 0, x2 = 0, y2 = 0;

		double firstCurrYAxisLength = firstNode.getDoubleVariable();
		double lastCurrYAxisLength = lastNode.getDoubleVariable();

		if (firstCurrYAxisLength < rectH) {
			if (lastCurrYAxisLength < rectH) {
				// 1
				x1 = firstNodeXParent;
				y1 = firstNodeYParent;
				x2 = lastNodeXParent;
				y2 = lastNodeYParent;
			} else if (lastCurrYAxisLength < highBound) {
				// 2
				x1 = firstNodeXParent;
				y1 = firstNodeYParent;
				x2 = firstNodeXParent;
				y2 = halfWorkRadius + blankArea.getTop();
				// others
				arc2d.setFrameFromCenter(topCircleCenter.getX(), topCircleCenter.getY(),
						topCircleCenter.getX() - parentCurrYAxisLength, topCircleCenter.getY() - parentCurrYAxisLength);

				arc2d.setAngles(lastNodeXParent, lastNodeYParent, x2, y2);
				g2d.draw(arc2d);
			} else {
				// 3

			}
		} else if (firstCurrYAxisLength < highBound) {
			if (lastCurrYAxisLength < highBound) {
				// 4
				arc2d.setFrameFromCenter(topCircleCenter.getX(), topCircleCenter.getY(),
						topCircleCenter.getX() - parentCurrYAxisLength, topCircleCenter.getY() - parentCurrYAxisLength);

				arc2d.setAngles(lastNodeXParent, lastNodeYParent, firstNodeXParent, firstNodeYParent);
				g2d.draw(arc2d);
			} else {
				// 5
				x1 = lastNodeXParent;
				y1 = halfWorkRadius + blankArea.getTop();
				x2 = lastNodeXParent;
				y2 = lastNodeYParent;
				// others
				arc2d.setFrameFromCenter(topCircleCenter.getX(), topCircleCenter.getY(),
						topCircleCenter.getX() - parentCurrYAxisLength, topCircleCenter.getY() - parentCurrYAxisLength);

				arc2d.setAngles(lastNodeXParent, y1, firstNodeXParent, firstNodeYParent);
				g2d.draw(arc2d);
			}
		} else {
			// 6
			x1 = firstNodeXParent;
			y1 = firstNodeYParent;
			x2 = lastNodeXParent;
			y2 = lastNodeYParent;
		}

		lineDrawUtil.setLine(x1, y1, x2, y2);
		g2d.draw(lineDrawUtil);
	}

	private void backGround(Graphics2D g2d) {
		g2d.setColor(Color.LIGHT_GRAY);
		{
			Rectangle2D.Double rectangle2d = new Rectangle2D.Double();
			rectangle2d.setFrameFromDiagonal(blankArea.getLeft(), blankArea.getTop(),
					currentWidth - blankArea.getRight(), currentHeight - blankArea.getBottom());
			g2d.draw(rectangle2d);
		}

		Rectangle2D.Double rectangle2d = new Rectangle2D.Double();
		rectangle2d.setFrameFromDiagonal(canvasCenter.getX() - halfWorkRadius, blankArea.getTop() + halfWorkRadius,
				canvasCenter.getX() + halfWorkRadius, currentHeight - blankArea.getBottom());
		g2d.draw(rectangle2d);

		Line2D.Double line = new Line2D.Double();
		line.setLine(rectangle2d.getX(), rectangle2d.getY(), rectangle2d.getX() + rectangle2d.getWidth(),
				rectangle2d.getY() + rectangle2d.getHeight());
		g2d.draw(line);
		line.setLine(rectangle2d.getX(), rectangle2d.getY() + rectangle2d.getHeight(),
				rectangle2d.getX() + rectangle2d.getWidth(), rectangle2d.getY());
		g2d.draw(line);


		{
			Ellipse2D.Double ellipse = new Ellipse2D.Double();
			ellipse.setFrameFromCenter(canvasCenter.getX(), canvasCenter.getY(), canvasCenter.getX() - globalRadicus,
					canvasCenter.getY() - globalRadicus);
			g2d.draw(ellipse);
		}

		Arc2D.Double arc2d = new Arc2D.Double();
		arc2d.setFrameFromCenter(topCircleCenter.getX(), topCircleCenter.getY(),
				topCircleCenter.getX() - halfWorkRadius, topCircleCenter.getY() - halfWorkRadius);
		arc2d.setAngleStart(0);
		arc2d.setAngleExtent(180); // 180度表示半圆
		g2d.draw(arc2d);

		{
			g2d.setColor(Color.black);
			// canvas center
			Ellipse2D.Double ellipse = new Ellipse2D.Double();
			ellipse.setFrameFromCenter(canvasCenter.getX(), canvasCenter.getY(), canvasCenter.getX() - 5,
					canvasCenter.getY() - 5);
			g2d.fill(ellipse);

		}
	}
}
