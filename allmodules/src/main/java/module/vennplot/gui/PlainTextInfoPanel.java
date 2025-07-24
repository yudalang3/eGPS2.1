package module.vennplot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;
import java.util.StringJoiner;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import egps2.UnifiedAccessPoint;
import module.vennplot.VennPlotController;
import module.vennplot.model.DataModel;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SetItem;

@SuppressWarnings("serial")
public class PlainTextInfoPanel extends JPanel {
	private JTable table;
	private VennPlotController controller;
	private DefaultTableModel defaultTableModel;
	private JTextArea textArea;

	public PlainTextInfoPanel() {
		
		defaultTableModel = new DefaultTableModel();
		
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(20, 20, 20, 20));
		setLayout(new BorderLayout(0, 30));
		
		table = new JTable(defaultTableModel);
		
		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		table.setFont(globalFont);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(globalFont);
		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setFont(globalFont.deriveFont(Font.BOLD));
		tableHeader.setOpaque(false);
		tableHeader.setBackground(new Color(32, 136, 203));
		tableHeader.setForeground(new Color(255, 255, 255));
		table.setRowHeight(20);
		
		
		
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		textArea = new JTextArea();
		add(textArea, BorderLayout.NORTH);
		textArea.setFont(globalFont.deriveFont(20f));
		
		
	}

	public void setController(VennPlotController controller) {
		this.controller = controller;
		
	}

	public void initializePanel() {
		ParameterModel parameterModel = controller.getParameterModel();
		if (parameterModel == null) {
			return;
		}
		
		IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
		
		
		Object[] header = new Object[] {"Intersection","Count","Elements"};
		
		List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();
		DataModel dataModel = controller.getDataModel();
		SetItem[] setItems = dataModel.getSetItems();
		
		int size = eles.size();
		Object[][] rowData = new Object[size][];
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			IntersectionRegionElement intersectionRegionElement = eles.get(i);
			String[] elementNames = intersectionRegionElement.getElementNames();
			byte[] booleans = intersectionRegionElement.getBooleans();
			
			String intersection = getIntersectionName(booleans,setItems);
			int count = elementNames.length;
			
			sb.setLength(0);
			for (String b : elementNames) {
				sb.append(b).append(";");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			
			Object[] row = new Object[] {intersection, String.valueOf(count) , sb.toString()};
			rowData[i] = row;
		}
		
		defaultTableModel.setDataVector(rowData, header);
		
		
		
		int numOfAllUniqueElements = dataModel.getNumOfAllUniqueElements();
		int numOfAllIntersectionElements = 0;
		for (IntersectionRegionElement intersectionRegionElement : eles) {
			byte[] booleans = intersectionRegionElement.getBooleans();
			boolean meet = true;
			for (byte bbb : booleans) {
				if (bbb != 1) {
					meet = false;
					break;
				}
			}
			if (meet) {
				numOfAllIntersectionElements = intersectionRegionElement.getCount();
				break;
			}
		}
		
		double index = numOfAllIntersectionElements / (double) numOfAllUniqueElements;
		
		DecimalFormat df = new DecimalFormat("0.000000");
		String text = "Num. of intersection of all sets is: "+ numOfAllIntersectionElements + " union is : " + numOfAllUniqueElements + "\nJarcard index is : " + df.format(index);
		textArea.setText(text);

		
	}

	private String getIntersectionName(byte[] booleans, SetItem[] setItems) {
		StringJoiner stringJoiner = new StringJoiner("&");
		for (int i = 0; i < setItems.length; i++) {
			if (booleans[i] == 1) {
				stringJoiner.add(setItems[i].getName());
			}
		}
		return stringJoiner.toString();
	}
	
	public JTable getTable() {
		return table;
	}

}
