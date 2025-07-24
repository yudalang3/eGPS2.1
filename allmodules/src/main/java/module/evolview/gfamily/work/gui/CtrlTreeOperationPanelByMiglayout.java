package module.evolview.gfamily.work.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.raven.swing.Button;
import com.raven.swing.ButtonBadges;

import egps2.utils.common.util.EGPSShellIcons;
import egps.lnf.utils.BEUtils;
import egps2.UnifiedAccessPoint;
import evoltree.struct.util.EvolNodeUtil;
import egps2.builtin.modules.IconObtainer;
import module.evolview.gfamily.work.gui.browser.BaseCtrlPanel;
import module.evolview.gfamily.work.gui.dialog.SeachDialog;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.ShowInnerNodePropertiesInfo;
import module.evolview.phylotree.visualization.graphics.struct.ShowLeafPropertiesInfo;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import graphic.engine.guirelated.JFontChooser;

@SuppressWarnings("serial")
public class CtrlTreeOperationPanelByMiglayout extends BaseCtrlPanel implements MouseListener, ActionListener {

	protected JToggleButton scaleBar;
	protected JToggleButton axisButton;
	private ButtonBadges nodeIncreaseButton;
	private ButtonBadges nodeDecreaseButton;
	private ButtonBadges lineDecreaseButton;
	private ButtonBadges lineIncreaseButton;
	private JToggleButton showleaveLabelToggleButton;
	private Button searchButton;
	private Button buttonLadderize4up;
	private Button buttonLaddrize4down;
	private Button autoSize4fitFrameButton;
	private SeachDialog seachDialog;

