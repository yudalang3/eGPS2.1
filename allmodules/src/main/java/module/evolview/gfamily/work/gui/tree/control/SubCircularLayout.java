package module.evolview.gfamily.work.gui.tree.control;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;

@SuppressWarnings("serial")
public class SubCircularLayout extends BaseCtrlPanel implements Turn2ThisLayoutPanel,ItemListener {
	private JCheckBox chckbxAlignToTip;
	private JSpinner spinnerRootLength;
	private JSlider sliderRootLength;
	private ChangeListener spinnerRootLengthChangeListener;
	private JSpinner spinnerStartDegree;
	private ChangeListener spinnerStartDegreeChangeListener;
	private ChangeListener spinnerExtentDegreeChangeListener;
	private JSpinner spinnerExtentDegree;
	private JRadioButton rdbtnPhylogram;
	private JRadioButton rdbtnCladogramAlign2tip;
	private JRadioButton rdbtnCladogramWithEqualLength;
	private JRadioButton rdbtnInnerCladogram;
	private JCheckBox checkBoxShowOutgroup;

	/**
	 * @param layoutProp
	 */
	public SubCircularLayout() {

		ButtonGroup buttonGroup = new ButtonGroup();
		GridBagLayout gridBagLayout = new GridBagLayout();
//		gridBagLayout.columnWidths = new int[] { 99, 0, 0 };
//		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
//		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
//				Double.MIN_VALUE };
		setLayout(gridBagLayout);
		rdbtnPhylogram = new JRadioButton("Phylogram");
		rdbtnPhylogram.setFont(globalFont);
		rdbtnPhylogram.setFocusable(false);
		GridBagConstraints gbc_rdbtnPhylogram = new GridBagConstraints();
		gbc_rdbtnPhylogram.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPhylogram.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPhylogram.gridx = 0;
		gbc_rdbtnPhylogram.gridy = 0;
		add(rdbtnPhylogram, gbc_rdbtnPhylogram);
		rdbtnPhylogram.setSelected(true);

		rdbtnPhylogram.addItemListener(this);
		buttonGroup.add(rdbtnPhylogram);

		chckbxAlignToTip = new JCheckBox("Align");
		chckbxAlignToTip.setFocusable(false);
		chckbxAlignToTip.setFont(globalFont);
		GridBagConstraints gbc_chckbxAlignToTip = new GridBagConstraints();
		gbc_chckbxAlignToTip.anchor = GridBagConstraints.WEST;
		gbc_chckbxAlignToTip.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxAlignToTip.gridx = 1;
		gbc_chckbxAlignToTip.gridy = 0;
		add(chckbxAlignToTip, gbc_chckbxAlignToTip);
		chckbxAlignToTip.addItemListener(e -> {
			actionsTurn2ThisLayout(null);
		});

		rdbtnCladogramWithEqualLength = new JRadioButton("Cladogram equal length");
		rdbtnCladogramWithEqualLength.setFont(globalFont);
		rdbtnCladogramWithEqualLength.setFocusable(false);
		GridBagConstraints gbc_rdbtnCladogramWithEqualLength = new GridBagConstraints();
		gbc_rdbtnCladogramWithEqualLength.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCladogramWithEqualLength.gridwidth = 2;
		gbc_rdbtnCladogramWithEqualLength.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnCladogramWithEqualLength.gridx = 0;
		gbc_rdbtnCladogramWithEqualLength.gridy = 1;
		add(rdbtnCladogramWithEqualLength, gbc_rdbtnCladogramWithEqualLength);
		rdbtnCladogramWithEqualLength.addItemListener(this);
		buttonGroup.add(rdbtnCladogramWithEqualLength);
		
		
		rdbtnCladogramAlign2tip = new JRadioButton("Cladogram align to tip");
		rdbtnCladogramAlign2tip.setFont(globalFont);
		rdbtnCladogramAlign2tip.setFocusable(false);
		GridBagConstraints gbc_rdbtnCladogramAlign2tip = new GridBagConstraints();
		gbc_rdbtnCladogramAlign2tip.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCladogramAlign2tip.gridwidth = 2;
		gbc_rdbtnCladogramAlign2tip.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnCladogramAlign2tip.gridx = 0;
		gbc_rdbtnCladogramAlign2tip.gridy = 2;
		add(rdbtnCladogramAlign2tip, gbc_rdbtnCladogramAlign2tip);
		rdbtnCladogramAlign2tip.addItemListener(this);
		buttonGroup.add(rdbtnCladogramAlign2tip);

		rdbtnInnerCladogram = new JRadioButton("Inner cladogram");
		rdbtnInnerCladogram.setFont(globalFont);
		rdbtnInnerCladogram.setFocusable(false);
		GridBagConstraints gbc_rdbtnInnerCladogram = new GridBagConstraints();
		gbc_rdbtnInnerCladogram.anchor = GridBagConstraints.WEST;
		gbc_rdbtnInnerCladogram.gridwidth = 2;
		gbc_rdbtnInnerCladogram.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnInnerCladogram.gridx = 0;
		gbc_rdbtnInnerCladogram.gridy = 3;
		add(rdbtnInnerCladogram, gbc_rdbtnInnerCladogram);
		rdbtnInnerCladogram.addItemListener(this);
		buttonGroup.add(rdbtnInnerCladogram);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 4;
		add(separator, gbc_separator);

		checkBoxShowOutgroup = new JCheckBox("Show root :");
		checkBoxShowOutgroup.setSelected(false);
		checkBoxShowOutgroup.setFocusable(false);
		checkBoxShowOutgroup.setFont(globalFont);
		GridBagConstraints gbc_checkBoxShowOutgroup = new GridBagConstraints();
		gbc_checkBoxShowOutgroup.anchor = GridBagConstraints.WEST;
		gbc_checkBoxShowOutgroup.gridwidth = 2;
		gbc_checkBoxShowOutgroup.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxShowOutgroup.gridx = 0;
		gbc_checkBoxShowOutgroup.gridy = 5;
		add(checkBoxShowOutgroup, gbc_checkBoxShowOutgroup);
		checkBoxShowOutgroup.addItemListener(e -> {
			boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			TreeLayoutProperties treeLayoutProperties = controller.getTreeLayoutProperties();
			treeLayoutProperties.setShowRoot(selected);
			sliderRootLength.setEnabled(selected);
			spinnerRootLength.setEnabled(selected);
			controller.refreshPhylogeneticTree();
		});
		final int theMaxValueOfRootLength = 200;
		sliderRootLength = new JSlider(0,theMaxValueOfRootLength, 15);
		sliderRootLength.setFocusable(false);
		sliderRootLength.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_sliderRootLength = new GridBagConstraints();
		gbc_sliderRootLength.anchor = GridBagConstraints.WEST;
		gbc_sliderRootLength.insets = new Insets(0, 0, 5, 5);
		gbc_sliderRootLength.gridx = 0;
		gbc_sliderRootLength.gridy = 6;
		add(sliderRootLength, gbc_sliderRootLength);
		sliderRootLength.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JSlider source = (JSlider) e.getSource();
				int value = source.getValue();
				spinnerRootLength.setValue(value);
			}
		});
		
		spinnerRootLength = new JSpinner();
		spinnerRootLength.setFont(globalFont);
		spinnerRootLength.setModel(new SpinnerNumberModel(15, 0, theMaxValueOfRootLength, 1));
		GridBagConstraints gbc_spinnerRootLength = new GridBagConstraints();
		gbc_spinnerRootLength.anchor = GridBagConstraints.WEST;
		gbc_spinnerRootLength.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerRootLength.gridx = 1;
		gbc_spinnerRootLength.gridy = 6;
		add(spinnerRootLength, gbc_spinnerRootLength);
		spinnerRootLengthChangeListener = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderRootLength.setValue(value);
			controller.getTreeLayoutProperties().setRootTipLength(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		};
		spinnerRootLength.addChangeListener(spinnerRootLengthChangeListener);

		JLabel lblStartDegree = new JLabel("Start degree :");
		lblStartDegree.setFont(globalFont);
		GridBagConstraints gbc_lblStartDegree = new GridBagConstraints();
		gbc_lblStartDegree.gridwidth = 2;
		gbc_lblStartDegree.insets = new Insets(0, 0, 5, 0);
		gbc_lblStartDegree.gridx = 0;
		gbc_lblStartDegree.gridy = 7;
		add(lblStartDegree, gbc_lblStartDegree);

		JSlider sliderStartDegree = new JSlider();
		sliderStartDegree.setFocusable(false);
		sliderStartDegree.setValue(285);
		sliderStartDegree.setMaximum(360);
		sliderStartDegree.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_sliderStartDegree = new GridBagConstraints();
		gbc_sliderStartDegree.anchor = GridBagConstraints.WEST;
		gbc_sliderStartDegree.insets = new Insets(0, 0, 5, 5);
		gbc_sliderStartDegree.gridx = 0;
		gbc_sliderStartDegree.gridy = 8;
		add(sliderStartDegree, gbc_sliderStartDegree);
		sliderStartDegree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JSlider source = (JSlider) e.getSource();
				int value = source.getValue();
				spinnerStartDegree.setValue(value);
			}
		});

		spinnerStartDegree = new JSpinner();
		spinnerStartDegree.setFont(globalFont);
		spinnerStartDegree.setModel(new SpinnerNumberModel(285, 0, 360, 1));
		GridBagConstraints gbc_spinnerStartDegree = new GridBagConstraints();
		gbc_spinnerStartDegree.anchor = GridBagConstraints.WEST;
		gbc_spinnerStartDegree.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerStartDegree.gridx = 1;
		gbc_spinnerStartDegree.gridy = 8;
		add(spinnerStartDegree, gbc_spinnerStartDegree);
		spinnerStartDegreeChangeListener = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderStartDegree.setValue(value);
			controller.getTreeLayoutProperties().getCircularLayoutPropertiy().setGlobalStartDegree(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		};
		spinnerStartDegree.addChangeListener(spinnerStartDegreeChangeListener);

		JLabel lblNewLabel = new JLabel("Extent degree :");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 9;
		add(lblNewLabel, gbc_lblNewLabel);

		JSlider sliderExtentDegree = new JSlider();
		sliderExtentDegree.setFocusable(false);
		sliderExtentDegree.setMaximum(360);
		sliderExtentDegree.setValue(330);
		sliderExtentDegree.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_sliderExtentDegree = new GridBagConstraints();
		gbc_sliderExtentDegree.anchor = GridBagConstraints.WEST;
		gbc_sliderExtentDegree.insets = new Insets(0, 0, 0, 5);
		gbc_sliderExtentDegree.gridx = 0;
		gbc_sliderExtentDegree.gridy = 10;
		add(sliderExtentDegree, gbc_sliderExtentDegree);
		sliderExtentDegree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JSlider source = (JSlider) e.getSource();
				int value = source.getValue();
				spinnerExtentDegree.setValue(value);
			}
		});

		spinnerExtentDegree = new JSpinner();
		spinnerExtentDegree.setFont(globalFont);
		spinnerExtentDegree.setModel(new SpinnerNumberModel(360, 0, 360, 1));
		GridBagConstraints gbc_spinnerExtentDegree = new GridBagConstraints();
		gbc_spinnerExtentDegree.anchor = GridBagConstraints.WEST;
		gbc_spinnerExtentDegree.gridx = 1;
		gbc_spinnerExtentDegree.gridy = 10;
		add(spinnerExtentDegree, gbc_spinnerExtentDegree);

		spinnerExtentDegreeChangeListener = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderExtentDegree.setValue(value);
			controller.getTreeLayoutProperties().getCircularLayoutPropertiy().setGlobalExtendDegree(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		};
		spinnerExtentDegree.addChangeListener(spinnerExtentDegreeChangeListener);

		/**
		 * 初始状态
		 */
		sliderRootLength.setEnabled(false);
		spinnerRootLength.setEnabled(false);

	}

	@Override
	public void actionsTurn2ThisLayout(TreeLayoutEnum layoutInfo) {

		PhylogeneticTreePanel selectedTreePanel = controller.getSelectedPhylogeneticTreePanel();
		if (rdbtnPhylogram.isSelected()) {
			if (chckbxAlignToTip.isSelected()) {
				selectedTreePanel.updateViewAccording2TreeLayout(TreeLayout.OUTER_CIR_PHYLO_ALIGNED);
			} else {
				selectedTreePanel.updateViewAccording2TreeLayout(TreeLayout.OUTER_CIR_PHYLO);
			}
			chckbxAlignToTip.setEnabled(true);
		} else if (rdbtnCladogramAlign2tip.isSelected()) {
			selectedTreePanel.updateViewAccording2TreeLayout(TreeLayout.OUTER_CIR_CLADO_ALIGNED);
			chckbxAlignToTip.setEnabled(false);
		} else if (rdbtnCladogramWithEqualLength.isSelected()) {
			selectedTreePanel.updateViewAccording2TreeLayout(TreeLayout.OUTER_CIR_CLADO_EQUAL);
			chckbxAlignToTip.setEnabled(false);
		} else {
			// inner circular layout!
			selectedTreePanel.updateViewAccording2TreeLayout(TreeLayout.INNER_CIR_CLADO);
			chckbxAlignToTip.setEnabled(false);
		}

		TreeLayoutProperties treeLayoutProperties = controller.getTreeLayoutProperties();
		boolean showRoot = treeLayoutProperties.isShowRoot();
		checkBoxShowOutgroup.setSelected(showRoot);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			actionsTurn2ThisLayout(null);
		}
	}

}
