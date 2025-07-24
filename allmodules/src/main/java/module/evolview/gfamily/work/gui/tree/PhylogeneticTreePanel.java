package module.evolview.gfamily.work.gui.tree;

import java.awt.*;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.work.listener.TreeListener;
import module.evolview.phylotree.visualization.graphics.struct.TreeLayout;
import module.evolview.gfamily.work.model.tree.AnnotationsProperties;
import module.evolview.gfamily.work.model.tree.AnnotationsProperties4LinageType;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.ScaleBarProperty;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import module.evolview.phylotree.visualization.layout.BaseLayout;
import module.evolview.phylotree.visualization.layout.CircularCladoAlign2Tip;
import module.evolview.phylotree.visualization.layout.CircularCladoEqual;
import module.evolview.phylotree.visualization.layout.CircularInnerCladoAligned;
import module.evolview.phylotree.visualization.layout.CircularPhylo;
import module.evolview.phylotree.visualization.layout.CircularPhyloAlign2Tip;
import module.evolview.phylotree.visualization.layout.RadialCladoLayout;
import module.evolview.phylotree.visualization.layout.RadialPhyloLayout;
import module.evolview.phylotree.visualization.layout.RadicalEqualDaylightCladoLayout;
import module.evolview.phylotree.visualization.layout.RadicalEqualDaylightPhyloLayout;
import module.evolview.phylotree.visualization.layout.RectCladoAlign2tip;
import module.evolview.phylotree.visualization.layout.RectCladoEqual;
import module.evolview.phylotree.visualization.layout.RectHalfCircleLayout;
import module.evolview.phylotree.visualization.layout.RectPhyloAlign2Tip;
import module.evolview.phylotree.visualization.layout.RectPhyloInnerYAxis;
import module.evolview.phylotree.visualization.layout.RectPhyloLayout;
import module.evolview.phylotree.visualization.layout.SlopeCladoAlignedLeft;
import module.evolview.phylotree.visualization.layout.SpiralCladoAlignedWithBeta;
import module.evolview.phylotree.visualization.layout.SpiralPhyloWithAlpha;
import module.evolview.phylotree.visualization.layout.SpiralPhyloWithBeta;
import module.evolview.phylotree.visualization.layout.SprialCladoAlignedWithAlpha;
import egps2.frame.gui.EGPSMainGuiUtil;
import graphic.engine.guicalculator.BlankArea;

/**
 * This class is the "Phylogenetic Tree" tab.
 */
public class PhylogeneticTreePanel extends JPanel {
	private static final Logger logger = LoggerFactory.getLogger(PhylogeneticTreePanel.class);

	protected TreeLayoutProperties layoutProp;

	private int currentHeight;
	private int currentWidth;

	protected BaseLayout layout;

	/**
	 * 在 PhylogeneticTreePanel有一个是否显示scaleBar的属性。那个是控制全局是否显示scale bar的。
	 */
	private final ScaleBarProperty scaleBarProperty = new ScaleBarProperty();

	private AnnotationsProperties mainAnnotationsProperties;
	private AnnotationsProperties4LinageType linageTypeAnnotationsProperties;

	protected TreeListener treeListener;
	protected GraphicsNode rootNode;

	private boolean isGlobalPhylogeneticTreePanel = false;

	private final Color color2fill4selectedArea = new Color(0, 0, 255, 50);

	public PhylogeneticTreePanel(TreeLayoutProperties properties) {
		this(properties, properties.getOriginalRootNode(), null, null);
	}

	public PhylogeneticTreePanel(TreeLayoutProperties treePro, GraphicsNode node,
			AnnotationsProperties4LinageType linageTypeAnnotationsProperties,
			AnnotationsProperties mainAnnotationsProperties) {
		setLayout(new BorderLayout());

		this.rootNode = node;
		this.layoutProp = treePro;

		setAutoscrolls(true);
		initializeTreeListener();

		setBackground(Color.white);

		if (linageTypeAnnotationsProperties == null) {
			this.linageTypeAnnotationsProperties = new AnnotationsProperties4LinageType();
		} else {
			this.linageTypeAnnotationsProperties = linageTypeAnnotationsProperties;
		}

		if (mainAnnotationsProperties == null) {
			this.mainAnnotationsProperties = new AnnotationsProperties();
		} else {
			this.mainAnnotationsProperties = mainAnnotationsProperties;
		}

	}

