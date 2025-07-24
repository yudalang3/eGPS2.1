package module.evolview.gfamily.work.gui.browser;

import java.awt.geom.Rectangle2D;
import java.util.List;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimerSet;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimers;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPrimersNucleotide;

public class TooltiperPrimers extends AbstractTrackTooltiper {

    private TrackPrimers primers;

    public TooltiperPrimers(TrackPrimers primers) {

        this.primers = primers;
    }

    @Override
    public boolean contains(double x, double y) {

        DrawingPropertyPrimerSet primerSet = primers.getPrimerSet();
        if (primerSet == null) {
            return false;
        }
        Rectangle2D.Double rect = new Rectangle2D.Double();
        List<DrawingPropertyPrimers> primers = primerSet.getPrimers();


        for (DrawingPropertyPrimers primer : primers) {
            if (primer.isReferencePrimers()) {
                continue;
            }
            List<DrawingPropertyPrimersNucleotide> forwardPrimersNucleotides = primer.getForwardPrimersNucleotides();

            for (DrawingPropertyPrimersNucleotide forwardPrimersNucleotide : forwardPrimersNucleotides) {
                rect.setRect(forwardPrimersNucleotide.getX(), forwardPrimersNucleotide.getY(), forwardPrimersNucleotide.getW(), forwardPrimersNucleotide.getH());
                if (rect.contains(x, y)) {
                    StringBuffer sb = new StringBuffer();
                    String institution = primer.getInstitution();
                    sb.append("Institution: ").append(institution).append("<br>");
                    String gene = primer.getGene();
                    sb.append("Target gene: ").append(gene).append("<br>");
                    String forward = primer.getForward();
                    sb.append("Forward primer: 5’-").append(forward).append("-3’").append("<br>");

                    String reverse = primer.getReverse();
                    sb.append("Reverse primer: 5’-").append(strReverseWithXor(reverse)).append("-3’").append("<br>");

                    int size = primer.getREnd() - primer.getFStart() + 1;
                    sb.append("PCR product size: ").append(size);
                    //  toolTip = sb.toString();
                    toolTip = sb.toString();
                    return true;
                }
            }
        }

        return false;
    }

    public static String strReverseWithXor(String string) {
        if (string == null || string.length() == 0) return string;
        char[] array = string.toCharArray();
        int length = string.length() - 1;
        for (int i = 0; i < length; i++, length--) {
            array[i] ^= array[length];
            array[length] ^= array[i];
            array[i] ^= array[length];
        }
        return new String(array);
    }
}
