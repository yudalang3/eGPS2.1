package module.evolview.genebrowser;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import module.evolview.gfamily.work.beans.GeneMetaInfo;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import module.evolview.gfamily.work.beans.SequenceElementInfo;
import module.evolview.gfamily.work.beans.TrackMultipleDomainBean;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;
import module.evolview.gfamily.work.gui.browser.draw.GeneStructureInfo;
import module.evolview.gfamily.work.model.AlignmentGetSequence;
import module.gff3opr.model.FeatureList;
import module.gff3opr.model.GFF3Feature;
import module.gff3opr.model.YuGFF3Parser;

public class GBRequiredGeneData_mRNA extends RequiredGeneData {

	String gff3chuckFilePath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\DVL\\DVL1_longest_transcript.gff3";
	private FeatureList gff2FeatureList;

	@Override
	public GeneStructureInfo getGeneStructComputerStruct() throws IOException {
		if (gff2FeatureList == null) {

			gff2FeatureList = new YuGFF3Parser().parseGFF3File(gff3chuckFilePath);

			GeneMetaInfo geneStruct = new GeneMetaInfo();

			List<SequenceElementInfo> list = new ArrayList<SequenceElementInfo>();

			FeatureList geneFeatureList = gff2FeatureList.selectByType("gene");
			GFF3Feature featureOfGene = geneFeatureList.get(0);

			FeatureList mRNAFeatureLIst = gff2FeatureList.selectByType("mRNA");
			GFF3Feature mRNAFeature = mRNAFeatureLIst.get(0);

			FeatureList exonFeatureList = gff2FeatureList.selectByType("exon");

			int colorIndex = 0;
			for (GFF3Feature featureI : exonFeatureList) {
				SequenceElementInfo geneInfor = new SequenceElementInfo();
				geneInfor.setGeneName(featureI.getAttribute("Name"));
				int startPos = featureI.location().bioStart() - mRNAFeature.location().bioStart() + 1;
				geneInfor.setStartPos(startPos);
				int endPos = featureI.location().bioEnd() - mRNAFeature.location().bioStart() + 1;
				geneInfor.setEndPos(endPos);

				geneInfor.setColor(Color.green);
				list.add(geneInfor);

				colorIndex++;
			}

			SequenceElementInfo[] results = list.toArray(new SequenceElementInfo[] {});

			geneStruct.setSequenceElements(results);
			geneStruct.setGeneLength(mRNAFeature.location().length());

			geneStruct.setGeneName("mRNA");
			geneStruct.setSequenceElementName("Exon");

			geneStructureInfo = new GeneStructureInfo(geneStruct);

		}
		return geneStructureInfo;
	}
	
	@Override
	public Optional<TrackMultipleDomainBean> getKeyDomains() {
		
		FeatureList mRNAFeatureLIst = gff2FeatureList.selectByType("mRNA");
		GFF3Feature mRNAFeature = mRNAFeatureLIst.get(0);
		
		FeatureList selectByCDS = gff2FeatureList.selectByType("CDS");
		DrawingPropertySequenceModel moduleItem = new DrawingPropertySequenceModel();
		List<DrawingPropertyElement> genomeElementModels = new ArrayList<>();

		for (GFF3Feature feature : selectByCDS) {
			DrawingPropertyElement genomeElementModel = new DrawingPropertyElement();
			String id = feature.getAttribute("ID");
			
			genomeElementModel.setName(id);
			genomeElementModel.setGene("Gene");
			
			int startPos = feature.location().bioStart() - mRNAFeature.location().bioStart() + 1;
			int endPos = feature.location().bioEnd() - mRNAFeature.location().bioStart() + 1;
			
			genomeElementModel.setStartPosition(startPos);
			genomeElementModel.setEndPositoin(endPos);
			genomeElementModels.add(genomeElementModel);
		}
		moduleItem.setHeader("Header I am");
		moduleItem.setOrigianlSeqElementModels(genomeElementModels);
		moduleItem.setSequenceElementModelName("CDS");
		
		TrackMultipleDomainBean ret = new TrackMultipleDomainBean();
		
		ret.setMultiTracksOfSeqElements(Arrays.asList(moduleItem));
		
		return Optional.ofNullable(ret);
		
	}
	
	@Override
	public Optional<AlignmentGetSequence> getAlignmentSequence() {
		return Optional.empty();
	}
}
