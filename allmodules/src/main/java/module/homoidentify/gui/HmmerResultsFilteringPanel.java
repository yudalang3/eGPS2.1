/**
 * 
 */
package module.homoidentify.gui;

import java.util.List;

import com.google.common.base.Stopwatch;

import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.frame.ComputationalModuleFace;
import module.homoidentify.totsv.SequenceTBL2Table;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class HmmerResultsFilteringPanel extends DockableTabModuleFaceOfVoice {

	public HmmerResultsFilteringPanel(ComputationalModuleFace cmf) {
		super(cmf);
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("@", "Filter results from the hmmer.The filter process better to execute in the R lang.", "");
		mapProducer.addKeyValueEntryBean("hmmer.tblout.file", "", "Input hmmer results file path. Necessary");
		mapProducer.addKeyValueEntryBean("output.file.path", "", "Output results file. Necessary");
		mapProducer.addKeyValueEntryBean("full.seq.Evalue", "1e-6", "The full sequence E-value cutoff, default 1e-6");
		mapProducer.addKeyValueEntryBean("full.seq.score", "",
				"The full sequence score cutoff, default none, only consider E-value.");
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {

		String inputFilePath = o.getSimplifiedString("hmmer.tblout.file");
		String outputFilePath = o.getSimplifiedString("output.file.path");
		// E-value 要大; score要小
//		double eValueCutOff = 9999999;
		double scoreCutOff = -1;
		double eValueCutOff = o.getSimplifiedDouble("$full.seq.Evalue");

		if (o.isSimplifiedValueExist("full.seq.score")){
			scoreCutOff = o.getSimplifiedDouble("full.seq.score");
		}

		Stopwatch stopwatch = Stopwatch.createStarted();

		List<String> outputs = perform(inputFilePath, outputFilePath, eValueCutOff, scoreCutOff);

		stopwatch.stop();
		long millis = stopwatch.elapsed().toMillis();
		outputs.add("Take time of  " + (millis) + " milliseconds to execute.");
		setText4Console(outputs);
	}

	private List<String> perform(String inputFilePath, String outputFilePath, double eValueCutOff, double scoreCutOff)
			throws Exception {

		SequenceTBL2Table sequenceTBL2Table = new SequenceTBL2Table();
		sequenceTBL2Table.setInputPath(inputFilePath);
		sequenceTBL2Table.setOutputPath(outputFilePath);
		sequenceTBL2Table.setCutOf(eValueCutOff, scoreCutOff);
		
		sequenceTBL2Table.doIt();

        return sequenceTBL2Table.getOutputs();

	}

	@Override
	public String getShortDescription() {
		return "";
	}

	@Override
	public String getTabName() {
		return "";
	}
}
