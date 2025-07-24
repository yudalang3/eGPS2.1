package module.vennplot.gui.classical;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import egps2.UnifiedAccessPoint;
import module.vennplot.model.ClassicalParameterModel;
import module.vennplot.model.DataModel;
import module.vennplot.model.SetItem;
import module.vennplot.util.AdjustAndCalculate;

public class ClassicalPaintingPanelVenn extends JPanel {
	private static final long serialVersionUID = 6808095153501040921L;
	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private DataModel dataModel;

	private int r = 200;

	private double intersectionLength = 1;

	private ClassicalVennPlotPanel classicalVennPlotPanel;

	public ClassicalParameterModel parameterModel;

	public ClassicalPaintingPanelVenn(DataModel dataModel, ClassicalParameterModel parameterModel) {
		this.dataModel = dataModel;

		this.parameterModel = parameterModel;
	}

	public void setCircleR(int r) {
		this.r = r;
	}

	@Override
	public void paint(Graphics g) {
		setPreferredSize(new Dimension(getWidth(), getHeight()));
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setFont(defaultFont);

		int XPos = getWidth() / 2;

		int YPos = getHeight() / 2;

		AdjustAndCalculate adjustAndCalculate = new AdjustAndCalculate();

		SetItem[] setItems = dataModel.getSetItems();

		int numOfSets = dataModel.getNumOfSets();

		switch (numOfSets) {
		case 1:
			classicalVennPlotPanel = new OneClassicalVennPlotPanel();

			classicalVennPlotPanel.setCircleR(r);

			classicalVennPlotPanel.setIntersectionLength(intersectionLength);

			classicalVennPlotPanel.paint(g2d, setItems, parameterModel, adjustAndCalculate, XPos, YPos);
			break;
		case 2:

			classicalVennPlotPanel = new TwoClassicalVennPlotPanel();

			classicalVennPlotPanel.setCircleR(r);

			classicalVennPlotPanel.setIntersectionLength(intersectionLength);

			classicalVennPlotPanel.paint(g2d, setItems, parameterModel, adjustAndCalculate, XPos, YPos);

			break;
		case 3:

			classicalVennPlotPanel = new ThreeClassicalVennPlotPanel();

			classicalVennPlotPanel.setCircleR(r);

			classicalVennPlotPanel.setIntersectionLength(intersectionLength);

			classicalVennPlotPanel.paint(g2d, setItems, parameterModel, adjustAndCalculate, XPos, YPos);
			break;
		case 4:

			classicalVennPlotPanel = new FourthClassicalVennPlotPanel();

			classicalVennPlotPanel.setCircleR(r);

			classicalVennPlotPanel.setIntersectionLength(intersectionLength);

			classicalVennPlotPanel.paint(g2d, setItems, parameterModel, adjustAndCalculate, XPos, YPos);
			break;
		case 5:

			classicalVennPlotPanel = new FiveClassicalVennPlotPanel();

			classicalVennPlotPanel.setCircleR(r);

			classicalVennPlotPanel.setIntersectionLength(intersectionLength);

			classicalVennPlotPanel.paint(g2d, setItems, parameterModel, adjustAndCalculate, XPos, YPos);
			break;

		default:

			break;
		}

	}
}
