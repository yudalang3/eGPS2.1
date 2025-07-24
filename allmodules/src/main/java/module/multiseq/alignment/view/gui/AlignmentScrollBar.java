package module.multiseq.alignment.view.gui;

import java.awt.Adjustable;
import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

import module.multiseq.alignment.view.AlignmentViewExecutor;
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

public class AlignmentScrollBar extends JScrollBar implements AdjustmentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -161165917239251655L;

	private AlignmentViewMain alignmentViewMain;

	private AlignmentViewContinuousRightPanel alignmentViewInnerRightPanel;

	public static int DEFALUTMAXIMUM = 100;

	private VisulizationDataProperty alignmentViewPort;

	private SequenceDataForAViewer sequenceData;

	public AlignmentScrollBar(AlignmentViewMain alignmentViewMain,
			AlignmentViewContinuousRightPanel alignmentViewInnerRightPanel, int orientation) {

		this.alignmentViewMain = alignmentViewMain;

		this.alignmentViewInnerRightPanel = alignmentViewInnerRightPanel;

		this.alignmentViewPort = alignmentViewMain.getAlignmentViewPort();

		this.sequenceData = alignmentViewPort.getSequenceData();

		setOrientation(orientation);

		setUnitIncrement(1);

		setBlockIncrement(10);

		addAdjustmentListener(this);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		// System.out.println("This is adjustmentValueChanged!");
		// orientation == 0 横向滚动条; orientation == 1纵向滚动条
		// refProperty(alignmentViewMain.getAlignmentViewPort());

		alignmentViewMain.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		AlignmentViewExecutor.refScrollBarProperty(alignmentViewPort, orientation, getValue(),
				alignmentViewInnerRightPanel.getSequenceJPanel());
		setScrollValues(alignmentViewPort);
		AlignmentViewExecutor.reInitializeCurrentPaintSequences(alignmentViewPort);

	}

	public void setScrollValues(VisulizationDataProperty alignmentViewPort) {
		int startRes = alignmentViewPort.getStartRes();
		int startSeq = alignmentViewPort.getStartSeq();

		// rightVscroll.updateUI();
		SequenceDataForAViewer sequenceData = alignmentViewPort.getSequenceData();
		int sequenceLength = sequenceData.getLength();
		int sequenceSize = sequenceData.getTotalSequenceCount();
		int dataLength = alignmentViewPort.getLength();
		int size = alignmentViewPort.getSize();

		if (dataLength > sequenceLength) {
			dataLength = sequenceLength;
		}

		if (size > sequenceSize) {
			size = sequenceSize;
		}

		if ((dataLength + startRes) > sequenceLength) {
			startRes = sequenceLength - dataLength;
		}

		if ((size + startSeq) > sequenceSize) {
			startSeq = sequenceSize - size;
		}

		if (startSeq < 0) {
			startSeq = 0;
		}

		if (startRes < 0) {
			startRes = 0;
		}

		// update the scroll values
		if (orientation == Adjustable.HORIZONTAL) {
			setValues(startRes, dataLength, 0, sequenceLength);
		} else if (orientation == Adjustable.VERTICAL) {
			setValues(startSeq, size, 0, sequenceSize);
		}
	}

}
