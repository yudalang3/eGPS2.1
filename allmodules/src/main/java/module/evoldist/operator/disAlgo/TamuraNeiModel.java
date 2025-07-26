package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel41;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class TamuraNeiModel<E extends DistParameterLevel41> implements EvoPairwiseDistMethod<E> {

	@Override
	public double getPairwiseDist(E t) {
		if (t.getValidateLength() < 1) {
			return Double.NaN;
		}

		// A G C T
		double[] countOf4Nucleotide = t.getTotalCountOf4Nucleotide();
		double valLen = t.getValidateLength();

		double ga = 0.5 * countOf4Nucleotide[0] / valLen;
		double gg = 0.5 * countOf4Nucleotide[1] / valLen;
		double gc = 0.5 * countOf4Nucleotide[2] / valLen;
		double gt = 0.5 * countOf4Nucleotide[3] / valLen;

		double P1 = t.getNumOfTransitionAG() / valLen;
		double P2 = t.getNumOfTransitionTC() / valLen;

		double Q = (double) t.getNumOfTransversion() / valLen;

		double gr = ga + gg;
		double gy = gt + gc;
		double k1 = 2 * ga * gg / gr;
		double k2 = 2 * gt * gc / gy;
		double k3 = 2 * (gr * gy - ga * gg * gy / gr - gt * gc * gr / gy);
		double w1 = 1.0 - P1 / k1 - Q / (2 * gr);
		double w2 = 1.0 - P2 / k2 - Q / (2 * gy);
		double w3 = 1.0 - Q / (2 * gr * gy);

		double ret = -k1 * Math.log(w1) - k2 * Math.log(w2) - k3 * Math.log(w3);

		if (ret <= 0) {
			return 0;
		} else {
			return ret;
		}
	}
	
}
