package module.evolview.gfamily.work.listener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import module.evolview.phylotree.visualization.graphics.struct.TreeLayout;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;
import module.evolview.gfamily.work.gui.ScaleBarRectObject;
import module.evolview.phylotree.visualization.graphics.large.interactive.TreePopupMenu;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.RectObj;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.ScaleBarProperty;
import evoltree.txtdisplay.TreeDrawUnit;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.graphics.struct.TreeDecideUtil;

public class TreeListener extends MouseAdapter implements KeyListener {
	private static final Logger logger = LoggerFactory.getLogger(TreeListener.class);

	private static final long TIME_DIFFERENCE_FOR_IDLE = 300;
	private long lastMoveTime;

	private final TreeLayoutProperties treeLayoutProperties;
	private final List<Consumer<GraphicsNode>> tree2AnalyzingPanelInteractions = Lists.newArrayList();

	private GraphicsNode rootNode;
	private final PhylogeneticTreePanel phylogeneticTreePanel;

	private JPopupMenu jPopupMenu4creativeMode;
	private TreePopupMenu jPopupMenu4normalMode;

	private final Line2D.Double repeatLine = new Line2D.Double();

	private RectObj scaleBarRectAdjust;
	private boolean whetherScaleBarSelectedInDrag = false;

	private Point origin;

	private final StringBuilder sBuilder = new StringBuilder(1024);
	private final String templateHeader = "<html><body font size='5'>";
	private final String templatefooter = "</body></html>";

	private final Rectangle rect4selectArea = new Rectangle();
	private final String gestureActionHint = new String(
			"<html><body font size='5'> Press<br> Shift + left click to collapse <br> Alt + left click to see info.<br> Right click to operate.</body></html>");

	public TreeListener(TreeLayoutProperties properties, GraphicsNode rootNode,
			PhylogeneticTreePanel phylogeneticTreePanel) {
		Objects.requireNonNull(properties);
		Objects.requireNonNull(rootNode);
		Objects.requireNonNull(phylogeneticTreePanel);
		this.treeLayoutProperties = properties;
		this.rootNode = rootNode;
		this.phylogeneticTreePanel = phylogeneticTreePanel;

	}

	public void setRootNode(GraphicsNode rootNode) {
		this.rootNode = rootNode;
	}

	public TreeLayoutProperties getTreeLayoutProperties() {
		return treeLayoutProperties;
	}

	public RectObj getScaleBarListenerObj() {
		if (scaleBarRectAdjust == null) {
			scaleBarRectAdjust = new ScaleBarRectObject(phylogeneticTreePanel.getScaleBarProperty());
		}

		return scaleBarRectAdjust;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

		Component component = arg0.getComponent();
		boolean controlDown = arg0.isControlDown();
		if (!controlDown) {
			component.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}

		if (getTreeLayoutProperties().isShowScaleBar() && whetherScaleBarSelectedInDrag) {
			Point point = arg0.getPoint();
			getScaleBarListenerObj().adjustPaintings(point.getX(), point.getY());
			phylogeneticTreePanel.repaint();
		}

		boolean isCreativeMode = treeLayoutProperties.isCreativeMode();

		if (isCreativeMode) {
			// 创造者模式
			List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
			if (selectedNodes.isEmpty()) {
				moveTheDrawingArea(arg0);
				return;
			}
			GraphicsNode graphicsNode = selectedNodes.get(0);

			double canves2logicRatio = phylogeneticTreePanel.getCurrLayout().getCanvas2logicRatio();

			double abs = FastMath.abs(graphicsNode.getXParent() - graphicsNode.getXSelf());
			graphicsNode.setRealBranchLength(abs / canves2logicRatio);

			// 这里除了要改变 self的 (x,y)坐标之外还要更改 Parent的坐标

			graphicsNode.setXSelf(arg0.getX());
			int childCount = graphicsNode.getChildCount();
			for (int i = 0; i < childCount; i++) {
				GraphicsNode child = (GraphicsNode) graphicsNode.getChildAt(i);
				child.setXParent(arg0.getX());

				abs = FastMath.abs(child.getXParent() - child.getXSelf());
				child.setRealBranchLength(abs / canves2logicRatio);
			}

			phylogeneticTreePanel.repaint();
		} else {
			// 非创造者模式

			if (controlDown) {
				// 用户拖拽时，动态更新矩形的宽度和高度
				Point endPoint = arg0.getPoint();
				rect4selectArea.setFrame(Math.min(origin.x, endPoint.x), Math.min(origin.y, endPoint.y),
						Math.abs(endPoint.x - origin.x), Math.abs(endPoint.y - origin.y));
				phylogeneticTreePanel.repaint();
			} else {
				moveTheDrawingArea(arg0);
			}

		}
	}

