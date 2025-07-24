package module.heatmap.eheatmap.rectpainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import egps2.utils.common.model.datatransfer.ThreeTuple;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.selection.AnnotationSelection;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;

public class ColAnnotationPainter {

	public void painting(Graphics2D g2d, PaintingLocationParas areaLocation, ParameterModel paraModel, DataModel dataModel) {
		
		double x = areaLocation.getColAnnoLeftTopX();
		double y = areaLocation.getColAnnoLeftTopY();
		double w = areaLocation.getColAnnoWeidth();
		double h = areaLocation.getColAnnoHeight();
		
		int[] colOrderMaping = dataModel.getColOrderMaping();
		
		int numOfCols = areaLocation.getNumOfCols();
		double cellWidth = areaLocation.getCellWidth();
		int gapSize = paraModel.getGapSize();
		int[] hGapLocations = paraModel.gethGapLocations();

		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		ThreeTuple<List<String>, List<Color[]>, List<byte[]>> colAnnoParas = annotaionParaObj.getColAnnoParas();
		List<Color[]> colAnnColors = colAnnoParas.second;
		List<byte[]> colAnnotaionIndexes = colAnnoParas.third;
		List<String> colAnnotationNames = colAnnoParas.first;
		
		int numOfAnnotations = colAnnotaionIndexes.size();
		double allAvaiheight = h / numOfAnnotations;
		double height = allAvaiheight - paraModel.annoLocationDivider;
		
		List<AnnotationSelection> annotationSelections = paraModel.getAnnotationSelections();
		
		for (int i = 0; i < numOfAnnotations; i++) {
			double yy = i * allAvaiheight + y;
			FactorHandler hfactorHandler = new FactorHandler(0, hGapLocations, hGapLocations.length);
			byte[] bs = colAnnotaionIndexes.get(i);
			Color[] colors = colAnnColors.get(i);
			double xx = 0;
			for (int j = 0; j < numOfCols; j++) {
				
				xx = x + j * cellWidth +  FactorCalculator.getIncreasedAddingFactor(hfactorHandler,j)* gapSize;
				g2d.setColor(colors[bs[colOrderMaping[j]]]);
				
				Double r = new Double(xx, yy, cellWidth, height);
				g2d.fill(r);
				
			}
			if (paraModel.isIfPaintRowNames()) {
				Font oriFont = g2d.getFont();
				g2d.setFont(paraModel.getRowNameFont().deriveFont(Font.BOLD));
				g2d.setColor(paraModel.getRowNameColor());
				
				g2d.drawString(colAnnotationNames.get(i), (float) (xx+ cellWidth + 6), (float) (yy + height));
				g2d.setFont(oriFont);
			}
			
			
			if (!paraModel.isIfShowBorder()) {
				if(annotationSelections.contains(new IntegerComparator(i,false))) {
					g2d.setStroke(paraModel.getDashedStroke());
					g2d.setColor(Color.blue);
					Double rect = new Double(x,yy,w,allAvaiheight);
					g2d.draw(rect);
				}
			}
			
		}

	}
	
	public void paintingBorder(Graphics2D g2d, PaintingLocationParas areaLocation, ParameterModel paraModel) {
		
		double x = areaLocation.getColAnnoLeftTopX();
		double y = areaLocation.getColAnnoLeftTopY();
		double w = areaLocation.getColAnnoWeidth();
		double h = areaLocation.getColAnnoHeight();
		
		int numOfCols = areaLocation.getNumOfCols();
		double cellWidth = areaLocation.getCellWidth();
		int gapSize = paraModel.getGapSize();
		int[] hGapLocations = paraModel.gethGapLocations();

		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		ThreeTuple<List<String>, List<Color[]>, List<byte[]>> colAnnoParas = annotaionParaObj.getColAnnoParas();
		
		List<byte[]> colAnnotaionIndexes = colAnnoParas.third;
		
		int numOfAnnotations = colAnnotaionIndexes.size();
		double allAvaiheight = h / numOfAnnotations;
		double height = allAvaiheight - paraModel.annoLocationDivider;
		
		List<AnnotationSelection> annotationSelections = paraModel.getAnnotationSelections();
		
		Stroke oldStroke = g2d.getStroke();
		
		for (int i = 0; i < numOfAnnotations; i++) {
			g2d.setColor(paraModel.getBorderColor());
			g2d.setStroke(new BasicStroke(1));
			
			double yy = i * allAvaiheight + y;
			FactorHandler hfactorHandler = new FactorHandler(0, hGapLocations, hGapLocations.length);
			for (int j = 0; j < numOfCols; j++) {
				double xx = j * cellWidth + x + FactorCalculator.getIncreasedAddingFactor(hfactorHandler,j)* gapSize;
				
				Double r = new Double(xx, yy, cellWidth, height);
				g2d.draw(r);
			}
			
			if(annotationSelections.contains(new IntegerComparator(i,false))) {
				g2d.setStroke(paraModel.getDashedStroke());
				g2d.setColor(Color.blue);
				Double rect = new Double(x,yy,w,allAvaiheight);
				g2d.draw(rect);
			}
			
		}
		
		g2d.setStroke(oldStroke);
		

//		Double rect = new Rectangle2D.Double(x, y, w, h);
//		g2d.draw(rect);
	}

}
