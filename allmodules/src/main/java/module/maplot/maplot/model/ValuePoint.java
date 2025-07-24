package module.maplot.maplot.model;

import java.util.List;
import java.util.Optional;

public class ValuePoint {

	private String id;

	private double xValue;

	private double yValue;

	private Optional<Double> pValue;
	
	private List<String> annotations; 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getXValue() {
		return xValue;
	}

	public void setXValue(double xValue) {
		this.xValue = xValue;
	}

	public double getYValue() {
		return yValue;
	}

	public void setYValue(double yValue) {
		this.yValue = yValue;
	}

	public Optional<Double> getPValue() {
		return pValue;
	}

	public void setPValue(Optional<Double> pValue) {
		this.pValue = pValue;
	}

	public List<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}

	
}
