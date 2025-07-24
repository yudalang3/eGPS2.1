package module.multiseq.alignment.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.jidesoft.swing.JideSplitPane;

import module.multiseq.alignment.view.AlignmentViewMain;

public class AlignmentViewContinuousRightPanel extends JPanel implements MouseWheelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7315338800751395888L;
	private JPanel leftJPanel;
	private JPanel leftAnnotationSpaceFillerJPanel;
	private AlignmentScrollBar bottomHscroll;
	private JPanel bottomHscrollFillerJPanel;
	private JPanel rightJPanel;
	private JPanel rightSeqPanelHoldeJPanel;
	private AlignmentScrollBar rightVscroll;
	private JScrollPane centerRatioJScrollPane;
	private JSplitPane topSplitPane;
	private JSplitPane mainSplitPane;
	private JPanel leftAndCenterJPanel;

	private ViewAreaSequenceNameJPanel sequenceNameJPanel;

	private JPanel rightAndCenterJPanel;

	private ViewAreaScalePanelHolder scalePanelHolder;

	private AlignmentViewMain alignmentViewMain;
	private ViewAreaSequenceJPanel sequenceJPanel = null;

	/**
	 * <pre>
	 * 
	 * Components in the Painting Panel for the continues layout
	|------------------------------------------------
	| HeaderPanel
	|_______________________________________________
	| MainSplitPanel
	| LeftPanel                       |  RightPanel
	|      Center                     |        Center
	|                                 |
	|      -------------------------- |---------------------
	|      South                      |         South
	|                                 |
	|-------------------------------------------------
	 * 
	 * 
	 * 
	 * </pre>
	 * 
	 * @param alignmentViewMain
	 */
	public AlignmentViewContinuousRightPanel(AlignmentViewMain alignmentViewMain) {
		this.alignmentViewMain = alignmentViewMain;

		setLayout(new BorderLayout());

		// 这里的Top就是有一条 |的地方，可以拖动seqName和seqs的间距
		add(getTopSplitPane(), BorderLayout.NORTH);
		add(getMainSplitPane(), BorderLayout.CENTER);

		addMouseWheelListener(this);
	}

	private JSplitPane getTopSplitPane() {
		if (topSplitPane == null) {
			topSplitPane = new JSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
			topSplitPane.setBorder(null);
			topSplitPane.setDividerSize(0);
			topSplitPane.setDividerLocation(260);
			spaceFillerWidthAdjuster = new SpaceFillerWidthAdjuster(alignmentViewMain);
			topSplitPane.add(spaceFillerWidthAdjuster);
			scalePanelHolder = new ViewAreaScalePanelHolder(alignmentViewMain);
			topSplitPane.add(scalePanelHolder);
		}

		return topSplitPane;
	}

	private JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
			mainSplitPane.setDividerSize(0);
			mainSplitPane.setDividerLocation(260);
			mainSplitPane.setBorder(null);
			mainSplitPane.add(getLeftJPanel());
			mainSplitPane.add(getRightJPanel());
		}
		return mainSplitPane;

	}

	public JPanel getRightJPanel() {
		if (rightJPanel == null) {
			rightJPanel = new JPanel();
			rightJPanel.setLayout(new BorderLayout());
			rightJPanel.add(getRightAndCenterJPanel(), BorderLayout.CENTER);
			rightJPanel.add(getBottomJScrollBar(), BorderLayout.SOUTH);
		}
		return rightJPanel;

	}

	public ViewAreaSequenceJPanel getSequenceJPanel() {
		if (sequenceJPanel == null) {

			sequenceJPanel = new ViewAreaSequenceJPanel(alignmentViewMain);

		}
		return sequenceJPanel;
	}

	public JPanel getRightSeqPanelHoldeJPanel() {
		if (rightSeqPanelHoldeJPanel == null) {
			rightSeqPanelHoldeJPanel = new JPanel();
			rightSeqPanelHoldeJPanel.setLayout(new BorderLayout());
			rightSeqPanelHoldeJPanel.add(getSequenceJPanel(), BorderLayout.CENTER);
			rightSeqPanelHoldeJPanel.add(getRightJScrollBar(), BorderLayout.EAST);
		}

		return rightSeqPanelHoldeJPanel;
	}

	public JPanel getRightAndCenterJPanel() {

		if (rightAndCenterJPanel == null) {
			rightAndCenterJPanel = new JPanel();
			rightAndCenterJPanel.setLayout(new BorderLayout());
			rightAndCenterJPanel.add(getRightSeqPanelHoldeJPanel(), BorderLayout.CENTER);
			rightAndCenterJPanel.add(getCenterRatioJScrollPane(), BorderLayout.SOUTH);

		}
		return rightAndCenterJPanel;
	}

	public JScrollPane getCenterRatioJScrollPane() {
		if (centerRatioJScrollPane == null) {
			centerRatioJScrollPane = new JScrollPane();
			centerRatioJScrollPane.setBorder(null);
			centerRatioJScrollPane.setPreferredSize(new Dimension(20, 80));
			ViewAreaSequenceAnnotationJPanel sequenceAnnotationJPanel = new ViewAreaSequenceAnnotationJPanel(
					alignmentViewMain);
			centerRatioJScrollPane.setViewportView(sequenceAnnotationJPanel);
		}
		return centerRatioJScrollPane;

	}

	private SpaceFillerWidthAdjuster spaceFillerWidthAdjuster;

	public JPanel getLeftJPanel() {
		if (leftJPanel == null) {
			leftJPanel = new JPanel();
			leftJPanel.setLayout(new BorderLayout());
			leftJPanel.add(getLeftAndCenterJPanel(), BorderLayout.CENTER);
			leftJPanel.add(getBottomHscrollFillerJPanel(), BorderLayout.SOUTH);
		}
		return leftJPanel;
	}

	public JPanel getLeftAndCenterJPanel() {

		if (leftAndCenterJPanel == null) {
			leftAndCenterJPanel = new JPanel();
			leftAndCenterJPanel.setLayout(new BorderLayout());
			sequenceNameJPanel = new ViewAreaSequenceNameJPanel(alignmentViewMain);
			leftAndCenterJPanel.add(sequenceNameJPanel, BorderLayout.CENTER);
			leftAnnotationSpaceFillerJPanel = new AnnotationSpaceFillerJPanel(alignmentViewMain);
			leftAndCenterJPanel.add(leftAnnotationSpaceFillerJPanel, BorderLayout.SOUTH);
		}

		return leftAndCenterJPanel;

	}

	public JScrollBar getBottomJScrollBar() {
		if (bottomHscroll == null) {
			bottomHscroll = new AlignmentScrollBar(alignmentViewMain, this, JScrollBar.HORIZONTAL);
			bottomHscroll.setPreferredSize(new Dimension(70, 20));
		}
		return bottomHscroll;
	}

	public JScrollBar getRightJScrollBar() {
		if (rightVscroll == null) {
			rightVscroll = new AlignmentScrollBar(alignmentViewMain, this, JScrollBar.VERTICAL);
		}
		return rightVscroll;
	}

	public JPanel getBottomHscrollFillerJPanel() {
		if (bottomHscrollFillerJPanel == null) {
			bottomHscrollFillerJPanel = new JPanel();
			bottomHscrollFillerJPanel.setPreferredSize(new Dimension(70, 20));
			bottomHscrollFillerJPanel.setBackground(Color.white);
		}
		return bottomHscrollFillerJPanel;
	}

	@Override
	protected void paintComponent(Graphics g) {
		invalidate();
		Dimension preferredSize = spaceFillerWidthAdjuster.getPreferredSize();

		topSplitPane.setDividerLocation(preferredSize.width);
		mainSplitPane.setDividerLocation(preferredSize.width);

		validate();
		super.paintComponent(g);

	}

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
