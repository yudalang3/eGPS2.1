package module.multiseq.gene2msa;

import java.util.Map;

import module.evoltre.pipline.TreeParameterHandler;
import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;

public class VOICM4gene2MSA extends VersatileOpenInputClickAbstractGuiBase {

	private GuiMain guiMain;

	public VOICM4gene2MSA(GuiMain guiMain) {
		this.guiMain = guiMain;
	}

	@Override
	public String getExampleText() {
		return "Eample";
	}

	@Override
	public void execute(String inputs) throws Exception {
		Map<String, String> buildTreeParametersMap = new TreeParameterHandler().getBuildTreeParametersMap();
		PLWeb2ObtainAlignment plWeb2ObtainAlignment = new PLWeb2ObtainAlignment(buildTreeParametersMap, "INS");
		guiMain.registerRunningTask(plWeb2ObtainAlignment);
		
	}

}
