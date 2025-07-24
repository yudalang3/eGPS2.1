package module.multiseq.alignerwithref;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import egps2.panels.dialog.SwingDialog;
import egps2.frame.gui.comp.JTextAreaWithDefaultContent_MouseAdapter;
import egps2.Authors;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.frame.MainFrameProperties;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.aligner.cli.MafftAlignerAutoMode;
import module.multiseq.alignment.view.Launcher4ModuleLoader;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;
import egps2.modulei.IModuleLoader;
import egps2.modulei.RunningTask;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {

	private JTextField refSequenceFilePathTextField;

	private JTextAreaWithDefaultContent_MouseAdapter otherSequencesJTextArea;

	final String TEXT_AREA_STRING = "textArea";
	final String LOADING_PATH_STRING = "loadingFromPath";
	final String PREVIOUS_REFERENCE_SEQ_FILE_PATH = "PREVIOUS_REFERENCE_SEQ_FILE_PATH_KEY";
	final String PREVIOUS_OTHERS_SEQ_FILE_PATH = "PREVIOUS_OTHERS_SEQ_FILE_PATH";
	private JTextField otherSequencesFilePathJTextField;

	private CardLayout layout;

	private JPanel otherSequenceImportJPanel;

	private Preferences userNodeForPackage;
	Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);

		setBorder(new EmptyBorder(15, 10, 6, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Reference sequence (one sequence in fasta file)");
		lblNewLabel.setFont(defaultFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		userNodeForPackage = Preferences.userNodeForPackage(getClass());

		refSequenceFilePathTextField = new JTextField(userNodeForPackage.get(PREVIOUS_REFERENCE_SEQ_FILE_PATH, ""));

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(refSequenceFilePathTextField, gbc_textField);
		refSequenceFilePathTextField.setColumns(10);

		JButton btnNewButton = new JButton("load");
		btnNewButton.setFont(defaultFont);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);
		btnNewButton.addActionListener(e -> {
			JFileChooser jFileChooser = new JFileChooser(refSequenceFilePathTextField.getText());
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int showOpenDialog = jFileChooser.showOpenDialog(this);
			if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
				String absolutePath = jFileChooser.getSelectedFile().getAbsolutePath();
				refSequenceFilePathTextField.setText(absolutePath);
				userNodeForPackage.put(PREVIOUS_REFERENCE_SEQ_FILE_PATH, absolutePath);
			}
		});

		JLabel lblNewLabel_1 = new JLabel("Other sequences");
		lblNewLabel_1.setFont(defaultFont);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		JRadioButton rdbtnImportFromTextArea = new JRadioButton("Direct import from text panel");
		rdbtnImportFromTextArea.setFont(defaultFont);
		rdbtnImportFromTextArea.setSelected(true);
		rdbtnImportFromTextArea.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				swithCard(TEXT_AREA_STRING);
			}
		});

		GridBagConstraints gbc_rdbtnImportFromTextArea = new GridBagConstraints();
		gbc_rdbtnImportFromTextArea.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnImportFromTextArea.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnImportFromTextArea.gridx = 1;
		gbc_rdbtnImportFromTextArea.gridy = 1;
		add(rdbtnImportFromTextArea, gbc_rdbtnImportFromTextArea);

		rdbtnImportFromFastaFile = new JRadioButton("Import from fasta file");
		rdbtnImportFromFastaFile.setFont(defaultFont);

		rdbtnImportFromFastaFile.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				swithCard(LOADING_PATH_STRING);
			}
		});

		GridBagConstraints gbc_rdbtnImportFromFastaFile = new GridBagConstraints();
		gbc_rdbtnImportFromFastaFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnImportFromFastaFile.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnImportFromFastaFile.gridx = 1;
		gbc_rdbtnImportFromFastaFile.gridy = 2;
		add(rdbtnImportFromFastaFile, gbc_rdbtnImportFromFastaFile);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnImportFromTextArea);
		buttonGroup.add(rdbtnImportFromFastaFile);

		JPanel importPanel = getImportPanel();
		GridBagConstraints gbc_importPanel = new GridBagConstraints();
		gbc_importPanel.insets = new Insets(0, 0, 5, 0);
		gbc_importPanel.gridwidth = 3;
		gbc_importPanel.fill = GridBagConstraints.BOTH;
		gbc_importPanel.gridx = 0;
		gbc_importPanel.gridy = 3;
		add(importPanel, gbc_importPanel);

		JButton runButton = new JButton("Run and show in anther tab");
		runButton.setFont(defaultFont);
		GridBagConstraints gbc_runButton = new GridBagConstraints();
		gbc_runButton.gridwidth = 3;
		gbc_runButton.gridx = 0;
		gbc_runButton.gridy = 4;
		add(runButton, gbc_runButton);

		// 定义一个计算任务
		final RunningTask runningTask = new RunningTask() {

			@Override
			public boolean isTimeCanEstimate() {
				return false;
			}

			@Override
			public int processNext() {
				try {
					runAndShowInOtherTab();
				} catch (Exception e2) {
					SwingDialog.showErrorMSGDialog("Input error",
							"Please check you refrence genome sequence file. " + e2.getMessage());
					e2.printStackTrace();
				}
				return PROGRESS_FINSHED;
			}

		};

		runButton.addActionListener(e -> {
			registerRunningTask(runningTask);
		});

	}

	private void runAndShowInOtherTab() throws IOException {

		String text = refSequenceFilePathTextField.getText();
		userNodeForPackage.put(PREVIOUS_REFERENCE_SEQ_FILE_PATH, text);

		File referenceSeqFile = new File(text);
		File file = new File("Temp.file.txt");
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

		List<String> readLines = FileUtils.readLines(referenceSeqFile, StandardCharsets.UTF_8);
		for (String string : readLines) {
			bufferedWriter.write(string);
			bufferedWriter.append('\n');
		}

		if (rdbtnImportFromFastaFile.isSelected()) {
			File otherSeqFile = new File(otherSequencesFilePathJTextField.getText());
			String readFileToString = FileUtils.readFileToString(otherSeqFile, StandardCharsets.UTF_8);
			bufferedWriter.write(readFileToString);
		} else {
			String text2 = otherSequencesJTextArea.getTextArea().getText();
			bufferedWriter.write(text2);
		}

		bufferedWriter.close();
		runAlignProcedure(file.toString());
		file.delete();

	}

	private void runAlignProcedure(String string) {
		MafftAlignerAutoMode mode = new MafftAlignerAutoMode();

		try {
			mode.getAlignment(string);
		} catch (Exception e) {
			SwingDialog.showErrorMSGDialog("Align error", "Some thing error in the align procedure.");
			e.printStackTrace();
			return;
		}
		List<String> seqNames = mode.getSeqNames();
		List<String> seqs = mode.getSequences();

		if (seqNames.isEmpty() || seqs.isEmpty()) {
			throw new InputMismatchException("Sorry, after align the sequences, no sequence obtained.");
		}

		List<SequenceI> vector = new ArrayList<>();
		for (int i = 0; i < seqs.size(); i++) {
			String seq = seqs.get(i);
			Sequence sequence = new Sequence(seqNames.get(i), seq.toUpperCase());
			vector.add(sequence);
		}

		final SequenceDataForAViewer sequenceData = new SequenceDataForAViewer(vector);

		SwingUtilities.invokeLater(() -> {
			Launcher4ModuleLoader independentModuleLoader = new Launcher4ModuleLoader();
			independentModuleLoader.setSequenceDataForAViewer(sequenceData);
			MainFrameProperties.loadTheModuleFromIModuleLoader(independentModuleLoader);
		});
		
		invokeTheFeatureMethod(0);
	}

	private JPanel getImportPanel() {
		layout = new CardLayout(10, 10);
		otherSequenceImportJPanel = new JPanel(layout);

		otherSequencesJTextArea = new JTextAreaWithDefaultContent_MouseAdapter();
		otherSequencesJTextArea.setFont(defaultFont);
		otherSequencesJTextArea.setTextContents(Arrays.asList(">other_seq1", "ATCGTCAGTCGATCGATCGATCGATGATTCATTCGTACGATCGACGATCGATCGACTAGCATCTATCAGCTACAGCTA"));

		otherSequenceImportJPanel.add(TEXT_AREA_STRING, otherSequencesJTextArea);

		JPanel fileImportJPanel = new JPanel(new BorderLayout(10, 10));
		String string = userNodeForPackage.get(PREVIOUS_OTHERS_SEQ_FILE_PATH, "");
		otherSequencesFilePathJTextField = new JTextField(string, 60);
		otherSequencesFilePathJTextField.setFont(defaultFont);
		JButton jButton = new JButton("Load");
		jButton.setFont(defaultFont);
		jButton.addActionListener(e -> {

			JFileChooser jFileChooser = new JFileChooser(otherSequencesFilePathJTextField.getText());
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int showOpenDialog = jFileChooser.showOpenDialog(this);
			if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
				String absolutePath = jFileChooser.getSelectedFile().getAbsolutePath();
				otherSequencesFilePathJTextField.setText(absolutePath);
				userNodeForPackage.put(PREVIOUS_OTHERS_SEQ_FILE_PATH, absolutePath);
			}
		});

		fileImportJPanel.add(otherSequencesFilePathJTextField, BorderLayout.CENTER);
		fileImportJPanel.add(jButton, BorderLayout.EAST);

		JPanel fileImportJPanelContaner = new JPanel(new BorderLayout());
		fileImportJPanelContaner.add(fileImportJPanel, BorderLayout.NORTH);
		otherSequenceImportJPanel.add(LOADING_PATH_STRING, fileImportJPanelContaner);

		return otherSequenceImportJPanel;
	}

	private void swithCard(String cardString) {
		layout.show(otherSequenceImportJPanel, cardString);
	}

	private String[] infos;

	private JRadioButton rdbtnImportFromFastaFile;

	public String[] getTeamAndAuthors() {
		if (infos == null) {

			infos = new String[3];
			infos[0] = "EvolGen";
			infos[1] = Authors.YUDALANG + "," + Authors.LIHAIPENG;
			infos[2] = "http://www.picb.ac.cn/evolgen/";
		}
		return infos;
	}

	@Override
	public void changeToThisTab() {

	}

	@Override
	public boolean closeTab() {
		return false;
	}

	@Override
	public boolean canImport() {
		return false;
	}

	@Override
	public void importData() {

	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void exportData() {

	}

	@Override
	public String[] getFeatureNames() {
		return new String[] {"Align to the reference"};
	}

	@Override
	protected void initializeGraphics() {

	}

}
