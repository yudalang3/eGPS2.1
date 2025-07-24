package module.vennplot.painter;

import java.awt.Point;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

import module.vennplot.model.DataModel;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SingleRegionDataModel;
import module.vennplot.model.SingleRegionElement;

public class NameSelectionJudger {
	
	List<NameSelection> nameSelections = new ArrayList<>();

	public boolean judgeWhetherSelected(PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel, Point point) {
		Double tt = new Double(point.getX() - 1, point.getY() - 1, 2, 2);
		
		return judgeWhetherSelected(paintingLocations, parameterModel,dataModel,tt);
	}

	public boolean judgeWhetherSelected(PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel, Double tt) {
		boolean ret = false;
		nameSelections.clear();
		// First is name region
		double x = paintingLocations.getNameRegionTopleftX();
		double y = paintingLocations.getNameRegionTopleftY();
		double w = paintingLocations.getNameRegionWidth();
		double h = paintingLocations.getNameRegionHeight();
		
		double iterateHeight = paintingLocations.getBodyRegionElementHeight();
		
		SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
		List<SingleRegionElement> eles = singleRegionDataModel.getEles();
		
		int numOfSets = dataModel.getNumOfSets();
		
		for (int i = 0; i < numOfSets; i++) {
			SingleRegionElement singleRegionElement = eles.get(i);
			double xx = x + 20;
			
			double yy = i * iterateHeight + iterateHeight + y;
			
			if (tt.intersects(xx, yy - 10, 50, 10)) {
				nameSelections.add(new NameSelection(i));
				ret = true;
			}
			
			
		}
		// Next is top title
		x = paintingLocations.getMiddleAreaTopleftX();
		y = paintingLocations.getMiddleAreaTopleftY();
		w = paintingLocations.getMiddleAreaWidth();
		h = paintingLocations.getMiddleAreaHeight();
		
		if (tt.intersects(x, y, w, h)) {
			nameSelections.add(new NameSelection(-2));
			ret = true;
		}
		
		// Final is bottom title
		x = paintingLocations.getSingleRegionTopleftX();
		y = paintingLocations.getSingleRegionTopleftY();
		w = paintingLocations.getSingleRegionWidth();
		h = paintingLocations.getSingleRegionHeight();
		
		int axisTipLength = parameterModel.getAxisTipLength();
		final int LineBarGap = 10;
		double yTitle = y + h + axisTipLength + 2 * 5 + LineBarGap;
		
		if (tt.intersects(x, yTitle, w, 20)) {
			nameSelections.add(new NameSelection(-1));
			ret = true;
		}
		
		return ret;
		
	}
	
	public List<NameSelection> getNameSelections() {
		return nameSelections;
	}

}
