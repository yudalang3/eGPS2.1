package module.vennplot.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import module.vennplot.model.DataModel;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;

public class LegendPainter {

	public void paint(Graphics2D g2d, PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel) {

		double x = paintingLocations.getIntersectRegionTopleftX();
		double y = paintingLocations.getIntersectRegionTopleftY();
		double w = paintingLocations.getIntersectRegionWidth();
		double h = paintingLocations.getIntersectRegionHeight();

		final int legendWidth = 200;
		final float roundRadicus = 8;
		final float roundDiameter = 2f * roundRadicus;

		g2d.setColor(Color.black);
		g2d.setFont(parameterModel.getNameFont());
		// first
		float xx = (float) (x + w - legendWidth);
		float yy = (float) y;

		// if painting border!
		// g2d.draw(new Rectangle2D.Float(xx - 2,yy-2,legendWidth,2 * roundDiameter +
		// 40));

		float xxFont = xx + roundDiameter + 6f;
		float yyFontAdjust = 4f;
		g2d.fill(new Ellipse2D.Double(xx, yy, roundDiameter, roundDiameter));
		g2d.drawString("Include", xxFont, yy + roundDiameter - yyFontAdjust);

		// second
		yy += roundDiameter + 10;
		g2d.setColor(new Color(216, 216, 216));
		g2d.fill(new Ellipse2D.Double(xx, yy, roundDiameter, roundDiameter));
		
		g2d.setColor(Color.black);
		g2d.drawString("Exclude", xxFont, yy + roundDiameter - yyFontAdjust);

		// third

		if (parameterModel.isContainsNotTakeintoCon()) {

			yy += roundDiameter + 10;

			g2d.draw(new Ellipse2D.Double(xx, yy, roundDiameter, roundDiameter));

			g2d.drawString("Not take into consideration", xxFont, yy + roundDiameter - yyFontAdjust);

		}

	}

}
