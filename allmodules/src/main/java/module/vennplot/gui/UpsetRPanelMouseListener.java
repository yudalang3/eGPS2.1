package module.vennplot.gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import egps2.panels.dialog.opendialog.StingArrayDisplayDialog;
import egps2.panels.dialog.opendialog.StringDisplayAndChangeDialog;
import egps2.frame.gui.interacive.RectAdjustMent;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SingleRegionDataModel;
import module.vennplot.model.SingleRegionElement;
import module.vennplot.painter.BodyAndIntersectionJudger;
import module.vennplot.painter.BodyIntersectionSelection;
import module.vennplot.painter.NameSelection;
import module.vennplot.painter.NameSelectionJudger;
import module.vennplot.painter.SingleRegionPainterAndSelectionJudger;
import module.vennplot.painter.SingleRegionSelection;

public class UpsetRPanelMouseListener implements MouseListener, MouseMotionListener {

	final private PaintingPanelUpsetR paintingPanel;
	final private UpsetRPanelOuterPanel upsetRPanel;

	NameSelectionJudger nameSelectionJudger = new NameSelectionJudger();
	BodyAndIntersectionJudger bodyAndIntersectionJudger = new BodyAndIntersectionJudger();

	Point dragStart;
	private RectAdjustMent selectedRectAdjustMent;
	private boolean ifInteractiveDragAvailable = false;

	public UpsetRPanelMouseListener(PaintingPanelUpsetR paintingPanel, UpsetRPanelOuterPanel upsetRPanel) {
		this.paintingPanel = paintingPanel;
		this.upsetRPanel = upsetRPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		int clickCount = e.getClickCount();
		boolean controlDown = e.isControlDown();
		int button = e.getButton();

		if (clickCount == 1) {

			if (button == MouseEvent.BUTTON1) {
				if (controlDown) {
					leftClickCtolDown(point);
				} else {
					leftClickNotCtolDown(point);
				}

			} else {
				// right click
				if (bodyAndIntersectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations,
						paintingPanel.parameterModel, paintingPanel.dataModel, point)) {
					BodyIntersectionSelection bodyIntersectionSelection = bodyAndIntersectionJudger
							.getBodyIntersectionSelections().get(0);
					paintingPanel.parameterModel.getBodyIntersectionSelections().add(bodyIntersectionSelection);

					int index = bodyIntersectionSelection.getIndex();
					IntersectionRegionElement intersectionRegionElement = paintingPanel.parameterModel
							.getIntersectionRegionDataModel().getEles().get(index);

					intersectionRegionElement.setShowPantagrame(!intersectionRegionElement.ifShowPantagrame());
				}
			}
		} else if (clickCount == 2) {
			doubleClick(point);
		}

		upsetRPanel.weakestUpdate();

