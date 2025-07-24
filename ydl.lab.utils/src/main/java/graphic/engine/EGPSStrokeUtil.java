package graphic.engine;

import java.awt.BasicStroke;

public class EGPSStrokeUtil {

	public static BasicStroke getStroke(double thickness) {
		return new BasicStroke((float) thickness);
	}

}
