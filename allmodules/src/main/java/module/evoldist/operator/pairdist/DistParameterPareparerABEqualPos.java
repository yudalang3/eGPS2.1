package module.evoldist.operator.pairdist;

import java.util.HashMap;
import java.util.Map;

import module.evoldist.operator.EvoDistanceUtil;

public class DistParameterPareparerABEqualPos implements DistParameterPareparer {

	protected final Map<Character, char[]> map;

	public DistParameterPareparerABEqualPos() {
//		nucleotides = new HashSet<Character>(15);
//		nucleotides.add('ElegantJTable');
//		nucleotides.add('T');
//		nucleotides.add('C');
//		nucleotides.add('G');
//
//		nucleotides.add('Y');
//		nucleotides.add('R');
//		nucleotides.add('W');
//		nucleotides.add('S');
//		nucleotides.add('K');
//		nucleotides.add('M');
//		nucleotides.add('D');
//		nucleotides.add('V');
//		nucleotides.add('H');
//		nucleotides.add('B');
//		nucleotides.add('N');

		map = new HashMap<Character, char[]>();
		map.put('A', new char[] { 'A' });
		map.put('G', new char[] { 'G' });
		map.put('C', new char[] { 'C' });
		map.put('T', new char[] { 'T' });
		map.put('Y', new char[] { 'C', 'T' });
		map.put('R', new char[] { 'A', 'G' });
		map.put('W', new char[] { 'A', 'T' });
		map.put('S', new char[] { 'G', 'C' });
		map.put('K', new char[] { 'T', 'G' });
		map.put('M', new char[] { 'A', 'C' });
		map.put('D', new char[] { 'A', 'G', 'T' });
		map.put('V', new char[] { 'A', 'G', 'C' });
		map.put('H', new char[] { 'A', 'T', 'C' });
		map.put('B', new char[] { 'T', 'G', 'C' });
		map.put('N', new char[] { 'A', 'G', 'C', 'T' });
	}

	public DistParameterLevel1 getLeve1Para(String seq1, String seq2) {
		SimpleObj ss = simpleParseTwoStrings(seq1, seq2);
		return new DistParameterLevel1(ss.numOfTransition[0] + ss.numOfTransition[1] + ss.numOfTransversion,
				ss.validLength);
	}

	private SimpleObj simpleParseTwoStrings(String seq1, String seq2) {

		SimpleObj simpleObj = new SimpleObj();
		int length = seq1.length();

		for (int i = 0; i < length; i++) {
			char a = Character.toUpperCase(seq1.charAt(i));
			char b = Character.toUpperCase(seq2.charAt(i));

			char[] seq1Map = map.get(a);
			char[] seq2Map = map.get(b);

			if (seq1Map == null || seq2Map == null) {
				continue;
			} else {
				double factor = 1.0 / (seq1Map.length * seq2Map.length);
				for (char c : seq1Map) {
					for (char d : seq2Map) {
						dealWithChars(c, d, factor, simpleObj);
					}
				}
			}

		}
		return simpleObj;
	}

	public SimpleObj simpleParseTwoCharSNP2GetDiff(char snp1, char snp2) {
		SimpleObj simpleObj = new SimpleObj();

		char a = Character.toUpperCase(snp1);
		char b = Character.toUpperCase(snp2);

		char[] seq1Map = map.get(a);
		char[] seq2Map = map.get(b);

		if (seq1Map == null || seq2Map == null) {
			return simpleObj;
		} else {
			double factor = 1.0 / (seq1Map.length * seq2Map.length);
			for (char c : seq1Map) {
				for (char d : seq2Map) {
					dealWithChars(c, d, factor, simpleObj);
				}
			}
		}

		return simpleObj;
	}

	private void dealWithChars(char c, char d, double factor, SimpleObj simpleObj) {
		switch (c) {
		case 'A':
			AmbiguousBaseCode.A.simpleSwithToGetParameter(factor, d, simpleObj);
			break;
		case 'G':
			AmbiguousBaseCode.G.simpleSwithToGetParameter(factor, d, simpleObj);
			break;
		case 'C':
			AmbiguousBaseCode.C.simpleSwithToGetParameter(factor, d, simpleObj);
			break;
		case 'T':
			AmbiguousBaseCode.T.simpleSwithToGetParameter(factor, d, simpleObj);
			break;
		default:
			System.err.println("Warning: its impossible!\t" + getClass());
			break;
		}

	}

