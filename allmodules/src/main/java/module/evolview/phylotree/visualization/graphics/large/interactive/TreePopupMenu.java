package module.evolview.phylotree.visualization.graphics.large.interactive;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import module.evolview.gfamily.work.gui.dialog.NodeAnnotationDialogContainer;
import module.evolview.moderntreeviewer.IndependentModuleLoader;
import module.evolview.phylotree.visualization.graphics.struct.CollapseProperty;
import egps2.panels.dialog.EGPSFileChooser;
import egps2.utils.common.model.datatransfer.Tuple;
import egps2.UnifiedAccessPoint;
import egps2.frame.MainFrameProperties;
import evoltree.struct.util.EvolNodeUtil;
import module.evoltre.mutation.IMutation4Rec;
import egps2.builtin.modules.IconObtainer;
import module.evolview.gfamily.work.NodeUtils;
import module.evolview.gfamily.work.gui.tree.CollapseDialog;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.gfamily.work.model.enums.BranchLengthType;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import evoltree.txtdisplay.TreeDrawUnit;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.layout.BaseLayout;
import egps2.frame.gui.dialog.DialogMoreInfoGenerator;

public class TreePopupMenu {

	private TreeLayoutProperties treeLayoutProperties;

	protected GraphicsNode currNode = null;
	protected final static DecimalFormat deciFor = new DecimalFormat("#.######");

	protected JPopupMenu popInternalNodeRightClick;
	protected JPopupMenu popLeafRightClick;
	protected JPopupMenu blinkAreaPopupMenu;

	private boolean debug = false;

	protected StringBuilder sBuilder = new StringBuilder();

	Function<IMutation4Rec, String> printSimpleMutationInfo = mutation -> {
		String origin = mutation.getAncestralState();
		int position = mutation.getPosition();
		String destination = mutation.getDerivedState();
		String newOrigin = origin;
		if (origin.length() > 3) {
			newOrigin = origin.substring(0, 1) + "..." + origin.substring(origin.length() - 1);
		}
		String newDestination = destination;
		if (destination.length() > 3) {
			newDestination = destination.substring(0, 1) + "..." + destination.substring(destination.length() - 1);
		}
		return newOrigin + "" + position + "" + newDestination;

	};
	Function<IMutation4Rec, String> printAllMutationInfo = mutation -> {
		return mutation.toString();
	};
	protected JMenuItem showInRightByCurrentRoot;
	protected JMenuItem showInRightByOriginalRoot;
	private JMenuItem clearAnnotatonJmenu4internalNode;

	protected final String BUTTOM_HTML_STRING = "<br><i>Right click or left click with alt to see more information<br>"
			+ "Right click and open new tab to see clade</i>";
	private JMenuItem pasteNodeAsSiblingJMenuItem;
	private JMenuItem detachNodeMenuItem;
	private JMenuItem clearAnnotaton4blinkArea;
	private JMenuItem clearCollapseNode;
	private JMenuItem refreshPhylogeneticTreePanel;
	private JMenuItem recover2defaultGraphicEffects;
	private JMenuItem removeAllSelections;
	private JMenuItem removeCustomizedColors;
	protected JMenuItem selectDescendants;
	private JMenuItem exportItem;
	private JMenuItem refreshMutationFreq;

	private JMenuItem displayNodeNameMenuItem;

	private PhylogeneticTreePanel phylogeneticTreePanel;

	public TreePopupMenu(PhylogeneticTreePanel phylogeneticTreePanel) {
		this.phylogeneticTreePanel = phylogeneticTreePanel;

	}

	public TreeLayoutProperties getTreeLayoutProperties() {
		if (treeLayoutProperties == null) {
			treeLayoutProperties = phylogeneticTreePanel.getLayoutProperties();
		}
		return treeLayoutProperties;
	}

	private JMenuItem detachNodeJMenuItem() {

		detachNodeMenuItem = new JMenuItem("Detach node");
		detachNodeMenuItem.setToolTipText("Detach this node or cut this node.");
		detachNodeMenuItem.addActionListener(e -> {
			currNode.getParent().removeChild(currNode);
			this.phylogeneticTreePanel.refreshViewPort();
		});

		return detachNodeMenuItem;
	}

	private JMenuItem getDisplayNodeNameMenuItem() {
		if (displayNodeNameMenuItem == null) {
			displayNodeNameMenuItem = new JMenuItem("Display/Hide node name");
			displayNodeNameMenuItem.setToolTipText("Display or hide node name inidividually");
			displayNodeNameMenuItem.addActionListener(e -> {

				List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
				if (!selectedNodes.isEmpty()) {
					for (GraphicsNode graphicsNode : selectedNodes) {
						TreeDrawUnit drawUnit = graphicsNode.getDrawUnit();
						drawUnit.setDrawName(!drawUnit.isDrawName());
					}

					this.phylogeneticTreePanel.weakRefreshViewPort();
				} else {
					TreeDrawUnit drawUnit = currNode.getDrawUnit();
					drawUnit.setDrawName(!drawUnit.isDrawName());
					this.phylogeneticTreePanel.weakRefreshViewPort();
				}

			});

		}

		return displayNodeNameMenuItem;
	}

// 这个功能暂时都去掉了
//	private JMenuItem pasteNodeAsSiblingJMenuItem() {
//
//		pasteNodeAsSiblingJMenuItem = new JMenuItem("Paste node as sibling");
//		pasteNodeAsSiblingJMenuItem.setToolTipText("Detach this node or cut this node.");
//		pasteNodeAsSiblingJMenuItem.addActionListener(e -> {
//			SwingUtilities.invokeLater(() -> {
//				GraphicsNode element = tempDebugTuple.getElement();
//				currNode.getParent().addChild(element);
//				tempDebugTuple.setElement(null);
//				controller.reCalculateTreeLayoutAndRefreshViewPort();
//			});
//		});
//
//		return pasteNodeAsSiblingJMenuItem;
//	}

