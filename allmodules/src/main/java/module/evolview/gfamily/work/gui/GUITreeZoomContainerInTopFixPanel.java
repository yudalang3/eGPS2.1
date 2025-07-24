package module.evolview.gfamily.work.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import egps2.frame.gui.comp.toggle.toggle.ToggleAdapter;
import egps2.frame.gui.comp.toggle.toggle.ToggleButton;
import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.gui.tree.control.Turn2ThisLayoutPanel;
import module.evolview.gfamily.work.model.enums.TreeLayoutEnum;

@SuppressWarnings("serial")
public class GUITreeZoomContainerInTopFixPanel extends BaseCtrlPanel implements Turn2ThisLayoutPanel {
	private JSpinner spinnerVerticalOrWholeZoom;
	private JSpinner spinnerHorizontalChange;

	private ChangeListener changeListenerSpinnerVerticalZoom;
	private ChangeListener changeListenerSpinnerHorizontal;

	private JLabel lblHorizontalZoom;
	private JLabel labelVerticalZoom;

	private final int defaultInitializeValue = 100;

	TreeLayoutEnum lastTreeLayoutControlPanel;
	private JCheckBox chckbxLockAspectRatio;
	private ToggleButton height1ScaleTogbtn;
	private ToggleButton width1ScaleTogbtn;

	public GUITreeZoomContainerInTopFixPanel() {
		GridBagLayout gbl_zoomAndRotate_1 = new GridBagLayout();
		gbl_zoomAndRotate_1.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_zoomAndRotate_1.rowHeights = new int[] { 0, 0, 23, 23, 0, 12, 0 };
		gbl_zoomAndRotate_1.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_zoomAndRotate_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gbl_zoomAndRotate_1);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 2;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		add(separator, gbc_separator);

		labelVerticalZoom = new JLabel("Height scale");
		labelVerticalZoom.setToolTipText("Zoom in or zoom out!");
		labelVerticalZoom.setFont(globalFont);
		GridBagConstraints gbc_labelVerticalZoom = new GridBagConstraints();
		gbc_labelVerticalZoom.anchor = GridBagConstraints.WEST;
		gbc_labelVerticalZoom.insets = new Insets(0, 0, 5, 5);
		gbc_labelVerticalZoom.gridx = 0;
		gbc_labelVerticalZoom.gridy = 2;
		add(labelVerticalZoom, gbc_labelVerticalZoom);

		spinnerVerticalOrWholeZoom = new JSpinner();
		spinnerVerticalOrWholeZoom
				.setModel(new SpinnerNumberModel(defaultInitializeValue, defaultInitializeValue, 100000, 1));
		spinnerVerticalOrWholeZoom.setFont(globalFont);
		GridBagConstraints gbc_spinnerVerticalZoom = new GridBagConstraints();
		gbc_spinnerVerticalZoom.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerVerticalZoom.gridx = 1;
		gbc_spinnerVerticalZoom.gridy = 2;
		add(spinnerVerticalOrWholeZoom, gbc_spinnerVerticalZoom);

		JLabel lblNewLabel = new JLabel("%");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 2;
		add(lblNewLabel, gbc_lblNewLabel);

		Dimension dimension = new Dimension(35, 25);
		height1ScaleTogbtn = new ToggleButton();
		java.awt.Color fg = new java.awt.Color(40, 139, 236);
		height1ScaleTogbtn.setForeground(fg);
		height1ScaleTogbtn.setSelected(true);
		height1ScaleTogbtn.setPreferredSize(dimension);
		height1ScaleTogbtn.setToolTipText("Control whether the mouse wheel action take effects on height scale.");
		height1ScaleTogbtn.addEventToggleSelected(new ToggleAdapter() {
			@Override
			public void onSelected(boolean selected) {
				controller.getTreeLayoutProperties().setWhetherHeightScaleOnMouseWheel(selected);;
			}
			
		});

		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 2;
		add(height1ScaleTogbtn, gbc_btnNewButton);

		lblHorizontalZoom = new JLabel("Width  scale");
		lblHorizontalZoom.setToolTipText("Zoom in or zoom out!");
		lblHorizontalZoom.setFont(globalFont);
		GridBagConstraints gbc_lblHorizontalZoom = new GridBagConstraints();
		gbc_lblHorizontalZoom.anchor = GridBagConstraints.WEST;
		gbc_lblHorizontalZoom.insets = new Insets(0, 0, 5, 5);
		gbc_lblHorizontalZoom.gridx = 0;
		gbc_lblHorizontalZoom.gridy = 3;
		add(lblHorizontalZoom, gbc_lblHorizontalZoom);

