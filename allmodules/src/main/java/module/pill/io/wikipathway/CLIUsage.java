package module.pill.io.wikipathway;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;


public class CLIUsage {

	public static void main(String[] args) throws Exception {
		String filePath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\工作汇报\\基于富集分析的通路激活\\WntCompoentsCollections\\Wnt\\WikiPathway\\WP428.json"; // 更新为您的文件路径
		String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

		JsonRootBean jsonRootBean = JSON.parseObject(jsonContent, JsonRootBean.class);

		List<String> outputList = new ArrayList<>();

		Map<String, Entity> entitiesById = jsonRootBean.getEntitiesById();
		for (Entity values : entitiesById.values()) {
			String textContent = values.getTextContent();
			List<String> types = values.getType();
			int typeSize = types.size();

//			System.out.println("First type is: " + types.get(0) +);

			String firstType = types.get(0);
			if (!Objects.equals(values.getWpType(), "GeneProduct")) {
				continue;
			}
//			if (typeSize > 2 && Objects.equals(types.get(2), "WikiPathways")) {
//				continue;
//			}

			outputList.add(textContent);
			System.out.printf("First type is: %s and content is %s.\n", textContent, values.getId());
		}

		Pathway pathway = jsonRootBean.getPathway();
		List<String> contains = pathway.getContains();
		System.out.println("GeneProducts are: " + outputList.size());
		System.out.println("contains are: " + contains.size());
		
		Collections.sort(outputList);
//		Files.write(Paths.get("C:\\Users\\yudal\\Desktop\\tmp.txt"), outputList);

	}
}
