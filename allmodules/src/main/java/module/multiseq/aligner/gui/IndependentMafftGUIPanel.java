package module.multiseq.aligner.gui;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.frame.gui.comp.DataImportPanel_OneTypeOneFile;
import module.multiseq.aligner.MultipleSeqAlignerMain;

@SuppressWarnings("serial")
public class IndependentMafftGUIPanel extends MafftGUIPanel {

	private DataImportPanel_OneTypeOneFile dataImportPanel;

	public IndependentMafftGUIPanel(MultipleSeqAlignerMain alignmentMain) {
		super(alignmentMain);
	}

	@Override
	protected void configTaskPanes(JXTaskPaneContainer taskPaneContainer) {
		JXTaskPane aJXTaskPane = new JXTaskPane();
		aJXTaskPane.setTitle("Import file");
		aJXTaskPane.setFont(titleFont);

		dataImportPanel = new DataImportPanel_OneTypeOneFile(this.getClass());
		dataImportPanel.setTooltipContents(Arrays.asList("The input format need to be fasta format.",
				"If your path contain any non-english words, please choose the right font in preference."));
		aJXTaskPane.add(dataImportPanel);

		taskPaneContainer.add(aJXTaskPane);
		super.configTaskPanes(taskPaneContainer);
	}

	@Override
	protected void obtainInputFiles() {
		Optional<File> inputFile = dataImportPanel.getInputFile();
		if (inputFile.isPresent()) {
			File file = inputFile.get();
			if (file.isDirectory()) {
				setInputFiles(Arrays.asList(file.listFiles()));
			} else {
				setInputFiles(Arrays.asList(file));
			}

		}
	}

}
