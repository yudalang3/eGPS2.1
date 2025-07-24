package module.maplot.maplot.model;

import java.util.List;

public class OriginalData extends DataModel {

	public OriginalData(List<ValuePoint> valuePoints) {
		this.valuePoints = valuePoints;

		maxXValue = valuePoints.stream().mapToDouble(valuePoint -> valuePoint.getXValue()).max().getAsDouble();
		minXValue = valuePoints.stream().mapToDouble(valuePoint -> valuePoint.getXValue()).min().getAsDouble();

		maxYValue = valuePoints.stream().mapToDouble(valuePoint -> valuePoint.getYValue()).max().getAsDouble();
		minYValue = valuePoints.stream().mapToDouble(valuePoint -> valuePoint.getYValue()).min().getAsDouble();

		minPValue = valuePoints.stream().filter(valuePoint -> valuePoint.getPValue().isPresent())
				.map(valuePoint -> valuePoint.getPValue()).mapToDouble(pValue -> pValue.get()).min();
		maxPValue = valuePoints.stream().filter(valuePoint -> valuePoint.getPValue().isPresent())
				.map(valuePoint -> valuePoint.getPValue()).mapToDouble(pValue -> pValue.get()).max();

	}


	
}
