package module.evolview.phylotree.visualization.layout;

import com.google.common.base.Strings;
import egps2.utils.common.model.datatransfer.TwoTuple;
import evoltree.txtdisplay.TreeDrawUnit;
import graphic.engine.AxisTickCalculator;
import graphic.engine.guicalculator.BlankArea;
import module.evolview.gfamily.work.gui.DrawUtil;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.gui.tree.annotation.*;
import module.evolview.model.enums.BranchLengthType;
import module.evolview.model.tree.*;
import module.evolview.phylotree.visualization.graphics.struct.CollapseProperty;
import module.evolview.phylotree.visualization.graphics.struct.NodeType;
import module.evolview.phylotree.visualization.graphics.struct.ShowInnerNodePropertiesInfo;
import module.evolview.phylotree.visualization.graphics.struct.TreeDecideUtil;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The baseLayout that store the common part of all layout.
 */
public abstract class BaseLayout implements ITreeLayout {
    protected final TreeLayoutProperties treeLayoutProperties;
    protected final PhylogeneticTreePanel phylogeneticTreePanel;
    protected final ScaleBarProperty scaleBarProperty;

    protected ButtomScaleBarDrawProperty buttomScaleBarDrawProperty = new ButtomScaleBarDrawProperty();

    protected Font annotationFont;

    protected double currentHeight;
    protected double currentWidth;

    protected final BlankArea blankArea;
    protected AxisTickCalculator axisTickCalculator = new AxisTickCalculator();

    protected boolean is4AnnotationDialog = false;
    protected boolean isShowAnnotation = true;

    /** 比例尺 */
    protected double canvas2logicRatio = 0.0;

    private final GeneralPath generalPath = new GeneralPath();
    protected Line2D.Double lineDrawUtil = new Line2D.Double();

    protected GraphicsNode rootNode;

    protected Function<LinkedList<GraphicsNode>, OutterSidewardLocation> linageTypeSidewardAnnotationCalculator;
    protected Function<LinkedList<GraphicsNode>, OutterSidewardLocation> linageTypeNode2LeafAnnotationCalculator;

    protected String maxLengthLeafName;
    protected int maxLeafNameLength;

    protected boolean[][] hideNodeArray;

    protected Collection<TracingPaintingProperty> tracingPaintingShapes = new LinkedList<>();

    protected int numOfLeavesWithoutOutgroup;

    private BranchLengthType branchLengthType = BranchLengthType.DIVERGENCE;

    public BaseLayout(TreeLayoutProperties controller, GraphicsNode rootNode,
                      PhylogeneticTreePanel phylogeneticTreePanel) {

        this.treeLayoutProperties = controller;
        this.blankArea = controller.getBlankArea();
        this.scaleBarProperty = phylogeneticTreePanel.getScaleBarProperty();
        this.phylogeneticTreePanel = phylogeneticTreePanel;
        setRootNode(rootNode);

        annotationFont = treeLayoutProperties.getAxisFont();

        axisTickCalculator.setWorkSpaceRatio(1f);
    }

    public void setRootNode(GraphicsNode rootNode) {
        this.rootNode = rootNode;

        List<GraphicsNode> leavesWithoutOutgroup = GraphicTreePropertyCalculator.getLeaves(rootNode);
        numOfLeavesWithoutOutgroup = leavesWithoutOutgroup.size();

        // 更新最大的叶子名字！
        String maxLengthLeafName = "";
        for (GraphicsNode tt : leavesWithoutOutgroup) {
            if (tt.getName().length() > maxLengthLeafName.length()) {
                maxLengthLeafName = tt.getName();
            }
        }
        this.maxLengthLeafName = maxLengthLeafName;

        tracingPaintingShapes.clear();

    }

    public String getMaxLengthLeafName() {
        return maxLengthLeafName;
    }

    /**
     * 所有layout计算之前的公共部分！现在只是赋值height与width
     */
    protected void beforeCalculate(int width, int height) {
        currentHeight = height;
        currentWidth = width;

        int realWidth = blankArea.getWorkWidth(width);
        int realHeight = blankArea.getWorkHeight(height);

        double widthCompressFactor = treeLayoutProperties.getWidthCompressFactor();
        double heightCompressFactor = treeLayoutProperties.getHeightCompressFactor();

        int arrayWidth = (int) (widthCompressFactor * realWidth);
        int arrayHeight = (int) (heightCompressFactor * realHeight);

        if (arrayWidth > 0 && arrayHeight > 0) {
            hideNodeArray = new boolean[arrayWidth][arrayHeight];
        } else {
            hideNodeArray = new boolean[1][1];
        }

    }

