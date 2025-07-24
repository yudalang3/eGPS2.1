package evoltree.struct;

import java.util.function.Consumer;

/**
 * The LinkedBasedNode class represents a node in a multi-branch tree, where
 * each node can have multiple children. This node structure is based on linked
 * lists, where each node keeps a reference to its parent, its first child, and
 * its next sibling. This class supports basic operations such as adding
 * children, removing children, and counting children.
 * <p>
 * This class differs from an Array-based tree node (like DefaultNode) in that
 * it uses a linked list structure to manage child nodes, while DefaultNode uses
 * arrays to store child nodes. Each approach has its pros and cons, which will
 * be discussed in the documentation.
 * <p>
 * Note: This is not ready for production use. It is provided for educational!
 * <p>This is used for the completeness of the EvolNode.
 *      Just like the List, consists of the arrayList and the LinkedList.
 *      For the real-world practical use, we should use the Array-based tree node.
 */
public class LinkedBasedNode {
    public static int nextID;
    protected int ID;
    private LinkedBasedNode parent;
    private LinkedBasedNode firstChild;
    private LinkedBasedNode nextSibling;
    protected String name;

	public LinkedBasedNode() {
        ID = nextID++;
    }

    @Override
    public String toString() {
        return "ID:" + String.valueOf(ID) + ";Name:" + name;
    }

    public void addChild(LinkedBasedNode child) {
        child.parent = this;
        if (firstChild == null) {
            firstChild = child;
        } else {
            LinkedBasedNode sibling = firstChild;
            while (sibling.nextSibling != null) {
                sibling = sibling.nextSibling;
            }
            sibling.nextSibling = child;
        }
    }

    public LinkedBasedNode getParent() {
        return parent;
    }

    public int getChildCount() {
        int count = 0;
        LinkedBasedNode child = firstChild;
        while (child != null) {
            count++;
            child = child.nextSibling;
        }
        return count;
    }

    public void insertChildAt(int index, LinkedBasedNode child) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        child.parent = this;
        if (index == 0) {
            child.nextSibling = firstChild;
            firstChild = child;
        } else {
            LinkedBasedNode sibling = firstChild;
            for (int i = 0; i < index - 1 && sibling != null; i++) {
                sibling = sibling.nextSibling;
            }
            if (sibling == null) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }
            child.nextSibling = sibling.nextSibling;
            sibling.nextSibling = child;
        }
    }

    public void removeChild(int index) {
        if (index < 0 || firstChild == null) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        if (index == 0) {
            firstChild = firstChild.nextSibling;
        } else {
            LinkedBasedNode sibling = firstChild;
            for (int i = 0; i < index - 1 && sibling != null; i++) {
                sibling = sibling.nextSibling;
            }
            if (sibling == null || sibling.nextSibling == null) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }
            sibling.nextSibling = sibling.nextSibling.nextSibling;
        }
    }

    public void removeChild(LinkedBasedNode child) {
        if (firstChild == child) {
            firstChild = firstChild.nextSibling;
        } else {
            LinkedBasedNode sibling = firstChild;
            while (sibling.nextSibling != null && sibling.nextSibling != child) {
                sibling = sibling.nextSibling;
            }
            if (sibling.nextSibling != null) {
                sibling.nextSibling = sibling.nextSibling.nextSibling;
            }
        }
        child.parent = null;
    }

    public void removeAllChildren() {
        firstChild = null;
    }

	public LinkedBasedNode getFirstChild() {
		return firstChild;
	}

	public LinkedBasedNode getNextSibling() {
		return nextSibling;
	}

	public LinkedBasedNode getChild(int k) {
		if (k < 0) {
			throw new IllegalArgumentException("Index must be non-negative");
		}

		LinkedBasedNode child = this.firstChild;
		int currentIndex = 0;

		while (child != null && currentIndex < k) {
			child = child.nextSibling;
			currentIndex++;
		}

		if (child == null) {
			throw new IndexOutOfBoundsException("Child index out of bounds");
		}

		return child;
	}

    public class LinkedTreeTraversal {

        public static void preOrderTraversal(LinkedBasedNode root, Consumer<LinkedBasedNode> visit) {
            if (root == null) {
                return;
            }

            visit.accept(root); // 访问根节点

            LinkedBasedNode child = root.getFirstChild();
            while (child != null) {
                preOrderTraversal(child, visit);  // 递归访问子节点
                child = child.getNextSibling();
            }
        }

        public static void postOrderTraversal(LinkedBasedNode root, Consumer<LinkedBasedNode> visit) {
            if (root == null) {
                return;
            }

            LinkedBasedNode child = root.getFirstChild();
            while (child != null) {
                postOrderTraversal(child, visit);  // 递归访问子节点
                child = child.getNextSibling();
            }

            visit.accept(root); // 访问根节点
        }

        // Simple test
        public static void main(String[] args) {
            LinkedBasedNode root = new LinkedBasedNode();
            root.name = "root";
            LinkedBasedNode b = new LinkedBasedNode();
            b.name = "I am B";
            LinkedBasedNode c = new LinkedBasedNode();
            c.name = "I am C";
            LinkedBasedNode d = new LinkedBasedNode();
            d.name = "I am D";
            LinkedBasedNode e = new LinkedBasedNode();
            e.name = "I am E";
            LinkedBasedNode f = new LinkedBasedNode();
            f.name = "I am F";

            root.addChild(b);
            root.addChild(c);
            root.addChild(d);
            b.addChild(e);
            b.addChild(f);

            System.out.println("Preorder iteration:");
            preOrderTraversal(root, data -> System.out.print(data + " "));

            System.out.println("\nPost order iteration:");
            postOrderTraversal(root, data -> System.out.print(data + " "));
        }
    }

}
