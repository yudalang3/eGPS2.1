package module.multiseq.alignment.view;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class Launcher4ModuleLoader implements IModuleLoader{
	
	String moduleLaunchWay = "Module launched by click the open button.";
	String whatDataInvoked;

	private SequenceDataForAViewer sequenceDataForAViewer;
	
	@Override
	public String getTabName() {
		return "Alignmnet view";
	}

	@Override
	public String getShortDescription() {
		return "ElegantJTable convenient remnant to view the multiple sequence alignment.";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/alignmentView.svg");		
		IconBean iconBean = new IconBean();
		iconBean.setSVG(true);
		iconBean.setInputStream(resourceAsStream);
		
		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		// 调整一下，使用VOICM
		return new AlignmentViewMain(this);
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

	public SequenceDataForAViewer getSequenceDataForAViewer() {
		SequenceDataForAViewer ret = sequenceDataForAViewer;
		// 得到之后立即销毁，防止影响后面模块的使用
		sequenceDataForAViewer = null;
		return ret;
	}

	public void setSequenceDataForAViewer(SequenceDataForAViewer sequenceDataForAViewer) {
		this.sequenceDataForAViewer = sequenceDataForAViewer;
	}
	
	public void setWhatDataInvoked(String whatDataInvoked) {
		this.whatDataInvoked = whatDataInvoked;
	}
	
	public void setModuleLaunchWay(String moduleLaunchWay) {
		this.moduleLaunchWay = moduleLaunchWay;
	}

}
