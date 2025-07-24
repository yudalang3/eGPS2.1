package module.fastatools.rename;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import utils.string.EGPSStringUtil;
import utils.EGPSUtil;

/**
 * <pre>
 * 请你帮我用JAVA的库 commons-cli，编写一个命令行的启动类。
 * 我有三个参数，第一个是 -i --input意义是 input fasta file path；
 * 第二个是 -o --output 意义是 output fasta file path；
 * 第三个是 -r --rules 意义是 sequence names transformation rules
 * 第四个是 -p --partial 意义是 whether partial match the string
 * 帮我写一个这样的命令行参数解析类。
 * 
 * </pre>
 */
public class CLI {
	public static void main(String[] args) {
		// Create the options
		Options options = new Options();

		// Add input file option
		Option input = new Option("i", "input", true, "input fasta file path");
		input.setRequired(true);
		options.addOption(input);

		// Add output file option
		Option output = new Option("o", "output", true, "output fasta file path");
		output.setRequired(true);
		options.addOption(output);

		// Add rules option
		Option rules = new Option("r", "rules", true, "sequence names transformation rules");
		rules.setRequired(true);
		options.addOption(rules);

		// Add partial option
		Option partial = new Option("k", "keep", false,
				"whether keep the total search rules for second match. E.g. your search string is seq. There are may have many seqence name contains seq string.");
		partial.setRequired(false);
		options.addOption(partial);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);

			String inputFilePath = cmd.getOptionValue("input");
			String outputFilePath = cmd.getOptionValue("output");
			String rulesValue = cmd.getOptionValue("rules");
			boolean isKeep = cmd.hasOption("keep");

			System.out.println("Input file: " + inputFilePath);
			System.out.println("Output file: " + outputFilePath);
			System.out.println("Rules: " + rulesValue);
			System.out.println("Keep search strings: " + isKeep);

			// Continue with your logic here
			ContinuousFastaRenamer continuousFastaRenamer = new ContinuousFastaRenamer();
			continuousFastaRenamer.setInputPath(inputFilePath);
			continuousFastaRenamer.setOutputPath(outputFilePath);
			continuousFastaRenamer.setKeepAllSearchStrings(isKeep);

			List<Pair<String, String>> inputRules = new ArrayList<>();
			List<String> lines = FileUtils.readLines(new File(rulesValue), StandardCharsets.UTF_8);
			for (String string : lines) {
				String[] strings = EGPSStringUtil.split(string, '\t');
				inputRules.add(Pair.of(strings[0], strings[1]));
			}

			continuousFastaRenamer.setTargetStringSets(inputRules);

			continuousFastaRenamer.doIt();

			List<String> outputList4console = continuousFastaRenamer.getOutputList4console();

			for (String string : outputList4console) {
				System.out.println(string);
			}


		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp(EGPSUtil.getCLIUtilityName(CLI.class), options);

			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
