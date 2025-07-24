package module.heatmap;

import java.io.InputStream;
import java.util.List;

import egps2.utils.common.model.datatransfer.ThreeTuple;
import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader {
	/**
	 * first is row names; next is col names; last is data matrix
	 * 
	 * @param element
	 */
	ThreeTuple<String[], String[], double[][]> element;
	
	List<String> inputContents;

	public void setElement(ThreeTuple<String[], String[], double[][]> element) {
		this.element = element;
	}

	public void setInputContents(List<String> inputContents) {
		this.inputContents = inputContents;
	}

	@Override
	public String getTabName() {
		return "EHeatmap";
	}

	@Override
	public String getShortDescription() {
		return "An interactive tool to draw heatmap!";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/myHeatmap.svg");
		IconBean iconBean = new IconBean();
		iconBean.setSVG(true);
		iconBean.setInputStream(resourceAsStream);

		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		return new EheatmapMain(this);
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
