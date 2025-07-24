package module.evolview.gfamily.work.model.tree;

import java.awt.Color;
import java.awt.Font;

public class LeafLabelProperty {

	Color color = Color.black;
	Font font = new Font("Arial", Font.PLAIN, 12);

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

}
