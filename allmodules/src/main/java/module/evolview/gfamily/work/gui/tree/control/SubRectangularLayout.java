package module.evolview.gfamily.work.gui.tree.control;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import egps2.frame.gui.comp.toggle.toggle.ToggleAdapter;
import egps2.frame.gui.comp.toggle.toggle.ToggleButton;
import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.phylotree.visualization.graphics.struct.TreeLayout;
import module.evolview.gfamily.work.model.enums.TreeLayoutEnum;

@SuppressWarnings("serial")
public class SubRectangularLayout extends BaseCtrlPanel implements Turn2ThisLayoutPanel, ItemListener {
	protected JSlider rootLengthSlider;
	protected JSpinner rootLengthSpinner;
	protected ChangeListener rootLengthSpinnerChangeListener;
	private JSlider curvatureSlider;
	private JSpinner curvatureSpinner;
	private ChangeListener curvatureSpinnerChangeListener;
	private JCheckBox chckbxAlignToTip;

	private JRadioButton rdbtnPhylogram;
	private JRadioButton rdbtnClado2tip;
	private JRadioButton rdbtnCladoWithEqual;
	protected ToggleButton checkBoxShowRoot;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnRectHalfCircularLayout;

	/**
	 * Create the panel.
	 *
	 * @param layoutProp
	 */
	public SubRectangularLayout() {
		setPanel();

	}

