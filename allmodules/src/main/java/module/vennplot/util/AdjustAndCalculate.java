package module.vennplot.util;

import java.awt.Cursor;

import egps2.frame.gui.interacive.RectAdjustMent;
import egps2.frame.gui.interacive.RectObj;
import module.vennplot.gui.UpsetRPanelOuterPanel;
import module.vennplot.model.DataModel;
import module.vennplot.model.Element;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SetCalculator;
import module.vennplot.model.SetItem;

public class AdjustAndCalculate {

	private ParameterModel parameterModel;
	private DataModel dataModel;
	private UpsetRPanelOuterPanel upsetRPanel;
	private int maximunIntersectionItemWidth = 40;
	private final double defaultIntersetionRegionBarWidthPer = 0.8;
	
	private final SetCalculator setCalculator = new SetCalculator();

	public PaintingLocations autoConfigPainatingParasPartial1(int width, int height, PaintingLocations pp) {
		//
		IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
		int numOfIntersectionItems = intersectionRegionDataModel.getEles().size();
		double intersectionItemWidth = maximunIntersectionItemWidth;
		if (numOfIntersectionItems != 0) {
			intersectionItemWidth = pp.getIntersectRegionWidth() / numOfIntersectionItems;
		}
		if (intersectionItemWidth > maximunIntersectionItemWidth) {
			intersectionItemWidth = maximunIntersectionItemWidth;
		}

		double d = intersectionItemWidth * defaultIntersetionRegionBarWidthPer;
		parameterModel.setTopBarWidth(d);
		double minD = Math.min(d, pp.getBodyRegionElementHeight());
		parameterModel.setRoundRadius(0.5 * minD);

		pp.setIntersectRegionElementWidth(intersectionItemWidth);

		return pp;

	}

