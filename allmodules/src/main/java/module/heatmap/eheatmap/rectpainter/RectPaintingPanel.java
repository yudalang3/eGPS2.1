package module.heatmap.eheatmap.rectpainter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.CellRendererPane;

import egps2.EGPSProperties;
import graphic.engine.GradientColorHolder;
import egps2.frame.gui.EGPSMainGuiUtil;
import module.heatmap.eheatmap.AbstrctEHeatmapPaintPanel;

/**
 * Only response for painting! 这个类只负责画图，不负责 位置信息等的计算。
 * 
 * @author yudalang
 *
 */
@SuppressWarnings("serial")
public class RectPaintingPanel extends AbstrctEHeatmapPaintPanel {

	
	private RectMapPainter mapPainter = new RectMapPainter();
	private RectColTreePainter colTreePainter = new RectColTreePainter();
	private ColAnnotationPainter colAnnotationPainter = new ColAnnotationPainter();
	private ColNamesPainter colNamesPainter = new ColNamesPainter();
	private RectRowTreePainter rowTreePainter = new RectRowTreePainter();
	private RowAnnotaionPainter rowAnnotaionPainter = new RowAnnotaionPainter();
	private RowNamesPainter rowNamesPainter = new RowNamesPainter();
	private MapLegendPainter mapLegendPainter = new MapLegendPainter();
	private AnnotationLegendPainter annotationLegendPainter = new AnnotationLegendPainter();

	private CellRendererPane cellRendererPane = new CellRendererPane();

	public RectPaintingPanel() {

	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		if (locationParas == null || dataModel == null || paraModel == null) {
			EGPSMainGuiUtil.setupHighQualityRendering(g2d);
			EGPSMainGuiUtil.drawLetUserImportDataString(g2d);
			return;
		}

		GradientColorHolder gColorHolder = paraModel.getgColorHolder();
		
		// draw col tree
		if (paraModel.isIfPaintColTree()) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			colTreePainter.painting(g2d, locationParas, paraModel, dataModel);
		}
		// draw col annotation
		if (paraModel.isIfPaintColAnnotation()) {
			colAnnotationPainter.painting(g2d, locationParas, paraModel, dataModel);
		}
		// draw col names
		if (paraModel.isIfPaintColNames()) {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			colNamesPainter.painting(g2d, locationParas, dataModel, paraModel);
		}
		// draw row tree
		if (paraModel.isIfPaintRowTree()) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			rowTreePainter.painting(g2d, locationParas, paraModel, dataModel);
		}
		// draw row annotation
		if (paraModel.isIfPaintRowAnnotation()) {
			rowAnnotaionPainter.painting(g2d, locationParas, paraModel, dataModel);
		}

		// draw rectangular cell
		mapPainter.painting(g2d, locationParas, dataModel, gColorHolder, paraModel);

		if (paraModel.isIfShowBorder()) {
			// draw rectangular border
			mapPainter.paintingBorder(g2d, locationParas, paraModel, dataModel);
			colAnnotationPainter.paintingBorder(g2d, locationParas, paraModel);
			rowAnnotaionPainter.paintingBorder(g2d, locationParas, paraModel);
		}

		int dataValueStatus = paraModel.getDataValueStatus();
		boolean ifShowOrigValue = (dataValueStatus == 1) || (dataValueStatus == 3);
		boolean ifGetPartial = (dataValueStatus == 3) || (dataValueStatus == 4);
		if (dataValueStatus > 0) {
			mapPainter.paintValues(g2d, locationParas, paraModel, dataModel, ifShowOrigValue, ifGetPartial);
		}

		// draw row names
		if (paraModel.isIfPaintRowNames()) {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			rowNamesPainter.painting(g2d, locationParas, dataModel, paraModel, cellRendererPane);
		}
		// draw map legend
		if (paraModel.isIfPaintMapLegend()) {
			int x = (int) locationParas.getMapLegendLeftTopX();
			int y = (int) locationParas.getMapLegendLeftTopY();
			int w = (int) locationParas.getMapLegendWeidth();
			int h = (int) locationParas.getMapLegendHeight();
			Rectangle float1 = new Rectangle(x, y, w, h);
			mapLegendPainter.painting(g2d, float1, gColorHolder, dataModel, paraModel);
		}
		// draw annotation legend
		if (paraModel.isIfPaintAnnotationLegend()) {
			annotationLegendPainter.painting(g2d, locationParas, paraModel);
		}

		// ########################################################################
		// deal with selected rectangle(gesture!)#################################
		if (dragRect != null) { // ##############################
			g2d.setPaint(EGPSProperties.dragFrameColor);// ##############################
			g2d.fill(dragRect); // ##############################
		} // #######################################################################
			// ########################################################################

		// #######################################

		g2d.dispose();
	}

	@Override
	public void reSerCellWidthAndHeightForGapDisplay() {
		double widthForV = locationParas.getHmapWeidth();
		double heightForH = locationParas.getHmapHeight();
		int numOfCols = locationParas.getNumOfCols();
		int numOfRows = locationParas.getNumOfRows();
		double cellWidth = (widthForV - paraModel.getGapSize() * paraModel.gethGapLocations().length) / numOfCols;
		double cellHeight = (heightForH - paraModel.getGapSize() * paraModel.getvGapLocations().length) / numOfRows;

		locationParas.setCellHeight(cellHeight);
		locationParas.setCellWidth(cellWidth);
	}

}
