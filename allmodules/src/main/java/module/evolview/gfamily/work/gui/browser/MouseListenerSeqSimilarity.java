package module.evolview.gfamily.work.gui.browser;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;

public class MouseListenerSeqSimilarity extends AbstractMouseListener {

    public MouseListenerSeqSimilarity(BrowserPanel genomeMain) {
        super(genomeMain);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        TrackSeqSimilarity genomePolylinePlot = genomeMain.getGenomePolylinePlot();
        AbstractTrackTooltiper shape = genomePolylinePlot.getShape();
        Point point = e.getPoint();
        if (shape.contains(point.getX(), point.getY())) {
            genomeMain.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            genomeMain.setCursor(Cursor.getDefaultCursor());
        }
        if (moveBlankArea(point, genomePolylinePlot)) {
            genomePolylinePlot.setHighlightShow(true);
        } else {
            genomePolylinePlot.setHighlightShow(false);
        }
        genomeMain.updateUI();
    }

    @Override
    public void mouseExited(MouseEvent e) {

        genomeMain.getGenomePolylinePlot().setHighlightShow(false);

        genomeMain.updateUI();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TrackSeqSimilarity genomePolylinePlot = genomeMain.getGenomePolylinePlot();
        setTrack(genomePolylinePlot);
        super.mouseClicked(e);
        Point point = e.getPoint();
       
		if ((e.getX()>LocationCalculator.BLINK_LEFT_SPACE_LENGTH)&(e.getX()<(genomeMain.getGenomePolylinePlot().getWidth()-LocationCalculator.BLINK_RIGHT_SPACE_LENGTH))){
        if (SwingUtilities.isRightMouseButton(e)) {
            Component component = e.getComponent();
        	JPopupMenu popup = new JPopupMenu();
        	popup.add(getViewPosJMenuItem());
        	 popup.show(component, e.getX(), e.getY());
        	 }
		}
       
        
    }
}
