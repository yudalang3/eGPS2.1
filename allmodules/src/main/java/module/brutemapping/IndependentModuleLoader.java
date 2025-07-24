package module.brutemapping;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{


	@Override
	public String getTabName() {
		return "Direct sequence mapping";
	}

	@Override
	public String getShortDescription() {
		return "Mapping the short sequence to genome with Knuth-Moris-Pratt algorithm.";
	}

//	@Override
//	public IconBean getIcon() {
//		InputStream resourceAsStream = getClass().getResourceAsStream("images/hexiantu.svg");		
//		IconBean iconBean = new IconBean();
//		iconBean.setSVG(true);
//		iconBean.setInputStream(resourceAsStream);
//		return iconBean;
//	}

	@Override
	public ModuleFace getFace() {
		return new GuiMain(this);
	}
	

	
	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_PROFESSIONAL_COMPUTATION_INDEX,
				ModuleClassification.BYAPPLICATION_GENOMICS_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_ONLY_EMPLOY_CONTAINER
		);
		return ret;
	}

}
