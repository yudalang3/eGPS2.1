package module.evoldist.view.contorl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.common.base.Strings;

import egps2.panels.dialog.SwingDialog;
import egps2.EGPSProperties;
import module.evoldist.view.gui.DistanceMatrixParameterMain;
import module.evoldist.view.gui.EGPSDistanceMatrixJTableHolder;
import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;
import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;
import module.webmsaoperator.webIO.DistMatrixTextInput;

public class VOICM4EvolDist extends VersatileOpenInputClickAbstractGuiBase {

	DistanceMatrixParameterMain main;

	public VOICM4EvolDist(DistanceMatrixParameterMain main) {
		this.main = main;
	}

	@Override
	public String getExampleText() {
		
		StringBuilder sb = EGPSProperties.getSpecificationHeader();
		sb.append("\n");
		sb.append("# Note: please check the format.\n");
		sb.append("$inputPath=config/bioData/example/testGeneticDistance.dist");
		return sb.toString();
	}

	@Override
	public void execute(String inputs) {
		VoiceParameterParser parser = new VoiceParameterParser();
		List<String> stringsFromLongSingleLine = parser.getStringsFromLongSingleLine(inputs);
		Map<String, LinkedList<String>> map = parser.parseInputString4organization(stringsFromLongSingleLine);

		LinkedList<String> linkedList = map.get("$inputPath");
		if (linkedList == null) {
			SwingDialog.showErrorMSGDialog("Error", "$inputPath must be defined.");
			return;
		}

		String filePath = parser.getStringAfterEqualStr(linkedList.get(0));
		
		if (Strings.isNullOrEmpty(filePath)) {
			SwingDialog.showErrorMSGDialog("Error", "$inputPath must be defined.");
			return;
		}

		DistMatrixTextInput d = new DistMatrixTextInput(filePath);
		double[][] finalDistance = d.getDistanceMatrix();
		String[] otu_names = d.getOTU_names();

		SwingUtilities.invokeLater(() -> {

			EGPSDistanceMatrixJTableHolder jtable = new EGPSDistanceMatrixJTableHolder(finalDistance, otu_names);
			jtable.setNumberAfterTheDecimalPoint(main.getDecimalPlacesValue());
			jtable.setDispalyedLayoutStyle(main.getMatrixIndex());
			jtable.setDisplayedCorlorMode(main.getDisplayedColorModeIndex());
			jtable.updateJtableAndLegendState();

			main.setJtable(jtable);
			JPanel tabbedPanel = main.getTabbedPanel();
			tabbedPanel.removeAll();
			tabbedPanel.add(jtable);
			tabbedPanel.revalidate();
		});

	}

}
