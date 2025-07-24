package module.multiseq.alignment.view.gui;

import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

public class AlignmentInterLeavedScrollBar extends JScrollBar implements AdjustmentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -161165917239251655L;

	private AlignmentViewMain alignmentViewMain;

	private AlignmentViewInterLeavedPanel alignmentViewInterLeavedPanel;

	public final int DEFALUTMAXIMUM = 100;

	private final int RIGHTDISTANCE = 50;

	private final int EXCEPTSEQCOUNT = 1;

	private VisulizationDataProperty alignmentViewPort;

	private SequenceDataForAViewer sequenceData;

	public AlignmentInterLeavedScrollBar(AlignmentViewMain alignmentViewMain,
			AlignmentViewInterLeavedPanel alignmentViewInterLeavedPanel, int orientation) {

		this.alignmentViewMain = alignmentViewMain;

		this.alignmentViewInterLeavedPanel = alignmentViewInterLeavedPanel;

		this.alignmentViewPort = alignmentViewMain.getAlignmentViewPort();

		this.sequenceData = alignmentViewPort.getSequenceData();

		setOrientation(orientation);

		setUnitIncrement(1);

		setBlockIncrement(10);

		addAdjustmentListener(this);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {

		refProperty(alignmentViewMain.getAlignmentViewPort());

		setScrollValues(alignmentViewPort);

		alignmentViewMain.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		refProperty(alignmentViewPort);

		setScrollValues(alignmentViewPort);

		int startRes = alignmentViewPort.getStartRes();

		int endRes = alignmentViewPort.getEndRes();

		if (startRes <= endRes) {

			sequenceData.initializeCurrentPaintSequcences(alignmentViewPort.getStartSeq(),
					alignmentViewPort.getEndSeq(), startRes, endRes);

			sequenceData.calculateStaticsOfSpecificColumns(startRes, endRes);
		}
	}

	private void refProperty(VisulizationDataProperty alignmentViewPort) {

		int width = alignmentViewInterLeavedPanel.getWidth() - alignmentViewPort.getBaseNameLenght() - RIGHTDISTANCE;

		int charWidth = alignmentViewPort.getCharWidth();

		int showDataLength = (int) Math.floor(width * 1.0 / charWidth);

		if (showDataLength > 10 && showDataLength % 10 != 0) {

			showDataLength = showDataLength / 10 * 10;
		}

		int value = getValue();

		int everyBlockContainsTheNumberOfCells = EXCEPTSEQCOUNT + alignmentViewPort.getTotalSequenceCount();

		int startRes = value / everyBlockContainsTheNumberOfCells * showDataLength;

		int endRes = showDataLength + startRes;

		alignmentViewPort.setStartRes(startRes);

		alignmentViewPort.setEndRes(endRes);

		int showStartNumber = value % everyBlockContainsTheNumberOfCells;

		alignmentViewPort.setStartSeq(showStartNumber);

		alignmentViewPort.setEndSeq(alignmentViewPort.getTotalSequenceCount());

	}

	private void setScrollValues(VisulizationDataProperty alignmentViewPort) {

		SequenceDataForAViewer sequenceData = alignmentViewPort.getSequenceData();

		int sequenceLength = sequenceData.getLength();

		int width = alignmentViewInterLeavedPanel.getWidth() - alignmentViewPort.getBaseNameLenght() - RIGHTDISTANCE;

		int charWidth = alignmentViewPort.getCharWidth();

		int showDataLength = (int) Math.floor(width * 1.0 / charWidth);

		if (showDataLength > 10 && showDataLength % 10 != 0) {

			showDataLength = showDataLength / 10 * 10;
		}

		int blockCount = (int) Math.ceil(sequenceLength * 1.0 / showDataLength);

		int everyBlockContainsTheNumberOfCells = EXCEPTSEQCOUNT + alignmentViewPort.getTotalSequenceCount();

		int newMax = everyBlockContainsTheNumberOfCells * blockCount;

		int value = getValue();

		int startSeq = alignmentViewPort.getStartSeq();

		int newValue = value / everyBlockContainsTheNumberOfCells * everyBlockContainsTheNumberOfCells + startSeq;

		if (newValue >= newMax) {
			newValue = newMax - 1;
		}

		setValues(newValue, 1, 0, newMax);
	}
}
