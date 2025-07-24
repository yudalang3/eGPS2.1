package module.heatmap.eheatmap.model.transform;

import org.apache.commons.math3.stat.StatUtils;

public class MinMaxScale implements VectorTransform{

	@Override
	public double[] transform(double[] in) {
		
		double min = StatUtils.min(in);
		double max = StatUtils.max(in);
		
		int len = in.length;
		double[] ret = new double[len];
		for (int i = 0; i < len; i++) {
			ret[i] = ( in[i] - min) / (max - min);
		}
		return ret;
	}
}