	private void moveTheDrawingArea(MouseEvent arg0) {
		JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, phylogeneticTreePanel);
		if (viewPort == null) {
			SwingDialog.showErrorMSGDialog("Internal error", "The tree panel should be add in the JScrollPanel.");
			return;
		}
		Rectangle viewArea = viewPort.getViewRect();

		int deltaX = origin.x - arg0.getX();
		int deltaY = origin.y - arg0.getY();


		viewArea.x += deltaX;
		viewArea.y += deltaY;

		phylogeneticTreePanel.scrollRectToVisible(viewArea);

		UnifiedAccessPoint.getInstanceFrame()
				.showTipsOnBottomStatusBar("Press Ctrl key with drag to select nodes.");

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		long currentTime = System.currentTimeMillis();
		long diff = currentTime - lastMoveTime;

		// 每两次tooltip的请求发送需要有一定的间隔
		if (diff <= TIME_DIFFERENCE_FOR_IDLE) {
			return;
		}

		lastMoveTime = System.currentTimeMillis();

		Point point = e.getPoint();

		ScaleBarProperty scaleBarProperty = phylogeneticTreePanel.getScaleBarProperty();
		Component component = e.getComponent();

		if (scaleBarProperty.isIfDrawScaleBar() && getTreeLayoutProperties().isShowScaleBar()) {
			boolean contains = getScaleBarListenerObj().contains(point.getX(), point.getY());
			if (contains) {
				component.setCursor(Cursor.getPredefinedCursor(getScaleBarListenerObj().getCursorType()));
				return;
			} else {
				component.setCursor(Cursor.getDefaultCursor());
			}
		}

		Optional<GraphicsNode> optRet = judgeSelectedNode(point);
		// Need to complete
		if (optRet.isPresent()) {
			phylogeneticTreePanel.setToolTipText(gestureActionHint);
		} else {
			component.setCursor(Cursor.getDefaultCursor());
			phylogeneticTreePanel.setToolTipText(null);
			return;
		}
//		GraphicsNode selectedNode = optRet.get();
//		boolean isLeaf = selectedNode.getChildCount() == 0;
//
//		String tooltipString = null;
//		// 在进行filter后内节点可能变成叶子节点，所以不能单通过判断是否有child判断叶子，加上了name是否为空的判断
//		if (isLeaf && !selectedNode.getName().isEmpty()) {
//			tooltipString = "Leaf node: ".concat(selectedNode.getName());
//		} else {
//			tooltipString = "fefefewafew";
//		}
//
//		phylogeneticTreePanel.setToolTipText(tooltipString);

