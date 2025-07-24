package module.multiseq.alignment.view.gui;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import msaoperator.alignment.sequence.SequenceComponentRatio;

public class SequenceAnnotation2D extends SequenceJShap {

	private ViewAreaSequenceAnnotationJPanel sequenceAnnotation;
	/**
	 * The column list.
	 */
	protected List<Rectangle2D.Double> colList;

	private int[] x_Array;

	public SequenceAnnotation2D(ViewAreaSequenceAnnotationJPanel sequenceAnnotation) {
		this.sequenceAnnotation = sequenceAnnotation;

		colList = new ArrayList<Rectangle2D.Double>(100);
	}

	@Override
	public boolean contains(double x, double y) {
		Rectangle2D.Double col;
		for (int i = 0; i < colList.size(); i++) {
			col = (Rectangle2D.Double) (colList.get(i));
			if (col.contains(x, y)) {
				SequenceComponentRatio sequenceRatio = sequenceAnnotation.getRatios().get(i);

				Character base = sequenceRatio.getBase();
				Integer maxValue = sequenceRatio.getMaxValue();
				// The entire column is missing, you need to add a judgment, otherwise it will
				// throw a null pointer.
				if (base == null || maxValue == null) {

					return false;
				}
				int percentage = sequenceRatio.getPercentage();

				tipText = base + " " + percentage + "%";

				return true;
			}
		}

		return false;
	}

	@Override
	public void repaint() {

		VisulizationDataProperty viewPort = sequenceAnnotation.getAlignmentViewPort();

		int dataLength = viewPort.getLength();

		x_Array = new int[dataLength];

		calculateParameterOfCol(dataLength, viewPort);

		colList.clear();

		Rectangle2D.Double col;

		for (int i = 0; i < x_Array.length; i++) {
			col = new Rectangle2D.Double(x_Array[i], 0, viewPort.getCharWidth(), sequenceAnnotation.getHeight());
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
				VisulizationDataProperty viewPort = sequenceAnnotation.getAlignmentViewPort();
				
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
