package module.vennplot.painter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import utils.EGPSFormatUtil;
import egps2.utils.common.util.IntervalUtil;
import module.vennplot.model.DataModel;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SingleRegionDataModel;
import module.vennplot.model.SingleRegionElement;

public class SingleRegionPainterAndSelectionJudger {

	List<SingleRegionSelection> selections = new ArrayList<>();

	private void commonIteration(PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel, SingleRegionTodo todo) {
		double x = paintingLocations.getSingleRegionTopleftX();
		double y = paintingLocations.getSingleRegionTopleftY();
		double w = paintingLocations.getSingleRegionWidth();
		// double h = paintingLocations.getSingleRegionHeight();

		double iterateHeight = paintingLocations.getBodyRegionElementHeight();
		int leftBarWidth = parameterModel.getLeftBarWidth();

		SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
		List<SingleRegionElement> eles = singleRegionDataModel.getEles();

		int numOfSets = dataModel.getNumOfSets();

		for (int i = 0; i < numOfSets; i++) {
			SingleRegionElement singleRegionElement = eles.get(i);
			double rectWidth = singleRegionElement.getRatio_this2higest() * w;
			double xx = x + w - rectWidth;

			double yy = i * iterateHeight + y + iterateHeight - leftBarWidth;

			todo.todo(i, singleRegionElement, new Rectangle2D.Double(xx, yy, rectWidth, leftBarWidth));

		}

	}

	public void paint(Graphics2D g2d, PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel) {

		List<SingleRegionSelection> singleRegionSelections = parameterModel.getSingleRegionSelections();
		SingleRegionTodo singleRegionTodo = new SingleRegionTodo() {

			@Override
			public void todo(int i, SingleRegionElement singleRegionElement, Rectangle2D.Double rect) {
				g2d.setColor(singleRegionElement.getFillColor());
				g2d.fill(rect);

				if (singleRegionSelections != null && singleRegionSelections.contains(new SingleRegionSelection(i))) {
					Stroke oldStroke = g2d.getStroke();
					g2d.setStroke(parameterModel.getDashedStroke());
					g2d.setColor(Color.blue);

					g2d.draw(rect);
					g2d.setStroke(oldStroke);
				}
				
				
				g2d.setFont(parameterModel.getNumberFont());
				g2d.setColor(Color.black);
				int count = singleRegionElement.getCount();

				FontMetrics fontMetrics = g2d.getFontMetrics();
				int fht = fontMetrics.getAscent();

				String addThousandSeparatorForInteger = EGPSFormatUtil.addThousandSeparatorForInteger(count);

				int stringWidth = fontMetrics.stringWidth(addThousandSeparatorForInteger);
				g2d.drawString(addThousandSeparatorForInteger, (float) (rect.getX() - stringWidth - 5),
						(float) (rect.getY() + 0.5 * (rect.getHeight() + fht)));
			}
		};

		commonIteration(paintingLocations, parameterModel, dataModel, singleRegionTodo);

		g2d.setColor(Color.black);

		double x = paintingLocations.getSingleRegionTopleftX();
		double y = paintingLocations.getSingleRegionTopleftY();
		double w = paintingLocations.getSingleRegionWidth();
		double h = paintingLocations.getSingleRegionHeight();

		final int numOfIntervals = 3;
		final int LineBarGap = 10;

		double yy1 = y + h + LineBarGap;
		g2d.draw(new Line2D.Double(x, yy1, x + w, yy1));

		SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
		int expendHigestNumber = singleRegionDataModel.getExpendHigestNumber();
		int axisTipLength = parameterModel.getAxisTipLength();

		int interval = IntervalUtil.getIntalval(expendHigestNumber, numOfIntervals);

		g2d.setFont(parameterModel.getNameFont());
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int fht = fontMetrics.getHeight();
		for (int i = 0; i < numOfIntervals + 1; i++) {
			int showNumber = i * interval;
			double ratio = showNumber / (double) expendHigestNumber;
			double xPlot = x + w - ratio * w;

			if (xPlot < x) {
				continue;
			}
			double yy = yy1 + axisTipLength;
			g2d.draw(new Line2D.Double(xPlot, yy1, xPlot, yy));
			
			String str = EGPSFormatUtil.addThousandSeparatorForInteger(showNumber);

			int stringWidth = fontMetrics.stringWidth(str);
			g2d.drawString(str, (float) (xPlot - 0.5 * stringWidth), (float) (yy + LineBarGap + 5));
		}

		String title = singleRegionDataModel.getTitle();
		float xTitle = (float) (x + 0.5 * w - fontMetrics.stringWidth(title));
		float yTitle = (float) (y + h + axisTipLength + 2 * fht + LineBarGap);

		Font oldFont = g2d.getFont();
		g2d.setFont(parameterModel.getTitleFont());
		int height = g2d.getFontMetrics().getHeight();
		g2d.drawString(title, xTitle, yTitle );

		g2d.setFont(oldFont);

		if (parameterModel.getNameSelections().contains(new NameSelection(-1))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(new Rectangle2D.Float(xTitle, yTitle - fht, fontMetrics.stringWidth(title) + 10, fht));
			g2d.setStroke(oldStroke);
		}
	}

	public boolean judgeWhetherSelected(PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel, Rectangle2D.Double userRect) {
		selections.clear();
		SingleRegionTodo singleRegionTodo = new SingleRegionTodo() {
			@Override
			public void todo(int i, SingleRegionElement singleRegionElement, Rectangle2D.Double rect) {
				if (rect.intersects(userRect)) {
					selections.add(new SingleRegionSelection(i));
				}
			}
		};

		commonIteration(paintingLocations, parameterModel, dataModel, singleRegionTodo);

		return !selections.isEmpty();
	}

	public boolean judgeWhetherSelected(PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel, Point point) {
		selections.clear();
		SingleRegionTodo singleRegionTodo = new SingleRegionTodo() {
			@Override
			public void todo(int i, SingleRegionElement singleRegionElement, Rectangle2D.Double rect) {
				if (rect.contains(point)) {
					selections.add(new SingleRegionSelection(i));
					return;
				}
			}
		};

		commonIteration(paintingLocations, parameterModel, dataModel, singleRegionTodo);

		return !selections.isEmpty();
	}

	public List<SingleRegionSelection> getSelections() {
		return selections;
	}

}

@FunctionalInterface
interface SingleRegionTodo {
	void todo(int i, SingleRegionElement singleRegionElement, Rectangle2D.Double rect);
}