	public void displayMoreInformation4leaf(GraphicsNode selectedNode) {
		List<String> lists = new ArrayList<>();
		lists.add("Name: ".concat(selectedNode.getName()));
		lists.add("Branch length: ".concat(String.valueOf(selectedNode.getLength())));

		DialogMoreInfoGenerator dialog = new DialogMoreInfoGenerator(lists);
		dialog.setTitle("Leaf node information");
		dialog.setVisible(true);
	}

	protected String getTitle(GraphicsNode currNode2) {

		String str = null;
		if (currNode2.getChildCount() == 0) {
			str = currNode2.getName();
		} else {
			str = currNode2.getCGBID();
		}
		return "$axis:title=".concat(str);
	}

	private JMenuItem getRecolorJMenuItem4all() {
		JMenuItem recolor = new JMenuItem("Recolor...");
		recolor.setIcon(IconObtainer.get("color.png"));
		recolor.setToolTipText("Recolor this node and its descendents, including internal nodes and leafs.");
		ActionListener recolorActionListener = e -> {
//			CustomizedRenderUtil.actions4Recolor2all(controller, currNode);
		};
		recolor.addActionListener(recolorActionListener);
		return recolor;
	}

	private JMenuItem getZoomInThisNodeJMenuItem() {
		JMenuItem zoomInThisNode = new JMenuItem("Zoom to see node");
		zoomInThisNode.setIcon(IconObtainer.get("zoomIn2.png"));
		zoomInThisNode.setToolTipText("Zoom in the area of this node.");
		zoomInThisNode.addActionListener(e -> {
			List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
			if (selectedNodes.size() > 0) {
				phylogeneticTreePanel.discreteZoomInOrOut(2500, 2500);
				phylogeneticTreePanel.refreshViewPort();
			}
		});
		return zoomInThisNode;
	}

