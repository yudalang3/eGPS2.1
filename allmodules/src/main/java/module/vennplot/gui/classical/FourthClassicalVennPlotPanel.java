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

public class FourthClassicalVennPlotPanel extends ClassicalVennPlotPanel {

	@Override
	public void paint(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int XPos, int YPos) {
		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		List<Color> colors = parameterModel.getColors();

		double XPos1 = XPos - r - r / 4;

		double YPos1 = YPos - r;

		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g2d.setComposite(c);

		int ratio = 45;

		// g2d.rotate(Math.toRadians(-45), XPos, YPos);
		// g2d.rotate(Math.toRadians(-45), XPos - r / 2, YPos);
		g2d.rotate(Math.toRadians(-ratio), XPos, YPos);
		// Assume x, y, and diameter are instance variables.
		Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos1, YPos1, r, 2 * r);
		g2d.setColor(colors.get(0));
		g2d.fill(circle1);

		if (nameSelections.contains(new NameSelection(0))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle1);
			g2d.setStroke(oldStroke);
		}

		double XPos2 = XPos - r / 2 - r / 4;
		Ellipse2D.Double circle2 = new Ellipse2D.Double(XPos2, YPos1 + r / 80 * 23, r, 2 * r);
		g2d.setColor(colors.get(1));
		g2d.fill(circle2);
		if (nameSelections.contains(new NameSelection(1))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle2);
			g2d.setStroke(oldStroke);
		}
		g2d.rotate(Math.toRadians(ratio), XPos, YPos);

		g2d.rotate(Math.toRadians(ratio), XPos, YPos);
		double XPos3 = XPos - r / 4;

		Ellipse2D.Double circle3 = new Ellipse2D.Double(XPos3, YPos1 + r / 80 * 23, r, 2 * r);
		g2d.setColor(colors.get(2));
		g2d.fill(circle3);
		if (nameSelections.contains(new NameSelection(2))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle3);
			g2d.setStroke(oldStroke);
		}

		double XPos4 = XPos + r / 4;
		Ellipse2D.Double circle4 = new Ellipse2D.Double(XPos4, YPos1, r, 2 * r);
		g2d.setColor(colors.get(3));
		g2d.fill(circle4);
		if (nameSelections.contains(new NameSelection(3))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle4);
			g2d.setStroke(oldStroke);
		}
		g2d.rotate(Math.toRadians(-ratio), XPos, YPos);

		// paintName(g2d, colors, setItems, XPos1, YPos1, XPos2, XPos3, XPos4);
		if (parameterModel.isShowSetValues()) {
			paintCount(g2d, parameterModel, setItems, adjustAndCalculate, XPos1, YPos1);
		}
		if (parameterModel.isShowSetLegend()) {
			paintName(g2d, parameterModel, setItems, XPos);
		}
	}

	private void paintCount(Graphics2D g2d, ClassicalParameterModel parameterModel, SetItem[] setItems,
			AdjustAndCalculate adjustAndCalculate, double XPos1, double YPos1) {

		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);
		g2d.setColor(Color.black);

		g2d.setFont(parameterModel.getIntersectionValueFont());

		// draw count
		Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0, 0 }, setItems);
		Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0, 0 }, setItems);
		Element[] first3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1, 0 }, setItems);
		Element[] first4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1, 0 }, setItems);
		Element[] first5 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 1, 1 }, setItems);
		Element[] first6 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 1, 1 }, setItems);
		Element[] first7 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1, 0, 1 }, setItems);
		Element[] first8 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0, 0, 1 }, setItems);
		FontMetrics fontMetrics = g2d.getFontMetrics();

		g2d.drawString("" + first1.length, (float) (XPos1 + r / 4 - fontMetrics.charWidth(first1.length) / 2),
				(float) (YPos1 + r + r / 4));

		g2d.drawString("" + first2.length, (float) (XPos1 + r / 3 * 2 - 10 - fontMetrics.charWidth(first2.length) / 2),
				(float) (YPos1 + r + r / 10));

		g2d.drawString("" + first3.length, (float) (XPos1 + r / 3 * 2 + 5 - fontMetrics.charWidth(first3.length) / 2),
				(float) (YPos1 + r + r / 7 * 6));

		g2d.drawString("" + first4.length, (float) (XPos1 + r / 10 * 9 - fontMetrics.charWidth(first4.length) / 2),
				(float) (YPos1 + r + r / 2));

		g2d.drawString("" + first5.length,
				(float) (XPos1 + r + r / 9 * 2 - 5 - fontMetrics.charWidth(first5.length) / 2),
				(float) (YPos1 + r + r / 6 * 5));

		g2d.drawString("" + first6.length, (float) (XPos1 + r + r / 15 - fontMetrics.charWidth(first6.length) / 2),
				(float) (YPos1 + r + r));

		g2d.drawString("" + first7.length, (float) (XPos1 + r + r / 8 * 3 - fontMetrics.charWidth(first7.length) / 2),
				(float) (YPos1 + r + r));

		g2d.drawString("" + first8.length, (float) (XPos1 + r + r / 9 * 2 - fontMetrics.charWidth(first8.length) / 2),
				(float) (YPos1 + r + r + r / 6));

		Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0, 0 }, setItems);

		Element[] second2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1, 0 }, setItems);

		Element[] second3 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 1, 1 }, setItems);

		Element[] second4 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1, 0, 1 }, setItems);

		g2d.drawString("" + second1.length, (float) (XPos1 + r / 8 * 7 - 5 - fontMetrics.charWidth(second1.length) / 2),
				(float) (YPos1 + r / 5 * 4));

		g2d.drawString("" + second2.length, (float) (XPos1 + r + r / 4 - 5 - fontMetrics.charWidth(second2.length) / 2),
				(float) (YPos1 + r + r / 10 - 3));

		g2d.drawString("" + second3.length, (float) (XPos1 + r + r / 2 - fontMetrics.charWidth(second3.length) / 2),
				(float) (YPos1 + r + r / 2 - 10));

		g2d.drawString("" + second4.length,
				(float) (XPos1 + r + r / 4 * 3 - 5 - fontMetrics.charWidth(second4.length) / 2),
				(float) (YPos1 + r + r / 10 * 9));

		Element[] three1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1, 0 }, setItems);

		Element[] three2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 1, 1 }, setItems);

		g2d.drawString("" + three1.length, (float) (XPos1 + r + r / 3 * 2 - fontMetrics.charWidth(three1.length) / 2),
				(float) (YPos1 + r / 5 * 4));

		g2d.drawString("" + three2.length, (float) (XPos1 + r + r / 6 * 5 - fontMetrics.charWidth(three2.length) / 2),
				(float) (YPos1 + r + r / 10 - 5));

		Element[] fourth1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 0, 0, 1 }, setItems);

		g2d.drawString("" + fourth1.length, (float) (XPos1 + r + r + r / 6 - fontMetrics.charWidth(fourth1.length) / 2),
				(float) (YPos1 + r + r / 3));
	}

	private void paintName(Graphics2D g2d, List<Color> colors, SetItem[] setItems, double XPos1, double YPos1,
			double XPos2, double XPos3, double XPos4) {
		// draw name
		// g2d.rotate(Math.toRadians(-ratio), XPos, YPos);
		g2d.setColor(colors.get(0));
		g2d.drawString(setItems[0].getName(), (int) XPos1, (int) YPos1 + r / 3 * 2);
		g2d.setColor(colors.get(1));
		g2d.drawString(setItems[1].getName(), (int) XPos2, (int) YPos1 + r / 2);

		// g2d.rotate(Math.toRadians(ratio), XPos, YPos);

		// g2d.rotate(Math.toRadians(ratio), XPos, YPos);
		g2d.setColor(colors.get(2));
		g2d.drawString(setItems[2].getName(), (int) XPos3 + r / 2, (int) YPos1 + r / 2);
		g2d.setColor(colors.get(3));
		g2d.drawString(setItems[3].getName(), (int) XPos4 + r / 3 * 2, (int) YPos1 + r / 3 * 2);

		// g2d.rotate(Math.toRadians(-ratio), XPos, YPos);
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

		int stringWidth1 = fontMetrics.stringWidth(name1);

		int stringWidth2 = fontMetrics.stringWidth(name2);

		int stringWidth3 = fontMetrics.stringWidth(name3);

		int stringWidth4 = fontMetrics.stringWidth(name4);

		int maxValue = stringWidth1 > stringWidth2 ? stringWidth1 : stringWidth2;

		maxValue = maxValue > stringWidth3 ? maxValue : stringWidth3;

		maxValue = maxValue > stringWidth4 ? maxValue : stringWidth4;

		double titleXPos = XPos + XPos - maxValue - 100;

		double YPos1 = 20;

		double YPos2 = YPos1 + d + 5;

		double YPos3 = YPos2 + d + 5;

		double YPos4 = YPos3 + d + 5;

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

		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);
		g2d.setColor(Color.black);
		g2d.drawString(name1, (int) titleXPos, (int) (YPos1 + d / 2 + 5));
		g2d.drawString(name2, (int) titleXPos, (int) (YPos2 + d / 2 + 5));
		g2d.drawString(name3, (int) titleXPos, (int) (YPos3 + d / 2 + 5));
		g2d.drawString(name4, (int) titleXPos, (int) (YPos4 + d / 2 + 5));

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

	}

}
