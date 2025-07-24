package module.heatmap.eheatmap.cirpainter.shape;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import module.heatmap.eheatmap.cirpainter.CirShapePainter;

public class CirDiamondPainter extends CirShapePainter {
	private final GeneralPath path = new GeneralPath();
	
	public CirDiamondPainter(double w, double h) {
		super(w, h);
	}
	/**
	 * 
	 *             xx2,yy2
	 *  xx1,yy1                 xx3,yy3
	 *             xx4,yy4
	 */
	@Override
	public Shape getShape(int x0, int y0, double value, double normedVa) {

		double avgR = 0.5 * (r1 + r2);
		double avgDegree = startDeg + 0.5 * extendDeg;
		
        double cosMiddle = Math.cos(avgDegree * oneDegRadians);
        double sinMiddle = Math.sin(avgDegree * oneDegRadians);
        
		double xx1 = x0 + r1 *cosMiddle;
		double yy1 = y0 - r1 * sinMiddle;
		
		
		double xx2 = x0 + avgR *Math.cos(startDeg * oneDegRadians);
		double yy2 = y0 - avgR * Math.sin(startDeg * oneDegRadians);
		
		double xx3 = x0 + r2 *cosMiddle;
		double yy3 = y0 - r2 *sinMiddle;
        
		double xx4 = x0 + avgR *Math.cos(endDeg * oneDegRadians);
		double yy4 = y0 - avgR * Math.sin(endDeg * oneDegRadians);
        
        path.reset();
       
        path.moveTo(xx1, yy1);
        path.lineTo(xx2, yy2);
        path.lineTo(xx3, yy3);
        path.lineTo(xx4, yy4);
        path.closePath();
        
        return path;
	}
	

}
