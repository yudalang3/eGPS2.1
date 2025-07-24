package module.sequencelogo.makesequencelogo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fasta.io.FastaReader;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;


/**
* @author zjw
* @date 2022年9月1日
*类说明 用于计算每个碱基的频率和bite
*/
public class CalculateFrequencyOfPerBase {

	/**
	 * 输入的是读取完Fasta文件后的map
	 * @param readFastaDNASequence
	 * @return 每个碱基每个位置的频率和bits
	 */
	public static Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> calculateFrequencyAndBitsOfPerBaseUsedFastaFile(
			LinkedHashMap<String, String> readFastaDNASequence) {
		
		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> frequenceOfBasesPerPosition = new LinkedHashMap<Integer, LinkedHashMap<Character, Double>>();
		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> bitsOfBasesPerPosition = new LinkedHashMap<Integer, LinkedHashMap<Character, Double>>();

		Set<Entry<String, String>> entrySet = readFastaDNASequence.entrySet();
		int seqLength = entrySet.iterator().next().getValue().length();
		int numOfSeq = entrySet.size();

		for (Entry<String, String> entry : entrySet) {
			String sequence = entry.getValue();
			if (seqLength != sequence.length()) {
				throw new RuntimeException("The length of sequences must be same!");
			}
		}
		
		int sequenceType = 0;
		
		for (Entry<String, String> entry : entrySet) {
			String value = entry.getValue();
			if (value.contains("Q") || value.contains("R") || value.contains("D") || value.contains("E")
					|| value.contains("H") || value.contains("N") || value.contains("Y") || value.contains("P")
					|| value.contains("S") || value.contains("M") || value.contains("K") || value.contains("V")
					|| value.contains("I") || value.contains("F") || value.contains("L") || value.contains("W")) {
				sequenceType = 20;
			} else if (value.contains("B") || value.contains("J") || value.contains("O") || value.contains("X")
					|| value.contains("Z")) {
				throw new RuntimeException("Your sequence must be protein, RNA or DNA!");
			} else {
				sequenceType = 4;
			}
		}
		
        for (int i = 0; i < seqLength; i++) {
            Map<Character, Integer> counts = new LinkedHashMap<>();
            
            for (Entry<String, String> entry : entrySet) {
            	String value = entry.getValue();
            	char aminoAcid = value.charAt(i);
            	counts.put(aminoAcid, counts.getOrDefault(aminoAcid, 0) + 1);
            }
            Map<Character, Double> frequencies = new LinkedHashMap<>();
            LinkedHashMap<Character, Double> sortedFrequencises =new LinkedHashMap<>();
            double bits = 0;
            for (char aminoAcid : counts.keySet()) {
            	double frequency = (double) counts.get(aminoAcid) / numOfSeq;
                frequencies.put(aminoAcid, frequency);
                sortedFrequencises = sortedHashMap(frequencies);              
                bits += frequency * (Math.log(frequency) / Math.log(2));               
            }
            
            
            double Rsequence;
            if (numOfSeq <= 10) {
            	Rsequence=(Math.log(sequenceType) / Math.log(2))-(-bits+CalculateBits.calculateCorrectionFactor(sequenceType, numOfSeq));
            }else {
            	Rsequence=(Math.log(sequenceType) / Math.log(2))-(-bits);
			}
            
            Map<Character, Double> bitsOfBases = new LinkedHashMap<Character, Double>();
            LinkedHashMap<Character, Double> sortedBitsOfBases = new LinkedHashMap<>();
            for (char aminoAcid : counts.keySet()) {
            	double frequency = (double) counts.get(aminoAcid) / numOfSeq;
            	bitsOfBases.put(aminoAcid, frequency*Rsequence/(Math.log(sequenceType) / Math.log(2))*2.0);
            	sortedBitsOfBases = sortedHashMap(bitsOfBases);
            }
            
            bitsOfBasesPerPosition.put(i, sortedBitsOfBases);
            frequenceOfBasesPerPosition.put(i, sortedFrequencises);          		
        }	
		
		Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> triple = new ImmutableTriple<>(
				seqLength, frequenceOfBasesPerPosition, bitsOfBasesPerPosition);
		
		return triple;
	}