    protected void afterCalculation() {
        if (isShowAnnotation) {
            configurateMainAnnotation(phylogeneticTreePanel.getMainAnnotationsProperties());
        }

        AnnotationsProperties4LinageType linageTypeAnnotationsProperties = phylogeneticTreePanel
                .getLinageTypeAnnotationsProperties();
        if (linageTypeAnnotationsProperties.hasAnnotation()) {
            configurateLinageTypeAnnotation(linageTypeAnnotationsProperties);
        }

        Map<CGBID, Color> nodes2highlight = treeLayoutProperties.getHighlightProperties().getNodes2highlight();

        tracingPaintingShapes.clear();
        // Tuple<Integer> sizeOfError = new Tuple<Integer>(0);
        recursive2doSth4eachNode(rootNode, node -> {
            // 配置tracing
            Color color = nodes2highlight.get(node.getCGBIDInstance());
            if (color != null) {
                tracingPaintingShapes.add(configurateTracingShape(node, color));
            }

            // 不要隐藏
            if (true) {
                node.setIfHideNode(false);
                return;
            }

            // 下面是优化显示
            // yudalang 优化显示,注意这里的 x 和 y的系数可以自己选择。
            // 越接近于0 ，显示的点约少，显示越快。
            double widthCompressFactor = treeLayoutProperties.getWidthCompressFactor();
            double heightCompressFactor = treeLayoutProperties.getHeightCompressFactor();

            int intXSelf = (int) (widthCompressFactor * (node.getXSelf() - blankArea.getLeft()));
            int intYSelf = (int) (heightCompressFactor * (node.getYSelf() - blankArea.getTop()));

            if (intXSelf < 0) {
                if (node.getParentCount() == 0) {
                    node.setIfHideNode(false);
                }
            } else if (intXSelf >= hideNodeArray.length) {
                node.setIfHideNode(false);
            } else {
                boolean[] bsArray = hideNodeArray[intXSelf];
                int length = bsArray.length;

                if (intYSelf < 0) {

                } else if (intYSelf >= length) {
                    node.setIfHideNode(false);
                } else {
                    if (bsArray[intYSelf]) {
                        node.setIfHideNode(true);
                    } else {
                        node.setIfHideNode(false);
                        bsArray[intYSelf] = true;
                    }
                }

            }

        });

    }

    /**
     * 递归的画每个节点，从而画树
     */
    protected void recursive2doSth4eachNode(GraphicsNode node, Consumer<GraphicsNode> consumer) {

        NodeType nodeType = TreeDecideUtil.decideNodeType(node, rootNode);
        switch (nodeType) {
            case LEAF:
                consumer.accept(node);
                break;
            default:
                for (int i = 0; i < node.getChildCount(); i++) {
                    recursive2doSth4eachNode((GraphicsNode) node.getChildAt(i), consumer);
                }

                // 改成后续遍历
                consumer.accept(node);
                break;
        }
    }

    protected void configurateMainAnnotation(AnnotationsProperties annotationsProperties) {
    }

    protected void configurateLinageTypeAnnotation(AnnotationsProperties4LinageType annotationsProperties) {

        GraphicsNode[] leavesArrayWithoutOutgroup = GraphicTreePropertyCalculator.getLeaves(rootNode)
                .toArray(new GraphicsNode[1]);

        if (annotationsProperties.isShowInsituAnno()) {
            annotationsProperties.clearAllInsituAnnos();
            Function<LinkedList<GraphicsNode>, OutterSidewardLocation> linageTypeNode2leavesAnnotationCalculator = new Function<LinkedList<GraphicsNode>, OutterSidewardLocation>() {
                @Override
                public OutterSidewardLocation apply(LinkedList<GraphicsNode> t) {

                    GraphicsNode first = t.getFirst();
                    GraphicsNode last = t.getLast();
                    GraphicsNode mrca = GraphicTreePropertyCalculator.getMostRecentCommonAnsester(first, last);
                    annotationsProperties.configurateOneInsituAnno(first.getID(), mrca.getXSelf(), mrca.getYSelf());
                    return null;
                }
            };
            annotationsProperties.configurateDrawPropOutterSidewardAnno4LinageTypes(2, leavesArrayWithoutOutgroup,
                    linageTypeNode2leavesAnnotationCalculator);
        }
        // leaf name annotation
        annotationsProperties.configurateLeafNamesAnnotaion();

        if (annotationsProperties.isShowSidewardAnno()) {
            annotationsProperties.configurateDrawPropOutterSidewardAnno4LinageTypes(0, leavesArrayWithoutOutgroup,
                    linageTypeSidewardAnnotationCalculator);
        }

        if (annotationsProperties.isShowInternalNode2leafAnno()) {
            annotationsProperties.configurateDrawPropOutterSidewardAnno4LinageTypes(1, leavesArrayWithoutOutgroup,
                    linageTypeNode2LeafAnnotationCalculator);
        }
    }

