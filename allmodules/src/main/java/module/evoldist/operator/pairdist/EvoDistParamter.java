package module.evoldist.operator.pairdist;

public class EvoDistParamter {
	/** No. of A <-> G; No. of C <-> T */
	private double numOfTransition[];
	/** A <-> (C,T); G <-> (C,T) */
	private double numOfTransversion;
	/** Sequences except ATGC( a.k.a fuzzy nucleotide or gap‘-’) */
	private double validateLength;
	/** count of A G C T */
	private double[] countOf4NucleotideOfSeq1;
	private double[] countOf4NucleotideOfSeq2;
	/**
	 * count of nucleotide to nucleotide when sequence compared! 
	 * <pre>
	 *   A G C T
	 * A x11 x12 x13 x14
	 * G . . . 
	 * C      . . . 
	 * T             x44
	 * </pre>
	 */
	private double[][] countOfNuctd2nd;

	/** Constructor for using this class to add!! */
	public EvoDistParamter(int cluster) {
		clear();
	}
	/** Genetic distance method cluster 1 */
	public EvoDistParamter(double[] tSition, double tVersion, double vLen) {
		this.numOfTransition = tSition;
		this.numOfTransversion = tVersion;
		this.validateLength = vLen;
	}

	/** Genetic distance method cluster 2 */
	public EvoDistParamter(double[] tSition, double tVersion, double vLen, double[] c4nn1, double[] c4nn2, double[][] cN2n) {
		this.numOfTransition = tSition;
		this.numOfTransversion = tVersion;
		this.validateLength = vLen;
		this.countOf4NucleotideOfSeq1 = c4nn1;
		this.countOf4NucleotideOfSeq2 = c4nn2;
		this.countOfNuctd2nd = cN2n;
	}
	
	@Override
	public String toString() {
//		if (geneticDistanceCluster == EvoDistPrepare.METHOD_CLUSTER1) {
//			return numOfTransition + " " + numOfTransversion + " " + validateLength;
//		}else {
//			return numOfTransition + " " + numOfTransversion + " " + validateLength;
//		}
		double tt = numOfTransition[0] + numOfTransition[1];
		return " " + tt + " " + numOfTransversion + " " + validateLength;
	}

	public void clear() {
		numOfTransition = new double[2];
		numOfTransversion = 0;
		validateLength = 0;
		countOf4NucleotideOfSeq1 = new double[4];
		countOf4NucleotideOfSeq2 = new double[4];
		countOfNuctd2nd = new double[4][4];
	}

	public double getNumOfTransition() {
		return numOfTransition[0] + numOfTransition[1];
	}
	
	public double[] getNumOfTransitionArray() {
		return numOfTransition;
	}
	
	public double getNumOfMismatch() {
		return getNumOfTransition() + getNumOfTransversion();
	}

	public double getNumOfTransversion() {
		return numOfTransversion;
	}

	public double getValidateLength() {
		return validateLength;
	}

	public double[] getTotalCountOf4Nucleotide() {
		double[] ret = new double[4];
		for (int i = 0; i < 4; i++) {
			ret[i] += countOf4NucleotideOfSeq1[i];
			ret[i] += countOf4NucleotideOfSeq2[i];
		}
		return ret;
	}
	
	public double getTotalNumOfNucleotide() {
		long ret = 0;
		for (int i = 0; i < 4; i++) {
			ret += countOf4NucleotideOfSeq1[i];
			ret += countOf4NucleotideOfSeq2[i];
		}
		return ret;
	}
	public double[] getCountOf4NucleotideOfSeq1() {
		return countOf4NucleotideOfSeq1;
	}
	public double[] getCountOf4NucleotideOfSeq2() {
		return countOf4NucleotideOfSeq2;
	}
	public double[][] getCountOfNuctd2nd() {
		return countOfNuctd2nd;
	}

}
