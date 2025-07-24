package module.heatmap.eheatmap.model.selection;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;


public class ColNameSelectionJudger extends AbstractNameSelectionJudger {

	public boolean getAndJudgeSelectionForOneClick(PaintingLocationParas areaLocation, DataModel dataModel,
			ParameterModel paraModel, Point2D p) {

		if (!paraModel.isIfPaintColNames()) {
			return false;
		}
		boolean colJudgeProcess = commonColJudge(areaLocation, dataModel, paraModel, 
				(dd) -> {return dd.contains(p);}, 
				true);
		return colJudgeProcess;
	}

	public boolean getAndJudgeSelectionForRegion(PaintingLocationParas areaLocation, DataModel dataModel,
			ParameterModel paraModel, Rectangle2D.Double rect) {
		
		if (!paraModel.isIfPaintColNames()) {
			return false;
		}
		
		boolean colJudgeProcess = commonColJudge(areaLocation, dataModel, paraModel, 
				(dd) -> {return dd.intersects(rect);}, 
				false);

		return colJudgeProcess;
	}

}
