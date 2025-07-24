package module.treetanglegram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.frame.gui.comp.DataImportPanel_OneTypeOneFile;
import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class SimpleLeftControlPanel extends JPanel{
	private TanglegramController controller;
	private DataImportPanel_OneTypeOneFile dataImportPanel1;
	private DataImportPanel_OneTypeOneFile dataImportPanel2;
	private JButton viewTanglegramButton;
	private ParametersPanel parametersPanel;
	

	public SimpleLeftControlPanel(TanglegramController controller) {
		this.controller = controller;
		
		setBackground(Color.WHITE);
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		
		JXTaskPaneContainer jxTaskPaneContainer = new JXTaskPaneContainer();
		jxTaskPaneContainer.setBackground(Color.WHITE);
		jxTaskPaneContainer.setBackgroundPainter(null);
		
		addJXTaskPanes(jxTaskPaneContainer);
		
		
		viewTanglegramButton = new JButton("View tanglegram");
		viewTanglegramButton.setFont(globalFont);
		viewTanglegramButton.addActionListener( e ->{
			
			Optional<File> inputFile1 = dataImportPanel1.getInputFile();
			Optional<File> inputFile2 = dataImportPanel2.getInputFile();
			boolean checkFile = DataImportPanel_OneTypeOneFile.checkFile(inputFile1);
			if (!checkFile) {
				return;
			}
			checkFile = DataImportPanel_OneTypeOneFile.checkFile(inputFile2);
			if (!checkFile) {
				return;
			}
			
			
			Font nameFont = parametersPanel.getNameFont();
			String outgroupString = null;
			
			if (parametersPanel.hasOutgroup()) {
				outgroupString = parametersPanel.getOutgroupString();
			}
			controller.loadTab(inputFile1.get(),inputFile2.get(),nameFont, outgroupString);
			controller.getMain().invokeTheFeatureMethod(0);
			
		});
		jxTaskPaneContainer.add(viewTanglegramButton);
		
		add(jxTaskPaneContainer,BorderLayout.CENTER);
		
	}
	
	private void addJXTaskPanes(JXTaskPaneContainer jxTaskPaneContainer) {
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		
		JXTaskPane jfJxTaskPane2 = new JXTaskPane();
		jfJxTaskPane2.setAlignmentX(Component.LEFT_ALIGNMENT);
		jfJxTaskPane2.setTitle("Data import ");
		jfJxTaskPane2.setFont(defaultTitleFont);
		
		dataImportPanel1 = new DataImportPanel_OneTypeOneFile(this.getClass());
		dataImportPanel2 = new DataImportPanel_OneTypeOneFile(this.getClass());
		
		List<String> formatStrings = Arrays.asList("Please input nwk(NH) format file.");
		jfJxTaskPane2.add(dataImportPanel1);
		jfJxTaskPane2.add(dataImportPanel2);
		dataImportPanel1.setTooltipContents(formatStrings);
		dataImportPanel2.setTooltipContents(formatStrings);
		jxTaskPaneContainer.add(jfJxTaskPane2);
		
		JXTaskPane jfJxTaskPane1 = new JXTaskPane();
		jfJxTaskPane1.setAlignmentX(Component.LEFT_ALIGNMENT);
		jfJxTaskPane1.setTitle("Parameters ");
		jfJxTaskPane1.setFont(defaultTitleFont);
		parametersPanel = new ParametersPanel(controller);
		jfJxTaskPane1.add(parametersPanel);
		jxTaskPaneContainer.add(jfJxTaskPane1);
		
	}




	public void enableAllGUIComponents(boolean b) {
		viewTanglegramButton.setEnabled(b);
	}

}
