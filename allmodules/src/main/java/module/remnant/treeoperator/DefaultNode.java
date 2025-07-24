package module.remnant.treeoperator;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 
 * <p>
 * Title: DefaultNode
 * </p>
 * 
 * <pre>
 * Description:  Nodes are building blocks for trees, which can be either
 * species tree or coalescent tree. ElegantJTable tree always have a root, regardless how
 * the tree is drawn. The essential properties of a node in a tree are 
 * (1) its parent 
 * (2) its child 
 * (3) its Node length (the length of the Node leading toddd the parent).
 *
 * For coalescent tree, it is important to know at what states of the coalescent
 * the node's Node start and end.
 * 
 * yudalang: The stratgy maybe
 * </pre>
 * 
 * @author yudalang
 * @date 2018-12-21
 */
public class DefaultNode implements NodeEGPSv1 {
	protected NodeEGPSv1 child[];
	protected NodeEGPSv1 parent[];
	protected int nextChild = 0; // also the number of children
	protected int nextParent = 0;// also the number of parent
	protected String name;
	protected DefaultBranch[] branch;
	protected int size;
	protected static int nextID; // yudalang: Used to record the created ID.
	protected final int ID;
	protected double bsValue;

	protected int nameID;

	// for Radial layout
	private double radial_angle = 0.0;

	// for Circulary layout
	private double circular_t1 = 0.0;
	private double circular_by = 0.0;
	private double circular_la = 0.0;

	private double angle;
	// the depth of the node; if the node is leaf, then depth = 0;
	private int depth;

	// add this variable to avoid 'double[] base = new
	// double[node.getChildCount()];' in drawing process
	private double basey;

	// for leafs or collapsed nodes
	private String leafName;

	// for rightside annotation, record the height and width of Leaf name
	private double leafComWidth = 0.0;
	private double leafComHeight = 0.0;

	// for xxx_left
	private double annoXLeft = 0.0;
	// for xxx_right
	private double annoXRight = 0.0;
	// for xxx_left and xxx_right
	private double annoYsmallLR = 0.0;
	private double annoYBiglLR = 0.0;
	// for xxx_up
	private double annoYUp = 0.0;
	// for xxx_down
	private double annoYDown = 0.0;
	// for xxx_up and xxx_down
	private double annoXsmallUD = 0.0;
	private double annoXBigUD = 0.0;

	private Rectangle labelRect = null;

	private boolean isSelect = false;
	private boolean isVisible = true;
	private boolean isCollapsed = false;

	private NodeEGPSv1 sibling = null;

	private Boolean keyConfig = false;

	private Map<String, Boolean> attrs = null;

	private double time;

	/**
	 * Default constructor
	 */
	public DefaultNode() {
		// most Nodes have two children and one parent
		child = new NodeEGPSv1[2];
		parent = new NodeEGPSv1[1];
		branch = new DefaultBranch[1];
		branch[0] = new DefaultBranch();
		nameID = nextID;// YDL: let nameID has default value!
		ID = nextID++;
		depth = 0;
		attrs = new HashMap<String, Boolean>();
	}

	public DefaultNode(String name) {
		this();
		setName(name);
	}

	/**
	 * shadow clone a node
	 */
	@Override
	public NodeEGPSv1 getClone() {
		DefaultNode newNode = new DefaultNode();
		newNode.getBranch().setLength(getBranch().getLength());
		newNode.setName(getName());
		newNode.setLeafName(getLeafName());
		newNode.setSize(getSize());
		newNode.setBs(getBs());
		//newNode.setNodeLine(getNodeLine());
		newNode.setLabelRect(getLabelRect());

		return newNode;
	}

	@Override
	public void setBs(double bsValue) {
		this.bsValue = bsValue;
	}

	@Override
	public double getBs() {
		return bsValue;
	}

	@Override
	public void addChild(NodeEGPSv1 a) {
		if (nextChild == child.length)
			child = growNodeArray(child); // At first: child = new Node[2]
		child[nextChild++] = a;
		a.addParent(this);
	}

	/**
	 * Grow a Node array by one, because array's capacity is fixed.
	 */
	public static NodeEGPSv1[] growNodeArray(NodeEGPSv1[] a) {
		NodeEGPSv1[] tmp = a;
		a = new NodeEGPSv1[a.length + 1];
		System.arraycopy(tmp, 0, a, 0, tmp.length);
		return a;
	}

