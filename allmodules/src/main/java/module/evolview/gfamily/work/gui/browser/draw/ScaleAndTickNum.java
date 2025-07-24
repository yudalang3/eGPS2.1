package module.evolview.gfamily.work.gui.browser.draw;

/**
 * 存储刻度尺tick的数量以及间距
 */

public class ScaleAndTickNum {
	private double scale;//每个刻度之间的间距

    private int tickNum;
    
    public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public int getTickNum() {
		return tickNum;
	}

	public void setTickNum(int tickNum) {
		this.tickNum = tickNum;
	}

	
}
