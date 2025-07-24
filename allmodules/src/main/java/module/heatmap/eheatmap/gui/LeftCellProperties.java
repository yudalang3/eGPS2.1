package module.heatmap.eheatmap.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.tuple.Pair;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.model.datatransfer.CallBackBehavior;
import egps2.UnifiedAccessPoint;
import graphic.engine.GradientColorHolder;
import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.enums.CellShape;
import module.heatmap.eheatmap.enums.ColorEnum;
import module.heatmap.eheatmap.model.ParameterModel;
import egps2.panels.reusablecom.ParameterInitialized;

@SuppressWarnings("serial")
public class LeftCellProperties extends JPanel implements ParameterInitialized {

	private EheatmapMain eheatmapMain;
	private HeatmapController controller;

	private JComboBox<String> jComboBox2ColoScheme;
	private JRadioButton rdbtnRectangular;
	private JRadioButton rdbtnEllipse;
	private JRadioButton rdbtnPlainCircle;
	private JRadioButton rdbtnCircleWithDynamicPointer;
	private JRadioButton rdbtnCircleWithDynamicSize;
	private ChangeListener rdbtnChangeListener;
	private JRadioButton rdbtnDoubleTriangle;
	private JRadioButton rdbtnDiamond;
	private JButton btnCreate;

