package fasta.io;

import com.google.common.base.Joiner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Script_prepare_longestUnique_forEnsembl_pepFa {
    static Joiner joiner = Joiner.on('\t');
    static String headerLine = "Name\tcount\tgene\tunique";
    static List<String> outputList = new ArrayList<>();
    static boolean removeOriginalFile = false;
    static boolean debug = true;


    static final String suffix = ".fa.gz";

    public static void main(String[] args) throws IOException {
        String inputDir = args[0];
        String outputDir = args[1];
        String summaryFile = args[2];
        if (inputDir == null) {
            throw new RuntimeException("Please input the pep fasta file dir.");
        }

        outputList.add(headerLine);
        Files.list(Path.of(inputDir)).forEach(pepFasta -> {
            String fileName = pepFasta.getFileName().toString();

            String fileNameWithoutSuffix = fileName.substring(0, fileName.length() - suffix.length());
            String outputFileName = fileNameWithoutSuffix + "_longestCDS_unique.fa.gz";

            if (!fileName.endsWith(suffix)) {
                System.out.println("fileName:" + fileName + " is not end with " + suffix);
            } else {
                try {
                    handleOneFile(pepFasta, fileName, Path.of(outputDir, outputFileName));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Files.write(Path.of(summaryFile), outputList);
    }

    private static void handleOneFile(Path pepFasta, String fileName, Path outputPath) throws IOException {
        Path outputPath1 = pepFasta.resolveSibling("longest.fa.gz");
        String s1 = Script_get_longest_CDS_forEnsembl_pepFa.transformProteomicFasta(pepFasta, outputPath1);

        String s2 = Script_get_unique_forEnsembl_pepFa.handle(outputPath1, outputPath);

        outputList.add(joiner.join(fileName, s1, s2));

        Files.delete(outputPath1);
    }

}

