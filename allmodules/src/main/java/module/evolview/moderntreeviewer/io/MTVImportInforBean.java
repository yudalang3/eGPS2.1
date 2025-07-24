package module.evolview.moderntreeviewer.io;

import java.awt.Font;

import egps2.UnifiedAccessPoint;
import module.evoltreio.EvolTreeImportInfoBean;
import graphic.engine.guicalculator.BlankArea;

public class MTVImportInforBean extends EvolTreeImportInfoBean {

	protected BlankArea blank_space;
	protected String titleString;
	
	protected boolean showLeafLabel = false;
	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
	
	protected boolean shouldLeafNameRightAlign = false;
	
	protected boolean whetherWidthScaleOnMouseWheel = true;
	protected boolean whetherHeightScaleOnMouseWheel = true;
	
	
	protected boolean needReverseAxisBar = false;
	protected String branchLengthUnit = "Time (MYA)";
	private String nodeVisualAnnotationConfigFilePath;
	
	public boolean isShouldLeafNameRightAlign() {
		return shouldLeafNameRightAlign;
	}

	public void setShouldLeafNameRightAlign(boolean shouldLeafNameRightAlign) {
		this.shouldLeafNameRightAlign = shouldLeafNameRightAlign;
	}
	
	
	public void setBlank_space(BlankArea egpsInsets) {
		this.blank_space = egpsInsets;
	}

	public BlankArea getBlank_space() {
		return blank_space;
	}

	public String getTitleString() {
		return titleString;
	}

	public void setTitleString(String titleString) {
		this.titleString = titleString;
	}

	public boolean isShowLeafLabel() {
		return showLeafLabel;
	}

	public void setShowLeafLabel(boolean showLeafLabel) {
		this.showLeafLabel = showLeafLabel;
	}

	public Font getDefaultFont() {
		return defaultFont;
	}

	public void setDefaultFont(Font defaultFont) {
		this.defaultFont = defaultFont;
	}

	public boolean isWhetherWidthScaleOnMouseWheel() {
		return whetherWidthScaleOnMouseWheel;
	}

	public void setWhetherWidthScaleOnMouseWheel(boolean whetherWidthScaleOnMouseWheel) {
		this.whetherWidthScaleOnMouseWheel = whetherWidthScaleOnMouseWheel;
	}

	public boolean isWhetherHeightScaleOnMouseWheel() {
		return whetherHeightScaleOnMouseWheel;
	}

	public void setWhetherHeightScaleOnMouseWheel(boolean whetherHeightScaleOnMouseWheel) {
		this.whetherHeightScaleOnMouseWheel = whetherHeightScaleOnMouseWheel;
	}
	
	public boolean isNeedReverseAxisBar() {
		return needReverseAxisBar;
	}

	public void setNeedReverseAxisBar(boolean needReverseAxisBar) {
		this.needReverseAxisBar = needReverseAxisBar;
	}

	public void setBranchLengthUnit(String string) {
		branchLengthUnit = string;
	}

	public String getBranchLengthUnit() {
		return branchLengthUnit;
	}

	public Font getDefaultTitleFont() {
		return defaultTitleFont;
	}

	public void setDefaultTitleFont(Font defaultTitleFont) {
		this.defaultTitleFont = defaultTitleFont;
	}

	public void setNodeVisualAnnotationConfigFilePath(String string) {
		this.nodeVisualAnnotationConfigFilePath = string;
	}
	
	public String getNodeVisualAnnotationConfigFilePath() {
		return nodeVisualAnnotationConfigFilePath;
	}
	
}
