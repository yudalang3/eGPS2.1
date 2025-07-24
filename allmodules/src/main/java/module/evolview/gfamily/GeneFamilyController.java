package module.evolview.gfamily;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.JColorChooser;
import javax.swing.SwingUtilities;

import utils.datetime.LocalDateHandler;
import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;
import evoltree.struct.util.EvolNodeUtil;
import module.evolview.gfamily.work.gui.ControlPanelContainner;
import module.evolview.gfamily.work.gui.CtrlGenomeBrowserPanel;
import module.evolview.gfamily.work.gui.CtrlTreeLayoutPanel;
import module.evolview.gfamily.work.gui.CtrlTreeOperationPanelByMiglayout;
import module.evolview.gfamily.work.gui.TreeLayoutSwitcher;
import module.evolview.gfamily.work.gui.render.CustomizedSaveBean;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNode2LeafAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNodeInsituAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropLeafNameAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropOutterSidewardAnno;
import module.evolview.gfamily.work.model.enums.BranchLengthType;
import module.evolview.gfamily.work.model.enums.ColorScheme;
import module.evolview.gfamily.work.model.tree.AnnotationsProperties;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.ShowLeafPropertiesInfo;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.layout.BaseLayout;

public class GeneFamilyController {
    final GeneFamilyMainFace main;

    protected PhylogeneticTreePanel globalPhylogeneticTreePanel;
    protected TreeLayoutProperties treeLayoutProperties;

    protected final Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
    protected final Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

    public static final DecimalFormat HUMAN_DECIMAL_INTEGER_FORMAT = new DecimalFormat(",###,###");
    public static final DateTimeFormatter HUMAN_READABLE_FORMAT = DateTimeFormatter.ofPattern("MMMM dd, yyyy",
            Locale.ENGLISH);

    private CtrlGenomeBrowserPanel leftGenomeBrowser;

    private LocalDateHandler dateHandler = new LocalDateHandler();

    private TreeLayoutSwitcher toplayoutSwither;
    private ControlPanelContainner leftControlPanelContainner;

    private final CustomizedSaveBean customizedSaveBean = new CustomizedSaveBean();

    public GraphicsNode cloneOriginalRootNode;
    private GraphicsNode wholeDataRoot;

    protected MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();

