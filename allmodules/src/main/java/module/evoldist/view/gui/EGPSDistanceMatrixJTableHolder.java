package module.evoldist.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.print.PrinterException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import egps2.utils.common.math.matrix.MatrixTriangleOp;
import module.evoldist.view.contorl.SaveDistanceMatrix;


/**
 * 
 * <p> Title: EGPSDistanceMatrixJTable </p>
 * <p> Description:
 * 
 * Usage: 1. new this class();
 *        2. don't forget initializeJtable()
 * </p>
 */
public class EGPSDistanceMatrixJTableHolder extends JPanel {

	private static final long serialVersionUID = -7435411428093044135L;
	private JTable heatMapJtable;
	private JTextPane descriptionsText;
	private MyTableModel model;
	private JPanel holder;
	private final int maxNumOfCharsForOUTs;
	
	private GradientPallet gPallet= new GradientPallet();
	private double maxValueInMatrix = -1;
	private double minValueInMatrix = -1;
	

	private String[] realColumnNames;
	private String[] displayedColumnNames;
	private double[][] realValueMatrix;
	private double[][] displayedValueMatrix;

	private int colorMode = 1;
	private int numOfDigits = 5;
	/** @param displayStyle: 0 downside; 1 upside; 2 all */
	private int displayStyle = 2;
	private final byte UPSIDE_DATA = 1;
	private final byte DOWN_DATA = 0;
	
	private Font jTableFont = getFont().deriveFont(12f);
	
	public EGPSDistanceMatrixJTableHolder(double[][] valueMatrix,String[] realColumnNames) {
		
		displayedColumnNames = new String[realColumnNames.length + 1];
		displayedColumnNames[0] = "  ";
		
		int tmp_numOfChars = 0;
		for (int i = 0; i < realColumnNames.length; i++) {
			int tt = i + 1;
			displayedColumnNames[tt] = " " + tt;
			
			String str = realColumnNames[i];
			int eleLen = str.length();
			if (eleLen > tmp_numOfChars) {
				tmp_numOfChars = eleLen;
			}
		}
		maxNumOfCharsForOUTs = tmp_numOfChars;
		//obtain the max number in matrix
		maxValueInMatrix = valueMatrix[0][0];
		minValueInMatrix = maxValueInMatrix;

		for (double[] ds : valueMatrix) {
			for (double d : ds) {
				if (d > maxValueInMatrix) {
					maxValueInMatrix = d;
				}else if (d < minValueInMatrix) {
					minValueInMatrix = d;
				}
			}
		}
		
		//check the value matrix length
		int len = realColumnNames.length;
		if ( len != valueMatrix.length) {
			realValueMatrix = new double[len][];
			for (int i = 0; i < len; i++) {
				double[] ds = new double[i+1];
				for (int j = 0; j < i; j++) {
					ds[j] = valueMatrix[i-1][j];
				}
				ds[i] = 0;
				realValueMatrix[i] = ds;
			}
		}else {
			this.realValueMatrix = valueMatrix;
		}
		
		this.realColumnNames = realColumnNames;
		
		initializeJtable();
	}
	
	private void initializeJtable() {
		
		descriptionsText = new JTextPane();
		descriptionsText.setEditable(false);
		descriptionsText.setBackground(Color.WHITE);
		descriptionsText.setFont(jTableFont);

		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout());

		this.add(new JScrollPane(getJtableAndLegend()), BorderLayout.CENTER);
		this.add(descriptionsText,BorderLayout.SOUTH);
		