	private void initializeTreeListener() {
		treeListener = new TreeListener(layoutProp, rootNode, this);
		addMouseMotionListener(treeListener);
		addMouseListener(treeListener);
		addMouseWheelListener(treeListener);
		addKeyListener(treeListener);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (layout == null) {
			EGPSMainGuiUtil.drawLetUserImportDataString((Graphics2D) g);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		try {
			EGPSMainGuiUtil.setupHighQualityRendering(g2d);
			drawProcess(g2d);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			g2d.dispose();
		}

	}

	protected void drawProcess(Graphics2D g2d) {
		/*
		  为什么这里是缩小才会重新计算呢？
		  因为如果改成放大或者缩小都计算的话，那么1)查看tool tip时候，2)鼠标右击的时候 都是重新refresh，所以先只是放大！
		 */
		if (currentHeight != 0) {
			if (currentHeight < getHeight() || currentWidth < getWidth()) {
				updatePhylogeneticTreePanel4fitFrame();
				return;
			}
		}

        if (layoutProp.isCreativeMode()) {
			Image image = layoutProp.getBackgroundImage4CreativeMode();
			BlankArea blankArea = layoutProp.getBlankArea();
			if (image != null) {
				int workWidth = blankArea.getWorkWidth(getWidth());
				int workHeight = blankArea.getWorkHeight(getHeight());
				g2d.drawImage(image, blankArea.getLeft(), blankArea.getTop(), workWidth, workHeight, null);
			}

		}


		layout.paintGraphics(g2d);
		layout.drawFixedTitle(g2d, getWidth(), getHeight());

		if (layoutProp.isShowWidthAndHeightString()) {
			String format = String.format("Width: %d / height: %d", currentWidth, currentHeight);
			Font axisFont = layoutProp.getAxisFont();
			g2d.setFont(axisFont);
			g2d.drawString(format, 30, 30);

			BlankArea blankArea = layout.getBlankArea();
			String string = blankArea.toString();
			g2d.drawString(string, 30, 45);
		}

		Rectangle rect4selectArea = treeListener.getRect4selectArea();
		if (rect4selectArea.getWidth() > 0) {
			// 设置颜色和透明度
			g2d.setColor(color2fill4selectedArea); // 半透明蓝色
			g2d.fill(rect4selectArea); // 填充矩形区域

			g2d.setColor(Color.BLUE); // 边框颜色
			g2d.draw(rect4selectArea); // 绘制矩形边框
		}

	}

	/**
	 * 这是比较弱的更新方法
	 */
	public void refreshViewPort() {
		SwingUtilities.invokeLater(() -> {
			repaint();
			this.revalidate();
		});
	}

	/**
	 * 这是最弱的更新方法
	 */
	public void weakRefreshViewPort() {
		SwingUtilities.invokeLater(this::repaint);
	}

	public void updatePhylogeneticTreePanel4fitFrame() {

		Container parent = this.getParent();
		if (parent instanceof JViewport viewPort) {

            currentWidth = viewPort.getWidth();
			currentHeight = viewPort.getHeight();
		} else {
			currentWidth = getWidth();
			currentHeight = getHeight();

			SwingDialog.showWarningMSGDialog("Warning to developers",
					"The tree panel should be add in the JScollPanel.");

		}

		updatePhylogeneticTreePanel();
	}

	/**
	 * 
	 */
	public void updatePhylogeneticTreePanel() {
		SwingWorker<Void, Void> swingWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                recalculatedInstructions(currentWidth, currentHeight);
                return null;
            }

            @Override
            protected void done() {
                try {
                    // 调用get()来捕获doInBackground()中抛出的异常
                    get();
                    // 如果没有异常，继续执行正常逻辑
                    refreshViewPort();
                } catch (Exception e) {
                    // 处理异常
                    e.printStackTrace();
                }
            }
        };

