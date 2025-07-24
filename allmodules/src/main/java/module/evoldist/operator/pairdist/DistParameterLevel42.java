package module.evoldist.operator.pairdist;

public class DistParameterLevel42 extends DistParameterLevel3{

	/**
	 * count of nucleotide to nucleotide when sequence compared! 
	 * <pre>
	 *   ElegantJTable G C T
	 * ElegantJTable x11 x12 x13 x14
	 * G . . . 
	 * C      . . . 
	 * T             x44
	 * </pre>
	 */
	double[][] countOfNucltd2nd;
	
	public DistParameterLevel42() {
		clear();
	}
	
	public DistParameterLevel42(double mis,double trans,double vl,double[] t4n,double[][] n2n){
		numOfMismatch = mis;
		numOfTransition = trans;
		validateLength = vl;
		totalCountOf4Nucl = t4n;
		countOfNucltd2nd = n2n;
	}
	
	@Override
	public void clear() {
		super.clear();
		countOfNucltd2nd = new double[4][4];
	}
	
	public void add(DistParameterLevel42 dpl) {
		super.add(dpl);
		double[][] otherN2N = dpl.getCountOfNucltd2nd();
		final int typesOfNucl = 4;
		for (int i = 0; i < typesOfNucl; i++) {
			for (int j = 0; j < typesOfNucl; j++) {
				countOfNucltd2nd[i][j] += otherN2N[i][j];
			}
		}
	}
	@Override
	public <E extends DistParameterLevel1> void add(E dpl) {
		super.add(dpl);
		double[][] otherN2N = ((DistParameterLevel42) dpl).getCountOfNucltd2nd();
		final int typesOfNucl = 4;
		for (int i = 0; i < typesOfNucl; i++) {
			for (int j = 0; j < typesOfNucl; j++) {
				countOfNucltd2nd[i][j] += otherN2N[i][j];
			}
		}
	}
	
	public double[][] getCountOfNucltd2nd() {
		return countOfNucltd2nd;
	}
}
