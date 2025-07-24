package module.vennplot.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.List;

import graphic.engine.drawer.SectorRingDrawer;
import module.vennplot.model.DataModel;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SingleRegionDataModel;
import module.vennplot.model.SingleRegionElement;

public class LeftPiePainter {

	SectorRingDrawer sectorRingDrawer = new SectorRingDrawer();

	public void paint(Graphics2D g2d, PaintingLocations paintingLocations, ParameterModel parameterModel,
			DataModel dataModel) {

		// ydl: 人为移动一下
		double x = paintingLocations.getLeftPieTopleftX() - 30;
		double y = paintingLocations.getLeftPieTopleftY() + 20;
		double w = paintingLocations.getLeftPieWidth();
		double h = paintingLocations.getLeftPieHeight();
		// g2d.draw(new java.awt.geom.Rectangle2D.Double(x, y, w, h));
		SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
		List<SingleRegionElement> eles = singleRegionDataModel.getEles();

		int maxNumOfElemet = dataModel.getNumOfAllUniqueElements();
		int[][] patintingMatrix = dataModel.getPatintingMatrix();

		if (patintingMatrix == null) {
			return;
		}
		double degreeOfEachCount = 360.0 / maxNumOfElemet;

		int numOfAllpaintVectors = patintingMatrix.length;
		double rMax = 0.309 * Math.min(w, h);
		double eachRadicus = rMax / numOfAllpaintVectors;

		x -= eachRadicus;
		y += eachRadicus;

		double xCircle = x + w - rMax;
		double yCircle = y + rMax;
		double startDeg = 0;
		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < numOfAllpaintVectors; i++) {
			double innerRadicus = i * eachRadicus + eachRadicus;
			double outerRaducus = innerRadicus + eachRadicus;

			int[] js = patintingMatrix[i];
			int numOfElement2Paint = js.length;

			g2d.setColor(eles.get(i).getFillColor());
			for (int j = 0; j < numOfElement2Paint; j++) {
				startDeg = js[j] * degreeOfEachCount;
				sectorRingDrawer.drawSectorRing1(g2d, xCircle, yCircle, outerRaducus, innerRadicus + 2, startDeg,
						degreeOfEachCount);

			}

		}

		g2d.setColor(Color.lightGray);
		Double double1 = new Double(xCircle - eachRadicus, yCircle - eachRadicus,
				eachRadicus + eachRadicus, eachRadicus + eachRadicus);
		g2d.fill(double1);
	}

}
