package module.vennplot.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;
import java.util.List;

import module.vennplot.model.DataModel;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SingleRegionDataModel;

public class BodyRegionPainter {

	public void paint(Graphics2D g2d, PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel) {
		double x = paintingLocations.getBodyTopleftX();
		double y = paintingLocations.getBodyTopleftY();
		double w = paintingLocations.getBodyWidth();
		double h = paintingLocations.getBodyHeight();

		g2d.setColor(Color.black);

		int numOfSets = dataModel.getNumOfSets();

		double bodyRegionElementHeight = paintingLocations.getBodyRegionElementHeight();
		double intersectRegionElementWidth = paintingLocations.getIntersectRegionElementWidth();

		//
		for (int i = 0; i < numOfSets; i++) {
			if (i % 2 != 0) {
				g2d.setColor(new Color(245, 245, 245));
				double y2 = y + (i + 0.5) * bodyRegionElementHeight - parameterModel.getRoundRadius();
				g2d.fill(new Double(x, y2, w, bodyRegionElementHeight));

			}
		}
		//
		IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
		SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
		List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();

		double radicus = parameterModel.getRoundRadius();
		int linkingBarThick = parameterModel.getLinkingBarThick();
		double roundDiameter = radicus * 2;

		LinkedList<java.lang.Double> twoDuble = new LinkedList<>();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int numOfIntersectionItems = eles.size();

		for (int i = 0; i < numOfIntersectionItems; i++) {
			IntersectionRegionElement intersectionRegionElement = eles.get(i);
			byte[] booleans = singleRegionDataModel.getBooleansAfterSort(intersectionRegionElement.getBooleans());

			twoDuble.clear();
			double xx = x + i * intersectRegionElementWidth;
			Color filledColor = intersectionRegionElement.getFilledColor();
			g2d.setColor(filledColor);
			for (int j = 0; j < numOfSets; j++) {

				double yy = y + (j + 1) * bodyRegionElementHeight;

				Ellipse2D.Double double1 = new Ellipse2D.Double(xx, yy - roundDiameter, roundDiameter,
						roundDiameter);
				if (booleans[j] == 1) {
					twoDuble.add(yy - radicus);
					g2d.setColor(filledColor);
					g2d.fill(double1);
				} else if (booleans[j] == 0) {
					g2d.setColor(new Color(216, 216, 216));
					g2d.fill(double1);
				} else {
					g2d.setColor(Color.black);
					g2d.draw(double1);
				}

			}

			// draw bar
			if (twoDuble.size() > 1) {
				double xxx = xx + radicus - 0.5 * linkingBarThick;
				double yyy = twoDuble.getFirst();
				double hhh = twoDuble.getLast() - yyy;
				Double double1 = new Double(xxx, yyy, linkingBarThick, hhh);
				g2d.setColor(filledColor);
				g2d.fill(double1);
			}

		}

	}

}
