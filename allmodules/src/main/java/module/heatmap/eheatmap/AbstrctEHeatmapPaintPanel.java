package module.heatmap.eheatmap;

import java.awt.geom.Rectangle2D.Double;

import javax.swing.JPanel;

import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;


public class AbstrctEHeatmapPaintPanel extends JPanel {
	
	protected ParameterModel paraModel;
	protected PaintingLocationParas locationParas;
	protected DataModel dataModel;
	
	protected HeatmapController controller;
	
	protected Double dragRect = null;
	
	public void reSerCellWidthAndHeightForGapDisplay() {}
	
	public void setAreaLocationPar(PaintingLocationParas par) {
		this.locationParas = par;
	}

	public PaintingLocationParas getLocationParas() {
		return locationParas;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	public void setParaModel(ParameterModel paraModel) {
		this.paraModel = paraModel;
	}
	
	public ParameterModel getParaModel() {
		return paraModel;
	}

	public void setDragRect(Double dragRect) {
		this.dragRect = dragRect;
	}

	public Double getDragRect() {
		return dragRect;
	}
	
	public DataModel getDataModel() {
		return dataModel;
	}

	public HeatmapController getController() {
		return controller;
	}

	public void setController(HeatmapController controller) {
		this.controller = controller;
	}

	
}
