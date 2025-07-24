package module.evolview.gfamily.work.model.tree;

import java.util.Objects;

import module.evolview.phylotree.visualization.graphics.phylogeny.DefaultGraphicsPhyloNode;


public class Node4BasicNCov19 extends DefaultGraphicsPhyloNode {

	private final CGBID cgbid = new CGBID();

	public Node4BasicNCov19() {

	}

	public Node4BasicNCov19(String string) {
		this.name = string;
	}


	@Override
	public String toString() {
		return getCGBID();
	}

	public int getCgbIDFirst() {
		return cgbid.cgbIDFirst;
	}

	public void setCgbIDFirst(int cgbIDFirst) {
		this.cgbid.cgbIDFirst = cgbIDFirst;
	}

	public int getCgbIDLast() {
		return cgbid.cgbIDLast;
	}

	public void setCgbIDLast(int cgbIDLast) {
		this.cgbid.cgbIDLast = cgbIDLast;
	}

	/**
	 * 得到字符串形式的CGBID。 注意：不要通过 字符串的比较来确定 两个节点的CGBID相等。而是要通过比较两个Integer的值来比较。
	 */
	public String getCGBID() {
		return cgbid.toString();
	}
	
	public CGBID getCGBIDInstance() {
		return cgbid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(getCgbIDFirst(), getCgbIDLast());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Node4BasicNCov19 other = (Node4BasicNCov19) obj;
		return getCgbIDFirst() == other.getCgbIDFirst() && getCgbIDLast() == other.getCgbIDLast();
	}

//
//	public static void main(String[] args) {
//		Node4BasicNCov19 node = new Node4BasicNCov19();
//
//		node.cgbIDFirst = 1239;
//		node.cgbIDLast = 444;
//		
//		node.addChild(new Node4BasicNCov19());
//
//		System.out.println(node.getCGBID());
//	}
}
