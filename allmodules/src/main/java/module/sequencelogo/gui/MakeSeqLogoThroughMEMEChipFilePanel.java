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
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import egps2.panels.dialog.EGPSFileChooser;
import egps2.utils.common.util.EGPSShellIcons;
import egps2.UnifiedAccessPoint;
import module.sequencelogo.MakeSeqLogoController;
import module.sequencelogo.extractMEMEinformation.ProcessMEMEChipOutfile;
import module.sequencelogo.makesequencelogo.MakeSequenceLogoParameter;

/**
 * 
 * @ClassName: MakeSeqLogoThroughMEMEChipFilePanel
 * @Description: 根据MEMEChip的.meme文件生成sequence logo的主要面板图
 * @author zjw
 * @date 2024-04-28 09:51:59
 */
public class MakeSeqLogoThroughMEMEChipFilePanel extends JPanel {

	private MakeSeqLogoController controller;
	private Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private String selectedOption;
	private String selectedLogoTypeString;
	private String selectedUnits;
	private String selectedAbscissaAngle;
	private String selectedOutputFormat;
	private String systemSeparator = System.getProperty("file.separator");

	/**
	 * Create the panel.
	 */
	public MakeSeqLogoThroughMEMEChipFilePanel(MakeSeqLogoController controller) {
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
		importContentRadioButton.setHorizontalAlignment(SwingConstants.RIGHT);
		importContentRadioButton.setFont(globalFont);
		importContentRadioButton.setBackground(Color.WHITE);
		GridBagConstraints gbc_importContentRadioButton = new GridBagConstraints();
		gbc_importContentRadioButton.anchor = GridBagConstraints.EAST;
		gbc_importContentRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_importContentRadioButton.gridx = 0;
		gbc_importContentRadioButton.gridy = 1;
		add(importContentRadioButton, gbc_importContentRadioButton);

		ButtonGroup group = new ButtonGroup();
		group.add(importContentRadioButton);
		selectedOption = "Import content";
		importContentRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (importContentRadioButton.isSelected()) {
					selectedOption = "Import content";
				}
			}
		});

		JTextArea textArea = new JTextArea();
		textArea.setFont(globalFont);
		textArea.setText(
				"MEME version 5.5.0 (Wed Sep 7 14:18:26 2022 -0700)\n\nALPHABET= ACGT\n\nstrands: + -\n\nBackground letter frequencies (from file `./background'):\nElegantJTable 0.39150 C 0.10850 G 0.39150 T 0.10850 \n\nMOTIF 1 AGGARG-MEME-1\n\nletter-probability matrix: alength= 4 w= 6 nsites= 2942 E= 2.1e-046\n  0.677430	  0.085656	  0.000000	  0.236914\t\n  0.999660	  0.000340	  0.000000	  0.000000\t\n  0.000000	  1.000000	  0.000000	  0.000000\t\n  0.784840	  0.040109	  0.175051	  0.000000\t\n  0.000000	  0.054045	  0.391230	  0.554725\t\n  0.000000	  1.000000	  0.000000	  0.000000\t");
		// textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(Color.LIGHT_GRAY);
		textArea.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				textArea.setForeground(Color.BLACK);
			}
		});
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setRows(13);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 5;
		gbc_textArea.gridheight = 3;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 0;
		add(scrollPane, gbc_textArea);

		JPanel importFilePanel = new JPanel();
		importFilePanel.setBackground(Color.white);
		GridBagConstraints gbc_importFilePanel = new GridBagConstraints();
		gbc_importFilePanel.insets = new Insets(0, 0, 5, 5);
		gbc_importFilePanel.fill = GridBagConstraints.BOTH;
		gbc_importFilePanel.gridx = 0;
		gbc_importFilePanel.gridy = 3;
		add(importFilePanel, gbc_importFilePanel);
		GridBagLayout gbl_importFilePanel = new GridBagLayout();
		gbl_importFilePanel.columnWidths = new int[] { 0, 0 };
		gbl_importFilePanel.rowHeights = new int[] { 0 };
		gbl_importFilePanel.columnWeights = new double[] { 1.0, 1.0 };
		gbl_importFilePanel.rowWeights = new double[] { 0.0 };
		importFilePanel.setLayout(gbl_importFilePanel);

		JRadioButton importFileRadioButton = new JRadioButton("Import file");
		GridBagConstraints gbc_importFileRadioButton = new GridBagConstraints();
		gbc_importFileRadioButton.insets = new Insets(0, 0, 0, 5);
		gbc_importFileRadioButton.gridx = 0;
		gbc_importFileRadioButton.gridy = 0;
		importFilePanel.add(importFileRadioButton, gbc_importFileRadioButton);
		//importFileRadioButton.setToolTipText("MEME-ChIP Combined Motifs (.meme) Format");
		importFileRadioButton.setHorizontalAlignment(SwingConstants.RIGHT);
		importFileRadioButton.setFont(globalFont);
		importFileRadioButton.setBackground(Color.WHITE);
		group.add(importFileRadioButton);

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setToolTipText("MEME-ChIP Combined Motifs (.meme) Format");
		lblNewLabel.setIcon(EGPSShellIcons.getHelpIcon());
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		importFilePanel.add(lblNewLabel, gbc_lblNewLabel);
		importFileRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (importFileRadioButton.isSelected()) {
					selectedOption = "Import file";
				}
			}
		});

		JTextField importFileTextField = new JTextField();
		//importFileTextField.setToolTipText("MEME-ChIP Combined Motifs (.meme) Format");
		GridBagConstraints gbc_importFileTextField = new GridBagConstraints();
		gbc_importFileTextField.gridwidth = 5;
		gbc_importFileTextField.insets = new Insets(0, 0, 5, 5);
		gbc_importFileTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_importFileTextField.gridx = 1;
		gbc_importFileTextField.gridy = 3;
		add(importFileTextField, gbc_importFileTextField);
		importFileTextField.setColumns(10);

		JButton btnNewButton_1 = new JButton("Load");
		//btnNewButton_1.setToolTipText("MEME-ChIP Combined Motifs (.meme) Format");
		btnNewButton_1.setFont(globalFont);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 6;
		gbc_btnNewButton_1.gridy = 3;
		add(btnNewButton_1, gbc_btnNewButton_1);

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EGPSFileChooser fileChooser = new EGPSFileChooser(getClass());
				int result = fileChooser.showOpenDialog();
				if (result == JFileChooser.APPROVE_OPTION) {
					importFileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		JTextField txtLogoType = new JTextField();
		txtLogoType.setBackground(Color.WHITE);
		txtLogoType.setBorder(null);
		txtLogoType.setEditable(false);
		txtLogoType.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLogoType.setFont(globalFont);
		txtLogoType.setText("Sequence type");
		GridBagConstraints gbc_txtLogoType = new GridBagConstraints();
		gbc_txtLogoType.insets = new Insets(0, 0, 5, 5);
		gbc_txtLogoType.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLogoType.gridx = 0;
		gbc_txtLogoType.gridy = 4;
		add(txtLogoType, gbc_txtLogoType);
		txtLogoType.setColumns(10);

		JComboBox<String> logoTypeComboBox = new JComboBox<String>();
		logoTypeComboBox.setFont(globalFont);
		logoTypeComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "", "DNA", "RNA", "Protein" }));
		logoTypeComboBox.setSelectedIndex(0);
		// logoTypeComboBox.setBackground(Color.WHITE);
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
		txtAbscissaAngle.setBorder(null);
		txtAbscissaAngle.setHorizontalAlignment(SwingConstants.RIGHT);
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
		// abscissaAngleComboBox.setBackground(Color.WHITE);
		abscissaAngleComboBox.setFont(globalFont);
		abscissaAngleComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Horizontal", "Vertical" }));
		abscissaAngleComboBox.setSelectedIndex(0);
		GridBagConstraints gbc_abscissaAngleComboBox_2 = new GridBagConstraints();
		gbc_abscissaAngleComboBox_2.gridwidth = 1;
		gbc_abscissaAngleComboBox_2.insets = new Insets(0, 0, 5, 5);
		gbc_abscissaAngleComboBox_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_abscissaAngleComboBox_2.gridx = 5;
		gbc_abscissaAngleComboBox_2.gridy = 4;
		add(abscissaAngleComboBox, gbc_abscissaAngleComboBox_2);

		selectedAbscissaAngle = (String) abscissaAngleComboBox.getSelectedItem();
		abscissaAngleComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedAbscissaAngle = (String) abscissaAngleComboBox.getSelectedItem();
			}
		});

		JTextField txtUnits = new JTextField();
		txtUnits.setBorder(null);
		txtUnits.setBackground(Color.WHITE);
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
		txtUnits.setColumns(5);

		JComboBox<String> unitsComboBox = new JComboBox<String>();
		// unitsComboBox.setBackground(Color.WHITE);
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

		JTextField txtOutputFormat = new JTextField();
		txtOutputFormat.setBorder(null);
		txtOutputFormat.setBackground(Color.WHITE);
		txtOutputFormat.setEditable(false);
		txtOutputFormat.setFont(globalFont);
		txtOutputFormat.setText("Output format");
		txtOutputFormat.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_txtOutputFormat = new GridBagConstraints();
		gbc_txtOutputFormat.gridwidth = 3;
		gbc_txtOutputFormat.insets = new Insets(0, 0, 5, 5);
		gbc_txtOutputFormat.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtOutputFormat.gridx = 2;
		gbc_txtOutputFormat.gridy = 5;
		add(txtOutputFormat, gbc_txtOutputFormat);
		txtOutputFormat.setColumns(5);

		JComboBox<String> outputFormatComboBox = new JComboBox<String>();
		// outputFormatComboBox.setBackground(Color.WHITE);
		outputFormatComboBox.setFont(globalFont);
		outputFormatComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] { "JPEG (*.jpg)", "PNG (*.png)", "PDF (*.pdf)", }));
		/**
		 * svg,eps,pptx还有些问题所以就先不加了
		 */
