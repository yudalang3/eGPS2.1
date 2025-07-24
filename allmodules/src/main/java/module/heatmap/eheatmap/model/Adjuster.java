package module.heatmap.eheatmap.model;

import evoltree.struct.EvolNode;
import module.heatmap.eheatmap.HeatmapController;
import module.heatmap.eheatmap.tree.ClusterParameter;
import module.heatmap.eheatmap.tree.GraphcsNode;
import module.heatmap.eheatmap.tree.TreeMethod;
import module.heatmap.eheatmap.tree.TreeUtility;

public class Adjuster {
	
	final int VERT_TREE_INDEX = 0;
	final int VERT_ANNOTATION_INDEX = 1;
	final int VERT_MAP_INDEX = 2;
	final int VERT_NAME_INDEX = 3;
	
	
	final int HORI_TREE_INDEX = 0;
	final int HORI_ANNOTATION_INDEX = 1;
	final int HORI_MAP_INDEX = 2;
	final int HORI_NAME_INDEX = 3;
	final int HORI_MAPlEGEND_INDEX = 4;
	final int HORI_ANNOLEGEND_INDEX = 5;
	
	public PaintingLocationParas autoConfigPainatingParas(double height, double width ,ParameterModel paraModel,DataModel dataModel, HeatmapController hh) {
		
		
		PaintingLocationParas par = new PaintingLocationParas(hh);
		
		par.setHeight(height);
		par.setWidth(width);
		
		double maxY = height - paraModel.blankLeft;
		double maxX = width - paraModel.blankLeft;
		double[] verticalStartPos = calVerticalStartPos(height, paraModel);
		double[] horizontalStartPos = calHorizontalStartPos(width, paraModel);
		
		/**
		 * 纵向来看
		 */
		// draw col tree
		double x1 = horizontalStartPos[HORI_MAP_INDEX];
		double widthForV = horizontalStartPos[HORI_MAP_INDEX+1] - horizontalStartPos[HORI_MAP_INDEX];
		if (paraModel.isIfPaintColTree()) {
			par.setColTreeLeftTopX(x1);
			par.setColTreeLeftTopY(verticalStartPos[VERT_TREE_INDEX]);
			par.setColTreeWeidth(widthForV);
			par.setColTreeHeight(verticalStartPos[VERT_TREE_INDEX+1]-verticalStartPos[VERT_TREE_INDEX]);
			
			doColClustering(paraModel,dataModel);
		}
		// draw col annotation
		if (paraModel.isIfPaintColAnnotation()) {
			par.setColAnnoLeftTopX(x1);
			par.setColAnnoLeftTopY(verticalStartPos[VERT_ANNOTATION_INDEX]);
			par.setColAnnoWeidth(widthForV);
			par.setColAnnoHeight(verticalStartPos[VERT_ANNOTATION_INDEX+1]-verticalStartPos[VERT_ANNOTATION_INDEX]);
		}
		// draw col names
		if (paraModel.isIfPaintColNames()) {
			par.setColNamesLeftTopX(x1);
			par.setColNamesLeftTopY(verticalStartPos[VERT_NAME_INDEX]);
			par.setColNamesWeidth(widthForV);
			par.setColNamesHeight(maxY - verticalStartPos[VERT_NAME_INDEX]);
		}
		
		/**
		 * 横向来看
		 */
		double heightForH = verticalStartPos[VERT_MAP_INDEX + 1] - verticalStartPos[VERT_MAP_INDEX];
		// draw row tree
		if (paraModel.isIfPaintRowTree()) {
			par.setRowTreeLeftTopX(horizontalStartPos[HORI_TREE_INDEX]);
			par.setRowTreeLeftTopY(verticalStartPos[VERT_MAP_INDEX]);
			par.setRowTreeHeight(heightForH);
			par.setRowTreeWeidth(horizontalStartPos[HORI_TREE_INDEX+1] - horizontalStartPos[HORI_TREE_INDEX]);
		
			if (!paraModel.isIfRowTreeFormExternalFile()) {
				// YDL No need to re clustering!
				doRowClustering(paraModel,dataModel);
			}
		}
		// draw row annotation
		if (paraModel.isIfPaintRowAnnotation()) {
			par.setRowAnnoLeftTopX(horizontalStartPos[HORI_ANNOTATION_INDEX]);
			par.setRowAnnoLeftTopY(verticalStartPos[VERT_MAP_INDEX]);
			par.setRowAnnoHeight(heightForH);
			par.setRowAnnoWeidth(horizontalStartPos[HORI_ANNOTATION_INDEX+1] - horizontalStartPos[HORI_ANNOTATION_INDEX]);
		}
		// draw row names
		if (paraModel.isIfPaintRowNames()) {
			par.setRowNamesLeftTopX(horizontalStartPos[HORI_NAME_INDEX]);
			par.setRowNamesLeftTopY(verticalStartPos[VERT_MAP_INDEX]);
			par.setRowNamesHeight(heightForH);
			par.setRowNamesWeidth(horizontalStartPos[HORI_NAME_INDEX+1] - horizontalStartPos[HORI_NAME_INDEX]);
		}
		// draw map legend
		if (paraModel.isIfPaintMapLegend()) {
			par.setMapLegendLeftTopX(horizontalStartPos[HORI_MAPlEGEND_INDEX]);
			par.setMapLegendLeftTopY(verticalStartPos[VERT_ANNOTATION_INDEX]);
			par.setMapLegendHeight(verticalStartPos[VERT_NAME_INDEX] - verticalStartPos[VERT_ANNOTATION_INDEX]);
			par.setMapLegendWeidth(horizontalStartPos[HORI_MAPlEGEND_INDEX+1] - horizontalStartPos[HORI_MAPlEGEND_INDEX]);
		}
		// draw annotation legend
		if (paraModel.isIfPaintAnnotationLegend()) {
			par.setAnnoLegendLeftTopX(horizontalStartPos[HORI_ANNOLEGEND_INDEX]);
			par.setAnnoLegendLeftTopY(verticalStartPos[VERT_ANNOTATION_INDEX]);
			par.setAnnoLegendHeight(verticalStartPos[VERT_NAME_INDEX] - verticalStartPos[VERT_ANNOTATION_INDEX]);
			par.setAnnoLegendWeidth(maxX - horizontalStartPos[HORI_ANNOLEGEND_INDEX]);
		}
		
		// for draw the most important part: cells
		int numOfRows = dataModel.getNumOfRows();
		int numOfCols = dataModel.getNumOfCols();
		paraModel.setNumOfCols(numOfCols);
		paraModel.setNumOfRows(numOfRows);

		double cellWidth = (widthForV - paraModel.getGapSize() * paraModel.gethGapLocations().length ) / numOfCols;
		double cellHeight = (heightForH - paraModel.getGapSize() * paraModel.getvGapLocations().length) / numOfRows;

		par.setCellHeight(cellHeight);
		par.setCellWidth(cellWidth);
		par.setHmapLeftTopX(x1);
		par.setHmapLeftTopY(verticalStartPos[VERT_MAP_INDEX]);
		par.setHmapHeight(heightForH);
		par.setHmapWeidth(widthForV);
		par.setNumOfCols(numOfCols);
		par.setNumOfRows(numOfRows);
		
		par.setVeriticalRects(paraModel.getVerticalPaintBooleans());
		par.setHorizontalRects(paraModel.getHorizontalPaintBooleans());
		
		/**
		 * Set the interactive rectangular for map and annotation legend
		 */
		//par.configMapLegendAndAnnotationLegend(paraModel.isIfPaintMapLegend(),paraModel.isIfPaintAnnotationLegend());
		par.getCenterPoint();
		
		int theNumber = paraModel.getNumOfCols();
		CircularParameters circularParameters = paraModel.getCircularParameters();
		double innerRadius = circularParameters.getInnerRadius();
		double calculateRingThickness = par.calculateRingThickness(theNumber,innerRadius);
		circularParameters.setRingThickness(calculateRingThickness);
		
		return par;
	}
	


