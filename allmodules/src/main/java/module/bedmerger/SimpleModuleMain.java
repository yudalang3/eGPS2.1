package module.bedmerger;

import java.awt.BorderLayout;

import javax.swing.border.EmptyBorder;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class SimpleModuleMain extends ModuleFace{

	private final SimpleModuleController controller ;
	private SimpleLeftControlPanel leftControlPanel;
	
	
	protected SimpleModuleMain(IModuleLoader load) {
		super(load);
		setBorder(new EmptyBorder(15, 25, 15, 15));
		controller = new SimpleModuleController(this);
		setLayout(new BorderLayout());
		
		leftControlPanel = new SimpleLeftControlPanel(controller);
		add(leftControlPanel,BorderLayout.WEST);
	}
	

	public SimpleModuleController getController() {
		return controller;
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
		return new String[] {"Merge bed records"};
	}

	@Override
	protected void initializeGraphics() {
		
	}

}
