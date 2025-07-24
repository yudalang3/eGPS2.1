package module.evolview.pathwaybrowser.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.compress.utils.Lists;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.google.common.collect.Maps;

import egps2.panels.dialog.SwingDialog;
import egps2.frame.gui.EGPSMainGuiUtil;
import module.pptxio.Decoder4pptx;

@SuppressWarnings("serial")
public class PathwayDetailsPanel extends JPanel {

	private XSLFSlide firstSlide;
	private String OUTname = "";
	private Dimension pageSize;

	private List<XSLFShape> skeletonGrobs = Lists.newArrayList();
	private Map<String, Grobs> name2componentGrobs = Maps.newHashMap();
	private Map<String, List<Short>> species2geneCountMap;
	private List<String> geneList;

	public PathwayDetailsPanel(String path, char geneNameSeparater) {

		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean rightMouseButton = SwingUtilities.isRightMouseButton(e);
				if (rightMouseButton) {
					PathwayDetailsPanel.this.repaint();
				}
			}
		};

		addMouseListener(mouseAdapter);

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

		for (XSLFShape shape : firstSlide.getShapes()) {
			String shapeName = shape.getShapeName();
			int indexOf = shapeName.indexOf(geneNameSeparater);
			if (indexOf > -1) {
				// has
				String name = shapeName.substring(0, indexOf);

				Grobs grobs = name2componentGrobs.get(name);
				if (grobs == null) {
					grobs = new Grobs();
					name2componentGrobs.put(name, grobs);
				}

				grobs.shapes.add(shape);

			} else {
				// no
				skeletonGrobs.add(shape);
			}
		}
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
			Font deriveFont = g2d.getFont().deriveFont(20f);
			g2d.setFont(deriveFont);
			String concat = "This is ".concat(OUTname);
			int stringWidth = g2d.getFontMetrics().stringWidth(OUTname);
			g2d.drawString(OUTname, (int) (this.getWidth() - 30 - stringWidth), 30);

		}



	}

	public void clickToSpecies(String name) {
		this.OUTname = name;
		List<XSLFShape> shapes = firstSlide.getShapes();
		shapes.clear();
		shapes.addAll(skeletonGrobs);

		List<Short> list = species2geneCountMap.get(name);
		int size = geneList.size();

		for (int i = 0; i < size; i++) {
			Short short1 = list.get(i);
			if (short1 != 0) {
				String string = geneList.get(i);
				Grobs grobs = name2componentGrobs.get(string);
				if (grobs == null) {
					System.err.println(string.concat(" NOT found."));
					continue;
				}
				grobs.addToShape(shapes);
			}
		}

		repaint();

	}

	public void setSpecies2CompMaps(List<String> geneList, Map<String, List<Short>> species2geneCountMap) {
		this.geneList = geneList;
		this.species2geneCountMap = species2geneCountMap;

	}

}

class Grobs {
	List<XSLFShape> shapes = Lists.newArrayList();

	void addToShape(List<XSLFShape> shapeContainer) {
		shapeContainer.addAll(shapes);
	}
}
