package module.evolview.gfamily.work.gui.browser;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import egps2.utils.common.math.Tools;
import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.browser.draw.AxisTicks;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyLine;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPaintBar;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;
import module.evolview.gfamily.work.gui.browser.draw.ScaleAndTickNum;
import module.evolview.gfamily.work.gui.browser.draw.TicksHandler;

public class CalculatorAlleleFreq implements LocationCalculator<DrawingPropertyPaintBar> {

	private double tickAndScaleValueDist = 10; // tick之间的距离

	private double blinkTopSpaceLength = 10; // 顶部留白

	private int tick = 4;// 刻度尺长度
	private double metrics = 20; // 刻度与value的高度
	private double maxtick;

	private GeneStructureInfo geneStructureInfo;


	public CalculatorAlleleFreq(GeneStructureInfo geneStructureInfo) {
		this.geneStructureInfo = geneStructureInfo;
	}

	public DrawingPropertyPaintBar calculatePaintingLocations(GeneDrawingLengthCursor drawProperties,
			List<Double> listOfScore, int width, int height) {

		DrawingPropertyPaintBar paintBar = new DrawingPropertyPaintBar();
		int drawStart = drawProperties.getDrawStart();
		int drawEnd = drawProperties.getDrawEnd();

		double availableWidth = width - BLINK_LEFT_SPACE_LENGTH - BLINK_RIGHT_SPACE_LENGTH;

		int showCountLength = drawEnd - drawStart;

		double xRange = availableWidth / showCountLength;

		double y = height - metrics - tickAndScaleValueDist;

		paintBar.setxLine(new Line2D.Double(BLINK_LEFT_SPACE_LENGTH, y, width - BLINK_RIGHT_SPACE_LENGTH, y));

		paintBar.setyLine(new Line2D.Double(BLINK_LEFT_SPACE_LENGTH, blinkTopSpaceLength, BLINK_LEFT_SPACE_LENGTH, y));

		TicksHandler ticksHandler = new TicksHandler();

		ScaleAndTickNum calculatorScaleAndTikcNumber = calculatorScaleAndTicksNumber(ticksHandler, showCountLength);

		int scale = (int) calculatorScaleAndTikcNumber.getScale();

		List<AxisTicks<Integer>> xAxisLocations = new ArrayList<>();
		// 绘制刻度尺
		for (int i = drawStart; i < drawEnd; i++) {
//            if (drawStart == i && i != 1) {
//                continue;
//            }

			if (i % scale == 0 || i == 1) {
				AxisTicks<Integer> scaleX = new AxisTicks<>();

				double xAxisLocation = xRange * (i - drawStart) + BLINK_LEFT_SPACE_LENGTH;

				scaleX.setLine(new Line2D.Double(xAxisLocation, y, xAxisLocation, y + tick));

				scaleX.setAxisValue(Tools.formatInteger(i));

				scaleX.setValueLocation(new Point2D.Double(xAxisLocation, height - tickAndScaleValueDist));

				xAxisLocations.add(scaleX);
			}

		}

		paintBar.setxAxisLocations(xAxisLocations);

		List<Double> alleleFreqScores = listOfScore;
		List<Double> charScores = alleleFreqScores.subList(drawStart - 1, drawEnd - 1);

		double maxStatics = Collections.max(charScores);
		// System.out.println("max statics:\t"+maxStatics);

		ScaleAndTickNum calculatorTicksNumberY = calculatorTicksNumberY(ticksHandler, maxStatics);

		List<AxisTicks<Double>> yAxisLocations = new ArrayList<>();

		int tickNum = calculatorTicksNumberY.getTickNum();

		double maxScaleValueY = calculatorTicksNumberY.getScale();

		double xForXAxis = BLINK_LEFT_SPACE_LENGTH;

		AxisTicks<Double> scaleY = new AxisTicks<>();

		scaleY.setLine(new Line2D.Double(xForXAxis - tick, y, xForXAxis, y));

		scaleY.setAxisValue(0.00);

		scaleY.setValueLocation(new Point2D.Double(xForXAxis - tick, y));

		yAxisLocations.add(scaleY);
		for (int i = tickNum; i < tickNum + 1; i++) {
			scaleY = new AxisTicks<>();
			double per = i / (double) tickNum;

			double y1 = y - per * (y - blinkTopSpaceLength);

			scaleY.setLine(new Line2D.Double(xForXAxis - tick, y1, xForXAxis, y1));

			// scaleY.setAxisValue(Tools.formatOneKeepTwoDecimal(per));
			// scaleY.setAxisValue(Tools.formatOneKeepTwoDecimal(maxScaleValueY / tickNum *
			// i));

//            double d = maxScaleValueY / tickNum * i;

//            double vv = (double) Math.round(d * 1000) / 1000;

			
			// 舍弃原来定死的scalesY[],通过最大值取log10确定刻度，yjn
			int index = (int) Math.ceil(Math.log10(maxStatics));
			if (maxStatics > Math.pow(10, index) / 2) {
				scaleY.setAxisValue(Math.pow(10, index));
			} else {
				scaleY.setAxisValue(Math.pow(10, index) / 2);
			}

			scaleY.setValueLocation(new Point2D.Double(xForXAxis - tick, y1));

			yAxisLocations.add(scaleY);
		}

		paintBar.setyAxisLocations(yAxisLocations);

		ArrayList<DrawingPropertyLine> paintLines = new ArrayList<>();

		// 绘制缺省区域
		double maxHeight = y - blinkTopSpaceLength;

		double lineWidth = xRange + 1;
		for (int i = 0; i < charScores.size(); i++) {
			double mutationFrequency = charScores.get(i);
			if (mutationFrequency == 0) {
				continue;
			}
			DrawingPropertyLine paintLine = new DrawingPropertyLine();
			double x1 = BLINK_LEFT_SPACE_LENGTH + i * xRange;
			double mutationFrequencyHeight = maxHeight * mutationFrequency / maxScaleValueY;
			double y1 = y - mutationFrequencyHeight;
			paintLine.setRect(new Rectangle2D.Double(x1 - 0.5, y1, lineWidth, mutationFrequencyHeight));
			int pos = i + drawStart;
			paintLine.setPos(pos);
			paintLine.setColor(geneStructureInfo.getColorByPosition(pos));
			paintLines.add(paintLine);
		}

		paintBar.setNcov2019GenomePaintLine(paintLines);
		int constrainedFromLocation = 1;
		// 还需要注意 getMaxValue是否是 1-based， 如果是才能用
		int constrainedToLocation = drawProperties.getMaxValue() + 1;
		// 每隔4个像素绘制一个斜线
		int pexl = 4;
		if (constrainedFromLocation > drawStart) {
			Rectangle2D.Double constrainedFromRectangle = new Rectangle2D.Double();
			double x1 = BLINK_LEFT_SPACE_LENGTH;
			boolean flag = constrainedFromLocation > drawEnd;
			int length = flag ? drawEnd - drawStart : constrainedFromLocation - drawStart;
			double w = length * xRange;
			constrainedFromRectangle.setRect(x1, blinkTopSpaceLength, w, maxHeight);
			paintBar.setConstrainedFromRectangle(constrainedFromRectangle);
			List<Line2D.Double> lines = new ArrayList<>();

			int totalCount = (int) w / pexl; // 绘制斜线的个数

			if (totalCount == 0) { // 当绘制的线段等于0时,只绘制一条线段
				double x = x1 + w;
				double x2 = x1;
				Line2D.Double line = new Line2D.Double(x, blinkTopSpaceLength, x2, blinkTopSpaceLength + maxHeight);
				lines.add(line);
			} else {
				for (int i = 1; i <= totalCount; i++) {
					double x = x1 + i * pexl;
					double x2 = x1 + (i - 1) * pexl;
					Line2D.Double line = new Line2D.Double(x, blinkTopSpaceLength, x2, blinkTopSpaceLength + maxHeight);
					lines.add(line);
				}
			}
			paintBar.setConstrainedFromLines(lines);
			// 绘制点的高度
//            double drawPointHeight = maxHeight - 4;
//            double v = drawPointHeight / 3;
//            List<Ellipse2D.Double> points = new ArrayList<>();
//            for (int i = 0; i < length; i++) {
//                double radius = xRange > 2 ? 2 : xRange;
//                double x = i * xRange + BLINKLEFTSPACELENGTH;
//                for (int j = 0; j < 3; j++) {
//                    Ellipse2D.Double point = new Ellipse2D.Double(x, blinkTopSpaceLength + 4 + v * j, radius, radius);
//                    points.add(point);
//                }
//            }
//            paintBar.setConstrainedFromPoints(points);

		}
		if (constrainedToLocation < drawEnd) {
			Rectangle2D.Double constrainedToRectangle = new Rectangle2D.Double();
			boolean flag = constrainedToLocation > drawStart;
			double x1 = flag ? BLINK_LEFT_SPACE_LENGTH + (constrainedToLocation - drawStart) * xRange
					: BLINK_LEFT_SPACE_LENGTH;
			int length = flag ? (drawEnd - constrainedToLocation) : (drawEnd - drawStart);
			double w = length * xRange;
			constrainedToRectangle.setRect(x1, blinkTopSpaceLength, w, maxHeight);
			paintBar.setConstrainedToRectangle(constrainedToRectangle);
			List<Line2D.Double> lines = new ArrayList<>();
			int totalCount = (int) w / pexl; // 绘制斜线的个数
			if (totalCount == 0) { // 当绘制的线段等于0时,只绘制一条线段
				double x = x1 + w;
				double x2 = x1;
				Line2D.Double line = new Line2D.Double(x, blinkTopSpaceLength, x2, blinkTopSpaceLength + maxHeight);
				lines.add(line);
			} else {
				for (int i = 1; i <= totalCount; i++) {
					double x = x1 + i * pexl;
					double x2 = x1 + (i - 1) * pexl;
					Line2D.Double line = new Line2D.Double(x, blinkTopSpaceLength, x2, blinkTopSpaceLength + maxHeight);
					lines.add(line);
				}
			}

//            for (int i = 1; i <= length; i++) {
//                double x = x1 + i * xRange;
//                double x2 = x1 + (i - 1) * xRange;
//                Line2D.Double line = new Line2D.Double(x, blinkTopSpaceLength, x2, blinkTopSpaceLength + maxHeight);
//                lines.add(line);
//            }
			paintBar.setConstrainedToLines(lines);
		}

		paintBar.setTrackName(SNPFREQUENCYTRACK);
		paintBar.setTrackNameXLocation(10);
		paintBar.setTrackNameYLocation(BLINKTOPSPACELENGTH);
		paintBar.setMaxHeight(maxHeight);

		paintBar.setBlinkTopSpaceLength(blinkTopSpaceLength);

		return paintBar;

	}

