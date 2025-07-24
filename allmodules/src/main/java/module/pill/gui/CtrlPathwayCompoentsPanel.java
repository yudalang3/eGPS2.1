package module.pill.gui;

import java.awt.Font;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;
import module.pill.core.CONSTANTS;
import module.pill.core.ConfigFileMaintainer;
import module.pill.core.DrawingPanelSkeletonMaker;
import module.pill.core.GraphicProperties;
import module.pill.core.GraphicsNodeShape;
import module.pill.core.IntuitiveShapeCreator;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CtrlPathwayCompoentsPanel extends JPanel{
	
	
	private JSpinner spinner_pnode_oval_w;
	private DrawingPanelSkeletonMaker pickPanel;
	private JSpinner spinner_pnode_oval_h;
	private JRadioButton jrb_pnodes_custom;
	private DefaultComboBoxModel<String> comboBoxModel;
	private JComboBox<String> comboBox;
	
	private ItemListener comboBoxListener;
	
	public GraphicsNodeShape defaultNodeShape;
	private GraphicsNodeShape currentNodeShape;
	
	private List<GraphicsNodeShape> allSupportedNodeshapes;

	public CtrlPathwayCompoentsPanel(DrawingPanelSkeletonMaker drawingPanelSkeletonMaker) {
		
		defaultNodeShape = new GraphicsNodeShape();
		defaultNodeShape.name = "Diamond ";
		defaultNodeShape.height = 20;
		defaultNodeShape.width = 20;
		defaultNodeShape.xInteger = Arrays.asList(0, 10, 0, -10);
		defaultNodeShape.yInteger = Arrays.asList(-10, 0, 10, 0);
		
		this.pickPanel = drawingPanelSkeletonMaker;
//		setLayout(new MigLayout("", "[][][]", "[][][][][][][][][][][][]"));
		setLayout(new MigLayout("align center center"));
		
		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		JLabel lblNewLabel_1 = new JLabel("pNodes :");
		lblNewLabel_1.setFont(defaultFont);
		add(lblNewLabel_1, "cell 0 0");
		
		JLabel lblNewLabel_2 = new JLabel("width");
		lblNewLabel_2.setFont(defaultFont);
		add(lblNewLabel_2, "cell 1 0");
		
		JLabel lblNewLabel_3 = new JLabel("height");
		lblNewLabel_3.setFont(defaultFont);
		add(lblNewLabel_3, "cell 2 0");
		
		spinner_pnode_oval_w = new JSpinner();
		spinner_pnode_oval_w.setFont(defaultFont);
		String spinnerTooltip = "<html>If you entery the number by keyboard.<br>Please do not forget to press the <strong>Enter</strong> key.";
		spinner_pnode_oval_w.setToolTipText(spinnerTooltip);
		spinner_pnode_oval_w.setModel(new SpinnerNumberModel(GraphicProperties.DEFAULT_SIZE, 1, 120, 1));
		ChangeListener changeListener1 = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int width = (int) spinner_pnode_oval_w.getValue();
				int height = (int) spinner_pnode_oval_h.getValue();
				pickPanel.changeEllipseSize(width, height);
			}
		};
		spinner_pnode_oval_w.addChangeListener(changeListener1);
		
		add(spinner_pnode_oval_w, "cell 1 1");
		
		spinner_pnode_oval_h = new JSpinner();
		spinner_pnode_oval_h.setFont(defaultFont);
		spinner_pnode_oval_h.setModel(new SpinnerNumberModel(GraphicProperties.DEFAULT_SIZE, 1, 120, 1));
		spinner_pnode_oval_h.setToolTipText(spinnerTooltip);
		spinner_pnode_oval_h.addChangeListener(changeListener1);
		
		add(spinner_pnode_oval_h, "cell 2 1");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JRadioButton jrb_pnodes_oval = new JRadioButton("Oval");
		jrb_pnodes_oval.setFont(defaultFont);
		buttonGroup.add(jrb_pnodes_oval);
		jrb_pnodes_oval.setSelected(true);
		jrb_pnodes_oval.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(1);
			}
		});
		jrb_pnodes_oval.setForeground(GraphicProperties.pnodeColor);
		add(jrb_pnodes_oval, "cell 0 2");
		
		JRadioButton jrb_pnodes_rect = new JRadioButton("Rect");
		jrb_pnodes_rect.setFont(defaultFont);
		buttonGroup.add(jrb_pnodes_rect);
		jrb_pnodes_rect.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(2);
			}
		});
		jrb_pnodes_rect.setForeground(GraphicProperties.pnodeColor);
		add(jrb_pnodes_rect, "cell 1 2");
		
		jrb_pnodes_custom = new JRadioButton("Custom");
		jrb_pnodes_custom.setFont(defaultFont);
		jrb_pnodes_custom.setToolTipText("Customize for the novel shape");
		jrb_pnodes_custom.setForeground(GraphicProperties.pnodeColor);
		buttonGroup.add(jrb_pnodes_custom);
		jrb_pnodes_custom.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				nodeCustomSelected();
			}
		});
		add(jrb_pnodes_custom, "cell 0 3");
		
		JButton btnNewButton = new JButton("New");
		btnNewButton.setFont(defaultFont);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();
				IntuitiveShapeCreator dialog = new IntuitiveShapeCreator(instanceFrame, true);
