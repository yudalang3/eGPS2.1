package module.datetimecalculator;

import java.awt.BorderLayout;

import egps2.frame.ModuleFace;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ModuleFace {

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());
		WorkBanch4presenter comp = new WorkBanch4presenter(this);
		add(comp, BorderLayout.CENTER);
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
		return new String[] { "Compute the elapsed time", "Compute the datetime after duration", "Use yyyy-MM format" };
	}
	
	@Override
	protected void initializeGraphics() {

	}

	void recordComputEelapsedTime() {
		recordFeatureUsed4user("Compute the elapsed time");
	}
	void recordTheDatetimeAfterDuration() {
		recordFeatureUsed4user("Compute the datetime after duration");
	}
	void recordYYYYMMformat() {
		recordFeatureUsed4user("Use yyyy-MM format");
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
