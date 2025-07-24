package module.evolview.phylotree.visualization.graphics.struct;

public class AdvancedParametersBean {

	private int zoomInAreaStepInBlank = 200;
	private int zoomInAreaRangeForNode = 2500;

	public int getZoomInAreaStepInBlank() {
		return zoomInAreaStepInBlank;
	}

	public void setZoomInAreaStepInBlank(int zoomInAreaStepInBlank) {
		this.zoomInAreaStepInBlank = zoomInAreaStepInBlank;
	}

	public int getZoomInAreaRangeForNode() {
		return zoomInAreaRangeForNode;
	}

	public void setZoomInAreaRangeForNode(int zoomInAreaRangeForNode) {
		this.zoomInAreaRangeForNode = zoomInAreaRangeForNode;
	}

}
