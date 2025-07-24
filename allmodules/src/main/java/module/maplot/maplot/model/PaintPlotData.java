package module.maplot.maplot.model;

import java.util.List;

public class PaintPlotData {

	private List<PaintingPoint> paintingPoints;

	private PaintingLine paintingLine;
	
	private IndicatingLine changeIndicatLine;

	public List<PaintingPoint> getPaintingPoints() {
		return paintingPoints;
	}

	public void setPaintingPoints(List<PaintingPoint> paintingPoints) {
		this.paintingPoints = paintingPoints;
	}

	public PaintingLine getPaintingLine() {
		return paintingLine;
	}

	public void setPaintingLine(PaintingLine paintingLine) {
		this.paintingLine = paintingLine;
	}

	public IndicatingLine getChangeIndicatLine() {
		return changeIndicatLine;
	}

	public void setChangeIndicatLine(IndicatingLine changeIndicatLine) {
		this.changeIndicatLine = changeIndicatLine;
	}
	
	

}
