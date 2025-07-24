package module.evolview.pathwaybrowser;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader {

	public IndependentModuleLoader() {

	}

	@Override
    public String getTabName() {
        return "Pathway family browser";
    }

    @Override
    public String getShortDescription() {
		return "Up-panel is phylogenetic tree while down-panel is pathway related analyzing sub-modules.";
    }

    @Override
    public IconBean getIcon() {
        InputStream resourceAsStream = getClass().getResourceAsStream("images/pathwayFamBrowser.svg");
        IconBean iconBean = new IconBean();
        iconBean.setSVG(true);
        iconBean.setInputStream(resourceAsStream);
        return iconBean;
    }

    @Override
    public ModuleFace getFace() {
		return new PathwayFamilyMainFace(this);
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
