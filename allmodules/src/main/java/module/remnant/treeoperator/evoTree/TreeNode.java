package module.remnant.treeoperator.evoTree;


import java.util.HashSet;
import java.util.Set;

/**
 * 
* <p>Title: TreeNode</p>  
* <p>Description: 

* @author yudalang  
* @date 2019-1-8
 */
public class TreeNode implements IEvoNode{
	
	protected String name;
	protected String leafName;
	protected int nodeID = 0;// The number of node id in a tree
	
	protected int bsValue;
	protected double time;
	
	protected IEvoNode[] children;
	protected IEvoNode[] parents;
	protected int nextChild = 0; // also the number of children
	protected int nextParent = 0;// also the number of parent
	protected int totalNodeCountIncludeItsSelf;
	
	protected IEvoBranch[] branches;
	
	public TreeNode(int nodeID) {
		this.nodeID = nodeID;
		
		// By default, the tree is binary tree
		children = new IEvoNode[2];
		parents = new IEvoNode[1];
		branches = new PhyloBranch[] { new PhyloBranch()};
		
	}
	@Override
	public double getTime() {
		return time;
	}
	@Override
	public void setTime(double time) {
		this.time = time;
	}
	@Override
	public void setBootstrapV(int bsValue) {
		this.bsValue = bsValue;
	}
	@Override
	public int getBootstrapV() {
		return bsValue;
	}
	@Override
	public int getParentCount() {
		return nextParent;
	}
	@Override
	public IEvoNode getParentAt(int k) {
		if (k < 0)
			return null;
		IEvoNode ret ;
		if (k >= nextParent) {// just return last one!
			ret = parents[nextParent-1];
		}else {
			ret = parents[k];
		}
		return ret;
	}
	@Override
	public boolean addParent(IEvoNode a) {
		if (nextParent == parents.length) {
			parents = growNodeArray(parents);

			// Also grow branch by one
			IEvoBranch[] tmp = branches;
			branches = new PhyloBranch[parents.length];
			System.arraycopy(tmp, 0, branches, 0, tmp.length);
			branches[parents.length - 1] = new PhyloBranch();
		}

		parents[nextParent] = a;
		nextParent ++;
		
		return true;
	}
	/**
	 * Grow a Node array by one, because array's capacity is fixed.
	 */
	private IEvoNode[] growNodeArray(IEvoNode[] src) {
		IEvoNode[] ret = new IEvoNode[src.length + 1];
		System.arraycopy(src, 0, ret, 0, src.length);
		
		return ret;
	}
	@Override
	public boolean removeParent(IEvoNode a) {
		if (remove(parents, a)) {
			nextParent--;
			return a.removeChild(this);
		}
		return false;
	}
	/**
	 * remove a node from a node array
	 */
	private boolean remove(IEvoNode[] a, IEvoNode b) {
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
	public IEvoNode popParent(int k) {
		if (k < 0 || k >= getParentCount())
			return null;
		IEvoNode a = remove(parents, k);
		if (a != null) {
			nextParent--;
			a.removeChild(this);
		}

		return a;
	}
	
	/**
	 * remove kth node
	 */
	private IEvoNode remove(IEvoNode[] a, int k) {
		if (k < 0 || k >= a.length || a[k] == null)
			return null;
		
		IEvoNode b = a[k];
		for (int i = k; i < a.length - 1; i++)
			a[i] = a[i + 1];
		a[a.length - 1] = null;
		
		return b;
	}
	@Override
	public boolean setParentAt(int k, IEvoNode a) {
		if (k < nextParent) {
			if (indexOf(parents, a) >= 0)
				return false; // do not allow duplicate parent
			IEvoNode b = parents[k];
			b.removeChild(this); // the Node being replaced has
			parents[k] = a;
		}else {
			addParent(a);
		}

		return true;
	}
	private int indexOf(IEvoNode[] a, IEvoNode b) {
		for (int i = a.length; i-- > 0;)
			if (a[i] == b)
				return i;
		return -1;
	}
	
	@Override
	public int indexOfParent(IEvoNode a) {
		return indexOf(parents, a);
	}
	@Override
	public void removeAllParent() {
		for (int i = nextParent - 1; i >= 0; i--) {
			IEvoNode a = parents[i];
			parents[i] = null;
			nextParent--;
			a.removeChild(this);
		}
	}
	@Override
	public IEvoNode[] getParents() {
		return parents;
	}
	@Override
	public int getChildCount() {
		return nextChild;
	}
	@Override
	public IEvoNode getChildAt(int k) {
		if (k < 0 || k >= children.length || children[k] == null)
			return null;
		
		return children[k];
	}
	@Override
	public IEvoNode getFirstChild() {
		return children[0];
	}
	@Override
	public IEvoNode getLastChild() {
		return children[nextChild - 1];
	}
	@Override
	public IEvoNode[] getChildren() {
		return children;
	}
	@Override
	public boolean insertChildAt(int index, IEvoNode aEvoNode) {
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
	public IEvoNode popChild(int k) {
		int n = getChildCount();
		if (k < 0 || k >= n)
			return null;

		IEvoNode a = children[k];
		for (int i = k; i < n - 1; i++)
			children[i] = children[i + 1];
		children[n - 1] = null;
		nextChild--;

		a.removeParent(this);
		return a;
	}
	@Override
	public boolean removeChild(IEvoNode a) {
		
		
		if (remove(children, a)) {
			nextChild--;
			a.removeParent(this);
			return true;
		}
		return false;
	}
	@Override
	public int indexOfChild(IEvoNode a) {
		return indexOf(children, a);
	}
	@Override
	public boolean setChildAt(int k, IEvoNode a) {
		if (k >= nextChild) {
			addChild(a);
			return true;
		} else if (k < 0) {
			return false;
		} else {

			// Don't need to duplicate
			if (indexOf(children, a) >= 0)
				return false;

			IEvoNode nodeToDelete = children[k];
			nodeToDelete.removeParent(this); // the Node being replaced has no this parent

			children[k] = a;
			a.addParent(this);

			return true;

		}
	}
	@Override
	public void addChild(IEvoNode a) {
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
	public IEvoNode[] getSibling() {
		if (parents[0] != null) {
			IEvoNode parent = parents[0];
			IEvoNode[] children2 = parent.getChildren();

			if (children2.length > 1) {
				int indexOfThisNodeInParents = indexOf(children2, this);

				IEvoNode[] ret = new IEvoNode[children2.length - 1];

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
	private int getLeafNum(IEvoNode node) {
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
	public void setSize(int k) {
		totalNodeCountIncludeItsSelf = k;
	}
	@Override
	public int getSize() {
		return totalNodeCountIncludeItsSelf;
	}
	@Override
	public Set<IEvoNode> getAll_leaves() {
		Set<IEvoNode> ret = new HashSet<>();
		getAllLeaves(ret, this);
		return ret;
	}
	@Override
	public Set<IEvoNode> getAll_internalNodes() {
		Set<IEvoNode> ret = new HashSet<>();
		getAllInternalNodes(ret, this);
		return ret;
	}
	private Set<IEvoNode> getAllLeaves(Set<IEvoNode> childSet, IEvoNode par) {
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

	private Set<IEvoNode> getAllInternalNodes(Set<IEvoNode> childSet, IEvoNode par) {
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
	public String getLeafName() {
		return leafName; 
	}
	@Override
	public void setLeafName(String leafName) {
		this.leafName = leafName;
	}
	@Override
	public IEvoBranch[] getBranch() {
		return branches;
	}
	@Override
	public IEvoBranch getBranch(int k) {
		if (k < 0 || (k > (branches.length -1)) ) {
			return null;
		}
		return branches[k];
	}
	@Override
	public void setBranch(IEvoBranch[] branches) {
		this.branches = branches;
	}
	
	@Override
	public IEvoNode getClone() {
		// If clone we increse id 1000
		IEvoNode newNode = new TreeNode(getNodeID() + 1000);
		newNode.setBranch(getBranch());
		newNode.setName(getName());
		newNode.setLeafName(getLeafName());
		newNode.setSize(getSize());
		newNode.setBootstrapV(getBootstrapV());
		return newNode;
	}
	
	@Override
	public String toString() {
		return "This is " + getNodeID();
	}
	
}
