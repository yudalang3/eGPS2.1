package module.heatmap.eheatmap.tree;

import java.awt.geom.Arc2D;

public class TangentialObjct {

	double radicusForCalculate = 20;
	double radicusForPaint;
	double startDeg;
	double extendDeg;
	
	public double getRadicusForPaint() {
		return radicusForPaint;
	}
	public void setRadicusForPaint(double radicusForPaint) {
		this.radicusForPaint = radicusForPaint;
	}
	public double getRadicusForCal() {
		return radicusForCalculate;
	}
	public void setRadicusForCal(double radicus) {
		this.radicusForCalculate = radicus;
	}
	public double getStartDeg() {
		return startDeg;
	}
	public void setStartDeg(double startDeg) {
		this.startDeg = startDeg;
	}
	public double getExtendDeg() {
		return extendDeg;
	}
	public void setEetendDeg(double endDeg) {
		this.extendDeg = endDeg;
	}
	
	
	public Arc2D.Double getPaintArc(int x0,int y0) {
		double len = radicusForPaint *2;
		return new Arc2D.Double(x0-radicusForPaint,y0-radicusForPaint,len,len,startDeg,extendDeg,Arc2D.OPEN);
	}
}
