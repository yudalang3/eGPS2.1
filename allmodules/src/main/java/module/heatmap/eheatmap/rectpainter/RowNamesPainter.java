package module.heatmap.eheatmap.rectpainter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D.Double;
import java.util.HashSet;
import java.util.List;

import javax.swing.CellRendererPane;

import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.selection.NameSelection;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;

public class RowNamesPainter {

	public void painting(Graphics2D g2d, PaintingLocationParas areaLocation, DataModel dataModel,
			ParameterModel paraModel, CellRendererPane cellRendererPane) {

		int[] rowOrderMaping = dataModel.getRowOrderMaping();

		double x = areaLocation.getRowNamesLeftTopX();
		double y = areaLocation.getRowNamesLeftTopY();

		Font oriFont = g2d.getFont();
		g2d.setFont(paraModel.getRowNameFont());

		FontMetrics metrics = g2d.getFontMetrics();
		int hgt = metrics.getHeight();
		double startY = y + 0.5 * areaLocation.getCellHeight();
		double startX = x + 6;

		List<NameSelection> nameSelections = paraModel.getRowNameSelections();
		HashSet<String> hashSet = new HashSet<String>();
		for (NameSelection nn : nameSelections) {
			hashSet.add(nn.getName());
		}

		String[] rowNames = dataModel.getRowNames();
		int gapSize = paraModel.getGapSize();
		int[] vGapLocations = paraModel.getvGapLocations();
		FactorHandler vfactorHandler = new FactorHandler(0, vGapLocations, vGapLocations.length);

		int length = rowNames.length;

		List<Integer> nameWidths = paraModel.getRowNameWidths();

		nameWidths.clear();

		for (int i = 0; i < length; i++) {
			g2d.setColor(paraModel.getRowNameColor());
			String string = rowNames[rowOrderMaping[i]];
			float xx = (float) startX;
			float yy = (float) (startY + FactorCalculator.getIncreasedAddingFactor(vfactorHandler, i) * gapSize);
			yy += 0.4 * hgt;
			int stringWidth = metrics.stringWidth(string) + 6;

			nameWidths.add(stringWidth);

			g2d.rotate(Math.toRadians(paraModel.getRowNamesRotaionAngle()), xx, yy);

			g2d.drawString(string, xx + 3, yy);

			if (paraModel.isNameOneMouseHasClick() || paraModel.isHasMouseDragEvent()) {
				if (hashSet.contains(string)) {
					Stroke oldStroke = g2d.getStroke();
					g2d.setStroke(paraModel.getDashedStroke());
					g2d.setColor(new Color(112, 142, 173));
					Double dd = new Double(xx, yy - hgt + 3, stringWidth, hgt + 3);
					g2d.draw(dd);
					g2d.setStroke(oldStroke);
				}
			}

			g2d.rotate(Math.toRadians(-paraModel.getRowNamesRotaionAngle()), xx, yy);
			startY += areaLocation.getCellHeight();

		}

		g2d.setFont(oriFont);

		double w = areaLocation.getRowNamesWeidth();
		double h = areaLocation.getRowNamesHeight();

//		Double rect = new Rectangle2D.Double(x, y, w, h);
//		g2d.setColor(Color.lightGray);
//		g2d.draw(rect);
	}

}
