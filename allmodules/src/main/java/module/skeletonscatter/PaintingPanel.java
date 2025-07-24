package module.skeletonscatter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;

import analysis.math.DoubleListUtils;
import graphic.engine.AxisTickCalculator;
import egps2.frame.gui.EGPSMainGuiUtil;
import graphic.engine.guicalculator.BlankArea;
import module.skeletonscatter.io.TSVImportInfoBean;

@SuppressWarnings("serial")
public class PaintingPanel extends JPanel {

	// 重用的 AffineTransform 实例
	private AffineTransform transform = new AffineTransform();

	BlankArea blankArea = new BlankArea(50, 50, 50, 50);

	private final AxisTickCalculator cal = new AxisTickCalculator();

	List<Point> drawPoints;
	private List<Integer> dataTickLocations;
	private List<String> dataLabels;
	private List<Integer> xAxisTickLocations;
	private List<String> xAxisLabels;

	private final float plotWorkspaceRatio = 0.96f;

	public PaintingPanel() {

		setBackground(Color.white);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (drawPoints == null) {
			return;
		}
		int width2 = getWidth();
		int height2 = getHeight();
		int workWidth = blankArea.getWorkWidth(width2);
		int workHeight = blankArea.getWorkHeight(height2);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2f));
		EGPSMainGuiUtil.setupHighQualityRendering(g2d);

		FontMetrics fontMetrics = g2d.getFontMetrics();
		int fontHeight = fontMetrics.getHeight();

		int yAxisLower = height2 - blankArea.getBottom();
		g2d.drawLine(blankArea.getLeft(), blankArea.getTop(), blankArea.getLeft(), yAxisLower);
		g2d.drawLine(blankArea.getLeft(), yAxisLower, width2 - blankArea.getRight(), yAxisLower);

		/*
		  绘制Y坐标
		 */
		Iterator<String> iterator = dataLabels.iterator();
		for (Integer yAxis : dataTickLocations) {
			// Y坐标的换算要特别的注意：
			int yAxis4draw = height2 - blankArea.getBottom() - yAxis;
			g2d.drawLine(blankArea.getLeft() - 5, yAxis4draw, blankArea.getLeft(), yAxis4draw);

			String next = iterator.next();
			int stringWidth = fontMetrics.stringWidth(next);
			g2d.drawString(next, blankArea.getLeft() - 5 - stringWidth - 5, yAxis4draw);

		}
		Iterator<String> iterator2 = xAxisLabels.iterator();
		/*
		  绘制X坐标
		 */
		for (Integer xAxis : xAxisTickLocations) {
			int xAxis4draw = xAxis + blankArea.getLeft();
			String string = iterator2.next();
			// Y坐标的换算要特别的注意：
			g2d.drawLine(xAxis4draw, yAxisLower, xAxis4draw, yAxisLower + 10);
			

			int stringWidth = fontMetrics.stringWidth(string);
			g2d.drawString(string, xAxis4draw - stringWidth / 2 , yAxisLower + 10 + fontHeight);
			
		}

		final int pointSize = 2;

		int leftBlankSpace = (int) (0.5 * (1 - plotWorkspaceRatio) * workWidth);
		int topBlankSpace = (int) (0.5 * (1 - plotWorkspaceRatio) * workHeight);

		for (Point point : drawPoints) {
			// Y坐标的换算要特别的注意：
			int yAxis4draw = height2 - blankArea.getBottom() - point.y - topBlankSpace;

			g2d.fillOval(leftBlankSpace + point.x + blankArea.getLeft() - pointSize, yAxis4draw - pointSize,
					pointSize + pointSize, pointSize + pointSize);
		}

	}

	private void clear() {
		drawPoints = null;
		dataTickLocations = null;
		dataLabels = null;
		xAxisTickLocations = null;
		xAxisLabels = null;
	}

	public void setImportBean(TSVImportInfoBean importBean) {
		clear();
		int width2 = getWidth();
		int height2 = getHeight();
		int workWidth = blankArea.getWorkWidth(width2);
		int workHeight = blankArea.getWorkHeight(height2);

		Pair<List<Double>, List<Double>> loadedData = importBean.loadData();
		List<Double> data = loadedData.getRight();
		List<Double> xAxisData = loadedData.getLeft();
		

		Pair<Double, Double> dataMinMax = DoubleListUtils.getMinMax(data);
		double reciprocalDataMinMax = 1.0 / (dataMinMax.getRight() - dataMinMax.getLeft());
		{

			cal.setMinAndMaxPair(dataMinMax);
			cal.setWorkingSpace(workHeight);
			cal.setWorkSpaceRatio(plotWorkspaceRatio);
			cal.determineAxisTick();

			dataLabels = cal.getTickLabels();
			dataTickLocations = cal.getTickLocations();
			cal.clear();
		}
		Pair<Double, Double> xAxisMinMax = DoubleListUtils.getMinMax(xAxisData);
		double reciprocalXAxisMinMax = 1.0 / (xAxisMinMax.getRight() - xAxisMinMax.getLeft());
		{

			cal.setMinAndMaxPair(xAxisMinMax);
			cal.setWorkingSpace(workWidth);
			cal.setWorkSpaceRatio(plotWorkspaceRatio);
			cal.determineAxisTick();

			xAxisLabels = cal.getTickLabels();
			xAxisTickLocations = cal.getTickLocations();
			cal.clear();
		}

		drawPoints = new LinkedList<>();

		Iterator<Double> dataIterator = data.iterator();
        for (double xAxisValue : xAxisData) {
            double paintXAxis = plotWorkspaceRatio * workWidth * reciprocalXAxisMinMax * (xAxisValue - xAxisMinMax.getLeft());
            Double dataValue = dataIterator.next();
            double paintYAxis = plotWorkspaceRatio * workHeight * reciprocalDataMinMax * (dataValue - dataMinMax.getLeft());
            drawPoints.add(new Point((int) paintXAxis, (int) paintYAxis));
        }

		repaint();
	}
}
