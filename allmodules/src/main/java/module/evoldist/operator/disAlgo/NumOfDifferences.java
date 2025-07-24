package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel1;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class NumOfDifferences<E extends DistParameterLevel1> implements EvoPairwiseDistMethod<E> {
	@Override
	public double getPairwiseDist(DistParameterLevel1 dpl) {
		if (dpl.getValidateLength() < 1) {
			return Double.NaN;
		}
		return dpl.getNumOfMismatch();
	}


}