	public PaintingLocations autoConfigAllPainatingParas(int width, int height) {
		PaintingLocations paintingLocations = new PaintingLocations();

		final int horizontalBlink = parameterModel.horizontalBlink;
		final int verticalBlink = parameterModel.verticalBlink;
		double aviWidth = width - 2 * horizontalBlink;
		double aviHeight = height - 2 * verticalBlink;

		double[] vertivalRatios = parameterModel.getVertivalRatios();
		double verticalInterval1 = vertivalRatios[0] * aviHeight;
		double verticalInterval2 = vertivalRatios[1] * aviHeight;

		paintingLocations.setLeftPieTopleftY(verticalBlink);
		paintingLocations.setMiddleAreaTopleftY(verticalBlink);
		paintingLocations.setIntersectRegionTopleftY(verticalBlink);
		paintingLocations.setLeftPieHeight(verticalInterval1);
		paintingLocations.setMiddleAreaHeight(verticalInterval1);
		paintingLocations.setIntersectRegionHeight(verticalInterval1);

		double y2 = verticalInterval1 + verticalBlink;
		paintingLocations.setSingleRegionTopleftY(y2);
		paintingLocations.setNameRegionTopleftY(y2);
		paintingLocations.setBodyTopleftY(y2);
		paintingLocations.setSingleRegionHeight(verticalInterval2);
		paintingLocations.setNameRegionHeight(verticalInterval2);
		paintingLocations.setBodyHeight(verticalInterval2);

		double[] horizontalRations = parameterModel.getHorizontalRations();
		double horizontalInterval1 = horizontalRations[0] * aviWidth;
		double horizontalInterval2 = horizontalRations[1] * aviWidth;
		double horizontalInterval3 = horizontalRations[2] * aviWidth;

		double x2 = horizontalBlink + horizontalInterval1;
		double x3 = horizontalInterval2 + x2;

		paintingLocations.setLeftPieTopleftX(horizontalBlink);
		paintingLocations.setSingleRegionTopleftX(horizontalBlink);
		paintingLocations.setMiddleAreaTopleftX(x2);
		paintingLocations.setNameRegionTopleftX(x2);
		paintingLocations.setIntersectRegionTopleftX(x3);
		paintingLocations.setBodyTopleftX(x3);

		paintingLocations.setLeftPieWidth(horizontalInterval1);
		paintingLocations.setSingleRegionWidth(horizontalInterval1);
		paintingLocations.setMiddleAreaWidth(horizontalInterval2);
		paintingLocations.setNameRegionWidth(horizontalInterval2);
		paintingLocations.setIntersectRegionWidth(horizontalInterval3);
		paintingLocations.setBodyWidth(horizontalInterval3);

		//
		int numOfIntersectionItems = dataModel.getNumOfIntersectionItems();
		double intersectionItemWidth = horizontalInterval3 / numOfIntersectionItems;

		if (intersectionItemWidth > maximunIntersectionItemWidth) {
			intersectionItemWidth = maximunIntersectionItemWidth;
		}
		int numOfSets = dataModel.getNumOfSets();
		double singleRegionItemHeight = verticalInterval2 / numOfSets;
		paintingLocations.setIntersectRegionElementWidth(intersectionItemWidth);
		paintingLocations.setBodyRegionElementHeight(singleRegionItemHeight);

		final int defaultSingleRegionGapSize = 10;
		double d = singleRegionItemHeight - defaultSingleRegionGapSize;
		if (d < parameterModel.getLeftBarWidth()) {
			parameterModel.setLeftBarWidth((int) d);
		}

		d = intersectionItemWidth * defaultIntersetionRegionBarWidthPer;
		if (d < parameterModel.getTopBarWidth()) {
			parameterModel.setTopBarWidth((int) d);
		}
		if (d < 2 * parameterModel.getRoundRadius()) {
			parameterModel.setRoundRadius(Math.min(0.5 * d, singleRegionItemHeight));
		}
		/**
		 * Configure the Painting objs
		 */

		RectObj verticalAdjuctor = new RectObj(horizontalBlink - 2, y2,
				horizontalInterval1 + horizontalInterval2 + horizontalInterval3, 4) {
			@Override
			public void adjustPaintings(double x0, double y0) {

				double yInterval1 = y0 - paintingLocations.getMiddleAreaTopleftY();
				paintingLocations.setLeftPieHeight(yInterval1);
				paintingLocations.setMiddleAreaHeight(yInterval1);
				paintingLocations.setIntersectRegionHeight(yInterval1);

				double oriY = paintingLocations.getSingleRegionTopleftY();
				double yInterval2 = oriY + paintingLocations.getSingleRegionHeight() - y0;
				double singleRegionItemHeight = yInterval2 / numOfSets;
				paintingLocations.setBodyRegionElementHeight(singleRegionItemHeight);

				paintingLocations.setSingleRegionTopleftY(y0);
				paintingLocations.setNameRegionTopleftY(y0);
				paintingLocations.setBodyTopleftY(y0);

				paintingLocations.setSingleRegionHeight(yInterval2);
				paintingLocations.setNameRegionHeight(yInterval2);
				paintingLocations.setBodyHeight(yInterval2);

				this.y = y0;

				upsetRPanel.weakestUpdate();
			}
			
			@Override
			public int getCursorType() {
				return Cursor.N_RESIZE_CURSOR;
			}

		};

		double ttHeight = verticalInterval1 + verticalInterval2;
		RectObj horizontalLeftAdjustor = new RectObj(x2 - 2, verticalBlink, 4, ttHeight) {
			@Override
			public void adjustPaintings(double x0, double y0) {
				double xInterval1 = x0 - horizontalBlink;
				paintingLocations.setSingleRegionWidth(xInterval1);
				paintingLocations.setLeftPieWidth(xInterval1);

				paintingLocations.setMiddleAreaTopleftX(x0);
				paintingLocations.setNameRegionTopleftX(x0);

				double xInterval2 = paintingLocations.getIntersectRegionTopleftX() - x0;
				paintingLocations.setMiddleAreaWidth(xInterval2);
				paintingLocations.setNameRegionWidth(xInterval2);

				this.x = x0;

				upsetRPanel.weakestUpdate();
			}
			
			@Override
			public int getCursorType() {
				return Cursor.E_RESIZE_CURSOR;
			}

		};

		RectAdjustMent[] horizontalAdjustors = new RectAdjustMent[2];
		horizontalAdjustors[0] = horizontalLeftAdjustor;
		RectObj horizontalRightAdjustor = new RectObj(x3 - 2, y2, 4, ttHeight) {
			@Override
			public void adjustPaintings(double x0, double y0) {
				double xInterval2 = x0 - paintingLocations.getMiddleAreaTopleftX();
				double xInterval3 = paintingLocations.getIntersectRegionWidth()
						+ paintingLocations.getIntersectRegionTopleftX() - x0;

				paintingLocations.setMiddleAreaWidth(xInterval2);
				paintingLocations.setNameRegionWidth(xInterval2);

				paintingLocations.setIntersectRegionTopleftX(x0);
				paintingLocations.setBodyTopleftX(x0);
				paintingLocations.setIntersectRegionWidth(xInterval3);
				paintingLocations.setBodyWidth(xInterval3);

				int size = parameterModel.getIntersectionRegionDataModel().getEles().size();
				double intersectionItemWidth = xInterval3 / size;

				if (intersectionItemWidth > maximunIntersectionItemWidth) {
					intersectionItemWidth = maximunIntersectionItemWidth;
				}
				paintingLocations.setIntersectRegionElementWidth(intersectionItemWidth);

				this.x = x0;

				upsetRPanel.weakestUpdate();
			}
			@Override
			public int getCursorType() {
				return Cursor.E_RESIZE_CURSOR;
			}

		};
		horizontalAdjustors[1] = horizontalRightAdjustor;

		paintingLocations.setVerticalAdjustor(verticalAdjuctor);
		paintingLocations.setHorizontalAdjustors(horizontalAdjustors);

		return paintingLocations;
	}

	

	public void setModels(ParameterModel pp, DataModel dd, UpsetRPanelOuterPanel upsetRPanel) {
		this.parameterModel = pp;
		this.dataModel = dd;
		this.upsetRPanel = upsetRPanel;
	}

	public Element[] getSetAfterIntersectAndComplement(byte[] ba, SetItem[] setItems) {
		return setCalculator.getSetAfterIntersectAndComplement(ba, setItems);
	}

}
