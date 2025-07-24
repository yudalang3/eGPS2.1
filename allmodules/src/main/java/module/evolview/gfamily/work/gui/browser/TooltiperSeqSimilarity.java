package module.evolview.gfamily.work.gui.browser;

public class TooltiperSeqSimilarity extends AbstractTrackTooltiper {

    private TrackSeqSimilarity ncov2019GenomePolylinePlot;

    public TooltiperSeqSimilarity(TrackSeqSimilarity ncov2019GenomePolylinePlot) {

        this.ncov2019GenomePolylinePlot = ncov2019GenomePolylinePlot;
    }

    @Override
    public boolean contains(double x, double y) {

        return false;
    }

}
