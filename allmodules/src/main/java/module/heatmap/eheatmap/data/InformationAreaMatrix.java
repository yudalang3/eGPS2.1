package module.heatmap.eheatmap.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.apache.commons.io.FileUtils;

import egps2.utils.common.tablelike.LineSparator;
import egps2.utils.common.tablelike.MatrixTableContentBean;
import egps2.UnifiedAccessPoint;

public class InformationAreaMatrix extends AbstactInformationArea {
	private static final long serialVersionUID = -2161958562317089394L;

	protected JTable table;
	protected JTextField textFieldNumOfNAs;
	protected JTextField textFieldNumOfRows;
	protected JTextField textFieldNumOfColumns;
	protected JComboBox<String> comboBoxLineSeparator;
	protected JCheckBox chckbxHeader;
	protected JCheckBox chckbxRowNames;

	private JScrollPane previewPanelScrollPane;

	protected DefaultTableModel tableModel;

	protected MatrixTableContentBean bTableContentBean = null;

	private File inputFile;

	private boolean ifFirstTimeLoad = true;
	protected JRadioButton rdbtnGeneralMatrix;
	protected JRadioButton rdbtnRNA;
	protected JRadioButton rdbtnProtein;

	/**
	 * Create the panel.
	 */
	public InformationAreaMatrix() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		JPanel topPanel = new JPanel();
		topPanel.setMaximumSize(new Dimension(32767, 150));
		topPanel.setBackground(Color.WHITE);
		add(topPanel);
		topPanel.setLayout(new GridLayout(1, 0, 0, 0));
		TitledBorder titledBorder = BorderFactory.createTitledBorder("File read options :");
		titledBorder.setTitleFont(globalFont.deriveFont(Font.BOLD));
		topPanel.setBorder(titledBorder);

		JLabel lblNewLabel = new JLabel("Line separator");
		lblNewLabel.setFont(globalFont);
		topPanel.add(lblNewLabel);

		comboBoxLineSeparator = new JComboBox<String>();
		comboBoxLineSeparator.setForeground(Color.BLACK);
		comboBoxLineSeparator.setBackground(Color.WHITE);

		LineSparator[] values = LineSparator.values();
		int length = values.length;
		String[] ss = new String[length];
		for (int i = 0; i < length; i++) {
			LineSparator lineFeeder = values[i];
			ss[i] = lineFeeder.getName();
		}
		comboBoxLineSeparator.setModel(new DefaultComboBoxModel<String>(ss));
		comboBoxLineSeparator.setFont(globalFont);
		topPanel.add(comboBoxLineSeparator);

		chckbxHeader = new JCheckBox("Header");
		chckbxHeader.setBackground(Color.WHITE);
		chckbxHeader.setFont(globalFont);
		topPanel.add(chckbxHeader);

		chckbxRowNames = new JCheckBox("Row names");
		chckbxRowNames.setBackground(Color.WHITE);
		chckbxRowNames.setFont(globalFont);
		topPanel.add(chckbxRowNames);

		previewPanelScrollPane = new JScrollPane();
		TitledBorder titledBorder2 = BorderFactory.createTitledBorder("Data preview : ");
		titledBorder2.setTitleFont(globalFont.deriveFont(Font.BOLD));
		previewPanelScrollPane.setBorder(titledBorder2);
		add(previewPanelScrollPane);

		table = new JTable();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(globalFont);
		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setFont(globalFont.deriveFont(Font.BOLD));
		tableHeader.setOpaque(false);
		tableHeader.setBackground(new Color(32, 136, 203));
		tableHeader.setForeground(new Color(255, 255, 255));
		table.setFont(globalFont);
		previewPanelScrollPane.setViewportView(table);

		JPanel summaryPanel = new JPanel();
		summaryPanel.setBackground(Color.WHITE);
		add(summaryPanel);
		summaryPanel.setLayout(new GridLayout(0, 2, 0, 0));
		TitledBorder createTitledBorder = BorderFactory.createTitledBorder("Summary :");
		createTitledBorder.setTitleFont(globalFont.deriveFont(Font.BOLD));
		summaryPanel.setBorder(createTitledBorder);

