package module.heatmap.eheatmap.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import egps2.UnifiedAccessPoint;
import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.AbstrctEHeatmapPaintPanel;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.enums.PaintingLayout;
import module.heatmap.eheatmap.model.CircularParameters;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import egps2.panels.reusablecom.ParameterInitialized;

public class LeftLayoutPanel0310 extends JPanel implements ParameterInitialized {
	private EheatmapMain eheatmapMain;
	private HeatmapController controller;
	
	private JSpinner jSpinnerCellHeight;
	private JSpinner jSpinnerCellWidth;
	private JSpinner jSpinnerGapSize;
	private JSpinner jSpinnerColClusterAllo;
	private JSpinner jSpinnerRowClusterAllo;
	private ChangeListener jSpinnerCellHeightChangeListener;
	private ChangeListener jSpinnerCellWidthChangeListener;
	private ChangeListener jSpinnerColClusterAlloChangeListener;
	private ChangeListener jSpinnerGapSizeChangeListener;
	private ChangeListener jSpinnerRowClusterAlloChangeListener;

	private JRadioButton jRadioButtonCircular;
	private JRadioButton jRadioButtonRect;
	private JCheckBox chckbx_animation;
	
	private JSpinner spinnerStartDegree;
	private JSpinner spinnerExtendDegree;
	private JSpinner spinnerInnerRadius;
	private JSpinner spinnerRingThickness;
	private ChangeListener circularLayoutChangeListener;
	private JSpinner spinnerRowAnnoRadius;
	private JSpinner spinnerRowAnnThickness;
	private JSpinner spinnerTotalDegree;
	private JSpinner spinnerColAnnoThickness;

	public LeftLayoutPanel0310(EheatmapMain main) {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		this.eheatmapMain = main;

		initComponents();
		setSize(279,750);
		
		circularLayoutChangeListener = (e) -> {
			adjustCircularLayout();
		};
		jSpinnerGapSizeChangeListener = (e) -> {
			ParameterModel paraModel = getController().getParaModel();
			if (paraModel.gethGapLocations().length == 0 && paraModel.getvGapLocations().length == 0) {
				return;
			}

			paraModel.setGapSize((int) jSpinnerGapSize.getValue());
			getController().weakestRefreshHeatmapForGaps();
		};
		jSpinnerCellHeightChangeListener = (e) -> {
			AbstrctEHeatmapPaintPanel paintJPanel = getController().getPaintJPanel();
			PaintingLocationParas locationParas = paintJPanel.getLocationParas();
			locationParas.setCellHeight((int) jSpinnerCellHeight.getValue());

			getController().getParaModel().setShouldCalculateRowTreeLocation(true);
			getController().weakestRefreshHeatmap();
		};
		jSpinnerCellWidthChangeListener = (e) -> {
			AbstrctEHeatmapPaintPanel paintJPanel = getController().getPaintJPanel();
			PaintingLocationParas locationParas = paintJPanel.getLocationParas();

			locationParas.setCellWidth((int) jSpinnerCellWidth.getValue());

			getController().getParaModel().setShouldCalculateColTreeLocation(true);
			getController().weakestRefreshHeatmap();
		};
	}

	public HeatmapController getController() {
		if (controller == null) {
			controller = (HeatmapController) eheatmapMain.getController();
		}
		return controller;
	}

	private void initComponents() {

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		// 定义按钮组
		ButtonGroup group = new ButtonGroup();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 82, 16, 51, 12, 0 };
		gridBagLayout.rowHeights = new int[] { 27, 19, 26, 19, 26, 30, 27, 26, 23, 26, 26, 26, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);
		jRadioButtonRect = new JRadioButton();

		jRadioButtonRect.setFont(globalFont);
		jRadioButtonRect.setSelected(true);
		jRadioButtonRect.setText("Rectangular");

