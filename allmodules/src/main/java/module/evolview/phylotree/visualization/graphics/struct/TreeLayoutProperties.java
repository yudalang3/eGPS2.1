package module.evolview.phylotree.visualization.graphics.struct;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import evoltree.txtdisplay.BaseGraphicNode;
import graphic.engine.guicalculator.BlankArea;
import module.evolview.phylotree.visualization.graphics.large.interactive.TreePopupMenu;
import evoltree.struct.util.EvolNodeUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class TreeLayoutProperties {
    /**
     * 这里存储的是最最原始读进来的根节点，
     * 有可能这棵树是抽样的树，这里保存原来的树
     */
    private BaseGraphicNode originalRoot;
    /**
     * 注意： collapse是专门给可视化的 Node用的，其余进化树节点根本不需要。
     */
    protected Map<Integer, CollapseProperty> collapsePropertyMaps;

    private final List<BaseGraphicNode> selectedNodes = Lists.newLinkedList();
    private final List<BaseGraphicNode> leavesNodes = Lists.newLinkedList();


    private DecimalFormat deciFor = new DecimalFormat("0.000000");

    private double widthCompressFactor = 1;
    private double heightCompressFactor = 1;


    // Painting properties!
    private boolean showScaleBar = false;
    private boolean showRoot = false;
    private double rootTipLength = 10;

    private boolean showBranchLabel = false;
    /**
     * yudalang: 0 is Mutation ; 1 is the vertical line ; 2 is the branch length
     */
    private int branchLabelIndex = 0;

    private boolean showWidthAndHeightString = false;
    private boolean showTitle = true;
    private String titleString = "The phylogenetic tree with {0} high-quality sequenced samples.";
    private boolean showAxisBar = true;
    private boolean needReverseAxisBar = false;
    private String branchLengthUnit;

    private TreeLayout myLayout = TreeLayout.RECT_CLADO_ALIGNED_LEFT;
    private boolean whetherWidthScaleOnMouseWheel = true;
    private boolean whetherHeightScaleOnMouseWheel = true;

    private int radicalLayoutRotationDeg = 0;
    private final RectangularLayoutProperty rectangularLayoutProperty = new RectangularLayoutProperty();
    private final CircularLayoutProperty circularLayoutProperty = new CircularLayoutProperty();
    private final SprialLayoutProperty sprialLayoutProperty = new SprialLayoutProperty();
    private final SlopeLayoutProperty slopeLayoutProperty = new SlopeLayoutProperty();
    private final ShowInnerNodePropertiesInfo showInnerNodePropertiesInfo = new ShowInnerNodePropertiesInfo();
    private final ShowLeafPropertiesInfo showLeafPropertiesInfo = new ShowLeafPropertiesInfo();

    // Render utils
    private CellRendererPane leafLabelRendererPane = null;
    private TreePopupMenu treePopupMenu;

    private AdvancedParametersBean advancedPara = new AdvancedParametersBean();

    private Font globalFont = new Font("Arial", Font.PLAIN, 12);
    private Font titleFont = new Font("Arial", Font.BOLD, 12);
    private Font axisFont = globalFont;

    private boolean isCreativeMode = false;
    private Image backgroundImage4CreativeMode;

    protected BlankArea blankArea = new BlankArea(20, 10, 80, 10);

    private boolean shouldLeafNameRightAlign = false;


    private TreeLayoutProperties() {

    }

    public TreeLayoutProperties(BaseGraphicNode roots) {
        this();
        this.collapsePropertyMaps = Maps.newHashMap();

        setOriginalRoot(roots);
        initializeRoot();
    }


    public void setOriginalRoot(BaseGraphicNode currentRoot) {
        originalRoot = currentRoot;
    }


    public BaseGraphicNode getOriginalRootNode() {
        return originalRoot;
    }

    /**
     * 现在需要调用这个方法的情况：0427 1. 这个对象被New出来的时候！ 2. 当有节点被 collapse的时候！
     */
    public void initializeRoot() {
        recursive2initializeParameters(originalRoot);
        EvolNodeUtil.initializeSize(originalRoot);
    }

    /**
     * 这个是很长的遍历节点函数，包含了如下功能！第一次载入数据会被调用！
     * 1. set Depth 属性
     * 2. 把叶子节点加入到全局变量中，供后面快捷调用！
     */
    protected void recursive2initializeParameters(BaseGraphicNode root) {

        NodeType decideNodeType = TreeDecideUtil.decideNodeType(root, originalRoot);

        switch (decideNodeType) {
            case LEAF:
                leavesNodes.add(root);
                root.setDepth(0);
                break;
            default:
                int childCount = root.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    BaseGraphicNode child = (BaseGraphicNode) root.getChildAt(i);
                    recursive2initializeParameters(child);
                }
                root.setDepth(getCurrNodeDepth(root));
                break;
        }
    }

    private int getCurrNodeDepth(BaseGraphicNode curr) {
        int count = curr.getChildCount();
        int max = -1;
        for (int i = 0; i < count; i++) {
            int tt = ((BaseGraphicNode) curr.getChildAt(i)).getDepth() + 1;
            if (tt > max) {
                max = tt;
            }
        }
        return max;
    }

    /**
     * 内节点的纵坐标取子节点的纵坐标的平均.
     * 这里的base有可能是角度！
     *
     *
     * @param base input the parent node
     * @return the average number of base Y!
     */
    public double averageValueOfChildrenForParent(BaseGraphicNode base) {
        double sum = 0.0;
        int size = base.getChildCount();
        for (int i = 0; i < size; i++) {
            sum += base.getChildAt(i).getDoubleVariable();
        }
        return sum / size;
    }

    public Font getDefaultLeafFont() {
        return globalFont;
    }


    /**
     * <pre>
     * 也就是root的深度加一！
     * 例如：
     *    |---Leaf1
     * ---|（root）
     *    |---Leaf2
     *
     * 那么这个函数返回2!
     * </pre>
     *
     * @return 最大深度加1
     */
    public double getMaxDepthPlusOne() {
        return originalRoot.getDepth() + 1.0;
    }

    public DecimalFormat getDeciFor() {
        return deciFor;
    }

    public BigDecimal getEfficientDigits(double a, int num) {
        BigDecimal b = new BigDecimal(String.valueOf(a));
        BigDecimal divisor = BigDecimal.ONE;
        MathContext mc = new MathContext(num);
        return b.divide(divisor, mc);
    }

    public void setDeciFor(DecimalFormat deciFor) {
        this.deciFor = deciFor;
    }


    public TreeLayout getMyLayout() {
        return myLayout;
    }

    public void setMyLayout(TreeLayout myLayout) {
        this.myLayout = myLayout;
    }

    public List<BaseGraphicNode> getLeaves() {
        return leavesNodes;
    }

    public CellRendererPane getLeafLabelPan() {
        if (leafLabelRendererPane == null) {
            this.leafLabelRendererPane = new CellRendererPane();
        }
        return leafLabelRendererPane;
    }

    public boolean isShowRoot() {
        return showRoot;
    }

    public void setShowRoot(boolean showRoot) {
        this.showRoot = showRoot;
    }


    public double getRootTipLength() {
        return rootTipLength;
    }

    public void setRootTipLength(double rootTipLength) {
        this.rootTipLength = rootTipLength;
    }

    public RectangularLayoutProperty getRectangularLayoutProperty() {
        return rectangularLayoutProperty;
    }

    public CircularLayoutProperty getCircularLayoutProperty() {
        return circularLayoutProperty;
    }

    public SprialLayoutProperty getSprialLayoutProperty() {
        return sprialLayoutProperty;
    }

    public SlopeLayoutProperty getSlopeLayoutProperty() {
        return slopeLayoutProperty;
    }


    public List<BaseGraphicNode> getSelectedNodes() {
        return selectedNodes;
    }

    public boolean isShowBranchLabel() {
        return showBranchLabel;
    }

    public void setShowBranchLabel(boolean showBranchLabel, int index) {
        this.showBranchLabel = showBranchLabel;
        this.branchLabelIndex = index;
    }

    public int getBranchLabelIndex() {
        return branchLabelIndex;
    }

    public TreePopupMenu getTreePopupMenu() {
        return treePopupMenu;
    }

    public void setTreePopupMenu(TreePopupMenu leafLabelPopupMenu) {
        this.treePopupMenu = leafLabelPopupMenu;
    }

    public Map<Integer, CollapseProperty> getCollapsePropertyMaps() {
        return collapsePropertyMaps;
    }

    public ShowLeafPropertiesInfo getShowLeafPropertiesInfo() {
        return showLeafPropertiesInfo;
    }


    public ShowInnerNodePropertiesInfo getShowInnerNodePropertiesInfo() {
        return showInnerNodePropertiesInfo;
    }

    public void setGlobalFont(Font font) {
        this.globalFont = font;
    }

    public Font getGlobalFont() {
        return globalFont;
    }

    public void setTitleFont(Font font) {
        titleFont = font;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public void setAxisFont(Font font) {
        axisFont = font;
    }

    public Font getAxisFont() {
        return axisFont;
    }

    /**
     * 全局性的显示要不要画，这是由 控制面板传过来的指令。 每个layout内在有控制的，phylogram是要画的
     *
     * @return the showScaleBar
     */
    public boolean isShowScaleBar() {
        return showScaleBar;
    }

    /**
     * @param showScaleBar the showScaleBar to set
     */
    public void setShowScaleBar(boolean showScaleBar) {
        this.showScaleBar = showScaleBar;
    }

    /**
     * 全局性的显示要不要画，这是由 控制面板传过来的指令。 每个layout内在有控制的，phylogram是要画的
     *
     * @return the showAxisBar
     */
    public boolean isShowAxisBar() {
        return showAxisBar;
    }

    /**
     * @param showAxisBar the AxisBar to set
     */
    public void setShowAxisBar(boolean showAxisBar) {
        this.showAxisBar = showAxisBar;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean b) {
        this.showTitle = b;
    }


    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
    }

    /**
     * @return the radicalLayoutRotationDeg
     */
    public int getRadicalLayoutRotationDeg() {
        return radicalLayoutRotationDeg;
    }

    /**
     * @param radicalLayoutRotationDeg the radicalLayoutRotationDeg to set
     */
    public void setRadicalLayoutRotationDeg(int radicalLayoutRotationDeg) {
        this.radicalLayoutRotationDeg = radicalLayoutRotationDeg;
    }

    public Image getOutgroupImage() {
        return null;
    }


    public boolean isCreativeMode() {
        return isCreativeMode;
    }

    public void setCreativeMode(boolean isCreativeMode) {
        this.isCreativeMode = isCreativeMode;
    }

    public void setShowWidthAndHeightString(boolean b) {
        this.showWidthAndHeightString = b;

    }

    public boolean isShowWidthAndHeightString() {
        return showWidthAndHeightString;
    }

    public boolean isWhetherWidthScaleOnMouseWheel() {
        return whetherWidthScaleOnMouseWheel;
    }

    public void setWhetherWidthScaleOnMouseWheel(boolean whetherWidthScaleOnMouseWheel) {
        this.whetherWidthScaleOnMouseWheel = whetherWidthScaleOnMouseWheel;
    }

    public boolean isWhetherHeightScaleOnMouseWheel() {
        return whetherHeightScaleOnMouseWheel;
    }

    public void setWhetherHeightScaleOnMouseWheel(boolean whetherHeightScaleOnMouseWheel) {
        this.whetherHeightScaleOnMouseWheel = whetherHeightScaleOnMouseWheel;
    }

    public BlankArea getBlankArea() {
        return blankArea;
    }

    public void setBlankArea(BlankArea blankArea) {
        this.blankArea = blankArea;
    }

    public void setShouldLeafNameRightAlign(boolean shouldLeafNameRightAlign) {
        this.shouldLeafNameRightAlign = shouldLeafNameRightAlign;

    }

    public boolean isShouldLeafNameRightAlign() {
        return shouldLeafNameRightAlign;
    }

    public boolean isNeedReverseAxisBar() {
        return needReverseAxisBar;
    }

    public void setNeedReverseAxisBar(boolean needReverseAxisBar) {
        this.needReverseAxisBar = needReverseAxisBar;
    }

    public String getBranchLengthUnit() {
        return branchLengthUnit;
    }

    public void setBranchLengthUnit(String branchLengthUnit) {
        this.branchLengthUnit = branchLengthUnit;
    }

    public double getWidthCompressFactor() {
        return widthCompressFactor;
    }

    public void setWidthCompressFactor(double widthCompressFactor) {
        this.widthCompressFactor = widthCompressFactor;
    }

    public double getHeightCompressFactor() {
        return heightCompressFactor;
    }

    public void setHeightCompressFactor(double heightCompressFactor) {
        this.heightCompressFactor = heightCompressFactor;
    }

    public void setAdvancedPara(AdvancedParametersBean advancedPara) {
        this.advancedPara = advancedPara;
    }

    public AdvancedParametersBean getAdvancedPara() {
        return advancedPara;
    }


    public Image getBackgroundImage4CreativeMode() {
        return backgroundImage4CreativeMode;
    }

    public void setBackgroundImage4CreativeMode(Image backgroundImage4CreativeMode) {
        this.backgroundImage4CreativeMode = backgroundImage4CreativeMode;
    }
}