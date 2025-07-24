package module.maplot.maplot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.panels.dialog.EGPSJSpinner;
import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.maplot.MainFace;
import module.maplot.maplot.listener.JTextFieldListener;
import module.maplot.maplot.model.TransformedMAData;
import module.maplot.maplot.model.ValuePoint;
import egps2.panels.DialogUtil;

@SuppressWarnings("serial")
public class LeftToolJPanel extends JPanel {

	private MainFace mainPanel;


	protected Font defaultFontFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	public final Font taskPaneTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private MA_PlotDrawProperties drawProperties;

	private JXTaskPane parametersPanel;
	private JTextField customTitleField;
	private JTextField xAxisTitleField;
	private JTextField yAxisTitleField;
	private JTextFieldListener textFieldListener;
	private EGPSJSpinner scatterSize;
	private JXTaskPane transformationPanel;
	private JTextField textField;
	private JRadioButton rdbtnNoTransformation;
	private JRadioButton rdbtnLogTransformation;

	private JComboBox<String> jComboBox1LogMethod;
	private JXTaskPane displayPanel;
	
	private double defaultLogBase = 2;


	private CriticalValuePanel criticalValuePanel;

	public LeftToolJPanel(MainFace plotMain) {

		this.mainPanel = plotMain;
		this.drawProperties = plotMain.getMA_PlotDrawProperties();
		this.textFieldListener = new JTextFieldListener(plotMain);
		this.defaultFontFont = drawProperties.getDefaultFont();

		setLayout(new BorderLayout());

		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
		taskPaneContainer.setBackground(Color.white);
		taskPaneContainer.setBackgroundPainter(null);
		setMinimumSize(new Dimension(250, 200));

		addJXTaskPanels(taskPaneContainer);

		add(new JScrollPane(taskPaneContainer), BorderLayout.CENTER);
	}

	private void addJXTaskPanels(JXTaskPaneContainer taskPaneContainer) {

		taskPaneContainer.add(getTransformationPanel());
		
		JButton showCurrentData = new JButton("Show plot data");
		showCurrentData.setFont(defaultFontFont);
		showCurrentData.addActionListener(e -> {
			StringBuilder sBuilder = new StringBuilder();
			List<ValuePoint> valuePoints = drawProperties.getCurrDrawingDataModel().getValuePoints();
			List<String> lists = new ArrayList<>();
			for (ValuePoint valuePoint : valuePoints) {
				sBuilder.setLength(0);
				sBuilder.append(valuePoint.getId()).append("\t");
				sBuilder.append(valuePoint.getXValue()).append("\t");
				sBuilder.append(valuePoint.getYValue());
				lists.add( sBuilder.toString());
			}
			
			DialogUtil.showDialog(lists);
			mainPanel.invokeTheFeatureOfIndex(1);
		});
		taskPaneContainer.add(showCurrentData);
		
		taskPaneContainer.add(getParametersPanel());
		taskPaneContainer.add(getDisplayPanel());
		addListener();
	}

