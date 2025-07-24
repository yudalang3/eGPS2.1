package fasta.io;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class Script_get_unique_forEnsembl_pepFa {
    public static void main(String[] args) throws IOException {
        String fastaPath = findGzipPepFasta(args);
        if (fastaPath.endsWith("_unique.fa.gz")){
            System.err.println(fastaPath + "\tAlready unique");
            return;
        }

        Path pepFasta = Path.of(fastaPath);
        String fileName = pepFasta.getFileName().toString();
        final String suffix = ".fa.gz";

        String fileNameWithoutSuffix = fileName.substring(0, fileName.length() - suffix.length());
        String outputFileName = fileNameWithoutSuffix + "_unique.fa.gz";
        Path outputPath = pepFasta.resolveSibling(outputFileName);


        handle(pepFasta, outputPath);

        Files.delete(pepFasta);
    }

    static String handle(Path pepFasta, Path outputPath) throws IOException {
        Map<String, Integer> sequence2count = Maps.newHashMap();
        Map<String, List<String>> sequence2name = Maps.newHashMap();

        MutableInt totalCount = new MutableInt();
        FastaReader.readAndProcessFastaPerEntry(pepFasta.toString(), (speciesName, sequence) -> {
            Integer i = sequence2count.computeIfAbsent(sequence, k -> 0);
            sequence2count.put(sequence, i + 1);
            List<String> strings = sequence2name.computeIfAbsent(sequence, k -> Lists.newLinkedList());
            strings.add(speciesName);
            totalCount.increment();
            return false;
        });


        List<String> outputs = Lists.newLinkedList();

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(sequence2count.entrySet());
        for (Map.Entry<String, Integer> entry : entries) {
            var sequence = entry.getKey();
            var count = entry.getValue();
            List<String> strings = sequence2name.get(sequence);
            String name = strings.getFirst();
            outputs.add(">".concat(name));
            outputs.add(sequence);
        }


        outputContents(outputPath, outputs);

        return String.valueOf(entries.size());

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

    private static void outputContents(Path outputPath, List<String> outputs) {
        try (GZIPOutputStream gzipOS = new GZIPOutputStream(Files.newOutputStream(outputPath));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(gzipOS, StandardCharsets.UTF_8))) {
            for (String output : outputs){
                writer.write(output);
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

