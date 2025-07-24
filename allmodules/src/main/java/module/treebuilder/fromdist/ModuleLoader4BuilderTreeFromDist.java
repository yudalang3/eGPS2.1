package module.treebuilder.fromdist;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class ModuleLoader4BuilderTreeFromDist implements IModuleLoader {

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_PROFESSIONAL_COMPUTATION_INDEX,
				ModuleClassification.BYAPPLICATION_EVOLUTION_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_3_INDEX,
				ModuleClassification.BYDEPENDENCY_CROSS_MODULE_REFERENCED);
		return ret;
	}

	@Override
	public String getShortDescription() {
		return "Construct the phylogentic tree from the evolutionary distance.  (inheritance from eGPS 1)";
	}

	@Override
	public ModuleFace getFace() {
		return new GuiMain(this);
	}

	@Override
	public String getTabName() {
		return "Tree builder: from Distance";
	}

}
