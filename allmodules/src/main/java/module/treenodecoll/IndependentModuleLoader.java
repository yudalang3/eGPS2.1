package module.treenodecoll;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{

	@Override
	public String getTabName() {
		return "Tree node info collector";
	}

	@Override
	public String getShortDescription() {
		return "Fast collect tree leaf data. Internal node also supported.";
	}


	@Override
	public ModuleFace getFace() {
		return new GuiMain(this);
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_SIMPLE_TOOLS_INDEX,
				ModuleClassification.BYAPPLICATION_EVOLUTION_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_2_INDEX,
				ModuleClassification.BYDEPENDENCY_CROSS_MODULE_REFERENCED
		);
		return ret;
	}
}
