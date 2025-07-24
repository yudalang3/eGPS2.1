package module.pill.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.Timer;

import egps2.frame.gui.EGPSSwingUtil;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import egps2.panels.dialog.EGPSFileChooser;
import egps2.panels.dialog.SwingDialog;
import egps2.UnifiedAccessPoint;
import module.pill.gui.CtrlConvenientOperationPanel;
import module.pill.gui.CtrlPathwayCompoentsPanel;
import module.pptxio.Decoder4pptx;

public class SkeletonMaker {

	DrawingPanelSkeletonMaker drawingPanelSkeletonMaker = null;
//	ControlPanelOfSkelMaker controlPanelOfSkelMaker = null;
	JFrame mainFrame = null;
	private CtrlConvenientOperationPanel treeOperationPanel;
	private CtrlPathwayCompoentsPanel ctrlPathwayCompoentsPanel;

	public JSplitPane launchInFrame() {

		drawingPanelSkeletonMaker = new DrawingPanelSkeletonMaker(this);
		drawingPanelSkeletonMaker.setBackground(Color.WHITE);

		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp.setDividerLocation(320);
		jsp.setLeftComponent(getLeftToolPane());

		JScrollPane jScrollPane = new JScrollPane(drawingPanelSkeletonMaker);
		jScrollPane.setBorder(null);
		jsp.setRightComponent(jScrollPane);
		jsp.setDividerSize(7);

		return jsp;
	}

	private JPanel getLeftToolPane() {
		JPanel jPanel = new JPanel(new BorderLayout());
		JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();

		taskPaneContainer.setBackground(Color.WHITE);
		taskPaneContainer.setBackgroundPainter(null);

		addJXTaskPanels(taskPaneContainer);

		jPanel.setMinimumSize(new Dimension(290, 200));
		jPanel.add(new JScrollPane(taskPaneContainer), BorderLayout.CENTER);

		return jPanel;
	}

	public CtrlConvenientOperationPanel getTreeOperationPanel() {
		return treeOperationPanel;
	}

	public CtrlPathwayCompoentsPanel getCtrlPathwayCompoentsPanel() {
		return ctrlPathwayCompoentsPanel;
	}

	private void addJXTaskPanels(JXTaskPaneContainer taskPaneContainer) {
		Font titleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		{

			JXTaskPane taskPane = new JXTaskPane();
			taskPane.setFont(titleFont);
			String title = "Convevient operation";
			taskPane.setTitle(title);
			treeOperationPanel = new CtrlConvenientOperationPanel(drawingPanelSkeletonMaker);

			taskPane.add(treeOperationPanel);
			taskPaneContainer.add(taskPane);

		}
		// 暂时不加载这个面板
//		{
//
//			JXTaskPane taskPane = new JXTaskPane();
//			taskPane.setFont(titleFont);
//			String title = "Creative mode";
//			taskPane.setTitle(title);
//			CreativeModeTaskPanel treeOperationPanel = new CreativeModeTaskPanel();
//
//			taskPane.add(treeOperationPanel);
//			taskPaneContainer.add(taskPane);
//
//		}

		{

			JXTaskPane taskPane = new JXTaskPane();
			taskPane.setFont(titleFont);
			String title = "Pathway Compoents";
			taskPane.setTitle(title);
			ctrlPathwayCompoentsPanel = new CtrlPathwayCompoentsPanel(drawingPanelSkeletonMaker);

			taskPane.add(ctrlPathwayCompoentsPanel);
			taskPaneContainer.add(taskPane);

		}

	}

	void exportAsRCodes(boolean showDialog) {
		if (showDialog) {
			TextContainer textContainer = new TextContainer(mainFrame, "R codes output", true);
			textContainer.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);// 设置关闭模式
			textContainer.showText(drawingPanelSkeletonMaker);
			textContainer.setSize(800, 600);
			textContainer.setLocationRelativeTo(mainFrame);
			textContainer.setVisible(true);
		} else {
			// Replace JOptionPane.showXxxx(args) with new JOptionPane(args)

			TextContainer textContainer = new TextContainer(mainFrame, "R codes output", true);
			textContainer.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);// 设置关闭模式
			textContainer.showText(drawingPanelSkeletonMaker);

			JOptionPane pane = new JOptionPane(
					"The content already copied.\nThis dialog will close automatically 3 seconds later!");
			final JDialog dialog = pane.createDialog("Copy success!");
			Timer timer = new Timer(3000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
					EGPSSwingUtil.copyToClipboard(textContainer.getjTextArea().getText());
				}
			});
			timer.setRepeats(false);
			timer.start();
			dialog.setVisible(true);

		}

	}

	public void checkSpinner(int pnodeHalfOvalWidth, int pnodeHalfOvalHeight, int pnodeHalfRectWidth,
			int pnodeHalfRectHeight) {
		ctrlPathwayCompoentsPanel.checkSpinner(pnodeHalfOvalWidth, pnodeHalfOvalHeight, pnodeHalfRectWidth,
				pnodeHalfRectHeight);

	}

	public void restoreEraser2disabledState() {
		treeOperationPanel.restoreEraser2disabledState();
	}

	public JFrame getMainFrame() {
		if (mainFrame == null) {
			return UnifiedAccessPoint.getInstanceFrame();
		}
		return mainFrame;
	}

	public void importData() {
		EGPSFileChooser egpsFileChooser = new EGPSFileChooser(getClass());
		int showOpenDialog = egpsFileChooser.showOpenDialog();
		if (EGPSFileChooser.APPROVE_OPTION == showOpenDialog) {
			File selectedFile = egpsFileChooser.getSelectedFile();
			String name = selectedFile.getName();
			if (name.endsWith("xml")) {
				drawingPanelSkeletonMaker.setKEGGInputFile(selectedFile);
			} else if (name.endsWith("pptx")) {
				Decoder4pptx decoder4pptx = new Decoder4pptx();
				try {
					decoder4pptx.decodeFile(selectedFile);
				} catch (Exception e) {
					e.printStackTrace();
					SwingDialog.showErrorMSGDialog("Input error", "Please check the pptx file.");
				}

				drawingPanelSkeletonMaker.setSlide(decoder4pptx.getFirstSlide());
			} else {
				SwingDialog.showErrorMSGDialog("Unkonw file format", name.concat(" format is not supported."));
			}

		}

	}
}
