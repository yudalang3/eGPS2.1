package module.evoldist.operator.pairdist;

import java.util.HashSet;

import module.evoldist.operator.EvoDistanceUtil;

public class DistParameterPareparerABAsGap implements DistParameterPareparer {

	protected final HashSet<Character> nucleotides;

	public DistParameterPareparerABAsGap() {
		nucleotides = new HashSet<Character>(4);
		nucleotides.add('A');
		nucleotides.add('T');
		nucleotides.add('C');
		nucleotides.add('G');
	}

	@Override
	public DistParameterLevel1 getLeve1Para(String seq1, String seq2) {
		int[] tt = simpleParseTwoStrings(seq1, seq2);
		return new DistParameterLevel1(tt[0] + tt[1] + tt[2], tt[3]);
	}

	private int[] simpleParseTwoStrings(String seq1, String seq2) {

		int[] numOfTransition = new int[2];
		int numOfTransversion = 0;
		int validLength = 0;

		int length = seq1.length();

		for (int i = 0; i < length; i++) {
			char a = Character.toUpperCase(seq1.charAt(i));
			char b = Character.toUpperCase(seq2.charAt(i));
			if (nucleotides.contains(a) && nucleotides.contains(b)) {
				validLength++;
				if (a != b) {
					byte res = isTransition(a, b);
					if (res == -1) {
						numOfTransversion++;

					} else {
						numOfTransition[res]++;
					}
				}
			}
		}

		return new int[] { numOfTransition[0], numOfTransition[1], numOfTransversion, validLength };
	}

	/**
	 * Judge if two chars is transition!
	 * 
	 * @return -1: not transition ; 0: A <-> G; 1: T <-> C;
	 */
	protected byte isTransition(char a, char b) {
		byte ret = -1;
		if (a == 'A' && b == 'G') {
			ret = 0;
		} else if (a == 'G' && b == 'A') {
			ret = 0;
		} else if (a == 'C' && b == 'T') {
			ret = 1;
		} else if (a == 'T' && b == 'C') {
			ret = 1;
		}

		return ret;
	}

	@Override
	public DistParameterLevel2 getLeve2Para(String seq1, String seq2) {

		int[] tt = simpleParseTwoStrings(seq1, seq2);

		int numOfMismatch = tt[0] + tt[1] + tt[2];
		int numOfTransi = tt[0] + tt[1];
		return new DistParameterLevel2(numOfMismatch, numOfTransi, tt[3]);
	}

	private EvoDistParamter complexParseTwoStrings(String seq1, String seq2) {

		double[] numOfTransition = new double[2];
		double numOfTransversion = 0;
		double validLength = 0;
		double[] countOf4nn1 = new double[4];
		double[] countOf4nn2 = new double[4];
		double[][] countOfn2n = new double[4][4];

		int length = seq1.length();

		for (int i = 0; i < length; i++) {
			char a = Character.toUpperCase(seq1.charAt(i));
			char b = Character.toUpperCase(seq2.charAt(i));

			int aIndex = getFirstIndexZeroBased(a);
			int bIndex = getFirstIndexZeroBased(b);

			if (aIndex != -1 && bIndex != -1) {
				validLength++;
				countOf4nn1[aIndex] += 1;
				countOf4nn2[bIndex] += 1;

				if (aIndex < bIndex) {
					countOfn2n[aIndex][bIndex] += 1;
				} else {
					countOfn2n[bIndex][aIndex] += 1;
				}

				if (aIndex != bIndex) {
					byte res = isTransition(a, b);
					if (res == -1) {
						numOfTransversion++;

					} else {
						numOfTransition[res]++;
					}
				}
			}
		}

		return new EvoDistParamter(numOfTransition, numOfTransversion, validLength, countOf4nn1, countOf4nn2,
				countOfn2n);
	}

	private int getFirstIndexZeroBased(char c) {
		return	EvoDistanceUtil.getFirstIndexZeroBased(c);
	}

	@Override
	public DistParameterLevel3 getLeve3Para(String seq1, String seq2) {
		EvoDistParamter tt = complexParseTwoStrings(seq1, seq2);
		return new DistParameterLevel3(tt.getNumOfMismatch(), tt.getNumOfTransition(), tt.getValidateLength(),
				tt.getTotalCountOf4Nucleotide());

	}

	@Override
	public DistParameterLevel41 getLeve41Para(String seq1, String seq2) {
		EvoDistParamter tt = complexParseTwoStrings(seq1, seq2);
		double[] numOfTransitionArray = tt.getNumOfTransitionArray();
		return new DistParameterLevel41(tt.getNumOfMismatch(), tt.getNumOfTransition(), tt.getValidateLength(),
				tt.getTotalCountOf4Nucleotide(), numOfTransitionArray[0]);

	}

	@Override
	public DistParameterLevel42 getLeve42Para(String seq1, String seq2) {
		EvoDistParamter tt = complexParseTwoStrings(seq1, seq2);
		return new DistParameterLevel42(tt.getNumOfMismatch(), tt.getNumOfTransition(), tt.getValidateLength(),
				tt.getTotalCountOf4Nucleotide(), tt.getCountOfNuctd2nd());

	}

	@Override
	public DistParameterLevel51 getLeve51Para(String seq1, String seq2) {
		EvoDistParamter tt = complexParseTwoStrings(seq1, seq2);
		double[] numOfTransitionArray = tt.getNumOfTransitionArray();
		return new DistParameterLevel51(tt.getNumOfMismatch(), tt.getNumOfTransition(), tt.getValidateLength(),
				tt.getTotalCountOf4Nucleotide(), numOfTransitionArray[0], tt.getCountOf4NucleotideOfSeq1());

	}

}
