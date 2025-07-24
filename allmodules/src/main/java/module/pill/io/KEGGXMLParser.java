package module.pill.io;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

// https://www.kegg.jp/kegg/xml/docs/
// 这其实可以当一个脚本型的文件来用，每次更具自己的需求来改写代码
public class KEGGXMLParser {

	private File inputFile = new File(
			"C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\工作汇报\\基于富集分析的通路激活\\KEGG\\Wnt\\hsa04310.xml");

	private Set<String> strings2see = new HashSet<>();
	private List<String> outputList = Lists.newArrayList();

	public KEGGXMLParser() {
	}

	public static void main(String[] args) throws Exception {
		KEGGXMLParser parser = new KEGGXMLParser();
		parser.parse();
	}

	public void parse() throws Exception {

		// 使用SAXReader读取XML文件
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputFile);

		// 获取根节点
		Element pathwayElement = document.getRootElement();

		// 遍历所有的<entry>标签
		for (Element entry : pathwayElement.elements("entry")) {
			parseOneEntry(entry);

		}
//		System.out.println("strings2see is:");
//		System.out.println(strings2see);

		outputList.add(0, "KEGG_ID\tGraphicNames\tLink");
		FileUtils.writeLines(new File(
				"C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\工作汇报\\基于富集分析的通路激活\\KEGG\\Wnt\\extracted_genes.tsv"),
				outputList);
	}

	private void parseOneEntry(Element entry) {
		String id = entry.attributeValue("id");
		String name = entry.attributeValue("name");
		String type = entry.attributeValue("type");
		String link = entry.attributeValue("link");

		String graphicsName = null;

		// 打印<graphics>标签内的信息
		Element graphicsElement = entry.element("graphics");
		if (graphicsElement != null) {
			graphicsName = graphicsElement.attributeValue("name");
		}

		strings2see.add(type);

		if (Objects.equal(type, "gene")) {
			outputList.add(name + "\t" + graphicsName + "\t" + link);
		} else if (Objects.equal(type, "ortholog")) {
			outputList.add(name + "\t" + graphicsName + "\t" + link);
		} else {

		}

	}

	protected boolean shouldKept(String id, String name, String type, String link) {
//		if (targetSets.contains(name)) {
//			targetSets.remove(name);
//			return true;
//		}
		return false;
	}

}
