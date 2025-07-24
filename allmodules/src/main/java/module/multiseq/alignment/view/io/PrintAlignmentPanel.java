package module.multiseq.alignment.view.io;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.alignment.view.AlignmentViewMain;
import module.multiseq.alignment.view.gui.VisulizationDataProperty;
import msaoperator.alignment.sequence.SequenceComponentRatio;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * Draw <code>alignment</code> output PDF panel
 * 
 * 
 * @ClassName PrintAlignmentPanel
 * 
 * @author mhl
 * 
 * @Date Created on:2019-11-09 10:25
 * 
 */
public class PrintAlignmentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -318906856510282041L;

	private int SCALEHEIGHT = 21; // scale height
	private final int LEFTDISTANCE = 10; // set left distance
	private final int SEQUENCEANNOTATIONHEIGHT = 50; // set annotation panel height
	private AlignmentViewMain alignmentViewMain;

	private PrintSequenceDataForAViewer sequenceData;

	private VisulizationDataProperty alignmentViewPort;

	private int startRes;

	private int endRes;

	private int charWidth;

	private int charHeight;

	public PrintAlignmentPanel(AlignmentViewMain alignmentViewMain, VisulizationDataProperty alignmentViewPort) {
		this.alignmentViewMain = alignmentViewMain;
		this.alignmentViewPort = alignmentViewPort;
		this.sequenceData = new PrintSequenceDataForAViewer(alignmentViewPort.getSequenceData().getDataSequences());
	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		sequenceData.initializeCurrentPaintSequcences(0, alignmentViewPort.getTotalSequenceCount(), startRes, endRes);

		sequenceData.calculateStaticsOfSpecificColumns(startRes, endRes);

		drawScaleHolder(g2, charWidth, charHeight);

		drawSequenceName(g2, charWidth, charHeight);

		drawSequence(g2, charWidth, charHeight);

		drawSequenceAnnotation(g2, charWidth, charHeight);
	}

	private void drawSequenceName(Graphics2D g2, int charWidth, int charHeight) {

		int yOffset = SCALEHEIGHT;

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

	protected void drawSequence(Graphics2D g2, int charWidth, int charHeight) {
		int yOf = getFontMetrics(alignmentViewPort.getFont()).getDescent();

		int yOffset = yOf * 2 + charHeight - yOf;

		List<SequenceI> dataSequences = sequenceData.getPaintSequences();

		for (SequenceI sequence : dataSequences) {

			yOffset += charHeight;

			int xOffsetStart = alignmentViewPort.getBaseNameLenght() + LEFTDISTANCE + 7;

			alignmentViewMain.getAlignmentDrawProperties().getSequenceBackgroundColor().drawSequence(g2, sequence,
					sequenceData, xOffsetStart, yOffset, charWidth, charHeight);
		}
	}

	private void drawScaleHolder(Graphics2D g2, int charWidth, int charHeight) {

		FontMetrics fm = getFontMetrics(alignmentViewPort.getFont());

		int yOf = fm.getDescent();

		for (int i = startRes; i < endRes; i++) {

			int x = (i - startRes) * charWidth + alignmentViewPort.getBaseNameLenght() + LEFTDISTANCE + 7;

			int y = charHeight - yOf;

			int posOffset = i + 1;

			if (posOffset % 10 == 0) {
				g2.drawString(String.valueOf(posOffset), x, y);

				g2.drawLine(x + charWidth / 2, y + 2, x + charWidth / 2, y + yOf * 2);
			} else if (posOffset % 5 == 0) {
				g2.drawLine(x + charWidth / 2, y + yOf, x + charWidth / 2, y + yOf * 2);
			}
		}
	}

	private void drawSequenceAnnotation(Graphics2D g2, int charWidth, int charHeight) {

		int size = sequenceData.getPaintSequences().size();

		int xPos = alignmentViewPort.getBaseNameLenght() + LEFTDISTANCE + 7;

		int yOf = getFontMetrics(alignmentViewPort.getFont()).getDescent();

		int SCALEHEIGHT = yOf * 2 + charHeight - yOf;

		int yPos = size * charHeight + SCALEHEIGHT + SEQUENCEANNOTATIONHEIGHT + charHeight;

		int seqWidth = (endRes - startRes) * charWidth;

		// 绘制一条直线,在统计量图形的最底部,防止位点缺失时出现空状态,影响美观
		g2.drawLine(xPos, yPos, xPos + seqWidth, yPos);

		List<SequenceComponentRatio> ratios = sequenceData.getRatio();

		int index = 0;

		FontMetrics fm = g2.getFontMetrics(alignmentViewPort.getFont());

		int ww = fm.stringWidth("Consensus");

		g2.drawString("Consensus", xPos - ww - 5, yPos);

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

	public void setStartRes(int startRes) {
		this.startRes = startRes;

	}

	public void setEndRes(int endRes) {
		this.endRes = endRes;

	}

	public void setCharWidth(int charWidth) {

		this.charWidth = charWidth;
	}

	public void setCharHeight(int charHeight) {

		this.charHeight = charHeight;
	}

}
