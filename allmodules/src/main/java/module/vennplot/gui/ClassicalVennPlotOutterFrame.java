package module.vennplot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.UnifiedAccessPoint;
import module.vennplot.VennPlotController;
import module.vennplot.VennPlotMain;
import module.vennplot.gui.classical.ClassicalPaintingPanelVenn;
import module.vennplot.listener.ClassicalPanelMouseListener;
import module.vennplot.model.ClassicalParameterModel;
import module.vennplot.model.DataModel;
import module.vennplot.model.SetItem;
import egps2.panels.reusablecom.ParameterInitialized;
import egps2.panels.reusablecom.ReusableCompoentBuilder;

public class ClassicalVennPlotOutterFrame extends JPanel {
	private static final long serialVersionUID = -808986630744992617L;


	private DataModel dataModel;

	private JScrollPane scrollPane;

	final int widthOfControlPanel = 350;

	public final Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

	public final Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();


	private ClassicalPaintingPanelVenn paintingPanel;

	private ClassicalParameterModel parameterModel;

	ParameterInitialized[] controlPanels = new ParameterInitialized[3];
	
	SetItem[] setItems;
	
	private VennPlotController controller;

	
	public ClassicalVennPlotOutterFrame() {
		setLayout(new BorderLayout());
	}


	public void initializePanel(SetItem[] setItems) {
		this.setItems = setItems;
		
		final int showCount = 5;
		if (setItems == null) {
			return;
		}
		if (setItems.length > showCount) {
			String msg = "Your import data contains more than 5 sets, please go to Upset plot to see the results!";
			JLabel comp = new JLabel(msg);
			comp.setFont(comp.getFont().deriveFont(20f));
			add(comp, BorderLayout.CENTER);
			return;
		}

		removeAll();
		
		dataModel = new DataModel();

		dataModel.setSetItems(setItems);

		parameterModel = new ClassicalParameterModel();

		parameterModel.initialized();

		paintingPanel = new ClassicalPaintingPanelVenn(dataModel, parameterModel);

		ClassicalPanelMouseListener listener = new ClassicalPanelMouseListener(paintingPanel, this);
		paintingPanel.addMouseListener(listener);

		/**
		 * adjust and set PaintingLocations DataModel and ParameterModel to Painting
		 * panel
		 */
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(paintingPanel);

		scrollPane.setBackground(Color.white);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(null);

		splitPane.setRightComponent(scrollPane);

		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
		splitPane.setOneTouchExpandable(true);

		splitPane.setDividerLocation(widthOfControlPanel);
		splitPane.setDividerSize(7);

		taskPaneContainer.setBackground(Color.WHITE);
		taskPaneContainer.setBackgroundPainter(null);

		taskPaneContainer.add(getInstantStatusTaskPanel());
		
		JXTaskPane convenietOperationTaskPanel = getConvenietOperationTaskPanel();
		taskPaneContainer.add(convenietOperationTaskPanel);

		JXTaskPane displayTaskPanel = getDisplayTaskPanel();
		taskPaneContainer.add(displayTaskPanel);

		JScrollPane comp = new JScrollPane(taskPaneContainer);
		splitPane.setLeftComponent(comp);

		add(splitPane, BorderLayout.CENTER);
		

		scrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> {
			weakestUpdate();
		});
		scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
			weakestUpdate();
		});

		initializeControlPanels();

		addlisteners();

		weakestUpdate();
	}

	public void initializeControlPanels() {
		for (ParameterInitialized parameterInitialized : controlPanels) {
			parameterInitialized.initializeParameters();
		}
	}

	public void addlisteners() {
		for (ParameterInitialized parameterInitialized : controlPanels) {
			parameterInitialized.addListeners();
		}
	}
	
	private JXTaskPane getInstantStatusTaskPanel() {
		MyInstantStatusPanel myInstantStatusPanel = new MyInstantStatusPanel();
		myInstantStatusPanel.setController(controller);
		controller.addMyInstantStatusPanel(myInstantStatusPanel);
		JXTaskPane task = ReusableCompoentBuilder.wrapperJPanelWithJXTaskPane(myInstantStatusPanel, "Instant status");
		controlPanels[0] = myInstantStatusPanel;
		return task;
	}

	private JXTaskPane getConvenietOperationTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		tmpJxTaskPane.setFont(titleFont);
		tmpJxTaskPane.setTitle("Convenient Operation");
		
		VennPlotMain viewPanel = controller.getViewPanel();
		LeftConveOperationClassicalPanel operationPanel = new LeftConveOperationClassicalPanel(this,viewPanel);
		tmpJxTaskPane.add(operationPanel);
		controlPanels[1] = operationPanel;
		return tmpJxTaskPane;
	}

	private JXTaskPane getDisplayTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		tmpJxTaskPane.setFont(titleFont);
		tmpJxTaskPane.setTitle("Display Options");
		LeftDisplayClassicalPanel displayPanel = new LeftDisplayClassicalPanel(this);
		tmpJxTaskPane.add(displayPanel);
		controlPanels[2] = displayPanel;

		return tmpJxTaskPane;
	}

	public void weakestUpdate() {
		if (scrollPane == null) {
			return;
		}
		scrollPane.updateUI();
	}

	public void clearPaintingPanel() {
		paintingPanel = null;
		dataModel = null;
		removeAll();
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public ClassicalPaintingPanelVenn getPaintingPanel() {
		return paintingPanel;
	}

	public ClassicalParameterModel getParameterModel() {

		return parameterModel;
	}

	public void changeTab() {
		controller.change2nextTab();
		
	}

	public void setController(VennPlotController controller) {
		this.controller = controller;
		
	}

	public VennPlotController getController() {
		return controller;
	}

}
