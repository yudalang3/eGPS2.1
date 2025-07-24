package module.evolview.gfamily.work.gui.browser.draw;

import java.util.List;

/**
 * 绘制Element的信息
 * 
 * 这里存储的是整个Track的绘制信息，subTrack 在 DrawingPropertySequenceModel 这个类里面
 */
public class DrawingPropertyKeyDomains {

    private List<DrawingPropertySequenceModel> genomePaintElementItems;
    private String trackName;
    private float trackNameXLocation;
    private float trackNameYLocation;
    
    
	public List<DrawingPropertySequenceModel> getGenomePaintElementItems() {
		return genomePaintElementItems;
	}
	public void setGenomePaintElementItems(List<DrawingPropertySequenceModel> genomePaintElementItems) {
		this.genomePaintElementItems = genomePaintElementItems;
	}
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

    
    
}