    public Font getGlobalFont() {
        return globalFont;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public GeneFamilyController(GeneFamilyMainFace main) {
        this.main = main;
    }

    public PhylogeneticTreePanel getGlobalPhylogeneticTreePanel() {
        return globalPhylogeneticTreePanel;
    }

    public void setGlobalPhylogeneticTreePanel(PhylogeneticTreePanel phylogeneticTreePanel) {
        this.globalPhylogeneticTreePanel = phylogeneticTreePanel;
    }

    public PhylogeneticTreePanel getSelectedPhylogeneticTreePanel() {
        return main.getSelectedPhylogeneticTreePanel();
    }

    public TreeLayoutProperties getTreeLayoutProperties() {
        return treeLayoutProperties;
    }

    public void setTreeLayoutProperties(TreeLayoutProperties treeLayoutProperties) {
        this.treeLayoutProperties = treeLayoutProperties;
    }

    public GeneFamilyMainFace getMain() {
        return main;
    }

    public void reCalculateTreeLayoutAndRefreshViewPort() {
        List<PhylogeneticTreePanel> list = main.getExistedPhylogeneticTreePanels();
        for (PhylogeneticTreePanel phylogeneticTreePanel : list) {
            phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
        }
    }

    /**
     * This is for mouse wheel move event。 这个是需要连续变化的！
     *
     * @param heightChange
     * @param widthChange
     * @param point
     */
    public void continuouslyZoomInOrOut(int heightChange, int widthChange, Point point) {
        PhylogeneticTreePanel selectedPhylogeneticTreePanel = getSelectedPhylogeneticTreePanel();
        selectedPhylogeneticTreePanel.continuouslyZoomInOrOut(heightChange, widthChange, point);
        refreshPhylogeneticTree();

    }

//    public void discreteZoomInOrOut(int heightChange, int widthChange) {
//        PhylogeneticTreePanel selectedPhylogeneticTreePanel = getSelectedPhylogeneticTreePanel();
//        selectedPhylogeneticTreePanel.discreteZoomInOrOut(heightChange, widthChange);
//        selectedPhylogeneticTreePanel.refreshViewPort();
//    }

    /**
     * 重新设置 放大缩小面板的属性
     *
     * @param newHeight 新的值，百分比
     * @param newWidth  新的值，百分比
     */
    public void resetZoomLayoutPanel(int newHeight, int newWidth) {
        getTreeLayoutPanel().resetZoomLayoutPanel(newHeight, newWidth);
    }

    public void refreshPhylogeneticTree() {
        SwingUtilities.invokeLater(() -> {
            getSelectedPhylogeneticTreePanel().refreshViewPort();
        });
    }

    public void weakRefreshPhylogeneticTree() {
        SwingUtilities.invokeLater(() -> {
            getSelectedPhylogeneticTreePanel().weakRefreshViewPort();
        });
    }

    public void letPhyloTreeHighlightNodesAndLines(int pos) {
//		HighlightNode highlightNode = new HighlightNode(this);
//		highlightNode.highlight(pos);
//		getSelectedPhylogeneticTreePanel().updatePhylogeneticTreePanel();
    }

    public void letGenomeViewUpdateStaticBarPlot(GraphicsNode node) {
//		this.main.calculatorBarPlotStatic(node);
//		this.getDrawProperties().setReCalculator(true);
//		refreshPhylogeneticTree();
//		refreshViewGenomeBrowser();
    }

    public void letGenomeViewUpdateStaticBarPlot(GraphicsNode node, List<Double> scores) {
//		this.main.calculatorBarPlotStatic(node, scores);
//		this.getDrawProperties().setReCalculator(true);
//		refreshPhylogeneticTree();
//		refreshViewGenomeBrowser();
    }

    public void refreshViewGenomeBrowser() {
        main.getSelectedBrowserPanel().updateUI();
    }

    /**
     * 注意，恢复折叠的节点的话，原始的
     */

    public void clearAllCollapseNodes() {
        treeLayoutProperties.clearCollapseProproties();

        TreeOperationUtil.recursiveIterateTreeIF(getTreeLayoutProperties().getOriginalRootNode(), node -> {
            node.setCollapse(false);
        });

        List<PhylogeneticTreePanel> existedPhylogeneticTreePanels = main.getExistedPhylogeneticTreePanels();
        for (PhylogeneticTreePanel phylogeneticTreePanel : existedPhylogeneticTreePanels) {
            GraphicsNode rootNode = phylogeneticTreePanel.getRootNode();
            TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
                node.setCollapse(false);
            });
        }

