package module.maplot.maplot.model;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public abstract class  DataModel {
	
	protected List<ValuePoint> valuePoints;
	protected double maxXValue;
	protected double minXValue;
	protected double maxYValue;
	protected double minYValue;
	protected OptionalDouble maxPValue;
	protected OptionalDouble minPValue;
	
	protected List<String> headerFields = new ArrayList<>();

	public List<ValuePoint> getValuePoints() {
		return valuePoints;
	}

	public double getMaxXValue() {
		return maxXValue;
	}

	public double getMinXValue() {
		return minXValue;
	}

	public double getMaxYValue() {
		return maxYValue;
	}

	public double getMinYValue() {
		return minYValue;
	}

	public OptionalDouble getMaxPValue() {
		return maxPValue;
	}

	public OptionalDouble getMinPValue() {
		return minPValue;
	}
	
	
	public List<String> getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(List<String> headerFields) {
		this.headerFields = headerFields;
	}
}
