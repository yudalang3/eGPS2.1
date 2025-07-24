package module.remnant.datapanel.informationArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import egps2.UnifiedAccessPoint;

public abstract class AbstractTableBasedArea extends AbstactInformationArea{
	private static final long serialVersionUID = 1481963980351932821L;
	
	protected DefaultTableModel tableModel;
	protected JTable table;
	
	public AbstractTableBasedArea() {
		setLayout(new BorderLayout());
		setBackground(Color.white);
		
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		
		TitledBorder createTitledBorder = BorderFactory.createTitledBorder("General file information");
		createTitledBorder.setTitleFont(globalFont.deriveFont(Font.BOLD).deriveFont(20f));
		setBorder(createTitledBorder);
		
		String[] columnNames = { "Item", "Value" };
        String [][]tableVales={{"L","W"},{"O","ElegantJTable"},{"ElegantJTable","B3"},{"D","I"},{"","T"}};
        
        tableModel = new DefaultTableModel(tableVales,columnNames);
        table = new JTable(tableModel);  
//        final int wordWrapColumnIndex = 1;
//        table = new JTable(tableModel) {    
//			private static final long serialVersionUID = -336236748696337491L;
//
//			public TableCellRenderer getCellRenderer(int row, int column) {
//                if (column == wordWrapColumnIndex ) {
//                    return new MultilineTableCell();
//                }
//                else {
//                    return super.getCellRenderer(row, column);
//                }
//            }
//        };
        //单选
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
		
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));
		add(jScrollPane, BorderLayout.CENTER);

		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setFont(globalFont.deriveFont(Font.BOLD));
		tableHeader.setOpaque(false);
		tableHeader.setBackground(new Color(32, 136, 203));
		tableHeader.setForeground(new Color(255, 255, 255));
		table.setFont(globalFont);
		
		table.setRowMargin(10);
        table.setRowHeight(50);
	}

	@Override
	public void loadingInformation(File file) {
		// 添加一行，记载文件的路径
		String[] rowValues = { Constants.INPUT_PATH_NAME, file.getAbsolutePath() };
		int rowCount = tableModel.getRowCount();
		for (int i = rowCount - 1; i > -1; i--) {
			tableModel.removeRow(i);
		}
		tableModel.addRow(rowValues);
		
		table.getColumnModel().getColumn(0).setMaxWidth(160);
		table.getColumnModel().getColumn(0).setMinWidth(160);
		
	}

	@Override
	public Object getInputParameters() {
		return null;
	}
	
	

}
