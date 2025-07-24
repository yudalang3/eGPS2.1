package module.tablecuration;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{


	@Override
	public String getTabName() {
		return "Table-like text curation";
	}

	@Override
	public String getShortDescription() {
		return "Several tools to curate the table-like text file.";
	}


	@Override
	public ModuleFace getFace() {
		return new GuiMain(this);
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_OPERATIONAL_WORKBENCH_INDEX,
				ModuleClassification.BYAPPLICATION_GENOMICS_INDEX, ModuleClassification.BYCOMPLEXITY_LEVEL_3_INDEX,
				ModuleClassification.BYDEPENDENCY_CROSS_MODULE_REFERENCED
		);
		return ret;
	}
}
