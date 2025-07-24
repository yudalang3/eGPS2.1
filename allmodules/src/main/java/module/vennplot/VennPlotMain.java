package module.vennplot;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;

import com.jidesoft.swing.JideTabbedPane;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.model.filefilter.FileFilterTxt;
import egps2.utils.common.util.SaveUtil;
import egps2.UnifiedAccessPoint;
import egps2.frame.ModuleFace;
import module.vennplot.gui.ClassicalVennPlotOutterFrame;
import module.vennplot.gui.PlainTextInfoPanel;
import module.vennplot.gui.UpsetRPanelOuterPanel;
import module.vennplot.model.SetItem;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

public class VennPlotMain extends ModuleFace {
	private static final long serialVersionUID = 5585444420135977263L;

	private final VennPlotController controller;
	private JideTabbedPane jTabbedPane2;

	private UpsetRPanelOuterPanel upsetRPanel2;
	private ClassicalVennPlotOutterFrame classicalVennPlotPanel2;
	private PlainTextInfoPanel plainTextInfoPanel;

	public final Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	public final Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private SetItem[] setItems;

	protected VennPlotMain(IModuleLoader moduleLoader) {
		super(moduleLoader);

		controller = new VennPlotController(this);

		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		jTabbedPane2 = new JideTabbedPane(JideTabbedPane.LEFT);
		jTabbedPane2.setFont(defaultFont);
		jTabbedPane2.setSelectedTabFont(defaultTitleFont);
		jTabbedPane2.setBoldActiveTab(true);
//		jTabbedPane2.setColorTheme(JideTabbedPane.COLOR_THEME_WINXP);

		upsetRPanel2 = new UpsetRPanelOuterPanel(this);
		upsetRPanel2.setController(controller);
		classicalVennPlotPanel2 = new ClassicalVennPlotOutterFrame();
		classicalVennPlotPanel2.setController(controller);

		jTabbedPane2.add("Classical plot", classicalVennPlotPanel2);
		jTabbedPane2.add("Upset plot", upsetRPanel2);

		plainTextInfoPanel = new PlainTextInfoPanel();
		jTabbedPane2.add("Plain text info.", plainTextInfoPanel);
		plainTextInfoPanel.setController(controller);

		jTabbedPane2.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		setLayout(new BorderLayout());
		add(jTabbedPane2, BorderLayout.CENTER);

	}

	public void initializeAction() {
		upsetRPanel2.initializePanel();
		upsetRPanel2.assitantForMacOS();
		classicalVennPlotPanel2.initializePanel(setItems);
		plainTextInfoPanel.initializePanel();
	}

	public VennPlotController getController() {
		return controller;
	}

	public Component getSelectedComponent() {
		return jTabbedPane2.getSelectedComponent();
	}

	public void change2nextTab() {
		int selectedIndex = jTabbedPane2.getSelectedIndex();

		selectedIndex++;

		if (selectedIndex < jTabbedPane2.getComponentCount()) {
			jTabbedPane2.setSelectedIndex(selectedIndex);
		}
	}

	public void saveData() {
		JPanel jPanel = null;
		Component selectedComponent = jTabbedPane2.getSelectedComponent();
		if (selectedComponent instanceof UpsetRPanelOuterPanel) {
			UpsetRPanelOuterPanel subPanel1 = (UpsetRPanelOuterPanel) selectedComponent;
			jPanel = subPanel1.getPaintingPanel();

		} else if (selectedComponent instanceof PlainTextInfoPanel) {
			export4tables();

		} else {
			ClassicalVennPlotOutterFrame subPanel2 = (ClassicalVennPlotOutterFrame) selectedComponent;
			jPanel = subPanel2.getPaintingPanel();
		}

		if (jPanel != null) {
			new SaveUtil().saveData(jPanel);
		}

	}

	private void export4tables() {
		Preferences pref = Preferences.userNodeForPackage(this.getClass());
		String lastPath = pref.get("lastPath", "");

		SwingUtilities.invokeLater(() -> {
			// swing thread
			JFileChooser jfc = null;
			if (lastPath.length() > 0) {
				jfc = new JFileChooser(lastPath);
			} else {
				jfc = new JFileChooser();
			}

			jfc.setDialogTitle("Save the results as ... ");

			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);

			FileFilterTxt filterTxt = new FileFilterTxt();
			jfc.addChoosableFileFilter(filterTxt);

			if (jfc.showSaveDialog(UnifiedAccessPoint.getInstanceFrame()) == JFileChooser.APPROVE_OPTION) {
				String ext = "jpg";
				File selectedF = jfc.getSelectedFile();

				if (selectedF.exists()) {
					int res = JOptionPane.showConfirmDialog(UnifiedAccessPoint.getInstanceFrame(),
							"File exists, confirm to overwriting the original file?", "Warning",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (res != JOptionPane.OK_OPTION) {
						return;
					}
				} else {
					selectedF = new File(jfc.getSelectedFile().getPath() + "." + ext);
				}

				try {
					exportTextWorkCold(selectedF);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				jfc.setCurrentDirectory(selectedF.getParentFile());
				pref.put("lastPath", selectedF.getParent());

				SwingDialog.showInfoMSGDialog("Information", "Output successfully !");
			}
			// swing thread
		});

	}

	private void exportTextWorkCold(File selectedF) throws IOException {
		JTable table = plainTextInfoPanel.getTable();

		List<String> outputStrings = new LinkedList<>();

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int columnCount = model.getColumnCount();

		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < columnCount; i++) {
			String columnName = model.getColumnName(i);
			sBuilder.append(columnName).append("\t");
		}
		sBuilder.deleteCharAt(sBuilder.length() - 1);
		outputStrings.add(sBuilder.toString());

		int rowCount = model.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			sBuilder.setLength(0);
			for (int j = 0; j < columnCount; j++) {
				Object valueAt = model.getValueAt(i, j);
				sBuilder.append(valueAt.toString()).append("\t");
			}

			sBuilder.deleteCharAt(sBuilder.length() - 1);
			outputStrings.add(sBuilder.toString());
		}

		FileUtils.writeLines(selectedF, outputStrings);
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
		VennPlotDataImportLoader vennPlotDataImportLoader = new VennPlotDataImportLoader(this);
		SwingDialog.QuickWrapperJCompWithDialog(vennPlotDataImportLoader, "Data Import Panel", 1000, 800);

	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		SwingUtilities.invokeLater(() -> {
			saveData();
		});
	}

	@Override
	public String[] getFeatureNames() {
		String[] ret = new String[] { 
				"Classical venn plot",
				"Upset plot",
				"Plain text info.",
				"Paint with consistent size",
				"Upset plot show pie plot"};
		return ret;
	}
	
	@Override
	public void invokeTheFeatureMethod(int index) {
		super.invokeTheFeatureMethod(index);
	}

	public UpsetRPanelOuterPanel getUpsetRPanel() {
		return upsetRPanel2;
	}

	public SetItem[] getSetItems() {
		return setItems;
	}

	public void setSetItems(SetItem[] setItems) {
		this.setItems = setItems;
	}

	@Override
	public void initializeGraphics() {
		importData();
	}

	@Override
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The data is loading from the import dialog.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The string set relation ship states/venn plot/upset plot is generated by the eGPS software.";
			}
		};
		return iInformation;
	}

}
