package fasta.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Script_allocate_raw_pep_to_dir_by_fileName {
    static final String suffix = ".fa.gz";

    public static void main(String[] args) throws IOException {
        String inputDir = args[0];
        String outputDir = args[1];
        if (inputDir == null) {
            throw new RuntimeException("Please input the pep fasta file dir.");
        }


        Files.list(Path.of(inputDir)).forEach(pepFasta -> {
            String fileName = pepFasta.getFileName().toString();
            if (!fileName.endsWith(suffix)) {
                System.out.println("fileName:" + fileName + " is not end with " + suffix);
            } else {
                try {
                    handleOneFile(pepFasta, fileName, outputDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    private static void handleOneFile(Path pepFasta, String fileName, String outputDir) throws IOException {
        int indexOf = fileName.indexOf('.');
        if (indexOf == -1) {
            System.out.println("fileName:" + fileName + " is not end with " + indexOf);
        } else {
            String newDirName = fileName.substring(0, indexOf);
            Files.createDirectories(Path.of(outputDir, newDirName));

            Path outPepPath = Path.of(outputDir, newDirName, pepFasta.getFileName().toString());
            Files.copy(pepFasta, outPepPath);
//                        Files.delete(outPepPath);
        }
    }

}

