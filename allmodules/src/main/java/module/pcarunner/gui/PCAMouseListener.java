package module.pcarunner.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PCAMouseListener implements MouseMotionListener {

	private PaintingPanel paintingPanel;
	
	public PCAMouseListener(PaintingPanel paintingPanel) {
		this.paintingPanel = paintingPanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point point = e.getPoint();
		
		
		paintingPanel.setToolTipText(point.toString());
	}

}
