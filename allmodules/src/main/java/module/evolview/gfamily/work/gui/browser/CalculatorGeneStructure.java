package module.evolview.gfamily.work.gui.browser;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import egps2.utils.common.math.Tools;
import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.Triangle;
import module.evolview.gfamily.work.gui.browser.draw.AxisTicks;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyGeneStructure;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyRegionData;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceElement;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;
import module.evolview.gfamily.work.gui.browser.draw.ScaleAndTickNum;
import module.evolview.gfamily.work.gui.browser.draw.TicksHandler;

public class CalculatorGeneStructure implements LocationCalculator<DrawingPropertyGeneStructure> {

	private int paintColorHeight = 20;

	private int paintColorDistance = 10;

	private int colorDownMoveDist = 5;

	private int tick = 4;// 刻度尺长度

	private int metrics = 20; // 刻度与value的高度


	private GeneStructureInfo geneStrutInfo;

	public CalculatorGeneStructure( GeneStructureInfo virusInfo) {
		this.geneStrutInfo = virusInfo;
	}

	public DrawingPropertyGeneStructure calculatePaintingLocations(GeneDrawingLengthCursor drawProperties,
			int width, int height) {

		int drawStart = drawProperties.getDrawStart();
		int drawEnd = drawProperties.getDrawEnd();

		int dataLength = geneStrutInfo.getGeneLength();

		double startDrawlocationY = BLINKTOPSPACELENGTH;

		DrawingPropertyGeneStructure paintBlock = calculatePaintColorBlock(dataLength, width, startDrawlocationY,
				drawStart, drawEnd);

		DrawingPropertyRegionData paintRegionData = calculatePaintRegionData(width, startDrawlocationY, drawStart,
				drawEnd);

		paintBlock.setRegionData(paintRegionData);

		paintBlock.setTrackName(GENOMESTRUCTURETRACK);
		paintBlock.setGeneName(geneStrutInfo.getGeneName());
		paintBlock.setSequenceElementName(geneStrutInfo.getSequenceElementName());
		
		paintBlock.setTrackNameXLocation(10);
		paintBlock.setTrackNameYLocation(BLINKTOPSPACELENGTH);

		return paintBlock;
	}

	private DrawingPropertyRegionData calculatePaintRegionData(double width, double paintRegionLocationY, int drawStart,
			int drawEnd) {
		DrawingPropertyRegionData paintRegionData = new DrawingPropertyRegionData();

		double availableWidth = width - BLINK_LEFT_SPACE_LENGTH - BLINK_RIGHT_SPACE_LENGTH;

		int dataLength = geneStrutInfo.getGeneLength();

		double perX = availableWidth / dataLength;

		double paintRegionLocationX = perX * drawStart + BLINK_LEFT_SPACE_LENGTH;

		double paintRegionWidth = perX * drawEnd + BLINK_LEFT_SPACE_LENGTH - paintRegionLocationX;

		// double paintRegionHeight = paintBarAndPaintBlockDist + paintColorHeight;
		double paintRegionHeight = paintColorHeight + paintColorDistance + metrics;

		Rectangle2D.Double rectangle = new Rectangle2D.Double(paintRegionLocationX, paintRegionLocationY,
				paintRegionWidth, paintRegionHeight);

		paintRegionData.setRectangle(rectangle);

		double triangleLocationY = paintRegionLocationY;

		Triangle startTriangle = new Triangle(new Point2D.Double(paintRegionLocationX, triangleLocationY));

		Triangle endTriangle = new Triangle(
				new Point2D.Double(paintRegionLocationX + paintRegionWidth, triangleLocationY));

		paintRegionData.setStartTriangle(startTriangle);

		paintRegionData.setEndTriangle(endTriangle);

		return paintRegionData;
	}

