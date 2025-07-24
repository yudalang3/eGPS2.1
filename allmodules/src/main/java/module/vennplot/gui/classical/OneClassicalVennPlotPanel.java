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

public class OneClassicalVennPlotPanel extends ClassicalVennPlotPanel {

	@Override
	public void paint(Graphics2D g2d, SetItem[] setItems, ClassicalParameterModel parameterModel,
			AdjustAndCalculate adjustAndCalculate, int XPos, int YPos) {
		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g2d.setComposite(c);

		List<Ellipse2D.Double> circleLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingCircleLocations();
		circleLocations.clear();

		// Assume x, y, and diameter are instance variables.
		double XPos1 = XPos - r;
		double YPos1 = YPos - r;
		List<Color> colors = parameterModel.getColors();
		Ellipse2D.Double circle = new Ellipse2D.Double(XPos1, YPos1, 2 * r, 2 * r);
		g2d.setColor(colors.get(0));
		g2d.fill(circle);
		circleLocations.add(circle);

		List<NameSelection> nameSelections = parameterModel.getNameSelections();
		if (nameSelections.contains(new NameSelection(0))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(circle);
			g2d.setStroke(oldStroke);
		}
		if (parameterModel.isShowSetValues()) {
			// draw count
			Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
			g2d.setComposite(c1);
			g2d.setFont(parameterModel.getIntersectionValueFont());
			FontMetrics fontMetrics = g2d.getFontMetrics();
			Element[] first1 = adjustAndCalculate.getSetAfterIntersectAndComplement(new byte[] { 1 }, setItems);
			g2d.setColor(Color.black);
			g2d.drawString("" + first1.length, (float) (XPos1 + r - fontMetrics.charWidth(first1.length) / 2),
					(float) YPos);
		}
		if (parameterModel.isShowSetLegend()) {
			// draw name
			paintName(g2d, parameterModel, setItems, XPos);
		}

	}

	private void paintName(Graphics2D g2d, ClassicalParameterModel parameterModel, SetItem[] setItems, int XPos) {
		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g2d.setComposite(c);

		g2d.setFont(parameterModel.getNameFont());

		List<Color> colors = parameterModel.getColors();

		List<Rectangle2D.Double> paintingLocations = parameterModel.getClassicalPaintingLocations()
				.getPaintingNameLocations();
		paintingLocations.clear();

		FontMetrics fontMetrics = g2d.getFontMetrics();

		int d = fontMetrics.getHeight();

		String name = setItems[0].getName();
		int stringWidth = fontMetrics.stringWidth(name);
		double titleXPos = XPos + XPos - stringWidth - 100;
		double YPos = 60;

		Ellipse2D.Double titleCircle1 = new Ellipse2D.Double(titleXPos - d - 5, YPos, d, d);
		g2d.setColor(colors.get(0));
		g2d.fill(titleCircle1);

		Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2d.setComposite(c1);
		g2d.setColor(Color.black);
		g2d.drawString(name, (int) titleXPos, (int) (YPos + d / 2 + 5));

		paintingLocations.add(new Rectangle2D.Double(titleXPos - d, YPos, d + stringWidth, d));

		List<NameSelection> nameSelections = parameterModel.getNameSelections();
		if (nameSelections.contains(new NameSelection(0))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(new Rectangle2D.Double(titleXPos - d - 9, YPos, fontMetrics.stringWidth(name) + d + 10, d));
			g2d.setStroke(oldStroke);
		}

	}

}
