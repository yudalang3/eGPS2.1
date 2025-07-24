/**
 * 
 */
package module.tablecuration.gui;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import utils.string.EGPSStringUtil;
import tsv.io.TSVReader;
import tsv.io.TSVWriter;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class ExtractTargetRowsPanel extends DIYToolSubTabModuleFace {

	public ExtractTargetRowsPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("@", "Table-like curation (Extract target records)", "");

		mapProducer.addKeyValueEntryBean("input.table.file", "",
				"Input data file, usually the tsv file. Necessary");
		mapProducer.addKeyValueEntryBean("target.colum", "", "input one column: col1. Necessary");
		mapProducer.addKeyValueEntryBean("entries", "",
				"input target entries, user can directly input or paste entries. Or input one file path. Necessary\n# entries=file/to/path\n# entries = \n# a\n# b\n# c");

		mapProducer.addKeyValueEntryBean("output.file", "", "Output file. Necessary");
	}

	@Override
	protected boolean whetherExecuteWithString2ListMap() {
		return true;
	}

	@Override
	protected void executeWithList(Map<String, LinkedList<String>> para) throws Exception {
		String inputTableFile = null;
		String targetColumnName = null;
		List<String> entries = null;
		String outputFile = null;

		LinkedList<String> list = para.get("$input.table.file");
		String string = getStringAfterEqualStr(list.get(0));
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $input.table.file ");
		} else {
			inputTableFile = string;
		}

		list = para.get("$target.colum");
		string = getStringAfterEqualStr(list.get(0));
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $target.colum ");
		} else {
			targetColumnName = string;
		}

		list = para.get("$output.file");
		string = getStringAfterEqualStr(list.get(0));
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $output.file ");
		} else {
			outputFile = string;
		}

		list = para.get("$entries");
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("Plese input the $entries ");
		} else {
			int size = list.size();
			if (size > 1) {
				entries = list.subList(1, size);
			} else {
				String filePath = getStringAfterEqualStr(list.get(0));
				File file = new File(filePath);
				if (!file.exists()) {
					throw new IllegalArgumentException(
							"Plese input file of the $entries NOT exists: ".concat(filePath));
				}
				entries = FileUtils.readLines(file, StandardCharsets.UTF_8);
			}

		}

		Stopwatch stopwatch = Stopwatch.createStarted();

		List<String> outputs = new LinkedList<>();

		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(inputTableFile);
		int totalSize = getRowSize(asKey2ListMap);

		TableOrganizer tableOrganizer = new TableOrganizer();
		Map<String, List<String>> ret = tableOrganizer.extractTargetRecords(asKey2ListMap, targetColumnName,
				entries);

		int size = getRowSize(ret);
		outputs.add("Total records size in table is : " + totalSize);
		String format = EGPSStringUtil.format("Records need to extract is {}, extracted record size is {}",
				entries.size(), size);

		outputs.add(format);
		if (entries.size() > size) {
			List<String> extracted = ret.get(targetColumnName);
			HashSet<String> hashSet = new HashSet<>(entries);
			hashSet.removeAll(extracted);
			outputs.add("Following entries not found:");
			for (String string2 : hashSet) {
				outputs.add(string2);
			}
		}



		TSVWriter.write(ret, outputFile);

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);

	}

	private int getRowSize(Map<String, List<String>> ret) {
		Entry<String, List<String>> firstEntry = ret.entrySet().iterator().next();
		int size = firstEntry.getValue().size();
		return size;
	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
	}


}
