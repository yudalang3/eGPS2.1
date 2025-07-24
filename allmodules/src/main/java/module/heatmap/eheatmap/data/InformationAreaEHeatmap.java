package module.heatmap.eheatmap.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import module.heatmap.eheatmap.model.DataModel;

public class InformationAreaEHeatmap extends InformationAreaMatrix {
	private static final long serialVersionUID = 6670740227975416002L;

	@Override
	public void loadingInformation(File file) {
		DataModel dataModel = new DataModel();
		
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(file,StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dataModel.setDataFromTextContens(lines, false);

		String[] columnNames = dataModel.getColNames();
		int leng = columnNames.length + 1;
		String[] realColNames = new String[leng];
		realColNames[0] = "Title";
		System.arraycopy(columnNames, 0, realColNames, 1, columnNames.length);

		String[] rowNames = dataModel.getRowNames();
		double[][] dataMatrix = dataModel.getDataMatrix();
		int length = rowNames.length > 50 ? 50 : rowNames.length;
		String[][] tableElements = new String[length][];
		for (int i = 0; i < length; i++) {
			String[] tt = new String[leng];
			tt[0] = rowNames[i];
			for (int j = 1; j < leng; j++) {
				tt[j] = dataMatrix[i][j - 1] + "";
			}
			tableElements[i] = tt;
		}
		// System.out.println(Arrays.toString(realColNames));
		getTableModel(columnNames, tableElements);

		textFieldNumOfNAs.setText(getNumOfNAs(dataMatrix));
		textFieldNumOfRows.setText(dataMatrix.length + "");
		textFieldNumOfColumns.setText(leng + "");

		comboBoxLineSeparator.setEnabled(false);
		chckbxHeader.setEnabled(false);
		chckbxRowNames.setEnabled(false);

		rdbtnGeneralMatrix.setEnabled(false);

		rdbtnRNA.setEnabled(false);

		rdbtnProtein.setEnabled(false);
	}

	private String getNumOfNAs(double[][] dataMatrix) {
		int ret = 0;
		for (double[] ds : dataMatrix) {
			for (double dd : ds) {
				if (Double.isNaN(dd)) {
					ret++;
				}
			}
		}
		return ret + "";
	}
}
