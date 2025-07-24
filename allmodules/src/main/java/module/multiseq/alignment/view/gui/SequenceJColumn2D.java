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
public class SequenceJColumn2D extends SequenceJShap {

	private ViewAreaSequenceJPanel sequenceJPanel;

	/**
	 * The column list.
	 */
	protected List<Rectangle2D.Double> colList;

	private int[] x_Array;

	private int[] y_Array;

	public SequenceJColumn2D(ViewAreaSequenceJPanel sequenceJPanel) {
		this.sequenceJPanel = sequenceJPanel;
		colList = new ArrayList<Rectangle2D.Double>(100);
	}

	@Override
	public boolean contains(double x, double y) {
		Rectangle2D.Double col;
		for (int i = 0; i < colList.size(); i++) {
			col = (Rectangle2D.Double) (colList.get(i));
			if (col.contains(x, y)) {
				VisulizationDataProperty viewPort = sequenceJPanel.getAlignmentViewPort();
				int dataLength = viewPort.getLength();
				int seq = viewPort.getStartSeq() + (int) (i / dataLength) + 1;

				int res = viewPort.getStartRes() + i % dataLength + 1;

				char[] chars = viewPort.getSequenceData().getSequenceI(seq - 1).getSeq();
				int numOfRealNucl = 0;
				for (int j = 0; j < res; j++) {
					if (chars[j] != '-') {
						numOfRealNucl++;
					}
				}

				tipText = "(seq:" + seq + ", aligned pos:" + res + ", non-gap pos:" + numOfRealNucl + ")";
				return true;
			}
		}
		return false;
	}

	public void repaint() {

		VisulizationDataProperty viewPort = sequenceJPanel.getAlignmentViewPort();

		int dataLength = viewPort.getLength();

		int size = viewPort.getSize();

		x_Array = new int[dataLength * size];

		y_Array = new int[dataLength * size];

		calculateParameterOfCol(size, dataLength, viewPort);

		colList.clear();

		Rectangle2D.Double col;

		for (int i = 0; i < x_Array.length; i++) {
			col = new Rectangle2D.Double(x_Array[i], y_Array[i], viewPort.getCharWidth(), viewPort.getCharHeight());
			colList.add(col);
		}
	}

	private void calculateParameterOfCol(int size, int dataLength, VisulizationDataProperty viewPort) {

		int charWidth = viewPort.getCharWidth();
		int charHeight = viewPort.getCharHeight();

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < dataLength; j++) {

				int count = i * dataLength + j;

				x_Array[count] = charWidth * j;

				y_Array[count] = charHeight * i;

			}
		}

	}

	@Override
	public void assignValuesIfMousePointMeetShap(List<UserSelectedViewElement> selectionElements, double x, double y) {
		Rectangle2D.Double col;
		for (int i = 0; i < colList.size(); i++) {
			col = (Rectangle2D.Double) (colList.get(i));
			if (col.contains(x, y)) {
				VisulizationDataProperty viewPort = sequenceJPanel.getAlignmentViewPort();

				int dataLength = viewPort.getLength();

				int xPos = i % dataLength + viewPort.getStartRes();

				int yPos = i / dataLength + viewPort.getStartSeq();

				UserSelectedViewElement selectionElement = new UserSelectedViewElement();

				selectionElement.setxPos(xPos);

				selectionElement.setyPos(yPos);

				selectionElement.setSelectRowNum(1);

				selectionElement.setSelectColumnNum(1);

				selectionElements.add(selectionElement);

				break;
			}
		}
	}

	@Override
	public void assignValuesIfMousePointMeetShaps(List<UserSelectedViewElement> selectionElements,
			Rectangle2D dragRect) {
		double startXPos = dragRect.getX();
		double startYPos = dragRect.getY();
		double width = dragRect.getWidth();
		double height = dragRect.getHeight();
		double endXPos = startXPos + width;
		double endYPos = startYPos + height;

		int xPos = 0;

		int yPos = 0;

		int x1Pos = 0;

		int y1Pos = 0;

		VisulizationDataProperty viewPort = sequenceJPanel.getAlignmentViewPort();
		int dataLength = viewPort.getLength();

		int charWidth = viewPort.getCharWidth();

		int charHeight = viewPort.getCharHeight();

		Rectangle2D.Double col;
		for (int i = 0; i < colList.size(); i++) {
			col = (Rectangle2D.Double) (colList.get(i));
			if (col.contains(startXPos, startYPos)) {

				xPos = i % dataLength + viewPort.getStartRes();

				yPos = i / dataLength + viewPort.getStartSeq();

				if (width == charWidth && height == charHeight) {

					UserSelectedViewElement selectionElement = new UserSelectedViewElement();

					selectionElement.setxPos(xPos);

					selectionElement.setyPos(yPos);

					selectionElement.setSelectColumnNum(1);

					selectionElement.setSelectRowNum(1);

					selectionElements.add(selectionElement);

					break;
				}

			} else if (col.contains(endXPos, endYPos)) {

				x1Pos = i % dataLength + viewPort.getStartRes();

				y1Pos = i / dataLength + viewPort.getStartSeq();

				UserSelectedViewElement selectionElement = new UserSelectedViewElement();

				selectionElement.setxPos(xPos);

				selectionElement.setyPos(yPos);
				if (xPos == x1Pos) {
					selectionElement.setSelectRowNum(1);
				} else {

					selectionElement.setSelectRowNum(x1Pos - xPos + 1);
				}

				if (yPos == y1Pos) {
					selectionElement.setSelectColumnNum(1);

				} else {
					selectionElement.setSelectColumnNum(y1Pos - yPos + 1);

				}

				selectionElements.add(selectionElement);

				break;
			}
		}
	}
}
