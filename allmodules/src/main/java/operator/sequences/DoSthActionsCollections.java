package operator.sequences;

import com.google.common.base.Joiner;
import module.ambigbse.DNAComplement;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import utils.string.EGPSStringUtil;

import java.util.*;
import java.util.function.Function;

public class DoSthActionsCollections {

    Function<String, String> getSortedUniqueSetBySemicolonAction() {
        return (input) -> {
            Joiner joiner = Joiner.on(';');
            StringJoiner stringJoiner = new StringJoiner("\n");
            String[] lines = getSequenceLines(input);
            for (String line : lines) {
                String[] split = EGPSStringUtil.split(line, ';');
                TreeSet<String> sortedStrings = new TreeSet<>(Arrays.asList(split));
                String join = joiner.join(sortedStrings);
                stringJoiner.add(join);
            }

            return stringJoiner.toString();
        };
    }

    Function<String, String> getSequenceLengthAction() {
        return (input) -> {
            StringJoiner stringJoiner = new StringJoiner("\n");
            String[] lines = getSequenceLines(input);
            for (String line : lines) {
                stringJoiner.add(String.valueOf(line.length()));
            }

            return stringJoiner.toString();
        };
    }
    Function<String, String> getReverseSequenceAction() {
        return (input) -> {
            StringJoiner stringJoiner = new StringJoiner("\n");
            String[] lines = getSequenceLines(input);
            for (String line : lines) {
                stringJoiner.add(StringUtils.reverse(line));
            }

            return stringJoiner.toString();
        };
    }
    Function<String, String> getReverseComplementSequence4DNAAction() {
        DNAComplement dnaComplement = new DNAComplement();
        return (input) -> {
            StringJoiner stringJoiner = new StringJoiner("\n");
            String[] lines = getSequenceLines(input);
            for (String line : lines) {
                String reverseComplement = dnaComplement.getReverseComplement(line);
                stringJoiner.add(reverseComplement);
            }

            return stringJoiner.toString();
        };
    }

    private static String[] getSequenceLines(String input) {
        String[] lines = EGPSStringUtil.split(input, '\n');
        List<String> ret = Lists.newArrayList();
        for (String line : lines){
            ret.add(line.stripTrailing());
        }
        return ret.toArray(new String[0]);
    }

}