	public CtrlTreeOperationPanelByMiglayout() {
		Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		setBorder(new EmptyBorder(4, 4, 4, 4));
		GridBagLayout gbl_panel = new GridBagLayout();

		setLayout(gbl_panel);

		JLabel lblConvenientOperation = new JLabel("Convenient Operation");
		lblConvenientOperation.setFont(titleFont);
		GridBagConstraints gbc_lblConvenientOperation = new GridBagConstraints();
		gbc_lblConvenientOperation.gridwidth = 4;
		gbc_lblConvenientOperation.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblConvenientOperation.insets = new Insets(0, 0, 5, 0);
		gbc_lblConvenientOperation.gridx = 0;
		gbc_lblConvenientOperation.gridy = 0;
		add(lblConvenientOperation, gbc_lblConvenientOperation);

		autoSize4fitFrameButton = new Button();
		autoSize4fitFrameButton.setToolTipText(
				"<html><body>Autosize to fit the drawing panel. <br> Note: If you zoom in the graph before, please zoom out first !");
		autoSize4fitFrameButton.setIcon(EGPSShellIcons.get("auto-size.png"));
		autoSize4fitFrameButton.addActionListener(this);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		add(autoSize4fitFrameButton, gbc_btnNewButton);

		buttonLadderize4up = new Button();
		buttonLadderize4up.addActionListener(this);
		buttonLadderize4up.setIcon(IconObtainer.get("ladderizeUp.png"));
		buttonLadderize4up.setToolTipText("Ladderize up the tree.");
		GridBagConstraints gbc_buttonLadderize4up = new GridBagConstraints();
		gbc_buttonLadderize4up.insets = new Insets(0, 0, 5, 5);
		gbc_buttonLadderize4up.gridx = 1;
		gbc_buttonLadderize4up.gridy = 1;
		add(buttonLadderize4up, gbc_buttonLadderize4up);

		buttonLaddrize4down = new Button();
		buttonLaddrize4down.addActionListener(this);
		buttonLaddrize4down.setIcon(IconObtainer.get("ladderizeDown.png"));
		buttonLaddrize4down.setToolTipText("Ladderize down the tree.");
		GridBagConstraints gbc_buttonLaddrize4down = new GridBagConstraints();
		gbc_buttonLaddrize4down.insets = new Insets(0, 0, 5, 5);
		gbc_buttonLaddrize4down.gridx = 2;
		gbc_buttonLaddrize4down.gridy = 1;
		add(buttonLaddrize4down, gbc_buttonLaddrize4down);

		searchButton = new Button();
		searchButton.addActionListener(this);
		searchButton.setIcon(IconObtainer.get("search.png"));
		searchButton.setToolTipText("Search the tree for specific content.");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 1;
		add(searchButton, gbc_btnNewButton_1);

		JLabel lblGraphicEffects = new JLabel("Graphic effects");
		lblGraphicEffects.setFont(titleFont);
		GridBagConstraints gbc_lblGraphicEffects = new GridBagConstraints();
		gbc_lblGraphicEffects.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblGraphicEffects.gridwidth = 4;
		gbc_lblGraphicEffects.insets = new Insets(0, 0, 5, 5);
		gbc_lblGraphicEffects.gridx = 0;
		gbc_lblGraphicEffects.gridy = 2;
		add(lblGraphicEffects, gbc_lblGraphicEffects);

		JButton globalFontButton = new JButton();
		globalFontButton.setFocusable(false);
		globalFontButton.setToolTipText("Change the global font. like nodes, including leave label and branch label.");
		globalFontButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TreeLayoutProperties treeLayoutProperties = controller.getTreeLayoutProperties();
//				ShowLeafPropertiesInfo showLeafPropertiesInfo = treeLayoutProperties.getShowLeafPropertiesInfo();
//				ShowInnerNodePropertiesInfo showInnerNodePropertiesInfo = treeLayoutProperties
//						.getShowInnerNodePropertiesInfo();
//				if (!treeLayoutProperties.isShowBranchLabel() && !showLeafPropertiesInfo.isShowLeafLabel()
//						&& !showInnerNodePropertiesInfo.isShowInternalNodeLabel()) {
//					SwingDialog.showErrorMSGDialog("Operation error",
//							"You need to show branch label or node label first!");
//					return;
//				}

				Font initialFont = treeLayoutProperties.getGlobalFont();
				JFontChooser jFontChooser = new JFontChooser(initialFont, null);
				int showDialog = jFontChooser.showDialog(UnifiedAccessPoint.getInstanceFrame());

				if (showDialog == JFontChooser.OK_OPTION) {
					Font font = jFontChooser.getSelectedFont();
					controller.getTreeLayoutProperties().setGlobalFont(font);
					controller.weakRefreshPhylogeneticTree();
				}
			}
		});

		JButton btnChangeColor = new JButton("");
		btnChangeColor.setFocusable(false);
		btnChangeColor.setToolTipText("<html><body>" + "Change color of selected nodes, including its offsprings."
				+ " <br>(If no nodes selected, all nodes will change.)" + "</body></html>");
		btnChangeColor.setIcon(IconObtainer.get("color.png"));
		btnChangeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> {
					controller.changeColorForSelectedNodesInTreePanel();
				});
			}
		});
		GridBagConstraints gbc_btnBranchLengthAs = new GridBagConstraints();
		gbc_btnBranchLengthAs.fill = GridBagConstraints.BOTH;
		gbc_btnBranchLengthAs.insets = new Insets(0, 0, 5, 5);
		gbc_btnBranchLengthAs.gridx = 0;
		gbc_btnBranchLengthAs.gridy = 3;
		add(btnChangeColor, gbc_btnBranchLengthAs);
		globalFontButton.setIcon(IconObtainer.get("font2.png"));
		GridBagConstraints gbc_btnFont = new GridBagConstraints();
		gbc_btnFont.fill = GridBagConstraints.BOTH;
		gbc_btnFont.insets = new Insets(0, 0, 5, 5);
		gbc_btnFont.gridx = 1;
		gbc_btnFont.gridy = 3;
		add(globalFontButton, gbc_btnFont);

		JButton titleFontButton = new JButton("");
		titleFontButton.setIcon(IconObtainer.get("statementFont.png"));
		titleFontButton.setToolTipText("Change font of the statement below. ");
		GridBagConstraints gbc_titleFontButton = new GridBagConstraints();
		gbc_titleFontButton.fill = GridBagConstraints.BOTH;
		gbc_titleFontButton.insets = new Insets(0, 0, 5, 5);
		gbc_titleFontButton.gridx = 2;
		gbc_titleFontButton.gridy = 3;
		add(titleFontButton, gbc_titleFontButton);
		titleFontButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Font initialFont = controller.getTreeLayoutProperties().getTitleFont();
				JFontChooser jFontChooser = new JFontChooser(initialFont, null);
				int showDialog = jFontChooser.showDialog(UnifiedAccessPoint.getInstanceFrame());

				if (showDialog == JFontChooser.OK_OPTION) {
					Font font = jFontChooser.getSelectedFont();
					controller.getTreeLayoutProperties().setTitleFont(font);
					controller.reCalculateTreeLayoutAndRefreshViewPort();
				}
			}
		});

		JButton axisFontButton = new JButton("");
		axisFontButton.setIcon(IconObtainer.get("scale_font.png"));
		axisFontButton.setToolTipText("Change font of x-axis and plotting scale.");
		GridBagConstraints gbc_AxisFontButton = new GridBagConstraints();
		gbc_AxisFontButton.fill = GridBagConstraints.BOTH;
		gbc_AxisFontButton.insets = new Insets(0, 0, 5, 0);
		gbc_AxisFontButton.gridx = 3;
		gbc_AxisFontButton.gridy = 3;
		add(axisFontButton, gbc_AxisFontButton);
		axisFontButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Font initialFont = controller.getTreeLayoutProperties().getAxisFont();
				JFontChooser jFontChooser = new JFontChooser(initialFont, null);
				int showDialog = jFontChooser.showDialog(UnifiedAccessPoint.getInstanceFrame());

				if (showDialog == JFontChooser.OK_OPTION) {
					Font font = jFontChooser.getSelectedFont();
					controller.getTreeLayoutProperties().setAxisFont(font);
					controller.reCalculateTreeLayoutAndRefreshViewPort();
				}
			}
		});

		lineIncreaseButton = new ButtonBadges();
		lineIncreaseButton.setBadges(0);
