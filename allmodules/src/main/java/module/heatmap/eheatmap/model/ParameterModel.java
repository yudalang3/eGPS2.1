package module.heatmap.eheatmap.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import egps2.UnifiedAccessPoint;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;
import graphic.engine.GradientColorHolder;
import module.heatmap.eheatmap.enums.CellShape;
import module.heatmap.eheatmap.enums.PaintingLayout;
import module.heatmap.eheatmap.model.selection.AnnotationSelection;
import module.heatmap.eheatmap.model.selection.CellSelection;
import module.heatmap.eheatmap.model.selection.NameSelection;
import module.heatmap.eheatmap.model.selection.TreeNodeSelection;
import module.heatmap.eheatmap.tree.ClusterParameter;
import module.heatmap.eheatmap.tree.GraphcsNode;

public class ParameterModel implements Cloneable {

	public Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	
	public int blankLeft = 40;
	public final int annoLocationDivider = 5;

	int numOfRows;
	int numOfCols;
	
	CellShape paintingCellShapeHandler = CellShape.Rectange;
	PaintingLayout paintingLayout = PaintingLayout.Rectangular;

	boolean ifPaintColTree = false;
	boolean ifPaintColAnnotation = false;
	boolean ifPaintColNames = true;
	boolean ifPaintRowTree = false;
	boolean ifPaintRowAnnotation = false;
	boolean ifPaintRowNames = true;
	boolean ifPaintMapLegend = true;
	boolean ifPaintAnnotationLegend = false;

	boolean shouldCalculateRowTreeLocation = true;
	boolean shouldCalculateColTreeLocation = true;

	ClusterParameter clusterPara = new ClusterParameter();
	GraphcsNode rowTreeRootNode;
	boolean ifRowTreeFormExternalFile = false;
	GraphcsNode colTreeRootNode;
	boolean ifShowAllValues = false;

	/**
	 * byte[] stores the color type
	 */
	AnnotaionParaObj annotaionParaObj = new AnnotaionParaObj();
	/**
	 * The default width to paint row annotation for rectangular layout in map region! 
	 */
	int defaultRowAnnotationCaseWidth = 2;
	/**
	 * The default width to paint col annotation for circular layout in map region! 
	 */
	int defaultColAnnotationCaseWidth = 2;

	// Display
	/**
	 * global data value status: 0 no value;1 original values;2 present value;3
	 * partial ori. 4 partial pre.
	 */
	int dataValueStatus = 0;
	double dataValuePartialFactor = 3;
	int numOfDecimalFormatOfDataaValue = 2;
	Color dataValueColor = Color.BLACK;
	Font dataValueFont = defaultFont;

	boolean ifShowBorder = true;
	Color borderColor = Color.LIGHT_GRAY;

	boolean ifHorizontalLegend = false;
	int mapLegendHeight = 300;
	int mapLegnedWidth = 30;

	int rowNamesRotaionAngle = 0;
	Font rowNameFont = defaultFont;
	Color rowNameColor = Color.black;

	boolean ifShowColNames = true;
	int colNamesRotaionAngle = 90;
	Font colNameFont = defaultFont;
	Color colNameColor = Color.black;

	int[] cellColorSchemePallet;

	// painting region weights
	// 横向的情况
	int rowClusterTreeWeightFinal = 12;
	int rowAnnotationWeightFinal = 12;
	int rowNamesWeightFinal = 10;
	int mapLegendWeightFinal = 10;
	int annoLegendWeightFinal = 12;
	int rowMapWeightFinal = 100 - rowClusterTreeWeightFinal - rowAnnotationWeightFinal - rowNamesWeightFinal
			- mapLegendWeightFinal - annoLegendWeightFinal;

	// 纵向情况
	int colClusterTreeWeightFinal = 12;
	int colAnnotationWeightFinal = 10;
	int colNamesWeightFinal = 10;
	int colMapWeightFinal = 100 - colClusterTreeWeightFinal - colAnnotationWeightFinal - colNamesWeightFinal;

	int gapSize = 8;
	int[] hGapLocations = {};
	int[] vGapLocations = {};

	// Situations for listeners
	// 监听的情况
	boolean cellOneMouseHasClick = false;
	boolean nameOneMouseHasClick = false;
	boolean treeOneMouseHasClick = false;

	GradientColorHolder gColorHolder = new GradientColorHolder();

