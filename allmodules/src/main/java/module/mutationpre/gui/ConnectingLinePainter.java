package module.mutationpre.gui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import module.mutationpre.model.ConnectingRegionRender;
import module.mutationpre.model.DrawConnectingLineElement;
import module.mutationpre.model.DrawPropertiesConnectingLine;

public class ConnectingLinePainter {

	public DrawPropertiesConnectingLine property;

	private CubicCurve2D.Double cubicDouble = new CubicCurve2D.Double();

	public void paint(Graphics2D g2d) {
		if (property == null) {
			return;
		}

		final double curveRatio = property.curvature;

		List<DrawConnectingLineElement> listOfMutationElement = property.listOfMutationElement;
		Collection<Integer> allPaintedPositions = property.allPaintedPositions;
		if (allPaintedPositions == null) {
			return;
		}
		Iterator<Integer> iterator = allPaintedPositions.iterator();
		int indexOfPaintedPosition = 0;
		List<ConnectingRegionRender> listOfConnectingRegionRenders = property.listOfConnectingRegionRenders;
		int limitationBound = listOfConnectingRegionRenders.size() - 1;

		BasicStroke basicStroke = new BasicStroke(property.connectingLineWidth);
		Stroke stroke = g2d.getStroke();

		for (DrawConnectingLineElement mutationElement : listOfMutationElement) {
			Integer next = iterator.next();
			if (!listOfConnectingRegionRenders.isEmpty()) {

				ConnectingRegionRender connectingRegionRender = listOfConnectingRegionRenders
						.get(indexOfPaintedPosition);
				while (connectingRegionRender.endPos <= next && indexOfPaintedPosition < limitationBound) {
					indexOfPaintedPosition++;
					connectingRegionRender = listOfConnectingRegionRenders.get(indexOfPaintedPosition);
				}

				if (next < connectingRegionRender.startPos || next > connectingRegionRender.endPos) {
					g2d.setColor(property.connectingLineColor);
					g2d.setStroke(basicStroke);
				} else {
					g2d.setColor(connectingRegionRender.color);
					g2d.setStroke(connectingRegionRender.basicStroke);
				}
			}

			Point p1 = mutationElement.locationInRegionArea;
			Point p2 = mutationElement.locationInMutationArea;

			double x1 = p1.getX();
			double y1 = p1.getY();
			final int label2linePointInterval = 4;
			double x2 = p2.getX();
			double y2 = p2.getY() - label2linePointInterval;

			double wanQv = curveRatio * (y2 - y1);

			cubicDouble.setCurve(x1, y1, x1, y1 + wanQv, x2, y2 - wanQv, x2, y2);

			g2d.draw(cubicDouble);
		}

		g2d.setStroke(stroke);
	}

	public void calculate(int width, int height) {
		// TODO Auto-generated method stub

	}

	public void setListOfMutationElement(DrawPropertiesConnectingLine listOfMutationElement) {
		this.property = listOfMutationElement;
	}

}
