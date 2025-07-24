package primary.struct.display.data;

import blast.parse.BlastHspRecord;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import fasta.io.FastaReader;
import graphic.engine.colors.EGPSColors;
import org.apache.commons.lang3.tuple.Pair;
import pfam.parse.PfamScanRecord;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PairwiseHomologyData {
    private String queryFastaPath;
    private Map<String, String> queryFastaMap;
    private String subjectFastaPath;
    private Map<String, String> subjectFastaMap;

    private String blastFmt6ResultPath;
    private List<BlastHspRecord> blastHspRecordList;
    private String diamondFmt6ResultPath;
    private List<BlastHspRecord> diamondBlastHspRecordList;

    private String pfamScanResultQueryPath;
    Map<String, List<PfamScanRecord>> querySeqId2PfamDBEntriesMap;
    private int currentDisplayQueryIndex = 0;
    private String pfamScanResultSubjectPath;
    Map<String, List<PfamScanRecord>> subjectSeqId2PfamDBEntriesMap;

    private String subjectName = "PRICKLE1";
    private String queryName1 = "XP_645632.1";
    private String queryName2 = "XP_646057.1";

    private EGPSColors colorAllocator = new EGPSColors();


    public List<BlastHspRecord> getDiamondBlastHspRecordList() {
        return diamondBlastHspRecordList;
    }

    public void setDiamondBlastHspRecordList(List<BlastHspRecord> diamondBlastHspRecordList) {
        this.diamondBlastHspRecordList = diamondBlastHspRecordList;
    }

    public String getDiamondFmt6ResultPath() {
        return diamondFmt6ResultPath;
    }

    public void setDiamondFmt6ResultPath(String diamondFmt6ResultPath) {
        this.diamondFmt6ResultPath = diamondFmt6ResultPath;
    }

    public Pair<List<BlastHspRecord>,List<BlastHspRecord>> getConnectingLines4blastp()  {
        List<BlastHspRecord> listOfConnectingLinesFirst2Second = Lists.newArrayList();
        List<BlastHspRecord> listOfConnectingLinesSecond2Third = Lists.newArrayList();


        for (BlastHspRecord blastHspRecord : blastHspRecordList){
            String qseqid = blastHspRecord.getQseqid();
            String sseqid = blastHspRecord.getSseqid();
            if (!Objects.equals(sseqid, subjectName)){
                continue;
            }
            if (Objects.equals(qseqid, queryName1)){
                listOfConnectingLinesFirst2Second.add(blastHspRecord);
            }
            if (Objects.equals(qseqid, queryName2)){
                listOfConnectingLinesSecond2Third.add(blastHspRecord);
            }
        }
        return Pair.of(listOfConnectingLinesFirst2Second, listOfConnectingLinesSecond2Third);

    }
    public Pair<List<BlastHspRecord>,List<BlastHspRecord>> getConnectingLines4diamond()  {
        if (diamondBlastHspRecordList == null){
            throw new IllegalArgumentException("Please enter diamond blast result path first.");
        }
        List<BlastHspRecord> listOfConnectingLinesFirst2Second = Lists.newArrayList();
        List<BlastHspRecord> listOfConnectingLinesSecond2Third = Lists.newArrayList();


        for (BlastHspRecord blastHspRecord : diamondBlastHspRecordList){
            String qseqid = blastHspRecord.getQseqid();
            String sseqid = blastHspRecord.getSseqid();
            if (!Objects.equals(sseqid, subjectName)){
                continue;
            }
            if (Objects.equals(qseqid, queryName1)){
                listOfConnectingLinesFirst2Second.add(blastHspRecord);
            }
            if (Objects.equals(qseqid, queryName2)){
                listOfConnectingLinesSecond2Third.add(blastHspRecord);
            }
        }
        return Pair.of(listOfConnectingLinesFirst2Second, listOfConnectingLinesSecond2Third);

    }
    public GeneDataV2 initialize() throws IOException {
        queryFastaMap = FastaReader.readFastaSequence(queryFastaPath);
        subjectFastaMap = FastaReader.readFastaSequence(subjectFastaPath);

        querySeqId2PfamDBEntriesMap = PfamScanRecord.parseHmmerScanOut(pfamScanResultQueryPath);
        subjectSeqId2PfamDBEntriesMap = PfamScanRecord.parseHmmerScanOut(pfamScanResultSubjectPath);

        if (!Strings.isNullOrEmpty(blastFmt6ResultPath)){
            blastHspRecordList = BlastHspRecord.parseFile(blastFmt6ResultPath);
        }
        if (!Strings.isNullOrEmpty(diamondFmt6ResultPath)){
            diamondBlastHspRecordList = BlastHspRecord.parseFile(diamondFmt6ResultPath);
        }

        String subjectSeq = subjectFastaMap.get(subjectName);

        GeneDataV2 geneDataV2 = new GeneDataV2();
        {
            String seqId = queryName1;
            String s = queryFastaMap.get(seqId);
            GeneV2 geneV2 = getGeneV2(seqId, s.length(), querySeqId2PfamDBEntriesMap);
            geneDataV2.getGenes().put(seqId, geneV2);
        }
        {
            String seqId = subjectName;
            int seqLength = subjectSeq.length();
            GeneV2 geneV2 = getGeneV2(seqId, seqLength, subjectSeqId2PfamDBEntriesMap);
            geneDataV2.getGenes().put(seqId, geneV2);
        }

        {
            String seqId = queryName2;
            String s = queryFastaMap.get(seqId);
            if (s != null) {
                GeneV2 geneV2 = getGeneV2(seqId, s.length(), querySeqId2PfamDBEntriesMap);
                geneDataV2.getGenes().put(seqId, geneV2);
            }
        }

        return geneDataV2;
    }

    private GeneV2 getGeneV2(String seqId, int seqLength, Map<String, List<PfamScanRecord>> map) {
        GeneV2 geneV2 = new GeneV2();
        geneV2.setGeneName(seqId);
        geneV2.setLength(seqLength);

        List<PfamScanRecord> pfamDBEntries = map.get(seqId);
        if (pfamDBEntries != null) {
            for (PfamScanRecord pfamDBEntry : pfamDBEntries) {
                DomainV2 domainV2 = new DomainV2();
                String hmmName = pfamDBEntry.getHmmName();
                domainV2.setName(hmmName);
                domainV2.setStart(pfamDBEntry.getAlignmentStart());
                domainV2.setEnd(pfamDBEntry.getAlignmentEnd());
                Color color = colorAllocator.allocateColor(hmmName);
                domainV2.setColor(color);
                geneV2.getDomains().add(domainV2);
            }
        }
        return geneV2;
    }

    public String getQueryName2() {
        return queryName2;
    }

    public void setQueryName2(String queryName2) {
        this.queryName2 = queryName2;
    }

    public String getQueryName1() {
        return queryName1;
    }

    public void setQueryName1(String queryName1) {
        this.queryName1 = queryName1;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPfamScanResultSubjectPath() {
        return pfamScanResultSubjectPath;
    }

    public void setPfamScanResultSubjectPath(String pfamScanResultSubjectPath) {
        this.pfamScanResultSubjectPath = pfamScanResultSubjectPath;
    }

    public String getPfamScanResultQueryPath() {
        return pfamScanResultQueryPath;
    }

    public void setPfamScanResultQueryPath(String pfamScanResultQueryPath) {
        this.pfamScanResultQueryPath = pfamScanResultQueryPath;
    }

    public String getBlastFmt6ResultPath() {
        return blastFmt6ResultPath;
    }

    public void setBlastFmt6ResultPath(String blastFmt6ResultPath) {
        this.blastFmt6ResultPath = blastFmt6ResultPath;
    }

    public String getSubjectFastaPath() {
        return subjectFastaPath;
    }

    public void setSubjectFastaPath(String subjectFastaPath) {
        this.subjectFastaPath = subjectFastaPath;
    }

    public String getQueryFastaPath() {
        return queryFastaPath;
    }

    public void setQueryFastaPath(String queryFastaPath) {
        this.queryFastaPath = queryFastaPath;
    }
}