	boolean hasMouseDragEvent = false;
	/**
	 * For dealing with selections
	 * 
	 */
	// temporary storage
	TreeNodeSelection selectedGraphcsNode;
	List<CellSelection> cellSelections = new ArrayList<CellSelection>();
	List<NameSelection> rowNameSelections = new ArrayList<NameSelection>();
	List<NameSelection> colNameSelections = new ArrayList<NameSelection>();
	List<AnnotationSelection> annotationSelections = new ArrayList<AnnotationSelection>();

	// 行名宽度集合
	List<Integer> rowNameWidths = new ArrayList<Integer>();
	// 列名宽度集合
	List<Integer> colNameWidths = new ArrayList<Integer>();
	/**
	 * Some constants
	 */
	float[] dash1 = { 2f, 0f };
	BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

	CircularParameters circularPara = new CircularParameters();

	private Pair<float[], Color[]> customizedColorScheme;
	
	
	public CircularParameters getCircularParameters() {
		return circularPara;
	}
	
	
	public List<Integer> getColNameWidths() {
		return colNameWidths;
	}

	public void setColNameWidths(List<Integer> colNameWidths) {
		this.colNameWidths = colNameWidths;
	}

	public List<Integer> getRowNameWidths() {
		return rowNameWidths;
	}

	public void setRowNameWidths(List<Integer> rowNameWidths) {
		this.rowNameWidths = rowNameWidths;
	}


