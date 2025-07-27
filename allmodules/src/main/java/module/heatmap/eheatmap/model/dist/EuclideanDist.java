package module.heatmap.eheatmap.model.dist;

import java.util.Arrays;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import module.heatmap.eheatmap.model.PairwiseDistance;

public class EuclideanDist implements PairwiseDistance{


	private EuclideanDistance euclideanDistance= new EuclideanDistance();

	@Override
	public double getPairwiseDist(double[] v1, double[] v2) {
		double compute = euclideanDistance.compute(v1, v2);
        
		if (compute < 0.0) {
            System.err.println("v1\t"+Arrays.toString(v1));
            System.err.println("v2\t"+Arrays.toString(v2));
        }
		return compute;
	}

}
