package module.evolview.gfamily.work.gui.tree.control;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.phylotree.visualization.graphics.struct.TreeLayout;
import module.evolview.gfamily.work.model.enums.TreeLayoutEnum;
import module.evolview.phylotree.visualization.graphics.struct.SprialLayoutProperty;

@SuppressWarnings("serial")
public class SubSprialLayout extends BaseCtrlPanel implements Turn2ThisLayoutPanel {
	private JRadioButton rdbtnPhylogram;
	private JRadioButton rdbtnCladogram;
	private JRadioButton rdbtnAlpha;
	private JRadioButton rdbtnBeta;
	private JSpinner spinnerBetaFactor;
	private JSpinner spinnerExtentDegree;
	private JSlider sliderExtentDegree;

	private ChangeListener spinnerExtentDegreeChangeListener;
	private JSlider sliderRootLength;
	private JSpinner spinnerRootTipLength;
	private ChangeListener rootLengthSpinnerChangeListener;

	private JSlider sliderGapFactor;
	private JSpinner spinnerGapFactor;
	private ChangeListener spinnerGapFactorChangeListener;
	private JCheckBox checkBoxShowOutgroup;

	public SubSprialLayout() {
		GridBagLayout gridBagLayout = new GridBagLayout();
//		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
//		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
//		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
//				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		rdbtnPhylogram = new JRadioButton("Phylogram");
		rdbtnPhylogram.setFocusable(false);
		rdbtnPhylogram.setFont(globalFont);
		rdbtnPhylogram.setSelected(true);
		GridBagConstraints gbc_rdbtnPhylogram = new GridBagConstraints();
		gbc_rdbtnPhylogram.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPhylogram.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPhylogram.gridx = 0;
		gbc_rdbtnPhylogram.gridy = 0;
		add(rdbtnPhylogram, gbc_rdbtnPhylogram);
		rdbtnPhylogram.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				actionsTurn2ThisLayout(null);
			}
		});

		rdbtnCladogram = new JRadioButton("Cladogram");
		rdbtnCladogram.setFocusable(false);
		rdbtnCladogram.setFont(globalFont);
		GridBagConstraints gbc_rdbtnCladogram = new GridBagConstraints();
		gbc_rdbtnCladogram.gridwidth = 2;
		gbc_rdbtnCladogram.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnCladogram.gridx = 2;
		gbc_rdbtnCladogram.gridy = 0;
		add(rdbtnCladogram, gbc_rdbtnCladogram);
		rdbtnCladogram.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				actionsTurn2ThisLayout(null);
			}
		});
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(rdbtnPhylogram);
		buttonGroup1.add(rdbtnCladogram);

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.anchor = GridBagConstraints.WEST;
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 1;
		add(separator_1, gbc_separator_1);

		final int theMaxValueOfRootLength = 200;
		checkBoxShowOutgroup = new JCheckBox("Show root");
		checkBoxShowOutgroup.setFocusable(false);
		checkBoxShowOutgroup.setFont(globalFont);
		checkBoxShowOutgroup.setSelected(false);
		GridBagConstraints gbc_checkBoxShowOutgroup = new GridBagConstraints();
		gbc_checkBoxShowOutgroup.anchor = GridBagConstraints.WEST;
		gbc_checkBoxShowOutgroup.insets = new Insets(0, 0, 5, 5);
		gbc_checkBoxShowOutgroup.gridx = 0;
		gbc_checkBoxShowOutgroup.gridy = 2;
		add(checkBoxShowOutgroup, gbc_checkBoxShowOutgroup);
		checkBoxShowOutgroup.addItemListener(e -> {
			boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			controller.getTreeLayoutProperties().setShowRoot(selected);
			controller.refreshPhylogeneticTree();

			sliderRootLength.setEnabled(selected);
			spinnerRootTipLength.setEnabled(selected);
		});

		sliderRootLength = new JSlider(0,theMaxValueOfRootLength, 15);
		sliderRootLength.setFocusable(false);
		sliderRootLength.setValue(15);
		sliderRootLength.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_sliderRootLength = new GridBagConstraints();
		gbc_sliderRootLength.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderRootLength.anchor = GridBagConstraints.WEST;
		gbc_sliderRootLength.gridwidth = 2;
		gbc_sliderRootLength.insets = new Insets(0, 0, 5, 5);
		gbc_sliderRootLength.gridx = 0;
		gbc_sliderRootLength.gridy = 3;

		sliderRootLength.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JSlider source = (JSlider) e.getSource();
				int value = source.getValue();
				spinnerRootTipLength.setValue(value);
			}
		});
		add(sliderRootLength, gbc_sliderRootLength);

		spinnerRootTipLength = new JSpinner();
		spinnerRootTipLength.setFont(globalFont);
		spinnerRootTipLength.setModel(new SpinnerNumberModel(15, 0, theMaxValueOfRootLength, 1));
		GridBagConstraints gbc_spinnerRootTipLength = new GridBagConstraints();
		gbc_spinnerRootTipLength.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerRootTipLength.anchor = GridBagConstraints.WEST;
		gbc_spinnerRootTipLength.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerRootTipLength.gridx = 2;
		gbc_spinnerRootTipLength.gridy = 3;
		add(spinnerRootTipLength, gbc_spinnerRootTipLength);
		rootLengthSpinnerChangeListener = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderRootLength.setValue(value);
			controller.getTreeLayoutProperties().setRootTipLength(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		};
		spinnerRootTipLength.addChangeListener(rootLengthSpinnerChangeListener);

		JLabel lblNewLabel = new JLabel("Extent degree");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 4;
		add(lblNewLabel, gbc_lblNewLabel);

		sliderExtentDegree = new JSlider();
		sliderExtentDegree.setFocusable(false);
		sliderExtentDegree.setMaximum(10000);
		sliderExtentDegree.setValue(720);
		sliderExtentDegree.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_sliderExtentDegree = new GridBagConstraints();
		gbc_sliderExtentDegree.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderExtentDegree.anchor = GridBagConstraints.WEST;
		gbc_sliderExtentDegree.gridwidth = 2;
		gbc_sliderExtentDegree.insets = new Insets(0, 0, 5, 5);
		gbc_sliderExtentDegree.gridx = 0;
		gbc_sliderExtentDegree.gridy = 5;
		sliderExtentDegree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JSlider source = (JSlider) e.getSource();
				int value = source.getValue();
				spinnerExtentDegree.setValue(value);
			}
		});
		add(sliderExtentDegree, gbc_sliderExtentDegree);

		spinnerExtentDegree = new JSpinner();
		spinnerExtentDegree.setFont(globalFont);
		spinnerExtentDegree.setModel(new SpinnerNumberModel(630, 0, 10000, 1));
		GridBagConstraints gbc_spinnerExtentDegree = new GridBagConstraints();
		gbc_spinnerExtentDegree.anchor = GridBagConstraints.WEST;
		gbc_spinnerExtentDegree.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerExtentDegree.gridx = 2;
		gbc_spinnerExtentDegree.gridy = 5;
		add(spinnerExtentDegree, gbc_spinnerExtentDegree);
		spinnerExtentDegreeChangeListener = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderExtentDegree.setValue(value);
			SprialLayoutProperty sprialLayoutPropertiy = controller.getTreeLayoutProperties()
					.getSprialLayoutPropertiy();
			sprialLayoutPropertiy.setGlobalExtendingDegree(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		};
		spinnerExtentDegree.addChangeListener(spinnerExtentDegreeChangeListener);

		JLabel lblGapFactor = new JLabel("Gap factor");
		lblGapFactor.setFont(globalFont);
		GridBagConstraints gbc_lblGapFactor = new GridBagConstraints();
		gbc_lblGapFactor.anchor = GridBagConstraints.WEST;
		gbc_lblGapFactor.insets = new Insets(0, 0, 5, 5);
		gbc_lblGapFactor.gridx = 0;
		gbc_lblGapFactor.gridy = 6;
		add(lblGapFactor, gbc_lblGapFactor);

		sliderGapFactor = new JSlider();
		sliderGapFactor.setFocusable(false);
		sliderGapFactor.setValue(10);
		sliderGapFactor.setMaximum(50);
		sliderGapFactor.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_sliderGapFactor = new GridBagConstraints();
		gbc_sliderGapFactor.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderGapFactor.anchor = GridBagConstraints.WEST;
		gbc_sliderGapFactor.gridwidth = 2;
		gbc_sliderGapFactor.insets = new Insets(0, 0, 5, 5);
		gbc_sliderGapFactor.gridx = 0;
		gbc_sliderGapFactor.gridy = 7;
		add(sliderGapFactor, gbc_sliderGapFactor);
		sliderGapFactor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JSlider source = (JSlider) e.getSource();
				int value = source.getValue();
				spinnerGapFactor.setValue(value);
			}
		});

		spinnerGapFactor = new JSpinner();
		spinnerGapFactor.setModel(new SpinnerNumberModel(10, 0, 50, 1));
		spinnerGapFactor.setFont(globalFont);
		GridBagConstraints gbc_spinnerGapFactor = new GridBagConstraints();
		gbc_spinnerGapFactor.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerGapFactor.anchor = GridBagConstraints.WEST;
		gbc_spinnerGapFactor.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerGapFactor.gridx = 2;
		gbc_spinnerGapFactor.gridy = 7;
		add(spinnerGapFactor, gbc_spinnerGapFactor);
		spinnerGapFactorChangeListener = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderGapFactor.setValue(value);
			SprialLayoutProperty sProperty = controller.getTreeLayoutProperties().getSprialLayoutPropertiy();
			sProperty.setGapSize(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		};
		spinnerGapFactor.addChangeListener(spinnerGapFactorChangeListener);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.anchor = GridBagConstraints.WEST;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridwidth = 3;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 8;
		add(separator, gbc_separator);

		rdbtnAlpha = new JRadioButton("Alpha");
		rdbtnAlpha.setFocusable(false);
		rdbtnAlpha.setFont(globalFont);
		GridBagConstraints gbc_rdbtnAlpha = new GridBagConstraints();
		gbc_rdbtnAlpha.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAlpha.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnAlpha.gridx = 0;
		gbc_rdbtnAlpha.gridy = 9;
		add(rdbtnAlpha, gbc_rdbtnAlpha);
		rdbtnAlpha.setSelected(true);
		rdbtnAlpha.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				actionsTurn2ThisLayout(null);
				spinnerBetaFactor.setEnabled(false);
			}
		});

		rdbtnBeta = new JRadioButton("Beta");
		rdbtnBeta.setFocusable(false);
		rdbtnBeta.setFont(globalFont);
		GridBagConstraints gbc_rdbtnBeta = new GridBagConstraints();
		gbc_rdbtnBeta.anchor = GridBagConstraints.WEST;
		gbc_rdbtnBeta.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnBeta.gridx = 0;
		gbc_rdbtnBeta.gridy = 10;
		add(rdbtnBeta, gbc_rdbtnBeta);
		rdbtnBeta.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				actionsTurn2ThisLayout(null);
				spinnerBetaFactor.setEnabled(true);
			}
		});
		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(rdbtnAlpha);
		buttonGroup2.add(rdbtnBeta);

		spinnerBetaFactor = new JSpinner();
		spinnerBetaFactor.setFont(globalFont);
		spinnerBetaFactor.setToolTipText("The beta factor. Unit is 0.01!");
		spinnerBetaFactor.setModel(new SpinnerNumberModel(100, 100, 500, 2));
		GridBagConstraints gbc_spinnerBetaFactor = new GridBagConstraints();
		gbc_spinnerBetaFactor.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerBetaFactor.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerBetaFactor.gridx = 2;
		gbc_spinnerBetaFactor.gridy = 10;
		add(spinnerBetaFactor, gbc_spinnerBetaFactor);
		spinnerBetaFactor.setEnabled(false);
		spinnerBetaFactor.addChangeListener(e -> {
			if (rdbtnBeta.isSelected()) {
				int value = (int) spinnerBetaFactor.getValue();
				controller.getTreeLayoutProperties().getSprialLayoutPropertiy().setBetaFactor(0.01 * value);
				controller.reCalculateTreeLayoutAndRefreshViewPort();
			}
		});

		// 初始状态
		sliderRootLength.setEnabled(false);
		spinnerRootTipLength.setEnabled(false);
	}

	@Override
	public void actionsTurn2ThisLayout(TreeLayoutEnum conPanl) {
		PhylogeneticTreePanel selectedPanel = controller.getSelectedPhylogeneticTreePanel();
		if (rdbtnPhylogram.isSelected()) {
			if (rdbtnAlpha.isSelected()) {
				selectedPanel
						.updateViewAccording2TreeLayout(TreeLayout.SPRIAL_ALPHA_PHYLO);
			} else {
				selectedPanel
						.updateViewAccording2TreeLayout(TreeLayout.SPRIAL_BETA_PHYLO);
			}
		} else {
			if (rdbtnAlpha.isSelected()) {
				selectedPanel
						.updateViewAccording2TreeLayout(TreeLayout.SPRIAL_ALPHA_CLADO);
			} else {
				selectedPanel
						.updateViewAccording2TreeLayout(TreeLayout.SPRIAL_BETA_CLADO);
			}
		}

		boolean showRoot = controller.getTreeLayoutProperties().isShowRoot();
		checkBoxShowOutgroup.setSelected(showRoot);
	
	}

}
