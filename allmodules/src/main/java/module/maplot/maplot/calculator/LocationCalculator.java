package module.maplot.maplot.calculator;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import egps2.utils.common.math.Tools;
import module.maplot.maplot.gui.MA_PlotDrawProperties;
import module.maplot.maplot.model.DataModel;
import module.maplot.maplot.model.IndicatingLine;
import module.maplot.maplot.model.PaintPlotData;
import module.maplot.maplot.model.PaintingLine;
import module.maplot.maplot.model.PaintingPoint;
import module.maplot.maplot.model.Scale;
import module.maplot.maplot.model.ValuePoint;

public class LocationCalculator {

	private final int blinkSpaceLength = 100;

	private double extendingPercentage = 0.05;

	private int numOfTicks = 5;

	private int tick = 4;

	private int metrics = 20;

	/**
	 * When the interface width or height changes, recalculate the drawing data
	 * position information
	 *
	 * @return PaintPlotData drawing data position information
	 * 
	 * @author mhl
	 *
	 * @Date Created on:2019-12-27 15:19
	 */
	public PaintPlotData calculatePaintingLocations(MA_PlotDrawProperties drawProperties, DataModel dataModel,
			double width, double height) {

		double maxXValue = dataModel.getMaxXValue();

		double minXValue = dataModel.getMinXValue();

		double maxYValue = dataModel.getMaxYValue();

		double minYValue = dataModel.getMinYValue();

		double availableWidth = width - blinkSpaceLength - blinkSpaceLength;

		double availableHeight = height - blinkSpaceLength - blinkSpaceLength;

		double xRange = maxXValue - minXValue;

		double xLeftBlink = extendingPercentage * xRange;

		double newXRange = xRange + xLeftBlink + xLeftBlink;

		double yRange = maxYValue - minYValue;

		double yTopBlink = extendingPercentage * yRange;

		double newYRange = yRange + yTopBlink + yTopBlink;

		PaintPlotData paintPlotData = new PaintPlotData();

		// 计算点的位置坐标
		List<PaintingPoint> paintingPoints = calculatorPaintingPoint(dataModel, minXValue, minYValue, availableWidth,
				availableHeight, xLeftBlink, newXRange, yTopBlink, newYRange);

		paintPlotData.setPaintingPoints(paintingPoints);

//		maxXPos = paintingPoints.stream().mapToDouble(paintingPoint -> paintingPoint.getXPos()).max().getAsDouble();
//		minXPos = paintingPoints.stream().mapToDouble(paintingPoint -> paintingPoint.getXPos()).min().getAsDouble();
//		maxYPos = paintingPoints.stream().mapToDouble(paintingPoint -> paintingPoint.getYPos()).max().getAsDouble();
//		minYPos = paintingPoints.stream().mapToDouble(paintingPoint -> paintingPoint.getYPos()).min().getAsDouble();
//
//		xRange = maxXPos - minXPos;
//		yRange = maxYPos - minYPos;
//		newXRange = xRange + xLeftBlink + xLeftBlink;
//
//		newYRange = yRange + yTopBlink + yTopBlink;

		// 计算绘制线段的坐标以及刻度
		PaintingLine paintingLine = calculatorPaintingLine(width, height, availableWidth, availableHeight, newXRange,
				newYRange, minXValue, minYValue, xLeftBlink, yTopBlink);

		paintPlotData.setPaintingLine(paintingLine);

		boolean should = drawProperties.getLeftToolJPanel().getCriticalValuePanel().shouldPaintIndicatingLine();
		if (should) {
			IndicatingLine calculate4IndicatingLine = calculate4IndicatingLine(paintPlotData, drawProperties, minXValue,
					minYValue, availableWidth, availableHeight, xLeftBlink, newXRange, yTopBlink, newYRange, width,
					height);
			paintPlotData.setChangeIndicatLine(calculate4IndicatingLine);
		}

		return paintPlotData;
	}