//		Button lineIncreaseButton = new Button();
//		lineIncreaseButton.setFocusable(false);

		BEUtils.setButtonIcon(lineIncreaseButton, IconObtainer.get("Line_add.png"));
		GridBagConstraints gbc_lineIncreaseButton = new GridBagConstraints();
		gbc_lineIncreaseButton.insets = new Insets(0, 0, 5, 5);
		gbc_lineIncreaseButton.fill = GridBagConstraints.BOTH;
		gbc_lineIncreaseButton.gridx = 0;
		gbc_lineIncreaseButton.gridy = 4;
		add(lineIncreaseButton, gbc_lineIncreaseButton);

		lineIncreaseButton.setToolTipText(
				"<html><body>Increase the line thickness of selected nodes, left click including its offsprings while<br> right click only for all leaves.<br>(The line thickness of the whole tree will be changed if no nodes are selected)</html></body>");
		lineIncreaseButton.addMouseListener(this);

		lineDecreaseButton = new ButtonBadges();
		lineDecreaseButton.setToolTipText(
				"<html><body>Decrease the line thickness of selected nodes, left click including its offsprings while<br> right click only for all leaves.<br> (The line thickness of the whole tree will be changed if no nodes are selected)</html></body>");
		lineDecreaseButton.addMouseListener(this);
		BEUtils.setButtonIcon(lineDecreaseButton, IconObtainer.get("Line_minus.png"));
		GridBagConstraints gbc_lineDecreaseButton = new GridBagConstraints();
		gbc_lineDecreaseButton.insets = new Insets(0, 0, 5, 5);
		gbc_lineDecreaseButton.fill = GridBagConstraints.BOTH;
		gbc_lineDecreaseButton.gridx = 1;
		gbc_lineDecreaseButton.gridy = 4;
		add(lineDecreaseButton, gbc_lineDecreaseButton);

		nodeIncreaseButton = new ButtonBadges();
		nodeIncreaseButton.setIcon(IconObtainer.get("node_add.png"));
		nodeIncreaseButton.setToolTipText(
				"<html><body>Increase the size of selected nodes, left click including its offsprings while<br> right click only for all leaves.<br>(The node size of the whole tree will be changed if no nodes are selected)</html></body>");
		GridBagConstraints gbc_nodeIncreaseButton = new GridBagConstraints();
		gbc_nodeIncreaseButton.fill = GridBagConstraints.BOTH;
		gbc_nodeIncreaseButton.insets = new Insets(0, 0, 5, 5);
		gbc_nodeIncreaseButton.gridx = 2;
		gbc_nodeIncreaseButton.gridy = 4;
		add(nodeIncreaseButton, gbc_nodeIncreaseButton);
		nodeIncreaseButton.addMouseListener(this);

		nodeDecreaseButton = new ButtonBadges();
		nodeDecreaseButton.setIcon(IconObtainer.get("node_minus.png"));
		nodeDecreaseButton.setToolTipText(
				"<html><body>Decrease the size of selected nodes, left click including its offsprings while<br> right click only for all leaves.<br>(The node size of the whole tree will be changed if no nodes are selected)</html></body>");
		GridBagConstraints gbc_nodeDecreaseButton = new GridBagConstraints();
		gbc_nodeDecreaseButton.fill = GridBagConstraints.BOTH;
		gbc_nodeDecreaseButton.insets = new Insets(0, 0, 5, 0);
		gbc_nodeDecreaseButton.gridx = 3;
		gbc_nodeDecreaseButton.gridy = 4;
		add(nodeDecreaseButton, gbc_nodeDecreaseButton);
		nodeDecreaseButton.addMouseListener(this);

		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.gridwidth = 4;
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 5;
		add(separator_2, gbc_separator_2);

		JLabel lblTreeInfromation = new JLabel("Tree infromation");
		lblTreeInfromation.setFont(titleFont);
		GridBagConstraints gbc_lblTreeInfromation = new GridBagConstraints();
		gbc_lblTreeInfromation.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTreeInfromation.gridwidth = 4;
		gbc_lblTreeInfromation.insets = new Insets(0, 0, 5, 0);
		gbc_lblTreeInfromation.gridx = 0;
		gbc_lblTreeInfromation.gridy = 6;
		add(lblTreeInfromation, gbc_lblTreeInfromation);

		scaleBar = new JToggleButton("");
		scaleBar.setFocusable(false);
		scaleBar.setToolTipText("Show the plotting scale for the phylogram.");
		scaleBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.getTreeLayoutProperties().setShowScaleBar(scaleBar.isSelected() && scaleBar.isEnabled());
				controller.refreshPhylogeneticTree();
			}
		});
		scaleBar.setIcon(IconObtainer.get("ruler.png"));
		GridBagConstraints gbc_btnLeaf = new GridBagConstraints();
		gbc_btnLeaf.fill = GridBagConstraints.BOTH;
		gbc_btnLeaf.insets = new Insets(0, 0, 5, 5);
		gbc_btnLeaf.gridx = 0;
		gbc_btnLeaf.gridy = 7;
		add(scaleBar, gbc_btnLeaf);

		showleaveLabelToggleButton = new JToggleButton("");
		showleaveLabelToggleButton.setFocusable(false);
		showleaveLabelToggleButton.setToolTipText("Show leave labels.");
		showleaveLabelToggleButton.setIcon(IconObtainer.get("showLeafNodeLabel.png"));
		showleaveLabelToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = showleaveLabelToggleButton.isSelected();
				controller.showLeaveLabel(selected);
				controller.refreshPhylogeneticTree();
			}
		});
		GridBagConstraints gbc_btnNewButton1 = new GridBagConstraints();
		gbc_btnNewButton1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton1.gridx = 1;
		gbc_btnNewButton1.gridy = 7;
		add(showleaveLabelToggleButton, gbc_btnNewButton1);

		JToggleButton displayTitle = new JToggleButton("");
		displayTitle.setFocusable(false);
		displayTitle.setToolTipText("Show the statement below.");
		displayTitle.setIcon(IconObtainer.get("mingpian.png"));
		displayTitle.setSelected(true);
		displayTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.getTreeLayoutProperties().setShowTitle(displayTitle.isSelected());
				controller.refreshPhylogeneticTree();
			}
		});
		GridBagConstraints gbc_button2 = new GridBagConstraints();
		gbc_button2.fill = GridBagConstraints.HORIZONTAL;
		gbc_button2.insets = new Insets(0, 0, 5, 5);
		gbc_button2.gridx = 2;
		gbc_button2.gridy = 7;
		add(displayTitle, gbc_button2);

		axisButton = new JToggleButton("");
		axisButton.setSelected(true);
		GridBagConstraints gbc_axisButton = new GridBagConstraints();
		gbc_axisButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_axisButton.insets = new Insets(0, 0, 5, 5);
		gbc_axisButton.gridx = 3;
		gbc_axisButton.gridy = 7;
		axisButton.setToolTipText("Show the x-axis!");
		axisButton.setIcon(IconObtainer.get("scale.png"));
		add(axisButton, gbc_axisButton);
		axisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.getTreeLayoutProperties().setShowAxisBar(axisButton.isSelected());
				controller.refreshPhylogeneticTree();
			}
		});

		JToggleButton toggleButtonWH = new JToggleButton("W&H");
