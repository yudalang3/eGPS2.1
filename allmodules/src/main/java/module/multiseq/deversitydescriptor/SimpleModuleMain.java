package module.multiseq.deversitydescriptor;

import java.awt.BorderLayout;

import egps2.frame.ModuleFace;
import module.multiseq.deversitydescriptor.gui.SimpleLeftControlPanel;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

public class SimpleModuleMain extends ModuleFace {

	private final SimpleModuleController controller;
	private SimpleLeftControlPanel leftControlPanel;

	protected SimpleModuleMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		controller = new SimpleModuleController(this);
		setLayout(new BorderLayout());

		leftControlPanel = new SimpleLeftControlPanel(controller);
		add(leftControlPanel, BorderLayout.CENTER);
	}

	public String[] getTeamAndAuthors() {
		return controller.getTeamAndAuthors();
	}

	public void enableAllGUIComponents(boolean b) {
		leftControlPanel.enableAllGUIComponents(b);

	}

	@Override
	public boolean closeTab() {
		return false;
	}

	@Override
	public void changeToThisTab() {

	}

	@Override
	public boolean canImport() {
		return false;
	}

	@Override
	public void importData() {

	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void exportData() {

	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "See diversity with text" };
	}

	@Override
	protected void initializeGraphics() {

	}
	
	@Override
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The data is loading from the import dialog.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The functionality is powered by the eGPS software.";
			}
		};
		return iInformation;
	}

}
