package module.evolview.pathwaybrowser.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.apache.poi.xslf.usermodel.XSLFSlide;

import egps2.panels.dialog.SwingDialog;
import egps2.frame.gui.EGPSMainGuiUtil;
import egps2.utils.common.util.poi.pptx.Decoder4pptx;

@SuppressWarnings("serial")
public class EvoSelectionPressurePanel extends JPanel {

	private XSLFSlide firstSlide;

	public EvoSelectionPressurePanel(String path) {

		Decoder4pptx decoder4pptx = new Decoder4pptx();
		try {
			decoder4pptx.decodeFile(path);
		} catch (Exception e) {
			e.printStackTrace();
			SwingDialog.showErrorMSGDialog("Input error", "Please check the pptx file.");
		}

		firstSlide = decoder4pptx.getFirstSlide();
		Dimension pageSize = decoder4pptx.getPageSize();
		setPreferredSize(pageSize);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		EGPSMainGuiUtil.setupHighQualityRendering(g2d);
		firstSlide.draw(g2d);
	}


}
