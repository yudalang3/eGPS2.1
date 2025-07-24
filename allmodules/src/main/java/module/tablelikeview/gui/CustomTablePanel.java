package module.tablelikeview.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class CustomTablePanel extends JPanel {
	private JTable table;
	private DefaultTableModel model;

	private int prefColumnWidth = 200;

	public CustomTablePanel() {
		setLayout(new BorderLayout());

		// 创建表格模型
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0; // 第一列（行号）不可编辑
			}
		};

		// 创建表格
		table = new JTable(model);
		

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.getTableHeader().setReorderingAllowed(false);

		// 设置表格外观
		table.setRowHeight(25);

		LaunchProperty lauchProperty = UnifiedAccessPoint.getLaunchProperty();
		Font defaultFont = lauchProperty.getDefaultFont();
		Font defaultTitleFont = lauchProperty.getDefaultTitleFont();
		table.setFont(defaultFont);
		table.getTableHeader().setFont(defaultTitleFont);
		table.setSelectionBackground(new Color(173, 216, 230));
		table.setGridColor(new Color(211, 211, 211));

		// 添加表格到滚动面板
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void loadData(List<List<String>> input, List<String> headerLine) {
		// 清除现有数据
		model.setRowCount(0);
		model.setColumnCount(0);

		if (input == null || input.isEmpty()) {
			return;
		}

		for (String string : headerLine) {
			model.addColumn(string);
		}

		// 添加数据行
		for (List<String> line : input) {
			Vector<Object> row = new Vector<>();
			row.addAll(line);
			model.addRow(row);
		}

		adjustColumnWidths();
		// 设置行号列的渲染器
		table.setIntercellSpacing(new Dimension(8, 2));
		TableColumnModel columnModel = table.getColumnModel();
		if (columnModel.getColumnCount() > 0) {

			columnModel.getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
				{
					setHorizontalAlignment(JLabel.CENTER);
					setBackground(new Color(240, 240, 240));
					setFont(getFont().deriveFont(Font.BOLD));
				}
			});
		}
	}

	public void adjustColumnWidths() {
		TableColumnModel columnModel = table.getColumnModel();
		TableColumn column2 = columnModel.getColumn(0);
		column2.setMaxWidth(80);

		int columnCount = columnModel.getColumnCount();
		for (int i = 1; i < columnCount; i++) {
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(prefColumnWidth);
		}
	}

	public void changeJTableResizeMode(int autoResizeAllColumns) {
		table.setAutoResizeMode(autoResizeAllColumns);
	}

	public void setPrefColumnWidth(int prefColumnWidth) {
		this.prefColumnWidth = prefColumnWidth;
	}

}

