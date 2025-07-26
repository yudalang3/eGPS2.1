package geneticcodes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import geneticcodes.codeTables.TheBacterialPlantPlastidCode;
import geneticcodes.codeTables.TheCiliateDHexamitaNuclearCode;
import geneticcodes.codeTables.TheEFMitochondrialCode;
import geneticcodes.codeTables.TheEuplotidNuclearCode;
import geneticcodes.codeTables.TheInvertebrateMitochondrialCode;
import geneticcodes.codeTables.TheMPCMCMSCode;
import geneticcodes.codeTables.TheStandardCode;
import geneticcodes.codeTables.TheVertebrateMitochondrialCode;
import geneticcodes.codeTables.TheYeastMitochondrialCode;

/**
 * 
 * <p>
 * Title: GeneticCode
 * </p>
 * <p>
 * Description:
 * https://www.ncbi.nlm.nih.gov/Taxonomy/taxonomyhome.html/index.cgi?chapter=tgencodes#SG1
 * 
 * </p>
 * 
 * @author yudalang
 * @date 2019-7-1
 */
public abstract class GeneticCode implements AminoAcid, IGeneticCode {

	protected Map<String, Character> geneticCodeMap = new HashMap<>(64);
	protected char[] nn4 = { 'A', 'T', 'C', 'G' };

	protected void initializeMap(String[] tripletNNs_aaAbbr) {
		int len = tripletNNs_aaAbbr.length;
		for (int i = 0; i < len; i++) {
			String s = tripletNNs_aaAbbr[i];
			String tripletNN = s.substring(0, 3);
			char aa = s.charAt(4);
			geneticCodeMap.put(tripletNN, aa);
		}
	}
	
	public Optional<Character> getAminoAcid(char[] tripletNN) {
		Character ret = null;
		if (tripletNN.length != 3) {
			throw new IllegalArgumentException();
		}
		
		ret = geneticCodeMap.get(new String(tripletNN));
		
		return Optional.ofNullable(ret);
	}
	
	public Optional<Character> getAminoAcid(String codon) {
		Character ret = null;
		if (codon.length() != 3) {
			throw new IllegalArgumentException();
		}
		ret = geneticCodeMap.get(codon);
		
		return Optional.ofNullable(ret);
	}

	/**
	 * 
	 * Degeneracy : <br>
	 * 0-fold degenerate sites are those at which all changes are
	 * nonsynonymous. <br>
	 * 2-fold degenerate sites are those at which one out of three
	 * changes is synonymous. (All sites at which two out of three changes are
	 * synonymous also are included in this category.) <br>
	 * 4-fold degenerate sites are
	 * those at which all changes are synonymous. <br>
	 * 
	 * @return 0: non degenerate site; 2 twofold degenerate site and 4 4-fold site
	 * 
	 */
	public byte[] degenerateAttr(char[] tripletNN) {
		byte[] degenerateAttrs = new byte[3];

		Character oriAAChar = geneticCodeMap.get(new String(tripletNN));

		for (int i = 0; i < 3; i++) {
			String[] posibleTriplets = possibleTripletNucls(tripletNN, i);
			degenerateAttrs[i] = judgeDegenerateAttr(posibleTriplets, oriAAChar);
		}
		return degenerateAttrs;
	}

	/**
	 * @return 0: nondegenerate site; 2 twofold degenerate site and 4 4-fold site
	 */
	private byte judgeDegenerateAttr(String[] posibleTriplets, Character oriAAChar) {
		byte numOfSynnonymousChange = getNumOfSynnonymousChange(posibleTriplets,oriAAChar);
		if (numOfSynnonymousChange == 3) {
			return 4;
		} else if (numOfSynnonymousChange == 0) {
			return 0;
		} else {
			return 2;
		}
	}
	
	private byte getNumOfSynnonymousChange(String[] posibleTriplets, Character oriAAChar) {
		byte numOfSynnonymousChange = 0;
		for (int i = 0; i < 3; i++) {
			Character aaChar = geneticCodeMap.get(posibleTriplets[i]);
			if (aaChar == oriAAChar) {
				numOfSynnonymousChange++;
			}
		}
		return numOfSynnonymousChange;
	}

	/**
	 * @param tripletNN : new char[] {'A','T','G'};
	 * @param i         : an integer of 0,1,2;
	 * @return three possible changes of <code>i</code>'s nucleotide.
	 */
	private String[] possibleTripletNucls(char[] tripletNN, int i) {
		String[] ret = new String[3];
		char[] tmpTripletNN = new char[3];
		byte assignIndex = 0;

		for (char nn : nn4) {
			if (nn != tripletNN[i]) {
				System.arraycopy(tripletNN, 0, tmpTripletNN, 0, 3);
				tmpTripletNN[i] = nn;
				ret[assignIndex] = new String(tmpTripletNN);
				assignIndex++;
			}
		}

		return ret;
	}

	/**
	 * 直接把CDS转成氨基酸序列，其它不考虑。 也就是说输入的就是0-ORF
	 * 
	 * @param nnString
	 * @return
	 */
	public String translateNotConsiderOther(String nnString) {
		StringBuilder proteinStr = new StringBuilder(4096);
		int len = nnString.length();
		for (int i = 0; i < len; i += 3) {
			String tripletNN = nnString.substring(i, i + 3);
			Character aaChar = geneticCodeMap.get(tripletNN);
			proteinStr.append(aaChar);
		}

		return proteinStr.toString();
	}

	public abstract int getNCBICodeTableIndex();

	public static GeneticCode geneticCodeFactory(String tableName) {
		GeneticCode geneticCode = null;

		if (tableName.equalsIgnoreCase(GeneticCodeTableNames[0])) {
			geneticCode = new TheStandardCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[1])) {
			geneticCode = new TheVertebrateMitochondrialCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[2])) {
			geneticCode = new TheYeastMitochondrialCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[3])) {
			geneticCode = new TheInvertebrateMitochondrialCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[4])) {
			geneticCode = new TheCiliateDHexamitaNuclearCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[5])) {
			geneticCode = new TheEFMitochondrialCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[6])) {
			geneticCode = new TheEuplotidNuclearCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[7])) {
			geneticCode = new TheBacterialPlantPlastidCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[8])) {
			geneticCode = new TheMPCMCMSCode();
		} else {
			geneticCode = new TheStandardCode();
		}

		return geneticCode;
	}
	
	public static void main(String[] args) {
		TheStandardCode theStandardCode = new TheStandardCode();
		
		String string = "GGT";
		char[] charArray = string.toCharArray();
		System.out.println(Arrays.toString(theStandardCode.degenerateAttr(charArray)));
	}

}
