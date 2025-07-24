package module.pcarunner.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class PointPlotContainer extends JPanel {
	private static final long serialVersionUID = 5319977701368206732L;
	
	private PaintingPanel paintingPanel;

	/**
	 * Create the panel.
	 */
	public PointPlotContainer() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		paintingPanel = new PaintingPanel();
		paintingPanel.addMouseMotionListener(new PCAMouseListener(paintingPanel));
		scrollPane_1.setViewportView(paintingPanel);

	}

}
