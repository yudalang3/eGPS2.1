package module.tablelikeview.io;

import module.tablelikeview.MainGUI;
import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;

public class VOICM4TableLikeView extends VersatileOpenInputClickAbstractGuiBase {
	private ImportAbstractParamsAssignerAndParser4TableLikeView importMapProduce4general2d = new ImportAbstractParamsAssignerAndParser4TableLikeView();
	private MainGUI mainFace;
	
	public VOICM4TableLikeView(MainGUI mainFace) {
		this.mainFace = mainFace;
	}

	@Override
	public String getExampleText() {
		String example = importMapProduce4general2d.getExampleString();
		return example;
	}

	@Override
	public void execute(String inputs) {
		WorkImportInfoBean importBean = importMapProduce4general2d.getImportBeanFromString(inputs);

		mainFace.loadingData(importBean);
	}

}
