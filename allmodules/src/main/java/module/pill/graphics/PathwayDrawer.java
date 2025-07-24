package module.pill.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import graphic.engine.colors.EGPSColors;
import module.pill.io.KEGGXML2JavaShapeParser;

public class PathwayDrawer {

	private GraphicsContainer gContainer = new GraphicsContainer();
	private Map<Integer, GraphBaseNotation> id2GraphicNodeMap;

	private boolean hasInputFile;
	private final KEGGXML2JavaShapeParser parser;

	public PathwayDrawer() {
		parser = new KEGGXML2JavaShapeParser() {
			@Override
			protected void parseGraphicElment(Element entry, Element graphicsElement) {

				String id = entry.attributeValue("id");
				String name = entry.attributeValue("name");
				String type = entry.attributeValue("type");
				// [gene, map, compound, group]
				String link = entry.attributeValue("link");

				String graphicsName = graphicsElement.attributeValue("name");
				String fgcolor = graphicsElement.attributeValue("fgcolor");
				String bgcolor = graphicsElement.attributeValue("bgcolor");
				String typeGraphics = graphicsElement.attributeValue("type");
				int x = Integer.parseInt(graphicsElement.attributeValue("x"));
				int y = Integer.parseInt(graphicsElement.attributeValue("y"));

				int width = Integer.parseInt(graphicsElement.attributeValue("width"));
				int height = Integer.parseInt(graphicsElement.attributeValue("height"));

				// x += width / 2;
//				y -= height / 2;

//				System.out.println("ID: " + id);
//				System.out.println("Name: " + name);
//				System.out.println("Type: " + type);
//				System.out.println("Link: " + link);
//				System.out.println("Graphics Name: " + graphicsName);
//				System.out.println("Foreground Color: " + fgcolor);
//				System.out.println("Background Color: " + bgcolor);
//				System.out.println("Type: " + typeGraphics);
//				System.out.println("Position (x, y): (" + x + ", " + y + ")");
//				System.out.println("Size (width, height): (" + width + ", " + height + ")");

				List<GraphBaseNotation> objects = gContainer.getGeneObjs();
				List<GraphBaseNotation> maps = gContainer.getMapObjs();
				List<GraphBaseNotation> groups = gContainer.getGroups();
				List<GraphBaseNotation> compoundObjs = gContainer.getCompoundObjs();

				GraphBaseNotation graphBaseNotation = new GraphBaseNotation();
				Color gfColorInstance = EGPSColors.parseColor(fgcolor);
				Color bgColorInstance = EGPSColors.parseColor(bgcolor);
				graphBaseNotation.setFgColor(gfColorInstance);
				graphBaseNotation.setBgColor(bgColorInstance);
				graphBaseNotation.setIdentifier(name);
				graphBaseNotation.setId(Integer.parseInt(id));

				// // [gene, map, compound, group]

				if ("gene".equals(type)) {

					Rectangle2D shape = new Rectangle2D.Float(x, y, width, height);
					graphBaseNotation.setPaintingShape(shape);

					// 这里要特殊处理
					int indexOf = graphicsName.indexOf(',');
					if (indexOf > 0) {

						String newName = graphicsName.substring(0, indexOf);
						graphBaseNotation.setName(newName);
					} else {
						graphBaseNotation.setName(graphicsName);
					}

					objects.add(graphBaseNotation);
				} else if ("map".equals(type)) {
					RoundRectangle2D shape = new RoundRectangle2D.Float(x, y, width, height, 8, 8);
					graphBaseNotation.setPaintingShape(shape);
					graphBaseNotation.setName(graphicsName);

					maps.add(graphBaseNotation);
				} else if ("compound".equals(type)) {
					// 注意，这里应该是圆心
					Ellipse2D shape = new Ellipse2D.Float(x, y + height / 2, width, height);
					graphBaseNotation.setPaintingShape(shape);
					graphBaseNotation.setName(graphicsName);

					compoundObjs.add(graphBaseNotation);
				} else if ("group".equals(type)) {
					// 他的 name是 undefined
					// 它还有 component 属性，这个暂时先不考虑。先不做这个了，它对于计算很有帮助。
					Rectangle2D shape = new Rectangle2D.Float(x, y, width, height);
					graphBaseNotation.setPaintingShape(shape);
					graphBaseNotation.setName("");
					groups.add(graphBaseNotation);
				}

			}

			@Override
			protected void parseRelationElment(Element entry) {
				String entry1 = entry.attributeValue("entry1");
				String entry2 = entry.attributeValue("entry2");
				// PPrel PCrel
				String type = entry.attributeValue("type");

				GraphBaseRelation graphBaseRelation = new GraphBaseRelation();
				graphBaseRelation.setEntry1(Integer.parseInt(entry1));
				graphBaseRelation.setEntry2(Integer.parseInt(entry2));

				List<GraphBaseRelation> proteinProteinRelations = gContainer.getProteinProteinRelations();
				List<GraphBaseRelation> proteinCompoundRelations = gContainer.getProteinCompoundRelations();

				if ("PCrel".equals(type)) {
					proteinCompoundRelations.add(graphBaseRelation);
				} else if ("PPrel".equals(type)) {
					proteinProteinRelations.add(graphBaseRelation);
				}

				List<Element> elements = entry.elements("subtype");
				for (Element ele : elements) {
					String name = ele.attributeValue("name");
					String value = ele.attributeValue("value");
					List<RelationSubtype> subTypes = graphBaseRelation.getSubTypes();
					RelationSubtype relationSubtype = new RelationSubtype();
					relationSubtype.setName(name);
					relationSubtype.setValue(value);
					subTypes.add(relationSubtype);
				}
			}
		};

	}