        reCalculateTreeLayoutAndRefreshViewPort();
    }

    public void changeColorForSelectedNodesInTreePanel() {

        List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
        GraphicsNode selectedNode = null;
        if (selectedNodes.size() > 0) {
            selectedNode = selectedNodes.get(0);
        } else {
            selectedNode = treeLayoutProperties.getOriginalRootNode();
        }

        Color lineColor = selectedNode.getDrawUnit().getLineColor();
        Color color = JColorChooser.showDialog(instanceFrame, "Recolor", lineColor);

        if (color == null) {
            return;
        }

        TreeOperationUtil.recursiveIterateTreeIF(selectedNode, node -> {
            node.getDrawUnit().setLineColor(color);
        });

        refreshPhylogeneticTree();

    }

    /**
     * 点击tree operation面板中的选中节点的枝加粗后调用此方法
     */
    public void increaseLineThinknessForSelectedNodesInTreePanel(boolean onlyLeaf) {
        List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();

        List<GraphicsNode> node2operate = new LinkedList<>();

        if (selectedNodes.size() > 0) {
            node2operate.addAll(selectedNodes);
        } else {
            GraphicsNode root = treeLayoutProperties.getOriginalRootNode();
            node2operate.add(root);
        }

        for (GraphicsNode graphicsNode : node2operate) {
            GraphicsNode determinedNode = onlyLeaf ? (GraphicsNode) EvolNodeUtil.getFirstLeaf(graphicsNode) : graphicsNode;
            final float thinkness = determinedNode.getDrawUnit().getStrokeSize() + 1;
            TreeOperationUtil.recursiveIterateTreeIF(graphicsNode, node -> {
                if (onlyLeaf) {
                    if (node.getChildCount() == 0) {
                        node.getDrawUnit().setStrokeSize(thinkness);
                    }
                } else {
                    node.getDrawUnit().setStrokeSize(thinkness);
                }

            });

        }
        refreshPhylogeneticTree();

    }

    /**
     * 点击tree operation面板中的选中节点的枝变细后调用此方法
     */
    public void decreaseLineThinknessForSelectedNodesInTreePanel(boolean onlyLeaf) {
        List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();

        List<GraphicsNode> node2operate = new LinkedList<>();

        if (selectedNodes.size() > 0) {
            node2operate.addAll(selectedNodes);
        } else {
            GraphicsNode root = treeLayoutProperties.getOriginalRootNode();
            node2operate.add(root);
        }

        for (GraphicsNode graphicsNode : node2operate) {
            GraphicsNode determinedNode = onlyLeaf ? (GraphicsNode) EvolNodeUtil.getFirstLeaf(graphicsNode) : graphicsNode;
            final float thinkness = determinedNode.getDrawUnit().getStrokeSize() - 1;
            if (thinkness < 0) {
                break;
            }
            TreeOperationUtil.recursiveIterateTreeIF(graphicsNode, node -> {
                if (onlyLeaf) {
                    if (node.getChildCount() == 0) {
                        node.getDrawUnit().setStrokeSize(thinkness);
                    }
                } else {
                    node.getDrawUnit().setStrokeSize(thinkness);
                }

            });

        }
        refreshPhylogeneticTree();

    }

    /**
     * 点击tree operation面板中的选中节点的变大后调用此方法
     *
     * @param onlyLeaf 右键表明只操作叶子，左键是所有都操作。
     */
    public void increaseSizeForSelectedNodesInTreePanel(boolean onlyLeaf) {
        List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();

        List<GraphicsNode> node2operate = new LinkedList<>();

        if (selectedNodes.size() > 0) {
            node2operate.addAll(selectedNodes);
        } else {
            GraphicsNode root = treeLayoutProperties.getOriginalRootNode();
            node2operate.add(root);
        }

        for (GraphicsNode graphicsNode : node2operate) {

            GraphicsNode determinedNode = onlyLeaf ? (GraphicsNode) EvolNodeUtil.getFirstLeaf(graphicsNode) : graphicsNode;

            final int circleRadius = determinedNode.getDrawUnit().getCircleRadius() + 1;
            if (circleRadius < 1) {
                break;
            }
            TreeOperationUtil.recursiveIterateTreeIF(graphicsNode, node -> {
                if (onlyLeaf) {
                    if (node.getChildCount() == 0) {
                        node.getDrawUnit().setCircleRadius(circleRadius);
                    }
                } else {
                    node.getDrawUnit().setCircleRadius(circleRadius);
                }
            });
        }

        refreshPhylogeneticTree();

    }

    /**
     * 点击tree operation面板中的选中节点的变小后调用此方法
     *
     * @param onlyLeaf
     */
    public void decreaseSizeForSelectedNodesInTreePanel(boolean onlyLeaf) {
        List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();

        List<GraphicsNode> node2operate = new LinkedList<>();

        if (selectedNodes.size() > 0) {
            node2operate.addAll(selectedNodes);
        } else {
            GraphicsNode root = treeLayoutProperties.getOriginalRootNode();
            node2operate.add(root);
        }

        for (GraphicsNode graphicsNode : node2operate) {
            GraphicsNode determinedNode = onlyLeaf ? (GraphicsNode) EvolNodeUtil.getFirstLeaf(graphicsNode) : graphicsNode;
            final int circleRadius = determinedNode.getDrawUnit().getCircleRadius() - 1;
            if (circleRadius < 0) {
                break;
            }
            TreeOperationUtil.recursiveIterateTreeIF(graphicsNode, node -> {
                if (onlyLeaf) {
                    if (node.getChildCount() == 0) {
                        node.getDrawUnit().setCircleRadius(circleRadius);
                    }
                } else {
                    node.getDrawUnit().setCircleRadius(circleRadius);
                }
            });
        }

        refreshPhylogeneticTree();


    }

    public void searchForSpecificLeavesInTreePanel(String text) {
        List<GraphicsNode> leaves = getTreeLayoutProperties().getLeaves();
        for (GraphicsNode graphicsNode : leaves) {
            if (graphicsNode.getName().equalsIgnoreCase(text)) {
                graphicsNode.getDrawUnit().setBranchSelected(true);
                break;
            }
        }
        refreshPhylogeneticTree();
    }

    public void setLeftGenomeBrowser(CtrlGenomeBrowserPanel panel) {
        this.leftGenomeBrowser = panel;
    }

    public CtrlGenomeBrowserPanel getLeftGenomeBrowser() {
        return leftGenomeBrowser;
    }

    /**
     * 在Tree operation面板中点击搜索按钮（btnSearch按钮）后监听跳转到该方法，<br>
     * 根据text内容进行节点搜索
     */

    public void searchForNode(String text) {

//		final String text4search = text.trim();
//		new Thread(() -> {
//			showGlobalTreePanel();
//
//			boolean searchNode = true;
//
//			try {
////				searchNode = getSearchNode3().searchNode(text4search, treeLayoutProperties.getRoot());
//			} catch (Exception e) {
//				SwingDialog.showInforMSGDialog(instanceFrame, "Input error", e.getMessage());
//				return;
//			}
//
//			refreshPhylogeneticTree();
//
//			if (!searchNode) {
//
//				String ss = "Sorry, can not found any nodes with " + text + ".";
//				SwingDialog.showInforMSGDialog(instanceFrame, "Node not found", ss);
//			}
//		}).start();

    }

    public void clearSearchedNode() {
        TreeOperationUtil.recursiveIterateTreeIF(treeLayoutProperties.getOriginalRootNode(), node -> {
            node.getDrawUnit().setNodeSelected(false);
        });
        refreshPhylogeneticTree();
    }

    /**
     * 在Tree operation面板中点击show leave label的图标后激发的动作
     */
    public void showLeaveLabel(boolean selected) {

        ShowLeafPropertiesInfo showLeafPropertiesInfo = treeLayoutProperties.getShowLeafPropertiesInfo();
        List<GraphicsNode> leaves = treeLayoutProperties.getLeaves();
        for (GraphicsNode graphicsNode : leaves) {
            graphicsNode.getDrawUnit().setDrawName(selected);
        }
        // showLeafPropertiesInfo.isShowLeafLabel() 一定和 selected相反，因为现在已经完成了 leaf label
        // GUI随Properties一起刷新的功能
        if (selected) {
            showLeafPropertiesInfo.setNeedChange4showLabel(true);
            showLeafPropertiesInfo.setNeedChange4hideLabel(false);
        } else {
            showLeafPropertiesInfo.setNeedChange4showLabel(false);
            showLeafPropertiesInfo.setNeedChange4hideLabel(true);
        }
        showLeafPropertiesInfo.setShowLeafLabel(selected);

    }

    public void setBranchLengthType(BranchLengthType branchLengthType) {
//
//		List<PhylogeneticTreePanel> list = main.getExistedPhylogeneticTreePanels();
//
//		for (PhylogeneticTreePanel phylogeneticTreePanel : list) {
//			GraphicsNode root = phylogeneticTreePanel.getRootNode();
//
//			assignBranchLengthAccording2lengthType(branchLengthType, root);
//			
//			phylogeneticTreePanel.getCurrLayout().setBranchLengthType(branchLengthType);
//		}
//
//		reCalculateTreeLayoutAndRefreshViewPort();
    }

    public void assignBranchLengthAccording2lengthType(BranchLengthType branchLengthType, GraphicsNode root4visulize) {
        switch (branchLengthType) {
            case MUTATION_COUNT:
                setBranchLengthMutation(root4visulize);
                break;
            default:
                // Divergence
//			setBranchLengthDivergence(root4visulize);
                break;
        }
    }

    /**
     * 在Branch display面板中Branch length选择Mutation后激发的动作
     */
    private void setBranchLengthMutation(GraphicsNode root) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * 在Branch display面板中勾选Branch label后激发的动作
     */
    public void showBranchLabel(boolean isSelect, boolean highlightBacMuts, int labelIndex) {
        getTreeLayoutProperties().setShowBranchLabel(isSelect, highlightBacMuts, labelIndex);
        reCalculateTreeLayoutAndRefreshViewPort();
    }

    public GraphicsNode getWholeDataRoot() {
        return wholeDataRoot;
    }

    public void setWholeDataRoot(GraphicsNode wholeDataRoot) {
        this.wholeDataRoot = wholeDataRoot;
    }

    /**
     * @Title: letGlobalTreePanelChangeRoot @Description:树发生了更新时，传入一个新的根
     * @param: @param newRoot @return: void @throws
     */
    public void letGlobalTreePanelChangeRoot(GraphicsNode newRoot) {
        getTreeLayoutProperties().resetNewRootNode(newRoot);
        globalPhylogeneticTreePanel.resetNewRoot(newRoot);

        // 注释的克隆过来

        AnnotationsProperties mainAnnotationsProperties = getGlobalPhylogeneticTreePanel()
                .getMainAnnotationsProperties();
        mainAnnotationsProperties.setShouldNotConfigurateIfNoGraphicsNode();
        TreeOperationUtil.recursiveIterateTreeIF(newRoot, node -> {
            List<DrawPropInternalNode2LeafAnno> internalNode2LeafAnnos = mainAnnotationsProperties
                    .getInternalNode2LeafAnnos();
            for (DrawPropInternalNode2LeafAnno tt : internalNode2LeafAnnos) {
                tt.substituteNodeIfMeetNodeID(node);
            }
            List<DrawPropOutterSidewardAnno> outterSidewardAnnos = mainAnnotationsProperties.getOutterSidewardAnnos();
            for (DrawPropOutterSidewardAnno tt : outterSidewardAnnos) {
                tt.substituteNodeIfMeetNodeID(node);
            }
            List<DrawPropInternalNodeInsituAnno> internalNode2LeafInsituAnnos = mainAnnotationsProperties
                    .getInternalNode2LeafInsituAnnos();
            for (DrawPropInternalNodeInsituAnno tt : internalNode2LeafInsituAnnos) {
                tt.substituteNodeIfMeetNodeID(node);
            }
            List<DrawPropLeafNameAnno> leafNameAnnos = mainAnnotationsProperties.getLeafNameAnnos();
            for (DrawPropLeafNameAnno tt : leafNameAnnos) {
                tt.substituteNodeIfMeetNodeID(node);
            }
        });
        mainAnnotationsProperties.assignNullIfShouldNotConfig4all();

        // 如果有子树，转到global tree
        showGlobalTreePanel();
        // 重新算突变率
        // 20211211，现在我们发现每次打开软件或者其它的一些操作之后更新这个突变频率 软件变得非常卡顿。
        // letGenomeViewUpdateStaticBarPlot(getTreeLayoutProperties().getRoot());
        globalPhylogeneticTreePanel.updatePhylogeneticTreePanel();

    }

    /**
     * 点击Filter面板的age的slider后调用此方法，根据样本年龄进行过滤
     */
    public void ageFilter(int ageStart, int ageEnd) {

    }

    /**
     * 过滤选中的节点，在进行search或者自己鼠标选中一些节点后，进行节点过滤，仅保留选中的节点
     */
    public void filterSelectedNode() {
//		int filterCount = 0;// 统计过滤所得的叶子节点的个数
//		Tuple<Integer> tuple = new Tuple<Integer>(null);
//		tuple.setElement(filterCount);
////		GraphicsNode cloneRoot = TreePropertyCalculator.clone((GraphicsNode) getTreeLayoutProperties().getRoot());
//		GraphicsNode cloneRoot = (GraphicsNode) getTreeLayoutProperties().getRoot();
//		List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
//		TreeOperationUtil.recursiveIterateTreeIF(cloneRoot, node -> {
//			node.setDoubleVariable(-1);
//			if (node.getParentCount() == 0) {
//				node.setDoubleVariable(1);
//			}
//			if (selectedNodes.contains(node)) {
//				node.setDoubleVariable(1);
//				tuple.setElement(tuple.getElement() + 1);
////				newFilter.setparentfilter(node);
//			}
//			if (node.getDrawUnit().isNodeSelected()) {
//				node.setDoubleVariable(1);
//				tuple.setElement(tuple.getElement() + 1);
////				newFilter.setparentfilter(node);
//			}
//		});
////		newFilter.buildfilterTree(cloneRoot);
//		if (tuple.getElement() == 0) {
//			SwingDialog.showErrorMSGDialog(instanceFrame, "Warning",
//					"No nodes were chosen or found.");
//
//		} else {
//			
//		}

    }

    public void showAllSamples() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void changeColorScheme(ColorScheme colorScheme) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void assignNodeColor4treePanel(ColorScheme colorScheme, GraphicsNode root) {
        switch (colorScheme) {
            case NO_COLOR:
                TreeOperationUtil.recursiveIterateTreeIF(root, node -> {
                    if (node.getChildCount() == 0) {
                        node.getDrawUnit().setLineColor(Color.black);
                    } else {
                        node.getDrawUnit().setLineColor(Color.lightGray);
                    }
                });
                break;
            case CUSTOMIZED:
                throw new UnsupportedOperationException("Not implemented yet");
//			CustomizedRender customizedRenderrender = new CustomizedRender(this);
//			customizedRenderrender.renderNodes(root);
//			break;
            default:
                break;
        }
    }

    /**
     * 在需要与Branch length的设置联动时调用此方法
     */
    public void doLastBranchLengthSetting() {
        new Thread(() -> {
            BaseLayout currLayout = getSelectedPhylogeneticTreePanel().getCurrLayout();
            BranchLengthType branchLengthType = currLayout.getBranchLengthType();

            setBranchLengthType(branchLengthType);
        });
    }

