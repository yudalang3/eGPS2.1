package module.evolview.gfamily.work.model.tree;

import java.awt.Font;

import egps2.UnifiedAccessPoint;

public class ScaleBarProperty {
	
	double xLocation = 50;
	double yLocation = 80;
	
	int width = 80;
	int height = 10;
	
	boolean ifDrawScaleBar = false;
	Font font = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	public double getxLocation() {
		return xLocation;
	}

	public void setxLocation(double xLocation) {
		this.xLocation = xLocation;
	}

	public double getyLocation() {
		return yLocation;
	}

	public void setyLocation(double yLocation) {
		this.yLocation = yLocation;
	}

	public boolean isIfDrawScaleBar() {
		return ifDrawScaleBar;
	}

	public void setIfDrawScaleBar(boolean ifDrawScaleBar) {
		this.ifDrawScaleBar = ifDrawScaleBar;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	
}
