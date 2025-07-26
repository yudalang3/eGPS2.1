package module.alignedpro2cds;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import utils.EGPSFileUtil;
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
		return "Aligned Protein To CDS";
	}

	@Override
	public String getShortDescription() {
		return "A convenient  graphic tool to quick translate protein alignment to CDS alignment.";
	}


	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_SIMPLE_TOOLS_INDEX,
				ModuleClassification.BYAPPLICATION_GENOMICS_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_2_INDEX,
				ModuleClassification.BYDEPENDENCY_CROSS_MODULE_REFERENCED
		);
		return ret;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {

		mapProducer.addKeyValueEntryBean("input.aligned.protein.path", "",
				"The target coding sequence file for translation, Necessary.");
		mapProducer.addKeyValueEntryBean("input.cds.path", "",
				"The target coding sequence file for translation, Necessary.");
		mapProducer.addKeyValueEntryBean("output.aligned.cds.path", "",
				"Set the output path of the aligned CDS file, if null, the file path is *aligned.cds.fas");

	}

	@Override
	protected void execute(Map<String, String> para) throws Exception {
		String inputAlignedProt = null;
		String inputCDSFile = null;
		String outputAlignedCDSFile = null;

		String string = para.get("$input.aligned.protein.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $input.aligned.protein.path");
		} else {
			inputAlignedProt = string;
		}

		string = para.get("$input.cds.path");
		if (Strings.isNullOrEmpty(string)) {
			throw new IllegalArgumentException("Plese input the $input.cds.path");
		} else {
			inputCDSFile = string;
		}

		string = para.get("$output.aligned.cds.path");
		if (Strings.isNullOrEmpty(string)) {
			outputAlignedCDSFile = EGPSFileUtil.appendAdditionalStr2path(inputCDSFile, "aligned.cds");
		} else {
			outputAlignedCDSFile = string;
		}


		try {
			AlignedProt2AlignedCDS.makeTheConversion(new File(inputAlignedProt), new File(inputCDSFile),
					new File(outputAlignedCDSFile));
			List<String> strings4output = Arrays
					.asList("Successfully converted to file: ".concat(outputAlignedCDSFile));
			this.setText4Console(strings4output);

		} catch (Exception e) {
			e.printStackTrace();
			List<String> strings4output = Arrays.asList("Error: ".concat(e.getMessage()));
			this.setText4Console(strings4output);
		}


	}
}
