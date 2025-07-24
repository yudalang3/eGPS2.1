package module.evolview.gfamily.work.gui.browser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D.Double;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.Triangle;
import module.evolview.gfamily.work.gui.browser.draw.AxisTicks;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyGeneStructure;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyRegionData;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceElement;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;

@SuppressWarnings("serial")
public class TrackGeneStructure extends AbstractTrackPanel {

	private CalculatorGeneStructure locationCalculator;
	protected DrawingPropertyGeneStructure paintStructure;

	private Double rectangle;
	private List<DrawingPropertySequenceElement> secondPaintBlocks;

	private Color fillColor = new Color(0, 0, 0, 25);

	final static float dash1[] = { 4.0f, 4f };
	final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
			0.0f);

	public TrackGeneStructure(BrowserPanel genomeMain, GeneStructureInfo data) {
		super(genomeMain);

		this.locationCalculator = new CalculatorGeneStructure(data);
		this.shape = new TooltiperGeneStructure(this);

		MouseListenerGeneStructure mouseListener = new MouseListenerGeneStructure(genomeMain);

		addMouseMotionListener(mouseListener);

		addMouseListener(mouseListener);
		
		addMouseWheelListener(mouseListener);
	}

	@Override
	public void initialize() {
		paintStructure = locationCalculator.calculatePaintingLocations(drawProperties, getWidth(), getHeight());

	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(32767, 130);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(10, 130);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		FontMetrics fontMetrics = g2.getFontMetrics();

		int width = getWidth();
		int height = getHeight();

		if (isHighlight) {
			g2.setBackground(backgroundColor);
			g2.clearRect(0, 0, width, height);
		}
		if (paintStructure == null) {
			return;
		}

		g2.drawString(paintStructure.getTrackName(), paintStructure.getTrackNameXLocation(),
				paintStructure.getTrackNameYLocation());
		paintUpCoordinateAndZoomedSubRegions(g2, fontMetrics);

		paintUpMaskedRegion(g2);

		g2.dispose();

	}

	private List<DrawingPropertySequenceElement> paintVirusStrainsInfoAll = new ArrayList<>();

	private void paintUpCoordinateAndZoomedSubRegions(Graphics2D g2, FontMetrics fontMetrics) {
		paintVirusStrainsInfoAll.clear();
		List<DrawingPropertySequenceElement> firstPaintBlocks = paintStructure.getFirstPaintBlockColors();
		
		paintGenomePaintFirstBlock(g2, fontMetrics, firstPaintBlocks);
		
		secondPaintBlocks = paintStructure.getSecondPaintBlockColors();

		paintGenomePaintSecondBlock(g2, fontMetrics, secondPaintBlocks);

		paintVirusStrainsInfoAll.addAll(firstPaintBlocks);
		paintVirusStrainsInfoAll.addAll(secondPaintBlocks);

		List<Double> secondBlankBlock = paintStructure.getSecondBlankBlock();
		for (Double double1 : secondBlankBlock) {
			g2.draw(double1);
		}

		g2.draw(paintStructure.getTickLine());

		// draw the axis tick and tick names
		List<AxisTicks<?>> xAxisLocations = paintStructure.getXAxisLocations();
		for (AxisTicks<?> scale : xAxisLocations) {
			g2.draw(scale.getLine());

			Integer axisValue = (Integer) scale.getAxisValue();
			String format = NumberFormat.getNumberInstance().format(axisValue);
			int stringWidth = fontMetrics.stringWidth(format);

			g2.drawString(format, (float) (scale.getValueLocation().getX() - 0.5 * stringWidth),
					(float) scale.getValueLocation().getY());
		}

		// draw the slashed lines
		Color oldColor = g2.getColor();
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(dashed);
		g2.setColor(Color.DARK_GRAY);
		g2.draw(paintStructure.getStarSlashtLine());
		g2.draw(paintStructure.getEndSlashtLine());
		g2.setStroke(oldStroke);
		g2.setColor(oldColor);
	}

	private void paintGenomePaintFirstBlock(Graphics2D g2, FontMetrics fontMetrics,
			List<DrawingPropertySequenceElement> paintBlocks) {
		for (DrawingPropertySequenceElement paintInfo : paintBlocks) {
			Double rectangle = paintInfo.getRectangle();
			g2.setColor(paintInfo.getColor());
			g2.fill(rectangle);
			g2.setColor(Color.black);
		}
		
		String geneName = paintStructure.getGeneName();
		if (geneName != null) {
			g2.drawString(geneName, paintStructure.getTrackNameXLocation() + 8f, paintBlocks.get(0).getNameLocationY() + 20.0f);
		}

	}

	private void paintGenomePaintSecondBlock(Graphics2D g2, FontMetrics fontMetrics,
			List<DrawingPropertySequenceElement> paintBlocks) {
		float yAxis4Name = 0f;
		
		for (DrawingPropertySequenceElement paintInfo : paintBlocks) {
			Double rectangle = paintInfo.getRectangle();
			g2.draw(rectangle);
			g2.setColor(paintInfo.getColor());
			g2.fill(rectangle);
			g2.setColor(Color.black);
			String geneName = paintInfo.getGeneName();

			if (geneName == null || geneName.isEmpty()) {
				continue;
			}
			int stringWidth = fontMetrics.stringWidth(geneName);
			if (stringWidth > rectangle.getWidth()) {
				continue;
			}

			yAxis4Name = paintInfo.getNameLocationY() + 2.0f;
			g2.drawString(geneName, 
					paintInfo.getNameLocationX() - 0.5f * stringWidth,
					yAxis4Name);
		}
		
		String geneName = paintStructure.getSequenceElementName();
		if (geneName == null || paintBlocks.isEmpty()) {
			return;
		}
		DrawingPropertySequenceElement drawingPropertySequenceElement = paintBlocks.get(0);
		g2.drawString(geneName, paintStructure.getTrackNameXLocation() + 8f,
				drawingPropertySequenceElement.getNameLocationY() + 2.0f);
	}

	private void paintUpMaskedRegion(Graphics2D g2) {
		DrawingPropertyRegionData regionData = paintStructure.getRegionData();
		Color color = g2.getColor();
		g2.setColor(fillColor);
		rectangle = regionData.getRectangle();
		g2.fill(rectangle);

		g2.setColor(color);
		Triangle startTriangle = regionData.getStartTriangle();
		startTriangle.fill(g2);
		Triangle endTriangle = regionData.getEndTriangle();
		endTriangle.fill(g2);
	}

	public DrawingPropertyGeneStructure getPaintStructure() {
		return paintStructure;
	}

	public Double getRectangle() {
		return rectangle;
	}

	public LocationCalculator<DrawingPropertyGeneStructure> getLocationCalculator() {
		return locationCalculator;
	}

	public List<DrawingPropertySequenceElement> getSecondPaintBlocks() {
		return secondPaintBlocks;
	}

	public List<DrawingPropertySequenceElement> getPaintVirusStrainsInfoAll() {
		return paintVirusStrainsInfoAll;
	}
}