	@Override
	public void addParent(NodeEGPSv1 a) {
		if (nextParent == parent.length) {
			parent = growNodeArray(parent);
			// Also grow branch by one
			DefaultBranch[] tmp = branch;
			branch = new DefaultBranch[parent.length];
			System.arraycopy(tmp, 0, branch, 0, tmp.length);
			branch[parent.length - 1] = new DefaultBranch();
		}

		parent[nextParent++] = a;
	}

	@Override
	public boolean setChildAt(int k, NodeEGPSv1 a) {

		if (k == nextChild) {
			addChild(a);
			return true;
		}

		// Following case need to be done
		// if(indexOf(child,a) >= 0) return false;

		NodeEGPSv1 b = child[k];
		child[k] = a;
		b.removeParent(this); // the Node being replaced has no this parent
		a.addParent(this);
		return true;
	}

	@Override
	public int getChildCount() {
		return nextChild;
	}

	@Override
	public boolean removeChild(NodeEGPSv1 a) {
		if (remove(child, a)) {
			nextChild--;
			a.removeParent(this);
			return true;
		}
		return false;
	}

	@Override
	public NodeEGPSv1 removeChild(int k) {
		int n = getChildCount();
		if (k < 0 || k >= n)
			return null;

		NodeEGPSv1 a = child[k];
		for (int i = k; i < n - 1; i++)
			child[i] = child[i + 1];
		child[n - 1] = null;
		nextChild--;

		a.removeParent(this);
		return a;
	}

	@Override
	public boolean insertChildAt(int index, NodeEGPSv1 popMod) {
		boolean flag = false;
		if (index == nextChild) {
			addChild(popMod);
			flag = true;
		} else {

			child = growNodeArray(child);
			for (int i = child.length - 2; i >= index; i--) {
				child[i + 1] = child[i];
			}
			child[index] = popMod;
			popMod.addParent(this); // must add this statement
			nextChild++;
		}
		return flag;
	}

	@Override
	public void removeAllChild() {
		for (int i = nextChild - 1; i >= 0; i--) {
			if (child[i] != null) {
				child[i].removeParent(this);
				child[i] = null;
			}
		}
		nextChild = 0;
	}

	@Override
	public int indexOfChild(NodeEGPSv1 a) {
		return indexOf(child, a);
	}

	@Override
	public NodeEGPSv1 getChildAt(int k) {
		return child[k];
	}

	@Override
	public NodeEGPSv1 getFirstChild() {
		return child[0];
	}

	@Override
	public NodeEGPSv1 getLastChild() {
		return child[nextChild - 1];
	}

	@Override
	public NodeEGPSv1 getSibling(NodeEGPSv1 a) {
		NodeEGPSv1 parent = a.getParent();
		NodeEGPSv1 returnNode = null;
		if (parent != null) { // it is not root
			int count = parent.getChildCount();
			int index = 0;
			for (int i = 0; i < count; i++) {
				if (parent.getChildAt(i).getName().equals(a.getName())) {
					index = i;
				}
			}
			if ((index + 1) < count) {
				returnNode = parent.getChildAt(index + 1);
			} else {
				if ((index - 1) < 0)
					return null;
				returnNode = parent.getChildAt(index - 1);
			}
		}
		return returnNode;
	}

	@Override
	public int getParentCount() {
		return nextParent;
	}

	@Override
	public int indexOfParent(NodeEGPSv1 a) {
		return indexOf(parent, a);
	}

	@Override
	public void removeAllParent() {
		for (int i = nextParent - 1; i >= 0; i--) {
			NodeEGPSv1 a = parent[i];
			parent[i] = null;
			nextParent--;
			a.removeChild(this);
		}
	}

	@Override
	public void setParent(NodeEGPSv1 a) {
		setParentAt(0, a);
	}

	@Override
	public NodeEGPSv1 getParent() {
		return parent[0];
	}

	@Override
	public boolean setParentAt(int k, NodeEGPSv1 a) {
		if (k < nextParent) {
			if (indexOf(parent, a) >= 0)
				return false; // do not allow duplicate parent
			NodeEGPSv1 b = parent[k];
			b.removeChild(this); // the Node being replaced has
			parent[k] = a;
		}

		else {
			addParent(a);
		}

		return true;
	}

