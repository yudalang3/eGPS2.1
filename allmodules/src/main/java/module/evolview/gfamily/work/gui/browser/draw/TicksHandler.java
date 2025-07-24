package module.evolview.gfamily.work.gui.browser.draw;

public class TicksHandler {

	private int[] scalesX = { 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1 };
	private int minTickNumX = 12;
	private int maxTickNumX = 24;
	private double[] scalesY = { 1.0, 0.8, 0.6, 0.4, 0.2, 0.1 ,0.01,0.001};
	private int[] tickNumY = { 5, 4, 3, 2, 2, 1 ,1 ,1 };
	
	
	
	public TicksHandler() {
		super();
	}

	public int[] getScalesX() {
		return scalesX;
	}

	public void setScalesX(int[] scalesX) {
		this.scalesX = scalesX;
	}

	public int getMinTickNumX() {
		return minTickNumX;
	}

	public void setMinTickNumX(int minTickNumX) {
		this.minTickNumX = minTickNumX;
	}

	public int getMaxTickNumX() {
		return maxTickNumX;
	}

	public void setMaxTickNumX(int maxTickNumX) {
		this.maxTickNumX = maxTickNumX;
	}

	public double[] getScalesY() {
		return scalesY;
	}

	public void setScalesY(double[] scalesY) {
		this.scalesY = scalesY;
	}

	public int[] getTickNumY() {
		return tickNumY;
	}

	public void setTickNumY(int[] tickNumY) {
		this.tickNumY = tickNumY;
	}

	

}
