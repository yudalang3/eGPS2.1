package module.sankeyplot;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.jidesoft.swing.JideTabbedPane;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.sankeyplot.gui.SankeyPlotContainer;
import module.sankeyplot.gui.SankeyPlotDataInputPanel;
import module.sankeyplot.model.StringElements;
import module.vennplot.model.SetItem;

@SuppressWarnings("serial")
public class VOICMSankeyPlotEGPS2 extends JPanel {

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
	private SankeyPlotDataInputPanel importView;
	private SankeyPlotContainer sankeyDrawerContainer;
	String whatDataInvoked;

	protected VOICMSankeyPlotEGPS2(SankeyPlotMain sankeyPlotMain) {
		setLayout(new BorderLayout());

		importView = new SankeyPlotDataInputPanel();

		sankeyDrawerContainer = sankeyPlotMain.sankeyDrawerContainer;
		Runnable action = () -> {
			SetItem[] setItems = importView.getSetItems();
			loadTheSankyPlot(setItems);
			whatDataInvoked = "Data is manually input to the visual box.";
		};
		
		importView.setAction4VisualizeButton(action);
		
		SankeyPlotImportHandler sankeyPlotImportHandler = new SankeyPlotImportHandler(this);

		JideTabbedPane jideTabbedPane = new JideTabbedPane();
		jideTabbedPane.addTab("Visual data input", importView);

		JPanel importDataDialog = sankeyPlotImportHandler.generateImportDataDialogGUI();
		jideTabbedPane.addTab("Versatile open input click remnant", importDataDialog);

		jideTabbedPane.setSelectedTabFont(titleFont);
		add(jideTabbedPane, BorderLayout.CENTER);

	}

	void loadTheSankyPlot(SetItem[] setItems) {
		if (setItems == null) {
			String errorString = importView.getErrorString();
			SwingDialog.showErrorMSGDialog("Data error", errorString);
		} else {

			if (whetherDataNotFitCondition(setItems)) {
				SwingDialog.showErrorMSGDialog("Data error", "Sorry the data of three sets should be identical!");
				return;
			}

			List<StringElements> list = new ArrayList<>();

			int len = setItems[0].getSetElements().length;
			int numOfSets = setItems.length;
			for (int i = 0; i < len; i++) {
				String[] ttArray = new String[numOfSets];
				for (int j = 0; j < numOfSets; j++) {
					ttArray[j] = setItems[j].getSetElements()[i].toString();
				}
				list.add(new StringElements(ttArray));
			}

			String[] setNames = new String[numOfSets];
			for (int i = 0; i < numOfSets; i++) {
				setNames[i] = setItems[i].getName();
			}
			sankeyDrawerContainer.initializePanel(list, setNames);
		}
	}

	private boolean whetherDataNotFitCondition(SetItem[] setItems) {
		int len = setItems[0].getSetElements().length;
		for (int i = 1; i < setItems.length; i++) {
			if (setItems[i].getSetElements().length != len) {
				return true;
			}
		}
		return false;
	}

}
