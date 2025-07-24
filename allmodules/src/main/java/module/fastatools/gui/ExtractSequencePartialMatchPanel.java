/**
 * 
 */
package module.fastatools.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Stopwatch;

import tsv.io.KitTable;
import tsv.io.TSVReader;
import egps2.frame.ComputationalModuleFace;
import module.fastadumper.extractpartial.ContinuousFastaExtractor;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class ExtractSequencePartialMatchPanel extends DIYToolSubTabModuleFace {

	public ExtractSequencePartialMatchPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("fasta.file.path", "", "Input fasta file path. Necessary");
		mapProducer.addKeyValueEntryBean("search.enties.path", "", "Input search file path, tsv format. Necessary");
		mapProducer.addKeyValueEntryBean("column.index", "1", "The column to extract sequence, default is 1.");
		mapProducer.addKeyValueEntryBean("keep.repeat.search", "F",
				"This only get one sequence on entry, if you want to get all sequence match the entry, set T.");
		mapProducer.addKeyValueEntryBean("output.file", "", "Output fasta file path. Necessary");
	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputDir = quickParseString("fasta.file.path");
		String searchEntryPath = quickParseString("search.enties.path");
		int columnIndex = quickParseInt("column.index");
		boolean keptRepeatSearch = quickParseBoolean("keep.repeat.search");
		String outputFastaPath = quickParseString("output.file");

		Stopwatch stopwatch = Stopwatch.createStarted();
		List<String> outputs = run(inputDir, searchEntryPath, columnIndex - 1, keptRepeatSearch, outputFastaPath);

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();

		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);
	}

	private List<String> run(String inputDir, String searchEntryPath, int columnIndex, boolean keptRepeatSearch,
			String outputFastaPath) throws Exception {

		KitTable tsvTextFile = TSVReader.readTsvTextFile(searchEntryPath, false);
		List<List<String>> contents = tsvTextFile.getContents();

		List<String> targets = new ArrayList<>();
		for (List<String> string : contents) {
			String string2 = string.get(columnIndex);
			targets.add(string2);
		}
		Map<String, Integer> cardinalityMap = CollectionUtils.getCardinalityMap(targets);

		ContinuousFastaExtractor fastaConcatenator = new ContinuousFastaExtractor();
		fastaConcatenator.setInputPath(inputDir);
		fastaConcatenator.setOutputPath(outputFastaPath);
		fastaConcatenator.setKeepAllSearchStrings(keptRepeatSearch);
		fastaConcatenator.setTargetStringSets(targets);
		fastaConcatenator.setTargetStringContents(tsvTextFile.getOriginalLines());

		fastaConcatenator.doIt();

		List<String> outputList4console = fastaConcatenator.getOutputList4console();


		Set<String> duplicates = cardinalityMap.entrySet().stream().filter(entry -> entry.getValue() > 1)
				.map(Map.Entry::getKey).collect(Collectors.toSet());
		if (!duplicates.isEmpty()) {
			outputList4console.add("Note: following elements has duplications.");
			outputList4console.addAll(duplicates);
		}

		return outputList4console;
	}


}
