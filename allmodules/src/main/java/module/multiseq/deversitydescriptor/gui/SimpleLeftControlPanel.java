package module.multiseq.deversitydescriptor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang3.tuple.Pair;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.evoltre.mutation.MutationOperator;
import module.multiseq.deversitydescriptor.SimpleModuleController;
import module.multiseq.deversitydescriptor.calculate.VariantDescriptorElement;
import module.multiseq.deversitydescriptor.calculate.VariantStateObtainer;

public class SimpleLeftControlPanel extends JPanel {
	private static final long serialVersionUID = 6360716192941698962L;
	private JButton runAndSaveButton;
	private SimpleModuleController controller;

	public SimpleLeftControlPanel(SimpleModuleController controller) {
		this.controller = controller;

		setBackground(Color.WHITE);
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		JXTaskPaneContainer jxTaskPaneContainer = new JXTaskPaneContainer();
		jxTaskPaneContainer.setBackground(Color.WHITE);
		jxTaskPaneContainer.setBackgroundPainter(null);
		
		JTextArea resultJTextArea = new JTextArea();
		resultJTextArea.setRows(20);
		resultJTextArea.setColumns(50);
		resultJTextArea.setFont(globalFont);

		addJXTaskPanes(jxTaskPaneContainer);

		VariantStateObtainer variantStateObtainer = new VariantStateObtainer();

		runAndSaveButton = new JButton("Execute");
		runAndSaveButton.setFont(globalFont);
		runAndSaveButton.setToolTipText("The file will be save as .trimmed.fas in the same directory!");
		runAndSaveButton.addActionListener(e -> {
			String path = controller.getInputFilePath();
			if (path.isEmpty()) {
				SwingDialog.showErrorMSGDialog("Input error", "You need to import file first!");
			} else {
				try {
					String refSequenceName = controller.getRefSequenceName();
					List<Pair<String, VariantDescriptorElement>> stateList = variantStateObtainer
							.getStateList(new File(path), refSequenceName);
					
					StringBuilder sb = new StringBuilder("#SeqName\tFirstPositionNotGap\tLastPositionNotGap\tmutations\n");
					for (Pair<String, VariantDescriptorElement> pair : stateList) {
						sb.append(pair.getLeft()).append("\t");
						VariantDescriptorElement right = pair.getRight();
						sb.append(right.getCountableFromOneBase()).append("\t");
						sb.append(right.getCountableToOneBase()).append("\t");
						String mutationList2FullString = MutationOperator.mutationList2FullString(right.getMutations());
						sb.append(mutationList2FullString);
						
						sb.append("\n");
					}
					
					resultJTextArea.setText(sb.toString());
					
					controller.getMain().invokeTheFeatureMethod(0);
				
				} catch (Exception e1) {
					resultJTextArea.setText("Running error:\n".concat(e1.getMessage()));
					e1.printStackTrace();
				}
			}
		});
		jxTaskPaneContainer.add(runAndSaveButton);

		setLayout(new BorderLayout(10, 10));
		add(jxTaskPaneContainer, BorderLayout.CENTER);


		TitledBorder titledBorder = new TitledBorder("Results:");
		titledBorder.setTitleFont(defaultTitleFont);
		resultJTextArea.setBorder(titledBorder);

		jxTaskPaneContainer.add(new JScrollPane(resultJTextArea));

	}

	private void addJXTaskPanes(JXTaskPaneContainer jxTaskPaneContainer) {
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		JXTaskPane jfJxTaskPane2 = new JXTaskPane();
		jfJxTaskPane2.setAlignmentX(Component.LEFT_ALIGNMENT);
		jfJxTaskPane2.setTitle("Data import ");
		jfJxTaskPane2.setFont(globalFont);
		jfJxTaskPane2.setCollapsed(false);
		DataImportPanel dataImportPanel = new DataImportPanel(controller);
		jfJxTaskPane2.add(dataImportPanel);
		jxTaskPaneContainer.add(jfJxTaskPane2);

		JXTaskPane jfJxTaskPane1 = new JXTaskPane();
		jfJxTaskPane1.setCollapsed(false);
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
