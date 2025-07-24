package module.webmsaoperator.webIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import egps2.panels.dialog.SwingDialog;

public class DistMatrixTextInput {

	private File inputFile;
	private String[] OTU_names;

	public DistMatrixTextInput(File inputFile) {
		this.inputFile = inputFile;
	}

	public DistMatrixTextInput(String filePath) {
		this.inputFile = new File(filePath);
	}

	public String[] getOTU_names() {
		return OTU_names;
	}

	public double[][] getDistanceMatrix() {
		double[][] distM = null;
		try {
			double[][] ttDistM = readInData();

			// remove dig elements
			int nn = ttDistM.length - 1;
			distM = new double[nn][];

			for (int i = 0; i < nn; i++) {
				double[] aRow = new double[i + 1];
				for (int j = 0; j <= i; j++) {
					aRow[j] = ttDistM[i + 1][j];
				}
				distM[i] = aRow;
			}
		} catch (IOException e) {
			SwingDialog.showErrorMSGDialog("Input Error",
					"The input evolutionary distance file contains something don't consistent with eGPS format specification."
							+ "\nPlease check it in user manual!");
		}

		return distM;
	}

	public double[][] readInData() throws IOException {
		double[][] ret = null;
		int numOfOTU = 0;
		int rowIndex = 0;

		List<String> otuList = null;

		String str = null;
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		while ((str = br.readLine()) != null) {
			str = str.trim();
			if (str.length() == 0 || str.startsWith("#")) {
				continue;
			}
			rowIndex++;
			if (rowIndex == 1) {

				numOfOTU = Integer.parseInt(str.trim());
				ret = new double[numOfOTU][];
				otuList = new ArrayList<>(numOfOTU);

				// OTU_names = Arrays.copyOfRange(firstLineElements, 1 ,
				// firstLineElements.length);
			} else {
				int i = rowIndex - 2; // when rowIndex = 2 , this is the first line
				String[] rowSplits = str.split("\\s+");
				// System.out.println(Arrays.toString(rowSplits)+"\t"+i);

				otuList.add(rowSplits[0]);

				double[] aRow = new double[i + 1];
				for (int j = 0; j <= i; j++) {
					aRow[j] = Double.parseDouble(rowSplits[j + 1]);
				}
				// System.out.println(Arrays.toString(aRow));
				ret[i] = aRow;
			}

		}
		OTU_names = otuList.toArray(new String[otuList.size()]);

		br.close();
		return ret;
	}

	/*
	 * public static void main(String[] args) { DistMatrixTextInput d = new
	 * DistMatrixTextInput(
	 * "E:/javaCode/eGPS_example_data_sets/Distance/testGeneticDistance.txt");
	 * double[][] distanceMatrix = d.getDistanceMatrix(); for (double[] es :
	 * distanceMatrix) { for (double e : es) { System.out.print(e+"\t"); }
	 * System.out.println(); } }
	 */
}
