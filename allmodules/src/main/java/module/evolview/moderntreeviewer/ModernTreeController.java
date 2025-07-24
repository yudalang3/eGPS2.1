package module.evolview.moderntreeviewer;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.GeneFamilyMainFace;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;

public class ModernTreeController extends GeneFamilyController {

	final MTreeViewMainFace mainFace;

	public ModernTreeController(GeneFamilyMainFace main) {
		this(main, null);
	}

	public ModernTreeController(GeneFamilyMainFace main, MTreeViewMainFace main2) {
		super(main);
		mainFace = main2;
		
	}

	@Override
	public void refreshPhylogeneticTree() {
		mainFace.phylogeneticTreePanel.refreshViewPort();
	}
	
	@Override
	public void reCalculateTreeLayoutAndRefreshViewPort() {
		mainFace.fitFrameRefresh();
		mainFace.invokeTheFeatureMethod(0);
	}
	
	@Override
	public PhylogeneticTreePanel getSelectedPhylogeneticTreePanel() {
		return mainFace.getSelectedPhylogenticTreePanel();
	}
	
	@Override
	public void showLeaveLabel(boolean selected) {
		super.showLeaveLabel(selected);
		mainFace.invokeTheFeatureMethod(2);
	}
	
	@Override
	public void fireLayoutChanged() {
		super.fireLayoutChanged();
		mainFace.invokeTheFeatureMethod(3);
	}
}
