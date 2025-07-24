package module.remnant.treeoperator;

import java.util.Set;

import module.remnant.treeoperator.evoTree.NodeUtil;

public interface GNode {
	
	/**Methods for deal with parents !**/

	/** get the parent count */
	int getParentCount();
	/** get k-th parent*/
	GNode getParentAt(int k);
	GNode getFirstParent();
	/** append a parent*/
	boolean addParent(GNode  a);
	/** remove a certain parent and all its children*/
	boolean removeParent(GNode  a);
	/** pop a k-th parent*/
	GNode popParent(int k);
	/** replace k-th parent with Node a*/
	boolean setParentAt(int k, GNode a);
	/** return the index of Node a if a is a parent of current node*/
	int indexOfParent(GNode a);
	/** remove all parents, i.e remove all up-links*/
	void removeAllParent();
	/** Get all parents. */
	GNode[] getParents();

	/**Methods for dealing with child!!**/

	/** return the number of children */
	int getChildCount();
	/** get child at index k*/
	GNode getChildAt(int k);
	/** get first child*/
	GNode getFirstChild();
	/** get last child*/
	GNode getLastChild();
	/** Get all children. */
	GNode[] getChildren();
	/** insert child after k, if k > len - 1, append the parent*/
	boolean insertChildAt(int k, GNode a);
	/** pop k-th child and all its child!*/
	GNode popChild(int k);
	/** remove a child and all its children!*/
	boolean removeChild(GNode a);
	/** get the index of a node */
	int indexOfChild(GNode a);
	/** replace k-th child with Node a*/
	boolean setChildAt(int k, GNode a);
	/** append a child to the end of children list. */
	void addChild(GNode a);
	/** Remove all childrens and down-links */
	void removeAllChild();

	/**Some convenient utilities!!**/

	/** Get all leaves of current node!*/
	Set<GNode> getLeaves();
	/** Get all internal nodes of current node!*/
	Set<GNode> getInternalNodes();
	/**get all sibling Nodes of current node, they have common parent*/
	GNode[] getSiblings();
	/** get the number of leaf for current node. */
	int getNumOfLeaves();
	/** This is the variable leaves for some computations. For example UPGMA
	 * 1. It can be represented the number of leaves. 
	 * The difference between {@link #getNumOfLeaves()}	is this is the quickly way to access.	 
	 */
	int getSize();
	/** This is the variable leaves for some computations. For example UPGMA
	 * 1. It can be represented the number of leaves. 
	 * The difference between {@link #getNumOfLeaves()}	is this is the quickly way to access.	 
	 */
	void setSize(int s);
	/** set the node's name */
	void setName(String name);
	/** get the node's name */
	String getName();
	/** set the node's leaf name, if it is a internal node, return null!*/
	void setLeafName(String name);
	/** get the node's leaf name , if it is a internal node, return null!*/
	String getLeafName();
	/** Node ID, integer*/
	public int getNodeID();
	/** Node ID, integer*/
	public void setNodeID(int ID);
	/**
	 * normally it is a shadow clone a node.
	 * Note this method is not fully clone of the Node, for it
	 * not includes the up and down links.
	 * if you want to use the full clone, please use {@link NodeUtil#cloneNode}
	 */
	GNode getClone();

	/** methods for dealing with branches **/
	/** Get branch of current node */
	GBranch[] getBranches();
	/** Get k-th branch of current node, if k not properly return null! */
	GBranch getBranch(int k);
	GBranch getFirstBranch();
	void setBranches(BasicBranch[] branches);
	
	/** For bootstap */
	double getBootstrapValue();
	void setBootstrapValue(double bs);
	
}