		JLabel lblNumberOfNAs = new JLabel(" Number of NAs");
		lblNumberOfNAs.setFont(globalFont);
		summaryPanel.add(lblNumberOfNAs);

		textFieldNumOfNAs = new JTextField();
		textFieldNumOfNAs.setText("0");
		textFieldNumOfNAs.setFont(globalFont);
		summaryPanel.add(textFieldNumOfNAs);
		textFieldNumOfNAs.setColumns(10);

		JLabel lblNumberOfRows = new JLabel(" Number of rows");
		lblNumberOfRows.setFont(globalFont);
		summaryPanel.add(lblNumberOfRows);

		textFieldNumOfRows = new JTextField();
		textFieldNumOfRows.setText("0");
		textFieldNumOfRows.setFont(globalFont);
		summaryPanel.add(textFieldNumOfRows);
		textFieldNumOfRows.setColumns(10);

		JLabel lblNumberOsColumns = new JLabel(" Number os columns");
		lblNumberOsColumns.setFont(globalFont);
		summaryPanel.add(lblNumberOsColumns);

		textFieldNumOfColumns = new JTextField();
		textFieldNumOfColumns.setText("0");
		textFieldNumOfColumns.setFont(globalFont);
		summaryPanel.add(textFieldNumOfColumns);
		textFieldNumOfColumns.setColumns(10);

		JPanel buttomPanel = new JPanel();
		buttomPanel.setBackground(Color.WHITE);
		add(buttomPanel);
		buttomPanel.setLayout(new GridLayout(1, 0, 0, 0));
		TitledBorder titledBorder3 = BorderFactory.createTitledBorder("Further analysis option :");
		titledBorder3.setTitleFont(globalFont.deriveFont(Font.BOLD));
		buttomPanel.setBorder(titledBorder3);

		// 创建一个按钮组
		ButtonGroup btnGroup = new ButtonGroup();

		rdbtnGeneralMatrix = new JRadioButton("General matrix");
		rdbtnGeneralMatrix.setFont(globalFont);
		buttomPanel.add(rdbtnGeneralMatrix);

		rdbtnRNA = new JRadioButton("RNA exp. profiles");
		rdbtnRNA.setFont(globalFont);
		buttomPanel.add(rdbtnRNA);

		rdbtnProtein = new JRadioButton("Protein exp. profiles");
		rdbtnProtein.setFont(globalFont);
		buttomPanel.add(rdbtnProtein);

		// 添加单选按钮到按钮组
		btnGroup.add(rdbtnGeneralMatrix);
		btnGroup.add(rdbtnRNA);
		btnGroup.add(rdbtnProtein);
		rdbtnGeneralMatrix.setSelected(true);

