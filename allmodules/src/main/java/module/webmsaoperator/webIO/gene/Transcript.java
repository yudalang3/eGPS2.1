package module.webmsaoperator.webIO.gene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import msaoperator.alignment.GlobalAlignmentSettings;


public class Transcript {
    private int start;
    private int end;
    private List<int[]> exons;
    private List<int[]> introns;
    private int[] cds;
    private Gene gene;
    private final String negativeString = "-1";
    
	public boolean quickSetExonsAndIntrons(int[] t) {
		int len = t.length;
		if (len % 2 != 0) {
			return false;
		}
		
		exons = new ArrayList<>();
		introns = new ArrayList<>();

		for (int i = 0; i < len; i += 2) {
			exons.add(new int[] { t[i], t[i + 1] });
			
			if (i != 0) {
				introns.add(new int[] { t[i-1] + 1, t[i] - 1 });
			}
			
		}
		
		return true;

	}

	public boolean obtainNonGapCoordinate(String sequence, boolean ifProteinCodingGene) {
		if ("-1".equalsIgnoreCase(gene.getString())) {
			reverseCorordinateIfNegativeChain(ifProteinCodingGene);
		}
		HashMap<Integer,Integer> positions = new HashMap<>(exons.size());
		HashSet<Integer> positionSet = new HashSet<Integer>(exons.size());
		
		for (int[] ints : exons) {
			positionSet.add(ints[0]);
			positionSet.add(ints[1]);
		}
		for (int[] ints : introns) {
			positionSet.add(ints[0]);
			positionSet.add(ints[1]);
		}
		if (ifProteinCodingGene) {
			positionSet.add(cds[0]);
			positionSet.add(cds[1]);
		}
		
		int len = sequence.length(); int real_1based_Index = 1;
		for (int i = 0; i < len; i++) {
			char aChar = sequence.charAt(i);
			if (aChar != GlobalAlignmentSettings.gapCharSymbol) {
				
				if (positionSet.contains(real_1based_Index)) {
					positions.put(real_1based_Index, i + 1);
				}
				
				real_1based_Index ++;
			}
		}
		
		
		for (int[] ints : exons) {
			for (int i = 0; i < 2; i++) {
				Integer integer = positions.get(ints[i]);
				if (integer == null) {
					return false;
				}
				ints[i] = integer;
			}
		}
		for (int[] ints : introns) {
			for (int i = 0; i < 2; i++) {
				Integer integer = positions.get(ints[i]);
				if (integer == null) {
					return false;
				}
				ints[i] = integer;
			}
		}
		
		if (ifProteinCodingGene) {
			for (int i = 0; i < 2; i++) {
				Integer integer = positions.get(cds[i]);
				if (integer == null) {
					return false;
				}
				cds[i] = integer;
			}
		}
		
    	return true;
	}

	private void reverseCorordinateIfNegativeChain(boolean ifProteinCodingGene) {
		int maxLength = gene.getEnd() - gene.getStart() + 1;
		for (int[] ints : exons) {
			int tt0 = ints[0];
			int tt1 = ints[1];
			
			ints[0] = maxLength - tt1 + 1;
			ints[1] = maxLength - tt0 + 1;
		}
		for (int[] ints : introns) {
			int tt0 = ints[0];
			int tt1 = ints[1];
			
			ints[0] = maxLength - tt1 + 1;
			ints[1] = maxLength - tt0 + 1;
		}
		Collections.reverse(exons);
		Collections.reverse(introns);
		
		if (ifProteinCodingGene) {
			int tt0 = cds[0];
			int tt1 = cds[1];
			
			cds[0] = maxLength - tt1 + 1;
			cds[1] = maxLength - tt0 + 1;
			
		}
		
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public List<int[]> getExons() {
		return exons;
	}

	public void setExons(List<int[]> exons) {
		this.exons = exons;
	}

	public List<int[]> getIntrons() {
		return introns;
	}

	public void setIntrons(List<int[]> introns) {
		this.introns = introns;
	}

	public int[] getCds() {
		return cds;
	}

	public void setCds(int[] cds) {
		this.cds = cds;
	}

	public Gene getGene() {
		return gene;
	}

	public void setGene(Gene gene) {
		this.gene = gene;
	}

	public String getNegativeString() {
		return negativeString;
	}
	
	
	
}
