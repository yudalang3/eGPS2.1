package module.evolview.phylotree.visualization.layout;

import java.awt.Color;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import evoltree.txtdisplay.TreeDrawUnit;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public abstract class CicularLayout extends BaseLayout {
	public CicularLayout(TreeLayoutProperties controller, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode, phylogeneticTreePanel);
	}

	@Override
	protected TracingPaintingProperty configurateTracingShape(GraphicsNode node, Color color) {
		TracingPaintingProperty tracingPaintingProperty = new TracingPaintingProperty();
		
		GeneralPath generalPath = new GeneralPath();

		double xSelf = node.getXSelf();
		double ySelf = node.getYSelf();

		int centerX = (int) (currentWidth / 2);
		int centerY = (int) (currentHeight / 2);

		TreeDrawUnit drawUnit = node.getDrawUnit();

		generalPath.moveTo(xSelf, ySelf);

		drawUnit.setNodeSelected(true);

		GraphicsNode tempNode = node;
		while (tempNode.getParentCount() > 0) {
			double currentAngle = tempNode.getAngleIfNeeded();

			tempNode = (GraphicsNode) tempNode.getParent();

			double radicus = tempNode.getRadicusIfNeeded();
			double parentAngle = tempNode.getAngleIfNeeded();

			Arc2D.Double arcDrawer = new Arc2D.Double(Arc2D.OPEN);

			arcDrawer.setArcByCenter(centerX, centerY, radicus, Math.max(currentAngle, parentAngle),
					-Math.abs(currentAngle - parentAngle), Arc2D.OPEN);

			generalPath.append(arcDrawer, true);
		}

		tracingPaintingProperty.path = generalPath;
		tracingPaintingProperty.pointOfNode = new Point2D.Double(node.getXSelf(), node.getYSelf());
		tracingPaintingProperty.fillColor = color;

		return tracingPaintingProperty;
	}

	/**
	 * @param leaves
	 * @return
	 * @category annotation
	 */
	protected double getRadicusForAnnotation(GraphicsNode[] leaves) {
		double radicus = 0;
		for (GraphicsNode nn : leaves) {
			double tmp = nn.getRadicusIfNeeded();
			if (tmp > radicus) {
				radicus = tmp;
			}
		}

		if (treeLayoutProperties.getShowLeafPropertiesInfo().isShowLeafLabel()) {
			int maxLengthLeafNameWidthAccording2CurrentGlobalFont = getMaxLengthLeafNameWidthAccording2CurrentFont();
			radicus += maxLengthLeafNameWidthAccording2CurrentGlobalFont;
		} else {
			radicus += 15;

		}

		return radicus;
	}
}