		spinnerHorizontalChange = new JSpinner();
		spinnerHorizontalChange
				.setModel(new SpinnerNumberModel(defaultInitializeValue, defaultInitializeValue, 100000, 1));
		spinnerHorizontalChange.setFont(globalFont);
		GridBagConstraints gbc_spinnerHorizontalChange = new GridBagConstraints();
		gbc_spinnerHorizontalChange.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerHorizontalChange.anchor = GridBagConstraints.WEST;
		gbc_spinnerHorizontalChange.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerHorizontalChange.gridx = 1;
		gbc_spinnerHorizontalChange.gridy = 3;
		add(spinnerHorizontalChange, gbc_spinnerHorizontalChange);

		JLabel lblNewLabel_1 = new JLabel("%");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 3;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		width1ScaleTogbtn = new ToggleButton();
		width1ScaleTogbtn.setPreferredSize(dimension);
		width1ScaleTogbtn.setSelected(true);
		width1ScaleTogbtn.setForeground(fg);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 3;
		add(width1ScaleTogbtn, gbc_btnNewButton_1);
		width1ScaleTogbtn.setToolTipText("Control whether the mouse wheel action take effects on width scale.");
		width1ScaleTogbtn.addEventToggleSelected(new ToggleAdapter() {
			@Override
			public void onSelected(boolean selected) {
				controller.getTreeLayoutProperties().setWhetherWidthScaleOnMouseWheel(selected);
			}
			
		});

		chckbxLockAspectRatio = new JCheckBox("Lock aspect ratio");
		chckbxLockAspectRatio.setFocusable(false);
		chckbxLockAspectRatio.setFont(globalFont);
		chckbxLockAspectRatio.setToolTipText("<html>This only influence the manually height and width scale in spinner.<br>The mouse wheel zoom in and out will not be affected.");;
		GridBagConstraints gbc_chckbxLockAspectRatio = new GridBagConstraints();
		gbc_chckbxLockAspectRatio.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxLockAspectRatio.gridwidth = 2;
		gbc_chckbxLockAspectRatio.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxLockAspectRatio.gridx = 0;
		gbc_chckbxLockAspectRatio.gridy = 4;
		add(chckbxLockAspectRatio, gbc_chckbxLockAspectRatio);

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.insets = new Insets(0, 0, 0, 5);
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.anchor = GridBagConstraints.CENTER;
		gbc_separator_1.gridwidth = 4;
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 5;
		add(separator_1, gbc_separator_1);

