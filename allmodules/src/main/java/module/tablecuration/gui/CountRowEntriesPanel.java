/**
 * 
 */
package module.tablecuration.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Stopwatch;

import utils.string.EGPSStringUtil;
import tsv.io.IntTable;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

public class CountRowEntriesPanel extends DIYToolSubTabModuleFace {

	public CountRowEntriesPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.table.file", "",
				"Input data file, usually the tsv file. Necessary");
		mapProducer.addKeyValueEntryBean("is.binary.output", "T", "Output results with 0-1 binary, default is T.");
		mapProducer.addKeyValueEntryBean("dimension1", "",
				"input one column: col1. Or multiple columns: col1;col2;col3 . Necessary");
		mapProducer.addKeyValueEntryBean("dimension2", "", "Only input one column: col2. Necessary");

		mapProducer.addKeyValueEntryBean("output.file", "", "Output file. Necessary");
	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputFile = quickParseString("input.table.file");
		boolean isBinaryOutput = quickParseBoolean("is.binary.output");
		String targetColumns = quickParseString("dimension1");
		String[] dim1 = EGPSStringUtil.split(targetColumns, ';');
		String dim2 = quickParseString("dimension2");
		String outputFilePath = quickParseString("output.file");

		Stopwatch stopwatch = Stopwatch.createStarted();

		List<String> outputs = new LinkedList<>();

		TableOrganizer tableOrganizer = new TableOrganizer();
		IntTable countRowEntries = tableOrganizer.countRowEntries(inputFile, Arrays.asList(dim1), dim2);

		outputResults(isBinaryOutput, outputFilePath, countRowEntries);

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);
	}

	protected void outputResults(boolean isBinaryOutput, String outputFilePath,
								 IntTable countRowEntries) throws IOException {

		try (BufferedWriter br = new BufferedWriter(new FileWriter(outputFilePath))) {
			String colNameOfRowNames = countRowEntries.getColNameOfRowNames();
			String[] rowNames = countRowEntries.getRowNames();
			String[] colNames = countRowEntries.getColNames();
			int[][] table = countRowEntries.getTable();
			String join = String.join("\t", colNames);
			br.write(colNameOfRowNames );
			br.write("\t");
			br.write(join);
			br.write("\n");

			for (int i = 0; i < rowNames.length; i++) {
				String rowName = rowNames[i];
				int[] row = table[i];
				br.write(rowName);
				for (int j = 0; j < row.length; j++) {
					int norValue = row[j];
					if (isBinaryOutput){
						norValue = norValue > 0 ? 1 : 0;
					}
					br.write('\t');
					br.write(String.valueOf(norValue));
				}
				br.write("\n");
			}

		}
	}


}
