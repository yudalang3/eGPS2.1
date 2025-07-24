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

public class RowAnnotaionPainter {
	

	public void painting(Graphics2D g2d, PaintingLocationParas areaLocation, ParameterModel paraModel, DataModel dataModel) {
		double x = areaLocation.getRowAnnoLeftTopX();
		double y = areaLocation.getRowAnnoLeftTopY();
		double w = areaLocation.getRowAnnoWeidth();
		double h = areaLocation.getRowAnnoHeight();
		
		int[] rowOrderMaping = dataModel.getRowOrderMaping();
		
		int numOfRows = areaLocation.getNumOfRows();
		double cellHeight = areaLocation.getCellHeight();
		int gapSize = paraModel.getGapSize();
		
		int[] vGapLocations = paraModel.getvGapLocations();
		
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		ThreeTuple<List<String>, List<Color[]>, List<byte[]>> rowAnnoParas = annotaionParaObj.getRowAnnoParas();
		
		List<Color[]> rowAnnColors = rowAnnoParas.second;
		List<byte[]> rowAnnotaionIndexes = rowAnnoParas.third;
		List<String> rowAnnotationNames = rowAnnoParas.first;
		
		int numOfAnnotations = rowAnnotaionIndexes.size();
		double allAvaiWidth = w / numOfAnnotations;
		double width = allAvaiWidth - paraModel.annoLocationDivider;
		
		List<AnnotationSelection> annotationSelections = paraModel.getAnnotationSelections();
		Stroke oldStroke = g2d.getStroke();
		
		for (int i = 0; i < numOfAnnotations; i++) {
			double xx = i * allAvaiWidth + x;
			Color[] colors = rowAnnColors.get(i);
			byte[] bs = rowAnnotaionIndexes.get(i);
			FactorHandler vfactorHandler = new FactorHandler(0,vGapLocations,vGapLocations.length);
			
			double yy = 0 ;
			for (int j = 0; j < numOfRows; j++) {
				yy = y + j * cellHeight + FactorCalculator.getIncreasedAddingFactor(vfactorHandler,j)* gapSize;
//				try {
//					g2d.setColor(colors[bs[rowOrderMaping[j]]]);
//				} catch (ArrayIndexOutOfBoundsException e) {
//					System.out.println(j);
//				}
				g2d.setColor(colors[bs[rowOrderMaping[j]]]);
				
				Double r = new Double(xx, yy, width, cellHeight);
				g2d.fill(r);
				
			}
			if (paraModel.isIfPaintColNames()) {
				yy += cellHeight;
				int colNamesRotaionAngle = paraModel.getColNamesRotaionAngle();
				Font oriFont = g2d.getFont();
				g2d.setColor(paraModel.getColNameColor());
				g2d.setFont(paraModel.getColNameFont().deriveFont(Font.BOLD));
				int hgt = g2d.getFontMetrics().getHeight();
				float[] colNameAdjuster = LocationAdjuster.colNameAdjuster(hgt,colNamesRotaionAngle,xx,yy,width,areaLocation);
				
				float startX = colNameAdjuster[0];
				float startY = colNameAdjuster[1];
				
				g2d.rotate(Math.toRadians(colNamesRotaionAngle), startX, startY);
				g2d.drawString(rowAnnotationNames.get(i), startX, startY);
				
				g2d.rotate(Math.toRadians(-colNamesRotaionAngle), startX, startY);
				g2d.setFont(oriFont);
			}
			if (!paraModel.isIfShowBorder()) {
				if(annotationSelections.contains(new IntegerComparator(i,true))) {
					g2d.setStroke(paraModel.getDashedStroke());
					g2d.setColor(Color.blue);
					Double rect = new Double(xx,y,allAvaiWidth,h);
					g2d.draw(rect);
				}
			}
		}
		
		g2d.setStroke(oldStroke);
		
//		Double rect = new Rectangle2D.Double(x,y,w,h);
//		g2d.draw(rect);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void paintingBorder(Graphics2D g2d, PaintingLocationParas areaLocation, ParameterModel paraModel) {
		double x = areaLocation.getRowAnnoLeftTopX();
		double y = areaLocation.getRowAnnoLeftTopY();
		double w = areaLocation.getRowAnnoWeidth();
		double h = areaLocation.getRowAnnoHeight();
		
		int numOfRows = areaLocation.getNumOfRows();
		double cellHeight = areaLocation.getCellHeight();
		int gapSize = paraModel.getGapSize();
		
		int[] vGapLocations = paraModel.getvGapLocations();
		
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		ThreeTuple<List<String>, List<Color[]>, List<byte[]>> rowAnnoParas = annotaionParaObj.getRowAnnoParas();
		
		List<byte[]> rowAnnotaionIndexes = rowAnnoParas.third;
		
		int numOfAnnotations = rowAnnotaionIndexes.size();
		double allAvaiWidth = w / numOfAnnotations;
		double width = allAvaiWidth - paraModel.annoLocationDivider;
		
		List<AnnotationSelection> annotationSelections = paraModel.getAnnotationSelections();
		
		Stroke oldStroke = g2d.getStroke();
		
		
		for (int i = 0; i < numOfAnnotations; i++) {
			FactorHandler vfactorHandler = new FactorHandler(0,vGapLocations,vGapLocations.length);
			
			double xx = i * allAvaiWidth + x;
			g2d.setColor(paraModel.getBorderColor());
			g2d.setStroke(new BasicStroke(1));
			for (int j = 0; j < numOfRows; j++) {
				double yy = j * cellHeight + y + FactorCalculator.getIncreasedAddingFactor(vfactorHandler,j)* gapSize;
				Double r = new Double(xx, yy, width, cellHeight);
				g2d.draw(r);
			}
			
			if(annotationSelections.contains(new IntegerComparator(i,true))) {
				g2d.setStroke(paraModel.getDashedStroke());
				g2d.setColor(Color.blue);
				Double rect = new Double(xx,y,allAvaiWidth,h);
				g2d.draw(rect);
			}
			
		}
		
		
		
		
		g2d.setStroke(oldStroke);
	}

}


class IntegerComparator {
	
	private int inputInteger;
	private boolean ifRow;

	public IntegerComparator(int inputInteger,boolean ifRow) {
		this.inputInteger = inputInteger;
		this.ifRow = ifRow;
	}

	@Override
	public boolean equals(Object obj) {
        if (obj instanceof AnnotationSelection) {
        	AnnotationSelection as =(AnnotationSelection) obj;
        	return as.getTheStorageIndex() == inputInteger && as.isIfRow() == ifRow;
		}
		
		return false;
    }
}
