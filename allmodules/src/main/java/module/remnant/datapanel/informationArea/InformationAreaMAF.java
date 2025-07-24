package module.remnant.datapanel.informationArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import egps2.UnifiedAccessPoint;

public class InformationAreaMAF extends AbstactInformationArea {
	private static final long serialVersionUID = 7973199616728274869L;
	
	private JTextField searchTextField;
	private JScrollPane scrollPane;

	private JTable table;
	private DefaultTableModel daTableModel;
	private TableRowSorter<TableModel> rowSorter;
	private JButton btnSelectAll;
	private JButton btnUnselectAll;
	private JButton btnReverseSelection;
	private JLabel totalLabel;
	private JLabel choosedLabel;

	private final Object[] columnNames = { "Number", "Operated unit", "Selected" };
	private Object[][] tableElements;
	private final int INDEX_BOOLEAN = 2;
	private final int INDEX_NAME = 1;

	/**
	 * Create the panel.
	 */
	public InformationAreaMAF() {
		setLayout(new BorderLayout(0, 0));

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		add(topPanel, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Sample information");
		lblNewLabel.setFont(globalFont);

		searchTextField = new JTextField();
		searchTextField.setFont(globalFont);
		searchTextField.setColumns(10);

		JLabel lblSearch = new JLabel("Search : ");
		lblSearch.setFont(globalFont);
		GroupLayout gl_topPanel = new GroupLayout(topPanel);
		gl_topPanel.setHorizontalGroup(gl_topPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_topPanel.createSequentialGroup().addContainerGap()
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 62, Short.MAX_VALUE).addComponent(lblSearch)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_topPanel
				.setVerticalGroup(gl_topPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_topPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_topPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
										.addComponent(searchTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblSearch))));
		topPanel.setLayout(gl_topPanel);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.WHITE);
		add(bottomPanel, BorderLayout.SOUTH);

		btnSelectAll = new JButton("Select all");
		btnSelectAll.setFont(globalFont);

		btnUnselectAll = new JButton("Unselect all");
		btnUnselectAll.setFont(globalFont);

		btnReverseSelection = new JButton("Reverse selection");
		btnReverseSelection.setFont(globalFont);
		JLabel lblTotal = new JLabel("Total");
		lblTotal.setFont(globalFont);
		totalLabel = new JLabel("10");
		totalLabel.setFont(globalFont);
		JLabel lblChoosed = new JLabel("Choosed");
		lblChoosed.setFont(globalFont);

		choosedLabel = new JLabel("8");
		choosedLabel.setFont(globalFont);

		GroupLayout gl_bottomPanel = new GroupLayout(bottomPanel);
		gl_bottomPanel.setHorizontalGroup(gl_bottomPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bottomPanel.createSequentialGroup().addContainerGap().addComponent(btnSelectAll)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnUnselectAll)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnReverseSelection)
						.addPreferredGap(ComponentPlacement.RELATED, 201, Short.MAX_VALUE).addComponent(lblTotal)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(totalLabel)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblChoosed)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(choosedLabel).addContainerGap()));
		gl_bottomPanel.setVerticalGroup(gl_bottomPanel.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_bottomPanel.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_bottomPanel.createParallelGroup(Alignment.BASELINE).addComponent(btnSelectAll)
								.addComponent(btnUnselectAll).addComponent(btnReverseSelection)
								.addComponent(choosedLabel).addComponent(lblChoosed).addComponent(totalLabel)
								.addComponent(lblTotal))
						.addContainerGap()));
		bottomPanel.setLayout(gl_bottomPanel);

		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		addListeners();
	}

	private void addListeners() {
		btnSelectAll.addActionListener(e -> {
			for (Object[] objects : tableElements) {
				objects[INDEX_BOOLEAN] = true;
			}
			daTableModel.setDataVector(tableElements, columnNames);

			choosedLabel.setText(tableElements.length + "");
		});

		btnUnselectAll.addActionListener(e -> {
			for (Object[] objects : tableElements) {
				objects[INDEX_BOOLEAN] = false;
			}
			daTableModel.setDataVector(tableElements, columnNames);
			choosedLabel.setText("0");
		});

		btnReverseSelection.addActionListener(e -> {

			Vector<?> dataVector = daTableModel.getDataVector();
			int s1 = dataVector.size();
			Object[][] localTmp = new Object[s1][];
			int numOfSelectedItems = 0;
			for (int i = 0; i < s1; i++) {
				Vector<?> vv = (Vector<?>) dataVector.get(i);
				int s2 = vv.size();
				Object[] tmpV = new Object[s2];
				for (int j = 0; j < s2; j++) {
					if (j == INDEX_BOOLEAN) {
						boolean tt = (Boolean) vv.get(j);
						tmpV[j] = !tt;
					} else {
						tmpV[j] = vv.get(j);
					}

				}
				localTmp[i] = tmpV;

				if ((boolean) tmpV[INDEX_BOOLEAN]) {
					numOfSelectedItems++;
				}
			}
			daTableModel.setDataVector(localTmp, columnNames);

			choosedLabel.setText(numOfSelectedItems + "");
		});
	}

	public void loadingInformationsOnce(List<String> list) {
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		int size = list.size();
		tableElements = new Object[size][];
		for (int i = 0; i < size; i++) {
			int order = i + 1;
			Object[] tt = new Object[] { order, list.get(i), true };
			tableElements[i] = tt;
		}

		daTableModel = new DefaultTableModel(tableElements, columnNames) {
			private static final long serialVersionUID = 4256085272701747095L;

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == INDEX_BOOLEAN) {
					return true;
				}
				return false;
			}
		};
		daTableModel.addTableModelListener(e -> {

			int[] selectedRow = table.getSelectedRows();
			int[] selectedColumns = table.getSelectedColumns();

			for (int i = 0; i < selectedRow.length; i++) {
				for (int j = 0; j < selectedColumns.length; j++) {
					Object valueAt = table.getValueAt(selectedRow[i], selectedColumns[j]);
					if (valueAt instanceof Boolean) {
						String text = choosedLabel.getText();
						int parseInt = Integer.parseInt(text);
						if ((boolean) valueAt) {
							parseInt++;
							choosedLabel.setText(parseInt + "");
						} else {
							parseInt--;
							choosedLabel.setText(parseInt + "");
						}
					}
				}
			}
		});
		table = new JTable(daTableModel) {

			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
//	                    case 0:
//	                        return Integer.class;
//	                    case 1:
//	                        return String.class;
				case 2:
					return Boolean.class;
				default:
					return String.class;
				}
			}
		};

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(globalFont);
		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setFont(globalFont.deriveFont(Font.BOLD));
		tableHeader.setOpaque(false);
		tableHeader.setBackground(new Color(32, 136, 203));
		tableHeader.setForeground(new Color(255, 255, 255));

		rowSorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorter);

		searchTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = searchTextField.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = searchTextField.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods,
																				// choose Tools | Templates.
			}

		});

		table.setFont(globalFont);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setGridColor(Color.lightGray);
		scrollPane.setViewportView(table);

		String text = tableElements.length + "";
		totalLabel.setText(text);
		choosedLabel.setText(text);
	}

	@Override
	public void loadingInformation(File file) {
	}

	@Override
	public Object getInputParameters() {
		return getSelectedItems();
	}

	private String[] getSelectedItems() {
		Vector<?> dataVector = daTableModel.getDataVector();
		int s1 = dataVector.size();
		List<String> strings = new ArrayList<String>();
		for (int i = 0; i < s1; i++) {
			Vector<?> vv = (Vector<?>) dataVector.get(i);
			boolean tt = (Boolean) vv.get(INDEX_BOOLEAN);
			if (tt) {
				String ss = (String) vv.get(INDEX_NAME);
				strings.add(ss);
			}
		}

		return strings.toArray(new String[0]);
	}

}