    protected void configurateButtomScaleBarDrawProperty(double maxLengthOfRoot2Leaf, GraphicsNode maxLengthLeaf) {
        buttomScaleBarDrawProperty.clear();

        int workWidth = blankArea.getWorkWidth((int) currentWidth);

        Pair<java.lang.Double, java.lang.Double> minMax = Pair.of(0.0, maxLengthOfRoot2Leaf);

        axisTickCalculator.setMinAndMaxPair(minMax);
        axisTickCalculator.setWorkingSpace(workWidth);
        axisTickCalculator.determineAxisTick();


        List<String> tickLabels = axisTickCalculator.getTickLabels();
        List<Integer> tickLocations = axisTickCalculator.getTickLocations();
        // do not forget
        axisTickCalculator.clear();

        int size = tickLabels.size();
        if (treeLayoutProperties.isNeedReverseAxisBar()) {
            for (int i = size - 1; i >= 0; i--) {
                String string = tickLabels.get(i);
                Integer integer = tickLocations.get(i);
                int plotValue = workWidth - integer;
                buttomScaleBarDrawProperty.addElement(string, plotValue);
            }
        } else {
            for (int i = 0; i < size; i++) {
                String string = tickLabels.get(i);
                Integer integer = tickLocations.get(i);
                buttomScaleBarDrawProperty.addElement(string, integer.intValue());
            }
        }

    }

    public void setBranchLengthType(BranchLengthType branchLengthType) {
        this.branchLengthType = branchLengthType;
    }

    public BranchLengthType getBranchLengthType() {
        return branchLengthType;
    }

    public void setIsShowAnnotation(boolean b) {
        this.isShowAnnotation = b;

    }

    protected boolean isLayoutSupportDrawScaleBar() {
        return true;
    }

    /**
     * 画比例尺
     *
     * @param g2d
     */
    private void drawScaleBar(Graphics2D g2d) {
        if (treeLayoutProperties.isShowScaleBar()) {
            drawScaleBar(g2d, scaleBarProperty);
        }
    }

    public void setIs4AnnotationDialog(boolean is4AnnotationDialog) {
        this.is4AnnotationDialog = is4AnnotationDialog;
    }

    /**
     * 给选中的节点加一个选中了的表示框 这个方法的作用就是说：
     *
     * <pre>
     *
     *  ----.  对于这个Node，调用后的结果就是：
     *
     *        -
     *   ----|.| 加一个框框
     *        -
     * </pre>
     *
     * @param g2d
     * @param x
     * @param y
     */
    private void paintSelectedRectangularSignatureDashLine(Graphics2D g2d, double x, double y) {
        int xx = (int) x;
        int yy = (int) y;
        Component selpan = LineLabel.getInstance().getRendLabel();
        treeLayoutProperties.getLeafLabelPan().paintComponent(g2d, selpan, phylogeneticTreePanel, xx - 7, yy - 7, 14,
                14, false);
    }

    /**
     *
     * 当现实叶子名称的时候经常需要得到最长的叶子名称所占的长度。
     *
     * @title getMaxLengthLeafNameWidthAccording2CurrentGlobalFont
     * @createdDate 2020-11-06 14:06
     * @lastModifiedDate 2020-11-06 14:06
     * @author yudalang
     * @since 1.7
     *
     * @return int
     */
    protected int getMaxLengthLeafNameWidthAccording2CurrentFont() {
        FontMetrics fontMetrics = phylogeneticTreePanel.getFontMetrics(treeLayoutProperties.getGlobalFont());
        int stringWidth = fontMetrics.stringWidth(maxLengthLeafName);
        return stringWidth;
    }

