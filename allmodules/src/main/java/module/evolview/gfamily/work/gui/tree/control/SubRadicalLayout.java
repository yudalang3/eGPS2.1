package module.evolview.gfamily.work.gui.tree.control;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.phylotree.visualization.graphics.struct.TreeLayout;
import module.evolview.model.enums.TreeLayoutEnum;

@SuppressWarnings("serial")
public class SubRadicalLayout extends BaseCtrlPanel implements Turn2ThisLayoutPanel {

	private JRadioButton rdbtnPhylogram;
	private JRadioButton rdbtnCladogram;

	private JRadioButton rdbtnEqualDaylightPhylogram;
	private JRadioButton rdbtnEqualDaylightCladogram;

	public SubRadicalLayout() {
//        setBorder(new EmptyBorder(0, 10, 0, 10));

		GridBagLayout gridBagLayout = new GridBagLayout();
//        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
//        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
//        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
//        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		ButtonGroup buttonGroup = new ButtonGroup();

		rdbtnPhylogram = new JRadioButton("Equal-angle Phylogram");
		rdbtnPhylogram.setFont(globalFont);
		rdbtnPhylogram.setFocusable(false);
		GridBagConstraints gbc_rdbtnPhylogram = new GridBagConstraints();
		gbc_rdbtnPhylogram.anchor = GridBagConstraints.WEST;
		gbc_rdbtnPhylogram.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPhylogram.gridx = 0;
		gbc_rdbtnPhylogram.gridy = 0;
		add(rdbtnPhylogram, gbc_rdbtnPhylogram);
		rdbtnPhylogram.setSelected(true);
		rdbtnPhylogram.addItemListener(e -> {
			if (rdbtnPhylogram.isSelected()) {
				doUpdateAction();
			}
		});

		buttonGroup.add(rdbtnPhylogram);

		{
			rdbtnCladogram = new JRadioButton("Equal-angle Cladogram");
			rdbtnCladogram.setFocusable(false);
			rdbtnCladogram.setFont(globalFont);
			GridBagConstraints gbc_rdbtnCladogram = new GridBagConstraints();
			gbc_rdbtnCladogram.anchor = GridBagConstraints.WEST;
			gbc_rdbtnCladogram.insets = new Insets(0, 0, 5, 5);
			gbc_rdbtnCladogram.gridx = 0;
			gbc_rdbtnCladogram.gridy = 1;
			add(rdbtnCladogram, gbc_rdbtnCladogram);
			rdbtnCladogram.addItemListener(e -> {
				if (rdbtnCladogram.isSelected()) {
					doUpdateAction();
				}
			});
			buttonGroup.add(rdbtnCladogram);
		}

//		{
//			rdbtnEqualDaylightPhylogram = new JRadioButton("Equal-daylight Phylogram");
//			rdbtnEqualDaylightPhylogram.setFocusable(false);
//			rdbtnEqualDaylightPhylogram.setFont(globalFont);
//			GridBagConstraints gbc_rdbtnCladogram = new GridBagConstraints();
//			gbc_rdbtnCladogram.anchor = GridBagConstraints.WEST;
//			gbc_rdbtnCladogram.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnCladogram.gridx = 0;
//			gbc_rdbtnCladogram.gridy = 2;
//			add(rdbtnEqualDaylightPhylogram, gbc_rdbtnCladogram);
//			rdbtnEqualDaylightPhylogram.addItemListener(e -> {
//				if (rdbtnEqualDaylightPhylogram.isSelected()) {
//					doUpdateAction();
//				}
//			});
//			buttonGroup.add(rdbtnEqualDaylightPhylogram);
//		}
//
//		{
//			rdbtnEqualDaylightCladogram = new JRadioButton("Equal-daylight Cladogram");
//			rdbtnEqualDaylightCladogram.setFocusable(false);
//			rdbtnEqualDaylightCladogram.setFont(globalFont);
//			GridBagConstraints gbc_rdbtnCladogram = new GridBagConstraints();
//			gbc_rdbtnCladogram.anchor = GridBagConstraints.WEST;
//			gbc_rdbtnCladogram.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnCladogram.gridx = 0;
//			gbc_rdbtnCladogram.gridy = 3;
//			add(rdbtnEqualDaylightCladogram, gbc_rdbtnCladogram);
//			rdbtnEqualDaylightCladogram.addItemListener(e -> {
//				if (rdbtnEqualDaylightCladogram.isSelected()) {
//					doUpdateAction();
//				}
//			});
//			buttonGroup.add(rdbtnEqualDaylightCladogram);
//		}

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 4;
		add(separator, gbc_separator);

		JLabel lblRotation = new JLabel("Rotation");
		lblRotation.setFont(globalFont);
		GridBagConstraints gbc_lblRotation = new GridBagConstraints();
		gbc_lblRotation.anchor = GridBagConstraints.WEST;
		gbc_lblRotation.insets = new Insets(0, 0, 5, 5);
		gbc_lblRotation.gridx = 0;
		gbc_lblRotation.gridy = 5;
		add(lblRotation, gbc_lblRotation);

		JSlider slider = new JSlider();
		slider.setPreferredSize(new Dimension(100, 23));
		slider.setValue(0);
		slider.setMaximum(360);
		slider.setFocusable(false);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.anchor = GridBagConstraints.WEST;
		gbc_slider.insets = new Insets(0, 0, 0, 5);
		gbc_slider.gridx = 0;
		gbc_slider.gridy = 6;
		add(slider, gbc_slider);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(0, 0, 360, 1));
		spinner.setFont(globalFont);
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 6;
		add(spinner, gbc_spinner);

		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int value = slider.getValue();
				spinner.setValue(value);
			}
		});
		spinner.addChangeListener(e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			slider.setValue(value);
			controller.getTreeLayoutProperties().setRadicalLayoutRotationDeg(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		});

	}

	@Override
	public void actionsTurn2ThisLayout(TreeLayoutEnum conPanl) {
		doUpdateAction();

	}

	private void doUpdateAction() {
		PhylogeneticTreePanel selectedPhylogeneticTreePanel = controller.getSelectedPhylogeneticTreePanel();
		if (rdbtnPhylogram.isSelected()) {
			selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RADIAL_PHYLO);
		} else if (rdbtnEqualDaylightPhylogram.isSelected()) {
			selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RADIAL_EQUAL_DAYLIGHT_PHYLO);
		} else if (rdbtnEqualDaylightCladogram.isSelected()) {
			selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RADIAL_EQUAL_DAYLIGHT_CLADO);
		} else {
			selectedPhylogeneticTreePanel.updateViewAccording2TreeLayout(TreeLayout.RADIAL_CLADO);
		}

	}

}
