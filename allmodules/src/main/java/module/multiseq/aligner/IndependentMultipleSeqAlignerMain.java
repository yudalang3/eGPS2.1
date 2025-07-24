package module.multiseq.aligner;

import module.multiseq.aligner.gui.IndependentMafftGUIPanel;
import egps2.modulei.IModuleLoader;


/**
 * 这个类相比于它的父类，这里多了一个输入文件的框。
 * 父类其实是没有必要的，因为没办法输入数据
 * @author yudal
 *
 */
@SuppressWarnings("serial")
public class IndependentMultipleSeqAlignerMain extends MultipleSeqAlignerMain{

	protected IndependentMultipleSeqAlignerMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
	}

	@Override
	protected void addTabbedPanelContents() {
		tabbedPane.add("MAFFT", new IndependentMafftGUIPanel(this));
		//tabbedPane.add("ClustalW", new IndependentClustalwGUIPanel(this));
	}
}
