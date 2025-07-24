package module.remnant.treeoperator;

import java.util.ArrayList;
import java.util.List;

import module.remnant.treeoperator.util.GenealogyAnalyzer;
import module.evolview.gfamily.work.model.tree.GraphicsNode;

/**
 *
 * @author Haipeng Li
 * yudalang: 2024-04-28
 */
public class TreeUtility {
	protected boolean debug = false;
	protected ArrayList<NodeEGPSv1> path = new ArrayList<NodeEGPSv1>();

	/**
	 * Reroot the tree at the mid-point between two most distant external nodes.
	 * 
	 * @author yudalang
	 */
	public NodeEGPSv1 rootAtMidPoint(NodeEGPSv1 root) {

		double longest = 0.0e0;
		GenealogyAnalyzer ga = new GenealogyAnalyzer();
		ga.setGenealogy(root);

		List<NodeEGPSv1> leafList = ga.getLeaves();
		NodeEGPSv1[] leafs = leafList.toArray(new NodeEGPSv1[leafList.size()]);
		// System.out.println("number of leafs="+leafs.length);

		NodeEGPSv1 midA = null, midB = null;
		for (int i = 0; i < leafs.length - 1; i++) {
			// System.out.println(i+" "+leafs[i]);

			for (int j = i + 1; j < leafs.length; j++) {
				double x = distanceFromTree(leafs[i], leafs[j]);
				if (x > longest) {
					midA = leafs[i];
					midB = leafs[j];
					longest = x;
				}
			} // ~for(int j=i+1; j<leafs.l...
		} // ~for(int i=0; i<leafs.len...

		// System.out.println(midA+"\n"+midB);

		/*
		 * java.text.DecimalFormat decimal = new java.text.DecimalFormat("#.####");
		 * coalescent.util.TreeCoder.decimal = decimal;
		 * System.out.println(coalescent.util.TreeCoder.code(tree.getRoot()));
		 * coalescent.util.GenealogyAnalyzer ga = new
		 * coalescent.util.GenealogyAnalyzer(); System.out.println("midA = " +
		 * midA.getName() + " midB = " + midB.getName() + " longest = " + longest);
		 */

		NodeEGPSv1 mrca = MRCA(midA, midB);
		for (int kk = 0; kk < 2; kk++) {
			double x = 0.0e0;
			NodeEGPSv1 tmp = midA;
			if (kk == 1)
				tmp = midB;

			while (true) {
				double bl = Math.max(0.0e0, tmp.getBranch().getLength());
				if (x + bl >= longest / 2) {
					if (debug)
						System.out.println("The root is at branch between nodes " + tmp.getName() + " "
								+ (tmp.getParent()).getName() + " bl=" + tmp.getBranch().getLength() + "\n"
								+ "distance to node " + tmp.getName() + " is " + (longest / 2 - x));
					break;
				}

				x += bl;
				tmp = tmp.getParent();
				if (tmp == mrca)
					break;
			} // ~while(true)...

			if (tmp != mrca) {
				root = setRootAt(root, tmp, longest / 2 - x);
				/*
				 * List leaves = ga.getLeafsOfDescendants(tmp); String str = ""; for (int i = 0;
				 * i < leaves.size(); i ++) { str += ((Node)(leaves.get(i))).getName(); if (i <
				 * leaves.size() - 1) str += "-"; } System.out.println("Rooted at " + str + ": "
				 * + (longest/2-x));
				 * System.out.println(coalescent.util.TreeCoder.code(tree.getRoot()));
				 */

				return root;
			}
		} // ~for(int kk=0; kk<2; kk++...

		return null;
	}

	/**
	 * Find the most recent common anestor of nodes i and j
	 * 
	 * @author yudalang
	 */
	public NodeEGPSv1 MRCA(NodeEGPSv1 a, NodeEGPSv1 b) {
		NodeEGPSv1 next = a;
		NodeEGPSv1 commonAncestor;
		while (true) {
			commonAncestor = next.getParent();
			if (inThePath(b, commonAncestor))
				break;
			next = commonAncestor;
		}

		return commonAncestor;
	}

