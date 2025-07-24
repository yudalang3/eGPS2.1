package module.evolview.gfamily.work.gui.browser;

import java.awt.geom.Rectangle2D;
import java.util.List;

import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceElement;

public class TooltiperGeneStructure extends AbstractTrackTooltiper {

    private TrackGeneStructure virusGenomeStructure;

    public TooltiperGeneStructure(TrackGeneStructure virusGenomeStructure) {
        this.virusGenomeStructure = virusGenomeStructure;
    }

    @Override
    public boolean contains(double x, double y) {
        List<DrawingPropertySequenceElement> secondPaintBlocks = virusGenomeStructure.getSecondPaintBlocks();
        if (secondPaintBlocks == null) {
            return false;
        }
        for (DrawingPropertySequenceElement paintBlock : secondPaintBlocks) {
            Rectangle2D.Double rectangle = paintBlock.getRectangle();
            if (rectangle.contains(x, y)) {
            	
            	sbuilder.setLength(0);
            	String geneName = paintBlock.getGeneName();
            	sbuilder.append("Name: ").append(geneName).append("<br>");
            	sbuilder.append("Start pos: ").append(paintBlock.getStartPose()).append(" endPose: ");
            	sbuilder.append((paintBlock.getEndPose() - 1));
                toolTip = sbuilder.toString();
               
                return true;
            }
        }
        return false;
    }

}
