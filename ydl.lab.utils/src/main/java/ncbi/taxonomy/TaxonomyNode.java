package ncbi.taxonomy;

public class TaxonomyNode {
    private int taxId;
    private int parentTaxId;
    private String rank;
    private String emblCode;
    private int divisionId;
    private int inheritedDivFlag;
    private int geneticCodeId;
    private int inheritedGCFlag;
    private int mitochondrialGeneticCodeId;
    private int inheritedMGCFlag;
    private int genBankHiddenFlag;
    private int hiddenSubtreeRootFlag;
    private String comments;
    private int plastidGeneticCodeId;
    private int inheritedPGCFlag;
    private int specifiedSpecies;
    private int hydrogenosomeGeneticCodeId;
    private int inheritedHGCFlag;

    @Override
    public String toString() {
        return  "TaxonomyNode{" +
                "taxId=" + taxId +
                ", parentTaxId=" + parentTaxId ;
    }

    // Getters and Setters
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public int getParentTaxId() {
        return parentTaxId;
    }

    public void setParentTaxId(int parentTaxId) {
        this.parentTaxId = parentTaxId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getEmblCode() {
        return emblCode;
    }

    public void setEmblCode(String emblCode) {
        this.emblCode = emblCode;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getInheritedDivFlag() {
        return inheritedDivFlag;
    }

    public void setInheritedDivFlag(int inheritedDivFlag) {
        this.inheritedDivFlag = inheritedDivFlag;
    }

    public int getGeneticCodeId() {
        return geneticCodeId;
    }

    public void setGeneticCodeId(int geneticCodeId) {
        this.geneticCodeId = geneticCodeId;
    }

    public int getInheritedGCFlag() {
        return inheritedGCFlag;
    }

    public void setInheritedGCFlag(int inheritedGCFlag) {
        this.inheritedGCFlag = inheritedGCFlag;
    }

    public int getMitochondrialGeneticCodeId() {
        return mitochondrialGeneticCodeId;
    }

    public void setMitochondrialGeneticCodeId(int mitochondrialGeneticCodeId) {
        this.mitochondrialGeneticCodeId = mitochondrialGeneticCodeId;
    }

    public int getInheritedMGCFlag() {
        return inheritedMGCFlag;
    }

    public void setInheritedMGCFlag(int inheritedMGCFlag) {
        this.inheritedMGCFlag = inheritedMGCFlag;
    }

    public int getGenBankHiddenFlag() {
        return genBankHiddenFlag;
    }

    public void setGenBankHiddenFlag(int genBankHiddenFlag) {
        this.genBankHiddenFlag = genBankHiddenFlag;
    }

    public int getHiddenSubtreeRootFlag() {
        return hiddenSubtreeRootFlag;
    }

    public void setHiddenSubtreeRootFlag(int hiddenSubtreeRootFlag) {
        this.hiddenSubtreeRootFlag = hiddenSubtreeRootFlag;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getPlastidGeneticCodeId() {
        return plastidGeneticCodeId;
    }

    public void setPlastidGeneticCodeId(int plastidGeneticCodeId) {
        this.plastidGeneticCodeId = plastidGeneticCodeId;
    }

    public int getInheritedPGCFlag() {
        return inheritedPGCFlag;
    }

    public void setInheritedPGCFlag(int inheritedPGCFlag) {
        this.inheritedPGCFlag = inheritedPGCFlag;
    }

    public int getSpecifiedSpecies() {
        return specifiedSpecies;
    }

    public void setSpecifiedSpecies(int specifiedSpecies) {
        this.specifiedSpecies = specifiedSpecies;
    }

    public int getHydrogenosomeGeneticCodeId() {
        return hydrogenosomeGeneticCodeId;
    }

    public void setHydrogenosomeGeneticCodeId(int hydrogenosomeGeneticCodeId) {
        this.hydrogenosomeGeneticCodeId = hydrogenosomeGeneticCodeId;
    }

    public int getInheritedHGCFlag() {
        return inheritedHGCFlag;
    }

    public void setInheritedHGCFlag(int inheritedHGCFlag) {
        this.inheritedHGCFlag = inheritedHGCFlag;
    }
}