	protected void setPanel() {
//		setBorder(new EmptyBorder(0, 10, 0, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
//		gridBagLayout.columnWidths = new int[] { 100, 0, 0 };
//		gridBagLayout.rowHeights = new int[] { 29, 29, 29, 0, 0, 29, 29, 0, 0 };
//		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0 };
//		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		ButtonGroup buttonGroup = new ButtonGroup();
		rdbtnPhylogram = new JRadioButton("Phylogram");
		rdbtnPhylogram.setFont(globalFont);
		rdbtnPhylogram.setFocusable(false);
		rdbtnPhylogram.setSelected(true);
		GridBagConstraints gbc_rdbtnPhylogram = new GridBagConstraints();
		gbc_rdbtnPhylogram.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPhylogram.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnPhylogram.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPhylogram.gridx = 0;
		gbc_rdbtnPhylogram.gridy = 0;
		add(rdbtnPhylogram, gbc_rdbtnPhylogram);
		buttonGroup.add(rdbtnPhylogram);
		rdbtnPhylogram.addItemListener(e -> {
			if (rdbtnPhylogram.isSelected()) {
				actionsTurn2ThisLayout(TreeLayoutEnum.RECTANGULAR_LAYOUT);
			} else {
				chckbxAlignToTip.setEnabled(false);
			}
		});

		chckbxAlignToTip = new JCheckBox("Align");
		chckbxAlignToTip.setFocusable(false);
		chckbxAlignToTip.setFont(globalFont);
		GridBagConstraints gbc_chckbxAlignToTip = new GridBagConstraints();
		gbc_chckbxAlignToTip.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxAlignToTip.gridx = 2;
		gbc_chckbxAlignToTip.gridy = 0;
		add(chckbxAlignToTip, gbc_chckbxAlignToTip);
		chckbxAlignToTip.addItemListener(e -> {
			actionsTurn2ThisLayout(TreeLayoutEnum.RECTANGULAR_LAYOUT);
		});

		rdbtnClado2tip = new JRadioButton("Cladogram align to tip");
		rdbtnClado2tip.setFont(globalFont);
		rdbtnClado2tip.setFocusable(false);
		GridBagConstraints gbc_rdbtnClado2tip = new GridBagConstraints();
		gbc_rdbtnClado2tip.anchor = GridBagConstraints.WEST;
		gbc_rdbtnClado2tip.gridwidth = 3;
		gbc_rdbtnClado2tip.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnClado2tip.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnClado2tip.gridx = 0;
		gbc_rdbtnClado2tip.gridy = 1;
		add(rdbtnClado2tip, gbc_rdbtnClado2tip);
		buttonGroup.add(rdbtnClado2tip);
		rdbtnClado2tip.addItemListener(this);

		rdbtnCladoWithEqual = new JRadioButton("Cladogram with equal length");
		rdbtnCladoWithEqual.setFont(globalFont);
		rdbtnCladoWithEqual.setFocusable(false);
		GridBagConstraints gbc_rdbtnCladoWithEqual = new GridBagConstraints();
		gbc_rdbtnCladoWithEqual.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCladoWithEqual.gridwidth = 3;
		gbc_rdbtnCladoWithEqual.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnCladoWithEqual.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnCladoWithEqual.gridx = 0;
		gbc_rdbtnCladoWithEqual.gridy = 2;
		add(rdbtnCladoWithEqual, gbc_rdbtnCladoWithEqual);
		buttonGroup.add(rdbtnCladoWithEqual);
		rdbtnCladoWithEqual.addItemListener(this);

		curvatureSlider = new JSlider();
		curvatureSlider.setFocusable(false);
		curvatureSlider.setValue(0);
		curvatureSlider.setMaximum(100);
		curvatureSlider.setPreferredSize(new Dimension(100, 23));

		rdbtnNewRadioButton = new JRadioButton("Inner Y Axis");
		rdbtnNewRadioButton.setFont(globalFont);
		rdbtnNewRadioButton.setToolTipText(
				"See the  Felsenstein, J. (2004) *Drawing Trees*, in Inferring Phylogenies. Sinauer Assoc., pp 573-584");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton.gridwidth = 3;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 3;
		add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		rdbtnNewRadioButton.addItemListener(this);
		buttonGroup.add(rdbtnNewRadioButton);

		rdbtnRectHalfCircularLayout = new JRadioButton("Rectangular half circle");
		rdbtnRectHalfCircularLayout.setFont(globalFont);
		GridBagConstraints gbc_rdbtnRectHalfCircularLayout = new GridBagConstraints();
		gbc_rdbtnRectHalfCircularLayout.anchor = GridBagConstraints.WEST;
		gbc_rdbtnRectHalfCircularLayout.gridwidth = 3;
		gbc_rdbtnRectHalfCircularLayout.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnRectHalfCircularLayout.gridx = 0;
		gbc_rdbtnRectHalfCircularLayout.gridy = 4;
		add(rdbtnRectHalfCircularLayout, gbc_rdbtnRectHalfCircularLayout);
		rdbtnRectHalfCircularLayout.addItemListener(this);
		buttonGroup.add(rdbtnRectHalfCircularLayout);

		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 5;
		add(separator_1, gbc_separator_1);

		JLabel lblNewLabel = new JLabel("Curvature ");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 15, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 6;
		add(lblNewLabel, gbc_lblNewLabel);
		GridBagConstraints gbc_curvatureSlider = new GridBagConstraints();
		gbc_curvatureSlider.gridwidth = 2;
		gbc_curvatureSlider.insets = new Insets(0, 0, 5, 5);
		gbc_curvatureSlider.gridx = 0;
		gbc_curvatureSlider.gridy = 7;
		add(curvatureSlider, gbc_curvatureSlider);

		curvatureSpinner = new JSpinner();
		curvatureSpinner.setFont(globalFont);
		curvatureSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		GridBagConstraints gbc_curvatureSpinner = new GridBagConstraints();
		gbc_curvatureSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_curvatureSpinner.gridx = 2;
		gbc_curvatureSpinner.gridy = 7;
		add(curvatureSpinner, gbc_curvatureSpinner);
		curvatureSpinnerChangeListener = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			curvatureSlider.setValue(value);
			controller.getTreeLayoutProperties().getRectangularLayoutPropertiy().setCurvature(value);
			controller.refreshPhylogeneticTree();
		};
		curvatureSpinner.addChangeListener(curvatureSpinnerChangeListener);
		curvatureSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int value = curvatureSlider.getValue();
				curvatureSpinner.setValue(value);
			}
		});

		final int theMaxValueOfRootLength = 200;
		
		Dimension dimension = new Dimension(120, 25);
		checkBoxShowRoot = new ToggleButton();
		checkBoxShowRoot.setIconDrawWidth(40);
		checkBoxShowRoot.setText("Show root");
		
		checkBoxShowRoot.setBorder(BorderFactory.createEmptyBorder());
		checkBoxShowRoot.setPreferredSize(dimension);
		checkBoxShowRoot.setFocusable(false);
		checkBoxShowRoot.setToolTipText("Whether show root in the graph.");
		checkBoxShowRoot.setFont(globalFont);
		GridBagConstraints gbc_checkBoxShowOutgroup = new GridBagConstraints();
		gbc_checkBoxShowOutgroup.gridwidth = 3;
		gbc_checkBoxShowOutgroup.anchor = GridBagConstraints.WEST;
		gbc_checkBoxShowOutgroup.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxShowOutgroup.gridx = 0;
		gbc_checkBoxShowOutgroup.gridy = 8;
		add(checkBoxShowRoot, gbc_checkBoxShowOutgroup);
		
		checkBoxShowRoot.addEventToggleSelected(new ToggleAdapter() {
			@Override
			public void onSelected(boolean selected) {
				controller.getTreeLayoutProperties().setShowRoot(selected);
				controller.refreshPhylogeneticTree();

				rootLengthSlider.setEnabled(selected);
				rootLengthSpinner.setEnabled(selected);
			}

			
		});

		rootLengthSlider = new JSlider(0,theMaxValueOfRootLength, 20);
		rootLengthSlider.setFocusable(false);
		rootLengthSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int value = rootLengthSlider.getValue();
				rootLengthSpinner.setValue(value);
			}
		});

		rootLengthSlider.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_rootLengthSlider = new GridBagConstraints();
		gbc_rootLengthSlider.insets = new Insets(0, 0, 0, 5);
		gbc_rootLengthSlider.gridwidth = 2;
		gbc_rootLengthSlider.gridx = 0;
		gbc_rootLengthSlider.gridy = 9;

		add(rootLengthSlider, gbc_rootLengthSlider);

		rootLengthSpinner = new JSpinner();
		rootLengthSpinner.setFont(globalFont);
		rootLengthSpinner.setModel(new SpinnerNumberModel(20, 0, theMaxValueOfRootLength, 1));
		GridBagConstraints gbc_rootLengthSpinner = new GridBagConstraints();
		gbc_rootLengthSpinner.gridx = 2;
		gbc_rootLengthSpinner.gridy = 9;

		add(rootLengthSpinner, gbc_rootLengthSpinner);

		rootLengthSpinnerChangeListener = e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			rootLengthSlider.setValue(value);
			controller.getTreeLayoutProperties().setRootTipLength(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		};

		rootLengthSpinner.addChangeListener(rootLengthSpinnerChangeListener);

		rootLengthSlider.setEnabled(false);
		rootLengthSpinner.setEnabled(false);
	}

	@Override
	public void actionsTurn2ThisLayout(TreeLayoutEnum conPanl) {
		PhylogeneticTreePanel selectedPhylogeneticTreePanel = controller.getSelectedPhylogeneticTreePanel();
		
		if (rdbtnPhylogram.isSelected()) {
			if (chckbxAlignToTip.isSelected()) {
				selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RECT_PHYLO_ALIGN2TIP);
			} else {
				selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RECT_PHYLO_LEFT);
			}
			chckbxAlignToTip.setEnabled(true);
		} else if (rdbtnClado2tip.isSelected()) {
			selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RECT_CLADO_ALIGNED_LEFT);
			chckbxAlignToTip.setEnabled(false);
		} else if (rdbtnCladoWithEqual.isSelected()) {
			selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RECT_CLADO_EQUAL_LEFT);
			chckbxAlignToTip.setEnabled(false);
		} else if (rdbtnRectHalfCircularLayout.isSelected()) {
			selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RECT_HALF_CIRCLE);
			chckbxAlignToTip.setEnabled(false);
		} else {
			// should not happen
			selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RECT_INNER_YAXIS);
			chckbxAlignToTip.setEnabled(false);
		}

		boolean showRoot = controller.getTreeLayoutProperties().isShowRoot();
		checkBoxShowRoot.setSelected(showRoot);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			actionsTurn2ThisLayout(TreeLayoutEnum.RECTANGULAR_LAYOUT);
		}
	}

}
