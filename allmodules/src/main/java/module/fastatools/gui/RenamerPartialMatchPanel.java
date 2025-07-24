/**
 * 
 */
package module.fastatools.gui;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Stopwatch;

import utils.string.EGPSStringUtil;
import module.fastatools.rename.ContinuousFastaRenamer;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.SubTabModuleRunner;

public class RenamerPartialMatchPanel implements SubTabModuleRunner {

	@Override
	public String getShortDescription() {
		return "<html>The extracted sequence with partial match may have long name,Mbr> rename the sequence according to the user rules.";
	}

	@Override
	public String getTabName() {
		return "3. Rename Sequence Partial Match";
	}

	@Override
	public void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("fasta.file.path", "", "Input fasta file path. Necessary");
		mapProducer.addKeyValueEntryBean("search.enties.path", "", "Input search file path, tsv format. Necessary");
		mapProducer.addKeyValueEntryBean("search.index", "1",
				"The column to anchor sequence, i.e. which column to search for sequence.");
		mapProducer.addKeyValueEntryBean("rename.index", "2", "The column to change to sequence name.");
		mapProducer.addKeyValueEntryBean("output.file", "", "Output fasta file path. Necessary");
		mapProducer.addKeyValueEntryBean("keep.repeat.search", "F",
				"This only get one sequence on entry, if you want to get all sequence match the entry, set T.");
	}

	@Override
	public List<String> execute(OrganizedParameterGetter o) throws Exception {
		String inputDir = o.getSimplifiedString("fasta.file.path");
		String searchEntryPath = o.getSimplifiedString("search.enties.path");
		int searchColumnIndex = o.getSimplifiedInt("search.index");
		int targetColumnIndex = o.getSimplifiedInt("rename.index");
		String outputFastaPath = o.getSimplifiedString("output.file");
		boolean keptRepeatSearch = o.getSimplifiedBool("keep.repeat.search");

		Stopwatch stopwatch = Stopwatch.createStarted();

		List<String> outputs = perform(inputDir, searchEntryPath, searchColumnIndex - 1, targetColumnIndex - 1,
				outputFastaPath,
				keptRepeatSearch);

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		return outputs;
	}

	private List<String> perform(String inputFastaFile, String searchEntryPath, int searchColumnIndex,
			int targetColumnIndex, String outputFastaPath, boolean keptRepeatSearch) throws Exception {

		// Continue with your logic here
		ContinuousFastaRenamer continuousFastaRenamer = new ContinuousFastaRenamer();
		continuousFastaRenamer.setInputPath(inputFastaFile);
		continuousFastaRenamer.setOutputPath(outputFastaPath);
		continuousFastaRenamer.setKeepAllSearchStrings(keptRepeatSearch);

		List<Pair<String, String>> inputRules = new ArrayList<>();
		List<String> lines = FileUtils.readLines(new File(searchEntryPath), StandardCharsets.UTF_8);
		for (String string : lines) {
			String[] strings = EGPSStringUtil.split(string, '\t');
			inputRules.add(Pair.of(strings[searchColumnIndex], strings[targetColumnIndex]));
		}

		continuousFastaRenamer.setTargetStringSets(inputRules);

		continuousFastaRenamer.doIt();

		List<String> outputList4console = continuousFastaRenamer.getOutputList4console();

		return outputList4console;

	}
}
