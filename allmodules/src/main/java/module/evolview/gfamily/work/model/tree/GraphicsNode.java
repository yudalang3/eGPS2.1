package module.evolview.gfamily.work.model.tree;

import java.util.Objects;

import evoltree.txtdisplay.TreeDrawUnit;
import evoltree.struct.ArrayBasedNode;

/**
 * Nodes are building blocks for trees, which can be either species tree or
 * coalescent tree. ElegantJTable tree always have a root, regardless how the tree is drawn.
 * The essential properties of a node in a tree are (1) its parent (2) its child
 * (3) its Node length (the length of the Node leading to the parent).
 *
 * For coalescent tree, it is important to know at what states of the coalescent
 * the node's Node start and end.
 * 
 * DispalyedLength和 realLength的设计是因为有很多布局是变一变枝长就行了的。例如 equal length与align to tip
 */
public class GraphicsNode extends Node4BasicNCov19 {

	private double x1 = 0.0;
	private double x2 = 0.0;
	private double y1 = 0.0;
	private double y2 = 0.0;

	private boolean hideNodeFlag = true;
	private TreeDrawUnit bioLine = new TreeDrawUnit();
	/**
	 * 原来那个 branchLength属性用来作为 displayLength了
	 */
	private double realBranchLength = 1;

	/**
	 * 针对spiral等旋转型的树显示方式，节点的旋转角
	 */
	private double angle;
	/** the depth of the node; if the node is leaf, then depth = 0 */
	private int depth;

	private double radius;

	/**
	 * 对于CGB2.0抽样树的可视化size，即抽样树中的实际抽出的size
	 */
	private int visualizeSize = 1;
	private int bootstrap;

	/**
	 * Default constructor
	 */
	public GraphicsNode() {
		super();

	}

	public GraphicsNode(String name) {
		this();
		setName(name);
	}

	public int getVisualizeSize() {
		return visualizeSize;
	}

	public void setVisualizeSize(int visualizeSize) {
		this.visualizeSize = visualizeSize;
	}

	@Override
	public String toString() {
		// 这里开发者可以展示任何自己需要的信息
		return name + "\t" + size;
	}

	public double getXParent() {
		return x1;
	}

	public double getXSelf() {
		return x2;
	}

	public double getYSelf() {
		return y2;
	}

	public void setXParent(double x11) {
		x1 = x11;
	}

	public void setXSelf(double x22) {
		x2 = x22;
	}

	public void setYSelf(double yy) {
		y2 = yy;
	}

	public double getYParent() {
		return y1;
	}

	public void setYParent(double y11) {
		y1 = y11;
	}

	public void setAngleIfNeeded(double angles) {
		angle = angles;
	}

	public double getAngleIfNeeded() {
		return angle;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int dep) {
		this.depth = dep;
	}

	public TreeDrawUnit getDrawUnit() {
		return bioLine;
	}

	public void setDisplayedBranchLength(double v) {
		setLength(v);
	}

	public double getDisplayedBranchLength() {
		return getLength();
	}

	public void setRadicusIfNeeded(double r) {
		this.radius = r;
	}

	public double getRadicusIfNeeded() {
		return radius;
	}

	public void setCollapse(boolean b) {
		bioLine.setCollapse(b);
	}

	public boolean getCollapse() {
		return bioLine.isCollapse();
	}

	public double getRealBranchLength() {
		return realBranchLength;
	}

	public void setRealBranchLength(double v) {
		this.realBranchLength = v;
		setDisplayedBranchLength(v);
	}

	public void setDrawUnit(TreeDrawUnit treeDrawUnit) {
		bioLine = treeDrawUnit;
	}

	public boolean hideNode() {
		return hideNodeFlag;
	}

	public void setIfHideNode(boolean hide) {
		this.hideNodeFlag = hide;

	}

	public int getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(int bootstrap) {
		this.bootstrap = bootstrap;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayBasedNode other = (ArrayBasedNode) obj;
		return ID == other.getID();
	}
}
