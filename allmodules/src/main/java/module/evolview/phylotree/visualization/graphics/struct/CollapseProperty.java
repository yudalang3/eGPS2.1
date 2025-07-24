package module.evolview.phylotree.visualization.graphics.struct;

import java.awt.Color;

public class CollapseProperty {
	
	int r,g,b;
	
	int triangleSize = 20;

	
	public CollapseProperty() {
		Color green = Color.black;
		r = green.getRed();
		g = green.getGreen();
		b = green.getBlue();
	}
	
	public void setColor(int r,int g, int b) {
		this.b = b;
		this.r = r;
		this.g = g;
	}
	
	public Color getColor() {
		return new Color(r, g, b);
	}

	public int getTriangleSize() {
		return triangleSize;
	}

	public void setTriangleSize(int triangleSize) {
		this.triangleSize = triangleSize;
	}
	
	
}
