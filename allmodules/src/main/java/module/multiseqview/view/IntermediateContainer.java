package module.multiseqview.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import module.multiseqview.SequenceStructureViewController;
import module.multiseqview.SequenceStructureViewMain;


@SuppressWarnings("serial")
public class IntermediateContainer extends JPanel {

	
	private final SequenceStructureViewController controller;
	private final SequenceStructureViewMain main;
	private final PaintingPanel paintingPanel;
	
	private JScrollPane jScrollPane;
	
	
	public IntermediateContainer(SequenceStructureViewController controller) {
		Objects.requireNonNull(controller);
		
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		this.controller = controller;
		this.main = controller.getMain();
		
		this.paintingPanel = new PaintingPanel(controller);
		controller.setPaintingPanel(paintingPanel);
		
		JSplitPane splitPanel = new JSplitPane();
		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
		taskPaneContainer.setBackground(Color.WHITE);
		taskPaneContainer.setBackgroundPainter(null);
		
		JXTaskPane convenietOperationTaskPanel = getConvenietOperationTaskPanel();
		taskPaneContainer.add(convenietOperationTaskPanel);
		JXTaskPane displayTaskPanel = getDisplayTaskPanel();
		taskPaneContainer.add(displayTaskPanel);
		
		JScrollPane tempJScrollPane = new JScrollPane(taskPaneContainer);
		splitPanel.setLeftComponent(tempJScrollPane);
		splitPanel.setBorder(null);
		
		splitPanel.setDividerLocation(300);
		splitPanel.setDividerSize(8);
		add(splitPanel,BorderLayout.CENTER);
		
		
		jScrollPane = new JScrollPane(paintingPanel);
		jScrollPane.setBackground(Color.white);
		splitPanel.setRightComponent(jScrollPane);
	}
	
	private JXTaskPane getConvenietOperationTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		tmpJxTaskPane.setFont(controller.titleFont);
		tmpJxTaskPane.setTitle("Convenient Operation");
		LeftConveOperationPanel operationPanel = new LeftConveOperationPanel(controller);
		tmpJxTaskPane.add(operationPanel);
		
		return tmpJxTaskPane;
	}
	
	private JXTaskPane getDisplayTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		// operationPane.setCollapsed(true);
		tmpJxTaskPane.setFont(controller.titleFont);
		tmpJxTaskPane.setTitle("Display Options");
		LeftDisplayPanel operationPanel = new LeftDisplayPanel(controller);
		tmpJxTaskPane.add(operationPanel);
		
		return tmpJxTaskPane;
	}


	public void refresh() {
		jScrollPane.updateUI();
		
	}
	
	public JScrollPane getjScrollPane() {
		return jScrollPane;
	}
	
	public PaintingPanel getPaintingPanel() {
		return paintingPanel;
	}
}
