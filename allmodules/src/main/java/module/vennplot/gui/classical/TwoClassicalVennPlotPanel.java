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

public class TwoClassicalVennPlotPanel extends ClassicalVennPlotPanel {

	private boolean isDifferentSize = true;

	public boolean isDifferentSize() {
		return isDifferentSize;
	}

	public void setDifferentSize(boolean isDifferentSize) {
		this.isDifferentSize = isDifferentSize;
	}

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

		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		List<Color> colors = parameterModel.getColors();

		int maxCount = getMaxCount(setItems);

		double count1 = setItems[0].getSetLists().length * 1.0;

		double count2 = setItems[1].getSetLists().length * 1.0;

		double percentage1 = count1 / maxCount;

		double percentage2 = count2 / maxCount;

		double r1 = r * percentage1;

		double r2 = r * percentage2;

		Element[] coincide = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1 }, setItems);

		double coincideCount = coincide.length * 1.0;

		double percentageCount1 = coincideCount / count1;

		double percentageCount2 = coincideCount / count2;

		double offset1 = r1 * (1 - percentageCount1);

		double offset2 = r2 * (1 - percentageCount2);

		// Assume x, y, and diameter are instance variables.
		double XPos1 = XPos - r1 - offset1;
		double YPos1 = YPos - r1;

		Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r1, 2 * r1);
		// circle.contains(x, y)
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

		double XPos2 = XPos - r2 + offset2;

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

		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);

		g2d.setColor(Color.BLACK);

		g2d.setFont(parameterModel.getIntersectionValueFont());

		if (parameterModel.isShowSetValues()) {

			// draw count
			FontMetrics fontMetrics = g2d.getFontMetrics();
			Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0 }, setItems);
			if (first1.length != 0) {
				// g2d.drawString("" + first1.length, (float) (XPos2 - offset1 / 2), (float)
				// YPos);
				g2d.drawString("" + first1.length, (float) (XPos2 - offset1 - fontMetrics.charWidth(first1.length) / 2),
						(float) YPos);
			}

			if (coincide.length != 0) {
				g2d.drawString("" + coincide.length, (float) XPos - fontMetrics.charWidth(coincide.length) / 2,
						(float) YPos);
			}

			Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1 }, setItems);

			if (second1.length != 0) {
				// g2d.drawString("" + second1.length, (float) (XPos1 + r1 * 2 + offset2 / 2),
				// (float) YPos);
				g2d.drawString("" + second1.length,
						(float) (XPos1 + r1 * 2 + offset2 - fontMetrics.charWidth(second1.length) / 2), (float) YPos);

			}
		}
		if (parameterModel.isShowSetLegend()) {
			paintName(g2d, parameterModel, setItems, XPos);
		}
	}

	private void notDifferentSize(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int XPos, int YPos) {
		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g2d.setComposite(c);
		g2d.setFont(parameterModel.getIntersectionValueFont());

		List<Ellipse2D.Double> circleLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingCircleLocations();
		circleLocations.clear();

		List<NameSelection> nameSelections = parameterModel.getNameSelections();

		List<Color> colors = parameterModel.getColors();
		// Assume x, y, and diameter are instance variables.
		double XPos1 = XPos - r - r / 2;
		double YPos1 = YPos - r;

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

		double XPos2 = XPos - r / 2;
		Ellipse2D.Double circle1 = new Ellipse2D.Double(XPos2, YPos1, 2 * r, 2 * r);
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
		if (parameterModel.isShowSetValues()) {
			paintCount(g2d, setItems, parameterModel, adjustAndCalculate, YPos, XPos1, XPos2);
		}
		if (parameterModel.isShowSetLegend()) {
			paintName(g2d, parameterModel, setItems, XPos);
		}
	}

	private void paintCount(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int YPos, double XPos1, double XPos2) {
		// draw count
		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);
		g2d.setColor(Color.BLACK);
		g2d.setFont(parameterModel.getIntersectionValueFont());
		FontMetrics fontMetrics = g2d.getFontMetrics();
		Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 0 }, setItems);
		Element[] first2 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1, 1 }, setItems);
		g2d.drawString("" + first1.length, (float) (XPos1 + r / 2 - fontMetrics.charWidth(first1.length) / 2),
				(float) YPos);

		g2d.drawString("" + first2.length, (float) (XPos1 + r + r / 2 - fontMetrics.charWidth(first2.length) / 2),
				(float) YPos);

		Element[] second1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 0, 1 }, setItems);

		g2d.drawString("" + second1.length, (float) (XPos2 + r + r / 2 - fontMetrics.charWidth(second1.length) / 2),
				(float) YPos);
	}

	private void paintName(Graphics2D g2d, SetItem[] setItems, double XPos1, double YPos1, double XPos2) {
		// draw name
		FontMetrics fontMetrics = g2d.getFontMetrics();
		String name = setItems[0].getName();

		int stringWidth = fontMetrics.stringWidth(name);

		g2d.drawString(name, (int) (XPos1 + r - stringWidth / 2), (int) (YPos1 - 10));

		String name2 = setItems[1].getName();

		int stringWidth2 = fontMetrics.stringWidth(name2);

		g2d.drawString(name2, (int) (XPos2 + r - stringWidth2 / 2), (int) (YPos1 - 10));
	}

	private void paintName(Graphics2D g2d, ClassicalParameterModel parameterModel, SetItem[] setItems, int XPos) {

		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);

		g2d.setComposite(c);

		g2d.setFont(parameterModel.getNameFont());

		List<Rectangle2D.Double> paintingLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingNameLocations();
		paintingLocations.clear();

		List<Color> colors = parameterModel.getColors();

		FontMetrics fontMetrics = g2d.getFontMetrics();

		int d = fontMetrics.getHeight();

		String name1 = setItems[0].getName();

		String name2 = setItems[1].getName();

		int stringWidth1 = fontMetrics.stringWidth(name1);

		int stringWidth2 = fontMetrics.stringWidth(name2);

		int maxValue = stringWidth1 > stringWidth2 ? stringWidth1 : stringWidth2;

		double titleXPos = XPos + XPos - maxValue - 100;

		double YPos1 = 60;

		double YPos2 = YPos1 + d + 5;

		Ellipse2D.Double titleCircle1 = new Ellipse2D.Double(titleXPos - d - 5, YPos1, d, d);
		g2d.setColor(colors.get(0));
		g2d.fill(titleCircle1);

		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos1, d + stringWidth1, d));

		Ellipse2D.Double titleCircle2 = new Ellipse2D.Double(titleXPos - d - 5, YPos2, d, d);
		g2d.setColor(colors.get(1));
		g2d.fill(titleCircle2);
		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos2, d + stringWidth2, d));

		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);
		g2d.setColor(Color.black);

		g2d.drawString(name1, (int) titleXPos, (int) (YPos1 + d / 2 + 5));

		g2d.drawString(name2, (int) titleXPos, (int) (YPos2 + d / 2 + 5));

		List<NameSelection> nameSelections = parameterModel.getNameSelections();

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

	}

}
