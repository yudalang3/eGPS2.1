package module.evolview.gfamily.work.gui.browser;

import java.awt.geom.RoundRectangle2D;
import java.util.List;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyKeyDomains;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;

public class TooltiperKeyDomains extends AbstractTrackTooltiper {

    private TrackKeyDomains elementStructure;
    public static final String STARTWITH = "PF";

    public TooltiperKeyDomains(TrackKeyDomains elementStructure) {

        this.elementStructure = elementStructure;
    }

    @Override
    public boolean contains(double x, double y) {
        DrawingPropertyKeyDomains paintElements = elementStructure.getPaintElements();
        if (paintElements == null) {
            return false;
        }
        List<DrawingPropertySequenceModel> paintElementItems = paintElements.getGenomePaintElementItems();

        if (paintElementItems == null || paintElementItems.size() == 0) {
            return false;
        }

        for (DrawingPropertySequenceModel paintElementItem : paintElementItems) {
            List<DrawingPropertyElement> genomeElementModels = paintElementItem.getGenomeElementModels();
            for (DrawingPropertyElement genomeElementModel : genomeElementModels) {
                RoundRectangle2D.Double roundRect = genomeElementModel.getRoundRect();
                if (roundRect.contains(x, y)) {
                    StringBuffer sb = new StringBuffer();
                    String name = genomeElementModel.getName();
                    sb.append("Name: ").append(name).append("<br>");
                    String gene = genomeElementModel.getGene();
                    sb.append("Domain: ").append(gene).append("<br>");
                    sb.append("Start: ").append(genomeElementModel.getStartPosition()).append("<br>");
                    sb.append("End: ").append(genomeElementModel.getEndPositoin()).append("<br>");
                    toolTip = sb.toString();
                    return true;
                }
            }
        }

        return false;
    }

}
