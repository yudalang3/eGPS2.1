package module.genome;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BedFileParser {
    public static List<GenomicInterval> parseBedFile(String bedFilePath) throws IOException {
        List<GenomicInterval> intervals = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(bedFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                String chromosome = fields[0];
                int start = Integer.parseInt(fields[1]);
                int end = Integer.parseInt(fields[2]);
                intervals.add(new GenomicInterval(chromosome, start, end));
            }
        }
        return intervals;
    }
}