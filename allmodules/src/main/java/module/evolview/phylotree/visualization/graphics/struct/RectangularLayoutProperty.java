package module.evolview.phylotree.visualization.graphics.struct;

public class RectangularLayoutProperty {

	/**
	 * 默认是101,就是两条直线：
	 * 
	 * <pre>
	 * 
	 *      |------.
	 * -----|.
	 *      |------
	 *      
	 * 这就是当值为101时，连接两个点之间的情况。
	 * 当值为0时，就是一条线直接连接过去！
	 * 
	 * </pre>
	 */
	int curvature = 0;
	
	public int getCurvature() {
		return curvature;
	}

	public void setCurvature(int curvature) {
		this.curvature = curvature;
	}

	
}
