package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel42;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class TajimaNeiModel<E extends DistParameterLevel42> implements EvoPairwiseDistMethod<E> {

	private double getB(double[] countOf4Nucleotide, double[][] countOfNuctd2nd,double pSquare,double valLen) {
		
		double sumGiSquare = 0;
		double[] gi = new double[4];
		for (int i = 0; i < 4; i++) {
			 double g= 0.5 * countOf4Nucleotide[i] / valLen;
			 gi[i] = g;
			 sumGiSquare += g * g; 
		}
		
		double sum = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = i + 1; j < 4; j++) {
				double xij = countOfNuctd2nd[i][j] / (double) valLen;
				sum += xij * xij / (2 * gi[i] * gi[j] );
			}
		}
		
		return 0.5 * (1 - sumGiSquare + pSquare / sum);
	}

	@Override
	public double getPairwiseDist(E dpl) {
		if (dpl.getValidateLength() < 1) {
			return Double.NaN;
		}
		
		double p = dpl.getNumOfMismatch() / dpl.getValidateLength();
		if (p == 0) {
			return 0;
		}
		
		double b = getB(dpl.getTotalCountOf4Nucleotide(), dpl.getCountOfNucltd2nd(), p * p,dpl.getValidateLength());
		double ret = -b * Math.log(1 - p / b);
		
		if (ret <= 0) {
			return 0;
		}else {
			return ret;
		}
	}
	
}
