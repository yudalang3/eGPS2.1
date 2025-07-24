package module.remnant.treeoperator.evoTree;

public class BinaryTree<E extends Comparable<E>> extends AbstractTree<E> {
	
	protected TreeNode root;
	protected int size = 0;

	/** Create a default binary tree */
	public BinaryTree() {
		root = new TreeNode(1);
		TreeNode n1 = new TreeNode(2);
		TreeNode n2 = new TreeNode(3);
		root.addChild(n1);
		root.addChild(n2);
		
		TreeNode n3 = new TreeNode(4);
		TreeNode n4 = new TreeNode(5);
		n1.addChild(n3);
		n1.addChild(n4);
	}
	/** Create a tree from root node*/
	public BinaryTree(TreeNode root) {
		this.root = root;
	}

	/** Create a binary tree from an array of objects */
	public BinaryTree(E[] objects) {
		for (int i = 0; i < objects.length; i++)
			insert(objects[i]);
	}

	@Override /** Returns true if the element is in the tree */
	public boolean search(E e) {
		return false;
	}

	/**
	 * Insert element o into the binary tree Return true if the element is inserted
	 * successfully
	 */
	@Override 
	public boolean insert(E e) {
		if (root == null)

		size++;
		return true; // Element inserted successfully
	}


	@Override /** Inorder traversal from the root */
	public void inorder() {
		inorder(root);
	}

	/** Inorder traversal from a subtree */
	protected void inorder(TreeNode root) {
		if (root == null)
			return;
		inorder(root);
		System.out.print(root + " ");
		inorder(root);
	}

	@Override /** Get the number of nodes in the tree */
	public int getSize() {
		return size;
	}

	/** Returns the root of the tree */
	public TreeNode getRoot() {
		return root;
	}

	/** Returns a path from the root leading to the specified element */
	public java.util.ArrayList<TreeNode> path(E e) {
		java.util.ArrayList<TreeNode> list = new java.util.ArrayList<>();
		
		TreeNode current = root; // Start from the root
		
		return list; // Return an array list of nodes
	}

	/**
	 * Delete an element from the binary tree. Return true if the element is deleted
	 * successfully Return false if the element is not in the tree
	 */
	@Override
	public boolean delete(E e) {
		// Locate the node to be deleted and also locate its parent node
		TreeNode parent = null;
		TreeNode current = root;
		

		if (current == null)
			return false; // Element is not in the tree

		
		size--;
		return true; // Element deleted successfully
	}

	@Override /** Obtain an iterator. Use inorder. */
	public java.util.Iterator<E> iterator() {
		return new InorderIterator();
	}

	// Inner class InorderIterator
	private class InorderIterator implements java.util.Iterator<E> {
		// Store the elements in a list
		private java.util.ArrayList<E> list = new java.util.ArrayList<>();
		private int current = 0; // Point to the current element in list

		public InorderIterator() {
			inorder(); // Traverse binary tree and store elements in list
		}

		/** Inorder traversal from the root */
		private void inorder() {
			inorder(root);
		}

		/** Inorder traversal from a subtree */
		private void inorder(TreeNode root) {
			if (root == null)
				return;
		}

		@Override /** More elements for traversing? */
		public boolean hasNext() {
			if (current < list.size())
				return true;

			return false;
		}

		@Override /** Get the current element and move to the next */
		public E next() {
			return list.get(current++);
		}

		@Override /** Remove the current element */
		public void remove() {
			delete(list.get(current)); // Delete the current element
			list.clear(); // Clear the list
			inorder(); // Rebuild the list
		}
	}

	/** Remove all elements from the tree */
	public void clear() {
		root = null;
		size = 0;
	}
}
