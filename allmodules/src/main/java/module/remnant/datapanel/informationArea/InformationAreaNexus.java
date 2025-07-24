package module.remnant.datapanel.informationArea;

import java.awt.Color;
import java.io.File;

import javax.swing.border.EtchedBorder;

import module.remnant.datapanel.data.DataCenter;
import module.remnant.datapanel.data.IDataCenter;

public class InformationAreaNexus extends AbstractTableBasedArea {

	private static final long serialVersionUID = -8177142760877757044L;

	public InformationAreaNexus() {
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

		rowValues = new String[] { "Block information", dataCenter.getDataFormatNameFromDataFormat(dataCenter.getCurrentDataFormat())};
		tableModel.addRow(rowValues);

		table.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		table.setGridColor(Color.lightGray);
		
		table.getColumnModel().getColumn(0).setMaxWidth(200);
		table.getColumnModel().getColumn(0).setMinWidth(200);
	}

}