    /**
     *
     * 我们再来回顾一下五大Layout: 矩形，环形，螺旋形，倾斜型，无根树型
     *
     * <pre>
     *
     *    (x1,y1)   (x2,y2)
     *     .|------ . 这一段是一个节点应该绘制的。
     *     |
     * ----|
     * </pre>
     *
     *
     *
     * @title commonDrawEachUnit
     * @createdDate 2020-11-06 15:32
     * @lastModifiedDate 2020-11-06 15:32
     * @author yudalang
     * @since 1.7
     *
     * @param g2d
     * @param node
     * @param drawLine
     * @return void
     */
    protected void commonDrawEachUnit(Graphics2D g2d, GraphicsNode node, boolean drawLine) {
        double x1 = node.getXParent();
        double y1 = node.getYParent();

        double x2 = node.getXSelf();
        double y2 = node.getYSelf();

        TreeDrawUnit drawUnit = node.getDrawUnit();
        g2d.setColor(drawUnit.getLineColor());
        g2d.setStroke(drawUnit.getStroke());

        if (drawLine) {
            lineDrawUtil.setLine(x1, y1, x2, y2);
            g2d.draw(lineDrawUtil);
        }

        // 处理Collapse
        if (node.getCollapse()) {
            int triangleSize = 15;
            CollapseProperty collapseProperty = getCollapseProporty(node);
            if (collapseProperty != null) {
                triangleSize = collapseProperty.getTriangleSize();
                g2d.setColor(collapseProperty.getColor());
            }
            generalPath.reset();
            generalPath.moveTo(x2, y2);
            generalPath.lineTo(x2 + triangleSize, y2 + triangleSize);
            generalPath.lineTo(x2 + triangleSize, y2 - triangleSize);
            generalPath.closePath();

            double angleIfNeeded = node.getAngleIfNeeded();
            if (angleIfNeeded != 0) {
                double radians = Math.toRadians(angleIfNeeded);
                g2d.rotate(-radians, x2, y2);
                g2d.fill(generalPath);
                g2d.rotate(radians, x2, y2);
            } else {
                g2d.fill(generalPath);
            }

        }

    }

    /**
     *
     * 因为CGB2.0和CGB1.0 collapseProportyMaps的记录位置不同，故将此部分分离出来
     *
     * @title getCollapseProporty
     * @createdDate 2022-11-30 11:18
     * @lastModifiedDate 2022-11-30 11:18
     * @author yjn
     * @since 1.7
     *
     * @param node
     * @return
     * @return CollapseProperty
     */
    protected CollapseProperty getCollapseProporty(GraphicsNode node) {
        Map<Integer, CollapseProperty> collapseProportyMaps = treeLayoutProperties.getCollapsePropertyMaps();
        CollapseProperty collapseProperty = collapseProportyMaps.get(node.getID());
        return collapseProperty;
    }

    protected void drawInnerNodeProperties(Graphics2D g2d, GraphicsNode node) {
        TreeDrawUnit drawUnit = node.getDrawUnit();
        int circleRadius = drawUnit.getCircleRadius();

        double x2 = node.getXSelf();
        double y2 = node.getYSelf();

        g2d.setColor(drawUnit.getLineColor());

        DrawUtil.drawRoundClassifiedColor(g2d, (int) x2, (int) y2, circleRadius);

        ShowInnerNodePropertiesInfo showInnerNodePropertiesInfo = treeLayoutProperties.getShowInnerNodePropertiesInfo();
        if (drawUnit.isDrawName()) {
            g2d.setColor(Color.black);
            g2d.setFont(treeLayoutProperties.getGlobalFont());

            FontMetrics fontMetrics = g2d.getFontMetrics();
            int height = fontMetrics.getAscent();
            double angleIfNeeded = node.getAngleIfNeeded();

            int xx = (int) (x2 + circleRadius + 4);
            int yy = (int) (y2 + 0.5 * height);

            String drawString = node.getName();
            if (!Strings.isNullOrEmpty(drawString)) {
                if (angleIfNeeded != 0) {// 如果存在旋转角，就旋转
                    double radians = Math.toRadians(angleIfNeeded);
                    g2d.rotate(-radians, x2, y2);
                    g2d.drawString(drawString, xx, yy);
                    g2d.rotate(radians, x2, y2);
                } else {
                    g2d.drawString(drawString, xx, yy);
                }
            }
        }

        if (showInnerNodePropertiesInfo.isShowInternalNodeBranchLength()) {
            g2d.setColor(Color.black);
            g2d.setFont(treeLayoutProperties.getGlobalFont());

            FontMetrics fontMetrics = g2d.getFontMetrics();
            int height = fontMetrics.getAscent();
            double angleIfNeeded = node.getAngleIfNeeded();
            String drawString = String.valueOf(node.getRealBranchLength());
            int stringWidth = fontMetrics.stringWidth(drawString);

            double x1 = node.getXParent();
            int xx = (int) (0.5 * (x2 + x1 - stringWidth));
            int yy = (int) (y2 - 0.5 * height);

            if (angleIfNeeded != 0) {// 如果存在旋转角，就旋转
                double radians = Math.toRadians(angleIfNeeded);
                g2d.rotate(-radians, x2, y2);
                g2d.drawString(drawString, xx, yy);
                g2d.rotate(radians, x2, y2);
            } else {
                g2d.drawString(drawString, xx, yy);
            }
        }

        if (showInnerNodePropertiesInfo.isShowInternalNodeBootstrap()) {
            // boot strap属性还没加， 需要去做。
        }

    }

