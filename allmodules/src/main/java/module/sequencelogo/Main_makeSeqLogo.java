package module.sequencelogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.painter.MattePainter;

import egps2.UnifiedAccessPoint;
import egps2.frame.ModuleFace;
import egps2.frame.MyFrame;
import egps2.frame.gui.EGPSMainGuiUtil;
import module.sequencelogo.gui.MakeSeqLogoJpanel;
import module.sequencelogo.gui.MakeSeqLogoThroughMEMEChipFilePanel;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

/**
 * 
 * @ClassName: Main_makeSeqLogo
 * @Description: 绘制sequence logo的主类
 * @author zjw
 * @date 2024-04-28 03:19:59
 */
@SuppressWarnings("serial")
public class Main_makeSeqLogo extends ModuleFace {

	MakeSeqLogoController controller;
	private Font defaultFont;
	private JSplitPane jSplitPane;

	private JPanel rightJPanel;
	private JXTaskPaneContainer jxTaskPaneContainer;

	private JScrollPane jScrollPanel = new JScrollPane();

	public Main_makeSeqLogo(IModuleLoader moduleLoader) {
		super(moduleLoader);
		controller = new MakeSeqLogoController(this);
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());

		jxTaskPaneContainer = new JXTaskPaneContainer();
		jxTaskPaneContainer.setBackground(Color.white);
		MattePainter mattePainter = new MattePainter(Color.white);
		jxTaskPaneContainer.setBackgroundPainter(mattePainter);

		MakeSeqLogoJpanel panel1 = new MakeSeqLogoJpanel(controller);
		JXTaskPane taskPane1 = new JXTaskPane();
		taskPane1.add(panel1);
		taskPane1.setFont(defaultFont);
		taskPane1.setTitle("Generate sequence logo");
		jxTaskPaneContainer.add(taskPane1);

		MakeSeqLogoThroughMEMEChipFilePanel panel2 = new MakeSeqLogoThroughMEMEChipFilePanel(controller);
		JXTaskPane taskPane2 = new JXTaskPane();
		taskPane2.add(panel2);
		taskPane2.setFont(defaultFont);
		taskPane2.setTitle("Generate sequence logo by MEME-ChIP out file");
		taskPane2.setCollapsed(true);
		jxTaskPaneContainer.add(taskPane2);
//		add((Component) jxTaskPaneContainer, "Center");


		jSplitPane = new JSplitPane();
//		jSplitPane.setLeftComponent(jxTaskPaneContainer);
		JPanel rightJPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				EGPSMainGuiUtil.setupHighQualityRendering(g2d);
				EGPSMainGuiUtil.drawLetUserImportDataString(g2d);
			}
		};
		rightJPanel.setBackground(Color.white);
		this.setRightJPanel(rightJPanel);

//		this.add(jSplitPane, BorderLayout.CENTER);
		this.add(jScrollPanel, BorderLayout.CENTER);
//		SwingUtilities.invokeLater(() -> {
//			jSplitPane.setDividerLocation(0.3);
//		});
	}

	public void setRightJPanel(JPanel rightJPanel) {
		this.rightJPanel = rightJPanel;
//		jSplitPane.setRightComponent(rightJPanel);

		jScrollPanel.setViewportView(rightJPanel);
	}

	public JPanel getRightJPanel() {
		return rightJPanel;
	}
	
	public JPanel getJxTaskPaneContainer() {
		return jxTaskPaneContainer;
	}
	
	public MakeSeqLogoController getController() {
		return controller;
	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Plot the sequence logo" };
	}

	@Override
	public void importData() {

		MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();
		JDialog jDialog = new JDialog(instanceFrame);
		jDialog.setTitle("Import Data");
		JScrollPane jScrollPane = new JScrollPane(jxTaskPaneContainer);
		jScrollPane.setBorder(null);
		jDialog.add(jScrollPane, BorderLayout.CENTER);

		EGPSMainGuiUtil.addEscapeListener(jDialog);

		jDialog.setSize(800, 700);
		jDialog.setLocationRelativeTo(instanceFrame);
		jDialog.setVisible(true);

	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		SwingUtilities.invokeLater(() -> {
			controller.saveViewPanelAs();
		});
	}

	@Override
	protected void initializeGraphics() {
		importData();
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
