package module.evolview.gfamily.work.gui.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

import module.evolview.gfamily.work.calculator.browser.LocationCalculator;
import module.evolview.gfamily.work.gui.Triangle;
import module.evolview.gfamily.work.gui.browser.draw.AlignmentWithDerivedStatistics;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignment;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignmentItem;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignmentNucleotide;

@SuppressWarnings("serial")
public class TrackAlignment extends AbstractTrackPanel {

    private List<Integer> lastStoredGenomeIndex = new ArrayList<>();
    private CalculatorAlignment locationCalculator;
    private DrawingPropertyAlignment paintAlignment;
    private boolean drawAlignmentNucleotide;
    private double xRange;
    private Double seqLocation = new Double();
	private AlignmentWithDerivedStatistics alignmentStati;
	private int prefHeight;

    public TrackAlignment(BrowserPanel genomeMain, AlignmentWithDerivedStatistics alignmentStati) {
        super(genomeMain);
        
		this.alignmentStati = alignmentStati;
		
		int size = alignmentStati.getSequence().getAltSequence().size() + 1;
		prefHeight = size * 18;
		
		this.locationCalculator = new CalculatorAlignment(genomeMain, this);
		this.shape = new TooltiperAlignment(this);
		MouseListenerAlignment mouseListener = new MouseListenerAlignment(genomeMain);
		addMouseMotionListener(mouseListener);
		addMouseListener(mouseListener);
    }

