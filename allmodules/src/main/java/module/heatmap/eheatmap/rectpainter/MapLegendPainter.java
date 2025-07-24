package module.heatmap.eheatmap.rectpainter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import graphic.engine.GradientColorHolder;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.ParameterModel;

public class MapLegendPainter {

	final int markInterval = 4;


	public void painting(Graphics2D g2d, Rectangle areaLocation, GradientColorHolder gColorHolder,
			DataModel dataModel, ParameterModel paraModel) {
		float x = (float) areaLocation.getX();
		float y = (float) areaLocation.getY();
		float w = (float) areaLocation.getWidth();
		float h = (float) areaLocation.getHeight();
		
		if (!paraModel.isIfPaintRowNames()) {
			x += 25;
		}

		float legendWidth = paraModel.getMapLegnedWidth();
		float legendHeight = paraModel.getMapLegendHeight();
//		legendWidth = 30;
//		legendHeight = 15;
		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		Color[] clone = gColorHolder.getColors().clone();
		Color[] reverseArray = reverseArray(clone);
//		Paint linearGradientPaint = JideSwingUtilities.getLinearGradientPaint(x, y, x + legendWidth, y + legendHeight,
//				gColorHolder.getDist(), reverseArray);
		
		LinearGradientPaint linearGradientPaint = new LinearGradientPaint(x, y, x + legendWidth, y + legendHeight, gColorHolder.getDist(), reverseArray);
		g2d.setPaint(linearGradientPaint);
		g2d.fill(new Rectangle2D.Float(x, y, legendWidth, legendHeight));

		g2d.setPaint(Color.black);
		double maxValue = dataModel.getMaxValue();
		double minValue = dataModel.getMinValue();
		int fht = g2d.getFontMetrics().getHeight();
		
		double range = maxValue - minValue;

		if ( range >1) {
			int interval = (int) Math.ceil(range / markInterval);
			int minIntValue = (int) Math.ceil(minValue);
			for (int i = 0; i < markInterval; i++) {
				int showValue = minIntValue + i * interval;
				float yy = y + (float) ((maxValue - showValue) / range * legendHeight + 0.5 * fht);
				
				if (yy < y) {
					continue;
				}
				g2d.drawString("" + showValue, x + legendWidth + 10, yy);
			}
		}else {
			DecimalFormat df = new DecimalFormat("0.00");
			float yy1 = y + (float) ((maxValue - minValue) / range * legendHeight + 0.5 * fht);
			g2d.drawString(df.format(minValue), x + legendWidth + 10, yy1);
			
			float yy2 = y + (float) ((maxValue - minValue) / range * legendHeight *0.5 + 0.5 * fht);
			g2d.drawString(df.format(0.5 * minValue + 0.5 * maxValue), x + legendWidth + 10, yy2);

			float yy3 = y + (float) (0.5 * fht);
			g2d.drawString(df.format(maxValue), x + legendWidth + 10, yy3);
			
		}
		

	}

	private Color[] reverseArray(Color[] arr) {
		List<Color> list = Arrays.asList(arr);
		Collections.reverse(list);
		return list.toArray(new Color[list.size()]);
	}

}
