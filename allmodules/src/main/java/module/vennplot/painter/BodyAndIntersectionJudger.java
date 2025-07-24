package module.vennplot.painter;

import java.awt.Point;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

import module.vennplot.model.DataModel;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;

public class BodyAndIntersectionJudger {
	
	List<BodyIntersectionSelection> bodyIntersectionSelections = new ArrayList<>();

	public boolean judgeWhetherSelected(PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel, Point point) {
		Double tt = new Double(point.getX() - 1, point.getY() - 1, 2, 2);
		
		return judgeWhetherSelected(paintingLocations, parameterModel,dataModel,tt);
	}

	public boolean judgeWhetherSelected(PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel, Double tt) {
		boolean ret = false;
		bodyIntersectionSelections.clear();
		
		double x = paintingLocations.getIntersectRegionTopleftX();
		double y = paintingLocations.getIntersectRegionTopleftY();
		double w = paintingLocations.getIntersectRegionWidth();
		double h = paintingLocations.getIntersectRegionHeight();
		double hOfBodyRegion = paintingLocations.getBodyHeight();
		
		double intersectRegionElementWidth = paintingLocations.getIntersectRegionElementWidth();
		
		IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
		List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();
		
		int numOfIntersectionItems = eles.size();
		for (int i = 0; i < numOfIntersectionItems; i++) {
			IntersectionRegionElement intersectionRegionElement = eles.get(i);
			double xx = i * intersectRegionElementWidth + x;
			double hh = h * intersectionRegionElement.getRatio_this2highest();
			double yy = y + h - hh;
			double ww = parameterModel.getTopBarWidth();
			
			if (tt.intersects(xx, yy, ww, hh + hOfBodyRegion)) {
				bodyIntersectionSelections.add(new BodyIntersectionSelection(i));
				ret = true;
			}
		}		
		
		return ret;
	}

	public List<BodyIntersectionSelection> getBodyIntersectionSelections() {
		return bodyIntersectionSelections;
	}
}
