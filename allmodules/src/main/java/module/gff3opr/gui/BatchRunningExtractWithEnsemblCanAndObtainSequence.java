package module.gff3opr.gui;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Stopwatch;

import utils.string.EGPSStringUtil;
import tsv.io.TSVReader;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.ComputationalModuleInjector;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.SubTabModule4VoiceInjector;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.builtin.modules.voice.fastmodvoice.SubTabModuleRunner;

public class BatchRunningExtractWithEnsemblCanAndObtainSequence
		implements SubTabModuleRunner, ComputationalModuleInjector, SubTabModule4VoiceInjector {
	private String inputTsvFile;
	private Optional<String> judgePassEntry;
	private String gff3fileHeader;
	private String genomefileHeader;
	private String outFastaFileHeader;
	private String judgePassEntryHeader;
	private String gff3fileDir;
	private String genomefileDir;
	private String outputfileDir;

	private int upstreamBasePair;
	private int downstreamBasePair;
	private String featureEndDirective;
	private String nameSeperator;
	private boolean ignoreStrand;
	private int[] outputNameCompositionIndexes;

	private ComputationalModuleFace cmf;
	private String keepField2Contains;
	private String keepField4Contains;
	private DockableTabModuleFaceOfVoice sFace;

	@Override
	public String getShortDescription() {
		return "The gene transcript use the Ensembl Cannonical";
	}

	@Override
	public String getTabName() {
		return "4. Batch running extract gene upstream and obtain sequences";
	}

	@Override
	public void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {

		mapProducer.addKeyValueEntryBean("input.file.tsv", "",
				"Input the file path of the gff3, genome fasta file and output fasta file, in tsv format. Common compressed format xz,gz,zip is supported.\n# gff1\tgenome1\tout.fa");
		mapProducer.addKeyValueEntryBean("tsv.file.header", "GFF3;GenomeFasta;~",
				"The header of the column name. Must has three column names represent the gff3 file paths, genome file paths and out fasta file paths.\n# GFF3;GenomeFasta;outName is the concrete name, ~ means auto get results with gff3 file and output in the gff3 location.");
		mapProducer.addKeyValueEntryBean("gff3File.dir", "",
				"The dir of the gff3 files, default none. The value will be concat as the prefix of the gff3 file path");
		mapProducer.addKeyValueEntryBean("genome.dir", "",
				"The dir of the genome files, default none. The value will be concat as the prefix of the genome file path");
		mapProducer.addKeyValueEntryBean("output.fas.dir", "",
				"The dir of the output fasta files, default none. The value will be concat as the prefix of the fasta file path");

		mapProducer.addKeyValueEntryBean("upstream.base.pair", "2000",
				"The base pair upstream the TSS (Transcription start site)");
		mapProducer.addKeyValueEntryBean("downstream.base.pair", "50",
				"The base pair downstream the TSS (Transcription start site)");

		mapProducer.addKeyValueEntryBean("keep.temp.bed", "F",
				"Whether keep the temporary bed file, the file name same as the gff3 file. ");
		mapProducer.addKeyValueEntryBean("output.fasta.name", "1,2,3",
				"The output fasta name composition. If input 1,2,3, output chr:startPostion:endPosition; input 2 output startPosition");
		// Advanced parameters
		mapProducer.addKeyValueEntryBean("^", "", "");
		mapProducer.addKeyValueEntryBean("input.entry.pass", "",
				"In some cases, users need to keep some entries run and some entries not run. The format is headerName=string\n# for example:commonName=!NotEqual means commonName column not equal to ‘NotEqual’ should run.");
		mapProducer.addKeyValueEntryBean("output.fasta.name.seperator", ":",
				"For example, if you input :, the output name will be chr:startPostion:endPosition.");
		mapProducer.addKeyValueEntryBean("only.extract.positive.strand", "F",
				"Optional. If T, the program will not consider the strand column in the bed file and only extract the positive strand.");
		mapProducer.addKeyValueEntryBean("feature.end.directive", "###",
				"This directive (three # signs in a row, officail gff3 file) indicates that all forward references to feature IDs that have been seen to this point have been resolved. ");

		mapProducer.addKeyValueEntryBean("%",
				"Filter some sequences in the genome fasta file. Very useful for the fasta file reading.", "");
		mapProducer.addKeyValueEntryBean("keep.field2.contains", "",
				"Only the field 2 contains the value will processed, default is no filtering string.");
		mapProducer.addKeyValueEntryBean("keep.field4.contains", "",
				"Only the field 4 equal to this value will processed, default is no filtering string.\n # Values could be REF, HAP");
	}

	@Override
	public List<String> execute(OrganizedParameterGetter o) throws Exception {
		Stopwatch stopwatch = Stopwatch.createStarted();

		inputTsvFile = o.getSimplifiedString("input.file.tsv");

		boolean simplifiedValueExist = o.isSimplifiedValueExist("input.entry.pass");
		if (simplifiedValueExist) {
			String simplifiedString = o.getSimplifiedString("input.entry.pass");
			String[] split = EGPSStringUtil.split(simplifiedString, '=');
			if (split.length != 2) {
				throw new InputMismatchException("The parameter $input.entry.pass format is not correct.");
			}
			judgePassEntryHeader = split[0];
			judgePassEntry = Optional.of(split[1]);
		} else {
			judgePassEntry = Optional.empty();
		}

		String tsvFileHeader = o.getSimplifiedString("tsv.file.header");
		String[] splits = EGPSStringUtil.split(tsvFileHeader, ';');
		if (splits.length != 3) {
			throw new InputMismatchException("The parameter tsv.file.header should has three elements!");
		}
		gff3fileHeader = splits[0];
		genomefileHeader = splits[1];
		outFastaFileHeader = splits[2];

		gff3fileDir = o.getSimplifiedStringWithDefault("gff3File.dir");
		genomefileDir = o.getSimplifiedStringWithDefault("genome.dir");
		outputfileDir = o.getSimplifiedStringWithDefault("output.fas.dir");

		upstreamBasePair = o.getSimplifiedInt("upstream.base.pair");
		downstreamBasePair = o.getSimplifiedInt("downstream.base.pair");
		featureEndDirective = o.getSimplifiedString("feature.end.directive");

		// extract files
		String outputIndexes = o.getSimplifiedString("output.fasta.name");
		nameSeperator = o.getSimplifiedString("output.fasta.name.seperator");

		ignoreStrand = false;
		simplifiedValueExist = o.isSimplifiedValueExist("only.extract.positive.strand");
		if (simplifiedValueExist) {
			ignoreStrand = o.getSimplifiedBool("only.extract.positive.strand");
		}

		String[] split = EGPSStringUtil.split(outputIndexes, ',');
		outputNameCompositionIndexes = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			int index = Integer.parseInt(split[i]);
			// one-based value to zero-based value
			outputNameCompositionIndexes[i] = index - 1;
		}

		keepField2Contains = o.getSimplifiedStringWithDefault("keep.field2.contains");
		keepField4Contains = o.getSimplifiedStringWithDefault("keep.field4.contains");

		List<String> outputs = perform();

		stopwatch.stop();
		outputs.add("Elapsed time: ".concat(stopwatch.toString()));
		return outputs;
	}

	private List<String> perform() throws Exception {

		List<String> outputs = new LinkedList<>();

		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(inputTsvFile);

		List<String> gff3FileList = asKey2ListMap.get(gff3fileHeader);
		List<String> genomeFileList = asKey2ListMap.get(genomefileHeader);
		List<String> outFileList = null;
		if (Objects.equals(outFastaFileHeader, "~")) {

		} else {
			outFileList = asKey2ListMap.get(outFastaFileHeader);
		}

		int size = gff3FileList.size();

		List<String[]> runningEntries = Lists.newArrayList();
		if (judgePassEntry.isPresent()) {
			List<String> judgeList = asKey2ListMap.get(judgePassEntryHeader);
			String judgeStr = judgePassEntry.get();
			for (int i = 0; i < size; i++) {
				String toBeJudgedValue = judgeList.get(i);
				if (judgeStr.startsWith("!")) {
					// should remove the ! symbol
					boolean ifContains = toBeJudgedValue.contains(judgeStr.substring(1));
					if (!ifContains) {
						// !开头的话，要 不包含的才行
						runningEntries.add(wrapperEntry(gff3FileList, genomeFileList, outFileList, i));
					}
				} else {
					boolean ifContains = toBeJudgedValue.contains(judgeStr);
					if (ifContains) {
						// 不是!开头的参数需要包含字符串才行
						runningEntries.add(wrapperEntry(gff3FileList, genomeFileList, outFileList, i));
					}
				}

			}
		} else {
			for (int i = 0; i < size; i++) {
				runningEntries.add(wrapperEntry(gff3FileList, genomeFileList, outFileList, i));
			}
		}

		ExtractUpstreamOfGeneByEnsemblCan firstCalculator = new ExtractUpstreamOfGeneByEnsemblCan();
		ObtainSequenceFromBedFile secondCalculator = new ObtainSequenceFromBedFile();

		int size2 = runningEntries.size();
		int index = 1;
		for (String[] is : runningEntries) {
			UnifiedAccessPoint.promoteAtBottom(cmf,
					EGPSStringUtil.format("Progress {} / {}, current is {}", index, size2, is[0]));
			processOneEntry(is, firstCalculator, secondCalculator);
			index++;
		}
		outputs.add("Processed batch file size is: " + runningEntries.size());
		return outputs;
	}

	private void processOneEntry(String[] is, ExtractUpstreamOfGeneByEnsemblCan firstCalculator,
			ObtainSequenceFromBedFile secondCalculator) throws Exception {

		int INDEX_GFF3 = 0;
		int INDEX_TEMP_BED = 3;
		int INDEX_GENOME_FASTA = 1;
		int INDEX_OUTPUT_FILE = 2;
		String bedFile = is[INDEX_TEMP_BED];
		firstCalculator.perform(is[INDEX_GFF3], bedFile, upstreamBasePair, downstreamBasePair, featureEndDirective);

		List<String> outputs = secondCalculator.perform(bedFile, is[INDEX_GENOME_FASTA], is[INDEX_OUTPUT_FILE],
				outputNameCompositionIndexes, nameSeperator, ignoreStrand, keepField2Contains, keepField4Contains);

		if (sFace == null) {
			for (String string : outputs) {
				System.out.println(string);
			}
		} else {
			sFace.setText4Console(outputs);
		}
	}

	protected String[] wrapperEntry(List<String> gff3FileList, List<String> genomeFileList, List<String> outFileList,
			int i) {
		String string = null;
		if (gff3fileDir.isEmpty()) {
			string = gff3FileList.get(i);
		} else {
			string = gff3fileDir + "/" + gff3FileList.get(i);
		}
		String string2 = null;
		if (genomefileDir.isEmpty()) {
			string2 = genomeFileList.get(i);
		} else {
			string2 = genomefileDir + "/" + genomeFileList.get(i);
		}

		String string3 = null;
		if (outFileList == null) {
			String baseName = FilenameUtils.getBaseName(string);
			string3 = outputfileDir + "/" + baseName + ".extracted.fa";
			if (outputfileDir.isEmpty()) {
				string3 = baseName + ".extracted.fa";
			} else {
				string3 = outputfileDir + "/" + baseName + ".extracted.fa";
			}
		} else {
			if (outputfileDir.isEmpty()) {
				string3 = outFileList.get(i);
			} else {
				string3 = outputfileDir + "/" + outFileList.get(i);
			}
		}

		String baseName = FilenameUtils.getBaseName(string);
		String string4 = outputfileDir + "/" + baseName + ".temp.bed";
		if (outputfileDir.isEmpty()) {
			string4 = baseName + ".temp.bed";
		} else {
			string4 = outputfileDir + "/" + baseName + ".temp.bed";
		}

		return new String[] { string, string2, string3, string4 };
	}

	@Override
	public void injectComputationalModuleFace(ComputationalModuleFace cmf) {
		this.cmf = cmf;
	}

	@Override
	public void injectSubTabModuleFace(DockableTabModuleFaceOfVoice sFace) {
		this.sFace = sFace;

	}

}
