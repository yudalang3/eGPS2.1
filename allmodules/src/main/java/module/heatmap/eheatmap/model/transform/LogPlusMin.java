package module.heatmap.eheatmap.model.transform;

public class LogPlusMin implements VectorTransform {
	
	private double base;
	private double absMinValue;
	
	public LogPlusMin(double base,double minValue) {
		this.base = base;
		this.absMinValue = Math.abs(minValue);
	}

	@Override
	public double[] transform(double[] in) {
		int len = in.length;
		double[] ret = new double[len];
		for (int i = 0; i < len; i++) {
			ret[i] =Math.log(in[i] + absMinValue + 1) / Math.log(base);
		}
		return ret;
	}

}
