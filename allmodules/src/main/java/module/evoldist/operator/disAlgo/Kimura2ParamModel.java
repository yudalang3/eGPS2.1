package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel2;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class Kimura2ParamModel<E extends DistParameterLevel2> implements EvoPairwiseDistMethod<E> {
	
	public double divergence_Kimura2ParamModel(double numOfTransition, double numOfTransversion, double length) {
		double w1 = w1(numOfTransition, numOfTransversion, length);
		double w2 = w2(numOfTransversion, length);

		double d = -0.5d * Math.log(w1) - 0.25 * Math.log(w2);
		return d;
	}

	private double w1(double numOfTransition, double numOfTransversion, double length) {
		double P = numOfTransition / length;
		double Q = numOfTransversion / length;
		double w1 = 1.0d - 2.0d * P - Q;
		return w1;
	}

	private double w2(double numOfTransversion, double length) {
		double Q = numOfTransversion / length;
		double w2 = 1.0d - 2.0d * Q;
		return w2;
	}

	@Override
	public double getPairwiseDist(E dpl) {
		if (dpl.getValidateLength() < 1) {
			return Double.NaN;
		}

		double ret = divergence_Kimura2ParamModel(dpl.getNumOfTransition(), dpl.getNumOfTransversion(),
				dpl.getValidateLength());
		if (ret <= 0) {
			return 0;
		} else {
			return ret;
		}
	}
}