	public GraphicsContainer getgContainer() {
		return gContainer;
	}

	public void draw(Graphics2D g2d, int canvasWidth, int canvasHeight) {
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int height = fontMetrics.getHeight();

		g2d.setColor(Color.black);
		drawRelationship(g2d);

		// 细胞膜
		g2d.setColor(new Color(128, 128, 128));
		g2d.fill3DRect(150 + 10, 20, 4, canvasHeight - 40, true);
		g2d.fill3DRect(150 + 30, 20, 4, canvasHeight - 40, true);

		List<GraphBaseNotation> objects = gContainer.getGeneObjs();
		for (GraphBaseNotation notation : objects) {
			RectangularShape paintingShape = notation.getPaintingShape();
			g2d.setColor(notation.getBgColor());
			g2d.fill(paintingShape);

			g2d.setColor(notation.getFgColor());
			g2d.draw(paintingShape);
			String name = notation.getName();
			int stringWidth = fontMetrics.stringWidth(name);
			g2d.setColor(Color.black);
			g2d.drawString(name, (int) paintingShape.getCenterX() - stringWidth / 2,
					(int) paintingShape.getCenterY() + height / 2);
		}
		List<GraphBaseNotation> maps = gContainer.getMapObjs();

		for (GraphBaseNotation notation : maps) {
			RectangularShape paintingShape = notation.getPaintingShape();
			g2d.setColor(notation.getBgColor());
			g2d.fill(paintingShape);

			g2d.setColor(notation.getFgColor());
			g2d.draw(paintingShape);
			int stringWidth = fontMetrics.stringWidth(notation.getName());

			int xx = (int) paintingShape.getCenterX() - stringWidth / 2;
			int yy = (int) paintingShape.getCenterY() + height / 2;
			g2d.drawString(notation.getName(), xx, yy);
		}

		List<GraphBaseNotation> compoundObjs = gContainer.getCompoundObjs();

		for (GraphBaseNotation notation : compoundObjs) {
			RectangularShape paintingShape = notation.getPaintingShape();
			g2d.setColor(notation.getBgColor());
			g2d.fill(paintingShape);

			g2d.setColor(notation.getFgColor());
			g2d.draw(paintingShape);
			int stringWidth = fontMetrics.stringWidth(notation.getName());
			g2d.drawString(notation.getName(), (int) paintingShape.getCenterX() - stringWidth / 2,
					(int) paintingShape.getCenterY() + height / 2);
		}
//		List<GraphBaseNotation> groups = gContainer.getGroups();
//		for (GraphBaseNotation notation : groups) {
//			RectangularShape paintingShape = notation.getPaintingShape();
//			g2d.setColor(notation.getBgColor());
//			g2d.fill(paintingShape);
//
//			g2d.setColor(notation.getFgColor());
//			g2d.draw(paintingShape);
//			int stringWidth = fontMetrics.stringWidth(notation.getName());
//			if (stringWidth > 0) {
//
//				g2d.drawString(notation.getName(), (int) paintingShape.getCenterX() - stringWidth / 2,
//						(int) paintingShape.getCenterY() + height / 2);
//			}
//		}

	}

