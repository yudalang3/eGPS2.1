package module.evolview.gfamily.work.gui.browser;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimers;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimersNucleotide;
import module.evolview.model.AlignmentGetSequence;
import msaoperator.alignment.sequence.SequenceI;

/**
 * 需要注意的是当绘制CalculatorPrimers的位置信息的时候,包含起始位置(左闭右闭原则)
 *
 * @author mhl
 */
public class CalculatorPrimers implements LocationCalculator<DrawingPropertyPrimerSet> {

	private Color referencePrimersBackground = new Color(108, 108, 108);
	private final int primersDist = 2;
	private final String gap = "*";
	// private List<Ncov2019GenomePaintPrimers> paintPrimers;
	private DrawingPropertyPrimerSet paintPrimerSet;
	private int showCountIndex;

	private BrowserPanel genomeMain;
	private AlignmentGetSequence alignmentGetSequence;

	public CalculatorPrimers(BrowserPanel genomeMain, DrawingPropertyPrimerSet drawingPropertyPrimerSet,
			AlignmentGetSequence alignmentGetSequence) {
		this.genomeMain = genomeMain;

		this.paintPrimerSet = drawingPropertyPrimerSet;
		this.alignmentGetSequence = alignmentGetSequence;
		init();
		PrimerUtil.createColors();
	}

	public DrawingPropertyPrimerSet getPaintPrimerSet() {
		return paintPrimerSet;
	}

	public void init() {
		List<DrawingPropertyPrimers> paintPrimers = paintPrimerSet.getPrimers();
		int size = paintPrimers.size();
		for (int i = 0; i < size; i++) {
			DrawingPropertyPrimers paintPrimer1 = paintPrimers.get(i);
			int fStart1 = paintPrimer1.getFStart();
			int rEnd1 = paintPrimer1.getREnd();
			int useCount = 0;
			for (int j = i + 1; j < size; j++) {
				DrawingPropertyPrimers paintPrimer2 = paintPrimers.get(j);
				int fStart2 = paintPrimer2.getFStart();
				int rEnd2 = paintPrimer2.getREnd();
				if (Math.min(rEnd1, rEnd2) >= Math.max(fStart1, fStart2)) {
					useCount += 2;
				}
			}
			paintPrimer1.setUsedTwice(-useCount);
		}
		this.showCountIndex = paintPrimers.stream().mapToInt(DrawingPropertyPrimers::getUsedTwice).min().getAsInt();
		this.showCountIndex = Math.abs(showCountIndex) + 2;
	}

