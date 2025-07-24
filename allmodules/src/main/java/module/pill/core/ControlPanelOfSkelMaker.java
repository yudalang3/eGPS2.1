package module.pill.core;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import module.pill.images.ImageUtils;

@SuppressWarnings("serial")
public class ControlPanelOfSkelMaker extends JPanel {
	private DrawingPanelSkeletonMaker pickPanel;
	private SkeletonMaker locationPicker;
	private JSpinner spinner_pnode_oval_w;
	private JSpinner spinner_pnode_oval_h;
	private JToggleButton tglbtn_eraser;
	private ItemListener eraserItemListner;
	private JCheckBox tglbtn_auto;

	private DefaultComboBoxModel<String> comboBoxModel;
	GraphicsNodeShape defaultNodeShape;
	private GraphicsNodeShape currentNodeShape;
	private List<GraphicsNodeShape> allSupportedNodeshapes;
	private JComboBox<String> comboBox;
	private ItemListener comboBoxListener;
	private JRadioButton jrb_pnodes_custom;

	{
		defaultNodeShape = new GraphicsNodeShape();
		defaultNodeShape.name = "Diamond ";
		defaultNodeShape.height = 20;
		defaultNodeShape.width = 20;
		defaultNodeShape.xInteger = Arrays.asList(0, 10, 0, -10);
		defaultNodeShape.yInteger = Arrays.asList(-10, 0, 10, 0);
	}

	public ControlPanelOfSkelMaker() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Import data");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		JButton btn_load = new JButton("Load image from clipboard");
		Icon icon = ImageUtils.getIcon("clipboard-solid.png");
		btn_load.setIcon(icon);
		btn_load.setRequestFocusEnabled(false);