    @Override
    public void initialize() {
        paintAlignment = locationCalculator.calculatePaintingLocations(this.drawProperties,alignmentStati, getWidth(), getHeight());
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        GeneDrawingLengthCursor drawProperties = genomeMain.getDrawProperties();
        
        int charHeight = drawProperties.getCharHeight();
        double charWidth = drawProperties.getCharWidth();

        FontMetrics fontMetrics = g2.getFontMetrics();
        int width = getWidth();
        int height = getHeight();
        if (isHighlight) {
            g2.setBackground(backgroundColor);
            g2.clearRect(0, 0, width, height);
        }
        
        if (paintAlignment == null) {
            return;
        }

        List<DrawingPropertyAlignmentItem> paintAlignmentItems = paintAlignment.getPaintAlignmentItems();

        if (paintAlignmentItems == null || paintAlignmentItems.size() == 0) {
            return;
        }
        Color oldColor = g2.getColor();
        double sequenceCenterlocation = 0;
        if (xRange > charWidth) {
            sequenceCenterlocation = xRange / 2 - charWidth / 2;
        }
        Color tempBackgroundColor = isHighlight ? backgroundColor : Color.white;

        int blinkLeftSpaceLength = LocationCalculator.BLINK_LEFT_SPACE_LENGTH;
        //绘制像素的总宽度
        double drawPixels = width - blinkLeftSpaceLength - LocationCalculator.BLINK_RIGHT_SPACE_LENGTH;
        //每个像素可以绘制核苷酸的个数。drawPixels已经是double value，所以这里不需要再转换。
        List<DrawingPropertyAlignmentNucleotide> firstSequence = paintAlignmentItems.get(0).getSequence();
        double singlePixel = firstSequence.size() / drawPixels;

        int startPosOfWindow = drawProperties.getDrawStart() - 1; // 1-based to 0-based
        if (this.lastStoredGenomeIndex.isEmpty()) {
            for (int pixelIndex = 0; pixelIndex < drawPixels - 1; pixelIndex++) {
                int nucleotideIndex = (int) (singlePixel * (pixelIndex + 0.5)); //绘制核苷酸的索引
                this.lastStoredGenomeIndex.add(nucleotideIndex + startPosOfWindow);
            }
        }

        for (DrawingPropertyAlignmentItem paintAlignmentItem : paintAlignmentItems) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            String alignmentName = paintAlignmentItem.getAlignmentName();
            Double alignmentNameLocation = paintAlignmentItem.getAlignmentNameLocation();
            int stringWidth = fontMetrics.stringWidth(alignmentName);
            float x;
            float y;
            List<DrawingPropertyAlignmentNucleotide> sequence = paintAlignmentItem.getSequence();
            if (singlePixel > 1.0d) { //一个像素可以绘制多个核苷酸
                for (int pixelIndex = 0; pixelIndex < drawPixels - 1; pixelIndex++) {
                    int leftBoundNucleotideIndex = (int) (singlePixel * (pixelIndex));
                    int rightBoundNextNucleotideIndex = (int) (singlePixel * (pixelIndex + 1));
                    int nucleotideIndex = (int) (singlePixel * (pixelIndex + 0.5)); //绘制核苷酸的索引
                    if (pixelIndex < this.lastStoredGenomeIndex.size()) {
                        int storedGenomeIndex = this.lastStoredGenomeIndex.get(pixelIndex);

                        if (leftBoundNucleotideIndex + startPosOfWindow <= storedGenomeIndex && storedGenomeIndex < rightBoundNextNucleotideIndex + startPosOfWindow) {
                            // 继续使用原来的索引
                            nucleotideIndex = storedGenomeIndex - startPosOfWindow;
                        } else {
                            // 使用新的索引，并且更新原来存储的索引
                            this.lastStoredGenomeIndex.set(pixelIndex, new Integer(nucleotideIndex + startPosOfWindow));
                        }
                    } else {
                        this.lastStoredGenomeIndex.add(nucleotideIndex + startPosOfWindow);
                    }
                    DrawingPropertyAlignmentNucleotide nucleotide = sequence.get(nucleotideIndex);
                    //    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f));                 // 1.0f为透明度 ，值从0-1.0，依次变得不透明
                    //Rectangle2D.Double seqLocation = nucleotide.getSeqLocation();
                    seqLocation.setRect(nucleotide.getX(), nucleotide.getY(), 1, nucleotide.getH());
                    //   double v = singlePixel > 1 ? 1 : seqLocation.getWidth();
                    g2.setColor(nucleotide.getBaseColor());
                    g2.fill(seqLocation);
                }
            } else {
                for (DrawingPropertyAlignmentNucleotide nucleotide : sequence) {
                    //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f));// 1.0f为透明度 ，值从0-1.0，依次变得不透明
                    String charSeq = nucleotide.getCharSeq();
                    //  Rectangle2D.Double seqLocation = nucleotide.getSeqLocation();
                    seqLocation.setRect(nucleotide.getX(), nucleotide.getY(), nucleotide.getW(), nucleotide.getH());
                    g2.setColor(nucleotide.getBaseColor());
                    g2.fill(seqLocation);
                    if (drawAlignmentNucleotide) {
                        g2.setColor(oldColor);
                        float x1 = (float) (seqLocation.getX() + sequenceCenterlocation);
                        float y1 = (float) (seqLocation.getY() + charHeight - 3);
                        g2.drawString(charSeq, x1, y1);
                        //    System.out.println("charSeq  : " + charSeq + "  proLocation : " + sequence.getProLocation());
                        if (nucleotide.isContainGapBase()) {
                            Triangle triangle = nucleotide.getTriangle();
                            triangle.draw(g2);
                            triangle.fill(g2);
                        }
                    }
                }
            }
            //   g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
            g2.setColor(tempBackgroundColor);
            //当blinkleftspacelength >= stringWidth + 2时("2"表示ALingmentName与sequence之间留两个单位的空隙),表示留白区可以绘制全ALingmentName
            //绘制SequenceName
            if (blinkLeftSpaceLength >= stringWidth + 4) {
                int tempStringWidth = blinkLeftSpaceLength - stringWidth;
                x = (float) (alignmentNameLocation.getX() + tempStringWidth - 4);
                y = (float) (alignmentNameLocation.getY() + charHeight - 3);
                g2.fill(alignmentNameLocation);
                g2.setColor(oldColor);
                g2.drawString(alignmentName, x, y);
            } else {
                //TODO截取AlignmentName字符串
                String tempAlignmentName = alignmentName.substring(0, 12) + "...";
                stringWidth = fontMetrics.stringWidth(tempAlignmentName);
                int tempStringWidth = blinkLeftSpaceLength - stringWidth;
                x = (float) (alignmentNameLocation.getX() + tempStringWidth - 4);
                y = (float) (alignmentNameLocation.getY() + charHeight - 3);
                g2.fill(alignmentNameLocation);
                g2.setColor(oldColor);
                g2.drawString(tempAlignmentName, x, y);
            }
            // g2.drawString(alignmentName, (float) alignmentNameLocation.getX(), (float) (alignmentNameLocation.getY() + charHeight - 3));
        }
        //  g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
        g2.setColor(tempBackgroundColor);
        g2.fill(new Double(width - locationCalculator.BLINK_RIGHT_SPACE_LENGTH, 0, locationCalculator.BLINK_RIGHT_SPACE_LENGTH, height));

        g2.dispose();

    }


    @Override
    public Dimension getMaximumSize() {
        return new Dimension(32767, prefHeight);
    }

    @Override
    public Dimension getPreferredSize() {
    	 return new Dimension(10, prefHeight);
    }
    public void isDrawAlignmentNucleotide(boolean drawAlignmentNucleotide) {
        this.drawAlignmentNucleotide = drawAlignmentNucleotide;
    }

    public void setSingleSequenceWidth(double xRange) {
        this.xRange = xRange;
    }

    public DrawingPropertyAlignment getPaintingLocations() {
        return paintAlignment;
    }
}
