package module.heatmap.eheatmap.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.model.DataModel;
import egps2.panels.reusablecom.ParameterInitialized;

public class LeftDataTransformationPanel extends JPanel implements ParameterInitialized {

	private static final long serialVersionUID = 7643242531763464117L;

	private final EheatmapMain eheatmapMain;
	private HeatmapController controller;
	
	private javax.swing.JComboBox<String> jComboBox1LogMethod;
	private javax.swing.JComboBox<String> jComboBoxCoxBoxMethod;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JRadioButton jRadioButtonNoTransforamtion;
	private javax.swing.JRadioButton jRadioButtonLogTrans;
	private javax.swing.JRadioButton jRadioButtonZscoreTrans;
	private javax.swing.JRadioButton jRadioButtonMinMaxScaling;
	private javax.swing.JRadioButton jRadioButtonCoxBox;
	private javax.swing.JTextField jTextFieldLogBase;
	private javax.swing.JTextField jTextFieldAvgLambda;

	private ButtonGroup buttonGroup;

	public LeftDataTransformationPanel(EheatmapMain main) {
		this.eheatmapMain = main;
		initComponents();
	}

	public HeatmapController getController() {
		if (controller == null) {
			controller = (HeatmapController) eheatmapMain.getController();
		}
		return controller;
	}

