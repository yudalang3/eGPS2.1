package module.multiseqview;

import java.awt.Font;

import javax.swing.SwingUtilities;

import egps2.UnifiedAccessPoint;
import module.multiseqview.calculator.PaintingDataCalculator;
import module.multiseqview.model.Data4SequenceStructureView;
import module.multiseqview.model.GraphicsBean;
import module.multiseqview.model.paint.PaintingData;
import module.multiseqview.view.IntermediateContainer;
import module.multiseqview.view.PaintingPanel;

public class SequenceStructureViewController {

	final SequenceStructureViewMain main;

	private PaintingDataCalculator calculator = new PaintingDataCalculator();
	private IntermediateContainer container;
	private PaintingPanel paintingPanel;

	public final Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	public final Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private GraphicsBean graphicsBean = new GraphicsBean();

	private Data4SequenceStructureView data;

	public SequenceStructureViewController(SequenceStructureViewMain main) {
		this.main = main;
	}

	public void refresh() {

		SwingUtilities.invokeLater(() -> {
			container.refresh();
		});
	}

	public void recalculateAndRefresh() {
		int width = paintingPanel.getWidth();
		int height = paintingPanel.getHeight();

		if (width == 0 || height == 0) {
			System.err.println(getClass() + "   waring width or height is 0! ");
		}

		recalculateAndRefresh(width, height);
	}

	public void recalculateAndRefresh(int width, int height) {
		if (this.data == null) {
			return;
		}

		new Thread(() -> {
			PaintingData patingData = calculator.getPatingData(width, height, this.data);

			paintingPanel.setPaintingData(patingData, width, height);
			refresh();
		}).start();
	}

	public void setPaintingPanel(PaintingPanel sequenceStructureViewContainer) {
		this.paintingPanel = sequenceStructureViewContainer;

	}

	public SequenceStructureViewMain getMain() {
		return main;
	}

	public void setSequenceStructureViewContainer(IntermediateContainer container) {
		this.container = container;

	}

	public void setTransparent(int value) {
		graphicsBean.setTransparent(value);
		refresh();
	}

	public void setCurveRatio(double value) {
		graphicsBean.setCurveRatio(value);
		refresh();
	}

	public void setBarWidth(int value) {

	}

	public void setTitleFont(Font font) {
		graphicsBean.setSequenceFont(font);
		refresh();
	}

	public void setNameFont(Font font) {
		graphicsBean.setNameFont(font);
		refresh();
	}

	public void setGraphicsBean(GraphicsBean graphicsBean) {
		this.graphicsBean = graphicsBean;
	}

	public GraphicsBean getGraphicsBean() {
		return graphicsBean;
	}

	public void calculateListOfAlongSequenceDirections() {
		calculator.calculateListOfAlongSequenceDirections();

	}

	public void setData(Data4SequenceStructureView data) {
		this.data = data;
		recalculateAndRefresh();
	}
	
	public void setIntervalMargin(int marginHeight) {
		calculator.setIntervalMargin(marginHeight);
		recalculateAndRefresh();
	}

}
