package fasta.stat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fasta.io.FastaReader;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class UniqueStat {
    static boolean output = true;
    public static void main(String[] args) throws IOException {
        String fastaPath = args[0];
        double ratio = getRatio(fastaPath);
        System.out.println("Repeat ratio: " + ratio);
    }

    public static double getRatio(String fastaPath) throws IOException {
        Map<String, Integer> sequence2count = Maps.newHashMap();
        Map<String, List<String>> sequence2name = Maps.newHashMap();

        MutableInt totalCount = new MutableInt();
        FastaReader.readAndProcessFastaPerEntry(fastaPath, (speciesName, sequence) -> {
            Integer i = sequence2count.computeIfAbsent(sequence, k -> 0);
            sequence2count.put(sequence, i + 1);
            List<String> strings = sequence2name.computeIfAbsent(sequence, k -> Lists.newLinkedList());
            strings.add(speciesName);
            totalCount.increment();
            return false;
        });
        MutableInt dupCount = new MutableInt();
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(sequence2count.entrySet());
        entries.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        for (Map.Entry<String, Integer> entry : entries){
            var sequence = entry.getKey();
            var count = entry.getValue();
            if (count > 1){
                if (output){
                    List<String> strings = sequence2name.get(sequence);
                    StringJoiner stringJoiner = new StringJoiner(";");
                    for (String string : strings) {
                        stringJoiner.add(string);
                    }
                    System.out.println(count + "\t" + stringJoiner.toString());
                }
                dupCount.add(count);
            }
        }

        double ratio = dupCount.doubleValue() / totalCount.doubleValue();
        return ratio;
    }
}
