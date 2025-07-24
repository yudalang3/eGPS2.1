package module.evolview.gfamily.work.gui.browser;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;

public class MouseListenerAlignment extends AbstractMouseListener {

    public MouseListenerAlignment(BrowserPanel genomeMain) {
        super(genomeMain);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        TrackAlignment alignment = genomeMain.getGenomerAlignment();

        Point point = e.getPoint();

        AbstractTrackTooltiper shape = alignment.getShape();
        if (shape.contains(point.getX(), point.getY())) {
            genomeMain.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            genomeMain.setCursor(Cursor.getDefaultCursor());
        }

        if (moveBlankArea(point, alignment)) {
            alignment.setHighlightShow(true);
            genomeMain.updateUI();
        } else {
            alignment.setHighlightShow(false);
            genomeMain.updateUI();
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {

        genomeMain.getGenomerAlignment().setHighlightShow(false);
        genomeMain.updateUI();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TrackAlignment alignment = genomeMain.getGenomerAlignment();
        setTrack(alignment);
        super.mouseClicked(e);
//        Ncov2019GenomerAlignment alignment = genomeMain.getGenomerAlignment();
//        setTrack(alignment);
//        Point point = e.getPoint();
//        if (moveBlankArea(point, track)) {
//            if (SwingUtilities.isRightMouseButton(e)) {
//                Component component = e.getComponent();
//                HideTrackPopupMenu popupMenu = new HideTrackPopupMenu(genomeMain);
//                JPopupMenu popup = popupMenu.setTracksHidePopupMenu();
//                popup.show(component, e.getX(), e.getY());
//            }
//        }
        Point point = e.getPoint();
        if ((e.getX()>LocationCalculator.BLINK_LEFT_SPACE_LENGTH)&(e.getX()<(genomeMain.getGenomerAlignment().getWidth()-LocationCalculator.BLINK_RIGHT_SPACE_LENGTH))){
        if (SwingUtilities.isRightMouseButton(e)) {
            Component component = e.getComponent();
        	JPopupMenu popup = new JPopupMenu();
        	popup.add(getViewPosJMenuItem());
        	 popup.show(component, e.getX(), e.getY());
        	 }
		}
    }
}
