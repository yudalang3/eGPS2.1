/**
 * 
 */
package module.tablecuration.gui;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import utils.string.EGPSStringUtil;
import tsv.io.KitTable;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class FilteringRowsAccording2AnotherTablePanel extends DIYToolSubTabModuleFace {

	public FilteringRowsAccording2AnotherTablePanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	public String getTabName() {
		return new String("Filtering Rows According To Another table");
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("need.filtering.table", "",
				"Input data file, usually the tsv file. As the reference. Necessary");
		mapProducer.addKeyValueEntryBean("table1.target.column", "", "Target column for key.");
		mapProducer.addKeyValueEntryBean("reference.table", "",
				"Input data file, usually the tsv file. As the search query. Need to be the bigger one. Necessary");
		mapProducer.addKeyValueEntryBean("table2.target.column", "", "Target column for key.");
		mapProducer.addKeyValueEntryBean("partial.match", "F",
				"The entry is only contains. False means two string need to be equal; True means string in table1 contains table2.");
		mapProducer.addKeyValueEntryBean("output.file.path", "", "Output file path, tsv usually, Necessary.");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputFile1 = quickParseString("need.filtering.table");
		String table1TargetCol = quickParseString("table1.target.column");
		String inputFile2 = quickParseString("reference.table");
		String table2TargetCol = quickParseString("table2.target.column");
		String outputFilePath = quickParseString("output.file.path");

		boolean partialMatch = quickParseBoolean("partial.match");

		Stopwatch stopwatch = Stopwatch.createStarted();
		List<String> outputs = perform(inputFile1, table1TargetCol, inputFile2, table2TargetCol, partialMatch,
				outputFilePath);

		stopwatch.stop();
		setText4Console(outputs);
		long millis = stopwatch.elapsed().toMillis();

		appendText2Console("Take time of  " + (millis) + " milliseconds to execute.");
	}

	private List<String> perform(String inputFile1, String table1TargetCol, String inputFile2, String table2TargetCol,
			boolean partialMatch, String outputFilePath) throws IOException {
		List<String> ret = new LinkedList<>();

		Map<String, List<String>> table1 = TSVReader.readAsKey2ListMap(inputFile1);
		Map<String, List<String>> table2 = TSVReader.readAsKey2ListMap(inputFile2);

		List<String> tab1Column = table1.get(table1TargetCol);
		List<String> tab2Column = table2.get(table2TargetCol);


		// help me write a function: input two List and boolean, output the indexes need
		// to retain for the first list(tab1Column)
		// the partialMatch boolean means: entries in tabl1Column only need to partial
		// match the tab2Column. Finally, document the function
		List<Integer> retainedIndexes = getRetainedIndex(tab1Column, tab2Column, partialMatch, ret);

		ret.add(EGPSStringUtil.format("The reference table has {} rows", tab2Column.size()));
		ret.add(EGPSStringUtil.format("The table1 column has {} rows, and {} rows will be retained.", tab1Column.size(),
				retainedIndexes.size()));

		KitTable tsvTextFile = TSVReader.readTsvTextFile(inputFile1);
		List<List<String>> contents = tsvTextFile.getContents();

		List<String> outputs = new LinkedList<>();
		String headerLine = String.join("\t", tsvTextFile.getHeaderNames());
		outputs.add(headerLine);

		for (Integer index : retainedIndexes) {
			outputs.add(String.join("\t", contents.get(index)));
		}
		FileUtils.writeLines(new File(outputFilePath), outputs);

		return ret;
	}

	/**
	 * Returns the indexes of elements in the first list (tab1Column) that need to
	 * be retained. If partialMatch is true, entries in tab1Column only need to
	 * partially match entries in tab2Column. Otherwise, a full match is required.
	 *
	 * @param tab1Column   the first list of strings
	 * @param tab2Column   the second list of strings
	 * @param partialMatch if true, only partial matches are required; otherwise,
	 *                     full matches are required
	 * @param outputs
	 * @return a list of indexes from tab1Column that should be retained
	 */
	private static List<Integer> getRetainedIndex(List<String> tab1Column, List<String> tab2Column,
			boolean partialMatch, List<String> outputs) {

		HashSet<String> set2NotMatched = new HashSet<>(tab2Column);
		List<Integer> retainedIndexes = Lists.newArrayList();

		for (int i = 0; i < tab1Column.size(); i++) {
			String tab1Entry = tab1Column.get(i);
			boolean matchFound = false;

			for (String tab2Entry : tab2Column) {
				if (partialMatch) {
					if (tab1Entry.contains(tab2Entry)) {
						matchFound = true;
						set2NotMatched.remove(tab2Entry);
						break;
					}
				} else {
					if (Objects.equal(tab1Entry, tab2Entry)) {
						matchFound = true;
						set2NotMatched.remove(tab2Entry);
						break;
					}
				}
			}

			if (matchFound) {
				retainedIndexes.add(i);
			}
		}

		for (String str : set2NotMatched) {
			outputs.add(str.concat("\t in ref. is not matched."));
		}

		return retainedIndexes;
	}

}
