package module.evolview.gfamily.work.gui.browser;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.calculator.similarityplot.ComparedSequenceResult;
import module.evolview.gfamily.work.calculator.similarityplot.SimilarityPlotData;
import module.evolview.gfamily.work.gui.browser.draw.AlignmentWithDerivedStatistics;
import module.evolview.gfamily.work.gui.browser.draw.AxisTicks;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPolyLine;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPolylinePlot;
import module.evolview.gfamily.work.gui.browser.draw.ScaleAndTickNum;
import module.evolview.gfamily.work.gui.browser.draw.TicksHandler;

public class CalculatorSeqSimilarity implements LocationCalculator<DrawingPropertyPolylinePlot> {

    private int tickAndScaleValueDist = 10; // tick之间的距离
    private int blinkTopSpaceHeight = 10;
    private final int LEGENDHEIGHT = 20;  //图例高度
    private int blinkTopSpaceLength = blinkTopSpaceHeight + LEGENDHEIGHT; // 顶部留白

    private int tick = 4;// 刻度尺长度`
    private int metrics = 20; // 刻度与value的高度

    public CalculatorSeqSimilarity() {
    	
    }

    public DrawingPropertyPolylinePlot calculatePaintingLocations(GeneDrawingLengthCursor drawProperties,
                                                                      AlignmentWithDerivedStatistics dataModel, int width, int height) {

        DrawingPropertyPolylinePlot polylinePlot = new DrawingPropertyPolylinePlot();
        int drawStart = drawProperties.getDrawStart();
        int drawEnd = drawProperties.getDrawEnd();
        double availableWidth = width - BLINK_LEFT_SPACE_LENGTH - BLINK_RIGHT_SPACE_LENGTH;
        int showCountlength = drawEnd - drawStart;
        double xRange = availableWidth / showCountlength;
        double y = height - metrics - tickAndScaleValueDist;
        polylinePlot.setxLine(new Line2D.Double(BLINK_LEFT_SPACE_LENGTH, y, width - BLINK_RIGHT_SPACE_LENGTH, y));
        polylinePlot.setyLine(new Line2D.Double(BLINK_LEFT_SPACE_LENGTH, blinkTopSpaceLength, BLINK_LEFT_SPACE_LENGTH, y));
        //绘制XAxis
        TicksHandler ticksHandler = new TicksHandler();
        ScaleAndTickNum calculatorScaleAndTikcNumber = calculatorScaleAndTicksNumber(ticksHandler, showCountlength);
        int scale = (int) calculatorScaleAndTikcNumber.getScale();
        List<AxisTicks<Integer>> xAxisLocations = new ArrayList<>();
        //绘制刻度尺
        for (int i = drawStart; i < drawEnd; i++) {
//            if (drawStart == i && i != 1) {
//                continue;
//            }
            if (i % scale == 0 || i == 1) {
                AxisTicks<Integer> scaleX = new AxisTicks<>();
                double xAxisLocation = xRange * (i - drawStart) + BLINK_LEFT_SPACE_LENGTH;
                scaleX.setLine(new Line2D.Double(xAxisLocation, y, xAxisLocation, y + tick));
                scaleX.setAxisValue(i);
                scaleX.setValueLocation(new Point2D.Double(xAxisLocation, height - tickAndScaleValueDist));
                xAxisLocations.add(scaleX);
            }
        }
        polylinePlot.setxAxisLocations(xAxisLocations);

        //绘制yAxis
        SimilarityPlotData simPlotData = dataModel.subGenomeDataModeltPolylinePlot(drawStart - 1, drawEnd - 1);
        List<ComparedSequenceResult> comparedSequenceResults = simPlotData.getComparedSequenceResults();
        List<Double> tempYAxis = new ArrayList<>();
        for (ComparedSequenceResult comparedSequenceResult : comparedSequenceResults) {
            List<Double> doubles = comparedSequenceResult.getyAxis();
            tempYAxis.addAll(doubles);
        }
        double maxStatics = tempYAxis.isEmpty() ? 0 : Collections.max(tempYAxis);
        ScaleAndTickNum calculatorTicksNumberY = calculatorTicksNumberY(ticksHandler, maxStatics);
        List<AxisTicks<Double>> yAxisLocations = new ArrayList<>();
        int tickNum = calculatorTicksNumberY.getTickNum();
        double maxScaleValueY = calculatorTicksNumberY.getScale();
        double xForXAxis = BLINK_LEFT_SPACE_LENGTH;
        AxisTicks<Double> scaleY = new AxisTicks<>();
        scaleY.setLine(new Line2D.Double(xForXAxis - tick, y, xForXAxis, y));
        scaleY.setAxisValue(0.0);
        scaleY.setValueLocation(new Point2D.Double(xForXAxis - tick, y));
        yAxisLocations.add(scaleY);
        for (int i = tickNum; i < tickNum + 1; i++) {
            scaleY = new AxisTicks<>();
            double per = i / (double) tickNum;
            double y1 = y - per * (y - blinkTopSpaceLength);
            scaleY.setLine(new Line2D.Double(xForXAxis - tick, y1, xForXAxis, y1));
            // scaleY.setAxisValue(Tools.formatOneKeepTwoDecimal(per));
            scaleY.setAxisValue(maxScaleValueY / tickNum * i);
            scaleY.setValueLocation(new Point2D.Double(xForXAxis - tick, y1));
            yAxisLocations.add(scaleY);
        }
        polylinePlot.setyAxisLocations(yAxisLocations);

        //绘制折线图中数据
        ArrayList<DrawingPropertyPolyLine> paintLines = new ArrayList<>();
        List<ComparedSequenceResult> comparedSequenceResults1 = simPlotData.getComparedSequenceResults();
        int resultSize = comparedSequenceResults1.size();
        for (int i = 0; i < resultSize; i++) {
            DrawingPropertyPolyLine ncov2019GenomePaintPolyLine = new DrawingPropertyPolyLine();
            ComparedSequenceResult comparedSequenceResult = comparedSequenceResults1.get(i);
            ncov2019GenomePaintPolyLine.setColor(comparedSequenceResult.getColor());
            ncov2019GenomePaintPolyLine.setName(comparedSequenceResult.getName());
            List<Double> doubles = comparedSequenceResult.getyAxis();

            int dataSize = doubles.size();

            List<Line2D.Double> rect = new ArrayList<>();

            List<Integer> xAxisSize = simPlotData.getxAxisIntegers();
            for (int j = 0; j < dataSize - 1; j++) { //dataSize-1 排除最后一个点
                Line2D.Double line2d = new Line2D.Double();
                Integer xAxis = xAxisSize.get(j) - drawStart;
                double x1 = BLINK_LEFT_SPACE_LENGTH + xAxis * xRange;
                Double aDouble1 = doubles.get(j);
                double mutationFrequencyHeight = (y - blinkTopSpaceLength) * aDouble1 / maxScaleValueY;
                double y1 = y - mutationFrequencyHeight;
                int nextIndex = j + 1;
                Integer nextXAxis = xAxisSize.get(nextIndex) - drawStart;
                double x2 = BLINK_LEFT_SPACE_LENGTH + nextXAxis * xRange;
                Double nextDouble1 = doubles.get(nextIndex);
                double nextMutationFrequencyHeight = (y - blinkTopSpaceLength) * nextDouble1 / maxScaleValueY;
                double y2 = y - nextMutationFrequencyHeight;
                line2d.setLine(x1, y1, x2, y2);
                rect.add(line2d);
            }
            ncov2019GenomePaintPolyLine.setRect(rect);
            float startLineX = (float) (xForXAxis + 10);
            float startLineY = blinkTopSpaceLength;
            ncov2019GenomePaintPolyLine.setStartLineLegendX(startLineX);
            ncov2019GenomePaintPolyLine.setStartLineLegendY(startLineY);
            paintLines.add(ncov2019GenomePaintPolyLine);
        }

        polylinePlot.setNcov2019GenomePaintPolyLine(paintLines);
        polylinePlot.setTrackName(COVSEQIDENTITYTRACK);
        polylinePlot.setTrackNameXLocation(10);
        polylinePlot.setTrackNameYLocation(BLINKTOPSPACELENGTH);
        return polylinePlot;
    }

    private ScaleAndTickNum calculatorTicksNumberY(TicksHandler ticksHandler, double maxStatics) {
        double[] scalesY = ticksHandler.getScalesY();
        ScaleAndTickNum scaleAndTickNum = new ScaleAndTickNum();
        if (maxStatics >= scalesY[0]) {
            scaleAndTickNum.setTickNum(ticksHandler.getTickNumY()[0]);

            scaleAndTickNum.setScale(scalesY[0]);

            return scaleAndTickNum;
        }
        for (int i = 0; i < scalesY.length; i++) {

            int index = i + 1;

            if (index < scalesY.length && maxStatics >= scalesY[index]) {

                int tickNumY = ticksHandler.getTickNumY()[i];

                scaleAndTickNum.setTickNum(tickNumY);

                scaleAndTickNum.setScale(scalesY[i]);

                return scaleAndTickNum;

            }
        }
        scaleAndTickNum.setTickNum(1);

        scaleAndTickNum.setScale(scalesY[scalesY.length - 1]);

        return scaleAndTickNum;
    }

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
