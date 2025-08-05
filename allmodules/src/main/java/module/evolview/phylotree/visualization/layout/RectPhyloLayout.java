
package module.evolview.phylotree.visualization.layout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.google.common.base.Strings;

import module.evolview.gfamily.work.gui.DrawUtil;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNode2LeafAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNodeInsituAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropOutterSidewardAnno;
import module.evolview.gfamily.work.gui.tree.annotation.OutterSidewardLocation;
import module.evolview.phylotree.visualization.graphics.struct.NodeType;
import module.evolview.model.tree.AnnotationsProperties;
import module.evolview.model.tree.AnnotationsProperties4LinageType;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.ShowLeafPropertiesInfo;
import module.evolview.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator.LongestRoot2leafBean;
import module.evolview.phylotree.visualization.graphics.struct.TreeDecideUtil;

public class RectPhyloLayout extends RectangularLayout {

	public RectPhyloLayout(TreeLayoutProperties controller, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode, phylogeneticTreePanel);
	}

	@Override
	protected void beforeCalculate(int width, int height) {
		super.beforeCalculate(width, height);

		loop = 0;
		// x axis
		LongestRoot2leafBean maxLengthOfRoot2LeafBean = GraphicTreePropertyCalculator.getMaxLengthOfRoot2Leaf(rootNode);
		double maxLengthOfRoot2Leaf = maxLengthOfRoot2LeafBean.getLength();

		canvas2logicRatio = blankArea.getWorkWidth((int) currentWidth) / maxLengthOfRoot2Leaf;
		// y axis
		eachYGapLength = blankArea.getWorkHeight((int) currentHeight)
				/ (GraphicTreePropertyCalculator.getLeafNumber(rootNode) - 1.0);

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
		root.setDisplayedBranchLength(0);
		recursiveCalculate(root, 0);

		this.scaleBarProperty.setIfDrawScaleBar(true);

		afterCalculation();
	}

	@Override
	protected void configurateMainAnnotation(AnnotationsProperties annotationsProperties) {

		List<DrawPropInternalNode2LeafAnno> internalNode2LeafAnnos = annotationsProperties.getInternalNode2LeafAnnos();
		for (DrawPropInternalNode2LeafAnno tt : internalNode2LeafAnnos) {
			if (tt.shouldConfigurateAndPaint()) {
				GraphicsNode currentGraphicsNode = tt.getCurrentGraphicsNode();
				List<GraphicsNode> leaves = GraphicTreePropertyCalculator.getLeaves(currentGraphicsNode);
				GraphicsNode firstLeaf = leaves.get(0);
				GraphicsNode lastLeaf = leaves.get(leaves.size() - 1);

				double leftMostX = currentGraphicsNode.getXSelf();
				double topMostY = firstLeaf.getYSelf();
				double buttomMostY = lastLeaf.getYSelf();
				double rightMostX = 0;
				for (GraphicsNode nn : leaves) {
					double xSelf = nn.getXSelf();
					if (xSelf > rightMostX) {
						rightMostX = xSelf;
					}
				}
				Rectangle2D.Double rect = new Rectangle2D.Double(leftMostX, topMostY, rightMostX - leftMostX,
						buttomMostY - topMostY);
				tt.setDrawShape(rect);
			}
		}
		// 这个注释要特殊处理
		// List<DrawPropLeafNameAnno> leafNameAnnos =
		// annotationsProperties.getLeafNameAnnos();
		annotationsProperties.configurateLeafNamesAnnotaion(false);

		List<DrawPropOutterSidewardAnno> outterSidewardAnnos = annotationsProperties.getOutterSidewardAnnos();
		for (DrawPropOutterSidewardAnno tt : outterSidewardAnnos) {
			if (tt.shouldConfigurateAndPaint()) {
				GraphicsNode currentGraphicsNode = tt.getCurrentGraphicsNode();
				List<GraphicsNode> leaves = GraphicTreePropertyCalculator.getLeaves(currentGraphicsNode);
				GraphicsNode firstLeaf = leaves.get(0);
				GraphicsNode lastLeaf = leaves.get(leaves.size() - 1);

				double topMostY = firstLeaf.getYSelf();
				double buttomMostY = lastLeaf.getYSelf();

				double xx = getRightMostXLocation(leaves) + 5;
				Line2D.Double rect = new Line2D.Double(xx, topMostY, xx, buttomMostY);
				tt.setTextLocationAndShape(xx, 0.5 * (topMostY + buttomMostY), rect);
			}
		}
		List<DrawPropInternalNodeInsituAnno> internalNode2LeafInsituAnnos = annotationsProperties
				.getInternalNode2LeafInsituAnnos();
		for (DrawPropInternalNodeInsituAnno tt : internalNode2LeafInsituAnnos) {
			if (tt.shouldConfigurateAndPaint()) {
				tt.configurate();
			}
		}

	}

	@Override
	protected void configurateLinageTypeAnnotation(AnnotationsProperties4LinageType annotationsProperties) {
		final double rightMostXLocationOfLeaf = getRightMostXLocation(GraphicTreePropertyCalculator.getLeaves(rootNode)) + 5;
		linageTypeSidewardAnnotationCalculator = new Function<LinkedList<GraphicsNode>, OutterSidewardLocation>() {

			@Override
			public OutterSidewardLocation apply(LinkedList<GraphicsNode> t) {

				GraphicsNode first = t.getFirst();
				GraphicsNode last = t.getLast();

				double y1 = first.getYSelf();
				double y2 = last.getYSelf();
				if (y1 == y2) {
					y1 -= 1;
					y2 += 1;
				}

				OutterSidewardLocation ret = new OutterSidewardLocation();
				Line2D.Double temp = new Line2D.Double(rightMostXLocationOfLeaf, y1,
						rightMostXLocationOfLeaf, y2);

				ret.setShape(temp);
				ret.setTextX((float) rightMostXLocationOfLeaf);
				ret.setTextY((float) (0.5 * (y1 + y2)));
				return ret;
			}
		};

		linageTypeNode2LeafAnnotationCalculator = new Function<LinkedList<GraphicsNode>, OutterSidewardLocation>() {

			@Override
			public OutterSidewardLocation apply(LinkedList<GraphicsNode> t) {

				GraphicsNode first = t.getFirst();
				GraphicsNode last = t.getLast();

				double y1 = first.getYSelf();
				double y2 = last.getYSelf();
				if (y1 == y2) {
					y1 -= 1;
					y2 += 1;
				}

				double rightMostX = getRightMostXLocation4node2leafAnno(t);
				GraphicsNode mrca = GraphicTreePropertyCalculator.getMostRecentCommonAnsester(first, last);

				OutterSidewardLocation ret = new OutterSidewardLocation();
				double xSelf = mrca.getXSelf();
				Rectangle2D.Double temp = new Rectangle2D.Double(xSelf, y1, rightMostX - xSelf, y2 - y1);
				ret.setShape(temp);
				ret.setTextX((float) (xSelf - 25));
				ret.setTextY((float) (0.5 * (y1 + y2)));
				return ret;
			}
		};

		super.configurateLinageTypeAnnotation(annotationsProperties);

	}

	/**
	 * 
	 * @param node
	 * @param cumulateBranchLengthExceptCurrent
	 * 
	 *                                          这个传入的0不是绝对距离， 而是branch，初始化的branch!!!
	 */
	protected void recursiveCalculate(GraphicsNode node, double cumulateBranchLengthExceptCurrent) {

		NodeType nodeType = TreeDecideUtil.decideNodeType(node, rootNode);
		switch (nodeType) {
		case LEAF:
			double basey = blankArea.getTop() + loop * eachYGapLength;
			assignLocation(node, cumulateBranchLengthExceptCurrent, basey);
			loop++;
			break;
		default:
			for (int i = 0; i < node.getChildCount(); i++) {
				recursiveCalculate((GraphicsNode) node.getChildAt(i),
						cumulateBranchLengthExceptCurrent + node.getDisplayedBranchLength());
			}
			basey = treeLayoutProperties.averageValueOfChildrenForParent(node);

			assignLocation(node, cumulateBranchLengthExceptCurrent, basey);

			if (nodeType == NodeType.ROOT) {
				node.setXParent(node.getXSelf() - treeLayoutProperties.getRootTipLength());
			}
			break;
		}
	}

	protected void assignLocation(GraphicsNode node, double cumulateBranchLengthExceptCurrent, double basey) {
		node.setDoubleVariable(basey);

		double xx = blankArea.getLeft();
		xx += cumulateBranchLengthExceptCurrent * canvas2logicRatio;
		node.setXParent(xx);
		node.setXSelf(node.getDisplayedBranchLength() * canvas2logicRatio + xx);

		double yDrawAxis = basey;
		node.setYParent(yDrawAxis);
		node.setYSelf(yDrawAxis);
		node.setAngleIfNeeded(0.0);
	}

	@Override
	protected void specificTreeDrawingProcess(Graphics2D g2d) {
		drawButtomAxis(g2d);
	}

	protected void drawButtomAxis(Graphics2D g2d) {
		if (is4AnnotationDialog) {
			return;
		}

		if (!treeLayoutProperties.isShowAxisBar()) {
			return;
		}

		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(1f));
		g2d.setFont(treeLayoutProperties.getAxisFont());
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int fontHeight = fontMetrics.getHeight();
		// 上面是基本的配置

		double yy = currentHeight - blankArea.getBottom() + 12;
		double leftBlankLength = blankArea.getLeft();
		// 这里绘制的是主轴，没问题
		lineDrawUtil.setLine(leftBlankLength, yy, currentWidth - blankArea.getRight(), yy);
		g2d.draw(lineDrawUtil);

		List<String> displayedStrings = buttomScaleBarDrawProperty.getDisplayedStrings();
		List<Double> displayeDoubles = buttomScaleBarDrawProperty.getDisplayeDoubles();
		int count = displayedStrings.size();

		for (int i = 0; i < count; i++) {
			Double xValue = displayeDoubles.get(i);
			int xx = (int) (leftBlankLength + xValue);
			double verticalTipLength = yy + 10;
			lineDrawUtil.setLine(xx, yy, xx, verticalTipLength);
			g2d.draw(lineDrawUtil);

			final String str = displayedStrings.get(i);

			float xStr = (float) (xx - 0.5 * fontMetrics.stringWidth(str));
			g2d.drawString(str, xStr, (float) (verticalTipLength + fontHeight));
		}

		String branchLengthUnit = treeLayoutProperties.getBranchLengthUnit();
		if (!Strings.isNullOrEmpty(branchLengthUnit)) {
			g2d.setColor(Color.blue);
			g2d.drawString(branchLengthUnit, (float) (currentWidth - blankArea.getRight() + 10), (float) (yy + fontHeight));
		}

	}

	@Override
	protected void leafLineDrawingProcess(Graphics2D g2d, GraphicsNode node) {
		g2d.setColor(node.getDrawUnit().getLineColor());
		drawEachLines(g2d, node);

	}

	@Override
	protected void rootDrawingProcess(Graphics2D g2d, GraphicsNode node) {

		DrawUtil.drawRootTip(g2d, node, treeLayoutProperties);

	}

	@Override
	protected void innerNodeDrawingProcess(Graphics2D g2d, GraphicsNode node) {
		g2d.setColor(node.getDrawUnit().getLineColor());
		drawEachLines(g2d, node);
	}

}
