package module.remnant.treeoperator;

import java.awt.Rectangle;
import java.util.Set;

/**
 * 
 * <p>
 * Title: Node
 * </p>
 * <p>
 * Description: The interface define methods for type Node supports.
 * 
 * <pre>
 * Node in a Tree(or a graphics) has the following properties. 
 * (1) ElegantJTable Node may have zero to many children Nodes
 * (2) ElegantJTable Node may have zero to many parent Nodes
 * (3) Node can have branch associated with it.
 * </pre>
 * </p>
 * 
 * @author yudalang-annotate
 * @date 2018-7-27
 */
public interface NodeEGPSv1{
	// Time is from root node to current node!
	double getTime();

	void setTime(double time);


	int getParentCount();

	/**
	 * return the parent
	 */
	NodeEGPSv1 getParentAt(int k);

	void addParent(NodeEGPSv1 a);

	boolean removeParent(NodeEGPSv1 a);

	NodeEGPSv1 removeParent(int k);

	/**
	 * set parent will not try to make the current Node as child of the parent,
	 * which should be initiated from add or setchild.
	 */
	boolean setParentAt(int k, NodeEGPSv1 a);

	int indexOfParent(NodeEGPSv1 a);

	/**
	 * set the parent of this Node. It is important to note that this method ONLY
	 * establish the up-link, ie, from this Node to parent, but down-link is not
	 * established, i.e. this Node may not be the child of its parent. Therefore,
	 * this method is intended to be used internally.
	 *
	 */
	void removeAllParent();

	/**
	 * setParentAt(0, Node a)
	 */
	void setParent(NodeEGPSv1 a);

	/**
	 * Get first parent.
	 */
	NodeEGPSv1 getParent();

	// To conveniently get leaves and internal nodes
	/**
	 * yudalang's annotation: this function is a recursive function. You can use by
	 * this way: Set<Node> leavesNodes = new HashSet<>();
	 * node.getAllLeaves(leavesNodes, node);
	 * 
	 * QUESTION: why we need to return the same Set<Node> as paramter. Answer: Make
	 * a rule ? QUESTION: Why not make it a static method in Node utility? Answer:
	 * Make a rule ?
	 * 
	 * @param par
	 *            is it-self
	 * @return ElegantJTable Node's all children, including it-self.
	 */
	Set<NodeEGPSv1> getAll_leaves();

	Set<NodeEGPSv1> getAll_internalNodes();

	// Methods for dealing with child
	/** return the number of children */
	int getChildCount();

	/**
	 * get child at index k
	 */
	NodeEGPSv1 getChildAt(int k);

	NodeEGPSv1 getFirstChild();

	NodeEGPSv1 getLastChild();

	boolean insertChildAt(int index, NodeEGPSv1 a);

	/**
	 * Remove k-th child
	 *
	 * Remove break both down-link and up-link from this Node to the one being
	 * removed.
	 */
	NodeEGPSv1 removeChild(int k);

	/** remove a child */
	boolean removeChild(NodeEGPSv1 a);

	/** get the index of a node */
	int indexOfChild(NodeEGPSv1 a);

	/**
	 * set the ith child to be a. Adding child always result in proper link between
	 * parent and child, i.e. both links from parent (this) to child and from child
	 * to parent are installed.
	 * 
	 * yudalang: Substitute the Node at index of k.
	 * 
	 * @param k
	 *            is the index of child, if k equal to total num of current
	 *            children, add the Node
	 * @param a
	 *            is a Node
	 */
	boolean setChildAt(int k, NodeEGPSv1 a);

	/** Add a child to the end of children list. */
	void addChild(NodeEGPSv1 a);

	/** Remove all childrens */
	void removeAllChild();

	/** get the number of leaf for current node. */
	int getNumOfLeaves();

	// added by GF
	/**
	 * get the sibling node of Node
	 * 
	 * For a Node a, find its index in the parent and than If the later one exists,
	 * return the later one, else return former one or null(for no sibing).
	 */
	NodeEGPSv1 getSibling(NodeEGPSv1 a);

	/** set bootstrap value */
	void setBs(double bsValue);

	/** get bootstrap value */
	double getBs();

	NodeEGPSv1 getSibling();

	void setSibling(NodeEGPSv1 sibling);

	// the following four attributes are specific to lineAttribute and
	// leafAttribute, since the 'name' must be unique for lineAttribute and
	// leafAttribute
	/**
	 * set the node's name
	 */
	void setName(String name);

	/**
	 * get the node's name
	 */
	String getName();

	public int getNameID();

	public void setNameID(int ID);

	/** get leaf name if current node is leaf */
	String getLeafName();

	void setLeafName(String leafName);

	// methods for dealing with branches
	/** Get branch of current node */
	DefaultBranch getBranch();

	DefaultBranch getBranch(int k);

	int getSize();

	/**
	 * The size means the number of leaves this node has.<br>
	 * e.g. leaf's size is 1; root's size is the number of all children. \@see
	 * GenealogyAnalyzer#initialSize
	 * 
	 * @author yudalang
	 * @param k
	 *            set the number of leaves
	 */
	void setSize(int k);

	void clear();

	/**
	 * normally it is a shadow clone a node
	 */
	NodeEGPSv1 getClone();

	// for radial layout
	void setRadialAngle(double ra);

	double getRadialAngle();

	// for circular layout
	void setCircularT1(double t1);

	double getCircularT1();

	void setCircularBasey(double by);

	double getCircularBasey();

	void setCircularLeafAngle(double la);

	double getCircularLeafAngle();

	void setAngle(double angle);

	double getAngle();

	void setDepth(int newDep);

	int getDepth();

	// TO BE DELETE ===================================
	double getBasey();

	void setBasey(double basey);
	// TO BE DELETE ===================================

	// for left
	double getAnnoXLeft();

	void setAnnoXLeft(double annoXLeft);

	// for right
	double getAnnoXRight();

	void setAnnoXRight(double annoXRight);

	// for right and left
	double getAnnoYsmallLR();

	void setAnnoYsmallLR(double annoYsmallLR);

	double getAnnoYBiglLR();

	void setAnnoYBiglLR(double annoYBiglLR);

	// for up
	double getAnnoYUp();

	void setAnnoYUp(double annoYUp);

	// for down
	double getAnnoYDown();

	void setAnnoYDown(double annoYDown);

	// for up and for down
	double getAnnoXsmallUD();

	void setAnnoXsmallUD(double annoXsmallUD);

	double getAnnoXBigUD();

	void setAnnoXBigUD(double annoXBigUD);

	/** for leaf label */
	Rectangle getLabelRect();

	void setLabelRect(Rectangle labelRect);

	boolean isSelect();

	void setSelect(boolean isSelect);

	boolean isVisible();

	void setVisible(boolean isVisible);

	double getLeafComWidth();

	void setLeafComWidth(double leafComWidth);

	double getLeafComHeight();

	void setLeafComHeight(double leafComHeight);


	Boolean getKeyConfig();

	void setKeyConfig(Boolean keyConfig);

	/**
	 * collapse the branch for internal node!
	 */
	Boolean isCollapsed();

	void setCollapsed(boolean isCollapsed);

}