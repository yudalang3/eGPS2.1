package fasta.comparison;

import fasta.io.FastaReader;
import tsv.io.TSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Input the query fasta, subject fasta and the fmt6 pair-wise alignment file.
 * Fmt6 file is the output of blast or diamond.
 */
public class FastaComparer {
    public static void main(String[] args) throws Exception {
        FastaComparer comparator = new FastaComparer();
        comparator.run(args[0],args[1], args[2]);
    }

    private void run(String file1, String file2, String fmt6outFile) throws IOException {
        List<String> file1Set = new ArrayList<>();
        List<String> file2Set = new ArrayList<>();
        readFasta(file1, file1Set);
        readFasta(file2, file2Set);

        Map<String, List<String>> stringListMap = TSVReader.readAsKey2ListMap(fmt6outFile, false);
        List<String> file1SetsInFmt6file = stringListMap.get("V1");
        List<String> file2SetsInFmt6file = stringListMap.get("V2");
        HashSet<String> file1uniqueSet = new HashSet<>(file1SetsInFmt6file);
        HashSet<String> file2uniqueSet = new HashSet<>(file2SetsInFmt6file);

        System.out.printf("The fasta1 matched count is %d, total count is %d. ", file1uniqueSet.size(), file1Set.size());
        double ratio1 = file1uniqueSet.size() / (double) file1Set.size();
        System.out.println(ratio1);
        double ratio2 = file2uniqueSet.size() / (double) file2Set.size();
        System.out.printf("The fasta2 matched count is %d, total count is %d. ", file2uniqueSet.size(), file2Set.size());
        System.out.println(ratio2);

        if (ratio2 > 1.0) {
            file2uniqueSet.removeAll(file2Set);
            System.out.println("The remaining unique count is " + file2uniqueSet.size());
            for (String s : file2uniqueSet) {
                System.out.println(s);
            }
        }


    }

    private void readFasta(String file1, List<String> file1Set) throws IOException {
        FastaReader.readAndProcessFastaPerEntry(file1, (key, seq) -> {
            var index = key.indexOf(' ');
            String name;
            if (index > 0){
                name = key.substring(0, index);
            }else {
                name = key;
            }
            file1Set.add(name);

            return  false;
        });
    }
}