	private JXTaskPane getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JXTaskPane();
			displayPanel.setTitle("Display");
			displayPanel.setFont(taskPaneTitleFont);
			criticalValuePanel = new CriticalValuePanel(mainPanel);
			displayPanel.add(criticalValuePanel);
		}

		return displayPanel;
	}

	private JXTaskPane getTransformationPanel() {
		if (transformationPanel == null) {
			transformationPanel = new JXTaskPane();

			transformationPanel.setTitle("Data Transformation");
			transformationPanel.setFont(taskPaneTitleFont);

			JPanel transformations = new JPanel();

			rdbtnNoTransformation = new JRadioButton("Original Data");
			rdbtnNoTransformation.setToolTipText("No any data transformation, x axis is the condition1, y axis is the condition2.");
			rdbtnNoTransformation.setFont(defaultFontFont);
			rdbtnNoTransformation.setFocusPainted(false);
			rdbtnLogTransformation = new JRadioButton("Do MA plot");
			rdbtnLogTransformation.setFont(defaultFontFont);
			rdbtnLogTransformation.setFocusPainted(false);

			JLabel lblLogMethod = new JLabel("Log method");
			lblLogMethod.setFont(defaultFontFont);

			JLabel lblLogBase = new JLabel("Log base");
			lblLogBase.setFont(defaultFontFont);

			jComboBox1LogMethod = new JComboBox<String>();
			jComboBox1LogMethod.setFont(defaultFontFont);
			jComboBox1LogMethod.setFocusable(false);
			jComboBox1LogMethod.setModel(
					new javax.swing.DefaultComboBoxModel<>(new String[] { "Direct",  "Log(x)", "Log(x + 1)", "Log(x+|min|+1)" }));
			jComboBox1LogMethod.setSelectedIndex(2);
			textField = new JTextField();
			textField.setColumns(10);
			textField.setText(String.valueOf(defaultLogBase));
			ButtonGroup group = new ButtonGroup();
			group.add(rdbtnNoTransformation);
			group.add(rdbtnLogTransformation);
			
			rdbtnNoTransformation.setSelected(true);
			jComboBox1LogMethod.setEnabled(false);
			textField.setEnabled(false);
			GroupLayout groupLayout = new GroupLayout(transformations);
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
					.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup().addContainerGap()
									.addComponent(rdbtnNoTransformation))
							.addGroup(groupLayout.createSequentialGroup().addContainerGap()
									.addComponent(rdbtnLogTransformation))
							.addGroup(groupLayout.createSequentialGroup().addGap(38)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addGroup(groupLayout.createSequentialGroup()
													.addComponent(lblLogBase, GroupLayout.PREFERRED_SIZE, 100,
															GroupLayout.PREFERRED_SIZE)
													.addGap(4).addComponent(textField, GroupLayout.PREFERRED_SIZE,
															GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addGroup(groupLayout.createSequentialGroup()
													.addComponent(lblLogMethod, GroupLayout.PREFERRED_SIZE, 100,
															GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(jComboBox1LogMethod, GroupLayout.PREFERRED_SIZE,
															GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(10, Short.MAX_VALUE)));
			groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(rdbtnNoTransformation)
							.addPreferredGap(ComponentPlacement.RELATED).addComponent(rdbtnLogTransformation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblLogMethod)
									.addComponent(jComboBox1LogMethod, GroupLayout.PREFERRED_SIZE,
											GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblLogBase)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE))
							.addContainerGap(10, Short.MAX_VALUE)));
			transformations.setLayout(groupLayout);

			transformationPanel.add(transformations);

		}

		return transformationPanel;
	}

	private JXTaskPane getParametersPanel() {
		if (parametersPanel == null) {
			parametersPanel = new JXTaskPane();
			parametersPanel.setTitle("Parameters");
			parametersPanel.setFont(taskPaneTitleFont);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(10, 20, 5, 0);
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			JPanel parameters = new JPanel(new GridBagLayout());
			JLabel titleLabel = new JLabel("Title");
			titleLabel.setFont(defaultFontFont);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			parameters.add(titleLabel, gridBagConstraints);
			customTitleField = new JTextField(15);
			customTitleField.setText("MA plot");
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			parameters.add(customTitleField, gridBagConstraints);

			JLabel scatterSizeLable = new JLabel("Point size");
			scatterSizeLable.setFont(defaultFontFont);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			parameters.add(scatterSizeLable, gridBagConstraints);
			// scatterSizeField = new JTextField(15);
			scatterSize = new EGPSJSpinner(5, 1, 20, 1);
			scatterSize.setPreferredSize(new Dimension(96, 23));

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			parameters.add(scatterSize, gridBagConstraints);

			JLabel xAxisTitleLable = new JLabel("X axis lable");
			xAxisTitleLable.setFont(defaultFontFont);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			parameters.add(xAxisTitleLable, gridBagConstraints);
			xAxisTitleField = new JTextField(15);
			xAxisTitleField.setText("X axis");
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			parameters.add(xAxisTitleField, gridBagConstraints);

			JLabel yAxisTitleLable = new JLabel("Y axis lable");
			yAxisTitleLable.setFont(defaultFontFont);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			parameters.add(yAxisTitleLable, gridBagConstraints);
			yAxisTitleField = new JTextField(15);
			yAxisTitleField.setText("Y axis");
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 3;
			parameters.add(yAxisTitleField, gridBagConstraints);
			parametersPanel.add(parameters);

		}

		return parametersPanel;
	}
	
	private void doDataTransformation() {
		TransformedMAData transformedMAData = new TransformedMAData(drawProperties,drawProperties.getOriginalDataModel());
		drawProperties.setCurrDrawingDataModel(transformedMAData);
	}

	private void addListener() {
		rdbtnNoTransformation.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				drawProperties.setReCalculate(true);

				drawProperties.setCurrDrawingDataModel(drawProperties.getOriginalDataModel());
				
				jComboBox1LogMethod.setEnabled(false);
				textField.setEnabled(false);
				mainPanel.getRightTabbedPanel().repaint();
				
				xAxisTitleField.setText("X axis");
				yAxisTitleField.setText("Y axis");
			}

		});
		rdbtnLogTransformation.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				drawProperties.setReCalculate(true);
				
				doDataTransformation();
				
				jComboBox1LogMethod.setEnabled(true);
				textField.setEnabled(true);
				mainPanel.getRightTabbedPanel().repaint();
				
				xAxisTitleField.setText("ElegantJTable");
				yAxisTitleField.setText("M");
				
				mainPanel.invokeTheFeatureOfIndex(0);
			}
		});

		jComboBox1LogMethod.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				drawProperties.setReCalculate(true);
				doDataTransformation();
				
				mainPanel.getRightTabbedPanel().repaint();
			}
		});

		textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String text = textField.getText();
            	if ("".equalsIgnoreCase(text) || text == null) {
					jComboBox1LogMethod.setEnabled(false);
					return;
				}

				if (text.equalsIgnoreCase("e")) {

					jComboBox1LogMethod.setEnabled(true);

					defaultLogBase = Math.E;
					drawProperties.setReCalculate(true);
					doDataTransformation();

					mainPanel.getRightTabbedPanel().repaint();

					return;

				}

				try {
					double valueOf = Double.parseDouble(text);

					if (valueOf <= 1) {

						jComboBox1LogMethod.setEnabled(false);

						String title = "input error";
						String message = "The value you enter need to be a greater than 1 !";
						SwingDialog.showErrorMSGDialog(UnifiedAccessPoint.getInstanceFrame(),title, message);

						return;

					}
					drawProperties.setReCalculate(true);
					defaultLogBase = valueOf;
					doDataTransformation();

					jComboBox1LogMethod.setEnabled(true);

					mainPanel.getRightTabbedPanel().repaint();

				} catch (NumberFormatException e2) {
					jComboBox1LogMethod.setEnabled(false);

					String title = "input error";
					String message = "The value you enter need to be an integer !";
					SwingDialog.showErrorMSGDialog(UnifiedAccessPoint.getInstanceFrame(),title, message);

					return;
				}
            }
        });
		

		customTitleField.getDocument().addDocumentListener(this.textFieldListener);

		scatterSize.addChangeListener(e -> {
			mainPanel.repaint();
			mainPanel.invokeTheFeatureOfIndex(2);
		});
		xAxisTitleField.getDocument().addDocumentListener(this.textFieldListener);
		yAxisTitleField.getDocument().addDocumentListener(this.textFieldListener);
	}

	public JTextField getxAxisTitleField() {
		return xAxisTitleField;
	}

	public void setxAxisTitleField(JTextField xAxisTitleField) {
		this.xAxisTitleField = xAxisTitleField;
	}

	public JTextField getyAxisTitleField() {
		return yAxisTitleField;
	}

	public void setyAxisTitleField(JTextField yAxisTitleField) {
		this.yAxisTitleField = yAxisTitleField;
	}

	public JTextFieldListener getTextFieldListener() {
		return textFieldListener;
	}


	public EGPSJSpinner getScatterSize() {
		return scatterSize;
	}

	public void setScatterSize(EGPSJSpinner scatterSize) {
		this.scatterSize = scatterSize;
	}

	public JTextField getCustomTitleField() {
		return customTitleField;
	}

	public void setCustomTitleField(JTextField customTitleField) {
		this.customTitleField = customTitleField;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JRadioButton getRdbtnNoTransformation() {
		return rdbtnNoTransformation;
	}

	public void setRdbtnNoTransformation(JRadioButton rdbtnNoTransformation) {
		this.rdbtnNoTransformation = rdbtnNoTransformation;
	}

	public JRadioButton getRdbtnLogTransformation() {
		return rdbtnLogTransformation;
	}

	public void setRdbtnLogTransformation(JRadioButton rdbtnLogTransformation) {
		this.rdbtnLogTransformation = rdbtnLogTransformation;
	}

	public String getTheUserOperatedOptions() {
		if(getRdbtnNoTransformation().isSelected()) {
			return "Directly plot the original mean of groups.";
		}else {
			return "Do log transformation with type: " + jComboBox1LogMethod.getSelectedItem().toString();
		}
	}


	public double getBase() {
		return defaultLogBase;

	}

	/**
	 * "Direct",  "Log(x)", "Log(x + 1)", "Log(x+|min|+1)"
	 * @return
	 */
	public int getLogMethodSelectedIndex() {
		int selectedIndex = jComboBox1LogMethod.getSelectedIndex();
		return selectedIndex;
	}
	
	public CriticalValuePanel getCriticalValuePanel() {
		return criticalValuePanel;
	}

}
