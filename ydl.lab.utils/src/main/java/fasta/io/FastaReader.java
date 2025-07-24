package fasta.io;

import org.apache.commons.lang3.mutable.MutableObject;
import utils.EGPSFileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class FastaReader {
    private final List<String> speciesNames = new ArrayList<>();
    private final List<String> sequence = new ArrayList<>();
    private final File inputFile;

    public FastaReader(File inputFile) {
        this.inputFile = inputFile;
    }

    public List<String> getSpeciesNames() {
        return this.speciesNames;
    }

    public List<String> getSequence() {
        return this.sequence;
    }

    public void processReader() throws IOException {
        LinkedHashMap<String, String> dnaSequences = readFastaDNASequence(this.inputFile);

        for (Map.Entry<String, String> entry : dnaSequences.entrySet()) {
            String key = entry.getKey();
            this.speciesNames.add(key.trim());
            this.sequence.add(entry.getValue().toUpperCase());
        }
    }

    public static LinkedHashMap<String, String> readFastaSequence(InputStream inputStream) throws IOException {
        int sz = 10 * 1024 * 1024;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), sz);
        return readFastaDNASequenceWorkCode(bufferedReader, false, false);
    }

    public static LinkedHashMap<String, String> readFastaSequence(String inputFile) throws IOException {
        int sz = 10 * 1024 * 1024;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile), sz);
        return readFastaDNASequenceWorkCode(bufferedReader, false, false);
    }

    public static LinkedHashMap<String, String> readFastaDNASequence(File inputFile) throws IOException {
        int sz = 10 * 1024 * 1024;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile), sz);
        return readFastaDNASequenceWorkCode(bufferedReader, false, false);
    }

    public static LinkedHashMap<String, String> readFastaDNASequence(File inputFile, boolean upCase, boolean x2n)
            throws IOException {
        int sz = 10 * 1024 * 1024;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile), sz);
        return readFastaDNASequenceWorkCode(bufferedReader, upCase, x2n);
    }

    public static LinkedHashMap<String, String> readFastaDNASequenceWorkCode(BufferedReader bufferedReader,
                                                                             boolean upCase, boolean x2n) throws IOException {
        LinkedHashMap<String, String> dnaSequences = new LinkedHashMap<>();
        String str;
        String speciesName = "";
        StringBuilder sequence = new StringBuilder(16384);
        while ((str = bufferedReader.readLine()) != null) {
            str = str.trim();
            if (str.startsWith(">")) {
                if (!sequence.isEmpty()) {
                    dnaSequences.put(speciesName, stringConvert(sequence.toString(), upCase, x2n));
                    sequence.setLength(0);
                }

                speciesName = str.substring(1);
                continue;
            }
            sequence.append(str);
        }

        if (!sequence.isEmpty()) {
            dnaSequences.put(speciesName, stringConvert(sequence.toString(), upCase, x2n));
            sequence.setLength(0);
        }

        bufferedReader.close();

        return dnaSequences;
    }

    public static void readAndProcessFastaPerEntry(BufferedReader bufferedReader,
                                                   BiPredicate<String, String> sequenceOperator) throws IOException {
        MutableObject<String> speciesNameObj = new MutableObject<>();
        StringBuilder sequence = new StringBuilder(16384);
        String currLine;
        while ((currLine = bufferedReader.readLine()) != null) {
            String str = currLine.trim();
            if (str.startsWith(">")) {
                if (!sequence.isEmpty()) {
                    boolean test = sequenceOperator.test(speciesNameObj.getValue(), sequence.toString());
                    sequence.setLength(0);
                    if (test) {
                        break;
                    }
                }

                String speciesName = str.substring(1);
                speciesNameObj.setValue(speciesName);
                continue;
            }
            sequence.append(str);
        }

        if (!sequence.isEmpty()) {
            sequenceOperator.test(speciesNameObj.getValue(), sequence.toString());
            sequence.setLength(0);
        }
        bufferedReader.close();
    }

    /**
 * Reads and processes each entry in a FASTA file.
 *
 * This function reads a FASTA file from the specified file path and applies the provided operation to each sequence entry.
 * The file can be either compressed or uncompressed, and the function handles both cases automatically.
 *
 * @param inputFilePath The path to the FASTA file, which can be a compressed or uncompressed file.
 * @param sequenceOperator A BiPredicate function that processes each sequence entry. The first parameter is the sequence ID, and the second parameter is the sequence itself.
 * @throws IOException If an I/O error occurs while reading the file.
 */
public static void readAndProcessFastaPerEntry(String inputFilePath, BiPredicate<String, String> sequenceOperator)
        throws IOException {
    // Set buffer size to 100MB to improve performance when reading large files
    int sz = 100 * 1024 * 1024;

    // Get the input stream, supporting automatic decompression for compressed files
    InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(inputFilePath);

    // Create a buffered reader with the specified buffer size
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), sz);
    readAndProcessFastaPerEntry(bufferedReader, sequenceOperator);
}


    public static LinkedHashMap<String, String> readFastaSequence(List<String> lines) {
        return readFastaDNASequenceWorkCode(lines, false, false);
    }

    public static LinkedHashMap<String, String> readFastaDNASequenceWorkCode(List<String> lines, boolean upCase,
                                                                             boolean x2n) {
        LinkedHashMap<String, String> dnaSequences = new LinkedHashMap<>();
        MutableObject<String> speciesNameObj = new MutableObject<>();

        StringBuilder sequence = new StringBuilder(16384);
        for (String stripContent : lines) {
            if (stripContent.startsWith(">")) {
                if (!sequence.isEmpty()) {
                    dnaSequences.put(speciesNameObj.getValue(), stringConvert(sequence.toString(), upCase, x2n));
                    sequence.setLength(0);
                }

                String speciesName = stripContent.substring(1);
                speciesNameObj.setValue(speciesName);
                continue;
            }
            sequence.append(stripContent);
        }

        if (!sequence.isEmpty()) {
            dnaSequences.put(speciesNameObj.getValue(), stringConvert(sequence.toString(), upCase, x2n));
            sequence.setLength(0);
        }

        return dnaSequences;
    }

    private static String stringConvert(String string, boolean upCase, boolean x2n) {
        if (upCase) {
            String upCaseString = string.toUpperCase();
            if (x2n) {
                char[] charArray = upCaseString.toCharArray();
                int len = charArray.length;
                for (int i = 0; i < len; i++) {
                    if (charArray[i] == 'X') {
                        charArray[i] = 'N';
                    }
                }
                return new String(charArray);
            }
            return upCaseString;
        }

        if (x2n) {
            char[] charArray = string.toCharArray();
            int len = charArray.length;
            for (int i = 0; i < len; i++) {
                if (charArray[i] == 'X') {
                    charArray[i] = 'N';
                } else if (charArray[i] == 'x') {
                    charArray[i] = 'n';
                }
            }
            return new String(charArray);
        }
        return string;
    }
}
