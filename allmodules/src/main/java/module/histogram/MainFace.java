package module.histogram;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JSlider;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

import egps2.frame.ModuleFace;
import module.skeletonscatter.io.TSVImportInfoBean;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class MainFace extends ModuleFace {

	private HistogramDataImportHandler histogramDataImportHandler;

	MainFace(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());
	}

	@Override
	public boolean closeTab() {
		return false;
	}

	@Override
	public void changeToThisTab() {

	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		HistogramDataImportHandler handler = getHistogramDataImportHandler();
		handler.doUserImportAction();

	}

	public HistogramDataImportHandler getHistogramDataImportHandler() {
		if (histogramDataImportHandler == null) {
			histogramDataImportHandler = new HistogramDataImportHandler(this);
		}
		return histogramDataImportHandler;
	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void exportData() {

	}

	@Override
	public void initializeGraphics() {
		this.importData();
	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Quick plot histogram" };
	}


	public void loadingData(TSVImportInfoBean object) {
		removeAll();
		// Create Chart
		CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title(getClass().getSimpleName())
				.xAxisTitle("Mean").yAxisTitle("Count").theme(ChartTheme.Matlab).build();

		// Customize Chart
		CategoryStyler styler = chart.getStyler();
		styler.setLegendPosition(LegendPosition.InsideNE);
		styler.setAvailableSpaceFill(.96);
		styler.setOverlapped(true);
		styler.setPlotGridVerticalLinesVisible(false);

		styler.setLabelsRotation(60);
		styler.setXAxisLabelRotation(60);

		styler.setXAxisDecimalPattern("#.##");

		final String histogramString = "histogram";
		List<Double> data = object.loadData().getRight();

		Histogram histogram1 = new Histogram(data, 30);
		chart.addSeries(histogramString, histogram1.getxAxisData(), histogram1.getyAxisData());

		XChartPanel<CategoryChart> xChartPanel = new XChartPanel<>(chart);

//		xChartPanel.setSize(800, 800);
//		xChartPanel.setBounds(200, 0, 800, 800);
//		setLayout(null);
//		add(xChartPanel);

		add(xChartPanel, BorderLayout.CENTER);

		JSlider jSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 15);
		jSlider.setMajorTickSpacing(5);
		jSlider.setPaintTicks(true);
		jSlider.setPaintLabels(true);

		jSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				int value = jSlider.getValue();
				Histogram histogram1 = new Histogram(data, value);
				chart.removeSeries(histogramString);
				chart.addSeries(histogramString, histogram1.getxAxisData(), histogram1.getyAxisData());
				xChartPanel.repaint();
			}
		});

		jSlider.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		add(jSlider, BorderLayout.SOUTH);

		validate();

		invokeTheFeatureMethod(0);
	}

}