		group.add(jRadioButtonRect);
		GridBagConstraints gbc_jRadioButtonRect = new GridBagConstraints();
		gbc_jRadioButtonRect.anchor = GridBagConstraints.NORTHWEST;
		gbc_jRadioButtonRect.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonRect.gridwidth = 2;
		gbc_jRadioButtonRect.gridx = 0;
		gbc_jRadioButtonRect.gridy = 0;
		add(jRadioButtonRect, gbc_jRadioButtonRect);
		JLabel jLabelRowAllo = new JLabel();

		jLabelRowAllo.setFont(globalFont);
		jLabelRowAllo.setText("Row cluster allocation");
		GridBagConstraints gbc_jLabelRowAllo = new GridBagConstraints();
		gbc_jLabelRowAllo.anchor = GridBagConstraints.WEST;
		gbc_jLabelRowAllo.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelRowAllo.gridwidth = 3;
		gbc_jLabelRowAllo.gridx = 0;
		gbc_jLabelRowAllo.gridy = 1;
		add(jLabelRowAllo, gbc_jLabelRowAllo);

		jSpinnerRowClusterAllo = new JSpinner();
		jSpinnerRowClusterAllo.setToolTipText("Directly drag in the painting panel to adjust!");

		jSpinnerRowClusterAllo.setFont(globalFont);
		GridBagConstraints gbc_jSpinnerRowClusterAllo = new GridBagConstraints();
		gbc_jSpinnerRowClusterAllo.anchor = GridBagConstraints.NORTH;
		gbc_jSpinnerRowClusterAllo.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerRowClusterAllo.insets = new Insets(0, 0, 5, 5);
		gbc_jSpinnerRowClusterAllo.gridx = 2;
		gbc_jSpinnerRowClusterAllo.gridy = 2;
		add(jSpinnerRowClusterAllo, gbc_jSpinnerRowClusterAllo);

		JLabel jLabel8 = new JLabel();

		jLabel8.setFont(globalFont);
		jLabel8.setText("%");
		GridBagConstraints gbc_jLabel8 = new GridBagConstraints();
		gbc_jLabel8.anchor = GridBagConstraints.WEST;
		gbc_jLabel8.insets = new Insets(0, 0, 5, 0);
		gbc_jLabel8.gridx = 3;
		gbc_jLabel8.gridy = 2;
		add(jLabel8, gbc_jLabel8);
		JLabel jLabelColAllo = new JLabel();

		jLabelColAllo.setFont(globalFont);
		jLabelColAllo.setText("Col cluster allocation");
		GridBagConstraints gbc_jLabelColAllo = new GridBagConstraints();
		gbc_jLabelColAllo.anchor = GridBagConstraints.WEST;
		gbc_jLabelColAllo.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelColAllo.gridwidth = 3;
		gbc_jLabelColAllo.gridx = 0;
		gbc_jLabelColAllo.gridy = 3;
		add(jLabelColAllo, gbc_jLabelColAllo);
		jSpinnerColClusterAllo = new JSpinner();
		jSpinnerColClusterAllo.setToolTipText("Directly drag in the painting panel to adjust!");

		jSpinnerColClusterAllo.setFont(globalFont);
		GridBagConstraints gbc_jSpinnerColClusterAllo = new GridBagConstraints();
		gbc_jSpinnerColClusterAllo.anchor = GridBagConstraints.NORTH;
		gbc_jSpinnerColClusterAllo.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerColClusterAllo.insets = new Insets(0, 0, 5, 5);
		gbc_jSpinnerColClusterAllo.gridx = 2;
		gbc_jSpinnerColClusterAllo.gridy = 4;
		add(jSpinnerColClusterAllo, gbc_jSpinnerColClusterAllo);
		JLabel jLabel9 = new JLabel();

