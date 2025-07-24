package module.multiseq.alignment.view.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import egps2.frame.gui.EGPSMainGuiUtil;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.model.AlignmentDrawProperties;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;
import msaoperator.alignment.sequence.SequenceComponentRatio;

public class AlignmentInterLeavedRightPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5900754753104073517L;
	private final String DEFUALT_ANNOTION = AnnotationSpaceFillerJPanel.DEFUALT_ANNOTION;
	private VisulizationDataProperty alignmentViewPort;
	private SequenceDataForAViewer sequenceData;
	private AlignmentDrawProperties alignmentDrawProperties;

	private int SCALEHEIGHT = 21; // scale height
	private final int LEFTDISTANCE = 10; // set left distance
	private final int SEQUENCEANNOTATIONHEIGHT = 50; // set annotation panel height
	private final int BLOCKDISTANCE = 20; // Set the spacing between blocks
	private final int EXCEPTSEQCOUNT = 1;

	private AlignmentViewInterLeavedPanel alignmentViewInterLeavedPanel;

	public AlignmentInterLeavedRightPanel(AlignmentViewMain alignmentViewMain,
			AlignmentViewInterLeavedPanel alignmentViewInterLeavedPanel) {

		this.alignmentViewPort = alignmentViewMain.getAlignmentViewPort();
		this.alignmentDrawProperties = alignmentViewMain.getAlignmentDrawProperties();
		this.sequenceData = alignmentViewPort.getSequenceData();
		this.alignmentViewInterLeavedPanel = alignmentViewInterLeavedPanel;

		setBackground(Color.white);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (sequenceData == null) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g;
		
		EGPSMainGuiUtil.setupHighQualityRendering(g2);

		g2.setFont(alignmentViewPort.getFont());

		int height = getHeight();

		int width = getWidth();

		if ((height == 0) || (width == 0)) {
			return; // If not need to show, return
		}
		int charWidth = alignmentViewPort.getCharWidth();

		int charHeight = alignmentViewPort.getCharHeight();

		int yOf = getFontMetrics(alignmentViewPort.getFont()).getDescent();

		SCALEHEIGHT = yOf * 2 + charHeight - yOf;
		// block的高度=scale高度+ sequence高度+
		// sequence与annotation之间的间距+annotation高度+charHeight+block之间的空隙
		int blockHeight = SCALEHEIGHT + alignmentViewPort.getTotalSequenceCount() * charHeight + charHeight
				+ SEQUENCEANNOTATIONHEIGHT + charHeight + BLOCKDISTANCE;

		int startSeq = alignmentViewPort.getStartSeq();

		int size = alignmentViewPort.getSize();

		int firstShowBlockHeight = SCALEHEIGHT + size * charHeight + charHeight + SEQUENCEANNOTATIONHEIGHT + charHeight
				+ BLOCKDISTANCE;

		int pageCount = (int) Math.ceil((height - firstShowBlockHeight) * 1.0 / blockHeight) + 1;

		int startRes = alignmentViewPort.getStartRes();

		int endRes = alignmentViewPort.getEndRes();

		AlignmentInterLeavedScrollBar rightJScrollBar = (AlignmentInterLeavedScrollBar) alignmentViewInterLeavedPanel
				.getRightJScrollBar();

		int value = rightJScrollBar.getValue();

		int showTotalLenght = alignmentViewPort.getLength();

		int nextBlockHeight = 0;

		for (int i = 0; i < pageCount; i++) {

			if (alignmentViewPort.getStartSeq() > alignmentViewPort.getEndSeq()) {

				continue;
			}

			if (i == 0) {

				int singleBlockCount = EXCEPTSEQCOUNT + alignmentViewPort.getTotalSequenceCount();

				int showStartNumber = value % singleBlockCount;

				drawScaleHolder(g2, charWidth, charHeight, nextBlockHeight);

				if (showStartNumber < singleBlockCount - 1) {
					drawSequenceName(g2, charWidth, charHeight, nextBlockHeight);

					drawSequence(g2, charWidth, charHeight, nextBlockHeight);
				}
				int pageShowCount = (int) Math.ceil((height - SCALEHEIGHT) * 1.0 / charHeight);

				if (pageShowCount > size) {
					drawSequenceAnnotation(g2, charWidth, charHeight, nextBlockHeight);
				}

			} else if (alignmentViewPort.getEndRes() <= alignmentViewPort.getTotalSequenceLength()
					&& alignmentViewPort.getEndRes() != alignmentViewPort.getStartRes()) {

				nextBlockHeight += firstShowBlockHeight;

				drawScaleHolder(g2, charWidth, charHeight, nextBlockHeight);

				drawSequenceName(g2, charWidth, charHeight, nextBlockHeight);

				drawSequence(g2, charWidth, charHeight, nextBlockHeight);

				drawSequenceAnnotation(g2, charWidth, charHeight, nextBlockHeight);

			}

			if (pageCount != 1 && i + 1 != pageCount) {

				alignmentViewPort.setStartSeq(0);

				alignmentViewPort.setEndSeq(alignmentViewPort.getTotalSequenceCount());

				alignmentViewPort.setStartRes(alignmentViewPort.getEndRes());

				alignmentViewPort.setEndRes(alignmentViewPort.getEndRes() + showTotalLenght);

				sequenceData.initializeCurrentPaintSequcences(alignmentViewPort.getStartSeq(),
						alignmentViewPort.getEndSeq(), alignmentViewPort.getStartRes(), alignmentViewPort.getEndRes());

				sequenceData.calculateStaticsOfSpecificColumns(alignmentViewPort.getStartRes(),
						alignmentViewPort.getEndRes());
			}
			nextBlockHeight = blockHeight * i;
		}

		alignmentViewPort.setStartSeq(startSeq);

		alignmentViewPort.setStartRes(startRes);

		alignmentViewPort.setEndRes(endRes);
		
		g2.dispose();

	}

	private void drawScaleHolder(Graphics2D g2, int charWidth, int charHeight, int nextBlockHeight) {
		int startRes = alignmentViewPort.getStartRes();

		int endRes = alignmentViewPort.getEndRes();

		FontMetrics fm = getFontMetrics(alignmentViewPort.getFont());

		int yOf = fm.getDescent();

		for (int i = startRes; i < endRes; i++) {

			int x = (i - startRes) * charWidth + alignmentViewPort.getBaseNameLenght() + LEFTDISTANCE + 7;

			int y = charHeight - yOf + nextBlockHeight;

			int posOffset = (i + 1);

			if (posOffset % 10 == 0) {
				g2.drawString(String.valueOf(posOffset), x, y);

				g2.drawLine(x + charWidth / 2, y + 2, x + charWidth / 2, y + yOf * 2);
			} else if (posOffset % 5 == 0) {
				g2.drawLine(x + charWidth / 2, y + yOf, x + charWidth / 2, y + yOf * 2);
			}
		}
	}

	private void drawSequenceName(Graphics2D g2, int charWidth, int charHeight, int nextBlockHeight) {

		int yOffset = SCALEHEIGHT + nextBlockHeight;

		List<SequenceI> dataSequences = sequenceData.getPaintSequences();

		for (SequenceI sequenceI : dataSequences) {
			if (sequenceI == null) {
				continue;

			}
			yOffset += charHeight;

			String seqName = sequenceI.getSeqName();

			if (seqName == null) {
				continue;// fix for racecondition
			}

			g2.drawString(seqName, LEFTDISTANCE, yOffset);
		}

	}

	protected void drawSequence(Graphics2D g2, int charWidth, int charHeight, int nextBlockHeight) {
		int yOffset = SCALEHEIGHT + nextBlockHeight;

		List<SequenceI> dataSequences = sequenceData.getPaintSequences();

		for (SequenceI sequence : dataSequences) {

			yOffset += charHeight;

			int xOffsetStart = alignmentViewPort.getBaseNameLenght() + LEFTDISTANCE + 7;

			alignmentDrawProperties.getSequenceBackgroundColor().drawSequence(g2, sequence, sequenceData, xOffsetStart,
					yOffset, charWidth, charHeight);
		}

	}

	private void drawSequenceAnnotation(Graphics2D g2, int charWidth, int charHeight, int nextBlockHeight) {

		int size = alignmentViewPort.getSize();

		int xPos = alignmentViewPort.getBaseNameLenght() + LEFTDISTANCE + 7;

		int yPos = size * charHeight + SCALEHEIGHT + SEQUENCEANNOTATIONHEIGHT + charHeight + nextBlockHeight;

		int seqWidth = alignmentViewPort.getLength() * charWidth;

		// 绘制一条直线,在统计量图形的最底部,防止位点缺失时出现空状态,影响美观
		g2.drawLine(xPos, yPos, xPos + seqWidth, yPos);

		List<SequenceComponentRatio> ratios = sequenceData.getRatio();

		int index = 0;

		FontMetrics fm = g2.getFontMetrics(alignmentViewPort.getFont());

		int ww = fm.stringWidth(DEFUALT_ANNOTION);

		g2.drawString(DEFUALT_ANNOTION, xPos - ww - 5, yPos);

		for (SequenceComponentRatio sequenceRatio : ratios) {

			Character base = sequenceRatio.getBase();

			Integer maxValue = sequenceRatio.getMaxValue();

			int x = xPos + charWidth * index;

			if (base == null || maxValue == null) {

				g2.fillRect(x, SEQUENCEANNOTATIONHEIGHT, charWidth, 0);
				g2.drawString("-", x, yPos + charHeight);
				index++;
				continue;
			}

			int newHeight = SEQUENCEANNOTATIONHEIGHT * maxValue / alignmentViewPort.getTotalSequenceCount();

			int y = yPos - newHeight;

			g2.fillRect(x, y, charWidth, newHeight);

			g2.drawString(base.toString(), x, yPos + charHeight);

			index++;

		}
	}

}
