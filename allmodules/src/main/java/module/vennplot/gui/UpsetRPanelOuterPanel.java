package module.vennplot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.panels.dialog.SwingDialog;
import egps2.panels.dialog.opendialog.StringSearchDialog;
import module.vennplot.VennPlotController;
import module.vennplot.VennPlotMain;
import module.vennplot.model.DataModel;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.PaintingLocations;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SetItem;
import module.vennplot.painter.BodyIntersectionSelection;
import module.vennplot.util.AdjustAndCalculate;
import egps2.panels.reusablecom.ParameterInitialized;
import egps2.panels.reusablecom.ReusableCompoentBuilder;

public class UpsetRPanelOuterPanel extends JPanel {

	private static final long serialVersionUID = -4970148934039341523L;

	private PaintingPanelUpsetR paintingPanel;

	private JScrollPane scrollPaneOfPating;

	private AdjustAndCalculate adjustAndCalculate;
	private ParameterModel parameterModel;
	private DataModel dataModel;

	ParameterInitialized[] controlPanels = new ParameterInitialized[4];

	final int widthOfControlPanel = 310;


	private VennPlotController controller;

	private VennPlotMain vennPlotMain;

	public UpsetRPanelOuterPanel(VennPlotMain vennPlotMain) {
		setLayout(new BorderLayout());
		this.vennPlotMain = vennPlotMain;
	}

	public void initializePanel() {
		SetItem[] setItems = vennPlotMain.getSetItems();
		if (setItems == null) {
			JLabel comp = new JLabel("You do not import the data yet! Please import data!");
			comp.setFont(comp.getFont().deriveFont(20f));
			add(comp);
			return;
		}

		removeAll();
		dataModel = new DataModel();
		dataModel.setSetItems(setItems);

		parameterModel = new ParameterModel();
		controller.setParameterModel(parameterModel);
		controller.setDataModel(dataModel);

		// 这一段可能会比较耗时
		dataModel.calculateDataForLeftPie();
		parameterModel.initializeIntersectionParamters(setItems);
		parameterModel.initializeSingleRegionParameters(setItems);

		paintingPanel = new PaintingPanelUpsetR();

		scrollPaneOfPating = new JScrollPane(paintingPanel);
		scrollPaneOfPating.setBackground(Color.white);

		UpsetRPanelMouseListener listener = new UpsetRPanelMouseListener(paintingPanel, this);
		paintingPanel.addMouseListener(listener);
		paintingPanel.addMouseMotionListener(listener);

		paintingPanel.setModels(parameterModel, dataModel);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(null);

		splitPane.setRightComponent(scrollPaneOfPating);
		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
		splitPane.setOneTouchExpandable(true);

		splitPane.setDividerLocation(widthOfControlPanel);
		splitPane.setDividerSize(8);

		taskPaneContainer.setBackground(Color.WHITE);
		taskPaneContainer.setBackgroundPainter(null);

		taskPaneContainer.add(getInstantStatusTaskPanel());

		JXTaskPane convenietOperationTaskPanel = getConvenietOperationTaskPanel();
		taskPaneContainer.add(convenietOperationTaskPanel);

		JXTaskPane displayTaskPanel = getDisplayTaskPanel();
		taskPaneContainer.add(displayTaskPanel);

		JXTaskPane explorAnalysisTaskPanel = getExplorAnalysisTaskPanel();
		taskPaneContainer.add(explorAnalysisTaskPanel);

		JScrollPane comp = new JScrollPane(taskPaneContainer);
		splitPane.setLeftComponent(comp);

		add(splitPane, BorderLayout.CENTER);

		autoSizePanel();

		addlisteners();
	}

	private JXTaskPane getInstantStatusTaskPanel() {
		MyInstantStatusPanel myInstantStatusPanel = new MyInstantStatusPanel();
		myInstantStatusPanel.setController(controller);
		controller.addMyInstantStatusPanel(myInstantStatusPanel);
		JXTaskPane task = ReusableCompoentBuilder.wrapperJPanelWithJXTaskPane(myInstantStatusPanel, "Instant status");
		controlPanels[0] = myInstantStatusPanel;
		return task;
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

	private JXTaskPane getExplorAnalysisTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		// operationPane.setCollapsed(true);
		tmpJxTaskPane.setFont(vennPlotMain.titleFont);
		tmpJxTaskPane.setTitle("Exporary Analysis");
		LeftExporaryAnalysisPanel operationPanel = new LeftExporaryAnalysisPanel(vennPlotMain);
		// operationPanel.setMinimumSize(new Dimension(200, 500));
		operationPanel.setPreferredSize(new Dimension(450, 300));
		tmpJxTaskPane.add(operationPanel);
		// tmpJxTaskPane.setMinimumSize(new Dimension(300, 500));
		controlPanels[3] = operationPanel;

		return tmpJxTaskPane;
	}