	/**
	 * 内节点右击时显示的菜单栏
	 * 
	 * @return
	 */
	protected JPopupMenu getPopInternalNodeRightClick() {

		popInternalNodeRightClick = new JPopupMenu();

		JMenuItem showInRightByCurrentRoot = getShowInRightByCurrentRootJMenuItem();


		JMenuItem annotation = new JMenuItem("Annotation...");
		annotation.setToolTipText("Annotate this clade.");
		annotation.setIcon(IconObtainer.get("Annotation.png"));
		annotation.addActionListener(e -> {
			NodeAnnotationDialogContainer nodeAnnotationDialog = new NodeAnnotationDialogContainer(
					phylogeneticTreePanel, currNode);
			phylogeneticTreePanel.setIsShowAnnotation(false);
			nodeAnnotationDialog.setVisible(true);
		});

		JMenuItem ladderizeUp = new JMenuItem("Ladderize up");
		ladderizeUp.setIcon(IconObtainer.get("ladderizeUp.png"));
		ladderizeUp.setToolTipText("Ladderize up this clade.");
		ladderizeUp.addActionListener(e -> {
			SwingUtilities.invokeLater(() -> {
				EvolNodeUtil.ladderizeNodeAccording2sizeAndLength(currNode, true);
				phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
			});
		});

		JMenuItem ladderizeDown = new JMenuItem("Ladderize down");
		ladderizeDown.setIcon(IconObtainer.get("ladderizeDown.png"));
		ladderizeDown.setToolTipText("Ladderize down this clade.");
		ladderizeDown.addActionListener(e -> {
			SwingUtilities.invokeLater(() -> {
				EvolNodeUtil.ladderizeNodeAccording2sizeAndLength(currNode, false);
				phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
			});
		});

		JMenuItem collapseNode = new JMenuItem("Collapse");
		collapseNode.setIcon(IconObtainer.get("collapse.png"));
		collapseNode.setToolTipText("Collapse this node.");
		collapseNode.addActionListener(e -> {
			collapseThisNode(currNode);
		});

		JMenuItem clearCollapseNode = new JMenuItem("Un-collapse this node");
		clearCollapseNode.setIcon(IconObtainer.get("recoverCollapse.png"));
		clearCollapseNode.setToolTipText("Recover this collapsed node.");
		clearCollapseNode.addActionListener(e -> {
			unCollapseThisNode(currNode);
		});

		JMenuItem swapNodeJMenuItem = new JMenuItem("Swap nodes");
		swapNodeJMenuItem.setIcon(IconObtainer.get("swapNode.png"));
		swapNodeJMenuItem.setToolTipText("Swap this node with its sibling.");
		swapNodeJMenuItem.addActionListener(e -> {
			SwingUtilities.invokeLater(() -> {
				swapNode(currNode);
				phylogeneticTreePanel.updatePhylogeneticTreePanel();
			});
		});

		clearAnnotatonJmenu4internalNode = new JMenuItem("Clear annotions");
		clearAnnotatonJmenu4internalNode.setIcon(IconObtainer.get("remove.png"));
		clearAnnotatonJmenu4internalNode.setToolTipText("Clear annotations located in this node or descents.");
		clearAnnotatonJmenu4internalNode.addActionListener(e -> {
			Set<GraphicsNode> nodeSet = new HashSet<>();
			TreeOperationUtil.recursiveIterateTreeIF(currNode, node -> {
				nodeSet.add(node);
			});
			phylogeneticTreePanel.getMainAnnotationsProperties().clearSelectedAnnotation(nodeSet);
			phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
		});

		JMenuItem displayNodeInformation = new JMenuItem("Display more information");
		displayNodeInformation.setIcon(IconObtainer.get("information.png"));
		displayNodeInformation.setToolTipText("Display more information on node to closer see the details!");
		displayNodeInformation.addActionListener(e -> {
			displayMoreInformation4internalNode(currNode);
		});

		JMenuItem outputLeavesInforJMenu = new JMenuItem("Export leaf information");
		outputLeavesInforJMenu.setIcon(IconObtainer.get("Export.png"));
		outputLeavesInforJMenu.setToolTipText("Export leaf information as tsv format.");
		outputLeavesInforJMenu.addActionListener(e -> {

			EGPSFileChooser egpsFileChooser = new EGPSFileChooser(this.getClass());
			FileNameExtensionFilter filter = new FileNameExtensionFilter("text(.txt)", "txt");
			egpsFileChooser.setFileFilter(filter);
			int showOpenDialog = egpsFileChooser.showSaveDialog(UnifiedAccessPoint.getInstanceFrame());
			if (showOpenDialog == EGPSFileChooser.APPROVE_OPTION) {
				exportLeavesInformation(egpsFileChooser.getSelectedFile());
			}

		});

		JMenuItem selectAllDescendant = new JMenuItem("Select all descendant nodes");
		selectAllDescendant.setIcon(IconObtainer.get("selectall.png"));
		selectAllDescendant.addActionListener(e -> {
			selectedAllDescendants4SingleInternalNode(currNode);
		});

		JMenuItem clearSelect = new JMenuItem("Set all descendant nodes unselected");
		clearSelect.setIcon(IconObtainer.get("clearselect.png"));
		clearSelect.addActionListener(e -> {
			clearSelcetionAllDescendants4SingleInternalNode(currNode);
		});

		popInternalNodeRightClick.add(showInRightByCurrentRoot);
//		JMenuItem showInRightByOriginalRoot = getShowInRightByOriginalRootjMenuItem();
//		popInternalNodeRightClick.add(showInRightByOriginalRoot);

		popInternalNodeRightClick.addSeparator();

		popInternalNodeRightClick.add(getZoomInThisNodeJMenuItem());
		popInternalNodeRightClick.add(selectAllDescendant);
		popInternalNodeRightClick.add(clearSelect);
		popInternalNodeRightClick.addSeparator();

		popInternalNodeRightClick.add(getDisplayNodeNameMenuItem());
		popInternalNodeRightClick.add(displayNodeInformation);
		popInternalNodeRightClick.add(outputLeavesInforJMenu);
		popInternalNodeRightClick.addSeparator();

		popInternalNodeRightClick.add(swapNodeJMenuItem);
		popInternalNodeRightClick.add(ladderizeUp);
		popInternalNodeRightClick.add(ladderizeDown);
		popInternalNodeRightClick.addSeparator();

		popInternalNodeRightClick.add(collapseNode);
		popInternalNodeRightClick.add(clearCollapseNode);
		popInternalNodeRightClick.addSeparator();

		popInternalNodeRightClick.add(annotation);
		popInternalNodeRightClick.add(clearAnnotatonJmenu4internalNode);
		popInternalNodeRightClick.addSeparator();

		JMenuItem branchColor = getRecolorJMenuItem4all();
		popInternalNodeRightClick.add(branchColor);

		popInternalNodeRightClick.addSeparator();
		popInternalNodeRightClick.add(detachNodeJMenuItem());

//		popInternalNodeRightClick.addSeparator();
//		popInternalNodeRightClick.add(getBackMutationPresenterItem());
//		popInternalNodeRightClick.addSeparator();
//		popInternalNodeRightClick.add(getDisplayGenomicMutationPresenterItem());
//		popInternalNodeRightClick.add(getDisplayAAMutationPresenterItem());

		return popInternalNodeRightClick;
	}

	protected void exportLeavesInformation(File localFinalFile) {
		final File file = localFinalFile;
	}

	/**
	 * 
	 * 右击节点，选择collapse时会调用此方法
	 * 
	 * @title collapseSingleInternalNode
	 * @lastModifiedDate 2022-11-30 13:47
	 * @author
	 * @since 1.7
	 * 
	 * @param node
	 * @return void
	 */
	public void collapseThisNode(GraphicsNode node) {
		new Thread(() -> {
			CollapseDialog collapseDialog = new CollapseDialog(node.getDrawUnit().getLineColor());
			collapseDialog.setCallBackConsumer((color, integer) -> {
				node.setCollapse(true);
				getTreeLayoutProperties().initializeRoot();
				Map<Integer, CollapseProperty> collapseProportyMaps = getTreeLayoutProperties()
						.getCollapsePropertyMaps();
				CollapseProperty collapseProperty = new CollapseProperty();
				collapseProperty.setColor(color.getRed(), color.getGreen(), color.getBlue());
				collapseProperty.setTriangleSize(integer);
				collapseProportyMaps.put(node.getID(), collapseProperty);
				// 现在我们不更新这个突变的Bar了
				// controller.letGenomeViewUpdateStaticBarPlot(controller.getTreeLayoutProperties().getRoot());
				phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
			});
			collapseDialog.setVisible(true);
		}).start();

	}

