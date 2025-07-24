package module.heatmap.eheatmap.rectpainter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D.Double;
import java.util.HashSet;
import java.util.List;

import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.selection.NameSelection;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;

public class ColNamesPainter {

	public void painting(Graphics2D g2d, PaintingLocationParas areaLocation, DataModel dataModel,
			ParameterModel paraModel) {

		int[] colOrderMaping = dataModel.getColOrderMaping();

		double x = areaLocation.getColNamesLeftTopX();
		double y = areaLocation.getColNamesLeftTopY();

		int colNamesRotaionAngle = paraModel.getColNamesRotaionAngle();
		// Note the angle will only be -90<= angle <=90+

		Font oriFont = g2d.getFont();
		g2d.setFont(paraModel.getColNameFont());

		FontMetrics metrics = g2d.getFontMetrics();
		int hgt = metrics.getHeight();

		float[] colNameAdjuster = LocationAdjuster.colNameAdjuster(hgt, colNamesRotaionAngle, x, y,
				areaLocation.getCellWidth(), areaLocation);

		double startX = colNameAdjuster[0];
		double startY = colNameAdjuster[1];

		List<NameSelection> nameSelections = paraModel.getColNameSelections();
		HashSet<String> hashSet = new HashSet<String>();
		for (NameSelection nn : nameSelections) {
			hashSet.add(nn.getName());
		}
		String[] colNames = dataModel.getColNames();

		int gapSize = paraModel.getGapSize();
		int[] hGapLocations = paraModel.gethGapLocations();
		FactorHandler hfactorHandler = new FactorHandler(0, hGapLocations, hGapLocations.length);

		int length = colNames.length;

		List<Integer> nameWidths = paraModel.getColNameWidths();
		nameWidths.clear();

		for (int i = 0; i < length; i++) {
			g2d.setColor(paraModel.getColNameColor());
			String string = colNames[colOrderMaping[i]];
			int width = metrics.stringWidth(string);
			nameWidths.add(width);

			float xx = (float) (startX + FactorCalculator.getIncreasedAddingFactor(hfactorHandler, i) * gapSize);
			float yy = (float) startY;
			g2d.rotate(Math.toRadians(colNamesRotaionAngle), xx, yy);
			g2d.drawString(string, xx, yy);

			if (paraModel.isNameOneMouseHasClick() || paraModel.isHasMouseDragEvent()) {
				if (hashSet.contains(string)) {
					Stroke oldStroke = g2d.getStroke();
					g2d.setStroke(paraModel.getDashedStroke());
					g2d.setColor(new Color(112, 142, 173));
					Double dd = new Double(xx, yy - hgt + 5, width, hgt);
					g2d.draw(dd);
					g2d.setStroke(oldStroke);
				}

			}

			g2d.rotate(Math.toRadians(-colNamesRotaionAngle), xx, yy);
			startX += areaLocation.getCellWidth();
		}

		g2d.setFont(oriFont);
//		double w = areaLocation.getColNamesWeidth();
//		double h = areaLocation.getColNamesHeight();
//		Double rect = new Rectangle2D.Double(x,y,w,h);
//		g2d.setColor(Color.lightGray);
//		g2d.draw(rect);
	}

}
