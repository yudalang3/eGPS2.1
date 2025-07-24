package module.vennplot.gui.classical;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ClassicalPaintingLocations {

	private List<Rectangle2D.Double> paintingNameLocations = new ArrayList<Rectangle2D.Double>();

	private List<Ellipse2D.Double> paintingCircleLocations = new ArrayList<Ellipse2D.Double>();

	public List<Ellipse2D.Double> getPaintingCircleLocations() {
		return paintingCircleLocations;
	}

	public void setPaintingCircleLocations(List<Ellipse2D.Double> paintingCircleLocations) {
		this.paintingCircleLocations = paintingCircleLocations;
	}

	public List<Rectangle2D.Double> getPaintingNameLocations() {

		return paintingNameLocations;
	}

	public void setPaintingNameLocations(List<Rectangle2D.Double> paintingLocations) {
		this.paintingNameLocations = paintingLocations;
	}

}
