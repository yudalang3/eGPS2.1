package module.evoldist.msa2distview;

import org.jdesktop.swingx.JXTaskPaneContainer;

import module.evoldist.view.gui.DistanceMatrixParameterMain;
import egps2.modulei.IModuleLoader;

/**
 * 从历史记录点击一个曾经运算过的距离矩阵后调用此面板
 * 
 * @author
 *
 */
public class MSA2DistanceMatrixViewerMain extends DistanceMatrixParameterMain {

	public MSA2DistanceMatrixViewerMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
	}

	private static final long serialVersionUID = -4820508801175534450L;

	@Override
	protected void addJXTaskPanels(JXTaskPaneContainer taskPaneContainer) {
		taskPaneContainer.add(getDecimalJXTaskPane());
		taskPaneContainer.add(getDisplayJXTaskPane());
		taskPaneContainer.add(getSetParametersTaskPane());

	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		VOICM4MSA2EvolDist voicm4EvolDist = new VOICM4MSA2EvolDist(this);
		voicm4EvolDist.doUserImportAction();
	}

	@Override
	protected void initializeGraphics() {
		super.initializeGraphics();
		this.importData();
	}
}