	/**
	 * 
	 * 右击节点，选择uncollapse时会调用此方法
	 * 
	 * @title unCollapseSingleInternalNode
	 * @createdDate 2022-11-30 13:48
	 * @lastModifiedDate 2022-11-30 13:48
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param node
	 * @return void
	 */
	protected void unCollapseThisNode(GraphicsNode node) {
		Runnable act = () -> {
			node.setCollapse(false);
			getTreeLayoutProperties().initializeRoot();
			Map<Integer, CollapseProperty> collapseProportyMaps = getTreeLayoutProperties().getCollapsePropertyMaps();
			collapseProportyMaps.remove(node.getID());
			phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
		};
	}

	/**
	 * 
	 * 此方法为右击内节点调用的选中所有子节点
	 * 
	 * @title selectedAllDescendants4InternalNode
	 * @createdDate 2022-11-29 14:13
	 * @lastModifiedDate 2022-11-29 14:13
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param currentNode
	 * @return void
	 */
	protected void selectedAllDescendants4SingleInternalNode(GraphicsNode currentNode) {
		TreeOperationUtil.recursiveIterateTreeIF(currentNode, node -> {
			node.getDrawUnit().setNodeSelected(true);
		});
		phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
	}

	/**
	 * 此方法为右击内节点调用的取消选中所有子节点
	 * 
	 * @title clearSelcetionAllDescendants4SingleInternalNode
	 * @createdDate 2022-11-30 14:12
	 * @lastModifiedDate 2022-11-30 14:12
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param currentNode
	 * @return void
	 */
	protected void clearSelcetionAllDescendants4SingleInternalNode(GraphicsNode currentNode) {
		TreeOperationUtil.recursiveIterateTreeIF(currentNode, node -> {
			node.getDrawUnit().setNodeSelected(false);
		});
		phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
	}

	/**
	 * display all information for internal node.
	 * 
	 * @title extracted
	 * @createdDate 2021-01-14 21:07
	 * @lastModifiedDate 2021-01-14 21:07
	 * @author yudalang
	 * @param selectedNode
	 * @since 1.7
	 * 
	 * @return void
	 */
	public void displayMoreInformation4internalNode(GraphicsNode selectedNode) {

		List<String> lists = new ArrayList<>();
		lists.add("Name: ".concat(selectedNode.getName()));
		lists.add("Branch length: ".concat(String.valueOf(selectedNode.getLength())));
		lists.add("Number of child: ".concat(String.valueOf(selectedNode.getChildCount())));
		lists.add("Node size: ".concat(String.valueOf(selectedNode.getSize())));

		DialogMoreInfoGenerator dialog = new DialogMoreInfoGenerator(lists);
		dialog.setTitle("Internal node information");
		dialog.setVisible(true);
	}

	/**
	 * 
	 * 在新的tab中显示当前clade（过滤操作后的状态）的JMenuItem
	 * 
	 * @title getShowInRightByCurrentRootJMenuItem
	 * @createdDate 2020-11-25 15:26
	 * @lastModifiedDate 2020-11-25 15:26
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @return
	 * @return JMenuItem
	 */
	protected JMenuItem getShowInRightByCurrentRootJMenuItem() {

		if (showInRightByCurrentRoot == null) {
			showInRightByCurrentRoot = new JMenuItem("View this clade in new tab");
			showInRightByCurrentRoot.setIcon(IconObtainer.get("tab.png"));
			showInRightByCurrentRoot.setToolTipText("Show this clade in a new tab");
			showInRightByCurrentRoot.addActionListener(e -> {
				List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
				if (selectedNodes.size() > 0) {
					GraphicsNode graphicsNode = selectedNodes.get(0);
					GraphicsNode convertBasicNode2graphicsNode = NodeUtils.convertBasicNode2graphicsNode(graphicsNode);

					IndependentModuleLoader independentModuleLoader = new IndependentModuleLoader();
					independentModuleLoader.setHowModuleLaunchedWithData(
							"Module launched by right click internal node.",
							"Data source is from the ".concat(graphicsNode.getName()));
					/**
					 * treeLayoutProperties 可选，如果输入为null，会自动根据 root生成 treeLayoutProperties
					 * 开发者也可以自己生成 TreeLayoutProperties类示例，进而设置一些参数
					 */
					independentModuleLoader.setModuleData(convertBasicNode2graphicsNode, null);
					MainFrameProperties.loadTheModuleFromIModuleLoader(independentModuleLoader);
				}
			});
		}

		return showInRightByCurrentRoot;
	}

	/**
	 * 
	 * 在新的tab中显示当前clade对应的原始树的JMenuItem
	 * 
	 * @title getShowInRightByOriginalRootjMenuItem
	 * @createdDate 2022-08-09 09:55
	 * @lastModifiedDate 2022-08-09 09:55
	 * @author
	 * @since 1.7
	 * 
	 * @return
	 * @return JMenuItem
	 */
	protected JMenuItem getShowInRightByOriginalRootjMenuItem() {
		if (showInRightByOriginalRoot == null) {
			showInRightByOriginalRoot = new JMenuItem("View this clade (incl. hidden strains) in new tab");
			showInRightByOriginalRoot.setIcon(IconObtainer.get("tab.png"));
			showInRightByOriginalRoot.setToolTipText("Showing this clade of original root in a new tab");
			showInRightByOriginalRoot.addActionListener(e -> {
				List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
				if (selectedNodes.size() > 0) {
					GraphicsNode graphicsNode = selectedNodes.get(0);
					int targetID = graphicsNode.getID();

					GraphicsNode originalRootNode = getTreeLayoutProperties().getOriginalRootNode();

					Tuple<GraphicsNode> node2view = new Tuple<GraphicsNode>(null);
					TreeOperationUtil.recursiveIterateLevelTraverse(originalRootNode, node -> {
						if (node.getID() == targetID) {
							node2view.setElement(node);
						}
					});

					if (node2view.getElement() == null) {
						System.err.println("Internal error: " + getClass());
					} else {
//						controller.getMain().addPhylogeneticTreeTab(node2view.getElement());
					}
				}
			});
		}

		return showInRightByOriginalRoot;
	}