	public LeftCellProperties(EheatmapMain main) {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		this.eheatmapMain = main;

		Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 122, 79, 0 };
		gridBagLayout.rowHeights = new int[] { 31, 31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		btnCreate = new JButton("Create");
		btnCreate.setFont(globalFont);

		final CallBackBehavior back = () -> {
			jComboBox2ColoScheme.setSelectedIndex(5);
		};

		btnCreate.addActionListener(e -> {
			GradientColorHolder gColorHolder = getController().getParaModel().getgColorHolder();
			float[] distFinal = gColorHolder.getDist();
			Color[] colorsFinal = gColorHolder.getColors();
			SwingUtilities.invokeLater(() -> {
				EHeatmapGradientColorChooser eHeatmapColorChooser = EHeatmapGradientColorChooser
						.createTheDialog(getController(), distFinal, colorsFinal, back);
				eHeatmapColorChooser.setVisible(true);
			});

		});
		JLabel lblColorScheme = new JLabel("Color Scheme");
		lblColorScheme.setFont(globalFont);
		GridBagConstraints gbc_lblColorScheme = new GridBagConstraints();
		gbc_lblColorScheme.anchor = GridBagConstraints.WEST;
		gbc_lblColorScheme.fill = GridBagConstraints.BOTH;
		gbc_lblColorScheme.insets = new Insets(0, 0, 5, 5);
		gbc_lblColorScheme.gridx = 0;
		gbc_lblColorScheme.gridy = 0;
		add(lblColorScheme, gbc_lblColorScheme);

		jComboBox2ColoScheme = new JComboBox<String>();
		jComboBox2ColoScheme.setFont(globalFont);
		jComboBox2ColoScheme.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "Gr-Bk-Rd", "Be-Yw-Bk", "Be-We-Rd", "We-Rd", "Pheatmap", "Customed" }));
		jComboBox2ColoScheme.setToolTipText("Please use following \"Create\" button to set customed scheme!");
		GridBagConstraints gbc_jComboBox2ColoScheme = new GridBagConstraints();
		gbc_jComboBox2ColoScheme.anchor = GridBagConstraints.WEST;
		gbc_jComboBox2ColoScheme.insets = new Insets(0, 0, 5, 0);
		gbc_jComboBox2ColoScheme.gridx = 1;
		gbc_jComboBox2ColoScheme.gridy = 0;
		add(jComboBox2ColoScheme, gbc_jComboBox2ColoScheme);

		JLabel lblCustomization = new JLabel("Customization");
		lblCustomization.setFont(globalFont);
		GridBagConstraints gbc_lblCustomization = new GridBagConstraints();
		gbc_lblCustomization.anchor = GridBagConstraints.WEST;
		gbc_lblCustomization.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCustomization.insets = new Insets(0, 0, 5, 5);
		gbc_lblCustomization.gridx = 0;
		gbc_lblCustomization.gridy = 1;
		add(lblCustomization, gbc_lblCustomization);
		GridBagConstraints gbc_btnCreate = new GridBagConstraints();
		gbc_btnCreate.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCreate.anchor = GridBagConstraints.WEST;
		gbc_btnCreate.insets = new Insets(0, 0, 5, 0);
		gbc_btnCreate.gridx = 1;
		gbc_btnCreate.gridy = 1;
		add(btnCreate, gbc_btnCreate);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 2;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 3;
		add(separator, gbc_separator);

		JLabel lblCellShape = new JLabel("Cell shape");
		lblCellShape.setFont(globalFont);
		GridBagConstraints gbc_lblCellShape = new GridBagConstraints();
		gbc_lblCellShape.anchor = GridBagConstraints.WEST;
		gbc_lblCellShape.insets = new Insets(0, 0, 5, 5);
		gbc_lblCellShape.gridx = 0;
		gbc_lblCellShape.gridy = 4;
		add(lblCellShape, gbc_lblCellShape);

		ButtonGroup buttonGroup = new ButtonGroup();

		rdbtnRectangular = new JRadioButton("Rectange");
		rdbtnRectangular.setSelected(true);
		buttonGroup.add(rdbtnRectangular);
		rdbtnRectangular.setFont(globalFont);
		GridBagConstraints gbc_rdbtnRectangular = new GridBagConstraints();
		gbc_rdbtnRectangular.gridwidth = 2;
		gbc_rdbtnRectangular.anchor = GridBagConstraints.WEST;
		gbc_rdbtnRectangular.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnRectangular.gridx = 0;
		gbc_rdbtnRectangular.gridy = 5;
		add(rdbtnRectangular, gbc_rdbtnRectangular);

		rdbtnEllipse = new JRadioButton("Ellipse");
		buttonGroup.add(rdbtnEllipse);
		rdbtnEllipse.setFont(globalFont);
		GridBagConstraints gbc_rdbtnEllipse = new GridBagConstraints();
		gbc_rdbtnEllipse.anchor = GridBagConstraints.WEST;
		gbc_rdbtnEllipse.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnEllipse.gridx = 0;
		gbc_rdbtnEllipse.gridy = 6;
		add(rdbtnEllipse, gbc_rdbtnEllipse);

		rdbtnDiamond = new JRadioButton("Diamond");
		rdbtnDiamond.setFont(globalFont);
		buttonGroup.add(rdbtnDiamond);
		GridBagConstraints gbc_rdbtnDiamond = new GridBagConstraints();
		gbc_rdbtnDiamond.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDiamond.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnDiamond.gridx = 0;
		gbc_rdbtnDiamond.gridy = 7;
		add(rdbtnDiamond, gbc_rdbtnDiamond);

		rdbtnPlainCircle = new JRadioButton("Plain circle");
		buttonGroup.add(rdbtnPlainCircle);
		rdbtnPlainCircle.setFont(globalFont);
		GridBagConstraints gbc_rdbtnPlainCircle = new GridBagConstraints();
		gbc_rdbtnPlainCircle.gridwidth = 2;
		gbc_rdbtnPlainCircle.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPlainCircle.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPlainCircle.gridx = 0;
		gbc_rdbtnPlainCircle.gridy = 8;
		add(rdbtnPlainCircle, gbc_rdbtnPlainCircle);

		rdbtnDoubleTriangle = new JRadioButton("Double triangle");
		rdbtnDoubleTriangle.setFont(globalFont);
		buttonGroup.add(rdbtnDoubleTriangle);
		GridBagConstraints gbc_rdbtnDoubleTriangle = new GridBagConstraints();
		gbc_rdbtnDoubleTriangle.gridwidth = 2;
		gbc_rdbtnDoubleTriangle.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDoubleTriangle.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnDoubleTriangle.gridx = 0;
		gbc_rdbtnDoubleTriangle.gridy = 9;
		add(rdbtnDoubleTriangle, gbc_rdbtnDoubleTriangle);

		rdbtnCircleWithDynamicSize = new JRadioButton("Circle with dynamic size");
		rdbtnCircleWithDynamicSize.setFont(globalFont);
		buttonGroup.add(rdbtnCircleWithDynamicSize);
		GridBagConstraints gbc_rdbtnCircleWithDynamicSize = new GridBagConstraints();
		gbc_rdbtnCircleWithDynamicSize.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnCircleWithDynamicSize.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCircleWithDynamicSize.gridwidth = 2;
		gbc_rdbtnCircleWithDynamicSize.gridx = 0;
		gbc_rdbtnCircleWithDynamicSize.gridy = 10;
		add(rdbtnCircleWithDynamicSize, gbc_rdbtnCircleWithDynamicSize);

		rdbtnCircleWithDynamicPointer = new JRadioButton("Circle with dynamic pointer");
		rdbtnCircleWithDynamicPointer.setFont(globalFont);
		buttonGroup.add(rdbtnCircleWithDynamicPointer);
		GridBagConstraints gbc_rdbtnCircleWithDynamicPointer = new GridBagConstraints();
		gbc_rdbtnCircleWithDynamicPointer.gridwidth = 2;
		gbc_rdbtnCircleWithDynamicPointer.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCircleWithDynamicPointer.gridx = 0;
		gbc_rdbtnCircleWithDynamicPointer.gridy = 11;
		add(rdbtnCircleWithDynamicPointer, gbc_rdbtnCircleWithDynamicPointer);

		rdbtnChangeListener = e -> {
			executeChangeCellShapeEvent();
		};

	}

	private void executeChangeCellShapeEvent() {
		HeatmapController controller = getController();
		ParameterModel paraModel = controller.getParaModel();

		if (rdbtnEllipse.isSelected()) {
			paraModel.setCellShapePainter(CellShape.Ellipse);
		} else if (rdbtnPlainCircle.isSelected()) {
			paraModel.setCellShapePainter(CellShape.PlainCircle);
		} else if (rdbtnCircleWithDynamicSize.isSelected()) {
			paraModel.setCellShapePainter(CellShape.CircleWithSize);
		} else if (rdbtnCircleWithDynamicPointer.isSelected()) {
			paraModel.setCellShapePainter(CellShape.CircleWithPointer);
		} else if (rdbtnDiamond.isSelected()) {
			paraModel.setCellShapePainter(CellShape.Diamond);
		} else if (rdbtnDoubleTriangle.isSelected()) {
			paraModel.setCellShapePainter(CellShape.DoubleTriangle);
		} else {
			paraModel.setCellShapePainter(CellShape.Rectange);
			// Rectangle is selected
		}
		controller.weakestRefreshHeatmap();
		
		eheatmapMain.invokeTheFeatureMethod(3);
	}

	public HeatmapController getController() {
		if (controller == null) {
			controller = (HeatmapController) eheatmapMain.getController();
		}
		return controller;
	}

	private void jComboBox2ColorSchemeActionPerformed() {
		GradientColorHolder gColorHolder = getController().getParaModel().getgColorHolder();

		float[] dist = gColorHolder.getDist();
		Color[] colors = gColorHolder.getColors();
		int selectedIndex = jComboBox2ColoScheme.getSelectedIndex();
		// The order is
		// new String[] { "Gr-Bk-Rd", "Be-Yw-Bk", "Be-We-Rd", "We-Rd", "Pheatmap",
		// "customed" }));

		switch (selectedIndex) {
		case 0:
			dist = ColorEnum.GRBKRD.getDists();
			colors = ColorEnum.GRBKRD.getColors();
			break;
		case 1:
			dist = ColorEnum.BEYWBK.getDists();
			colors = ColorEnum.BEYWBK.getColors();
			break;
		case 2:
			dist = ColorEnum.BEWERD.getDists();
			colors = ColorEnum.BEWERD.getColors();
			break;
		case 3:
			dist = ColorEnum.WERD.getDists();
			colors = ColorEnum.WERD.getColors();
			break;
		case 4:
			dist = ColorEnum.PHEATMAP.getDists();
			colors = ColorEnum.PHEATMAP.getColors();

			break;
		default:
			// 就是第五种
			ParameterModel paraModel = getController().getParaModel();
			Pair<float[], Color[]> customizedColorScheme = paraModel.getCustomizedColorScheme();
			if (customizedColorScheme == null) {
				SwingDialog.showInfoMSGDialog("Effects note",
						"Please click the \"Create\" button to create custome color scheme.");
			}else {
				dist = customizedColorScheme.getLeft();
				colors = customizedColorScheme.getRight();
			}
			break;
		}

		gColorHolder.setColorScheme(dist, colors);
		getController().weakestRefreshHeatmap();
		eheatmapMain.invokeTheFeatureMethod(2);
	}

	@Override
	public void initializeParameters() {
		removeListeners();

		HeatmapController controller = getController();
		ParameterModel paraModel = controller.getParaModel();

		CellShape cellShapePainter = paraModel.getCellShapePainter();
		if (cellShapePainter == CellShape.Ellipse) {
			rdbtnPlainCircle.setSelected(true);
		} else if (cellShapePainter == CellShape.PlainCircle) {
			rdbtnPlainCircle.setSelected(true);
		} else if (cellShapePainter == CellShape.CircleWithSize) {
			rdbtnCircleWithDynamicSize.setSelected(true);
		} else if (cellShapePainter == CellShape.CircleWithPointer) {
			rdbtnCircleWithDynamicPointer.setSelected(true);
		} else if (cellShapePainter == CellShape.Diamond) {
			rdbtnDiamond.setSelected(true);
		} else if (cellShapePainter == CellShape.DoubleTriangle) {
			rdbtnDoubleTriangle.setSelected(true);
		} else {
			// Rectangle is selected
			rdbtnRectangular.setSelected(true);
		}

		addListeners();
	}

	@Override
	public void addListeners() {
		jComboBox2ColoScheme.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jComboBox2ColorSchemeActionPerformed();
			}
		});

		rdbtnRectangular.addChangeListener(rdbtnChangeListener);
		rdbtnEllipse.addChangeListener(rdbtnChangeListener);
		rdbtnPlainCircle.addChangeListener(rdbtnChangeListener);
		rdbtnCircleWithDynamicPointer.addChangeListener(rdbtnChangeListener);
		rdbtnCircleWithDynamicSize.addChangeListener(rdbtnChangeListener);
		rdbtnDiamond.addChangeListener(rdbtnChangeListener);
		rdbtnDoubleTriangle.addChangeListener(rdbtnChangeListener);
	}

	@Override
	public void removeListeners() {
		ActionListener[] actionListeners = jComboBox2ColoScheme.getActionListeners();
		for (ActionListener actionListener : actionListeners) {
			jComboBox2ColoScheme.removeActionListener(actionListener);
		}

		rdbtnRectangular.removeChangeListener(rdbtnChangeListener);
		rdbtnEllipse.removeChangeListener(rdbtnChangeListener);
		rdbtnPlainCircle.removeChangeListener(rdbtnChangeListener);
		rdbtnCircleWithDynamicPointer.removeChangeListener(rdbtnChangeListener);
		rdbtnCircleWithDynamicSize.removeChangeListener(rdbtnChangeListener);
		rdbtnDiamond.removeChangeListener(rdbtnChangeListener);
		rdbtnDoubleTriangle.removeChangeListener(rdbtnChangeListener);
	}
}
