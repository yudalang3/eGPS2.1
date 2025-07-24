package module.localblast.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.jidesoft.swing.JideTabbedPane;

import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;

@SuppressWarnings("serial")
public class AbstractBasicPanel extends JPanel {

	protected Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
	protected Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();

	protected JTextArea textArea_normal;
	protected JTextArea textArea_error;
	protected JideTabbedPane bottomTabbedPanel;

	protected LocalBlastMain localBlastMain;

	public AbstractBasicPanel(LocalBlastMain localBlastMain) {
		this.localBlastMain = localBlastMain;

		bottomTabbedPanel = new JideTabbedPane();
		bottomTabbedPanel.setFont(defaultFont);
		bottomTabbedPanel.setPreferredSize(new Dimension(5, 120));

		textArea_normal = new JTextArea();
		textArea_normal.setWrapStyleWord(true);
		textArea_normal.setLineWrap(true);
		textArea_normal.setFont(defaultFont);
		JScrollPane component = new JScrollPane(textArea_normal);
		component.setBorder(null);
		bottomTabbedPanel.addTab("Normal output console", null, component, null);
		textArea_error = new JTextArea();
		textArea_error.setWrapStyleWord(true);
		textArea_error.setLineWrap(true);
		textArea_error.setFont(defaultFont);
		JScrollPane component2 = new JScrollPane(textArea_error);
		component2.setBorder(null);
		bottomTabbedPanel.addTab("Error console", null, component2, null);

		JPanel buttonsPanel = getButtonsPanel();
		bottomTabbedPanel.setTabTrailingComponent(buttonsPanel);

		JPanel upParametersPanel = initializeUpParametersPanel();
		JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upParametersPanel, bottomTabbedPanel);
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setDividerSize(9);
		jSplitPane.setResizeWeight(0.5);
		this.setLayout(new BorderLayout(0, 0));
		this.add(jSplitPane, BorderLayout.CENTER);

	}

	protected JPanel initializeUpParametersPanel() {
		JPanel upParametersPanel = new JPanel();
		TitledBorder titledBorder = BorderFactory.createTitledBorder("Input parameters:");
		titledBorder.setTitleFont(defaultTitleFont);
		upParametersPanel.setBorder(titledBorder);

		upParametersPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane(getUpParametersPanel());
		scrollPane.setBorder(null);
		upParametersPanel.add(scrollPane, BorderLayout.CENTER);
		return upParametersPanel;
	}

	protected JPanel getUpParametersPanel() {
		throw new UnsupportedOperationException();
	};

	protected JPanel getButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.white);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setBorder(null);

		JButton runButton = new JButton("Execute");
		runButton.setFont(defaultFont);
		runButton.addActionListener(e -> {
			processRun();
		});
		buttonsPanel.add(runButton);

		JButton openDirectoryButton = new JButton("Open directory");
		openDirectoryButton.setFont(defaultFont);
		openDirectoryButton.addActionListener(e -> {
			processOpenDir();
		});
		buttonsPanel.add(openDirectoryButton);

		JButton advanceModeButton = new JButton("Advanced mode");
		advanceModeButton.addActionListener(e -> {
			SwingDialog.showErrorMSGDialog("Information",
					"The advance parameters is beyond the scope of this remnant, please refer to the Blast software document.");
		});
		advanceModeButton.setFont(defaultFont);
		buttonsPanel.add(advanceModeButton);

		return buttonsPanel;
	}

	protected void processOpenDir() {

	}

	protected void processRun() {

	}

}