	/**
	 * 输入文件只有每个位置的碱基频率，用来计算每个位置碱基的频率和Bits
	 * @param frequenceOfBasesPerPosition
	 * @return
	 */
	public static Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> calculateFrequencyAndBitsOfPerBaseUsedFrequencyFile(
			LinkedHashMap<Integer, LinkedHashMap<Character, Double>> frequenceOfBasesPerPosition) {

		int seqlength = frequenceOfBasesPerPosition.size();
		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> sortedFrequenceOfBasesPerPosition = new LinkedHashMap<Integer, LinkedHashMap<Character, Double>>();
		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> bitsOfBasesPerPosition = new LinkedHashMap<Integer, LinkedHashMap<Character, Double>>();

		for (Entry<Integer, LinkedHashMap<Character, Double>> entry1 : frequenceOfBasesPerPosition.entrySet()) {		

			ArrayList<Double> frequenceOfBasesInArray = new ArrayList<Double>();
			LinkedHashMap<Character, Double> bitsOfBases = new LinkedHashMap<Character, Double>();

			LinkedHashMap<Character, Double> frequenceOfBases = entry1.getValue();
			for (Entry<Character, Double> entry2 : frequenceOfBases.entrySet()) {
				frequenceOfBasesInArray.add(entry2.getValue());
			}

			double Rsequence = CalculateBits.calculateAmountOfInformationPresentAtPerPosition(frequenceOfBasesInArray);

			for (Entry<Character, Double> entry2 : frequenceOfBases.entrySet()) {
				bitsOfBases.put(entry2.getKey(), entry2.getValue() * Rsequence);
			}

			LinkedHashMap<Character, Double> sortedFrequenceOfBases = sortedHashMap(frequenceOfBases);
			LinkedHashMap<Character, Double> sortedBitsOfBases = sortedHashMap(bitsOfBases);

			bitsOfBasesPerPosition.put(entry1.getKey(), sortedBitsOfBases);
			sortedFrequenceOfBasesPerPosition.put(entry1.getKey(), sortedFrequenceOfBases);
		}

		Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> triple = new ImmutableTriple<>(
				seqlength, sortedFrequenceOfBasesPerPosition, bitsOfBasesPerPosition);
		return triple;
	}

	private static LinkedHashMap<Character, Double> sortedHashMap(Map<Character, Double> frequencies) {

		//先转成ArrayList集合
		ArrayList<Entry<Character, Double>> list = new ArrayList<Entry<Character, Double>>(frequencies.entrySet());

		//从小到大排序（从大到小将o1与o2交换即可）
		Collections.sort(list, new Comparator<Entry<Character, Double>>() {

			@Override
			public int compare(Entry<Character, Double> o1, Entry<Character, Double> o2) {
				return ((o1.getValue() - o2.getValue() == 0) ? 0 : (o1.getValue() - o2.getValue() > 0) ? 1 : -1);
			}

		});

		//新建一个LinkedHashMap，把排序后的List放入
		LinkedHashMap<Character, Double> sortedMap = new LinkedHashMap<>();
		for (Entry<Character, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
	
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public static void main(String[] args) throws IOException {

		File file = new File("E://task/seqLogo/fastaFile/seq.fas");
		LinkedHashMap<String, String> readFastaDNASequence = FastaReader.readFastaDNASequence(file, true, false);

		Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> numOfseqAndfrequenceOfBasesPerPositionAndBitsOfBasesPerPosition = calculateFrequencyAndBitsOfPerBaseUsedFastaFile(
				readFastaDNASequence);

		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> frequenceOfBasesPerPosition = numOfseqAndfrequenceOfBasesPerPositionAndBitsOfBasesPerPosition
				.getMiddle();

		int numOfseq = numOfseqAndfrequenceOfBasesPerPositionAndBitsOfBasesPerPosition.getLeft();

		LinkedHashMap<Integer, LinkedHashMap<Character, Double>> bitsOfBasesPerPosition = numOfseqAndfrequenceOfBasesPerPositionAndBitsOfBasesPerPosition
				.getRight();
		//		System.out.println(numOfseq);
		//		System.out.println(bitsOfBasesPerPosition);
		//		System.out.println(frequenceOfBasesPerPosition);
	}

}
