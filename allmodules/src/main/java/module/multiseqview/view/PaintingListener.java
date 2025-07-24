package module.multiseqview.view;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Optional;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.multiseqview.SequenceStructureViewController;
import module.multiseqview.model.paint.AlongSequenceDirection;
import module.multiseqview.model.paint.PaintingData;

public class PaintingListener implements MouseListener, MouseMotionListener {

	private SequenceStructureViewController controller;
	private JPopupMenu popupMenu;
	private PaintingPanel container;

	private boolean ifPressedContainsSequence = false;
	private double xPressedShift = 0;
	private double yPressedShift = 0;

	Rectangle2D.Double rectDouble = new Rectangle2D.Double();
	private AlongSequenceDirection pressedAlongSequenceDirection;

	public PaintingListener(SequenceStructureViewController controller, PaintingPanel container) {
		this.controller = controller;
		this.container = container;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			popupMenu = new JPopupMenu("Quick adjust");

			JMenuItem menuItem = new JMenuItem("Refresh");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.recalculateAndRefresh();

				}
			});
			popupMenu.add(menuItem);

			popupMenu.show(container, e.getX(), e.getY());
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

		Optional<PaintingData> paintingData = container.getPaintingData();
		if (!paintingData.isPresent()) {
			return;
		}

		List<AlongSequenceDirection> aList = paintingData.get().getAlongSequenceDirections();

		Point point = e.getPoint();
		double xPoint = point.getX();
		double yPoint = point.getY();
		for (AlongSequenceDirection alongSequenceDirection : aList) {
			int x = alongSequenceDirection.getX();
			int y = alongSequenceDirection.getY();
			int width = alongSequenceDirection.getWidth();
			int height = alongSequenceDirection.getHeight();

			rectDouble.setFrame(x, y, width, height);

			if (rectDouble.contains(point)) {
				ifPressedContainsSequence = true;
				
				xPressedShift = xPoint - x;
				yPressedShift = yPoint - y;

				this.pressedAlongSequenceDirection = alongSequenceDirection;
				return;
			}

		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if (ifPressedContainsSequence) {

			e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			ifPressedContainsSequence = false;
			
			
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		
		if (ifPressedContainsSequence) {

			int x = e.getX();
			int y = e.getY();
			double newX = x- xPressedShift;
			double newY = y - yPressedShift;
			pressedAlongSequenceDirection.setX((int) newX);
			pressedAlongSequenceDirection.setY((int) newY);
			
			controller.calculateListOfAlongSequenceDirections();
			controller.refresh();
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		Optional<PaintingData> paintingData = container.getPaintingData();
		if (!paintingData.isPresent()) {
			return;
		}
		
		List<AlongSequenceDirection> aList = paintingData.get().getAlongSequenceDirections();

		Point point = e.getPoint();
		
		for (AlongSequenceDirection alongSequenceDirection : aList) {
			int x = alongSequenceDirection.getX();
			int y = alongSequenceDirection.getY();
			int width = alongSequenceDirection.getWidth();
			int height = alongSequenceDirection.getHeight();

			rectDouble.setFrame(x, y, width, height);

			if (rectDouble.contains(point)) {
				e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				return;
			}

		}
		
		e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}

}
