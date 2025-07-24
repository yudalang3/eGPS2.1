package module.evolview.treebarplot;

import module.evolview.treebarplot.io.ParamsAssignerAndParser4TreeBarPlot;
import module.evolview.treebarplot.io.TreeBarplotImportInforBean;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

public class VOICM4TreeBarPlot extends AbstractGuiBaseVoiceFeaturedPanel {

	private final GuiMain guiMain;

	private final ParamsAssignerAndParser4TreeBarPlot generator = new ParamsAssignerAndParser4TreeBarPlot();

	public VOICM4TreeBarPlot(GuiMain guiMain) {
		this.guiMain = guiMain;
	}

	@Override
	public String getExampleText() {
		String example = generator.getExampleString();
		return example;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {

	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		TreeBarplotImportInforBean importBeanInfor = generator.getImportBeanInfo(o);
		guiMain.evolTreeImportInfoBean = importBeanInfor;
		guiMain.initializeGraphics();
	}

}
