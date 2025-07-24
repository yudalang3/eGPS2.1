package module.heatmap.eheatmap.listener;

import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;

import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.AbstrctEHeatmapPaintPanel;
import module.heatmap.eheatmap.HeatmapController;

public class MouseWheelListener implements java.awt.event.MouseWheelListener{

	private final AbstrctEHeatmapPaintPanel paintJPanel;
	private final HeatmapController heamapCont;

	public MouseWheelListener(AbstrctEHeatmapPaintPanel paintJPanel,HeatmapController heamapCont) {
		this.paintJPanel = paintJPanel;
		this.heamapCont = heamapCont;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		heamapCont.getParaModel().setCellOneMouseHasClick(false);
		heamapCont.getParaModel().setNameOneMouseHasClick(false);
		heamapCont.getParaModel().setTreeOneMouseHasClick(false);
		
        Dimension size = paintJPanel.getSize();
        int amount = - e.getWheelRotation() * 30;
		size.width += amount;
        size.height += amount;
        if ((size.width < 10) || (size.height < 10)) {
             size.width -= amount;
             size.height -= amount;
        }
        paintJPanel.setPreferredSize(size);
        paintJPanel.setSize(size);
        
        heamapCont.strongestRefreshHeatmapAndRecoverDim();
        
        EheatmapMain main = (EheatmapMain) heamapCont.getViewPanel();
		main.reInitializeLeftLayoutPanelLayout();
   }


}
