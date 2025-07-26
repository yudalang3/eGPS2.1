package module.evoldist.operator.disAlgo;

import module.evoldist.operator.pairdist.DistParameterLevel51;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class LogDetTamuraKumar<E extends DistParameterLevel51> implements EvoPairwiseDistMethod<E> {

	@Override
	public double getPairwiseDist(E dpl) {
		if (dpl.getValidateLength() < 1) {
			return Double.NaN;
		}
		double valLen = dpl.getValidateLength();
		// A0 G1 C2 T3
		double[] countOf4Nucleotide1 = dpl.getSeq1CountOf4Nucl();
		double[] countOf4Nucleotide2 = dpl.getSeq2CountOf4Nucl();
		double[] g1s = new double[4];
		double[] g2s = new double[4];
		for (int i = 0; i < 4; i++) {
			g1s[i] = (double) countOf4Nucleotide1[i] / valLen;
			g2s[i] = (double) countOf4Nucleotide2[i] / valLen;
		}

		double ga = 0.5 * (g1s[0] + g2s[0]);
		double gg = 0.5 * (g1s[1] + g2s[1]);
		double gc = 0.5 * (g1s[2] + g2s[2]);
		double gt = 0.5 * (g1s[3] + g2s[3]);

		// 0 1
		double fAG = g1s[0] * g2s[1] + g1s[1] * g2s[0];
		double fTC = g1s[2] * g2s[3] + g1s[3] * g2s[2];
		// R = A + G; Y = T + C;
		double fRY = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 2; j < 4; j++) {
				fRY += g1s[i] * g2s[j] + g1s[j] * g2s[i];
			}
		}

		double P1 = dpl.getNumOfTransitionAG() / valLen;
		double P2 = dpl.getNumOfTransitionTC() / valLen;

		double Q = dpl.getNumOfTransversion() / valLen;

		double gr = ga + gg;
		double gy = gt + gc;
		double k1 = 2 * ga * gg / gr;
		double k2 = 2 * gt * gc / gy;
		double k3 = 2 * (gr * gy - ga * gg * gy / gr - gt * gc * gr / gy);
		double w1 = 1.0 - P1 * gr / (fAG) - Q / (2 * gr);
		double w2 = 1.0 - P2 * gy / (fTC) - Q / (2 * gy);
		double w3 = 1.0 - Q / (fRY);

		double ret = -k1 * Math.log(w1) - k2 * Math.log(w2) - k3 * Math.log(w3);
		
		if (ret <= 0) {
			return 0;
		}else {
			return ret;
		}
		
	}
}
