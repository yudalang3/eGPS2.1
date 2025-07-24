package module.evolview.gfamily.work.gui.tree.control;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.phylotree.visualization.graphics.struct.SlopeLayoutProperty;

@SuppressWarnings("serial")
public class SubSlopeLayout extends BaseCtrlPanel {
	private JSpinner spinnerOfLeafLocation;
	private JSlider sliderOfLeafLocation;
	private JLabel lblRotation;
	private JSlider sliderOfRotation;
	private JSpinner spinnerRotation;
	private JSeparator separator;
	private JSpinner spinnerOfMargin;

	public SubSlopeLayout() {
//		setBorder(new EmptyBorder(15, 15, 15, 15));

		GridBagLayout gridBagLayout = new GridBagLayout();
//		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
//		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
//		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0 };
//		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblLeafLocationRatio = new JLabel("Leaf location ratio");
		lblLeafLocationRatio.setFont(globalFont);
		GridBagConstraints gbc_lblLeafLocationRatio = new GridBagConstraints();
		gbc_lblLeafLocationRatio.insets = new Insets(0, 0, 5, 5);
		gbc_lblLeafLocationRatio.gridx = 1;
		gbc_lblLeafLocationRatio.gridy = 0;
		add(lblLeafLocationRatio, gbc_lblLeafLocationRatio);

		sliderOfLeafLocation = new JSlider();
		sliderOfLeafLocation.setFocusable(false);
		sliderOfLeafLocation.setValue(100);
		sliderOfLeafLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int value = sliderOfLeafLocation.getValue();
				spinnerOfLeafLocation.setValue(value);
			}
		});
		sliderOfLeafLocation.setPreferredSize(new Dimension(100, 23));
		GridBagConstraints gbc_sliderOfLeafLocation = new GridBagConstraints();
		gbc_sliderOfLeafLocation.anchor = GridBagConstraints.WEST;
		gbc_sliderOfLeafLocation.insets = new Insets(0, 0, 5, 5);
		gbc_sliderOfLeafLocation.gridx = 1;
		gbc_sliderOfLeafLocation.gridy = 1;
		add(sliderOfLeafLocation, gbc_sliderOfLeafLocation);

		spinnerOfLeafLocation = new JSpinner();
		spinnerOfLeafLocation.setFont(globalFont);
		spinnerOfLeafLocation.setModel(new SpinnerNumberModel(80, 0, 100, 1));
		GridBagConstraints gbc_spinnerOfLeafLocation = new GridBagConstraints();
		gbc_spinnerOfLeafLocation.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerOfLeafLocation.gridx = 2;
		gbc_spinnerOfLeafLocation.gridy = 1;
		add(spinnerOfLeafLocation, gbc_spinnerOfLeafLocation);
		
		JLabel lblNewLabel = new JLabel("Blank margin ratio");
		lblNewLabel.setFont(globalFont);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 2;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JSlider sliderOfMargin = new JSlider();
		sliderOfMargin.setFont(globalFont);
		sliderOfMargin.setFocusable(false);
		sliderOfMargin.setValue(0);
		sliderOfMargin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int value = sliderOfMargin.getValue();
				spinnerOfMargin.setValue(value);
			}
		});
		sliderOfMargin.setPreferredSize(new Dimension(100, 23));
		
		
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 3;
		add(sliderOfMargin, gbc_lblNewLabel_1);
		
		spinnerOfMargin = new JSpinner();
		spinnerOfMargin.setFont(globalFont);
		spinnerOfMargin.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		GridBagConstraints gbc_spinnerOfMargin = new GridBagConstraints();
		gbc_spinnerOfMargin.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerOfMargin.gridx = 2;
		gbc_spinnerOfMargin.gridy = 3;
		add(spinnerOfMargin, gbc_spinnerOfMargin);
		
		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 4;
		add(separator, gbc_separator);

		lblRotation = new JLabel("Rotation");
		lblRotation.setFont(globalFont);
		GridBagConstraints gbc_lblRotation = new GridBagConstraints();
		gbc_lblRotation.anchor = GridBagConstraints.WEST;
		gbc_lblRotation.insets = new Insets(0, 0, 5, 5);
		gbc_lblRotation.gridx = 1;
		gbc_lblRotation.gridy = 5;
		add(lblRotation, gbc_lblRotation);

		sliderOfRotation = new JSlider();
		sliderOfRotation.setPreferredSize(new Dimension(100, 23));
		sliderOfRotation.setMaximum(360);
		sliderOfRotation.setValue(0);
		sliderOfRotation.setFocusable(false);
		GridBagConstraints gbc_sliderOfRotation = new GridBagConstraints();
		gbc_sliderOfRotation.anchor = GridBagConstraints.WEST;
		gbc_sliderOfRotation.insets = new Insets(0, 0, 0, 5);
		gbc_sliderOfRotation.gridx = 1;
		gbc_sliderOfRotation.gridy = 6;
		add(sliderOfRotation, gbc_sliderOfRotation);

		spinnerRotation = new JSpinner();
		spinnerRotation.setFont(globalFont);
		spinnerRotation.setModel(new SpinnerNumberModel(0, 0, 360, 1));
		GridBagConstraints gbc_spinnerRotation = new GridBagConstraints();
		gbc_spinnerRotation.gridx = 2;
		gbc_spinnerRotation.gridy = 6;
		add(spinnerRotation, gbc_spinnerRotation);

		sliderOfRotation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int value = sliderOfRotation.getValue();
				spinnerRotation.setValue(value);
			}
		});
		spinnerRotation.addChangeListener(e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderOfRotation.setValue(value);
			controller.getTreeLayoutProperties().getSlopeLayoutProperety().setSlopeLayoutRotationDeg(value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		});

		spinnerOfLeafLocation.addChangeListener(e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderOfLeafLocation.setValue(value);
			SlopeLayoutProperty slopeLayoutProperety = controller.getTreeLayoutProperties().getSlopeLayoutProperety();
			slopeLayoutProperety.setLeafLocationRightMostRatio(0.01 * value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		});
		spinnerOfMargin.addChangeListener(e -> {
			JSpinner source = (JSpinner) e.getSource();
			int value = (int) source.getValue();
			sliderOfMargin.setValue(value);
			
			
			SlopeLayoutProperty slopeLayoutProperety = controller.getTreeLayoutProperties().getSlopeLayoutProperety();
			slopeLayoutProperety.setBlankMarginRatio(0.01 * value);
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		});

	}

}
