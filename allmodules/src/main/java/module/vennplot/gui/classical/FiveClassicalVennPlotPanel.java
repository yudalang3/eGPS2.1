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

public class FiveClassicalVennPlotPanel extends ClassicalVennPlotPanel {

	@Override
	public void paint(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int XPos, int YPos) {

		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g2d.setComposite(c);

		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		double XPos1 = XPos - r / 4;
		double YPos1 = YPos - r;
		g2d.rotate(Math.toRadians(-70), XPos, YPos);
		// Assume x, y, and diameter are instance variables.
		Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos1, YPos1, r, 2 * r);
		List<Color> colors = parameterModel.getColors();
		g2d.setColor(colors.get(0));
		g2d.fill(circle1);

		if (nameSelections.contains(new NameSelection(0))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle1);
			g2d.setStroke(oldStroke);
		}
		g2d.rotate(Math.toRadians(70), XPos, YPos);

		double XPos2 = XPos - r / 4;
		double YPos2 = YPos - r - r / 2;
		Ellipse2D.Double circle2 = new Ellipse2D.Double(XPos2, YPos2, r, 2 * r);
		g2d.setColor(colors.get(1));
		g2d.fill(circle2);
		if (nameSelections.contains(new NameSelection(1))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle2);
			g2d.setStroke(oldStroke);
		}

		g2d.rotate(Math.toRadians(75), XPos, YPos);
		double XPos3 = XPos - r + r / 3;
		double YPos3 = YPos - r - r / 10 * 7;
		Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos3, r, 2 * r);
		g2d.setColor(colors.get(2));
		g2d.fill(circle3);
		if (nameSelections.contains(new NameSelection(2))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle3);
			g2d.setStroke(oldStroke);
		}

		g2d.rotate(Math.toRadians(-75), XPos, YPos);

		g2d.rotate(Math.toRadians(40), XPos, YPos);
		double XPos4 = XPos - r / 4;
		double YPos4 = YPos - r - r / 10;
		Ellipse2D.Double circle4 = new Ellipse2D.Double(XPos4, YPos4, r, 2 * r);
		g2d.setColor(colors.get(3));
		g2d.fill(circle4);
		if (nameSelections.contains(new NameSelection(3))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle4);
			g2d.setStroke(oldStroke);
		}

		g2d.rotate(Math.toRadians(-40), XPos, YPos);

		g2d.rotate(Math.toRadians(-40), XPos, YPos);
		double XPos5 = XPos;
		double YPos5 = YPos - r + r / 3;
		Ellipse2D.Double circle5 = new Ellipse2D.Double(XPos5, YPos5, r, 2 * r);
		g2d.setColor(colors.get(4));
		g2d.fill(circle5);
		if (nameSelections.contains(new NameSelection(4))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle5);
			g2d.setStroke(oldStroke);
		}

		g2d.rotate(Math.toRadians(40), XPos, YPos);

		// paintName(g2d, colors, setItems, XPos, XPos1, YPos1, XPos2, YPos2, XPos3,
		// YPos3, XPos4, YPos4, YPos5);
		if (parameterModel.isShowSetValues()) {
			paintCount(g2d, parameterModel, setItems, adjustAndCalculate, XPos1, YPos1, XPos2, YPos2, XPos3, YPos3,
					XPos4, YPos4, XPos5, YPos5);
		}
		if (parameterModel.isShowSetLegend()) {
			paintName(g2d, parameterModel, setItems, XPos);
		}
	}

	private void paintName(Graphics2D g2d, List<Color> colors, SetItem[] setItems, int XPos, double XPos1, double YPos1,
			double XPos2, double YPos2, double XPos3, double YPos3, double XPos4, double YPos4, double YPos5) {
		FontMetrics fontMetrics = g2d.getFontMetrics();

		String name1 = setItems[0].getName();
		g2d.setColor(colors.get(0));
		g2d.drawString(name1, (int) XPos1 - r / 2 - 30 - fontMetrics.stringWidth(name1), (int) YPos1 + 90);

		String name2 = setItems[1].getName();
		g2d.setColor(colors.get(1));
		g2d.drawString(name2, (int) XPos2 - fontMetrics.stringWidth(name2) + r / 2 + 20, (int) YPos2 - 10);

		String name3 = setItems[2].getName();
		g2d.setColor(colors.get(2));
		g2d.drawString(name3, (int) XPos3 + r * 2 + 50, (int) YPos3 + r);

		String name4 = setItems[3].getName();
		g2d.setColor(colors.get(3));
		g2d.drawString(name4, (int) XPos4 - r / 2 + 50, (int) YPos4 + r * 2 + 20);

		String name5 = setItems[4].getName();
		g2d.setColor(colors.get(4));
		g2d.drawString(name5, (int) XPos + r + 30, (int) YPos5 + r * 2 - 100);
	}

	private void paintCount(Graphics2D g2d, ClassicalParameterModel parameterModel, SetItem[] setItems,
			AdjustAndCalculate adjustAndCalculate, double XPos1, double YPos1, double XPos2, double YPos2, double XPos3,
			double YPos3, double XPos4, double YPos4, double XPos5, double YPos5) {

		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);

		g2d.setColor(Color.BLACK);

		g2d.setFont(parameterModel.getIntersectionValueFont());

		Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0, 0, 0 }, setItems);
		Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1, 0, 0 }, setItems);
		Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1, 1, 0 }, setItems);
		Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0, 1, 0 }, setItems);
		Element[] first5 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0, 0, 0 }, setItems);
		Element[] first6 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1, 0, 0 }, setItems);
		Element[] first7 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1, 1, 0 }, setItems);
		Element[] first8 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0, 1, 0 }, setItems);
		Element[] first9 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0, 0, 1 }, setItems);
		Element[] first10 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1, 0, 1 },
				setItems);
		Element[] first11 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1, 1, 1 },
				setItems);
		Element[] first12 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0, 1, 1 },
				setItems);
		Element[] first13 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1, 1, 1 },
				setItems);
		Element[] first14 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0, 1, 1 },
				setItems);
		Element[] first15 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1, 0, 1 },
				setItems);
		Element[] first16 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0, 0, 1 },
				setItems);

		FontMetrics fontMetrics = g2d.getFontMetrics();

		g2d.drawString("" + first1.length, (float) (XPos1 - r / 3 - fontMetrics.charWidth(first1.length) / 2),
				(float) (YPos1 + r / 2 + 10));

		g2d.drawString("" + first2.length, (float) (XPos1 - r / 20 - fontMetrics.charWidth(first2.length) / 2),
				(float) (YPos1 + r / 15 * 13));

		g2d.drawString("" + first3.length, (float) (XPos1 - fontMetrics.charWidth(first3.length) / 2),
				(float) (YPos1 + r));

		g2d.drawString("" + first4.length, (float) (XPos1 - fontMetrics.charWidth(first4.length) / 2),
				(float) (YPos1 + r + 29));

		g2d.drawString("" + first5.length, (float) (XPos1 + r / 15 - 7 - fontMetrics.charWidth(first5.length) / 2),
				(float) (YPos1 + r / 3 + 10));

		g2d.drawString("" + first6.length, (float) (XPos1 + r / 25 - fontMetrics.charWidth(first6.length) / 2),
				(float) (YPos1 + r / 3 * 2 + 10));

		g2d.drawString("" + first7.length, (float) (XPos1 + r / 7 - fontMetrics.charWidth(first7.length) / 2),
				(float) (YPos1 + r));

		g2d.drawString("" + first8.length, (float) (XPos1 + r / 4 + 5 - fontMetrics.charWidth(first8.length) / 2),
				(float) (YPos1 + r + 50));

		g2d.drawString("" + first9.length, (float) (XPos1 + r / 5 - fontMetrics.charWidth(first9.length) / 2),
				(float) (YPos1 + r / 3 + 6));

		g2d.drawString("" + first10.length, (float) (XPos1 + r / 5 + 15 - fontMetrics.charWidth(first10.length) / 2),
				(float) (YPos1 + r / 2));

		g2d.drawString("" + first11.length, (float) (XPos1 + r / 2 + 15 - fontMetrics.charWidth(first11.length) / 2),
				(float) (YPos1 + r - 40));

		g2d.drawString("" + first12.length, (float) (XPos1 + r / 2 + 20 - fontMetrics.charWidth(first12.length) / 2),
				(float) (YPos1 + r + 55));

		g2d.drawString("" + first13.length, (float) (XPos1 + r + 9 - fontMetrics.charWidth(first13.length) / 2),
				(float) (YPos1 + r - 20));

		g2d.drawString("" + first14.length, (float) (XPos1 + r - 20 - fontMetrics.charWidth(first14.length) / 2),
				(float) (YPos1 + r + 50));

		g2d.drawString("" + first15.length, (float) (XPos1 + r + r / 5 - fontMetrics.charWidth(first15.length) / 2),
				(float) (YPos1 + r));

		g2d.drawString("" + first16.length, (float) (XPos1 + r + r / 8 - fontMetrics.charWidth(first16.length) / 2),
				(float) (YPos1 + r + 40));

		Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0, 0, 0 },
				setItems);
		Element[] second2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0, 0, 1 },
				setItems);
		Element[] second3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1, 0, 1 },
				setItems);
		Element[] second4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1, 0, 0 },
				setItems);
		Element[] second5 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1, 1, 1 },
				setItems);
		Element[] second6 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1, 1, 0 },
				setItems);
		Element[] second7 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0, 1, 1 },
				setItems);
		Element[] second8 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0, 1, 0 },
				setItems);

		g2d.drawString("" + second1.length, (float) (XPos2 + r / 2 - fontMetrics.charWidth(second1.length) / 2),
				(float) (YPos2 + r / 3 + 25));

		g2d.drawString("" + second2.length, (float) (XPos2 + r / 2 - 20 - fontMetrics.charWidth(second2.length) / 2),
				(float) (YPos2 + r / 2 + 40));

		g2d.drawString("" + second3.length, (float) (XPos2 + r / 2 + 20 - fontMetrics.charWidth(second3.length) / 2),
				(float) (YPos2 + r / 2 + 60));

		g2d.drawString("" + second4.length, (float) (XPos2 + r / 2 + 70 - fontMetrics.charWidth(second4.length) / 2),
				(float) (YPos2 + r / 2 + 45));

		g2d.drawString("" + second5.length, (float) (XPos2 + r / 2 + 70 - fontMetrics.charWidth(second5.length) / 2),
				(float) (YPos2 + r - 10));

		g2d.drawString("" + second6.length, (float) (XPos2 + r - 20 - fontMetrics.charWidth(second6.length) / 2),
				(float) (YPos2 + r - 35));

		g2d.drawString("" + second7.length, (float) (XPos2 + r / 2 + 25 - fontMetrics.charWidth(second7.length) / 2),
				(float) (YPos2 + r * 2 - 20));

		g2d.drawString("" + second8.length, (float) (XPos2 + r / 2 - 20 - fontMetrics.charWidth(second8.length) / 2),
				(float) (YPos2 + r * 2 - 10));

		Element[] three1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1, 0, 0 }, setItems);
		Element[] three2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1, 1, 0 }, setItems);
		Element[] three3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1, 1, 1 }, setItems);
		Element[] three4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1, 0, 1 }, setItems);

		g2d.drawString("" + three1.length, (float) (XPos3 + r * 2 - 30 - fontMetrics.charWidth(three1.length) / 2),
				(float) (YPos3 + r + 40));

		g2d.drawString("" + three2.length, (float) (XPos3 + r + r / 2 - fontMetrics.charWidth(three2.length) / 2),
				(float) (YPos3 + r + 25));

		g2d.drawString("" + three3.length, (float) (XPos3 + r + r / 2 - 5 - fontMetrics.charWidth(three3.length) / 2),
				(float) (YPos3 + r + 55));

		g2d.drawString("" + three4.length, (float) (XPos3 + r * 2 - 55 - fontMetrics.charWidth(three4.length) / 2),
				(float) (YPos3 + r + r / 2 + 20));

		Element[] fourth1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 0, 1, 0 },
				setItems);
		Element[] fourth2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 0, 1, 1 },
				setItems);

		g2d.drawString("" + fourth1.length, (float) (XPos4 + 20 - fontMetrics.charWidth(fourth1.length) / 2),
				(float) (YPos4 + r * 2 - 60));

		g2d.drawString("" + fourth2.length, (float) (XPos4 + r - 50 - fontMetrics.charWidth(fourth2.length) / 2),
				(float) (YPos4 + r * 2 - 90));

		Element[] five1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 0, 0, 1 }, setItems);

		g2d.drawString("" + five1.length, (float) (XPos5 + r - fontMetrics.charWidth(fourth2.length) / 2),
				(float) (YPos5 + r + 20));
	}

	private void paintName(Graphics2D g2d, ClassicalParameterModel parameterModel, SetItem[] setItems, int XPos) {

		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);

		g2d.setComposite(c);

		g2d.setFont(parameterModel.getNameFont());

		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		List<Rectangle2D.Double> paintingLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingNameLocations();
		paintingLocations.clear();

		List<Color> colors = parameterModel.getColors();

		FontMetrics fontMetrics = g2d.getFontMetrics();

		int d = fontMetrics.getHeight();

		String name1 = setItems[0].getName();

		String name2 = setItems[1].getName();

		String name3 = setItems[2].getName();

		String name4 = setItems[3].getName();

		String name5 = setItems[4].getName();

		int stringWidth1 = fontMetrics.stringWidth(name1);

		int stringWidth2 = fontMetrics.stringWidth(name2);

		int stringWidth3 = fontMetrics.stringWidth(name3);

		int stringWidth4 = fontMetrics.stringWidth(name4);

		int stringWidth5 = fontMetrics.stringWidth(name5);

		int maxValue = stringWidth1 > stringWidth2 ? stringWidth1 : stringWidth2;

		maxValue = maxValue > stringWidth3 ? maxValue : stringWidth3;

		maxValue = maxValue > stringWidth4 ? maxValue : stringWidth4;

		maxValue = maxValue > stringWidth5 ? maxValue : stringWidth5;

		double titleXPos = XPos + XPos - maxValue - 100;

		double YPos1 = 20;

		double YPos2 = YPos1 + d + 5;

		double YPos3 = YPos2 + d + 5;

		double YPos4 = YPos3 + d + 5;

		double YPos5 = YPos4 + d + 5;

		Ellipse2D.Double titleCircle1 = new Ellipse2D.Double(titleXPos - d - 5, YPos1, d, d);
		g2d.setColor(colors.get(0));
		g2d.fill(titleCircle1);
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos1, d + stringWidth1, d));

		Ellipse2D.Double titleCircle2 = new Ellipse2D.Double(titleXPos - d - 5, YPos2, d, d);
		g2d.setColor(colors.get(1));
		g2d.fill(titleCircle2);
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos2, d + stringWidth2, d));

		Ellipse2D.Double titleCircle3 = new Ellipse2D.Double(titleXPos - d - 5, YPos3, d, d);
		g2d.setColor(colors.get(2));
		g2d.fill(titleCircle3);
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos3, d + stringWidth3, d));

		Ellipse2D.Double titleCircle4 = new Ellipse2D.Double(titleXPos - d - 5, YPos4, d, d);
		g2d.setColor(colors.get(3));
		g2d.fill(titleCircle4);
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos4, d + stringWidth4, d));

		Ellipse2D.Double titleCircle5 = new Ellipse2D.Double(titleXPos - d - 5, YPos5, d, d);
		g2d.setColor(colors.get(4));
		g2d.fill(titleCircle5);
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos5, d + stringWidth5, d));

		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);
		g2d.setColor(Color.black);

		g2d.drawString(name1, (int) titleXPos, (int) (YPos1 + d / 2 + 5));

		g2d.drawString(name2, (int) titleXPos, (int) (YPos2 + d / 2 + 5));

		g2d.drawString(name3, (int) titleXPos, (int) (YPos3 + d / 2 + 5));

		g2d.drawString(name4, (int) titleXPos, (int) (YPos4 + d / 2 + 5));

		g2d.drawString(name5, (int) titleXPos, (int) (YPos5 + d / 2 + 5));

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

		if (nameSelections.contains(new NameSelection(3))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos4, fontMetrics.stringWidth(name4) + d + 10, d));
			g2d.setStroke(oldStroke);
		}

		if (nameSelections.contains(new NameSelection(4))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos5, fontMetrics.stringWidth(name5) + d + 10, d));
			g2d.setStroke(oldStroke);
		}

	}

}
