package fasta.io;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;
import utils.string.EGPSStringUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.GZIPOutputStream;

public class Script_get_longest_CDS_forEnsembl_pepFa {

    static boolean removeOriginalFile = false;
    static boolean debug = false;

    public static void main(String[] args) throws IOException {
        String input = findGzipPepFasta(args);
        if (input == null) {
            throw new RuntimeException("Please input the pep fasta file.");
        }
        Path pepFasta = Path.of(input);
        final String suffix = ".fa.gz";

        String fileName = pepFasta.getFileName().toString();
        if (fileName.endsWith(suffix)) {
            String fileNameWithoutSuffix = fileName.substring(0, fileName.length() - suffix.length());
            String outputFileName = fileNameWithoutSuffix + "_longestCDS.fa.gz";

            Path outputPath = pepFasta.resolveSibling(outputFileName);
            if (Files.exists(outputPath)) {
                throw new RuntimeException("File already exists.");
            } else {
                transformProteomicFasta(pepFasta, outputPath);
                if (removeOriginalFile) {
                    Files.delete(pepFasta);
                }
            }
        } else {
            throw new RuntimeException("fileName:" + fileName + " is not end with " + suffix);
        }
    }

    private static String findGzipPepFasta(String[] args) throws IOException {
        if (args.length == 1) {
            return args[0];
        } else {
            Path path = Path.of(".");
            MutableObject<String> filePath = new MutableObject<>();

            Files.list(path).forEach(ppp -> {
                String string = ppp.toString();
                if (string.endsWith("fa.gz")) {
                    if (!string.endsWith("_longestCDS.fa.gz")) {
                        filePath.setValue(string);
                    }
                }
            });

            return filePath.getValue();
        }
    }

    static String transformProteomicFasta(Path pepFasta, Path outputPath) throws IOException {
        MutableInt totalCount = new MutableInt();

        Set<String> geneTypes = new HashSet<>();

        Map<String, Pair<String, String>> pep2entries = new HashMap<>();
        LinkedHashMap<String, List<Pair<String, Integer>>> gene2pepAndLength = new LinkedHashMap<>();

        // 0 1 2
        //>CapteP211078 pep supercontig:Capitella_teleta_v1.0:CAPTEscaffold_1000:482:3795:1
        // 3 4 5 6
        // gene:CapteG211078 transcript:CapteT211078 gene_biotype:protein_coding transcript_biotype:protein_coding
        System.out.println(pepFasta.toString());
        FastaReader.readAndProcessFastaPerEntry(pepFasta.toString(), (header, sequence) -> {

            String[] strings = EGPSStringUtil.split(header, ' ');

            if (strings.length < 6){
                System.out.println();
            }
            geneTypes.add(strings[5]);
            String transcriptName = strings[4];
            pep2entries.put(transcriptName, Pair.of(header, sequence));

            var geneName = strings[3];
            List<Pair<String, Integer>> stringIntegerPair = gene2pepAndLength.get(geneName);
            Pair<String, Integer> pair = Pair.of(transcriptName, sequence.length());

            if (stringIntegerPair == null) {
                List<Pair<String, Integer>> list = new ArrayList<>();
                list.add(pair);
                gene2pepAndLength.put(geneName, list);
            } else {
                stringIntegerPair.add(pair);
            }

            totalCount.increment();
            return false;
        });

        if (!check4proteinFormat(pep2entries)) {
            throw new RuntimeException("The pep format are NOT correct. Please inspect it...");
        }

        StringJoiner stringJoiner = new StringJoiner("\t");
//        System.out.println("Total number of sequences are: " + totalCount.intValue());
        stringJoiner.add("Summary");
        stringJoiner.add(pepFasta.getFileName().toString());
        stringJoiner.add(totalCount.toString());

        if (pep2entries.size() != totalCount.intValue()) {
            throw new RuntimeException("pep2entries.size() != totalCount.intValue()");
        }

        Comparator<Pair<String, Integer>> comparator = (o1, o2) -> {
            int o1Length = o1.getRight();
            int o2Length = o2.getRight();
            return Integer.compare(o2Length, o1Length);
        };

        Set<Map.Entry<String, List<Pair<String, Integer>>>> entries = gene2pepAndLength.entrySet();
        for (Map.Entry<String, List<Pair<String, Integer>>> entry : entries) {
            entry.getValue().sort(comparator);
        }
//        System.out.println("Total number of genes are: " + entries.size());
        stringJoiner.add(String.valueOf(entries.size()));

        for (String geneType : geneTypes) {
//            System.out.println(geneType);
            stringJoiner.add(geneType);
        }

        if (debug){
            return null;
        }

        if (entries.size() == totalCount.intValue()) {
            System.out.println("Found one gene one transcript, so directly cp the original file.");
            Files.copy(pepFasta, outputPath);
        } else {
            outputResults(entries, pep2entries, outputPath);
        }

        System.out.println(stringJoiner);

        return totalCount + "\t" + entries.size();
    }

    private static boolean check4proteinFormat(Map<String, Pair<String, String>> pep2entries) {
        Optional<Map.Entry<String, Pair<String, String>>> first = pep2entries.entrySet().stream().findFirst();
        Map.Entry<String, Pair<String, String>> stringPairEntry = first.get();
        String header = stringPairEntry.getValue().getKey();
        String[] split = EGPSStringUtil.split(header, ' ');
        if (!Objects.equals(split[1], "pep")) {
            return false;
        }

        return true;
    }

    private static void outputResults(Set<Map.Entry<String, List<Pair<String, Integer>>>> entries,
                                      Map<String, Pair<String, String>> pep2entries,
                                      Path outputPath) {

        try (GZIPOutputStream gzipOS = new GZIPOutputStream(Files.newOutputStream(outputPath));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(gzipOS, StandardCharsets.UTF_8))) {
            for (Map.Entry<String, List<Pair<String, Integer>>> entry : entries) {
                List<Pair<String, Integer>> value = entry.getValue();
                Pair<String, Integer> first = value.getFirst();

                Pair<String, String> stringStringPair = pep2entries.get(first.getKey());
                String header = stringStringPair.getLeft();

                writer.write(">");
                writer.write(header);
                writer.write("\n");
                writer.write(stringStringPair.getValue());
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

