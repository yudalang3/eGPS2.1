package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel1;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class JukesCantorModel<E extends DistParameterLevel1> implements EvoPairwiseDistMethod<E> {

	@Override
	public double getPairwiseDist(E t) {
		if (t.getValidateLength() < 1) {
			return Double.NaN;
		}
		
		double p = t.getNumOfMismatch() / t.getValidateLength();
		double ret =  - 0.75 * Math.log(1 - 4.0 / 3 * p);
		
		if (ret <= 0) {
			return 0;
		} else {
			return ret;
		}
	}

}
