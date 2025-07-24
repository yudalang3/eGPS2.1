package module.heatmap.eheatmap.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import egps2.panels.dialog.EGPSFontChooser;
import egps2.utils.common.model.datatransfer.CallBackBehavior;
import egps2.UnifiedAccessPoint;
import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.model.ParameterModel;
import egps2.panels.reusablecom.ParameterInitialized;

public class LeftDisplayPanel0320 extends JPanel implements ParameterInitialized {

	private static final long serialVersionUID = 5888946141906274755L;

	private EheatmapMain eheatmapMain;
	private HeatmapController controller;

	private JButton jButton1DataValueColor;
	private JButton jButton2DataValueFont;
	private JButton jButton3BorderColor;
	private JButton jButton4RowNameFont;
	private JButton jButton5RowNameColor;
	private JButton jButton6ColNameColor;
	private JButton jButton7ColNameFont;
	private javax.swing.JCheckBox jCheckBox1Border;
	private javax.swing.JCheckBox jCheckBox2Legend;
	private javax.swing.JCheckBox jCheckBox3RowNames;
	private javax.swing.JCheckBox jCheckBox4ColNames;
	private JComboBox<String> jComboBox1DataValues;
	private javax.swing.JSpinner jSpinnerLegendWidth;
	private javax.swing.JSpinner jSpinnerLegendHeight;
	private javax.swing.JSpinner jSpinnerRowNamesRotation;
	private javax.swing.JSpinner jSpinnerColNamesRotation;
	private javax.swing.JTextField jTextField1PartialBoundFactor;
	private javax.swing.JTextField jTextField4NumOfDecimal;
	// End of variables declaration

	private ChangeListener jSpinnerColNamesRotationClis;
	private ChangeListener jSpinnerRowNamesRotationClis;
	private ChangeListener jSpinnerLegendWidthClis;
	private ChangeListener jSpinnerLegendHeightClis;

	public LeftDisplayPanel0320(EheatmapMain main) {
		setBorder(new EmptyBorder(6, 10, 6, 10));
		this.eheatmapMain = main;

		initComponents();

		jSpinnerColNamesRotationClis = (e) -> {
			jCheckBox4ColNamesActionPerformed();
		};
		jSpinnerRowNamesRotationClis = (e) -> {
			jCheckBox3RowNamesActionPerformed();
		};

		jSpinnerLegendWidthClis = (e) -> {
			jCheckBox2LegendActionPerformed();
		};
		jSpinnerLegendHeightClis = (e) -> {
			jCheckBox2LegendActionPerformed();
		};
	}

	public HeatmapController getController() {
		if (controller == null) {
			controller = (HeatmapController) eheatmapMain.getController();
		}
		return controller;
	}

