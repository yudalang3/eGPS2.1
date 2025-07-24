package module.heatmap.eheatmap.cirpainter;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import module.heatmap.eheatmap.model.ParameterModel;


public class SectorDrawer {
	
	private double r1 = 20;
	private double r2 = 20 + 70;
	
	private double startDeg = 0;
	private double extendDeg = 90;
	private double endDeg;
	
	private final GeneralPath path = new GeneralPath();
	private final double oneDegRadians = Math.PI / 180;
	
	public SectorDrawer() {
		
	}
	/**
	 * @param r1 小圆半径
	 * @param r2 大圆半径
	 * @param startDeg 是角度
	 * @param extendDeg 是角度
	 */
	public SectorDrawer(double r1, double r2, double startDeg, double extendDeg) {
		this.r1 = r1;
		this.r2 = r2;
		this.startDeg = startDeg;
		this.extendDeg = extendDeg;
		endDeg = startDeg + extendDeg;
	}
	public void setProperty(double r1, double r2, double startDeg, double extendDeg) {
		this.r1 = r1;
		this.r2 = r2;
		this.startDeg = startDeg;
		this.extendDeg = extendDeg;
		endDeg = startDeg + extendDeg;
	}
	/**
	 * 画笔和圆心所在位置
	 * @param g2d
	 * @param x0
	 * @param y0
	 * @param d 
	 * @param paraModel 
	 * @return 返回一个绘制的点
	 */
	public Point2D.Double drawSector(Graphics2D g2d, int x0,int y0, ParameterModel paraModel) {
		
		Rectangle2D.Double innerRect = new Rectangle2D.Double(x0-r1, y0-r1, r1+r1, r1+r1);
		Rectangle2D.Double outterRect = new Rectangle2D.Double(x0-r2, y0-r2, r2+r2, r2+r2);
        
        Point2D.Double p1 = new Point2D.Double(x0 + r1 *Math.cos(startDeg * oneDegRadians), y0 - r1 * Math.sin(startDeg * oneDegRadians));
        path.reset();
       
        path.moveTo(p1.getX(), p1.getY());
        path.append(new Arc2D.Double(innerRect, startDeg, extendDeg, Arc2D.OPEN),false);
        //path.lineTo(p2.getX(), p2.getY());
        //path.lineTo(p3.getX(), p3.getY());
        path.append(new Arc2D.Double(outterRect, endDeg, -extendDeg, Arc2D.OPEN),true);
        //path.lineTo(p4.getX(), p4.getY());
        path.closePath();
        
        g2d.fill(path);
        
        if(paraModel.isIfShowBorder()) {
        	g2d.setColor(paraModel.getBorderColor());
        	g2d.draw(path);
        }
//        if(paraModel.isIfShowAllValues()) {
//        	g2d.drawString(""+displayValue, (float) p1.getX(), (float) p1.getY());
//        }
        
        //g2d.drawString("p1", (float) p1.getX(), (float) p1.getY());
		
        return p1;
	}
	/**
	 * Draw a piece of text on a circular curve, one character at a time. This is
	 * harder than it looks...
	 * 
	 * This method accepts many arguments: g - a Graphics2D ready to be used to
	 * draw, 
	 * st - the string to draw,
	 *  center - the center point of the circle(Point), 
	 * r - the radius of the circle, 
	 * a1 - the beginning angle on the circle to start, in radians, 
	 * af - the angle advance factor (usually 1.0)
	 */
	void drawCircleText(Graphics2D g2d, String st, int x0,int y0, double r, double beginR) {
		
	}
	
	/**
	 * Get the width of a given character under the specified FontMetrics,
	 * interpreting all spaces as en-spaces.
	 */
	int getWidth(char c, FontMetrics fm) {
		if (c == ' ' || Character.isSpaceChar(c)) {
			return fm.charWidth('n');
		} else {
			return fm.charWidth(c);
		}
	}

}
