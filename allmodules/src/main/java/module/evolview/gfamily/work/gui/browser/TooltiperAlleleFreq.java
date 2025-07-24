package module.evolview.gfamily.work.gui.browser;

import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyLine;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyPaintBar;

public class TooltiperAlleleFreq extends AbstractTrackTooltiper {

    private TrackAlleleFreq ncov2019GenomeBarPlot;

    private int pos;

    public TooltiperAlleleFreq(TrackAlleleFreq ncov2019GenomeBarPlot) {
        this.ncov2019GenomeBarPlot = ncov2019GenomeBarPlot;
    }

    @Override
    public boolean contains(double x, double y) {
        DrawingPropertyPaintBar genomePaintBar = ncov2019GenomeBarPlot.getPaintBar();

        if (genomePaintBar == null) {
            return false;
        }
        List<DrawingPropertyLine> paintLines = genomePaintBar.getNcov2019GenomePaintLine();
        double maxHeight = genomePaintBar.getMaxHeight();
        Double aDouble = new Double();

        for (DrawingPropertyLine paintLine : paintLines) {
            Double rect = paintLine.getRect();
            // aDouble.setRect(rect.getX(), blinkTopSpaceLength, rect.getWidth() , maxHeight);
//            aDouble.setRect(rect.getX() - 2, rect.getY() - 5, rect.getWidth() + 4, maxHeight + 5);
            
            //识别区高度都加了3，为了让频率极低的bar的tooltip显示能更加灵敏
            aDouble.setRect(rect.getX()-2 , rect.getY()-3, rect.getWidth()+4, rect.getHeight()+3);

            if (aDouble.contains(x, y)) {
//                GeneCalculatedDerivedStatistics dataModel = ncov2019GenomeBarPlot.getDataModel();
//                MutationFrequencyTooltip mutationFrequencyTooltip = dataModel.getMutationFrequencyTooltip();
//                toolTip = mutationFrequencyTooltip.getTooltipString(paintLine.getPos());
                toolTip="";
                this.pos = paintLine.getPos();
                return true;
            }
        }


        return false;
    }

    public int getPos() {
        return pos;
    }
}
