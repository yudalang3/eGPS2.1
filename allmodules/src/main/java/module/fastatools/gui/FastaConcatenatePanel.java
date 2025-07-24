/**
 * 
 */
package module.fastatools.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import utils.string.EGPSStringUtil;
import egps2.frame.ComputationalModuleFace;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;

@SuppressWarnings("serial")
public class FastaConcatenatePanel extends DIYToolSubTabModuleFace {

	public FastaConcatenatePanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("fasta.dir.path", "",
				"Input fasta directory path, the fasta files in this directory will be processed. Necessary");

		mapProducer.addKeyValueEntryBean("output.file.path", "", "Output fasta file path. Necessary");
		mapProducer.addKeyValueEntryBean("exclude.file.suffix", "",
				"File to exclude, multiple entries are supported, for example a;b;c. Default is none.");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputDir = null;
		String outputFastaPath = null;
		List<String> excludeStrings = null;

		String string = para.get("$fasta.dir.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the download file path.");
		} else {
			inputDir = string;
		}

		string = para.get("$output.file.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $pep.info.file.name");
		} else {
			outputFastaPath = string;
		}

		string = para.get("$exclude.file.suffix");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $intra.species.unique.info.file.name");
		} else {
			String[] split = EGPSStringUtil.split(string, ';');
			excludeStrings = Arrays.asList(split);
		}

		run(inputDir, outputFastaPath, excludeStrings);
	}

	private void run(String inputDir, String outputFastaPath, List<String> excludeStrings) throws Exception {

		FastaConcatenator fastaConcatenator = new FastaConcatenator();
		List<String> inputFiles = Arrays.asList(inputDir);
		fastaConcatenator.setInputFiles(inputFiles);
		fastaConcatenator.setExcludeStrings(excludeStrings);

		fastaConcatenator.setOutputPath(outputFastaPath);
		fastaConcatenator.setModuleFace(computationalModuleFace);

		long timeMillis = System.currentTimeMillis();

		fastaConcatenator.doIt();

		List<String> outputs = fastaConcatenator.getOutputs();
		long thisTimeMillis = System.currentTimeMillis();
		outputs.add("Take time of  " + (thisTimeMillis - timeMillis) + " milliseconds to execute.");

		setText4Console(outputs);

	}


}
