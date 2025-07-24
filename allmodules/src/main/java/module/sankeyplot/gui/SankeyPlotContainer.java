package module.sankeyplot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import module.sankeyplot.SankeyPlotMain;
import module.sankeyplot.model.StringElements;
import egps2.panels.reusablecom.ParameterInitialized;


@SuppressWarnings("serial")
public class SankeyPlotContainer extends JPanel {
	
	ParameterInitialized[] controlPanels = new ParameterInitialized[3];
	
	private final SankeyPlotMain sankeyPlotMain;
	private final PaintingPanel sankeyPlotDrawer;
	
	private int numberOfSets;

	private LeftConveOperationPanel operationPanel;

	private LeftDisplayPanel leftDisplayPanel;

	public SankeyPlotContainer(SankeyPlotMain sankeyPlotMain) {
		setBackground(Color.white);
		this.sankeyPlotMain = sankeyPlotMain;
		sankeyPlotDrawer = new PaintingPanel(sankeyPlotMain);
		sankeyPlotDrawer.addMouseListener(new SankeyMouseListener(sankeyPlotDrawer));
		setLayout(new BorderLayout());
		
		JSplitPane comp = new JSplitPane();
		comp.setBorder(null);
		
		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
		taskPaneContainer.setBackground(Color.WHITE);
		taskPaneContainer.setBackgroundPainter(null);
		
		JXTaskPane convenietOperationTaskPanel = getConvenietOperationTaskPanel();
		taskPaneContainer.add(convenietOperationTaskPanel);
		
		
		JXTaskPane displayTaskPanel = getDisplayTaskPanel();
		taskPaneContainer.add(displayTaskPanel);
		
		JScrollPane tempJScrollPane = new JScrollPane(taskPaneContainer);
		comp.setLeftComponent(tempJScrollPane);
		
		comp.setRightComponent(sankeyPlotDrawer);
		add(comp,BorderLayout.CENTER);
		
		for (ParameterInitialized parameterInitialized : controlPanels) {
			if (parameterInitialized == null) {
				continue;
			}
			parameterInitialized.initializeParameters();
			parameterInitialized.addListeners();
		}
		
	}
	
	private JXTaskPane getConvenietOperationTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		tmpJxTaskPane.setFont(sankeyPlotMain.titleFont);
		tmpJxTaskPane.setTitle("Convenient Operation");
		operationPanel = new LeftConveOperationPanel(sankeyPlotDrawer);
		controlPanels[1] = operationPanel;
		tmpJxTaskPane.add(operationPanel);
		
		return tmpJxTaskPane;
	}
	
	private JXTaskPane getDisplayTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		// operationPane.setCollapsed(true);
		tmpJxTaskPane.setFont(sankeyPlotMain.titleFont);
		tmpJxTaskPane.setTitle("Display Options");
		leftDisplayPanel = new LeftDisplayPanel(sankeyPlotDrawer);
		tmpJxTaskPane.add(leftDisplayPanel);
		controlPanels[2] = leftDisplayPanel;
		
		return tmpJxTaskPane;
	}

	/**
	 * Input data and initialize panel!
	 * @param inputData
	 * @param setNames 
	 */
	public void initializePanel(List<StringElements> inputData, String[] setNames) {
		numberOfSets = setNames.length;
		
		sankeyPlotDrawer.setInputData(inputData,setNames);
		sankeyPlotDrawer.initializeData();
		sankeyPlotDrawer.repaint();
		
	}
	
	public int getNumberOfSets() {
		return numberOfSets;
	}
	
	public boolean isInitialized() {
		return sankeyPlotDrawer.getIfInitalized();
	}

	public void clear() {
		sankeyPlotDrawer.clear();
		
	}
	
	public PaintingPanel getSankeyPlotDrawer() {
        return sankeyPlotDrawer;
    }
	
	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("The figure is generated with color rendered by ");
		sBuilder.append(operationPanel.toString());
		sBuilder.append(" style.<br>");
		sBuilder.append(leftDisplayPanel.toString());
		return sBuilder.toString();
	}

}
