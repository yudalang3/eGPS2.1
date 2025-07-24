package evoltree.struct;

import java.util.Objects;

/**
 * Default implementation of the EvolNode interface using array-based storage.
 *
 * This class provides a basic implementation for representing evolutionary tree nodes
 * using arrays to store child and parent relationships.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class ArrayBasedNode implements EvolNode {
	protected EvolNode[] child;
	protected EvolNode[] parent;
	protected int nextChild = 0, nextParent = 0;
	protected String name = "";
	public static int nextID;
	protected int ID;

	/** Num. of leaves */
	protected int size;
	protected double length;
	
	protected double variable;

	public ArrayBasedNode() {
		child = new EvolNode[2];
		parent = new EvolNode[1];
		ID = nextID++;
	}


	/**
	 * remove kth node
	 */
	private static EvolNode remove(EvolNode[] a, int k) {
		if (k < 0 || k >= a.length || a[k] == null)
			return null;
		EvolNode b = a[k];
		for (int i = k; i < a.length - 1; i++)
			a[i] = a[i + 1];
		a[a.length - 1] = null;
		return b;
	}

	/**
	 * remove a node from a node array
	 */
	private static boolean remove(EvolNode[] a, EvolNode b) {
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
	 * grow a Node array by one
	 */
	private static EvolNode[] growNodeArray(EvolNode[] a) {
		EvolNode[] tmp = a;
		a = new EvolNode[a.length + 1];
		System.arraycopy(tmp, 0, a, 0, tmp.length);
		return a;
	}

	/**
	 * 
	 * Find the index of b in the array!
	 *  
	 * @title indexOf
	 * @createdDate 2020-10-14 15:37
	 * @lastModifiedDate 2020-10-14 15:37
	 * @author "yudalang"
	 * @since 1.7
	 *   
	 * @param a the node array
	 * @param b the target node
	 * @return int : -1 if not find!
	 */
	private static int indexOf(EvolNode[] a, EvolNode b) {
		for (int i = a.length; i-- > 0;)
			if (a[i] == b)
				return i;
		return -1;
	}

	/**
	 * Add a child to the end of children list.
	 */
	@Override
	public void addChild(EvolNode a) {
		if (nextChild == child.length)
			child = growNodeArray(child);
		child[nextChild++] = a;
		a.addParent(this);
	}

	@Override
	public boolean setChildAt(int k, EvolNode a) {
		if (k == nextChild) {
			addChild(a);
			return true;
		}

		EvolNode b = child[k];
		child[k] = a;
		b.removeParent(this); // the Node being replaced has no this parent
		a.addParent(this);
		return true;
	}

	/**
	 * return the number of children
	 */
	@Override
	public int getChildCount() {
		return nextChild;
	}

	/*
	 * remove a child
	 */
	@Override
	public boolean removeChild(EvolNode a) {
		if (remove(child, a)) {
			nextChild--;
			a.removeParent(this);
			return true;
		}

		return false;
	}

	
	@Override
	public EvolNode removeChild(int k) {
		int n = getChildCount();
		if (k < 0 || k >= n)
			return null;

		EvolNode a = child[k];
		for (int i = k; i < n - 1; i++)
			child[i] = child[i + 1];
		child[n - 1] = null;
		nextChild--;

		a.removeParent(this);
		return a;
	}

	/**
	 * 
	 * <p>Title: insertChildAt</p>   
	 * <p>Description: 
	 *    return false if append the node to the end. Same as {@link #addChild(EvolNode)}
	 *    return true if insert at target location.
	 * </p>   
	 * @param index the inserted index
	 * @param popMod the node to insert
	 * @return the node inserted successfully or not
	 *
	 */
	@Override
	public boolean insertChildAt(int index, EvolNode popMod) {
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

	/**
	 * get the index of a node
	 */
	@Override
	public int indexOfChild(EvolNode a) {
		return indexOf(child, a);
	}

	/**
	 * get child at index k
	 */
	@Override
	public EvolNode getChildAt(int k) {
		return child[k];
	}

	@Override
	public EvolNode getFirstChild() {
		return child[0];
	}

	@Override
	public EvolNode getLastChild() {
		return child[nextChild - 1];
	}

	/**
	 * 
	 * <p>Title: getParentCount</p>   
	 * <p>Description: 
	 *    Support multiple parent!
	 * </p>   
     *
	 */
	@Override
	public int getParentCount() {
		return nextParent;
	}

	@Override
	public int indexOfParent(EvolNode a) {
		return indexOf(parent, a);
	}

	@Override
	public void removeAllParent() {
		for (int i = nextParent - 1; i >= 0; i--) {
			EvolNode a = parent[i];
			parent[i] = null;
			nextParent--;
			a.removeChild(this);
		}
	}
	
	/**
	 * 
	 * <p>Title: addParent</p>   
	 * <p>Description: 
	 *    Support multiple parent so append parent array!
	 * </p>   
     *
	 */
	@Override
	public void addParent(EvolNode a) {
		if (nextParent == parent.length) {
			parent = growNodeArray(parent);
		}

		parent[nextParent++] = a;
	}

	@Override
	public void setParent(EvolNode a) {
		setParentAt(0, a);
	}

	@Override
	public EvolNode getParent() {
		return parent[0];
	}

	/**
	 * 
	 * <p>Title: setParentAt</p>   
	 * <p>Description: 
	 *    Support multiple parent.
	 * </p>   
	 * @param k the index of parent
	 * @return boolean : return false if node already existed.
	 * @lastModifiedDate 2025-04-07 15:37
	 *
	 */
	@Override
	public boolean setParentAt(int k, EvolNode a) {
		if (indexOf(parent, a) >= 0) {
			return false; // do not allow duplicate parent
		}
		
		if (k < nextParent) {
			// 如果要插入的k位置比先有的parent数量小
			EvolNode b = parent[k];
			b.removeChild(this); // the Node being replaced has
			parent[k] = a;
		}else {
			addParent(a);
		}

		return true;
	}

	@Override
	public boolean removeParent(EvolNode a) {
		if (remove(parent, a)) {
			nextParent--;
			return a.removeChild(this);
		}

		return false;
	}

	@Override
	public EvolNode removeParent(int k) {
		if (k < 0 || k >= getParentCount())
			return null;
		EvolNode a = remove(parent, k);
		if (a != null) {
			nextParent--;
			a.removeChild(this);
		}

		return a;
	}

	/**
	 * return the parent
	 */
	@Override
	public EvolNode getParentAt(int k) {
		return parent[k];
	}

	/**
	 * set the node's name
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the node's name
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Node ID: ").append(ID).append(" Node name: ").append(name).append("\t");

		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			sb.append(" ").append(getChildAt(i).getID());
		}

		return sb.toString();
	}

	@Override
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public void setSize(int s) {
		this.size = s;
	}
	

	@Override
	public void setLength(double len) {
		this.length = len;
	}


	@Override
	public double getLength() {
		return length;
	}


	@Override
	public double getDoubleVariable() {
		return variable;
	}


	@Override
	public void setDoubleVariable(double value) {
		variable = value;
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
		return ID == other.ID;
	}
	
}