package module.evolview.phylotree.visualization.layout;

import java.util.List;

import module.evolview.phylotree.visualization.graphics.struct.TreeDecideUtil;
import evoltree.struct.util.EvolNodeUtil;
import module.evolview.gfamily.work.gui.DrawUtil;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.phylotree.visualization.graphics.struct.NodeType;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public class RadicalEqualDaylightPhyloLayout extends RadialPhyloLayout {

	protected double oneUnitDegree = 180 / Math.PI;
	private List<GraphicsNode> leaves;

	public RadicalEqualDaylightPhyloLayout(TreeLayoutProperties controller, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode, phylogeneticTreePanel);
	}

	/**
	 * 租用 xParent和yParent存储为节点的角度
	 */

	@Override
	public void calculateForPainting(int width, int height) {

		if (rootNode.getSize() < 4) {
			EvolNodeUtil.initializeSize(rootNode);
		}

		beforeCalculate(width, height);
		recursizeCalculate(rootNode, 0, 2 * Math.PI);
		System.out.println("--------------");

		int radicalLayoutRotationDeg = treeLayoutProperties.getRadicalLayoutRotationDeg();
		if (radicalLayoutRotationDeg != 0) {
			DrawUtil.rotationTransform(rootNode, radicalLayoutRotationDeg, currentWidth, currentHeight);
		}

		leaves = EvolNodeUtil.getLeaves(rootNode);

		recursizeAssignEqualDaylight(rootNode);

		afterCalculation();
	}

	private void recursizeAssignEqualDaylight(GraphicsNode node) {
		NodeType nodeType = TreeDecideUtil.decideNodeType(node, rootNode);
		switch (nodeType) {
		case LEAF:
			break;
		default:
			if (nodeType == NodeType.ROOT) {

			} else {

			}
			for (int i = 0; i < node.getChildCount(); i++) {

				GraphicsNode child = (GraphicsNode) node.getChildAt(i);

				recursizeAssignEqualDaylight(child);
			}

			break;
		}

	}

}
