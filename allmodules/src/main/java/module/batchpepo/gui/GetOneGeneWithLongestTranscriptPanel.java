/**
 * 
 */
package module.batchpepo.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import fasta.io.FastaReader;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import tsv.io.TSVReader;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.SubTabModuleRunner;
import egps2.builtin.modules.voice.template.SubTabJPanel4Voice;
import egps2.builtin.modules.voice.template.SubTabJPanel4VoiceInjector;

public class GetOneGeneWithLongestTranscriptPanel implements SubTabModuleRunner, SubTabJPanel4VoiceInjector {

	private SubTabJPanel4Voice sFace;

	@Override
	public void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.dir", "",
				"The output dir that has already downloaded files. Necessary");

		mapProducer.addKeyValueEntryBean("pep.suffix", "pep.all.fa.gz", "The peptides file suffix.");

		mapProducer.addKeyValueEntryBean("pep.info.file", "",
				"The information of the peptides, Necessary. It was prodeced by the first step.");
		mapProducer.addKeyValueEntryBean("output.fasta.file", "", "Output file path, Necessary.");

	}

	private void run(String inputDir, String pepInfoFileName, String outputFilePath, String pepSuffix)
			throws IOException {

		File dir = new File(inputDir);

		Map<String, String> pep2geneMap = TSVReader.organizeMap("pepName", "geneName", pepInfoFileName);

		BufferedWriter bufferedWriter4fasta = new BufferedWriter(new FileWriter(new File(outputFilePath)));

		File[] listFiles = dir.listFiles();

		List<File> filesToProcess = new LinkedList<>();

		for (File file : listFiles) {
			if (file.getName().endsWith(pepSuffix)) {
				filesToProcess.add(file);
			}
		}

		int size = filesToProcess.size();

		Comparator<Pair<String, Integer>> comparator = new Comparator<Pair<String, Integer>>() {

			@Override
			public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
//				降序
				return o2.getRight().compareTo(o1.getRight());
			}
		};


		for (int i = 0; i < size; i++) {
			int oneBaseIndex = i + 1;
			File file = filesToProcess.get(i);
			Map<String, List<Pair<String, Integer>>> gene2pepListMap = new HashMap<>();
			Map<String, String> pepSequenceMap = new HashMap<>();

			FastaReader.readAndProcessFastaPerEntry(file.getAbsolutePath(), (name, seq) -> {
				String geneNameOfThis = pep2geneMap.get(name);
				Objects.requireNonNull(geneNameOfThis);

				List<Pair<String, Integer>> list = gene2pepListMap.get(geneNameOfThis);
				if (list == null) {
					ArrayList<Pair<String, Integer>> newArrayList = Lists.newArrayList();
					newArrayList.add(Pair.of(name, seq.length()));
					gene2pepListMap.put(geneNameOfThis, newArrayList);
				} else {
					list.add(Pair.of(name, seq.length()));
				}

				pepSequenceMap.put(name, seq);
				return false;
			});

			for (Entry<String, List<Pair<String, Integer>>> entry : gene2pepListMap.entrySet()) {
				String key = entry.getKey();
				List<Pair<String, Integer>> value = entry.getValue();
				if (value.size() > 1) {
					Collections.sort(value, comparator);
				}
				String left = value.get(0).getLeft();
				String seq = pepSequenceMap.get(left);

				bufferedWriter4fasta.write(">");
				bufferedWriter4fasta.write(key);
				bufferedWriter4fasta.write("|");
				bufferedWriter4fasta.write(left);
				bufferedWriter4fasta.write("\n");
				bufferedWriter4fasta.write(seq);
				bufferedWriter4fasta.write("\n");
			}

			List<String> outputs = new LinkedList<>();
			outputs.add("Current is: ".concat(file.getName()));
			outputs.add("Current is: " + oneBaseIndex + "  /  " + size);
			setText4Console(outputs);

		}

		bufferedWriter4fasta.close();
	}

	@Override
	public String getShortDescription() {
		return "Obtain the logest protein sequence for a gene locus, normally caused by alternative splicing.";
	}

	private void setText4Console(List<String> outputs) {
		if (sFace == null) {
			for (String string : outputs) {
				System.out.println(string);
			}
		} else {
			sFace.setText4Console(outputs);
		}
	}

	@Override
	public String getTabName() {
		return "2. Get the longest protein for gene";
	}

	@Override
	public Collection<String> execute(OrganizedParameterGetter o) throws Exception {
		String inputDir = o.getSimplifiedString("$input.dir");
		String pepInfoFileName = o.getSimplifiedString("$pep.info.file");
		String pepSuffix = o.getSimplifiedString("$pep.suffix");
		String outputFilePath = o.getSimplifiedString("$output.fasta.file");
		run(inputDir, pepInfoFileName, outputFilePath, pepSuffix);

		return Collections.emptyList();
	}

	@Override
	public void injectSubTabModuleFace(SubTabJPanel4Voice sFace) {
		this.sFace = sFace;
	}

}
