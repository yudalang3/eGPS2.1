package module.evolview.phylotree.visualization.graphics.struct.util;

import java.util.ArrayList;
import java.util.List;

import module.evolview.gfamily.work.model.tree.GraphicsNode;
import evoltree.struct.util.EvolTreeOperator;
import module.evolview.phylotree.visualization.graphics.struct.NodeType;
import module.evolview.phylotree.visualization.graphics.struct.TreeDecideUtil;

/**
 * 这个类是实现树的一些性质计算或者改变操作！<br>
 *
 * @author yjn, ydl
 */

public class GraphicTreePropertyCalculator {

	private static double tempVariable;
	private static GraphicsNode originalNode;
	private static GraphicsNode tempCalculateNode;

	/**
	 * 计算从根到叶子的最长距离（包括外群）<br>
	 * 不包括，root长度
	 */
	public static LongestRoot2leafBean getMaxLengthOfRoot2Leaf(GraphicsNode node) {
		originalNode = node;
		tempVariable = 0;
		recursive2getMaxValue(node, 0);
		originalNode = null;
		GraphicsNode retNode = tempCalculateNode;
		tempCalculateNode = null;
		return new LongestRoot2leafBean(tempVariable, retNode);
	}

	/**
	 * 计算从根到叶子的最长距离（包括外群）调用此递归方法<br>
	 * 不包括，root长度
	 */
	private static void recursive2getMaxValue(GraphicsNode node, double len) {
		double newLength = len + node.getDisplayedBranchLength();
		// System.out.println(newLength);

		NodeType decideNodeType = TreeDecideUtil.decideNodeType(node, originalNode);
		switch (decideNodeType) {
		case LEAF:
			// System.out.println("its leaf\t"+newLength);
			if (newLength > tempVariable) {
				tempVariable = newLength;
				tempCalculateNode = node;
			}
			break;
		default:
			if (decideNodeType == NodeType.ROOT) {
				newLength = 0;
			}
			int childCount = node.getChildCount();
			for (int j = 0; j < childCount; j++) {
				recursive2getMaxValue((GraphicsNode) node.getChildAt(j), newLength);
			}
			break;
		}
	}

	/**
	 * 注意 collapse的node变成了叶子
	 * 
	 * @param rootNode
	 * @return
	 */
	public static List<GraphicsNode> getLeaves(GraphicsNode rootNode) {
		originalNode = rootNode;
		List<GraphicsNode> leaves = new ArrayList<GraphicsNode>(8192);
		recursive2getLeafNumber(rootNode, leaves);
		originalNode = null;

		return leaves;
	}

	/**
	 * 注意 collapse的node变成了叶子
	 * 
	 * @param rootNode
	 * @return
	 */
	public static int getLeafNumber(GraphicsNode rootNode) {
		return getLeaves(rootNode).size();
	}

	private static void recursive2getLeafNumber(GraphicsNode node, List<GraphicsNode> leaves) {

		NodeType decideNodeType = TreeDecideUtil.decideNodeType(node, originalNode);
		switch (decideNodeType) {
		case LEAF:
			leaves.add(node);
			break;
		default:
			int childCount = node.getChildCount();
			for (int j = 0; j < childCount; j++) {
				recursive2getLeafNumber((GraphicsNode) node.getChildAt(j), leaves);
			}
			break;
		}
	}

	public static GraphicsNode clone(GraphicsNode root) {
		GraphicsNode newroot = new GraphicsNode();

		newroot.setID(root.getID());

		newroot.setDepth(root.getDepth());
		newroot.setDisplayedBranchLength(root.getDisplayedBranchLength());
		newroot.setDrawUnit(root.getDrawUnit());
		newroot.setName(root.getName());


		newroot.setSize(root.getSize());
		newroot.setCgbIDFirst(root.getCgbIDFirst());
		newroot.setCgbIDLast(root.getCgbIDLast());

		if (root.getChildCount() != 0) {
			int childCount = root.getChildCount();
			for (int i = 0; i < childCount; i++) {
				GraphicsNode child = (GraphicsNode) root.getChildAt(i);
				boolean success = newroot.setChildAt(i, clone(child));
			}
		}
		return newroot;
	}

	/**
	 * 返回 null如果两个node都不是同一个root来的！
	 *
	 * @param node1
	 * @param node2
	 * @return
	 */
	public static GraphicsNode getMostRecentCommonAnsester(GraphicsNode node1, GraphicsNode node2) {
		GraphicsNode mostRecentCommonAncestor = EvolTreeOperator.getMostRecentCommonAncestor(node1, node2);
		return mostRecentCommonAncestor;
	}

	public static void main(String[] args) throws CloneNotSupportedException {

		GraphicsNode r = new GraphicsNode("root");
		GraphicsNode I1 = new GraphicsNode("I1");
		I1.setRealBranchLength(0.1);
		GraphicsNode I2 = new GraphicsNode("I2");
		I2.setRealBranchLength(0.1);
		r.addChild(I1);
		r.addChild(I2);
		GraphicsNode L1 = new GraphicsNode("L1");
		L1.setRealBranchLength(0.2);
		GraphicsNode L2 = new GraphicsNode("L2");
		L2.setRealBranchLength(0.2);
		I1.addChild(L1);
		I1.addChild(L2);
		GraphicsNode I3 = new GraphicsNode("I3");
		I3.setRealBranchLength(0.1);

		GraphicsNode L3 = new GraphicsNode("L3");
		L3.setRealBranchLength(0.2);
		GraphicsNode L4 = new GraphicsNode("L4");
		L4.setRealBranchLength(0.2);
		GraphicsNode L5 = new GraphicsNode("L5");
		L5.setRealBranchLength(0.2);

		I2.addChild(I3);
		I2.addChild(L5);
		I3.addChild(L3);
		I3.addChild(L4);

		GraphicsNode c2 = GraphicTreePropertyCalculator.clone(r);
		System.out.println(r);

		System.out.println(c2);

	}

	public static class LongestRoot2leafBean{
		private double length;
		private GraphicsNode leaf;
		
		public LongestRoot2leafBean(double length, GraphicsNode leaf) {
			super();
			this.length = length;
			this.leaf = leaf;
		}

		public double getLength() {
			return length;
		}

		public GraphicsNode getLeaf() {
			return leaf;
		}

	}

}
