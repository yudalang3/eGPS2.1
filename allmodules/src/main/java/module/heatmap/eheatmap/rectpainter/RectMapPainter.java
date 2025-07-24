package module.heatmap.eheatmap.rectpainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graphic.engine.GradientColorHolder;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.selection.CellSelection;
import module.heatmap.eheatmap.util.CellHandler;
import module.heatmap.eheatmap.util.CellLocationHandler;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;

public class RectMapPainter extends CellLocationHandler{
	
	public void painting(Graphics2D g2d, PaintingLocationParas p, DataModel dataModel, GradientColorHolder gColorHolder,
			ParameterModel paraModel) {
		double[][] dataMatrix = dataModel.getDataMatrix();
		int[] rowOrderMaping = dataModel.getRowOrderMaping();
		int[] colOrderMaping = dataModel.getColOrderMaping();
		numOfRows = p.getNumOfRows();
		numOfCols = p.getNumOfCols();
		double maxMinusMix = dataModel.getMaxValue() - dataModel.getMinValue();
		
		Set<Integer> xSet = new HashSet<Integer>();
		Set<Integer> ySet = new HashSet<Integer>();
		
		CellSelection oneSelection  = null;
		if (paraModel.isCellOneMouseHasClick()) {
			List<CellSelection> cellSelections = paraModel.getCellSelections();
			if (cellSelections.size() > 0) {
				oneSelection  = cellSelections.get(0);
			}
		}else if (paraModel.isHasMouseDragEvent()) {
			List<CellSelection> cellSelections = paraModel.getCellSelections();
			for (CellSelection cellSelection : cellSelections) {
				xSet.add(cellSelection.getIi());
				ySet.add(cellSelection.getJj());
			}
			
		}
		
		int[] hGapLocations = paraModel.gethGapLocations();
		FactorHandler hfactorHandler = new FactorHandler(0,hGapLocations,hGapLocations.length);
		hfactors = new int[numOfCols];
		for (int i = 0; i < numOfCols; i++) {
			hfactors[i] = FactorCalculator.getIncreasedAddingFactor(hfactorHandler,i);
		}
		
		CellHandler cellHandler = new CellHandler(g2d,oneSelection) {
			public void handleProcess(RectShapePainter painter,int i,int j,double x,double y) {
				double d = dataMatrix[rowOrderMaping[i]][colOrderMaping[j]];
				double value = ( d- dataModel.getMinValue()) / maxMinusMix;
				Shape r = painter.getShape(x, y, d, value);
				if (paraModel.isCellOneMouseHasClick()) {
					if (Double.isNaN(value)) {
						g2d.setColor(Color.gray);
					}else {
						g2d.setColor(gColorHolder.getColorFromPallet(value));
					}
					
					g2d.fill(r);
					
					if (oSelection != null && oSelection.equals(i,j)) {
						g2d.setColor(selectBorderColor);
						Stroke oriStroke = g2d.getStroke();
						g2d.setStroke(new BasicStroke(3));
						g2d.draw(r);
						g2d.setStroke(oriStroke);
					}
				}else if (paraModel.isHasMouseDragEvent()) {
					if (xSet.contains(i) && ySet.contains(j)) {
						g2d.setColor(selectCellColor);
						g2d.fill(r);
					}else {
						if (Double.isNaN(value)) {
							g2d.setColor(Color.gray);
						}else {
							g2d.setColor(gColorHolder.getColorFromPallet(value));
						}
						g2d.fill(r);
					}
				}else {
					if (Double.isNaN(value)) {
						g2d.setColor(Color.gray);
					}else {
						g2d.setColor(gColorHolder.getColorFromPallet(value));
					}
					g2d.fill(r);
				}
			};
		};
		
		twoDimIterateProcess(cellHandler, p, paraModel,dataModel);
	}

	public void paintingBorder(Graphics2D g2d, PaintingLocationParas areaLocation, ParameterModel paraModel,DataModel dataModel) {
		g2d.setColor(paraModel.getBorderColor());
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(1));
		
		CellHandler cellHandler = new CellHandler(g2d,null) {
			public void handleProcess(RectShapePainter painter,int i,int j,double x,double y) {
				Shape r = painter.getShape(x, y, 0, 0);
				g2d.draw(r);
			};
		};
		
		twoDimIterateProcess(cellHandler, areaLocation, paraModel,dataModel);
		
		g2d.setStroke(oldStroke);
	}

	public void paintValues(Graphics2D g2d, PaintingLocationParas areaLocation, ParameterModel paraModel,
			DataModel dataModel, boolean ifShowOrigValue, boolean ifGetPartial) {
		
		int[] rowOrderMaping = dataModel.getRowOrderMaping();
		int[] colOrderMaping = dataModel.getColOrderMaping();
		
		g2d.setColor(paraModel.getDataValueColor());
		g2d.setFont(paraModel.getDataValueFont());

		double[][] dataMatrix;

		double min = 0;double max = 0;
		double partialFactor = paraModel.getDataValuePartialFactor();
		if (ifShowOrigValue) {
			dataMatrix = dataModel.getOriDataMatrix();
			min = dataModel.getOriMinValue();
			max = dataModel.getOriMaxValue();
		} else {
			dataMatrix = dataModel.getDataMatrix();
			min = dataModel.getMinValue();
			max = dataModel.getMaxValue();
		}
		
		final double minValueInMatrix = min;
		final double maxValueInMatrix = max;
		
		DecimalFormat df = paraModel.getDecimalFormat();
		FontMetrics fontMetrics = g2d.getFontMetrics();
		double cellHeight = areaLocation.getCellHeight();
		double cellWidth = areaLocation.getCellWidth();
		double halfHeight = 0.5 * fontMetrics.getHeight();
		
		CellHandler cellHandler = new CellHandler(g2d,null) {
				
			public void handleProcess(RectShapePainter r,int i, int j,double x,double y) {
				double value = dataMatrix[rowOrderMaping[i]][colOrderMaping[j]];
				
				if (ifGetPartial) {
					if (value > minValueInMatrix + partialFactor && value < maxValueInMatrix-partialFactor) {
						return;
					}
				}
				String format = df.format(value);
				int stringWidth = fontMetrics.stringWidth(format);
				g2d.drawString(format, (float) (x + 0.5 * cellWidth - 0.5 * stringWidth), (float) (y+ 0.5 * cellHeight + halfHeight));
			};
		};
		
		twoDimIterateProcess(cellHandler, areaLocation, paraModel,dataModel);
		
	}
	
}

