package module.gff3opr;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{

	@Override
	public String getTabName() {
		return "GFF3 operator";
	}

	@Override
	public String getShortDescription() {
		return "Quick obtain information from the gff format file.";
	}


	@Override
	public ModuleFace getFace() {
		return new GFF3OperatorMainFace(this);
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
