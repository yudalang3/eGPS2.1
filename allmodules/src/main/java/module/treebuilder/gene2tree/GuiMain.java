package module.treebuilder.gene2tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import module.evoltre.pipline.TreeParameterHandler;
import module.evoltrepipline.BuildTreeAllRelatedParametersConfigGUI;
import module.evoltrepipline.ConstantNameClass_WebQuery;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {

	private JTextArea jTextArea;

	Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());
	}

	/**
	 * <pre>
	 * |-TopConfigPanel----------------------------------------------------------------------
	 * ||VOICM------------------------------|BuildTreeAllRelatedParametersConfigGUI---------
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * ||                                   |
	 * |-------------------------------------------------------------------------------------
	 * |Console
	 * |
	 * |
	 * |
	 * |------------------------------------------------------------------------------------
	 * </pre>
	 */
	@Override
	protected void initializeGraphics() {

		LineBorder lineBorder = new LineBorder(Color.gray, 1, true);
		TitledBorder border = new TitledBorder(lineBorder, "Console: ", TitledBorder.LEADING, TitledBorder.TOP,
				defaultFont.deriveFont(Font.BOLD), Color.black);

		jTextArea = new JTextArea();
		jTextArea.setFont(defaultFont);
		JScrollPane jScrollPane = new JScrollPane(jTextArea);

		jScrollPane.setBorder(border);

		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, getTopConfigPanel(), jScrollPane);
		sp.setOneTouchExpandable(true);
		sp.setDividerSize(8);
		sp.setDividerLocation((int) (getHeight() * 0.84));
		sp.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		add(sp, BorderLayout.CENTER);

		setText4Console(Arrays.asList("Enter the \"Run\" button to run the remnant."));

	}

	private JComponent getTopConfigPanel() {
//		JideTabbedPane jideTabbedPane = new JideTabbedPane(JideTabbedPane.LEFT);
		JTabbedPane jideTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
//		jideTabbedPane.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		jideTabbedPane.setBorder(BorderFactory.createEmptyBorder());
//		jideTabbedPane.setAutoFocusOnTabHideClose(false);
//		jideTabbedPane.setFocusable(false);
//		jideTabbedPane.setRequestFocusEnabled(false);
//		jideTabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_WINXP);
//		jideTabbedPane.setTabShape(JideTabbedPane.SHAPE_ROUNDED_VSNET);


		jideTabbedPane.setFont(defaultTitleFont);
		// 先不用这个Tab因为感觉GUI很好用
		jideTabbedPane.addTab("Classic GUI panel", null, getCommonGUIsettingPanel(),
				"Setting parameters by the traditional user interface.");

		return jideTabbedPane;
	}

	private JComponent getCommonGUIsettingPanel() {

		BuildTreeAllRelatedParametersConfigGUI BuildTreeAllRelatedParametersConfigGUI = new BuildTreeAllRelatedParametersConfigGUI() {
			@Override
			protected void configurateJBottomPanel(JPanel jBottomPanel) {
				String message = "Please check your input, for example, either input gene symbol or genomic region";
				{
					JButton jButtonApply = new JButton("Run and turn to alignment view");
					jButtonApply.setFont(defaultFont);
					jButtonApply.setFocusable(false);
					jButtonApply.addActionListener(e -> {
						if (!checkParameters()) {
							SwingDialog.showErrorMSGDialog("Input error", message);
							return;
						}

						saveParameter();
						runTheProgram(null);
					});

					jBottomPanel.add(jButtonApply);
				}
//				{
//					JButton jButtonApply = new JButton("Run and save result to file");
//					jButtonApply.setFont(defaultFont);
//					jButtonApply.setFocusable(false);
//					jButtonApply.addActionListener(e -> {
//						if (!checkParameters()) {
//							SwingDialog.showErrorMSGDialog("Input error", message);
//							return;
//						}
//						saveParameter();
//						EGPSFileChooser egpsFileChooser = new EGPSFileChooser(GuiMain.this.getClass());
//						egpsFileChooser.setAcceptAllFileFilterUsed(false);
//						SaveFilterFasta saveFilterFasta = new SaveFilterFasta();
//						egpsFileChooser.setFileFilter(saveFilterFasta);
//						int showOpenDialog = egpsFileChooser.showOpenDialog();
//						if (showOpenDialog == EGPSFileChooser.APPROVE_OPTION) {
//							String defaultFormatName = saveFilterFasta.getDefaultFormatName();
//							String prefix = egpsFileChooser.getSelectedFile().getAbsolutePath();
//							final String concat = prefix + "." + defaultFormatName;
//							runTheProgram(concat);
//						}
//					});
//
//					jBottomPanel.add(jButtonApply);
//				}
			}
		};

		JComponent centerPanel = BuildTreeAllRelatedParametersConfigGUI.getCenterPanel4gene2geneTree();
		return centerPanel;
	}

	protected void runTheProgram(String concat) {
		ConstantNameClass_WebQuery constantNameClass_WebQuery = new ConstantNameClass_WebQuery();

		Map<String, String> parameterMap = new TreeParameterHandler().getBuildTreeParametersMap();
		String geneSym = parameterMap.get(constantNameClass_WebQuery.label1_geneSymbol);
		String genomicRegion = parameterMap.get(constantNameClass_WebQuery.label2_genomicRegion);

		PLWeb2ObtainPhyloTree plWeb2ObtainAlignment = null;

		if (geneSym.isEmpty()) {
			plWeb2ObtainAlignment = new PLWeb2ObtainPhyloTree(parameterMap, genomicRegion, true);
		} else {
			plWeb2ObtainAlignment = new PLWeb2ObtainPhyloTree(parameterMap, geneSym);
		}

		plWeb2ObtainAlignment.setOutputFile(concat);
		GuiMain.this.registerRunningTask(plWeb2ObtainAlignment);
		
		invokeTheFeatureMethod(0);
	}

	protected void setText4Console(List<String> inputs) {
		StringBuilder sb = new StringBuilder();
		for (String string : inputs) {
			sb.append("> ").append(string).append("\n");
		}

		// 命令行使用的时候是 null
		if (jTextArea == null) {
			return;
		}
		SwingUtilities.invokeLater(() -> {
			jTextArea.setText(sb.toString());
		});
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
		return new String[] {"Contruct phylogenetic tree from gene"};
	}
	
	@Override
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The gene symbol or genomic region is entered by user.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The gene to gene tree functionality is powered by the eGPS software.";
			}
			
			@Override
			public String getHowUserOperates() {
				return "The gene symbol or genomic region entered into the textfield.";
			}
		};
		return iInformation;
	}

}
