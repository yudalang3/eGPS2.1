package module.heatmap.eheatmap.cirpainter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import module.heatmap.eheatmap.AbstrctEHeatmapPaintPanel;
import module.heatmap.eheatmap.model.CircularParameters;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.rectpainter.AnnotationLegendPainter;
import module.heatmap.eheatmap.rectpainter.MapLegendPainter;


@SuppressWarnings("serial")
public class CircularPaintingPanel extends AbstrctEHeatmapPaintPanel implements Runnable {

	private final CircularParameters circularParameters;
	CirMapPainter cirMapPainter = new CirMapPainter();

	private CirColTreePainter colTreePainter = new CirColTreePainter();
	private CirRowTreePainter rowTreePainter = new CirRowTreePainter();
	private MapLegendPainter mapLegendPainter = new MapLegendPainter();
	private AnnotationLegendPainter annotationLegendPainter = new AnnotationLegendPainter();

	/**
	 * 如果第一次，也许有动画！
	 */

	public CircularPaintingPanel(ParameterModel paraModel) {
		this.paraModel = paraModel;
		this.circularParameters = paraModel.getCircularParameters();
		if (circularParameters.isShouldAnimate()) {
			new Thread(this).start();
		}
	}

	@Override
	public void paint(Graphics g) {

		if (circularParameters.getTotalEextendDeg() < 0) {
			g.drawString("The total extending degree not large enough", 100, 100);
			return;
		}
		if (locationParas == null || dataModel == null || paraModel == null) {
			g.drawString("You have not load data yet", 100, 100);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// draw col tree
		if (paraModel.isIfPaintColTree()) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			colTreePainter.painting(g2d, locationParas, paraModel, dataModel);
		}
		// draw row tree
		if (paraModel.isIfPaintRowTree()) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			rowTreePainter.painting(g2d, locationParas, paraModel, dataModel);
		}
		// draw map legend
		if (paraModel.isIfPaintMapLegend()) {
			int x = (int) locationParas.getMapLegendLeftTopX();
			int y = (int) locationParas.getMapLegendLeftTopY();
			int w = (int) locationParas.getMapLegendWeidth();
			int h = (int) locationParas.getMapLegendHeight();
			Rectangle float1 = new Rectangle(x, y, w, h);

			mapLegendPainter.painting(g2d, float1, paraModel.getgColorHolder(), dataModel, paraModel);
		}

		// draw annotation legend
		if (paraModel.isIfPaintAnnotationLegend()) {
			annotationLegendPainter.painting(g2d, locationParas, paraModel);
		}
		cirMapPainter.painting(g2d, locationParas, dataModel, paraModel.getgColorHolder(), paraModel);
		// drawCurves(g2d,x0,y0,blinkRadius);
	}

//	private void drawCurves(Graphics2D g2d, int x0, int y0, int blinkRadius) {
//		g2d.setColor(Color.red);
//		QuadCurve2D q = new QuadCurve2D.Float();
//		
//		for (int i = 0; i < froms.length; i++) {
//			// draw QuadCurve2D.Float with set coordinates
//			Double fromP1 = linkages.get(froms[i]);
//			Double toP1 = linkages.get(tos[i]);
//			q.setCurve(fromP1.getX(),fromP1.getY(), x0, y0,toP1.getX(),toP1.getY());
//			g2d.draw(q);
//		}
//		
//	}

	@Override
	public void run() {
		final double totalSstartDeg = circularParameters.getTotalSstartDeg();
		final double totalEextendDeg = circularParameters.getTotalEextendDeg();
		double startD = 90;
		double extendD = 360;

		circularParameters.setTotalSstartDeg(startD);
		circularParameters.setTotalEextendDeg(extendD);

		while (true) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			startD += 10;
			extendD -= 20;
			circularParameters.setTotalSstartDeg(startD);
			circularParameters.setTotalEextendDeg(extendD);
			controller.weakestRefreshHeatmapForInteractiveDrag(true, true);

			if (startD > 270 || extendD < 30) {
				circularParameters.setTotalSstartDeg(totalSstartDeg);
				circularParameters.setTotalEextendDeg(totalEextendDeg);
				controller.weakestRefreshHeatmapForInteractiveDrag(true, true);
				break;
			}

		}

	}

}
