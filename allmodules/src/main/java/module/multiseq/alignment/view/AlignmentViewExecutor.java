package module.multiseq.alignment.view;

import java.awt.Adjustable;

import module.multiseq.alignment.view.gui.ViewAreaSequenceJPanel;
import module.multiseq.alignment.view.gui.VisulizationDataProperty;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

public class AlignmentViewExecutor {

	public static void reInitializeCurrentPaintSequences(VisulizationDataProperty alignmentViewPort) {
		int startRes = alignmentViewPort.getStartRes();

		int endRes = alignmentViewPort.getEndRes();

		SequenceDataForAViewer sequenceData = alignmentViewPort.getSequenceData();
		sequenceData.initializeCurrentPaintSequcences(alignmentViewPort.getStartSeq(), alignmentViewPort.getEndSeq(),
				startRes, endRes);

		sequenceData.calculateStaticsOfSpecificColumns(startRes, endRes);

	}

	public static void refScrollBarProperty(VisulizationDataProperty alignmentViewPort, int orientation, int value,
			ViewAreaSequenceJPanel viewAreaSequenceJPanel) {
		if (orientation == Adjustable.HORIZONTAL) {
			int startRes = value;
			int width = viewAreaSequenceJPanel.getWidth();
			int charWidth = alignmentViewPort.getCharWidth();
			int showSeqNumsV = (int) Math.floor(width / charWidth);
			int endRes = showSeqNumsV + startRes;
			alignmentViewPort.setStartRes(startRes);
			alignmentViewPort.setEndRes(endRes);

		} else if (orientation == Adjustable.VERTICAL) {
			int startSeq = value;
			int height = viewAreaSequenceJPanel.getHeight();
			int charHeight = alignmentViewPort.getCharHeight();
			int showSeqNumsH = (int) Math.floor(height / charHeight);
			int endSeq = showSeqNumsH + startSeq;
			alignmentViewPort.setStartSeq(startSeq);
			alignmentViewPort.setEndSeq(endSeq);
		}

	}

}
