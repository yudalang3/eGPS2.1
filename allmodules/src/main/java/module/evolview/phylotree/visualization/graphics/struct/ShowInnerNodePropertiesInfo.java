package module.evolview.phylotree.visualization.graphics.struct;

public class ShowInnerNodePropertiesInfo {

	private boolean showInternalNodeLabel = false;
	private boolean showInternalNodeBranchLength = false;
	private boolean showInternalNodeBootstrap = false;
	
	
	
	public boolean isShowInternalNodeLabel() {
		return showInternalNodeLabel;
	}
	public void setShowInternalNodeLabel(boolean showInternalNodeLabel) {
		this.showInternalNodeLabel = showInternalNodeLabel;
	}
	public boolean isShowInternalNodeBranchLength() {
		return showInternalNodeBranchLength;
	}
	public void setShowInternalNodeBranchLength(boolean showInternalNodeBranchLength) {
		this.showInternalNodeBranchLength = showInternalNodeBranchLength;
	}
	public boolean isShowInternalNodeBootstrap() {
		return showInternalNodeBootstrap;
	}
	public void setShowInternalNodeBootstrap(boolean showInternalNodeBootstrap) {
		this.showInternalNodeBootstrap = showInternalNodeBootstrap;
	}
	
	
}
