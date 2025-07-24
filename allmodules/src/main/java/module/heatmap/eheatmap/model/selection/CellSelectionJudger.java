package module.heatmap.eheatmap.model.selection;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;


public class CellSelectionJudger {
	
	List<CellSelection> cellSelections = new ArrayList<CellSelection>();
	
	public boolean getAndJudgeSelectionForOneClick(PaintingLocationParas areaLocation, ParameterModel paraModel,Point2D p) {
		double width = areaLocation.getCellWidth();
		double height = areaLocation.getCellHeight();
		
		int numOfRows = areaLocation.getNumOfRows();
		int numOfCols = areaLocation.getNumOfCols();
		
		double x0 = p.getX();
        double y0 = p.getY();
        
        int[] hGapLocations = paraModel.gethGapLocations();
		FactorHandler hfactorHandler = new FactorHandler(0,hGapLocations,hGapLocations.length);
		int[] hfactors = new int[numOfCols];
		for (int i = 0; i < numOfCols; i++) {
			hfactors[i] = FactorCalculator.getIncreasedAddingFactor(hfactorHandler,i);
		}
		int gapSize = paraModel.getGapSize();
		int[] vGapLocations = paraModel.getvGapLocations();
		FactorHandler vfactorHandler = new FactorHandler(0, vGapLocations, vGapLocations.length);
		
		for (int i = 0; i < numOfRows; i++) {
			int ttt = FactorCalculator.getIncreasedAddingFactor(vfactorHandler, i);
			double y = i * height + areaLocation.getHmapLeftTopY() +  ttt* gapSize;
			for (int j = 0; j < numOfCols; j++) {
				double x = j * width + areaLocation.getHmapLeftTopX() + hfactors[j] * gapSize;
				
				boolean ifcontains = ifContaions(x0, y0, x, y, width, height);
		        if (ifcontains) {
		        	cellSelections.clear();
		        	CellSelection cellSelection = new CellSelection();
		        	cellSelection.setTopLeftPointLocation(i,j);
		        	cellSelections.add(cellSelection);
					return true;
				}
			}
		}
		
		return false;

	}
	
	/**
	 * 
	 * @param areaLocation : PaintingLocationParas
	 * @param paraModel : ParameterModel
	 * @param rect : Rectangle2D.Double user dragged area
	 */
	public boolean getAndJudgeSelectionForRegion(PaintingLocationParas areaLocation, ParameterModel paraModel,Rectangle2D.Double rect) {
		
		cellSelections.clear();
		boolean hasElement = false;
		double width = areaLocation.getCellWidth();
		double height = areaLocation.getCellHeight();
		
		int numOfRows = areaLocation.getNumOfRows();
		int numOfCols = areaLocation.getNumOfCols();
        
		int[] hGapLocations = paraModel.gethGapLocations();
		FactorHandler hfactorHandler = new FactorHandler(0,hGapLocations,hGapLocations.length);
		int[] hfactors = new int[numOfCols];
		for (int i = 0; i < numOfCols; i++) {
			hfactors[i] = FactorCalculator.getIncreasedAddingFactor(hfactorHandler,i);
		}
		int gapSize = paraModel.getGapSize();
		int[] vGapLocations = paraModel.getvGapLocations();
		FactorHandler vfactorHandler = new FactorHandler(0, vGapLocations, vGapLocations.length);
		
		for (int i = 0; i < numOfRows; i++) {
			int ttt = FactorCalculator.getIncreasedAddingFactor(vfactorHandler, i);
			double y = i * height + areaLocation.getHmapLeftTopY() +  ttt* gapSize;
			for (int j = 0; j < numOfCols; j++) {
				double x = j * width + areaLocation.getHmapLeftTopX()+ hfactors[j] * gapSize;
				
				boolean ifcontains = rect.intersects(x, y, width, height);
		        if (ifcontains) {
		        	CellSelection cellSelection = new CellSelection();
		        	cellSelection.setTopLeftPointLocation(i,j);
		        	cellSelections.add(cellSelection);
					hasElement = true;
				}
			}
		}
		
		return hasElement;

	}
	/**
	 * 
	 * @param x : target x
	 * @param y : target y
	 * 
	 * @param x0
	 * @param y0
	 * @param width
	 * @param height
	 * @return
	 */
	private boolean ifContaions(double x,double y,double x0,double y0,double width,double height) {
		return (x >= x0 && y >= y0 && x < x0 + width && y < y0 + height);
	}

	public List<CellSelection> getCellSelections() {
		return cellSelections;
	}
}
