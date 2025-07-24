package module.vennplot.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.JPanel;

import module.vennplot.model.DataModel;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.painter.BodyRegionPainter;
import module.vennplot.painter.IntersectionRegionPainter;
import module.vennplot.painter.LeftPiePainter;
import module.vennplot.painter.LegendPainter;
import module.vennplot.painter.MiddleAreaPainter;
import module.vennplot.painter.NamesPainter;
import module.vennplot.painter.SingleRegionPainterAndSelectionJudger;

public class PaintingPanelUpsetR extends JPanel {

	private static final long serialVersionUID = -3665382995427272565L;

	PaintingLocations paintingLocations;
	ParameterModel parameterModel;
	DataModel dataModel;

	LeftPiePainter leftPiePainter = new LeftPiePainter();
	BodyRegionPainter bodyRegionPainter = new BodyRegionPainter();
	IntersectionRegionPainter intersectionRegionPainter = new IntersectionRegionPainter();
	MiddleAreaPainter middleAreaPainter = new MiddleAreaPainter();
	NamesPainter namesPainter = new NamesPainter();
	SingleRegionPainterAndSelectionJudger singleRegionPainter = new SingleRegionPainterAndSelectionJudger();
	LegendPainter legenedPainter = new LegendPainter();

	Double dragedRectanglar;

	public PaintingPanelUpsetR() {
		this.setBackground(Color.white);
	}

	public void setModels(ParameterModel parameterModel, DataModel dataModel) {
		this.parameterModel = parameterModel;
		this.dataModel = dataModel;
	}

	public PaintingLocations getPaintingLocations() {
		return paintingLocations;
	}

	public void setPaintingLocations(PaintingLocations paintingLocations) {
		this.paintingLocations = paintingLocations;
	}

	@Override
	public void paint(Graphics g) {

		if (parameterModel == null || dataModel == null) {
			return;
		}

		Graphics2D g2d = (Graphics2D) g;

		bodyRegionPainter.paint(g2d, paintingLocations, parameterModel, dataModel);
		singleRegionPainter.paint(g2d, paintingLocations, parameterModel, dataModel);

		namesPainter.paint(g2d, paintingLocations, parameterModel, dataModel);

		middleAreaPainter.paint(g2d, paintingLocations, parameterModel, dataModel);


		intersectionRegionPainter.paint(g2d, paintingLocations, parameterModel, dataModel);

		if (parameterModel.isShowLegend()) {
			legenedPainter.paint(g2d, paintingLocations, parameterModel, dataModel);
		}

		if (parameterModel.isShowLeftPie()) {
			leftPiePainter.paint(g2d, paintingLocations, parameterModel, dataModel);
		}

		// ########################################################################
		// deal with selected rectangle(gesture!)#################################
		if (dragedRectanglar != null) { // ##############################
			g2d.setPaint(new Color(0, 0, 182, 50));// ##############################
			g2d.fill(dragedRectanglar); // ##############################
		} // #######################################################################
			// ########################################################################
			// #######################################

//		paintAreas(g2d);

		g2d.dispose();
	}

	private void paintAreas(Graphics2D g2d) {
		// For debug

		Double rectDouble = new Double();

		// red region
		g2d.setColor(new Color(255, 0, 0, 50));
		rectDouble.setFrame(paintingLocations.getSingleRegionTopleftX(), paintingLocations.getSingleRegionTopleftY(),
				paintingLocations.getSingleRegionWidth(), paintingLocations.getSingleRegionHeight());
		g2d.drawString("single region: ", (float) paintingLocations.getSingleRegionTopleftX() , (float) (paintingLocations.getSingleRegionTopleftY() + 30) );
		g2d.fill(rectDouble);

		// green region
		g2d.setColor(new Color(0, 255, 0, 50));
		rectDouble.setFrame(paintingLocations.getNameRegionTopleftX(), paintingLocations.getNameRegionTopleftY(),
				paintingLocations.getNameRegionWidth(), paintingLocations.getNameRegionHeight());
		g2d.fill(rectDouble);

		// green region
		g2d.setColor(new Color(0, 0, 255, 50));
		rectDouble.setFrame(paintingLocations.getMiddleAreaTopleftX(), paintingLocations.getMiddleAreaTopleftY(),
				paintingLocations.getMiddleAreaWidth(), paintingLocations.getMiddleAreaHeight());
		g2d.fill(rectDouble);
		
		//
		g2d.drawString("Body region: ", (float) paintingLocations.getBodyTopleftX() , (float) (paintingLocations.getBodyTopleftY() + 30) );
		g2d.setColor(new Color(0, 255, 255, 50));
		rectDouble.setFrame(paintingLocations.getBodyTopleftX(), paintingLocations.getBodyTopleftY(),
				paintingLocations.getBodyWidth(), paintingLocations.getBodyHeight());
		g2d.fill(rectDouble);
		
		//
		g2d.setColor(new Color(255, 0 , 255, 50));
		rectDouble.setFrame(paintingLocations.getIntersectRegionTopleftX(), paintingLocations.getIntersectRegionTopleftY(),
				paintingLocations.getIntersectRegionWidth(), paintingLocations.getIntersectRegionHeight());
		g2d.drawString("IntersectRegion: ", (float) paintingLocations.getIntersectRegionTopleftX() , (float) (paintingLocations.getIntersectRegionTopleftY() + 30) );
		g2d.fill(rectDouble);
		
		//
		g2d.setColor(new Color(255, 255 , 0, 50));
		rectDouble.setFrame(paintingLocations.getLeftPieTopleftX(), paintingLocations.getLeftPieTopleftY(),
				paintingLocations.getLeftPieWidth(), paintingLocations.getLeftPieHeight());
		g2d.fill(rectDouble);

	}

	public void setDragRect(Double double1) {
		dragedRectanglar = double1;

	}

//	public void setRegionForDebug(Rectangle2D.Double regionForDebug) {
//		this.regionForDebug = regionForDebug;
//	}
}
