package module.evolview.phylotree.visualization.layout;

import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.model.tree.TreeLayoutProperties;

public class SprialCladoAlignedWithAlpha extends SpiralPhyloWithAlpha {

	
	public SprialCladoAlignedWithAlpha(TreeLayoutProperties controller, GraphicsNode rootNode,PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode,phylogeneticTreePanel);
	}

	@Override
	public void calculateForPainting(int width, int height) {
		// 让所有的枝长都一样！
		TreeOperationUtil.letAllNodesBeEqualLength(rootNode);

		super.calculateForPainting(width, height);
		// 恢复枝长
		TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
			node.setDisplayedBranchLength(node.getRealBranchLength());

			if (node.getChildCount() == 0) {
				node.setRadicusIfNeeded(maxAlpha);
				assignLocation(node);
			}
		});
		
		scaleBarProperty.setIfDrawScaleBar(false);
	}

}
