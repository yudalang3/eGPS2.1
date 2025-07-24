package module.heatmap.eheatmap.rectpainter;

import module.heatmap.eheatmap.model.PaintingLocationParas;

public class LocationAdjuster {
	
	public static final float[] colNameAdjuster(int hgt, int colNamesRotaionAngle, double x, double y,double wid, PaintingLocationParas areaLocation) {
		float startY = (float) (y + hgt - (colNamesRotaionAngle - 45) * 0.3 +8);

		float xLocationFactor = 0.5f;
		if (colNamesRotaionAngle > 60) {
			xLocationFactor = 0;
		} else if (colNamesRotaionAngle < -70) {
			xLocationFactor = -0.1f;
		} else {
			xLocationFactor = 0;
		}
		float startX = (float) (x + 0.5 * wid - 0.5 * hgt
				+ (colNamesRotaionAngle - 60) * xLocationFactor);
		

		return new float[] { startX, startY };
	}
	public static final float[] rowNameAdjuster() {
		
		return new float[] {1f,2f};
	}

}
