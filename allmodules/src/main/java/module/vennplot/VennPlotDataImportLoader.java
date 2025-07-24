package module.vennplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import com.jidesoft.swing.JideTabbedPane;

import egps2.panels.dialog.SwingDialog;
import egps2.frame.gui.comp.DataImportPanel_OneTypeMultiFiles_WithInputBox;
import egps2.UnifiedAccessPoint;
import module.sankeyplot.gui.SankeyPlotDataInputPanel;
import module.vennplot.io.VOICM4VennPlot;
import module.vennplot.model.SetItem;
import module.vennplot.util.InputDataParser;

@SuppressWarnings("serial")
public class VennPlotDataImportLoader extends JPanel {

	private DataImportPanel_OneTypeMultiFiles_WithInputBox dataImportPanel;

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private ParameterPanel parameterPanel;

	private InputDataParser inputDataParser = new InputDataParser();
	private VennPlotMain vennPlotMain;

	protected VennPlotDataImportLoader(VennPlotMain vennPlotMain) {
		this.vennPlotMain = vennPlotMain;
		setLayout(new BorderLayout());
		initializeGraphics();
	}

	protected void configurateImportWay2TaskPanes(JXTaskPaneContainer taskPaneContainer) {
		JXTaskPane aJXTaskPane = new JXTaskPane();
		aJXTaskPane.setTitle("Import Data");
		aJXTaskPane.setFont(titleFont);

		dataImportPanel = new DataImportPanel_OneTypeMultiFiles_WithInputBox(this.getClass());
		dataImportPanel.setPreferredSize(new Dimension(800, 360));
		dataImportPanel.getButtonExample4ImportContent().setVisible(false);

		dataImportPanel.setContentsOfDirectArea(Arrays.asList("set1	a	b	c	d	e	f	g",
				"set2	a	b	c	h	i	j", "set3	a	b	c	k	l	m	n"));
		aJXTaskPane.add(dataImportPanel);

		taskPaneContainer.add(aJXTaskPane);

		// parameter panel

		parameterPanel = new ParameterPanel();
		parameterPanel.setPreferredSize(new Dimension(dataImportPanel.getWidth(), 190));
		parameterPanel.setIndependentVennPlotLoader0509(this);

		JXTaskPane aJXTaskPane2 = new JXTaskPane();
		aJXTaskPane2.setTitle("Parameter panel");
		aJXTaskPane2.setFont(titleFont);
		aJXTaskPane2.add(parameterPanel);

		taskPaneContainer.add(aJXTaskPane2);

		dataImportPanel.setDirectContentConsumer(list -> {

			List<String> stateStrings = new LinkedList<>();
			stateStrings.add("Number of sets is " + list.size());
			for (String string : list) {
				String[] split = string.split("\t", -1);
				stateStrings.add(split[0] + " : has " + (split.length - 1) + " count");
			}

			parameterPanel.setStatement(stateStrings);
		});

		dataImportPanel.setInputFileListConsumer(list -> {

			if (list.isEmpty() || !new File(list.get(0)).exists()) {
				return;
			}

			List<String> stateStrings = new LinkedList<>();
			stateStrings.add("Number of files is " + list.size());
			for (String string : list) {
				List<String> readLines = null;
				File file = new File(string);

				if (file.exists()) {

					try {
						readLines = FileUtils.readLines(file,StandardCharsets.UTF_8);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					stateStrings.add("Number of set in file  is " + readLines.size());
				}
			}

			parameterPanel.setStatement(stateStrings);
		});
		dataImportPanel.setInputdirConsumer(file -> {

			if (!file.exists()) {
				return;
			}

			File[] list = file.listFiles();

			List<String> stateStrings = new LinkedList<>();

			stateStrings.add("Number of files in dir is " + list.length);
			for (File string : list) {

				if (string.exists()) {

					List<String> readLines = null;
					try {
						readLines = FileUtils.readLines(string,StandardCharsets.UTF_8);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					stateStrings.add("Number of set in file " + string.getName() + " is " + readLines.size());
				}
			}

			parameterPanel.setStatement(stateStrings);
		});

		List<String> inputFormatStatement = getFormatStatement();
		dataImportPanel.setDialogContent4inputFormat(inputFormatStatement);

		List<String> formatTooltips = new LinkedList<>();
		formatTooltips.add("Direct import the content of your target format.");
		formatTooltips.add("Please input content with tab separator and first column is the set name.");
		dataImportPanel.setTooltipContents4importContent(formatTooltips);

		JButton jButton = new JButton("View venn diagram");
		jButton.setFont(defaultFont);

		jButton.addActionListener(e -> {
			new Thread(() -> {
				loadTheTab();
				
				Component root = SwingUtilities.getRoot(this);
				if (root instanceof JDialog) {
					((JDialog) root).dispose();
				}
			}).start();
		});
		taskPaneContainer.add(jButton);

	}

	private List<String> getFormatStatement() {
		List<String> ret = new LinkedList<>();
		ret.add("The input format of this remnant is a text file separated by a Tab separator, ");
		ret.add("which can be divided into two specific types according to the different set elements. ");
		ret.add("The first is the String type, below is an example content:  ");
		ret.add("");
		ret.add("Set1	a	b	c	d	e	f	g");
		ret.add("Set2	a	b	c	h	i	j");
		ret.add("Set3	a	b	c	k	l	m	n");
		ret.add("");

		ret.add("The first column is fixed as a collection name, and there are several columns as specific elements.");
		ret.add("The second is the Genomic Region type, below is an example content:  ");
		ret.add("");
		ret.add("Set1	chr1,100,201	chr1,200,301	chr2,301,401	chr2,501,600");
		ret.add("Set2	chr2,301,401	chr2,501,600	chr3,300,401	chr4,450,500");
		ret.add("Set3	chr2,301,401	chr4,450,500");
		ret.add("");
		ret.add("This type of difference between String is that all elements are a comma-separated genomic region.");

		return ret;
	}

	private void loadTheTab() {
		boolean importFiles = dataImportPanel.isImportFiles();

		if (importFiles) {
			List<File> inputFile = dataImportPanel.getInputFile();

			if (!DataImportPanel_OneTypeMultiFiles_WithInputBox.checkFile(inputFile)) {
				return;
			}

			for (File file : inputFile) {
				try {
					List<String> readLines = FileUtils.readLines(file,StandardCharsets.UTF_8);
					loadVennPlot(readLines);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		} else {
			String content = dataImportPanel.getDirectInputContent();

			String[] split = content.split("\n");
			List<String> asList = Arrays.asList(split);

			loadVennPlot(asList);

		}

	}

	private void loadVennPlot(List<String> asList) {
		SetItem[] setItems = inputDataParser.parseData(asList, !parameterPanel.isGenomicRegionDataType());
		loadData4thisModule(setItems);
		
	}

	public void loadData4thisModule(SetItem[] setItems) {
		if (setItems == null) {
			String errorString = inputDataParser.getErrorString();
			SwingDialog.showErrorMSGDialog("Data error", errorString);
		} else {
			vennPlotMain.setSetItems(setItems);
			vennPlotMain.initializeAction();
			
			vennPlotMain.invokeTheFeatureMethod(0);
			vennPlotMain.invokeTheFeatureMethod(1);
			vennPlotMain.invokeTheFeatureMethod(2);
		}
	}

	public VennPlotDataImportLoader getController() {
		return this;
	}

	public void displayNextExample(List<String> inforStrings) {
		dataImportPanel.setContentsOfDirectArea(inforStrings);

	}

	protected void initializeGraphics() {

		JideTabbedPane outterTabbedPane = new JideTabbedPane();
		outterTabbedPane.setSelectedTabFont(titleFont);
		outterTabbedPane.setFont(defaultFont);

		SankeyPlotDataInputPanel importView = new SankeyPlotDataInputPanel();
		importView.setAction4VisualizeButton(() -> {
			SetItem[] setItems = importView.getSetItems();
			loadData4thisModule(setItems);
		});
		outterTabbedPane.add("Import way1", importView);
		
		JScrollPane comp = new JScrollPane(getImportWay2Container());
		outterTabbedPane.add("Import way2", comp);
		
		VOICM4VennPlot voicm4VennPlot = new VOICM4VennPlot(this);
		JPanel importDataDialog = voicm4VennPlot.generateImportDataDialogGUI();
		
		outterTabbedPane.add("Versatile Open Input Click remnant", importDataDialog);
		
		add(outterTabbedPane, BorderLayout.CENTER);
		
		
	}
	
	private JXTaskPaneContainer getImportWay2Container() {
		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
		taskPaneContainer.setBackground(Color.WHITE);
		taskPaneContainer.setBackgroundPainter(null);
		configurateImportWay2TaskPanes(taskPaneContainer);
		return taskPaneContainer;
	}

}
