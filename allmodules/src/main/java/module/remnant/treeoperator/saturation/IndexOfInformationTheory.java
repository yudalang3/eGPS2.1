package module.remnant.treeoperator.saturation;

public class IndexOfInformationTheory {
	
	private double[] globalFreq;
	
	public final static long factorial(long number) {
        if (number <= 1)
            return 1;
        else
            return number * factorial(number - 1);
    }
	
	double getFSS(String[] alignedSequences) {
		int numOfSeqs = alignedSequences.length;
		double count = 0;
		double ret = 0;
		
		for (int a = 0; a <= numOfSeqs; a++) {
			int tmp1 = numOfSeqs - a;
			for (int c = 0; c <= tmp1; c++) {
				int tmp2 = numOfSeqs - a - c;
				for (int g = 0; g <= tmp2; g++) {
					int t = numOfSeqs - a - c -g;
					
					double tmpRet = (double) factorial(numOfSeqs) / factorial(a) / factorial(c) / factorial(g) / factorial(t);
					tmpRet *= Math.pow(globalFreq[0], a);
					tmpRet *= Math.pow(globalFreq[1], c);
					tmpRet *= Math.pow(globalFreq[2], g);
					tmpRet *= Math.pow(globalFreq[3], t);
					
					double tmpRet2 = 0;
					tmpRet2 += getEntropyItem((double) a / numOfSeqs);
					tmpRet2 += getEntropyItem((double) c/ numOfSeqs);
					tmpRet2 += getEntropyItem((double) g / numOfSeqs);
					tmpRet2 += getEntropyItem((double) t / numOfSeqs);
					
					tmpRet *= tmpRet2;
					
					ret += tmpRet;
					//if (count == 20) {
					//System.out.println(count+":\t"+a+"\t"+t+"\t"+c+"\t"+g+"\t"+tmpRet+"\t"+ret);
					//}
					count ++;
				}
			}
		}
		
		//System.out.println("The count is: " + count);
		return - ret;
	}
	
	public double getISS(String[] alignedSequences) {
		int len = alignedSequences[0].length();
		int numOfSeqs = alignedSequences.length;
		
		// to upper case
		toUpperCase(alignedSequences);
		
		double averageH = getAverageH(alignedSequences);
		double hFSS = getFSS(alignedSequences);
		
		return averageH / hFSS;
	}
	
	private void toUpperCase(String[] seqs) {
		// to upper case
		for (int i = 0; i < seqs.length; i++) {
			seqs[i] = seqs[i].toUpperCase();
		}
	}
	
	public double getAverageH(String[] alignedSequences) {
		
		toUpperCase(alignedSequences);
		
		int len = alignedSequences[0].length();
		int numOfSeqs = alignedSequences.length;
		double ret = 0;
		int[] globalNumOfEachNucleotides = new int[4];
		double totalNumOfNucleotides = numOfSeqs * len;
		
		for (int i = 0; i < len; i++) {
			// A C G T
			int[] numOfEachNucleotides = new int[4];
			for (int j = 0; j < numOfSeqs; j++) {
				char c = alignedSequences[j].charAt(i);
				
				if (c == 'A' ) {
					numOfEachNucleotides[0] ++;
				} else if (c == 'C') {
					numOfEachNucleotides[1] ++;
				}else if (c == 'G') {
					numOfEachNucleotides[2] ++;
				}else if (c == 'T') {
					numOfEachNucleotides[3] ++;
				}
			}
			
			ret += getHForSiteI(numOfEachNucleotides);
			
			for (int j = 0; j < 4; j++) {
				globalNumOfEachNucleotides[j] += numOfEachNucleotides[j];
			}
			
		}
		
		globalFreq = new double[4];
		for (int j = 0; j < 4; j++) {
			globalFreq[j] = globalNumOfEachNucleotides[j] / totalNumOfNucleotides;
		}
		
		return ret / len;
		
	}

	double getHForSiteI(int[] numOfEachNucleotides) {
		double ret = 0;
		double sum = 0;
		for (int i = 0; i < 4; i++) {
			sum += numOfEachNucleotides[i];
		}
		
		for (int i = 0; i < 4; i++) {
			double pj = numOfEachNucleotides[i] / sum;
			ret += getEntropyItem(pj);
		}
		
		return -ret;
		
	}
	
	private double getEntropyItem(double pj) {
		if (pj == 0) {
			return 0;
		}else {
			return pj * Math.log(pj) / Math.log(2);
		}
		
	}

}
