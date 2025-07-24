package module.vennplot;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import module.vennplot.gui.ClassicalVennPlotOutterFrame;
import module.vennplot.gui.MyInstantStatusPanel;
import module.vennplot.gui.UpsetRPanelOuterPanel;
import module.vennplot.model.ClassicalParameterModel;
import module.vennplot.model.DataModel;
import module.vennplot.model.IntersectionRegionDataModel;
import module.vennplot.model.IntersectionRegionElement;
import module.vennplot.model.ParameterModel;
import module.vennplot.model.SingleRegionDataModel;
import module.vennplot.model.SingleRegionElement;
import module.vennplot.painter.BodyIntersectionSelection;
import module.vennplot.painter.NameSelection;
import module.vennplot.painter.SingleRegionSelection;

public class VennPlotController {

	private final VennPlotMain view;
	private ParameterModel parameterModel;
	private DataModel dataModel;
	private List<MyInstantStatusPanel> myInstantStatusPanels = new ArrayList<>();

	public VennPlotController(VennPlotMain view) {
		this.view = view;
	}


	public VennPlotMain getViewPanel() {
		return view;
	}
	public Color getCurrentColorStatus() {
		Color color = Color.black;

		Component selectedComponent = view.getSelectedComponent();
		if (selectedComponent instanceof UpsetRPanelOuterPanel) {
			UpsetRPanelOuterPanel subPanel1 = (UpsetRPanelOuterPanel) selectedComponent;

			ParameterModel parameterModel = subPanel1.getParameterModel();

			List<SingleRegionSelection> singleRegionSelections = parameterModel.getSingleRegionSelections();
			List<BodyIntersectionSelection> bodyIntersectionSelections = parameterModel.getBodyIntersectionSelections();

			if (!singleRegionSelections.isEmpty()) {
				SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
				List<SingleRegionElement> eles = singleRegionDataModel.getEles();

				int firstIndex = singleRegionSelections.get(0).getIndex();
				SingleRegionElement singleRegionElement = eles.get(firstIndex);
				color = singleRegionElement.getFillColor();
			} else if (!bodyIntersectionSelections.isEmpty()) {
				IntersectionRegionDataModel intersectionRegionDataModel = parameterModel
						.getIntersectionRegionDataModel();
				List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();
				int firstIndex = bodyIntersectionSelections.get(0).getIndex();

				IntersectionRegionElement intersectionRegionElement = eles.get(firstIndex);
				color = intersectionRegionElement.getFilledColor();
			}
		} else if (selectedComponent instanceof ClassicalVennPlotOutterFrame) {
			
			ClassicalVennPlotOutterFrame subPanel2 = (ClassicalVennPlotOutterFrame) selectedComponent;

			ClassicalParameterModel parameterModel = subPanel2.getParameterModel();

			List<Color> colors = parameterModel.getColors();

			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			if (!nameSelections.isEmpty()) {

				NameSelection nameSelection = nameSelections.get(0);

				int index = nameSelection.getIndex();

				color = colors.get(index);
			}
		}

		return color;
	}

	public boolean canSetColor() {

		Component selectedComponent = view.getSelectedComponent();
		if (selectedComponent instanceof UpsetRPanelOuterPanel) {
			UpsetRPanelOuterPanel subPanel1 = (UpsetRPanelOuterPanel) selectedComponent;
			ParameterModel parameterModel = subPanel1.getParameterModel();
			if (parameterModel == null) {
				return false;
			}

			List<SingleRegionSelection> singleRegionSelections = parameterModel.getSingleRegionSelections();
			List<BodyIntersectionSelection> bodyIntersectionSelections = parameterModel.getBodyIntersectionSelections();

			return !singleRegionSelections.isEmpty() || !bodyIntersectionSelections.isEmpty();
		} else if (selectedComponent instanceof ClassicalVennPlotOutterFrame) {
			ClassicalVennPlotOutterFrame subPanel2 = (ClassicalVennPlotOutterFrame) selectedComponent;

			ClassicalParameterModel parameterModel = subPanel2.getParameterModel();
			if (parameterModel == null) {
				return false;
			}

			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			return !nameSelections.isEmpty();
		} else {
			return false;
		}

	}

	public void setColor(Color newColor) {

		Component selectedComponent = view.getSelectedComponent();
		if (selectedComponent instanceof UpsetRPanelOuterPanel) {
			UpsetRPanelOuterPanel subPanel1 = (UpsetRPanelOuterPanel) selectedComponent;

			ParameterModel parameterModel = subPanel1.getParameterModel();

			List<SingleRegionSelection> singleRegionSelections = parameterModel.getSingleRegionSelections();
			List<BodyIntersectionSelection> bodyIntersectionSelections = parameterModel.getBodyIntersectionSelections();

			if (!singleRegionSelections.isEmpty()) {
				SingleRegionDataModel singleRegionDataModel = parameterModel.getSingleRegionDataModel();
				List<SingleRegionElement> eles = singleRegionDataModel.getEles();

				for (SingleRegionSelection bodyIntersectionSelection : singleRegionSelections) {
					eles.get(bodyIntersectionSelection.getIndex()).setFillColor(newColor);
				}
			} else if (!bodyIntersectionSelections.isEmpty()) {
				IntersectionRegionDataModel intersectionRegionDataModel = parameterModel
						.getIntersectionRegionDataModel();
				List<IntersectionRegionElement> eles = intersectionRegionDataModel.getEles();

				for (BodyIntersectionSelection bodyIntersectionSelection : bodyIntersectionSelections) {
					eles.get(bodyIntersectionSelection.getIndex()).setFilledColor(newColor);
				}
			}

			subPanel1.weakestUpdate();
		} else if (selectedComponent instanceof ClassicalVennPlotOutterFrame) {
			ClassicalVennPlotOutterFrame subPanel2 = (ClassicalVennPlotOutterFrame) selectedComponent;

			ClassicalParameterModel parameterModel = subPanel2.getParameterModel();

			List<Color> colors = parameterModel.getColors();

			List<NameSelection> nameSelections = parameterModel.getNameSelections();

			if (!nameSelections.isEmpty()) {
				for (int i = 0; i < nameSelections.size(); i++) {

					NameSelection nameSelection = nameSelections.get(i);

					int index = nameSelection.getIndex();

					colors.set(index, newColor);

				}
			}
			subPanel2.weakestUpdate();
		}

	}

	public void change2nextTab() {

		SwingUtilities.invokeLater(() -> {
			view.change2nextTab();
		});
	}

	public void setParameterModel(ParameterModel parameterModel) {
		this.parameterModel = parameterModel;

	}

	public ParameterModel getParameterModel() {
		return parameterModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public DataModel getDataModel() {
		return dataModel;
	}


	public void addMyInstantStatusPanel(MyInstantStatusPanel panel) {
		myInstantStatusPanels.add(panel);
	}
	
	public void refreshInstantStatusPanel() {
		for (MyInstantStatusPanel myInstantStatusPanel : myInstantStatusPanels) {
			myInstantStatusPanel.refreshInstantStatus();
		}
	}

}
