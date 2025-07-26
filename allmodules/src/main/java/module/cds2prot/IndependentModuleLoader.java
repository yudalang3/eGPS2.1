package module.cds2prot;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import geneticcodes.GeneticCode;
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
		return "CDS 2Protein translator";
	}

	@Override
	public String getShortDescription() {
		return "A convenient graphic tool to quick translate coding squence (cds) to protein sequence.";
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
		String[] possibleNames = GeneticCode.GeneticCodeTableNames;

		mapProducer.addKeyValueEntryBean("input.file.path", "",
				"The target coding sequence file for translation, Necessary.");
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("The genetic code table name, current supported names are:");
		int size = possibleNames.length;
		for (int i = 0; i < size; i++) {
			String string = possibleNames[i];
			sBuilder.append("\n#");
			sBuilder.append(string);
		}
		mapProducer.addKeyValueEntryBean("genetic.code.table", possibleNames[0], sBuilder.toString());

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {

		String queryString = null;
		String inputFilePath = null;

		String string = para.get("$input.file.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $input.file.path");
		} else {
			inputFilePath = string;
		}

		string = para.get("$genetic.code.table");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $query.string");
		} else {
			queryString = string;
		}


		DoCds2ProtTranslateAction doGrepAction = new DoCds2ProtTranslateAction();
		doGrepAction.inputFilePath = inputFilePath;
		doGrepAction.geneticodeName = queryString;

		doGrepAction.doIt();
		List<String> strings4output = doGrepAction.getOutputList();
		this.setText4Console(strings4output);

	}
}
