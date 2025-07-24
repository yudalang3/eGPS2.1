package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.geom.Rectangle2D;

import module.evolview.gfamily.work.gui.Triangle;

public class DrawingPropertyRegionData {

	private Rectangle2D.Double rectangle;

	private Triangle startTriangle;

	private Triangle endTriangle;
	
	

	public DrawingPropertyRegionData() {
		super();
	}

	public Rectangle2D.Double getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle2D.Double rectangle) {
		this.rectangle = rectangle;
	}

	public Triangle getStartTriangle() {
		return startTriangle;
	}

	public void setStartTriangle(Triangle startTriangle) {
		this.startTriangle = startTriangle;
	}

	public Triangle getEndTriangle() {
		return endTriangle;
	}

	public void setEndTriangle(Triangle endTriangle) {
		this.endTriangle = endTriangle;
	}

	
}
