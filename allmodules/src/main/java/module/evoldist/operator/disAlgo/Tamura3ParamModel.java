package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel3;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class Tamura3ParamModel<E extends DistParameterLevel3> implements EvoPairwiseDistMethod<E> {

	@Override
	public double getPairwiseDist(E dpl) {
		if (dpl.getValidateLength() < 1) {
			return Double.NaN;
		}
		// ElegantJTable G C T
		double[] countOf4Nucleotide = dpl.getTotalCountOf4Nucleotide();
		double valLen = dpl.getValidateLength();

		double[] gi = new double[4];
		for (int i = 0; i < 4; i++) {
			double g = 0.5 * countOf4Nucleotide[i] / valLen;
			gi[i] = g;
		}

		// GC%
		double theta = gi[1] + gi[2];
		double wTheta = 2 * theta * (1 - theta);
		double P = dpl.getNumOfTransition() / valLen;
		double Q = dpl.getNumOfTransversion() / valLen;
		double w1 = 1.0 - P / wTheta - Q;
		double w2 = 1.0 - 2 * Q;

		double ret = -wTheta * Math.log(w1) - 0.5 * (1 - wTheta) * Math.log(w2);

		if (ret <= 0) {
			return 0;
		} else {
			return ret;
		}
	}
}
