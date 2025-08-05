package module.evolview.gfamily.work.gui.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;
import module.evolview.phylotree.visualization.graphics.struct.NodeType;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.TreeDecideUtil;

public class TreeOperationUtil {

	/**
	 * 一个快捷的Util工具，用来遍历我们的树结构！然后对每个节点做一些操作！例如赋值 这个是internal node first的遍历！
	 * IF是internal node first的缩写！
	 * 
	 * @param rootNode
	 * @param func     : 如果返回 ture就退出！
	 */
	public static void recursiveIterateTreeIF(GraphicsNode rootNode, Predicate<GraphicsNode> func) {
		EvolNodeUtil.recursiveIterateTreeIFWithStop(rootNode, func);
	}

	/**
	 * 一个快捷的Util工具，用来遍历我们的树结构！然后对每个节点做一些操作！例如赋值 这个是internal node first的遍历！
	 * IF是internal node first的缩写！
	 * 
	 * @param rootNode
	 * @param func
	 */
	public static void recursiveIterateTreeIF(GraphicsNode rootNode, Consumer<GraphicsNode> func) {
		EvolNodeUtil.recursiveIterateTreeIF(rootNode, func);
	}

	/**
	 * 一个快捷的Util工具，用来遍历我们的树结构！然后对每个节点做一些操作！例如赋值 这个是leaf first的遍历！ LF是leaf first的缩写！
	 * 
	 * @param rootNode
	 * @param func
	 */
	public static void recursiveIterateTreeLF(GraphicsNode rootNode, Consumer<GraphicsNode> func) {
		EvolNodeUtil.recursiveIterateTreeLF(rootNode, func);
	}

	public static void displayTreeWithoutGUI(GraphicsNode root) {
		TreeOperationUtil.recursiveIterateLevelTraverseWithNull(root, node -> {

			if (node == null) {
				System.out.println();
			} else {
				String parentID = "";
				if (node.getParentCount() != 0) {
					parentID += node.getParent().getID();
					parentID += " ";
					parentID += node.getID();
					parentID += "    ";
					System.out.print(parentID);
				} else {
					System.out.println("Format:ParentID NodeID");
					System.out.println("    " + node.getID());
				}

			}

		});
	}

	/**
	 * 注意这个递归函数，每个层会以 null分割！
	 * 
	 * @param rootNode
	 * @param func
	 */
	public static void recursiveIterateLevelTraverseWithNull(GraphicsNode rootNode, Consumer<GraphicsNode> func) {

		LinkedList<GraphicsNode> queue = new LinkedList<>();
		queue.offer(rootNode);
		while (!queue.isEmpty()) {
			GraphicsNode node = queue.poll();

			func.accept(node);

			if (node != null) {
				int childCount = node.getChildCount();
				for (int i = 0; i < childCount; i++) {
					GraphicsNode child = (GraphicsNode) node.getChildAt(i);
					queue.offer(child);
				}
				// 添加一个null来分隔一层！
				queue.offer(null);
			}
		}
	}

	/**
	 * 这个方式是非递归的，有个好处就是：可以中途直接退出！
	 * 
	 * @param rootNode
	 * @param func
	 */
	public static void recursiveIterateLevelTraverse(GraphicsNode rootNode, Consumer<GraphicsNode> func) {

		LinkedList<GraphicsNode> queue = new LinkedList<>();
		queue.offer(rootNode);
		while (!queue.isEmpty()) {
			GraphicsNode node = queue.poll();

			func.accept(node);

			int childCount = node.getChildCount();
			for (int i = 0; i < childCount; i++) {
				GraphicsNode child = (GraphicsNode) node.getChildAt(i);
				queue.offer(child);
			}
		}
	}

