package module.brutemapping;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import utils.EGPSUtil;

public class CLI {

	public static void main(String[] args) {
		Options options = new Options();

		Option queryFasta = Option.builder("q").longOpt("queryFastaPath").desc("Set query FASTA file path").hasArg()
				.required().build();

		Option genomeFasta = Option.builder("g").longOpt("genomeFastaPath").desc("Set genome FASTA file path").hasArg()
				.required().build();

		Option keySequence = Option.builder("k").longOpt("onlyIncludeKeySeqeunce")
				.desc("Specify key sequence to include").hasArg().build();

		Option excludeSequence = Option.builder("e").longOpt("excludeSequenceName")
				.desc("Specify sequence name to exclude").hasArg().build();

		Option outputBed = Option.builder("o").longOpt("outputBedPath").desc("Set output BED file path").hasArg()
				.build();

		Option outputTotalCount = Option.builder().longOpt("outputTotal").desc("Set output file path for total counts")
				.hasArg().build();

		Option reverseComplement = Option.builder("r").longOpt("needReverseComplement")
				.desc("Set if reverse complement is needed").build();

		options.addOption(queryFasta);
		options.addOption(genomeFasta);
		options.addOption(keySequence);
		options.addOption(excludeSequence);
		options.addOption(outputBed);
		options.addOption(reverseComplement);
		options.addOption(outputTotalCount);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);

			ImportDataBean importData = new ImportDataBean();
			importData.setQueryFastaPath(cmd.getOptionValue("queryFastaPath"));
			importData.setGenomeFastaPath(cmd.getOptionValue("genomeFastaPath"));
			importData.setOnlyIncludeKeySeqeunce(cmd.getOptionValue("onlyIncludeKeySeqeunce"));
			importData.setExcludeSequenceName(cmd.getOptionValue("excludeSequenceName"));
			importData.setOutputBedPath(cmd.getOptionValue("outputBedPath"));
			importData.setOutputTotalCountFilePath(cmd.getOptionValue("outputTotal"));
			importData.setNeedReverseComplement(cmd.hasOption("needReverseComplement"));

			// Perform operations using importData object as needed
			// Perform operations using importData object as needed
			VOICM4BruteMapping voicm4BruteMapping = new VOICM4BruteMapping(null);
			voicm4BruteMapping.importDataInformation = importData;

			voicm4BruteMapping.processNext();
		} catch (Exception e) {
			e.printStackTrace();
			String cliUtilityName = EGPSUtil.getCLIUtilityName(CLI.class);
			formatter.printHelp(cliUtilityName, options);
		}
	}
}