	@Override
	public boolean removeParent(NodeEGPSv1 a) {
		if (remove(parent, a)) {
			nextParent--;
			return a.removeChild(this);
		}

		return false;
	}

	@Override
	public NodeEGPSv1 removeParent(int k) {
		if (k < 0 || k >= getParentCount())
			return null;
		NodeEGPSv1 a = remove(parent, k);
		if (a != null) {
			nextParent--;
			a.removeChild(this);
		}

		return a;
	}

	@Override
	public NodeEGPSv1 getParentAt(int k) {
		return parent[k];
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	/*
	 * @Override public String toString() { String sb = null; if (getParent() !=
	 * null) { sb = "Name=" + getName() + " branLen=" + getBranch().getLength() +
	 * " lefname=" + getLeafName() + " Parent=" + getParent().getName(); } else { sb
	 * = "Name=" + getName() + " branLen=" + getBranch().getLength() + " lefname=" +
	 * getLeafName() + " Parent= null" ; } return sb; }
	 */

	@Override
	public void clear() {
		removeAllChild();
		removeAllParent();
		setName(null);
	}

	public void fastClear() {
		for (int i = getChildCount(); i-- > 0;)
			child[i] = null;
		for (int i = getParentCount(); i-- > 0;)
			parent[i] = null;
		nextChild = 0;
		nextParent = 0;
		setName(null);
		setSize(0);
	}

	public int getID() {
		return ID;
	}

	/**
	 * return true if it is an ancestral Node of given Node a
	 */
	public boolean isAncestorOf(NodeEGPSv1 a) {
		if (a == null)
			return false;
		if (a == this)
			return true;

		while (a.getParentCount() > 0) {
			if (a.getParentAt(0) == this)
				return true;
			a = a.getParentAt(0);
		}

		return false;
	}

	@Override
	public DefaultBranch getBranch(int k) {
		return branch[k];
	}

	@Override
	public DefaultBranch getBranch() {
		return branch[0];
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public void setSize(int k) {
		size = k;
	}

	/**
	 * remove a node from a node array
	 */
	public static boolean remove(NodeEGPSv1[] a, NodeEGPSv1 b) {
		if (b == null)
			return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] == b) {
				for (int k = i; k < a.length - 1; k++)
					a[k] = a[k + 1];
				a[a.length - 1] = null;
				return true;
			}
		}

