/**  
* <p>Title: RestAlignment.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2018-11-9  
* @version 1.0  

*/
package module.webmsaoperator.webIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import geneticcodes.GeneticCode;
import geneticcodes.codeTables.TheStandardCode;
import module.webmsaoperator.SpeciesNameTrans;
import module.webmsaoperator.webIO.gene.EmployedRegionNotAvailable;
import module.webmsaoperator.webIO.gene.Gene;

/**
 * <p>
 * Title: RestAlignment
 * </p>
 * <p>
 * Description:
 * Web2Alignment means either from gene symbol to get alignment or from genomic region ro aligment!
 * </p>
 * 
 * @author yudalang
 * @date 2018-11-9
 */
public class RestWeb2AlignmentMultiStep {

	private String speciesName;
	private String speciesSetGroup;
	private String region;
	private GeneticCode geneticCode = new TheStandardCode();

	private final boolean shouldGetGenomicRegion;
	/**
	 * 0 whole genebody; 1 exons; 2 intron ; 3 Coding sequence; 4 4-fold degenerate
	 * sites
	 */
	private final int emplyedRegionIndex;
	private int timeoutMilliseconds;
	
	private RestGenomicRegionMultiStep restGenomicRegion;
	private RestGRegion2MSAMultiStep restGenomicRegion2MSA;
	private List<String> seqs;
	private List<String> seq_names;
	
	private int processIndex = 1;
	

	/**
	 * The constructor of getting Web alignment from gene symbol to alignment!
	 * 
	 * @param timeoutMilliseconds
	 * @param speciesName : "homo_sapiens"
	 * @param geneSymbol : for example INS
	 * @param speciesSetGroup : "mammals"
	 * @param emplyedRegionIndex : 0 whole genebody; 1 exons; 2 intron ; 3 Coding sequence; 4 4-fold degenerate
	 * sites
	 */
	public RestWeb2AlignmentMultiStep(int timeoutMilliseconds,String speciesName, String geneSymbol, String speciesSetGroup, int emplyedRegionIndex){
		this.speciesName = speciesName;
		this.speciesSetGroup = speciesSetGroup;
		this.timeoutMilliseconds = timeoutMilliseconds;
		this.emplyedRegionIndex = emplyedRegionIndex;
		
		shouldGetGenomicRegion = true;
		
		// YDL: if we get alignment from eGPS Cloud, we need to transform the speciesName
		String speciesNameTemp = null;
		if ("100 vertebrate species".equalsIgnoreCase(speciesSetGroup)) {
			speciesNameTemp = new SpeciesNameTrans().getdatFromAssemblyName(speciesName)[1];
		}else {
			speciesNameTemp = speciesName;
		}
		
		/** This is to get full length of gene body and or total exons' length */
		final int getFullLengthIndex = 0;
		if (emplyedRegionIndex == getFullLengthIndex) {
			restGenomicRegion = new RestGenomicRegionMultiStep(speciesNameTemp, geneSymbol, 0);
		} else {
			restGenomicRegion = new RestGRegionMultiStepWithExon(speciesNameTemp, geneSymbol, 1);
		}
		
		restGenomicRegion.setTimeoutMilliseconds(timeoutMilliseconds);
	}
	
	/**
	 * The constructor of getting Web alignment from genomic region to alignment!
	 * 
	 * @param timeoutMilliseconds
	 * @param speciesName
	 * @param speciesSetGroup
	 * @param region
	 */
	public RestWeb2AlignmentMultiStep(int timeoutMilliseconds, String speciesName, String speciesSetGroup,
			String region) {
		this.speciesName = speciesName;
		this.speciesSetGroup = speciesSetGroup;
		this.region = region;
		this.timeoutMilliseconds = timeoutMilliseconds;

		shouldGetGenomicRegion = false;
		emplyedRegionIndex = 0;
	}
	
	
	public boolean getAlignmentFromWebMultiStep() throws IOException, EmployedRegionNotAvailable {
		switch (processIndex) {

		case 1:
			if (getGenomicRegionMultiSteps()) {
			} else {
				processIndex++;
				checkForRegionBeObtain();
			}
			break;
		case 2:
			if (getAlignmentMultiSteps()) {
			} else {
				processIndex++;
				//System.out.println(getClass()+" case 2 getAlignment finshed!");
			}
			break;
		case 3:
			if (processAlignemtAccordingToGeneInfor()) {
			} else {
				processIndex++;
				//System.out.println(getClass()+" case 3 processAlignemtAccordingToGeneInfor finshed!");
			}
			break;
		default:
			// processIndex more than last case!
			return false;
		}

		return true;
	}

	private void checkForRegionBeObtain() throws EmployedRegionNotAvailable {
		
		if (emplyedRegionIndex > 2) {
			if (restGenomicRegion.getIfProteinCodingGene() == -1) {
				throw new EmployedRegionNotAvailable("Get error", "The gene you interset is not a protein-coding gene, eGPS can't query coding sequences! ");
			}
		}
	}

