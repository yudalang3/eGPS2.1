package module.tablelikeview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.UnifiedAccessPoint;
import module.tablelikeview.gui.NavigationPanel;
import module.tablelikeview.gui.SearchPanel;
import module.tablelikeview.gui.TableConfiguration;

@SuppressWarnings("serial")
public class SimpleLeftControlPanel extends JPanel {

	private MainGUI mainGUI;

	public SimpleLeftControlPanel(MainGUI mainGUI) {
		this.mainGUI = mainGUI;

		setBackground(Color.WHITE);
		setLayout(new BorderLayout());

		JXTaskPaneContainer jxTaskPaneContainer = new JXTaskPaneContainer();
		jxTaskPaneContainer.setBackground(Color.WHITE);
		jxTaskPaneContainer.setBackgroundPainter(null);

		addJXTaskPanes(jxTaskPaneContainer);

		JScrollPane jScrollPane = new JScrollPane(jxTaskPaneContainer);
		add(jScrollPane, BorderLayout.CENTER);

	}

	private void addJXTaskPanes(JXTaskPaneContainer jxTaskPaneContainer) {
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

		{
			JXTaskPane taskPanel = new JXTaskPane();
			taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			taskPanel.setTitle("Navigation Panel ");
			taskPanel.setFont(defaultTitleFont);

			NavigationPanel navigationPanel = new NavigationPanel(mainGUI);
			taskPanel.add(navigationPanel);
			jxTaskPaneContainer.add(taskPanel);
		}
		{
			JXTaskPane taskPanel = new JXTaskPane();
			taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			taskPanel.setTitle("Search Panel ");
			taskPanel.setFont(defaultTitleFont);

			SearchPanel navigationPanel = new SearchPanel(mainGUI);
			taskPanel.add(navigationPanel);
			jxTaskPaneContainer.add(taskPanel);
		}
		{
			JXTaskPane taskPanel = new JXTaskPane();
			taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			taskPanel.setTitle("Table Configuration Panel ");
			taskPanel.setFont(defaultTitleFont);

			TableConfiguration navigationPanel = new TableConfiguration(mainGUI);
			taskPanel.add(navigationPanel);
			jxTaskPaneContainer.add(taskPanel);
		}
	}

}
