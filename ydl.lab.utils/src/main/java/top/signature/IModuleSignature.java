package top.signature;

/**
 * The top level signature of the remnant.
 */
public interface IModuleSignature {

	/**
	 * Get the description of the runner, HTML is supported. A.K.A. Tooltip
	 */
	String getShortDescription();

	/**
	 * The name of the remnant, for GUI remnant this is displayed on the tab.
	 * History: because the name of the method getName() already used by Swing JDK
	 */
	String getTabName();
}
