package module.mutationpre.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import module.mutationpre.model.DrawConnectingLineElement;
import module.mutationpre.model.DrawMutationElement;
import module.mutationpre.model.DrawPropertiesAxis;
import module.mutationpre.model.DrawPropertiesConnectingLine;
import module.mutationpre.model.DrawPropertiesMutation;
import module.mutationpre.model.DrawPropertiesRegion;
import module.mutationpre.model.DrawRegionElement;
import module.mutationpre.model.DrawVariantsProperty;


public class DrawProperties {

	DrawPropertiesRegion regionProperty = new DrawPropertiesRegion();

	DrawPropertiesMutation mutationProperty = new DrawPropertiesMutation();

	DrawPropertiesAxis axisProperty = new DrawPropertiesAxis();
	
	DrawPropertiesConnectingLine connectingLineProperty = new DrawPropertiesConnectingLine();
	
	final List<DrawMutationElement> selectedMutationElement = new ArrayList<>();

	public Dimension paintingPanelSize;

	public DrawProperties() {

	}

	public static DrawProperties getExample() {
		DrawProperties ret = new DrawProperties();
		{

			ret.axisProperty.tickMarkList.add(new Point(200, 100));
			ret.axisProperty.tickMarkList.add(new Point(300, 100));
			ret.axisProperty.tickMarkList.add(new Point(400, 100));
			ret.axisProperty.tickMarkList.add(new Point(500, 100));

			ret.axisProperty.labels.add("100");
			ret.axisProperty.labels.add("200");
			ret.axisProperty.labels.add("300");
			ret.axisProperty.labels.add("400");
		}

		List<DrawConnectingLineElement> listOfConnectingLineElements = ret.regionProperty.listOfConnectingLineElements;
		List<DrawRegionElement> regions = ret.regionProperty.regions;

		int regionTopY = 200;
		int mutationTopY = 500;

		{
			DrawRegionElement drawRegionElement = new DrawRegionElement();
			drawRegionElement.label = "Orf1ab";
			drawRegionElement.location = Pair.of(new Point(121, regionTopY), 200);
			drawRegionElement.color = Color.pink;
			regions.add(drawRegionElement);
		}
		{
			DrawRegionElement drawRegionElement = new DrawRegionElement();
			drawRegionElement.label = "S protein";
			drawRegionElement.location = Pair.of(new Point(361, regionTopY), 85);
			drawRegionElement.color = Color.cyan;
			regions.add(drawRegionElement);
		}
		{
			DrawRegionElement drawRegionElement = new DrawRegionElement();
			drawRegionElement.label = "M";
			drawRegionElement.location = Pair.of(new Point(545, regionTopY), 32);
			drawRegionElement.color = Color.magenta;
			regions.add(drawRegionElement);
		}

		// This is connecting line elements
		int regionBottomY = regionTopY + ret.regionProperty.height;
		{
			DrawConnectingLineElement ele = new DrawConnectingLineElement();
			int xx = 160;
			ele.locationInRegionArea = new Point(xx, regionBottomY);
			ele.locationInMutationArea = new Point(xx - 30, mutationTopY);
			listOfConnectingLineElements.add(ele);
		}
		{
			DrawConnectingLineElement ele = new DrawConnectingLineElement();
			int xx = 268;
			ele.locationInRegionArea = new Point(xx, regionBottomY);
			ele.locationInMutationArea = new Point(xx - 60, mutationTopY);
			listOfConnectingLineElements.add(ele);
		}
		{
			DrawConnectingLineElement ele = new DrawConnectingLineElement();
			int xx = 333;
			ele.locationInRegionArea = new Point(xx, regionBottomY);
			ele.locationInMutationArea = new Point(xx + 23, mutationTopY);
			listOfConnectingLineElements.add(ele);
		}
		{
			DrawConnectingLineElement ele = new DrawConnectingLineElement();
			int xx = 564;
			ele.locationInRegionArea = new Point(xx, regionBottomY);
			ele.locationInMutationArea = new Point(xx - 65, mutationTopY);
			listOfConnectingLineElements.add(ele);
		}

		ret.mutationProperty.listofConnectingLineElements = listOfConnectingLineElements;

		List<DrawVariantsProperty> listOfDrawVariants = ret.mutationProperty.listOfDrawVariants;

		{
			DrawVariantsProperty drawVariantsProperty = new DrawVariantsProperty();
			drawVariantsProperty.variantName = "Alpha VOC";
			List<DrawMutationElement> listOfMutationElement = drawVariantsProperty.listOfMutationElement;
			{
				DrawMutationElement drawMutationElement = new DrawMutationElement();
				drawMutationElement.name = "One";
				listOfMutationElement.add(drawMutationElement);
			}
			{
				DrawMutationElement drawMutationElement = new DrawMutationElement();
				drawMutationElement.name = "Two";
				listOfMutationElement.add(drawMutationElement);
			}
			{
				DrawMutationElement drawMutationElement = new DrawMutationElement();
				drawMutationElement.name = "Three";
				listOfMutationElement.add(drawMutationElement);
			}
			{
				DrawMutationElement drawMutationElement = new DrawMutationElement();
				drawMutationElement.name = "Four";
				listOfMutationElement.add(drawMutationElement);
			}
			listOfDrawVariants.add(drawVariantsProperty);
		}
		
		{
			DrawVariantsProperty drawVariantsProperty = new DrawVariantsProperty();
			drawVariantsProperty.variantName = "Delta VOC";
			List<DrawMutationElement> listOfMutationElement = drawVariantsProperty.listOfMutationElement;
			{
				DrawMutationElement drawMutationElement = new DrawMutationElement();
				drawMutationElement.name = "One";
				listOfMutationElement.add(drawMutationElement);
			}
			{
				DrawMutationElement drawMutationElement = new DrawMutationElement();
				drawMutationElement.name = "Two";
				listOfMutationElement.add(drawMutationElement);
			}
			{
				DrawMutationElement drawMutationElement = new DrawMutationElement();
				drawMutationElement.name = "Three";
				listOfMutationElement.add(drawMutationElement);
			}
			{
				DrawMutationElement drawMutationElement = new DrawMutationElement();
				drawMutationElement.name = "Four";
				listOfMutationElement.add(drawMutationElement);
			}
			listOfDrawVariants.add(drawVariantsProperty);
		}

		return ret;
	}

	public DrawPropertiesRegion getRegionProperty() {
		return regionProperty;
	}

	public DrawPropertiesMutation getMutationProperty() {
		return mutationProperty;
	}

	public DrawPropertiesAxis getAxisProperty() {
		return axisProperty;
	}
	
	public DrawPropertiesConnectingLine getConnectingLineProperty() {
		return connectingLineProperty;
	}
	
	public List<DrawMutationElement> getSelectedMutationElement() {
		return selectedMutationElement;
	}

}
