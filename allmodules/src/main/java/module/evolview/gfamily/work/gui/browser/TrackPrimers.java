package module.evolview.gfamily.work.gui.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.List;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimers;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimersNucleotide;
import module.evolview.model.AlignmentGetSequence;

/**
 * 绘制ElementStructure信息,可能会有多行
 */
@SuppressWarnings("serial")
public class TrackPrimers extends AbstractTrackPanel {

	private CalculatorPrimers calculatorPrimers;
	private DrawingPropertyPrimerSet primerSet;
	private double xRange;

	public TrackPrimers(BrowserPanel genomeMain, DrawingPropertyPrimerSet drawingPropertyPrimerSet,
			AlignmentGetSequence alignmentGetSequence) {
		super(genomeMain);

		this.calculatorPrimers = new CalculatorPrimers(genomeMain, drawingPropertyPrimerSet, alignmentGetSequence);

		this.shape = new TooltiperPrimers(this);
		MouseListenerPrimers structureMouseListener = new MouseListenerPrimers(genomeMain);
		addMouseMotionListener(structureMouseListener);
		addMouseListener(structureMouseListener);
		addNotify();
	}

	public CalculatorPrimers getCalculatorPrimers() {
		return calculatorPrimers;
	}
	
	@Override
	public void initialize() {
		primerSet = calculatorPrimers.calculatePaintingLocations(drawProperties, getWidth(), getHeight());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		GeneDrawingLengthCursor drawProperties = genomeMain.getDrawProperties();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int width = getWidth();
		int height = getHeight();
		double charWidth = drawProperties.getCharWidth();
		int charHeight = drawProperties.getCharHeight();
		if (isHighlight) {
			g2.setBackground(backgroundColor);
			g2.clearRect(0, 0, width, height);
		}
		if (primerSet == null) {
			return;
		}
		g2.drawString(primerSet.getTrackName(), primerSet.getTrackNameXLocation(), primerSet.getTrackNameYLocation());

		double sequenceCenterlocation = 0;
		if (xRange > charWidth) {
			sequenceCenterlocation = xRange / 2 - charWidth / 2;
		}
		Rectangle2D.Double location = new Rectangle2D.Double();

		List<DrawingPropertyPrimers> primers = primerSet.getPrimers();
		FontMetrics fontMetrics = g2.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth("W");

		for (DrawingPropertyPrimers primer : primers) {
			Color primersColor = primer.getPrimersColor();
			List<DrawingPropertyPrimersNucleotide> primersNucleotides = primer.getForwardPrimersNucleotides();
			for (DrawingPropertyPrimersNucleotide primersNucleotide : primersNucleotides) {
				String base = primersNucleotide.getBase();
				g2.setColor(primersColor);
				location.setRect(primersNucleotide.getX(), primersNucleotide.getY(), primersNucleotide.getW(),
						primersNucleotide.getH());
				g2.fill(location );
				if (drawBase ) {
					float x1 = (float) (primersNucleotide.getX() + sequenceCenterlocation);
					float y1 = (float) (primersNucleotide.getY() + charHeight - 3);
					g2.setColor(Color.WHITE);
					g2.drawString(base, x1, y1);
				}
			}
		}
		g2.dispose();
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(32767, 125);
	}
	
	@Override
    public Dimension getPreferredSize() {
    	 return new Dimension(10, 125);
    }

	private boolean drawBase;

	public DrawingPropertyPrimerSet getPrimerSet() {
		return primerSet;
	}

	public void setXRange(double xRange) {
		this.xRange = xRange;
	}

	public void setDrawBase(boolean drawBase) {
		this.drawBase = drawBase;
	}
}