	private void initComponents() {
		
		setBorder(BorderFactory.createEmptyBorder(8, 6, 10, 6));

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		buttonGroup = new ButtonGroup();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 105, 96, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 25, 25, 25, 25, 23, 23, 25, 17, 23, 21, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		jRadioButtonNoTransforamtion = new javax.swing.JRadioButton();

		jRadioButtonNoTransforamtion.setFont(globalFont); 
		jRadioButtonNoTransforamtion.setText("No transformation");

		buttonGroup.add(jRadioButtonNoTransforamtion);
		jRadioButtonNoTransforamtion.setSelected(true);
		GridBagConstraints gbc_jRadioButtonNoTransforamtion = new GridBagConstraints();
		gbc_jRadioButtonNoTransforamtion.fill = GridBagConstraints.HORIZONTAL;
		gbc_jRadioButtonNoTransforamtion.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonNoTransforamtion.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonNoTransforamtion.gridwidth = 2;
		gbc_jRadioButtonNoTransforamtion.gridx = 0;
		gbc_jRadioButtonNoTransforamtion.gridy = 0;
		add(jRadioButtonNoTransforamtion, gbc_jRadioButtonNoTransforamtion);
		jRadioButtonZscoreTrans = new javax.swing.JRadioButton();

		jRadioButtonZscoreTrans.setFont(globalFont); 
		jRadioButtonZscoreTrans.setText("Z-score standardization");
		buttonGroup.add(jRadioButtonZscoreTrans);
		GridBagConstraints gbc_jRadioButtonZscoreTrans = new GridBagConstraints();
		gbc_jRadioButtonZscoreTrans.fill = GridBagConstraints.HORIZONTAL;
		gbc_jRadioButtonZscoreTrans.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonZscoreTrans.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonZscoreTrans.gridwidth = 2;
		gbc_jRadioButtonZscoreTrans.gridx = 0;
		gbc_jRadioButtonZscoreTrans.gridy = 1;
		add(jRadioButtonZscoreTrans, gbc_jRadioButtonZscoreTrans);
		jRadioButtonMinMaxScaling = new javax.swing.JRadioButton();

		jRadioButtonMinMaxScaling.setFont(globalFont); 
		jRadioButtonMinMaxScaling.setText("Min-Max scaling");
		buttonGroup.add(jRadioButtonMinMaxScaling);
		GridBagConstraints gbc_jRadioButtonMinMaxScaling = new GridBagConstraints();
		gbc_jRadioButtonMinMaxScaling.fill = GridBagConstraints.HORIZONTAL;
		gbc_jRadioButtonMinMaxScaling.gridwidth = 2;
		gbc_jRadioButtonMinMaxScaling.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonMinMaxScaling.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonMinMaxScaling.gridx = 0;
		gbc_jRadioButtonMinMaxScaling.gridy = 2;
		add(jRadioButtonMinMaxScaling, gbc_jRadioButtonMinMaxScaling);
		jRadioButtonLogTrans = new javax.swing.JRadioButton();

		jRadioButtonLogTrans.setFont(globalFont); 
		jRadioButtonLogTrans.setText("Log transformation");
		buttonGroup.add(jRadioButtonLogTrans);
		GridBagConstraints gbc_jRadioButtonLogTrans = new GridBagConstraints();
		gbc_jRadioButtonLogTrans.fill = GridBagConstraints.HORIZONTAL;
		gbc_jRadioButtonLogTrans.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonLogTrans.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonLogTrans.gridwidth = 2;
		gbc_jRadioButtonLogTrans.gridx = 0;
		gbc_jRadioButtonLogTrans.gridy = 3;
		add(jRadioButtonLogTrans, gbc_jRadioButtonLogTrans);
		jLabel1 = new javax.swing.JLabel();

		jLabel1.setFont(globalFont); 
		jLabel1.setText("Log method ");
		GridBagConstraints gbc_jLabel1 = new GridBagConstraints();
		gbc_jLabel1.anchor = GridBagConstraints.EAST;
		gbc_jLabel1.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel1.gridx = 0;
		gbc_jLabel1.gridy = 4;
		add(jLabel1, gbc_jLabel1);
		jComboBox1LogMethod = new javax.swing.JComboBox<>();

		jComboBox1LogMethod.setFont(globalFont); 
		jComboBox1LogMethod.setModel(
				new javax.swing.DefaultComboBoxModel<>(new String[] { "Log(x + 1)", "Log(x)", "Log(x+|min|+1)" }));
		GridBagConstraints gbc_jComboBox1LogMethod = new GridBagConstraints();
		gbc_jComboBox1LogMethod.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBox1LogMethod.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBox1LogMethod.gridx = 1;
		gbc_jComboBox1LogMethod.gridy = 4;
		add(jComboBox1LogMethod, gbc_jComboBox1LogMethod);
		jLabel2 = new javax.swing.JLabel();

		jLabel2.setFont(globalFont); 
		jLabel2.setText("Log base ");
		GridBagConstraints gbc_jLabel2 = new GridBagConstraints();
		gbc_jLabel2.anchor = GridBagConstraints.EAST;
		gbc_jLabel2.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel2.gridx = 0;
		gbc_jLabel2.gridy = 5;
		add(jLabel2, gbc_jLabel2);
		jTextFieldLogBase = new javax.swing.JTextField();

		jTextFieldLogBase.setFont(globalFont); 
		jTextFieldLogBase.setText("e");
		GridBagConstraints gbc_jTextFieldLogBase = new GridBagConstraints();
		gbc_jTextFieldLogBase.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldLogBase.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldLogBase.gridx = 1;
		gbc_jTextFieldLogBase.gridy = 5;
		add(jTextFieldLogBase, gbc_jTextFieldLogBase);
		jRadioButtonCoxBox = new javax.swing.JRadioButton();

		jRadioButtonCoxBox.setFont(globalFont); 
		jRadioButtonCoxBox.setText("Cox-Box");
		buttonGroup.add(jRadioButtonCoxBox);
		GridBagConstraints gbc_jRadioButtonCoxBox = new GridBagConstraints();
		gbc_jRadioButtonCoxBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_jRadioButtonCoxBox.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonCoxBox.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonCoxBox.gridx = 0;
		gbc_jRadioButtonCoxBox.gridy = 6;
		add(jRadioButtonCoxBox, gbc_jRadioButtonCoxBox);
		jLabel3 = new javax.swing.JLabel();

		jLabel3.setFont(globalFont); 
		jLabel3.setText("  Method to estimate lambda");
		GridBagConstraints gbc_jLabel3 = new GridBagConstraints();
		gbc_jLabel3.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabel3.anchor = GridBagConstraints.WEST;
		gbc_jLabel3.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel3.gridwidth = 2;
		gbc_jLabel3.gridx = 0;
		gbc_jLabel3.gridy = 7;
		add(jLabel3, gbc_jLabel3);
		jComboBoxCoxBoxMethod = new javax.swing.JComboBox<>();

		jComboBoxCoxBoxMethod.setFont(globalFont); 
		jComboBoxCoxBoxMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Enumerate" }));
		GridBagConstraints gbc_jComboBoxCoxBoxMethod = new GridBagConstraints();
		gbc_jComboBoxCoxBoxMethod.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxCoxBoxMethod.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxCoxBoxMethod.gridx = 1;
		gbc_jComboBoxCoxBoxMethod.gridy = 8;
		add(jComboBoxCoxBoxMethod, gbc_jComboBoxCoxBoxMethod);
		jLabel4 = new javax.swing.JLabel();

		jLabel4.setFont(globalFont); 
		jLabel4.setText("  Average lambda ");
		GridBagConstraints gbc_jLabel4 = new GridBagConstraints();
		gbc_jLabel4.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabel4.anchor = GridBagConstraints.WEST;
		gbc_jLabel4.insets = new Insets(0, 0, 0, 5);
		gbc_jLabel4.gridx = 0;
		gbc_jLabel4.gridy = 9;
		add(jLabel4, gbc_jLabel4);
		jTextFieldAvgLambda = new javax.swing.JTextField();

		jTextFieldAvgLambda.setEditable(false);
		jTextFieldAvgLambda.setText("");
		GridBagConstraints gbc_jTextFieldAvgLambda = new GridBagConstraints();
		gbc_jTextFieldAvgLambda.insets = new Insets(0, 0, 0, 5);
		gbc_jTextFieldAvgLambda.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldAvgLambda.gridx = 1;
		gbc_jTextFieldAvgLambda.gridy = 9;
		add(jTextFieldAvgLambda, gbc_jTextFieldAvgLambda);
	}

