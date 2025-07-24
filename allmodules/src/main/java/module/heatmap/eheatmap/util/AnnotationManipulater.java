package module.heatmap.eheatmap.util;

import java.awt.Color;
import java.util.List;

import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.selection.NameSelection;

public class AnnotationManipulater {

	public void generateColAnnotaion(String string, ParameterModel paraModel) {
		List<NameSelection> colNameSelections = paraModel.getColNameSelections();

		if (colNameSelections.isEmpty()) {
			return;
		}
		int numOfRows = paraModel.getNumOfRows();
		byte[] ba = new byte[numOfRows];
		for (NameSelection ns : colNameSelections) {
			ba[ns.getIndex()] = 1;
		}

		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		annotaionParaObj.addAColAnnotation(string, ba);

		boolean tt = paraModel.isIfPaintColAnnotation();
		if (!tt) {
			paraModel.setIfPaintColAnnotation(true);
		}
	}
	
	public void generateColAnnotaion(String string,Color[] colors ,String[] caseNames,ParameterModel paraModel) {
		List<NameSelection> colNameSelections = paraModel.getColNameSelections();

		if (colNameSelections.isEmpty()) {
			return;
		}
		int numOfCols = paraModel.getNumOfCols();
		byte[] ba = new byte[numOfCols];
		for (NameSelection ns : colNameSelections) {
			ba[ns.getIndex()] = 1;
		}
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		annotaionParaObj.addAColAnnotation(string, colors,ba,caseNames);

		boolean tt = paraModel.isIfPaintColAnnotation();
		if (!tt) {
			paraModel.setIfPaintColAnnotation(true);
		}
	}
	
	public void generateRowAnnotaion(String string, Color[] colors, String[] caseNames, ParameterModel paraModel) {
		List<NameSelection> rowNameSelections = paraModel.getRowNameSelections();
		
		if (rowNameSelections.isEmpty()) {
			return;
		}
		int numOfRows = paraModel.getNumOfRows();
		byte[] ba = new byte[numOfRows];
		for (NameSelection ns : rowNameSelections) {
			ba[ns.getIndex()] = 1;
		}
		
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		annotaionParaObj.addARowAnnotation(string, colors,ba,caseNames);
		
		boolean ifPaintRowAnnotation = paraModel.isIfPaintRowAnnotation();
		if (!ifPaintRowAnnotation) {
			paraModel.setIfPaintRowAnnotation(true);
		}
	}
	public void generateRowAnnotaion(String string, ParameterModel paraModel) {
		List<NameSelection> rowNameSelections = paraModel.getRowNameSelections();
		
		if (rowNameSelections.isEmpty()) {
			return;
		}
		int numOfRows = paraModel.getNumOfRows();
		byte[] ba = new byte[numOfRows];
		for (NameSelection ns : rowNameSelections) {
			ba[ns.getIndex()] = 1;
		}
		
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		annotaionParaObj.addARowAnnotation(string, ba);
		
		boolean ifPaintRowAnnotation = paraModel.isIfPaintRowAnnotation();
		if (!ifPaintRowAnnotation) {
			paraModel.setIfPaintRowAnnotation(true);
		}
	}
}