    /**
     * 如果需要的话给叶子加上标签。 Just draw the name
     *
     * @param g2d
     * @param node
     * @param x2
     * @param y2
     */
    protected void drawEachLeafNameGraphicsIfNeeded(Graphics2D g2d, GraphicsNode node, double x2, double y2) {

        TreeDrawUnit drawUnit = node.getDrawUnit();
        g2d.setColor(drawUnit.getLineColor());
        int circleRadius = drawUnit.getCircleRadius();

        // 处理折叠
        if (!node.getCollapse()) {
            DrawUtil.drawRoundClassifiedColor(g2d, (int) x2, (int) y2, circleRadius);
        }

        if (!drawUnit.isDrawName()) {
            return;
        }

        if (node.getChildCount() != 0) {
            return;
        }
        // 如果isShowLeaveLabel()为true就显示leave label
        String drawString = node.getName();

        if (Strings.isNullOrEmpty(drawString)) {
            return;
        }
        // 为什么还要判定 getChildCount()呢？因为这里有可能是折叠的节点
        // 如果是叶子节点才画leave label

        AnnotationsProperties annotationsProperties = phylogeneticTreePanel.getMainAnnotationsProperties();
        Map<Integer, Color> leafNameRenderMap = annotationsProperties.getLeafNameRenderMap();

        AnnotationsProperties4LinageType linageTypeAnnotationsProperties = phylogeneticTreePanel
                .getLinageTypeAnnotationsProperties();
        Map<Integer, Color> leafNameRenderMap2 = linageTypeAnnotationsProperties.getLeafNameRenderMap();

        g2d.setColor(Color.black);

        g2d.setFont(treeLayoutProperties.getGlobalFont());

        boolean shouldLeafNameRightAlign = treeLayoutProperties.isShouldLeafNameRightAlign();

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int height = fontMetrics.getAscent();
        double angleIfNeeded = node.getAngleIfNeeded();
        int xx = (int) (x2 + circleRadius + 4);

        if (shouldLeafNameRightAlign) {
            xx = xx + maxLeafNameLength - fontMetrics.stringWidth(drawString);
        }

        int yy = (int) (y2 + 0.5 * height);

        if (angleIfNeeded != 0) {// 如果存在旋转角，就旋转
            double radians = Math.toRadians(angleIfNeeded);
            g2d.rotate(-radians, x2, y2);
            g2d.drawString(drawString, xx, yy);
            Color color = leafNameRenderMap.get(node.getID());
            if (color != null) {
                int stringWidth = fontMetrics.stringWidth(drawString);
                g2d.setColor(color);
                g2d.fillRect(xx, yy - height, stringWidth, height);
            } else {
                color = leafNameRenderMap2.get(node.getID());
                if (color != null) {
                    int stringWidth = fontMetrics.stringWidth(drawString);
                    g2d.setColor(color);
                    g2d.fillRect(xx, yy - height, stringWidth, height);
                }
            }

            g2d.rotate(radians, x2, y2);
        } else {
            g2d.drawString(drawString, xx, yy);

            Color color = leafNameRenderMap.get(node.getID());
            if (color != null) {
                int stringWidth = fontMetrics.stringWidth(drawString);
                g2d.setColor(color);
                g2d.fillRect(xx, yy - height, stringWidth, height);
            } else {
                color = leafNameRenderMap2.get(node.getID());
                if (color != null) {
                    int stringWidth = fontMetrics.stringWidth(drawString);
                    g2d.setColor(color);
                    g2d.fillRect(xx, yy - height, stringWidth, height);
                }
            }

        }

    }

