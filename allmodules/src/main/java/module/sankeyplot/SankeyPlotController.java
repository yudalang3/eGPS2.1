package module.sankeyplot;

import java.awt.Color;

import egps2.utils.common.util.SaveUtil;
import egps2.UnifiedAccessPoint;
import module.sankeyplot.gui.PaintingPanel;

public class SankeyPlotController {

	final SankeyPlotMain main;

	public SankeyPlotController(SankeyPlotMain sankeyPlotMain) {
		this.main = sankeyPlotMain;
	}


	public void saveViewPanelAs() {
		new SaveUtil().saveData(main.getPaintingJpanel());
	}

	public Color getCurrentColorStatus() {
		PaintingPanel paintingJpanel = main.getPaintingJpanel();
		return paintingJpanel.getCurrentRectColorStatus();
	}

	public boolean canSetColor() {
		PaintingPanel paintingJpanel = main.getPaintingJpanel();
		return paintingJpanel.ifContainsSelection();
	}

	public void setColor(Color newColor) {
		PaintingPanel paintingJpanel = main.getPaintingJpanel();
		paintingJpanel.setColor(newColor);
	}


	public void refreshInstantStatus() {
		UnifiedAccessPoint.getInstanceFrame().refreshRightGraphicPropertiesPanel();
	}


}
