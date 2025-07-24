package operator.sequences;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{

	@Override
	public String getTabName() {
		return "Bio. sequence operator";
	}

	@Override
	public String getShortDescription() {
		return "The collections of the convenient operations for one sequence per line.";
	}


	@Override
	public ModuleFace getFace() {
		return new OneSeqOperatorPanel(this);
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_SIMPLE_TOOLS_INDEX,
				ModuleClassification.BYAPPLICATION_COMMON_MODULE_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_2_INDEX,
				ModuleClassification.BYDEPENDENCY_ONLY_EMPLOY_CONTAINER
		);
		return ret;
	}
}
