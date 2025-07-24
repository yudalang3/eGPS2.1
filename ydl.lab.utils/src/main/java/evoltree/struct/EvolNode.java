package evoltree.struct;


/**
 * 
 * The interface define methods for the most top-level generic node. <br>
 * 
 * <p>
 * {@code Node} in a Tree(or a graphics) has the following properties. <br>
 * </p>
 * <ol>
 *    <li>A Node may have zero to many children Nodes.</li>
 *    <li>A Node may have zero to many parent Nodes.</li>
 *    <li>Node can have branch associated with it.</li>
 * </ol>
 * 
 * <h2> The rule and usage of this interface. </h2>
 * 
 * <h3> 1. Down-links and Up-links </h3>
 * <p>
 * Methods deal with children <b>must always</b> correctly handle up-links and down-links.
 * <p>
 * The meanings of links are:
 * </p>
 * <pre>
 *     |------------------ leaf1
 *     |
 *     |root
 *     |          |---------------leaf2
 *     |----------|internal1
 *                |----------------leaf3
 *     For a node, example internal1: it has up-link: root; down-links: leaf2 and leaf3
 *     for node leaf2: it only has up-link internal1
 * </pre>
 * 
 * So, if you need to remove leaf2 from internal1.
 *     {@code internal1.removeChild(leaf2)} will correctly remove down-links of internal1
 *     and up-links of leaf2.
 *     {@code leaf2.removeParent(internal1)} will only up-links in leaf2.
 * <p>
 *  But this is not absolutely, the implementation can also correctly deal with parent, it depends on the implementation.
 * 
 * <h3> 2. Multiple parents or not </h3>
 * 
 * <p> The most cases in evolution, i.e. phylogenetic tree do not need to support multiple parents.
 *     So be caution to read the {@code method javadoc}, for example {@link #getParentCount()} to see if the {@code Implementation}  support or not.
 * </p>
 * 
 * 
 * &#064;createdDate  2020-10-11 21:21
 * &#064;lastModifiedDate  2020-10-11 21:21
 * @author many people (including Li WenXiong, Fu YunXing and Li Haipeng);"yudalang"
 *
 */
public interface EvolNode {

	/**
	 * 
	 * Users should caution that the implementation of this {@code interface} support multiple parents or not!
	 * The {@link ArrayBasedNode} not support multiple parents.
	 * 
	 * <ul>
	 * <li>For implementation support multiple, it may return 0,1,2 ...</li>
	 * <li>For implementation only support phylogentic tree</li>
	 * </ul>
	 *  
	 * @title  getParentCount
	 * &#064;createdDate  2020-10-12 15:51
	 * &#064;lastModifiedDate  2020-10-12 15:51
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @return int : The number of parents.
	 */
    int getParentCount();

	/**
	 * 
	 * Get k-th parent. Please check k is less than {@link #getParentCount()}!
	 * <p>
	 * @title  getParentAt
	 * &#064;createdDate  2020-10-14 15:20
	 * &#064;lastModifiedDate  2020-10-14 15:20
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param k : the index of parent.
	 * @return EvolNode
	 */
    EvolNode getParentAt(int k);

	/**
	 * 
	 * Add one parent to current node. 
	 * 
	 * <h2> The rule and usage of this method. </h2>
	 * 
	 * For parent support multiple add one more parent;
	 * For parent not support, replace the old one.
	 * <p>
	 * @title  addParent
	 * &#064;createdDate  2020-10-14 15:21
	 * &#064;lastModifiedDate  2020-10-14 15:21
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param a: the parent to be added.
     */
    void addParent(EvolNode a);

	/**
	 * 
	 * Remove specific parent!
	 * <p>
	 * if input Node is not in parent array, then return false.
	 * <p>
	 * @title  removeParent
	 * &#064;createdDate  2020-10-14 15:31
	 * &#064;lastModifiedDate  2020-10-14 15:31
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param a: the parent to be removed.
	 * @return boolean : success or not.
	 */
    boolean removeParent(EvolNode a);

	/**
	 * 
	 * remove k-th parent.
	 * If k < 0 or k >= getParentCount(), it will return null!
	 * <p>
	 * @title  removeParent
	 * &#064;createdDate  2020-10-14 15:34
	 * &#064;lastModifiedDate  2020-10-14 15:34
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param k: the index of parent.
	 * @return EvolNode
	 */
    EvolNode removeParent(int k);

	/**
	 * 
	 *  Return false if node already existed if support multiple parent.
	 * <p>
	 *  If implementation not support multiple parent, when adding one more parent should return false.
	 *  
	 * @title  setParentAt
	 * @createdDate 2020-10-14 15:46
	 * @lastModifiedDate 2020-10-14 15:46
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param k: the place to set
	 * @param a: the parent to be set.
	 * @return boolean
	 */
    boolean setParentAt(int k, EvolNode a);