	public int getCurrentProgressIndex() {
		int ret = processIndex;
		if (shouldGetGenomicRegion) {
			ret += restGenomicRegion.getCurrentProgressIndex();
		} else {
			ret += 1;
		}
		if (restGenomicRegion2MSA != null) {
			ret += restGenomicRegion2MSA.getCurrentProgressIndex();
		}
		return ret;
	}

	/**
	 * The error should be catched by outer method！ The information should be "Can't
	 * get region <code>region</code> in Rest!"
	 * 
	 * @author yudalang
	 */
	private boolean getGenomicRegionMultiSteps() throws IOException {

		if (shouldGetGenomicRegion) {
			boolean flag = restGenomicRegion.getRegionStringMultiStep();
			if (flag) {
				return true;
			} else {
				// get region from REST!! 3:181711924-181714436:1
				region = restGenomicRegion.getGenomicRegionInfo().getRegionStr();
				// The final step we initialize get Alignment object!
				restGenomicRegion2MSA = new RestGRegion2MSAMultiStep(timeoutMilliseconds,speciesName, region, speciesSetGroup);
				return false;
			}
		} else {
			// Don't need to get genomic region we directly initialize the MSA
			restGenomicRegion2MSA = new RestGRegion2MSAMultiStep(timeoutMilliseconds,speciesName, region, speciesSetGroup);
			return false;
		}

	}

	/**
	 * In the final step, it will assign the seqs and seqNames values!
	* @author yudalang
	 */
	private boolean getAlignmentMultiSteps() throws IOException {
		boolean flag = restGenomicRegion2MSA.getEnsembleMSAMultiStep();
	
		if (flag) {
			return true;
		} else {
			// This means ensemblRMaf has already get seqs and names
			seqs = restGenomicRegion2MSA.getSeqs();
			seq_names = restGenomicRegion2MSA.getSeq_names();

			return false;
		}
	}

	private boolean processAlignemtAccordingToGeneInfor() throws EmployedRegionNotAvailable {
		if (emplyedRegionIndex > 0 ) {
			int seqSize = seqs.size();
			List<String> seqExons = new ArrayList<>(seqSize);
			Gene geneStructureInfo = restGenomicRegion.getGenomicRegionInfo().getGeneStructureInfo();
			
			switch (emplyedRegionIndex) {
			case 1:
				for (int i = 0; i < seqSize; i++) {
					geneStructureInfo.setSequence(seqs.get(i));
					if (i == 0) {
						geneStructureInfo.obtainNonGapCoordinate();
					}
					seqExons.add(geneStructureInfo.getExonsSeqOfFirstTranscript());
				}
				break;
			case 2:
				for (int i = 0; i < seqSize; i++) {
					geneStructureInfo.setSequence(seqs.get(i));
					if (i == 0) {
						geneStructureInfo.obtainNonGapCoordinate();
					}
					geneStructureInfo.getIntronsSeqOfFirstTranscript();
					seqExons.add(geneStructureInfo.getIntronsSeqOfFirstTranscript());
				}
				break;
			case 3:
				for (int i = 0; i < seqSize; i++) {
					geneStructureInfo.setSequence(seqs.get(i));
					if (i == 0) {
						geneStructureInfo.obtainNonGapCoordinate();
					}
					seqExons.add(geneStructureInfo.getCodingStringOfFirstTranscript());
				}
				break;
			case 4:
				for (int i = 0; i < seqSize; i++) {
					geneStructureInfo.setSequence(seqs.get(i));
					if (i == 0) {
						geneStructureInfo.obtainNonGapCoordinate();
					}
					seqExons.add(geneStructureInfo.get4FoldSitesOfFirstTranscript(geneticCode));
				}
				break;
			}
			
			seqs = seqExons;
		}
		// Else seqs won't be change!
		return false;
	}
	
	public List<String> getSeq_names() {
		
		if ("100 vertebrate species".equalsIgnoreCase(speciesSetGroup)) {
			List<String> newNames = new ArrayList<>(seq_names.size());
			SpeciesNameTrans speciesNameTrans = new SpeciesNameTrans();
			for (String string : seq_names) {
				String tt = speciesNameTrans.getdatFromAssemblyName(string)[1];
				newNames.add(tt);
			}
			return newNames;
		}
		
		return seq_names;
	}

	public List<String> getSeqsIfNeedToJoin() {
		return seqs;
	}


	public int getNeededSteps() {
		// The first ret index is 1
		int ret = processIndex;
		if (shouldGetGenomicRegion) {
			ret += RestGenomicRegionMultiStep.neededTotalSteps;
		}

		ret += RestGRegion2MSAMultiStep.neededTotalSteps;
		
		return ret;
	}
	
	public void setGeneticCode(GeneticCode geneticCode) {
		this.geneticCode = geneticCode;
	}

}