	protected void swapNode(GraphicsNode find) {
		GraphicsNode par = (GraphicsNode) find.getParent();
		int currChildIndex = par.indexOfChild(find);
		int cc = par.getChildCount();
		if (cc > 2) {
			// nodes is the copy of par
			List<GraphicsNode> allChildren = new ArrayList<GraphicsNode>(cc);
			for (int x = 0; x < cc; x++) {
				allChildren.add((GraphicsNode) par.getChildAt(x));
			}
			GraphicsNode child1 = (GraphicsNode) par.getChildAt(currChildIndex);
			GraphicsNode child2 = null;
			if ((currChildIndex + 1) < cc) {
				child2 = (GraphicsNode) par.getChildAt(currChildIndex + 1);
				par.removeAllChild();
				for (int n = 0; n < cc; n++) {
					if (n == currChildIndex) {
						par.addChild(child2);
						par.addChild(child1);
						n = n + 1;
					} else {
						par.addChild(allChildren.get(n));
					}
				}
			} else {
				// this means swap the last node and the first node
				child2 = (GraphicsNode) par.getChildAt(0);
				par.removeAllChild();
				par.addChild(child1);
				for (int n = 1; n < cc - 1; n++) {
					par.addChild(allChildren.get(n));
				}
				par.addChild(child2);
			}
		} else if (cc == 2) {
			GraphicsNode child1 = (GraphicsNode) par.getChildAt(0);
			GraphicsNode child2 = (GraphicsNode) par.getChildAt(1);
			par.removeAllChild();
			par.addChild(child2);
			par.addChild(child1);
		} else {
			// 时间filter后，会有节点下只有一个child的情况，所以增加一个保护
		}

	}

//	public JPopupMenu getLeafInfoPop(GraphicsNode node) {
//
//		JPopupMenu leafNodeInfo = new JPopupMenu();
//		JMenuItem leafNodeInfo_nodeName = new JMenuItem();
//		JMenuItem leafNodeInfo_branchLength = new JMenuItem();
//		JMenuItem leaftNodeInfo_mutation = new JMenuItem();
//		JMenuItem leaftNodeInfo_date = new JMenuItem();
//		JMenuItem leaftNodeInfo_age = new JMenuItem();
//		JMenuItem leaftNodeInfo_sexul = new JMenuItem();
//		JMenuItem leaftNodeInfo_country = new JMenuItem();
//		JMenuItem leaftNodeInfo_province = new JMenuItem();
//		leafNodeInfo.add(leafNodeInfo_nodeName);
//		leafNodeInfo.add(leafNodeInfo_branchLength);
//		leafNodeInfo.add(leaftNodeInfo_mutation);
//		leafNodeInfo.add(leaftNodeInfo_date);
//		leafNodeInfo.add(leaftNodeInfo_age);
//		leafNodeInfo.add(leaftNodeInfo_sexul);
//		leafNodeInfo.add(leaftNodeInfo_country);
//		leafNodeInfo.add(leaftNodeInfo_province);
//		leafNodeInfo.setFocusable(false);
//
//		String[] leafTooltipInformation = getLeafTooltipInformation(node);
//		leafNodeInfo_nodeName.setText(leafTooltipInformation[0]);
//		leafNodeInfo_branchLength.setText(leafTooltipInformation[1]);
//		leaftNodeInfo_mutation.setText(leafTooltipInformation[2]);
//		leaftNodeInfo_date.setText(leafTooltipInformation[3]);
//		leaftNodeInfo_age.setText(leafTooltipInformation[4]);
//		leaftNodeInfo_sexul.setText(leafTooltipInformation[5]);
//		leaftNodeInfo_country.setText(leafTooltipInformation[6]);
//		leaftNodeInfo_province.setText(leafTooltipInformation[7]);
//
//		return leafNodeInfo;
//	}

	/**
	 * 注意顺序！
	 *
	 * @param node
	 * @return
	 * @throws ParseException
	 */
	public List<String> getLeafTooltipInformation(GraphicsNode node, boolean showAllOrigin) {
		List<String> informations = new ArrayList<>();

		informations.add("Leaf name: ".concat(String.valueOf(node.getName())));
		informations.add("CGB ID: ".concat(String.valueOf(node.getCGBID())));
		informations.add("Branch length:  ".concat(String.valueOf(getBranchLength(node))));

		informations.add("");

		informations.add("");

//		DateTimeFormatter humanReadableDataFormat = controller.getHumanReadableDataFormat();

		informations.add(BUTTOM_HTML_STRING);

		if (debug) {
			informations.add("");
			informations.add("Debug information:");
			informations.add("Node ID: " + node.getID());
		}
		return informations;
	}

