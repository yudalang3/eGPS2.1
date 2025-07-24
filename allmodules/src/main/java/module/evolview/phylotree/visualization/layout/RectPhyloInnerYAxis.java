package module.evolview.phylotree.visualization.layout;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import evoltree.struct.util.EvolNodeUtil;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

public class RectPhyloInnerYAxis extends RectPhyloLayout {

	private int myTargetMiddle;
	private List<GraphicsNode> allLeaves;

	public RectPhyloInnerYAxis(TreeLayoutProperties controller, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		super(controller, rootNode, phylogeneticTreePanel);
	}

	@Override
	protected void beforeCalculate(int width, int height) {
		super.beforeCalculate(width, height);

		allLeaves = EvolNodeUtil.getLeavesByRecursive(rootNode);
		int size = allLeaves.size();
		myTargetMiddle = size / 2;

	}

	protected void assignLocation(GraphicsNode node, double cumulateBranchLengthExceptCurrent, double basey) {

		double xx = blankArea.getLeft();
		xx += cumulateBranchLengthExceptCurrent * canvas2logicRatio;
		node.setXParent(xx);
		node.setXSelf(node.getDisplayedBranchLength() * canvas2logicRatio + xx);

//		System.out.println("size is:" + node.getSize());
		double yDrawAxis = getYAxis(node, basey);
		node.setYParent(yDrawAxis);
		node.setYSelf(yDrawAxis);
		node.setAngleIfNeeded(0.0);
	}

	private double getYAxis(GraphicsNode node, double basey) {
		if (node.getChildCount() == 0) {
			return basey;
		} else {
			List<GraphicsNode> leaves = EvolNodeUtil.getLeavesByRecursive(node);
			GraphicsNode median = findMedian(leaves);
			double ySelf = median.getYSelf();
			return ySelf;
		}
	}

	public GraphicsNode findMedian(List<GraphicsNode> leaves) {
		int size = leaves.size();

		// 如果List为空，返回null
		if (size == 0) {
			return null;
		}

		GraphicsNode o = leaves.get(0);
		if (size > 9) {
			System.out.println();
		}
		int ret = allLeaves.indexOf(o);
		int miniDeviation = FastMath.abs(ret - myTargetMiddle);
		ret = 0;

		for (int i = 1; i < size; i++) {
			int indexOf = allLeaves.indexOf(leaves.get(i));
			int deviation = FastMath.abs(indexOf - myTargetMiddle);
			if (deviation < miniDeviation) {
				ret = i;
				miniDeviation = deviation;
			}
		}

		// 如果是奇数，返回中间那个节点
		return leaves.get(ret);
	}

}
