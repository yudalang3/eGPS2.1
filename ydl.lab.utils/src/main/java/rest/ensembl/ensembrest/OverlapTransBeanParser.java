package rest.ensembl.ensembrest;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OverlapTransBeanParser {

	private static final Logger logger = LoggerFactory.getLogger(OverlapTransBeanParser.class);

	public Map<String, List<OverlapTransElementBean>> parse(String path) throws IOException {
		return parse(new File(path));
	}
	public Map<String, List<OverlapTransElementBean>> parse(File path) throws IOException {

		String cont = FileUtils.readFileToString(path, StandardCharsets.US_ASCII);
		List<OverlapTransElementBean> arrays = JSONArray.parseArray(cont, OverlapTransElementBean.class);

		ImmutableListMultimap<String, OverlapTransElementBean> type2beanMap = Multimaps.index(arrays,
				new Function<OverlapTransElementBean, String>() {
					@Override
					public String apply(OverlapTransElementBean input) {
						return input.getType();
					}
				});

		Map<String, List<OverlapTransElementBean>> asMap2 = Multimaps.asMap(type2beanMap);

		for (Entry<String, List<OverlapTransElementBean>> entry : asMap2.entrySet()) {
			logger.trace("{} , size {}", entry.getKey(), entry.getValue().size());
		}

		return asMap2;
	}

	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\DVL\\protein\\rest.all.json";
		new OverlapTransBeanParser().parse(path);
	}

}