		jLabel9.setFont(globalFont);
		jLabel9.setText("%");
		GridBagConstraints gbc_jLabel9 = new GridBagConstraints();
		gbc_jLabel9.anchor = GridBagConstraints.WEST;
		gbc_jLabel9.insets = new Insets(0, 0, 5, 0);
		gbc_jLabel9.gridx = 3;
		gbc_jLabel9.gridy = 4;
		add(jLabel9, gbc_jLabel9);
		JLabel jLabelGapSize = new JLabel();

		jLabelGapSize.setFont(globalFont);
		jLabelGapSize.setText("Gap size");
		GridBagConstraints gbc_jLabelGapSize = new GridBagConstraints();
		gbc_jLabelGapSize.anchor = GridBagConstraints.WEST;
		gbc_jLabelGapSize.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelGapSize.gridx = 0;
		gbc_jLabelGapSize.gridy = 5;
		add(jLabelGapSize, gbc_jLabelGapSize);

		jSpinnerGapSize = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
		jSpinnerGapSize.setFont(globalFont);
		GridBagConstraints gbc_jSpinnerGapSize = new GridBagConstraints();
		gbc_jSpinnerGapSize.anchor = GridBagConstraints.SOUTH;
		gbc_jSpinnerGapSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerGapSize.insets = new Insets(0, 0, 5, 5);
		gbc_jSpinnerGapSize.gridx = 2;
		gbc_jSpinnerGapSize.gridy = 5;
		add(jSpinnerGapSize, gbc_jSpinnerGapSize);
		JLabel jLabelCellWidth = new JLabel();

		jLabelCellWidth.setFont(globalFont);
		jLabelCellWidth.setText("Cell width");
		GridBagConstraints gbc_jLabelCellWidth = new GridBagConstraints();
		gbc_jLabelCellWidth.anchor = GridBagConstraints.WEST;
		gbc_jLabelCellWidth.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelCellWidth.gridx = 0;
		gbc_jLabelCellWidth.gridy = 6;
		add(jLabelCellWidth, gbc_jLabelCellWidth);

		jSpinnerCellWidth = new JSpinner(new SpinnerNumberModel(2, 1, 100, 1));
		jSpinnerCellWidth.setFont(globalFont);
		GridBagConstraints gbc_jSpinnerCellWidth = new GridBagConstraints();
		gbc_jSpinnerCellWidth.anchor = GridBagConstraints.SOUTH;
		gbc_jSpinnerCellWidth.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerCellWidth.insets = new Insets(0, 0, 5, 5);
		gbc_jSpinnerCellWidth.gridx = 2;
		gbc_jSpinnerCellWidth.gridy = 6;
		add(jSpinnerCellWidth, gbc_jSpinnerCellWidth);
		JLabel jLabelCellHeight = new JLabel();

		jLabelCellHeight.setFont(globalFont);
		jLabelCellHeight.setText("Cell height");
		GridBagConstraints gbc_jLabelCellHeight = new GridBagConstraints();
		gbc_jLabelCellHeight.anchor = GridBagConstraints.WEST;
		gbc_jLabelCellHeight.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelCellHeight.gridx = 0;
		gbc_jLabelCellHeight.gridy = 7;
		add(jLabelCellHeight, gbc_jLabelCellHeight);

		jSpinnerCellHeight = new JSpinner(new SpinnerNumberModel(2, 1, 100, 1));
		jSpinnerCellHeight.setFont(globalFont);
		GridBagConstraints gbc_jSpinnerCellHeight = new GridBagConstraints();
		gbc_jSpinnerCellHeight.anchor = GridBagConstraints.NORTH;
		gbc_jSpinnerCellHeight.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSpinnerCellHeight.insets = new Insets(0, 0, 5, 5);
		gbc_jSpinnerCellHeight.gridx = 2;
		gbc_jSpinnerCellHeight.gridy = 7;
		add(jSpinnerCellHeight, gbc_jSpinnerCellHeight);

