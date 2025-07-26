package module.scountmerger;

import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.diytools.DIYToolModuleFace;
import egps2.modulei.ModuleClassification;

@SuppressWarnings("serial")
public class IndependentModuleLoader extends DIYToolModuleFace {

	public IndependentModuleLoader() {
		super(null);
	}

	@Override
	public String getTabName() {
		return "Sample counts merger";
	}

	@Override
	public String getShortDescription() {
		return "A convenient graphic tool to merge expression mapped counts of genes calculated from the featureCounts program.";
	}


	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_SIMPLE_TOOLS_INDEX,
				ModuleClassification.BYAPPLICATION_GENOMICS_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_ONLY_EMPLOY_CONTAINER
		);
		return ret;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("counts.file.suffix", ".counts.tsv",
				"The suffix of the counts file, this parameter is mandatory. Please take seriously.\n# DON NOT forget the . char");
		mapProducer.addKeyValueEntryBean("input.dir.path", "",
				"Input the directory path that contians the input files.");
		mapProducer.addKeyValueEntryBean("out.file.path", "",
				"Input the output file path, this parameter is mandatory.");
		mapProducer.addKeyValueEntryBean("if.export.tpm.matrix", "T", "Whether export the tpm matrix file.");
	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputFilePath = null;
		String outputFilePath = null;
		String fileSuffix = null;
		boolean shouldExportTPM = true;

		String string = para.get("$input.dir.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Please input the parameter $input.fasta.path ");
		} else {
			inputFilePath = string;
		}
		string = para.get("$out.file.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Please input the parameter $out.file.path ");
		} else {
			outputFilePath = string;
		}
		string = para.get("$counts.file.suffix");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Please input the parameter $counts.file.suffix ");
		} else {
			fileSuffix = string;
		}

		string = para.get("$if.export.tpm.matrix");
		if (!Strings.isNullOrEmpty(string)) {
			shouldExportTPM = BooleanUtils.toBoolean(string);
		}

		MergeCountFiles mergeCountFiles = new MergeCountFiles();
		mergeCountFiles.setInputPath(inputFilePath);
		mergeCountFiles.setOutputPath(outputFilePath);
		mergeCountFiles.fileSuffix = fileSuffix;
		mergeCountFiles.shouldExportTPM = shouldExportTPM;

		mergeCountFiles.doIt();
		

		this.setText4Console(mergeCountFiles.output4console);
	}
}
