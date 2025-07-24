package fasta.io;

import blast.parse.BlastHspRecord;
import com.google.common.collect.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pfam.parse.PfamScanRecord;
import tsv.io.KitTable;
import tsv.io.TSVReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Script_homoGene_finder_blastpWithHmmer {

    private static final Logger log = LoggerFactory.getLogger(Script_homoGene_finder_blastpWithHmmer.class);
    private List<String> speciesOrderList;

    static class Criterion {
        Set<String> clans = Sets.newHashSet();
        Set<String> domains = Sets.newHashSet();

        int getNumberOfCriterion() {
            return clans.size() + domains.size();
        }

        @Override
        public String toString() {
            return clans.toString() + "||" + domains.toString();
        }

        public boolean judgeMatchedDomain(Set<String> candidateClans, Set<String> candidateDomains) {
            int numberOfCriterion = getNumberOfCriterion();
            int matched = 0;
            for (String clan : candidateClans) {
                if (this.clans.contains(clan)) {
                    matched++;
                }
            }
            for (String domain : candidateDomains) {
                if (this.domains.contains(domain)) {
                    matched++;
                }
            }
            if (numberOfCriterion == 3) {
                return matched >= 2;
            } else if (numberOfCriterion == 0) {
                int candidateSize = candidateClans.size() + candidateDomains.size();
                //这个逻辑就是 标准的都鉴定不到结构域，那么现在就更加不可能鉴定到了
                if (candidateSize > 0 ){
                    return false;
                }else {
                    return true;
                }
            } else if (numberOfCriterion > 3) {
                return matched >= 3;
            } else {
                // 1 and 2
                return matched >= numberOfCriterion;
            }
        }
    }

    private final Map<String, Criterion> name2criterion = Maps.newLinkedHashMap();
    private final Table<String, String, Integer> table = HashBasedTable.create();

    private final byte CLAN_SIZE_THRESHOLD = 10;
    private double CANDIDATES_COVERAGE_THRESHOLD = 0.5;
    private String output_summary_table_path = "";

    final String fileNameOfCandidates = "1.blastp.result.tsv";
    final String fileNameCandidatesDomains = "3.wnt_candidates_domain.tbl";
    final String fileNameOfDiamondCandidates = "1.diamond.out.more.sensitive.tsv";
    final String fastaOfAllSequence = "fa.gz";

    private static boolean debug = false;
    public static void main(String[] args) throws IOException {
        String pathOfDir = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\SpeciesDataSet1_oneSpeciesOnePhylum\\processed\\run_on_20250421_novelPipeline";
        String humanWntCompDomainFile = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Wnt_QuerySequences\\compoentSet4\\set4\\pfam_curated_seq_domains.sorted.mandatoryDomain.reduced.tsv";
        String speciesOrderPath = null;
        String outputDirPath = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\SpeciesDataSet1_oneSpeciesOnePhylum\\processed\\20250421_final_wnt_pathway_matrix_and_corr2bodyplan\\oneCandidate2geneCount";
//        speciesOrderPath = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Holzem et al., 2024_used_to_judge_homo\\process\\passing_by_similarity\\dir_to_run_mine_blastp_EDA_no_clan_overlap\\summary_wnt_comp_blastp.tsv";
        if (args.length > 0) {
            pathOfDir = args[0];
            humanWntCompDomainFile = args[1];
            speciesOrderPath = args[2];
            outputDirPath = args[3];
        }
        Script_homoGene_finder_blastpWithHmmer scriptHomoGeneFinderBlastpWithHmmer = new Script_homoGene_finder_blastpWithHmmer();
        scriptHomoGeneFinderBlastpWithHmmer.configSequenceDom(humanWntCompDomainFile);

        List<String> speciesOrderList = Lists.newArrayList();

        if (StringUtils.isNotEmpty(speciesOrderPath)){
            TSVReader.readTsvTextFile(speciesOrderPath).getContents().forEach(strings -> {
                String seqName = strings.getFirst();
                speciesOrderList.add(seqName);
            });
            scriptHomoGeneFinderBlastpWithHmmer.configSpeciesOrder(speciesOrderList);
        }else {
            // If the species order is not provided, use the dir names instead.
        }

        String parentPath = outputDirPath;
        if (!debug){
            {
                scriptHomoGeneFinderBlastpWithHmmer.output_summary_table_path = parentPath + "/summary_wnt_comp_my_criterion_cov50.tsv";
                scriptHomoGeneFinderBlastpWithHmmer.CANDIDATES_COVERAGE_THRESHOLD = 0.5;
                scriptHomoGeneFinderBlastpWithHmmer.runIt(pathOfDir);
            }
            {
                scriptHomoGeneFinderBlastpWithHmmer.output_summary_table_path = parentPath + "/summary_wnt_comp_my_criterion_cov40.tsv";
                scriptHomoGeneFinderBlastpWithHmmer.CANDIDATES_COVERAGE_THRESHOLD = 0.4;
                scriptHomoGeneFinderBlastpWithHmmer.runIt(pathOfDir);
            }
        }

        {
            scriptHomoGeneFinderBlastpWithHmmer.output_summary_table_path = parentPath + "/summary_wnt_comp_my_criterion_cov30.tsv";
            scriptHomoGeneFinderBlastpWithHmmer.CANDIDATES_COVERAGE_THRESHOLD = 0.3;
            scriptHomoGeneFinderBlastpWithHmmer.runIt(pathOfDir);
        }
    }

    private void configSpeciesOrder(List<String> speciesOrderList) {
        this.speciesOrderList = speciesOrderList;
    }

    private void configSequenceDom(String sequenceDomain) throws IOException {
        KitTable kitTable = TSVReader.readTsvTextFile(sequenceDomain);
        String NO_CLAN_STR = "No_clan";
        kitTable.getContents().forEach(strings -> {
            String seqName = strings.get(0);
            String lengthStr = strings.get(1);
            String clan = strings.get(6);
            String domainName = strings.get(2);

            Criterion criterion = name2criterion.computeIfAbsent(seqName, k -> new Criterion());
            int length = Integer.parseInt(lengthStr);
            if (length > 0){
                if (Objects.equals(clan, NO_CLAN_STR)) {
                    criterion.domains.add(domainName);
                } else {
                    String clanSizeStr = strings.get(7);
                    int clanSize = Integer.parseInt(clanSizeStr);
                    // 这很重要，必须取其一
                    if (clanSize > CLAN_SIZE_THRESHOLD) {
                        criterion.domains.add(domainName);
                    } else {
                        criterion.clans.add(clan);
                    }
                }
            }else {

            }
        });
    }

    private void runIt(String pathOfDir) throws IOException {

        Map<Path, String> speciesName2fastaPath = Maps.newLinkedHashMap();
        List<Path> paths = Files.list(Path.of(pathOfDir)).toList();
        for (Path path : paths){
            if (!Files.isDirectory(path)) {
                continue;
            }
            List<Path> list = Files.list(path).toList();
            boolean hasFastaFile = false;
            for (Path path1 : list){
                if (path1.getFileName().toString().endsWith(fastaOfAllSequence)){
                    speciesName2fastaPath.put(path, path1.toString());
                    hasFastaFile = true;
                    break;
                }
            }

            if (!hasFastaFile){
                log.warn("The path {} is not meet command (don not hava the *fa.gz file), skip it.", path.getFileName().toString());
            }
        }

        if (this.speciesOrderList == null){
            this.speciesOrderList = Lists.newArrayList();
            for (Map.Entry<Path, String> entry : speciesName2fastaPath.entrySet()) {
                speciesOrderList.add(entry.getKey().getFileName().toString());
            }
        }

        log.info("Number of species to handle is {}.", speciesName2fastaPath.size());

        for (Map.Entry<Path, String> entry : speciesName2fastaPath.entrySet()) {
            Path dir = entry.getKey();
            String fastaGzFile = entry.getValue();

            Path path1 = Path.of(dir.toString(), fileNameOfCandidates);
            Path path2 = Paths.get(dir.toString(), fileNameCandidatesDomains);
            Path path3 = Paths.get(dir.toString(), fileNameOfDiamondCandidates);

            if (!Files.exists(path3)) {
                log.warn("The file {} is not exist, skip it.", path3);
                continue;
            }

            List<BlastHspRecord> blastHspRecords = BlastHspRecord.parseFile(path1.toString());
            List<BlastHspRecord> diamondBlastHspRecords = BlastHspRecord.parseFile(path3.toString());

            Map<String, List<PfamScanRecord>> name2PfamScanRecords = Maps.newHashMap();
            for (String line : Files.readAllLines(path2)) {
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }
                PfamScanRecord pfamScanRecord = new PfamScanRecord(line);
                List<PfamScanRecord> pfamScanRecords = name2PfamScanRecords.computeIfAbsent(pfamScanRecord.getSeqId(), k -> Lists.newArrayList());
                pfamScanRecords.add(pfamScanRecord);
            }

            filterAlignment4bestHits(blastHspRecords, diamondBlastHspRecords);
            handleOneSpecies(dir, fastaGzFile, blastHspRecords, diamondBlastHspRecords, name2PfamScanRecords);

            if (debug){
//                look4confusedFormalMembers(dir.getFileName().toString());
            }
        }

        summary();
    }

    private void filterAlignment4bestHits(List<BlastHspRecord> blastHspRecords, List<BlastHspRecord> diamondBlastHspRecords) {
        Comparator<BlastHspRecord> recordComparator = Comparator.comparingDouble(BlastHspRecord::getBitscore);

        List<BlastHspRecord> blastHspRecordsBack = Lists.newArrayList();
        List<BlastHspRecord> diamondBlastHspRecordsBack = Lists.newArrayList();

        int originalSize = blastHspRecords.size();
        originalSize += diamondBlastHspRecords.size();

        Table<String, String, Boolean> querySubjectTable = HashBasedTable.create();
        Map<String, List<BlastHspRecord>> candidateName2listMap = Maps.newHashMap();
        for (BlastHspRecord blastHspRecord : blastHspRecords) {
            List<BlastHspRecord> list = candidateName2listMap.computeIfAbsent(blastHspRecord.getQseqid(), k -> Lists.newArrayList());
            list.add(blastHspRecord);
        }
        for (BlastHspRecord blastHspRecord : diamondBlastHspRecords) {
            List<BlastHspRecord> list = candidateName2listMap.computeIfAbsent(blastHspRecord.getQseqid(), k -> Lists.newArrayList());
            list.add(blastHspRecord);
        }
        for (Map.Entry<String, List<BlastHspRecord>> entry : candidateName2listMap.entrySet()){
            String key = entry.getKey();
            List<BlastHspRecord> list = entry.getValue();
            Set<String> set = Sets.newHashSet();
            for (BlastHspRecord blastHspRecord : list){
                set.add(blastHspRecord.getSseqid());
            }
            if (set.size() == 1){
                querySubjectTable.put(key, set.iterator().next(), Boolean.TRUE);
            } else if (set.isEmpty()) {
                throw new IllegalArgumentException("Impossible...");
            }else {
                list.sort(recordComparator);
                BlastHspRecord last = list.getLast();
                querySubjectTable.put(key, last.getSseqid(), Boolean.TRUE);
            }
        }

        for (BlastHspRecord blastHspRecord : blastHspRecords) {
            Boolean bool = querySubjectTable.get(blastHspRecord.getQseqid(), blastHspRecord.getSseqid());
            if (bool != null && bool){
                blastHspRecordsBack.add(blastHspRecord);
            }
        }
        for (BlastHspRecord blastHspRecord : diamondBlastHspRecords) {
            Boolean bool = querySubjectTable.get(blastHspRecord.getQseqid(), blastHspRecord.getSseqid());
            if (bool != null && bool){
                diamondBlastHspRecordsBack.add(blastHspRecord);
            }
        }

        blastHspRecords.clear();
        blastHspRecords.addAll(blastHspRecordsBack);
        diamondBlastHspRecords.clear();
        diamondBlastHspRecords.addAll(diamondBlastHspRecordsBack);

        int currentSize = blastHspRecords.size();
        currentSize += diamondBlastHspRecords.size();

        log.debug("The number of blastHspRecords is reduced from {} to {}.", originalSize, currentSize);
    }


    private void summary() {
        // System.out 重定向到一个文件
        if (!debug){
            try {
                PrintStream printStream = new PrintStream(output_summary_table_path);
                System.setOut(printStream);
            } catch (FileNotFoundException e) {
                log.error("The file {} is not exist, error.", output_summary_table_path);
            }
        }
        System.out.print("Name");
        for (Map.Entry<String, Criterion> entry : name2criterion.entrySet()) {
            String wntCompt = entry.getKey();
            System.out.print("\t" + wntCompt);
        }
        System.out.println();
        Set<String> strings = table.rowKeySet();
        for (String species : speciesOrderList) {
            if (!strings.contains(species)){
                continue;
            }
            System.out.print(species);
            for (Map.Entry<String, Criterion> entry : name2criterion.entrySet()) {
                String wntCompt = entry.getKey();
                Integer count = table.get(species, wntCompt);
                //System.out.printf("%-10s %-10s %-5d%n", species, wntCompt, count.intValue());
                System.out.print("\t" + count);
            }

            System.out.println();
        }
    }

    private void handleOneSpecies(Path dir,String fastaGzFile, List<BlastHspRecord> blastHspRecords, List<BlastHspRecord> diamondBlastHspRecords, Map<String, List<PfamScanRecord>> name2PfamScanRecords) {
        // Use the union of them
        Map<String, Set<String>> wntComp2candidatesMap = Maps.newHashMap();
        for (BlastHspRecord blastHspRecord : blastHspRecords) {
            String qseqid = blastHspRecord.getQseqid();
            String sseqid = blastHspRecord.getSseqid();
            Set<String> strings = wntComp2candidatesMap.computeIfAbsent(sseqid, k -> Sets.newHashSet());
            strings.add(qseqid);
        }
        for (BlastHspRecord blastHspRecord : diamondBlastHspRecords) {
            String qseqid = blastHspRecord.getQseqid();
            String sseqid = blastHspRecord.getSseqid();
            Set<String> strings = wntComp2candidatesMap.computeIfAbsent(sseqid, k -> Sets.newHashSet());
            strings.add(qseqid);
        }

        Map<String, Integer> seqName2lengthMap = Maps.newHashMap();
        try {
            FastaReader.readAndProcessFastaPerEntry(fastaGzFile, (seqName, seq) -> {
                int speceIndex = seqName.indexOf(' ');
                String subName = speceIndex == -1 ? seqName : seqName.substring(0, speceIndex);
                seqName2lengthMap.put(subName, seq.length());
                return false;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String speciesName = dir.getFileName().toString();
        for (Map.Entry<String, Criterion> entry : name2criterion.entrySet()) {
            handleOneCompInSpecies(speciesName, seqName2lengthMap, wntComp2candidatesMap, blastHspRecords, diamondBlastHspRecords, name2PfamScanRecords, entry);
        }
    }

    private void look4confusedFormalMembers(String speciesName, Table<String, String, List<String>> tableOfCandidateSeqs) {
        Map<String, List<String>> row = tableOfCandidateSeqs.row(speciesName);

//        int totalCount = 0;
//        int uniqueCount = 0;
//        Set<String> uniqueNames = Sets.newHashSet();
//        for (Map.Entry<String, List<String>> entry : row.entrySet()){
//            List<String> values = entry.getValue();
//
//            totalCount += values.size();
//            uniqueNames.addAll(values);
//        }
//        uniqueCount = uniqueNames.size();
//        log.info("Species: {}, total count: {}, unique count: {}, more than once count: {}", speciesName, totalCount, uniqueCount, totalCount - uniqueCount);

        Map<String, List<String>> map = Maps.newHashMap();
        for (Map.Entry<String, List<String>> entry : row.entrySet()){
            String key = entry.getKey();
            List<String> value = entry.getValue();
            for (String name : value){
                List<String> strings = map.computeIfAbsent(name, k -> Lists.newArrayList());
                strings.add(key);
            }
        }
        Set<String> uniqueNames = Sets.newHashSet();
        for (Map.Entry<String, List<String>> entry : map.entrySet()){
            List<String> value = entry.getValue();
            if (value.size() > 1){
//                log.info("Species: {}, name: {}, components: {}", speciesName, entry.getKey(), value);
                uniqueNames.addAll(value);
            }
        }
        log.info("Species: {}, confused formal members: {}", speciesName, uniqueNames);
    }

    private void handleOneCompInSpecies(String speciesName, Map<String, Integer> seqName2lengthMap, Map<String, Set<String>> query2subjects,
                                        List<BlastHspRecord> blastHspRecords, List<BlastHspRecord> diamondBlastHspRecords,
                                        Map<String, List<PfamScanRecord>> name2PfamScanRecords,
                                        Map.Entry<String, Criterion> entry) {
        String wntComponent = entry.getKey();
        Criterion criterion = entry.getValue();


        Set<String> candidateNames = query2subjects.get(wntComponent);
        MutableInt matchedProteinCount = new MutableInt();
        List<String> formalMembers = Lists.newArrayList();

        if (debug){
            if (Objects.equals(wntComponent,"NKD1") && Objects.equals(speciesName,"Vertebrata")){
                System.out.println("SHISA2");
            }
        }


        int countFIrstMatch = 0;
        if (CollectionUtils.isNotEmpty(candidateNames)) {
            for (String seqName : candidateNames) {
                List<PfamScanRecord> pfamScanRecords = name2PfamScanRecords.get(seqName);

                Set<String> clans = Sets.newHashSet();
                Set<String> domains = Sets.newHashSet();
                if (pfamScanRecords != null){
                    pfamScanRecords.forEach(pfamScanRecord -> {
                        domains.add(pfamScanRecord.getHmmName());
                        clans.add(pfamScanRecord.getClan());
                    });
                }

                /*
                 * 判定最后得到的结果是否满足我们同源基因的判定标准：
                 * 1. 有Blastp HSP 记录
                 * 2. 有 pfam_scan 显著性的domain
                 *     1) 有三个domain记录的要符合 其中2 个；
                 *     2）有三个以上domain记录的要符合其中任意 3 个
                 * 3. 最后是 qeury 和subject的覆盖率，后面再单独看
                 */
                boolean matched = criterion.judgeMatchedDomain(clans, domains);
                Integer length = seqName2lengthMap.get(seqName);
                if (length == null){
                    throw new IllegalArgumentException("Please check your fasta file.");
                }
                if (!matched){
                    continue;
                }
                countFIrstMatch++;
                double candidateCoverage = judgeCoverage(blastHspRecords, diamondBlastHspRecords, seqName, length, wntComponent);
                boolean coverageMatched = candidateCoverage > CANDIDATES_COVERAGE_THRESHOLD;
                if (coverageMatched) {
                    matchedProteinCount.increment();
                    formalMembers.add(seqName);
                }else {

                }
            }
        }

        table.put(speciesName, wntComponent, matchedProteinCount.intValue());
        //System.out.printf("%-10s %-5d %-15d%n", wntComponent, criterion.getNumberOfCriterion(), matchedProteinCount.intValue());
    }

    private double judgeCoverage(List<BlastHspRecord> blastHspRecords, List<BlastHspRecord> diamondBlastHspRecords, String seqName,int queryLength, String wntComponent) {
        List<BlastHspRecord> queryHspRecords = Lists.newLinkedList();
        int count = 0;
        for (BlastHspRecord blastHspRecord : blastHspRecords){
            if (Objects.equals(blastHspRecord.getQseqid(), seqName) && Objects.equals(blastHspRecord.getSseqid(), wntComponent)){
                queryHspRecords.add(blastHspRecord);
                count++;
            }
        }
        for (BlastHspRecord blastHspRecord : diamondBlastHspRecords){
            if (Objects.equals(blastHspRecord.getQseqid(), seqName) && Objects.equals(blastHspRecord.getSseqid(), wntComponent)){
                queryHspRecords.add(blastHspRecord);
                count++;
            }
        }
        if (count == 0){
            throw  new IllegalArgumentException("Error");
        }
        boolean[] coverageArray = new boolean[queryLength];
        for (BlastHspRecord blastHspRecord : queryHspRecords){
            int qstart = blastHspRecord.getQstart();
            int qend = blastHspRecord.getQend();
            for (int i = qstart - 1; i < qend; i++){
                coverageArray[i] = true;
            }
        }

        double coverage = 0;
        for (boolean b : coverageArray){
            if (b){
                coverage ++;
            }
        }

        return coverage /queryLength;
    }

}
