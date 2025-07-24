package module.mutationpre;

import java.io.InputStream;
import java.util.List;

import javax.swing.SwingUtilities;

import egps2.frame.MainFrameProperties;
import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader{

	@Override
	public String getTabName() {
		return "Mutation presenter";
	}

	@Override
	public String getShortDescription() {
		return "ElegantJTable user-friendly tool for drawing genomic mutations.";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/mutationPre.svg");		
		IconBean iconBean = new IconBean();
		iconBean.setSVG(true);
		iconBean.setInputStream(resourceAsStream);
		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		return new GenomicMutationPresenterMain(this);
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
	
	public void openModuleWithInputStrings(List<String> list) {
		
		MainFrameProperties.loadTheModuleFromIModuleLoader(this);
		GenomicMutationPresenterMain simpleModuleMain = (GenomicMutationPresenterMain) getFace();
		
//		List<String> list = new ArrayList<>();
//		list.add("$totalLength=1273");
//		list.add("$regionInfo");
//		list.add("NTD	13	304	0,0,100,50");
//		list.add("$mutationInfo");
//		list.add("variant=Delta VOC");
//		list.add("69	254,0,0,255	HV69-");
		SwingUtilities.invokeLater(() -> {
//			simpleModuleMain.loadMuduleWithInputStrings(list);
		});
	}
}
