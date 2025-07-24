package module.remnant.treeoperator;


import java.util.HashSet;
import java.util.Set;

/**
 * 
* <p>Title: TreeNode</p>  
* <p>Description: 

* @author yudalang  
* @date 2019-1-8
 */
public class BasicNode implements GNode{
	
	protected String name;
	protected String leafName;
	// The number of node id in a tree
	protected int nodeID = 0;
	
	protected double bootstrapValue;
	protected double time;
	
	protected GNode[] children;
	protected GNode[] parents;
	// also the number of children
	protected int nextChild = 0; 
	// also the number of parent
	protected int nextParent = 0;
	
	/** To record the orignal order! Note this order is 0 based*/
	protected int orignalOrderInMatrix = -1;
	
	protected BasicBranch[] branches;
	private int size = 0;
	
	public BasicNode(int nodeID) {
		this.nodeID = nodeID;
		
		// By default, the tree is binary tree
		children = new GNode[2];
		parents = new GNode[1];
		branches = new BasicBranch[] { new BasicBranch()};
	}
	/**
	 * This is for graphics only!
	 */
	protected BasicNode() {
		// By default, the tree is binary tree
		children = new GNode[2];
		parents = new GNode[1];
		branches = new BasicBranch[] { new BasicBranch() };
	}
	@Override
	public int getParentCount() {
		return nextParent;
	}
	@Override
	public GNode getParentAt(int k) {
		if (k < 0)
			return null;
		GNode ret ;
		if (k >= nextParent) {// just return last one!
			ret = parents[nextParent-1];
		}else {
			ret = parents[k];
		}
		return ret;
	}
	@Override
	public GNode getFirstParent() {
		return parents[0];
	}
	@Override
	public boolean addParent(GNode a) {
		if (nextParent == parents.length) {
			parents = growNodeArray(parents);

			// Also grow branch by one
			GBranch[] tmp = branches;
			branches = new BasicBranch[parents.length];
			System.arraycopy(tmp, 0, branches, 0, tmp.length);
			branches[parents.length - 1] = new BasicBranch();
		}

		parents[nextParent] = a;
		nextParent ++;
		
		return true;
	}
	/**
	 * Grow a Node array by one, because array's capacity is fixed.
	 */
	private GNode[] growNodeArray(GNode[] src) {
		GNode[] ret = new GNode[src.length + 1];
		System.arraycopy(src, 0, ret, 0, src.length);
		
		return ret;
	}
	@Override
	public boolean removeParent(GNode a) {
		if (remove(parents, a)) {
			nextParent--;
			return a.removeChild(this);
		}
		return false;
	}
	/**
	 * remove a node from a node array
	 */
	private boolean remove(GNode[] a, GNode b) {
		if (b == null)
			return false;
		for (int i = 0; i < a.length; i++) {
			if (b.equals(a[i])) {
				for (int k = i; k < a.length - 1; k++)
					a[k] = a[k + 1];
				a[a.length - 1] = null;
				return true;
			}
		}

		return false;
	}
	@Override
	public GNode popParent(int k) {
		if (k < 0 || k >= getParentCount())
			return null;
		GNode a = remove(parents, k);
		if (a != null) {
			nextParent--;
			a.removeChild(this);
		}

		return a;
	}
	
	/**
	 * remove kth node
	 */
	private GNode remove(GNode[] a, int k) {
		if (k < 0 || k >= a.length || a[k] == null)
			return null;
		
		GNode b = a[k];
		for (int i = k; i < a.length - 1; i++)
			a[i] = a[i + 1];
		a[a.length - 1] = null;
		
		return b;
	}
	
	@Override
	public boolean setParentAt(int k, GNode a) {
		if (k < nextParent) {
			if (indexOf(parents, a) >= 0)
				return false; // do not allow duplicate parent
			GNode b = parents[k];
			b.removeChild(this); // the Node being replaced has
			parents[k] = a;
		}else {
			addParent(a);
		}

		return true;
	}
	private int indexOf(GNode[] a, GNode b) {
		for (int i = a.length; i-- > 0;)
			if (a[i] == b)
				return i;
		return -1;
	}
	