		changeListenerSpinnerHorizontal = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();

//			Dimension heightAndWidthChangeDimension = getHeightAndWidthChangeDimension(value);
//			if (chckbxLockAspectRatio.isSelected()) {
//				controller.discreteZoomInOrOut(heightAndWidthChangeDimension.height,
//						heightAndWidthChangeDimension.width);
//
//				spinnerVerticalOrWholeZoom.removeChangeListener(ChangeListenerSpinnerVerticalZoom);
//				spinnerVerticalOrWholeZoom.setValue(value);
//				spinnerVerticalOrWholeZoom.addChangeListener(ChangeListenerSpinnerVerticalZoom);
//			} else {
//				controller.discreteZoomInOrOut(0, heightAndWidthChangeDimension.width);
//			}
		};

		changeListenerSpinnerVerticalZoom = e -> {
			Object source = e.getSource();
			JSpinner slider = (JSpinner) source;
			int value = (int) slider.getValue();

			Dimension heightAndWidthChangeDimension = getHeightAndWidthChangeDimension(value);
//			if (chckbxLockAspectRatio.isSelected()) {
//				controller.discreteZoomInOrOut(heightAndWidthChangeDimension.height,
//						heightAndWidthChangeDimension.width);
//
//				spinnerHorizontalChange.removeChangeListener(ChangeListenerSpinnerHorizontal);
//				spinnerHorizontalChange.setValue(value);
//				spinnerHorizontalChange.addChangeListener(ChangeListenerSpinnerHorizontal);
//			} else {
//				controller.discreteZoomInOrOut(heightAndWidthChangeDimension.height, 0);
//			}
		};

		// 初始化状态与监听
		actionsTurn2ThisLayout(TreeLayoutEnum.RECTANGULAR_LAYOUT);

	}

	protected boolean shouldAddShowOutgroupCheckBox() {
		return true;
	}

	public Dimension getHeightAndWidthChangeDimension(int value) {
		return null;
//		Dimension treePanelDimensize = controller.getGlobalPhylogeneticTreePanel().getTreePanelDimensize();
//
//		double increaseFactor = 0.01 * (value - defaultInitializeValue);
//		int heightChange = (int) (increaseFactor * treePanelDimensize.getHeight());
//		int widthChange = (int) (increaseFactor * treePanelDimensize.getWidth());
//
//		return new Dimension(widthChange, heightChange);
	}

	@Override
	public void actionsTurn2ThisLayout(TreeLayoutEnum conPanl) {
		removeAllListeners();
		switch (conPanl) {
		case SLOPE_LAYOUT:
			switchBetweenLayout(conPanl);
			addAllListeners(conPanl);
			break;
		case RECTANGULAR_LAYOUT:
			switchBetweenLayout(conPanl);
			addAllListeners(conPanl);
			break;
		case CIRCULAR_LAYOUT:
			switchBetweenLayout(conPanl);
			addAllListeners(conPanl);
			break;
		case RADICAL_LAYOUT:
			switchBetweenLayout(conPanl);
			addAllListeners(conPanl);

			break;
		case SPRIAL_LAYOUT:
			switchBetweenLayout(conPanl);
			addAllListeners(conPanl);
			break;
		default:
			break;
		}

		lastTreeLayoutControlPanel = conPanl;
		
		if (controller != null) {
			controller.fireLayoutChanged();
		}

	}

	private boolean isWholeZoomLayout(TreeLayoutEnum controlPanel) {
		if (controlPanel == TreeLayoutEnum.SLOPE_LAYOUT || controlPanel == TreeLayoutEnum.RECTANGULAR_LAYOUT) {
			return false;
		}

		return true;
	}

	private void switchBetweenLayout(TreeLayoutEnum conPanl) {

		// Vertical就是whole
		int verticalValue = (int) spinnerVerticalOrWholeZoom.getValue();
		if (isWholeZoomLayout(conPanl)) {
			if (!isWholeZoomLayout(lastTreeLayoutControlPanel)) {
				// 从rectangular或者 slope 变成whole
				int horizontalValue = (int) spinnerHorizontalChange.getValue();

				if (verticalValue > horizontalValue) {
					spinnerHorizontalChange.setValue(verticalValue);
				} else if (verticalValue < horizontalValue) {
					spinnerVerticalOrWholeZoom.setValue(horizontalValue);
				}
			}

		} else {
			if (isWholeZoomLayout(lastTreeLayoutControlPanel)) {
				// 从whole变成分开的的slope 或者 rectangular layout
				spinnerHorizontalChange.setValue(verticalValue);
			}
		}

	}

	private void removeAllListeners() {

		spinnerVerticalOrWholeZoom.removeChangeListener(changeListenerSpinnerVerticalZoom);
		spinnerHorizontalChange.removeChangeListener(changeListenerSpinnerHorizontal);

	}

	private void addAllListeners(TreeLayoutEnum conPanl) {
		spinnerVerticalOrWholeZoom.addChangeListener(changeListenerSpinnerVerticalZoom);
		spinnerHorizontalChange.addChangeListener(changeListenerSpinnerHorizontal);

		if (isWholeZoomLayout(conPanl)) {
			chckbxLockAspectRatio.setSelected(true);
			chckbxLockAspectRatio.setEnabled(false);
		} else {
			chckbxLockAspectRatio.setSelected(false);
			chckbxLockAspectRatio.setEnabled(true);
		}

	}

	public void resetZoomInAndOut2zero() {
		removeAllListeners();

		spinnerVerticalOrWholeZoom.setValue(defaultInitializeValue);
		spinnerHorizontalChange.setValue(defaultInitializeValue);

		spinnerVerticalOrWholeZoom.addChangeListener(changeListenerSpinnerVerticalZoom);
		spinnerHorizontalChange.addChangeListener(changeListenerSpinnerHorizontal);
	}

	public void resetZoomLayoutPanel(int newHeight, int newWidth) {
//		PhylogeneticTreePanel globalPhylogeneticTreePanel = controller.getGlobalPhylogeneticTreePanel();
//		int oriWidth = globalPhylogeneticTreePanel.getWidth();
//		int oriHeight = globalPhylogeneticTreePanel.getHeight();
//		removeAllListeners();
//
//		double d1 = newHeight / (double) oriHeight;
//		double d2 = newWidth / (double) oriWidth;
//		spinnerVerticalOrWholeZoom.setValue((int) (d1 * 100));
//		spinnerHorizontalChange.setValue((int) (d2 * 100));
//
//		spinnerVerticalOrWholeZoom.addChangeListener(ChangeListenerSpinnerVerticalZoom);
//		spinnerHorizontalChange.addChangeListener(ChangeListenerSpinnerHorizontal);
	}

}
