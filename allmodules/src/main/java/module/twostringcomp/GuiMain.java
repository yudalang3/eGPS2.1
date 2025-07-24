package module.twostringcomp;

import java.awt.BorderLayout;
import java.util.List;

import egps2.frame.ModuleFace;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ModuleFace{
	
	WorkBanch4presenter comp = new WorkBanch4presenter(this);
	
	public GuiMain(IModuleLoader loder ) {
		super(loder);
		
		
		setLayout(new BorderLayout());
		
		add(comp, BorderLayout.CENTER);
	}


	public void loadResultWithInputStrings(List<String> list) {
		comp.setText4TopTextArea(list);
		comp.handle(list);
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
		return new String[] {"Pair-wise string comparison"};
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

