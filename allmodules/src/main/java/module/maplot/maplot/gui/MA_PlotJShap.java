package module.maplot.maplot.gui;

import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Optional;

import module.maplot.maplot.model.DataModel;
import module.maplot.maplot.model.PaintPlotData;
import module.maplot.maplot.model.PaintingPoint;
import module.maplot.maplot.model.ValuePoint;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName MA_PlotJShap
 * 
 * @author mhl
 * 
 * @Date Created on:2020-01-07 16:08
 * 
 */
public class MA_PlotJShap {

	private MA_PlotDrawProperties drawProperties;

	protected String tooTipText;

	public MA_PlotJShap(MA_PlotDrawProperties drawProperties) {
		this.drawProperties = drawProperties;
	}

	public boolean contains(double xPos, double yPos) {
		PaintPlotData paintPlotData = drawProperties.getCalculatorPlotDataForViewer().getPaintPlotData();

		if (paintPlotData == null) {

			return false;
		}

		List<PaintingPoint> paintingPoints = paintPlotData.getPaintingPoints();
		
		DataModel dataModel = drawProperties.getCurrDrawingDataModel();

		for (PaintingPoint paintingPoint : paintingPoints) {

			Integer scatterSize = (Integer) drawProperties.getLeftToolJPanel().getScatterSize().getValue();

			Ellipse2D.Double circle = new Ellipse2D.Double(paintingPoint.getXPos(), paintingPoint.getYPos(),
					scatterSize, scatterSize);

			if (circle.contains(xPos, yPos)) {

				// tipText = xPos + " " + yPos;

				StringBuffer sb = new StringBuffer();
				
				sb.append("<html><body>");
				ValuePoint originalPoint = paintingPoint.getOriginalPoint();
				sb.append("ID: ").append(originalPoint.getId()).append("<br/>");
				sb.append("A value: ").append(originalPoint.getXValue()).append("<br/>");
				sb.append("M value: ").append(originalPoint.getYValue()).append("<br/>");

				Optional<Double> pValue = originalPoint.getPValue();

				if (pValue.isPresent()) {
					sb.append("P value: ").append(pValue.get());
				}
				
				List<String> annotations = originalPoint.getAnnotations();
				if (annotations != null) {
					List<String> headerFields = dataModel.getHeaderFields();
					sb.append("<hr><p>");
					int size = headerFields.size();
					for (int i = 0; i < size; i++) {
						sb.append(headerFields.get(i)).append(":\t");
						sb.append(annotations.get(i)).append("<br/>");
					}
				}
				
				sb.append("</body></html>");

				tooTipText = sb.toString();
				return true;
			}
		}

		return false;
	}

	private void getConversionValue(double pos) {

	}

	public String getToolTipText() {
		return tooTipText;
	}
}
