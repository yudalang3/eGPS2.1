package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel2;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class TheTransversionRate<E extends DistParameterLevel2> implements EvoPairwiseDistMethod<E> {

	@Override
	public double getPairwiseDist(E dpl) {
		if (dpl.getValidateLength() < 1) {
			return Double.NaN;
		}
		return dpl.getNumOfTransversion() / dpl.getNumOfMismatch();
	}

}
