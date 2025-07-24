package module.pill.core;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

class ArrowHead {

	public ArrowHead() {
	}
	
	static void arrowDrawer(Graphics2D g, Line2D.Double line) {
		
		Point startPoint = new Point((int) line.getX1(), (int) line.getY1());
		Point endPoint = new Point((int) line.getX2(), (int) line.getY2());
		
		g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
		
        int arrowSize = 10; // 调整箭头大小
        double angle = Math.atan2(endPoint.y - startPoint.y, endPoint.x - startPoint.x);
        int x1 = (int) (endPoint.x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (endPoint.y - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (endPoint.x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (endPoint.y - arrowSize * Math.sin(angle + Math.PI / 6));

        int[] xPoints = {endPoint.x, x1, x2};
        int[] yPoints = {endPoint.y, y1, y2};

        g.fillPolygon(xPoints, yPoints, 3);
	}
}