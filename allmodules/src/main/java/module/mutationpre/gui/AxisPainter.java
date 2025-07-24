package module.mutationpre.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.List;

import module.mutationpre.model.DrawPropertiesAxis;

public class AxisPainter {

	DrawPropertiesAxis property;

	public void paint(Graphics2D g2d) {

		if (property == null) {
			return;
		}
		
		g2d.setFont(property.titleFont);g2d.setColor(property.titleColor);
		
		FontMetrics fontMetrics = g2d.getFontMetrics();
		Point point = property.point;
		
		// ============= for debug
//		g2d.setColor(Color.red);
//		g2d.fillRect(point.x + property.drawWidth / 2, point.y - 50, 4, 4);
//		g2d.fillRect(point.x , point.y , property.drawWidth, 4);
		// ============= for debug
		
		float xx = point.x + (float) 0.5 * ( property.drawWidth - fontMetrics.stringWidth(property.titleString) );
		float yy = point.y - 50;
		g2d.drawString(property.titleString, xx, yy);
		
		g2d.setColor(Color.black);
		
		List<Point> tickMarkList = property.tickMarkList;
		List<String> labels = property.labels;
		int size = labels.size();

		g2d.setFont(property.labelFont);
		fontMetrics = g2d.getFontMetrics();
		Stroke stroke = g2d.getStroke();
		BasicStroke basicStroke = new BasicStroke(property.axisLineWidth);
		g2d.setStroke(basicStroke);
		
		//坐标轴线
		int rightMostX = point.x + property.drawWidth;
		g2d.drawLine(point.x, point.y, rightMostX, point.y);
		
		// 小凸起
		for (int i = 0; i < size; i++) {
			Point point2 = tickMarkList.get(i);
			String string = labels.get(i);
			int stringWidth = fontMetrics.stringWidth(string);
			g2d.drawString(string, point2.x - (float) 0.5 * stringWidth, point2.y - 10);
			
			g2d.drawLine(point2.x, point2.y, point2.x, point2.y + property.tickMarkVerticalHeight);
		}
		
		g2d.setStroke(stroke);
		
//		g2d.setColor(new Color(200,150,200,20));
//		g2d.fillRect(point.x, point.y + 48, property.drawWidth, 25);

	}

	public void calculate(int width, int height) {
		// TODO Auto-generated method stub

	}
	
	public void setProperty(DrawPropertiesAxis property) {
		this.property = property;
	}

}
