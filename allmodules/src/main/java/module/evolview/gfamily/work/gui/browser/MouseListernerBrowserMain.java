package module.evolview.gfamily.work.gui.browser;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class MouseListernerBrowserMain extends AbstractMouseListener {

    public MouseListernerBrowserMain(BrowserPanel genomeMain) {

        super(genomeMain);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        if (SwingUtilities.isRightMouseButton(e)) {
            Component component = e.getComponent();
            HideTrackPopupMenu popupMenu = new HideTrackPopupMenu(genomeMain);
            JPopupMenu popup = popupMenu.setTracksHidePopupMenu();
            popup.show(component, e.getX(), e.getY());
        }
    }
}