	@Override
	public ParameterModel clone() throws CloneNotSupportedException {
		ParameterModel ret = new ParameterModel();
		ret.numOfRows = this.numOfRows;
		ret.numOfCols = this.numOfCols;

		ret.ifPaintColTree = this.ifPaintColTree;
		ret.ifPaintColAnnotation = this.ifPaintColAnnotation;
		ret.ifPaintColNames = this.ifPaintColNames;
		ret.ifPaintRowTree = this.ifPaintRowTree;
		ret.ifPaintRowAnnotation = this.ifPaintRowAnnotation;
		ret.ifPaintRowNames = this.ifPaintRowNames;
		ret.ifPaintMapLegend = this.ifPaintMapLegend;
		ret.ifPaintAnnotationLegend = this.ifPaintAnnotationLegend;

		ret.shouldCalculateRowTreeLocation = this.shouldCalculateRowTreeLocation;
		ret.shouldCalculateColTreeLocation = this.shouldCalculateColTreeLocation;

		ret.clusterPara = this.clusterPara.clone();
		ret.rowTreeRootNode = this.rowTreeRootNode;
		ret.ifRowTreeFormExternalFile = this.ifRowTreeFormExternalFile;
		ret.colTreeRootNode = this.colTreeRootNode;
		ret.ifShowAllValues = this.ifShowAllValues;
		AnnotaionParaObj annotaionParaObjClone = this.annotaionParaObj.clone();

		ret.annotaionParaObj = annotaionParaObjClone;
		ret.defaultRowAnnotationCaseWidth = this.defaultRowAnnotationCaseWidth;
		ret.defaultColAnnotationCaseWidth = this.defaultColAnnotationCaseWidth;

		// Display
		/**
		 * global data value status: 0 no value=this.;1 original values=this.;2 present
		 * value=this.;3 partial ori. 4 partial pre.
		 */
		ret.dataValueStatus = this.dataValueStatus;
		ret.dataValuePartialFactor = this.dataValuePartialFactor;
		ret.numOfDecimalFormatOfDataaValue = this.numOfDecimalFormatOfDataaValue;
		Color dataValueColor = new Color(this.dataValueColor.getRed(), this.dataValueColor.getGreen(),
				this.dataValueColor.getBlue());
		Font dataValueFont = new Font(this.dataValueFont.getFamily(), this.dataValueFont.getStyle(),
				this.dataValueFont.getSize());

		ret.dataValueColor = dataValueColor;
		ret.dataValueFont = dataValueFont;
		ret.ifShowBorder = this.ifShowBorder;
		Color borderColor = new Color(this.borderColor.getRed(), this.borderColor.getGreen(),
				this.borderColor.getBlue());

		ret.borderColor = borderColor;
		ret.ifHorizontalLegend = this.ifHorizontalLegend;
		ret.mapLegendHeight = this.mapLegendHeight;
		ret.mapLegnedWidth = this.mapLegnedWidth;

		ret.rowNamesRotaionAngle = this.rowNamesRotaionAngle;
		Font rowNameFont = new Font(this.rowNameFont.getFamily(), this.rowNameFont.getStyle(),
				this.rowNameFont.getSize());
		Color rowNameColor = new Color(this.rowNameColor.getRed(), this.rowNameColor.getGreen(),
				this.rowNameColor.getBlue());

		ret.rowNameFont = rowNameFont;
		ret.rowNameColor = rowNameColor;

		ret.ifShowColNames = this.ifShowColNames;
		ret.colNamesRotaionAngle = this.colNamesRotaionAngle;
		Font colNameFont = new Font(this.colNameFont.getFamily(), this.colNameFont.getStyle(),
				this.colNameFont.getSize());
		Color colNameColor = new Color(this.colNameColor.getRed(), this.colNameColor.getGreen(),
				this.colNameColor.getBlue());
		ret.colNameFont = colNameFont;
		ret.colNameColor = colNameColor;

		if (this.cellColorSchemePallet != null) {
			ret.cellColorSchemePallet = this.cellColorSchemePallet.clone();
		}

		// painting region weights
		// 横向的情况
		ret.rowClusterTreeWeightFinal = this.rowClusterTreeWeightFinal;
		ret.rowAnnotationWeightFinal = this.rowAnnotationWeightFinal;
		ret.rowNamesWeightFinal = this.rowNamesWeightFinal;
		ret.mapLegendWeightFinal = this.mapLegendWeightFinal;
		ret.annoLegendWeightFinal = this.annoLegendWeightFinal;
		ret.rowMapWeightFinal = this.rowMapWeightFinal;

		// 纵向情况
		ret.colClusterTreeWeightFinal = this.colClusterTreeWeightFinal;
		ret.colAnnotationWeightFinal = this.colAnnotationWeightFinal;
		ret.colNamesWeightFinal = this.colNamesWeightFinal;
		ret.colMapWeightFinal = this.colMapWeightFinal;

		ret.gapSize = this.gapSize;
		ret.hGapLocations = this.hGapLocations.clone();
		ret.vGapLocations = this.vGapLocations.clone();

		// Situations for listeners
		// 监听的情况
		ret.cellOneMouseHasClick = this.cellOneMouseHasClick;
		ret.nameOneMouseHasClick = this.nameOneMouseHasClick;
		ret.treeOneMouseHasClick = this.treeOneMouseHasClick;

		ret.gColorHolder = this.gColorHolder.clone();
		ret.hasMouseDragEvent = this.hasMouseDragEvent;

		// temporary storage
		if (this.selectedGraphcsNode != null) {
			ret.selectedGraphcsNode = new TreeNodeSelection(this.selectedGraphcsNode.getGraphcsNode(),
					this.selectedGraphcsNode.isIfRowSelected());
		}
		if (this.cellSelections != null) {
			ret.cellSelections.addAll(this.cellSelections);

		}
		ret.rowNameSelections.addAll(this.rowNameSelections);
		ret.colNameSelections.addAll(this.colNameSelections);
		ret.annotationSelections.addAll(this.annotationSelections);
		
		
		ret.circularPara = getCircularParameters().clone();
		ret.paintingCellShapeHandler = this.paintingCellShapeHandler;
		return ret;
	}

	public int getColAnnotationWeightFinal() {
		return defaultColAnnotationCaseWidth * getNumOfColAnnotaions();
	}

	public void setColAnnotationWeightFinal(int t) {
		colAnnotationWeightFinal = t;
	}

	public int getRowAnnotationWeightFinal() {
		return defaultRowAnnotationCaseWidth * getNumOfRowAnnotaions();
	}

	public void setRowAnnotationWeightFinal(int t) {
		rowAnnotationWeightFinal = t;
	}

	public DecimalFormat getDecimalFormat() {
		String dfFormat = "##.";
		for (int i = 0; i < numOfDecimalFormatOfDataaValue; i++) {
			dfFormat += "#";
		}
		DecimalFormat decimalFormat = new DecimalFormat(dfFormat);

		return decimalFormat;
	}

	public void setDecimalFormat(int t) {
		numOfDecimalFormatOfDataaValue = t;
	}

