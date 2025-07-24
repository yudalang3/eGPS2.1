package module.heatmap.eheatmap.cirpainter.shape;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import module.heatmap.eheatmap.cirpainter.CirShapePainter;

public class CirDoubleTrianglePainter extends CirShapePainter {
	private final GeneralPath path = new GeneralPath();
	
	public CirDoubleTrianglePainter(double w, double h) {
		super(w, h);
	}
	@Override
	public Shape getShape(int x0, int y0, double value, double normedVa) {

        double cosS = Math.cos(startDeg * oneDegRadians);
        double sinS = Math.sin(startDeg * oneDegRadians);
		double xx11 = x0 + r1 *cosS;
		double yy11 = y0 - r1 * sinS;
		double xx12 = x0 + r2 *cosS;
		double yy12 = y0 - r2 * sinS;
        
        
        double cosE = Math.cos(endDeg * oneDegRadians);
        double sinE = Math.sin(endDeg * oneDegRadians);
		double xx21 = x0 + r1 *cosE;
		double yy21 = y0 - r1 * sinE;
        double xx22 = x0 + r2 *cosE;
        double yy22 = y0 - r2 * sinE;
        
        path.reset();
       
        path.moveTo(xx11, yy11);
        path.lineTo(xx12, yy12);
        path.lineTo(xx21, yy21);
        path.lineTo(xx22, yy22);
        path.closePath();
        
        return path;
	}
	

}