//	/**
//	 * 奇怪，何必要这么麻烦呢？直接用 EvolNodeUtil中的ladd
//	 * @param currNode: 用户点击时的节点！
//	 * @param b         : true for up; false for down
//	 */
//	public void ladderizeNode(GraphicsNode currNode, boolean b) {
//		TreeMap<Integer, List<GraphicsNode>> mapOfNodesInSameDeepth = new TreeMap<Integer, List<GraphicsNode>>(
//				new Comparator<Integer>() {
//					@Override
//					public int compare(Integer obj1, Integer obj2) {
//						int factor = b ? 1 : -1;
//						return factor * obj1.compareTo(obj2);
//					}
//				});
//
//		ladderizeNode(currNode, mapOfNodesInSameDeepth);
//
//	}
//
//	private void ladderizeNode(GraphicsNode currentNode, Map<Integer, List<GraphicsNode>> mapOfNodesInSameDeepth) {
//		mapOfNodesInSameDeepth.clear();
//		int childCount = currentNode.getChildCount();
//		// If current node is leaf or all children is leaf,don't need to ladderize!
//		if (currentNode.getChildCount() == 0 || isAllNodesAreLeaf(currentNode)) {
//			return;
//		}
//		GraphicsNode tempNode = null;
//		// if two or more child have same num of leaves, then add them to one List<Node>
//		for (int i = 0; i < childCount; i++) {
//			tempNode = (GraphicsNode) currentNode.getChildAt(i);
//
//			int leafNum = TreePropertyCalculator.getLeafNumber(tempNode);
//
//			if (mapOfNodesInSameDeepth.containsKey(leafNum)) {
//				List<GraphicsNode> cnodes = mapOfNodesInSameDeepth.get(leafNum);
//				cnodes.add(tempNode);
//				mapOfNodesInSameDeepth.put(leafNum, cnodes);
//			} else {
//				List<GraphicsNode> cnodes = new ArrayList<GraphicsNode>(3);
//				cnodes.add(tempNode);
//				mapOfNodesInSameDeepth.put(leafNum, cnodes);
//			}
//		}
//
//		currentNode.removeAllChild();
//
//		Iterator<Entry<Integer, List<GraphicsNode>>> mapIterator = mapOfNodesInSameDeepth.entrySet().iterator();
//		while (mapIterator.hasNext()) {
//			Entry<Integer, List<GraphicsNode>> cnEntry = mapIterator.next();
//			List<GraphicsNode> childrenOfCurrentNode = cnEntry.getValue();
//			for (int m = 0; m < childrenOfCurrentNode.size(); m++) {
//				GraphicsNode cnTempNode = childrenOfCurrentNode.get(m);
//				cnTempNode.removeAllParent();
//				currentNode.addChild(cnTempNode);
//			}
//		}
//		for (int i = 0; i < childCount; i++) {
//			ladderizeNode((GraphicsNode) currentNode.getChildAt(i), mapOfNodesInSameDeepth);
//		}
//	}

    private boolean isAllNodesAreLeaf(GraphicsNode node) {
        boolean allIsLeaf = true;
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChildAt(i).getChildCount() != 0) {
                allIsLeaf = false;
                break;
            }
        }
        return allIsLeaf;
    }

    public String getMouthNameAbbr(String name) {
        return dateHandler.getMonthNameAbbr(name);
    }

    public void setTreeLayoutSwither(TreeLayoutSwitcher toplayoutSwither2) {
        this.toplayoutSwither = toplayoutSwither2;

    }

    public void enableTreeLayoutSwitherButtons(boolean enable) {
        SwingUtilities.invokeLater(() -> {
            toplayoutSwither.enableTreeLayoutSwitcherButtons(enable);
        });
    }

    public void setLeftControlPanelContainner(ControlPanelContainner leftControlPanelContainner) {
        this.leftControlPanelContainner = leftControlPanelContainner;

    }

    public ControlPanelContainner getLeftControlPanelContainner() {
        return leftControlPanelContainner;
    }

    public CtrlTreeOperationPanelByMiglayout getLeftOparetionPanel() {
        return leftControlPanelContainner.getLeftOparetionPanel();
    }

    public CtrlTreeLayoutPanel getTreeLayoutPanel() {
        return leftControlPanelContainner.getTreeLayoutPanel();
    }

    public DateTimeFormatter getHumanReadableDataFormat() {
        return HUMAN_READABLE_FORMAT;
    }

    /**
     * 用法： 先用 {@link #getHumanReadableDataFormat()} format一下，然后把str导入这个方法！
     *
     * @param str
     * @return
     */
    public String getMouthNameAbbrAndYearMouthDay(String str) {
        return dateHandler.getMonthNameAbbrAndYearMonthDay(str);
    }

    /**
     * 用法： 先用 {@link #getHumanReadableDataFormat()} format一下，然后把str导入这个方法！ 没有Day
     *
     * @param str
     * @return
     */
    public String getMouthNameAbbrAndYearMouth(String str) {
        return dateHandler.getMonthNameAbbrAndYearMonth(str);
    }

    public void showGlobalTreePanel() {
//		main.showGlobalTreePanel();
    }

    public CustomizedSaveBean getCustomizedSaveBean() {
        return customizedSaveBean;
    }

    public void recoverAllGraphicsEffects() {
        TreeOperationUtil.recursiveIterateTreeIF(getTreeLayoutProperties().getOriginalRootNode(), node -> {
            node.getDrawUnit().recover2PrimaryStates();
        });

        getTreeLayoutProperties().recover2defaultFont();
        refreshPhylogeneticTree();

    }

    /**
     * Layout布局切换的时候会调用这个方法
     */
    public void fireLayoutChanged() {

    }

}
