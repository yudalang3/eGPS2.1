package module.maplot.maplot.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D.Double;
import java.util.List;

import javax.swing.JPanel;

import module.maplot.maplot.calculator.ViewerPlotDataCalculator;
import module.maplot.maplot.listener.MA_PanelMouseListener;
import module.maplot.maplot.model.DataModel;
import module.maplot.maplot.model.IndicatingLine;
import module.maplot.maplot.model.PaintPlotData;
import module.maplot.maplot.model.PaintingLine;
import module.maplot.maplot.model.PaintingPoint;
import module.maplot.maplot.model.Scale;

@SuppressWarnings("serial")
public class MA_PlotPanel extends JPanel implements MouseMotionListener {
	private MA_PlotDrawProperties drawProperties;
	private MA_PlotJShap shape;

	public MA_PlotPanel(MA_PlotDrawProperties drawProperties) {

		this.drawProperties = drawProperties;
		shape = new MA_PlotJShap(drawProperties);

		MA_PanelMouseListener mouseListener = new MA_PanelMouseListener(this, drawProperties);
		addMouseListener(mouseListener);
		
		addMouseMotionListener(this);

		setBackground(Color.white);
	}

	@Override
	public boolean contains(int x, int y) {
		if (shape == null) {
			setToolTipText(null);
		} else {
			synchronized (shape) {
				if (shape.contains(x, y)) {

					setToolTipText(shape.getToolTipText());
				} else {
					setToolTipText(null);
				}
			}
		}

		return super.contains(x, y);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

//		System.out.println("JComponent Double buffering is: " + isDoubleBuffered());
//		RepaintManager repaintManager = RepaintManager.currentManager(this);
//		System.out.println("RepaintManager Double buffering is: " + repaintManager.isDoubleBufferingEnabled());

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int width = getWidth();
		int height = getHeight();

		ViewerPlotDataCalculator plotDataForViewer = this.drawProperties.getCalculatorPlotDataForViewer();

		DataModel currDrawingDataModel = this.drawProperties.getCurrDrawingDataModel();
		PaintPlotData paintPlotData = plotDataForViewer.calculatePaintingLocations(currDrawingDataModel, width, height);

		List<PaintingPoint> calculator = paintPlotData.getPaintingPoints();

		FontMetrics fontMetrics = g2.getFontMetrics();

		PaintingLine paintingLine = paintPlotData.getPaintingLine();

		// Plot x-axis
		Double xLine = paintingLine.getXLine();
		g2.draw(xLine);

		List<Scale> xAxisLocations = paintingLine.getXAxisLocations();
		for (Scale scale : xAxisLocations) {
			g2.draw(scale.getXLine());

			String valueOf = String.valueOf(scale.getAxisValue());

			int stringWidth = fontMetrics.stringWidth(valueOf);

			g2.drawString(valueOf, (float) (scale.getValueLocation().getX() - stringWidth / 2.0),
					(float) scale.getValueLocation().getY());
		}

		// Plot y-axis
		Double yLine = paintingLine.getYLine();
		g2.draw(yLine);
		List<Scale> yAxisLocations = paintingLine.getYAxisLocations();
		for (Scale scale : yAxisLocations) {
			g2.draw(scale.getXLine());
			double axisValue = scale.getAxisValue();

			String valueOf = String.valueOf(axisValue);

			int stringWidth = fontMetrics.stringWidth(valueOf);

			g2.drawString(valueOf, (float) scale.getValueLocation().getX() - stringWidth - 5,
					(float) (scale.getValueLocation().getY() + 3));
		}
		LeftToolJPanel leftToolJPanel = drawProperties.getLeftToolJPanel();

		Integer scatterSize = (Integer) leftToolJPanel.getScatterSize().getValue();
		
		// drawing the indicators;
		IndicatingLine changeIndicatLine = paintPlotData.getChangeIndicatLine();
		if (changeIndicatLine != null) {
			Double frontLine = changeIndicatLine.getFrontLine();
			Double behindLine = changeIndicatLine.getBehindLine();
			
			g2.draw(frontLine);
			g2.draw(behindLine);
		}
		

		for (PaintingPoint paintingPoint : calculator) {
			Color paintingColor = paintingPoint.getPaintingColor();

			if (paintingColor != null) {

				g2.setColor(paintingColor);
			}

			Ellipse2D.Double circle = new Ellipse2D.Double(paintingPoint.getXPos(), paintingPoint.getYPos(),
					scatterSize, scatterSize);
			// Ellipse2D.Double circle = paintingPoint.getCircle();

			g2.fill(circle);

		}

		g2.setColor(Color.BLACK);

		g2.setFont(drawProperties.getTitleFont());

		fontMetrics = g2.getFontMetrics();

		String title = leftToolJPanel.getCustomTitleField().getText();

		int stringWidth = fontMetrics.stringWidth(title);

		int xPos = width / 2 - stringWidth / 2;

		g2.drawString(title, xPos, 40);

		String yAxisTitle = leftToolJPanel.getyAxisTitleField().getText();

		stringWidth = fontMetrics.stringWidth(yAxisTitle);

		int y = height / 2;

		int yPos = y + stringWidth / 2;

		g2.rotate(-Math.toRadians(90), 40, yPos);

		g2.drawString(yAxisTitle, 40, yPos);

		g2.rotate(Math.toRadians(90), 40, yPos);

		String xAxisTitle = leftToolJPanel.getxAxisTitleField().getText();

		stringWidth = fontMetrics.stringWidth(xAxisTitle);

		g2.drawString(xAxisTitle, width / 2 - stringWidth / 2, height - 100 + 60);

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (shape.contains(e.getPoint().getX(), e.getPoint().getY())) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

	}

}