//				dialog.setSkeletonMaker(locationPicker);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(instanceFrame);
				dialog.setVisible(true);
			}
		});
		btnNewButton.setBorder(new EmptyBorder(3, 10, 3, 10));
		add(btnNewButton, "cell 1 3 2 1");
		
		comboBox = new JComboBox<String>();
		comboBox.setFont(defaultFont);
		comboBoxModel = new DefaultComboBoxModel<String>();
		comboBox.setModel(comboBoxModel);
		add(comboBox, "cell 0 4 3 1,growx");
		
		SwingUtilities.invokeLater(() -> {
			loadTheCustomizedShapes(defaultNodeShape.name);
			currentNodeShape = defaultNodeShape;
		});
		
		JSeparator separator1 = new JSeparator();
		add(separator1, "cell 0 5 3 1,growx");
		
		JLabel lblNewLabel_4 = new JLabel("pMembrane");
		lblNewLabel_4.setFont(defaultFont);
		add(lblNewLabel_4, "cell 0 6");
		
		JRadioButton jrb_pmenbrane_oval = new JRadioButton("Oval");
		jrb_pmenbrane_oval.setFont(defaultFont);
		buttonGroup.add(jrb_pmenbrane_oval);
		jrb_pmenbrane_oval.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(3);
			}
		});
		jrb_pmenbrane_oval.setForeground(GraphicProperties.pmembraneColor);
		add(jrb_pmenbrane_oval, "cell 0 7");
		
		JRadioButton jrb_pmenbrane_rect = new JRadioButton("Rect");
		jrb_pmenbrane_rect.setFont(defaultFont);
		buttonGroup.add(jrb_pmenbrane_rect);
		jrb_pmenbrane_rect.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(4);
			}
		});
		jrb_pmenbrane_rect.setForeground(GraphicProperties.pmembraneColor);
		add(jrb_pmenbrane_rect, "cell 1 7 2 1");
		
		JSeparator separator = new JSeparator();
		add(separator, "cell 0 8 3 1,growx");
		
		JLabel lblNewLabel_5 = new JLabel("pNucleus");
		lblNewLabel_5.setFont(defaultFont);
		add(lblNewLabel_5, "cell 0 9");
		
		JRadioButton jrb_pnucl_oval = new JRadioButton("Oval");
		jrb_pnucl_oval.setFont(defaultFont);
		buttonGroup.add(jrb_pnucl_oval);
		jrb_pnucl_oval.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(5);
			}
		});
		jrb_pnucl_oval.setForeground(GraphicProperties.pnuclearColor);
		add(jrb_pnucl_oval, "cell 1 9 2 1");
		
		JLabel lblNewLabel_6 = new JLabel("pDNAstring");
		lblNewLabel_6.setFont(defaultFont);
		add(lblNewLabel_6, "cell 0 10");
		
		JRadioButton jrb_pnucl_DNA = new JRadioButton("Line");
		jrb_pnucl_DNA.setFont(defaultFont);
		buttonGroup.add(jrb_pnucl_DNA);
		jrb_pnucl_DNA.addItemListener(e -> {
			JRadioButton jrb = (JRadioButton) e.getSource();
			if (jrb.isSelected()) {
				pickPanel.switchToOtherMode(7);
			}
		});
		jrb_pnucl_DNA.setForeground(GraphicProperties.pnuclearDNAColor);
		add(jrb_pnucl_DNA, "cell 1 10 2 1");
		
		JLabel lblNewLabel_7 = new JLabel("pFreeArrow");
		lblNewLabel_7.setFont(defaultFont);
		add(lblNewLabel_7, "cell 0 11");
		
		JRadioButton jrb_freeArrow = new JRadioButton("Segment");
		jrb_freeArrow.setFont(defaultFont);
		buttonGroup.add(jrb_freeArrow);
		jrb_freeArrow.addItemListener(e -> {
			if (jrb_freeArrow.isSelected()) {
				pickPanel.switchToOtherMode(8);
			}
		});
		jrb_freeArrow.setForeground(GraphicProperties.parrowColor);
		add(jrb_freeArrow, "cell 1 11 2 1");
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
}
