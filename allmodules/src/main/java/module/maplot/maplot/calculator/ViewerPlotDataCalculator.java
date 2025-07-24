package module.maplot.maplot.calculator;

import module.maplot.maplot.gui.MA_PlotDrawProperties;
import module.maplot.maplot.model.DataModel;
import module.maplot.maplot.model.PaintPlotData;

public class ViewerPlotDataCalculator {

	private LocationCalculator locationCalculator;

	private double oldWidth = -1;

	private double oldHeight = -1;

	private PaintPlotData paintPlotData;

	private MA_PlotDrawProperties drawProperties;

	public ViewerPlotDataCalculator(MA_PlotDrawProperties drawProperties) {
		this.drawProperties = drawProperties;

		this.locationCalculator = new LocationCalculator();
	}

	public PaintPlotData calculatePaintingLocations(DataModel dataModel, double width, double height) {

		if (oldWidth != width || oldHeight != height || drawProperties.isReCalculate()) {

			paintPlotData = locationCalculator.calculatePaintingLocations(drawProperties, dataModel, width, height);

			oldWidth = width;
			oldHeight = height;
			drawProperties.setReCalculate(false);

		}
		return paintPlotData;
	}

	public PaintPlotData getPaintPlotData() {
		return paintPlotData;
	}

}
