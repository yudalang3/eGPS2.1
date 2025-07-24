/**
 * 
 */
package module.tablecuration.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import utils.string.EGPSStringUtil;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class RemoveDupEntriesPanel extends DIYToolSubTabModuleFace {

	public RemoveDupEntriesPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.table.file", "",
				"Input data file, usually the tsv file. Necessary");
		mapProducer.addKeyValueEntryBean("target.columns", "",
				"input one column: col1. Or multiple columns: col1;col2;col3 . Necessary");

		mapProducer.addKeyValueEntryBean("output.file", "", "Output file. Necessary");
	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputFile = quickParseString("input.table.file");
		String targetColumns = quickParseString("target.columns");
		String[] dim1 = EGPSStringUtil.split(targetColumns, ';');
		String outputFilePath = quickParseString("output.file");

		Stopwatch stopwatch = Stopwatch.createStarted();

		List<String> outputs = new LinkedList<>();

		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(inputFile);

		TableOrganizer tableOrganizer = new TableOrganizer();
		List<List<String>> countRowEntries = tableOrganizer.removeDuplicatedEnties(asKey2ListMap, Arrays.asList(dim1));
		tableOrganizer.writeToFile(countRowEntries, outputFilePath);

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);
	}

	protected void outputResults(boolean isBinaryOutput, String[] dim1, String outputFilePath,
			List<List<String>> countRowEntries) throws IOException {
		int lengthOfDim1 = dim1.length;

		try (BufferedWriter br = new BufferedWriter(new FileWriter(outputFilePath))) {
			Iterator<List<String>> iterator = countRowEntries.iterator();
			List<String> firstLine = iterator.next();
			String firstJoinedLine = String.join("\t", firstLine);
			br.write(firstJoinedLine);
			br.write("\n");

			while (iterator.hasNext()) {

				List<String> next = iterator.next();

				Iterator<String> iterator2 = next.iterator();

				for (int i = 0; i < lengthOfDim1; i++) {
					String next2 = iterator2.next();
					if (i > 0) {
						br.write("\t");
					}
					br.write(next2);
				}
				while (iterator2.hasNext()) {
					String next2 = iterator2.next();
					br.write("\t");
					if (isBinaryOutput) {
						if (Objects.equal(next2, "0")) {

						} else {
							next2 = "1";
						}
					}
					br.write(next2);
				}
				br.write("\n");
			}

		}
	}

	/**
	 * <pre>
	 * 我有一个表格类型的数据，存储的结构是 一个 Map<String, List<String>> asKey2ListMap 
	 * Key是列名，Value是一个列表，Value都是等长的。
	 * 我的目的是将这个数据组织成一个二维表格，dim1是一个List<String> dim1，它是其中的若干个Key；dim2是一个Key，叫做dim2。
	 * 内容是count，也就是说满足行是dim1列是dim2的数量。也就是说，你希望创建一个统计表，表格中的每个单元格表示特定 dim1 和 dim2 组合出现的次数。
	 * 请你帮我写这个方法 organize( Map<String, List<String>> asKey2ListMap, List<String> dim1, String dim2)
	 * </pre>
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<String> perform() throws Exception {

		return Lists.newArrayList();

	}

}
