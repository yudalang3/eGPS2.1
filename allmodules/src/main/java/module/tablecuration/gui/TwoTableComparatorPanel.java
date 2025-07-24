/**
 * 
 */
package module.tablecuration.gui;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;

import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import utils.string.EGPSStringUtil;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class TwoTableComparatorPanel extends DockableTabModuleFaceOfVoice {

	public TwoTableComparatorPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.table1.file", "",
				"Input data file, usually the tsv file. As the reference. Necessary");
		mapProducer.addKeyValueEntryBean("table1.exclude.columns", "",
				"Exclude columns for comparison. Default none, possible values are a or a;b;c");
		mapProducer.addKeyValueEntryBean("table1.target.column", "", "Target column for key. If none use the first column");
		mapProducer.addKeyValueEntryBean("input.table2.file", "",
				"Input data file, usually the tsv file. As the search query. Necessary");
		mapProducer.addKeyValueEntryBean("table2.exclude.columns", "",
				"Exclude columns for comparison. Default none, possible values are a or a;b;c");
		mapProducer.addKeyValueEntryBean("table2.target.column", "", "Target column for key.  If none use the first column");
		mapProducer.addKeyValueEntryBean("ignore.null", "T", "Whether ignore the null with in table1");

	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {

		String inputFile1 = o.getSimplifiedString("input.table1.file");
		String table1TargetCol = o.getSimplifiedStringWithDefault("table1.target.column");
		String inputFile2 = o.getSimplifiedString("input.table2.file");
		String table2TargetCol = o.getSimplifiedStringWithDefault("table2.target.column");
		boolean ignoreNull = o.getSimplifiedBool("ignore.null");

		String table1Exclude = o.getSimplifiedStringWithDefault("$table1.exclude.columns");
		String[] table1ExcludeEntries = null;
		if (table1Exclude != null && !table1Exclude.isEmpty()) {
			table1ExcludeEntries = EGPSStringUtil.split(table1Exclude, ';');
		}
		String table2Exclude = o.getSimplifiedStringWithDefault("$table2.exclude.columns");
		String[] table2ExcludeEntries = null;
		if (table2Exclude != null && !table2Exclude.isEmpty()) {
			table2ExcludeEntries = EGPSStringUtil.split(table2Exclude, ';');
		}

		Stopwatch stopwatch = Stopwatch.createStarted();
		List<String> outputs = new LinkedList<>();
		setText4Console(outputs);

		perform(inputFile1, table1TargetCol, table1ExcludeEntries, inputFile2, table2TargetCol, table2ExcludeEntries,
				ignoreNull);

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();

		appendText2Console("Take time of  " + (millis) + " milliseconds to execute.");
	}

	private void perform(String inputFile1, String table1TargetCol, String[] table1ExcludeEntries, String inputFile2,
			String table2TargetCol, String[] table2ExcludeEntries, boolean ignoreNull) throws IOException {

		Map<String, List<String>> table1 = TSVReader.readAsKey2ListMap(inputFile1);
		if (table1ExcludeEntries != null) {
			for (String string : table1ExcludeEntries) {
				table1.remove(string);
			}
		}

		Map<String, List<String>> table2 = TSVReader.readAsKey2ListMap(inputFile2);
		if (table2ExcludeEntries != null) {
			for (String string : table2ExcludeEntries) {
				table2.remove(string);
			}
		}
		if (Strings.isNullOrEmpty(table1TargetCol)){
			table1TargetCol = table1.keySet().iterator().next();
		}
		if (Strings.isNullOrEmpty(table2TargetCol)){
			table2TargetCol = table2.keySet().iterator().next();
		}

		List<String> table1KeyCol = table1.remove(table1TargetCol);
		if (table1KeyCol == null) {
			throw new InputMismatchException(table1TargetCol.concat(" NOT exists in the table 1."));
		}
		List<String> table2KeyCol = table2.remove(table2TargetCol);
		if (table2KeyCol == null) {
			throw new InputMismatchException(table1TargetCol.concat(" NOT exists in the table 2."));
		}

		Builder<String, String, String> builder = ImmutableTable.builder();
		for (Entry<String, List<String>> entry : table1.entrySet()) {
			String key2 = entry.getKey();
			Iterator<String> iterator = entry.getValue().iterator();
			for (String key1 : table1KeyCol) {
				builder.put(key1, key2, iterator.next());
			}
		}

		ImmutableTable<String, String, String> ref = builder.build();

		boolean allSame = true;
		for (Entry<String, List<String>> entry : table2.entrySet()) {
			String key2 = entry.getKey();
			Iterator<String> iterator = entry.getValue().iterator();
			for (String key1 : table2KeyCol) {
				String value2 = iterator.next();
				String value1 = ref.get(key1, key2);
				
				boolean diff = !Objects.equal(value1, value2);
				if (diff) {
					String ss = key1 + "\t" + key2 + " in table1 and table2 are: " + value1 + "\t" + value2 + "\n";
					if (ignoreNull) {
						if (value1 != null) {
							appendText2Console(ss);
							allSame = false;
						}
					}else {
						appendText2Console(ss);
						allSame = false;
					}
				}


			}
		}

		if (allSame){
			appendText2Console("All the same.\n");
		}

	}

	@Override
	public String getShortDescription() {
		return "";
	}

	@Override
	public String getTabName() {
		return "";
	}
}
