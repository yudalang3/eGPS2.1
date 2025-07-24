package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel1;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class SimplePDistance<E extends DistParameterLevel1> implements EvoPairwiseDistMethod<E> {

	@Override
	public double getPairwiseDist(E dpl) {
		if (dpl.getValidateLength() < 1) {
			return Double.NaN;
		}
		return dpl.getNumOfMismatch() / dpl.getValidateLength();
	}
	

}
