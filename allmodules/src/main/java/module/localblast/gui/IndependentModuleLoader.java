package module.localblast.gui;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{


	@Override
	public String getTabName() {
		return "Local blast wrapper";
	}

	@Override
	public String getShortDescription() {
		return "Local blast wrapper for convenient run the program and process the results.";
	}

	@Override
	public ModuleFace getFace() {
		return new LocalBlastMain(this);
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_OPERATIONAL_WORKBENCH_INDEX,
				ModuleClassification.BYAPPLICATION_GENOMICS_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_2_INDEX,
				ModuleClassification.BYDEPENDENCY_COMPUTATIONAL_MECHANISM_EMPLOYED
		);
		return ret;
	}
}
