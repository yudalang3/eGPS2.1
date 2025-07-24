package module.evoldist.gene2dist;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class ModuleLoader4WebGene2EvolDistMain implements IModuleLoader {

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(

				ModuleClassification.BYFUNCTIONALITY_PROFESSIONAL_COMPUTATION_INDEX,
				ModuleClassification.BYAPPLICATION_EVOLUTION_INDEX, ModuleClassification.BYCOMPLEXITY_LEVEL_2_INDEX,
				ModuleClassification.BYDEPENDENCY_CROSS_MODULE_REFERENCED);
		return ret;
	}

	@Override
	public String getShortDescription() {
		return "Obtain the MSA from the Ensembel RESTful service and compute the evolutionary distance from the MSA.  (inheritance from eGPS 1)";
	}

	@Override
	public ModuleFace getFace() {
		return new DistMatrixViewMainForGene2Dist(this);
	}

	@Override
	public String getTabName() {
		return "Gene to evolutionary distance";
	}



}
