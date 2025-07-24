package module.benchensdownloader;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{


	@Override
	public String getTabName() {
		return "Batch Ensembl annotation downloader";
	}

	@Override
	public String getShortDescription() {
		return "Downloaders for bulk downloading of Ensembl annotation files, such as cds, protein and gff3";
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
				ModuleClassification.BYDEPENDENCY_ONLY_EMPLOY_CONTAINER
		);
		return ret;
	}
}
