package module.evolview.gfamily.work.gui.browser;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignment;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignmentItem;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyAlignmentNucleotide;

public class TooltiperAlignment extends AbstractTrackTooltiper {

    private static final String HUMANCOV2019 = "Human-CoV-2019";
    private static final String BATCOVRATG13 = "Bat-CoV-RaTG13";
    private static final String PANGOLINCOV = "Pangolin-CoV";
    private static final String HUMANSARSCOV = "Human-SARS-CoV";
    private static final String BATSARSCOV1 = "Bat-SARS-CoV1";
    private static final String BATSARSCOV2 = "Bat-SARS-CoV2";
    private TrackAlignment ncov2019GenomerAlignment;
    private static HashMap<String, String> names = new HashMap<>();

    private Rectangle2D.Double seqLocation = new Rectangle2D.Double();

    static {
        names.put(HUMANCOV2019, "NC_045512.2, Human SARS CoV 2 isolate Wuhan-Hu-1");
        names.put(BATCOVRATG13, "MN996532.1, Bat coronavirus RaTG13");
        names.put(PANGOLINCOV, "MT040334.1, Pangolin SARS-like coronavirus isolate GX-P1E");
        names.put(HUMANSARSCOV, "AY278488.2, Human SARS coronavirus BJ01");
        names.put(BATSARSCOV1, "KY417146.1, Bat SARS-like coronavirus isolate Rs4231");
        names.put(BATSARSCOV2, "MK211376.1, Bat SARS-like coronavirus isolate YN2018B");
    }

    public TooltiperAlignment(TrackAlignment ncov2019GenomerAlignment) {
        this.ncov2019GenomerAlignment = ncov2019GenomerAlignment;

    }

    @Override
    public boolean contains(double x, double y) {
        DrawingPropertyAlignment paintAlignment = ncov2019GenomerAlignment.getPaintingLocations();
        if (paintAlignment == null) {
            return false;
        }
        List<DrawingPropertyAlignmentItem> paintAlignmentItems = paintAlignment.getPaintAlignmentItems();
        if (paintAlignmentItems == null) {
            return false;
        }

        for (DrawingPropertyAlignmentItem paintAlignmentItem : paintAlignmentItems) {
            Rectangle2D.Double alignmentNameLocation = paintAlignmentItem.getAlignmentNameLocation();
            //判断焦点是否在名称上
            if (alignmentNameLocation.contains(x, y)) {
                toolTip = names.get(paintAlignmentItem.getAlignmentName());
                return true;
            }
            List<DrawingPropertyAlignmentNucleotide> sequence = paintAlignmentItem.getSequence();
            for (DrawingPropertyAlignmentNucleotide nucleotide : sequence) {
                //    Rectangle2D.Double seqLocation = nucleotide.getSeqLocation();
                seqLocation.setRect(nucleotide.getX(), nucleotide.getY(), nucleotide.getW(), nucleotide.getH());
                //判断焦点是否在碱基上
                if (seqLocation.contains(x, y)) {
                    String proteinNam = nucleotide.getProteinName();
                    int proLocation = nucleotide.getProLocation();
                    if (nucleotide.isContainGapBase()) {
                        String tooltipSuffix = " The region " + paintAlignmentItem.getAlignmentName() + " specific: <br/>" + nucleotide.getGapBase();
                        toolTip = proteinNam == null ? "Position: " + proLocation + "<br/>" + tooltipSuffix : "Position in " + proteinNam + ": " + proLocation + "<br/>" + tooltipSuffix;
                    } else {
                        toolTip = proteinNam == null ? "Position: " + proLocation : "Position in " + proteinNam + ": " + proLocation;
                    }

                    return true;
                }
            }
        }
        return false;
    }
}
