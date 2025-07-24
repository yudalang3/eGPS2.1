package module.evolview.phylotree.visualization.layout;

import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public class CircularInnerCladoAligned extends CircularInnerPhylo{

	public CircularInnerCladoAligned(TreeLayoutProperties controller, GraphicsNode rootNode,PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode,phylogeneticTreePanel);
	}

	@Override
	public void calculateForPainting(int width, int height) {
		
		TreeOperationUtil.letAllNodesBeEqualLength(rootNode);
		
		super.calculateForPainting(width, height);
		// 恢复枝长
		int innerCircleRadicus = treeLayoutProperties.getCircularLayoutPropertiy().getInnerCircleRadicus();
		TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
			node.setDisplayedBranchLength(node.getRealBranchLength());

			if (node.getChildCount() == 0) {
				node.setRadicusIfNeeded(innerCircleRadicus);
				assignLocation(node);
			}
		});
		this.scaleBarProperty.setIfDrawScaleBar(false);
	}
	
	protected boolean isLayoutSupportDrawScaleBar() {
		return true;
	}

}
