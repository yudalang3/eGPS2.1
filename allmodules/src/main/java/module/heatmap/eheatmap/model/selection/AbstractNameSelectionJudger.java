package module.heatmap.eheatmap.model.selection;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;


public class AbstractNameSelectionJudger {

	protected List<NameSelection> rowNameSelections = new ArrayList<NameSelection>();
	protected List<NameSelection> colNameSelections = new ArrayList<NameSelection>();

	public List<NameSelection> getRowNameSelections() {
		return rowNameSelections;
	}

	public List<NameSelection> getColNameSelections() {
		return colNameSelections;
	}

	protected boolean commonColJudge(PaintingLocationParas areaLocation, DataModel dataModel, ParameterModel paraModel,
			PointIntersect pointIntersect, boolean b) {

		colNameSelections.clear();
		boolean ret = false;
		double x = areaLocation.getColNamesLeftTopX();
		double y = areaLocation.getColNamesLeftTopY();

		float xx = (float) x;
		float yy = (float) y;

		String[] colNames = dataModel.getColNames();

		int length = colNames.length;
		double cellWidth = areaLocation.getCellWidth();
		int[] colOrderMaping = dataModel.getColOrderMaping();

		List<Integer> nameWidths = paraModel.getColNameWidths();

		for (int i = 0; i < length; i++) {

			// Double dd = new Rectangle2D.Double(xx, yy, cellWidth,
			// areaLocation.getColNamesHeight());
			Double dd = new Double(xx, yy, cellWidth, nameWidths.get(i));

			AffineTransform at = new AffineTransform();
			int degree = paraModel.getRowNamesRotaionAngle();
			at.rotate(Math.toRadians(degree), xx, yy);
			Shape newRect = at.createTransformedShape(dd);
			at.rotate(-Math.toRadians(degree), xx, yy);

			if (pointIntersect.isIntersect(newRect)) {

				String string = colNames[colOrderMaping[i]];
				NameSelection nameSelection = new NameSelection();
				nameSelection.setName(string);
				nameSelection.setIndex(colOrderMaping[i]);
//				nameSelection.setIndex(getCurr2OriMapingIndex(colOrderMaping,i));
				colNameSelections.add(nameSelection);
				ret = true;
				if (b) {
					return ret;
				}
			}
			xx += cellWidth;
		}

		return ret;
	}

	protected boolean commonRowJudge(PaintingLocationParas areaLocation, DataModel dataModel, ParameterModel paraModel,
			RectIntersect rectIntersect, boolean b) {

		rowNameSelections.clear();
		boolean ret = false;
		double x = areaLocation.getRowNamesLeftTopX();
		double y = areaLocation.getRowNamesLeftTopY();

		float yy = (float) y;
		float xx = (float) x;

		double cellHeight = areaLocation.getCellHeight();
		int[] rowOrderMaping = dataModel.getRowOrderMaping();
		int[] vGapLocations = paraModel.getvGapLocations();
		FactorHandler vfactorHandler = new FactorHandler(0, vGapLocations, vGapLocations.length);
		int gapSize = paraModel.getGapSize();

		String[] rowNames = dataModel.getRowNames();
		int length = rowNames.length;

		List<Integer> nameWidths = paraModel.getRowNameWidths();

		for (int i = 0; i < length; i++) {

			float drawY = yy + FactorCalculator.getIncreasedAddingFactor(vfactorHandler, i) * gapSize;

			// Double dd = new Rectangle2D.Double(xx, drawY,
			// areaLocation.getRowNamesWeidth(), cellHeight);
			Double dd = new Double(xx, drawY, nameWidths.get(i), cellHeight);

			AffineTransform at = new AffineTransform();
			int degree = paraModel.getRowNamesRotaionAngle();
			at.rotate(Math.toRadians(degree), xx, drawY);
			Shape newRect = at.createTransformedShape(dd);
			at.rotate(-Math.toRadians(degree), xx, drawY);
			if (rectIntersect.isIntersect(newRect)) {

				ret = true;
				String string = rowNames[rowOrderMaping[i]];
				NameSelection nameSelection = new NameSelection();
				nameSelection.setName(string);
				nameSelection.setIndex(rowOrderMaping[i]);
				rowNameSelections.add(nameSelection);

				if (b) {
					return ret;
				}
			}
			yy += cellHeight;

		}
		return ret;

	}

	protected int getCurr2OriMapingIndex(int[] mapping, int target) {
		int length = mapping.length;
		for (int j = 0; j < length; j++) {
			if (mapping[j] == target) {
				return j;
			}
		}
		// it's impossible!
		return 0;
	}
}