//		outputFormatComboBox.setModel(new DefaultComboBoxModel(new String[] { "Bitmap image: JPEG (*.jpg)",
//				"Bitmap image: PNG (*.png)", "Vector graphic: SVG (*.svg)", "Vector graphic: PDF (*.pdf)",
//				"Vector graphic: EPS (*.eps)", "PowerPoint slides: PPTX (*.pptx)" }));
		outputFormatComboBox.setSelectedIndex(0);
		GridBagConstraints gbc_outputFormatComboBox = new GridBagConstraints();
		gbc_outputFormatComboBox.gridwidth = 1;
		gbc_outputFormatComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_outputFormatComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_outputFormatComboBox.gridx = 5;
		gbc_outputFormatComboBox.gridy = 5;
		add(outputFormatComboBox, gbc_outputFormatComboBox);

		selectedOutputFormat = (String) outputFormatComboBox.getSelectedItem();
		outputFormatComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedOutputFormat = (String) outputFormatComboBox.getSelectedItem();
			}
		});

		JTextField txtOutputFolder = new JTextField();
		txtOutputFolder.setBorder(null);
		txtOutputFolder.setBackground(Color.WHITE);
		txtOutputFolder.setFont(globalFont);
		txtOutputFolder.setEditable(false);
		txtOutputFolder.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOutputFolder.setText("Output folder");
		GridBagConstraints gbc_txtOutputFolder = new GridBagConstraints();
		gbc_txtOutputFolder.insets = new Insets(0, 0, 5, 5);
		gbc_txtOutputFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtOutputFolder.gridx = 0;
		gbc_txtOutputFolder.gridy = 7;
		add(txtOutputFolder, gbc_txtOutputFolder);
		txtOutputFolder.setColumns(10);

		JTextField outputFolderTextField = new JTextField();
		GridBagConstraints gbc_outputFolderTextField = new GridBagConstraints();
		gbc_outputFolderTextField.gridwidth = 5;
		gbc_outputFolderTextField.insets = new Insets(0, 0, 5, 5);
		gbc_outputFolderTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_outputFolderTextField.gridx = 1;
		gbc_outputFolderTextField.gridy = 7;
		add(outputFolderTextField, gbc_outputFolderTextField);
		outputFolderTextField.setColumns(10);

		JButton btnNewButton_2 = new JButton("Load");
		btnNewButton_2.setFont(globalFont);
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 6;
		gbc_btnNewButton_2.gridy = 7;
		add(btnNewButton_2, gbc_btnNewButton_2);

		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EGPSFileChooser fileChooser = new EGPSFileChooser(getClass());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 设置选择模式为文件夹模式
				int result = fileChooser.showSaveDialog();
				if (result == JFileChooser.APPROVE_OPTION) {
					outputFolderTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		JButton creatSeqLogoButton = new JButton("Create sequence logo");
		creatSeqLogoButton.setFont(globalFont);
		GridBagConstraints gbc_creatSeqLogoButton = new GridBagConstraints();
		gbc_creatSeqLogoButton.gridwidth = 7;
		gbc_creatSeqLogoButton.gridx = 0;
		gbc_creatSeqLogoButton.gridy = 8;
		add(creatSeqLogoButton, gbc_creatSeqLogoButton);

		creatSeqLogoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (validateSelections(outputFolderTextField)) {
					String tempOutFolder = "processed_output";
					String tempOutMotifHeader = "motif_";
					String finalOutFoleder = "outImage";
					String outputFloderString = outputFolderTextField.getText() + systemSeparator;
					// 得到输入文件的路径
					String inputFilePath = getInputFilePath(outputFolderTextField, importFileTextField, textArea);

					try {
						// 处理MEMEChip输出的.meme文件
						ProcessMEMEChipOutfile.processMEMEChipOutfile(outputFloderString, inputFilePath, tempOutFolder,
								tempOutMotifHeader);
					} catch (RuntimeException e1) {
						UIManager.put("OptionPane.messageFont", globalFont);
						JOptionPane.showMessageDialog(null, "Please check input file format!", "Error",
								JOptionPane.PLAIN_MESSAGE);
					}

					// 如果是通过Import content输入数据，会生成一个中间文件，处理完之后需要将它删除
					if (selectedOption.equals("Import content")) {
						new File(inputFilePath).delete();
					}

					// 得到将.meme文件处理后生成的所有的motif的每个碱基在每个位置的比例文件所在的目录
					File inputFileFolder = new File(outputFloderString + tempOutFolder);
					String[] files = null;// 所有的motif的每个碱基在每个位置的比例文件
					if (inputFileFolder.exists() && inputFileFolder.isDirectory()) {
						File directory = new File(outputFloderString + tempOutFolder);
						files = directory.list();
					}
					Arrays.sort(files, new MotifOrderComparator());

					// 所有的sequence logo图需要输出到的目录路径
					String outImageFolderPath = null;
					outImageFolderPath = outputFloderString + finalOutFoleder;
					File outImagefolder = new File(outImageFolderPath);
					outImagefolder.mkdirs();

					String outputFormat = getFileExtension(selectedOutputFormat);
					MakeSequenceLogoParameter parameter = setParamter();
					controller.loadTab4MEMEChipOut(files, tempOutMotifHeader, outputFloderString, tempOutFolder,
							outImageFolderPath, outputFormat, parameter);
					Component root = SwingUtilities.getRoot(controller.getMain().getJxTaskPaneContainer());
					if (root instanceof JDialog) {
						((JDialog) root).dispose();
					}
				}				
			}
		});
	}

	/**
	 * 
	 * @MethodName: getInputFilePath
	 * @Description: 得到输入文件路径，如果用户是导入的文件，直接就可以得到，如果用户导入的内容，需要生成一个中间文件
	 * 然后得到这个中间文件的路径
	 * @author zjw
	 * @param outputFolderTextField
	 * @param importFileTextField
	 * @param textArea
	 * @return String
	 * @date 2024-04-26 05:28:44
	 */
	private String getInputFilePath(JTextField outputFolderTextField, JTextField importFileTextField,
			JTextArea textArea) {

		String outputFloderString = outputFolderTextField.getText() + systemSeparator;
		String inputFilePath = null;
		if (selectedOption.equals("Import file")) {
			inputFilePath = importFileTextField.getText();
		} else if (selectedOption.equals("Import content")) {
			String textAreaContent = textArea.getText();
			try (PrintWriter writer = new PrintWriter(outputFloderString + "textarea_content.txt")) {
				writer.println(textAreaContent);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			inputFilePath = outputFloderString + "textarea_content.txt";
		}

		return inputFilePath;
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
	private boolean validateSelections(JTextField outputFolderTextField) {
		boolean validateSelectionsResult = true;
		if (selectedLogoTypeString.equals("")) {
			validateSelectionsResult = false;
			UIManager.put("OptionPane.messageFont", globalFont);
			JOptionPane.showMessageDialog(null, "Please choose sequence type!", "Error", JOptionPane.PLAIN_MESSAGE);
		}

		if (validateSelectionsResult) {
			if (outputFolderTextField.getText().equals("")) {
				UIManager.put("OptionPane.messageFont", globalFont);
				JOptionPane.showMessageDialog(null, "Please select output folder!", "Error", JOptionPane.PLAIN_MESSAGE);
				validateSelectionsResult = false;
			}
		}
		return validateSelectionsResult;
	}

	/**
	 * 
	 * @MethodName: getFileExtension
	 * @Description: 得到用户选择的想要输出的文件格式，并提取出后缀，例如“pdf”
	 * @author zjw
	 * @param format
	 * @return String
	 * @date 2024-04-26 05:22:35
	 */
	private static String getFileExtension(String format) {
		// 通过括号来分割字符串，获取最后一个括号中的单词作为文件扩展名
		String[] parts = format.split("\\(");
		if (parts.length > 1) {
			String lastPart = parts[parts.length - 1];
			// 去除括号和星号，并转换为小写
			return lastPart.replaceAll("[^a-zA-Z]", "").toLowerCase();
		}
		return "";
	}

	/**
	 * 
	 * @MethodName: setParamter
	 * @Description: 将用户设置的参数进行保存
	 * @author zjw
	 * @return MakeSequenceLogoParameter
	 * @date 2024-05-03 04:01:09
	 */
	private MakeSequenceLogoParameter setParamter() {

		MakeSequenceLogoParameter parameter = new MakeSequenceLogoParameter();

		if (selectedUnits.equals("Bits")) {
			parameter.setSeqLogoType(true);
		} else if (selectedUnits.equals("Probability")) {
			parameter.setSeqLogoType(false);
		}

		if (selectedAbscissaAngle.equals("Vertical")) {
			parameter.setAbscissaAngle(true);
		} else if (selectedAbscissaAngle.equals("Horizontal")) {
			parameter.setAbscissaAngle(false);
		}

		if (selectedLogoTypeString.equals("DNA") || selectedLogoTypeString.equals("RNA")) {
			parameter.setSequenceType(0);
		} else if (selectedLogoTypeString.equals("Protein")) {
			parameter.setSequenceType(1);
		}

		// 因为通过MEMEChip生成sequence logo时不止生成一个，所以只能是默认
		parameter.setIsSeqLogoFontSizeDefault(true);

		double drawingBoardWidth = controller.getMain().getRightJPanel().getWidth();
		double drawingBoardHeight = controller.getMain().getRightJPanel().getHeight();
		parameter.setDrawingBoardWidth(drawingBoardWidth);
		parameter.setdrawingBoardHeight(drawingBoardHeight);
		parameter.setInitialCoordinate();

		return parameter;
	}

	/**
	 * 
	 * @ClassName: MotifOrderComparator
	 * @Description: 根据motif的频率矩阵的文件的名字进行排序，主要根据“motif_”和“-”的数字进行排序
	 * @author zjw
	 * @date 2024-04-26 05:20:51
	 */
	class MotifOrderComparator implements Comparator<String> {
		@Override
		public int compare(String motif1, String motif2) {
			int number1 = extractNumber(motif1);
			int number2 = extractNumber(motif2);
			return Integer.compare(number1, number2);
		}

		private int extractNumber(String motif) {
			int start = motif.indexOf("motif_") + 6;
			int end = motif.indexOf("-", start);
			String numberStr = motif.substring(start, end);
			return Integer.parseInt(numberStr);
		}
	}

}
