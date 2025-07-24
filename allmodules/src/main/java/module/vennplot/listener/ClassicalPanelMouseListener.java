package module.vennplot.listener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import module.vennplot.gui.ClassicalVennPlotOutterFrame;
import module.vennplot.gui.classical.ClassicalPaintingLocations;
import module.vennplot.gui.classical.ClassicalPaintingPanelVenn;
import module.vennplot.model.ClassicalParameterModel;
import module.vennplot.painter.NameSelection;
import module.vennplot.painter.NameSelectionJudger;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName ClassicalPanelMouseListener
 * 
 * @author mhl
 * @param <V>
 * 
 * @Date Created on:2019-12-13 16:27
 * 
 */
public class ClassicalPanelMouseListener implements MouseListener {

	final private ClassicalPaintingPanelVenn paintingPanel;
	final private ClassicalVennPlotOutterFrame classicalPanel;

	NameSelectionJudger nameSelectionJudger = new NameSelectionJudger();

	public ClassicalPanelMouseListener(ClassicalPaintingPanelVenn paintingPanel,
			ClassicalVennPlotOutterFrame classicalPanel) {
		this.paintingPanel = paintingPanel;
		this.classicalPanel = classicalPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		int button = e.getButton();

		if (button == MouseEvent.BUTTON1) {

			leftClickNotCtolDown(point);
		}

		classicalPanel.weakestUpdate();

		classicalPanel.getController().refreshInstantStatusPanel();
	}

	/**
	 * 只支持单选节点,当选择名称或者选择图形时,都是通过添加选择名称进行设置
	 * 
	 *
	 * @Title leftClickNotCtolDown
	 *
	 * @param point
	 * 
	 * @author mhl
	 *
	 * @Date Created on:2019-12-13 16:27
	 */
	private void leftClickNotCtolDown(Point point) {

		ClassicalParameterModel parameterModel = classicalPanel.getParameterModel();

		List<NameSelection> nameSelections = paintingPanel.parameterModel.getNameSelections();

		nameSelections.clear();

		ClassicalPaintingLocations paintingLocations = parameterModel.getClassicalPaintingLocations();

		List<Rectangle2D.Double> nameLocations = paintingLocations.getPaintingNameLocations();

		List<Ellipse2D.Double> paintingCircleLocations = paintingLocations.getPaintingCircleLocations();

		Rectangle2D.Double tt = new Rectangle2D.Double(point.getX() - 1, point.getY() - 1, 2, 2);

		for (int i = 0; i < nameLocations.size(); i++) {
			Rectangle2D.Double double1 = nameLocations.get(i);
			if (double1.contains(tt)) {
				nameSelections.add(new NameSelection(i));
				return;
			}

		}

		for (int i = paintingCircleLocations.size() - 1; i >= 0; i--) {

			Ellipse2D.Double double2 = paintingCircleLocations.get(i);

			if (double2.contains(tt)) {

				nameSelections.add(new NameSelection(i));
				return;
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
