package module.correlation4wnt;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import utils.string.EGPSStringUtil;
import egps2.EGPSProperties;
import module.correlation4wnt.io.ParaModel;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

public class VoiceImporter extends AbstractGuiBaseVoiceFeaturedPanel {

	private GuiMain mainFace;

	public VoiceImporter(GuiMain mainFace) {
		this.mainFace = mainFace;

	}


	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("input.tsv",
				EGPSProperties.PROPERTIES_DIR + "/bioData/example/reordered.data.wnt.tsv",
				"The tsv files of input data");
		mapProducer.addKeyValueEntryBean("cat1.col.names", "name;category", "Could also be name;cat1;color");
		mapProducer.addKeyValueEntryBean("cat2.col.names", "name;cat2;value", "The inter-connecting lines");
		mapProducer.addKeyValueEntryBean("expMatrix.col.names", "name",
				"The remains will be used as the data samples data.");
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {
		String path = o.getSimplifiedString("input.tsv");
		String str1 = o.getSimplifiedString("cat1.col.names");
		String str2 = o.getSimplifiedString("cat2.col.names");
		String str3 = o.getSimplifiedString("expMatrix.col.names");
		PaintingPanel paintingPanel = mainFace.getPaintingPanel();

		ParaModel paraModel = new ParaModel();
		paraModel.setExpMatrixCol(Arrays.asList(str3));
		paraModel.setCat1Col(Arrays.asList(EGPSStringUtil.split(str1, ';')));
		paraModel.setCat2Col(Arrays.asList(EGPSStringUtil.split(str2, ';')));

		DataModel dataModel = new DataModel(path, paraModel);
		paintingPanel.dataModel = dataModel;

		SwingUtilities.invokeLater(() -> {
			paintingPanel.repaint();
		});

	}

}