	private DrawingPropertyGeneStructure calculatePaintColorBlock(int dataLength, double width,
			double startDrawlocationY, int drawStart, int drawEnd) {

		double availableWidth = width - BLINK_LEFT_SPACE_LENGTH - BLINK_RIGHT_SPACE_LENGTH;

		int showCountlength = drawEnd - drawStart;
		double showXRange = availableWidth / showCountlength;
		double totalXRange = availableWidth / dataLength;

		DrawingPropertyGeneStructure paintStructure = new DrawingPropertyGeneStructure();

		ArrayList<DrawingPropertySequenceElement> firstPaintBlockColors = new ArrayList<>();

		// 计算第一个colorBlock位置
		paintVirusStrainsInfo(firstPaintBlockColors, paintColorHeight, startDrawlocationY, totalXRange, 1, dataLength,
				false);

		paintStructure.setFirstPaintBlockColors(firstPaintBlockColors);

		ArrayList<DrawingPropertySequenceElement> secondPaintBlockColors = new ArrayList<>();

		double secondColorBlockStartDrawlocationY = startDrawlocationY + paintColorHeight + paintColorDistance + metrics
				+ paintColorDistance + 10;
		// 计算第二个colorBlock位置
		// 外框的高度增高了
		paintVirusStrainsInfo(secondPaintBlockColors, paintColorHeight * 2, secondColorBlockStartDrawlocationY,
				showXRange, drawStart, drawEnd, true);

		paintStructure.setSecondPaintBlockColors(secondPaintBlockColors);

		List<Integer[]> calculateBlank = geneStrutInfo.calculateBlank();

		ArrayList<Rectangle2D.Double> paintBlanks = new ArrayList<Rectangle2D.Double>();

		//// 计算colorBlock留白区域位置
		calculatorBlanks(paintBlanks, calculateBlank, paintColorHeight * 2, secondColorBlockStartDrawlocationY,
				showXRange, drawStart, drawEnd);

		paintStructure.setSecondBlankBlock(paintBlanks);

		// 计算colorblock中刻度尺的位置
		double yAxisLocation = startDrawlocationY + paintColorHeight + paintColorDistance;

		paintStructure.setTickLine(
				new Line2D.Double(BLINK_LEFT_SPACE_LENGTH, yAxisLocation, width - BLINK_RIGHT_SPACE_LENGTH, yAxisLocation));

		List<AxisTicks<?>> xAxisLocations = new ArrayList<>();

		calculatorBlockScale(availableWidth, dataLength, yAxisLocation, xAxisLocations);

		paintStructure.setXAxisLocations(xAxisLocations);

		// 计算选中区域
		double perX = availableWidth / dataLength;

		double paintRegionLocationX = perX * drawStart + BLINK_LEFT_SPACE_LENGTH;

		double startSlashY = startDrawlocationY + paintColorHeight + paintColorDistance + metrics;

		paintStructure.setStarSlashtLine(new Line2D.Double(paintRegionLocationX, startSlashY, BLINK_LEFT_SPACE_LENGTH,
				secondColorBlockStartDrawlocationY));

		double paintRegionLocationX1 = perX * drawEnd + BLINK_LEFT_SPACE_LENGTH;

		paintStructure.setEndSlashtLine(new Line2D.Double(paintRegionLocationX1, startSlashY,
				width - BLINK_RIGHT_SPACE_LENGTH, secondColorBlockStartDrawlocationY));

		return paintStructure;
	}

	private void calculatorBlanks(ArrayList<Rectangle2D.Double> paintBlanks, List<Integer[]> calculateBlank,
			int paintColorHeight, double secondColorBlockStartDrawlocationY, double showXRange, int drawStart,
			int drawEnd) {
		Integer[] startPositions = calculateBlank.get(0);
		Integer[] endPositions = calculateBlank.get(1);

		for (int i = 0; i < startPositions.length; i++) {

			int startPos = startPositions[i] + 1;
			int endPos = endPositions[i];

			if (!(endPos < drawStart || startPos > drawEnd)) {
				if (startPos < drawStart && endPos >= drawStart) {
					startPos = drawStart;
				}
				if (endPos > drawStart && endPos > drawEnd) {
					endPos = drawEnd;
				}

				double x1 = (startPos - drawStart) * showXRange + BLINK_LEFT_SPACE_LENGTH;

				double y1 = secondColorBlockStartDrawlocationY;

				double width1 = (endPos - startPos) * showXRange;

				paintBlanks.add(new Rectangle2D.Double(x1, y1, width1, paintColorHeight));
			}

		}

	}

	private void calculatorBlockScale(double availableWidth, int dataLength, double yAxisLocation,
			List<AxisTicks<?>> xAxisLocations) {
		ScaleAndTickNum calculatorScaleAndTikcNumber = calculatorScaleAndTiksNumber(dataLength);

		int scale = (int) calculatorScaleAndTikcNumber.getScale();

		double per = availableWidth / dataLength;

		for (int i = 1; i <= dataLength; i++) {

//			
//			if (drawStart == i && i != 0) {
//				continue;
//			}

			if (i % scale == 0 || i == 1) {

				AxisTicks<Integer> scaleX = new AxisTicks<>();

				double xAxisLocation = per * i + BLINK_LEFT_SPACE_LENGTH;

				scaleX.setLine(new Line2D.Double(xAxisLocation, yAxisLocation, xAxisLocation, yAxisLocation + tick));

				scaleX.setAxisValue(Tools.formatInteger(i));

				scaleX.setValueLocation(new Point2D.Double(xAxisLocation, yAxisLocation + metrics));

				xAxisLocations.add(scaleX);
			}

		}
	}

