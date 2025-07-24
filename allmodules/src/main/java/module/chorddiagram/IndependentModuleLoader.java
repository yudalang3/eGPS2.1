package module.chorddiagram;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{


	@Override
	public String getTabName() {
		return "Chord diagram for WNT";
	}

	@Override
	public String getShortDescription() {
		return "Highly interactive chord diagram to depict the complex interaction.";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/hexiantu.svg");		
		IconBean iconBean = new IconBean();
		iconBean.setSVG(true);
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
				ModuleClassification.BYFUNCTIONALITY_COMPLICATED_VISUALIZATION_INDEX,
				ModuleClassification.BYAPPLICATION_VISUALIZATION_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_1_INDEX,
				ModuleClassification.BYDEPENDENCY_ONLY_EMPLOY_CONTAINER
		);
		return ret;
	}

}