		textFieldNumOfNAs.setEditable(false);
		textFieldNumOfRows.setEditable(false);
		textFieldNumOfColumns.setEditable(false);

	}

	private void addListeners() {
		ActionListener reLoadDataAccordingToUserOptions = e -> {
			reLoadDataTables();
		};
		comboBoxLineSeparator.addActionListener(reLoadDataAccordingToUserOptions);
		chckbxHeader.addActionListener(reLoadDataAccordingToUserOptions);
		chckbxRowNames.addActionListener(reLoadDataAccordingToUserOptions);

		rdbtnGeneralMatrix.addActionListener(e ->{
			if (rdbtnGeneralMatrix.isSelected()) {
				//BioMainFrame.getInstance().getDataEventManager().letRightShowDefaultMatrixMethods();
			}
		});
		
		rdbtnRNA.addActionListener(e ->{
			if (rdbtnRNA.isSelected()) {
				//BioMainFrame.getInstance().getDataEventManager().letRightShowRNAMethods();
			}
		});
		
		rdbtnProtein.addActionListener(e ->{
			if (rdbtnProtein.isSelected()) {
				//BioMainFrame.getInstance().getDataEventManager().letRightShowProMethods();
			}
		});
	}

	private void reLoadDataTables() {
		int selectedIndex = comboBoxLineSeparator.getSelectedIndex();
		LineSparator lineFeeder = LineSparator.values()[selectedIndex];

		String splitReg = lineFeeder.getRegularExp();
		boolean hasColName = chckbxHeader.isSelected();
		boolean hasRowName = chckbxRowNames.isSelected();
		
		//Set The global bean, because every remnant will needed
		bTableContentBean.setLineSeparetorIndex(lineFeeder.getIndexInCombox());
		bTableContentBean.setHasColumn(hasColName);
		bTableContentBean.setHasRows(hasRowName);
		
		//System.out.println(hasColName);
		List<String> readLines = null;
		try {
			readLines = FileUtils.readLines(inputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String firstLine = readLines.get(0);
		String[] firstLineSplits = firstLine.split(splitReg);
		int size = readLines.size();
		String[][] tableElements = null;
		String[] colNames = null;
		
		if (hasColName) {
			tableElements = new String[size - 1][];
			for (int i = 1; i < size; i++) {
				String string = readLines.get(i);
				String[] split = string.split(splitReg);
				tableElements[i - 1] = split;
			}
			colNames = firstLineSplits;
		} else {
			colNames = new String[firstLineSplits.length];
			for (int i = 0; i < colNames.length; i++) {
				colNames[i] = "ColName " + i;
			}
			tableElements = new String[size][];
			for (int i = 0; i < size; i++) {
				String string = readLines.get(i);
				String[] split = string.split(splitReg);
				tableElements[i] = split;
			}

		}

		getTableModel(colNames, tableElements);
	}

	public void loadingInformation(File file) {
		this.inputFile = file;

//		SwingWorker<MatrixTableContentBean, Void> swingWorker = new SwingWorker<MatrixTableContentBean, Void>() {
//			@Override
//			protected MatrixTableContentBean doInBackground() throws Exception {
//				ValidateMatrixTable validateMatrixTable = new ValidateMatrixTable(file);
//				// 这一步是必须的！！否则后面的返回会空
//				validateMatrixTable.getFileFormat();
//				Optional<MatrixTableContentBean> bean = validateMatrixTable.getBean();
//				if (bean.isPresent()) {
//					return bean.get();
//				} else {
//					return null;
//				}
//
//			}
//
//			@Override
//			protected void done() {
//				try {
//					bTableContentBean = get();
//				} catch (InterruptedException | ExecutionException e) {
//					e.printStackTrace();
//				}
//
//				if (bTableContentBean == null) {
//					// 这个理论上是不会发生的，因为一开始数据是会检查过的！
//					System.err.println("An internal logical error happen\tin\t" + getClass());
//					return;
//				}
//				String[] columnNames = bTableContentBean.getColumnNames();
//				String[][] tableElements = bTableContentBean.getTableElements();
//				getTableModel(columnNames, tableElements);
//
//				comboBoxLineSeparator.setSelectedIndex(bTableContentBean.getLineSeparetorIndex());
//				chckbxHeader.setSelected(bTableContentBean.isHasColumn());
//				chckbxRowNames.setSelected(bTableContentBean.isHasRows());
//
//				textFieldNumOfNAs.setText(bTableContentBean.getNumOfNAs() + "");
//				textFieldNumOfRows.setText(bTableContentBean.getNumOfRows() + "");
//				textFieldNumOfColumns.setText(bTableContentBean.getNumOfColumns() + "");
//
//				if (ifFirstTimeLoad) {
//					addListeners();
//					ifFirstTimeLoad = false;
//				}
//				
//			}
//		};
//
//		swingWorker.execute();
	}

	/**
	 * 这里的tableElements 的行数要控制一下
	 * @param columnNames
	 * @param tableElements
	 */
	protected void getTableModel(String[] columnNames, String[][] tableElements) {
		int length = tableElements.length > 50 ? 50 : tableElements.length;
		
		String[][] newTableElements = new String[length][];
		for (int i = 0; i < length; i++) {
			newTableElements[i] = tableElements[i];
		}
		
		if (tableModel == null) {
			tableModel = new DefaultTableModel(newTableElements, columnNames);
			table.setModel(tableModel);
			table.setGridColor(Color.lightGray);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		} else {
			tableModel.setDataVector(newTableElements, columnNames);
		}

	}

	/**
	 * reture an instance of MatrixTableContentBean
	 */
	public Object getInputParameters() {
		return bTableContentBean;
	}

}
