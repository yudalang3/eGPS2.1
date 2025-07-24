package module.pill.io;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * https://www.kegg.jp/kegg/xml/docs/ https://www.kegg.jp/kegg/xml/
 * 
 * 关键信息 代谢通路图的KGML文件包含两种类型的图形对象模式，即盒子(酶)如何通过“关系”连接，以及圆圈(化合物)如何通过“反应”连接。 The KGML
 * files for metabolic pathway maps contain two types of graph object patterns,
 * how boxes (enzymes) are linked by "relations" and how circles (chemical
 * compounds) are linked by "reactions". 盒子和圆圈就是图形上的那些标记。
 * 
 * 非代谢途径图的KGML文件仅包含盒(蛋白质)如何通过“关系”连接的方面。
 * 
 * The KGML files for non-metabolic pathway maps contain only the aspect of how
 * boxes (proteins) are linked by "relations".
 * 
 * 上面就是在信号通路
 * 
 */
public class KEGGXML2JavaShapeParser {

	private File inputFile = new File(
			"C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\工作汇报\\方法原理\\KEGG\\mTOR\\hsa04150.xml");
//"C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\工作汇报\\方法原理\\KEGG\\Hippo\\hsa04390.xml");


	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public void parse() {

		Set<String> forTempCount = new HashSet<>();

		int totalEntries = 0;

		try {

			// 使用SAXReader读取XML文件
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputFile);

			// 获取根节点
			Element pathwayElement = document.getRootElement();

			// 遍历所有的<entry>标签
			for (Element entry : pathwayElement.elements("entry")) {

				// [gene, map, compound, group]
				// entry.attributeValue("type");
				totalEntries++;
				// 打印<graphics>标签内的信息
				Element graphicsElement = entry.element("graphics");
				if (graphicsElement != null) {
					parseGraphicElment(entry, graphicsElement);

				}

//				String string2see = entry.attributeValue("type");
//				forTempCount.add(string2see);
			}
			// 遍历所有的<relation>标签
			for (Element entry : pathwayElement.elements("relation")) {

				parseRelationElment(entry);

				String string2see = entry.attributeValue("type");
				forTempCount.add(string2see);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Get total enetry numbers are: " + totalEntries);

		System.out.println(forTempCount);
	}

	protected void parseRelationElment(Element entry) {
		List<Element> elements = entry.elements("subtype");
		for (Element string : elements) {

		}

	}

	protected void parseGraphicElment(Element entry, Element graphicsElement) {
		String id = entry.attributeValue("id");
		String name = entry.attributeValue("name");
		String type = entry.attributeValue("type");
		String link = entry.attributeValue("link");

		String graphicsName = graphicsElement.attributeValue("name");
		String fgcolor = graphicsElement.attributeValue("fgcolor");
		String bgcolor = graphicsElement.attributeValue("bgcolor");
		String typeGraphics = graphicsElement.attributeValue("type");
		int x = Integer.parseInt(graphicsElement.attributeValue("x"));
		int y = Integer.parseInt(graphicsElement.attributeValue("y"));
		int width = Integer.parseInt(graphicsElement.attributeValue("width"));
		int height = Integer.parseInt(graphicsElement.attributeValue("height"));

		System.out.println("ID: " + id);
		System.out.println("Name: " + name);
//		System.out.println("Type: " + type);
//		System.out.println("Link: " + link);
		System.out.println("Graphics Name: " + graphicsName);
		System.out.println("Foreground Color: " + fgcolor);
		System.out.println("Background Color: " + bgcolor);
		System.out.println("Type: " + typeGraphics);
		System.out.println("Position (x, y): (" + x + ", " + y + ")");
		System.out.println("Size (width, height): (" + width + ", " + height + ")");

		System.out.println("-----------------------------");
	}


}
