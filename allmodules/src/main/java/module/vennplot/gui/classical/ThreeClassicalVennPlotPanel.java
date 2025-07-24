package module.vennplot.gui.classical;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import module.vennplot.model.ClassicalParameterModel;
import module.vennplot.model.Element;
import module.vennplot.model.SetItem;
import module.vennplot.painter.NameSelection;
import module.vennplot.util.AdjustAndCalculate;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName ThreeClassicalVennPlotPanel
 * 
 * @author mhl
 * 
 * @Date Created on:2019-11-25 15:08
 * 
 */
public class ThreeClassicalVennPlotPanel extends ClassicalVennPlotPanel {

	@Override
	public void paint(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int XPos, int YPos) {

		if (parameterModel.isPaintingConsistentWithSize()) {
			differentSize(g2d, setItems, parameterModel, adjustAndCalculate, XPos, YPos);

		} else {
			notDifferentSize(g2d, setItems, parameterModel, adjustAndCalculate, XPos, YPos);
		}
	}

	private void differentSize(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int XPos, int YPos) {
		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g2d.setComposite(c);

		List<Ellipse2D.Double> circleLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingCircleLocations();
		circleLocations.clear();

		int maxCount = getMaxCount(setItems);

		double count1 = setItems[0].getSetLists().length * 1.0;

		double count2 = setItems[1].getSetLists().length * 1.0;

		double count3 = setItems[2].getSetLists().length * 1.0;

		double percentage1 = count1 / maxCount;

		double percentage2 = count2 / maxCount;

		double percentage3 = count3 / maxCount;

		double r1 = r * percentage1;

		double r2 = r * percentage2;

		double r3 = r * percentage3;

		Element[] coincide = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 }, setItems);

		double coincideCount = coincide.length * 1.0;

		Element[] tempFirst12 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, -1 }, setItems);
		Element[] tempFirst13 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, -1, 1 }, setItems);

		Element[] tempFirst23 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { -1, 1, 1 }, setItems);

		double percentageCount1 = coincideCount / count1;

		double percentageCount2 = coincideCount / count2;

		double percentageCount3 = coincideCount / count3;

		double offset1 = r1 * (1 - percentageCount1);

		double offset2 = r2 * (1 - percentageCount2);

		double offset3 = r3 * (1 - percentageCount3);

		if (tempFirst12.length == count2 && tempFirst13.length == count3) {
			Element[] coincide11 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { -1, 1, 1 },
					setItems);
			drawInclusionRelationship(g2d, parameterModel, XPos, YPos, count2, count3, r1, r2, r3, coincide11,
					adjustAndCalculate, setItems, 0, circleLocations);

		} else if (tempFirst12.length == count1 && tempFirst23.length == count3) {

			Element[] coincide11 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, -1, 1 },
					setItems);
			drawInclusionRelationship(g2d, parameterModel, XPos, YPos, count1, count3, r2, r1, r3, coincide11,
					adjustAndCalculate, setItems, 1, circleLocations);

		} else if (tempFirst13.length == count1 && tempFirst23.length == count2) {

			Element[] coincide11 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, -1 },
					setItems);
			drawInclusionRelationship(g2d, parameterModel, XPos, YPos, count1, count2, r3, r1, r2, coincide11,
					adjustAndCalculate, setItems, 2, circleLocations);

		} else if (tempFirst12.length == count2) {

			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			List<Color> colors = parameterModel.getColors();

			Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 }, setItems);

			Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 }, setItems);

			Element[] tempFirst22 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, -1, 1 },
					setItems);

			double tempXPos11 = (tempFirst22.length != 0 && first4.length == 0) ? 2 * r1 * (tempFirst22.length / count1)
					: 0;
			// Assume x, y, and diameter are instance variables.
			double XPos1 = XPos - r1 - offset1;

			double YPos1 = YPos - r1;

			Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(0));
			g2d.fill(circle);
			circleLocations.add(circle);

			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle);
				g2d.setStroke(oldStroke);
			}

			double XPos2 = XPos - r2 - offset2 - tempXPos11;
			double YPos2 = YPos - r2;
			Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos2, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(1));
			g2d.fill(circle1);
			circleLocations.add(circle1);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle1);
				g2d.setStroke(oldStroke);
			}

			boolean isEqual = first4.length == count2;
			double XPos3 = isEqual && tempFirst22.length > count2
					? XPos - r3 + r3 * (first3.length / count3) - tempXPos11
					: XPos - r3 + offset3 - tempXPos11;
			double YPos3 = YPos - r3;
			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, 2 * r3, 2 * r3);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}

			if (parameterModel.isShowSetValues()) {

				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);

				// draw count
				g2d.setFont(parameterModel.getIntersectionValueFont());
				Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0 },
						setItems);
				FontMetrics fontMetrics = g2d.getFontMetrics();
				if (first1.length != 0) {

					double tempXPos = first3.length == 0 ? XPos1 + (XPos2 - XPos1) / 2
							: (tempFirst22.length != 0 && first4.length == 0) ? XPos1 + (XPos2 - XPos1) / 2
									: first4.length < count2 ? XPos1 + (XPos2 - XPos1) / 2
											: XPos1 + (XPos3 - XPos1) / 2;

					g2d.drawString("" + first1.length, (float) tempXPos - fontMetrics.charWidth(first1.length) / 2,
							(float) YPos);
				}

				Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 },
						setItems);

				if (first2.length != 0) {
					double tempXPos = XPos2 + (XPos3 - XPos2) / 2;
					g2d.drawString("" + first2.length, (float) tempXPos - fontMetrics.charWidth(first2.length) / 2,
							(float) YPos);
				}

				if (first3.length != 0) {

					double tempXPos = tempFirst12.length == count2 ? XPos3 + (XPos2 - XPos3) / 2
							: XPos3 + (XPos1 - XPos3) / 2;

					double tempXPos1 = (first3.length != 0 && first4.length == 0) ? (XPos - tempXPos11 / 2)
							: isEqual ? tempXPos : XPos;

					double tempYPos1 = (first3.length != 0 && first4.length == 0) ? YPos
							: isEqual ? YPos : tempFirst12.length == count2 ? YPos2 + 15 : YPos1 + 15;

					g2d.drawString("" + first3.length, (float) tempXPos1 - fontMetrics.charWidth(first3.length) / 2,
							(float) tempYPos1);
				}

				if (first4.length != 0) {
					// double tempXPos = tempFirst1.length == count2 ? XPos - offset2 : XPos -
					// offset1;

					g2d.drawString("" + first4.length, (float) XPos - fontMetrics.charWidth(first4.length) / 2,
							(float) YPos);
				}
				Element[] three1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1 },
						setItems);
				if (three1.length != 0) {

					double tempOffset = XPos1 + r1 * 2 + ((XPos3 + 2 * r3) - (XPos1 + r1 * 2)) / 2;

					g2d.drawString("" + three1.length, (float) tempOffset - fontMetrics.charWidth(three1.length) / 2,
							(float) YPos);
				}
			}
			if (parameterModel.isShowSetLegend()) {
				paintName(g2d, parameterModel, setItems, XPos);
			}
		} else if (tempFirst13.length == count3) {

			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			List<Color> colors = parameterModel.getColors();
			Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 }, setItems);

			Element[] tempFirst22 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, -1 },
					setItems);

			double tempXPos11 = (tempFirst22.length != 0 && first4.length == 0) ? r1 * (tempFirst22.length / count1) * 2
					: 0;

			// Assume x, y, and diameter are instance variables.
			double XPos1 = XPos - r1 - offset1;
			double YPos1 = YPos - r1;

			Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(0));
			g2d.fill(circle);
			circleLocations.add(circle);
			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle);
				g2d.setStroke(oldStroke);
			}

			Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 }, setItems);

			boolean isEqual = first4.length == count3;
			double XPos2 = isEqual && tempFirst22.length > count3
					? XPos - r2 + r2 * (first2.length / count2) - tempXPos11
					: XPos - r2 + offset2 - tempXPos11;

			double YPos2 = YPos - r2;
			Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos2, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(1));
			g2d.fill(circle1);
			circleLocations.add(circle1);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle1);
				g2d.setStroke(oldStroke);
			}

			double XPos3 = XPos - r3 - offset3 - tempXPos11;
			double YPos3 = YPos - r3;
			Ellipse2D.Double circle2 = new Ellipse2D.Double(XPos3, YPos3, 2 * r3, 2 * r3);
			g2d.setColor(colors.get(2));
			g2d.fill(circle2);
			circleLocations.add(circle2);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle2);
				g2d.setStroke(oldStroke);
			}
			if (parameterModel.isShowSetValues()) {
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);
				g2d.setFont(parameterModel.getIntersectionValueFont());
				// draw count
				Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0 },
						setItems);
				FontMetrics fontMetrics = g2d.getFontMetrics();
				if (first1.length != 0) {

					// double tempXPos = first2.length == 0 ? XPos1 + (XPos3 - XPos1) / 2 : XPos1 +
					// (XPos2 - XPos1) / 2;

					double tempXPos = first2.length == 0 ? XPos1 + (XPos3 - XPos1) / 2
							: (tempFirst22.length != 0 && first4.length == 0) ? XPos1 + (XPos3 - XPos1) / 2
									: first4.length < count3 ? XPos1 + (XPos3 - XPos1) / 2
											: XPos1 + (XPos2 - XPos1) / 2;

					g2d.drawString("" + first1.length, (float) tempXPos - fontMetrics.charWidth(first1.length) / 2,
							(float) YPos);
				}

				if (first2.length != 0) {
					double tempXPos = XPos3 + (XPos2 - XPos3) / 2;
					// boolean isEqual = tempFirst2.length == count3 ? first4.length == count3 :
					// first4.length == count1;

					double tempXPos1 = (tempFirst22.length != 0 && first4.length == 0) ? (XPos - tempXPos11 / 2)
							: isEqual ? tempXPos : XPos;

					double tempYPos1 = (tempFirst22.length != 0 && first4.length == 0) ? YPos
							: isEqual ? YPos : tempFirst13.length == count3 ? YPos3 + 15 : YPos1 + 15;

					g2d.drawString("" + first2.length, (float) tempXPos1 - fontMetrics.charWidth(first2.length) / 2,
							(float) tempYPos1);
				}

				Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 },
						setItems);

				if (first3.length != 0) {

					double tempXPos = XPos2 + (XPos3 - XPos2) / 2;

					g2d.drawString("" + first3.length, (float) tempXPos - fontMetrics.charWidth(first3.length) / 2,
							(float) YPos);

				}

				if (first4.length != 0) {
					// double tempXPos = tempFirst2.length == count3 ? XPos - offset3 : XPos -
					// offset1;
					g2d.drawString("" + first4.length, (float) XPos - fontMetrics.charWidth(first4.length) / 2,
							(float) YPos);
				}
				Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0 },
						setItems);

				if (second1.length != 0) {
					double tempXPos = (XPos2 + 2 * r2 - (XPos1 + 2 * r1)) / 2;

					g2d.drawString("" + second1.length,
							(float) (XPos1 + 2 * r1 + tempXPos - fontMetrics.charWidth(second1.length) / 2),
							(float) YPos);
				}
			}
			if (parameterModel.isShowSetLegend()) {
				paintName(g2d, parameterModel, setItems, XPos);
			}

		} else if (tempFirst12.length == count1) {
			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			List<Color> colors = parameterModel.getColors();
			Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 }, setItems);

			Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 }, setItems);

			Element[] tempFirst22 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { -1, 1, 1 },
					setItems);

			double tempXPos11 = (tempFirst22.length != 0 && first4.length == 0) ? r2 * (tempFirst22.length / count2) * 2
					: 0;
			// Assume x, y, and diameter are instance variables.
			double XPos1 = XPos - r1 - offset1 - tempXPos11;
			double YPos1 = YPos - r1;
			Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(0));
			g2d.fill(circle);
			circleLocations.add(circle);
			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle);
				g2d.setStroke(oldStroke);
			}

			double XPos2 = XPos - r2 - offset2;

			double YPos2 = YPos - r2;

			Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos2, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(1));
			g2d.fill(circle1);
			circleLocations.add(circle1);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle1);
				g2d.setStroke(oldStroke);
			}

			boolean isEqual = first4.length == count1;

			double XPos3 = isEqual && tempFirst22.length > count1
					? XPos - r3 + r3 * (first3.length / count3) - tempXPos11
					: XPos - r3 + offset3 - tempXPos11;

			double YPos3 = YPos - r3;
			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, 2 * r3, 2 * r3);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}
			if (parameterModel.isShowSetValues()) {
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);
				g2d.setFont(parameterModel.getIntersectionValueFont());
				// draw count
				Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0 },
						setItems);
				FontMetrics fontMetrics = g2d.getFontMetrics();
				if (first1.length != 0) {

					double tempXPos = first3.length == 0 ? XPos1 + (XPos2 - XPos1) / 2
							: (tempFirst22.length != 0 && first4.length == 0) ? XPos2 + (XPos1 - XPos2) / 2
									: first4.length < count1 ? XPos2 + (XPos1 - XPos2) / 2
											: XPos2 + (XPos3 - XPos2) / 2;

					g2d.drawString("" + first1.length, (float) tempXPos - fontMetrics.charWidth(first1.length) / 2,
							(float) YPos);
				}

				Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 },
						setItems);

				if (first2.length != 0) {

					double tempXPos = XPos1 + (XPos3 - XPos1) / 2;

					g2d.drawString("" + first2.length, (float) tempXPos - fontMetrics.charWidth(first2.length) / 2,
							(float) YPos);
				}

				if (first3.length != 0) {

					double tempXPos = XPos3 + (XPos1 - XPos3) / 2;

					double tempXPos1 = (first3.length != 0 && first4.length == 0) ? (XPos - tempXPos11 / 2)
							: isEqual ? tempXPos : XPos;

					double tempYPos1 = (first3.length != 0 && first4.length == 0) ? YPos
							: isEqual ? YPos : tempFirst12.length == count2 ? YPos2 + 15 : YPos1 + 15;

					g2d.drawString("" + first3.length, (float) tempXPos1 - fontMetrics.charWidth(first3.length) / 2,
							(float) tempYPos1);
				}

				if (first4.length != 0) {
					// double tempXPos = tempFirst1.length == count2 ? XPos - offset2 : XPos -
					// offset1;

					g2d.drawString("" + first4.length, (float) XPos - fontMetrics.charWidth(first4.length) / 2,
							(float) YPos);
				}
				Element[] three1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1 },
						setItems);
				if (three1.length != 0) {

					double tempOffset = XPos2 + r2 * 2 + ((XPos3 + 2 * r3) - (XPos2 + r2 * 2)) / 2;

					g2d.drawString("" + three1.length, (float) tempOffset - fontMetrics.charWidth(three1.length) / 2,
							(float) YPos);
				}
			}
			if (parameterModel.isShowSetLegend()) {
				paintName(g2d, parameterModel, setItems, XPos);
			}
		} else if (tempFirst23.length == count3) {
			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			List<Color> colors = parameterModel.getColors();

			Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 }, setItems);

			Element[] tempFirst22 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, -1 },
					setItems);

			double tempXPos11 = (tempFirst22.length != 0 && first4.length == 0) ? r1 * (tempFirst22.length / count1) * 2
					: 0;

			// Assume x, y, and diameter are instance variables.
			double XPos1 = XPos - r1 + offset1 - tempXPos11;
			double YPos1 = YPos - r1;

			Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(0));
			g2d.fill(circle);
			circleLocations.add(circle);
			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle);
				g2d.setStroke(oldStroke);
			}

			double XPos2 = XPos - r2 - offset2;

			double YPos2 = YPos - r2;

			Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos2, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(1));
			g2d.fill(circle1);
			circleLocations.add(circle1);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle1);
				g2d.setStroke(oldStroke);
			}

			double XPos3 = XPos - r3 - offset3 - tempXPos11;
			double YPos3 = YPos - r3;
			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, 2 * r3, 2 * r3);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}

			Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 }, setItems);

			boolean isEqual = first4.length == count3;
			if (parameterModel.isShowSetValues()) {
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);
				g2d.setFont(parameterModel.getIntersectionValueFont());
				// draw count
				Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0 },
						setItems);
				FontMetrics fontMetrics = g2d.getFontMetrics();
				if (first1.length != 0) {

					double tempXPos = first2.length == 0 ? XPos2 + (XPos3 - XPos2) / 2
							: (tempFirst22.length != 0 && first4.length == 0) ? XPos2 + (XPos3 - XPos2) / 2
									: first4.length < count3 ? XPos2 + (XPos3 - XPos2) / 2
											: XPos2 + (XPos1 - XPos2) / 2;
					g2d.drawString("" + first1.length, (float) tempXPos - fontMetrics.charWidth(first1.length) / 2,
							(float) YPos);
				}

				if (first2.length != 0) {
					double tempXPos = XPos3 + (XPos2 - XPos3) / 2;
					// boolean isEqual = tempFirst2.length == count3 ? first4.length == count3 :
					// first4.length == count1;

					double tempXPos1 = (tempFirst22.length != 0 && first4.length == 0) ? (XPos - tempXPos11 / 2)
							: isEqual ? tempXPos : XPos;

					double tempYPos1 = (tempFirst22.length != 0 && first4.length == 0) ? YPos
							: isEqual ? YPos : YPos3 + 15;

					g2d.drawString("" + first2.length, (float) tempXPos1 - fontMetrics.charWidth(first2.length) / 2,
							(float) tempYPos1 - 5);
				}

				Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 },
						setItems);

				if (first3.length != 0) {

					double tempXPos = XPos3 + (XPos1 - XPos3) / 2;

					g2d.drawString("" + first3.length, (float) tempXPos - fontMetrics.charWidth(first3.length) / 2,
							(float) YPos);
				}

				if (first4.length != 0) {
					// double tempXPos = tempFirst2.length == count3 ? XPos - offset3 : XPos -
					// offset1;
					g2d.drawString("" + first4.length, (float) XPos - fontMetrics.charWidth(first4.length) / 2,
							(float) YPos);
				}
				Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0 },
						setItems);

				if (second1.length != 0) {
					double tempXPos = (XPos2 + 2 * r2 - (XPos1 + 2 * r1)) / 2;

					g2d.drawString("" + second1.length,
							(float) (XPos1 + 2 * r1 + tempXPos - fontMetrics.charWidth(second1.length) / 2),
							(float) YPos);
				}
			}
			if (parameterModel.isShowSetLegend()) {
				paintName(g2d, parameterModel, setItems, XPos);
			}
		} else if (tempFirst13.length == count1) {
			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			List<Color> colors = parameterModel.getColors();

			Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 }, setItems);

			Element[] tempFirst22 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { -1, 1, 1 },
					setItems);

			double tempXPos11 = (tempFirst22.length != 0 && first4.length == 0) ? r2 * (tempFirst22.length / count2) * 2
					: 0;

			// Assume x, y, and diameter are instance variables.
			double XPos1 = XPos - r1 - offset1 - tempXPos11;
			double YPos1 = YPos - r1;

			Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(0));
			g2d.fill(circle);
			circleLocations.add(circle);
			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle);
				g2d.setStroke(oldStroke);
			}

			Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 }, setItems);

			boolean isEqual = first4.length == count1;

			double XPos2 = isEqual && tempFirst22.length > count1
					? XPos - r2 + r2 * (first2.length / count2) - tempXPos11
					: XPos - r2 + offset2 - tempXPos11;

			double YPos2 = YPos - r2;

			Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos2, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(1));
			g2d.fill(circle1);
			circleLocations.add(circle1);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle1);
				g2d.setStroke(oldStroke);
			}

			double XPos3 = XPos - r3 - offset3;

			double YPos3 = YPos - r3;

			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, 2 * r3, 2 * r3);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}
			if (parameterModel.isShowSetValues()) {
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);
				g2d.setFont(parameterModel.getIntersectionValueFont());
				// draw count
				Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1 },
						setItems);
				FontMetrics fontMetrics = g2d.getFontMetrics();
				if (first1.length != 0) {

					// double tempXPos = first2.length == 0 ? XPos1 + (XPos3 - XPos1) / 2 : XPos1 +
					// (XPos2 - XPos1) / 2;

					double tempXPos = first2.length == 0 ? XPos3 + (XPos1 - XPos3) / 2
							: (tempFirst22.length != 0 && first4.length == 0) ? XPos3 + (XPos1 - XPos3) / 2
									: first4.length < count1 ? XPos3 + (XPos1 - XPos3) / 2
											: XPos3 + (XPos2 - XPos3) / 2;

					g2d.drawString("" + first1.length, (float) tempXPos - fontMetrics.charWidth(first1.length) / 2,
							(float) YPos);
				}

				if (first2.length != 0) {
					double tempXPos = XPos3 + (XPos2 - XPos3) / 2;
					// boolean isEqual = tempFirst2.length == count3 ? first4.length == count3 :
					// first4.length == count1;

					double tempXPos1 = (tempFirst22.length != 0 && first4.length == 0) ? (XPos - tempXPos11 / 2)
							: isEqual ? tempXPos : XPos;

					double tempYPos1 = (tempFirst22.length != 0 && first4.length == 0) ? YPos
							: isEqual ? YPos : YPos1 + 5;

					g2d.drawString("" + first2.length, (float) tempXPos1 - fontMetrics.charWidth(first2.length) / 2,
							(float) tempYPos1);
				}

				Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 },
						setItems);

				if (first3.length != 0) {

					double tempXPos = XPos1 + (XPos2 - XPos1) / 2;

					g2d.drawString("" + first3.length, (float) tempXPos - fontMetrics.charWidth(first3.length) / 2,
							(float) YPos);

				}

				if (first4.length != 0) {
					// double tempXPos = tempFirst2.length == count3 ? XPos - offset3 : XPos -
					// offset1;
					g2d.drawString("" + first4.length, (float) XPos - fontMetrics.charWidth(first4.length) / 2,
							(float) YPos);
				}
				Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0 },
						setItems);

				if (second1.length != 0) {
					double tempXPos = (XPos2 + 2 * r2 - (XPos3 + 2 * r3)) / 2;

					g2d.drawString("" + second1.length,
							(float) (XPos3 + 2 * r3 + tempXPos - fontMetrics.charWidth(second1.length) / 2),
							(float) YPos);
				}
			}
			if (parameterModel.isShowSetLegend()) {
				paintName(g2d, parameterModel, setItems, XPos);
			}
		} else if (tempFirst23.length == count2) {
			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			List<Color> colors = parameterModel.getColors();
			Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 }, setItems);

			Element[] tempFirst22 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, -1, 1 },
					setItems);

			double tempXPos11 = (tempFirst22.length != 0 && first4.length == 0) ? r2 * (tempFirst22.length / count2) * 2
					: 0;

			// Assume x, y, and diameter are instance variables.
			double XPos1 = XPos - r1 + offset1 - tempXPos11;
			double YPos1 = YPos - r1;

			Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(0));
			g2d.fill(circle);
			circleLocations.add(circle);
			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle);
				g2d.setStroke(oldStroke);
			}

			double XPos2 = XPos - r2 - offset2 - tempXPos11;

			double YPos2 = YPos - r2;

			Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos2, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(1));
			g2d.fill(circle1);
			circleLocations.add(circle1);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle1);
				g2d.setStroke(oldStroke);
			}

			double XPos3 = XPos - r3 - offset3;
			double YPos3 = YPos - r3;
			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, 2 * r3, 2 * r3);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}

			Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 }, setItems);

			boolean isEqual = first4.length == count2;
			if (parameterModel.isShowSetValues()) {
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);
				g2d.setFont(parameterModel.getIntersectionValueFont());
				// draw count
				Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1 },
						setItems);
				FontMetrics fontMetrics = g2d.getFontMetrics();
				if (first1.length != 0) {

					double tempXPos = first2.length == 0 ? XPos3 + (XPos2 - XPos3) / 2
							: (tempFirst22.length != 0 && first4.length == 0) ? XPos3 + (XPos2 - XPos3) / 2
									: first4.length < count2 ? XPos3 + (XPos2 - XPos3) / 2
											: XPos3 + (XPos1 - XPos3) / 2;
					g2d.drawString("" + first1.length, (float) tempXPos - fontMetrics.charWidth(first1.length) / 2,
							(float) YPos);
				}

				if (first2.length != 0) {
					double tempXPos = XPos3 + (XPos2 - XPos3) / 2;

					double tempXPos1 = (tempFirst22.length != 0 && first4.length == 0) ? (XPos - tempXPos11 / 2)
							: isEqual ? tempXPos : XPos;

					double tempYPos1 = (tempFirst22.length != 0 && first4.length == 0) ? YPos
							: isEqual ? YPos : YPos2 + 5;

					g2d.drawString("" + first2.length, (float) tempXPos1 - fontMetrics.charWidth(first2.length) / 2,
							(float) tempYPos1);
				}

				Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 },
						setItems);

				if (first3.length != 0) {

					double tempXPos = XPos2 + (XPos1 - XPos2) / 2;

					g2d.drawString("" + first3.length, (float) tempXPos - fontMetrics.charWidth(first3.length) / 2,
							(float) YPos);
				}

				if (first4.length != 0) {
					g2d.drawString("" + first4.length, (float) XPos - fontMetrics.charWidth(first4.length) / 2,
							(float) YPos);
				}
				Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0 },
						setItems);

				if (second1.length != 0) {
					double tempXPos = (XPos1 + 2 * r1 - (XPos3 + 2 * r3)) / 2;

					g2d.drawString("" + second1.length,
							(float) (XPos3 + 2 * r3 + tempXPos - fontMetrics.charWidth(second1.length) / 2),
							(float) YPos);
				}
			}
			if (parameterModel.isShowSetLegend()) {
				paintName(g2d, parameterModel, setItems, XPos);
			}
		} else {
			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			List<Color> colors = parameterModel.getColors();
			// Assume x, y, and diameter are instance variables.
			double XPos1 = XPos - r1;
			double YPos1 = YPos - r1 - offset1;
			Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(0));
			g2d.fill(circle);
			circleLocations.add(circle);
			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle);
				g2d.setStroke(oldStroke);
			}

			double tempOffset2 = r2 * (1 - tempFirst23.length / count2);

			double XPos2 = XPos - r2 - tempOffset2;//

			double YPos2 = YPos - r2 + r2 * (1 - tempFirst12.length / count2);

			double h1 = (YPos2 + r2) - (YPos1 + r1);

			double pow1 = Math.pow(h1, 2) + Math.pow(tempOffset2, 2);

			double ypos11 = (2 * h1 - Math.sqrt(8 * Math.pow(h1, 2) - 4 * pow1)) / 2;

			Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos2 - ypos11, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(1));
			g2d.fill(circle1);
			circleLocations.add(circle1);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle1);
				g2d.setStroke(oldStroke);
			}

			double tempOffset3 = r3 * (1 - tempFirst23.length / count3);

			double XPos3 = XPos - r3 + tempOffset3;//

			double YPos3 = YPos - r3 + r3 * (1 - tempFirst13.length / count3);

			double h2 = (YPos3 + r3) - (YPos1 + r1);

			double pow2 = Math.pow(h2, 2) + Math.pow(tempOffset3, 2);

			double ypos22 = (2 * h2 - Math.sqrt(8 * Math.pow(h2, 2) - 4 * pow2)) / 2;

			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3 - ypos22, 2 * r3, 2 * r3);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}
			if (parameterModel.isShowSetValues()) {
				// draw count
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);
				g2d.setFont(parameterModel.getIntersectionValueFont());
				FontMetrics fontMetrics = g2d.getFontMetrics();
				Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0 },
						setItems);
				Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 },
						setItems);
				Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 },
						setItems);
				Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 },
						setItems);

				if (first1.length != 0) {

					double tempYPos = YPos1
							+ ((YPos2 - ypos11 > YPos3 - ypos22 ? YPos3 - ypos22 : YPos2 - ypos11) - YPos1) / 2;
					g2d.drawString("" + first1.length, (float) XPos - fontMetrics.charWidth(first1.length) / 2,
							(float) tempYPos);
				}
				if (first2.length != 0) {

					g2d.drawString("" + first2.length, (float) (XPos - r2 * tempFirst23.length / count2
							- fontMetrics.charWidth(first2.length) / 2), (float) (YPos2 + 20));
				}
				if (first3.length != 0) {

					g2d.drawString("" + first3.length, (float) (XPos + r3 * tempFirst23.length / count3
							- fontMetrics.charWidth(first3.length) / 2), (float) (YPos3 + 20));
				}
				if (first4.length != 0) {

					g2d.drawString("" + first4.length, (float) XPos - fontMetrics.charWidth(first4.length) / 2,
							(float) YPos);
				}

				Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0 },
						setItems);

				Element[] second2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 },
						setItems);

				if (second1.length != 0) {

					double tempXPos = second2.length != 0 || first4.length != 0 ? XPos2 + (XPos3 - XPos2) / 2
							: XPos2 + r2;

					double tempYPos = YPos2 + r2;

					g2d.drawString("" + second1.length, (float) tempXPos - fontMetrics.charWidth(second1.length) / 2,
							(float) tempYPos);
				}

				if (second2.length != 0) {

					double tempXPos = XPos3 + (XPos2 + 2 * r2 - XPos3) / 2;

					double tempYPos = YPos2 + r2;

					g2d.drawString("" + second2.length, (float) tempXPos - fontMetrics.charWidth(second2.length) / 2,
							(float) tempYPos);
				}

				Element[] three1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1 },
						setItems);

				if (three1.length != 0) {

					double tempXPos = second2.length != 0 || first4.length != 0
							? (XPos2 + r2 * 2) + ((XPos3 + r3 * 2) - (XPos2 + r2 * 2)) / 2
							: XPos3 + r3;

					double tempYPos = YPos3 + r3;
					g2d.drawString("" + three1.length, (float) tempXPos - fontMetrics.charWidth(three1.length) / 2,
							(float) tempYPos);
				}
			}
			if (parameterModel.isShowSetLegend()) {
				paintName(g2d, parameterModel, setItems, XPos);
			}
		}
	}

	/**
	 *
	 * @Title drawInclusionRelationship
	 *
	 * @param g2d
	 * @param XPos               :界面中心位置X轴坐标
	 * @param YPos               :界面中心位置Y轴坐标
	 * @param count1
	 * @param count2
	 * @param maxCircle          :最大数据半径
	 * @param r1
	 * @param r2
	 * @param coincide
	 * @param adjustAndCalculate
	 * @param setItems
	 * @param maxIndex           : 0表示第一条数据最大,1表示第二条数据最大,2表示第三条数据最大
	 * 
	 * @author mhl
	 * @param circleLocations
	 *
	 * @Date Created on:2019-11-25 15:08
	 */
	private void drawInclusionRelationship(Graphics2D g2d, ClassicalParameterModel parameterModel, int XPos, int YPos,
			double count1, double count2, double maxCircle, double r1, double r2, Element[] coincide,
			AdjustAndCalculate adjustAndCalculate, SetItem[] setItems, int maxIndex,
			List<Ellipse2D.Double> circleLocations) {

		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		List<Color> colors = parameterModel.getColors();
		double coincideCount = coincide.length * 1.0;

		double percentageCount1 = coincideCount / count1;

		double percentageCount2 = coincideCount / count2;

		double offset1 = r1 * (1 - percentageCount1);

		double offset2 = r2 * (1 - percentageCount2);

		// Assume x, y, and diameter are instance variables.
		double XPos1 = XPos - maxCircle;
		double YPos1 = YPos - maxCircle;
		Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos1, YPos1, 2 * maxCircle, 2 * maxCircle);
		g2d.setColor(colors.get(0));
		g2d.fill(circle1);
		circleLocations.add(circle1);
		if (nameSelections.contains(new NameSelection(0))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle1);
			g2d.setStroke(oldStroke);
		}

		g2d.setFont(parameterModel.getIntersectionValueFont());

		FontMetrics fontMetrics = g2d.getFontMetrics();

		Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0 }, setItems);

		Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0 }, setItems);

		Element[] three1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1 }, setItems);

		if (coincideCount == count2) { // 大圆包含中圆,中圆包含小圆
			double offset = maxCircle - r2;
			double XPos2 = XPos - r1 - offset1 + offset;
			double YPos2 = YPos - r1;
			Ellipse2D.Double circle2 = new Ellipse2D.Double(XPos2, YPos2, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(1));
			g2d.fill(circle2);
			circleLocations.add(circle2);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle2);
				g2d.setStroke(oldStroke);
			}

			double XPos3 = XPos - r2 + offset2 + offset;
			double YPos3 = YPos - r2;
			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}
			if (parameterModel.isShowSetValues()) {
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);

				if (first1.length != 0) {
					Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 },
							setItems);

					g2d.drawString("" + first1.length,
							(float) (XPos1 + maxCircle - r1 - fontMetrics.charWidth(first1.length) / 2), (float) YPos);

					if (first2.length != 0) {
						g2d.drawString("" + first2.length,
								(float) (XPos3 - offset1 - fontMetrics.charWidth(first2.length) / 2), (float) YPos);
					}

					Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 },
							setItems);

					g2d.drawString("" + first3.length, (float) (XPos3 + r2 - fontMetrics.charWidth(first3.length) / 2),
							(float) YPos);
				} else if (second1.length != 0) {
					Element[] second2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 },
							setItems);

					// g2d.drawString("" + second1.length, (float) (XPos3 - offset), (float) YPos);
					g2d.drawString("" + second1.length,
							(float) (XPos1 + (XPos2 - XPos1) / 2 - fontMetrics.charWidth(second1.length) / 2),
							(float) YPos);

					if (second2.length != 0) {

						g2d.drawString("" + second2.length,
								(float) (XPos3 - offset1 - fontMetrics.charWidth(second2.length) / 2), (float) YPos);
					}

					Element[] second3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 },
							setItems);

					g2d.drawString("" + second3.length,
							(float) (XPos3 + r2 - fontMetrics.charWidth(second3.length) / 2), (float) YPos);
				} else if (three1.length != 0) {

					Element[] three2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 },
							setItems);

					// g2d.drawString("" + three1.length, (float) (XPos3 - offset), (float) YPos);
					g2d.drawString("" + three1.length,
							(float) (XPos1 + (XPos2 - XPos1) / 2 - fontMetrics.charWidth(three1.length) / 2),
							(float) YPos);

					if (three2.length != 0) {

						g2d.drawString("" + three2.length,
								(float) (XPos3 - offset1 - fontMetrics.charWidth(three2.length) / 2), (float) YPos);
					}

					Element[] three3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 },
							setItems);

					g2d.drawString("" + three3.length, (float) (XPos3 + r2 - fontMetrics.charWidth(three3.length) / 2),
							(float) YPos);

				}
			}
		} else if (coincideCount == count1) { // 大圆包含中圆,中圆包含小圆
			double offset = maxCircle - r2;
			double XPos2 = XPos - r1 + offset2 + offset;
			double YPos2 = YPos - r1;
			Ellipse2D.Double circle2 = new Ellipse2D.Double(XPos2, YPos2, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(1));
			g2d.fill(circle2);
			circleLocations.add(circle2);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle2);
				g2d.setStroke(oldStroke);
			}

			double XPos3 = XPos - r2 - offset1 + offset;
			double YPos3 = YPos - r2;
			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}
			if (parameterModel.isShowSetValues()) {
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);

				if (first1.length != 0) {
					Element[] first2 = count1 > count2
							? adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 }, setItems)
							: adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 }, setItems);

					g2d.drawString("" + first1.length,
							(float) (XPos3 - offset - fontMetrics.charWidth(first1.length) / 2), (float) YPos);

					g2d.drawString("" + first2.length,
							(float) (XPos2 - offset2 - fontMetrics.charWidth(first2.length) / 2), (float) YPos);

					Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 },
							setItems);

					g2d.drawString("" + first3.length, (float) (XPos2 + r1 - fontMetrics.charWidth(first3.length) / 2),
							(float) YPos);
				} else if (second1.length != 0) {
					Element[] second2 = count1 > count2
							? adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 }, setItems)
							: adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 }, setItems);

					g2d.drawString("" + second1.length,
							(float) (XPos3 - offset - fontMetrics.charWidth(second1.length) / 2), (float) YPos);

					g2d.drawString("" + second2.length,
							(float) (XPos2 - offset2 - fontMetrics.charWidth(second2.length) / 2), (float) YPos);

					Element[] second3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 },
							setItems);

					g2d.drawString("" + second3.length,
							(float) (XPos2 + r1 - fontMetrics.charWidth(second3.length) / 2), (float) YPos);
				} else if (three1.length != 0) {
					Element[] three2 = count1 > count2
							? adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 }, setItems)
							: adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 }, setItems);

					g2d.drawString("" + three1.length,
							(float) (XPos3 - offset - fontMetrics.charWidth(three1.length) / 2), (float) YPos);

					g2d.drawString("" + three2.length,
							(float) (XPos2 - offset2 - fontMetrics.charWidth(three2.length) / 2), (float) YPos);

					Element[] three3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 },
							setItems);

					g2d.drawString("" + three3.length, (float) (XPos2 + r1 - fontMetrics.charWidth(three3.length) / 2),
							(float) YPos);
				}
			}
		} else {// 大圆包含两个小圆,小圆非包含关系

			double offset11 = maxCircle - r2;
			double offset = offset11 - offset2;

			double XPos2 = XPos - r1 - offset1 + offset;
			double YPos2 = YPos - r1;
			Ellipse2D.Double circle2 = new Ellipse2D.Double(XPos2, YPos2, 2 * r1, 2 * r1);
			g2d.setColor(colors.get(1));
			g2d.fill(circle2);
			circleLocations.add(circle2);
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle2);
				g2d.setStroke(oldStroke);
			}

			double XPos3 = XPos - r2 + offset11;
			double YPos3 = YPos - r2;
			Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, 2 * r2, 2 * r2);
			g2d.setColor(colors.get(2));
			g2d.fill(circle3);
			circleLocations.add(circle3);
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(circle3);
				g2d.setStroke(oldStroke);
			}
			if (parameterModel.isShowSetValues()) {
				Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
				g2d.setComposite(c1);
				g2d.setColor(Color.black);

				Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 },
						setItems);

				Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 },
						setItems);

				Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 },
						setItems);

				Element[] second2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 },
						setItems);

				if (maxIndex == 0) {

					if (first1.length != 0) {
						g2d.drawString("" + first1.length,
								(float) (XPos1 + (XPos2 - XPos1) / 2 - fontMetrics.charWidth(first1.length) / 2),
								(float) YPos);
					}
					if (first2.length != 0) {
						g2d.drawString("" + first2.length,
								(float) (XPos2 + (XPos3 - XPos2) / 2 - fontMetrics.charWidth(first2.length) / 2),
								(float) YPos);
					}
					if (first3.length != 0) {
						double offsetX2 = XPos2 + 2 * r1;
						g2d.drawString("" + first3.length, (float) (offsetX2 + (XPos3 + 2 * r2 - offsetX2) / 2
								- fontMetrics.charWidth(first3.length) / 2), (float) YPos);
					}
					if (first4.length != 0) {
						g2d.drawString("" + first4.length,
								(float) (XPos + offset - fontMetrics.charWidth(first4.length) / 2), (float) YPos);
					}

				} else if (maxIndex == 1) {

					if (second1.length != 0) {
						g2d.drawString("" + second1.length,
								(float) (XPos1 + (XPos2 - XPos1) / 2 - fontMetrics.charWidth(second1.length) / 2),
								(float) YPos);
					}
					if (first2.length != 0) {
						g2d.drawString("" + first2.length,
								(float) (XPos2 + (XPos3 - XPos2) / 2 - fontMetrics.charWidth(first2.length) / 2),
								(float) YPos);
					}
					if (second2.length != 0) {
						double offsetX2 = XPos2 + 2 * r1;
						g2d.drawString("" + second2.length, (float) (offsetX2 + (XPos3 + 2 * r2 - offsetX2) / 2
								- fontMetrics.charWidth(second2.length) / 2), (float) YPos);
					}
					if (first4.length != 0) {
						g2d.drawString("" + first4.length,
								(float) (XPos + offset - fontMetrics.charWidth(first4.length) / 2), (float) YPos);
					}

				} else if (maxIndex == 2) {

					if (three1.length != 0) {
						g2d.drawString("" + three1.length,
								(float) (XPos1 + (XPos2 - XPos1) / 2 - fontMetrics.charWidth(three1.length) / 2),
								(float) YPos);
					}

					if (first3.length != 0) {
						g2d.drawString("" + first3.length,
								(float) (XPos2 + (XPos3 - XPos2) / 2 - fontMetrics.charWidth(first3.length) / 2),
								(float) YPos);
					}
					if (first4.length != 0) {
						g2d.drawString("" + first4.length,
								(float) (XPos + offset - fontMetrics.charWidth(first4.length) / 2), (float) YPos);
					}
					if (second2.length != 0) {

						double offsetX2 = XPos2 + 2 * r1;

						double offsetX3 = XPos3 + 2 * r2;

						g2d.drawString("" + second2.length, (float) (offsetX2 + (offsetX3 - offsetX2) / 2
								- fontMetrics.charWidth(second2.length) / 2), (float) YPos);
					}

				}
			}
		}
		if (parameterModel.isShowSetLegend()) {
			paintName(g2d, parameterModel, setItems, XPos, maxIndex);
		}
	}

	private void paintName(Graphics2D g2d, ClassicalParameterModel parameterModel, SetItem[] setItems, int XPos,
			int maxIndex) {
		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g2d.setComposite(c);

		g2d.setFont(parameterModel.getNameFont());

		List<Rectangle2D.Double> paintingLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingNameLocations();
		paintingLocations.clear();

		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		List<Color> colors = parameterModel.getColors();

		FontMetrics fontMetrics = g2d.getFontMetrics();

		int d = fontMetrics.getHeight();

		String name1 = setItems[0].getName();

		String name2 = setItems[1].getName();

		String name3 = setItems[2].getName();

		int stringWidth1 = fontMetrics.stringWidth(name1);

		int stringWidth2 = fontMetrics.stringWidth(name2);

		int stringWidth3 = fontMetrics.stringWidth(name3);

		int maxValue = stringWidth1 > stringWidth2 ? stringWidth1 : stringWidth2;

		maxValue = maxValue > stringWidth3 ? maxValue : stringWidth3;

		double titleXPos = XPos + XPos - maxValue - 100;

		if (maxIndex == 0) {
			double YPos1 = 20;
			double YPos2 = YPos1 + d + 5;
			double YPos3 = YPos2 + d + 5;

			Ellipse2D.Double titleCircle1 = new Ellipse2D.Double(titleXPos - d - 5, YPos1, d, d);
			g2d.setColor(colors.get(0));
			g2d.fill(titleCircle1);

			Ellipse2D.Double titleCircle2 = new Ellipse2D.Double(titleXPos - d - 5, YPos2, d, d);
			g2d.setColor(colors.get(1));
			g2d.fill(titleCircle2);

			Ellipse2D.Double titleCircle3 = new Ellipse2D.Double(titleXPos - d - 5, YPos3, d, d);
			g2d.setColor(colors.get(2));
			g2d.fill(titleCircle3);

			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos1, d + stringWidth1, d));
			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos2, d + stringWidth2, d));
			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos3, d + stringWidth3, d));

			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos1, fontMetrics.stringWidth(name1) + d + 10, d));
				g2d.setStroke(oldStroke);
			}
			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos2, fontMetrics.stringWidth(name2) + d + 10, d));
				g2d.setStroke(oldStroke);
			}

			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos3, fontMetrics.stringWidth(name3) + d + 10, d));
				g2d.setStroke(oldStroke);
			}
			g2d.setColor(Color.black);

			Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
			g2d.setComposite(c1);

			g2d.drawString(name1, (int) titleXPos, (int) (YPos1 + d / 2 + 5));

			g2d.drawString(name2, (int) titleXPos, (int) (YPos2 + d / 2 + 5));

			g2d.drawString(name3, (int) titleXPos, (int) (YPos3 + d / 2 + 5));

		} else if (maxIndex == 1) {
			double YPos1 = 20;
			double YPos2 = YPos1 + d + 5;
			double YPos3 = YPos2 + d + 5;

			Ellipse2D.Double titleCircle1 = new Ellipse2D.Double(titleXPos - d - 5, YPos1, d, d);
			g2d.setColor(colors.get(1));
			g2d.fill(titleCircle1);

			Ellipse2D.Double titleCircle2 = new Ellipse2D.Double(titleXPos - d - 5, YPos2, d, d);
			g2d.setColor(colors.get(0));
			g2d.fill(titleCircle2);

			Ellipse2D.Double titleCircle3 = new Ellipse2D.Double(titleXPos - d - 5, YPos3, d, d);
			g2d.setColor(colors.get(2));
			g2d.fill(titleCircle3);

			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos2, d + stringWidth2, d));

			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos1, d + stringWidth1, d));

			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos3, d + stringWidth3, d));

			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos1, fontMetrics.stringWidth(name1) + d + 10, d));
				g2d.setStroke(oldStroke);
			}
			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos2, fontMetrics.stringWidth(name2) + d + 10, d));
				g2d.setStroke(oldStroke);
			}

			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos3, fontMetrics.stringWidth(name3) + d + 10, d));
				g2d.setStroke(oldStroke);
			}

			g2d.setColor(Color.black);

			Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
			g2d.setComposite(c1);

			g2d.drawString(name1, (int) titleXPos, (int) (YPos1 + d / 2 + 5));

			g2d.drawString(name2, (int) titleXPos, (int) (YPos2 + d / 2 + 5));

			g2d.drawString(name3, (int) titleXPos, (int) (YPos3 + d / 2 + 5));

		} else if (maxIndex == 2) {

			double YPos1 = 20;
			double YPos2 = YPos1 + d + 5;
			double YPos3 = YPos2 + d + 5;

			Ellipse2D.Double titleCircle1 = new Ellipse2D.Double(titleXPos - d - 5, YPos1, d, d);
			g2d.setColor(colors.get(1));
			g2d.fill(titleCircle1);

			Ellipse2D.Double titleCircle2 = new Ellipse2D.Double(titleXPos - d - 5, YPos2, d, d);
			g2d.setColor(colors.get(2));
			g2d.fill(titleCircle2);

			Ellipse2D.Double titleCircle3 = new Ellipse2D.Double(titleXPos - d - 5, YPos3, d, d);
			g2d.setColor(colors.get(0));
			g2d.fill(titleCircle3);

			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos3, d + stringWidth3, d));

			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos1, d + stringWidth1, d));

			paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos2, d + stringWidth2, d));

			if (nameSelections.contains(new NameSelection(1))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos1, fontMetrics.stringWidth(name1) + d + 10, d));
				g2d.setStroke(oldStroke);
			}
			if (nameSelections.contains(new NameSelection(2))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos2, fontMetrics.stringWidth(name2) + d + 10, d));
				g2d.setStroke(oldStroke);
			}

			if (nameSelections.contains(new NameSelection(0))) {
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos3, fontMetrics.stringWidth(name3) + d + 10, d));
				g2d.setStroke(oldStroke);
			}

			Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
			g2d.setComposite(c1);
			g2d.setColor(Color.black);

			g2d.drawString(name1, (int) titleXPos, (int) (YPos1 + d / 2 + 5));

			g2d.drawString(name2, (int) titleXPos, (int) (YPos2 + d / 2 + 5));

			g2d.drawString(name3, (int) titleXPos, (int) (YPos3 + d / 2 + 5));

		}
	}

	private void notDifferentSize(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int XPos, int YPos) {
		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g2d.setComposite(c);

		List<Ellipse2D.Double> circleLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingCircleLocations();
		circleLocations.clear();

		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		List<Color> colors = parameterModel.getColors();

		double sideLength = (2 - intersectionLength) * r;
		double temp = 2 * Math.sqrt(3);
		double XPos1 = XPos - r;
		double YPos1 = YPos - r - sideLength * 2 / temp;
		// Assume x, y, and diameter are instance variables.
		Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r, 2 * r);
		g2d.setColor(colors.get(0));
		g2d.fill(circle);
		circleLocations.add(circle);
		if (nameSelections.contains(new NameSelection(0))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle);
			g2d.setStroke(oldStroke);
		}

		double XPos2 = XPos - sideLength / 2 - r;
		double YPos2 = YPos + sideLength / temp - r;
		Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos2, 2 * r, 2 * r);
		g2d.setColor(colors.get(1));
		g2d.fill(circle1);
		circleLocations.add(circle1);
		if (nameSelections.contains(new NameSelection(1))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle1);
			g2d.setStroke(oldStroke);
		}

		double XPos3 = XPos + sideLength / 2 - r;
		double YPos3 = YPos + sideLength / temp - r;
		Ellipse2D.Double circle2 = new Ellipse2D.Double(XPos3, YPos3, 2 * r, 2 * r);
		g2d.setColor(colors.get(2));
		g2d.fill(circle2);
		circleLocations.add(circle2);
		if (nameSelections.contains(new NameSelection(2))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle2);
			g2d.setStroke(oldStroke);
		}
		if (parameterModel.isShowSetValues()) {
			paintCount(g2d, parameterModel, setItems, adjustAndCalculate, XPos1, YPos1, XPos2, YPos2, XPos3, YPos3);

		}
		if (parameterModel.isShowSetLegend()) {
			// paintName(g2d, setItems, XPos1, YPos1, XPos2, YPos2, XPos3, YPos3);
			paintName(g2d, parameterModel, setItems, XPos);

		}

	}

	private void paintCount(Graphics2D g2d, ClassicalParameterModel parameterModel, SetItem[] setItems,
			AdjustAndCalculate adjustAndCalculate, double XPos1, double YPos1, double XPos2, double YPos2, double XPos3,
			double YPos3) {
		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);
		g2d.setColor(Color.black);
		g2d.setFont(parameterModel.getIntersectionValueFont());
		// draw count
		Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0 }, setItems);
		Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0 }, setItems);
		Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1 }, setItems);
		Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1 }, setItems);

		FontMetrics fontMetrics = g2d.getFontMetrics();

		g2d.drawString("" + first1.length, (float) (XPos1 + r - fontMetrics.charWidth(first1.length) / 2),
				(float) (YPos1 + r / 2));

		g2d.drawString("" + first2.length, (float) (XPos1 + r / 2 - 20 - fontMetrics.charWidth(first2.length) / 2),
				(float) (YPos1 + r + r / 4));

		g2d.drawString("" + first3.length, (float) (XPos1 + r - fontMetrics.charWidth(first3.length) / 2),
				(float) (YPos1 + r + r / 2));

		g2d.drawString("" + first4.length, (float) (XPos1 + r + r / 2 + 20 - fontMetrics.charWidth(first4.length) / 2),
				(float) (YPos1 + r + r / 4));

		Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0 }, setItems);

		Element[] second2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1 }, setItems);

		double sqrt = r / (2 * Math.sqrt(2));

		g2d.drawString("" + second1.length, (float) (XPos2 + r - sqrt - fontMetrics.charWidth(second1.length) / 2),
				(float) (YPos2 + r + sqrt));

		g2d.drawString("" + second2.length, (float) (XPos2 + r + r / 2 - fontMetrics.charWidth(second2.length) / 2),
				(float) (YPos2 + r + r / 2));

		Element[] three1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1 }, setItems);

		g2d.drawString("" + three1.length, (float) (XPos3 + r + sqrt - fontMetrics.charWidth(three1.length) / 2),
				(float) (YPos3 + r + sqrt));
	}

	private void paintName(Graphics2D g2d, ClassicalParameterModel parameterModel, SetItem[] setItems, int XPos) {

		List<Rectangle2D.Double> paintingLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingNameLocations();
		paintingLocations.clear();
		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);

		g2d.setComposite(c);

		g2d.setFont(parameterModel.getNameFont());

		List<Color> colors = parameterModel.getColors();

		FontMetrics fontMetrics = g2d.getFontMetrics();

		int d = fontMetrics.getHeight();

		String name1 = setItems[0].getName();

		String name2 = setItems[1].getName();

		String name3 = setItems[2].getName();

		int stringWidth1 = fontMetrics.stringWidth(name1);

		int stringWidth2 = fontMetrics.stringWidth(name2);

		int stringWidth3 = fontMetrics.stringWidth(name3);

		int maxValue = stringWidth1 > stringWidth2 ? stringWidth1 : stringWidth2;

		maxValue = maxValue > stringWidth3 ? maxValue : stringWidth3;

		double titleXPos = XPos + XPos - maxValue - 100;

		double YPos1 = 20;

		double YPos2 = YPos1 + d + 5;

		double YPos3 = YPos2 + d + 5;

		Ellipse2D.Double titleCircle1 = new Ellipse2D.Double(titleXPos - d - 5, YPos1, d, d);
		g2d.setColor(colors.get(0));
		g2d.fill(titleCircle1);
		if (nameSelections.contains(new NameSelection(0))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos1, fontMetrics.stringWidth(name1) + d + 10, d));
			g2d.setStroke(oldStroke);
		}
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos1, d + stringWidth1, d));

		Ellipse2D.Double titleCircle2 = new Ellipse2D.Double(titleXPos - d - 5, YPos2, d, d);
		g2d.setColor(colors.get(1));
		g2d.fill(titleCircle2);
		if (nameSelections.contains(new NameSelection(1))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos2, fontMetrics.stringWidth(name2) + d + 10, d));
			g2d.setStroke(oldStroke);
		}
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos2, d + stringWidth2, d));

		Ellipse2D.Double titleCircle3 = new Ellipse2D.Double(titleXPos - d - 5, YPos3, d, d);
		g2d.setColor(colors.get(2));
		g2d.fill(titleCircle3);
		if (nameSelections.contains(new NameSelection(2))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos3, fontMetrics.stringWidth(name3) + d + 10, d));
			g2d.setStroke(oldStroke);
		}
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos3, d + stringWidth3, d));

		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);
		g2d.setColor(Color.black);
		g2d.drawString(name1, (int) titleXPos, (int) (YPos1 + d / 2 + 5));
		g2d.drawString(name2, (int) titleXPos, (int) (YPos2 + d / 2 + 5));
		g2d.drawString(name3, (int) titleXPos, (int) (YPos3 + d / 2 + 5));

	}

//	
//	private void paintName(Graphics2D g2d,List<Color> colors, SetItem[] setItems, double XPos1, double YPos1, double XPos2, double YPos2,
//			private void paintName(Graphics2D g2d,List<Color> colors, SetItem[] setItems, double XPos1, double YPos1, double XPos2, double YPos2,
//					double XPos3, double YPos3) {
//		// draw name
//		FontMetrics fontMetrics = g2d.getFontMetrics();
//		
//		String name1 = setItems[0].getName();
//		
//		int stringWidth1 = fontMetrics.stringWidth(name1);
//		
//		g2d.drawString(name1, (int) (XPos1 + r - stringWidth1 / 2), (int) (YPos1 - 10));
//		
//		String name2 = setItems[1].getName();
//		
//		int stringWidth2 = fontMetrics.stringWidth(name2);
//		
//		g2d.drawString(name2, (int) (XPos2 + r - stringWidth2 / 2), (int) (YPos2 + 2 * r + 15));
//		
//		String name3 = setItems[2].getName();
//		
//		int stringWidth3 = fontMetrics.stringWidth(name3);
//		
//		g2d.drawString(name3, (int) (XPos3 + r - stringWidth3 / 2), (int) (YPos3 + 2 * r + 15));
//	}

}
