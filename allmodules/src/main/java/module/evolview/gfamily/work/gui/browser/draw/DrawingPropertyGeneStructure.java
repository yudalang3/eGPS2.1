package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class DrawingPropertyGeneStructure {


    private String trackName;
    
    private String geneName;
    private String sequenceElementName;

    private float trackNameXLocation;
    private float trackNameYLocation;

    private Line2D.Double tickLine;

    private List<AxisTicks<?>> xAxisLocations;

    private List<DrawingPropertySequenceElement> firstPaintBlockColors;

    private List<DrawingPropertySequenceElement> secondPaintBlockColors;

    private List<Rectangle2D.Double> secondBlankBlock;

    private Line2D.Double starSlashtLine;

    private Line2D.Double endSlashtLine;

    private DrawingPropertyRegionData regionData;
    

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public float getTrackNameXLocation() {
		return trackNameXLocation;
	}

	public void setTrackNameXLocation(float trackNameXLocation) {
		this.trackNameXLocation = trackNameXLocation;
	}

	public float getTrackNameYLocation() {
		return trackNameYLocation;
	}

	public void setTrackNameYLocation(float trackNameYLocation) {
		this.trackNameYLocation = trackNameYLocation;
	}

	public Line2D.Double getTickLine() {
		return tickLine;
	}

	public void setTickLine(Line2D.Double tickLine) {
		this.tickLine = tickLine;
	}

	public List<AxisTicks<?>> getXAxisLocations() {
		return xAxisLocations;
	}

	public void setXAxisLocations(List<AxisTicks<?>> xAxisLocations) {
		this.xAxisLocations = xAxisLocations;
	}

	public List<DrawingPropertySequenceElement> getFirstPaintBlockColors() {
		return firstPaintBlockColors;
	}

	public void setFirstPaintBlockColors(List<DrawingPropertySequenceElement> firstPaintBlockColors) {
		this.firstPaintBlockColors = firstPaintBlockColors;
	}

	public List<DrawingPropertySequenceElement> getSecondPaintBlockColors() {
		return secondPaintBlockColors;
	}

	public void setSecondPaintBlockColors(List<DrawingPropertySequenceElement> secondPaintBlockColors) {
		this.secondPaintBlockColors = secondPaintBlockColors;
	}

	public List<Rectangle2D.Double> getSecondBlankBlock() {
		return secondBlankBlock;
	}

	public void setSecondBlankBlock(List<Rectangle2D.Double> secondBlankBlock) {
		this.secondBlankBlock = secondBlankBlock;
	}

	public Line2D.Double getStarSlashtLine() {
		return starSlashtLine;
	}

	public void setStarSlashtLine(Line2D.Double starSlashtLine) {
		this.starSlashtLine = starSlashtLine;
	}

	public Line2D.Double getEndSlashtLine() {
		return endSlashtLine;
	}

	public void setEndSlashtLine(Line2D.Double endSlashtLine) {
		this.endSlashtLine = endSlashtLine;
	}

	public DrawingPropertyRegionData getRegionData() {
		return regionData;
	}

	public void setRegionData(DrawingPropertyRegionData regionData) {
		this.regionData = regionData;
	}

	public String getGeneName() {
		return geneName;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	public String getSequenceElementName() {
		return sequenceElementName;
	}

	public void setSequenceElementName(String sequenceElementName) {
		this.sequenceElementName = sequenceElementName;
	}
    
    

}
