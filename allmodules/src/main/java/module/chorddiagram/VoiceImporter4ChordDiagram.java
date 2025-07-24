package module.chorddiagram;

import egps2.EGPSProperties;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;
import egps2.builtin.modules.voice.template.AbstractGuiBaseVoiceFeaturedPanel;

public class VoiceImporter4ChordDiagram extends AbstractGuiBaseVoiceFeaturedPanel {

	private MainFace mainFace;

	VoiceImporter4ChordDiagram(MainFace mainFace) {
		this.mainFace = mainFace;
	}

	@Override
	protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		String configPath = EGPSProperties.PROPERTIES_DIR;
		mapProducer.addKeyValueEntryBean("entity.info.path", configPath + "/bioData/chorddiagram/entity.info.tsv",
				"The tsv file that contains the entity information, entity could be gene, cds, protein.");
		mapProducer.addKeyValueEntryBean("entity.interaction.path",
				configPath + "/bioData/chorddiagram/entity.interaction.tsv",
				"The tsv file that contains the entity information, entity could be gene, cds, protein. Format is:\n# ### for one circle");

		mapProducer.addKeyValueEntryBean("number.of.column", "3",
				"If multiple chord diagram need to draw, this control how many columns to display.");
	}

	@Override
	protected void execute(OrganizedParameterGetter o) throws Exception {

		String path1 = o.getSimplifiedString("entity.info.path");
		String path2 = o.getSimplifiedString("entity.interaction.path");
//		System.out.println(path1 + "\t" + path2);
		int numOfColumn = o.getSimplifiedInt("number.of.column");
		mainFace.loadData(path1, path2, numOfColumn);
	}

}