	private void paintVirusStrainsInfo(ArrayList<DrawingPropertySequenceElement> paintVirusStrainsInfos,
			double paintColorHeight, double startDrawlocationY, double xRange, int drawStart, int drawEnd,
			boolean isDrawName) {

		String[] geneNames = geneStrutInfo.getSequenceElementNames();

		Integer[] startPoses = geneStrutInfo.getStartPoses();

		Integer[] endPoses = geneStrutInfo.getEndPoses();

		Integer[] listOfNucleotideUsedTwice = geneStrutInfo.getListOfNucleotideUsedTwice();

		Color[] colors = geneStrutInfo.getColors();

		for (int i = 0; i < geneNames.length; i++) {
			int startPose = startPoses[i];
			int endPose = endPoses[i] + 1;
			int nucleotideUsedTwice = listOfNucleotideUsedTwice[i];
			if (!(endPose < drawStart || startPose > drawEnd)) {
				paintVirusStrainsInfos
						.add(paintVirusStrainsInfo(startPose, endPose, geneNames[i], colors[i], nucleotideUsedTwice,
								xRange, paintColorHeight, startDrawlocationY, drawStart, drawEnd, isDrawName));
			}
		}
	}

	private DrawingPropertySequenceElement paintVirusStrainsInfo(int startPose, int endPose, String geneName,
			Color color, int nucleotideUsedTwice, double xRange, double paintColorHeight, double startDrawlocationY,
			int drawStart, int drawEnd, boolean isDrawName) {

		if (endPose < drawStart || startPose > drawEnd) {
			return null;
		}
		DrawingPropertySequenceElement paintInfo = new DrawingPropertySequenceElement();
		paintInfo.setStartPose(startPose);
		paintInfo.setEndPose(endPose);

		if (startPose < drawStart) {
			startPose = drawStart;
		}
		if (endPose > drawStart && endPose > drawEnd) {
			endPose = drawEnd;
		}
		double x1 = (startPose - drawStart) * xRange + BLINK_LEFT_SPACE_LENGTH;
		// double x1 = startPose >= drawStart ?(startPose - drawStart) * xRange +
		// BLINKLEFTSPACELENGTH : BLINKLEFTSPACELENGTH - (drawStart - startPose) *
		// xRange;

		double y1 = startDrawlocationY;

		if (nucleotideUsedTwice != -1) {
			y1 += colorDownMoveDist;
		}
		double width1 = (endPose - startPose) * xRange;

		paintInfo.setRectangle(new Rectangle2D.Double(x1, y1, width1, paintColorHeight));

		if (isDrawName) {
			paintInfo.setGeneName(geneName);
		}

		paintInfo.setNameLocationX((float) (width1 / 2 + x1));

		paintInfo.setNameLocationY((float) (paintColorHeight / 2 + y1));

		paintInfo.setColor(color);

		return paintInfo;

	}

	private ScaleAndTickNum calculatorScaleAndTiksNumber(int countLength) {

		ScaleAndTickNum scaleAndTickNum = new ScaleAndTickNum();

		TicksHandler ticksHandler = new TicksHandler();

		int[] scales = ticksHandler.getScalesX();

		int minTickNum = ticksHandler.getMinTickNumX();

		int maxTickNum = ticksHandler.getMaxTickNumX();
		int scale = 0;
		int tickNum = 0;
		for (int i = 0; i < scales.length; i++) {
			scale = scales[i];
			int numOfTicks = countLength / scale;
			if (numOfTicks >= minTickNum && numOfTicks <= maxTickNum) {
				tickNum = numOfTicks;
				break;
			}

			if (numOfTicks < minTickNum) {
				tickNum = numOfTicks;
			}

			if (numOfTicks > maxTickNum) {
				break;
			}

		}
		scaleAndTickNum.setScale(scale);

		scaleAndTickNum.setTickNum(tickNum);
		return scaleAndTickNum;
	}

	public int caculatorDrawLocation(Point point, double width,
			double height) {

		int x = point.x - BLINK_LEFT_SPACE_LENGTH;

		if (x <= 0) {
			return 1;
		}
		double availableWidth = width - BLINK_LEFT_SPACE_LENGTH - BLINK_RIGHT_SPACE_LENGTH;
		int dataLength = geneStrutInfo.getGeneLength();

		if (x >= availableWidth) {

			return dataLength + 1;
		}

		double perX = availableWidth / dataLength;

		int drawLocation = (int) (x / perX);

		return drawLocation + 1;
	}

}