		// 有可能这个过程不是在GUI下面
		if (UnifiedAccessPoint.isGULaunched()) {
			swingWorker.execute();
		} else {
			recalculatedInstructions(currentWidth, currentHeight);
		}

	}

	/**
	 * 第一次启动本程序之后的调用方法。
	 */
	public void initializeLeftPanel() {
		this.isGlobalPhylogeneticTreePanel = true;

		layoutProp.setMyLayout(TreeLayout.RECT_PHYLO_LEFT);
		layout = obtainRightLayout(layoutProp.getMyLayout());

		updatePhylogeneticTreePanel4fitFrame();
	}

	public void updateViewAccording2TreeLayout(TreeLayout tt) {
		layoutProp.setMyLayout(tt);
		layout = obtainRightLayout(tt);
		updatePhylogeneticTreePanel();
	}

	/**
	 * 注意这里还有一项功能是 controller.getLeftOparetionPanel().enableScaleBarStates(enable);
	 *
	 * @param currLayout
	 */
	private BaseLayout obtainRightLayout(TreeLayout currLayout) {
		TreeLayoutProperties treeLayoutProperties = layoutProp;
		BaseLayout ret;
		switch (currLayout) {
		case SLOPE_CLADO_ALIGNED_LEFT:
			ret = new SlopeCladoAlignedLeft(treeLayoutProperties, rootNode, this);
			break;
		case RECT_CLADO_ALIGNED_LEFT:
			ret = new RectCladoAlign2tip(treeLayoutProperties, rootNode, this);
			break;
		case RECT_CLADO_EQUAL_LEFT:
			ret = new RectCladoEqual(treeLayoutProperties, rootNode, this);
			break;
		case RECT_INNER_YAXIS:
			ret = new RectPhyloInnerYAxis(treeLayoutProperties, rootNode, this);
			break;
		case RECT_HALF_CIRCLE:
			ret = new RectHalfCircleLayout(treeLayoutProperties, rootNode, this);
			break;
		case RECT_PHYLO_ALIGN2TIP:
			ret = new RectPhyloAlign2Tip(treeLayoutProperties, rootNode, this);
			break;
		case RECT_PHYLO_LEFT:
			ret = new RectPhyloLayout(treeLayoutProperties, rootNode, this);
			break;
		case INNER_CIR_CLADO:
			ret = new CircularInnerCladoAligned(treeLayoutProperties, rootNode, this);
			break;
		case OUTER_CIR_CLADO_ALIGNED:
			ret = new CircularCladoAlign2Tip(treeLayoutProperties, rootNode, this);
			break;
		case OUTER_CIR_CLADO_EQUAL:
			ret = new CircularCladoEqual(treeLayoutProperties, rootNode, this);
			break;
		case OUTER_CIR_PHYLO:
			ret = new CircularPhylo(treeLayoutProperties, rootNode, this);
			break;
		case OUTER_CIR_PHYLO_ALIGNED:
			ret = new CircularPhyloAlign2Tip(treeLayoutProperties, rootNode, this);
			break;
		case RADIAL_CLADO:
			ret = new RadialCladoLayout(treeLayoutProperties, rootNode, this);
			break;
		case RADIAL_EQUAL_DAYLIGHT_PHYLO:
			ret = new RadicalEqualDaylightPhyloLayout(treeLayoutProperties, rootNode, this);
			break;
		case RADIAL_EQUAL_DAYLIGHT_CLADO:
			ret = new RadicalEqualDaylightCladoLayout(treeLayoutProperties, rootNode, this);
			break;
		case SPRIAL_ALPHA_PHYLO:
			ret = new SpiralPhyloWithAlpha(treeLayoutProperties, rootNode, this);
			break;
		case SPRIAL_ALPHA_CLADO:
			ret = new SprialCladoAlignedWithAlpha(treeLayoutProperties, rootNode, this);
			break;
		case SPRIAL_BETA_PHYLO:
			ret = new SpiralPhyloWithBeta(treeLayoutProperties, rootNode, this);
			break;
		case SPRIAL_BETA_CLADO:
			ret = new SpiralCladoAlignedWithBeta(treeLayoutProperties, rootNode, this);
			break;
		default:
			// RADIAL_PHYLO
			ret = new RadialPhyloLayout(treeLayoutProperties, rootNode, this);
			break;
		}
		return ret;
	}

	private void recalculatedInstructions(int w, int h) {
		// 这个设置之前是为了防止JscollerPanel不出现滚动条
		// 现在还是只能这样否则它不出现滚动条
		setPreferredSize(new Dimension(w - 4, h - 4));
		if (layout == null) {
			return;
		}
		layout.calculateForPainting(w, h);

	}

	/**
	 * 鼠标滚轮的滑动！
	 * 
	 * @param heightChange
	 * @param widthChange
	 * @param p
	 */
	public void continuouslyZoomInOrOut(int heightChange, int widthChange, Point p) {

		int tempHeight = currentHeight + heightChange;
		int tempWidth = currentWidth + widthChange;
		if (tempHeight < 0.5 * getHeight() || tempWidth < 0.5 * getWidth()) {
			return;
		}
		double d1 = tempHeight / (double) (currentHeight);
		double d2 = tempWidth / (double) (currentWidth);

		currentHeight = tempHeight;
		currentWidth = tempWidth;
		recalculatedInstructions(currentWidth, currentHeight);

		// The left control panel should update the value
		Point location = getLocation();

		int offX = (int) (p.x * d2) - p.x;
		int offY = (int) (p.y * d1) - p.y;

		setLocation(location.x - offX, location.y - offY);

		logger.trace("The mouse point location is {} {}", p.getX(), p.getY());
		logger.trace("Current location is {} {}", location.getX(), location.getY());
		logger.trace("The new location is {} {}", location.x - offX, location.y - offY);

		Container parent = getParent();
		parent.doLayout();
		/*
		 * 终于解决这个问题了。
		 * https://stackoverflow.com/questions/115103/how-do-you-implement-position-
		 * sensitive-zooming-inside-a-jscrollpane 之前是用这个
		 * revalidate()，但是这种方式一直有一个问题：第一次直接 zoom in at this node的时候，一直在左上角。
		 * this.revalidate();
		 */

	}

	public void discreteZoomInOrOut(int heightChange, int widthChange) {
		List<GraphicsNode> selectedNodes = layoutProp.getSelectedNodes();
		if (selectedNodes.size() > 0) {
			GraphicsNode node = selectedNodes.get(selectedNodes.size() - 1);
			double xSelf = node.getXSelf();
			double ySelf = node.getYSelf();

			Point point = new Point();
			point.setLocation(xSelf, ySelf);
			continuouslyZoomInOrOut(heightChange, widthChange, point);
		} else {
			SwingDialog.showInfoMSGDialog("Choose node first", "Your need to choose node first");
		}

	}

	public TreeLayoutProperties getLayoutProperties() {
		return layoutProp;
	}

	public void resetNewRoot(GraphicsNode newRoot) {
		this.rootNode = newRoot;
		layout.setRootNode(newRoot);
		treeListener.setRootNode(newRoot);
	}

	public void updateViewAccording2TreeLayoutForAnnotationDialog() {
		layout = obtainRightLayout(TreeLayout.RECT_CLADO_ALIGNED_LEFT);
		actionsForAnnotationDialog();
		updatePhylogeneticTreePanel();

	}

	private void actionsForAnnotationDialog() {
		removeMouseMotionListener(treeListener);
		removeMouseWheelListener(treeListener);
		removeMouseListener(treeListener);
		removeKeyListener(treeListener);

		layout.setIs4AnnotationDialog(true);
	}

	public void setGlobalTreeBoolean(boolean b) {
		isGlobalPhylogeneticTreePanel = b;
	}

	public boolean isGlobalPhylogeneticTreePanel() {
		return isGlobalPhylogeneticTreePanel;
	}

	public ScaleBarProperty getScaleBarProperty() {
		return scaleBarProperty;
	}

	public GraphicsNode getRootNode() {
		return rootNode;
	}

	public void setIsShowAnnotation(boolean b) {
		layout.setIsShowAnnotation(b);

	}

	public AnnotationsProperties getMainAnnotationsProperties() {
		return mainAnnotationsProperties;
	}

	public void setMainAnnotationsProperties(AnnotationsProperties annotationsProperties) {
		this.mainAnnotationsProperties = annotationsProperties;
	}

	public AnnotationsProperties4LinageType getLinageTypeAnnotationsProperties() {
		return linageTypeAnnotationsProperties;
	}

	public void setLinageTypeAnnotationsProperties(AnnotationsProperties4LinageType linageTypeAnnotationsProperties) {
		this.linageTypeAnnotationsProperties = linageTypeAnnotationsProperties;
	}

	public void setWidthAndHeight(int width, int height) {
		currentWidth = width;
		currentHeight = height;
	}

	public Dimension getTreePanelDimension() {
		return new Dimension(currentWidth, currentHeight);
	}

	public BaseLayout getCurrLayout() {
		return layout;
	}

	public TreeListener getTreeListener() {
		return treeListener;
	}
}
