package module.pcarunner.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import egps2.utils.common.model.datatransfer.TwoTuple;
import module.pcarunner.model.LocationCalculator;
import module.pcarunner.model.PaintingLocations;
import module.pcarunner.model.ParameterModel;

public class PaintingPanel extends JPanel {

	private static final long serialVersionUID = 2120133754654693370L;
	private ParameterModel parameterModel = new ParameterModel();
	private LocationCalculator calculator = new LocationCalculator();
	
	
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;

		int width = getWidth();
		int height = getHeight();

		calculator.setParameterModel(parameterModel);

		TwoTuple<double[], double[]> twoDimMatrix = null;
		//TODO use math lib to calculate the locations
		PaintingLocations locations = calculator.calculatePaintingLocations(twoDimMatrix, width, height);

		g2d.draw(new Line2D.Double(locations.getOriginCoordinate(), locations.getyMostCoordinate()));
		g2d.draw(new Line2D.Double(locations.getOriginCoordinate(), locations.getxMostCoordinate()));

		// draw xAxis
		double yForXAxis = locations.getyForXAxis();

		TwoTuple<double[], double[]> xAxis = locations.getxAxis();
		float tick = parameterModel.getTick();
		double increasedYForXAxis = yForXAxis + tick;

		int length = xAxis.first.length;
		double[] first = xAxis.first;
		double[] second = xAxis.second;
		for (int i = 0; i < length; i++) {
			g2d.draw(new Line2D.Double(first[i], yForXAxis, first[i], increasedYForXAxis));

			g2d.drawString(second[i] + "", (float) first[i], (float) (increasedYForXAxis + 20));
		}

		// draw yAxis
		TwoTuple<double[], double[]> getyAxis = locations.getyAxis();
		first = getyAxis.first;
		second = getyAxis.second;

		double xForYAxis = locations.getxForYAxis();
		double increasedXForYAxis = xForYAxis + tick;

		length = first.length;

		FontMetrics fontMetrics = g2d.getFontMetrics();
		String ss = second[length - 1] + "";
		double maxDeri = fontMetrics.stringWidth(ss);

		for (int i = 0; i < length; i++) {
			double yPos = first[i];
			g2d.draw(new Line2D.Double(xForYAxis, yPos, increasedXForYAxis, yPos));
			g2d.drawString(second[i] + "", (float) (increasedXForYAxis - maxDeri), (float) yPos);

		}

		// draw points
		g2d.setColor(Color.red);
		TwoTuple<double[], double[]> twoDimArraysForPainting = locations.getTwoDimArraysForPainting();

		double shapeSize = parameterModel.getShapeSize();
		first = twoDimArraysForPainting.first;
		second = twoDimArraysForPainting.second;
		length = first.length;
		for (int i = 0; i < length; i++) {
			g2d.fill(new Ellipse2D.Double(first[i] - shapeSize, second[i] - shapeSize, shapeSize * 2, shapeSize * 2));
		}

	}
	
	


}
