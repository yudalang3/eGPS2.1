package module.evolview.pathwaybrowser.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;

import egps2.panels.dialog.SwingDialog;
import egps2.frame.gui.EGPSMainGuiUtil;
import egps2.utils.common.util.poi.pptx.Decoder4pptx;

@SuppressWarnings("serial")
public class PathwayStatisticsPanel extends JPanel {

	private XSLFSlide firstSlide;
	private String OUTname = "";
	private Dimension pageSize;
	private List<String> categoryList;
	private Map<String, List<Short>> species2geneCountMap;
	private String categoryColumnName;
	Map<String, Integer> cat2countMap = new HashMap<>();

	public PathwayStatisticsPanel(String path, String categoryColumnName) {
		this.categoryColumnName = categoryColumnName;

		Decoder4pptx decoder4pptx = new Decoder4pptx();
		try {
			decoder4pptx.decodeFile(path);
		} catch (Exception e) {
			e.printStackTrace();
			SwingDialog.showErrorMSGDialog("Input error", "Please check the pptx file.");
		}

		firstSlide = decoder4pptx.getFirstSlide();
		pageSize = decoder4pptx.getPageSize();
		setPreferredSize(pageSize);

//		List<XSLFShape> shapes = firstSlide.getShapes();
//
//		for (XSLFShape xslfShape : shapes) {
//			String shapeName = xslfShape.getShapeName();
//			if (shapeName.startsWith(categoryColumnName)) {
//				cat2countMap.put(shapeName, xslfShape);
//			}
//		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		{
			Graphics2D g2d = (Graphics2D) g.create();
			EGPSMainGuiUtil.setupHighQualityRendering(g2d);
			firstSlide.draw(g2d);
		}

		{
			Graphics2D g2d = (Graphics2D) g.create();
			EGPSMainGuiUtil.setupHighQualityRendering(g2d);
			List<XSLFShape> shapes = firstSlide.getShapes();
			for (XSLFShape shape : shapes) {

				if (shape instanceof XSLFTextBox) {
					XSLFTextBox textShape = (XSLFTextBox) shape;
					String text = textShape.getShapeName();
					Integer integer = cat2countMap.get(text);
					if (integer == null) {
						continue;
					}
					Rectangle2D anchor = textShape.getAnchor();
					double maxX = anchor.getMaxX() - 50;
					double minY = anchor.getMinY();
					g2d.drawString("Count: " + integer, (int) maxX, (int) minY);
				}
			}

			Font deriveFont = g2d.getFont().deriveFont(20f);
			g2d.setFont(deriveFont);
			int stringWidth = g2d.getFontMetrics().stringWidth(OUTname);
			g2d.drawString(OUTname, (int) (this.getWidth() - 30 - stringWidth), 30);

		}

	}

	public void clickToSpecies(String name) {
		this.OUTname = name;

		List<Short> list = species2geneCountMap.get(name);
		int size = categoryList.size();

		cat2countMap.clear();
		for (int i = 0; i < size; i++) {
			String cat = categoryList.get(i);
			Short value = list.get(i);

			cat2countMap.put(cat, cat2countMap.getOrDefault(cat, 0) + value);
		}

		repaint();

	}

	public void setSpeciesCategory2CompMaps(List<String> categoryList, Map<String, List<Short>> gene2countMap) {
		this.categoryList = categoryList;
		this.species2geneCountMap = gene2countMap;

	}

}
