package module.evolview.phylotree.visualization.layout;

import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.phylotree.visualization.graphics.struct.CircularLayoutProperty;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator;

public class CircularInnerPhylo extends CircularPhylo{

	public CircularInnerPhylo(TreeLayoutProperties controller, GraphicsNode rootNode,PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode,phylogeneticTreePanel);
	}

	@Override
	protected void calculateRatioAndChooseStartLength() {
		CircularLayoutProperty circularLayoutPropertiy = treeLayoutProperties.getCircularLayoutPropertiy();
		
		double avaliableLength = biggestCircleRadicus -circularLayoutPropertiy.getInnerCircleRadicus();
		canvas2logicRatio = avaliableLength / GraphicTreePropertyCalculator.getMaxLengthOfRoot2Leaf(rootNode).getLength();
		
		recursiveCalculateStartLength = (int) biggestCircleRadicus;
		
		
		this.scaleBarProperty.setIfDrawScaleBar(false);
		
	}
	
	@Override
	protected double calculateRadicus(double cumulateRealBranchLengthExceptCurrent, GraphicsNode node) {
		double radicus = cumulateRealBranchLengthExceptCurrent - 
				node.getDisplayedBranchLength() * canvas2logicRatio;
		return radicus;
	}

}
