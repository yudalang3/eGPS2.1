package module.pcarunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import egps2.utils.common.tablelike.MatrixTableContentBean;
import module.heatmap.eheatmap.data.ValidateMatrixTable;

public class PCADemo {

    public static void main(String[] args) {
        // Create your data matrix
//        double[][] data = {
//            {1, 2, 3},
//            {4, 5, 6},
//            {7, 8, 9},
//				{ 7, 8, 9 }, { 7, 8, 9 }, { 7, 8, 9 },
//            // ... more data rows
//        };
		double[][] data = getData();

        // Convert the data into a RealMatrix object
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        // Perform PCA
        EigenDecomposition eigenDecomp = new EigenDecomposition(matrix.transpose().multiply(matrix));
        double[] eigenvalues = eigenDecomp.getRealEigenvalues();
        RealMatrix eigenvectors = eigenDecomp.getV();

		// Calculate the total variance
		double totalVariance = Arrays.stream(eigenvalues).sum();

		// Calculate the percentage of variance explained by each component
		for (int i = 0; i < eigenvalues.length; i++) {
			double percentage = (eigenvalues[i] / totalVariance) * 100;
			System.out.printf("Component %d explains %.2f%% of the variance.\n", i + 1, percentage);
		}

        // Sort eigenvectors by eigenvalue in descending order
        double[][] sortedEigenvectors = sortEigenvectorsByEigenvalue(eigenvalues, eigenvectors);

        // Print the principal components
        System.out.println("Principal Components:");
		int length = sortedEigenvectors.length;
		for (int i = 0; i < length; i++) {
			if (i > 1) {
				break;
			}

			double[] a = sortedEigenvectors[i];
			System.out.println("length of a is " + a.length);
			System.out.println(Arrays.toString(a));
        }

		double[] xData = sortedEigenvectors[0];
		double[] yData = sortedEigenvectors[1];

		// Create Chart
		XYChart chart = new XYChartBuilder().width(800).height(600).title("PCA Visualization").xAxisTitle("PC1")
				.yAxisTitle("PC2").build();

		// Customize Chart
		chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
//		chart.getStyler().setAvailableSpaceFill(true);
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

		// Series
		chart.addSeries("PCA", xData, yData);

		// Show it
		new SwingWrapper<>(chart).displayChart();
    }

    private static double[][] sortEigenvectorsByEigenvalue(double[] eigenvalues, RealMatrix eigenvectors) {
        // Create an array to hold the sorted eigenvectors
        double[][] sortedEigenvectors = new double[eigenvalues.length][];

        // Copy the eigenvectors to the sorted array in descending order of eigenvalue magnitude
        int sortedIndex = 0;
        for (int i = 0; i < eigenvalues.length; i++) {
            double maxEigenvalue = Double.NEGATIVE_INFINITY;
            int maxIndex = -1;
            for (int j = 0; j < eigenvalues.length; j++) {
                if (eigenvalues[j] > maxEigenvalue && sortedEigenvectors[j] == null) {
                    maxEigenvalue = eigenvalues[j];
                    maxIndex = j;
                }
            }
            sortedEigenvectors[sortedIndex++] = eigenvectors.getColumn(maxIndex);
            eigenvalues[maxIndex] = Double.NEGATIVE_INFINITY;
        }

        return sortedEigenvectors;
    }

	private static double[][] getData() {
		String inputPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\带学生\\zhenghn\\ZHN-fat sample- RNA-Seq\\RNA-seq20240628_ydl4zhn\\all.sample.expression.counts.tsvmatrix.tpm.tsv";
		ValidateMatrixTable validateMatrixTable = new ValidateMatrixTable();
		try {
			validateMatrixTable.getFileFormat(new File(inputPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Optional<MatrixTableContentBean> bean = validateMatrixTable.getBean();
		MatrixTableContentBean matrixTableContentBean = bean.get();
		String[][] tableElements = matrixTableContentBean.getTableElements();
		
		int numOfRows = tableElements.length;
		int numOfCols = matrixTableContentBean.getNumOfColumns();
		double[][] ret = new double[numOfRows][numOfCols - 1];
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 1; j < numOfCols; j++) {
				String string = tableElements[i][j];
				double value = Double.parseDouble(string);
				ret[i][j - 1] = value;
			}
		}
		return ret;

	}
}