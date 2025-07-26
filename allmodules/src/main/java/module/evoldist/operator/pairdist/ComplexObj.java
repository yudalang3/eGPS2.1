package module.evoldist.operator.pairdist;

public class ComplexObj extends SimpleObj {
	double[] countOf4nnSeq1 = new double[4];
	double[] countOf4nnSeq2 = new double[4];
	double[][] countOfnucl2nuc = new double[4][4];

	/**
	 * @param index : A G C T index of count to add(0,1,2,3)
	 * @param value : value to be added
	 */
	public void addSeq1CountInfo(int index, double value) {
		countOf4nnSeq1[index] += value;
	}

	/**
	 * @param index : A G C T index of count to add
	 * @param value : value to be added
	 */
	public void addSeq2CountInfo(int index, double value) {
		countOf4nnSeq2[index] += value;
	}

	/**
	 * A G C T A G C T
	 */
	public void addToCountMatrix(int i, int j, double value) {
		countOfnucl2nuc[i][j] += value;
	}

}