	public DistParameterLevel2 getLeve2Para(String seq1, String seq2) {

		SimpleObj ss = simpleParseTwoStrings(seq1, seq2);

		double numOfMismatch = ss.numOfTransition[0] + ss.numOfTransition[1] + ss.numOfTransversion;
		double numOfTransi = ss.numOfTransition[0] + ss.numOfTransition[1];
		return new DistParameterLevel2(numOfMismatch, numOfTransi, ss.validLength);
	}

	private ComplexObj complexParseTwoStrings(String seq1, String seq2) {

		ComplexObj complexObj = new ComplexObj();
		int length = seq1.length();

		for (int i = 0; i < length; i++) {
			char a = Character.toUpperCase(seq1.charAt(i));
			char b = Character.toUpperCase(seq2.charAt(i));

			char[] seq1Map = map.get(a);
			char[] seq2Map = map.get(b);

			if (seq1Map == null || seq2Map == null) {
				continue;
			} else {
				double factor = 1.0 / (seq1Map.length * seq2Map.length);
				for (char c : seq1Map) {
					for (char d : seq2Map) {
						int x = EvoDistanceUtil.getFirstIndexZeroBased(c);
						int y = EvoDistanceUtil.getFirstIndexZeroBased(d);
						complexObj.addSeq1CountInfo(x, factor);
						complexObj.addSeq1CountInfo(y, factor);
						complexObj.addToCountMatrix(x, y, factor);
						dealWithChars(c, d, factor, complexObj);
					}
				}
			}
		}

		return complexObj;
	}

	public DistParameterLevel3 getLeve3Para(String seq1, String seq2) {
		ComplexObj ss = complexParseTwoStrings(seq1, seq2);

		double numOfMismatch = ss.numOfTransition[0] + ss.numOfTransition[1] + ss.numOfTransversion;
		double numOfTransi = ss.numOfTransition[0] + ss.numOfTransition[1];
		double[] totoalCountOf4nn = new double[4];
		for (int i = 0; i < 4; i++) {
			totoalCountOf4nn[i] += ss.countOf4nnSeq1[i];
			totoalCountOf4nn[i] += ss.countOf4nnSeq2[i];
		}
		return new DistParameterLevel3(numOfMismatch, numOfTransi, ss.validLength, totoalCountOf4nn);

	}

	public DistParameterLevel41 getLeve41Para(String seq1, String seq2) {
		ComplexObj ss = complexParseTwoStrings(seq1, seq2);

		double numOfMismatch = ss.numOfTransition[0] + ss.numOfTransition[1] + ss.numOfTransversion;
		double numOfTransi = ss.numOfTransition[0] + ss.numOfTransition[1];
		double[] totoalCountOf4nn = new double[4];
		for (int i = 0; i < 4; i++) {
			totoalCountOf4nn[i] += ss.countOf4nnSeq1[i];
			totoalCountOf4nn[i] += ss.countOf4nnSeq2[i];
		}

		return new DistParameterLevel41(numOfMismatch, numOfTransi, ss.validLength, totoalCountOf4nn,
				ss.numOfTransition[0]);

	}

	public DistParameterLevel42 getLeve42Para(String seq1, String seq2) {
		ComplexObj ss = complexParseTwoStrings(seq1, seq2);

		double numOfMismatch = ss.numOfTransition[0] + ss.numOfTransition[1] + ss.numOfTransversion;
		double numOfTransi = ss.numOfTransition[0] + ss.numOfTransition[1];
		double[] totoalCountOf4nn = new double[4];
		for (int i = 0; i < 4; i++) {
			totoalCountOf4nn[i] += ss.countOf4nnSeq1[i];
			totoalCountOf4nn[i] += ss.countOf4nnSeq2[i];
		}

		return new DistParameterLevel42(numOfMismatch, numOfTransi, ss.validLength, totoalCountOf4nn,
				ss.countOfnucl2nuc);

	}

	public DistParameterLevel51 getLeve51Para(String seq1, String seq2) {
		ComplexObj ss = complexParseTwoStrings(seq1, seq2);

		double numOfMismatch = ss.numOfTransition[0] + ss.numOfTransition[1] + ss.numOfTransversion;
		double numOfTransi = ss.numOfTransition[0] + ss.numOfTransition[1];
		double[] totoalCountOf4nn = new double[4];
		for (int i = 0; i < 4; i++) {
			totoalCountOf4nn[i] += ss.countOf4nnSeq1[i];
			totoalCountOf4nn[i] += ss.countOf4nnSeq2[i];
		}
		return new DistParameterLevel51(numOfMismatch, numOfTransi, ss.validLength, totoalCountOf4nn,
				ss.numOfTransition[0], ss.countOf4nnSeq1);

	}

}
