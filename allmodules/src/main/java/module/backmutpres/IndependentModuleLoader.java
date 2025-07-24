package module.backmutpres;

import java.util.List;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{

	List<String> list;
	
	public void setList(List<String> list) {
		this.list = list;
	}
	

	@Override
	public String getTabName() {
		return "Back mutation presenter";
	}

	@Override
	public String getShortDescription() {
		return "Text visualization of the back mutations.";
	}

	@Override
	public ModuleFace getFace() {
		GuiMain guiMain = new GuiMain(this);
		return guiMain;
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_SIMPLE_TOOLS_INDEX,
				ModuleClassification.BYAPPLICATION_VISUALIZATION_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_CROSS_MODULE_REFERENCED
		);
		return ret;
	}
}
