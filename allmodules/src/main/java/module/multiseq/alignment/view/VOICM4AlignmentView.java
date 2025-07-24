package module.multiseq.alignment.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import module.multiseq.alignment.view.io.AlignmentImportBean;
import egps2.panels.dialog.SwingDialog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.MultiSplitLayout;

import com.jidesoft.swing.JideTabbedPane;

import egps2.frame.gui.comp.DataImportPanel_OneTypeMultiFiles_WithInputBox;
import egps2.frame.gui.comp.DataImportPanel_OneTypeOneFile_WithInputBox;
import egps2.Authors;
import egps2.UnifiedAccessPoint;
import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.MSA_DATA_FORMAT;
import msaoperator.io.seqFormat.SequenceParser;
import msaoperator.io.seqFormat.parser.ClustalWParser;
import msaoperator.io.seqFormat.parser.FastaParser;
import msaoperator.io.seqFormat.parser.GCGMSFParser;
import msaoperator.io.seqFormat.parser.MEGAParser;
import msaoperator.io.seqFormat.parser.NEXUSParser;
import msaoperator.io.seqFormat.parser.PAMLParser;
import msaoperator.io.seqFormat.parser.PHYParser;
import module.multiseq.alignment.view.io.VOICE4AlignmentView;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

@SuppressWarnings("serial")
public class VOICM4AlignmentView extends JPanel {

	private DataImportPanel_OneTypeOneFile_WithInputBox dataImportPanel;

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private ParameterPanel parameterPanel;

	private AlignmentViewMain alignmentViewMain;

	protected VOICM4AlignmentView(AlignmentViewMain alignmentViewMain) {
		setLayout(new BorderLayout());
		this.alignmentViewMain = alignmentViewMain;

		VOICE4AlignmentView importDataHandler = new VOICE4AlignmentView(this);

		JideTabbedPane jideTabbedPane = new JideTabbedPane();
		jideTabbedPane.addTab("Text input", getTextInputPanel());
		JPanel importDataDialog = importDataHandler.generateImportDataDialogGUI();
		jideTabbedPane.addTab("Versatile open input click remnant", importDataDialog);

		jideTabbedPane.setSelectedTabFont(titleFont);
		add(jideTabbedPane, BorderLayout.CENTER);

	}

