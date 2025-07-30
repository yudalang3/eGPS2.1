package module.heatmap.eheatmap.model.distance;

import module.heatmap.eheatmap.model.PairwiseDistance;

public class AvgDotProduct implements PairwiseDistance{

	@Override
	public double getPairwiseDist(double[] v1, double[] v2) {
		double ret = 0;
		int len = v1.length;
		
		for (int i = 0; i < len; i++) {
			ret += v1[i] * v2[i];
		}
		return ret / len;
	}

}