	/**
	 *
	 * Return the index of parent.
	 *
	 * @title indexOfParent
	 * @createdDate 2020-10-14 15:48
	 * @lastModifiedDate 2020-10-14 15:48
	 * @author "yudalang"
	 * @since 1.7
	 *
	 * @param a: the parent to be found.
	 * @return int : -1 for not existed in parent.
	 */
    int indexOfParent(EvolNode a);

	/**
	 * 
	 * Remove all parents.
	 *  
	 * @title removeAllParent
	 * @createdDate 2020-10-14 16:07
	 * @lastModifiedDate 2020-10-14 16:07
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 */
    void removeAllParent();

	/**
	 * 
	 * Same as setParentAt(0, a);
	 *  
	 * @title setParent
	 * @createdDate 2020-10-14 16:08
	 * @lastModifiedDate 2020-10-14 16:08
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param a: the parent to be set.
	 */
    void setParent(EvolNode a);

	/**
	 * 
	 * Get first child.
	 *  
	 * @title getParent
	 * @createdDate 2020-10-14 16:10
	 * @lastModifiedDate 2020-10-14 16:10
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @return EvolNode
	 */
    EvolNode getParent();

	// methods for dealing with child
	
	/**
	 * 
	 * Get the number of child count.
	 *  
	 * @title getChildCount
	 * @createdDate 2020-10-14 16:12
	 * @lastModifiedDate 2020-10-14 16:12
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @return int
	 */
    int getChildCount();

	/**
	 * 
	 * The user should check if k >=0 && k < {@link #getChildCount()}.
	 *  
	 * @title getChildAt
	 * @createdDate 2020-10-14 16:13
	 * @lastModifiedDate 2020-10-14 16:13
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param k: the index of child.
	 * @return EvolNode
	 */
    EvolNode getChildAt(int k);

	EvolNode getFirstChild();

	EvolNode getLastChild();

	/**
	 * 
	 * Insert child at specific position.
	 * <p>
	 *     Not replace the child.
	 * </p>
	 *  
	 * @title insertChildAt
	 * @createdDate 2020-10-14 16:15
	 * @lastModifiedDate 2020-10-14 16:15
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param index : >= 0
	 * @param a: the child to be inserted.
	 * @return boolean: it depends on the implementation.
	 */
    boolean insertChildAt(int index, EvolNode a);

	/**
	 *
	 * Set the ith child to be <code>a</code>. Adding child always result in proper link between
	 * parent and child, i.e. both links from parent (this) to child and from child
	 * to parent are installed.
	 *
	 * <p>
	 *     The difference between insertChildAt() function is that this is the replace method.
	 * </p>
	 *  
	 * @title setChildAt
	 * @createdDate 2020-10-14 16:26
	 * @lastModifiedDate 2020-10-14 16:26
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param k: the place to set
	 * @param a: the child to be set.
	 * @return  false if node already existed. but this is not necessary.
	 */
    boolean setChildAt(int k, EvolNode a);

	/**
	 * 
	 * Remove k-th child
	 * <p>
	 * Remove break both down-link and up-link from this Node to the one being
	 * removed.
	 *  
	 * @title removeChild
	 * @createdDate 2020-10-14 16:21
	 * @lastModifiedDate 2020-10-14 16:21
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param k : should >= 0 && < numberOfChild.
	 * @return EvolNode : if k < 0 or k >= numberOfChild return null.
	 */
    EvolNode removeChild(int k);

	/**
	 * 
	 * remove specific node.
	 * If successfully removed, return true.
	 *  
	 * @title removeChild
	 * @createdDate 2020-10-14 16:23
	 * @lastModifiedDate 2020-10-14 16:23
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param a: the node to be removed.
	 * @return boolean
	 */
    boolean removeChild(EvolNode a);
	
	/**
	 * 
	 * Return -1 if not found.
	 * <p>
	 * @title  indexOfChild
	 * &#064;createdDate  2020-10-14 16:23
	 * &#064;lastModifiedDate  2020-10-14 16:23
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param a: the node to be found.
	 * @return int
	 */
    int indexOfChild(EvolNode a);

	void addChild(EvolNode a);

	void removeAllChild();

	int getID();

	int getSize();

	/**
	 * This is the variable leaves for some computations.
	 * <p>
	 * For example UPGMA
	 * 1. It can be represented the number of leaves.
	 * The difference between {@code NodeUtil.getNumOfLeaves()} is this is the quick way to access.
	 */
	void setSize(int s);

	/**
	 * this is a fast way to store a variable!
	 */
	double getDoubleVariable();

	void setDoubleVariable(double value);

	/** set the node's name */
	void setName(String name);

	/** get the node's name */
	String getName();

	/** set the node's name */
	void setLength(double len);

	/** get the node's name */
	double getLength();

}