	/**
	 * 
	 * Calculate the position information of the drawing point
	 *
	 * @author mhl
	 *
	 * @Date Created on:2019-12-27 15:21
	 */
	private List<PaintingPoint> calculatorPaintingPoint(DataModel dataModel, double minXValue, double minYValue,
			double availableWidth, double availableHeight, double xLeftBlink, double newXRange, double yTopBlink,
			double newYRange) {

		OptionalDouble maxPValue = dataModel.getMaxPValue();

		List<PaintingPoint> paintingPoints = new ArrayList<PaintingPoint>();

		Color color1 = Color.red;

		Color color2 = Color.blue;

		for (ValuePoint valuePoint : dataModel.getValuePoints()) {

			PaintingPoint paintingPoint = new PaintingPoint();

			double xPos = (valuePoint.getXValue() - (minXValue - xLeftBlink)) / newXRange * availableWidth
					+ blinkSpaceLength;

			double yPos = availableHeight + blinkSpaceLength
					- (valuePoint.getYValue() - (minYValue - yTopBlink)) / newYRange * availableHeight;

			// paintingPoint.setId(originalPoint.getId());

			paintingPoint.setXPos(xPos);

			paintingPoint.setYPos(yPos);

			paintingPoint.setOriginalPoint(valuePoint);

			// paintingPoint.setAValue(originalPoint.getXPos());
			// paintingPoint.setMValue(originalPoint.getYPos());

			// Ellipse2D.Double circle = new Ellipse2D.Double(xPos, yPos, scatterSize,
			// scatterSize);

			// paintingPoint.setCircle(circle);

			Optional<Double> pValue = valuePoint.getPValue();

			if (pValue.isPresent()) {

				Double value = pValue.get();

				// float ratio = (float) (value / 1.0);
				double ratio = (double) (value / maxPValue.getAsDouble());

				int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));

				int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));

				int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));

				Color paintingColor = new Color(red, green, blue);

				paintingPoint.setPaintingColor(paintingColor);

			} else {

				paintingPoint.setPaintingColor(Color.BLACK);

			}
			// paintingPoint.setPValue(pValue);

			paintingPoints.add(paintingPoint);
		}
		return paintingPoints;
	}

	private IndicatingLine calculate4IndicatingLine(PaintPlotData paintPlotData, MA_PlotDrawProperties properties,
			double minXValue, double minYValue, double availableWidth, double availableHeight, double xLeftBlink,
			double newXRange, double yTopBlink, double newYRange, double width, double height) {

		double criticalValue = properties.getLeftToolJPanel().getCriticalValuePanel().getCriticalValue();

		double yy1 = criticalValue;
		double yy2 = -criticalValue;

		double yPos1 = availableHeight + blinkSpaceLength
				- (yy1 - (minYValue - yTopBlink)) / newYRange * availableHeight;
		double yPos2 = availableHeight + blinkSpaceLength
				- (yy2 - (minYValue - yTopBlink)) / newYRange * availableHeight;

		Line2D.Double upLine = new Line2D.Double(blinkSpaceLength, yPos1, width - blinkSpaceLength,
				yPos1);
		Line2D.Double downLine = new Line2D.Double(blinkSpaceLength, yPos2, width - blinkSpaceLength,
				yPos2);

		IndicatingLine indicatingLine = new IndicatingLine();

		indicatingLine.setFrontLine(upLine);
		indicatingLine.setBehindLine(downLine);

		List<PaintingPoint> paintingPoints = paintPlotData.getPaintingPoints();
		for (PaintingPoint valuePoint : paintingPoints) {
			if (valuePoint.getYPos() < yPos2 && valuePoint.getYPos() > yPos1) {
				valuePoint.setPaintingColor(Color.gray);
			}
		}

		return indicatingLine;
	}

	/**
	 * 
	 * Calculate the position information of the drawing axis
	 *
	 * @Date Created on:2019-12-27 15:21
	 */

	private PaintingLine calculatorPaintingLine(double width, double height, double availableWidth,
			double availableHeight, double newXRange, double newYRange, double minXValue, double minYValue,
			double xLeftBlink, double yTopBlink) {

		List<Scale> xAxisLocations = new ArrayList<Scale>();

		for (int i = 0; i < numOfTicks; i++) {

			Scale scale = new Scale();

			double per = i / (double) numOfTicks;

			double xAxisLocation = per * availableWidth + blinkSpaceLength;

			double yForXAxis = height - blinkSpaceLength;

			scale.setXLine(new Line2D.Double(xAxisLocation, yForXAxis, xAxisLocation, yForXAxis - tick));

			scale.setAxisValue(Tools.formatDoubleKeepTwoDecimal(per * newXRange + minXValue - xLeftBlink));

			scale.setValueLocation(new Point2D.Double(xAxisLocation, yForXAxis + metrics));

			xAxisLocations.add(scale);
		}

		List<Scale> yAxisLocations = new ArrayList<Scale>();

		for (int i = 0; i < numOfTicks; i++) {
			Scale scale = new Scale();

			double per = i / (double) numOfTicks;

			double yAxisLocation = availableHeight + blinkSpaceLength - per * availableHeight;

			double xForXAxis = blinkSpaceLength;

			scale.setXLine(new Line2D.Double(xForXAxis, yAxisLocation, xForXAxis + tick, yAxisLocation));

			scale.setAxisValue(Tools.formatDoubleKeepTwoDecimal(per * newYRange + minYValue - yTopBlink));

			scale.setValueLocation(new Point2D.Double(xForXAxis, yAxisLocation));

			yAxisLocations.add(scale);
		}

		PaintingLine paintingLine = new PaintingLine();

		paintingLine.setXLine(new Line2D.Double(blinkSpaceLength, height - blinkSpaceLength, width - blinkSpaceLength,
				height - blinkSpaceLength));

		paintingLine.setXAxisLocations(xAxisLocations);

		paintingLine.setYLine(
				new Line2D.Double(blinkSpaceLength, blinkSpaceLength, blinkSpaceLength, height - blinkSpaceLength));

		paintingLine.setYAxisLocations(yAxisLocations);

		return paintingLine;
	}

}
