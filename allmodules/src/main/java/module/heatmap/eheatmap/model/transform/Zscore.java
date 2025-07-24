package module.heatmap.eheatmap.model.transform;

import org.apache.commons.math3.stat.StatUtils;

public class Zscore implements VectorTransform{

	@Override
	public double[] transform(double[] in) {
		
		double mean = StatUtils.mean(in);
		double std = Math.sqrt(StatUtils.variance(in));
		
		int len = in.length;
		double[] ret = new double[len];
		for (int i = 0; i < len; i++) {
			ret[i] = ( in[i] - mean) / std;
		}
		return ret;
	}
}
