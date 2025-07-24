package module.vennplot.painter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import module.vennplot.model.DataModel;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SingleRegionDataModel;
import module.vennplot.model.SingleRegionElement;

public class NamesPainter {

	public void paint(Graphics2D g2d, PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel) {
		double x = paintingLocations.getNameRegionTopleftX();
		double y = paintingLocations.getNameRegionTopleftY();
		double w = paintingLocations.getNameRegionWidth();
		double h = paintingLocations.getNameRegionHeight();

		List<NameSelection> nameSelections = parameterModel.getNameSelections();
		double iterateHeight = paintingLocations.getBodyRegionElementHeight();

		SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
		List<SingleRegionElement> eles = singleRegionDataModel.getEles();

		int numOfSets = dataModel.getNumOfSets();

		List<String> nameOfInputSetItemForSearch = parameterModel.getNameOfInputSetItemForSearch();

		Font oldFont = g2d.getFont();
		g2d.setFont(parameterModel.getNameFont());

		FontMetrics fontMetrics = g2d.getFontMetrics();
		int ascent = fontMetrics.getAscent();
		int fht = fontMetrics.getHeight();
		for (int i = 0; i < numOfSets; i++) {
			SingleRegionElement singleRegionElement = eles.get(i);
			double xx = x + 20;

			double yy = i * iterateHeight + (iterateHeight - 5) + y;
			g2d.setColor(Color.black);
			String setName = singleRegionElement.getSetName();
			g2d.drawString(setName, (float) xx, (float) yy);

			if (nameSelections.contains(new NameSelection(i))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Double(xx, yy - fht, fontMetrics.stringWidth(setName), fht));
				g2d.setStroke(oldStroke);
			}

			if (nameOfInputSetItemForSearch != null && nameOfInputSetItemForSearch.contains(setName)) {
				double wholeWidth = paintingLocations.getBodyWidth() + paintingLocations.getNameRegionWidth()
						+ paintingLocations.getSingleRegionWidth();

				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				int leftBarWidth = parameterModel.getLeftBarWidth();
				double gap = iterateHeight - leftBarWidth;
				Double double1 = new Double(xx - paintingLocations.getSingleRegionWidth(),
						yy - iterateHeight + gap, wholeWidth, leftBarWidth);
				g2d.draw(double1);
				g2d.setStroke(oldStroke);
			}

		}

		g2d.setFont(oldFont);
	}

}
