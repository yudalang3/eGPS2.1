package module.evolview.gfamily.work.gui;

import java.awt.Cursor;

import module.evolview.gfamily.work.gui.tree.RectObj;
import module.evolview.model.tree.ScaleBarProperty;

public class ScaleBarRectObject extends RectObj{
	
	final ScaleBarProperty scaleBarProperty;
	
	public ScaleBarRectObject(ScaleBarProperty scaleBarProperty) {
		this.x = scaleBarProperty.getxLocation();
		this.y = scaleBarProperty.getyLocation();
		this.w = scaleBarProperty.getWidth();
		this.h = scaleBarProperty.getHeight();
		
		this.scaleBarProperty = scaleBarProperty;
	}

	@Override
	public int getCursorType() {
		return Cursor.CROSSHAIR_CURSOR;
	}

	@Override
	public void adjustPaintings(double d, double e) {
		double newX = d + dx;
		double newY = e + dy;
		
		this.x = newX;
		this.y = newY;
		
		scaleBarProperty.setxLocation(newX);
		scaleBarProperty.setyLocation(newY);
	}

}
