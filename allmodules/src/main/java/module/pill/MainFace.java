package module.pill;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.util.SaveUtil;
import egps2.frame.ModuleFace;
import module.pill.core.SkeletonMaker;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class MainFace extends ModuleFace{
	
	
	private JSplitPane contentPanel;
	private SkeletonMaker skeletonMaker;

	MainFace(IModuleLoader moduleLoader) {
		super(moduleLoader);
		
		setLayout(new BorderLayout());
		
		skeletonMaker = new SkeletonMaker();
		contentPanel = skeletonMaker.launchInFrame();
		
		add(contentPanel, BorderLayout.CENTER);
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
		return true;
	}

	@Override
	public void importData() {
		try {
			skeletonMaker.importData();
		} catch (Exception e) {
			SwingDialog.showErrorMSGDialog("Parse error", e.getMessage());
		}


	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		JComponent rightComponent = (JComponent) contentPanel.getRightComponent();
		new SaveUtil().saveData(rightComponent, getClass());
	}

	@Override
	public void initializeGraphics() {
		
	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Import KEGG pathway", "Draw pathway elements" };
	}

}
