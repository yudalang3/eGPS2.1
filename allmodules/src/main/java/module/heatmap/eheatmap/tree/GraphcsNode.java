package module.heatmap.eheatmap.tree;

import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;

public class GraphcsNode extends ArrayBasedNode{
	
	//This is for rectangular
	LineObj verticaLineObj ;
	LineObj horizontalLineObj;
	//This is for circular
	RadialObjct radialObj;
	TangentialObjct tangentialObj;
	boolean isSelectedByUser = false;
	
	/** To record the orignal order! Note this order is 0 based*/
	protected int orignalOrderInMatrix = -1;

	public GraphcsNode(int nodeID) {
		ID = nodeID;
	}
	
	/**
	 * 这个方法保留作为克隆节点的方法，先留在。
	 * 现在是所有节点都直接用 GraphicsNode创建出来的。
	 * @param tnode
	 */
	private GraphcsNode(EvolNode tnode) {
		
		GraphcsNode node = (GraphcsNode) tnode;
		
		
		String name = node.getName();
		if (name != null) {
			this.name = name;
		}
		this.ID = node.getID();
		
		this.orignalOrderInMatrix = node.getOrignalOrderInMatrix();
		
		int childCount = node.getChildCount();
		for (int i = 0; i < childCount; i++) {
			GraphcsNode gNode = (GraphcsNode) node.getChildAt(i);
			GraphcsNode tt = new GraphcsNode(gNode);
			addChild(tt);
		}
		
	}
	
	public LineObj getVerticaLineObj() {
		if (verticaLineObj == null) {
			verticaLineObj = new LineObj();
		}
		return verticaLineObj;
	}
	
	public LineObj getHorizontalLineObj() {
		if (horizontalLineObj == null) {
			horizontalLineObj = new LineObj();
		}
		return horizontalLineObj;
	}

	public boolean isSelectedByUser() {
		return isSelectedByUser;
	}

	public void setSelectedByUser(boolean isSelectedByUser) {
		this.isSelectedByUser = isSelectedByUser;
	}

	public RadialObjct getRadialObj() {
		if (radialObj ==null) {
			radialObj = new RadialObjct();
		}
		return radialObj;
	}

	public TangentialObjct getTangentialObj() {
		if (tangentialObj == null) {
			tangentialObj = new TangentialObjct();
		}
		return tangentialObj;
	}

	public int getOrignalOrderInMatrix() {
		return orignalOrderInMatrix;
	}

	public void setOrignalOrderInMatrix(int orignalOrderInMatrix) {
		this.orignalOrderInMatrix = orignalOrderInMatrix;
	}
	
	
}



