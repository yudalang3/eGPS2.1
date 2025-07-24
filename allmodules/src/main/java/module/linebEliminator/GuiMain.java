package module.linebEliminator;

import java.awt.BorderLayout;

import egps2.frame.ModuleFace;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ModuleFace{
	
	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());
		add(new WorkBanch4presenter(this), BorderLayout.CENTER);
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
		return new String[] {"Remove redundant break character"};
	}

	@Override
	protected void initializeGraphics() {
		
	}

}

