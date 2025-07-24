package module.bedmerger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.panels.dialog.EGPSFileChooser;
import egps2.frame.gui.comp.DataImportPanel_OneTypeMultiFiles;
import egps2.EGPSProperties;
import egps2.UnifiedAccessPoint;
import module.bedmerger.calculate.BedFileManager;
import module.bedmerger.calculate.BedRecord;
import egps2.builtin.modules.largetextedi.MethodsForText2Editor;

public class SimpleLeftControlPanel extends JPanel {
	private static final long serialVersionUID = 6360716192941698962L;
	private JButton analysisButton;
	private Font globalFont;
	private JButton analysisButton2;
	private JButton analysisButton3;
	private SimpleModuleController controller;

	public SimpleLeftControlPanel(SimpleModuleController controller) {
		this.controller = controller;
		setBackground(Color.WHITE);
		globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		JXTaskPaneContainer jxTaskPaneContainer = new JXTaskPaneContainer();
		jxTaskPaneContainer.setBackground(Color.WHITE);
		jxTaskPaneContainer.setBackgroundPainter(null);

		addJXTaskPanes(jxTaskPaneContainer);

		add(jxTaskPaneContainer, BorderLayout.CENTER);
	}

	private void addJXTaskPanes(JXTaskPaneContainer jxTaskPaneContainer) {

		JXTaskPane jfJxTaskPane1 = new JXTaskPane();
		jfJxTaskPane1.setAlignmentX(Component.LEFT_ALIGNMENT);
		jfJxTaskPane1.setTitle("Data import ");
		jfJxTaskPane1.setFont(globalFont);
		DataImportPanel_OneTypeMultiFiles parametersPanel = new DataImportPanel_OneTypeMultiFiles(this.getClass());
		parametersPanel.setTooltipContents(Arrays.asList("Line start with 'track' and 'browser' will be ignored!",
				"See http://www.genome.ucsc.edu/FAQ/FAQformat.html#format1 for details."));
		jfJxTaskPane1.add(parametersPanel);
		jxTaskPaneContainer.add(jfJxTaskPane1);

		analysisButton = new JButton("Run & Save data");
		analysisButton.setFont(globalFont);
		analysisButton.addActionListener(e -> {

			new Thread(() -> {

				List<File> inputFile = parametersPanel.getInputFile();
				boolean checkFile = DataImportPanel_OneTypeMultiFiles.checkFile(inputFile);
				if (!checkFile) {
					return;
				}
				
				EGPSFileChooser egpsFileChooser = new EGPSFileChooser(SimpleLeftControlPanel.this.getClass());
				int showSaveDialog = egpsFileChooser.showSaveDialog();
				if (showSaveDialog != EGPSFileChooser.APPROVE_OPTION) {
					return;
				}

				File selectedFile = egpsFileChooser.getSelectedFile();

				enableAllGUIComponents(false);

				BedFileManager fileManager = new BedFileManager();
				List<BedRecord> allBedRecodes = new ArrayList<>(16384);

				for (File file : inputFile) {
					try {
						List<BedRecord> bedRecordsFromFile = fileManager.getBedRecordsFromFile(file.getAbsolutePath());
						allBedRecodes.addAll(bedRecordsFromFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				List<BedRecord> mergeBedRecords = fileManager.mergeBedRecords(allBedRecodes);

				try {
					fileManager.writeBedRecords2file(selectedFile, mergeBedRecords);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				enableAllGUIComponents(true);
			}).start();
		});
		analysisButton2 = new JButton("Run & Show in editor");
		analysisButton2.setFont(globalFont);
		analysisButton2.addActionListener(e -> {
			new Thread(() -> {
				List<File> inputFile = parametersPanel.getInputFile();
				boolean checkFile = DataImportPanel_OneTypeMultiFiles.checkFile(inputFile);
				if (!checkFile) {
					return;
				}
				
				File selectedFile = new File(EGPSProperties.PROPERTIES_DIR + "/temp_file_candelete.txt");

				enableAllGUIComponents(false);

				BedFileManager fileManager = new BedFileManager();
				List<BedRecord> allBedRecodes = new ArrayList<>(16384);

				for (File file : inputFile) {
					try {
						List<BedRecord> bedRecordsFromFile = fileManager.getBedRecordsFromFile(file.getAbsolutePath());
						allBedRecodes.addAll(bedRecordsFromFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				List<BedRecord> mergeBedRecords = fileManager.mergeBedRecords(allBedRecodes);

				try {
					fileManager.writeBedRecords2file(selectedFile, mergeBedRecords);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				new MethodsForText2Editor().addNewTextEditorTab(selectedFile, false);
				
				enableAllGUIComponents(true);
			}).start();
		});
		analysisButton3 = new JButton("Run bench mode (one input one output)");
		analysisButton3.addActionListener(e -> {
			new Thread(() -> {
				List<File> inputFile = parametersPanel.getInputFile();
				boolean checkFile = DataImportPanel_OneTypeMultiFiles.checkFile(inputFile);
				if (!checkFile) {
					return;
				}
				
				File selectedFile = new File(EGPSProperties.PROPERTIES_DIR + "/temp_file_candelete.txt");

				enableAllGUIComponents(false);

				BedFileManager fileManager = new BedFileManager();

				for (File file : inputFile) {
					try {
						List<BedRecord> bedRecordsFromFile = fileManager.getBedRecordsFromFile(file.getAbsolutePath());
					
						List<BedRecord> mergeBedRecords = fileManager.mergeBedRecords(bedRecordsFromFile);
						fileManager.writeBedRecords2file(selectedFile, mergeBedRecords);
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				new MethodsForText2Editor().addNewTextEditorTab(selectedFile, false);
				enableAllGUIComponents(true);
			}).start();
		});
		analysisButton3.setFont(globalFont);
		jxTaskPaneContainer.add(analysisButton);
		jxTaskPaneContainer.add(analysisButton2);
		jxTaskPaneContainer.add(analysisButton3);

	}

	public void enableAllGUIComponents(boolean b) {
		SwingUtilities.invokeLater(() -> {
			analysisButton.setEnabled(b);
			analysisButton2.setEnabled(b);
			analysisButton3.setEnabled(b);
			
			if (b) {
				setCursor(Cursor.getDefaultCursor());
			}else {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}
		});
		
		if (b) {
			controller.getMain().invokeTheFeatureMethod(0);
		}
	}

}