	private ScaleAndTickNum calculatorTicksNumberY(TicksHandler ticksHandler, double maxStatics) {
		double[] scalesY = ticksHandler.getScalesY();
		ScaleAndTickNum scaleAndTickNum = new ScaleAndTickNum();
//        if (maxStatics >= scalesY[0]) {
//            scaleAndTickNum.setTickNum(ticksHandler.getTickNumY()[0]);
//
////            scaleAndTickNum.setScale(scalesY[0]);
//          
//            
//            return scaleAndTickNum;
//        }
//        for (int i = 0; i < scalesY.length; i++) {
//
//            int index = i + 1;
//
//            if (index < scalesY.length && maxStatics >= scalesY[index]) {
//
//                int tickNumY = ticksHandler.getTickNumY()[i];
//
//                scaleAndTickNum.setTickNum(tickNumY);
//
//                scaleAndTickNum.setScale(scalesY[i]);
//
//                return scaleAndTickNum;
//
//            }
//        }

//        scaleAndTickNum.setScale(scalesY[scalesY.length - 1]);

		// 舍弃原来定死的scalesY[],通过最大值取log10确定刻度，yjn
		scaleAndTickNum.setTickNum(1);
		int index = (int) Math.ceil(Math.log10(maxStatics));
		if (maxStatics > Math.pow(10, index - 1) / 2) {
			scaleAndTickNum.setScale(Math.pow(10, index));
		} else {
			scaleAndTickNum.setScale(Math.pow(10, index) / 2);
		}

		return scaleAndTickNum;
	}

	/**
	 * 计算当区域被拖拽时,横向的tick的数量
	 *
	 * @param countLength
	 * @author mhl
	 * @Date Created on:2020-04-23 15:19
	 */
	private ScaleAndTickNum calculatorScaleAndTicksNumber(TicksHandler ticksHandler, int countLength) {

		ScaleAndTickNum scaleAndTickNum = new ScaleAndTickNum();

		int[] scalesX = ticksHandler.getScalesX();

		int minTickNumX = ticksHandler.getMinTickNumX();

		int maxTickNumX = ticksHandler.getMaxTickNumX();
		int scale = 0;
		int tickNum = 0;
		for (int i = 0; i < scalesX.length; i++) {
			scale = scalesX[i];
			int numOfTicks = countLength / scale;
			if (numOfTicks >= minTickNumX && numOfTicks <= maxTickNumX) {
				tickNum = numOfTicks;
				break;
			}

			if (numOfTicks < minTickNumX) {
				tickNum = numOfTicks;
			}

			if (numOfTicks > maxTickNumX) {
				break;
			}

		}
		scaleAndTickNum.setScale(scale);

		scaleAndTickNum.setTickNum(tickNum);
		return scaleAndTickNum;
	}
}
