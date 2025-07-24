package module.evoldist.view.gui;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
    public Class getColumnClass(int c) {
        if (c > 0) {
			return Double.class;
		}
        return getValueAt(0, c).getClass();
    }
}