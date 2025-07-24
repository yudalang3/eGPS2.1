package module.heatmap.eheatmap.model.selection;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import egps2.utils.common.model.datatransfer.ThreeTuple;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;

public class AnnotationSelectionJudger {

	private List<AnnotationSelection> annotationSelections = new ArrayList<AnnotationSelection>();

	public boolean getAndJudgeSelectionForOneClick(PaintingLocationParas areaLocation, DataModel dataModel,
			ParameterModel paraModel, Point2D p) {
		Rectangle2D.Double ret = new Rectangle2D.Double(p.getX()-1,p.getY()-1,2,2);
		return getAndJudgeSelectionForRegion(areaLocation,dataModel,paraModel,ret);

	}

	public boolean getAndJudgeSelectionForRegion(PaintingLocationParas areaLocation, DataModel dataModel,
			ParameterModel paraModel, Rectangle2D.Double rect) {

		annotationSelections.clear();
		if (!paraModel.isIfPaintRowAnnotation() &&!paraModel.isIfPaintColAnnotation()) {
			return false;
		}
		
		double x = areaLocation.getRowAnnoLeftTopX();
		double y = areaLocation.getRowAnnoLeftTopY();
		double w = areaLocation.getRowAnnoWeidth();
		double h = areaLocation.getRowAnnoHeight();
		
		//judge row annotations
		boolean flag = false;
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		ThreeTuple<List<String>, List<Color[]>, List<byte[]>> rowAnnoParas = annotaionParaObj.getRowAnnoParas();
		
		List<byte[]> rowAnnotaionIndexes = rowAnnoParas.third;
		
		int numOfAnnotations = rowAnnotaionIndexes.size();
		double allAvaiWidth = w / numOfAnnotations;
		
		for (int i = 0; i < numOfAnnotations; i++) {
			double xx = x + i * allAvaiWidth;
			if (rect.intersects(xx, y, allAvaiWidth, h)) {
				annotationSelections.add(new AnnotationSelection(i, true));
				flag = true;
			}
		}
		if (flag) {
			return true;
		}
		//judge col annotations
		x = areaLocation.getColAnnoLeftTopX();
		y = areaLocation.getColAnnoLeftTopY();
		w = areaLocation.getColAnnoWeidth();
		h = areaLocation.getColAnnoHeight();
		ThreeTuple<List<String>, List<Color[]>, List<byte[]>> colAnnoParas = annotaionParaObj.getColAnnoParas();
		
		List<byte[]> colAnnotaionIndexes = colAnnoParas.third;
		
		numOfAnnotations = colAnnotaionIndexes.size();
		double allAvaiheight = h / numOfAnnotations;
		
		for (int i = 0; i < numOfAnnotations; i++) {
			double yy = y + i * allAvaiheight;
			if (rect.intersects(x, yy, w, allAvaiheight)) {
				annotationSelections.add(new AnnotationSelection(i, false));
				flag = true;
			}
		}
		if (flag) {
			return true;
		}
		return false;

	}
	
	
	public List<AnnotationSelection> getAnnotationSelections() {
		return annotationSelections;
	}
	
}
