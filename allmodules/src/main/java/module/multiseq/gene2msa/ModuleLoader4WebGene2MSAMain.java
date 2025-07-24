package module.multiseq.gene2msa;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class ModuleLoader4WebGene2MSAMain implements IModuleLoader {

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_PROFESSIONAL_COMPUTATION_INDEX,
				ModuleClassification.BYAPPLICATION_EVOLUTION_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_CROSS_MODULE_REFERENCED);
		return ret;
	}

	@Override
	public String getShortDescription() {
		return "Obtain the MSA from the Ensembel or UCSC RESTful service.";
	}

	@Override
	public ModuleFace getFace() {
		return new GuiMain(this);
	}

	@Override
	public String getTabName() {
		return "Gene to MSA";
	}

}
