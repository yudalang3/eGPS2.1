package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * 存储一行alignment信息
 */
public class DrawingPropertyAlignmentItem {

    private String alignmentName;

    private Rectangle2D.Double alignmentNameLocation;//alignment名称位置

    private List<DrawingPropertyAlignmentNucleotide> sequence;//sequences序列信息

	public String getAlignmentName() {
		return alignmentName;
	}

	public void setAlignmentName(String alignmentName) {
		this.alignmentName = alignmentName;
	}

	public Rectangle2D.Double getAlignmentNameLocation() {
		return alignmentNameLocation;
	}

	public void setAlignmentNameLocation(Rectangle2D.Double alignmentNameLocation) {
		this.alignmentNameLocation = alignmentNameLocation;
	}

	public List<DrawingPropertyAlignmentNucleotide> getSequence() {
		return sequence;
	}

	public void setSequence(List<DrawingPropertyAlignmentNucleotide> sequence) {
		this.sequence = sequence;
	}

    
}
