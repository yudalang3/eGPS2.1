package module.treebuilder.fromdist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Arrays;
import java.util.List;

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
import module.evoltrepipline.BuildTreeAllRelatedParametersConfigGUI;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {

	private JTextArea jTextArea;

	Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	private ParameterPanel inputFileParameterPanel;

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());
	}

	/**
	 * <pre>
	 * |-TopConfigPanel----------------------------------------------------------------------
	 * ||Input ways-------------------------|Parameters---------
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
		JTabbedPane jideTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		jideTabbedPane.setBorder(BorderFactory.createEmptyBorder());

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
				String message = "Please check your input format";
				{
					JButton jButtonApply = new JButton("Run and turn to modern tree view");
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

		JPanel jPanel = new JPanel(new BorderLayout());
		inputFileParameterPanel = new ParameterPanel();

		jPanel.add(inputFileParameterPanel, BorderLayout.NORTH);
		JComponent centerPanel = BuildTreeAllRelatedParametersConfigGUI.getCenterPanel4buildTreeFromDist();
		jPanel.add(centerPanel, BorderLayout.CENTER);
		return jPanel;
	}

	protected void runTheProgram(String concat) {
		String filePath = inputFileParameterPanel.getFilePath();
		File file = new File(filePath);
		PLGeneticDistsFile2Phylotree plWeb2ObtainAlignment = new PLGeneticDistsFile2Phylotree(file);

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
		return new String[] { "Contruct phylogenetic tree from evolutionary distance." };
	}

	@Override
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The evolutionary distance file is imported by user.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The evolutionary distance to phylogenetic tree functionality is powered by the eGPS software.";
			}

			@Override
			public String getHowUserOperates() {
				return "Please refer to the parameters panel for details.";
			}
		};
		return iInformation;
	}

}
