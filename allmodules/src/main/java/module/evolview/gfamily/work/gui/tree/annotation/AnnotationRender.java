package module.evolview.gfamily.work.gui.tree.annotation;

import java.awt.Graphics2D;

import module.evolview.gfamily.work.model.tree.GraphicsNode;

/**
 * 这里其实应该有两个过程：
 * 1. configuration作为一个配置
 * 2. 绘制
 * 
 *  但是配置是多样化的，对于不同的layout结果是不一样的！
 *
 */
public abstract class AnnotationRender {
	protected boolean shouldConfigurateAndPaint = true;

	protected GraphicsNode node;
	protected int nodeID;
	
	public AnnotationRender(GraphicsNode node) {
		if (node != null) {
			this.nodeID = node.getID();
			this.node = node;
		}
	}
	/**
	 * 这个方法返回 true才会绘制。
	 * @return
	 */
	public boolean shouldConfigurateAndPaint() {
		return shouldConfigurateAndPaint;
	}
	/**
	 * 配置好之后就绘制注释。但是配置是多样化的，对于不同的layout结果是不一样的！
	 * @param g2d
	 */
	public abstract void drawAnnotation(Graphics2D g2d);
	
	/**
	 * 下面两个方法是为克隆用的，因为每次克隆之后需要重新改变注释的 Graphics node！
	 */
	public void setShouldNotConfigurate() {
		shouldConfigurateAndPaint = false;
	}
	
	public void substituteNodeIfMeetNodeID(GraphicsNode gNode) {
		if (gNode.getID() == nodeID) {
			this.node = gNode;
			shouldConfigurateAndPaint = true;
		}
	}
	
	public void assignNullIfShouldNotConfig() {
		if (!shouldConfigurateAndPaint) {
			this.node = null;
		}
	}
	
	public void setNode(GraphicsNode node) {
		this.node = node;
		this.nodeID = node.getID();
	}
	
}