	@Override
	public int indexOfParent(GNode a) {
		return indexOf(parents, a);
	}
	@Override
	public void removeAllParent() {
		for (int i = nextParent - 1; i >= 0; i--) {
			GNode a = parents[i];
			parents[i] = null;
			nextParent--;
			a.removeChild(this);
		}
	}
	@Override
	public GNode[] getParents() {
		return parents;
	}
	@Override
	public int getChildCount() {
		return nextChild;
	}
	@Override
	public GNode getChildAt(int k) {
		if (k < 0 || k >= children.length || children[k] == null)
			return null;
		
		return children[k];
	}
	@Override
	public GNode getFirstChild() {
		return children[0];
	}
	@Override
	public GNode getLastChild() {
		return children[nextChild - 1];
	}
	@Override
	public GNode[] getChildren() {
		return children;
	}
	@Override
	public boolean insertChildAt(int index, GNode aEvoNode) {
		boolean flag = false;
		if (index >= nextChild) {
			addChild(aEvoNode);
			flag = true;
		} else {

			children = growNodeArray(children);
			for (int i = children.length - 2; i >= index; i--) {
				children[i + 1] = children[i];
			}
			children[index] = aEvoNode;
			aEvoNode.addParent(this); // must add this statement
			nextChild++;
		}
		return flag;
	}
	@Override
	public GNode popChild(int k) {
		int n = getChildCount();
		if (k < 0 || k >= n)
			return null;

		GNode a = children[k];
		for (int i = k; i < n - 1; i++)
			children[i] = children[i + 1];
		children[n - 1] = null;
		nextChild--;

		a.removeParent(this);
		return a;
	}
	@Override
	public boolean removeChild(GNode a) {
		
		
		if (remove(children, a)) {
			nextChild--;
			a.removeParent(this);
			return true;
		}
		return false;
	}
	@Override
	public int indexOfChild(GNode a) {
		return indexOf(children, a);
	}
	@Override
	public boolean setChildAt(int k, GNode a) {
		if (k >= nextChild) {
			addChild(a);
			return true;
		} else if (k < 0) {
			return false;
		} else {

			// Don't need to duplicate
			if (indexOf(children, a) >= 0)
				return false;

			GNode nodeToDelete = children[k];
			nodeToDelete.removeParent(this); // the Node being replaced has no this parent

			children[k] = a;
			a.addParent(this);

			return true;

		}
	}
	@Override
	public void addChild(GNode a) {
		if (nextChild == children.length)
			children = growNodeArray(children); // At first: child = new Node[2]
		
		children[nextChild] = a;
		nextChild ++;
		a.addParent(this);
	}
	@Override
	public void removeAllChild() {
		for (int i = nextChild - 1; i >= 0; i--) {
			if (children[i] != null) {
				children[i].removeParent(this);
				children[i] = null;
			}
		}
		nextChild = 0;
		
	}
	/**
	 * Currently just implements simple sibling, i.e parent[0] ' sibling
	 */
	@Override
	public GNode[] getSiblings() {
		if (parents[0] != null) {
			GNode parent = parents[0];
			GNode[] children2 = parent.getChildren();

			if (children2.length > 1) {
				int indexOfThisNodeInParents = indexOf(children2, this);

				GNode[] ret = new GNode[children2.length - 1];

				for (int i = 0; i < indexOfThisNodeInParents; i++) {
					ret[i] = children2[i];
				}
				for (int i = indexOfThisNodeInParents + 1; i < children2.length; i++) {
					ret[i - 1] = children2[i];
				}

				return ret;
			}

		}
		return null;
	}
	@Override
	public int getNumOfLeaves() {
		return getLeafNum(this);
	}
	private int getLeafNum(GNode node) {
		int leafNum = 0;
		if (node.getChildCount() > 0) {
			for (int i = 0; i < node.getChildCount(); i++) {
				leafNum = leafNum + getLeafNum(node.getChildAt(i));
			}
		} else {
			leafNum++;
		}
		return leafNum;
	}
	@Override
	public int getSize() {
		return size;
	}
	@Override
	public void setSize(int s) {
		this.size  = s;
	}
	@Override
	public Set<GNode> getLeaves() {
		Set<GNode> ret = new HashSet<>();
		getAllLeaves(ret, this);
		return ret;
	}
	@Override
	public Set<GNode> getInternalNodes() {
		Set<GNode> ret = new HashSet<>();
		getAllInternalNodes(ret, this);
		return ret;
	}
	private Set<GNode> getAllLeaves(Set<GNode> childSet, GNode par) {
		int cc = par.getChildCount();
		if (cc == 0) {
			childSet.add(par);
			return childSet;
		}
		for (int i = 0; i < cc; i++) {
			childSet = getAllLeaves(childSet, par.getChildAt(i));
		}
		return childSet;
	}

	private Set<GNode> getAllInternalNodes(Set<GNode> childSet, GNode par) {
		int cc = par.getChildCount();

		for (int i = 0; i < cc; i++) {
			childSet = getAllInternalNodes(childSet, par.getChildAt(i));
		}

		if (cc != 0) {
			childSet.add(par);
			return childSet;
		}
		return childSet;

	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public int getNodeID() {
		return nodeID;
	}
	@Override
	public void setNodeID(int ID) {
		this.nodeID = ID;
	}
	@Override
	public BasicBranch[] getBranches() {
		return branches;
	}
	@Override
	public BasicBranch getBranch(int k) {
		if (k < 0 || (k > (branches.length -1)) ) {
			return null;
		}
		return branches[k];
	}
	@Override
	public GBranch getFirstBranch() {
		return branches[0];
	}
	@Override
	public void setBranches(BasicBranch[] branches) {
		this.branches = branches;
	}
	
	@Override
	public GNode getClone() {
		// If clone we increse id 1000
		GNode newNode = new BasicNode(getNodeID() + 1000);
		newNode.setBranches(getBranches());
		newNode.setName(getName());
		return newNode;
	}
	
	@Override
	public String toString() {
		return "This is " + getNodeID();
	}
	@Override
	public void setLeafName(String name) {
		this.leafName = name;
	}
	@Override
	public String getLeafName() {
		return leafName;
	}
	
	/** To record the orignal order! Note this order is 0 based*/
	public void setOrignalOrderInMatrix(int orignalOrderInMatrix) {
		this.orignalOrderInMatrix = orignalOrderInMatrix;
	}
	
	/** To record the orignal order! Note this order is 0 based*/
	public int getOrignalOrderInMatrix() {
		return orignalOrderInMatrix;
	}
	@Override
	public double getBootstrapValue() {
		return bootstrapValue;
	}
	@Override
	public void setBootstrapValue(double bs) {
		this.bootstrapValue = bs;
	}
	
}
