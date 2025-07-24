package module.remnant.treeoperator.evoTree;

public class NodeUtil {
	
	public static IEvoNode getAEvoNode() {
		IEvoNode root = new TreeNode(1);
		
		return root;
	}

	public static IEvoNode cloneNode(IEvoNode root) {
		IEvoNode newRoot = root.getClone();
		copyTheNode(newRoot, root);

		return newRoot;
	}

	private static void copyTheNode(IEvoNode newNode, IEvoNode oldNode) {
		for (int i = 0; i < oldNode.getChildCount(); i++) {
			IEvoNode oldChild = oldNode.getChildAt(i);
			IEvoNode newChild = oldChild.getClone();
			newNode.addChild(newChild);

			copyTheNode(newChild, oldChild);
			// System.out.println(newNode + " -> " + newChild);
		}
	}
}
