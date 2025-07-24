package module.mutationpre.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import egps2.UnifiedAccessPoint;
import module.mutationpre.model.DrawConnectingLineElement;
import module.mutationpre.model.DrawMutationElement;
import module.mutationpre.model.DrawVariantsProperty;

public class ListenerOfPaintingPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	private PaintingPanel paintingPanel;
	private MutationPainter mutationPainter;

	private Point dragStartPoint;
	private boolean isRegionSelectionMode = false;

	// Set<DrawMutationElement> selectedElements = new HashSet<>();
	Set<Integer> selectedElementsIndexes = new HashSet<>();;

	private int previousX;
	private int previousY;

	public ListenerOfPaintingPanel() {

	}

	public void setMutationPainter(MutationPainter mutationPainter) {
		this.mutationPainter = mutationPainter;
	}

	public void setPaintingPanel(PaintingPanel paintingPanel) {
		this.paintingPanel = paintingPanel;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int wheelRotation = e.getWheelRotation();

		Dimension size = paintingPanel.getSize();

		// System.out.println("Current size is: " + size + "\t" + wheelRotation);

	}

	@Override
	public void mouseDragged(MouseEvent e) {

		Point point = e.getPoint();

		if (paintingPanel.shouldRotate4vertical) {
			paintingPanel.dragMovedDistance.x = (point.x - previousX);
			paintingPanel.dragMovedDistance.y = (point.y - previousY);

			e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
//			System.out.println("This is" + point);
//			System.out.println("Moved with " + paintingPanel.dragMovedDistance);
			previousX = point.x;
			previousY = point.y;
		} else {

			if (selectedElementsIndexes.isEmpty()) {
				if (dragStartPoint == null) {
					return;
				}

				isRegionSelectionMode = true;

				int x1 = Math.min(dragStartPoint.x, point.x);
				int y1 = Math.min(dragStartPoint.y, point.y);
				int x2 = Math.max(dragStartPoint.x, point.x);
				int y2 = Math.max(dragStartPoint.y, point.y);

				Rectangle dragRectangle = paintingPanel.getDragRectangle();
				dragRectangle.setFrame(x1, y1, x2 - x1, y2 - y1);
			} else {

				int movedX = point.x - previousX;
				int movedY = point.y - previousY;

				List<DrawConnectingLineElement> listOfMutationElement = mutationPainter.property.listofConnectingLineElements;
				for (Integer index : selectedElementsIndexes) {
					DrawConnectingLineElement drawConnectingLineElement = listOfMutationElement.get(index);

					int newX = drawConnectingLineElement.locationInMutationArea.x + movedX;
					int newY = drawConnectingLineElement.locationInMutationArea.y + movedY;
					drawConnectingLineElement.locationInMutationArea.setLocation(newX, newY);
				}

				previousX = point.x;
				previousY = point.y;
			}

		}

		paintingPanel.repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// System.out.println("mouseMoved");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// System.out.println("mouseClicked");
		if (SwingUtilities.isRightMouseButton(e)) {
			paintingPanel.shouldRotate4vertical = !paintingPanel.shouldRotate4vertical;
			int width = paintingPanel.getParent().getWidth();
			int height = paintingPanel.getParent().getHeight();
//			paintingPanel.setPreferredSize(new Dimension(height * 3, width));
			paintingPanel.heavyRefresh();
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (mutationPainter == null) {
			return;
		}

		if (mutationPainter.property == null) {
			return;
		}

		if (paintingPanel.shouldRotate4vertical) {
			e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}

		List<DrawVariantsProperty> listOfDrawVariants = mutationPainter.property.listOfDrawVariants;

		Point point = e.getPoint();
		Point destPoint = new Point();

		boolean hasEvent = false;

		DrawProperties drawProperties = paintingPanel.getDrawProperties();
		List<DrawMutationElement> selectedMutationElement = drawProperties.getSelectedMutationElement();
		
		for (DrawVariantsProperty drawVariantsProperty : listOfDrawVariants) {
			List<DrawMutationElement> listOfMutationElement = drawVariantsProperty.listOfMutationElement;
			int size = listOfMutationElement.size();
			for (int i = 0; i < size; i++) {
				DrawMutationElement mutationElement = listOfMutationElement.get(i);

				Rectangle rectangle = mutationElement.rectangle;

				AffineTransform af = new AffineTransform();
				Point pp = mutationElement.rotationPoint;
				af.rotate(-mutationPainter.property.degree, pp.x, pp.y);
				af.transform(point, destPoint);

				if (rectangle.contains(destPoint)) {
					mutationElement.selected = true;
					selectedElementsIndexes.add(i);
					hasEvent = true;
					selectedMutationElement.add(mutationElement);
				}
			}
		}

		if (hasEvent) {

		} else {
			for (DrawVariantsProperty drawVariantsProperty : listOfDrawVariants) {
				for (DrawMutationElement mutationElement : drawVariantsProperty.listOfMutationElement) {
					mutationElement.selected = false;
				}
			}
			selectedElementsIndexes.clear();
			selectedMutationElement.clear();
		}
		
		UnifiedAccessPoint.getInstanceFrame().refreshRightGraphicPropertiesPanel();

		paintingPanel.repaint();

		dragStartPoint = point;
		previousX = point.x;
		previousY = point.y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (isRegionSelectionMode) {

			Rectangle dragRectangle = paintingPanel.getDragRectangle();
			List<DrawVariantsProperty> listOfDrawVariants = mutationPainter.property.listOfDrawVariants;
			boolean hasEvent = false;

			for (DrawVariantsProperty drawVariantsProperty : listOfDrawVariants) {
				List<DrawMutationElement> listOfMutationElement = drawVariantsProperty.listOfMutationElement;

				int size = listOfMutationElement.size();
				for (int i = 0; i < size; i++) {
					DrawMutationElement mutationElement = listOfMutationElement.get(i);

					Rectangle rectangle = mutationElement.rectangle;
					if (dragRectangle.intersects(rectangle)) {
						mutationElement.selected = true;
						selectedElementsIndexes.add(i);
						hasEvent = true;
					} else {
						mutationElement.selected = false;
					}
				}
			}

			if (!hasEvent) {
				for (DrawVariantsProperty drawVariantsProperty : listOfDrawVariants) {
					List<DrawMutationElement> listOfMutationElement = drawVariantsProperty.listOfMutationElement;

					for (DrawMutationElement mutationElement : listOfMutationElement) {
						mutationElement.selected = false;
					}
				}
				selectedElementsIndexes.clear();
			}

			clearDragRectange();
		}

		isRegionSelectionMode = false;

		paintingPanel.repaint();

		if (paintingPanel.shouldRotate4vertical) {
			e.getComponent().setCursor(Cursor.getDefaultCursor());
		}

	}

	private void clearDragRectange() {
		dragStartPoint = null;
		paintingPanel.getDragRectangle().setFrame(0, 0, 0, 0);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// System.out.println("mouseEntered");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// System.out.println("mouseExited");
	}

	public Set<Integer> getSelectedElementsIndexes() {
		return selectedElementsIndexes;
	}
}
