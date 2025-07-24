package module.histogram;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{

	@Override
	public String getTabName() {
		return "Quick histogram";
	}

	@Override
	public String getShortDescription() {
		return "Quick plot histogram according to the series of data.";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/histogram.png");		
		IconBean iconBean = new IconBean();
		iconBean.setSVG(false);
		iconBean.setInputStream(resourceAsStream);
		
		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		return new MainFace(this);
	}
	
	
	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				
				ModuleClassification.BYFUNCTIONALITY_PRIMITIVE_VISUALIZATION_INDEX,
				ModuleClassification.BYAPPLICATION_STATISTICS_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_VOICM_INVOKED
		);
		return ret;
	}

}