	private Component getTextInputPanel() {
		JXMultiSplitPane containerPanel = new JXMultiSplitPane();

		String layoutDef = "(COLUMN " + "(LEAF name=top weight=0.98) " + "(LEAF name=editor weight=0.01) "
				+ "(LEAF name=bottom weight=0.01) " + ")";
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		containerPanel.getMultiSplitLayout().setModel(modelRoot);
		containerPanel.setBorder(BorderFactory.createEmptyBorder(6, 14, 10, 14));
		
		
		JXTaskPane aJXTaskPane = new JXTaskPane();
		aJXTaskPane.setTitle("Import data");
		aJXTaskPane.setFont(titleFont);

		dataImportPanel = new DataImportPanel_OneTypeOneFile_WithInputBox(this.getClass());
		dataImportPanel.setTooltipContents4importFiles(
				Arrays.asList("Please input multiple sequences alignment file, such as fasta file!"));
		dataImportPanel.setTooltipContents4importContent(Arrays.asList("Data is from following paper:",
				"Vigilant L, Stoneking M, Harpending H, Hawkes K, Wilson AC (1991). African populations and the evolution of human mitochondrial DNA. Science 253(5027):1503-1507.",
				"Hedges SB, Kumar S, Tamura K, & Stoneking M (1992). Human origins and analysis of mitochondrial DNA sequences. Science 255:737-739."));


		InputStream resourceAsStream = AlignmentImportBean.class.getResourceAsStream("mtCDNA.fas");
	
		try {
			List<String> lines = IOUtils.readLines(resourceAsStream, StandardCharsets.UTF_8);
			dataImportPanel.setContentsOfDirectArea(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		aJXTaskPane.setLayout(new BorderLayout());
		aJXTaskPane.add(dataImportPanel,BorderLayout.CENTER);

//		CbuttonPane.add(aJXTaskPane);
		containerPanel.add(aJXTaskPane,"top");

		// parameter panel

		parameterPanel = new ParameterPanel();

		JXTaskPane aJXTaskPane2 = new JXTaskPane();
		aJXTaskPane2.setTitle("Parameter panel");
		aJXTaskPane2.setFont(titleFont);
		aJXTaskPane2.add(parameterPanel);

//		taskPaneContainer.add(aJXTaskPane2);
		containerPanel.add(aJXTaskPane2,"editor");

		JButton jButton = new JButton("View multiple sequence alignments");
		jButton.setFont(defaultFont);

		jButton.addActionListener(e -> {
			new Thread(() -> {
				loadAlignmentView();
				Component root = SwingUtilities.getRoot(VOICM4AlignmentView.this );
				if (root instanceof JDialog) {
					((JDialog) root).dispose();
				}
			}).start();
		});
//		taskPaneContainer.add(jButton);
		containerPanel.add(jButton,"bottom");

		return containerPanel;
	}

	protected void configurateTaskPanes(JXTaskPaneContainer taskPaneContainer) {
		JXTaskPane aJXTaskPane = new JXTaskPane();
		aJXTaskPane.setTitle("Import data");
		aJXTaskPane.setFont(titleFont);

		dataImportPanel = new DataImportPanel_OneTypeOneFile_WithInputBox(this.getClass());
		dataImportPanel.setTooltipContents4importFiles(
				Arrays.asList("Please input multiple sequences alignment file, such as fasta file!"));
		dataImportPanel.setContentsOfDirectArea(Arrays.asList(">seq1", "ATCGATCGATCGATCGATCAGTCAGTCAGCATG", ">seq2",
				"ATCGATCGATCGATCGATCAGTCAGTCAGCATG", ">seq3", "ATCGATCGATCGATCGATCAGTCAGTCAGCATG", ">seq4",
				"ATCGATCGATCGATCGATCAGTCAGTCAGCATG"));
		aJXTaskPane.add(dataImportPanel);

		taskPaneContainer.add(aJXTaskPane);

		// parameter panel

		parameterPanel = new ParameterPanel();

		JXTaskPane aJXTaskPane2 = new JXTaskPane();
		aJXTaskPane2.setTitle("Parameter panel");
		aJXTaskPane2.setFont(titleFont);
		aJXTaskPane2.add(parameterPanel);

		taskPaneContainer.add(aJXTaskPane2);

		JButton jButton = new JButton("View multiple sequence alignments");
		jButton.setFont(defaultFont);

		jButton.addActionListener(e -> {
			new Thread(() -> {
				loadAlignmentView();
				Component root = SwingUtilities.getRoot(VOICM4AlignmentView.this );
				if (root instanceof JDialog) {
					((JDialog) root).dispose();
				}
			}).start();
		});
		taskPaneContainer.add(jButton);

		taskPaneContainer.setPreferredSize(new Dimension(1200, 1000));
	}

	private void loadAlignmentView() {
		boolean importFiles = dataImportPanel.isImportFiles();
		
		MSA_DATA_FORMAT dataFormat = parameterPanel.getDataFormat();

		if (importFiles) {
			List<File> inputFile = dataImportPanel.getInputFile();

			if (!DataImportPanel_OneTypeMultiFiles_WithInputBox.checkFile(inputFile)) {
				return;
			}

			for (File file : inputFile) {
				SequenceDataForAViewer parseData = parseData(file,dataFormat);
				loadOneAlignmentViewTab(parseData, "Data directly imported form file ".concat(file.getName()));
			}

		} else {
			String nwkTreeContent = dataImportPanel.getDirectInputContent();
			String trim = nwkTreeContent.trim();
			if (trim.endsWith(";")) {
				trim.substring(0, trim.length() - 1);
			}

			if (!DataImportPanel_OneTypeMultiFiles_WithInputBox.checkContent(trim)) {
				return;
			}

			File tmpFile = null;
			try {
				tmpFile = File.createTempFile("ttttt", null);
				FileUtils.writeStringToFile(tmpFile, trim, StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}

			SequenceDataForAViewer parseData = parseData(tmpFile,dataFormat);
			loadOneAlignmentViewTab(parseData, "Data directly imported form the text content.");

			tmpFile.delete();

		}

	}

	public SequenceDataForAViewer parseData(File inputFile,MSA_DATA_FORMAT dataFormat) {
//		ALIGNED_CLUSTALW("ClustalW"), ALIGNED_FASTA("Aligned fasta"), ALIGNED_GCGMSF("GCG MSF"),
//		ALIGNED_PAML("PAML"),
//		ALIGNED_MEGA("MEGA"), ALIGNED_PHYLIP("PHYLIP"), NEXUS_SEQ("NEXUS");

		SequenceParser seqPar = null;

		switch (dataFormat) {
		case ALIGNED_CLUSTALW:
			seqPar = new ClustalWParser(inputFile);
			break;
		case ALIGNED_FASTA:
			seqPar = new FastaParser(inputFile);
			break;
		case ALIGNED_GCGMSF:
			seqPar = new GCGMSFParser(inputFile);
			break;
		case ALIGNED_PAML:
			seqPar = new PAMLParser(inputFile);
			break;
		case ALIGNED_MEGA:
			seqPar = new MEGAParser(inputFile);
			break;
		case ALIGNED_PHYLIP:
			seqPar = new PHYParser(inputFile);
			break;

		default:
			seqPar = new NEXUSParser(inputFile);
			break;
		}

		try {
			seqPar.parse();
		} catch (Exception e1) {
			SwingDialog.showErrorMSGDialog("File error", e1.getMessage());
			return null;
		}

		BasicSequenceData seqElements = (BasicSequenceData) seqPar.getSeqElements();
		// Version 2.0.3.80
		List<SequenceI> dataSequences = seqElements.getDataSequences();
		for (SequenceI sequenceI : dataSequences) {
			String seqAsString = sequenceI.getSeqAsString();
			String deleteWhitespace = StringUtils.deleteWhitespace(seqAsString);
			sequenceI.setSeq(deleteWhitespace);
		}
		return new SequenceDataForAViewer(dataSequences);
	}

	public void loadOneAlignmentViewTab(SequenceDataForAViewer sequenceData, String whatDataInvoked) {

		Launcher4ModuleLoader moduleLoader = (Launcher4ModuleLoader) alignmentViewMain.getModuleLoader().get();
		moduleLoader.whatDataInvoked = whatDataInvoked;
		moduleLoader.setSequenceDataForAViewer(sequenceData);
		alignmentViewMain.initializeGraphics();
	}

	public String[] getTeamAndAuthors() {
		String[] info = new String[3];
		info[0] = "EvolGen";
		info[1] = Authors.orgnizeAuthors(Authors.GAOFENG, Authors.MUHAILONG, Authors.YUDALANG, Authors.LIHAIPENG);
		info[2] = "http://www.picb.ac.cn/evolgen/";

		return info;
	}

}