	@Override
	public void initializeParameters() {
		removeListeners();

		int wayOfTransform = getController().getDataModel().getWayOfTransform();
		switch (wayOfTransform) {
		case 2:
			jTextFieldLogBase.setEditable(true);
			break;
		case 3:
			jRadioButtonZscoreTrans.setSelected(true);
			break;
		case 4:
			jRadioButtonMinMaxScaling.setSelected(true);
			break;
		case 5:
			jRadioButtonCoxBox.setSelected(true);
			break;

		default:
			jRadioButtonNoTransforamtion.setSelected(true);
			break;
		}
		jComboBox1LogMethod.setSelectedIndex(2);

		addListeners();
	}

	@Override
	public void removeListeners() {
		removeJRadioButtonListener(jRadioButtonNoTransforamtion);
		removeJRadioButtonListener(jRadioButtonLogTrans);
		removeJRadioButtonListener(jRadioButtonZscoreTrans);
		removeJRadioButtonListener(jRadioButtonMinMaxScaling);
		removeJRadioButtonListener(jRadioButtonCoxBox);

		ItemListener[] itemListeners = jComboBox1LogMethod.getItemListeners();
		for (ItemListener itemListener : itemListeners) {
			jComboBox1LogMethod.removeItemListener(itemListener);
		}
	}

	private void removeJRadioButtonListener(AbstractButton button) {
		ItemListener[] itemListeners = button.getItemListeners();
		for (ItemListener itemListener : itemListeners) {
			button.removeItemListener(itemListener);
		}

	}

	@Override
	public void addListeners() {

		jRadioButtonNoTransforamtion.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// No transformation
//				System.out.println(getClass()+"\tNo transformation");
				getController().transformData(1, 1, Math.E);

			}
		});

		jRadioButtonLogTrans.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				doLogTransformation();
				eheatmapMain.invokeTheFeatureMethod(5);
			}
		});

		jRadioButtonZscoreTrans.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// z score
//				System.out.println(getClass()+"\tz score");
				getController().transformData(3, 1, Math.E);
			}

		});

		jRadioButtonMinMaxScaling.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// min-max
//				System.out.println(getClass()+"\tmin-max");
				getController().transformData(4, 1, Math.E);
			}

		});

		jRadioButtonCoxBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// coxbox
//				System.out.println(getClass()+"\tcoxbox");
				getController().transformData(5, 1, Math.E);
				double avgOfLambda = getController().getDataModel().getAvgOfLambda();
				DecimalFormat df = new DecimalFormat("##.##");
				jTextFieldAvgLambda.setText(df.format(avgOfLambda) + "");
			}

		});

		jComboBox1LogMethod.addItemListener(e -> {
			if (jRadioButtonLogTrans.isSelected() && e.getStateChange() == ItemEvent.SELECTED) {
				doLogTransformation();
			}

		});

	}

	private void doLogTransformation() {
		DataModel dataModel = getController().getDataModel();
		int selectedIndex = jComboBox1LogMethod.getSelectedIndex();

		if (selectedIndex == 0) {
			if (dataModel.getOriMinValue() < 0) {
				SwingDialog.showErrorMSGDialog("Transformation err",
						"Sorry, the presented data matrix's minimum value less than 0,\n "
								+ "eGPS can't execute log(x+1) transformation!");
				return;
			}
		} else if (selectedIndex == 1) {
			if (dataModel.getOriMinValue() <= 0) {
				SwingDialog.showErrorMSGDialog("Transformation err",
						"Sorry, the presented data matrix's minimum value less than or equal to 0,\n "
								+ "eGPS can't execute log(x) transformation!");
				return;
			}
		}

		// log trans
//		System.out.println(getClass()+"\tlog trans");

		String text = jTextFieldLogBase.getText();
		double base = Math.E;
		if ("e".equalsIgnoreCase(text)) {
			// base = Math.E;
		} else {
			base = Double.parseDouble(text);
		}
		// in case of non number string
		getController().transformData(2, selectedIndex + 1, base);
	}
	
	@Override
	public String toString() {
		String selectedButtonText = getSelectedButtonText();
		return selectedButtonText;
	}
	 /**
     * 获取ButtonGroup中当前被选中按钮的文本。
     * @param group ButtonGroup实例
     * @return 被选中按钮的文本，如果没有选中的按钮则返回null
     */
    private String getSelectedButtonText() {
    	Enumeration<AbstractButton> elements = buttonGroup.getElements();
    	while (elements.hasMoreElements()) {
    		AbstractButton button = elements.nextElement();
    		if (button.isSelected()) {
                return button.getText();
            }
		}
        return null; // 如果没有按钮被选中，则返回null
    }
}
