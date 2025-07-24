package module.evolview.gfamily.work.gui.browser.draw;

import java.util.List;

/**
 * 绘制引物的信息
 */
public class DrawingPropertyPrimerSet {

    private String trackName;
    private float trackNameXLocation;
    private float trackNameYLocation;
    private String header;

  //primers信息
    private List<DrawingPropertyPrimers> primers;

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

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<DrawingPropertyPrimers> getPrimers() {
		return primers;
	}

	public void setPrimers(List<DrawingPropertyPrimers> primers) {
		this.primers = primers;
	}

    
}
