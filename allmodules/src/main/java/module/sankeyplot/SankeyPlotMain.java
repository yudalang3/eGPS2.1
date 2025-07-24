package module.sankeyplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Optional;

import javax.swing.SwingUtilities;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import egps2.frame.ModuleFace;
import module.sankeyplot.gui.PaintingPanel;
import module.sankeyplot.gui.SankeyPlotContainer;
import module.sankeyplot.model.ParameterModel;
import egps2.modulei.AdjusterFillAndLine;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class SankeyPlotMain extends ModuleFace implements AdjusterFillAndLine, IInformation {

	private final SankeyPlotController controller;

	public final Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	public final Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

//    final private JTabbedPane outterTabbedPane;
//    final private SankeyPlotDataInputPanel importView;

	private ParameterModel parameterModel;
	SankeyPlotContainer sankeyDrawerContainer;

	private final String[] FEATURES = new String[] { "Different color scheme", "Adjust transparency",
			"Adjust curve rate" };

	private VOICMSankeyPlotEGPS2 voicmSankeyPlotEGPS2;

	protected SankeyPlotMain(IModuleLoader moduleLoader) {
		super(moduleLoader);

		controller = new SankeyPlotController(this);

		sankeyDrawerContainer = new SankeyPlotContainer(this);

		setLayout(new BorderLayout());
		add(sankeyDrawerContainer, BorderLayout.CENTER);
	}

	public ParameterModel getParameterModel() {
		if (parameterModel == null) {
			parameterModel = new ParameterModel();
		}
		return parameterModel;
	}

	public SankeyPlotController getSankeyPlotController() {
		return controller;
	}

	public SankeyPlotController getController() {
		return controller;
	}

	public void weakUpdateGraphics() {
		sankeyDrawerContainer.repaint();

	}

	PaintingPanel getPaintingJpanel() {
		return sankeyDrawerContainer.getSankeyPlotDrawer();
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
		if (voicmSankeyPlotEGPS2 == null) {
			voicmSankeyPlotEGPS2 = new VOICMSankeyPlotEGPS2(this);
		}
		SwingDialog.QuickWrapperJCompWithDialog(voicmSankeyPlotEGPS2, "Data Import Panel", 1000, 800);
	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		SwingUtilities.invokeLater(() -> {
			getController().saveViewPanelAs();
		});
	}

	@Override
	public String[] getFeatureNames() {
		return FEATURES;
	}

	public void recordTheFeatureInvoked(int featureIndex) {
		recordFeatureUsed4user(FEATURES[featureIndex]);
	}

	@Override
	protected void initializeGraphics() {
		this.importData();
	}

	@Override
	public Optional<Color> couldSetFillColor() {
		boolean canSetColor = controller.canSetColor();
		Color currentColorStatus = null;
		if (canSetColor) {
			currentColorStatus = controller.getCurrentColorStatus();
		}
		return Optional.ofNullable(currentColorStatus);
	}

	@Override
	public Optional<Font> couldSetFont() {
		return Optional.empty();
	}

	@Override
	public Optional<Color> couldSetLineColor() {
		return Optional.empty();
	}

	@Override
	public Optional<Integer> couldSetLineThickness() {
		return Optional.empty();
	}

	@Override
	public void adjustFillColor(Color newCol) {
		controller.setColor(newCol);
	}

	@Override
	public void adjustFillFont(Font newFont) {
	}

	@Override
	public void adjustLineColor(Color newCol) {
	}

	@Override
	public void adjustLineThickness(int newThickNess) {
	}

	@Override
	public IInformation getModuleInfo() {
		return this;
	}

	@Override
	public String getWhatDataInvoked() {
		if (voicmSankeyPlotEGPS2 == null) {
			return "No data, click the import button to load data.";
		}
		return voicmSankeyPlotEGPS2.whatDataInvoked;
	}

	@Override
	public String getSummaryOfResults() {
		if (voicmSankeyPlotEGPS2 == null) {
			return "";
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("The eGPS software is employed make the sanky plot with ");
		stringBuilder.append(sankeyDrawerContainer.getNumberOfSets());
		stringBuilder.append(" sets. ");
		String string = sankeyDrawerContainer.toString();
		stringBuilder.append(string);
		return stringBuilder.toString();
	}
}
