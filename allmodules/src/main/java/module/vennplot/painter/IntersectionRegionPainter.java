package module.vennplot.painter;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import utils.EGPSFormatUtil;
import module.vennplot.model.DataModel;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;

public class IntersectionRegionPainter {
	final double degrees144 = Math.toRadians(144);
	
	public void paint(Graphics2D g2d, PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel) {
		
		double x = paintingLocations.getIntersectRegionTopleftX();
		double y = paintingLocations.getIntersectRegionTopleftY();
		double w = paintingLocations.getIntersectRegionWidth();
		double h = paintingLocations.getIntersectRegionHeight();
		
		List<BodyIntersectionSelection> bodyIntersectionSelections = parameterModel.getBodyIntersectionSelections();
		double intersectRegionElementWidth = paintingLocations.getIntersectRegionElementWidth();
		
		IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
		List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();
		
		g2d.setFont(parameterModel.getNumberFont());
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int numOfIntersectionItems = eles.size();
		
		for (int i = 0; i < numOfIntersectionItems; i++) {
			IntersectionRegionElement intersectionRegionElement = eles.get(i);
			double xx = i * intersectRegionElementWidth + x;
			double hh = h * intersectionRegionElement.getRatio_this2highest();
			double yy = y + h - hh;
			double ww = parameterModel.getTopBarWidth();
			
			
			g2d.setColor(intersectionRegionElement.getFilledColor());
			
			
			if (hh == 0) {
				g2d.draw(new Line2D.Double(xx, yy, xx + ww, yy));
			}else {
				g2d.fill(new Double(xx, yy, ww, hh));
			}
			
			if (intersectionRegionElement.ifShowPantagrame()) {
				drawPentagram(g2d, 10, (int)xx, (int) (yy -10), Color.red, Color.red);
			}
			
			if (parameterModel.isShowIntersectionValues()) {
				g2d.setColor(Color.black);
				String ss = EGPSFormatUtil.addThousandSeparatorForInteger(intersectionRegionElement.getCount());
				float x3 = (float) (xx + 0.5 * ww - 0.5 * fontMetrics.stringWidth(ss));
				g2d.drawString(ss, (float) x3, (float) (yy - 2));
				
			}
			
			if (bodyIntersectionSelections.contains(new BodyIntersectionSelection(i))) {
				Double rect = new Double(xx, yy, ww, hh + paintingLocations.getBodyHeight());
				Stroke oldStroke = g2d.getStroke();
				g2d.setStroke(parameterModel.getDashedStroke());
				g2d.setColor(Color.blue);
				g2d.draw(rect);
				g2d.setStroke(oldStroke);
			}
			
		}
		
	}
	
	private void drawPentagram(Graphics2D g, int len, int x, int y,
            Color fill, Color stroke) {
        double angle = 0;
 
        Path2D p = new Path2D.Float();
        p.moveTo(x, y);
 
        for (int i = 0; i < 5; i++) {
            int x2 = x + (int) (Math.cos(angle) * len);
            int y2 = y + (int) (Math.sin(-angle) * len);
            p.lineTo(x2, y2);
            x = x2;
            y = y2;
            angle -= degrees144;
        }
        p.closePath();
 
        g.setColor(fill);
        g.fill(p);
 
        g.setColor(stroke);
        g.draw(p);
    }
	private void drawTriangle(Graphics2D g, int len, int x, int y,
			Color fill, Color stroke) {
		double angle = 0;
		
		Path2D p = new Path2D.Float();
		p.moveTo(x, y);
		
		p.lineTo(x, y);
		p.lineTo(x+len, y);
		p.lineTo(x + 0.5 * len , y - 1. * len);
		p.closePath();
		
		g.setColor(fill);
		g.fill(p);
		
		g.setColor(stroke);
		g.draw(p);
	}

}
