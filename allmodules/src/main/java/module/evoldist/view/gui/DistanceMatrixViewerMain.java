package module.evoldist.view.gui;

import org.jdesktop.swingx.JXTaskPaneContainer;

import module.evoldist.view.contorl.VOICM4EvolDist;
import egps2.modulei.IModuleLoader;

/**
 * 从历史记录点击一个曾经运算过的距离矩阵后调用此面板
 * 
 * @author
 *
 */
public class DistanceMatrixViewerMain extends DistanceMatrixParameterMain {

	public DistanceMatrixViewerMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
	}

	private static final long serialVersionUID = -4820508801175534450L;

	@Override
	protected void addJXTaskPanels(JXTaskPaneContainer taskPaneContainer) {
		taskPaneContainer.add(getDecimalJXTaskPane());
		taskPaneContainer.add(getDisplayJXTaskPane());

	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		VOICM4EvolDist voicm4EvolDist = new VOICM4EvolDist(this);
		voicm4EvolDist.doUserImportAction();
	}

	@Override
	protected void initializeGraphics() {
		importData();
	}
}
