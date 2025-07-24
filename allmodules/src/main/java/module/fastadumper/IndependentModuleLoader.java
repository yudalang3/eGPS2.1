package module.fastadumper;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader {

	@Override
	public String getTabName() {
		return "Fasta dumper";
	}

	@Override
	public String getShortDescription() {
		return "Quick fasta content dumnper, with features of remove gaps, rename and substring.";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/Dumper-Truck.png");
		IconBean iconBean = new IconBean();
		iconBean.setSVG(false);
		iconBean.setInputStream(resourceAsStream);

		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		GuiMain simpleModuleMain = new GuiMain(this);
		return simpleModuleMain;
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
