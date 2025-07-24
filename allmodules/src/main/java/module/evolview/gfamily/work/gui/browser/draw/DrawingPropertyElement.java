package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * 存储文件中Element信息
 */
public class DrawingPropertyElement {

	private String name;
	private String gene;
	/**
	 * 1-based position
	 */
	private int startPosition;
	/**
	 * 1-based position
	 */
	private int endPositoin;
	private RoundRectangle2D.Double roundRect;

	/** name 所在的矩形区域 */
	private Rectangle2D.Double rectangle;

	private Color color = Color.black;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPositoin() {
		return endPositoin;
	}

	public void setEndPositoin(int endPositoin) {
		this.endPositoin = endPositoin;
	}

	public RoundRectangle2D.Double getRoundRect() {
		return roundRect;
	}

	public void setRoundRect(RoundRectangle2D.Double roundRect) {
		this.roundRect = roundRect;
	}

	public Rectangle2D.Double getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle2D.Double rectangle) {
		this.rectangle = rectangle;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public DrawingPropertyElement cloneThis() {
		DrawingPropertyElement ret = new DrawingPropertyElement();
		ret.name = this.name;
		ret.gene = this.gene;
		ret.startPosition = startPosition;
		ret.endPositoin = endPositoin;
		ret.roundRect = roundRect;
		ret.rectangle = rectangle;
		ret.color = color;
		return ret;
	}

}
