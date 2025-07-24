package module.remnant.datapanel.informationArea;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.border.EtchedBorder;

import org.apache.commons.io.FileUtils;

import module.remnant.datapanel.data.DataCenter;
import module.remnant.datapanel.data.DataFormat;
import module.remnant.datapanel.data.IDataCenter;
import msaoperator.io.seqFormat.parser.ClustalWParser;
import msaoperator.io.seqFormat.parser.GCGMSFParser;
import msaoperator.io.seqFormat.parser.PHYParser;

public class InformationAreaMultipleSeqs extends AbstractTableBasedArea {

	private static final long serialVersionUID = -8177142760877757044L;

	public InformationAreaMultipleSeqs() {
	}

	@Override
	public void loadingInformation(File file) {
		// 添加一行，记载文件的路径

		int rowCount = tableModel.getRowCount();
		for (int i = rowCount - 1; i > -1; i--) {
			tableModel.removeRow(i);
		}

		IDataCenter dataCenter = new DataCenter();
		int currentDataFormat = dataCenter.getCurrentDataFormat();

		String[] rowValues = { Constants.INPUT_PATH_NAME, file.getAbsolutePath() };
		tableModel.addRow(rowValues);
		rowValues = new String[] { Constants.DATA_TYPE_NAME,
				dataCenter.getDataTypeNameFromDataType(dataCenter.getCurrentDataType()) };
		tableModel.addRow(rowValues);
		rowValues = new String[] { Constants.DATA_FORMAT_NAME,
				dataCenter.getDataFormatNameFromDataFormat(currentDataFormat) };
		tableModel.addRow(rowValues);

		String seqType = "";
		// "aligned" || "unaligned"

		if (currentDataFormat == DataFormat.UNALIGNED_FASTA) {
			seqType = "unaligned";
		} else {
			seqType = "aligned";
		}

		rowValues = new String[] { " Sequence type: ", seqType };
		tableModel.addRow(rowValues);

		String contentType = "";
		if (currentDataFormat == DataFormat.ALIGNED_FASTA) {
			try {
				List<String> readLines = FileUtils.readLines(file);
				boolean ifThirdLineStartWithSigSymbol = readLines.get(2).startsWith(">");
				if (ifThirdLineStartWithSigSymbol) {
					contentType = "Sequential";
				}else {
					contentType = "Interleaved";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (currentDataFormat == DataFormat.ALIGNED_PAML || currentDataFormat == DataFormat.ALIGNED_MEGA) {
			contentType = "Sequential";
		} else {
			int number = 0;
			try {
				if (currentDataFormat == DataFormat.ALIGNED_CLUSTALW) {
					ClustalWParser clustalWParser = new ClustalWParser(file);
					number = clustalWParser.getBlockNumber();
				} else if (currentDataFormat == DataFormat.ALIGNED_PHYLIP) {
					PHYParser phyParser = new PHYParser(file);
					number = phyParser.getBlockNumber();
				} else if (currentDataFormat == DataFormat.ALIGNED_GCGMSF) {
					GCGMSFParser gcgmsfParser = new GCGMSFParser(file);
					number = gcgmsfParser.getBlockNumber();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			contentType = "Interleaved(Block = " + number + ")";
		}
		rowValues = new String[] { " Content type: ", contentType };
		tableModel.addRow(rowValues);

		table.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		table.setGridColor(Color.lightGray);

		table.getColumnModel().getColumn(0).setMaxWidth(200);
		table.getColumnModel().getColumn(0).setMinWidth(200);
	}

}
