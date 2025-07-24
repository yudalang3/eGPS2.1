package module.fastadumper;

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
import module.fastadumper.extractmatch.VOICM4FastaDumper;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ModuleFace {

	private VOICM4FastaDumper voicmImp;
	private JTextArea jTextArea;

	GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);

		setLayout(new BorderLayout());

		voicmImp = new VOICM4FastaDumper(this);
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
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The data is loading from the VOICM import dialog.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The fasta file is operated by the eGPS software.";
			}
			
			@Override
			public String getHowUserOperates() {
				return "Please see the input contents for details.";
			}
		};
		return iInformation;
	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Extract sequence from fasta files" };
	}

	@Override
	protected void initializeGraphics() {
		JPanel importDataDialog = voicmImp.generateImportDataDialogGUI();
		importDataDialog.setBorder(null);

		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();

		LineBorder lineBorder = new LineBorder(Color.gray, 1, true);
		TitledBorder border = new TitledBorder(lineBorder, "Console: ", TitledBorder.LEADING, TitledBorder.TOP,
				defaultFont.deriveFont(Font.BOLD), Color.black);

		jTextArea = new JTextArea();
		jTextArea.setFont(defaultFont);
		JScrollPane jScrollPane = new JScrollPane(jTextArea);

		jScrollPane.setBorder(border);

		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, importDataDialog, jScrollPane);
		sp.setDividerSize(8);
		int height = getHeight();
		if (height <= 0){
			throw new IllegalStateException("The value should great 0, please tell developers.");
		}
		sp.setDividerLocation((int) (height * 0.84));
		sp.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		add(sp, BorderLayout.CENTER);
		sp.setOneTouchExpandable(true);

		setText4Console(Arrays.asList("Here will report the progress."));
	}
	
	public void clearConsole() {
		SwingUtilities.invokeLater(() -> {
			jTextArea.setText("");
		});
	}

	public void setText4Console(List<String> inputs) {
		StringBuilder sb = new StringBuilder();
		for (String string : inputs) {
			sb.append("> ").append(string).append("\n");
		}

		// 命令行使用的时候是 null
		if (jTextArea == null) {
			return;
		}
		SwingUtilities.invokeLater(() -> {
			jTextArea.append(sb.toString());
		});
	}

}
