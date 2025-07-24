package module.mutationpre;

import java.util.List;

import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

public class ImportDataHandler extends VersatileOpenInputClickAbstractGuiBase {

	private MyWorkStudio myWorkStudio;
	private SimplestModuleController controller;

	public ImportDataHandler(MyWorkStudio myWorkStudio, SimplestModuleController controller) {
		this.myWorkStudio = myWorkStudio;
		this.controller = controller;
	}

	@Override
	public String getExampleText() {
		return myWorkStudio.getExampleStr();
	}
	
	@Override
	protected int getNumberOfExamples() {
		return 5;
	}

	@Override
	public void execute(String inputs) {
		List<String> strs = new VoiceParameterParser().getStringsFromLongSingleLine(inputs);
		controller.refreshGraphcs(strs);
		
		controller.getMain().invokeTheFeatureMethod(0);
	}

}