		jRadioButtonCircular = new JRadioButton("Circular");
		jRadioButtonCircular.setFont(globalFont);
		group.add(jRadioButtonCircular);
		GridBagConstraints gbc_jRadioButtonCircular = new GridBagConstraints();
		gbc_jRadioButtonCircular.anchor = GridBagConstraints.NORTHWEST;
		gbc_jRadioButtonCircular.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonCircular.gridx = 0;
		gbc_jRadioButtonCircular.gridy = 8;
		add(jRadioButtonCircular, gbc_jRadioButtonCircular);

		chckbx_animation = new JCheckBox("Animation");
		chckbx_animation.setFont(globalFont);
		GridBagConstraints gbc_chckbx_animation = new GridBagConstraints();
		gbc_chckbx_animation.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbx_animation.insets = new Insets(0, 0, 5, 5);
		gbc_chckbx_animation.gridwidth = 2;
		gbc_chckbx_animation.gridx = 1;
		gbc_chckbx_animation.gridy = 8;
		add(chckbx_animation, gbc_chckbx_animation);

		JLabel lblStartDegree = new JLabel("Start degree");
		lblStartDegree.setFont(globalFont);
		GridBagConstraints gbc_lblStartDegree = new GridBagConstraints();
		gbc_lblStartDegree.anchor = GridBagConstraints.WEST;
		gbc_lblStartDegree.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartDegree.gridwidth = 2;
		gbc_lblStartDegree.gridx = 0;
		gbc_lblStartDegree.gridy = 9;
		add(lblStartDegree, gbc_lblStartDegree);

		spinnerStartDegree = new JSpinner(new SpinnerNumberModel(2, 0, 360, 1));
		spinnerStartDegree.setFont(globalFont);
		;
		GridBagConstraints gbc_spinnerStartDegree = new GridBagConstraints();
		gbc_spinnerStartDegree.anchor = GridBagConstraints.NORTH;
		gbc_spinnerStartDegree.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerStartDegree.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerStartDegree.gridx = 2;
		gbc_spinnerStartDegree.gridy = 9;
		add(spinnerStartDegree, gbc_spinnerStartDegree);

		JLabel label = new JLabel();
		label.setText("'");
		label.setFont(globalFont);
		;
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 3;
		gbc_label.gridy = 9;
		add(label, gbc_label);

		JLabel lblExtendDegree = new JLabel("Extend degree");
		lblExtendDegree.setFont(globalFont);
		GridBagConstraints gbc_lblExtendDegree = new GridBagConstraints();
		gbc_lblExtendDegree.anchor = GridBagConstraints.WEST;
		gbc_lblExtendDegree.insets = new Insets(0, 0, 5, 5);
		gbc_lblExtendDegree.gridwidth = 2;
		gbc_lblExtendDegree.gridx = 0;
		gbc_lblExtendDegree.gridy = 10;
		add(lblExtendDegree, gbc_lblExtendDegree);

		spinnerExtendDegree = new JSpinner(new SpinnerNumberModel(2, 0, 360, 1));
		spinnerExtendDegree.setFont(globalFont);
		GridBagConstraints gbc_spinnerExtendDegree = new GridBagConstraints();
		gbc_spinnerExtendDegree.anchor = GridBagConstraints.NORTH;
		gbc_spinnerExtendDegree.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerExtendDegree.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerExtendDegree.gridx = 2;
		gbc_spinnerExtendDegree.gridy = 10;
		add(spinnerExtendDegree, gbc_spinnerExtendDegree);

		JLabel label_1 = new JLabel();
		label_1.setText("'");
		label_1.setFont(globalFont);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 3;
		gbc_label_1.gridy = 10;
		add(label_1, gbc_label_1);

		JLabel lblInnerRadius = new JLabel("Inner radius");
		lblInnerRadius.setFont(globalFont);
		GridBagConstraints gbc_lblInnerRadius = new GridBagConstraints();
		gbc_lblInnerRadius.anchor = GridBagConstraints.WEST;
		gbc_lblInnerRadius.insets = new Insets(0, 0, 5, 5);
		gbc_lblInnerRadius.gridwidth = 2;
		gbc_lblInnerRadius.gridx = 0;
		gbc_lblInnerRadius.gridy = 11;
		add(lblInnerRadius, gbc_lblInnerRadius);

