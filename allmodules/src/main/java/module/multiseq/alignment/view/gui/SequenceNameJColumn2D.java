package module.multiseq.alignment.view.gui;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName SequenceJColumn2D
 * 
 * @author mhl
 * 
 * @Date Created on:2019-07-11 14:14
 * 
 */
public class SequenceNameJColumn2D extends SequenceJShap {

	private ViewAreaSequenceNameJPanel sequenceNameJPanel;

	/**
	 * The column list.
	 */
	protected List<Rectangle2D.Double> colList;

	private int[] y_Array;

	public SequenceNameJColumn2D(ViewAreaSequenceNameJPanel sequenceNameJPanel) {
		this.sequenceNameJPanel = sequenceNameJPanel;
		colList = new ArrayList<Rectangle2D.Double>(100);
	}

	@Override
	public boolean contains(double x, double y) {
		Rectangle2D.Double col;
		for (int i = 0; i < colList.size(); i++) {
			col = (Rectangle2D.Double) (colList.get(i));
			if (col.contains(x, y)) {
				VisulizationDataProperty viewPort = sequenceNameJPanel.getAlignmentViewPort();

				int pos = viewPort.getStartSeq() + i;

				tipText = viewPort.getSequenceData().getName(pos);

				return true;
			}
		}
		return false;
	}

	public void repaint() {

		VisulizationDataProperty viewPort = sequenceNameJPanel.getAlignmentViewPort();

		int size = viewPort.getSize();

		y_Array = new int[size];

		calculateParameterOfCol(size, viewPort);

		colList.clear();

		Rectangle2D.Double col;

		for (int i = 0; i < y_Array.length; i++) {

			col = new Rectangle2D.Double(0, y_Array[i], sequenceNameJPanel.getWidth(), viewPort.getCharHeight());
			colList.add(col);
		}
	}

	private void calculateParameterOfCol(int size, VisulizationDataProperty viewPort) {

		int charHeight = viewPort.getCharHeight();

		for (int i = 0; i < size; i++) {

			y_Array[i] = charHeight * i;
		}

	}

	@Override
	public void assignValuesIfMousePointMeetShap(List<UserSelectedViewElement> selectionElements, double x, double y) {
		Rectangle2D.Double col;
		for (int i = 0; i < colList.size(); i++) {
			col = (Rectangle2D.Double) (colList.get(i));
			if (col.contains(x, y)) {
				VisulizationDataProperty viewPort = sequenceNameJPanel.getAlignmentViewPort();

				UserSelectedViewElement selectionElement = new UserSelectedViewElement();
				
				selectionElement.setxPos(0);

				selectionElement.setyPos(i + viewPort.getStartSeq());

				selectionElement.setSelectRowNum(viewPort.getSequenceData().getLength());

				selectionElement.setSelectColumnNum(1);

				selectionElements.add(selectionElement);

				break;
			}
		}
	}

	@Override
	public void assignValuesIfMousePointMeetShaps(List<UserSelectedViewElement> selectionElements, Rectangle2D dragRect) {
	}
}
