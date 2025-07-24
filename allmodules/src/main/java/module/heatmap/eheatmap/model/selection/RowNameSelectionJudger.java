package module.heatmap.eheatmap.model.selection;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;


public class RowNameSelectionJudger extends AbstractNameSelectionJudger {

	public boolean getAndJudgeSelectionForOneClick(PaintingLocationParas areaLocation, DataModel dataModel,
			ParameterModel paraModel, Point2D p) {
		
		if (!paraModel.isIfPaintRowNames()) {
			return false;
		}
		boolean rowJudgeProcess = commonRowJudge(areaLocation, dataModel, paraModel, 
				(dd) -> {return dd.contains(p);},
				true);

		return rowJudgeProcess;
	}

	public boolean getAndJudgeSelectionForRegion(PaintingLocationParas areaLocation, DataModel dataModel,
			ParameterModel paraModel, Rectangle2D.Double rect) {
		if (!paraModel.isIfPaintRowNames()) {
			return false;
		}
		boolean rowJudgeProcess = commonRowJudge(areaLocation, dataModel, paraModel, 
				(dd) -> {return dd.intersects(rect);},
				false);

		return rowJudgeProcess;
	}

}

@FunctionalInterface
interface PointIntersect {
	boolean isIntersect(Shape dd);
}

@FunctionalInterface
interface RectIntersect {
	boolean isIntersect(Shape dd);
}
