package module.maplot.maplot.model;

import java.util.ArrayList;

import module.maplot.maplot.gui.LeftToolJPanel;
import module.maplot.maplot.gui.MA_PlotDrawProperties;


public class TransformedMAData extends DataModel {


	public TransformedMAData(MA_PlotDrawProperties drawProperties, DataModel dataModel) {

		this.valuePoints = new ArrayList<ValuePoint>();

		createDataModel(drawProperties, dataModel);

		maxXValue = this.valuePoints.stream().mapToDouble(valuePoint -> valuePoint.getXValue()).max().getAsDouble();
		minXValue = this.valuePoints.stream().mapToDouble(valuePoint -> valuePoint.getXValue()).min().getAsDouble();

		maxYValue = this.valuePoints.stream().mapToDouble(valuePoint -> valuePoint.getYValue()).max().getAsDouble();
		minYValue = this.valuePoints.stream().mapToDouble(valuePoint -> valuePoint.getYValue()).min().getAsDouble();

		
		minPValue = this.valuePoints.stream().filter(valuePoint -> valuePoint.getPValue().isPresent())
				.map(valuePoint -> valuePoint.getPValue()).mapToDouble(pValue -> pValue.get()).min();
		maxPValue = this.valuePoints.stream().filter(valuePoint -> valuePoint.getPValue().isPresent())
				.map(valuePoint -> valuePoint.getPValue()).mapToDouble(pValue -> pValue.get()).max();

		this.headerFields = dataModel.headerFields;
	}

	private void createDataModel(MA_PlotDrawProperties drawProperties, DataModel dataModel) {
		LeftToolJPanel leftToolJPanel = drawProperties.getLeftToolJPanel();

		int logMethodSelectIndex = leftToolJPanel.getLogMethodSelectedIndex();

		double base = leftToolJPanel.getBase();

		for (ValuePoint valuePoint : dataModel.getValuePoints()) {

			double xValue = valuePoint.getXValue();
			double yValue = valuePoint.getYValue();

			PaintPointPostion logMethod = getLogMethod(dataModel, xValue, yValue, logMethodSelectIndex,base);

			ValuePoint valuePoint2 = new ValuePoint();
			valuePoint2.setId(valuePoint.getId());
			valuePoint2.setXValue(logMethod.xValue);
			valuePoint2.setYValue(logMethod.yValue);
			valuePoint2.setPValue(valuePoint.getPValue());
			valuePoint2.setAnnotations(valuePoint.getAnnotations());
			
			this.valuePoints.add(valuePoint2);
		}

	}
	
	private double log(double base, double val) {
		return Math.log(val) / Math.log(base);
	}

	public PaintPointPostion getLogMethod(DataModel dataModel, double xValue, double yValue, int logMethodSelectIndex, double base) {
		double avg = 0; double change = 0;
		switch (logMethodSelectIndex) {
		case 0:
			// do not do anything
			avg = 0.5 * (xValue + yValue);
			change = yValue - xValue ;
			break;
		case 1:
			double yy = log(base, yValue);
			double xx = log(base, xValue);
			
			avg = 0.5 * ( yy + xx );
			change = yy - xx;
			break;
		case 2:
			yy = log(base, yValue + 1);
			xx = log(base, xValue + 1);
			
			avg = 0.5 * ( yy + xx );
			change = yy - xx;
			
			break;
		case 3:
			yy = yValue + dataModel.getMinXValue() +1;
			xx = xValue + dataModel.getMinYValue() + 1;
			avg = 0.5 * ( log(yy, base) + log(xx, base) );
			change = log(base, yy) - log(base, xx);
			break;

		default:
			break;
		}

		return new PaintPointPostion(avg, change);

	}

	class PaintPointPostion {
		double xValue;
		double yValue;

		public PaintPointPostion(double xValue, double yValue) {
			this.xValue = xValue;
			this.yValue = yValue;
		}

		public double getxValue() {
			return xValue;
		}

		public void setxValue(double xValue) {
			this.xValue = xValue;
		}

		public double getyValue() {
			return yValue;
		}

		public void setyValue(double yValue) {
			this.yValue = yValue;
		}
		
		@Override
		public String toString() {
			return "The value is: " + xValue + "\t" + yValue;
		}

	}
}
