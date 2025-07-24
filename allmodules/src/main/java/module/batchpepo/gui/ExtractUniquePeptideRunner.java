/**
 * 
 */
package module.batchpepo.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.fastmodvoice.SubTabModuleRunner;
import egps2.builtin.modules.voice.template.SubTabJPanel4Voice;
import egps2.builtin.modules.voice.template.SubTabJPanel4VoiceInjector;

public class ExtractUniquePeptideRunner implements SubTabModuleRunner, SubTabJPanel4VoiceInjector {

	private SubTabJPanel4Voice sFace;


	@Override
	public void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.dir", "",
				"The output dir that has already downloaded files. Necessary");

		mapProducer.addKeyValueEntryBean("pep.suffix", "pep.all.fa.gz", "The peptides file suffix.");
		mapProducer.addKeyValueEntryBean("start.download.index", "1", "The start file index to process, default is 1.");

		mapProducer.addKeyValueEntryBean("%", "File names for output", "");
		mapProducer.addKeyValueEntryBean("pep.info.file.name", "pep_info.tsv",
				"The information of the peptides information, the output directory is $download.dir.file");
		mapProducer.addKeyValueEntryBean("intra.species.unique.info.file.name", "intra.species.uniqueInfo.tsv",
				"The information of the peptides information, the output directory is $download.dir.file");

	}


	private void run(String inputDir, String pepInfoFileName, String intraSpeciesFileName, String pepSuffix,
			int startProcessIndex) throws IOException {

		File dir = new File(inputDir);

		File outputDir = new File(dir, "outputUniquePep");
		if (!outputDir.exists()) {
			boolean mkdir = outputDir.mkdir();
			if (!mkdir) {
				throw new IOException("Can not create the output dir.");
			}
		}

		boolean shouldAppend = startProcessIndex > 1;
		BufferedWriter bufferedWriter4Info = new BufferedWriter(
				new FileWriter(new File(outputDir, pepInfoFileName), shouldAppend));
		BufferedWriter bufferedWriter4IntraSpeciesSamePep = new BufferedWriter(
				new FileWriter(new File(outputDir, intraSpeciesFileName), shouldAppend));

		RemoveRedundantPeps removeRedundantPeps = new RemoveRedundantPeps();
		File[] listFiles = dir.listFiles();

		List<File> filesToProcess = new LinkedList<>();

		for (File file : listFiles) {
			if (file.getName().endsWith(pepSuffix)) {
				filesToProcess.add(file);
			}
		}

		int size = filesToProcess.size();

		String headerLine = RemoveRedundantPeps.PepEntry.getHeaderLine();
		bufferedWriter4Info.write(headerLine);
		bufferedWriter4Info.write("\n");

		for (int i = 0; i < size; i++) {
			int oneBaseIndex = i + 1;
			if (oneBaseIndex < startProcessIndex) {
				continue;
			}
			File file = filesToProcess.get(i);

			removeRedundantPeps.setInputFastaPath(file);
			removeRedundantPeps.setOutputFastaPath(new File(outputDir, file.getName()));
			removeRedundantPeps.setWriter4info(bufferedWriter4Info);
			removeRedundantPeps.setWriter4intraSpeciesSamePep(bufferedWriter4IntraSpeciesSamePep);

			List<String> outputs = new LinkedList<>();
			try {
				removeRedundantPeps.run();
			} catch (Exception e) {
				String message = e.getMessage();
				outputs.add(message);
				outputs.add("Current is: ".concat(file.getName()));
				outputs.add("Current is: " + oneBaseIndex + "  /  " + size);
				setText4Console(outputs);
				throw e;
			}
			outputs.add("Current is: ".concat(file.getName()));
			outputs.add("Current is: " + oneBaseIndex + "  /  " + size);
			setText4Console(outputs);

		}

		bufferedWriter4Info.close();
		bufferedWriter4IntraSpeciesSamePep.close();
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
	public String getShortDescription() {
		return "only keep the identiacal sequence for all the proteins";
	}

	@Override
	public String getTabName() {
		return "1. Extract unique peptide";
	}

	@Override
	public void injectSubTabModuleFace(SubTabJPanel4Voice sFace) {
		this.sFace = sFace;
	}

	@Override
	public Collection<String> execute(OrganizedParameterGetter o) throws Exception {
		String inputDir = o.getSimplifiedString("$input.dir");
		String pepInfoFileName = o.getSimplifiedString("$pep.info.file.name");
		String intraSpeciesFileName = o.getSimplifiedString("$intra.species.unique.info.file.name");
		String pepSuffix = o.getSimplifiedString("$pep.suffix");
		int startProcessIndex = o.getSimplifiedInt("$start.download.index");

		run(inputDir, pepInfoFileName, intraSpeciesFileName, pepSuffix, startProcessIndex);

		return Collections.emptyList();
	}



}
