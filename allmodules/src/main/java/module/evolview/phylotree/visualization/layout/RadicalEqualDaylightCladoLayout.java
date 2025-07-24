package module.evolview.phylotree.visualization.layout;

import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public class RadicalEqualDaylightCladoLayout extends RadicalEqualDaylightPhyloLayout {

	public RadicalEqualDaylightCladoLayout(TreeLayoutProperties controller, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode, phylogeneticTreePanel);
	}

	@Override
	public void calculateForPainting(int width, int height) {
		// 让所有的枝长都一样！
		TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
			if (node.getParentCount() == 0) {
				node.setDisplayedBranchLength(0);
			} else {
				node.setDisplayedBranchLength(0.1);
			}
		});

		super.calculateForPainting(width, height);
		// 恢复枝长
		TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
			node.setDisplayedBranchLength(node.getRealBranchLength());
		});
	}
}
