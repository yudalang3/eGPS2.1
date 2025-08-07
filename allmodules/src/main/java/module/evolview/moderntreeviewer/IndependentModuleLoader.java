package module.evolview.moderntreeviewer;

import java.io.InputStream;

import egps2.frame.ModuleFace;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.model.tree.TreeLayoutProperties;
import egps2.modulei.IModuleLoader;
import egps2.modulei.IconBean;
import egps2.modulei.ModuleClassification;

public class IndependentModuleLoader implements IModuleLoader {

	GraphicsNode rootNode;
	TreeLayoutProperties treeLayoutProperties;

	String howModuleLaunched = "Module launched by directly click the open button";
	String whatDataInvoked = "";

	String waitingTextMsg = "Please click the import button in the toolbar to load data";
	
	@Override
	public String getTabName() {
		return "Modern tree view";
	}

	@Override
	public String getShortDescription() {
		return "A modern tree operation and visualization complex remnant.";
	}

	@Override
	public IconBean getIcon() {
		InputStream resourceAsStream = getClass().getResourceAsStream("images/cengcijulei.svg");
		IconBean iconBean = new IconBean();
		iconBean.setSVG(true);
		iconBean.setInputStream(resourceAsStream);
		return iconBean;
	}

	@Override
	public ModuleFace getFace() {
		return new MTreeViewMainFace(this);
	}

	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				ModuleClassification.BYFUNCTIONALITY_COMPLICATED_VISUALIZATION_INDEX,
				ModuleClassification.BYAPPLICATION_VISUALIZATION_INDEX, ModuleClassification.BYCOMPLEXITY_LEVEL_5_INDEX,
				ModuleClassification.BYDEPENDENCY_FULL_FEATURES_INVOKED);
		return ret;
	}

	/**
	 * @param root 这是必需的
	 * @param treeLayoutProperties 可选，如果输入为null，会自动根据 root生成 treeLayoutProperties
	 */
	public void setModuleData(GraphicsNode root, TreeLayoutProperties treeLayoutProperties) {
		this.rootNode = root;
		this.treeLayoutProperties = treeLayoutProperties;
	}
	
	public void setHowModuleLaunchedWithData(String howModuleLaunched, String whatDataInvoked) {
		this.howModuleLaunched = howModuleLaunched;
		this.whatDataInvoked = whatDataInvoked;
	}

	public void setWaitingText2loading(){
		waitingTextMsg = "Waiting for eGPS to loading the tree...";
	}
}
