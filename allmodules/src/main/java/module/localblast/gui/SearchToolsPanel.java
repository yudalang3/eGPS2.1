package module.localblast.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.jidesoft.swing.JideTabbedPane;

import module.localblast.gui.search.programe.BasicBlastnPanel;
import module.localblast.gui.search.programe.BasicBlastpPanel;
import module.localblast.gui.search.programe.SearchProgrameCommon;
import egps2.modulei.RunningTask;

@SuppressWarnings("serial")
public class SearchToolsPanel extends AbstractBasicPanel {

	private SearchProgrameCommon[] prgramePanels;
	private JTabbedPane tabbedPane_searchgrame;
	private BasicBlastnPanel blastnPanel;
	private BasicBlastpPanel blastpPanel;

	/**
	 * Create the panel.
	 * 
	 * @param localBlastMain
	 */
	public SearchToolsPanel(LocalBlastMain localBlastMain) {
		super(localBlastMain);
	}

	@Override
	protected JPanel initializeUpParametersPanel() {
		JPanel upParametersAndButtonPanel = new JPanel();
		upParametersAndButtonPanel.setLayout(new BorderLayout(0, 0));
		tabbedPane_searchgrame = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_searchgrame.setFont(defaultFont);

		upParametersAndButtonPanel.add(tabbedPane_searchgrame, BorderLayout.CENTER);
		upParametersAndButtonPanel.setBorder(null);

		{
			blastnPanel = new BasicBlastnPanel(this);
			JScrollPane component = new JScrollPane(blastnPanel);
			component.setBorder(null);
			tabbedPane_searchgrame.addTab("blastn", null, component,
					"Searches a nucleotide query against a nucleotide database");
		}
		{
			blastpPanel = new BasicBlastpPanel(this);
			JScrollPane component = new JScrollPane(blastpPanel);
			component.setBorder(null);
			tabbedPane_searchgrame.addTab("blastp", null, component,
					"Searches a protein query against a protein database");
		}

		prgramePanels = new SearchProgrameCommon[2];
		prgramePanels[0] = blastnPanel;
		prgramePanels[1] = blastpPanel;

		return upParametersAndButtonPanel;
	}

	@Override
	protected void processOpenDir() {
		SearchProgrameCommon cc = prgramePanels[tabbedPane_searchgrame.getSelectedIndex()];
		String targetDir = cc.getTargetDir();
		if (targetDir != null && targetDir.length() > 0) {

			try {
				Desktop.getDesktop().open(new File(targetDir).getParentFile());
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

	@Override
	protected void processRun() {
		RunningTask runningTask = new RunningTask() {

			@Override
			public int processNext() throws Exception {
				SearchProgrameCommon cc = prgramePanels[tabbedPane_searchgrame.getSelectedIndex()];
				cc.run(textArea_normal, textArea_error);
				return PROGRESS_FINSHED;
			}

			@Override
			public boolean isTimeCanEstimate() {
				return false;
			}
		};
		localBlastMain.registerRunningTask(runningTask);
	}

	public LocalBlastMain getLocalBlastMain() {
		return localBlastMain;
	}

	public JideTabbedPane getBottomTabbedPanel() {
		return bottomTabbedPanel;
	}

	public String getBlastnDBFilePath() {
		return blastnPanel.getDBPathString();
	}

	public String getBlastpDBFilePath() {
		return blastnPanel.getDBPathString();
	}

}
