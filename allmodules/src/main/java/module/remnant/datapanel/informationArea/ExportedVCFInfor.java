package module.remnant.datapanel.informationArea;

import java.util.List;

public class ExportedVCFInfor {
	
	String includedBedFilePath = "";
	String excludedBedFilePath = "";
	
	String[] selectedSpeciesNames;
	int[] selectedIndiviIndexes;
	List<String> theCompleteSampleIndividualIDs;
	public String getIncludedBedFilePath() {
		return includedBedFilePath;
	}
	public void setIncludedBedFilePath(String includedBedFilePath) {
		this.includedBedFilePath = includedBedFilePath;
	}
	public String getExcludedBedFilePath() {
		return excludedBedFilePath;
	}
	public void setExcludedBedFilePath(String excludedBedFilePath) {
		this.excludedBedFilePath = excludedBedFilePath;
	}
	public String[] getSelectedSpeciesNames() {
		return selectedSpeciesNames;
	}
	public void setSelectedSpeciesNames(String[] selectedSpeciesNames) {
		this.selectedSpeciesNames = selectedSpeciesNames;
	}
	public int[] getSelectedIndiviIndexes() {
		return selectedIndiviIndexes;
	}
	public void setSelectedIndiviIndexes(int[] selectedIndiviIndexes) {
		this.selectedIndiviIndexes = selectedIndiviIndexes;
	}
	public List<String> getTheCompleteSampleIndividualIDs() {
		return theCompleteSampleIndividualIDs;
	}
	public void setTheCompleteSampleIndividualIDs(List<String> theCompleteSampleIndividualIDs) {
		this.theCompleteSampleIndividualIDs = theCompleteSampleIndividualIDs;
	}
	
	
}