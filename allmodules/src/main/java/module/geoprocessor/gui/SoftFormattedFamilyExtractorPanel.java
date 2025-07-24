/**
 * 
 */
package module.geoprocessor.gui;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class SoftFormattedFamilyExtractorPanel extends DIYToolSubTabModuleFace {

	public SoftFormattedFamilyExtractorPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.txt.file", "",
				"Input data file, plain text file or compressed files are allowed. Necessary");
		mapProducer.addKeyValueEntryBean("extract.tag.names", "!Sample_geo_accession;!Sample_title",
				"Seperate with ; character.");

		mapProducer.addKeyValueEntryBean("output.file", "", "Output file. Necessary");
	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputFile = quickParseString("input.txt.file");
		String outputFilePath = quickParseString("output.file");
		String tagNames = quickParseString("extract.tag.names");
		Stopwatch stopwatch = Stopwatch.createStarted();

		String[] splits = EGPSStringUtil.split(tagNames, ';');

		List<Pair<String, List<String>>> key2ListMap = Lists.newArrayList();
		for (String string : splits) {
			Pair<String, List<String>> of = Pair.of(string, Lists.newArrayList());
			key2ListMap.add(of);
		}

		List<String> outputs = new LinkedList<>();

		List<String> contents = EGPSFileUtil.getContentsFromOneFileMaybeCompressed(inputFile);

		for (String string : contents) {
			for (Pair<String, List<String>> entry : key2ListMap) {
				if (string.startsWith(entry.getKey())) {
					int indexOf = string.indexOf('=');
					String substring = string.substring(indexOf + 1);
					entry.getValue().add(substring.trim());
				}
			}
		}

		List<String> targetContents = new LinkedList<>();

		int size = key2ListMap.get(0).getValue().size();

		StringJoiner stringJoiner = new StringJoiner("\t");
		for (Pair<String, List<String>> entry : key2ListMap) {
			stringJoiner.add(entry.getKey());
		}
		targetContents.add(stringJoiner.toString());

		for (int i = 0; i < size; i++) {
			StringJoiner sJ = new StringJoiner("\t");
			for (Pair<String, List<String>> entry : key2ListMap) {
				String string = entry.getValue().get(i);
				sJ.add(string);
			}
			targetContents.add(sJ.toString());
		}


		FileUtils.writeLines(new File(outputFilePath), targetContents);


		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);
	}


}