//		JToggleButton toggleButtonWH = new JToggleButton("");
		toggleButtonWH.setFocusable(false);
//		toggleButtonWH.setIcon(IconObtainer.get("showInnerNodeLabel.png"));
		toggleButtonWH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controller != null) {
					TreeLayoutProperties treeLayoutProperties = controller.getTreeLayoutProperties();
					treeLayoutProperties.setShowWidthAndHeightString(toggleButtonWH.isSelected());
					controller.refreshPhylogeneticTree();
				}
			}
		});
		toggleButtonWH.setToolTipText(
				"<html><body>Show width and height in the drawing panel. <br> \r\nThis is usefull when using the circular layout");
		GridBagConstraints gbc_toggleButtonWH = new GridBagConstraints();
		gbc_toggleButtonWH.insets = new Insets(0, 0, 5, 5);
		gbc_toggleButtonWH.gridx = 0;
		gbc_toggleButtonWH.gridy = 8;
		add(toggleButtonWH, gbc_toggleButtonWH);

		JToggleButton showInnerNodeLabel = new JToggleButton("");
		showInnerNodeLabel.setToolTipText("Show internal node label.");
		showInnerNodeLabel.setIcon(IconObtainer.get("showInnerNodeLabel.png"));
		showInnerNodeLabel.setFocusable(false);
		GridBagConstraints gbc_showInnerNodeLabel = new GridBagConstraints();
		gbc_showInnerNodeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_showInnerNodeLabel.gridx = 1;
		gbc_showInnerNodeLabel.gridy = 8;
		add(showInnerNodeLabel, gbc_showInnerNodeLabel);
		showInnerNodeLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = showInnerNodeLabel.isSelected();
				TreeLayoutProperties treeLayoutProperties = controller.getTreeLayoutProperties();
				GraphicsNode originalRootNode = treeLayoutProperties.getOriginalRootNode();
				EvolNodeUtil.recursiveIterateTreeIF(originalRootNode, node -> {
					if (node.getChildCount() > 0) {
						node.getDrawUnit().setDrawName(selected);
					}
				});

				ShowInnerNodePropertiesInfo showInnerNodePropertiesInfo = treeLayoutProperties
						.getShowInnerNodePropertiesInfo();
				showInnerNodePropertiesInfo.setShowInternalNodeLabel(selected);
				controller.refreshPhylogeneticTree();
			}
		});

		JToggleButton showbranchLengthButton = new JToggleButton("");
		showbranchLengthButton.setToolTipText("Show internal node branch length.");
		showbranchLengthButton.setIcon(IconObtainer.get("showBranchlength.png"));
		showbranchLengthButton.setFocusable(false);
		GridBagConstraints gbc_showAxisButton = new GridBagConstraints();
		gbc_showAxisButton.insets = new Insets(0, 0, 5, 5);
		gbc_showAxisButton.gridx = 2;
		gbc_showAxisButton.gridy = 8;
		add(showbranchLengthButton, gbc_showAxisButton);
		showbranchLengthButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = showbranchLengthButton.isSelected();
				ShowInnerNodePropertiesInfo showInnerNodePropertiesInfo = controller.getTreeLayoutProperties()
						.getShowInnerNodePropertiesInfo();
				showInnerNodePropertiesInfo.setShowInternalNodeBranchLength(selected);
				controller.refreshPhylogeneticTree();
			}
		});

		JToggleButton showBtspButton = new JToggleButton("");
		showBtspButton.setToolTipText("Show internal node bootstrap value.");
		showBtspButton.setIcon(IconObtainer.get("showBootstrapValue.png"));
		showBtspButton.setFocusable(false);
		GridBagConstraints gbc_showBtspButton = new GridBagConstraints();
		gbc_showBtspButton.insets = new Insets(0, 0, 5, 5);
		gbc_showBtspButton.gridx = 3;
		gbc_showBtspButton.gridy = 8;
		add(showBtspButton, gbc_showBtspButton);
		showBtspButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = showBtspButton.isSelected();
				ShowInnerNodePropertiesInfo showInnerNodePropertiesInfo = controller.getTreeLayoutProperties()
						.getShowInnerNodePropertiesInfo();
				showInnerNodePropertiesInfo.setShowInternalNodeBootstrap(selected);
				controller.refreshPhylogeneticTree();
			}
		});