	/**
	 * 让所有的枝长都一样，但根 不设置！
	 * 这个方法用得比较频繁
	 */
	public static void letAllNodesBeEqualLength(GraphicsNode rootNode) {
		recursiveIterateTreeIF(rootNode, node -> {
			if (node.getParentCount() == 0) {
				node.setDisplayedBranchLength(0);
			} else {
				node.setDisplayedBranchLength(1);
			}
		});
	}

	/**
	 * It will break search if meet the command!
	 * 
	 * @param root       : The Node to traverse !
	 * @param prodicator : true for stop; false, continue to search !
	 * @return
	 */
	public static Optional<GraphicsNode> nodeTraverseByLevelFirst(GraphicsNode root,
			Predicate<GraphicsNode> prodicator) {
		GraphicsNode ret = null;

		LinkedList<GraphicsNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			GraphicsNode node = queue.poll();
			if (prodicator.test(node)) {
				ret = node;
				// 去掉break可以避免在叶子的枝长为零时tooltip显示内节点的不显示叶子的问题
				// break;
			}
			int childCount = node.getChildCount();

			NodeType decideNodeType = TreeDecideUtil.decideNodeType(node, root);

			switch (decideNodeType) {
			case LEAF:
				break;
			default:
				for (int i = 0; i < childCount; i++) {
					GraphicsNode child = (GraphicsNode) node.getChildAt(i);
					queue.offer(child);
				}
				break;
			}
		}

		return Optional.ofNullable(ret);
	}

	/**
	 * 不知道这个方法，还有没有用，这个complement指的是，先意会一下吧
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param graphicsNode
	 * @return
	 */
	public static List<GraphicsNode> getComplementNodes(GraphicsNode graphicsNode) {
		List<GraphicsNode> ret = new ArrayList<>();
		iterate2getComplementNodes(ret, graphicsNode);
		return ret;
	}

	private static void iterate2getComplementNodes(List<GraphicsNode> ret, GraphicsNode graphicsNode) {
		List<GraphicsNode> siblings = getSiblings(graphicsNode);
		for (GraphicsNode tt : siblings) {
			ret.add(tt);
		}

		if (graphicsNode.getParentCount() != 0) {
			iterate2getComplementNodes(ret, (GraphicsNode) graphicsNode.getParent());
		}
	}

	public static List<GraphicsNode> getSiblings(GraphicsNode node) {
		List<GraphicsNode> ret = new ArrayList<>();

		if (node.getParentCount() == 0) {
			return ret;
		}

		GraphicsNode parent = (GraphicsNode) node.getParent();
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			GraphicsNode child = (GraphicsNode) parent.getChildAt(i);
			if (child != node) {
				ret.add(child);
			}
		}
		return ret;
	}

	public static void describeNodeWithMutations(GraphicsNode node) {
		recursiveIterateTreeIF(node, tmp -> {
			StringBuilder sb = new StringBuilder();
			sb.append("ID: ").append(tmp.getID());
			sb.append("\tChildrenID: ");

			int childCount = tmp.getChildCount();
			for (int i = 0; i < childCount; i++) {
				sb.append(tmp.getChildAt(i).getID()).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);

//			List<IMutation4Rec> mutationsOfNode = tmp.getMutations();

			System.out.println(sb.toString());
		});
	}

	public static EvolNode truncateTreeWithCollapsedNode(EvolNode root, Set<String> newHashSet,
			Supplier<EvolNode> createNodeFun) {
		return convertNode(newHashSet, root, createNodeFun);
	}

	private static EvolNode convertNode(Set<String> collapsedNodes, EvolNode node, Supplier<EvolNode> createNodeFun) {

		EvolNode ret = createNodeFun.get();

		ret.setName(node.getName());
		ret.setLength(node.getLength());
		ret.setSize(node.getSize());

		if (collapsedNodes.contains(node.getName())) {
			return ret;
		}

		int childCount = node.getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				EvolNode child = node.getChildAt(i);
				EvolNode convertNode = convertNode(collapsedNodes, child, createNodeFun);
				ret.addChild(convertNode);

			}
		}
		return ret;
	}

}