	private JXTaskPane getDisplayTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		// operationPane.setCollapsed(true);
		tmpJxTaskPane.setFont(vennPlotMain.titleFont);
		tmpJxTaskPane.setTitle("Display Options");
		LeftDisplayPanel operationPanel = new LeftDisplayPanel(vennPlotMain);
		tmpJxTaskPane.add(operationPanel);
		controlPanels[2] = operationPanel;

		return tmpJxTaskPane;
	}

	private JXTaskPane getConvenietOperationTaskPanel() {
		JXTaskPane tmpJxTaskPane = new JXTaskPane();
		tmpJxTaskPane.setFont(vennPlotMain.titleFont);
		tmpJxTaskPane.setTitle("Convenient Operation");
		LeftConveOperationPanel operationPanel = new LeftConveOperationPanel(vennPlotMain);
		controlPanels[1] = operationPanel;
		tmpJxTaskPane.add(operationPanel);

		return tmpJxTaskPane;
	}

	public void clearPaintingPanel() {
		paintingPanel = null;
		parameterModel = null;
		dataModel = null;
		scrollPaneOfPating = null;
		// removeAll();
	}

	public void weakestUpdate() {
		scrollPaneOfPating.updateUI();
	}

	public void assitantForMacOS() {
		revalidate();
	}

	public void adjustPaintingElemetsForPaintingPartial() {
		AdjustAndCalculate adjustAndCalculator = getAdjustAndCalculator();
		adjustAndCalculator.setModels(parameterModel, dataModel, this);
		PaintingLocations locations = adjustAndCalculator.autoConfigPainatingParasPartial1(paintingPanel.getWidth(),
				paintingPanel.getHeight(), paintingPanel.paintingLocations);
		paintingPanel.setPaintingLocations(locations);
	}

	private AdjustAndCalculate getAdjustAndCalculator() {
		if (adjustAndCalculate == null) {
			adjustAndCalculate = new AdjustAndCalculate();
		}
		return adjustAndCalculate;
	}

	public ParameterModel getParameterModel() {
		return parameterModel;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void autoSizePanel() {
		// Old way to auto size the panel

//		AdjustAndCalculate adjustAndCalculator = getAdjustAndCalculator();
//		adjustAndCalculator.setModels(parameterModel, dataModel, this);
//
//		int ww = getWidth() - widthOfControlPanel;
//		int hh = getHeight();
//		PaintingLocations locations = adjustAndCalculator.autoConfigAllPainatingParas(ww, hh);
//		paintingPanel.setPaintingLocations(locations);

		parameterModel.clearAllSelections();

		SetItem[] setItems = vennPlotMain.getSetItems();
		parameterModel.initializeIntersectionParamters(setItems);

		AdjustAndCalculate adjustAndCalculator = getAdjustAndCalculator();
		adjustAndCalculator.setModels(parameterModel, dataModel, this);
		int ww = getWidth() - widthOfControlPanel;
		int hh = getHeight() - 30;
		PaintingLocations locations = adjustAndCalculator.autoConfigAllPainatingParas(ww, hh);
		paintingPanel.setPaintingLocations(locations);

		initializeControlPanels();
		weakestUpdate();

	}

	public void searchSpecificItem() {
		StringSearchDialog stingArrayDisplayDialog = new StringSearchDialog((obj) -> {

			boolean hasSearched = false;

			String target = obj.toString();

			parameterModel.clearAllSelections();
			List<String> namesOfSetItemContains = new ArrayList<>();

			// Search from input sets
			SetItem[] setItems = dataModel.getSetItems();
			for (SetItem setItem : setItems) {
				String[] setLists = setItem.getSetLists();
				for (String ss : setLists) {
					if (ss.equalsIgnoreCase(target)) {
						namesOfSetItemContains.add(setItem.getName());
						hasSearched = true;
						break;
					}
				}
			}

			parameterModel.setNameOfInputSetItemForSearch(namesOfSetItemContains);

			// Search from intersections
			IntersectionRegionDataModel intersectionRegionDataModel = parameterModel.getIntersectionRegionDataModel();
			List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();
			for (int i = 0; i < eles.size(); i++) {
				IntersectionRegionElement intersectionRegionElement = eles.get(i);
				String[] elementNames = intersectionRegionElement.getElementNames();

				for (String ss : elementNames) {
					if (ss.equalsIgnoreCase(target)) {
						parameterModel.getBodyIntersectionSelections().add(new BodyIntersectionSelection(i));
						hasSearched = true;
						break;
					}
				}

			}

			if (hasSearched) {
				paintingPanel.revalidate();
			} else {
				SwingDialog.showInfoMSGDialog("No value found", "Can not find the corresponding value: " + target);
			}

		});

		stingArrayDisplayDialog.setVisible(true);
	}

	public PaintingPanelUpsetR getPaintingPanel() {
		return paintingPanel;
	}

	public void changeView2ClassicalPlot() {
		controller.change2nextTab();

	}

	public void setController(VennPlotController controller) {
		this.controller = controller;

	}

	public VennPlotController getController() {
		return controller;
	}

}