    private void drawScaleBar(Graphics2D g2d, ScaleBarProperty scaleBarProperty) {

        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(Color.black);

        g2d.setFont(treeLayoutProperties.getAxisFont());
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();

        List<String> displayedStrings = buttomScaleBarDrawProperty.getDisplayedStrings();
        List<java.lang.Double> displayeDoubles = buttomScaleBarDrawProperty.getDisplayeDoubles();

        int xAxis = (int) scaleBarProperty.getxLocation();
        int yAxis = (int) scaleBarProperty.getyLocation();

        // vertical bar
        g2d.drawLine(xAxis, yAxis, xAxis, yAxis + 10);

        java.lang.Double xValue = displayeDoubles.get(1);
        int xx = (int) (xAxis + xValue);
        double verticalTipLength = yAxis + 10;
        // vertical bar
        lineDrawUtil.setLine(xx, yAxis, xx, verticalTipLength);
        g2d.draw(lineDrawUtil);

        // horizontal bar
        g2d.drawLine(xx, yAxis + 5, xAxis, yAxis + 5);

        final String str = displayedStrings.get(1);

        float xStr = (float) (0.5 * (xx + xAxis) - 0.5 * fontMetrics.stringWidth(str));
        g2d.drawString(str, xStr, (float) (verticalTipLength + fontHeight));

//		double SHOW_LENGTH = 80;
//
//		g2d.drawLine(xAxis, yAxis, xAxis, yAxis + 4);
//		g2d.drawLine(xAxis, yAxis + 2, (int) (xAxis + SHOW_LENGTH), yAxis + 2);
//		g2d.drawLine((int) (xAxis + SHOW_LENGTH), yAxis, (int) (xAxis + SHOW_LENGTH), yAxis + 4);
//		scaleBarProperty.setFont(treeLayoutProperties.getAxisFont());
//		g2d.setFont(scaleBarProperty.getFont());
//		String displayedStr = null;
//		if (isLayoutSupportDrawScaleBar()) {
//			int intAdjOneUnitBranchLength = (int) (SHOW_LENGTH / canves2logicRatio);
//			displayedStr = String.valueOf(intAdjOneUnitBranchLength);
//		} else {
//			displayedStr = "Unit only for visual effect";
//		}
//
//		int strX = (int) (xAxis + SHOW_LENGTH / 2.0 - 0.5 * g2d.getFontMetrics().stringWidth(displayedStr));
//
//		g2d.drawString(displayedStr, strX, yAxis - 5);
    }

    /**
     *
     * <p>
     * Title: paintGraphics
     * </p>
     * <p>
     * Description: 这里的抽象类，提供一个基础的实现，绘制的时候会有开头与结尾有一些公共的绘制流程。
     * </p>
     *
     * @param g2d
     * @see ITreeLayout#paintGraphics(Graphics2D)
     *
     */
    @Override
    public void paintGraphics(Graphics2D g2d) {
        drawScaleBar(g2d);

        specificTreeDrawingProcess(g2d);
        firstRecursiveDraw(g2d, rootNode);

        maxLeafNameLength = getMaxLengthLeafNameWidthAccording2CurrentFont();
        lastRecursiveDraw(g2d, rootNode);
        // yjn:选中节点的绘制得放在第三次遍历中得进行，否则会被点绘制覆盖一些
        recursiveDrawSelectedNode(g2d, rootNode);
        commonTailInDrawingProcess(g2d);
    }

    /**
     *
     * 每个子类只需要绘制公共需要绘制的东西。
     *
     * @title treeDrawingProcess
     * @createdDate 2020-11-05 21:13
     * @lastModifiedDate 2020-11-05 21:13
     * @author yudalang
     * @param g2d
     *
     * @since 1.7
     *
     * @return void
     */
    protected abstract void specificTreeDrawingProcess(Graphics2D g2d);

    /**
     * 20220915 yjn:这个方法里只进行叶子节点的线的绘制。叶子节点的点的绘制为方法drawEachLeafNameGraphicsIfNeeded()
     */
    protected abstract void leafLineDrawingProcess(Graphics2D g2d, GraphicsNode node);

    protected abstract void rootDrawingProcess(Graphics2D g2d, GraphicsNode node);

    protected abstract void innerNodeDrawingProcess(Graphics2D g2d, GraphicsNode node);

    /**
     * 递归的画每个节点，从而画树
     *
     * @param node
     */
    private void firstRecursiveDraw(Graphics2D g2d, GraphicsNode node) {
        TreeDrawUnit drawUnit = node.getDrawUnit();

        NodeType nodeType = TreeDecideUtil.decideNodeType(node, rootNode);
        switch (nodeType) {
            case LEAF:
                boolean shouldDraw = false;
                if (drawUnit.isCollapse()) {
                    shouldDraw = true;
                } else {
                    // Draw the node if node not hide!
                    shouldDraw = !node.hideNode();
                }
                if (shouldDraw) {
                    // 这里先只画叶子节点的线，叶子节点的点再lastRecursiveDraw()里绘制，以防止后绘制的节点的线覆盖先绘制节点的点
                    leafLineDrawingProcess(g2d, node);
                }
                break;
            default:
                if (nodeType == NodeType.ROOT) {
                    rootDrawingProcess(g2d, node);
                } else {
                    shouldDraw = false;
                    if (drawUnit.isCollapse()) {
                        shouldDraw = true;
                    } else {
                        // Draw the node if node not hide!
                        shouldDraw = !node.hideNode();
                    }
                    if (shouldDraw) {
                        innerNodeDrawingProcess(g2d, node);
                    }
                }

                for (int i = 0; i < node.getChildCount(); i++) {
                    firstRecursiveDraw(g2d, (GraphicsNode) node.getChildAt(i));
                }
                break;
        }
    }

