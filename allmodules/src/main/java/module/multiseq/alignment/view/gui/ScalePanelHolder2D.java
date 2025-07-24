package module.multiseq.alignment.view.gui;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ScalePanelHolder2D extends SequenceJShap {

	private ViewAreaScalePanelHolder scalePanelHolder;
	/**
	 * The column list.
	 */
	protected List<Rectangle2D.Double> colList;

	private int[] x_Array;

	public ScalePanelHolder2D(ViewAreaScalePanelHolder scalePanelHolder) {
		this.scalePanelHolder = scalePanelHolder;

		colList = new ArrayList<Rectangle2D.Double>(100);
	}

	@Override
	public boolean contains(double x, double y) {
		Rectangle2D.Double col;
		for (int i = 0; i < colList.size(); i++) {
			col = (Rectangle2D.Double) (colList.get(i));
			if (col.contains(x, y)) {

				return true;
			}
		}

		return false;
	}

	@Override
	public void repaint() {

		VisulizationDataProperty viewPort = scalePanelHolder.getAlignmentViewPort();

		int dataLength = viewPort.getLength();

		x_Array = new int[dataLength];

		calculateParameterOfCol(dataLength, viewPort);

		colList.clear();

		Rectangle2D.Double col;

		for (int i = 0; i < x_Array.length; i++) {
			col = new Rectangle2D.Double(x_Array[i], 0, viewPort.getCharWidth(), scalePanelHolder.getHeight());
			colList.add(col);
		}

	}

	private void calculateParameterOfCol(int dataLength, VisulizationDataProperty viewPort) {

		int charWidth = viewPort.getCharWidth();

		for (int j = 0; j < dataLength; j++) {

			x_Array[j] = charWidth * j;
		}

	}

	@Override
	public void assignValuesIfMousePointMeetShap(List<UserSelectedViewElement> selectionElements, double x, double y) {

		Rectangle2D.Double col;
		for (int i = 0; i < colList.size(); i++) {
			col = (Rectangle2D.Double) (colList.get(i));
			if (col.contains(x, y)) {
				VisulizationDataProperty viewPort = scalePanelHolder.getAlignmentViewPort();
				
				UserSelectedViewElement selectionElement = new UserSelectedViewElement();

				selectionElement.setxPos(i + viewPort.getStartRes());

				selectionElement.setyPos(0);

				selectionElement.setSelectRowNum(1);

				selectionElement.setSelectColumnNum(viewPort.getTotalSequenceCount());

				selectionElements.add(selectionElement);

				break;

			}
		}
	}

	@Override
	public void assignValuesIfMousePointMeetShaps(List<UserSelectedViewElement> selectionElements, Rectangle2D dragRect) {

	}

}
