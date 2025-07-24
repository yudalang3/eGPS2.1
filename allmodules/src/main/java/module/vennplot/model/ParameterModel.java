package module.vennplot.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import egps2.UnifiedAccessPoint;
import module.vennplot.painter.BodyIntersectionSelection;
import module.vennplot.painter.NameSelection;
import module.vennplot.painter.SingleRegionSelection;

public class ParameterModel {

	private final SetCalculator setCalculator = new SetCalculator();
	
	public int horizontalBlink = 30;
	public int verticalBlink = 90;

	public int leftPieLength = 10;
	public int rightPieLength = 10;

	public Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	private double[] vertivalRatios = { 0.5, 0.5 };
	private double[] horizontalRations = { 0.25, 0.1, 0.65 };

	double roundRadius = 10;
	int linkingBarThick = 2;
	int axisTipLength = 8;
	double topBarWidth = 20;
	int leftBarWidth = 30;
	
	Font titleFont;
	Font nameFont;
	private Font numberFont;

	IntersectionRegionDataModel intersectionRegionDataModel;
	SingleRegionDataModel singleRegionDataModel;

	boolean ifSortIntersectionRegion = true;
	boolean showLegend = true;
	boolean showLeftPie = false;
	boolean showIntersectionValues = true;
	boolean containsNotTakeintoCon = false;

	/**
	 * Selections
	 */
	List<SingleRegionSelection> singleRegionSelections = new ArrayList<>();
	List<NameSelection> nameSelections = new ArrayList<>();
	List<BodyIntersectionSelection> bodyIntersectionSelections = new ArrayList<>();

