package module.heatmap.eheatmap.util;

import java.awt.Graphics2D;

import module.heatmap.eheatmap.model.selection.CellSelection;
import module.heatmap.eheatmap.rectpainter.RectShapePainter;

public abstract class CellHandler {
	
	protected final Graphics2D g2d;
	protected final CellSelection oSelection;

	public CellHandler(Graphics2D g2d,CellSelection c) {
		this.g2d = g2d;
		this.oSelection = c;
	}

	public abstract void handleProcess(RectShapePainter r,int i,int j,double x,double y);
	
	
}
