package module.fastadumper.extractpartial;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Objects;

import utils.string.EGPSStringUtil;
import utils.EGPSUtil;

public class CLI {

	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			System.err.println(
					"Usage: " + cliUtilityName + " fasta.file input.entries.tsv");
			System.err.println("The input.entries.tsv contents are tab key separated:");
			System.err.println("seq1_name");
			System.err.println("seq2_name\tothers\t#only first included.");
			System.err.println(
					"Usage: " + cliUtilityName + " fasta.file input.entries.tsv -k");
			System.err.println(
					"This only get one sequence on entry, if you want to get all sequence match the entry. keep the -k option");
			return;
		}


		List<String> lines = FileUtils.readLines(new File(args[1]), StandardCharsets.UTF_8);

		List<String> searchStrings = new ArrayList<>();

		for (String string : lines) {
			String[] split = EGPSStringUtil.split(string, '\t');
			searchStrings.add(split[0]);
		}

		ContinuousFastaExtractor continuousFastaExtractor = new ContinuousFastaExtractor();
		continuousFastaExtractor.setInputPath(args[0]);
		continuousFastaExtractor.setTargetStringSets(searchStrings);

		if (args.length == 2 && Objects.equal(args[2], "-k")) {
			continuousFastaExtractor.setKeepAllSearchStrings(true);
		}

		continuousFastaExtractor.setOutputPath("output.results.fa");

		continuousFastaExtractor.doIt();

		List<String> outputList4console = continuousFastaExtractor.outputList4console;
		for (String string : outputList4console) {
			System.out.println(string);
		}

	}

}
