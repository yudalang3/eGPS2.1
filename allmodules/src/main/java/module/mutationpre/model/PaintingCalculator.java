package module.mutationpre.model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import module.mutationpre.gui.DrawProperties;

public class PaintingCalculator {
	ParameterModel parameterModel = new ParameterModel();

	public void calculate(InputDataModel inputDataModel, DrawProperties drawProperties, Dimension paintingPanelSize) {

		drawProperties.paintingPanelSize = paintingPanelSize;
		
		int leftTopMostX = parameterModel.Horizontal_Blank_Length_LEFT;
		int leftTopMostY = parameterModel.Vertical_Blank_Length;

		double availableWidth = paintingPanelSize.width - leftTopMostX - parameterModel.Horizontal_Blank_Length_RIGHT;

		RegionsInputInfor regionsInputInfor = inputDataModel.regionsInputInfor;
		VariantsInputInfor variants = inputDataModel.variants;

		int totalLength = regionsInputInfor.totalLength;
		int startPosition = regionsInputInfor.startPosition;
		double horizontalCoordinateAxisRatio = availableWidth / totalLength;

		/**
		 * 配置 Axis
		 */
		{
			DrawPropertiesAxis axisProperty = drawProperties.getAxisProperty();

			axisProperty.drawWidth = (int) availableWidth;
			axisProperty.point = new Point(leftTopMostX, leftTopMostY);

			int sizeOftickMarks = inputDataModel.axisTickSize;
			double length2paint = totalLength;
			int interval = (int) Math.ceil(length2paint / (sizeOftickMarks - 1));

			List<String> labels = axisProperty.labels;
			List<Point> tickMarkList = axisProperty.tickMarkList;
			labels.clear();
			tickMarkList.clear();

			labels.add(String.valueOf(startPosition));
			tickMarkList.add(axisProperty.point);
			int currentPos = interval;

			while (currentPos < totalLength) {
				labels.add(String.valueOf(currentPos));

				int xx = (int) (horizontalCoordinateAxisRatio * currentPos);
				tickMarkList.add(new Point(leftTopMostX + xx, leftTopMostY));
				currentPos += interval;

			}

			if (currentPos != totalLength) {
				labels.add(String.valueOf(totalLength));
				tickMarkList.add(new Point((int) (leftTopMostX + availableWidth), leftTopMostY));
			}

		}

		/**
		 * 这里计算region和mutation的区域信息
		 */
		int endPositionPlusOne = startPosition + totalLength;
		
		int regionYAxis = leftTopMostY + inputDataModel.axisToRegionVerticalGap;
		{
			DrawPropertiesRegion regionProperty = drawProperties.getRegionProperty();
			// assign region height
			regionProperty.height = inputDataModel.regionHeight;

			List<RegionInfor> listOfRegionInfors = regionsInputInfor.listOfRegionInfors;
			Collection<Integer> paintedPositions = variants.paintedPositions;
			int totalPaintedPositions = paintedPositions.size();
			if (totalPaintedPositions > 1) {
				totalPaintedPositions --;
			}

			double evenInterval = availableWidth / totalPaintedPositions;
			int regionAreaHeight = regionProperty.height;

			List<DrawConnectingLineElement> listOfMutationElement = regionProperty.listOfConnectingLineElements;
			listOfMutationElement.clear();

			Map<Integer, Integer> humanMutationPostion2paintingPostionMap = new LinkedHashMap<>();
			int index = 0;
			for (Integer integer : paintedPositions) {
				int paintingX = (int) (horizontalCoordinateAxisRatio * integer);
				int realPaintingX = leftTopMostX + paintingX;
				humanMutationPostion2paintingPostionMap.put(integer, realPaintingX);

				Point mutationPointInRegionArea = new Point(realPaintingX, regionYAxis + regionAreaHeight);
				DrawConnectingLineElement drawConnectingLineElement = new DrawConnectingLineElement();
				drawConnectingLineElement.locationInRegionArea = mutationPointInRegionArea;

				int mutationPointInMutationAreaX = (int) (index * evenInterval + leftTopMostX);
				Point mutationPointInMutationArea = new Point(mutationPointInMutationAreaX,
						regionYAxis + regionAreaHeight + inputDataModel.connectingLineVerticalSpacing);
				drawConnectingLineElement.locationInMutationArea = mutationPointInMutationArea;
				drawConnectingLineElement.genomicPosition = integer;
				listOfMutationElement.add(drawConnectingLineElement);
				index++;
			}
			List<VariantInfo> variants2 = variants.variants;

			List<DrawRegionElement> regions = regionProperty.regions;
			regions.clear();

			
			for (RegionInfor regionInfor : listOfRegionInfors) {
				DrawRegionElement drawRegionElement = new DrawRegionElement();
				drawRegionElement.color = regionInfor.fillColor;
				drawRegionElement.label = regionInfor.name;
				int downMovedDistance = regionInfor.downMovedDistance;
				drawRegionElement.downMovedDistance = downMovedDistance;
				
				// 注意，这里的坐标系最左端的位置为1
				// 因为坐标轴第一个起始的位置是 1，所以这里要减去1
				int startIndex = regionInfor.startPostion - 1;
				
				int xx = (int) (horizontalCoordinateAxisRatio * startIndex);
				int val = leftTopMostX + xx;
				Point point = new Point(val, regionYAxis);
				// 这里with的宽度相乘的时候也要加一
				int width = (int) (horizontalCoordinateAxisRatio
						* (regionInfor.endPosition - regionInfor.startPostion + 1));
				drawRegionElement.location = Pair.of(point, width);

				regions.add(drawRegionElement);
				
				
				// 如果 这个 downMovedDistance的值大于0，那么 locationInRegionArea 应该要重新处理一下
				if (downMovedDistance > 0) {
					for (DrawConnectingLineElement reg : listOfMutationElement) {
						if (reg.genomicPosition >= regionInfor.startPostion && reg.genomicPosition <= regionInfor.endPosition) {
							reg.locationInRegionArea.y += downMovedDistance;
						}
					}
				}
			}
			
			

			DrawPropertiesMutation mutationProperty = drawProperties.getMutationProperty();
			mutationProperty.leftMostX = leftTopMostX;
			mutationProperty.listofConnectingLineElements = listOfMutationElement;
			mutationProperty.drawWidth = (int) availableWidth;
			List<DrawVariantsProperty> listOfDrawVariants = mutationProperty.listOfDrawVariants;
			listOfDrawVariants.clear();

			Set<Integer> intesectionPositionSet = variants.intesectionPositionSet;
			
			Set<Entry<Integer, Integer>> entrySet = humanMutationPostion2paintingPostionMap.entrySet();
			for (VariantInfo variantInfo : variants2) {
				List<InputMutationInfor> variantsList = variantInfo.variantsList;

				DrawVariantsProperty drawVariantsProperty = new DrawVariantsProperty();
				drawVariantsProperty.variantName = variantInfo.variantName;
				List<DrawMutationElement> listOfMutationElement2 = drawVariantsProperty.listOfMutationElement;

				Iterator<Entry<Integer, Integer>> iteratorOfAllSites = entrySet.iterator();
				Iterator<InputMutationInfor> iteratorOfInputMutation = variantsList.iterator();

				InputMutationInfor inputMutationInfor = null;
				if (iteratorOfInputMutation.hasNext()) {
					inputMutationInfor = iteratorOfInputMutation.next();
				}
				while (iteratorOfAllSites.hasNext()) {
					Entry<Integer, Integer> siteInfo = iteratorOfAllSites.next();
					DrawMutationElement drawMutationElement = new DrawMutationElement();
					
					
					if (inputMutationInfor != null && inputMutationInfor.position == siteInfo.getKey()) {
						drawMutationElement.name = inputMutationInfor.annotationString;
						drawMutationElement.color = inputMutationInfor.color;

						inputMutationInfor = iteratorOfInputMutation.hasNext() ? iteratorOfInputMutation.next() : null;
						
						if (intesectionPositionSet.contains(siteInfo.getKey())) {
							drawMutationElement.highlight = -1;
						}
					} else {
						if(variants.mutationInputType == 1) {
							drawMutationElement.name = ".";
						}else {
							drawMutationElement.name = "";
						}
					}
					listOfMutationElement2.add(drawMutationElement);
				}

				listOfDrawVariants.add(drawVariantsProperty);
			}

		}

		/**
		 * 配置Connecting line 区域
		 */
		DrawPropertiesMutation mutationProperty = drawProperties.getMutationProperty();

		DrawPropertiesConnectingLine connectingLineProperty = drawProperties.getConnectingLineProperty();
		connectingLineProperty.listOfMutationElement = mutationProperty.listofConnectingLineElements;
		connectingLineProperty.allPaintedPositions = variants.paintedPositions;
		connectingLineProperty.listOfConnectingRegionRenders = inputDataModel.listOfConnectingRegionRenders;

		connectingLineProperty.curvature = inputDataModel.connectingLineCurvature;

		connectingLineProperty.connectingLineWidth = inputDataModel.connectingLineWidth;
		connectingLineProperty.connectingLineColor = inputDataModel.connectingLineColor;

		mutationProperty.degree = Math.toRadians(inputDataModel.variants.rotationDegree);
		mutationProperty.positionCoordinateDegree = Math.toRadians(inputDataModel.variants.positionCoordinateRotationDegree);
		mutationProperty.showPositionCoordinate = inputDataModel.variants.showPositionCoordinate;
		mutationProperty.mutationFont = inputDataModel.mutationFont;

		/**
		 * 配置Axis参数
		 */
		DrawPropertiesAxis axisProperty = drawProperties.getAxisProperty();
		axisProperty.titleString = inputDataModel.title;
		axisProperty.titleFont = inputDataModel.titleFont;
		axisProperty.titleColor = inputDataModel.titleColor;
		axisProperty.labelFont = inputDataModel.labelFont;
		axisProperty.axisLineWidth = inputDataModel.axisLineWidth;

		/**
		 * 配置Region参数
		 */

		DrawPropertiesRegion regionProperty = drawProperties.getRegionProperty();
		regionProperty.regionLabelFont = inputDataModel.regionLabelFontt;
		regionProperty.regionLabelColor = inputDataModel.regionLabelColor;
		regionProperty.roundRectangularCurvature = inputDataModel.roundRectangularCurvature;
		regionProperty.hasRegionBorder = inputDataModel.hasRegionBorder;
		regionProperty.regionBorderColor = inputDataModel.regionBorderColor;
		regionProperty.regionBorderLineWidth = inputDataModel.regionBorderLineWidth;
		regionProperty.regionShowMutationVerticalLine = inputDataModel.regionShowMutationVerticalLine;

	}

}