	private void initComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 57, 7, 34, 47, 44, 0 };
		gridBagLayout.rowHeights = new int[] { 17, 23, 21, 21, 25, 25, 25, 25, 25, 24, 24, 25, 24, 25, 21, 25, 24, 25,
				25, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		javax.swing.JLabel jLabel1 = new javax.swing.JLabel();

		jLabel1.setFont(defaultTitleFont); 
		jLabel1.setText("Data values ( Cell values ) :");
		GridBagConstraints gbc_jLabel1 = new GridBagConstraints();
		gbc_jLabel1.gridwidth = 5;
		gbc_jLabel1.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabel1.anchor = GridBagConstraints.WEST;
		gbc_jLabel1.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel1.gridx = 0;
		gbc_jLabel1.gridy = 0;
		add(jLabel1, gbc_jLabel1);
		javax.swing.JLabel jLabel2 = new javax.swing.JLabel();

		jLabel2.setFont(globalFont); 
		jLabel2.setText("Value status");
		GridBagConstraints gbc_jLabel2 = new GridBagConstraints();
		gbc_jLabel2.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabel2.anchor = GridBagConstraints.WEST;
		gbc_jLabel2.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel2.gridwidth = 3;
		gbc_jLabel2.gridx = 0;
		gbc_jLabel2.gridy = 1;
		add(jLabel2, gbc_jLabel2);
		jComboBox1DataValues = new JComboBox<>();

		jComboBox1DataValues.setFont(globalFont); 
		jComboBox1DataValues.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "No value", "Original", "Present", "Partial Ori.", "Partial Pre." }));
		GridBagConstraints gbc_jComboBox1DataValues = new GridBagConstraints();
		gbc_jComboBox1DataValues.anchor = GridBagConstraints.NORTHWEST;
		gbc_jComboBox1DataValues.insets = new Insets(0, 0, 5, 0);
		gbc_jComboBox1DataValues.gridwidth = 2;
		gbc_jComboBox1DataValues.gridx = 3;
		gbc_jComboBox1DataValues.gridy = 1;
		add(jComboBox1DataValues, gbc_jComboBox1DataValues);
		javax.swing.JLabel jLabel27 = new javax.swing.JLabel();

		jLabel27.setFont(globalFont); 
		jLabel27.setText("Number of decimals");
		jLabel27.setToolTipText(
				"minValue<= x <= minValue + factor and maxValue - factor <= x <= maxValue will be displayed");
		GridBagConstraints gbc_jLabel27 = new GridBagConstraints();
		gbc_jLabel27.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabel27.anchor = GridBagConstraints.WEST;
		gbc_jLabel27.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel27.gridwidth = 4;
		gbc_jLabel27.gridx = 0;
		gbc_jLabel27.gridy = 2;
		add(jLabel27, gbc_jLabel27);
		jTextField4NumOfDecimal = new javax.swing.JTextField();
		jTextField4NumOfDecimal.setFont(globalFont);
		jTextField4NumOfDecimal.setText("4");
		GridBagConstraints gbc_jTextField4NumOfDecimal = new GridBagConstraints();
		gbc_jTextField4NumOfDecimal.anchor = GridBagConstraints.NORTH;
		gbc_jTextField4NumOfDecimal.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextField4NumOfDecimal.insets = new Insets(0, 0, 5, 0);
		gbc_jTextField4NumOfDecimal.gridx = 4;
		gbc_jTextField4NumOfDecimal.gridy = 2;
		add(jTextField4NumOfDecimal, gbc_jTextField4NumOfDecimal);

		javax.swing.JLabel jLabel24 = new javax.swing.JLabel();

		jLabel24.setFont(globalFont); 
		jLabel24.setText("Partial bounds factor");
		jLabel24.setToolTipText(
				"minValue<= x <= minValue + factor and maxValue - factor <= x <= maxValue will be displayed");
		GridBagConstraints gbc_jLabel24 = new GridBagConstraints();
		gbc_jLabel24.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabel24.anchor = GridBagConstraints.WEST;
		gbc_jLabel24.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel24.gridwidth = 4;
		gbc_jLabel24.gridx = 0;
		gbc_jLabel24.gridy = 3;
		add(jLabel24, gbc_jLabel24);
		jTextField1PartialBoundFactor = new javax.swing.JTextField();
		jTextField1PartialBoundFactor.setFont(globalFont);

		jTextField1PartialBoundFactor.setText("1");
		GridBagConstraints gbc_jTextField1PartialBoundFactor = new GridBagConstraints();
		gbc_jTextField1PartialBoundFactor.anchor = GridBagConstraints.NORTH;
		gbc_jTextField1PartialBoundFactor.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextField1PartialBoundFactor.insets = new Insets(0, 0, 5, 0);
		gbc_jTextField1PartialBoundFactor.gridx = 4;
		gbc_jTextField1PartialBoundFactor.gridy = 3;
		add(jTextField1PartialBoundFactor, gbc_jTextField1PartialBoundFactor);
		javax.swing.JLabel jLabel3 = new javax.swing.JLabel();

		jLabel3.setFont(globalFont); 
		jLabel3.setText("Color");
		GridBagConstraints gbc_jLabel3 = new GridBagConstraints();
		gbc_jLabel3.gridwidth = 2;
		gbc_jLabel3.anchor = GridBagConstraints.EAST;
		gbc_jLabel3.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel3.gridx = 0;
		gbc_jLabel3.gridy = 4;
		add(jLabel3, gbc_jLabel3);
		jButton1DataValueColor = new JButton();

		jButton1DataValueColor.setFont(globalFont); 
		jButton1DataValueColor.setText("Col");
		jButton1DataValueColor.setToolTipText("Change corlor");
		GridBagConstraints gbc_jButton1DataValueColor = new GridBagConstraints();
		gbc_jButton1DataValueColor.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButton1DataValueColor.anchor = GridBagConstraints.NORTH;
		gbc_jButton1DataValueColor.insets = new Insets(0, 0, 5, 0);
		gbc_jButton1DataValueColor.gridwidth = 2;
		gbc_jButton1DataValueColor.gridx = 3;
		gbc_jButton1DataValueColor.gridy = 4;
		add(jButton1DataValueColor, gbc_jButton1DataValueColor);
		javax.swing.JLabel jLabel4 = new javax.swing.JLabel();

		jLabel4.setFont(globalFont); 
		jLabel4.setText("Font");
		GridBagConstraints gbc_jLabel4 = new GridBagConstraints();
		gbc_jLabel4.gridwidth = 2;
		gbc_jLabel4.anchor = GridBagConstraints.EAST;
		gbc_jLabel4.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel4.gridx = 0;
		gbc_jLabel4.gridy = 5;
		add(jLabel4, gbc_jLabel4);
		jButton2DataValueFont = new JButton();

		jButton2DataValueFont.setFont(globalFont); 
		jButton2DataValueFont.setText("font");
		jButton2DataValueFont.setToolTipText("Change Font");
		GridBagConstraints gbc_jButton2DataValueFont = new GridBagConstraints();
		gbc_jButton2DataValueFont.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButton2DataValueFont.anchor = GridBagConstraints.NORTH;
		gbc_jButton2DataValueFont.insets = new Insets(0, 0, 5, 0);
		gbc_jButton2DataValueFont.gridwidth = 2;
		gbc_jButton2DataValueFont.gridx = 3;
		gbc_jButton2DataValueFont.gridy = 5;
		add(jButton2DataValueFont, gbc_jButton2DataValueFont);
		jCheckBox1Border = new javax.swing.JCheckBox();

		jCheckBox1Border.setFont(globalFont); 
		jCheckBox1Border.setText("Border");
		GridBagConstraints gbc_jCheckBox1Border = new GridBagConstraints();
		gbc_jCheckBox1Border.fill = GridBagConstraints.HORIZONTAL;
		gbc_jCheckBox1Border.gridwidth = 2;
		gbc_jCheckBox1Border.anchor = GridBagConstraints.NORTHWEST;
		gbc_jCheckBox1Border.insets = new Insets(0, 0, 5, 5);
		gbc_jCheckBox1Border.gridx = 0;
		gbc_jCheckBox1Border.gridy = 6;
		add(jCheckBox1Border, gbc_jCheckBox1Border);
		javax.swing.JLabel jLabel5 = new javax.swing.JLabel();

		jLabel5.setFont(globalFont); 
		jLabel5.setText("Color");
		GridBagConstraints gbc_jLabel5 = new GridBagConstraints();
		gbc_jLabel5.gridwidth = 2;
		gbc_jLabel5.anchor = GridBagConstraints.EAST;
		gbc_jLabel5.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel5.gridx = 0;
		gbc_jLabel5.gridy = 7;
		add(jLabel5, gbc_jLabel5);
		jButton3BorderColor = new JButton();

		jButton3BorderColor.setFont(globalFont); 
		jButton3BorderColor.setText("Col");
		jButton3BorderColor.setToolTipText("Change corlor");
		GridBagConstraints gbc_jButton3BorderColor = new GridBagConstraints();
		gbc_jButton3BorderColor.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButton3BorderColor.anchor = GridBagConstraints.NORTH;
		gbc_jButton3BorderColor.insets = new Insets(0, 0, 5, 0);
		gbc_jButton3BorderColor.gridwidth = 2;
		gbc_jButton3BorderColor.gridx = 3;
		gbc_jButton3BorderColor.gridy = 7;
		add(jButton3BorderColor, gbc_jButton3BorderColor);
		jCheckBox2Legend = new javax.swing.JCheckBox();

		jCheckBox2Legend.setFont(globalFont); 
		jCheckBox2Legend.setText("Legend");
		GridBagConstraints gbc_jCheckBox2Legend = new GridBagConstraints();
		gbc_jCheckBox2Legend.fill = GridBagConstraints.HORIZONTAL;
		gbc_jCheckBox2Legend.gridwidth = 4;
		gbc_jCheckBox2Legend.anchor = GridBagConstraints.WEST;
		gbc_jCheckBox2Legend.insets = new Insets(0, 0, 5, 5);
		gbc_jCheckBox2Legend.gridx = 0;
		gbc_jCheckBox2Legend.gridy = 8;
		add(jCheckBox2Legend, gbc_jCheckBox2Legend);
		javax.swing.JLabel jLabel8 = new javax.swing.JLabel();

		jLabel8.setFont(globalFont); 
		jLabel8.setText("Width");
		GridBagConstraints gbc_jLabel8 = new GridBagConstraints();
		gbc_jLabel8.anchor = GridBagConstraints.EAST;
		gbc_jLabel8.gridwidth = 2;
		gbc_jLabel8.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel8.gridx = 0;
		gbc_jLabel8.gridy = 9;
		add(jLabel8, gbc_jLabel8);
		jSpinnerLegendWidth = new javax.swing.JSpinner();

		jSpinnerLegendWidth.setFont(globalFont); 
		jSpinnerLegendWidth.setValue(2);
		GridBagConstraints gbc_jSpinnerLegendWidth = new GridBagConstraints();
		gbc_jSpinnerLegendWidth.anchor = GridBagConstraints.NORTH;
		gbc_jSpinnerLegendWidth.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerLegendWidth.insets = new Insets(0, 0, 5, 0);
		gbc_jSpinnerLegendWidth.gridwidth = 2;
		gbc_jSpinnerLegendWidth.gridx = 3;
		gbc_jSpinnerLegendWidth.gridy = 9;
		add(jSpinnerLegendWidth, gbc_jSpinnerLegendWidth);
		javax.swing.JLabel jLabel9 = new javax.swing.JLabel();

		jLabel9.setFont(globalFont); 
		jLabel9.setText("Height");
		GridBagConstraints gbc_jLabel9 = new GridBagConstraints();
		gbc_jLabel9.gridwidth = 2;
		gbc_jLabel9.anchor = GridBagConstraints.EAST;
		gbc_jLabel9.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel9.gridx = 0;
		gbc_jLabel9.gridy = 10;
		add(jLabel9, gbc_jLabel9);
		jSpinnerLegendHeight = new javax.swing.JSpinner();

		jSpinnerLegendHeight.setFont(globalFont); 
		jSpinnerLegendHeight.setValue(3);
		GridBagConstraints gbc_jSpinnerLegendHeight = new GridBagConstraints();
		gbc_jSpinnerLegendHeight.anchor = GridBagConstraints.NORTH;
		gbc_jSpinnerLegendHeight.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerLegendHeight.insets = new Insets(0, 0, 5, 0);
		gbc_jSpinnerLegendHeight.gridwidth = 2;
		gbc_jSpinnerLegendHeight.gridx = 3;
		gbc_jSpinnerLegendHeight.gridy = 10;
		add(jSpinnerLegendHeight, gbc_jSpinnerLegendHeight);
		jCheckBox3RowNames = new javax.swing.JCheckBox();

		jCheckBox3RowNames.setFont(globalFont); 
		jCheckBox3RowNames.setText("Row names");
		GridBagConstraints gbc_jCheckBox3RowNames = new GridBagConstraints();
		gbc_jCheckBox3RowNames.fill = GridBagConstraints.HORIZONTAL;
		gbc_jCheckBox3RowNames.anchor = GridBagConstraints.WEST;
		gbc_jCheckBox3RowNames.insets = new Insets(0, 0, 5, 5);
		gbc_jCheckBox3RowNames.gridwidth = 3;
		gbc_jCheckBox3RowNames.gridx = 0;
		gbc_jCheckBox3RowNames.gridy = 11;
		add(jCheckBox3RowNames, gbc_jCheckBox3RowNames);
		javax.swing.JLabel jLabel10 = new javax.swing.JLabel();

		jLabel10.setFont(globalFont); 
		jLabel10.setText("Rotation");
		GridBagConstraints gbc_jLabel10 = new GridBagConstraints();
		gbc_jLabel10.gridwidth = 2;
		gbc_jLabel10.anchor = GridBagConstraints.EAST;
		gbc_jLabel10.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel10.gridx = 0;
		gbc_jLabel10.gridy = 12;
		add(jLabel10, gbc_jLabel10);
		jSpinnerRowNamesRotation = new javax.swing.JSpinner(new SpinnerNumberModel(2, -45, 45, 1));

		jSpinnerRowNamesRotation.setFont(globalFont); 
		jSpinnerRowNamesRotation.setValue(8);
		GridBagConstraints gbc_jSpinnerRowNamesRotation = new GridBagConstraints();
		gbc_jSpinnerRowNamesRotation.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerRowNamesRotation.anchor = GridBagConstraints.WEST;
		gbc_jSpinnerRowNamesRotation.insets = new Insets(0, 0, 5, 0);
		gbc_jSpinnerRowNamesRotation.gridwidth = 2;
		gbc_jSpinnerRowNamesRotation.gridx = 3;
		gbc_jSpinnerRowNamesRotation.gridy = 12;
		add(jSpinnerRowNamesRotation, gbc_jSpinnerRowNamesRotation);
		javax.swing.JLabel jLabel12 = new javax.swing.JLabel();

		jLabel12.setFont(globalFont); 
		jLabel12.setText("Font");
		GridBagConstraints gbc_jLabel12 = new GridBagConstraints();
		gbc_jLabel12.anchor = GridBagConstraints.EAST;
		gbc_jLabel12.gridwidth = 2;
		gbc_jLabel12.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel12.gridx = 0;
		gbc_jLabel12.gridy = 13;
		add(jLabel12, gbc_jLabel12);
		jButton4RowNameFont = new JButton();

		jButton4RowNameFont.setFont(globalFont); 
		jButton4RowNameFont.setText("font");
		jButton4RowNameFont.setToolTipText("Change Font");
		GridBagConstraints gbc_jButton4RowNameFont = new GridBagConstraints();
		gbc_jButton4RowNameFont.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButton4RowNameFont.anchor = GridBagConstraints.WEST;
		gbc_jButton4RowNameFont.insets = new Insets(0, 0, 5, 0);
		gbc_jButton4RowNameFont.gridwidth = 2;
		gbc_jButton4RowNameFont.gridx = 3;
		gbc_jButton4RowNameFont.gridy = 13;
		add(jButton4RowNameFont, gbc_jButton4RowNameFont);
		javax.swing.JLabel jLabel13 = new javax.swing.JLabel();

		jLabel13.setFont(globalFont); 
		jLabel13.setText("Color");
		GridBagConstraints gbc_jLabel13 = new GridBagConstraints();
		gbc_jLabel13.gridwidth = 2;
		gbc_jLabel13.anchor = GridBagConstraints.EAST;
		gbc_jLabel13.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel13.gridx = 0;
		gbc_jLabel13.gridy = 14;
		add(jLabel13, gbc_jLabel13);
		jButton5RowNameColor = new JButton();

		jButton5RowNameColor.setFont(globalFont); 
		jButton5RowNameColor.setText("Col");
		jButton5RowNameColor.setToolTipText("Change corlor");
		GridBagConstraints gbc_jButton5RowNameColor = new GridBagConstraints();
		gbc_jButton5RowNameColor.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButton5RowNameColor.anchor = GridBagConstraints.WEST;
		gbc_jButton5RowNameColor.insets = new Insets(0, 0, 5, 0);
		gbc_jButton5RowNameColor.gridheight = 2;
		gbc_jButton5RowNameColor.gridwidth = 2;
		gbc_jButton5RowNameColor.gridx = 3;
		gbc_jButton5RowNameColor.gridy = 14;
		add(jButton5RowNameColor, gbc_jButton5RowNameColor);
		jCheckBox4ColNames = new javax.swing.JCheckBox();

		jCheckBox4ColNames.setFont(globalFont); 
		jCheckBox4ColNames.setText("Col names");
		GridBagConstraints gbc_jCheckBox4ColNames = new GridBagConstraints();
		gbc_jCheckBox4ColNames.fill = GridBagConstraints.HORIZONTAL;
		gbc_jCheckBox4ColNames.anchor = GridBagConstraints.WEST;
		gbc_jCheckBox4ColNames.insets = new Insets(0, 0, 5, 5);
		gbc_jCheckBox4ColNames.gridwidth = 3;
		gbc_jCheckBox4ColNames.gridx = 0;
		gbc_jCheckBox4ColNames.gridy = 15;
		add(jCheckBox4ColNames, gbc_jCheckBox4ColNames);
		javax.swing.JLabel jLabel14 = new javax.swing.JLabel();

		jLabel14.setFont(globalFont); 
		jLabel14.setText("Rotation");
		GridBagConstraints gbc_jLabel14 = new GridBagConstraints();
		gbc_jLabel14.gridwidth = 2;
		gbc_jLabel14.anchor = GridBagConstraints.EAST;
		gbc_jLabel14.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel14.gridx = 0;
		gbc_jLabel14.gridy = 16;
		add(jLabel14, gbc_jLabel14);
		jSpinnerColNamesRotation = new javax.swing.JSpinner(new SpinnerNumberModel(2, -45, 90, 1));

		jSpinnerColNamesRotation.setFont(globalFont); 
		jSpinnerColNamesRotation.setValue(9);
		GridBagConstraints gbc_jSpinnerColNamesRotation = new GridBagConstraints();
		gbc_jSpinnerColNamesRotation.anchor = GridBagConstraints.WEST;
		gbc_jSpinnerColNamesRotation.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerColNamesRotation.insets = new Insets(0, 0, 5, 0);
		gbc_jSpinnerColNamesRotation.gridwidth = 2;
		gbc_jSpinnerColNamesRotation.gridx = 3;
		gbc_jSpinnerColNamesRotation.gridy = 16;
		add(jSpinnerColNamesRotation, gbc_jSpinnerColNamesRotation);
		javax.swing.JLabel jLabel15 = new javax.swing.JLabel();

		jLabel15.setFont(globalFont); 
		jLabel15.setText("Font");
		GridBagConstraints gbc_jLabel15 = new GridBagConstraints();
		gbc_jLabel15.gridwidth = 2;
		gbc_jLabel15.anchor = GridBagConstraints.EAST;
		gbc_jLabel15.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel15.gridx = 0;
		gbc_jLabel15.gridy = 17;
		add(jLabel15, gbc_jLabel15);
		jButton7ColNameFont = new JButton();

		jButton7ColNameFont.setFont(globalFont); 
		jButton7ColNameFont.setText("font");
		jButton7ColNameFont.setToolTipText("Change Font");
		GridBagConstraints gbc_jButton7ColNameFont = new GridBagConstraints();
		gbc_jButton7ColNameFont.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButton7ColNameFont.anchor = GridBagConstraints.WEST;
		gbc_jButton7ColNameFont.insets = new Insets(0, 0, 5, 0);
		gbc_jButton7ColNameFont.gridwidth = 2;
		gbc_jButton7ColNameFont.gridx = 3;
		gbc_jButton7ColNameFont.gridy = 17;
		add(jButton7ColNameFont, gbc_jButton7ColNameFont);
		javax.swing.JLabel jLabel16 = new javax.swing.JLabel();

		jLabel16.setFont(globalFont); 
		jLabel16.setText("Color");
		GridBagConstraints gbc_jLabel16 = new GridBagConstraints();
		gbc_jLabel16.anchor = GridBagConstraints.EAST;
		gbc_jLabel16.gridwidth = 2;
		gbc_jLabel16.insets = new Insets(0, 0, 0, 5);
		gbc_jLabel16.gridx = 0;
		gbc_jLabel16.gridy = 18;
		add(jLabel16, gbc_jLabel16);
		jButton6ColNameColor = new JButton();

		jButton6ColNameColor.setFont(globalFont); 
		jButton6ColNameColor.setText("Col");
		jButton6ColNameColor.setToolTipText("Change corlor");
		GridBagConstraints gbc_jButton6ColNameColor = new GridBagConstraints();
		gbc_jButton6ColNameColor.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButton6ColNameColor.anchor = GridBagConstraints.WEST;
		gbc_jButton6ColNameColor.gridwidth = 2;
		gbc_jButton6ColNameColor.gridx = 3;
		gbc_jButton6ColNameColor.gridy = 18;
		add(jButton6ColNameColor, gbc_jButton6ColNameColor);

	}

	private void jButton1DataValueColorActionPerformed(java.awt.event.ActionEvent evt) {
		SwingUtilities.invokeLater(() -> {
			EHeatmapColorChooserDialog dialog = new EHeatmapColorChooserDialog(jButton1DataValueColor);
			dialog.setVisible(true);
			dialog.setCallBackEvent(() -> {
				jComboBox1DataValuesActionPerformed();
			});
		});
	}

	private void jButton2DataValueFontActionPerformed(java.awt.event.ActionEvent evt) {
		fontChanged(jButton2DataValueFont, () -> {
			jComboBox1DataValuesActionPerformed();
		});
	}

	private void fontChanged(JButton button, CallBackBehavior callBackBehevior) {
		EGPSFontChooser fontChooser = new EGPSFontChooser();
		int result = fontChooser.showDialog(getController().getViewPanel());
		if (result == EGPSFontChooser.OK_OPTION) {
			Font font = fontChooser.getSelectedFont();
			button.setFont(font);
			callBackBehevior.doAfterCorrectClick();
		}
	}

	private void jCheckBox1BorderActionPerformed() {
		boolean selected = jCheckBox1Border.isSelected();
		ParameterModel paraModel = getController().getParaModel();
		paraModel.setIfShowBorder(selected);
		paraModel.setBorderColor(jButton3BorderColor.getBackground());
		jButton3BorderColor.setEnabled(selected);
		getController().weakestRefreshHeatmap();
	}

	private void jCheckBox2LegendActionPerformed() {
		boolean selected = jCheckBox2Legend.isSelected();

		ParameterModel paraModel = getController().getParaModel();
		paraModel.setIfPaintMapLegend(selected);
		paraModel.setMapLegnedWidth((int) jSpinnerLegendWidth.getValue());
		paraModel.setMapLegendHeight((int) jSpinnerLegendHeight.getValue());
		jSpinnerLegendWidth.setEnabled(selected);
		jSpinnerLegendHeight.setEnabled(selected);

//		getController().strongestRefreshHeatmapAndRecoverDim();
		getController().weakestRefreshHeatmap();
	}

	private void jCheckBox3RowNamesActionPerformed() {
		boolean selected = jCheckBox3RowNames.isSelected();
		ParameterModel paraModel = getController().getParaModel();
		paraModel.setIfPaintRowNames(selected);
		paraModel.setRowNameColor(jButton5RowNameColor.getBackground());
		paraModel.setRowNameFont(jButton4RowNameFont.getFont());
		paraModel.setRowNamesRotaionAngle((int) jSpinnerRowNamesRotation.getValue());
		jButton4RowNameFont.setEnabled(selected);
		jButton5RowNameColor.setEnabled(selected);
		jSpinnerRowNamesRotation.setEnabled(selected);

//		getController().strongestRefreshHeatmapAndRecoverDim();
		getController().weakestRefreshHeatmap();
	}

	private void jCheckBox4ColNamesActionPerformed() {
		boolean selected = jCheckBox4ColNames.isSelected();
		ParameterModel paraModel = getController().getParaModel();
		paraModel.setIfPaintColNames(selected);
		paraModel.setColNameColor(jButton6ColNameColor.getBackground());
		paraModel.setColNameFont(jButton7ColNameFont.getFont());
		paraModel.setColNamesRotaionAngle((int) jSpinnerColNamesRotation.getValue());
		jButton6ColNameColor.setEnabled(selected);
		jButton7ColNameFont.setEnabled(selected);
		jSpinnerColNamesRotation.setEnabled(selected);

		getController().strongestRefreshHeatmapAndRecoverDim();
	}

	/**
	 * "No value", "Original", "Present", "Partial Ori.", "Partial Pre."
	 * 
	 * @param evt
	 */
	private void jComboBox1DataValuesActionPerformed() {
		ParameterModel paraModel = getController().getParaModel();
		int selectedIndex = jComboBox1DataValues.getSelectedIndex();
		paraModel.setDataValueStatus(selectedIndex);
		paraModel.setDataValuePartialFactor(Double.parseDouble(jTextField1PartialBoundFactor.getText()));
		paraModel.setDataValueFont(jButton2DataValueFont.getFont());
		paraModel.setDataValueColor(jButton1DataValueColor.getBackground());
		int parseInt = Integer.parseInt(jTextField4NumOfDecimal.getText());
		paraModel.setNumOfDecimalFormatOfDataaValue(parseInt);

		if (selectedIndex == 0) {
			jButton2DataValueFont.setEnabled(false);
			jButton1DataValueColor.setEnabled(false);
		} else {
			jButton2DataValueFont.setEnabled(true);
			jButton1DataValueColor.setEnabled(true);
		}
		getController().weakestRefreshHeatmap();
		
		eheatmapMain.invokeTheFeatureMethod(8);
	}

	private void jButton3BorderColorActionPerformed(java.awt.event.ActionEvent evt) {
		SwingUtilities.invokeLater(() -> {
			EHeatmapColorChooserDialog dialog = new EHeatmapColorChooserDialog(jButton3BorderColor);
			dialog.setCallBackEvent(() -> {
				jCheckBox1BorderActionPerformed();
			});
			dialog.setVisible(true);
		});
	}

	@Override
	public void initializeParameters() {
		removeListeners();

		ParameterModel paraModel = getController().getParaModel();

		boolean ifShowBorder = paraModel.isIfShowBorder();
		jCheckBox1Border.setSelected(ifShowBorder);
		jButton3BorderColor.setEnabled(ifShowBorder);

		boolean ifPaintMapLegend = paraModel.isIfPaintMapLegend();
		jCheckBox2Legend.setSelected(ifPaintMapLegend);
		jSpinnerLegendWidth.setEnabled(ifPaintMapLegend);
		jSpinnerLegendHeight.setEnabled(ifPaintMapLegend);

		boolean ifPaintRowNames = paraModel.isIfPaintRowNames();
		jCheckBox3RowNames.setSelected(ifPaintRowNames);
		jButton4RowNameFont.setEnabled(ifPaintRowNames);
		jButton5RowNameColor.setEnabled(ifPaintRowNames);
		jSpinnerRowNamesRotation.setEnabled(ifPaintRowNames);

		boolean ifPaintColNames = paraModel.isIfPaintColNames();
		jCheckBox4ColNames.setSelected(ifPaintColNames);
		jButton6ColNameColor.setEnabled(ifPaintColNames);
		jButton7ColNameFont.setEnabled(ifPaintColNames);
		jSpinnerColNamesRotation.setEnabled(ifPaintColNames);

		// For data values
		Color dataValueColor = paraModel.getDataValueColor();
		jButton1DataValueColor.setBackground(dataValueColor);
		jButton1DataValueColor.setForeground(dataValueColor);
		int nn = paraModel.getNumOfDecimalFormatOfDataaValue();
		jTextField4NumOfDecimal.setText(nn + "");
		String factor = paraModel.getDataValuePartialFactor() + "";
		jTextField1PartialBoundFactor.setText(factor.substring(0, 3));
		Font dataValueFont = paraModel.getDataValueFont();
		jButton2DataValueFont.setFont(dataValueFont);

		int selectedIndex = paraModel.getDataValueStatus();
		jComboBox1DataValues.setSelectedIndex(selectedIndex);
		if (selectedIndex == 0) {
			jButton2DataValueFont.setEnabled(false);
			jButton1DataValueColor.setEnabled(false);
		}

		// Border
		Color borderColor = paraModel.getBorderColor();
		jButton3BorderColor.setBackground(borderColor);
		jButton3BorderColor.setForeground(borderColor);

		jSpinnerLegendWidth.setValue(paraModel.getMapLegnedWidth());
		jSpinnerLegendHeight.setValue(paraModel.getMapLegendHeight());

		// row names
		Color rowNameColor = paraModel.getRowNameColor();
		jButton5RowNameColor.setBackground(rowNameColor);
		jButton5RowNameColor.setForeground(rowNameColor);
		jSpinnerRowNamesRotation.setValue(paraModel.getRowNamesRotaionAngle());
		jButton4RowNameFont.setFont(paraModel.getRowNameFont());

		// col names
		Color colNameColor = paraModel.getColNameColor();
		jButton6ColNameColor.setBackground(colNameColor);
		jButton6ColNameColor.setForeground(colNameColor);
		jSpinnerColNamesRotation.setValue(paraModel.getColNamesRotaionAngle());
		jButton7ColNameFont.setFont(paraModel.getColNameFont());


		addListeners();
	}

	@Override
	public void removeListeners() {
		removeActionListenerForJCombox(jComboBox1DataValues);
		removeActionListenerForAbsButton(jButton1DataValueColor);
		removeActionListenerForAbsButton(jButton2DataValueFont);
		removeActionListenerForAbsButton(jButton3BorderColor);
		removeActionListenerForAbsButton(jCheckBox1Border);
		removeActionListenerForAbsButton(jCheckBox2Legend);

		jSpinnerLegendWidth.removeChangeListener(jSpinnerLegendWidthClis);
		jSpinnerLegendHeight.removeChangeListener(jSpinnerLegendHeightClis);
		jSpinnerRowNamesRotation.removeChangeListener(jSpinnerRowNamesRotationClis);
		jSpinnerColNamesRotation.removeChangeListener(jSpinnerColNamesRotationClis);

		removeActionListenerForAbsButton(jCheckBox3RowNames);
		removeActionListenerForAbsButton(jCheckBox4ColNames);
		removeActionListenerForAbsButton(jButton4RowNameFont);
		removeActionListenerForAbsButton(jButton4RowNameFont);
		removeActionListenerForAbsButton(jButton5RowNameColor);
		removeActionListenerForAbsButton(jButton6ColNameColor);
		removeActionListenerForAbsButton(jButton7ColNameFont);
	}

	private void removeActionListenerForJCombox(JComboBox<String> jComboBox) {
		ActionListener[] actionListeners = jComboBox.getActionListeners();
		for (ActionListener actionListener : actionListeners) {
			jComboBox.removeActionListener(actionListener);
		}

	}

	private void removeActionListenerForAbsButton(AbstractButton abstractButton) {
		ActionListener[] actionListeners = abstractButton.getActionListeners();
		for (ActionListener actionListener : actionListeners) {
			abstractButton.removeActionListener(actionListener);
		}
	}

	@Override
	public void addListeners() {
		jComboBox1DataValues.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jComboBox1DataValuesActionPerformed();
			}
		});

		jButton1DataValueColor.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1DataValueColorActionPerformed(evt);
			}
		});

		jButton2DataValueFont.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2DataValueFontActionPerformed(evt);
			}
		});

		jButton3BorderColor.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3BorderColorActionPerformed(evt);
			}
		});
		jCheckBox1Border.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBox1BorderActionPerformed();
			}
		});

		jCheckBox2Legend.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBox2LegendActionPerformed();
			}
		});

		jCheckBox3RowNames.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBox3RowNamesActionPerformed();
			}
		});
		jButton4RowNameFont.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fontChanged(jButton4RowNameFont, () -> {
					jCheckBox3RowNamesActionPerformed();
				});
			}
		});
		jButton5RowNameColor.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SwingUtilities.invokeLater(() -> {
					EHeatmapColorChooserDialog dialog = new EHeatmapColorChooserDialog(jButton5RowNameColor);
					dialog.setCallBackEvent(() -> {
						jCheckBox3RowNamesActionPerformed();
					});
					dialog.setVisible(true);
				});
			}
		});

		jCheckBox4ColNames.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBox4ColNamesActionPerformed();
			}
		});
		jButton6ColNameColor.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SwingUtilities.invokeLater(() -> {
					EHeatmapColorChooserDialog dialog = new EHeatmapColorChooserDialog(jButton6ColNameColor);
					dialog.setCallBackEvent(() -> {
						jCheckBox4ColNamesActionPerformed();
					});
					dialog.setVisible(true);
				});
			}
		});
		jButton7ColNameFont.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fontChanged(jButton7ColNameFont, () -> {
					jCheckBox4ColNamesActionPerformed();
				});
			}
		});

		jSpinnerLegendWidth.addChangeListener(jSpinnerLegendWidthClis);
		jSpinnerLegendHeight.addChangeListener(jSpinnerLegendHeightClis);
		jSpinnerRowNamesRotation.addChangeListener(jSpinnerRowNamesRotationClis);
		jSpinnerColNamesRotation.addChangeListener(jSpinnerColNamesRotationClis);

	}

}