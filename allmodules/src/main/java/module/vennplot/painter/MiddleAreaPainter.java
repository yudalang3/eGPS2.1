package module.vennplot.painter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import utils.EGPSFormatUtil;
import egps2.utils.common.util.IntervalUtil;
import module.vennplot.model.DataModel;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;

public class MiddleAreaPainter {

	private final double topAxisGap = 15;

	public void paint(Graphics2D g2d, PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel) {
		double x = paintingLocations.getMiddleAreaTopleftX();
		double y = paintingLocations.getMiddleAreaTopleftY();
		double w = paintingLocations.getMiddleAreaWidth();
		double h = paintingLocations.getMiddleAreaHeight();
		
		final int numOfIntervals = 4;
		final int axisTipLength = parameterModel.getAxisTipLength();
		
		g2d.setColor(Color.black);

		IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
		int expendHigestNumber = intersectionRegionDataModel.getExpendHigestNumber();
		
		
		int interval = IntervalUtil.getIntalval(expendHigestNumber, numOfIntervals);
		double x2 = x+w - topAxisGap;
		
		g2d.setFont(parameterModel.getNameFont());
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int fht = fontMetrics.getHeight();
		
		double yPlot = 0;
		for (int i = 0; i < numOfIntervals; i++) {
			int showNumber = i * interval + interval;
			double ratio = showNumber / (double) expendHigestNumber;
			yPlot = y + h - ratio * h;
			
			if (yPlot < y - 10 ) {
				break;
			}
			double xx = x2 - axisTipLength;
			g2d.draw(new Line2D.Double(xx,yPlot,x2,yPlot));
			
			String str = EGPSFormatUtil.addThousandSeparatorForInteger(showNumber);
			
			int stringWidth = fontMetrics.stringWidth(str);
			g2d.drawString(str, (float) (xx - stringWidth - 5), (float) (yPlot+ 0.3 * fht));
		}
		double xx = x2 - axisTipLength;
		g2d.draw(new Line2D.Double(xx,y + h,x2,y + h));
		g2d.drawString("0", (float) (xx - 10), (float) (y + h+ 0.3 * fht));
		
		
		
		g2d.draw(new Line2D.Double(x2,y-10,x2,y+h));
		
		String title = intersectionRegionDataModel.getTitle();
		float xTitle = (float) (x + 0.2*w - fht);
		float yTitle = (float) (y + 0.5*h + 0.5 * fontMetrics.stringWidth(title));
		
		g2d.rotate(Math.toRadians(-90), xTitle, yTitle-fht);
		
		if (parameterModel.getNameSelections().contains(new NameSelection(-2))) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(parameterModel.getDashedStroke());
			g2d.setColor(Color.blue);
			g2d.draw(new Rectangle2D.Float(xTitle, yTitle - fht, fontMetrics.stringWidth(title), fht));
			g2d.setStroke(oldStroke);
		}
		
		g2d.setColor(Color.black);

		Font oldFont = g2d.getFont();
		g2d.setFont(parameterModel.getTitleFont());
		g2d.drawString(title, xTitle, yTitle);
		g2d.setFont(oldFont);
		g2d.rotate(Math.toRadians(90), xTitle, yTitle-fht);
		
	}
	

}
