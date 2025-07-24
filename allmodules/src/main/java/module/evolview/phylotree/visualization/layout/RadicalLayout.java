package module.evolview.phylotree.visualization.layout;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public abstract class RadicalLayout extends CicularLayout {


	public RadicalLayout(TreeLayoutProperties controller, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode, phylogeneticTreePanel);
	}

	protected void unRootedhorizontalLine(Graphics2D g2d, final GraphicsNode node) {
		commonDrawEachUnit(g2d, node, true);
	}

	protected double getRadicusForAnnotation(GraphicsNode[] leaves, double centerX, double centerY) {

		Point2D.Double point = new Point2D.Double(centerX, centerY);
		double biggestRadicus = getRadicus(leaves[0], point);

		int length = leaves.length;

		for (int i = 1; i < length; i++) {
			double radicus = getRadicus(leaves[i], point);
			if (radicus > biggestRadicus) {
				biggestRadicus = radicus;
			}
		}

		if (treeLayoutProperties.getShowLeafPropertiesInfo().isShowLeafLabel()) {
			int maxLengthLeafNameWidthAccording2CurrentGlobalFont = getMaxLengthLeafNameWidthAccording2CurrentFont();
			biggestRadicus += maxLengthLeafNameWidthAccording2CurrentGlobalFont;
		} else {
			biggestRadicus += 15;

		}

		return biggestRadicus;
	}

	protected double getRadicus(GraphicsNode graphicsNode, Point2D point) {
		return point.distance(graphicsNode.getXSelf(), graphicsNode.getYSelf());
	}

}