	/**
	 * Compute the distance between two nodes which the path is through the
	 * tree!<br>
	 * <b>Note:</b> if Node a is child of Node b or else, you should use
	 * {@link #upToAncestor(NodeEGPSv1, NodeEGPSv1)}
	 * 
	 * <pre>
	 * e.g. node leaf1 to node leaf2 is 10 '-'.
	 *   |----leaf1
	 * --|
	 *   |------leaf2
	 * </pre>
	 *
	 * @author yudalang
	 */
	public double distanceFromTree(NodeEGPSv1 a, NodeEGPSv1 b) {
		NodeEGPSv1 mrca = MRCA(a, b);
		double x = upToAncestor(a, mrca);
		double y = upToAncestor(b, mrca);
		return x + y;
	}

	/**
	 * Sum of branch lengths up to ancestor(not including its length)<br>
	 * If child Node actually is not the child of the ancestor, -1 will return
	 * 
	 * @author yudalang
	 * @return -1 if error
	 */
	public double upToAncestor(NodeEGPSv1 child, NodeEGPSv1 ancestor) {
		double x = Math.max(0.0e0, child.getBranch().getLength());
		NodeEGPSv1 parent = child;
		while (true) {
			parent = parent.getParent();
			// YDL: not use equals() ??
			if (parent == ancestor)
				break;
			try {
				x += Math.max(0.0e0, parent.getBranch().getLength());
			} catch (NullPointerException e) {
				// YDL: deal with situation when ancestor is not real the child's ancestor
				return -1;
			}
		}

		return x;
	}

	/**
	 * Check if given ancestor node is on the path from node to the root!
	 * 
	 * @author yudalang
	 */
	public boolean inThePath(NodeEGPSv1 node, NodeEGPSv1 ancestor) {
		NodeEGPSv1 next = node;
		if (next == null)
			return false;
		while (true) {
			if (next.getParent() == ancestor)
				return true;
			next = next.getParent();
			if (next == null || next == next.getParent())
				break;
		}

		return false;
	}

	/**
	 * Place the root of the tree on the Node of node such that the length of node
	 * is equal to length
	 */
	private NodeEGPSv1 setRootAt(NodeEGPSv1 root, NodeEGPSv1 node, double length) {
		// List a is an order list of parents with the first being the
		// paraent and the last being the root.

		List<NodeEGPSv1> a = getPath(root, node);
		int size = a.size();
		if (size < 2)
			return root;

		NodeEGPSv1 oldParent = node.getParent();
		NodeEGPSv1 oldRoot = root;

		// redirect the parents
		NodeEGPSv1 previous = a.get(size - 1);
		// System.out.println(previous);
		for (int i = size - 2; i >= 0; i--) {
			NodeEGPSv1 current = a.get(i);
			previous.removeChild(current);
			current.addChild(previous);

			previous.getBranch().setLength(current.getBranch().getLength());
			previous = current;
		}

		// add the new root
		NodeEGPSv1 newRoot = new DefaultNode();
		newRoot.addChild(node);

		node.removeChild(oldParent);
		newRoot.addChild(oldParent);

		node.getBranch().setLength(length);
		oldParent.getBranch().setLength(oldParent.getBranch().getLength() - length);

		// remove the old root if it only has one child
		if (oldRoot.getChildCount() == 1) {
			NodeEGPSv1 c = oldRoot.removeChild(0);
			c.getBranch().setLength(c.getBranch().getLength() + oldRoot.getBranch().getLength());
			oldRoot.getParent().addChild(c);
			oldRoot.removeAllParent();
		}

		return newRoot;
	}