	protected int getNumOfChildren(GraphicsNode node) {
		return node.getChildCount();
	}

	protected String getBranchLength(GraphicsNode node) {

		double displayedBranchLength = node.getDisplayedBranchLength();
		String ret = deciFor.format(displayedBranchLength);

		BaseLayout currLayout = phylogeneticTreePanel.getCurrLayout();
		BranchLengthType branchLengthType = currLayout.getBranchLengthType();

		switch (branchLengthType) {
		case DIVERGENCE:

			break;
		case MUTATION_COUNT:
			if (displayedBranchLength > 1) {
				ret += " counts";
			} else {
				ret += " count";
			}
			break;
		default:
			break;
		}

		return ret;
	}

	private JMenuItem getClearAnnotaton() {
		if (clearAnnotaton4blinkArea == null) {
			clearAnnotaton4blinkArea = new JMenuItem("Clear all annotions");
			clearAnnotaton4blinkArea.setIcon(IconObtainer.get("remove.png"));
			clearAnnotaton4blinkArea.setToolTipText("Clear all annotations.");
			clearAnnotaton4blinkArea.addActionListener(e -> {
				phylogeneticTreePanel.getMainAnnotationsProperties().clearAllAnnotation();
				phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
			});
		}
		return clearAnnotaton4blinkArea;
	}

	private JMenuItem getClearCollapseNode() {
		if (clearCollapseNode == null) {
			clearCollapseNode = new JMenuItem("Un-collapse all nodes");
			clearCollapseNode.setIcon(IconObtainer.get("recoverCollapse.png"));
			clearCollapseNode.setToolTipText("Recover all collapsed nodes.");
			clearCollapseNode.addActionListener(e -> {
				clearAllCollapsedNodes();
			});
		}
		return clearCollapseNode;
	}

