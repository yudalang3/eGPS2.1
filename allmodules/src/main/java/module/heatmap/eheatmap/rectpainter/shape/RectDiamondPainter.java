package module.heatmap.eheatmap.rectpainter.shape;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import module.heatmap.eheatmap.rectpainter.RectShapePainter;

public class RectDiamondPainter extends RectShapePainter {
	private final GeneralPath path = new GeneralPath();
	
	public RectDiamondPainter(double w, double h) {
		super(w, h);
	}
	
	/**
	 * 
	 *             xx2,yy2
	 *  xx1,yy1                 xx3,yy3
	 *             xx4,yy4
	 */
	@Override
	public Shape getShape(double x, double y, double value, double normedVa) {
		double xx1 = x;
		double yy1 = y + 0.5 * h;
		
		
		double xx2 = x + 0.5 * w;
		double yy2 = y;
		
		double xx3 = x + w;
		double yy3 = yy1;
        
		double xx4 = xx2;
		double yy4 = y + h;
        
        path.reset();
       
        path.moveTo(xx1, yy1);
        path.lineTo(xx2, yy2);
        path.lineTo(xx3, yy3);
        path.lineTo(xx4, yy4);
        path.closePath();
        
        return path;
	}
	

}
