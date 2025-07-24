package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * 用于绘制显示PaintBar的突变率信息
 */

public class DrawingPropertyLine {

    public Rectangle2D.Double getRect() {
		return rect;
	}

	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	private Rectangle2D.Double rect;

    private int pos;

    private Color color;

}