	/**
	 * Some constants
	 */
	final float[] dash1 = { 10.0f };
	final BasicStroke dashedStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
			0.0f);
	private List<String> nameOfInputSetItemForSearch;


	public boolean isContainsNotTakeintoCon() {
		return containsNotTakeintoCon;
	}

	public void setContainsNotTakeintoCon(boolean containsNotTakeintoCon) {
		this.containsNotTakeintoCon = containsNotTakeintoCon;
	}

	public boolean isShowLeftPie() {
		return showLeftPie;
	}

	public void setShowLeftPie(boolean showLeftPie) {
		this.showLeftPie = showLeftPie;
	}

	public boolean isShowIntersectionValues() {
		return showIntersectionValues;
	}

	public void setShowIntersectionValues(boolean showIntersectionValues) {
		this.showIntersectionValues = showIntersectionValues;
	}

	public boolean isShowLegend() {
		return showLegend;
	}

	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	public List<SingleRegionSelection> getSingleRegionSelections() {
		return singleRegionSelections;
	}

	public List<NameSelection> getNameSelections() {
		return nameSelections;
	}

	public List<BodyIntersectionSelection> getBodyIntersectionSelections() {
		return bodyIntersectionSelections;
	}

	public void clearAllSelections() {
		singleRegionSelections.clear();
		nameSelections.clear();
		bodyIntersectionSelections.clear();

		if (nameOfInputSetItemForSearch != null) {
			nameOfInputSetItemForSearch.clear();
		}

	}

	public boolean getIfConatainsSelection() {
		return !singleRegionSelections.isEmpty() || !nameSelections.isEmpty() || !bodyIntersectionSelections.isEmpty();
	}

	public Color getDefaultElementColor() {
		return Color.black;
	}

	public double[] getVertivalRatios() {
		return vertivalRatios;
	}

	public void setVertivalRatios(double[] vertivalRatios) {
		this.vertivalRatios = vertivalRatios;
	}

	public double[] getHorizontalRations() {
		return horizontalRations;
	}

	public void setHorizontalRations(double[] horizontalRations) {
		this.horizontalRations = horizontalRations;
	}

	public double getRoundRadius() {
		return roundRadius;
	}

	public void setRoundRadius(double roundRadius) {
		this.roundRadius = roundRadius;
	}

	public int getLinkingBarThick() {
		return linkingBarThick;
	}

	public void setLinkingBarThick(int linkingBarThick) {
		this.linkingBarThick = linkingBarThick;
	}

	public IntersectionRegionDataModel getIntersectionRegionDataModel() {
		return intersectionRegionDataModel;
	}

	public void setIntersectionRegionDataModel(IntersectionRegionDataModel intersectionRegionDataModel) {
		this.intersectionRegionDataModel = intersectionRegionDataModel;
	}

	public SingleRegionDataModel getSingleRegionDataModel() {
		return singleRegionDataModel;
	}

	public void setSingleRegionDataModel(SingleRegionDataModel singleRegionDataModel) {
		this.singleRegionDataModel = singleRegionDataModel;
	}

	public Stroke getDashedStroke() {
		return dashedStroke;
	}

	public int getAxisTipLength() {
		return axisTipLength;
	}

	public void setAxisTipLength(int axisTipLength) {
		this.axisTipLength = axisTipLength;
	}

	public boolean isIfSortIntersectionRegion() {
		return ifSortIntersectionRegion;
	}

	public void setIfSortIntersectionRegion(boolean ifSortIntersectionRegion) {
		this.ifSortIntersectionRegion = ifSortIntersectionRegion;
	}

	public double getTopBarWidth() {
		return topBarWidth;
	}

	public void setTopBarWidth(double topBarWidth) {
		this.topBarWidth = topBarWidth;
	}

	public int getLeftBarWidth() {
		return leftBarWidth;
	}

	public void setLeftBarWidth(int leftBarWidth) {
		this.leftBarWidth = leftBarWidth;
	}

	public Font getTitleFont() {
		return titleFont;
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	public Font getNameFont() {
		return nameFont;
	}

	public void setNameFont(Font nameFont) {
		this.nameFont = nameFont;
	}

	public void setNameOfInputSetItemForSearch(List<String> namesOfSetItemContains) {
		this.nameOfInputSetItemForSearch = namesOfSetItemContains;
	}

	public List<String> getNameOfInputSetItemForSearch() {
		return nameOfInputSetItemForSearch;
	}

	
	public void initializeIntersectionParamters(SetItem[] setItems) {
		IntersectionRegionDataModel intersectionRegionDataModel = setCalculator.calculate(setItems);
		
		
		setIntersectionRegionDataModel(intersectionRegionDataModel);
	}
	
	public void initializeSingleRegionParameters(SetItem[] setItems) {
		SingleRegionDataModel singleRegionDataModel = new SingleRegionDataModel();
		
		int numOfSets = setItems.length;

		// get highest value and expend to more
		int highest = 0;
		for (int i = 0; i < numOfSets; i++) {
			String[] setLists = setItems[i].getSetLists();
			int length = setLists.length;
			if (length > highest) {
				highest = length;
			}
		}
		singleRegionDataModel.setHigestNumber(highest);
		singleRegionDataModel.setExpendHigestNumber((int) ( 1.2 * highest));

		List<SingleRegionElement> eles = singleRegionDataModel.getEles();
		for (int i = 0; i < numOfSets; i++) {
			String[] setLists = setItems[i].getSetLists();
			SingleRegionElement singleRegionElement = new SingleRegionElement();
			singleRegionElement.setOriginalIndex(i);

			int count = setLists.length;
			singleRegionElement.setCount(count);
			singleRegionElement.setRatio_this2higest(count / (float) singleRegionDataModel.getExpendHigestNumber());

			singleRegionElement.setFillColor(Color.black);

			singleRegionElement.setSetName(setItems[i].getName());

			eles.add(singleRegionElement);
		}

		Collections.sort(eles);
		
		int[] indexes = new int[numOfSets];
		for (int i = 0; i < numOfSets; i++) {
			indexes[i] = eles.get(i).getOriginalIndex() ;
		}
		
		singleRegionDataModel.setIndexes(indexes);
		
		
		setSingleRegionDataModel(singleRegionDataModel);
	}

	public void setNumberFont(Font font) {
		this.numberFont = font;
		
	}
	
	public Font getNumberFont() {
		return numberFont;
	}
}