		spinnerInnerRadius = new JSpinner(new SpinnerNumberModel(2, 0, 500, 1));
		spinnerInnerRadius.setFont(globalFont);
		GridBagConstraints gbc_spinnerInnerRadius = new GridBagConstraints();
		gbc_spinnerInnerRadius.anchor = GridBagConstraints.NORTH;
		gbc_spinnerInnerRadius.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerInnerRadius.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerInnerRadius.gridx = 2;
		gbc_spinnerInnerRadius.gridy = 11;
		add(spinnerInnerRadius, gbc_spinnerInnerRadius);

		JLabel lblRingThickness = new JLabel("Ring thickness");
		lblRingThickness.setFont(globalFont);
		GridBagConstraints gbc_lblRingThickness = new GridBagConstraints();
		gbc_lblRingThickness.anchor = GridBagConstraints.WEST;
		gbc_lblRingThickness.insets = new Insets(0, 0, 5, 5);
		gbc_lblRingThickness.gridwidth = 2;
		gbc_lblRingThickness.gridx = 0;
		gbc_lblRingThickness.gridy = 12;
		add(lblRingThickness, gbc_lblRingThickness);

		spinnerRingThickness = new JSpinner(new SpinnerNumberModel(2, 0, 360, 1));
		spinnerRingThickness.setFont(globalFont);
		GridBagConstraints gbc_spinnerRingThickness = new GridBagConstraints();
		gbc_spinnerRingThickness.anchor = GridBagConstraints.NORTH;
		gbc_spinnerRingThickness.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerRingThickness.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerRingThickness.gridx = 2;
		gbc_spinnerRingThickness.gridy = 12;
		add(spinnerRingThickness, gbc_spinnerRingThickness);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 3;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 13;
		add(separator, gbc_separator);
		
		JLabel lblNewLabel = new JLabel("Row annotation");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 14;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblRowAnnotationRadius = new JLabel("Start radius");
		lblRowAnnotationRadius.setToolTipText("Row annnotion radius");
		lblRowAnnotationRadius.setFont(globalFont);
		GridBagConstraints gbc_lblRowAnnotationRadius = new GridBagConstraints();
		gbc_lblRowAnnotationRadius.anchor = GridBagConstraints.WEST;
		gbc_lblRowAnnotationRadius.gridwidth = 2;
		gbc_lblRowAnnotationRadius.insets = new Insets(0, 0, 5, 5);
		gbc_lblRowAnnotationRadius.gridx = 0;
		gbc_lblRowAnnotationRadius.gridy = 15;
		add(lblRowAnnotationRadius, gbc_lblRowAnnotationRadius);
		
		spinnerRowAnnoRadius = new JSpinner(new SpinnerNumberModel(2, 0, 500, 1));
		spinnerRowAnnoRadius.setFont(globalFont);
		GridBagConstraints gbc_spinnerRowAnnoRadius = new GridBagConstraints();
		gbc_spinnerRowAnnoRadius.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerRowAnnoRadius.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerRowAnnoRadius.gridx = 2;
		gbc_spinnerRowAnnoRadius.gridy = 15;
		add(spinnerRowAnnoRadius, gbc_spinnerRowAnnoRadius);
		
		JLabel lblAnnThickness = new JLabel("Thickness");
		lblAnnThickness.setToolTipText("Each ow annnotion thickness");
		lblAnnThickness.setFont(globalFont);
		GridBagConstraints gbc_lblAnnThickness = new GridBagConstraints();
		gbc_lblAnnThickness.anchor = GridBagConstraints.WEST;
		gbc_lblAnnThickness.insets = new Insets(0, 0, 5, 5);
		gbc_lblAnnThickness.gridx = 0;
		gbc_lblAnnThickness.gridy = 16;
		add(lblAnnThickness, gbc_lblAnnThickness);
		
