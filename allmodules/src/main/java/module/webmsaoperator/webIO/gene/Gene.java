package module.webmsaoperator.webIO.gene;

import java.util.ArrayList;
import java.util.List;

import geneticcodes.GeneticCode;
import msaoperator.alignment.GlobalAlignmentSettings;


public class Gene {
	
    private int start;
    private int end;
    /**Also the chromosome and some scaffold*/
    private String chrom;
    /** E.g. GHR38 */
    private String assemblyName;
    private String string;
    private String sequence;
    private int[] fourFoldSiteIndex;
    private boolean ifProteinCodingGene;
    private List<Transcript> transcripts = new ArrayList<>();
    
    public void addOneTranscript(Transcript transcript) {
		transcripts.add(transcript);
	}
    
    public String getExonsSeqOfFirstTranscript() {
		List<int[]> exons = transcripts.get(0).getExons();
		StringBuilder strBuilder = new StringBuilder(8192);
		int size = exons.size();
		for (int i = 0; i < size; i++) {
			String s = sequence.substring(exons.get(i)[0] - 1, exons.get(i)[1]);
			strBuilder.append(s);
		}
		return strBuilder.toString();
	}
    
    public String getIntronsSeqOfFirstTranscript() {
  		List<int[]> introns = transcripts.get(0).getIntrons();
  		StringBuilder strBuilder = new StringBuilder(8192);
  		int size = introns.size();
  		for (int i = 0; i < size; i++) {
  			String s = sequence.substring(introns.get(i)[0] - 1, introns.get(i)[1]);
  			strBuilder.append(s);
  		}
  		return strBuilder.toString();
  	}
    
    public String getCodingStringOfFirstTranscript() throws EmployedRegionNotAvailable{
    	
    	if (!ifProteinCodingGene) {
			throw new EmployedRegionNotAvailable("Get error", "The gene you interset is not a protein-coding gene, eGPS can't query coding sequences! ");
		}
    	int[] cds = transcripts.get(0).getCds();
    	List<int[]> exons = transcripts.get(0).getExons();
    	
    	int exonCountReduce1 = exons.size() - 1;
    	
    	List<int[]> exonsCopy = new ArrayList<>();
    	// integrate the CDS information to get the alignment!
    	int[] firstEle = {cds[0] , exons.get(0)[1]};
    	int[] lastEle = { exons.get(exonCountReduce1)[0] , cds[1]};
    	
    	exonsCopy.add(firstEle);
    	for (int i = 1; i < exonCountReduce1 - 1; i++) {
    		exonsCopy.add(exons.get(i));
		}
    	exonsCopy.add(lastEle);
    	
    	
		StringBuilder strBuilder = new StringBuilder(8192);
		for (int i = 0; i < exonsCopy.size(); i++) {
			String s = sequence.substring(exonsCopy.get(i)[0] - 1, exonsCopy.get(i)[1]);
			strBuilder.append(s);
		}
		
		return strBuilder.toString();
    }
    
    public boolean obtainNonGapCoordinate() {
    	if (sequence == null) {
			return false;
		}
    	return transcripts.get(0).obtainNonGapCoordinate(sequence,ifProteinCodingGene);
	}

	public String get4FoldSitesOfFirstTranscript(GeneticCode gcode) throws EmployedRegionNotAvailable {

		String cds = getCodingStringOfFirstTranscript();
		int len = cds.length();

		if (fourFoldSiteIndex == null) {
			initializeFourFoldSiteIndexArray(cds,gcode);
		}
		StringBuilder sBuilder = new StringBuilder(len);

		
		for (int i : fourFoldSiteIndex) {
			sBuilder.append(cds.charAt(i));
		}

		return sBuilder.toString();
	}

	private void initializeFourFoldSiteIndexArray(String cds, GeneticCode gcode) {
		final int len = cds.length();
		final int candidate4FoldSiteIndex = 2;
		int threeTimesIndex = 0;
		
		List<Integer> fourFoldSiteIndexes = new ArrayList<>(len / 2);
		
		char[] tmpTripletNN = new char[3];
		for (int i = 0; i < len; i++) {
			char charAt = cds.charAt(i);
			if (charAt != GlobalAlignmentSettings.gapCharSymbol ) {
				tmpTripletNN[threeTimesIndex] = charAt;
				threeTimesIndex ++;
				if (threeTimesIndex % 3 == 0) {
					threeTimesIndex = 0;
					
					byte degenerateindex = gcode.degenerateAttr(tmpTripletNN)[candidate4FoldSiteIndex];
					if (degenerateindex == 4) {
//						System.out.print(Arrays.toString(tmpTripletNN)+i+
//								"\t");
						fourFoldSiteIndexes.add(i);
					}
					
					fourFoldSiteIndexes.add(i);
				}
			}
		}
		
	
		
		fourFoldSiteIndex = fourFoldSiteIndexes.stream().mapToInt(i -> i).toArray();
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

	public String getChrom() {
		return chrom;
	}

	public void setChrom(String chrom) {
		this.chrom = chrom;
	}

	public String getAssemblyName() {
		return assemblyName;
	}

	public void setAssemblyName(String assemblyName) {
		this.assemblyName = assemblyName;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public int[] getFourFoldSiteIndex() {
		return fourFoldSiteIndex;
	}

	public void setFourFoldSiteIndex(int[] fourFoldSiteIndex) {
		this.fourFoldSiteIndex = fourFoldSiteIndex;
	}

	public boolean isIfProteinCodingGene() {
		return ifProteinCodingGene;
	}

	public void setIfProteinCodingGene(boolean ifProteinCodingGene) {
		this.ifProteinCodingGene = ifProteinCodingGene;
	}

	public List<Transcript> getTranscripts() {
		return transcripts;
	}

	public void setTranscripts(List<Transcript> transcripts) {
		this.transcripts = transcripts;
	}
	
	
   
}
