package module.multiseq.aligner.gui;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.frame.gui.comp.DataImportPanel_OneTypeOneFile;
import module.multiseq.aligner.MultipleSeqAlignerMain;

@SuppressWarnings("serial")
public class IndependentClustalwGUIPanel extends ClustalwGUIPanel{

	public IndependentClustalwGUIPanel(MultipleSeqAlignerMain alignmentMain) {
		super(alignmentMain);
	}

	private DataImportPanel_OneTypeOneFile dataImportPanel;
	

	@Override
	protected void configTaskPanes(JXTaskPaneContainer taskPaneContainer) {
		JXTaskPane aJXTaskPane = new JXTaskPane();
		aJXTaskPane.setTitle("Import file");
		aJXTaskPane.setFont(titleFont);
		
		dataImportPanel = new DataImportPanel_OneTypeOneFile(this.getClass());
		aJXTaskPane.add(dataImportPanel);
		
		taskPaneContainer.add(aJXTaskPane);
		super.configTaskPanes(taskPaneContainer);
	}
	
	@Override
	protected void obtainInputFiles() {
		Optional<File> inputFile = dataImportPanel.getInputFile();
		if (inputFile.isPresent()) {
			setInputFiles(Arrays.asList(inputFile.get()));
		}
	}

}