		upsetRPanel.getController().refreshInstantStatusPanel();
	}

	private void doubleClick(Point point) {
		clearAllSelections();
		ParameterModel parameterModel = paintingPanel.parameterModel;

		if (nameSelectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations, paintingPanel.parameterModel,
				paintingPanel.dataModel, point)) {

			NameSelection nameSelections = nameSelectionJudger.getNameSelections().get(0);
			paintingPanel.parameterModel.getNameSelections().add(nameSelections);
			int index = nameSelections.getIndex();

			if (index == -2) {
				StringDisplayAndChangeDialog stingArrayDisplayDialog = new StringDisplayAndChangeDialog((obj) -> {
					parameterModel.getIntersectionRegionDataModel().setTitle(obj.toString());
					paintingPanel.revalidate();
				});
				stingArrayDisplayDialog.setVisible(true);
			} else if (index == -1) {
				StringDisplayAndChangeDialog stingArrayDisplayDialog = new StringDisplayAndChangeDialog((obj) -> {
					parameterModel.getSingleRegionDataModel().setTitle(obj.toString());
					paintingPanel.revalidate();
				});
				stingArrayDisplayDialog.setVisible(true);
			} else {
				SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();

				SingleRegionElement singleRegionElement = singleRegionDataModel.getEles().get(index);

				StringDisplayAndChangeDialog stingArrayDisplayDialog = new StringDisplayAndChangeDialog((obj) -> {
					singleRegionElement.setSetName(obj.toString());
					paintingPanel.revalidate();
				});
				stingArrayDisplayDialog.setText(singleRegionElement.getSetName());
				stingArrayDisplayDialog.setVisible(true);
			}

		} else if (bodyAndIntersectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations,
				paintingPanel.parameterModel, paintingPanel.dataModel, point)) {
			BodyIntersectionSelection bodyIntersectionSelection = bodyAndIntersectionJudger
					.getBodyIntersectionSelections().get(0);
			paintingPanel.parameterModel.getBodyIntersectionSelections().add(bodyIntersectionSelection);

			int index = bodyIntersectionSelection.getIndex();
			IntersectionRegionElement intersectionRegionElement = parameterModel.getIntersectionRegionDataModel()
					.getEles().get(index);
			String[] elementNames = intersectionRegionElement.getElementNames();

			String arrayString = getArrayString(elementNames);
			StingArrayDisplayDialog stingArrayDisplayDialog = new StingArrayDisplayDialog();

			if (intersectionRegionElement != null) {
				if (intersectionRegionElement.getElements()[0].isIfGenomicRegion()) {
					stingArrayDisplayDialog.setSize(250, 550);
				}
			}

			stingArrayDisplayDialog.setText(arrayString);
			stingArrayDisplayDialog.setVisible(true);
		}

	}

	private String getArrayString(String[] a) {
		int iMax = a.length - 1;
		if (iMax == -1) {
			return "";
		}
		StringBuilder b = new StringBuilder();
		for (int i = 0;; i++) {
			b.append(a[i]);
			if (i == iMax)
				return b.toString();
			b.append("\n");
		}

	}

	private void leftClickCtolDown(Point point) {
		ParameterModel parameterModel = paintingPanel.parameterModel;
		if (parameterModel.getIfConatainsSelection()) {
			SingleRegionPainterAndSelectionJudger singleRegionPainter = paintingPanel.singleRegionPainter;

			if (singleRegionPainter.judgeWhetherSelected(paintingPanel.paintingLocations, paintingPanel.parameterModel,
					paintingPanel.dataModel, point)) {
				List<SingleRegionSelection> singleRegionSelections = paintingPanel.parameterModel
						.getSingleRegionSelections();

				if (singleRegionSelections.isEmpty()) {
					clearAllSelections();
				}
				singleRegionSelections.add(singleRegionPainter.getSelections().get(0));

			} else if (nameSelectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations,
					paintingPanel.parameterModel, paintingPanel.dataModel, point)) {

				NameSelection nameSelections = nameSelectionJudger.getNameSelections().get(0);

				List<NameSelection> nameSelectionsInPara = paintingPanel.parameterModel.getNameSelections();
				if (nameSelectionsInPara.isEmpty()) {
					clearAllSelections();
				}
				nameSelectionsInPara.add(nameSelections);
			} else if (bodyAndIntersectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations,
					paintingPanel.parameterModel, paintingPanel.dataModel, point)) {
				BodyIntersectionSelection bodyIntersectionSelection = bodyAndIntersectionJudger
						.getBodyIntersectionSelections().get(0);
				List<BodyIntersectionSelection> bodyIntersectionSelectionsInPara = paintingPanel.parameterModel
						.getBodyIntersectionSelections();
				if (bodyIntersectionSelectionsInPara.isEmpty()) {
					clearAllSelections();
				}
				bodyIntersectionSelectionsInPara.add(bodyIntersectionSelection);
			}
		} else {
			leftClickNotCtolDown(point);
		}

	}

	private void leftClickNotCtolDown(Point point) {
		clearAllSelections();
		SingleRegionPainterAndSelectionJudger singleRegionPainter = paintingPanel.singleRegionPainter;

		if (singleRegionPainter.judgeWhetherSelected(paintingPanel.paintingLocations, paintingPanel.parameterModel,
				paintingPanel.dataModel, point)) {
			List<SingleRegionSelection> singleRegionSelections = paintingPanel.parameterModel
					.getSingleRegionSelections();
			singleRegionSelections.add(singleRegionPainter.getSelections().get(0));
		} else if (nameSelectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations,
				paintingPanel.parameterModel, paintingPanel.dataModel, point)) {

			NameSelection nameSelections = nameSelectionJudger.getNameSelections().get(0);
			paintingPanel.parameterModel.getNameSelections().add(nameSelections);

		} else if (bodyAndIntersectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations,
				paintingPanel.parameterModel, paintingPanel.dataModel, point)) {
			BodyIntersectionSelection bodyIntersectionSelection = bodyAndIntersectionJudger
					.getBodyIntersectionSelections().get(0);
			paintingPanel.parameterModel.getBodyIntersectionSelections().add(bodyIntersectionSelection);
		}

