package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Point;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * 存错Ncov2019GenomeElementModel的集合信息,但是本身也可能会有多条信息
 */
public class DrawingPropertySequenceModel {
	// This is for painting
    private ImmutableList<DrawingPropertyElement> genomeElementModels;
    /**
     * This is the original input values
     * 有空可以重构一下，因为原始的输入不需要 额外的绘制属性
     */
    private ImmutableList<DrawingPropertyElement> originalSeqElementModels;
    private String header;
    
    private String sequenceElementModelName;
    
    /**
     * 这个坐标主要用来定位前面的名字：例如Pfam或者Panther的来源 Track
     */
    private Point subTrackLocator;
    
    
	public List<DrawingPropertyElement> getGenomeElementModels() {
		return genomeElementModels;
	}
	public void setGenomeElementModels(List<DrawingPropertyElement> genomeElementModels) {
		this.genomeElementModels = ImmutableList.copyOf(genomeElementModels);
	}
	
	public List<DrawingPropertyElement> getOrigianlSeqElementModels() {
		return originalSeqElementModels;
	}
	public void setOrigianlSeqElementModels(List<DrawingPropertyElement> genomeElementModels) {
		this.originalSeqElementModels = ImmutableList.copyOf(genomeElementModels);
	}
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getSequenceElementModelName() {
		return sequenceElementModelName;
	}
	public void setSequenceElementModelName(String sequenceElementModelName) {
		this.sequenceElementModelName = sequenceElementModelName;
	}
	public Point getSubTrackLocator() {
		return subTrackLocator;
	}
	public void setSubTrackLocator(Point subTrackLocator) {
		this.subTrackLocator = subTrackLocator;
	}
    
}