	/**
	 * If root is null, or the outgroup name cannot be found in the tree (with the
	 * root), return null. <b>Note:</b> This method not used, so I have not
	 * implement Junit4 test!
	 *
	 * @param root
	 * @param outgroupName
	 * @return
	 */
	public NodeEGPSv1 setRootAt(NodeEGPSv1 root, String outgroupName) {
		NodeEGPSv1 outgroupNode = null;

		GenealogyAnalyzer ga = new GenealogyAnalyzer();
		ga.setGenealogy(root);
		List<NodeEGPSv1> leaves = ga.getLeaves();
		for (int i = 0; i < leaves.size(); i++) {
			NodeEGPSv1 leaf = leaves.get(i);

			String leafName = leaf.getName();
			if (leafName != null && leafName.equalsIgnoreCase(outgroupName)) {
				outgroupNode = leaf;

				break;
			}
		}
		NodeEGPSv1 newRoot = null;
		if (outgroupNode != null) {
			newRoot = setRootAt(root, outgroupNode, outgroupNode.getBranch().getLength() * 0.5);
		}

		return newRoot;
	}

	/**
	 * Get the path from specific node to node! <b>Note:</b> The parameter root
	 * should be the ancestor of child!
	 * 
	 * @param root  the start node, should be ancestor of child!
	 * @param child the end node
	 * @return an List of nodes from root to child(include root and child)!
	 * @author yudalang
	 */
	public ArrayList<NodeEGPSv1> getPath(NodeEGPSv1 root, NodeEGPSv1 child) {
		path.clear();
		NodeEGPSv1 current = child;
		while (current != root) {
			path.add(current);
			current = current.getParent();
		}

		path.add(root);
		return path;
	}

	/**
	 * shadow copy the input node!
	 * 
	 * @author yudalang
	 */
	public NodeEGPSv1 copyTheTree(NodeEGPSv1 root) {
		NodeEGPSv1 newRoot = root.getClone();
		copyTheNode(newRoot, root);

		return newRoot;
	}

	private void copyTheNode(NodeEGPSv1 newNode, NodeEGPSv1 oldNode) {
		for (int i = 0; i < oldNode.getChildCount(); i++) {
			NodeEGPSv1 oldChild = oldNode.getChildAt(i);
			NodeEGPSv1 newChild = oldChild.getClone();
			newNode.addChild(newChild);

			copyTheNode(newChild, oldChild);

			// System.out.println(newNode + " -> " + newChild);
		}
	}

	/**
	 * Compute the deeps of given tree!<br>
	 * <b>Note:</b> Not the longest deep nor does the shortest deep!
	 * 
	 * <pre>
	 * e.g. in this tree the deeps is 1.
	 *   |----leaf1
	 * --|
	 *   |------leaf2
	 * </pre>
	 *
	 * @author yudalang
	 */
	public int getTreeDeeps(NodeEGPSv1 root) {
		int ret = 0;
		NodeEGPSv1 tmp = root;

		while (tmp.getChildCount() != 0) {
			ret++;
			tmp = tmp.getFirstChild();
		}
		return ret;
	}
	
	public GraphicsNode node4eGPSToGraphicsNode(NodeEGPSv1 node) {
		GraphicsNode ret = new GraphicsNode();
		
		int childCount = node.getChildCount();
		//------------------------------------
		if (childCount == 0) {
			ret.setName(node.getLeafName());
		}else {
			ret.setName(node.getName());
		}
		// branch length
		ret.setRealBranchLength(node.getBranch().getLength());
		// bootstrap
		if (node.getBs() < 1) {
			ret.setBootstrap( (int) (node.getBs() * 100));
		}else {
			ret.setBootstrap( (int) (node.getBs()));
		}
		//------------------------------------
		
		
		for (int i = 0; i < childCount; i++) {
			NodeEGPSv1 child = node.getChildAt(i);
			GraphicsNode child4graphics = node4eGPSToGraphicsNode(child);
			ret.addChild(child4graphics);
		}
		
		return ret;
	}
}
