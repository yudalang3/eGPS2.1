package module.heatmap.eheatmap.enums;

import java.awt.Color;

public enum ColorEnum {

	GRBKRD(() -> new Color[] { Color.green, Color.black, Color.red }),
	BEYWBK(() -> new Color[] { Color.blue, Color.yellow, Color.black }),
	BEWERD(() -> new Color[] { Color.blue, Color.white, Color.red }),
	WERD(() -> new Color[] { Color.white, Color.red }),
	PHEATMAP(() -> new Color[] { new Color(69, 117, 180), new Color(145, 191, 219), new Color(224, 243, 248),
			new Color(255, 255, 191), new Color(254, 224, 144), new Color(252, 141, 89), new Color(215, 48, 39) });

	private Color[] colors;

	private ColorEnum(Colors colors) {
		this.colors = colors.getColors();
	}

	public Color[] getColors() {
		return colors;
	}

	public float[] getDists() {
		int numOfColors = colors.length;
		float[] dist = new float[numOfColors];
		double interval = 1.0 / numOfColors;
		for (int i = 0; i < numOfColors; i++) {
			dist[i] = (float) (interval * i);
		}
		return dist;
	}

}

interface Colors {

	Color[] getColors();

}
