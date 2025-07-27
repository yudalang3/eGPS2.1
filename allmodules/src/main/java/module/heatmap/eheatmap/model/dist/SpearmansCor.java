package module.heatmap.eheatmap.model.dist;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import module.heatmap.eheatmap.model.PairwiseDistance;


public class SpearmansCor implements PairwiseDistance{

	private SpearmansCorrelation pp= new SpearmansCorrelation();

	@Override
	public double getPairwiseDist(double[] v1, double[] v2) {
		double correlation = pp.correlation(v1, v2);
		return 1.0 - Math.abs(correlation);
	}

}
