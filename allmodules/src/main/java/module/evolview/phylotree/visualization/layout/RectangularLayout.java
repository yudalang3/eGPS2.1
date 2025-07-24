package module.evolview.phylotree.visualization.layout;

import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.util.List;

import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.RectangularLayoutProperty;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public abstract class RectangularLayout extends BaseLayout {

	protected double eachYGapLength;
	protected int loop = 0;
	
	private CubicCurve2D.Double cubicDouble = new CubicCurve2D.Double();

	public RectangularLayout(TreeLayoutProperties controller, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode, phylogeneticTreePanel);
	}

	protected void drawEachLines(Graphics2D g2d, GraphicsNode node) {
		double x1 = node.getXParent();
		double x2 = node.getXSelf();
		double y1 = node.getYParent();
		double y2 = node.getYSelf();

		GraphicsNode parent = (GraphicsNode) node.getParent();
		double parentX = parent.getXSelf();
		double parentY = parent.getYSelf();

		commonDrawEachUnit(g2d, node, false);
		configCuratureCurve(g2d, parentX, parentY, x1, y1, x2, y2);
	}

	private void configCuratureCurve(Graphics2D g2d, double parentX, double parentY, double x1, double y1, double x2,
			double y2) {
		
		RectangularLayoutProperty rectangularLayoutPropertiy = treeLayoutProperties.getRectangularLayoutPropertiy();
		int curvature = rectangularLayoutPropertiy.getCurvature();

		if (curvature == 0) {
			g2d.drawLine((int) x1, (int) y1, (int) parentX, (int) parentY);
			g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		} else {
			
			double controlDist = 0.01 * curvature * (x2 - parentX);
			double ctrlx1 = parentX + controlDist;
			double ctrlx2 = x2 - controlDist;
			cubicDouble.setCurve(parentX, parentY, ctrlx1, parentY, ctrlx2, y2, x2, y2);
			
//			double controlDist = 0.01 * curvature * (y2 - parentY);
//			cubicDouble.setCurve(parentX, parentY, parentX , parentY + controlDist, x2, y2 - controlDist, x2, y2);
			
			g2d.draw(cubicDouble);
		}
	}

	/**
	 * This is for the annotation utility!
	 * 这个是给 sideward 注释准备的！
	 *
	 * @param leaves
	 * @return
	 * @category annotation
	 */
	protected double getRightMostXLocation(List<GraphicsNode> leaves) {
		double rightMostX = 0;

		for (GraphicsNode nn : leaves) {
			double xSelf = nn.getXSelf() ;
			
			if (xSelf > rightMostX) {
				rightMostX = xSelf;
			}
		}
		if (treeLayoutProperties.getShowLeafPropertiesInfo().isShowLeafLabel()) {
			int maxLengthLeafNameWidthAccording2CurrentGlobalFont = getMaxLengthLeafNameWidthAccording2CurrentFont();
			rightMostX += maxLengthLeafNameWidthAccording2CurrentGlobalFont;
		} else {
			rightMostX += 15;
			
		}

		return rightMostX;
	}
	
	protected double getRightMostXLocation4node2leafAnno(List<GraphicsNode> t) {
		double rightMostX = 0;
		for (GraphicsNode graphicsNode : t) {
			if (graphicsNode.getXSelf() > rightMostX) {
				rightMostX = graphicsNode.getXSelf();
			}
		}
		return rightMostX;
	}

	public double getEachYGapLength() {
		return eachYGapLength;
	}
}
