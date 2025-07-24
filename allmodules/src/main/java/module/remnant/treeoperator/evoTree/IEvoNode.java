package module.remnant.treeoperator.evoTree;

import java.util.Set;

/**
 * 
 * <p>
 * Title: BioNode Description: The Node interface for the evolutaional node.
 * <pre>
 * Node in a Tree(or a graphics) has the following properties. 
 * (1) ElegantJTable Node may have zero to many children Nodes
 * (2) ElegantJTable Node may have zero to many parent Nodes
 * (3) Node can have branch associated with it.
 * </pre>
 * Define an abstruct node. Node should not related with GUI!
 * We need to subject to JDK collection frame API design pattern!<br>
 * The node is a generic type class.
 * </p>
 * 
 * @author yudalang
 * @date 2019-1-10
 */
public interface IEvoNode extends Cloneable{

	// Time is from root node to current node!
	double getTime();
	void setTime(double time);
	/** set bootstrap value */
	void setBootstrapV(int bsValue);
	/** get bootstrap value */
	int getBootstrapV();

	/*
	// These methods may needed in the future
	double getNumOfChangeToParent();
	void setNumOfChangeToParent(int changeCount);*/

	// Dealing with multiple parents
	/** get the parent count */
	int getParentCount();
	/** get k-th parent*/
	IEvoNode getParentAt(int k);
	/** append a parent*/
	boolean addParent(IEvoNode a);
	/** remove a certain parent and all its parents*/
	boolean removeParent(IEvoNode a);
	/** pop a k-th parent*/
	IEvoNode popParent(int k);
	/** replace k-th parent with Node a*/
	boolean setParentAt(int k, IEvoNode a);
	/** return the index of Node a if a is a parent of current node*/
	int indexOfParent(IEvoNode a);
	/** remove all parents, i.e remove all up-links*/
	void removeAllParent();
	/** Get all parents. */
	IEvoNode[] getParents();
	
	// Methods for dealing with child
	/** return the number of children */
	int getChildCount();
	/** get child at index k*/
	IEvoNode getChildAt(int k);
	/** get first child*/
	IEvoNode getFirstChild();
	/** get last child*/
	IEvoNode getLastChild();
	/** Get all children. */
	IEvoNode[] getChildren();
	/** insert child after k, if k > len - 1, append the parent*/
	boolean insertChildAt(int k, IEvoNode a);
	/** pop k-th child and all its child!*/
	IEvoNode popChild(int k);
	/** remove a child and all its child!*/
	boolean removeChild(IEvoNode a);
	/** get the index of a node */
	int indexOfChild(IEvoNode a);
	/** replace k-th child with Node a*/
	boolean setChildAt(int k, IEvoNode a);
	/** append a child to the end of children list. */
	void addChild(IEvoNode a);
	/** Remove all childrens and down-links */
	void removeAllChild();
	
	/**
	 * get all sibling Nodes of Node a, they have common parents
	 */
	IEvoNode[] getSibling();

	/** get the number of leaf for current node. */
	int getNumOfLeaves();
	/** Here size is the number of all children*/
	void setSize(int k);
	/** Here size is the number of all children*/
	int getSize();
	/** get all leaves. */
	Set<IEvoNode> getAll_leaves();
	/** get all internal nodes, including itself!*/
	Set<IEvoNode> getAll_internalNodes();
	
	/** set the node's name */
	void setName(String name);
	/** get the node's name */
	String getName();

	public int getNodeID();
	public void setNodeID(int ID);

	/** get leaf name if current node is leaf */
	String getLeafName();
	void setLeafName(String leafName);

	// methods for dealing with branches
	/** Get branch of current node */
	IEvoBranch[] getBranch();
	/** Get k-th branch of current node, if k not properly return null! */
	IEvoBranch getBranch(int k);
	void setBranch(IEvoBranch[] branches);
	
	/**
	 * normally it is a shadow clone a node.
	 * Note this method is not fully clone of the Node, for it
	 * not includes the up and down links.
	 * if you want to use the full clone, please use {@link NodeUtil#cloneNode}
	 */
	IEvoNode getClone();
}
