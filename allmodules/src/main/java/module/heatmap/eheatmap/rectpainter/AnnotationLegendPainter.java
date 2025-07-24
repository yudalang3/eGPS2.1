package module.heatmap.eheatmap.rectpainter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import egps2.utils.common.model.datatransfer.FourTuple;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;

public class AnnotationLegendPainter {

	final float squareLength = 25;

	public void painting(Graphics2D g2d, PaintingLocationParas areaLocation, ParameterModel paraModel) {
		g2d.setColor(Color.black);
		double x = areaLocation.getAnnoLegendLeftTopX();
		double y = areaLocation.getAnnoLegendLeftTopY();
		double w = areaLocation.getAnnoLegendWeidth();
		double h = areaLocation.getAnnoLegendHeight();
		
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		int numOfColAnnotaions = annotaionParaObj.getNumOfColAnnotaions();
		int numOfRowAnnotaions = annotaionParaObj.getNumOfRowAnnotaions();
		float globalY = (float) (y + 5);
		float globalColorX = (float) (x + 4);float globalNameX = (float) (x + squareLength + 10);
		// First print col annotation information
		FourTuple<List<String>, List<Color[]>, List<byte[]>, List<String[]>> colAnnoParas = annotaionParaObj.getColAnnoParas();
		for (int i = 0; i < numOfColAnnotaions; i++) {
			g2d.setFont(annotaionParaObj.getFont().deriveFont(Font.BOLD) );
			g2d.setColor(Color.black);
			int fht = g2d.getFontMetrics().getHeight();
			globalY += fht;
			g2d.drawString(colAnnoParas.first.get(i), globalColorX, globalY);
			
			// gap between annotation label name and color block
			globalY += 4;
			Color[] colors = colAnnoParas.second.get(i);
			String[] strings = colAnnoParas.fourth.get(i);
			
			int length = colors.length;
			g2d.setFont(annotaionParaObj.getFont());
			fht = g2d.getFontMetrics().getHeight();
			for (int j = 0; j < length; j++) {
				g2d.setColor(colors[j]);
				float yy = globalY + j * squareLength;
				Double recFloat = new Double(globalColorX, yy, squareLength, squareLength);
				g2d.fill(recFloat);

				float yyy = (float) (yy + 0.5 * squareLength + 0.2 * fht);
				g2d.setColor(Color.black);
				g2d.drawString(strings[j], globalNameX, yyy);
			}
			
			// paint border
			
			for (int j = 0; j < length; j++) {
				
				g2d.setColor(paraModel.getBorderColor());
				Double recFloat = new Double(globalColorX, globalY, squareLength, squareLength);
				g2d.draw(recFloat);
				
				globalY += squareLength;
			}
		}
		
		// Annotation gaps
		globalY += 10;
		
		// Next print row annotation information
		FourTuple<List<String>, List<Color[]>, List<byte[]>, List<String[]>> rowAnnoParas = annotaionParaObj.getRowAnnoParas();
		for (int i = 0; i < numOfRowAnnotaions; i++) {
			g2d.setFont(annotaionParaObj.getFont().deriveFont(Font.BOLD) );
			g2d.setColor(Color.black);
			int fht = g2d.getFontMetrics().getHeight();
			globalY += fht;
			g2d.drawString(rowAnnoParas.first.get(i), globalColorX, globalY);
			
			// gap between annotation label name and color block
			globalY += 4;
			Color[] colors = rowAnnoParas.second.get(i);
			String[] strings = rowAnnoParas.fourth.get(i);
			
			int length = colors.length;
			g2d.setFont(annotaionParaObj.getFont());
			fht = g2d.getFontMetrics().getHeight();
			for (int j = 0; j < length; j++) {
				g2d.setColor(colors[j]);
				float yy = globalY + j * squareLength;
				Double recFloat = new Double(globalColorX, yy, squareLength, squareLength);
				g2d.fill(recFloat);

				float yyy = (float) (yy + 0.5 * squareLength + 0.2 * fht);
				g2d.setColor(Color.black);
				g2d.drawString(strings[j], globalNameX, yyy);
			}
			
			for (int j = 0; j < length; j++) {
				
				g2d.setColor(paraModel.getBorderColor());
				Double recFloat = new Double(globalColorX, globalY, squareLength, squareLength);
				g2d.draw(recFloat);
				
				globalY += squareLength;
			}
		}

//		Double rect = new Rectangle2D.Double(x, y, w, h);
//		g2d.setColor(Color.lightGray);
//		g2d.draw(rect);
	}

}
