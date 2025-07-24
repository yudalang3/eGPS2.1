package module.evolview.gfamily.work.gui.browser;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyKeyDomains;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;

public class MouseListenerKeyDomains extends AbstractMouseListener {

	public MouseListenerKeyDomains(BrowserPanel genomeMain) {
		super(genomeMain);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);

		TrackKeyDomains elementStructure = genomeMain.getElementStructure();

		Point point = e.getPoint();
		AbstractTrackTooltiper shape = elementStructure.getShape();
		if (shape.contains(point.getX(), point.getY())) {
			genomeMain.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			genomeMain.setCursor(Cursor.getDefaultCursor());
		}

		if (moveBlankArea(point, elementStructure)) {
			elementStructure.setHighlightShow(true);
			genomeMain.updateUI();
		} else {
			elementStructure.setHighlightShow(false);
			genomeMain.updateUI();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {

		genomeMain.getElementStructure().setHighlightShow(false);

		genomeMain.updateUI();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		TrackKeyDomains elementStructure = genomeMain.getElementStructure();
		setTrack(elementStructure);
		super.mouseClicked(e);
		DrawingPropertyKeyDomains paintElements = elementStructure.getPaintElements();
		if (paintElements == null) {
			return;
		}
		List<DrawingPropertySequenceModel> paintElementItems = paintElements.getGenomePaintElementItems();

		if (paintElementItems == null || paintElementItems.size() == 0) {
			return;
		}

		for (DrawingPropertySequenceModel paintElementItem : paintElementItems) {
			List<DrawingPropertyElement> genomeElementModels = paintElementItem.getGenomeElementModels();
			for (DrawingPropertyElement genomeElementModel : genomeElementModels) {
				RoundRectangle2D.Double roundRect = genomeElementModel.getRoundRect();
				if (roundRect.contains(e.getX(), e.getY())) {
					if (SwingUtilities.isRightMouseButton(e)) {
						Component component = e.getComponent();
						PopupMenuKeyDomains popupMenu = new PopupMenuKeyDomains(genomeMain);
						JPopupMenu popup = popupMenu.getViewGenePopupMenu(genomeElementModel);
						popup.add(getViewPosJMenuItem());
						popup.show(component, e.getX(), e.getY());
					}
					return;
				}

			}
			TrackKeyDomains genomerAlignment = genomeMain.getElementStructure();
			if ((e.getX() > LocationCalculator.BLINK_LEFT_SPACE_LENGTH)
					& (e.getX() < (genomerAlignment.getWidth()
							- LocationCalculator.BLINK_RIGHT_SPACE_LENGTH))) {
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
}
