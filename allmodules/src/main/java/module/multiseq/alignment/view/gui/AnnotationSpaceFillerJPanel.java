package module.multiseq.alignment.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import egps2.frame.gui.EGPSMainGuiUtil;
import module.multiseq.alignment.view.AlignmentViewMain;

public class AnnotationSpaceFillerJPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4637447842065044034L;

	static final String DEFUALT_ANNOTION = "Consensus";

	private VisulizationDataProperty alignmentViewPort;

	public AnnotationSpaceFillerJPanel(AlignmentViewMain alignmentViewMain) {
		this.alignmentViewPort = alignmentViewMain.getAlignmentViewPort();
		setPreferredSize(new Dimension(20, 80));
		setLayout(new BorderLayout());
		setBackground(Color.white);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		EGPSMainGuiUtil.setupHighQualityRendering(g2);
		g2.setFont(alignmentViewPort.getFont());
		g2.setColor(alignmentViewPort.getColor());

		int width = getWidth();

		int height = getHeight();

		FontMetrics fm = g2.getFontMetrics(alignmentViewPort.getFont());

		int ww = fm.stringWidth(DEFUALT_ANNOTION);

		g2.drawString(DEFUALT_ANNOTION, width - ww - 10, height - alignmentViewPort.getCharHeight());

		g2.dispose();
	}

}
