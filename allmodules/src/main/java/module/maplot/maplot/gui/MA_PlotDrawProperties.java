package module.maplot.maplot.gui;

import java.awt.Font;

import egps2.UnifiedAccessPoint;
import module.maplot.maplot.calculator.ViewerPlotDataCalculator;
import module.maplot.maplot.model.DataModel;


public class MA_PlotDrawProperties {

	private ViewerPlotDataCalculator dataForViewer;

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private DataModel originalDataModel;
	private DataModel currDrawingDataModel;
	private LeftToolJPanel leftToolJPanel;
	private boolean isReCalculate;

	public MA_PlotDrawProperties(DataModel dataModel) {
		this.originalDataModel = dataModel;
		this.currDrawingDataModel = dataModel;
		this.dataForViewer = new ViewerPlotDataCalculator(this);
	}

	public DataModel getOriginalDataModel() {
		return originalDataModel;
	}
	
	public DataModel getCurrDrawingDataModel() {
		return currDrawingDataModel;
	}

	public void setCurrDrawingDataModel(DataModel currDrawingDataModel) {
		this.currDrawingDataModel = currDrawingDataModel;
	}

	public ViewerPlotDataCalculator getCalculatorPlotDataForViewer() {

		return dataForViewer;
	}

	public Font getDefaultFont() {
		return defaultFont;
	}

	public void setDefaultFont(Font defaultFont) {
		this.defaultFont = defaultFont;
	}
	

	public void setLeftToolJPanel(LeftToolJPanel leftToolJPanel) {
		this.leftToolJPanel = leftToolJPanel;
	}

	public LeftToolJPanel getLeftToolJPanel() {

		return leftToolJPanel;
	}

	public Font getTitleFont() {
		return titleFont;
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	public boolean isReCalculate() {
		return isReCalculate;
	}

	/**
	 * 这一步是用来计算是否需要 从数据的值转换到 画布坐标的值
	 * @param isReCaculate
	 */
	public void setReCalculate(boolean isReCaculate) {
		this.isReCalculate = isReCaculate;
	}

}
