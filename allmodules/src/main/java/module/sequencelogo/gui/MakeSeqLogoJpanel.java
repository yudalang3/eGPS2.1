package module.sequencelogo.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fasta.io.FastaReader;
import org.apache.commons.lang3.tuple.Triple;

import egps2.panels.dialog.EGPSFileChooser;
import egps2.UnifiedAccessPoint;
import module.sequencelogo.MakeSeqLogoController;
import module.sequencelogo.makesequencelogo.CalculateFrequencyOfPerBase;
import module.sequencelogo.makesequencelogo.MakeSequenceLogoParameter;
import module.sequencelogo.makesequencelogo.ReadFrequencyFile;
import module.sequencelogo.makesequencelogo.ReadJustSequencesFile;

/**
 * 
 * @ClassName: MakeSeqLogoJpanel
 * @Description: 绘制sequencelogo的主要控制面板
 * @author zjw
 * @date 2024-04-28 10:13:22
 */
public class MakeSeqLogoJpanel extends JPanel {

	private MakeSeqLogoController controller;
	private String selectedOption;
	private int txtContenTextAreaIndex;
	private String selectedImportFileTypeString;
	private String selectedLogoTypeString;
	private String selectedAbscissaAngle;
	private String selectedFontSizeString;
	private String selectedUnits;
	private Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	/**
	 * NFB = numOfseqAndfrequenceOfBasesPerPositionAndBitsOfBasesPerPosition
	 */
	private Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> NFB;

