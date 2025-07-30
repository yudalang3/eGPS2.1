package module.heatmap.eheatmap.model.distance;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import module.heatmap.eheatmap.model.PairwiseDistance;


public class PearsonsCor implements PairwiseDistance{
	
	private PearsonsCorrelation pp = new PearsonsCorrelation();

	@Override
	public double getPairwiseDist(double[] v1, double[] v2) {
		double correlation = pp.correlation(v1,v2);
		return 1.0 - Math.abs(correlation);
	}

}