	public void drawRelationship(Graphics2D g2d) {
		ArrowLineDrawerByGPT35 arrowLineDrawerByGPT35 = new ArrowLineDrawerByGPT35();
		// draw relationship
		List<GraphBaseRelation> proteinProteinRelations = gContainer.getProteinProteinRelations();
		List<GraphBaseRelation> proteinCompoundRelations = gContainer.getProteinCompoundRelations();

		List<GraphBaseRelation> tempList = new LinkedList<>();
		tempList.addAll(proteinProteinRelations);
		tempList.addAll(proteinCompoundRelations);

		for (GraphBaseRelation graphBaseRelation : tempList) {
			int entry1 = graphBaseRelation.getEntry1();
			int entry2 = graphBaseRelation.getEntry2();
			GraphBaseNotation node1 = id2GraphicNodeMap.get(entry1);
			GraphBaseNotation node2 = id2GraphicNodeMap.get(entry2);

			RectangularShape paintingShape1 = node1.getPaintingShape();
			RectangularShape paintingShape2 = node2.getPaintingShape();

			arrowLineDrawerByGPT35.setRects(paintingShape1, paintingShape2);

			boolean activate = true;
			String string4drawing = null;

			List<RelationSubtype> subTypes = graphBaseRelation.getSubTypes();
			for (RelationSubtype type : subTypes) {
				String relValue = type.getValue();
				String relName = type.getName();

				if ("inhibition".equals(relName)) {
					activate = false;
				} else if ("activation".equals(relName)) {
					activate = true;
				} else {
					string4drawing = relValue;
				}
			}
			arrowLineDrawerByGPT35.draw(g2d, activate, string4drawing);

		}
	}

	public void drawWrappedString(Graphics2D g, String text, int x, int y, int width, int lineHeight) {
		// 获取当前字体的渲染上下文
		FontRenderContext frc = g.getFontRenderContext();

		// 创建一个属性字符串，以便我们可以使用LineBreakMeasurer
		AttributedString attributedString = new AttributedString(text);

		// 设置字体属性
		attributedString.addAttribute(TextAttribute.FONT, g.getFont());

		// 创建LineBreakMeasurer对象，用于测量和换行文本
		LineBreakMeasurer measurer = new LineBreakMeasurer(attributedString.getIterator(), frc);

		// 开始绘制文本
		while (measurer.getPosition() < text.length()) {
			// 计算下一行的宽度，如果超过总宽度则换行
			int nextPos = measurer.nextOffset(width);
			String nextLayout = text.substring(measurer.getPosition(), nextPos);

			// 绘制这一行文本
			TextLayout layout = measurer.nextLayout(width);
			y += lineHeight; // 更新y坐标为下一行
			layout.draw(g, (float) x, (float) y);

			// 移动到下一行的起始位置
			measurer.setPosition(nextPos);
		}

	}

	public boolean isHasInputFile() {
		return hasInputFile;
	}

	public void setHasInputFile(File inputFile) {
		this.hasInputFile = true;
		parser.setInputFile(inputFile);
		parser.parse();
		id2GraphicNodeMap = gContainer.getID2GraphicNodeMap();
	}

}
