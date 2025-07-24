package module.evolview.gfamily.work.model.tree;

import java.awt.Color;

public class LineProperty {

	int r,g,b;
	float lineThinkness = 2.6f;
	
	public LineProperty() {
		Color green = Color.decode("#e84a5f");
		r = green.getRed();
		g = green.getGreen();
		b = green.getBlue();
	}
	public void setLineThinkness(float lineThinkness) {
		this.lineThinkness = lineThinkness;
	}
	
	public float getLineThinkness() {
		return lineThinkness;
	}
	
	public void setColor(int r,int g, int b) {
		this.b = b;
		this.r = r;
		this.g = g;
	}
	
	public Color getColor() {
		return new Color(r, g, b);
	}
}
