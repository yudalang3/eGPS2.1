package module.gff3opr.myscript;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import egps2.frame.gui.comp.search.SearchItem;
import module.gff3opr.model.FeatureList;
import module.gff3opr.model.GFF3Feature;
import module.gff3opr.model.YuFeatureFormatter;
import module.gff3opr.model.YuGFF3Parser;

/**
 * 
 * <h1>目的</h1>
 * <p ALIGN=center>
 *  
 * 这个目的是 Get properties of interest genes
 * 也就是说输入一定的条件，得到自己感兴趣的目标基因，接着输出自己感兴趣的一些属性
 * 这里输入的文件是 gff3文件
 *  </p>
 *  
 * <h1>输入/输出</h1>
 * 
 * <p>
 * 输入一个gff3格式的文件，输出一个满足目标条件的文本文件。
 * </p>
 * 
 * <h1>使用方法</h1>
 * 
 * <blockquote>
 * GetPropertiesOfInterestGenes worker = new GetPropertiesOfInterestGenes();
 * worker.setGff3file(inputPath);
 * worker.setOutputFile(outputPath);
 * worker.setSearchKeyWords(searchKey);
 * 
 * worker.doIt();
 * </blockquote>
 * 
 * <h1>注意点</h1>
 * <ol>
 * <li>
 * 在调用主要方法 <code>doIt()</code>时不要忘记设置一些属性
 * </li>
 * </ol>
 * 
 * @implSpec
 * 直接通过遍历整个文件所有的Features完成的。
 * 
 * @author yudal
 *
 */
public class GetPropertiesOfInterestGenes {

	private static final Logger logger = LoggerFactory.getLogger(GetPropertiesOfInterestGenes.class);

	private String gff3file = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\Human_db_emblFTP\\Homo_sapiens.GRCh38.110.gff3";
	private String outputFile = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\DVL\\DVL.gene.info.tsv";

	private MapSplitter attrMapSplitter = Splitter.on(';').withKeyValueSeparator('=');
	private Map<String, String> map4header = null;
	private Joiner tabJoiner = Joiner.on('\t');

	private List<String> outputList = new ArrayList<>();
	private List<String> warnings = new ArrayList<>();

	private SearchItem searchKeyWords;

	public void doIt() throws IOException {

		logger.debug("Start to search...");
		YuGFF3Parser yuGFF3Parser = new YuGFF3Parser();
		FeatureList geneFeatures = yuGFF3Parser.searchFeaturesWithKeywordsByPartialMatch(searchKeyWords,gff3file);
		FeatureList allFeaturesInGff3 = yuGFF3Parser.getFeatureList();

		/**
		 * 这里要对 type字段进行过滤，有可能是 gene/ mRNA / transcript / ncRNA_gene / pseudogene / lnc_RNA
		 * gene|32, lnc_RNA|5, ncRNA_gene|3, pseudogene|1, transcript|14, mRNA|55
		 */
		HashSet<String> maskedTypes = Sets.newHashSet("lnc_RNA", "transcript", "mRNA");
		FeatureList list = new FeatureList();
		for (GFF3Feature featureI : geneFeatures) {
			if (maskedTypes.contains(featureI.type())) {
				continue;
			}
			
			if (!Objects.equals("gene", featureI.type())) {
				warnings.add(featureI.toString());
				continue;
			}
			
			list.add(featureI);
			
		}
		geneFeatures = list;
		
		if (geneFeatures.isEmpty()) {
			logger.info("====> Attention here <==== : No features found in the gff3 file.");
			return;
		}
		
		YuFeatureFormatter featureFormatter = new YuFeatureFormatter();

		ArrayList<String> outputList = Lists.newArrayList();
		outputList.add(featureFormatter.GENE_WITH_LONGEST_CDS_HEADER);
		
		for (GFF3Feature featureI : geneFeatures) {
			String ID_of_gene = featureI.getAttribute("ID");
			FeatureList features4oneGene = allFeaturesInGff3.selectFeatureList4gene(ID_of_gene);
			GFF3Feature cdsFeature = features4oneGene.geneWithLongestCDS();
			
			Objects.requireNonNull(cdsFeature);
			
			if (((GFF3Feature) featureI).source() == null) {
				System.out.println();
			}
			String str = featureFormatter.formatOfGeneFeatureWithLongestCDS((GFF3Feature) featureI, cdsFeature);
			
			outputList.add(str);
			
		}
		
		FileUtils.forceMkdirParent(new File(outputFile));
		Path path = Paths.get(outputFile);
		Files.write(path, outputList, StandardCharsets.US_ASCII);
		Files.write(Paths.get(outputFile.concat("Warnings.txt")), warnings, StandardCharsets.US_ASCII);

		
		logger.debug("Finished...");
	}




	private void lineParse(String type, String attrStr) {
		Map<String, String> split = attrMapSplitter.split(attrStr);

		String join = tabJoiner.join(split.values());
		outputList.add(join);

		if (map4header == null) {
			map4header = split;
		}

	}

	public String getGff3file() {
		return gff3file;
	}

	public void setGff3file(String gff3file) {
		this.gff3file = gff3file;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}




	public SearchItem getSearchKeyWords() {
		return searchKeyWords;
	}




	public void setSearchKeyWords(SearchItem searchKeyWords) {
		this.searchKeyWords = searchKeyWords;
	}


}
