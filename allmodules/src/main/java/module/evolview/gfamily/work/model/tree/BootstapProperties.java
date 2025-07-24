package module.evolview.gfamily.work.model.tree;

import java.awt.Color;

public class BootstapProperties {
	
	Color thermoColor1 = new Color(255, 0, 0);
	Color thermoColor2 = new Color(0, 128, 0);
	
	Color intervalColors1 = new Color(255, 0, 0);
	Color intervalColors2 = new Color(0, 128, 0);
	Color intervalColors3 = new Color(255, 255, 0);
	
	int intervalArray1 = 50;
	int intervalArray2 = 100;
	
	String flag;
	
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Color getThermoColor1() {
		return thermoColor1;
	}
	public void setThermoColor1(Color thermoColor1) {
		this.thermoColor1 = thermoColor1;
	}
	public Color getThermoColor2() {
		return thermoColor2;
	}
	public void setThermoColor2(Color thermoColor2) {
		this.thermoColor2 = thermoColor2;
	}
	public Color getIntervalColors1() {
		return intervalColors1;
	}
	public void setIntervalColors1(Color intervalColors1) {
		this.intervalColors1 = intervalColors1;
	}
	public Color getIntervalColors2() {
		return intervalColors2;
	}
	public void setIntervalColors2(Color intervalColors2) {
		this.intervalColors2 = intervalColors2;
	}
	public Color getIntervalColors3() {
		return intervalColors3;
	}
	public void setIntervalColors3(Color intervalColors3) {
		this.intervalColors3 = intervalColors3;
	}
	public int getIntervalArray1() {
		return intervalArray1;
	}
	public void setIntervalArray1(int intervalArray1) {
		this.intervalArray1 = intervalArray1;
	}
	public int getIntervalArray2() {
		return intervalArray2;
	}
	public void setIntervalArray2(int intervalArray2) {
		this.intervalArray2 = intervalArray2;
	}
	
	
}