	/**
	 * Create the panel.
	 */
	public MakeSeqLogoJpanel(MakeSeqLogoController controller) {
		this.controller = controller;
		setBackground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		setLayout(gridBagLayout);

		JRadioButton importContentRadioButton = new JRadioButton("Import content");
		importContentRadioButton.setSelected(true);
		importContentRadioButton.setFont(globalFont);
		importContentRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		importContentRadioButton.setBackground(Color.WHITE);
		GridBagConstraints gbc_importContentRadioButton = new GridBagConstraints();
		gbc_importContentRadioButton.anchor = GridBagConstraints.EAST;
		gbc_importContentRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_importContentRadioButton.gridx = 0;
		gbc_importContentRadioButton.gridy = 1;
		add(importContentRadioButton, gbc_importContentRadioButton);

		JRadioButton importFileRadioButton = new JRadioButton("Import file");
		importFileRadioButton.setBackground(Color.WHITE);
		importFileRadioButton.setFont(globalFont);
		importFileRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_importFileRadioButton_2 = new GridBagConstraints();
		gbc_importFileRadioButton_2.anchor = GridBagConstraints.EAST;
		gbc_importFileRadioButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_importFileRadioButton_2.gridx = 0;
		gbc_importFileRadioButton_2.gridy = 3;
		add(importFileRadioButton, gbc_importFileRadioButton_2);

		ButtonGroup group = new ButtonGroup();
		group.add(importContentRadioButton);
		group.add(importFileRadioButton);

		selectedOption = "Import content";
		importContentRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (importContentRadioButton.isSelected()) {
					selectedOption = "Import content";
				}
			}
		});

		importFileRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (importFileRadioButton.isSelected()) {
					selectedOption = "Import file";
				}
			}
		});

		JTextArea txtContenTextArea = new JTextArea();
		//txtContenTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		txtContenTextArea.setFont(globalFont);
		txtContenTextArea.setText("AGTaACTA\nAtAACTAA\nATAAgCAA\nATAAATAA\nAATAATAA\nAATAATAA\n");
		txtContenTextArea.setForeground(Color.LIGHT_GRAY);
		txtContenTextArea.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				txtContenTextArea.setForeground(Color.BLACK);
			}
		});
		txtContenTextArea.setWrapStyleWord(true);
		txtContenTextArea.setLineWrap(true);
		txtContenTextArea.setRows(9);
		JScrollPane scrollPane = new JScrollPane(txtContenTextArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_txtContent = new GridBagConstraints();
		gbc_txtContent.gridwidth = 5;
		gbc_txtContent.gridheight = 3;
		gbc_txtContent.insets = new Insets(0, 0, 5, 5);
		gbc_txtContent.fill = GridBagConstraints.BOTH;
		gbc_txtContent.gridx = 1;
		gbc_txtContent.gridy = 0;
		add(scrollPane, gbc_txtContent);

		JButton btnExampleNewButton = new JButton("Example");
		btnExampleNewButton.setFont(globalFont);
		GridBagConstraints gbc_btnExampleNewButton = new GridBagConstraints();
		gbc_btnExampleNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnExampleNewButton.gridx = 6;
		gbc_btnExampleNewButton.gridy = 1;
		add(btnExampleNewButton, gbc_btnExampleNewButton);

		List<String> txtContenTextAreaList = new ArrayList<>();
		txtContenTextAreaList.add("AGUaACUA\nAUAACUAA\nAUAAGCAA\nAUAAAUAA\nAAUAAUAA\nAAUAAUAA\n");
		txtContenTextAreaList.add(
				">seq1\nAGUaACUA\n>seq2\nAUAACUAA\n>seq3\nAUAAGCAA\n>seq4\nAUAAAUAA\n>seq5\nAAUAAUAA\n>seq6\nAAUAAUAA\n");
		txtContenTextAreaList.add(
				"position\tA\tC\tG\tT\n1\t0.67\t0.08\t0.00\t0.23\n2\t0.00\t0.00\t0.99\t0.00\n3\t0.00\t0.00\t1.00\t0.00\n4\t0.78\t0.04\t0.17\t0.00");
		txtContenTextAreaList.add("AGTaACTA\nAtAACTAA\nATAAgCAA\nATAAATAA\nAATAATAA\nAATAATAA\n");
		txtContenTextAreaIndex = 0;
		btnExampleNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtContenTextArea.setText(txtContenTextAreaList.get(txtContenTextAreaIndex));
				txtContenTextAreaIndex++;
				if (txtContenTextAreaIndex >= txtContenTextAreaList.size()) {
					txtContenTextAreaIndex = 0;
				}
			}
		});

		JTextField importFileTextField = new JTextField();
		GridBagConstraints gbc_importFileTextField = new GridBagConstraints();
		gbc_importFileTextField.gridwidth = 5;
		gbc_importFileTextField.insets = new Insets(0, 0, 5, 5);
		gbc_importFileTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_importFileTextField.gridx = 1;
		gbc_importFileTextField.gridy = 3;
		add(importFileTextField, gbc_importFileTextField);
		importFileTextField.setColumns(10);

		JButton btnLoadNewButton = new JButton("Load");
		btnLoadNewButton.setFont(globalFont);
		GridBagConstraints gbc_btnLoadNewButton = new GridBagConstraints();
		gbc_btnLoadNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadNewButton.gridx = 6;
		gbc_btnLoadNewButton.gridy = 3;
		add(btnLoadNewButton, gbc_btnLoadNewButton);
		btnLoadNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EGPSFileChooser fileChooser = new EGPSFileChooser(getClass());
				int result = fileChooser.showOpenDialog();
				if (result == JFileChooser.APPROVE_OPTION) {
					importFileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		JTextField txtLogoType = new JTextField();
		txtLogoType.setText("Sequence type");
		txtLogoType.setBorder(null);
		txtLogoType.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLogoType.setFont(globalFont);
		txtLogoType.setBackground(Color.WHITE);
		txtLogoType.setEditable(false);
		GridBagConstraints gbc_txtLogoType = new GridBagConstraints();
		gbc_txtLogoType.insets = new Insets(0, 0, 5, 5);
		gbc_txtLogoType.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLogoType.gridx = 0;
		gbc_txtLogoType.gridy = 4;
		add(txtLogoType, gbc_txtLogoType);
		txtLogoType.setColumns(10);

		JComboBox<String> logoTypeComboBox = new JComboBox<String>();
		//logoTypeComboBox.setBackground(Color.WHITE);
		logoTypeComboBox.setFont(globalFont);
		logoTypeComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "", "DNA", "RNA", "Protein" }));
		logoTypeComboBox.setSelectedIndex(0);
		GridBagConstraints gbc_logoTypeComboBox = new GridBagConstraints();
		gbc_logoTypeComboBox.gridwidth = 1;
		gbc_logoTypeComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_logoTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_logoTypeComboBox.gridx = 1;
		gbc_logoTypeComboBox.gridy = 4;
		add(logoTypeComboBox, gbc_logoTypeComboBox);

		selectedLogoTypeString = (String) logoTypeComboBox.getSelectedItem();
		logoTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedLogoTypeString = (String) logoTypeComboBox.getSelectedItem();
			}
		});

		JTextField txtAbscissaAngle = new JTextField();
		txtAbscissaAngle.setHorizontalAlignment(SwingConstants.RIGHT);
		txtAbscissaAngle.setBorder(null);
		txtAbscissaAngle.setBackground(Color.WHITE);
		txtAbscissaAngle.setEditable(false);
		txtAbscissaAngle.setFont(globalFont);
		txtAbscissaAngle.setText("Abscissa angle");
		GridBagConstraints gbc_txtAbscissaAngle = new GridBagConstraints();
		gbc_txtAbscissaAngle.gridwidth = 3;
		gbc_txtAbscissaAngle.insets = new Insets(0, 0, 5, 5);
		gbc_txtAbscissaAngle.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAbscissaAngle.gridx = 2;
		gbc_txtAbscissaAngle.gridy = 4;
		add(txtAbscissaAngle, gbc_txtAbscissaAngle);
		txtAbscissaAngle.setColumns(10);

		JComboBox<String> abscissaAngleComboBox = new JComboBox<String>();
		abscissaAngleComboBox.setFont(globalFont);
		abscissaAngleComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Horizontal", "Vertical" }));
		abscissaAngleComboBox.setSelectedIndex(0);
		//abscissaAngleComboBox.setBackground(Color.WHITE);
		GridBagConstraints gbc_abscissaAngleComboBox = new GridBagConstraints();
		gbc_abscissaAngleComboBox.gridwidth = 1;
		gbc_abscissaAngleComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_abscissaAngleComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_abscissaAngleComboBox.gridx = 5;
		gbc_abscissaAngleComboBox.gridy = 4;
		add(abscissaAngleComboBox, gbc_abscissaAngleComboBox);

		selectedAbscissaAngle = (String) abscissaAngleComboBox.getSelectedItem();
		abscissaAngleComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedAbscissaAngle = (String) abscissaAngleComboBox.getSelectedItem();
			}
		});

		JTextField txtUnits = new JTextField();
		txtUnits.setBackground(Color.WHITE);
		txtUnits.setBorder(null);
		txtUnits.setEditable(false);
		txtUnits.setFont(globalFont);
		txtUnits.setText("Units");
		txtUnits.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_txtUnits = new GridBagConstraints();
		gbc_txtUnits.insets = new Insets(0, 0, 5, 5);
		gbc_txtUnits.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUnits.gridx = 0;
		gbc_txtUnits.gridy = 5;
		add(txtUnits, gbc_txtUnits);
		txtUnits.setColumns(10);

		JComboBox<String> unitsComboBox = new JComboBox<String>();
		//unitsComboBox.setBackground(Color.WHITE);
		unitsComboBox.setFont(globalFont);
		unitsComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Bits", "Frequency" }));
		unitsComboBox.setSelectedIndex(0);
		GridBagConstraints gbc_unitsComboBox = new GridBagConstraints();
		gbc_unitsComboBox.gridwidth = 1;
		gbc_unitsComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_unitsComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_unitsComboBox.gridx = 1;
		gbc_unitsComboBox.gridy = 5;
		add(unitsComboBox, gbc_unitsComboBox);

		selectedUnits = (String) unitsComboBox.getSelectedItem();
		unitsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedUnits = (String) unitsComboBox.getSelectedItem();
			}
		});

		JTextField txtLogoSize = new JTextField();
		txtLogoSize.setEditable(false);
		txtLogoSize.setBorder(null);
		txtLogoSize.setBackground(Color.WHITE);
		txtLogoSize.setText("Logo size");
		txtLogoSize.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLogoSize.setFont(globalFont);
		GridBagConstraints gbc_txtLogoSize = new GridBagConstraints();
		gbc_txtLogoSize.gridwidth = 3;
		gbc_txtLogoSize.insets = new Insets(0, 0, 5, 5);
		gbc_txtLogoSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLogoSize.gridx = 2;
		gbc_txtLogoSize.gridy = 5;
		add(txtLogoSize, gbc_txtLogoSize);
		txtLogoSize.setColumns(10);

		JComboBox<String> logoSizeComboBox = new JComboBox<String>();
		//logoSizeComboBox.setBackground(Color.WHITE);
		logoSizeComboBox.setFont(globalFont);
		logoSizeComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] { "Default", "100", "150", "200", "250", "300", "350", "400" }));
		logoSizeComboBox.setSelectedIndex(0);
		GridBagConstraints gbc_logoSizeComboBox = new GridBagConstraints();
		gbc_logoSizeComboBox.gridwidth = 1;
		gbc_logoSizeComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_logoSizeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_logoSizeComboBox.gridx = 5;
		gbc_logoSizeComboBox.gridy = 5;
		add(logoSizeComboBox, gbc_logoSizeComboBox);

		selectedFontSizeString = (String) logoSizeComboBox.getSelectedItem();
		logoSizeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedFontSizeString = (String) logoSizeComboBox.getSelectedItem();
			}
		});

		JTextField txtImportDataType = new JTextField();
		txtImportDataType.setBackground(Color.WHITE);
		txtImportDataType.setBorder(null);
		txtImportDataType.setEditable(false);
		txtImportDataType.setFont(globalFont);
		txtImportDataType.setHorizontalAlignment(SwingConstants.RIGHT);
		txtImportDataType.setText("Import data type");
		GridBagConstraints gbc_txtImportDataType = new GridBagConstraints();
		gbc_txtImportDataType.insets = new Insets(0, 0, 5, 5);
		gbc_txtImportDataType.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtImportDataType.gridx = 0;
		gbc_txtImportDataType.gridy = 6;
		add(txtImportDataType, gbc_txtImportDataType);
		txtImportDataType.setColumns(10);

		JComboBox<String> importDataTypeComboBox = new JComboBox<String>();
		//importDataTypeComboBox.setBackground(Color.WHITE);
		importDataTypeComboBox.setFont(globalFont);
		importDataTypeComboBox
				.setModel(new DefaultComboBoxModel<String>(new String[] { "", "Sequences", "Fasta", "Frequency" }));
		importDataTypeComboBox.setSelectedIndex(0);
		GridBagConstraints gbc_importDataTypeComboBox = new GridBagConstraints();
		gbc_importDataTypeComboBox.gridwidth = 1;
		gbc_importDataTypeComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_importDataTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_importDataTypeComboBox.gridx = 1;
		gbc_importDataTypeComboBox.gridy = 6;
		add(importDataTypeComboBox, gbc_importDataTypeComboBox);

		selectedImportFileTypeString = (String) importDataTypeComboBox.getSelectedItem();
		importDataTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedImportFileTypeString = (String) importDataTypeComboBox.getSelectedItem();
			}
		});

		JButton createSeqLogoButton = new JButton("Create sequence logo");
		createSeqLogoButton.setFont(globalFont);
		GridBagConstraints gbc_createSeqLogoButton = new GridBagConstraints();
		gbc_createSeqLogoButton.gridwidth = 7;
		gbc_createSeqLogoButton.gridx = 0;
		gbc_createSeqLogoButton.gridy = 8;
		add(createSeqLogoButton, gbc_createSeqLogoButton);
		createSeqLogoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String outputFloder = "";
				String inputFilename = null;
				String systemSeparator = System.getProperty("file.separator");
				if (validateSelections()) {
					if (selectedOption.equals("Import file")) {
						inputFilename = importFileTextField.getText();
					} else if (selectedOption.equals("Import content")) {
						String textAreaContent = txtContenTextArea.getText();
						outputFloder = System.getProperty("user.dir");
						;
						try (PrintWriter writer = new PrintWriter(
								outputFloder + systemSeparator + "textAreaContent.txt")) {
							writer.println(textAreaContent);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						inputFilename = outputFloder + systemSeparator + "textAreaContent.txt";
					}
					if (produceInputFile(inputFilename)) {
						new File(outputFloder + systemSeparator + "textAreaContent.txt").delete();
						MakeSequenceLogoParameter parameter = setParamter();
						controller.loadTab(parameter);
						Component root = SwingUtilities.getRoot(controller.getMain().getJxTaskPaneContainer());
						if (root instanceof JDialog) {
							((JDialog) root).dispose();
						}
					}
				}				
			}
		});
	}

	/**
	 * 
	 * @MethodName: validateSelections
	 * @Description: 判断一些必选的选项是否为空
	 * @author zjw
	 * @param outputFolderTextField
	 * @return boolean
	 * @date 2024-04-26 05:24:59
	 */
	private boolean validateSelections() {

		boolean validateSelectionsResult = true;
		if (selectedLogoTypeString.equals("") && selectedImportFileTypeString.equals("")) {
			validateSelectionsResult = false;
			UIManager.put("OptionPane.messageFont", globalFont);
			JOptionPane.showMessageDialog(null, "Please choose sequence type and import file/content type!", "Error",
					JOptionPane.PLAIN_MESSAGE);
		} else if (selectedLogoTypeString.equals("")) {
			validateSelectionsResult = false;
			UIManager.put("OptionPane.messageFont", globalFont);
			JOptionPane.showMessageDialog(null, "Please choose sequence type!", "Error", JOptionPane.PLAIN_MESSAGE);
		} else if (selectedImportFileTypeString.equals("")) {
			validateSelectionsResult = false;
			UIManager.put("OptionPane.messageFont", globalFont);
			JOptionPane.showMessageDialog(null, "Please choose import file/content type!", "Error",
					JOptionPane.PLAIN_MESSAGE);
		}

		return validateSelectionsResult;
	}

	/**
	 * 
	 * @MethodName: produceInputFile
	 * @Description: 根据输入的不同文件格式处理文件，得到NFB
	 * @author zjw
	 * @param inputFilename
	 * @return boolean
	 * @date 2024-04-26 05:41:18
	 */
	private boolean produceInputFile(String inputFilename) {

		boolean isProduceInputFileSuccessfullly = true;
		File file = new File(inputFilename);
		// System.out.println(inputFilename);
		if (selectedImportFileTypeString.equals("Fasta")) {
			try {
				LinkedHashMap<String, String> readFastaDNASequence = null;
				readFastaDNASequence = FastaReader.readFastaDNASequence(file, true, false);
				NFB = CalculateFrequencyOfPerBase.calculateFrequencyAndBitsOfPerBaseUsedFastaFile(readFastaDNASequence);
			} catch (FileNotFoundException e) {
				isProduceInputFileSuccessfullly = false;
				UIManager.put("OptionPane.messageFont", globalFont);
				JOptionPane.showMessageDialog(null, "Please check if your file exists!", "Error",
						JOptionPane.PLAIN_MESSAGE);
			} catch (IOException e) {
				isProduceInputFileSuccessfullly = false;
				e.printStackTrace();
			} catch (RuntimeException e) {
				isProduceInputFileSuccessfullly = false;
				UIManager.put("OptionPane.messageFont", globalFont);
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
			}
		} else if (selectedImportFileTypeString.equals("Frequency")) {
			LinkedHashMap<Integer, LinkedHashMap<Character, Double>> frequenceOfBasesPerPosition = null;
			try {
				frequenceOfBasesPerPosition = ReadFrequencyFile.readFrequencyFile(file);
			} catch (FileNotFoundException e) {
				isProduceInputFileSuccessfullly = false;
				UIManager.put("OptionPane.messageFont", globalFont);
				JOptionPane.showMessageDialog(null, "Please check if your file exists!", "Error",
						JOptionPane.PLAIN_MESSAGE);
			} catch (IOException e) {
				isProduceInputFileSuccessfullly = false;
			} catch (RuntimeException e) {
				isProduceInputFileSuccessfullly = false;
				UIManager.put("OptionPane.messageFont", globalFont);
				JOptionPane.showMessageDialog(null, "Please check input file format!", "Error",
						JOptionPane.PLAIN_MESSAGE);
			}
			if (isProduceInputFileSuccessfullly) {
				NFB = CalculateFrequencyOfPerBase
						.calculateFrequencyAndBitsOfPerBaseUsedFrequencyFile(frequenceOfBasesPerPosition);
			}
		} else if (selectedImportFileTypeString.equals("Sequences")) {
			try {
				LinkedHashMap<String, String> readJustSequencesFile = null;
				readJustSequencesFile = ReadJustSequencesFile.readJustSequencesFile(file);
				NFB = CalculateFrequencyOfPerBase
						.calculateFrequencyAndBitsOfPerBaseUsedFastaFile(readJustSequencesFile);
			} catch (IOException e) {
				isProduceInputFileSuccessfullly = false;
			} catch (RuntimeException e) {
				isProduceInputFileSuccessfullly = false;
				UIManager.put("OptionPane.messageFont", globalFont);
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
			}
		}
		return isProduceInputFileSuccessfullly;
	}

	/**
	 * 
	 * @MethodName: setParamter
	 * @Description: 将用户设置的参数进行保存
	 * @author zjw
	 * @return MakeSequenceLogoParameter
	 * @date 2024-05-03 03:33:20
	 */
	private MakeSequenceLogoParameter setParamter() {
		
		MakeSequenceLogoParameter parameter = new MakeSequenceLogoParameter();
		
		if (selectedUnits.equals("Bits")) {			
			parameter.setSeqLogoType(true);
		} else if (selectedUnits.equals("Probability")) {
			parameter.setSeqLogoType(false);
		}
		
		if(selectedAbscissaAngle.equals("Vertical")) {
			parameter.setAbscissaAngle(true);
		}else if (selectedAbscissaAngle.equals("Horizontal")) {
			parameter.setAbscissaAngle(false);
		}
		
		if(selectedLogoTypeString.equals("DNA") || selectedLogoTypeString.equals("RNA")) {
			parameter.setSequenceType(0);
		}else if (selectedLogoTypeString.equals("Protein")) {
			parameter.setSequenceType(1);
		}				
		
		if (selectedFontSizeString.equals("Default")) {
			parameter.setIsSeqLogoFontSizeDefault(true);
		} else {
			parameter.setIsSeqLogoFontSizeDefault(false);
			int fontSize = Integer.parseInt(selectedFontSizeString);
			parameter.setSeqLogoFontSize(fontSize);
		}
		
		parameter.setNFB(NFB);
		
		double drawingBoardWidth = controller.getMain().getRightJPanel().getWidth();
		double drawingBoardHeight = controller.getMain().getRightJPanel().getHeight();		
		parameter.setDrawingBoardWidth(drawingBoardWidth);
		parameter.setdrawingBoardHeight(drawingBoardHeight);
		parameter.setInitialCoordinate();
		
		return parameter;
	}
	
}