		btn_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pickPanel != null) {
					SwingUtilities.invokeLater(() -> {
						pickPanel.loadImage();
					});
				}
			}
		});
		GridBagConstraints gbc_btn_load = new GridBagConstraints();
		gbc_btn_load.anchor = GridBagConstraints.WEST;
		gbc_btn_load.gridwidth = 3;
		gbc_btn_load.insets = new Insets(0, 0, 5, 0);
		gbc_btn_load.gridx = 0;
		gbc_btn_load.gridy = 1;
		add(btn_load, gbc_btn_load);

		JLabel lblNewLabel_1 = new JLabel("Export");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		JButton btn_export = new JButton("Export R codes");
		btn_export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				locationPicker.exportAsRCodes(!tglbtn_auto.isSelected());
			}
		});
		GridBagConstraints gbc_btn_export = new GridBagConstraints();
		gbc_btn_export.anchor = GridBagConstraints.WEST;
		gbc_btn_export.gridwidth = 2;
		gbc_btn_export.insets = new Insets(0, 0, 5, 5);
		gbc_btn_export.gridx = 0;
		gbc_btn_export.gridy = 3;
		add(btn_export, gbc_btn_export);

		JSeparator separator = new JSeparator();
		separator.setBackground(Color.WHITE);
		separator.setForeground(Color.BLACK);
		separator.setVisible(true);

		tglbtn_auto = new JCheckBox("Direct copy");
		tglbtn_auto.setToolTipText("Do not show the Jialog, direct copy the codes to the clipboard.");
		GridBagConstraints gbc_tglbtn_auto = new GridBagConstraints();
		gbc_tglbtn_auto.anchor = GridBagConstraints.EAST;
		gbc_tglbtn_auto.insets = new Insets(0, 0, 5, 0);
		gbc_tglbtn_auto.gridx = 2;
		gbc_tglbtn_auto.gridy = 3;
		add(tglbtn_auto, gbc_tglbtn_auto);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 3;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 4;
		add(separator, gbc_separator);

		JLabel lblNewLabel_2 = new JLabel("Pathway components");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.gridwidth = 3;
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 5;
		add(lblNewLabel_2, gbc_lblNewLabel_2);

		setBackground(Color.WHITE);

		ButtonGroup buttonGroup = new ButtonGroup();

		JLabel lblNewLabel_2_1 = new JLabel("pNodes :");
		GridBagConstraints gbc_lblNewLabel_2_1 = new GridBagConstraints();
		gbc_lblNewLabel_2_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2_1.gridx = 0;
		gbc_lblNewLabel_2_1.gridy = 6;
		add(lblNewLabel_2_1, gbc_lblNewLabel_2_1);

		JLabel lblNewLabel_6 = new JLabel("width");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 1;
		gbc_lblNewLabel_6.gridy = 6;
		add(lblNewLabel_6, gbc_lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("height");
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_7.gridx = 2;
		gbc_lblNewLabel_7.gridy = 6;
		add(lblNewLabel_7, gbc_lblNewLabel_7);

		spinner_pnode_oval_w = new JSpinner();
		String spinnerTooltip = "If you entery the number by keyboard.\r\nPlease do not forget to press the \r\n\"Enter\" key.";
		spinner_pnode_oval_w.setToolTipText(spinnerTooltip);
		spinner_pnode_oval_w.setModel(new SpinnerNumberModel(GraphicProperties.DEFAULT_SIZE, 1, 120, 1));
		GridBagConstraints gbc_spinner_pnode_oval_w = new GridBagConstraints();
		gbc_spinner_pnode_oval_w.anchor = GridBagConstraints.EAST;
		gbc_spinner_pnode_oval_w.insets = new Insets(0, 0, 5, 5);
		gbc_spinner_pnode_oval_w.gridx = 1;
		gbc_spinner_pnode_oval_w.gridy = 7;
		add(spinner_pnode_oval_w, gbc_spinner_pnode_oval_w);

		ChangeListener changeListener1 = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int width = (int) spinner_pnode_oval_w.getValue();
				int height = (int) spinner_pnode_oval_h.getValue();
				pickPanel.changeEllipseSize(width, height);
			}
		};
		spinner_pnode_oval_w.addChangeListener(changeListener1);

		spinner_pnode_oval_h = new JSpinner();
		GridBagConstraints gbc_spinner_pnode_oval_h = new GridBagConstraints();
		spinner_pnode_oval_h.setModel(new SpinnerNumberModel(GraphicProperties.DEFAULT_SIZE, 1, 120, 1));
		spinner_pnode_oval_h.setToolTipText(spinnerTooltip);
		spinner_pnode_oval_h.addChangeListener(changeListener1);
		gbc_spinner_pnode_oval_h.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_pnode_oval_h.gridx = 2;
		gbc_spinner_pnode_oval_h.gridy = 7;
		add(spinner_pnode_oval_h, gbc_spinner_pnode_oval_h);
		
		JRadioButton jrb_pnodes_oval = new JRadioButton("Oval");
		GridBagConstraints gbc_jrb_pnodes_oval = new GridBagConstraints();
		gbc_jrb_pnodes_oval.anchor = GridBagConstraints.WEST;
		gbc_jrb_pnodes_oval.insets = new Insets(0, 0, 5, 5);
		gbc_jrb_pnodes_oval.gridx = 0;
		gbc_jrb_pnodes_oval.gridy = 8;
		add(jrb_pnodes_oval, gbc_jrb_pnodes_oval);
		buttonGroup.add(jrb_pnodes_oval);
		jrb_pnodes_oval.setSelected(true);
		jrb_pnodes_oval.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(1);
			}
		});
		jrb_pnodes_oval.setForeground(GraphicProperties.pnodeColor);

		JRadioButton jrb_pnodes_rect = new JRadioButton("Rect");
		GridBagConstraints gbc_jrb_pnodes_rect = new GridBagConstraints();
		gbc_jrb_pnodes_rect.anchor = GridBagConstraints.WEST;
		gbc_jrb_pnodes_rect.insets = new Insets(0, 0, 5, 0);
		gbc_jrb_pnodes_rect.gridx = 2;
		gbc_jrb_pnodes_rect.gridy = 8;
		add(jrb_pnodes_rect, gbc_jrb_pnodes_rect);
		buttonGroup.add(jrb_pnodes_rect);
		jrb_pnodes_rect.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(2);
			}
		});
		jrb_pnodes_rect.setForeground(GraphicProperties.pnodeColor);

		jrb_pnodes_custom = new JRadioButton("Custom");
		jrb_pnodes_custom.setToolTipText("Customize for the novel shape");
		GridBagConstraints gbc_jrb_pnodes_custom = new GridBagConstraints();
		gbc_jrb_pnodes_custom.anchor = GridBagConstraints.WEST;
		gbc_jrb_pnodes_custom.insets = new Insets(0, 0, 5, 5);
		gbc_jrb_pnodes_custom.gridx = 0;
		gbc_jrb_pnodes_custom.gridy = 9;
		add(jrb_pnodes_custom, gbc_jrb_pnodes_custom);
		jrb_pnodes_custom.setForeground(GraphicProperties.pnodeColor);
		buttonGroup.add(jrb_pnodes_custom);
		jrb_pnodes_custom.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				nodeCustomSelected();
			}
		});

		JButton btnNewButton = new JButton("New");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IntuitiveShapeCreator dialog = new IntuitiveShapeCreator(locationPicker.getMainFrame(), true);
				dialog.setSkeletonMaker(locationPicker);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(locationPicker.getMainFrame());
				dialog.setVisible(true);
			}
		});
		btnNewButton.setBorder(new EmptyBorder(3, 10, 3, 10));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 9;
		add(btnNewButton, gbc_btnNewButton);

		comboBox = new JComboBox<>();
		comboBoxModel = new DefaultComboBoxModel<String>();
		comboBox.setModel(comboBoxModel);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.EAST;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 9;
		add(comboBox, gbc_comboBox);

		SwingUtilities.invokeLater(() -> {
			loadTheCustomizedShapes(defaultNodeShape.name);
			currentNodeShape = defaultNodeShape;
		});

		JLabel lblNewLabel_4 = new JLabel("pMembrane");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.gridwidth = 3;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 10;
		add(lblNewLabel_4, gbc_lblNewLabel_4);

		JRadioButton jrb_pmenbrane_oval = new JRadioButton("Oval");
		jrb_pmenbrane_oval.setToolTipText("Note: only first shape will export as R codes.");
		GridBagConstraints gbc_jrb_pmenbrane_oval = new GridBagConstraints();
		gbc_jrb_pmenbrane_oval.anchor = GridBagConstraints.WEST;
		gbc_jrb_pmenbrane_oval.gridwidth = 2;
		gbc_jrb_pmenbrane_oval.insets = new Insets(0, 0, 5, 5);
		gbc_jrb_pmenbrane_oval.gridx = 0;
		gbc_jrb_pmenbrane_oval.gridy = 11;
		add(jrb_pmenbrane_oval, gbc_jrb_pmenbrane_oval);
		buttonGroup.add(jrb_pmenbrane_oval);
		jrb_pmenbrane_oval.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(3);
			}
		});
		jrb_pmenbrane_oval.setForeground(GraphicProperties.pmembraneColor);

		JRadioButton jrb_pmenbrane_rect = new JRadioButton("Rect");
		jrb_pmenbrane_rect.setToolTipText("Note: only first shape will export as R codes.");
		GridBagConstraints gbc_jrb_pmenbrane_rect = new GridBagConstraints();
		gbc_jrb_pmenbrane_rect.anchor = GridBagConstraints.WEST;
		gbc_jrb_pmenbrane_rect.insets = new Insets(0, 0, 5, 0);
		gbc_jrb_pmenbrane_rect.gridx = 2;
		gbc_jrb_pmenbrane_rect.gridy = 11;
		add(jrb_pmenbrane_rect, gbc_jrb_pmenbrane_rect);
		buttonGroup.add(jrb_pmenbrane_rect);
		jrb_pmenbrane_rect.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(4);
			}
		});
		jrb_pmenbrane_rect.setForeground(GraphicProperties.pmembraneColor);

		JLabel lblNewLabel_5 = new JLabel("pNucleus");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.gridwidth = 3;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 12;
		add(lblNewLabel_5, gbc_lblNewLabel_5);

		JRadioButton jrb_pnucl_oval = new JRadioButton("Oval");
		jrb_pnucl_oval.setToolTipText("Note: only first shape will export as R codes.");
		GridBagConstraints gbc_jrb_pnucl_oval = new GridBagConstraints();
		gbc_jrb_pnucl_oval.gridwidth = 2;
		gbc_jrb_pnucl_oval.anchor = GridBagConstraints.WEST;
		gbc_jrb_pnucl_oval.insets = new Insets(0, 0, 5, 5);
		gbc_jrb_pnucl_oval.gridx = 0;
		gbc_jrb_pnucl_oval.gridy = 13;
		add(jrb_pnucl_oval, gbc_jrb_pnucl_oval);
		buttonGroup.add(jrb_pnucl_oval);
		jrb_pnucl_oval.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(5);
			}
		});
		jrb_pnucl_oval.setForeground(GraphicProperties.pnuclearColor);

		JRadioButton jrb_pnucl_rect = new JRadioButton("Rect");
		jrb_pnucl_rect.setToolTipText("Note: only first shape will export as R codes.");
		GridBagConstraints gbc_jrb_pnucl_rect = new GridBagConstraints();
		gbc_jrb_pnucl_rect.anchor = GridBagConstraints.WEST;
		gbc_jrb_pnucl_rect.insets = new Insets(0, 0, 5, 0);
		gbc_jrb_pnucl_rect.gridx = 2;
		gbc_jrb_pnucl_rect.gridy = 13;
		add(jrb_pnucl_rect, gbc_jrb_pnucl_rect);
		buttonGroup.add(jrb_pnucl_rect);
		jrb_pnucl_rect.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(6);
			}
		});
		jrb_pnucl_rect.setForeground(GraphicProperties.pnuclearColor);

		JRadioButton jrb_pnucl_DNA = new JRadioButton("DNA string");
		GridBagConstraints gbc_jrb_pnucl_DNA = new GridBagConstraints();
		gbc_jrb_pnucl_DNA.insets = new Insets(0, 0, 5, 5);
		gbc_jrb_pnucl_DNA.anchor = GridBagConstraints.WEST;
		gbc_jrb_pnucl_DNA.gridwidth = 2;
		gbc_jrb_pnucl_DNA.gridx = 0;
		gbc_jrb_pnucl_DNA.gridy = 14;
		add(jrb_pnucl_DNA, gbc_jrb_pnucl_DNA);
		buttonGroup.add(jrb_pnucl_DNA);
		jrb_pnucl_DNA.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(7);
			}
		});
		jrb_pnucl_DNA.setForeground(GraphicProperties.pnuclearDNAColor);

		JLabel lblNewLabel_5_1 = new JLabel("pFreeArrow");
		GridBagConstraints gbc_lblNewLabel_5_1 = new GridBagConstraints();
		gbc_lblNewLabel_5_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5_1.gridwidth = 3;
		gbc_lblNewLabel_5_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_5_1.gridx = 0;
		gbc_lblNewLabel_5_1.gridy = 15;
		add(lblNewLabel_5_1, gbc_lblNewLabel_5_1);

		JRadioButton jrb_freeArrow = new JRadioButton("Segment");
		GridBagConstraints gbc_jrb_freeArrow = new GridBagConstraints();
		gbc_jrb_freeArrow.anchor = GridBagConstraints.WEST;
		gbc_jrb_freeArrow.gridwidth = 2;
		gbc_jrb_freeArrow.insets = new Insets(0, 0, 5, 5);
		gbc_jrb_freeArrow.gridx = 0;
		gbc_jrb_freeArrow.gridy = 16;
		add(jrb_freeArrow, gbc_jrb_freeArrow);
		buttonGroup.add(jrb_freeArrow);
		jrb_freeArrow.addItemListener(e -> {
			if (jrb_freeArrow.isSelected()) {
				pickPanel.switchToOtherMode(8);
			}
		});
		jrb_freeArrow.setForeground(GraphicProperties.parrowColor);

		JButton btn_clearAll = new JButton("Clear all");
		btn_clearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pickPanel.clearAll();
			}
		});
		btn_clearAll.setRequestFocusEnabled(false);
		GridBagConstraints gbc_btn_clearAll = new GridBagConstraints();
		gbc_btn_clearAll.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn_clearAll.insets = new Insets(0, 0, 5, 0);
		gbc_btn_clearAll.anchor = GridBagConstraints.WEST;
		gbc_btn_clearAll.gridwidth = 3;
		gbc_btn_clearAll.gridx = 0;
		gbc_btn_clearAll.gridy = 17;
		add(btn_clearAll, gbc_btn_clearAll);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 18;
		add(separator_1, gbc_separator_1);

		tglbtn_eraser = new JToggleButton("Eraser");
		GridBagConstraints gbc_tglbtn_eraser = new GridBagConstraints();
		gbc_tglbtn_eraser.anchor = GridBagConstraints.WEST;
		gbc_tglbtn_eraser.gridwidth = 2;
		gbc_tglbtn_eraser.insets = new Insets(0, 0, 5, 5);
		gbc_tglbtn_eraser.gridx = 0;
		gbc_tglbtn_eraser.gridy = 19;
		add(tglbtn_eraser, gbc_tglbtn_eraser);
		icon = ImageUtils.getIcon("eraser.png");
		tglbtn_eraser.setIcon(icon);
		tglbtn_eraser.setRequestFocusEnabled(false);

		eraserItemListner = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					pickPanel.setEraserState(true);
				} else {
					pickPanel.setEraserState(false);
				}
			}
		};
		tglbtn_eraser.addItemListener(eraserItemListner);

		JToggleButton tglbtn_grid = new JToggleButton("Grid");
		GridBagConstraints gbc_tglbtn_grid = new GridBagConstraints();
		gbc_tglbtn_grid.anchor = GridBagConstraints.WEST;
		gbc_tglbtn_grid.insets = new Insets(0, 0, 5, 0);
		gbc_tglbtn_grid.gridx = 2;
		gbc_tglbtn_grid.gridy = 19;
		icon = ImageUtils.getIcon("grid.png");
		tglbtn_grid.setIcon(icon);
		tglbtn_grid.setRequestFocusEnabled(false);
		ItemListener gridItemListner = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					pickPanel.setGridState(true);
				} else {
					pickPanel.setGridState(false);
				}
			}
		};
		tglbtn_grid.addItemListener(gridItemListner);
		add(tglbtn_grid, gbc_tglbtn_grid);

		JToggleButton btn_imageMask = new JToggleButton("Image mask");
		btn_imageMask.setIcon(ImageUtils.getIcon("bx-mask.png"));
		btn_imageMask.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					locationPicker.drawingPanelSkeletonMaker.setImageMaskState(true);
				} else {
					locationPicker.drawingPanelSkeletonMaker.setImageMaskState(false);
				}
			}
		});
		GridBagConstraints gbc_btn_imageMask = new GridBagConstraints();
		gbc_btn_imageMask.anchor = GridBagConstraints.WEST;
		gbc_btn_imageMask.gridwidth = 2;
		gbc_btn_imageMask.insets = new Insets(0, 0, 5, 5);
		gbc_btn_imageMask.gridx = 0;
		gbc_btn_imageMask.gridy = 20;
		add(btn_imageMask, gbc_btn_imageMask);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.BLACK);
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.gridwidth = 3;
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 21;
		add(separator_2, gbc_separator_2);


	}

	private void nodeCustomSelected() {
		SwingUtilities.invokeLater(() -> {
			GraphicProperties graphicProperties = pickPanel.getGraphicProperties();

			Path2D path = new Path2D.Double();
			int size = currentNodeShape.xInteger.size();

			path.moveTo(currentNodeShape.xInteger.get(0), currentNodeShape.yInteger.get(0));
			for (int i = 1; i < size; i++) {
				Integer xx = currentNodeShape.xInteger.get(i);
				Integer yy = currentNodeShape.yInteger.get(i);
				path.lineTo(xx, yy);
			}
			path.closePath();
			graphicProperties.currentNodesCustom = path;
			pickPanel.switchToOtherMode(9);
		});
	}

	public void bindToPickPanel(DrawingPanelSkeletonMaker pickPanel) {
		this.pickPanel = pickPanel;
	}

	public void bindToLocationPicker(SkeletonMaker locationPicker) {
		this.locationPicker = locationPicker;
	}

	public void checkSpinner(int pnodeHalfOvalWidth, int pnodeHalfOvalHeight, int pnodeHalfRectWidth,
			int pnodeHalfRectHeight) {

		int pnodeHalfOvalHeight_s = (int) spinner_pnode_oval_h.getValue();
		int pnodeHalfOvalWidth_s = (int) spinner_pnode_oval_w.getValue();

		if (pnodeHalfOvalWidth == pnodeHalfOvalWidth_s && pnodeHalfOvalHeight == pnodeHalfOvalHeight_s) {
			return;
		}

		spinner_pnode_oval_h.setValue(pnodeHalfOvalHeight);
		spinner_pnode_oval_w.setValue(pnodeHalfOvalWidth);

	}

	public void restoreEraser2disabledState() {
		tglbtn_eraser.removeItemListener(eraserItemListner);
		tglbtn_eraser.setSelected(false);
		tglbtn_eraser.addItemListener(eraserItemListner);
		pickPanel.setEraserState(false);
	}

	public void loadTheCustomizedShapes(String selectedItem) {

		if (comboBoxListener != null) {
			comboBox.removeItemListener(comboBoxListener);
		}

		comboBoxModel.removeAllElements();

		comboBoxModel.addElement(defaultNodeShape.name);
		File file = new File(CONSTANTS.SHAPE_FILE_DIR);
		if (!file.exists()) {
			return;
		}
		File[] listFiles = file.listFiles();
		int length = listFiles.length;

		allSupportedNodeshapes = new ArrayList<>();
		allSupportedNodeshapes.add(defaultNodeShape);
		for (int i = 0; i < length; i++) {
			File file2 = listFiles[i];
			GraphicsNodeShape aa = (GraphicsNodeShape) ConfigFileMaintainer.readObjectFromFile(file2);
			comboBoxModel.addElement(aa.name);
			allSupportedNodeshapes.add(aa);
		}

		comboBoxModel.setSelectedItem(selectedItem);
		if (comboBoxListener == null) {
			comboBoxListener = new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {

						int selectedIndex = comboBox.getSelectedIndex();
						currentNodeShape = allSupportedNodeshapes.get(selectedIndex);

						if (jrb_pnodes_custom.isSelected()) {
							nodeCustomSelected();
						}
					}
				}
			};
		}
		comboBox.addItemListener(comboBoxListener);
	}
}
