package module.heatmap.eheatmap.model;

import java.awt.geom.Rectangle2D;

public class MouseDragClickObj {
	boolean hasClicked = false;
	Rectangle2D selectionRectangle2d;
	
	public boolean isHasClicked() {
		return hasClicked;
	}
	public void setHasClicked(boolean hasClicked) {
		this.hasClicked = hasClicked;
	}
	public Rectangle2D getRect() {
		return selectionRectangle2d;
	}
	public void setRect(Rectangle2D point2) {
		this.selectionRectangle2d = point2;
	}
	
}
