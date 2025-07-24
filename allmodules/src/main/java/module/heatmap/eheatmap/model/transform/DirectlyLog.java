package module.heatmap.eheatmap.model.transform;

public class DirectlyLog implements VectorTransform {

	private double base;
	
	public DirectlyLog(double base) {
		this.base = base;
	}

	
	@Override
	public double[] transform(double[] in) {
		int len = in.length;
		double[] ret = new double[len];
		for (int i = 0; i < len; i++) {
			ret[i] =Math.log(in[i]) / Math.log(base);
		}
		return ret;
	}

}
