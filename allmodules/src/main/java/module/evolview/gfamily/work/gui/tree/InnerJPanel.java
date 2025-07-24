package module.evolview.gfamily.work.gui.tree;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import module.evolview.phylotree.visualization.layout.BaseLayout;

public class InnerJPanel extends JPanel {

	private BaseLayout layout;
	
	

	public InnerJPanel(BaseLayout layout) {
		super();
		this.layout = layout;
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		layout.paintGraphics(g2d);
		g2d.drawString("fsddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd", 100, 100);

	}
	
}