package module.symbol2id;

import java.util.List;
import java.util.Map;

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
		return "Gene symbol to entrezID";
	}

	@Override
	public String getShortDescription() {
		return "Quick translated the gene symbol to entrez IDs.";
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
		mapProducer.addKeyValueEntryBean("input.geneID.path", "",
				"The file path has the gene information, file format is tsv. Necessary");
		mapProducer.addKeyValueEntryBean("input.need2translate", "",
				"The file path has the gene symbols to translation, file format is tsv. Necessary");
		mapProducer.addKeyValueEntryBean("output.file.path", "",
				"The file path to output, file format is tsv. Necessary");
		mapProducer.addKeyValueEntryBean("index.of.column4translate", "1",
				"This is the column index need to translate (First is 1)");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String refPath = null;
		String geneSymbolQueryPath = null;
		String outputPath = null;
		int translateIndex = 0;

		String string = para.get("$input.geneID.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the geneID path");
		} else {
			refPath = string;
		}

		string = para.get("$input.need2translate");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the need to translate file path");
		} else {
			geneSymbolQueryPath = string;
		}

		string = para.get("$output.file.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the output file path");
		} else {
			outputPath = string;
		}

		string = para.get("$index.of.column4translate");
		if (string != null) {
			translateIndex = Integer.parseInt(string) - 1;
		}


		TranslateCalculator runner = new TranslateCalculator();
		runner.setInputPath(refPath);
		runner.setOutputPath(outputPath);
		runner.setNeedToTranslateFilePath(geneSymbolQueryPath);

		runner.setIndexOfGeneSymbol(translateIndex);
		runner.symbol2entrezID();
		
		List<String> strings4output = runner.strings4output;

		this.setText4Console(strings4output);
	}
}
