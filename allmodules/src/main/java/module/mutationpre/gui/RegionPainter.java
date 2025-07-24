package module.mutationpre.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D.Double;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import module.mutationpre.model.DrawConnectingLineElement;
import module.mutationpre.model.DrawPropertiesRegion;
import module.mutationpre.model.DrawRegionElement;

public class RegionPainter {
	DrawPropertiesRegion property;

	private Double rouDouble = new Double();

	public void paint(Graphics2D g2d) {

		if (property == null) {
			return;
		}

		if (property.regionShowMutationVerticalLine) {

			g2d.setColor(Color.black);
			List<DrawConnectingLineElement> listOfMutationElement = property.listOfConnectingLineElements;
			int regionHeight = property.height;
			for (DrawConnectingLineElement mutationElement : listOfMutationElement) {
				Point p1 = mutationElement.locationInRegionArea;
				g2d.drawLine(p1.x, p1.y - regionHeight, p1.x, p1.y);
			}
		}
		
		
		g2d.setFont(property.regionLabelFont);

		FontMetrics fontMetrics = g2d.getFontMetrics();
		
		List<DrawRegionElement> regions = property.regions;

		int height = fontMetrics.getHeight();

		boolean hasRegionBorder = property.hasRegionBorder;

		BasicStroke basicStroke = new BasicStroke(property.regionBorderLineWidth);
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(basicStroke);

		for (DrawRegionElement regionElement : regions) {

			Pair<Point, Integer> pair = regionElement.location;

			Color color = regionElement.color;
			g2d.setColor(color);
			Point left = pair.getLeft();
			rouDouble.setRoundRect(left.x, left.y + regionElement.downMovedDistance, pair.getRight(), property.height, property.roundRectangularCurvature,
					property.roundRectangularCurvature);
			g2d.fill(rouDouble);
			if (hasRegionBorder) {
				g2d.setColor(property.regionBorderColor);
				g2d.draw(rouDouble);
			}

			g2d.setColor(property.regionLabelColor);
			g2d.drawString(regionElement.label, left.x + 3, left.y + (height + property.height) / 2);
		}

		g2d.setStroke(oldStroke);

	}

	public void calculate(int width, int height) {
		// TODO Auto-generated method stub

	}

	public void setProperty(DrawPropertiesRegion property) {
		this.property = property;
	}

}
