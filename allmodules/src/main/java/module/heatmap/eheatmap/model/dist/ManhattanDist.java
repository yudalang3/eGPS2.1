package module.heatmap.eheatmap.model.dist;

import org.apache.commons.math3.ml.distance.ManhattanDistance;

import module.heatmap.eheatmap.model.PairwiseDistance;

public class ManhattanDist implements PairwiseDistance{


	private ManhattanDistance manhattanDistance= new ManhattanDistance();

	@Override
	public double getPairwiseDist(double[] v1, double[] v2) {
		return manhattanDistance.compute(v1, v2); 
	}

}
