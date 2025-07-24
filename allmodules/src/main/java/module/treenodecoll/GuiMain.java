package module.treenodecoll;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import egps2.UnifiedAccessPoint;
import egps2.frame.ModuleFace;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ModuleFace {

	private VOICM4TreeLeafInfoGainer voicm4TreeLeafInfoGainer;
	private JTextArea jTextArea;
	
	private String[] features = new String[] {"Export leaf name", "ladderize the tree"};

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());

		voicm4TreeLeafInfoGainer = new VOICM4TreeLeafInfoGainer(this);
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
		return false;
	}

	@Override
	public void importData() {

	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void exportData() {

	}

	@Override
	public String[] getFeatureNames() {
		return features;
	}
	
	public void recordTheExportFunction() {
		recordFeatureUsed4user(features[0]);
	}
	
	public void recordTheLadderizeFunction() {
		recordFeatureUsed4user(features[1]);
	}

	@Override
	protected void initializeGraphics() {

		JPanel importDataDialog = voicm4TreeLeafInfoGainer.generateImportDataDialogGUI();
//		add(importDataDialog, BorderLayout.CENTER);
		importDataDialog.setBorder(null);

		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
//		Font defaultTitleFont = UniSoftInstance.getLauchProperty().defaultTitleFont;

		LineBorder lineBorder = new LineBorder(Color.gray, 1, true);
//		Border border2 = BorderFactory.createDashedBorder(null);
//		EmptyBorder emptyBorder = new EmptyBorder(6, 6, 6, 6);
		TitledBorder border = new TitledBorder(lineBorder, "Console: ", TitledBorder.LEADING, TitledBorder.TOP,
				defaultFont.deriveFont(Font.BOLD), Color.black);

		jTextArea = new JTextArea();
		jTextArea.setFont(defaultFont);
		JScrollPane jScrollPane = new JScrollPane(jTextArea);
//		jScrollPane.setBorder(null);

//		JXTitledPanel jxTitledPanel = new JXTitledPanel("Console: ", jScrollPane);
//		jxTitledPanel.setTitleFont(defaultTitleFont);

		jScrollPane.setBorder(border);
//		jScrollPane.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, importDataDialog, jScrollPane);
		sp.setOneTouchExpandable(true);
		sp.setDividerSize(8);
		sp.setDividerLocation((int) (getHeight() * 0.84));
		sp.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		add(sp, BorderLayout.CENTER);

		setText4Console(Arrays.asList("Enter the \"Import and execute\" button to run the remnant."));

	}

	protected void setText4Console(List<String> inputs) {
		StringBuilder sb = new StringBuilder();
		for (String string : inputs) {
			sb.append("> ").append(string).append("\n");
		}

		//命令行使用的时候是 null
		if (jTextArea == null) {
			return;
		}
		SwingUtilities.invokeLater(() -> {
			jTextArea.setText(sb.toString());
		});
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
				return "The functionality is powered by the eGPS software.";
			}
		};
		return iInformation;
	}

}
