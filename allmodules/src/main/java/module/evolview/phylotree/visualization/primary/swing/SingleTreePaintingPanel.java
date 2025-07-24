package module.evolview.phylotree.visualization.primary.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.*;

import module.evolview.phylotree.visualization.graphics.struct.CollapseProperty;
import egps2.panels.dialog.SwingDialog;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;
import egps2.frame.gui.EGPSMainGuiUtil;
import evoltree.swingvis.OneNodeDrawer;
import evoltree.txtdisplay.ReflectGraphicNode;

public class SingleTreePaintingPanel<T extends EvolNode> extends JPanel {

    final ReflectGraphicNode<T> root;
    final Line2D.Double rectDouble = new Line2D.Double();

    private final FastestLeftRectangularLayoutCalculator calculator = new FastestLeftRectangularLayoutCalculator();

    private final OneNodeDrawer<T> oneNodeDrawer;
    private final SingleTreePaintingListener singleTreePaintingListener;
    private int currentHeight;
    private int currentWidth;

    private Consumer<Graphics2D> beforeDrawingProcedure;
    private JFrame frame;

    public SingleTreePaintingPanel(ReflectGraphicNode<T> root, OneNodeDrawer<T> oneNodeDrawer) {
        this.oneNodeDrawer = oneNodeDrawer;
        this.root = root;

        singleTreePaintingListener = new SingleTreePaintingListener(this);

        setAutoscrolls(true);
        setBackground(Color.white);
        addMouseListener(singleTreePaintingListener);
        addMouseWheelListener(singleTreePaintingListener);
        addMouseMotionListener(singleTreePaintingListener);

    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentHeight < getHeight() || currentWidth < getWidth()) {
            fitFrameRefresh();
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        // 设置抗锯齿
        EGPSMainGuiUtil.setupHighQualityRendering(g2d);

        if (beforeDrawingProcedure != null) {
            beforeDrawingProcedure.accept(g2d);
        }
        iterateTree(g2d, this.root);
    }

    public void setBeforeDrawingProcedure(Consumer<Graphics2D> beforeDrawingProcedure) {
        this.beforeDrawingProcedure = beforeDrawingProcedure;
    }

    @SuppressWarnings("unchecked")
    private void iterateTree(Graphics2D g2d, ReflectGraphicNode<T> node) {
        int childCount = node.getChildCount();

        Stroke stroke = g2d.getStroke();
        g2d.setStroke(node.getStroke());

        this.rectDouble.setLine(node.getXSelf(), node.getYSelf(), node.getXParent(), node.getYParent());
        g2d.draw(this.rectDouble);

        if (childCount > 0) {
            ReflectGraphicNode<T> firstChild = (ReflectGraphicNode<T>) node.getFirstChild();
            ReflectGraphicNode<T> lastChild = (ReflectGraphicNode<T>) node.getLastChild();
            this.rectDouble.setLine(firstChild.getXParent(), firstChild.getYParent(), lastChild.getXParent(),
                    lastChild.getYParent());
            g2d.draw(this.rectDouble);
        }

        g2d.setStroke(stroke);

        if (node.isCollapse()) {
            Map<String, CollapseProperty> collapsePropertyMaps = getProperties().collapsePropertyMaps;
            CollapseProperty collapseProperty = collapsePropertyMaps.get(node.getName());

            double x2 = node.getXSelf();
            double y2 = node.getYSelf();
            g2d.setColor(collapseProperty.getColor());
            int triangleSize = collapseProperty.getTriangleSize();
            GeneralPath generalPath = new GeneralPath();
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

        for (int i = 0; i < childCount; i++) {
            ReflectGraphicNode<T> childAt = (ReflectGraphicNode<T>) node.getChildAt(i);
            iterateTree(g2d, childAt);
        }


        if (oneNodeDrawer != null) {
            this.oneNodeDrawer.draw(g2d, node);
        }
    }


    public FastGraphicsProperties getProperties() {
        return calculator.getProperties();
    }

    public ReflectGraphicNode<T> getRoot() {
        return root;
    }

    void continuouslyZoomInOrOut(int heightChange, int widthChange, Point p) {
        int tempHeight = currentHeight + heightChange;
        int tempWidth = currentWidth + widthChange;
        if (tempHeight < 0.5 * getHeight() || tempWidth < 0.5 * getWidth()) {
            return;
        }
        double d1 = tempHeight / (double) (currentHeight);
        double d2 = tempWidth / (double) (currentWidth);

        currentHeight = tempHeight;
        currentWidth = tempWidth;
        Dimension dimension = new Dimension(tempWidth, tempHeight);
        calculator.calculateTree(root, dimension);

        // The left control panel should update the value
        Point location = getLocation();

        int offX = (int) (p.x * d2) - p.x;
        int offY = (int) (p.y * d1) - p.y;

        setLocation(location.x - offX, location.y - offY);

        setPreferredSize(new Dimension(tempWidth - 4, tempHeight - 4));

        Container parent = getParent();
        parent.doLayout();
    }

    public void fitFrameRefresh() {
        Container parent = this.getParent();
        if (parent instanceof JViewport viewPort) {

            currentWidth = viewPort.getWidth();
            currentHeight = viewPort.getHeight();

            setPreferredSize(new Dimension(currentWidth - 4, currentHeight - 4));

            Dimension dimension = new Dimension(currentWidth, currentHeight);
            calculator.calculateTree(root, dimension);
            viewPort.updateUI();
        } else {

            SwingDialog.showWarningMSGDialog("Warning to developers",
                    "The tree panel should be add in the JScrollPanel.");
        }

        // initialize the collapse properties
        Map<String, CollapseProperty> collapsePropertyMaps = getProperties().collapsePropertyMaps;
        EvolNodeUtil.recursiveIterateTreeIF(root, node -> {
            CollapseProperty collapseProperty = collapsePropertyMaps.get(node.getName());
            if (collapseProperty != null) {
                node.setCollapse(true);
            }
        });


    }


    public void setJFrame(JFrame frame) {
        this.frame = frame;
    }

    public JFrame getFrame() {
        return frame;
    }

    public ReflectGraphicNode<T> getSelectedNode() {
        return (ReflectGraphicNode<T>) singleTreePaintingListener.selectedNode;
    }

    public SingleTreePaintingListener getSingleTreePaintingListener() {
        return singleTreePaintingListener;
    }
}
