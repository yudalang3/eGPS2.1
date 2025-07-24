package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.util.List;

/**
 * 用于绘制显示PolyLinePlot的线段
 */
public class DrawingPropertyPolyLine {

    private List<Line2D.Double> rect;
    private String name;
    private Color color;
    //    private Line2D.Float line;
    private float startLineLegendX;
    private float startLineLegendY;
	public List<Line2D.Double> getRect() {
		return rect;
	}
	public void setRect(List<Line2D.Double> rect) {
		this.rect = rect;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public float getStartLineLegendX() {
		return startLineLegendX;
	}
	public void setStartLineLegendX(float startLineLegendX) {
		this.startLineLegendX = startLineLegendX;
	}
	public float getStartLineLegendY() {
		return startLineLegendY;
	}
	public void setStartLineLegendY(float startLineLegendY) {
		this.startLineLegendY = startLineLegendY;
	}

    
}
