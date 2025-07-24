package module.heatmap.eheatmap.rectpainter.shape;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import module.heatmap.eheatmap.rectpainter.RectShapePainter;

public class RectDoubleTrianglePainter extends RectShapePainter {
	private final GeneralPath path = new GeneralPath();
	
	public RectDoubleTrianglePainter(double w, double h) {
		super(w, h);
	}
	@Override
	public Shape getShape(double x, double y, double value, double normedVa) {
		
			double xx11 = x;
			double yy11 = y;
			double xx12 = x;
			double yy12 = y+h;
	        
	        
			double xx21 = x + w;
			double yy21 = y;
	        double xx22 = xx21;
	        double yy22 = yy12;
	        
	        path.reset();
	       
	        path.moveTo(xx11, yy11);
	        path.lineTo(xx12, yy12);
	        path.lineTo(xx21, yy21);
	        path.lineTo(xx22, yy22);
	        path.closePath();
	        
	        return path;
	}
	

}
