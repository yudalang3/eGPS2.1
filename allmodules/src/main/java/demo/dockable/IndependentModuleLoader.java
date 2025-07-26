package demo.dockable;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;
import egps2.modulei.ModuleClassification;

/**
 * Module loader for the dockable demo modules.
 * This class is responsible for loading and categorizing the dockable demo modules
 * in the eGPS software platform.
 */
public class IndependentModuleLoader implements IModuleLoader{

	/**
	 * Get the tab name for this module group
	 * @return The tab name string
	 */
	@Override
	public String getTabName() {
		return "VOICE demo: Dockable";
	}

	/**
	 * Get a short description of this module group
	 * @return The description string
	 */
	@Override
	public String getShortDescription() {
		return "a dockable VOICE panels to cultivate software usage habits, as well as developing procedure.";
	}


	/**
	 * Get the module face (GUI) for this module group
	 * @return The ModuleFace instance
	 */
	@Override
	public ModuleFace getFace() {
		return new GuiMain(this);
	}
	
	/**
	 * Get the category classification for this module group
	 * @return Array of category indices
	 */
	@Override
	public int[] getCategory() {
		int[] ret = ModuleClassification.getOneModuleClassification(
				
				ModuleClassification.BYFUNCTIONALITY_OPERATIONAL_WORKBENCH_INDEX,
				ModuleClassification.BYAPPLICATION_GENOMICS_INDEX,
				ModuleClassification.BYCOMPLEXITY_LEVEL_3_INDEX,
				ModuleClassification.BYDEPENDENCY_CROSS_MODULE_REFERENCED
		);
		return ret;
	}

}