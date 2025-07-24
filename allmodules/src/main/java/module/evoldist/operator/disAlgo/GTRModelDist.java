package module.evoldist.operator.disAlgo;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import module.evoldist.operator.pairdist.DistParameterLevel42;
import module.evoldist.operator.pairdist.EvoPairwiseDistMethod;

public class GTRModelDist<E extends DistParameterLevel42> implements EvoPairwiseDistMethod<E> {

	private RealMatrix inverseMatrix(RealMatrix A) {
		RealMatrix result = new LUDecomposition(A).getSolver().getInverse();
		return result;
	}

	@Override
	public double getPairwiseDist(E dpl) {
		if (dpl.getValidateLength() < 1) {
			return Double.NaN;
		}

		double[][] countOfNuctd2nd = dpl.getCountOfNucltd2nd();
		double[] totalCountOf4Nucleotide = dpl.getTotalCountOf4Nucleotide();
		double total = 2 * dpl.getValidateLength();

		double[][] PIMatrix = new double[4][4];
		for (int i = 0; i < 4; i++) {
			PIMatrix[i][i] = totalCountOf4Nucleotide[i] / total;
		}

		double[][] fHat = new double[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				fHat[i][j] = (countOfNuctd2nd[i][j] + countOfNuctd2nd[j][i]) / total;
			}
		}

		RealMatrix realPIMatrix = new Array2DRowRealMatrix(PIMatrix);
		RealMatrix realFHatMatrix = new Array2DRowRealMatrix(fHat);

		RealMatrix inverseRealPIMatrix = inverseMatrix(realPIMatrix);
		RealMatrix multiply = inverseRealPIMatrix.multiply(realFHatMatrix);

		double[][] data = multiply.getData();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length; j++) {
				data[i][j] = Math.log(data[i][j]);
			}
		}

		RealMatrix temp = new Array2DRowRealMatrix(data);
		RealMatrix ret = realPIMatrix.multiply(temp);

		return -ret.getTrace();
	}
}
