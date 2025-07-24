package module.evolview.phylotree.visualization.graphics.struct;

public class SprialLayoutProperty {

	/** 现在我们定死，初始的就是0*/
	private int globalStartDegree = 0;
	/** 现在这个就是最大旋转角度！*/
	private int globalExtendingDegree = 630;
	
	private double betaFactor = 1;
	/** 包括alpha与beta型的螺旋化,这个gap size不一定是像素，也要可能是一个系数！*/
	private int gapSize = 10;
	
	
	public int getGlobalStartDegree() {
		return globalStartDegree;
	}
	public void setGlobalStartDegree(int globalStartDegree) {
		this.globalStartDegree = globalStartDegree;
	}
	
	/** 现在这个就是最大旋转角度！*/
	public int getGlobalExtendingDegree() {
		return globalExtendingDegree;
	}
	public void setGlobalExtendingDegree(int globalExtendingDegree) {
		this.globalExtendingDegree = globalExtendingDegree;
	}
	public void setBetaFactor(double d) {
		betaFactor = d;
	}
	
	public double getBetaFactor() {
		return betaFactor;
	}
	public int getGapSize() {
		return gapSize;
	}
	public void setGapSize(int gapSize) {
		this.gapSize = gapSize;
	}
	
	
	
}
