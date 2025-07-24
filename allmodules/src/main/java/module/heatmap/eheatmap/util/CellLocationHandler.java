package module.heatmap.eheatmap.util;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

import module.heatmap.eheatmap.enums.CellShape;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.rectpainter.RectShapePainter;

public abstract class CellLocationHandler {
	
	final protected Color selectBorderColor = new Color(33,115,70);
	final protected Color selectCellColor = Color.lightGray;
	
	protected int numOfRows;
	protected int numOfCols;
	
	protected int[] hfactors ;

	protected void twoDimIterateProcess(CellHandler cellHandler,PaintingLocationParas p, ParameterModel paraModel, DataModel dataModel) {

		int gapSize = paraModel.getGapSize();
		int[] vGapLocations = paraModel.getvGapLocations();
		double[][] dataMatrix = dataModel.getDataMatrix();
		
		FactorHandler vfactorHandler = new FactorHandler(0, vGapLocations, vGapLocations.length);

		double cellWidth = p.getCellWidth();
		double cellHeight = p.getCellHeight();
		
		CellShape cellShape = paraModel.getCellShapePainter();
		RectShapePainter painter = 
				CellShapeFactory.obtainRectLayoutCellShapePainter(cellShape, cellWidth, cellHeight);
		
		for (int i = 0; i < numOfRows; i++) {
			int ttt = FactorCalculator.getIncreasedAddingFactor(vfactorHandler, i);
			double y = i * cellHeight + p.getHmapLeftTopY() +  ttt* gapSize;
			
			int length = dataMatrix[i].length;
			for (int j = 0; j < length; j++) {
				double x = j * cellWidth + p.getHmapLeftTopX() + hfactors[j] * gapSize;
				Double r = new Double(x, y, cellWidth, cellHeight);
				
				cellHandler.handleProcess(painter,i,j,x,y);
			}
		}

	}
	
	
}

