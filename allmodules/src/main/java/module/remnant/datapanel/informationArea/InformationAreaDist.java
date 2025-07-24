package module.remnant.datapanel.informationArea;

import java.awt.Color;
import java.io.File;

import javax.swing.border.EtchedBorder;

import module.remnant.datapanel.data.DataCenter;
import module.remnant.datapanel.data.IDataCenter;
import module.webmsaoperator.webIO.DistMatrixTextInput;

public class InformationAreaDist extends AbstractTableBasedArea {

	private static final long serialVersionUID = -8177142760877757044L;

	public InformationAreaDist() {
	}

	@Override
	public void loadingInformation(File file) {
		// 添加一行，记载文件的路径

		int rowCount = tableModel.getRowCount();
		for (int i = rowCount - 1; i > -1; i--) {
			tableModel.removeRow(i);
		}

		IDataCenter dataCenter = new DataCenter();

		String[] rowValues = { Constants.INPUT_PATH_NAME, file.getAbsolutePath() };
		tableModel.addRow(rowValues);
		rowValues = new String[] { Constants.DATA_TYPE_NAME,
				dataCenter.getDataTypeNameFromDataType(dataCenter.getCurrentDataType()) };
		tableModel.addRow(rowValues);

		DistMatrixTextInput d = new DistMatrixTextInput(file);
		d.getDistanceMatrix();
		String[] otu_names = d.getOTU_names();
		rowValues = new String[] { " Number of OTUs", otu_names.length+""};
		tableModel.addRow(rowValues);

		table.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		table.setGridColor(Color.lightGray);
		
		table.getColumnModel().getColumn(0).setMaxWidth(200);
		table.getColumnModel().getColumn(0).setMinWidth(200);
	}

}
