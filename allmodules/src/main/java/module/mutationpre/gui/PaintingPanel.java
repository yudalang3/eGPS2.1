package module.mutationpre.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import egps2.EGPSProperties;
import module.mutationpre.SimplestModuleController;

@SuppressWarnings("serial")
public class PaintingPanel extends JPanel {

	private Rectangle dragRectangle = new Rectangle();
	private SimplestModuleController controller;
	private AxisPainter axisPainter;
	private RegionPainter regionPainter;
	private ConnectingLinePainter connectingLinePainter;
	private MutationPainter mutationPainter;
	private DrawProperties drawProperties;

	boolean shouldRotate4vertical = false;
	Point dragMovedDistance = new Point();
	Point dragMovedOriginalPoint = new Point();
	private ListenerOfPaintingPanel listenerOfPaintingPanel;

	public PaintingPanel() {
		setBackground(Color.white);

		mutationPainter = new MutationPainter();
		axisPainter = new AxisPainter();
		regionPainter = new RegionPainter();
		connectingLinePainter = new ConnectingLinePainter();

		listenerOfPaintingPanel = new ListenerOfPaintingPanel();
		listenerOfPaintingPanel.setMutationPainter(mutationPainter);
		listenerOfPaintingPanel.setPaintingPanel(this);

		addMouseListener(listenerOfPaintingPanel);
		addMouseMotionListener(listenerOfPaintingPanel);
		addMouseWheelListener(listenerOfPaintingPanel);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// g2d.drawRect(45, 50, getWidth() - 45, 50);

		if (shouldRotate4vertical) {
			
//			System.out.println("dragMovedOriginalPoint distance is : " + dragMovedOriginalPoint);
//			System.out.println("Draged distance is : " + dragMovedDistance);
			
			dragMovedOriginalPoint.x += dragMovedDistance.x;
			dragMovedOriginalPoint.y += dragMovedDistance.y;
			g2d.translate(dragMovedOriginalPoint.x, dragMovedOriginalPoint.y);
			
			
			int rotateX = drawProperties.paintingPanelSize.width / 2;
			int rotateY = drawProperties.paintingPanelSize.height / 2;
			g2d.rotate(Math.PI * 0.5, rotateX ,rotateY);
			g2d.translate(rotateX - rotateY , 0);
		}

		axisPainter.paint(g2d);
		regionPainter.paint(g2d);
		connectingLinePainter.paint(g2d);
		mutationPainter.paint(g2d);

		if (dragRectangle.getWidth() > 0) {
			g2d.setColor(EGPSProperties.dragFrameColor);
			g2d.fill(dragRectangle);
		}

		if (shouldRotate4vertical) {
			int rotateX = drawProperties.paintingPanelSize.width / 2;
			int rotateY = drawProperties.paintingPanelSize.height / 2;
			g2d.rotate(-Math.PI * 0.5 , rotateX , rotateY);
			g2d.translate( rotateY -rotateX, 0);
			
		}

	}

	public Rectangle getDragRectangle() {
		return dragRectangle;
	}

	public void setController(SimplestModuleController controller) {
		this.controller = controller;

		DrawProperties drawProperties = controller.getDrawProperties();
		setProperty(drawProperties);

	}

	public void setProperty(DrawProperties drawProperties) {
		axisPainter.setProperty(drawProperties.axisProperty);
		regionPainter.setProperty(drawProperties.regionProperty);
		connectingLinePainter.setListOfMutationElement(drawProperties.connectingLineProperty);
		mutationPainter.setProperty(drawProperties.mutationProperty);
		this.drawProperties = drawProperties;
	}
	
	void heavyRefresh() {
		controller.heavyRefresh();
	}
	
	
	public DrawProperties getDrawProperties() {
		return drawProperties;
	}
	
	public ListenerOfPaintingPanel getListenerOfPaintingPanel() {
		return listenerOfPaintingPanel;
	}

}