	public int getNumOfColAnnotaions() {
		return annotaionParaObj.getNumOfColAnnotaions();
	}

	public int getNumOfRowAnnotaions() {
		return annotaionParaObj.getNumOfRowAnnotaions();
	}

	public boolean hasSelectionElemets() {
		boolean ret = selectedGraphcsNode == null && cellSelections.isEmpty() && colNameSelections.isEmpty()
				&& rowNameSelections.isEmpty();

		return !ret;
	}

	public boolean[] getHorizontalPaintBooleans() {
		boolean[] ret = new boolean[6];
		ret[0] = ifPaintRowTree;
		ret[1] = ifPaintRowAnnotation;
		ret[2] = true;
		ret[3] = ifPaintRowNames;
		ret[4] = ifPaintMapLegend;
		ret[5] = ifPaintAnnotationLegend;
		return ret;
	}

	public boolean[] getVerticalPaintBooleans() {
		boolean[] ret = new boolean[4];
		ret[0] = ifPaintColTree;
		ret[1] = ifPaintColAnnotation;
		ret[2] = true;
		ret[3] = ifPaintColNames;
		return ret;
	}

	// Following are setter and getter!!
	public Font getDefaultFont() {
		return defaultFont;
	}

	public void setDefaultFont(Font defaultFont) {
		this.defaultFont = defaultFont;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

	public int getNumOfCols() {
		return numOfCols;
	}

	public void setNumOfCols(int numOfCols) {
		this.numOfCols = numOfCols;
	}

	public boolean isIfPaintColTree() {
		return ifPaintColTree;
	}

	public void setIfPaintColTree(boolean ifPaintColTree) {
		this.ifPaintColTree = ifPaintColTree;
	}

	public boolean isIfPaintColAnnotation() {
		return ifPaintColAnnotation;
	}

	public void setIfPaintColAnnotation(boolean ifPaintColAnnotation) {
		this.ifPaintColAnnotation = ifPaintColAnnotation;
	}

	public boolean isIfPaintColNames() {
		return ifPaintColNames;
	}

	public void setIfPaintColNames(boolean ifPaintColNames) {
		this.ifPaintColNames = ifPaintColNames;
	}

	public boolean isIfPaintRowTree() {
		return ifPaintRowTree;
	}

	public void setIfPaintRowTree(boolean ifPaintRowTree) {
		this.ifPaintRowTree = ifPaintRowTree;
	}

	public boolean isIfPaintRowAnnotation() {
		return ifPaintRowAnnotation;
	}

	public void setIfPaintRowAnnotation(boolean ifPaintRowAnnotation) {
		this.ifPaintRowAnnotation = ifPaintRowAnnotation;
	}

	public boolean isIfPaintRowNames() {
		return ifPaintRowNames;
	}

	public void setIfPaintRowNames(boolean ifPaintRowNames) {
		this.ifPaintRowNames = ifPaintRowNames;
	}

	public boolean isIfPaintMapLegend() {
		return ifPaintMapLegend;
	}

	public void setIfPaintMapLegend(boolean ifPaintMapLegend) {
		this.ifPaintMapLegend = ifPaintMapLegend;
	}

	public boolean isIfPaintAnnotationLegend() {
		return ifPaintAnnotationLegend;
	}

	public void setIfPaintAnnotationLegend(boolean ifPaintAnnotationLegend) {
		this.ifPaintAnnotationLegend = ifPaintAnnotationLegend;
	}

	public boolean isShouldCalculateRowTreeLocation() {
		return shouldCalculateRowTreeLocation;
	}

	public void setShouldCalculateRowTreeLocation(boolean shouldCalculateRowTreeLocation) {
		this.shouldCalculateRowTreeLocation = shouldCalculateRowTreeLocation;
	}

	public boolean isShouldCalculateColTreeLocation() {
		return shouldCalculateColTreeLocation;
	}

	public void setShouldCalculateColTreeLocation(boolean shouldCalculateColTreeLocation) {
		this.shouldCalculateColTreeLocation = shouldCalculateColTreeLocation;
	}

	public ClusterParameter getClusterPara() {
		return clusterPara;
	}

	public void setClusterPara(ClusterParameter clusterPara) {
		this.clusterPara = clusterPara;
	}

	public GraphcsNode getRowTreeRootNode() {
		return rowTreeRootNode;
	}

	public void setRowTreeRootNode(GraphcsNode rowTreeRootNode) {
		this.rowTreeRootNode = rowTreeRootNode;
	}

	public GraphcsNode getColTreeRootNode() {
		return colTreeRootNode;
	}

	public void setColTreeRootNode(GraphcsNode colTreeRootNode) {
		this.colTreeRootNode = colTreeRootNode;
	}

	public boolean isIfShowAllValues() {
		return ifShowAllValues;
	}

	public void setIfShowAllValues(boolean ifShowAllValues) {
		this.ifShowAllValues = ifShowAllValues;
	}

	public AnnotaionParaObj getAnnotaionParaObj() {
		return annotaionParaObj;
	}

	public void setAnnotaionParaObj(AnnotaionParaObj annotaionParaObj) {
		this.annotaionParaObj = annotaionParaObj;
	}

	public int getRowAnnotationCaseWidth() {
		return defaultRowAnnotationCaseWidth;
	}

	public void setRowAnnotationCaseWidth(int rowAnnotationCaseWidth) {
		this.defaultRowAnnotationCaseWidth = rowAnnotationCaseWidth;
	}

	public int getColAnnotationCaseWidth() {
		return defaultColAnnotationCaseWidth;
	}

	public void setColAnnotationCaseWidth(int colAnnotationCaseWidth) {
		this.defaultColAnnotationCaseWidth = colAnnotationCaseWidth;
	}

	public int getDataValueStatus() {
		return dataValueStatus;
	}

	public void setDataValueStatus(int dataValueStatus) {
		this.dataValueStatus = dataValueStatus;
	}

	public double getDataValuePartialFactor() {
		return dataValuePartialFactor;
	}

	public void setDataValuePartialFactor(double dataValuePartialFactor) {
		this.dataValuePartialFactor = dataValuePartialFactor;
	}

	public int getNumOfDecimalFormatOfDataaValue() {
		return numOfDecimalFormatOfDataaValue;
	}

	public void setNumOfDecimalFormatOfDataaValue(int numOfDecimalFormatOfDataaValue) {
		this.numOfDecimalFormatOfDataaValue = numOfDecimalFormatOfDataaValue;
	}

	public Color getDataValueColor() {
		return dataValueColor;
	}

	public void setDataValueColor(Color dataValueColor) {
		this.dataValueColor = dataValueColor;
	}

	public Font getDataValueFont() {
		return dataValueFont;
	}

	public void setDataValueFont(Font dataValueFont) {
		this.dataValueFont = dataValueFont;
	}

	public boolean isIfShowBorder() {
		return ifShowBorder;
	}

	public void setIfShowBorder(boolean ifShowBorder) {
		this.ifShowBorder = ifShowBorder;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public boolean isIfHorizontalLegend() {
		return ifHorizontalLegend;
	}

	public void setIfHorizontalLegend(boolean ifHorizontalLegend) {
		this.ifHorizontalLegend = ifHorizontalLegend;
	}

	public int getMapLegendHeight() {
		return mapLegendHeight;
	}

	public void setMapLegendHeight(int mapLegendHeight) {
		this.mapLegendHeight = mapLegendHeight;
	}

	public int getMapLegnedWidth() {
		return mapLegnedWidth;
	}

	public void setMapLegnedWidth(int mapLegnedWidth) {
		this.mapLegnedWidth = mapLegnedWidth;
	}

	public int getRowNamesRotaionAngle() {
		return rowNamesRotaionAngle;
	}

	public void setRowNamesRotaionAngle(int rowNamesRotaionAngle) {
		this.rowNamesRotaionAngle = rowNamesRotaionAngle;
	}

	public Font getRowNameFont() {
		return rowNameFont;
	}

	public void setRowNameFont(Font rowNameFont) {
		this.rowNameFont = rowNameFont;
	}

	public Color getRowNameColor() {
		return rowNameColor;
	}

	public void setRowNameColor(Color rowNameColor) {
		this.rowNameColor = rowNameColor;
	}

	public boolean isIfShowColNames() {
		return ifShowColNames;
	}

	public void setIfShowColNames(boolean ifShowColNames) {
		this.ifShowColNames = ifShowColNames;
	}

	public int getColNamesRotaionAngle() {
		return colNamesRotaionAngle;
	}

	public void setColNamesRotaionAngle(int colNamesRotaionAngle) {
		this.colNamesRotaionAngle = colNamesRotaionAngle;
	}

	public Font getColNameFont() {
		return colNameFont;
	}

	public void setColNameFont(Font colNameFont) {
		this.colNameFont = colNameFont;
	}

	public Color getColNameColor() {
		return colNameColor;
	}

	public void setColNameColor(Color colNameColor) {
		this.colNameColor = colNameColor;
	}

	public int[] getCellColorSchemePallet() {
		return cellColorSchemePallet;
	}

	public void setCellColorSchemePallet(int[] cellColorSchemePallet) {
		this.cellColorSchemePallet = cellColorSchemePallet;
	}

	public int getRowClusterTreeWeightFinal() {
		return rowClusterTreeWeightFinal;
	}

	public void setRowClusterTreeWeightFinal(int rowClusterTreeWeightFinal) {
		this.rowClusterTreeWeightFinal = rowClusterTreeWeightFinal;
	}

	public int getRowNamesWeightFinal() {
		return rowNamesWeightFinal;
	}

	public void setRowNamesWeightFinal(int rowNamesWeightFinal) {
		this.rowNamesWeightFinal = rowNamesWeightFinal;
	}

	public int getMapLegendWeightFinal() {
		return mapLegendWeightFinal;
	}

	public void setMapLegendWeightFinal(int mapLegendWeightFinal) {
		this.mapLegendWeightFinal = mapLegendWeightFinal;
	}

	public int getAnnoLegendWeightFinal() {
		return annoLegendWeightFinal;
	}

	public void setAnnoLegendWeightFinal(int annoLegendWeightFinal) {
		this.annoLegendWeightFinal = annoLegendWeightFinal;
	}

	public int getRowMapWeightFinal() {
		return rowMapWeightFinal;
	}

	public void setRowMapWeightFinal(int rowMapWeightFinal) {
		this.rowMapWeightFinal = rowMapWeightFinal;
	}

	public int getColClusterTreeWeightFinal() {
		return colClusterTreeWeightFinal;
	}

	public void setColClusterTreeWeightFinal(int colClusterTreeWeightFinal) {
		this.colClusterTreeWeightFinal = colClusterTreeWeightFinal;
	}

	public int getColNamesWeightFinal() {
		return colNamesWeightFinal;
	}

	public void setColNamesWeightFinal(int colNamesWeightFinal) {
		this.colNamesWeightFinal = colNamesWeightFinal;
	}

	public int getColMapWeightFinal() {
		return colMapWeightFinal;
	}

	public void setColMapWeightFinal(int colMapWeightFinal) {
		this.colMapWeightFinal = colMapWeightFinal;
	}

	public int getGapSize() {
		return gapSize;
	}

	public void setGapSize(int gapSize) {
		this.gapSize = gapSize;
	}

	public int[] gethGapLocations() {
		return hGapLocations;
	}

	public void sethGapLocations(int[] hGapLocations) {
		this.hGapLocations = hGapLocations;
	}

	public int[] getvGapLocations() {
		return vGapLocations;
	}

	public void setvGapLocations(int[] vGapLocations) {
		this.vGapLocations = vGapLocations;
	}

	public boolean isCellOneMouseHasClick() {
		return cellOneMouseHasClick;
	}

	public void setCellOneMouseHasClick(boolean cellOneMouseHasClick) {
		this.cellOneMouseHasClick = cellOneMouseHasClick;
	}

	public boolean isNameOneMouseHasClick() {
		return nameOneMouseHasClick;
	}

	public void setNameOneMouseHasClick(boolean nameOneMouseHasClick) {
		this.nameOneMouseHasClick = nameOneMouseHasClick;
	}

	public boolean isTreeOneMouseHasClick() {
		return treeOneMouseHasClick;
	}

	public void setTreeOneMouseHasClick(boolean treeOneMouseHasClick) {
		this.treeOneMouseHasClick = treeOneMouseHasClick;
	}

	public GradientColorHolder getgColorHolder() {
		return gColorHolder;
	}

	public void setgColorHolder(GradientColorHolder gColorHolder) {
		this.gColorHolder = gColorHolder;
	}

	public boolean isHasMouseDragEvent() {
		return hasMouseDragEvent;
	}

	public void setHasMouseDragEvent(boolean hasMouseDragEvent) {
		this.hasMouseDragEvent = hasMouseDragEvent;
	}

	public TreeNodeSelection getSelectedGraphcsNode() {
		return selectedGraphcsNode;
	}

	public void setSelectedGraphcsNode(GraphcsNode g, boolean ifRow, DataModel dataModel) {
		this.selectedGraphcsNode = new TreeNodeSelection(g, ifRow);
		iterateDealWithTreeNodeSelection(g, true);

		List<GraphcsNode> leaves = EvolNodeUtil.getLeaves(selectedGraphcsNode.getGraphcsNode());
		if (ifRow) {
			String[] rowNames = dataModel.getRowNames();
			for (EvolNode gNode : leaves) {
				GraphcsNode bNode = (GraphcsNode) gNode;
				int orignalOrderInMatrix = bNode.getOrignalOrderInMatrix();
				String string = rowNames[orignalOrderInMatrix];
				NameSelection nameSelection = new NameSelection();
				nameSelection.setName(string);
				nameSelection.setIndex(orignalOrderInMatrix);
				rowNameSelections.add(nameSelection);
			}

		} else {
			String[] colNames = dataModel.getColNames();
			for (EvolNode gNode : leaves) {
				GraphcsNode bNode = (GraphcsNode) gNode;
				int orignalOrderInMatrix = bNode.getOrignalOrderInMatrix();
				String string = colNames[orignalOrderInMatrix];
				NameSelection nameSelection = new NameSelection();
				nameSelection.setName(string);
				nameSelection.setIndex(orignalOrderInMatrix);
				colNameSelections.add(nameSelection);
			}
		}

	}

	public void clearSelectedGraphcsNode() {
		if (selectedGraphcsNode == null) {
			return;
		}
		iterateDealWithTreeNodeSelection(selectedGraphcsNode.getGraphcsNode(), false);
		selectedGraphcsNode = null;
	}

	/**
	 * 
	 * @param graphcsNode
	 * @param b           : Set current Node and its children to status b.
	 */
	private void iterateDealWithTreeNodeSelection(GraphcsNode graphcsNode, boolean b) {
		graphcsNode.setSelectedByUser(b);

		int childCount = graphcsNode.getChildCount();
		for (int i = 0; i < childCount; i++) {
			EvolNode gNode = graphcsNode.getChildAt(i);
			GraphcsNode gg = (GraphcsNode) gNode;
			iterateDealWithTreeNodeSelection(gg, b);
		}
	}

	public List<CellSelection> getCellSelections() {
		return cellSelections;
	}

	public void setCellSelections(List<CellSelection> cellSelections) {
		this.cellSelections = cellSelections;
	}

	public List<NameSelection> getRowNameSelections() {
		return rowNameSelections;
	}

	public void setRowNameSelections(List<NameSelection> rowNameSelections) {
		this.rowNameSelections = rowNameSelections;
	}

	public List<NameSelection> getColNameSelections() {
		return colNameSelections;
	}

	public void setColNameSelections(List<NameSelection> colNameSelections) {
		this.colNameSelections = colNameSelections;
	}

	public List<AnnotationSelection> getAnnotationSelections() {
		return annotationSelections;
	}

	public void setAnnotationSelections(List<AnnotationSelection> annotationSelections) {
		this.annotationSelections = annotationSelections;
	}

	public boolean isIfRowTreeFormExternalFile() {
		return ifRowTreeFormExternalFile;
	}

	public void setIfRowTreeFormExternalFile(boolean ifRowTreeFormExternalFile) {
		this.ifRowTreeFormExternalFile = ifRowTreeFormExternalFile;
	}

	public BasicStroke getDashedStroke() {
		return dashedStroke;
	}

	
	public CellShape getCellShapePainter() {
		return paintingCellShapeHandler;
	}
	public void setCellShapePainter(CellShape cellShape) {
		this.paintingCellShapeHandler = cellShape;
	}


	public PaintingLayout getPaintingLayout() {
		return paintingLayout;
	}


	public void setPaintingLayout(PaintingLayout paintingLayout) {
		this.paintingLayout = paintingLayout;
	}


	public void setCustomizedColorScheme(Pair<float[], Color[]> of) {
		this.customizedColorScheme = of;
	}
	
	public Pair<float[], Color[]> getCustomizedColorScheme() {
		return customizedColorScheme;
	}
	
}
