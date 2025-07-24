package module.evolview.genebrowser;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import fasta.io.FastaReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import egps2.EGPSProperties;
import tsv.io.TsvNameTransversionInfo;
import module.evolview.gfamily.work.beans.GeneMetaInfo;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import module.evolview.gfamily.work.beans.SequenceElementInfo;
import module.evolview.gfamily.work.beans.TrackMultipleDomainBean;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;
import module.evolview.gfamily.work.model.AlignmentGetSequence;
import graphic.engine.colors.EGPSColors;
import msaoperator.alignment.sequence.Sequence;
import rest.ensembl.ensembrest.OverlapTransBeanParser;
import rest.ensembl.ensembrest.OverlapTransElementBean;

public class GBRequiredGeneData_Prot extends RequiredGeneData {
	
	
	private static final Logger logger = LoggerFactory.getLogger(GBRequiredGeneData_Prot.class);

	public String mainType = "Pfam";
	public String protFastaFile = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\DVL\\protein\\ENSP00000368166.pro.fa";
	public String protFeatureJsonFile = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\DVL\\protein\\ENSP00000368166.features.json";
	public TsvNameTransversionInfo sequenceNameTranlationBean;
	
	private Map<String, List<OverlapTransElementBean>> type2listBeanMap;
	private String protName;
	private String protSequence;

	
	public GBRequiredGeneData_Prot() {
		super();
	}

	@Override
	public GeneStructureInfo getGeneStructComputerStruct() throws IOException {
		if (geneStructureInfo == null) {

			File jsonFile = new File(protFeatureJsonFile);
			// 获取fasta蛋白质序列
			LinkedHashMap<String, String> fastaFile = FastaReader.readFastaDNASequence(new File(protFastaFile));
			
			protName = StringUtils.substringBefore(jsonFile.getName(), ".");
			protSequence = fastaFile.get(protName);
			
			if (sequenceNameTranlationBean != null) {
				Map<String, String> transferHash = sequenceNameTranlationBean.getTransferHash();
				protName = transferHash.get(protName);
				if (protName == null) {
					throw new InputMismatchException("Sorry, your input tsv do not have the protein name.");
				}
			}
			
			Objects.requireNonNull(protSequence, "Please check your input fasta file, it does not contains ".concat(protName));

			OverlapTransBeanParser overlapTransBeanParser = new OverlapTransBeanParser();
			type2listBeanMap = overlapTransBeanParser.parse(jsonFile);


			GeneMetaInfo geneStruct = new GeneMetaInfo();

			List<SequenceElementInfo> list = new ArrayList<SequenceElementInfo>();
			SequenceElementInfo sequenceElementInfo = new SequenceElementInfo();
			sequenceElementInfo.setGeneName("Protein sequence");
			sequenceElementInfo.setStartPos(0);
			sequenceElementInfo.setEndPos(protSequence.length());
			sequenceElementInfo.setColor(EGPSProperties.dragFrameColor);
//			sequenceElementInfo.setColor(Color.decode("#CBECFF"));
			list.add(sequenceElementInfo);

			SequenceElementInfo[] results = list.toArray(new SequenceElementInfo[] {});

			geneStruct.setSequenceElements(results);
			geneStruct.setGeneLength(protSequence.length());

			geneStruct.setGeneName(protName);
			geneStruct.setSequenceElementName("Frame illustrator");

			geneStructureInfo = new GeneStructureInfo(geneStruct);

		}
		return geneStructureInfo;
	}

	@Override
	public Optional<TrackMultipleDomainBean> getKeyDomains() {

		List<DrawingPropertySequenceModel> seqModels = Lists.newArrayList();

		Color[] predefinedColors = EGPSColors.getPredinedCellChatColors();
		
		Set<Entry<String, List<OverlapTransElementBean>>> entrySet = type2listBeanMap.entrySet();
		//排个序先
		ArrayList<Entry<String, List<OverlapTransElementBean>>> newArrayList = Lists.newArrayList(entrySet);
		newArrayList.sort(new Comparator<Entry<String, List<OverlapTransElementBean>>>() {
			@Override
			public int compare(Entry<String, List<OverlapTransElementBean>> o1,
					Entry<String, List<OverlapTransElementBean>> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		
		int index = 0;
		for (Entry<String, List<OverlapTransElementBean>> entry : newArrayList) {
			
			DrawingPropertySequenceModel sequenceModel = new DrawingPropertySequenceModel();
			List<DrawingPropertyElement> listOfSeqModelElements = new ArrayList<>();

			List<OverlapTransElementBean> value = entry.getValue();
			
			int currentColorElementIndex = 1;//没错第一个元素我们不要，红色太显眼了
			
			Map<String, Color> element2colMap = new HashMap<>();
			for (OverlapTransElementBean bean : value) {
				
				DrawingPropertyElement genomeElementModel = new DrawingPropertyElement();

				String name = bean.getDescription();
				if (Strings.isEmpty(name)) {
					name = bean.getHseqname();
				}
				
				if (Strings.isEmpty(name)) {
					name = bean.getId();
				}
				
				genomeElementModel.setName(name);
				genomeElementModel.setGene(bean.getId());

				int startPos = Ints.tryParse(bean.getStart());
				int endPos = Ints.tryParse(bean.getEnd());

				genomeElementModel.setStartPosition(startPos);
				genomeElementModel.setEndPositoin(endPos);
				listOfSeqModelElements.add(genomeElementModel);

				sequenceModel.setHeader("Header I am");
				sequenceModel.setOrigianlSeqElementModels(listOfSeqModelElements);
				sequenceModel.setSequenceElementModelName(entry.getKey());
				
				
				//设置颜色了
				Color color = element2colMap.get(name);
				if ("Pfam".equalsIgnoreCase(entry.getKey())) {
					logger.trace("name is {} color is {}", name , color);
				}
				
				if (color == null) {
					int colorObtainIndex = currentColorElementIndex;
					if (colorObtainIndex >= (predefinedColors.length - 1) ) {
						colorObtainIndex = colorObtainIndex % (predefinedColors.length);
					}
					color = predefinedColors[colorObtainIndex];
					element2colMap.put(name, color);
					currentColorElementIndex ++;
				}
				
				genomeElementModel.setColor(color);
				
			}
			
			seqModels.add(sequenceModel);
			index ++;
		}

		TrackMultipleDomainBean ret = new TrackMultipleDomainBean();
		ret.setMultiTracksOfSeqElements(seqModels);

		return Optional.ofNullable(ret);

	}

	@Override
	public Optional<AlignmentGetSequence> getAlignmentSequence() {
		
		if (sequenceInforObtainer == null) {
			sequenceInforObtainer = new AlignmentGetSequence();
			try {
				sequenceInforObtainer.setSequenceWithOnlyOneEntry(new Sequence(protName, protSequence));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return Optional.ofNullable(sequenceInforObtainer);
	}
}
