package module.evolview.phylotree.visualization.graphics.struct;

public class ShowLeafPropertiesInfo {

	boolean showLeafLabel;
	
	/**
	 * 下面两个变量的目的是为了防止计算布局的时候重复添加间距. <br>
	 * 是否需要因为第一次显示label而调整 布局的留白 <br>
	 * 其实应该这样表述更好，是否需要因为在之前都是隐藏的情况下，第一次显示label而 调整布局的留白
	 */
	boolean needChange4showLabel;
	/**
	 * 是否是因为在之前显示label之后，第一次隐藏label而调整布局的留白
	 */
	boolean needChange4hideLabel;
	
	
	public boolean isShowLeafLabel() {
		return showLeafLabel;
	}
	public void setShowLeafLabel(boolean showLeafLabel) {
		this.showLeafLabel = showLeafLabel;
	}
	public boolean isNeedChange4showLabel() {
		return needChange4showLabel;
	}
	public void setNeedChange4showLabel(boolean needChange4showLabel) {
		this.needChange4showLabel = needChange4showLabel;
	}
	public boolean isNeedChange4hideLabel() {
		return needChange4hideLabel;
	}
	public void setNeedChange4hideLabel(boolean needChange4hideLabel) {
		this.needChange4hideLabel = needChange4hideLabel;
	}
	
	
	
}
