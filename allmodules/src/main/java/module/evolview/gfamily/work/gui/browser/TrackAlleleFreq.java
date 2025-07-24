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
import module.evolview.gfamily.work.gui.browser.draw.AxisTicks;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyLine;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPaintBar;

@SuppressWarnings("serial")
public class TrackAlleleFreq extends AbstractBarPlot {

    private CalculatorAlleleFreq locationCalculator;

    private DrawingPropertyPaintBar paintBar;

	private List<Double> listOfScore;

    public TrackAlleleFreq(BrowserPanel genomeMain, List<Double> listOfScore) {
        super(genomeMain);
        
        this.listOfScore = listOfScore;
        this.locationCalculator = new CalculatorAlleleFreq(genomeMain.getGeneStructureInfo());

        shape = new TooltiperAlleleFreq(this);

        MouseListenerAlleleFreq barPlotMouseListener = new MouseListenerAlleleFreq(genomeMain);

        addMouseMotionListener(barPlotMouseListener);

        addMouseListener(barPlotMouseListener);

    }

    @Override
    public void initialize() {
    	
        paintBar = locationCalculator.calculatePaintingLocations(drawProperties,listOfScore, getWidth(), getHeight());
    }

    @Override
    public Dimension getMaximumSize() {

        //   return new Dimension(32767, 90);
        return new Dimension(32767, 60);
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
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        
        FontMetrics fontMetrics = g2.getFontMetrics();
        int width = getWidth();

        int height = getHeight();

        if (isHighlight) {
            g2.setBackground(backgroundColor);
            g2.clearRect(0, 0, width, height);
        }

//        if (genomeMain.isReCalculator()) {
//
//            paintBar = locationCalculator.calculatePaintingLocations(drawProperties, dataModel, width, height);
//        }

        if (paintBar == null) {
            return;
        }
        g2.drawString(paintBar.getTrackName(), paintBar.getTrackNameXLocation(), paintBar.getTrackNameYLocation());
        Rectangle2D.Double constrainedFromRectangle = paintBar.getConstrainedFromRectangle();
        Color oldColor = g2.getColor();
        if (constrainedFromRectangle != null) {
            g2.setColor(new Color(192, 192, 192));
            g2.fill(constrainedFromRectangle);
            g2.setColor(oldColor);
            g2.draw(constrainedFromRectangle);
            List<Line2D.Double> constrainedFromLines = paintBar.getConstrainedFromLines();
            if (constrainedFromLines != null) {
                for (Line2D.Double line : constrainedFromLines) {
                    g2.draw(line);
                }
            }
        }
        Rectangle2D.Double constrainedToRectangle = paintBar.getConstrainedToRectangle();
        if (constrainedToRectangle != null) {
            g2.setColor(new Color(192, 192, 192));
            g2.fill(constrainedToRectangle);
            g2.setColor(oldColor);
            g2.draw(constrainedToRectangle);
            List<Line2D.Double> constrainedToLines = paintBar.getConstrainedToLines();
            if (constrainedToLines != null) {
                for (Line2D.Double line : constrainedToLines) {
                    g2.draw(line);
                }
            }
        }

        paintNcov2019GenomeBar(g2, fontMetrics, paintBar);

        g2.dispose();
    }

    private void paintNcov2019GenomeBar(Graphics2D g2, FontMetrics fontMetrics, DrawingPropertyPaintBar paintBar) {

        Line2D.Double xLine = paintBar.getxLine();
        Line2D.Double yLine = paintBar.getyLine();


        List<AxisTicks<Integer>> xAxisLocations = paintBar.getxAxisLocations();

        for (AxisTicks<Integer> scale : xAxisLocations) {
            g2.draw(scale.getLine());

            String xAxisValue = GeneFamilyController.HUMAN_DECIMAL_INTEGER_FORMAT.format(scale.getAxisValue());
            int stringWidth = fontMetrics.stringWidth(xAxisValue);
            g2.drawString(xAxisValue, (float) (scale.getValueLocation().getX() - stringWidth / 2.0),
                    (float) scale.getValueLocation().getY());
        }

        List<AxisTicks<Double>> yAxisLocations = paintBar.getyAxisLocations();

        for (AxisTicks<Double> scale : yAxisLocations) {
            g2.draw(scale.getLine());

            String valueOf = String.valueOf(scale.getAxisValue());
           // System.out.println(scale.getAxisValue());

            int stringWidth = fontMetrics.stringWidth(valueOf);

            g2.drawString(valueOf, (float) scale.getValueLocation().getX() - stringWidth - 5,
                    (float) (scale.getValueLocation().getY() + 3));
        }

        List<DrawingPropertyLine> paintLines = paintBar.getNcov2019GenomePaintLine();

        Color oldColor = g2.getColor();

        for (DrawingPropertyLine paintLine : paintLines) {

            Rectangle2D.Double rect = paintLine.getRect();

            int selectPostion = drawProperties.getSelectPostion();
            int pos = paintLine.getPos();

            // System.out.println(selectPostion + " " + pos);

            // 去掉判断，直接显示颜色。
            //if (selectPostion == pos) {
                // System.out.println(paintLine.getColor());
                g2.setColor(paintLine.getColor());
            //}
            g2.draw(rect);
            g2.fill(rect);
            g2.setColor(oldColor);
        }
        //坐标轴最后画，防止bar覆盖了坐标轴
        g2.draw(xLine);
        g2.draw(yLine);
    }

    public DrawingPropertyPaintBar getPaintBar() {
        return paintBar;
    }
}
