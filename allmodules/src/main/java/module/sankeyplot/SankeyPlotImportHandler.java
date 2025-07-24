package module.sankeyplot;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;
import module.vennplot.model.Element;
import module.vennplot.model.SetItem;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

public class SankeyPlotImportHandler extends VersatileOpenInputClickAbstractGuiBase {

	private VOICMSankeyPlotEGPS2 voicmSankeyPlotEGPS2;

	public SankeyPlotImportHandler(VOICMSankeyPlotEGPS2 voicmSankeyPlotEGPS2) {
		this.voicmSankeyPlotEGPS2 = voicmSankeyPlotEGPS2;
	}

	@Override
	public String getExampleText() {
		InputStream stream = getClass().getResourceAsStream("example.input.txt");
		String ret = null;
		try {
			ret = EGPSFileUtil.getContentFromInputStreamAsString(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void execute(String inputs) {
		List<String> contents = new VoiceParameterParser().getStringsFromLongSingleLine(inputs);
		int length = contents.size();
		String firstLine = contents.get(0).trim();
		String[] splits = EGPSStringUtil.split(firstLine, '\t');

		SetItem[] setItems = new SetItem[splits.length];
		for (int i = 0; i < splits.length; i++) {
			SetItem setItem = new SetItem();
			setItem.setName(splits[i]);
			Element[] elements = new Element[length - 1];
			setItem.setSetElements(elements);
			setItems[i] = setItem;
			for (int j = 1; j < length; j++) {
				String line = contents.get(j).trim();
				String[] lineSplits = EGPSStringUtil.split(line, '\t');
				
				Element element = new Element(lineSplits[i]);
				element.setIfGenomicRegion(false);
				elements[j - 1]  = element;
			}

		}
		
		voicmSankeyPlotEGPS2.loadTheSankyPlot(setItems);
		voicmSankeyPlotEGPS2.whatDataInvoked = "Data is manually input to the VOICM import box.";
		
		Component root = SwingUtilities.getRoot(voicmSankeyPlotEGPS2);
		if (root instanceof JDialog) {
			((JDialog) root).dispose();
		}

	}

}
