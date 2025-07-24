package module.multiseq.alignment.view.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import module.multiseq.alignment.view.AlignmentViewMain;

public class AlignmentViewInterLeavedPanel extends JPanel implements MouseWheelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7315338800751395888L;

	private AlignmentInterLeavedRightPanel rightJPanel;

	private AlignmentViewMain alignmentViewMain;

	private AlignmentInterLeavedScrollBar rightVscroll;

	public AlignmentViewInterLeavedPanel(AlignmentViewMain alignmentViewMain) {
		this.alignmentViewMain = alignmentViewMain;

		setLayout(new BorderLayout());

		add(getMainSplitPane(), BorderLayout.CENTER);

		add(getRightJScrollBar(), BorderLayout.EAST);

		addMouseWheelListener(this);
	}

	public JPanel getMainSplitPane() {
		if (rightJPanel == null) {

			rightJPanel = new AlignmentInterLeavedRightPanel(alignmentViewMain, this);
		}
		return rightJPanel;

	}

	public JScrollBar getRightJScrollBar() {
		if (rightVscroll == null) {
			rightVscroll = new AlignmentInterLeavedScrollBar(alignmentViewMain, this, JScrollBar.VERTICAL);
		}
		return rightVscroll;
	}

//	@Override
//	protected void paintComponent(Graphics g) {
//		invalidate();
//		// Dimension preferredSize = spaceFillerWidthAdjuster.getPreferredSize();
//
//		// topSplitPane.setDividerLocation(preferredSize.width);
//		// mainSplitPane.setDividerLocation(preferredSize.width);
//
//		validate();
//		super.paintComponent(g);
//
//	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() == 1) {
			JScrollBar rightJScrollBar = getRightJScrollBar();

			rightJScrollBar.setValue(rightJScrollBar.getValue() + 1);

		}
		if (e.getWheelRotation() == -1) {
			JScrollBar rightJScrollBar = getRightJScrollBar();

			rightJScrollBar.setValue(rightJScrollBar.getValue() - 1);
		}

	}

}
