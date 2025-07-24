package module.evolview.genebrowser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import utils.EGPSObjectsUtil;
import egps2.EGPSProperties;
import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.beans.LefControlPanelCollapseProperties;
import module.evolview.gfamily.work.gui.ControlPanelContainner;
import module.evolview.gfamily.work.gui.CtrlGenomeBrowserPanel;
import module.evolview.gfamily.work.gui.CtrlSignalingComps;

@SuppressWarnings("serial")
public class GBControlPanelContainner extends ControlPanelContainner {
	protected GeneFamilyController controller;
	protected String PERSISTENT_STORE_PATH = EGPSProperties.JSON_DIR.concat("/gb_control_panel.json");
	private JXTaskPaneContainer taskPaneContainer;

	private final Font globalFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	private final Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	/**
	 * Create the panel.
	 *
	 * @param controller
	 */
	public GBControlPanelContainner(GeneFamilyController controller) {

		setLayout(new BorderLayout());

		this.controller = controller;
		controller.setLeftControlPanelContainner(this);

		taskPaneContainer = new JXTaskPaneContainer();

		taskPaneContainer.setBackground(Color.WHITE);
		taskPaneContainer.setBackgroundPainter(null);

		addJXTaskPanels(taskPaneContainer);

		add(new JScrollPane(taskPaneContainer), BorderLayout.CENTER);
//		JLabel jLabel = new JLabel("  Gene struct browser");
//		jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
//		jLabel.setFont(titleFont);
//		jLabel.setPreferredSize(new Dimension(20, 30));
//		jLabel.setBackground(Color.white);
//		Border createRaisedSoftBevelBorder = BorderFactory.createRaisedSoftBevelBorder();
//		jLabel.setOpaque(true);
//		jLabel.setBorder(createRaisedSoftBevelBorder);
//		add(jLabel, BorderLayout.NORTH);
	}

	protected void addJXTaskPanels(JXTaskPaneContainer taskPaneContainer) {
		List<JXTaskPane> listOfJxTaskPanes = new LinkedList<>();

//		listOfJxTaskPanes.add(getComponentsPanel());

		listOfJxTaskPanes.add(getGenomeBrowserTaskPanel());

		File jsonFile = new File(PERSISTENT_STORE_PATH);
		LefControlPanelCollapseProperties bean = null;
		if (jsonFile.exists()) {
			try {
				bean = EGPSObjectsUtil.obtainJavaBeanByFastaJSON(LefControlPanelCollapseProperties.class, jsonFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (bean == null) {
			bean = new LefControlPanelCollapseProperties();
		}

		boolean[] isCollapseArray = bean.getIsCollapseArray();
		int size = listOfJxTaskPanes.size();
		for (int i = 0; i < size; i++) {
			boolean b = isCollapseArray[i];
			JXTaskPane jxTaskPane = listOfJxTaskPanes.get(i);
			jxTaskPane.setCollapsed(b);

			taskPaneContainer.add(jxTaskPane);
		}

	}

	@Override
	public void actionForSaveFile() {
		Component[] components = taskPaneContainer.getComponents();

		List<Boolean> temp = new LinkedList<>();
		for (Component component : components) {
			if (component instanceof JXTaskPane) {
				JXTaskPane cc = (JXTaskPane) component;
				boolean collapsed = cc.isCollapsed();
				temp.add(collapsed);
			}
		}

		LefControlPanelCollapseProperties bean = new LefControlPanelCollapseProperties();
		boolean[] arrary = new boolean[temp.size()];
		for (int i = 0; i < arrary.length; i++) {
			arrary[i] = temp.get(i);
		}
		bean.setIsCollapseArray(arrary);
		try {
			EGPSObjectsUtil.persistentSaveJavaBeanByFastaJSON(bean, new File(PERSISTENT_STORE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected JXTaskPane getGenomeBrowserTaskPanel() {
		JXTaskPane taskPane = new JXTaskPane();
		taskPane.setFont(titleFont);
		taskPane.setTitle("Gene browser");
		CtrlGenomeBrowserPanel panel = new CtrlGenomeBrowserPanel();
		panel.setController(controller);

		controller.setLeftGenomeBrowser(panel);
		taskPane.add(new JScrollPane(panel));
		return taskPane;
	}

//	protected JXTaskPane getCustomizedBrowserTracksTaskPanel() {
//		JXTaskPane taskPane = new JXTaskPane();
//		taskPane.setFont(getTitleFont());
//		taskPane.setTitle("Customized browser tracks");
//		LeftCustomizedBrowserTracks panel = new LeftCustomizedBrowserTracks(controller);
//
//		taskPane.add(new JScrollPane(panel));
//		return taskPane;
//	}

	private JXTaskPane getComponentsPanel() {
		JXTaskPane taskPane = new JXTaskPane();
		taskPane.setCollapsed(false);
		taskPane.setFont(titleFont);
		taskPane.setTitle("Signaling pathway components");

		CtrlSignalingComps debugPanel = new CtrlSignalingComps();
		debugPanel.setController(controller);
		taskPane.add(new JScrollPane(debugPanel));

		return taskPane;
	}

}
