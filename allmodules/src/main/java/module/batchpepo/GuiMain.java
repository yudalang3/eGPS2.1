package module.batchpepo;

import java.util.List;

import com.google.common.collect.Lists;

import module.batchpepo.gui.ExtractUniquePeptideRunner;
import module.batchpepo.gui.GetOneGeneWithLongestTranscriptPanel;
import egps2.builtin.modules.voice.diytools.DIYToolSubTabModuleFace;
import egps2.builtin.modules.voice.template.ContainerModuleFace4Voice;
import egps2.builtin.modules.voice.template.SubTabJPanel4Voice;
import egps2.builtin.modules.voice.template.SubTabJPanel4VoiceCreator;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ContainerModuleFace4Voice {

	List<DIYToolSubTabModuleFace> listOfSubTabModuleFace = Lists.newArrayList();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
	}

	@Override
	protected void configSubTabs(List<SubTabJPanel4Voice> subTabs) {
		{
			ExtractUniquePeptideRunner subPanel = new ExtractUniquePeptideRunner();
			SubTabJPanel4VoiceCreator creator = new SubTabJPanel4VoiceCreator(this, subPanel);
			subTabs.add(creator);
		}
		{
			GetOneGeneWithLongestTranscriptPanel subPanel = new GetOneGeneWithLongestTranscriptPanel();
			SubTabJPanel4VoiceCreator creator = new SubTabJPanel4VoiceCreator(this, subPanel);
			subTabs.add(creator);
		}
	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Operate the protein sequences." };
	}

}