    protected TracingPaintingProperty configurateTracingShape(GraphicsNode node, Color color) {

        TracingPaintingProperty tracingPaintingProperty = new TracingPaintingProperty();
        GeneralPath generalPath = new GeneralPath();

        double xSelf = node.getXSelf();
        double ySelf = node.getYSelf();

        TreeDrawUnit drawUnit = node.getDrawUnit();

        generalPath.moveTo(xSelf, ySelf);

        drawUnit.setNodeSelected(true);

        GraphicsNode tempNode = node;
        while (tempNode.getParentCount() > 0) {
            generalPath.lineTo(tempNode.getXParent(), tempNode.getYParent());
            tempNode = (GraphicsNode) tempNode.getParent();
            generalPath.lineTo(tempNode.getXSelf(), tempNode.getYSelf());
        }

        tracingPaintingProperty.path = generalPath;
        tracingPaintingProperty.pointOfNode = new Point2D.Double(node.getXSelf(), node.getYSelf());
        tracingPaintingProperty.fillColor = color;

        return tracingPaintingProperty;
    }

    /**
     * 画顶上显示样本量的title 现在移到底部了，一个数据说明
     *
     * @param g2d
     */
    public void drawFixedTitle(Graphics2D g2d, int width, int height) {
        if (!is4AnnotationDialog && treeLayoutProperties.isShowTitle()) {
            g2d.setColor(new Color(255, 235, 205, 200));
            int threeDRectHeight = 30;

            g2d.fill3DRect(0, (int) (height - threeDRectHeight), (int) width, threeDRectHeight, true);

            Font titleFont = treeLayoutProperties.getTitleFont();
            g2d.setFont(titleFont);
            g2d.setColor(Color.darkGray);

            DecimalFormat df = new DecimalFormat(",###,###");

            String titleString = treeLayoutProperties.getTitleString();
            String drawString = MessageFormat.format(titleString, df.format(numOfLeavesWithoutOutgroup));

            FontMetrics fontMetrics = g2d.getFontMetrics();

            int startXAxis = (int) (0.5 * width - 0.5 * fontMetrics.stringWidth(drawString));
            g2d.drawString(drawString, startXAxis,
                    (int) (height - 0.5 * threeDRectHeight + 0.5 * fontMetrics.getHeight() - 3));

        }
    }

