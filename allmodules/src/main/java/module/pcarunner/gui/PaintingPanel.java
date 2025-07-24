package module.pcarunner.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import egps2.utils.common.math.pca.Pca;
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
		TwoTuple<double[], double[]> twoDimMatrix = getTwoDimMatrix();

		
		calculator.setParameterModel(parameterModel);
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
	
	
	private TwoTuple<double[], double[]> getTwoDimMatrix() {
		double[][] data = {
	            {2.0,4.0,1.0,4.0,4.0,1.0,5.0,5.0,5.0,2.0,1.0,4.0}, 
	            {2.0,6.0,3.0,1.0,1.0,2.0,6.0,4.0,4.0,4.0,1.0,5.0},
	            {3.0,4.0,4.0,4.0,2.0,3.0,5.0,6.0,3.0,1.0,1.0,1.0},
	            {3.0,6.0,3.0,3.0,1.0,2.0,4.0,6.0,1.0,2.0,4.0,4.0}, 
	            {1.0,6.0,4.0,2.0,2.0,2.0,3.0,4.0,6.0,3.0,4.0,1.0}, 
	            {2.0,5.0,5.0,3.0,1.0,1.0,6.0,6.0,3.0,2.0,6.0,1.0}
	        };

//	    	ThreeTuple<double[][], String[], String[]> readTXTFile = MatrixIO.readTXTFile("E:\\javaCode\\workSpace2\\eGPS_dev_resources\\exampleDataTestForDev\\PCA\\pca.list");
//	    	double[][] data = readTXTFile.first;
//	    	String[] rowNames = readTXTFile.second;
//	    	String[] colNames = readTXTFile.third;
	    	
	    	
	    	
	        DoubleMatrix2D matrix = new DenseDoubleMatrix2D(data);
	        Pca pca = new Pca();
	        DoubleMatrix2D pm = pca.pcaTransform(matrix);

	        // print the first two dimensions of the transformed matrix - they capture most of the variance of the original data
	        
	        DoubleMatrix2D viewPart = pm.viewPart(0, 0, pm.rows(), 2);
	        DoubleMatrix1D v1 = viewPart.viewColumn(0);
	        DoubleMatrix1D v2 = viewPart.viewColumn(1);
	        
	        
	        //return new TwoTuple<double[], double[]>(v1.toArray(), v2.toArray());
	        
	        int numOfPoints = 2000;
			double[] xx = new double[numOfPoints];
	        double[] yy = new double[numOfPoints];
	        for (int i = 0; i < numOfPoints; i++) {
				xx[i] = i;
				yy[i] = Math.abs(1000 - i);
			}
	        
	        return new TwoTuple<double[], double[]>(xx,yy);
	}
	
	

}
