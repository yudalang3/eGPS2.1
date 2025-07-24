package module.heatmap.eheatmap.model.selection;

import module.heatmap.eheatmap.tree.GraphcsNode;

public class TreeNodeSelection {
	GraphcsNode graphcsNode;
	boolean ifRowSelected;
	
	public TreeNodeSelection(GraphcsNode graphcsNode, boolean ifRowSelected) {
		this.graphcsNode = graphcsNode;
		this.ifRowSelected = ifRowSelected;
	}

	public void setGraphcsNode(GraphcsNode graphcsNode) {
		this.graphcsNode = graphcsNode;
	}
	
	public GraphcsNode getGraphcsNode() {
		return graphcsNode;
	}

	public boolean isIfRowSelected() {
		return ifRowSelected;
	}

	public void setIfRowSelected(boolean ifRowSelected) {
		this.ifRowSelected = ifRowSelected;
	}
	
}
