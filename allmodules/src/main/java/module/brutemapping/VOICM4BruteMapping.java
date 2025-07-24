package module.brutemapping;

import java.util.List;

import module.brutemapping.algo.ChromosomeLevelMapper;
import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import egps2.modulei.RunningTask;

public class VOICM4BruteMapping extends VersatileOpenInputClickAbstractGuiBase implements RunningTask {

	Importer4BruteMappingAbstract importer = new Importer4BruteMappingAbstract();
	private GuiMain guiMain;
	ImportDataBean importDataInformation;

	private ChromosomeLevelMapper mapper = new ChromosomeLevelMapper();

	public VOICM4BruteMapping(GuiMain guiMain) {
		this.guiMain = guiMain;
	}

	@Override
	public String getExampleText() {
		return importer.getExampleString();
	}

	@Override
	public void execute(String inputs) throws Exception {
		importDataInformation = importer.getImportDataInformation(inputs);

		guiMain.registerRunningTask(this);
	}

	@Override
	public boolean isTimeCanEstimate() {
		return false;
	}

	@Override
	public int processNext() throws Exception {
		mapper.setImportDataInformation(importDataInformation);
		mapper.run();

		List<String> output = mapper.getOutput();
		if (guiMain == null) {
			for (String string : output) {
				System.out.println(string);
			}
		} else {
			guiMain.setText4Console(output);

		}
		return PROGRESS_FINSHED;
	}


}