	private void doColClustering(ParameterModel paraModel, DataModel dataModel) {
		ClusterParameter clusterPara = paraModel.getClusterPara();
		// calculate distance
		PairwiseDistance pairwiseDistance = clusterPara.getPairwiseDistance();
		double[][] distance = dataModel.getColDistanceMatrix(pairwiseDistance);
		EvolNode tree = clusterPara.getTreeMethod().tree(distance, dataModel.getColNames());
		paraModel.setColTreeRootNode((GraphcsNode) tree);
		
		new TreeUtility().ladderizeUp(tree);
		
	}

	private void doRowClustering(ParameterModel paraModel, DataModel dataModel) {
		ClusterParameter clusterPara = paraModel.getClusterPara();
		// calculate distance
		PairwiseDistance pairwiseDistance = clusterPara.getPairwiseDistance();
		double[][] distance = dataModel.getRowDistanceMatrix(pairwiseDistance);
		
		TreeMethod treeMethod = clusterPara.getTreeMethod();
		EvolNode tree = treeMethod.tree(distance, dataModel.getRowNames());
		paraModel.setRowTreeRootNode((GraphcsNode) tree);
		
		new TreeUtility().ladderizeUp(tree);
	}

	/**
	 * The location allocation for horizontal!
	 * 横向的位置分配
	 */
	double[] calHorizontalStartPos(double aviliabeW, ParameterModel paraModel) {
		final int totalLen = 6;
		
		int[] absoluteWeights = new int[totalLen];
		absoluteWeights[0] = paraModel.isIfPaintRowTree() ? paraModel.getRowClusterTreeWeightFinal() : 0;
		absoluteWeights[1] = paraModel.isIfPaintRowAnnotation() ? paraModel.getRowAnnotationWeightFinal() : 0;
		absoluteWeights[2] = paraModel.getRowMapWeightFinal();
		absoluteWeights[3] = paraModel.isIfPaintRowNames() ? paraModel.getRowNamesWeightFinal() : 0;
		absoluteWeights[4] = paraModel.isIfPaintMapLegend() ? paraModel.getMapLegendWeightFinal() : 0;
		absoluteWeights[5] = paraModel.isIfPaintAnnotationLegend() ? paraModel.getAnnoLegendWeightFinal() : 0;
		
		for (int i = 1; i < totalLen; i++) {
			absoluteWeights[i] += absoluteWeights[i - 1];
		}
		
		double[] ret = new double[totalLen];
		double realLength = aviliabeW - 2.0 * paraModel.blankLeft;
		ret[0] = paraModel.blankLeft;
		for (int i = 1; i < totalLen; i++) {
			ret[i] = absoluteWeights[i - 1] / (double) absoluteWeights[totalLen-1] * realLength + paraModel.blankLeft;
		}

		return ret;
	}

	/**
	 * The location allocation for vertical!
	 * 纵向的位置分配
	 */
	double[] calVerticalStartPos(double avoliableH, ParameterModel paraModel) {
		int[] absoluteWeights = new int[4];
		absoluteWeights[0] = paraModel.isIfPaintColTree() ? paraModel.getColClusterTreeWeightFinal() : 0;
		absoluteWeights[1] = paraModel.isIfPaintColAnnotation() ? paraModel.getColAnnotationWeightFinal() : 0;
		absoluteWeights[2] = paraModel.getColMapWeightFinal();
		absoluteWeights[3] = paraModel.isIfPaintColNames() ? paraModel.getColNamesWeightFinal() : 0;

		for (int i = 1; i < 4; i++) {
			absoluteWeights[i] += absoluteWeights[i - 1];
		}
		double[] ret = new double[4];
		double realLength = avoliableH - 2 * paraModel.blankLeft;
		ret[0] = paraModel.blankLeft;
		for (int i = 1; i < 4; i++) {

			ret[i] = absoluteWeights[i - 1] / (double) absoluteWeights[3] * realLength + paraModel.blankLeft;
		}

		return ret;
	}

}