	/**
	 * 
	 * 右击空白处可以选择清空所有collapse的节点
	 * 
	 * @title clearAllCollapsedNodes
	 * @createdDate
	 * @lastModifiedDate 2022-11-30 15:14
	 * @author
	 * @since 1.7
	 * 
	 * @return void
	 */
	protected void clearAllCollapsedNodes() {
		SwingUtilities.invokeLater(() -> {

			treeLayoutProperties.clearCollapseProproties();

			TreeOperationUtil.recursiveIterateTreeIF(getTreeLayoutProperties().getOriginalRootNode(), node -> {
				node.setCollapse(false);
			});

			GraphicsNode rootNode = phylogeneticTreePanel.getRootNode();
			TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
				node.setCollapse(false);
			});

			phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();

		});
	}

	private JMenuItem getRefreshPhylogeneticTreePanel() {
		if (refreshPhylogeneticTreePanel == null) {
			refreshPhylogeneticTreePanel = new JMenuItem("Refresh ( Fit frame )");
			refreshPhylogeneticTreePanel.setIcon(IconObtainer.get("refresh.png"));
			refreshPhylogeneticTreePanel.setToolTipText("Refresh phylogenetic tree panel.");
			refreshPhylogeneticTreePanel.addActionListener(e -> {
				phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
			});
		}
		return refreshPhylogeneticTreePanel;
	}

	private JMenuItem getRecover2defaultGraphicEffects() {
		if (recover2defaultGraphicEffects == null) {
			recover2defaultGraphicEffects = new JMenuItem("Recover all graphics effects");
			recover2defaultGraphicEffects.setIcon(IconObtainer.get("default.png"));
			recover2defaultGraphicEffects.setToolTipText("Recover to default graphic effects.");
			recover2defaultGraphicEffects.addActionListener(e -> {
				recoverAllGraphicsEffects();
			});
		}
		return recover2defaultGraphicEffects;
	}

	protected void recoverAllGraphicsEffects() {
		TreeOperationUtil.recursiveIterateTreeIF(getTreeLayoutProperties().getOriginalRootNode(), node -> {
			node.getDrawUnit().recover2PrimaryStates();
		});

		getTreeLayoutProperties().recover2defaultFont();
		phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
	}

	private JMenuItem getRemoveAllSelectionsJMenuItem() {
		if (removeAllSelections == null) {
			removeAllSelections = new JMenuItem("Cancel all selections");
			removeAllSelections.setIcon(IconObtainer.get("no_select.png"));
			removeAllSelections.setToolTipText("Remove all selections( Added by search and left click.)");
			removeAllSelections.addActionListener(e -> {
				SwingUtilities.invokeLater(() -> {
					removeAllSelections();
				});
			});
		}
		return removeAllSelections;
	}

	/**
	 * 
	 * 清除树上的所有选中
	 * 
	 * @title removeAllSelections
	 * @createdDate
	 * @lastModifiedDate 2022-11-30 15:19
	 * @author
	 * @since 1.7
	 * 
	 * @return void
	 */
	protected void removeAllSelections() {
		List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
		for (GraphicsNode node : selectedNodes) {
			node.getDrawUnit().setBranchSelected(false);
		}
		selectedNodes.clear();
		TreeOperationUtil.recursiveIterateTreeIF(getTreeLayoutProperties().getOriginalRootNode(), node -> {
			TreeDrawUnit drawUnit = node.getDrawUnit();
			if (drawUnit.isNodeSelected()) {
				drawUnit.setNodeSelected(false);
			}
		});

		phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
	}

	private JMenuItem getRemoveCustomizedColors() {
		if (removeCustomizedColors == null) {
			removeCustomizedColors = new JMenuItem("Remove customized colors");
			removeCustomizedColors.setIcon(IconObtainer.get("cancel_recolor.png"));
			removeCustomizedColors
					.setToolTipText("Remove customized colors( Added by right click or allele freq. bar.)");
			removeCustomizedColors.addActionListener(e -> {
				SwingUtilities.invokeLater(() -> {
//					int beforeRenderingIndex = controller.getCustomizedSaveBean().getBeforeRenderingIndex();
					// controller.changeColorScheme(ColorScheme.getColorSchemeAccording2index(beforeRenderingIndex));
					// color scheme的面板的选择状态也要修改，所以就直接修改选择状态好了。选择状态修改了，会自动调用changeColorScheme()的
//					controller.getLeftControlPanelContainner().setCustomizedButtonSelected(beforeRenderingIndex);
				});
			});
		}
		return removeCustomizedColors;
	}

	/**
	 * 
	 * 此方法为右击空白处调用
	 * 
	 * @title getSelectDescendants
	 * @createdDate 2022-11-29 14:11
	 * @lastModifiedDate 2022-11-29 14:11
	 * @author
	 * @since 1.7
	 * 
	 * @return
	 * @return JMenuItem
	 */
	protected JMenuItem getSelectDescendantsJMenuItem() {
		if (selectDescendants == null) {
			selectDescendants = new JMenuItem("Select descendants of marked nodes");
			selectDescendants.setIcon(IconObtainer.get("selectall.png"));
			selectDescendants.setToolTipText("Select all descendants of selected nodes.");
			selectDescendants.addActionListener(e -> {
				selectAllDescendants();
			});
		}
		return selectDescendants;
	}

	/**
	 * 
	 * 选中所有当前选中节点的子节点
	 * 
	 * @title selectAllDescendants
	 * @createdDate
	 * @lastModifiedDate 2022-11-30 19:47
	 * @author
	 * @since 1.7
	 * 
	 * @return void
	 */
	protected void selectAllDescendants() {

		TreeOperationUtil.recursiveIterateTreeIF(getTreeLayoutProperties().getOriginalRootNode(), node -> {
			if (node.getDrawUnit().isNodeSelected()) {
				TreeOperationUtil.recursiveIterateTreeIF(node, inner -> {
					// System.out.println(node.getID() + "true");
					inner.getDrawUnit().setNodeSelected(true);
				});
				return true;
			} else {
				return false;
			}
		});

		phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
	}

	private JMenuItem getExportItem() {
		if (exportItem == null) {
			exportItem = new JMenuItem("Export");
			exportItem.setIcon(IconObtainer.get("Export.png"));
			exportItem.setToolTipText("Export pictures or data.");
			exportItem.addActionListener(e -> {
				exportPicturesOrData();
			});
		}
		return exportItem;
	}

	protected void exportPicturesOrData() {

//		controller.saveViewPanelAs();
	}

	private JMenuItem getRefreshMutationFreq() {
		if (refreshMutationFreq == null) {
			refreshMutationFreq = new JMenuItem("Refresh mutation freq.");
			// refreshMutationFreq.setIcon(SarsCov2Icons.get("Export.png"));
			refreshMutationFreq.setToolTipText("Refresh the mutation freq tarck in the genome browser.");
			refreshMutationFreq.addActionListener(e -> {
				refreshMutationFrequency();
			});
		}
		return refreshMutationFreq;
	}

	public void refreshMutationFrequency() {
		Runnable act = () -> {
//			controller.letGenomeViewUpdateStaticBarPlot(treeLayoutProperties.getOriginalRootNode());
		};
	}

	public JPopupMenu getLeafRightClickPop(GraphicsNode currNodes, Component pan) {
		currNode = currNodes;
		if (popLeafRightClick == null) {
			assembleLeafRightClickPopMenu();
		}
		return popLeafRightClick;
	}

	protected void assembleLeafRightClickPopMenu() {

		popLeafRightClick = new JPopupMenu();
		JMenuItem recolor = getRecolorJMenuItem4all();

		JMenuItem displayNodeInformation = new JMenuItem("Display more information");
		displayNodeInformation.setIcon(IconObtainer.get("information.png"));
		displayNodeInformation.addActionListener(e -> {
			displayMoreInformation4leaf(currNode);
		});

		JMenuItem swapNodeJMenuItem = new JMenuItem("Swap nodes");
		swapNodeJMenuItem.setIcon(IconObtainer.get("swapNode.png"));
		swapNodeJMenuItem.setToolTipText("Swap this node with its sibling.");
		swapNodeJMenuItem.addActionListener(e -> {
			SwingUtilities.invokeLater(() -> {
				swapNode(currNode);
				phylogeneticTreePanel.updatePhylogeneticTreePanel();
			});
		});

		popLeafRightClick.add(getDisplayNodeNameMenuItem());
		popLeafRightClick.add(displayNodeInformation);

		popLeafRightClick.add(getZoomInThisNodeJMenuItem());
		popLeafRightClick.add(recolor);
		popLeafRightClick.addSeparator();
		popLeafRightClick.add(swapNodeJMenuItem);

		if (currNode.getCollapse()) {
			JMenuItem modifyCollapseProperty = new JMenuItem("modify collapse property");
			modifyCollapseProperty.addActionListener(e -> {

				Map<Integer, CollapseProperty> collapseProportyMaps = getTreeLayoutProperties()
						.getCollapsePropertyMaps();

				final CollapseProperty collapseProperty = collapseProportyMaps.get(currNode.getID());

				CollapseDialog collapseDialog = new CollapseDialog(collapseProperty.getColor());

				collapseDialog.setCallBackConsumer((color, integer) -> {
					if (collapseProperty != null) {
						collapseProperty.setColor(color.getRed(), color.getGreen(), color.getBlue());
						collapseProperty.setTriangleSize(integer);
						collapseProportyMaps.put(currNode.getID(), collapseProperty);
						phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
					}
				});
				collapseDialog.setVisible(true);
			});

			popLeafRightClick.add(modifyCollapseProperty);
		}

		popLeafRightClick.add(detachNodeJMenuItem());

//		popLeafRigtClick.addSeparator();
//		popLeafRigtClick.add(getBackMutationPresenterItem());
//		popLeafRigtClick.addSeparator();
//		popLeafRigtClick.add(getDisplayGenomicMutationPresenterItem());
//		popLeafRigtClick.add(getDisplayAAMutationPresenterItem());

//		JMenuItem pasteNodeAsSiblingJMenuItem = pasteNodeAsSiblingJMenuItem();
//		pasteNodeAsSiblingJMenuItem.setEnabled(tempDebugTuple.getElement() != null);
//		popLeafRigtClick.add(pasteNodeAsSiblingJMenuItem);

	}

	public JPopupMenu getInternalNodeRightClickPop(GraphicsNode currNodes) {
		currNode = currNodes;

		if (popInternalNodeRightClick == null) {
			getPopInternalNodeRightClick();
		}
		PhylogeneticTreePanel selectedPhylogeneticTreePanel = phylogeneticTreePanel;
		boolean hasAnnotation = selectedPhylogeneticTreePanel.getMainAnnotationsProperties().hasAnnotation();
		clearAnnotatonJmenu4internalNode.setEnabled(hasAnnotation);

		boolean collapse = currNodes.getCollapse();
		getShowInRightByCurrentRootJMenuItem().setEnabled(!collapse);

		// 下面这个功能现在用不到
//		JMenuItem pasteNodeAsSiblingJMenuItem = pasteNodeAsSiblingJMenuItem();
//		pasteNodeAsSiblingJMenuItem.setEnabled(tempDebugTuple.getElement() != null);

		return popInternalNodeRightClick;
	}

	public JPopupMenu getBlinkAreaPop(Component component, Point point) {
		if (blinkAreaPopupMenu == null) {
			blinkAreaPopupMenu = new JPopupMenu();

			getClearAnnotaton();

			getClearCollapseNode();

			blinkAreaPopupMenu.add(getRefreshPhylogeneticTreePanel());
			blinkAreaPopupMenu.add(getRecover2defaultGraphicEffects());
			blinkAreaPopupMenu.addSeparator();
			blinkAreaPopupMenu.add(clearAnnotaton4blinkArea);
			blinkAreaPopupMenu.add(clearCollapseNode);

			blinkAreaPopupMenu.add(getRemoveAllSelectionsJMenuItem());

			getRemoveCustomizedColors();

			blinkAreaPopupMenu.add(getSelectDescendantsJMenuItem());
//			popLeafRigtClick.add(removeCustomizedColors);

			blinkAreaPopupMenu.addSeparator();

			JMenuItem zoomInThisNode = new JMenuItem("Zoom the area");
			zoomInThisNode.setIcon(IconObtainer.get("zoomIn2.png"));
			zoomInThisNode.setToolTipText("Zoom in the area, adjust the zoom level on the advanced parameters panel.");
			zoomInThisNode.addActionListener(e -> {

				Timer timer = new Timer(200, new ActionListener() {
					int count = 0;

					@Override
					public void actionPerformed(ActionEvent e) {
						phylogeneticTreePanel.continuouslyZoomInOrOut(200, 200, point);
						// 当任务执行5次时，停止定时器
						if (count >= 5) {
							((Timer) e.getSource()).stop();
						}
						count++;
					}
				});

				timer.start(); // 启动定时器

			});
			blinkAreaPopupMenu.add(zoomInThisNode);

		}


		boolean hasAnnotation = phylogeneticTreePanel.getMainAnnotationsProperties().hasAnnotation();
		clearAnnotaton4blinkArea.setEnabled(hasAnnotation);
		boolean hasCollapseNode = hasCollapsedNode();
		clearCollapseNode.setEnabled(hasCollapseNode);
//		removeCustomizedColors.setEnabled(true);

		return blinkAreaPopupMenu;
	}

	/**
	 * 
	 * 判定当前树上是否有collapse的节点
	 * 
	 * @title hasCollapsedNode
	 * @createdDate
	 * @lastModifiedDate 2022-11-30 14:58
	 * @author
	 * @since 1.7
	 * 
	 * @return
	 * @return boolean
	 */
	protected boolean hasCollapsedNode() {
		return !getTreeLayoutProperties().getCollapsePropertyMaps().isEmpty();
	}

	/**
	 * 指定日期后几天
	 *
	 * @param date  指定日期
	 * @param count 天数
	 * @return
	 */
	protected String getAfterDay(LocalDate date, int count, DateTimeFormatter df) {
		LocalDate minusDays = date.plusDays(count);
		return df.format(minusDays);
	}

	/**
	 * 指定日期前几天
	 *
	 * @param date  指定日期
	 * @param count 天数
	 * @return
	 */
	protected String getBeforeDay(LocalDate date, int count, DateTimeFormatter df) {
		LocalDate minusDays = date.minusDays(count);
		return df.format(minusDays);
	}

}
