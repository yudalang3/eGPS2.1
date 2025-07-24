package module.heatmap.eheatmap.model.transform;

public class LogPlusOne implements VectorTransform {
	
	private double base;
	
	public LogPlusOne(double base) {
		this.base = base;
	}

	@Override
	public double[] transform(double[] in) {
		int len = in.length;
		double[] ret = new double[len];
		for (int i = 0; i < len; i++) {
			ret[i] =Math.log1p(in[i]) / Math.log1p(base);
		}
		return ret;
	}

}
