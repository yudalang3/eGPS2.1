package module.evolview.gfamily.work.gui.browser;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimers;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimersNucleotide;

public class MouseListenerPrimers extends AbstractMouseListener {

    public MouseListenerPrimers(BrowserPanel genomeMain) {
        super(genomeMain);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        TrackPrimers genomePrimers = genomeMain.getGenomePrimers();
        Point point = e.getPoint();
        AbstractTrackTooltiper shape = genomePrimers.getShape();
        if (shape.contains(point.getX(), point.getY())) {
            genomeMain.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            genomeMain.setCursor(Cursor.getDefaultCursor());
        }

        Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, trackWidth, genomePrimers.getHeight());
        if (rectangle2d.contains(point.x, point.y)) {
            genomePrimers.setHighlightShow(true);
            genomeMain.updateUI();
        } else {
            genomePrimers.setHighlightShow(false);
            genomeMain.updateUI();
        }

        if (moveBlankArea(point, genomePrimers)) {
            genomePrimers.setHighlightShow(true);
            genomeMain.updateUI();
        } else {
            genomePrimers.setHighlightShow(false);
            genomeMain.updateUI();
        }

    }


    @Override
    public void mouseExited(MouseEvent e) {

        genomeMain.getGenomePrimers().setHighlightShow(false);

        genomeMain.updateUI();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        TrackPrimers genomePrimers = genomeMain.getGenomePrimers();
        setTrack(genomePrimers);
        super.mouseClicked(e);
        DrawingPropertyPrimerSet primerSet = genomePrimers.getPrimerSet();
        if (primerSet == null) {
            return;
        }
        Rectangle2D.Double rect = new Rectangle2D.Double();
        List<DrawingPropertyPrimers> primers = primerSet.getPrimers();


        for (DrawingPropertyPrimers primer : primers) {
            if (primer.isReferencePrimers()) {
                continue;
            }
            List<DrawingPropertyPrimersNucleotide> forwardPrimersNucleotides = primer.getForwardPrimersNucleotides();

            for (DrawingPropertyPrimersNucleotide forwardPrimersNucleotide : forwardPrimersNucleotides) {
                rect.setRect(forwardPrimersNucleotide.getX(), forwardPrimersNucleotide.getY(), forwardPrimersNucleotide.getW(), forwardPrimersNucleotide.getH());
                if (rect.contains(point.getX(), point.getY())) {
                    if (SwingUtilities.isRightMouseButton(e)) {

                        Component component = e.getComponent();
                        PopupMenuPrimers popupMenu = new PopupMenuPrimers(genomeMain);
                        JPopupMenu popup = popupMenu.getViewGenePopupMenu(primer);
                        popup.add(getViewPosJMenuItem());
                        popup.show(component, e.getX(), e.getY());
                        return;
                    }
                    
                }
                
             
            }
           
        }
        if ((e.getX()>LocationCalculator.BLINK_LEFT_SPACE_LENGTH)&(e.getX()<(genomeMain.getGenomerAlignment().getWidth()-LocationCalculator.BLINK_RIGHT_SPACE_LENGTH))){
			if (SwingUtilities.isRightMouseButton(e)) {
				Component component = e.getComponent();
				JPopupMenu popup = new JPopupMenu();
				popup.add(getViewPosJMenuItem());
				popup.show(component, e.getX(), e.getY());
			}
			genomeMain.updateUI();
		}

    }

}
