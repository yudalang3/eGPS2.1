package module.multiseqview;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

import module.multiseqview.io.ImportInfoBean4gBrowser;
import module.multiseqview.io.MapperOfMultiSeqView;
import module.multiseqview.model.Data4SequenceStructureView;
import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

public class ImportDataHander extends VersatileOpenInputClickAbstractGuiBase {
	private MapperOfMultiSeqView requiredDataBuilder = new MapperOfMultiSeqView();

	SequenceStructureViewMain main;

	public ImportDataHander(SequenceStructureViewMain main) {
		this.main = main;
	}


	@Override
	public String getExampleText() {
		String generateListOfExample = requiredDataBuilder.getExampleString();

		return generateListOfExample;
	}

	@Override
	public void execute(String inputs) throws IOException {
		List<String> stringsFromLongSingleLine = new VoiceParameterParser().getStringsFromLongSingleLine(inputs);
		ImportInfoBean4gBrowser buildFromListOfString = requiredDataBuilder.buildFromListOfString(stringsFromLongSingleLine);

		Data4SequenceStructureView data = requiredDataBuilder.prepareData(buildFromListOfString);
		if (data.getSequences().isEmpty()) {
			throw new InputMismatchException("The input file do not contain any sequences, pleace check your inputs.");
		}else {
			main.getController().setData(data);
		}
	}

}