		spinnerRowAnnThickness = new JSpinner(new SpinnerNumberModel(2, 0, 1000, 1));
		spinnerRowAnnThickness.setFont(globalFont);
		GridBagConstraints gbc_spinnerRowAnnThickness = new GridBagConstraints();
		gbc_spinnerRowAnnThickness.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerRowAnnThickness.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerRowAnnThickness.gridx = 2;
		gbc_spinnerRowAnnThickness.gridy = 16;
		add(spinnerRowAnnThickness, gbc_spinnerRowAnnThickness);
		
		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 17;
		add(separator_1, gbc_separator_1);
		
		JLabel lblColAnnotation = new JLabel("Col annotation & tree");
		lblColAnnotation.setFont(globalFont);
		GridBagConstraints gbc_lblColAnnotation = new GridBagConstraints();
		gbc_lblColAnnotation.anchor = GridBagConstraints.WEST;
		gbc_lblColAnnotation.gridwidth = 3;
		gbc_lblColAnnotation.insets = new Insets(0, 0, 5, 5);
		gbc_lblColAnnotation.gridx = 0;
		gbc_lblColAnnotation.gridy = 18;
		add(lblColAnnotation, gbc_lblColAnnotation);
		
		JLabel lblPaintingDegree = new JLabel("Total degree");
		lblPaintingDegree.setToolTipText("Each ow annnotion thickness");
		lblPaintingDegree.setFont(globalFont);
		GridBagConstraints gbc_lblPaintingDegree = new GridBagConstraints();
		gbc_lblPaintingDegree.anchor = GridBagConstraints.WEST;
		gbc_lblPaintingDegree.insets = new Insets(0, 0, 5, 5);
		gbc_lblPaintingDegree.gridx = 0;
		gbc_lblPaintingDegree.gridy = 19;
		add(lblPaintingDegree, gbc_lblPaintingDegree);
		
		spinnerTotalDegree = new JSpinner(new SpinnerNumberModel(60, 10, 120, 2));
		spinnerTotalDegree.setFont(globalFont);
		GridBagConstraints gbc_spinnerTotalDegree = new GridBagConstraints();
		gbc_spinnerTotalDegree.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerTotalDegree.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTotalDegree.gridx = 2;
		gbc_spinnerTotalDegree.gridy = 19;
		add(spinnerTotalDegree, gbc_spinnerTotalDegree);
		