    /**
     * Now drawing the annotation and lineage tracing highlight!
     *
     * @param g2d
     */
    private void commonTailInDrawingProcess(Graphics2D g2d) {

        if (isShowAnnotation) {
            AnnotationsProperties annotationsProperties = phylogeneticTreePanel.getMainAnnotationsProperties();

            List<DrawPropInternalNode2LeafAnno> internalNode2LeafAnnos = annotationsProperties
                    .getInternalNode2LeafAnnos();
            for (DrawPropInternalNode2LeafAnno tt : internalNode2LeafAnnos) {
                if (tt.shouldConfigurateAndPaint()) {
                    tt.drawAnnotation(g2d);
                }
            }

            List<DrawPropOutterSidewardAnno> outterSidewardAnnos = annotationsProperties.getOutterSidewardAnnos();
            for (DrawPropOutterSidewardAnno tt : outterSidewardAnnos) {
                if (tt.shouldConfigurateAndPaint()) {
                    tt.drawAnnotation(g2d);
                }
            }
            List<DrawPropInternalNodeInsituAnno> internalNode2LeafInsituAnnos = annotationsProperties
                    .getInternalNode2LeafInsituAnnos();
            for (DrawPropInternalNodeInsituAnno tt : internalNode2LeafInsituAnnos) {
                if (tt.shouldConfigurateAndPaint()) {
                    tt.drawAnnotation(g2d);
                }
            }
        }

        if (isShowAnnotation) {
            AnnotationsProperties4LinageType linageTypeAnnotationsProperties = phylogeneticTreePanel
                    .getLinageTypeAnnotationsProperties();
            // In-situ annotations
            List<DrawPropInternalNodeInsituAnno> linageTypeinternalNode2LeafInsituAnnos = linageTypeAnnotationsProperties
                    .getInternalNode2LeafInsituAnnos();
            for (DrawPropInternalNodeInsituAnno tt : linageTypeinternalNode2LeafInsituAnnos) {
                tt.drawAnnotation(g2d);
            }
            // Side ward annotation
            List<DrawPropOutterSidewardAnno4LinageType> outterSidewardAnnos = linageTypeAnnotationsProperties
                    .getOutterSidewardAnnos();
            for (DrawPropOutterSidewardAnno4LinageType tt : outterSidewardAnnos) {
                tt.drawAnnotation(g2d);
            }
            // node2leaf annotation
            List<DrawPropOutterSidewardAnno4LinageType> internalNode2leafAnno4LinageTypes = linageTypeAnnotationsProperties
                    .getInternalNode2leafAnno4LinageTypes();
            for (DrawPropOutterSidewardAnno4LinageType tt : internalNode2leafAnno4LinageTypes) {
                tt.drawAnnotation(g2d);
            }

            g2d.setStroke(new BasicStroke(1f));
            g2d.setFont(annotationFont);
            Map<String, Color> categ2colorMap = linageTypeAnnotationsProperties.getCateg2colorMap();
            if (linageTypeAnnotationsProperties.getLeafNameRenderMap().size() > 0 && categ2colorMap.size() > 0) {
                List<TwoTuple<String, Color>> list = new ArrayList<TwoTuple<String, Color>>();

                for (Entry<String, Color> entry : categ2colorMap.entrySet()) {
                    list.add(new TwoTuple<String, Color>(entry.getKey(), entry.getValue()));
                }
                DrawUtil.drawLegend4Annotations(currentWidth, currentHeight, list, g2d);
            }
            // 这两者应该互斥
            Map<String, SidewardNodeAnnotation> categlory2SidewardNodeAnnotation = linageTypeAnnotationsProperties
                    .getCateglory2SidewardNodeAnnotation();
            if (categlory2SidewardNodeAnnotation.size() > 0) {
                List<TwoTuple<String, Color>> list = new ArrayList<TwoTuple<String, Color>>();
                for (Entry<String, SidewardNodeAnnotation> entry : categlory2SidewardNodeAnnotation.entrySet()) {
                    SidewardNodeAnnotation value = entry.getValue();
                    Color lineColor = value.getLineColor();
                    list.add(new TwoTuple<String, Color>(entry.getKey(), lineColor));
                }
                DrawUtil.drawLegend4Annotations(currentWidth, currentHeight, list, g2d);
            }

        }

        // highlight lineages

        if (!tracingPaintingShapes.isEmpty()) {
            BasicStroke basicStroke = new BasicStroke(2f);
            g2d.setStroke(basicStroke);

            for (TracingPaintingProperty bs : tracingPaintingShapes) {
                g2d.setColor(bs.fillColor);
                g2d.draw(bs.path);

                int circleRadius = DrawUtil.size;
                Double double1 = new Double(bs.pointOfNode.x - circleRadius,
                        bs.pointOfNode.y - circleRadius, 2 * circleRadius, 2 * circleRadius);
                g2d.fill(double1);

//				g2d.setColor(Color.black);
//				g2d.draw(double1);

            }
        }

    }

    private void lastRecursiveDraw(Graphics2D g2d, GraphicsNode node) {
        NodeType nodeType = TreeDecideUtil.decideNodeType(node, rootNode);

        if (nodeType == NodeType.LEAF) {
            if (!node.hideNode()) {
                drawEachLeafNameGraphicsIfNeeded(g2d, node, node.getXSelf(), node.getYSelf());
            }
            // 这里要return掉，不然collapse掉的点还会递归下去。collapse后用NodeType判定会判定为LEAF
            return;
        } else {
            // Internal node
            drawInnerNodeProperties(g2d, node);

        }

        for (int i = 0; i < node.getChildCount(); i++) {
            lastRecursiveDraw(g2d, (GraphicsNode) node.getChildAt(i));
        }

    }

    private void recursiveDrawSelectedNode(Graphics2D g2d, GraphicsNode node) {

        double x1 = node.getXParent();
        double y1 = node.getYParent();

        double x2 = node.getXSelf();
        double y2 = node.getYSelf();

        TreeDrawUnit drawUnit = node.getDrawUnit();

        if (drawUnit.isBranchSelected()) {
            paintSelectedRectangularSignatureDashLine(g2d, x1, y1);
            paintSelectedRectangularSignatureDashLine(g2d, x2, y2);
        }

        if (drawUnit.isNodeSelected()) {
            DrawUtil.paintSelectedRectangularSignatureSolidLine(g2d, x2, y2);
        }

        NodeType nodeType = TreeDecideUtil.decideNodeType(node, rootNode);

        if (nodeType == NodeType.LEAF) {
            // collapse后用NodeType判定会判定为LEAF。这里要return掉，这样collapse掉的内节点不会递归下去。
            return;
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            recursiveDrawSelectedNode(g2d, (GraphicsNode) node.getChildAt(i));
        }

    }

    public double getCanvas2logicRatio() {
        return canvas2logicRatio;
    }

    public BlankArea getBlankArea() {
        return blankArea;
    }
}
