package module.multiseqview;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{
	
	public IndependentModuleLoader() {
	}
	
	@Override
	public String getTabName() {
		return "Multi-seqs struct view";
	}

	@Override
	public String getShortDescription() {
		return "ElegantJTable user-friendly visualization remnant for gene features like domain and motif, design for multiple sequences.";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/gene.png");		
		IconBean iconBean = new IconBean();
		iconBean.setSVG(false);
		iconBean.setInputStream(resourceAsStream);
		
		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		return new SequenceStructureViewMain(this);
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(

				ModuleClassification.BYFUNCTIONALITY_COMPLICATED_VISUALIZATION_INDEX,
				ModuleClassification.BYAPPLICATION_VISUALIZATION_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_5_INDEX,
				ModuleClassification.BYDEPENDENCY_FULL_FEATURES_INVOKED
				);
		return ret;
	}
}
