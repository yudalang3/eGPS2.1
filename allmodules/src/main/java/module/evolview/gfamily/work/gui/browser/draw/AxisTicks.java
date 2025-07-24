package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * 绘制横向刻度尺的信息
 */
public class AxisTicks<T> {

    private Line2D.Double line; // tick线段

    private T axisValue; // 刻度的值(横向刻度尺为整形,纵向刻度尺为浮点型)

    private Point2D.Double valueLocation; // 刻度值得位置
    
    public AxisTicks() {
	}

	public Line2D.Double getLine() {
		return line;
	}

	public void setLine(Line2D.Double line) {
		this.line = line;
	}

	public T getAxisValue() {
		return axisValue;
	}

	public void setAxisValue(T axisValue) {
		this.axisValue = axisValue;
	}

	public Point2D.Double getValueLocation() {
		return valueLocation;
	}

	public void setValueLocation(Point2D.Double valueLocation) {
		this.valueLocation = valueLocation;
	}

    
}
