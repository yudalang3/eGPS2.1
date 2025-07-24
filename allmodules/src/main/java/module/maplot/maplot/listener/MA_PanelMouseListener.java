package module.maplot.maplot.listener;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.List;

import module.maplot.maplot.gui.MA_PlotDrawProperties;
import module.maplot.maplot.gui.MA_PlotPanel;
import module.maplot.maplot.model.DataModel;
import module.maplot.maplot.model.PaintingPoint;
import module.maplot.maplot.model.ValuePoint;
import egps2.panels.DialogUtil;

public class MA_PanelMouseListener extends MouseAdapter {

	private MA_PlotDrawProperties plotDrawProperties;

	private MA_PlotPanel drawPanel;

	public MA_PanelMouseListener(MA_PlotPanel drawPanel, MA_PlotDrawProperties drawProperties) {

		this.drawPanel = drawPanel;
		this.plotDrawProperties = drawProperties;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		Point point = event.getPoint();

		List<PaintingPoint> paintingPoints = plotDrawProperties.getCalculatorPlotDataForViewer().getPaintPlotData()
				.getPaintingPoints();

		DataModel model = plotDrawProperties.getCurrDrawingDataModel();
		for (PaintingPoint paintingPoint : paintingPoints) {
			Integer scatterSize = (Integer) plotDrawProperties.getLeftToolJPanel().getScatterSize().getValue();

			Ellipse2D.Double circle = new Ellipse2D.Double(paintingPoint.getXPos(), paintingPoint.getYPos(),
					scatterSize, scatterSize);

			if (circle.contains(point.getX(), point.getY())) {
				// System.out.println("" + point.getX() + " " + point.getY());
				showPopupMenu(event, paintingPoint,model);
				break;
			}

		}
	}

	private void showPopupMenu(final MouseEvent event, PaintingPoint paintingPoint, DataModel model) {
		StringBuffer sb = new StringBuffer();
		
		ValuePoint originalPoint = paintingPoint.getOriginalPoint();
		sb.append("ID: ").append(originalPoint.getId()).append("\n");
		sb.append("ElegantJTable value: ").append(originalPoint.getXValue()).append("\n");
		sb.append("M value: ").append(originalPoint.getYValue()).append("\n");
		
		List<String> headerFields = model.getHeaderFields();
		List<String> annotations = originalPoint.getAnnotations();
		if (annotations != null) {
			sb.append("- - - - - - - - - - - - - - - - - - - - - - - - - - - \n");
			int size = headerFields.size();
			for (int i = 0; i < size; i++) {
				sb.append(headerFields.get(i)).append(":\t");
				sb.append(annotations.get(i)).append("\n");
			}
		}
		
		DialogUtil.showDialog(Arrays.asList(sb.toString()));
	}

}
