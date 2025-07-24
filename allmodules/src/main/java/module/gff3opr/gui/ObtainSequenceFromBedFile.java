package module.gff3opr.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fasta.io.FastaReader;
import org.apache.logging.log4j.util.Strings;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;

import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;
import utils.string.StringCounter;
import module.ambigbse.DNAComplement;
import module.genome.EnsemblFastaName;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.SubTabModuleRunner;

public class ObtainSequenceFromBedFile implements SubTabModuleRunner {

	private final StringCounter stringCounter = new StringCounter();


	@Override
	public String getShortDescription() {
		return "Input fasta genome file and get the bed file.\nThe genome file is fasta format and name is designed by Ensembl.";
	}

	@Override
	public String getTabName() {
		return "3. Obtain sequences from bed file";
	}

	@Override
	public void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.bed.file", "",
				"Input the file path of the gff3 file. Common compressed format xz,gz,zip is supported.");
		mapProducer.addKeyValueEntryBean("output.fasta.name", "1,2,3",
				"The output fasta name composition. If input 1,2,3, output chr:startPostion:endPosition; input 2 output startPosition");
		mapProducer.addKeyValueEntryBean("genome.fasta.file", "",
				"Input the file path of the genome fasta file. Common compressed format xz,gz,zip is supported.");
		mapProducer.addKeyValueEntryBean("output.fasta.file", "", "output file path");
		mapProducer.addKeyValueEntryBean("^", "", "");
		mapProducer.addKeyValueEntryBean("output.fasta.name.seperator", ":",
				"For example, if you input :, the output name will be chr:startPostion:endPosition.");
		mapProducer.addKeyValueEntryBean("only.extract.positive.strand", "F",
				"Optional. If T, the program will not consider the strand column in the bed file and only extract the positive strand.");

		mapProducer.addKeyValueEntryBean("%",
				"Filter some sequences in the genome fasta file. Very useful for handling big fasta file.", "");
		mapProducer.addKeyValueEntryBean("keep.field2.contains", "",
				"Only the field 2 contians the value will processed, default is no filtering string.");
		mapProducer.addKeyValueEntryBean("keep.field4.contains", "",
				"Only the field 4 equal to this value will processed, default is no filtering string.\n # Values could be REF, HAP");

	}

	@Override
	public List<String> execute(OrganizedParameterGetter o) throws Exception {
		String bedFilePath = o.getSimplifiedString("input.bed.file");
		String genomeFilePath = o.getSimplifiedString("genome.fasta.file");
		String outputFastaFilePath = o.getSimplifiedString("output.fasta.file");
		String outputIndexes = o.getSimplifiedString("output.fasta.name");
		String nameSeperator = o.getSimplifiedString("output.fasta.name.seperator");

		boolean ignoreStrand = false;
		boolean simplifiedValueExist = o.isSimplifiedValueExist("only.extract.positive.strand");
		if (simplifiedValueExist) {
			if (simplifiedValueExist) {
				ignoreStrand = o.getSimplifiedBool("only.extract.positive.strand");
			}
		}

		String keepField2Contains = o.getSimplifiedStringWithDefault("keep.field2.contains");
		String keepField4Contains = o.getSimplifiedStringWithDefault("keep.field4.contains");

		String[] split = EGPSStringUtil.split(outputIndexes, ',');
		int[] outputNameCompositionIndexes = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			int index = Integer.parseInt(split[i]);
			// one-based value to zero-based value
			outputNameCompositionIndexes[i] = index - 1;
		}

		Stopwatch stopwatch = Stopwatch.createStarted();

		List<String> outputs = perform(bedFilePath, genomeFilePath, outputFastaFilePath, outputNameCompositionIndexes,
				nameSeperator,
				ignoreStrand, keepField2Contains, keepField4Contains);

		stopwatch.stop();

		outputs.add("Elapsed time: ".concat(stopwatch.toString()));

		return outputs;

	}

	List<String> perform(String bedFilePath, String genomeFilePath, String outputFastaFilePath,
			int[] outputNameCompositionIndexes, String nameSeperator, boolean ignoreStrand, String keepField2Contains,
			String keepField4Contains) throws IOException {

		int sz = 100 * 1024 * 1024;
		stringCounter.clear();

		Map<String, String> name2seqMap = new HashMap<>();
		FastaReader.readAndProcessFastaPerEntry(genomeFilePath, (name, seq) -> {
//			19 dna_rm:chromosome chromosome:GRCh38:19:1:58617616:1 REF
//			HG2233_PATCH dna_rm:scaffold scaffold:GRCh38:HG2233_PATCH:1:208149:1

			// Danio_rerio
//			> 1 dna_rm:chromosome chromosome:GRCz11:1:1:59578282:1 REF
//			> CHR_ALT_CTG10_1_5 dna_rm:chromosome chromosome:GRCz11:CHR_ALT_CTG10_1_5:1:45424051:1 HAP
//			> KN149790.1 dna_rm:scaffold scaffold:GRCz11:KN149790.1:1:115850:1 REF
			// 所以总结一下 Ensembl基因组的格式，先用空格分隔。第一个是染色体或者sca cont的名字；第二个是 第三个 以及 第四个

			EnsemblFastaName ensemblFastaName = new EnsemblFastaName(name);

			if (Strings.isNotEmpty(keepField2Contains)) {
				String type = ensemblFastaName.getType();
				if (!type.contains(keepField2Contains)) {
					return false;
				}
			}
			if (Strings.isNotEmpty(keepField4Contains)) {
				Optional<String> sequenceType2 = ensemblFastaName.getSequenceType();
				if (sequenceType2.isPresent()) {
					String sequenceType = sequenceType2.get();
					if (!Objects.equal(sequenceType, keepField4Contains)) {
						return false;
					}
				} else {
					return false;
				}

			}

			name2seqMap.put(ensemblFastaName.getName(), seq);
			return false;
		});


		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(bedFilePath);

		DNAComplement dnaComplement = new DNAComplement();

		List<String> outputs = new LinkedList<>();

		BufferedWriter wr = new BufferedWriter(new FileWriter(outputFastaFilePath));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), sz)) {
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				String[] strings = EGPSStringUtil.split(readLine, '\t');
				String seqName = strings[0];
				String startPositionZeroBased = strings[1];
				String endPositionZeroBased = strings[2];
				String strand = strings[3];

				String sequence = name2seqMap.get(seqName);
				if (sequence == null) {
					outputs.add("\nThe sequence " + seqName + " is not found in the genome file.");
					continue;
				}
				char strandType = strand.charAt(0);
				int start = Integer.parseInt(startPositionZeroBased);
				int end = Integer.parseInt(endPositionZeroBased);

				// name
				wr.write('>');

				wr.write(strings[outputNameCompositionIndexes[0]]);
				int len = outputNameCompositionIndexes.length;
				for (int i = 1; i < len; i++) {
					int index = outputNameCompositionIndexes[i];
					wr.write(nameSeperator);
					wr.write(strings[index]);
				}

				wr.write('\n');

				String substring = null;
				if (end > sequence.length()) {
					substring = sequence.substring(start);
					// 不输出了，不然太多了
//					outputs.add(
//							"\nWarning: the bed end position greater than sequence length.\n".concat(readLine));
				} else {
					substring = sequence.substring(start, end);
				}

				if (ignoreStrand) {
					wr.write(substring);
					wr.write('\n');
				} else {
					if (strandType == '+') {
						wr.write(substring);
						wr.write('\n');
					} else if (strandType == '-') {
						String reverseComplement = dnaComplement.getReverseComplement(substring);
						wr.write(reverseComplement);
						wr.write('\n');
					} else {
						outputs.add(
								"\nPlease check your strand, is should be either + or -.\n".concat(readLine));
					}
				}


			}
		}

		wr.close();

		return outputs;
	}


}