		JLabel label_2 = new JLabel("Thickness");
		label_2.setToolTipText("Each ow annnotion thickness");
		label_2.setFont(globalFont);
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.WEST;
		gbc_label_2.insets = new Insets(0, 0, 0, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 20;
		add(label_2, gbc_label_2);
		
		spinnerColAnnoThickness = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1));
		spinnerColAnnoThickness.setFont(globalFont);
		GridBagConstraints gbc_spinnerColAnnoThickness = new GridBagConstraints();
		gbc_spinnerColAnnoThickness.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerColAnnoThickness.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerColAnnoThickness.gridx = 2;
		gbc_spinnerColAnnoThickness.gridy = 20;
		add(spinnerColAnnoThickness, gbc_spinnerColAnnoThickness);
		
		SwingUtilities.invokeLater(() -> {
			setCircularCompsEnable(false);
		});
		
	}

	@Override
	public void initializeParameters() {
		removeListeners();

		PaintingLocationParas locationParas = getController().getPaintJPanel().getLocationParas();
		double dd = locationParas.getRowTreeWeidth() / locationParas.getWidth() * 100;
		jSpinnerRowClusterAllo.setValue((int) dd);
		jSpinnerRowClusterAllo.setEnabled(false);

		double tt = locationParas.getColTreeHeight() / locationParas.getHeight() * 100;
		jSpinnerColClusterAllo.setValue((int) tt);
		jSpinnerColClusterAllo.setEnabled(false);

		ParameterModel paraModel = getController().getParaModel();
		CircularParameters cirPara = paraModel.getCircularParameters();
		jSpinnerGapSize.setValue(paraModel.getGapSize());
		jSpinnerCellHeight.setValue((int) locationParas.getCellHeight());
		jSpinnerCellWidth.setValue((int) locationParas.getCellWidth());
		
		spinnerStartDegree.setValue((int) cirPara.getTotalSstartDeg());
		spinnerExtendDegree.setValue((int) cirPara.getTotalEextendDeg());
		spinnerInnerRadius.setValue((int) cirPara.getInnerRadius());
		spinnerRingThickness.setValue((int) cirPara.getRingThickness());
		
		spinnerRowAnnoRadius.setValue((int) cirPara.getRowAnnotationRadius());
		spinnerRowAnnThickness.setValue((int) cirPara.getRowAnnotationEachThickness());

		spinnerTotalDegree.setValue((int) cirPara.getTotalDegreeForPaintingColTree());
		spinnerColAnnoThickness.setValue((int) cirPara.getColEachAnnotationDegree());
		addListeners();
	}

	@Override
	public void removeListeners() {
		jSpinnerRowClusterAllo.removeChangeListener(jSpinnerRowClusterAlloChangeListener);
		jSpinnerColClusterAllo.removeChangeListener(jSpinnerColClusterAlloChangeListener);
		jSpinnerGapSize.removeChangeListener(jSpinnerGapSizeChangeListener);
		jSpinnerCellHeight.removeChangeListener(jSpinnerCellHeightChangeListener);
		jSpinnerCellWidth.removeChangeListener(jSpinnerCellWidthChangeListener);
		spinnerRowAnnoRadius.removeChangeListener(circularLayoutChangeListener);
		spinnerRowAnnThickness.removeChangeListener(circularLayoutChangeListener);
		spinnerTotalDegree.removeChangeListener(circularLayoutChangeListener);
		spinnerColAnnoThickness.removeChangeListener(circularLayoutChangeListener);
		
		spinnerStartDegree.removeChangeListener(circularLayoutChangeListener);
		spinnerExtendDegree.removeChangeListener(circularLayoutChangeListener);
		spinnerInnerRadius.removeChangeListener(circularLayoutChangeListener);
		spinnerRingThickness.removeChangeListener(circularLayoutChangeListener);
		
		ItemListener[] itemListeners1 = jRadioButtonCircular.getItemListeners();
		for (ItemListener itemListener : itemListeners1) {
			jRadioButtonCircular.removeItemListener(itemListener);
		}
		ItemListener[] itemListeners2 = jRadioButtonRect.getItemListeners();
		for (ItemListener itemListener : itemListeners2) {
			jRadioButtonRect.removeItemListener(itemListener);
		}

	}

	private void adjustCircularLayout() {
		HeatmapController controller = getController();
		ParameterModel paraModel = controller.getParaModel();
		CircularParameters cirPara = paraModel.getCircularParameters();
		
		cirPara.setTotalSstartDeg(Double.parseDouble(spinnerStartDegree.getValue().toString()));
		cirPara.setTotalEextendDeg(Double.parseDouble(spinnerExtendDegree.getValue().toString()));
		cirPara.setInnerRadius(Double.parseDouble(spinnerInnerRadius.getValue().toString()));
		cirPara.setRingThickness(Double.parseDouble(spinnerRingThickness.getValue().toString()));
		cirPara.setShouldAnimate(chckbx_animation.isSelected());
		
		
		//这个变量应该智能地跳动！
		double rowAnnotaionStartRadicus = Double.parseDouble(spinnerRowAnnoRadius.getValue().toString());
		if (rowAnnotaionStartRadicus + 12 > cirPara.getInnerRadius() && rowAnnotaionStartRadicus < cirPara.getInnerRadius()) {
			rowAnnotaionStartRadicus = paraModel.getNumOfCols() * cirPara.getRingThickness()+ cirPara.getInnerRadius() + 20;
			spinnerRowAnnoRadius.setValue((int) rowAnnotaionStartRadicus); 
		}
		
		cirPara.setRowAnnotationRadius(rowAnnotaionStartRadicus);
		cirPara.setRowAnnotationEachThickness(Double.parseDouble(spinnerRowAnnThickness.getValue().toString()));
		
		
		cirPara.setTotalDegreeForPaintingColTree(Double.parseDouble(spinnerTotalDegree.getValue().toString()));
		cirPara.setColEachAnnotationDegree(Integer.parseInt(spinnerColAnnoThickness.getValue().toString()));
		
		controller.weakestRefreshHeatmapForInteractiveDrag(true, true);
	}

	@Override
	public void addListeners() {
		spinnerStartDegree.addChangeListener(circularLayoutChangeListener);
		spinnerExtendDegree.addChangeListener(circularLayoutChangeListener);
		spinnerInnerRadius.addChangeListener(circularLayoutChangeListener);
		spinnerRingThickness.addChangeListener(circularLayoutChangeListener);
		spinnerRowAnnoRadius.addChangeListener(circularLayoutChangeListener);
		spinnerRowAnnThickness.addChangeListener(circularLayoutChangeListener);
		spinnerTotalDegree.addChangeListener(circularLayoutChangeListener);
		spinnerColAnnoThickness.addChangeListener(circularLayoutChangeListener);
		
		jSpinnerGapSize.addChangeListener(jSpinnerGapSizeChangeListener);
		jSpinnerCellHeight.addChangeListener(jSpinnerCellHeightChangeListener);
		jSpinnerCellWidth.addChangeListener(jSpinnerCellWidthChangeListener);

		String string = "<html><body>Note: this purely change to cell width.<br>If you want to change location of row names or col names,<br> please directly drag on the graph!";
		jSpinnerCellHeight.setToolTipText(string);
		jSpinnerCellWidth.setToolTipText(string);

		jRadioButtonCircular.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				/**
				 * 这里有点特殊，因为这个值是要传过去的！
				 */
				CircularParameters cirPara = getController().getParaModel().getCircularParameters();
				cirPara.setShouldAnimate(chckbx_animation.isSelected());
				eheatmapMain.initializePintingPanel(PaintingLayout.Circular);
				
				setCircularCompsEnable(true);
				setRectangularCompsEnable(false);
				eheatmapMain.invokeTheFeatureMethod(4);
			}
		});
		jRadioButtonRect.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				eheatmapMain.initializePintingPanel(PaintingLayout.Rectangular);
				setCircularCompsEnable(false);
				setRectangularCompsEnable(true);
			}
		});
	}
	
	private void setRectangularCompsEnable(boolean b) {
		jSpinnerGapSize.setEnabled(b);
		jSpinnerCellHeight.setEnabled(b);
		jSpinnerCellWidth.setEnabled(b);
	}
	private void setCircularCompsEnable(boolean b) {
		
		ParameterModel paraModel = getController().getParaModel();
		spinnerStartDegree.setEnabled(b);
		spinnerExtendDegree.setEnabled(b);
		spinnerInnerRadius.setEnabled(b);
		spinnerRingThickness.setEnabled(b);
		
		
		boolean ifPaintRowAnnotation = paraModel ==null? false :paraModel.isIfPaintRowAnnotation();
		spinnerRowAnnoRadius.setEnabled(b && ifPaintRowAnnotation);
		spinnerRowAnnThickness.setEnabled(b && ifPaintRowAnnotation);
		
		boolean ifPaintColAnnotation = paraModel ==null? false :paraModel.isIfPaintColAnnotation();
		spinnerTotalDegree.setEnabled(b && ifPaintColAnnotation);
		spinnerColAnnoThickness.setEnabled(b && ifPaintColAnnotation);
		
	}
}
