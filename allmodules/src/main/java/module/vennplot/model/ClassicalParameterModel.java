package module.vennplot.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import egps2.UnifiedAccessPoint;
import module.vennplot.gui.classical.ClassicalPaintingLocations;
import module.vennplot.painter.NameSelection;

public class ClassicalParameterModel {

	private List<Color> listColors;

	private boolean isPaintingConsistentWithSize;

	private Font IntersectionValueFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private Font nameFont = IntersectionValueFont;

	private boolean showSetValues;

	private boolean showSetLegend;

	public boolean isPaintingConsistentWithSize() {
		return isPaintingConsistentWithSize;
	}

	public void setPaintingConsistentWithSize(boolean isPaintingConsistentWithSize) {

		this.isPaintingConsistentWithSize = isPaintingConsistentWithSize;
	}

	private ClassicalPaintingLocations paintingLocations;

	public ClassicalPaintingLocations getClassicalPaintingLocations() {

		return paintingLocations;

	}

	public void initialized() {
		listColors = new ArrayList<Color>();
		listColors.add(Color.RED);
		listColors.add(Color.GREEN);
		listColors.add(Color.BLUE);
		listColors.add(Color.YELLOW);
		listColors.add(Color.CYAN);

		paintingLocations = new ClassicalPaintingLocations();
	}

	public List<Color> getColors() {

		return listColors;
	}

	public Font getIntersectionValueFont() {
		return IntersectionValueFont;
	}

	public void setIntersectionValueFont(Font intersectionValueFont) {
		IntersectionValueFont = intersectionValueFont;
	}

	public Font getNameFont() {
		return nameFont;
	}

	public void setNameFont(Font nameFont) {
		this.nameFont = nameFont;
	}

	List<NameSelection> nameSelections = new ArrayList<>();

	public List<NameSelection> getNameSelections() {

		return nameSelections;
	}

	final float[] dash1 = { 2f, 0f, 2.0f };
	final BasicStroke dashedStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
			0.0f);

	public Stroke getDashedStroke() {
		return dashedStroke;
	}

	public void setShowSetLegend(boolean showSetLegend) {
		this.showSetLegend = showSetLegend;
	}

	public void setShowSetValues(boolean showSetValues) {
		this.showSetValues = showSetValues;

	}

	public List<Color> getListColors() {
		return listColors;
	}

	public boolean isShowSetValues() {
		return showSetValues;
	}

	public boolean isShowSetLegend() {
		return showSetLegend;
	}

	public ClassicalPaintingLocations getPaintingLocations() {
		return paintingLocations;
	}

	public float[] getDash1() {
		return dash1;
	}

}
