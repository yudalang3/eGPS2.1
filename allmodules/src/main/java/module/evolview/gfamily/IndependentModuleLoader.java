package module.evolview.gfamily;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;
public class IndependentModuleLoader implements IModuleLoader{
	@Override
	public String getTabName() {
		
		return "Gene family browser";
	}

	@Override
	public String getShortDescription() {
		return "Up-panel is tree while down-panel is gene related analysis sub-modules.";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/family.png");		
		IconBean iconBean = new IconBean();
		iconBean.setSVG(false);
		iconBean.setInputStream(resourceAsStream);
		
		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		return new GeneFamilyMainFace(this);
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