	public DrawingPropertyPrimerSet calculatePaintingLocations(GeneDrawingLengthCursor drawProperties, int width,
			int height) {
		int drawStart = drawProperties.getDrawStart();
		int drawEnd = drawProperties.getDrawEnd();
		int charHeight = drawProperties.getCharHeight();
		double charWidth = drawProperties.getCharWidth();
		double availableWidth = width - BLINK_LEFT_SPACE_LENGTH - BLINK_RIGHT_SPACE_LENGTH;

		int showCountlength = drawEnd - drawStart;
		double xRange = availableWidth / showCountlength;
		TrackPrimers genomePrimers = genomeMain.getGenomePrimers();
		genomePrimers.setDrawBase(xRange >= charWidth);
		genomePrimers.setXRange(xRange);

		DrawingPropertyPrimerSet primerSet = new DrawingPropertyPrimerSet();

		List<DrawingPropertyPrimers> newPrimers = new ArrayList<>();

		AlignmentGetSequence sequence = alignmentGetSequence;

		List<SequenceI> DNASeq = sequence.returnDNASequence(drawStart - 1, drawEnd - 1);
		SequenceI sequenceI = DNASeq.get(0);
		
		// TODO 要注意 remove gap
		
		
		double peferencePrimerYOffset = 0;
		List<DrawingPropertyPrimers> paintPrimers = paintPrimerSet.getPrimers();
		int size = paintPrimers.size();
		for (int i = 0; i < size; i++) {
			DrawingPropertyPrimers paintPrimer = paintPrimers.get(i);
			int fStart = paintPrimer.getFStart();
			int fEnd = paintPrimer.getFEnd() + 1;

			int rStart = paintPrimer.getRStart();
			int rEnd = paintPrimer.getREnd() + 1;
			int usedTwice = paintPrimer.getUsedTwice();
			if (rEnd < drawStart || fStart > drawEnd) {
				continue;
			}
			if (fStart < drawStart) {
				fStart = drawStart;
			}
			if (fEnd > drawEnd) {
				fEnd = drawEnd;
			}
			List<DrawingPropertyPrimersNucleotide> primersNucleotides = new ArrayList<>();
			double xOffset = 0;
			double yOffset = usedTwice < 0 ? Math.abs(usedTwice) * charHeight : 0;
			double y1 = 0;
			if (fEnd > drawStart) {
				String seqAsString = sequenceI.getSeqAsString(fStart - drawStart, fEnd - drawStart);
				int length = seqAsString.length();
				double drawStartPrimerLocation = BLINK_LEFT_SPACE_LENGTH + (fStart - drawStart) * xRange;
				// 绘制碱基
				for (int j = 0; j < length; j++) {
					String charAt = seqAsString.substring(j, j + 1);
					// 正向引物
					DrawingPropertyPrimersNucleotide forwardPrimersNucleotide = new DrawingPropertyPrimersNucleotide();
					forwardPrimersNucleotide.setBase(charAt);
					double x1 = drawStartPrimerLocation + xOffset;
					y1 = BLINKTOPSPACELENGTH / 2 + yOffset;
					forwardPrimersNucleotide.setX(x1);
					forwardPrimersNucleotide.setY(y1 + primersDist);
					forwardPrimersNucleotide.setW(xRange);
					forwardPrimersNucleotide.setH(charHeight);
					primersNucleotides.add(forwardPrimersNucleotide);
					// 反向引物
					DrawingPropertyPrimersNucleotide reversePrimersNucleotide = new DrawingPropertyPrimersNucleotide();
					y1 = y1 + charHeight;
					if (rStart <= j + drawStart) {
						reversePrimersNucleotide.setBase(PrimerUtil.forwardPrimersToreversePrimers(charAt));
					} else {
						reversePrimersNucleotide.setBase(gap);
					}
					reversePrimersNucleotide.setX(x1);
					reversePrimersNucleotide.setY(y1);
					reversePrimersNucleotide.setW(xRange);
					reversePrimersNucleotide.setH(charHeight);
					primersNucleotides.add(reversePrimersNucleotide);
					xOffset += xRange;

				}
				// paintPrimers.setForward(seqAsString);
			}

			if (rStart < drawStart) {
				rStart = drawStart;
			}
			String seqAsString = sequenceI.getSeqAsString(rStart - drawStart, rEnd - drawStart);
			int tempStart = Math.max(fEnd, drawStart);
			int tempREnd = Math.min(rEnd, drawEnd);
			int drawNum = tempREnd - tempStart;
			double drawStartPrimerLocation = BLINK_LEFT_SPACE_LENGTH + (tempStart - drawStart) * xRange;
			xOffset = 0;
			int tempIndex = rStart - tempStart;// 反向引物绘制开始到包含碱基时gap的数量

			for (int j = 0; j < drawNum; j++) {
				// 正向gap
				DrawingPropertyPrimersNucleotide forwardPrimersNucleotide = new DrawingPropertyPrimersNucleotide();
				forwardPrimersNucleotide.setBase(gap);
				double x1 = drawStartPrimerLocation + xOffset;
				y1 = BLINKTOPSPACELENGTH / 2 + yOffset;
				forwardPrimersNucleotide.setX(x1);
				forwardPrimersNucleotide.setY(y1 + primersDist);
				forwardPrimersNucleotide.setW(xRange);
				forwardPrimersNucleotide.setH(charHeight);
				primersNucleotides.add(forwardPrimersNucleotide);

				y1 = y1 + charHeight;
				if (tempStart + j >= rStart) {
					// 反向引物有碱基
					DrawingPropertyPrimersNucleotide reversePrimersNucleotide = new DrawingPropertyPrimersNucleotide();
					String charAt = PrimerUtil
							.forwardPrimersToreversePrimers(seqAsString.substring(j - tempIndex, j - tempIndex + 1)); // 反向引物转换
					reversePrimersNucleotide.setBase(charAt);
					reversePrimersNucleotide.setX(x1);
					reversePrimersNucleotide.setY(y1);
					reversePrimersNucleotide.setW(xRange);
					reversePrimersNucleotide.setH(charHeight);
					primersNucleotides.add(reversePrimersNucleotide);
				} else {
					// 反向引物gap
					DrawingPropertyPrimersNucleotide reversePrimersNucleotide = new DrawingPropertyPrimersNucleotide();
					reversePrimersNucleotide.setBase(gap);
					reversePrimersNucleotide.setX(x1);
					reversePrimersNucleotide.setY(y1);
					reversePrimersNucleotide.setW(xRange);
					reversePrimersNucleotide.setH(charHeight);
					primersNucleotides.add(reversePrimersNucleotide);
				}
				// paintPrimers.setReverse(seqAsString);
				xOffset += xRange;
			}

			// peferencePrimerYOffset = Math.max(peferencePrimerYOffset, y1);

			paintPrimer.setForwardPrimersNucleotides(primersNucleotides);
			paintPrimer.setReferencePrimers(false);
			paintPrimer.setPrimersColor(PrimerUtil.getColor(i));
			newPrimers.add(paintPrimer);
		}

		peferencePrimerYOffset = showCountIndex * charHeight + showCountIndex / 2 * primersDist;
		// 绘制参考引物
		int startRes = sequenceI.getStartRes();
		int endRes = sequenceI.getEndRes();
		// String seqAsString = sequenceI.getSeqAsString(drawStart - 1, drawEnd - 1);
		// int length = seqAsString.length();
		double drawStartPrimerLocation = BLINK_LEFT_SPACE_LENGTH;
		double xOffset = 0;
		DrawingPropertyPrimers newPaintPrimers = new DrawingPropertyPrimers();

		List<DrawingPropertyPrimersNucleotide> primersNucleotides = new ArrayList<>();
		// 绘制碱基
		for (int i = 0; i < endRes - startRes; i++) {
			String charAt = sequenceI.getCharAt(i) + "";
			// 正向引物
			DrawingPropertyPrimersNucleotide forwardPrimersNucleotide = new DrawingPropertyPrimersNucleotide();
			forwardPrimersNucleotide.setBase(charAt);
			double x1 = drawStartPrimerLocation + xOffset;
			double y1 = peferencePrimerYOffset + charHeight;
			forwardPrimersNucleotide.setX(x1);
			forwardPrimersNucleotide.setY(y1 + primersDist);
			forwardPrimersNucleotide.setW(xRange);
			forwardPrimersNucleotide.setH(charHeight);
			primersNucleotides.add(forwardPrimersNucleotide);
			// 反向引物
			DrawingPropertyPrimersNucleotide reversePrimersNucleotide = new DrawingPropertyPrimersNucleotide();
			reversePrimersNucleotide.setBase(PrimerUtil.forwardPrimersToreversePrimers(charAt));
			reversePrimersNucleotide.setX(x1);
			reversePrimersNucleotide.setY(y1 + charHeight);
			reversePrimersNucleotide.setW(xRange);
			reversePrimersNucleotide.setH(charHeight);
			primersNucleotides.add(reversePrimersNucleotide);
			xOffset += xRange;
		}
		newPaintPrimers.setForwardPrimersNucleotides(primersNucleotides);
		newPaintPrimers.setPrimersColor(referencePrimersBackground);
		newPaintPrimers.setReferencePrimers(true);
		newPrimers.add(newPaintPrimers);
		primerSet.setHeader(this.paintPrimerSet.getHeader());
		primerSet.setPrimers(newPrimers);
		primerSet.setTrackName(PRIMERSETSTRACK);
		primerSet.setTrackNameXLocation(10);
		primerSet.setTrackNameYLocation(BLINKTOPSPACELENGTH);

		return primerSet;
	}

	public void setGenomePrimerSet(DrawingPropertyPrimerSet primerSet) {
		this.paintPrimerSet = primerSet;
		init();
	}
}
