package module.evolview.gfamily.work.gui.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.browser.draw.AlignmentWithDerivedStatistics;
import module.evolview.gfamily.work.gui.browser.draw.AxisTicks;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPolyLine;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPolylinePlot;

@SuppressWarnings("serial")
public class TrackSeqSimilarity extends AbstractTrackPanel {

    private CalculatorSeqSimilarity locationCalculator;

    private DrawingPropertyPolylinePlot polylinePlot;

    /**
     * This is provide by the alignment 
     */
	private AlignmentWithDerivedStatistics alignmentStati;

    public TrackSeqSimilarity(BrowserPanel genomeMain, AlignmentWithDerivedStatistics alignmentStati) {
        super(genomeMain);
        this.locationCalculator = new CalculatorSeqSimilarity();
        this.alignmentStati = alignmentStati;

        shape = new TooltiperSeqSimilarity(this);

        MouseListenerSeqSimilarity polyLinePlotMouseListener = new MouseListenerSeqSimilarity(genomeMain);
        addMouseMotionListener(polyLinePlotMouseListener);
        addMouseListener(polyLinePlotMouseListener);

    }

    @Override
    public void initialize() {

        polylinePlot = locationCalculator.calculatePaintingLocations(drawProperties, alignmentStati, getWidth(), getHeight());

    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(32767, 110);
    }

    @Override
    public Dimension getPreferredSize() {
    	 return new Dimension(10, 110);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        FontMetrics fontMetrics = g2.getFontMetrics();

        int width = getWidth();

        int height = getHeight();

        if (isHighlight) {
            g2.setBackground(backgroundColor);
            g2.clearRect(0, 0, width, height);
        }

        if (polylinePlot == null) {
            return;
        }
        g2.drawString(polylinePlot.getTrackName(), polylinePlot.getTrackNameXLocation(), polylinePlot.getTrackNameYLocation());

        paintNcov2019GenomeBar(g2, fontMetrics, polylinePlot);

        g2.dispose();
    }

    private void paintNcov2019GenomeBar(Graphics2D g2, FontMetrics fontMetrics, DrawingPropertyPolylinePlot polylinePlot) {
        Line2D.Double xLine = polylinePlot.getxLine();
        Line2D.Double yLine = polylinePlot.getyLine();
        float height = fontMetrics.getHeight();
       
        List<AxisTicks<Integer>> xAxisLocations = polylinePlot.getxAxisLocations();
        for (AxisTicks<Integer> scale : xAxisLocations) {
            g2.draw(scale.getLine());
            String xAxisValue = GeneFamilyController.HUMAN_DECIMAL_INTEGER_FORMAT.format(scale.getAxisValue());
            int stringWidth = fontMetrics.stringWidth(xAxisValue);
            g2.drawString(xAxisValue, (float) (scale.getValueLocation().getX() - stringWidth / 2.0),
                    (float) scale.getValueLocation().getY());
        }
        List<AxisTicks<Double>> yAxisLocations = polylinePlot.getyAxisLocations();
        for (AxisTicks<Double> scale : yAxisLocations) {
            g2.draw(scale.getLine());
            String valueOf = String.valueOf(scale.getAxisValue());
            int stringWidth = fontMetrics.stringWidth(valueOf);
            g2.drawString(valueOf, (float) scale.getValueLocation().getX() - stringWidth - 5,
                    (float) (scale.getValueLocation().getY() + 3));
        }
        List<DrawingPropertyPolyLine> paintLines = polylinePlot.getNcov2019GenomePaintPolyLine();
        
        float startLineLegendX = paintLines.get(0).getStartLineLegendX();
        float startLineLegendY = paintLines.get(0).getStartLineLegendY() - height;
        for (DrawingPropertyPolyLine paintLine : paintLines) {
            Color color = paintLine.getColor();
            g2.setColor(color);
            List<Line2D.Double> rect = paintLine.getRect();
            for (Line2D.Double aDouble : rect) {
                g2.draw(aDouble);
            }
            // Line2D.Float line = new Line2D.Float();
            Rectangle2D.Float line = new Rectangle2D.Float();
            //    line.setLine(startLineLegendX, tempStartLineLegendY, startLineLegendX + 15, tempStartLineLegendY);

            float lineWidth = 20;
            float lineHeight = 2;
            line.setRect(startLineLegendX, startLineLegendY - 5, lineWidth, lineHeight);
            String name = paintLine.getName();
            float tempStartLineLegendX = line.x + lineWidth + 5;

            g2.drawString(name, tempStartLineLegendX, startLineLegendY);
            g2.fill(line);
            //   g2.draw(line);
            startLineLegendX += fontMetrics.stringWidth(name) + lineWidth + 20;
        }
        
        
        g2.draw(xLine);
        g2.draw(yLine);
    }
}
