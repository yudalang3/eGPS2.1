package module.correlation4wnt;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class ModuleLoader implements IModuleLoader {

	@Override
	public String getTabName() {
		return "Correlation visualization for WNT";
	}

	@Override
	public String getShortDescription() {
		return "Customized correlation visulization for the Wnt pathway";
	}


	@Override
	public ModuleFace getFace() {
		return new GuiMain(this);
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_PRIMITIVE_VISUALIZATION_INDEX,
				ModuleClassification.BYAPPLICATION_COMMON_MODULE_INDEX, ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_ONLY_EMPLOY_CONTAINER);
		return ret;
	}

}
