package module.brutemapping;

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

import egps2.LaunchProperty;
import egps2.UnifiedAccessPoint;
import egps2.frame.ComputationalModuleFace;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ComputationalModuleFace {

	VOICM4BruteMapping voicm = new VOICM4BruteMapping(this);
	private JTextArea jTextArea;

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);

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
	protected void initializeGraphics() {
		JPanel importDataDialog = voicm.generateImportDataDialogGUI();
		importDataDialog.setBorder(null);

		LaunchProperty lauchProperty = UnifiedAccessPoint.getLaunchProperty();
		Font defaultFont = lauchProperty.getDefaultFont();

		LineBorder lineBorder = new LineBorder(Color.gray, 1, true);
		TitledBorder border = new TitledBorder(lineBorder, "Console: ", TitledBorder.LEADING, TitledBorder.TOP,
				defaultFont.deriveFont(Font.BOLD), Color.black);

		jTextArea = new JTextArea();
		jTextArea.setFont(defaultFont);
		JScrollPane jScrollPane = new JScrollPane(jTextArea);

		jScrollPane.setBorder(border);

		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, importDataDialog, jScrollPane);
		sp.setOneTouchExpandable(true);
		sp.setDividerSize(8);
		sp.setDividerLocation((int) (getHeight() * 0.84));
		sp.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		add(sp, BorderLayout.CENTER);

		setText4Console(Arrays.asList("Enter the \"Import and execute\" button to run the remnant."));
		
		add(sp, BorderLayout.CENTER);

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

}
