package module.multiseq.alignment.trimmer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.multiseq.alignment.trimmer.SimpleModuleController;
import module.multiseq.alignment.trimmer.calculate.AlignmentTrimmer;
import module.multiseq.alignment.view.ModuleLauncher4MSA2AlignmentView;

public class SimpleLeftControlPanel extends JPanel {
	private static final long serialVersionUID = 6360716192941698962L;
	private JButton runAndSaveButton;
	private SimpleModuleController controller;

	public SimpleLeftControlPanel(SimpleModuleController controller) {
		this.controller = controller;

		setBackground(Color.WHITE);
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		JXTaskPaneContainer jxTaskPaneContainer = new JXTaskPaneContainer();
		jxTaskPaneContainer.setBackground(Color.WHITE);
		jxTaskPaneContainer.setBackgroundPainter(null);

		addJXTaskPanes(jxTaskPaneContainer);

		runAndSaveButton = new JButton("Run & Save data");
		runAndSaveButton.setFont(globalFont);
		runAndSaveButton.setToolTipText("The file will be save as .trimmed.fas in the same directory!");
		runAndSaveButton.addActionListener(e -> {
			String path = controller.getInputFilePath();
			if (path.isEmpty()) {
				SwingDialog.showErrorMSGDialog("Input error", "You need to import file first!");
			} else {
				String refSequenceName = controller.getRefSequenceName();
				int refSequenceStartPos = controller.getRefSequenceStartPos();
				int refSequenceEndPos = controller.getRefSequenceEndPos();
				try {
					new AlignmentTrimmer().trimAlignmentAndWrite2FileWithSuffix(refSequenceName, refSequenceStartPos,
							refSequenceEndPos, path);
					SwingDialog.showInfoMSGDialog("Running success", "File successful created in \n" + path);
				} catch (Exception e1) {
					SwingDialog.showErrorMSGDialog("Running error", e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		jxTaskPaneContainer.add(runAndSaveButton);

		JButton runAndGo2AlignmentViewButton = new JButton("Run & Go to alignment view");
		runAndGo2AlignmentViewButton.setFont(globalFont);
		runAndGo2AlignmentViewButton.addActionListener(e -> {
			String path = controller.getInputFilePath();
			if (path.isEmpty()) {
				SwingDialog.showErrorMSGDialog("Input error", "You need to import file first!");
			} else {
				String refSequenceName = controller.getRefSequenceName();
				int refSequenceStartPos = controller.getRefSequenceStartPos();
				int refSequenceEndPos = controller.getRefSequenceEndPos();
				try {
					new AlignmentTrimmer().trimAlignmentAndWrite2FileWithSuffix(refSequenceName, refSequenceStartPos,
							refSequenceEndPos, path);
					File file = new File(path + ".trimmed.fas");
					new ModuleLauncher4MSA2AlignmentView().msaData2AlignmentView(Arrays.asList(file));
					file.delete();
				} catch (Exception e1) {
					SwingDialog.showErrorMSGDialog("Running error", e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		jxTaskPaneContainer.add(runAndGo2AlignmentViewButton);

		add(jxTaskPaneContainer, BorderLayout.CENTER);

	}

	private void addJXTaskPanes(JXTaskPaneContainer jxTaskPaneContainer) {
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		JXTaskPane jfJxTaskPane2 = new JXTaskPane();
		jfJxTaskPane2.setAlignmentX(Component.LEFT_ALIGNMENT);
		jfJxTaskPane2.setTitle("Data import ");
		jfJxTaskPane2.setFont(globalFont);
		DataImportPanel dataImportPanel = new DataImportPanel(controller);
		jfJxTaskPane2.add(dataImportPanel);
		jxTaskPaneContainer.add(jfJxTaskPane2);

		JXTaskPane jfJxTaskPane1 = new JXTaskPane();
		jfJxTaskPane1.setAlignmentX(Component.LEFT_ALIGNMENT);
		jfJxTaskPane1.setTitle("Parameters ");
		jfJxTaskPane1.setFont(globalFont);
		ParametersPanel parametersPanel = new ParametersPanel(controller);
		jfJxTaskPane1.add(parametersPanel);
		jxTaskPaneContainer.add(jfJxTaskPane1);

	}

	public void enableAllGUIComponents(boolean b) {
		runAndSaveButton.setEnabled(b);
	}

}
