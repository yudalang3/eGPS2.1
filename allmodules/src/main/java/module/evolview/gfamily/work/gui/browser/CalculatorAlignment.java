package module.evolview.gfamily.work.gui.browser;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.AlignmentWithDerivedStatistics;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignment;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignmentItem;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignmentNucleotide;
import module.evolview.gfamily.work.gui.browser.draw.SequenceBaseColorEnum;
import module.evolview.model.AlignmentGetSequence;
import msaoperator.alignment.sequence.SequenceI;

public class CalculatorAlignment implements LocationCalculator<DrawingPropertyAlignment> {

	private double seqNameWidth = LocationCalculator.BLINK_LEFT_SPACE_LENGTH;
	private BrowserPanel genomeMain;
	protected final String indel = "-";

	private TrackAlignment genomerAlignment;

	public CalculatorAlignment(BrowserPanel genomeMain, TrackAlignment genomerAlignment) {
		this.genomeMain = genomeMain;
		this.genomerAlignment = genomerAlignment;
	}

	public DrawingPropertyAlignment calculatePaintingLocations(GeneDrawingLengthCursor drawProperties,
			AlignmentWithDerivedStatistics alignmentStati, int width, int height) {

		int drawStart = drawProperties.getDrawStart();
		int drawEnd = drawProperties.getDrawEnd();
		if (drawStart == drawEnd) {
			return null;
		}
		DrawingPropertyAlignment paintAlignment = new DrawingPropertyAlignment();
		double availableWidth = width - BLINK_LEFT_SPACE_LENGTH - BLINK_RIGHT_SPACE_LENGTH;
		int showCountlength = drawEnd - drawStart;
		double xRange = availableWidth / showCountlength;
		double charHeight = drawProperties.getCharHeight();
		double charWidth = drawProperties.getCharWidth();

		AlignmentGetSequence sequence = alignmentStati.getSequence();
		calculatorNucleotide(paintAlignment, drawStart, drawEnd, charWidth, xRange, charHeight, sequence);
		return paintAlignment;
	}

	/**
	 * 计算DNA位置信息
	 *
	 * @Author: mhl
	 * @Date Created on: 2020-05-18 14:20
	 */
	private void calculatorNucleotide(DrawingPropertyAlignment paintAlignment, int drawStart, int drawEnd,
			double charWidth, double xRange, double charHeight, AlignmentGetSequence sequence) {

		TrackAlignment genomeAlignment = genomeMain.getGenomerAlignment();
		boolean drawAlignmentNucleotide = xRange >= charWidth;
		genomeAlignment.isDrawAlignmentNucleotide(drawAlignmentNucleotide);
		genomeAlignment.setSingleSequenceWidth(xRange);
		List<DrawingPropertyAlignmentItem> paintAlignmentItems = new ArrayList<>();
		List<SequenceI> DNASeq = sequence.returnDNASequence(drawStart - 1, drawEnd - 1);
		int startRes = DNASeq.get(0).getStartRes();
		int endRes = DNASeq.get(0).getEndRes();
		int yOffset = 0;
		// for (SequenceI sequenceI : DNASeq) {
		int size = DNASeq.size();
		for (int i = 0; i < size; i++) {
			SequenceI sequenceI = DNASeq.get(i);
			yOffset += charHeight;
			double x1 = 0;
			double y1 = yOffset;
			double w = seqNameWidth;
			DrawingPropertyAlignmentItem paintAlignmentItem = new DrawingPropertyAlignmentItem();
			paintAlignmentItem.setAlignmentName(sequenceI.getSeqName());
			paintAlignmentItem.setAlignmentNameLocation(new Rectangle2D.Double(x1, y1, w, charHeight));
			List<DrawingPropertyAlignmentNucleotide> sequences = new ArrayList<>();
			double xOffset = 0;
			for (int k = 0; k < endRes - startRes; k++) {
				double x2 = BLINK_LEFT_SPACE_LENGTH + xOffset;
				DrawingPropertyAlignmentNucleotide paintAlignmentSequence = new DrawingPropertyAlignmentNucleotide();
				String tempCharSeq = sequenceI.getCharAt(k) + "";
				paintAlignmentSequence.setCharSeq(tempCharSeq);
				int location = startRes + k + 1;
				paintAlignmentSequence.setProLocation(location);
				paintAlignmentSequence.setBaseColor(SequenceBaseColorEnum.getBaseColor(tempCharSeq));
				// paintAlignmentSequence.setSeqLocation(new Rectangle2D.Double(x2, y1, xRange,
				// charHeight));
				paintAlignmentSequence.setX(x2);
				paintAlignmentSequence.setY(y1);
				paintAlignmentSequence.setW(xRange);
				paintAlignmentSequence.setH(charHeight);

				// 绘制gap三角形
//				if (drawAlignmentNucleotide) {
//					int gapPosSize = gapPos.size();
//					StringBuffer buffer = new StringBuffer();
//					for (int j = 0; j < gapPosSize; j++) {
//						Integer pos = gapPos.get(j);
//						if (pos + startRes == location) {
//							List<Character> characters = gapBase.get(i); // 当前物种gap碱基信息
//							Character character = characters.get(j);
//							buffer.append(character);
//						}
//					}
//					String currentGapBase = buffer.toString().replace(indel, "").trim(); // 当前位置的gapBase信息
//					if (currentGapBase != null && !"".equals(currentGapBase)) {
//						Point2D.Double p1 = new Point2D.Double(x2 + xRange, y1 + charHeight * 2 / 3);
//						Point2D.Double p2 = new Point2D.Double(x2 + xRange * 2 / 3, y1 + charHeight);
//						Point2D.Double p3 = new Point2D.Double(x2 + xRange, y1 + charHeight);
//						Triangle triangle = new Triangle(p1, p2, p3);
//						paintAlignmentSequence.setTriangle(triangle);
//						paintAlignmentSequence.setContainGapBase(true);
//						paintAlignmentSequence.setGapBase(currentGapBase);
//					}
//				}
				sequences.add(paintAlignmentSequence);
				xOffset += xRange;
			}
			paintAlignmentItem.setSequence(sequences);
			paintAlignmentItems.add(paintAlignmentItem);
		}
		paintAlignment.setPaintAlignmentItems(paintAlignmentItems);
	}

}