package module.maplot.maplot.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import egps2.UnifiedAccessPoint;
import module.maplot.MainFace;
import module.maplot.maplot.model.DataModel;
import module.maplot.maplot.model.ValuePoint;
import egps2.panels.DialogUtil;

@SuppressWarnings("serial")
public class CriticalValuePanel extends JPanel {
	private JTextField textField;
	private JCheckBox ckBox_showSignificant;

	private double criticalValue = 3.00;
	private MA_PlotDrawProperties drawProperties;
	private MainFace mainPanel;
	private JButton btnExport;

	/**
	 * Create the panel.
	 */
	public CriticalValuePanel(MainFace mainPanel) {
		this.drawProperties = mainPanel.getMA_PlotDrawProperties();
		this.mainPanel = mainPanel;

		setBorder(new EmptyBorder(15, 15, 5, 5));

		Font defaultFontFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		ckBox_showSignificant = new JCheckBox("Show significant line");
		ckBox_showSignificant.setFont(defaultFontFont);
		GridBagConstraints gbc_ckBox_showSignificant = new GridBagConstraints();
		gbc_ckBox_showSignificant.anchor = GridBagConstraints.WEST;
		gbc_ckBox_showSignificant.gridwidth = 3;
		gbc_ckBox_showSignificant.insets = new Insets(0, 0, 5, 0);
		gbc_ckBox_showSignificant.gridx = 0;
		gbc_ckBox_showSignificant.gridy = 0;
		add(ckBox_showSignificant, gbc_ckBox_showSignificant);

		JLabel lblNewLabel = new JLabel("Fold change value");
		lblNewLabel.setFont(defaultFontFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 5, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);

		textField = new JTextField();
		textField.setText(String.valueOf(criticalValue));
		textField.setFont(defaultFontFont);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		add(textField, gbc_textField);
		textField.setColumns(5);

		btnExport = new JButton("Export Sig. genes");
		btnExport.setFont(defaultFontFont);
		btnExport.setEnabled(false);

		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExport.gridwidth = 3;
		gbc_btnExport.gridx = 0;
		gbc_btnExport.gridy = 3;
		add(btnExport, gbc_btnExport);

		addListners();

	}

	private void addListners() {

		// 添加监听器，当文本框内容变化时更新滑块位置
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				drawProperties.setReCalculate(true);
				try {
					criticalValue = Double.parseDouble(textField.getText());

				} catch (NumberFormatException ex) {
					// 处理非数字输入的情况，这里可以根据需要添加其他处理逻辑
					textField.setText("Error");
				}

				mainPanel.getRightTabbedPanel().repaint();
			}
		});

		ckBox_showSignificant.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				drawProperties.setReCalculate(true);
				mainPanel.getRightTabbedPanel().repaint();

				boolean selected = e.getStateChange() == ItemEvent.SELECTED;
				btnExport.setEnabled(selected);

				if (selected) {
					mainPanel.invokeTheFeatureOfIndex(3);
				}
			}
		});

		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					@Override
					public void run() {
						mainPanel.invokeTheFeatureOfIndex(4);
						
						LeftToolJPanel leftToolJPanel = drawProperties.getLeftToolJPanel();
						double cV = leftToolJPanel.getCriticalValuePanel().getCriticalValue();
						DataModel model = drawProperties.getCurrDrawingDataModel();
						List<ValuePoint> valuePoints = model.getValuePoints();
						List<ValuePoint> keptValues = new ArrayList<>();

						for (ValuePoint valuePoint : valuePoints) {
							if (Math.abs(valuePoint.getYValue()) > cV) {
								keptValues.add(valuePoint);
							}
						}

						Comparator<ValuePoint> comparator = new Comparator<ValuePoint>() {
							@Override
							public int compare(ValuePoint o1, ValuePoint o2) {
								int compare = Double.compare(o2.getYValue(), o1.getYValue());
								return compare;
							}
						};
						Collections.sort(keptValues, comparator);

						exportSigGenes(keptValues, drawProperties);
					}

				}.start();

			}
		});
	}

	private void exportSigGenes(List<ValuePoint> keptValues, MA_PlotDrawProperties drawProperties) {
		DataModel model = drawProperties.getCurrDrawingDataModel();

		StringBuffer sb = new StringBuffer();
		sb.append("A value\t");
		sb.append("M value");

		List<String> headerFields = model.getHeaderFields();
		for (String string : headerFields) {
			sb.append("\t").append(string);
		}

		List<String> output = new ArrayList<>();
		output.add(sb.toString());

		for (ValuePoint point : keptValues) {
			List<String> annotations = point.getAnnotations();
			sb.setLength(0);
			sb.append(point.getXValue()).append("\t");
			sb.append(point.getYValue()).append("\t");
			for (String string : annotations) {
				sb.append("\t").append(string);
			}
			output.add(sb.toString());
		}

		DialogUtil.showDialog(output);

	};

	public double getCriticalValue() {
		return criticalValue;
	}

	public boolean shouldPaintIndicatingLine() {
		return ckBox_showSignificant.isSelected();
	}

}