//		Rectangle2D.Double tt = new Rectangle2D.Double(point.getX() - 1, point.getY() - 1, 2, 2);
//		paintingPanel.setRegionForDebug(tt);

	}

	private void clearAllSelections() {
		paintingPanel.parameterModel.clearAllSelections();

	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (e.isControlDown()) {
			return;
		}

		paintingPanel.requestFocus();
		Point point = e.getPoint();

		ifInteractiveDragAvailable = ifInteractiveDrag(point.getX(), point.getY());

		if (!ifInteractiveDragAvailable) {
			dragStart = point;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point point = e.getPoint();
		if (ifInteractiveDragAvailable || dragStart == null) {
			return;
		}

		double x1 = Math.min(dragStart.getX(), point.getX());
		double y1 = Math.min(dragStart.getY(), point.getY());
		double x2 = Math.max(dragStart.getX(), point.getX());
		double y2 = Math.max(dragStart.getY(), point.getY());

		clearAllSelections();
		Double double1 = new Double(x1, y1, x2 - x1, y2 - y1);
		SingleRegionPainterAndSelectionJudger singleRegionPainter = paintingPanel.singleRegionPainter;

		if (singleRegionPainter.judgeWhetherSelected(paintingPanel.paintingLocations, paintingPanel.parameterModel,
				paintingPanel.dataModel, double1)) {
			List<SingleRegionSelection> singleRegionSelections = paintingPanel.parameterModel
					.getSingleRegionSelections();
			singleRegionSelections.addAll(singleRegionPainter.getSelections());
		} else if (nameSelectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations,
				paintingPanel.parameterModel, paintingPanel.dataModel, double1)) {

			paintingPanel.parameterModel.getNameSelections().addAll(nameSelectionJudger.getNameSelections());

		} else if (bodyAndIntersectionJudger.judgeWhetherSelected(paintingPanel.paintingLocations,
				paintingPanel.parameterModel, paintingPanel.dataModel, double1)) {
			paintingPanel.parameterModel.getBodyIntersectionSelections()
					.addAll(bodyAndIntersectionJudger.getBodyIntersectionSelections());
		}

		dragStart = null;
		paintingPanel.setDragRect(null);
		upsetRPanel.weakestUpdate();
//		BioMainFrame.getInstance().updateMenuItemsCateglory2();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point point = e.getPoint();

		if (ifInteractiveDragAvailable) {
			selectedRectAdjustMent.adjustPaintings(point.getX(), point.getY());
		}

		if (dragStart == null) {
			return;
		}

		double x1 = Math.min(dragStart.getX(), point.getX());
		double y1 = Math.min(dragStart.getY(), point.getY());
		double x2 = Math.max(dragStart.getX(), point.getX());
		double y2 = Math.max(dragStart.getY(), point.getY());

		paintingPanel.setDragRect(new Double(x1, y1, x2 - x1, y2 - y1));
		upsetRPanel.weakestUpdate();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point point = e.getPoint();
		ifInteractiveDrag(point.getX(), point.getY());
	}

	private boolean ifInteractiveDrag(double x, double y) {
		PaintingLocations paintingLocations = paintingPanel.getPaintingLocations();
		RectAdjustMent verticalAdjustor = paintingLocations.getVerticalAdjustor();
		RectAdjustMent[] horizontalAdjustors = paintingLocations.getHorizontalAdjustors();

		boolean contains = verticalAdjustor.contains(x, y);
		if (contains) {
			paintingPanel.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			selectedRectAdjustMent = verticalAdjustor;
			return true;
		}

		for (RectAdjustMent rectAdjustMent : horizontalAdjustors) {
			contains = rectAdjustMent.contains(x, y);
			if (contains) {
				paintingPanel.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
				selectedRectAdjustMent = rectAdjustMent;
				return true;
			}
		}

		paintingPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		selectedRectAdjustMent = null;
		return false;

	}

}