//		JButton buttonWhatYouWant = new JButton("Adjust tree whatever you want");
//		buttonWhatYouWant.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				MyFrame instanceFrame = UniSoftInstance.getInstanceFrame();
//				StandardDialog standardDialog = new StandardDialog(instanceFrame) {
//
//					@Override
//					public JComponent createBannerPanel() {
//						BannerPanel bannerPanel = new BannerPanel("Modern tree view parameters panel", "Set all parameters in text, make whatever effects you want.");
//						bannerPanel.setTitleColor(Color.yellow);
//						bannerPanel.setSubTitleColor(Color.white);
//						bannerPanel.setSubTitleIndent(9);
//						bannerPanel.setStartColor(Color.blue);
//						bannerPanel.setEndColor(Color.white);
//						return bannerPanel;
//					}
//
//					@Override
//					public JComponent createContentPanel() {
//						JTextArea jTextArea = new JTextArea();
//						jTextArea.setBorder(BorderFactory.createTitledBorder("Text here:"));
//						
//						StringBuilder specificationHeader = EGPSProperties.getSpecificationHeader();
//						specificationHeader.append("#$caption=DVL\n");
//						specificationHeader.append("#$branchThickness=2\n");
//						jTextArea.setText(specificationHeader.toString());
//						jTextArea.setFont(defaultFont);
//						return new JScrollPane(jTextArea);
//					}
//
//					@Override
//					public ButtonPanel createButtonPanel() {
//						ButtonPanel panel = createOKCancelButtonPanel();
//						return panel;
//					}
//					
//				};
//				
//				standardDialog.setSize(800, 800);
//				standardDialog.setLocationRelativeTo(instanceFrame);
//				standardDialog.setTitle("Set full parameters");
//				standardDialog.setVisible(true);
//				
//			}
//		});

		Border emptyBorder = BorderFactory.createEmptyBorder(5, 7, 5, 7);
		Component[] components = getComponents();
		for (Component component : components) {
			if (component instanceof JButton) {
				JButton bb = (JButton) component;
				bb.setBorder(emptyBorder);
			}
		}
	}

	@Override
	public void reInitializeGUIAccording2treeLayoutProperties() {
		ActionListener[] actionListeners = showleaveLabelToggleButton.getActionListeners();
		for (ActionListener actionListener : actionListeners) {
			showleaveLabelToggleButton.removeActionListener(actionListener);
		}

		TreeLayoutProperties treeLayoutProperties = controller.getTreeLayoutProperties();
		ShowLeafPropertiesInfo showLeafPropertiesInfo = treeLayoutProperties.getShowLeafPropertiesInfo();
		showleaveLabelToggleButton.setSelected(showLeafPropertiesInfo.isShowLeafLabel());

		for (ActionListener actionListener : actionListeners) {
			showleaveLabelToggleButton.addActionListener(actionListener);
		}

	}

	/**
	 * 这个按钮的状态是和当前树的布局是否支持显示scale bar和axis bar相关联的。 重构的时候要特别注意！
	 *
	 * @param enable
	 */
	public void enableScaleBarStates(boolean enable) {
		scaleBar.setEnabled(enable);
		axisButton.setEnabled(enable);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean isRightClicked = SwingUtilities.isRightMouseButton(e);

		Object source = e.getSource();
		if (source == nodeIncreaseButton) {
			nodeIncreaseButton.setBadges(nodeIncreaseButton.getBadges() + 1);
			controller.increaseSizeForSelectedNodesInTreePanel(isRightClicked);
		} else if (source == nodeDecreaseButton) {
			controller.decreaseSizeForSelectedNodesInTreePanel(isRightClicked);
			nodeDecreaseButton.setBadges(nodeDecreaseButton.getBadges() + 1);
		} else if (source == lineDecreaseButton) {
			controller.decreaseLineThinknessForSelectedNodesInTreePanel(isRightClicked);
			lineDecreaseButton.setBadges(lineDecreaseButton.getBadges() + 1);
		} else if (source == lineIncreaseButton) {
			controller.increaseLineThinknessForSelectedNodesInTreePanel(isRightClicked);
			int count = lineIncreaseButton.getBadges() + 1;
			lineIncreaseButton.setBadges(count);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (searchButton == source) {
			seachDialog = getSearchDialog();
			seachDialog.setVisible(true);
		} else if (source == buttonLadderize4up) {
			PhylogeneticTreePanel panel = controller.getSelectedPhylogeneticTreePanel();
			GraphicsNode rootNode = panel.getRootNode();
			EvolNodeUtil.initializeSize(rootNode);
			EvolNodeUtil.ladderizeNodeAccording2sizeAndLength(rootNode, true);
			panel.updatePhylogeneticTreePanel4fitFrame();
		} else if (source == buttonLaddrize4down) {
			PhylogeneticTreePanel panel = controller.getSelectedPhylogeneticTreePanel();
			GraphicsNode rootNode = panel.getRootNode();
			EvolNodeUtil.initializeSize(rootNode);
			EvolNodeUtil.ladderizeNodeAccording2sizeAndLength(rootNode, false);
			panel.updatePhylogeneticTreePanel4fitFrame();
		} else if (source == autoSize4fitFrameButton) {
			controller.reCalculateTreeLayoutAndRefreshViewPort();
		}

	}

	private SeachDialog getSearchDialog() {
		if (seachDialog == null) {
			seachDialog = new SeachDialog(controller);
		}
		return seachDialog;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
