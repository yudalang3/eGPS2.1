package module.multiseq.aligner;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{

	@Override
	public String getTabName() {
		return "Multi-sequences aligner: MAFFT";
	}

	@Override
	public String getShortDescription() {
		return "A convenient wrapper for the multi seqs aligner: MAFFT. (inheritance from eGPS 1)";
	}

	@Override
	public ModuleFace getFace() {
		// 这是有输入框的，下面那个没有，为什么会出现这种情况，因为之前设计的不好。
		return new IndependentMultipleSeqAlignerMain(this);
//		return new MultipleSeqAlignerMain(this);
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