		return false;
	}

	/**
	 * remove kth node
	 */
	public static NodeEGPSv1 remove(NodeEGPSv1[] a, int k) {
		if (k < 0 || k >= a.length || a[k] == null)
			return null;
		NodeEGPSv1 b = a[k];
		for (int i = k; i < a.length - 1; i++)
			a[i] = a[i + 1];
		a[a.length - 1] = null;
		return b;
	}

	public static int indexOf(NodeEGPSv1[] a, NodeEGPSv1 b) {
		for (int i = a.length; i-- > 0;)
			if (a[i] == b)
				return i;
		return -1;
	}

	@Override
	public void setAngle(double angles) {
		angle = angles;
	}

	@Override
	public double getAngle() {
		return angle;
	}

	@Override
	public int getDepth() {
		return depth;
	}

	@Override
	public void setDepth(int dep) {
		this.depth = dep;
	}

	@Override
	public double getBasey() {
		return basey;
	}

	@Override
	public void setBasey(double basey) {
		this.basey = basey;
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
	public double getAnnoXLeft() {
		return annoXLeft;
	}

	@Override
	public void setAnnoXLeft(double annoXLeft) {
		this.annoXLeft = annoXLeft;
	}

	@Override
	public double getAnnoXRight() {
		return annoXRight;
	}

	@Override
	public void setAnnoXRight(double annoXRight) {
		this.annoXRight = annoXRight;
	}

	@Override
	public double getAnnoYsmallLR() {
		return annoYsmallLR;
	}

	@Override
	public void setAnnoYsmallLR(double annoYsmallLR) {
		this.annoYsmallLR = annoYsmallLR;
	}

	@Override
	public double getAnnoYBiglLR() {
		return annoYBiglLR;
	}

	@Override
	public void setAnnoYBiglLR(double annoYBiglLR) {
		this.annoYBiglLR = annoYBiglLR;
	}

	@Override
	public double getAnnoYUp() {
		return annoYUp;
	}

	@Override
	public void setAnnoYUp(double annoYUp) {
		this.annoYUp = annoYUp;
	}

	@Override
	public double getAnnoYDown() {
		return annoYDown;
	}

	@Override
	public void setAnnoYDown(double annoYDown) {
		this.annoYDown = annoYDown;
	}

	@Override
	public double getAnnoXsmallUD() {
		return annoXsmallUD;
	}

	@Override
	public void setAnnoXsmallUD(double annoXsmallUD) {
		this.annoXsmallUD = annoXsmallUD;
	}

	@Override
	public double getAnnoXBigUD() {
		return annoXBigUD;
	}

	@Override
	public void setAnnoXBigUD(double annoXBigUD) {
		this.annoXBigUD = annoXBigUD;
	}

	@Override
	public Rectangle getLabelRect() {
		return labelRect;
	}

	@Override
	public void setLabelRect(Rectangle labelRect) {
		this.labelRect = labelRect;
	}

	@Override
	public boolean isSelect() {
		return isSelect;
	}

	@Override
	public void setSelect(boolean isSel) {
		this.isSelect = isSel;
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}


	@Override
	public double getLeafComWidth() {
		return leafComWidth;
	}

	@Override
	public void setLeafComWidth(double leafComWidth) {
		this.leafComWidth = leafComWidth;
	}

	@Override
	public double getLeafComHeight() {
		return leafComHeight;
	}

	@Override
	public void setLeafComHeight(double leafComHeight) {
		this.leafComHeight = leafComHeight;
	}

	@Override
	public NodeEGPSv1 getSibling() {
		return sibling;
	}

	@Override
	public void setSibling(NodeEGPSv1 sibling) {
		this.sibling = sibling;
	}

	/*
	 * @Override public JLabel getLabIcon() { return labIcon; }
	 * 
	 * @Override public void setLabIcon(JLabel labIcon) { this.labIcon = labIcon; }
	 */

	@Override
	public Boolean getKeyConfig() {
		return keyConfig;
	}

	@Override
	public void setKeyConfig(Boolean keyConfig) {
		this.keyConfig = keyConfig;
	}

	@Override
	public Boolean isCollapsed() {
		return isCollapsed;
	}

	@Override
	public void setCollapsed(boolean isCol) {
		this.isCollapsed = isCol;
	}


	@Override
	public void setRadialAngle(double ra) {
		this.radial_angle = ra;

	}

	@Override
	public double getRadialAngle() {
		return this.radial_angle;
	}

	@Override
	public void setCircularT1(double t1) {
		this.circular_t1 = t1;
	}

	@Override
	public double getCircularT1() {
		return this.circular_t1;
	}

	@Override
	public void setCircularBasey(double by) {
		this.circular_by = by;
	}

	@Override
	public double getCircularBasey() {
		return this.circular_by;
	}

	@Override
	public double getCircularLeafAngle() {
		return this.circular_la;
	}

	@Override
	public void setCircularLeafAngle(double la) {
		this.circular_la = la;
	}

	@Override
	public Set<NodeEGPSv1> getAll_leaves() {
		Set<NodeEGPSv1> ret = new HashSet<>();
		getAllLeaves(ret, this);
		return ret;
	}

	@Override
	public Set<NodeEGPSv1> getAll_internalNodes() {
		Set<NodeEGPSv1> ret = new HashSet<>();
		getAllInternalNodes(ret, this);
		return ret;
	}

	public Set<NodeEGPSv1> getAllLeaves(Set<NodeEGPSv1> childSet, NodeEGPSv1 par) {
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

	public Set<NodeEGPSv1> getAllInternalNodes(Set<NodeEGPSv1> childSet, NodeEGPSv1 par) {
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
	public double getTime() {
		return time;
	}

	@Override
	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public int getNameID() {
		return nameID;
	}

	@Override
	public void setNameID(int ID) {
		this.nameID = ID;
	}

	@Override
	public int getNumOfLeaves() {
		return getLeafNum(this);
	}

	private int getLeafNum(NodeEGPSv1 node) {
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



}