		updateJtableAndLegendState();
	}
	
	private JComponent getJtableAndLegend() {
		model = new MyTableModel();
		heatMapJtable = new JTable(model);

		heatMapJtable.setDefaultRenderer(Double.class, new DoubleTypeCellRenderer());
		// heatMapJtable.setRowHeight(cellHeight);

		heatMapJtable.setShowGrid(true);

		holder = new JPanel(new BorderLayout());

		holder.add(heatMapJtable, BorderLayout.CENTER);
		holder.setBackground(Color.WHITE);
		return holder;
	}
	
	/**
	 * This is an important method to update the UI!
	 * Remember: every time you assigne the global parameters! don't forget to invoke this method.
	 * So the UI will update!
	* @author yudalang
	* @date 2019-7-15
	 */
	public void updateJtableAndLegendState() {
		
		model.setDataVector(transFormDataAccordingToStype(displayStyle), displayedColumnNames);
		heatMapJtable.getColumnModel().getColumn(0).setCellRenderer(new StringCellRenderer());
	
		JTableHeader header = heatMapJtable.getTableHeader(); // 创建表格标题对象
		header.setBackground(Color.WHITE);
		// head.setPreferredSize(new Dimension(head.getWidth(), 35));// 设置表头大小
		header.setFont(jTableFont);// 设置表格字体
		holder.add(header, BorderLayout.NORTH);
		
		if (colorMode > 0 ) {
			holder.add(gPallet, BorderLayout.EAST);
		}else {
			holder.add(new JPanel(), BorderLayout.EAST);
		}
		descriptionsText.setFont(jTableFont);
		repaint();
	}

	/**
	 * @param colorMode : 0 No color; style : 1. BLUE WHITE RED 2.GREEN BLACK REG 3. WHITE YELLOW RED  
	 */
	public void setDisplayedCorlorMode(int colorMode) {
		if (colorMode > 0 ) {
			gPallet.changeCorlorPattern(colorMode);
		}
		this.colorMode = colorMode;
	}
	
	public void setDispalyedLayoutStyle(int displayStyle) {
		this.displayStyle = displayStyle;
	}

	public void setNumberAfterTheDecimalPoint(int t) {
		this.numOfDigits = t;
	}
	
	public void setFontFamilyAndFontSize(Font newFont) {
		this.jTableFont = newFont;
	}
	public Font getjTableFont() {
		return jTableFont;
	}
	
	private Object[][] transFormDataAccordingToStype(int displayStyle) {
		int len = realColumnNames.length;
		Object[][] ret = new Object[len][];

		if (displayStyle == UPSIDE_DATA) {
			displayedValueMatrix = MatrixTriangleOp.downTriangle2up(realValueMatrix);

			for (int i = 0; i < len; i++) {
				String ss = realColumnNames[i];
				Object[] rowVector = new Object[len + 1];
				rowVector[0] = (i + 1) + ". " + ss;

				for (int j = i; j < len; j++) {
					double parseDouble = Double.parseDouble(setDecimalPlaces(displayedValueMatrix[i][j] ));
					rowVector[j + 1] = parseDouble;
				}

				ret[i] = rowVector;
			}

		} else if (displayStyle == DOWN_DATA) {
			for (int i = 0; i < len; i++) {
				String ss = realColumnNames[i];
				Object[] rowVector = new Object[len + 1];
				rowVector[0] = (i + 1) + ". " + ss;

				for (int j = 0; j < i + 1; j++) {
					double parseDouble = Double.parseDouble(setDecimalPlaces(realValueMatrix[i][j] ));
					rowVector[j + 1] = parseDouble;
				}
				ret[i] = rowVector;
			}
		} else {

			displayedValueMatrix = MatrixTriangleOp.downTriangle2full(realValueMatrix);

			for (int i = 0; i < len; i++) {
				String ss = realColumnNames[i];
				Object[] rowVector = new Object[len + 1];
				rowVector[0] = (i + 1) + ". " + ss;

				for (int j = 0; j < len; j++) {
					double parseDouble = Double.parseDouble(setDecimalPlaces(displayedValueMatrix[i][j] ));
					rowVector[j + 1] = parseDouble;
				}
				ret[i] = rowVector;
			}
		}

		return ret;
	}
	
	private String setDecimalPlaces(double value) {
		return String.format("%." + numOfDigits + "f", value);
	}

	private String displayCellInfo(int i, int j) {
		String res = "Distance between " + realColumnNames[i] + " and " + realColumnNames[j - 1] + " is: "
				+ setDecimalPlaces(realValueMatrix[i][j - 1]);
		return res;
	}
	
	public void saveViewPanelAs() {
		SaveDistanceMatrix save = new SaveDistanceMatrix(holder);
		save.SaveData(realColumnNames,realValueMatrix,displayedValueMatrix);
	}

	public void printPanel() throws PrinterException {
		heatMapJtable.print();
	}

	
	

	private class DoubleTypeCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (hasFocus) {
				if (value != null) {
					descriptionsText.setText(displayCellInfo(row, column));
				}
				c.setBackground(Color.LIGHT_GRAY);
			} else {

				dealWithCellColor(c,value);

			}
			return c;

		}

		private void dealWithCellColor(Component c, Object value) {
			if (value == null) {
				c.setBackground(Color.WHITE);
			} else {
				double val = (double) value;
				if (Double.isNaN(val)) {
					c.setBackground(Color.GRAY);
				} else {
					if (colorMode != 0) {
						double v = val / maxValueInMatrix ;
						if (v < 0.0) {
							c.setBackground(new Color(75,0,130));
						}else if (v< 1.0) {
							Color colorFromPallet = gPallet.getColorFromPallet(v);
							c.setBackground(colorFromPallet);	
						}else {
							Color colorFromPallet = gPallet.getColorFromPallet(1.0);
							c.setBackground(colorFromPallet);	
						}
						
					}
					
					this.setFont(jTableFont);
				}

			}
		};

	}
	
	@SuppressWarnings("serial")
	class StringCellRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			this.setValue(table.getValueAt(row, column));
			this.setFont(jTableFont);
			return this;
		}
	}

	public String[] getRealColumnNames() {
		return realColumnNames;
	}
	public double[][] getOrignalValueMatrix() {
		int length = realValueMatrix.length;
		double[][] ret = new double[length - 1][];
		for (int i = 1; i < length; i++) {
			double[] ds = new double[i];
			System.arraycopy(realValueMatrix[i], 0, ds, 0, i);
			ret[i-1] = ds;
		}
		
		return ret;
	}
	
	public double[][] getRealValueMatrix() {
		return realValueMatrix;
	}

}




