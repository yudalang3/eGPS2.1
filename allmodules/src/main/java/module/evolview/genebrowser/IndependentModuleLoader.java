package module.evolview.genebrowser;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{

	@Override
	public String getTabName() {
		return "Gene browser";
	}

	@Override
	public String getShortDescription() {
		return "Gene struct browser";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/gBrowser.svg");		
		IconBean iconBean = new IconBean();
		iconBean.setSVG(true);
		iconBean.setInputStream(resourceAsStream);
		
		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		return new GeneBrowserMainFace(this);
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				
				ModuleClassification.BYFUNCTIONALITY_COMPLICATED_VISUALIZATION_INDEX,
				ModuleClassification.BYAPPLICATION_VISUALIZATION_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_4_INDEX,
				ModuleClassification.BYDEPENDENCY_MAINFRAME_FEEDBACK_REMAINDER_INVOKED
		);
		return ret;
	}
}