		// 鼠标挪动的位置有节点则显示手势
		component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	}

	public String getTooltipString(List<String> contents) {
		sBuilder.setLength(0);
		sBuilder.append(templateHeader);

		for (String string : contents) {
			sBuilder.append("  ");
			sBuilder.append(string);
			sBuilder.append("  ");
			sBuilder.append("<br>");
		}

//		sBuilder.append("<style=\"font-family:verdana;font-size:4;color:white\">" + 
//				"<i>Click to see more information<br>" + 
//				"Right click and open new tab to see clade</i>" +
//				"</style>");

		sBuilder.append(templatefooter);

		return sBuilder.toString();
	}

	public Optional<GraphicsNode> judgeSelectedNode(Point point) {

		Predicate<GraphicsNode> func = node -> {
			return ifPointLocatedInNode(point, node);
		};
		Optional<GraphicsNode> searchNodeWithReturn = EvolNodeUtil.searchNodeWithReturn(rootNode, func);
		return searchNodeWithReturn;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		arg0.getComponent().setCursor(Cursor.getDefaultCursor());

		// 鼠标释放后，可以决定是否保留这个矩形，或者执行其他逻辑
		origin = null; // 重置起始点

		if (rect4selectArea.getWidth() > 0) {
			List<GraphicsNode> selectedNodes = new LinkedList<>();

			EvolNodeUtil.recursiveIterateTreeIF(rootNode, node -> {
				double xSelf = node.getXSelf();
				double ySelf = node.getYSelf();
				//
				boolean contains = rect4selectArea.contains(xSelf, ySelf);
				if (contains) {
					selectedNodes.add(node);
				}
			});
			treeLayoutProperties.getSelectedNodes().addAll(selectedNodes);

			for (GraphicsNode node : selectedNodes) {
				node.getDrawUnit().setBranchSelected(true);
			}

		}

		rect4selectArea.setSize(0, 0);
		// ref
		phylogeneticTreePanel.weakRefreshViewPort();



	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		origin = new Point(arg0.getPoint());

		ScaleBarProperty scaleBarProperty = phylogeneticTreePanel.getScaleBarProperty();
		if (scaleBarProperty.isIfDrawScaleBar()) {
			Point point = arg0.getPoint();
			boolean contains = getScaleBarListenerObj().contains(point.getX(), point.getY());
			if (contains) {
				getScaleBarListenerObj().setFirstPressedPoint(point.x, point.y);
			}
			this.whetherScaleBarSelectedInDrag = contains;
		}

		actionForMouseClick(arg0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// 这是一个高级实践，因为我们已经有其它的实现了是，所以这个方法设置为空
	}

	protected void actionForMouseClick(MouseEvent e) {
		Optional<GraphicsNode> optRet = judgeSelectedNode(e.getPoint());

		Component component = e.getComponent();

		if (SwingUtilities.isRightMouseButton(e)) {// 鼠标右击操作
			mouseRightClickAction(e, optRet, component);
		} else {
			// 鼠标左键
			boolean controlDown = e.isControlDown();
			boolean altDown = e.isAltDown();
			boolean shiftDown = e.isShiftDown();

			if (controlDown) {
				if (optRet.isPresent()) {
					GraphicsNode selectedNode = optRet.get();
					controlPressSelect(selectedNode);
					TreeDrawUnit bioLine = selectedNode.getDrawUnit();
					bioLine.setBranchSelected(true);
					List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
					if (!selectedNodes.contains(selectedNode)) {
						selectedNodes.add(selectedNode);
					}
					phylogeneticTreePanel.updatePhylogeneticTreePanel4fitFrame();
				}
			} else if (altDown) {
				if (optRet.isPresent()) {
					GraphicsNode selectedNode = optRet.get();
					dealWithSelectedNodes(selectedNode);

//					boolean isLeaf = TreeDecideUtil.decideNodeTypeOfLeaf(selectedNode);
					// yudalang: 这里是显示具体的信息，所以判定就直接用 getChildCount==0来好了。
					boolean isLeaf = selectedNode.getChildCount() == 0;
					if (isLeaf) {
						getTreePopupMenu().displayMoreInformation4leaf(selectedNode);
					} else {
						getTreePopupMenu().displayMoreInformation4internalNode(selectedNode);
					}
				} else {
					dealWithSelectedNodes(null);
				}

			} else if (shiftDown) {

				if (optRet.isPresent()) {
					GraphicsNode selectedNode = optRet.get();
					dealWithSelectedNodes(selectedNode);

					boolean isLeaf = TreeDecideUtil.decideNodeTypeOfLeaf(selectedNode);
					// 在进行filter后内节点可能变成叶子节点，所以不能单通过判断是否有child判断叶子，加上了name是否为空的判断
					if (isLeaf && !selectedNode.getName().isEmpty()) {
					} else {
						getTreePopupMenu().collapseThisNode(selectedNode);
					}
				} else {
					dealWithSelectedNodes(null);
				}

			} else {

				// 单纯的左击
				if (optRet.isPresent()) {
					GraphicsNode selectedNode = optRet.get();
					dealWithSelectedNodes(selectedNode);
					
					for (Consumer<GraphicsNode> consumer : tree2AnalyzingPanelInteractions) {
						consumer.accept(selectedNode);
					}
				} else {
					dealWithSelectedNodes(null);
				}

			}
		}

		MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();
		instanceFrame.refreshRightGraphicPropertiesPanel();
		phylogeneticTreePanel.refreshViewPort();

	}

	private void mouseRightClickAction(MouseEvent e, Optional<GraphicsNode> optRet, Component component) {
		if (optRet.isPresent()) {
			// 当有节点被选中时
			GraphicsNode selectedNode = optRet.get();
			TreeLayoutProperties treeLayoutProperties2 = getTreeLayoutProperties();
			List<GraphicsNode> selectedNodes = treeLayoutProperties2.getSelectedNodes();
			if (selectedNodes.contains(selectedNode)) {

			} else {
				dealWithSelectedNodes(selectedNode);
			}

			if (treeLayoutProperties2.isCreativeMode()) {
				JPopupMenu popup = getPopupMenu4creativeMode();
				popup.show(component, e.getX(), e.getY());
			} else {
				boolean isLeaf = TreeDecideUtil.decideNodeTypeOfLeaf(selectedNode);
				// 在进行filter后内节点可能变成叶子节点，所以不能单通过判断是否有child判断叶子，加上了name是否为空的判断
				if (isLeaf && !selectedNode.getName().isEmpty()) {
					JPopupMenu popup = getTreePopupMenu().getLeafRightClickPop(selectedNode, component);
					popup.show(component, e.getX(), e.getY());
				} else {
					JPopupMenu popup = getTreePopupMenu().getInternalNodeRightClickPop(selectedNode);
					popup.show(component, e.getX(), e.getY());
				}
			}

		} else {
			// 当没有节点的时候
			JPopupMenu popup = getTreePopupMenu().getBlinkAreaPop(component, e.getPoint());
			popup.show(component, e.getX(), e.getY());
			dealWithSelectedNodes(null);
		}
	}

	/**
	 * 
	 * 按住control的多节点选中
	 * 
	 * @title controlPressSelect
	 * @createdDate 2022-11-29 15:27
	 * @lastModifiedDate 2022-11-29 15:27
	 * @author
	 * @since 1.7
	 * 
	 * @param selectedNode
	 * @return void
	 */
	protected void controlPressSelect(GraphicsNode selectedNode) {

		TreeDrawUnit bioLine = selectedNode.getDrawUnit();
		bioLine.setBranchSelected(true);
		List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
		if (!selectedNodes.contains(selectedNode)) {
			selectedNodes.add(selectedNode);
		}
		phylogeneticTreePanel.refreshViewPort();
	}

	protected void dealWithSelectedNodes(GraphicsNode selectedNode) {

		List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();
		for (GraphicsNode node : selectedNodes) {
			node.getDrawUnit().setBranchSelected(false);
		}
		selectedNodes.clear();

		if (selectedNode == null) {
			UnifiedAccessPoint.getInstanceFrame().showTipsOnBottomStatusBar(null);
			return;
		}

		TreeDrawUnit bioLine = selectedNode.getDrawUnit();
		bioLine.setBranchSelected(true);
		selectedNodes.add(selectedNode);

		sBuilder.setLength(0);
		sBuilder.append("The node \"");
		sBuilder.append(selectedNode.getName());
		sBuilder.append("\" selected, ");
		sBuilder.append("child count is: ");
		sBuilder.append(selectedNode.getChildCount());
		sBuilder.append(", branch length is: ");
		sBuilder.append(selectedNode.getLength());

		UnifiedAccessPoint.getInstanceFrame().showTipsOnBottomStatusBar(sBuilder.toString());
	}

	private boolean ifPointLocatedInNode(Point point, GraphicsNode node) {
		// 如果是隐藏的属性的话，那么应该直接返回false
		if (node.hideNode()) {
			return false;
		}

		double xSelf = node.getXSelf();
		double ySelf = node.getYSelf();

		double distanceSq = point.distanceSq(xSelf, ySelf);

//		logger.info("The dis is {}", distanceSq);
		if (distanceSq < 10 * 10) {
			return true;
		}

		double xParent = node.getXParent();
		double yParent = node.getYParent();

		repeatLine.setLine(xSelf, ySelf, xParent, yParent);
		double distance = repeatLine.ptSegDist(point);

//		System.out.println("------------------------------------------------------");
//		System.out.println(xParent + "\t" + yParent + "\t" + xSelf + "\t" + ySelf);
//		System.out.println(point + "\t" + node.getID() + "\t" + node.getName() + "\t" + distance);
		if (distance <= 1.5) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int scrollAmount = -1 * 10 * e.getUnitsToScroll();

		boolean whetherHeightScaleOnMouseWheel = treeLayoutProperties.isWhetherHeightScaleOnMouseWheel();
		boolean whetherWidthScaleOnMouseWheel = treeLayoutProperties.isWhetherWidthScaleOnMouseWheel();

		int heightScrollAmout = whetherHeightScaleOnMouseWheel ? scrollAmount : 0;
		int widthScrollAmout = whetherWidthScaleOnMouseWheel ? scrollAmount : 0;
		phylogeneticTreePanel.continuouslyZoomInOrOut(heightScrollAmout, widthScrollAmout, e.getPoint());
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		List<GraphicsNode> selectedNodes = getTreeLayoutProperties().getSelectedNodes();

		if (selectedNodes.size() == 0) {
			return;
		}

		GraphicsNode node = selectedNodes.get(0);
		boolean isLeaf = node.getChildCount() == 0;
		boolean isRoot = node.getParentCount() == 0;

		GraphicsNode selectedNode = null;

		if (keyCode == KeyEvent.VK_UP) {
			if (!isRoot) {
				Optional<GraphicsNode> sibling = getSibling(node, true);
				if (sibling.isPresent()) {
					selectedNode = sibling.get();
				}
			}
		} else if (keyCode == KeyEvent.VK_DOWN) {
			if (!isRoot) {
				Optional<GraphicsNode> sibling = getSibling(node, false);
				if (sibling.isPresent()) {
					selectedNode = sibling.get();
				}
			}
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			if (!isLeaf) {
				selectedNode = (GraphicsNode) node.getFirstChild();
			}
		} else if (keyCode == KeyEvent.VK_LEFT) {
			if (!isRoot) {
				selectedNode = (GraphicsNode) node.getParent();
			}
		}

		if (selectedNode == null) {
			return;
		}

		dealWithSelectedNodes(selectedNode);

		String tooltipString = null;
		if (isLeaf) {
//			tooltipString = getTooltipString(getTreePopupMenu().getLeafTooltipInformation(selectedNode, false));
		} else {
//			tooltipString = getTooltipString(getTreePopupMenu().getInternalNodeTooltipInformation(selectedNode, false));
		}
		phylogeneticTreePanel.setToolTipText(tooltipString);
		phylogeneticTreePanel.refreshViewPort();
	}

	private Optional<GraphicsNode> getSibling(GraphicsNode node, boolean isUp) {
		GraphicsNode parent = (GraphicsNode) node.getParent();
		int indexOfChild = parent.indexOfChild(node);
		int childCount = parent.getChildCount();
		GraphicsNode ret = null;
		if (isUp) {
			if (indexOfChild > 0) {
				ret = (GraphicsNode) parent.getChildAt(indexOfChild - 1);
			}
		} else {
			if (indexOfChild < childCount - 1) {
				ret = (GraphicsNode) parent.getChildAt(indexOfChild + 1);
			}
		}

		return Optional.ofNullable(ret);
	}

	private JPopupMenu getPopupMenu4creativeMode() {
		if (jPopupMenu4creativeMode == null) {
			jPopupMenu4creativeMode = new JPopupMenu("Creative popup menu");

			Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

			{
				JMenuItem jMenuItem = new JMenuItem("Add child");
				jPopupMenu4creativeMode.add(jMenuItem);
				jMenuItem.setFont(defaultFont);
				jMenuItem.addActionListener(e -> {
					List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
					if (selectedNodes.isEmpty()) {
						return;
					}
					GraphicsNode graphicsNode = selectedNodes.get(0);
					double realBranchLength = graphicsNode.getRealBranchLength();
					if (graphicsNode.getChildCount() == 0) {

						GraphicsNode graphicsNode2 = new GraphicsNode();
						String name = "Leaf_" + GraphicsNode.nextID;
						graphicsNode2.setName(name);
						GraphicsNode graphicsNode3 = new GraphicsNode();
						name = "Leaf_" + GraphicsNode.nextID;
						graphicsNode3.setName(name);

						graphicsNode.addChild(graphicsNode2);
						graphicsNode.addChild(graphicsNode3);
						graphicsNode2.setRealBranchLength(realBranchLength);
						graphicsNode3.setRealBranchLength(realBranchLength);

					} else {
						GraphicsNode graphicsNode2 = new GraphicsNode();
						String name = "Leaf_" + GraphicsNode.nextID;
						graphicsNode2.setName(name);
						graphicsNode2.setRealBranchLength(realBranchLength);
						graphicsNode.addChild(graphicsNode2);
					}

					getTreeLayoutProperties().initializeRoot();
					phylogeneticTreePanel.updatePhylogeneticTreePanel();
				});
			}

			{
				JMenuItem jMenuItem = new JMenuItem("Add sibling");
				jPopupMenu4creativeMode.add(jMenuItem);
				jMenuItem.setFont(defaultFont);
				jMenuItem.addActionListener(e -> {
					List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
					if (selectedNodes.isEmpty()) {
						return;
					}
					GraphicsNode graphicsNode = selectedNodes.get(0);
					double realBranchLength = graphicsNode.getRealBranchLength();

					EvolNode parent = graphicsNode.getParent();
					GraphicsNode graphicsNode2 = new GraphicsNode();
					String name = "Leaf_" + GraphicsNode.nextID;
					graphicsNode2.setName(name);
					graphicsNode2.setRealBranchLength(realBranchLength);
					parent.addChild(graphicsNode2);

					getTreeLayoutProperties().initializeRoot();
					phylogeneticTreePanel.updatePhylogeneticTreePanel();
				});
			}
			{
				JMenuItem jMenuItem = new JMenuItem("Add sibling and merge");
				jPopupMenu4creativeMode.add(jMenuItem);
				jMenuItem.setFont(defaultFont);
				jMenuItem.addActionListener(e -> {
					List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
					if (selectedNodes.isEmpty()) {
						return;
					}
					GraphicsNode graphicsNode = selectedNodes.get(0);
					double realBranchLength = graphicsNode.getRealBranchLength();

					EvolNode parent = graphicsNode.getParent();
					GraphicsNode graphicsNode2 = new GraphicsNode();
					String name = "Leaf_" + GraphicsNode.nextID;
					graphicsNode2.setName(name);
					graphicsNode2.setRealBranchLength(realBranchLength);

					graphicsNode.removeAllParent();

					GraphicsNode newParent = new GraphicsNode();
					newParent.setRealBranchLength(realBranchLength);
					newParent.addChild(graphicsNode2);
					newParent.addChild(graphicsNode);

					parent.addChild(newParent);

					getTreeLayoutProperties().initializeRoot();
					phylogeneticTreePanel.updatePhylogeneticTreePanel();
				});
			}
			{
				JMenuItem jMenuItem = new JMenuItem("Rename");
				jPopupMenu4creativeMode.add(jMenuItem);
				jMenuItem.setFont(defaultFont);
				jMenuItem.addActionListener(e -> {
					List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
					if (selectedNodes.isEmpty()) {
						return;
					}
					GraphicsNode graphicsNode = selectedNodes.get(0);

					String name = graphicsNode.getName();
					name = name == null ? "" : name;
					Optional<String> showInputDialog = SwingDialog.showInputDialog(UnifiedAccessPoint.getInstanceFrame(),
							name);
					if (showInputDialog.isPresent()) {
						graphicsNode.setName(showInputDialog.get());

						getTreeLayoutProperties().initializeRoot();

						TreeLayout myLayout = treeLayoutProperties.getMyLayout();
						getTreeLayoutProperties().initializeRoot();
						phylogeneticTreePanel.updateViewAccording2TreeLayout(myLayout);
					}

				});
			}

			{
				JMenuItem jMenuItem = new JMenuItem("Swap with sibling");
				jPopupMenu4creativeMode.add(jMenuItem);
				jMenuItem.setFont(defaultFont);
				jMenuItem.addActionListener(e -> {
					List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
					if (selectedNodes.isEmpty()) {
						return;
					}
					GraphicsNode graphicsNode = selectedNodes.get(0);
					if (graphicsNode.getParentCount() == 0) {
						return;
					}

					EvolNodeUtil.swapNodeWithSibling(graphicsNode);
					phylogeneticTreePanel.updatePhylogeneticTreePanel();
				});
			}

			{
				JMenuItem jMenuItem = new JMenuItem("Ladderize up");
				jPopupMenu4creativeMode.add(jMenuItem);
				jMenuItem.setFont(defaultFont);
				jMenuItem.addActionListener(e -> {
					List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
					if (selectedNodes.isEmpty()) {
						return;
					}
					GraphicsNode node = selectedNodes.get(0);
					EvolNodeUtil.initializeSize(node);
					EvolNodeUtil.ladderizeNodeAccording2sizeAndLength(node, true);
					phylogeneticTreePanel.updatePhylogeneticTreePanel();
				});
			}

			{
				JMenuItem jMenuItem = new JMenuItem("Ladderize down");
				jPopupMenu4creativeMode.add(jMenuItem);
				jMenuItem.setFont(defaultFont);
				jMenuItem.addActionListener(e -> {
					List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
					if (selectedNodes.isEmpty()) {
						return;
					}
					GraphicsNode node = selectedNodes.get(0);
					EvolNodeUtil.initializeSize(node);
					EvolNodeUtil.ladderizeNodeAccording2sizeAndLength(node, false);
					phylogeneticTreePanel.updatePhylogeneticTreePanel();
				});
			}
			{
				JMenuItem jMenuItem = new JMenuItem("Delete clade");
				jPopupMenu4creativeMode.add(jMenuItem);
				jMenuItem.setFont(defaultFont);
				jMenuItem.addActionListener(e -> {
					List<GraphicsNode> selectedNodes = treeLayoutProperties.getSelectedNodes();
					if (selectedNodes.isEmpty()) {
						return;
					}
					GraphicsNode node = selectedNodes.get(0);

					Optional<GraphicsNode> parentOpt = EvolNodeUtil.getParent(node);
					if (!parentOpt.isPresent()) {
						return;
					}
					GraphicsNode parent = parentOpt.get();
					int childCount = parent.getChildCount();

					if (childCount > 2) {
						node.removeAllParent();
						phylogeneticTreePanel.updatePhylogeneticTreePanel();
						return;
					}
					Optional<GraphicsNode> grandParentOpt = EvolNodeUtil.getParent(parent);
					if (!grandParentOpt.isPresent()) {
						SwingDialog.showErrorMSGDialog("Delete error",
								"Can not delete this clade, \nbecause the parent is a binary fork while the grand parent not exist.");
						return;
					}

					GraphicsNode grandParent = grandParentOpt.get();
					GraphicsNode[] childrenArray = EvolNodeUtil.getChildrenArray(parent);
					parent.removeAllChild();
					parent.removeAllParent();

					for (GraphicsNode iter : childrenArray) {
						if (iter != node) {
							grandParent.addChild(iter);
						}
					}
					phylogeneticTreePanel.updatePhylogeneticTreePanel();

				});
			}

		}

		return jPopupMenu4creativeMode;
	}

	private TreePopupMenu getTreePopupMenu() {
		if (jPopupMenu4normalMode == null) {
			jPopupMenu4normalMode = new TreePopupMenu(phylogeneticTreePanel);
		}
		return jPopupMenu4normalMode;
	}

	public Rectangle getRect4selectArea() {
		return rect4selectArea;
	}

	public List<Consumer<GraphicsNode>> getTree2AnalyzingPanelInteractions() {
		return tree2AnalyzingPanelInteractions;
	}
}
