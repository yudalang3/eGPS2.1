package module.sankeyplot.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D.Double;

import module.sankeyplot.SankeyPlotController;

public class SankeyMouseListener implements MouseListener {

    private final PaintingPanel sankeyPlotDrawer;

    public SankeyMouseListener(PaintingPanel sankeyPlotDrawer) {
        this.sankeyPlotDrawer = sankeyPlotDrawer;
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        boolean controlDown = e.isControlDown();
        Point point = e.getPoint();
        Double double1 = new Double(point.x - 2, point.y - 2, 4, 4);
        if (controlDown) {
            sankeyPlotDrawer.judgeIfSelectionsAndAsigneValues(double1);
        } else {
            sankeyPlotDrawer.clearSelection();
            sankeyPlotDrawer.judgeIfSelectionsAndAsigneValues(double1);
        }
        
        sankeyPlotDrawer.updateGraphics();
        
        SankeyPlotController controller = sankeyPlotDrawer.getSankeyPlotMain().getController();
        controller.refreshInstantStatus();
    }
}
