package ncbi.taxonomy;

public class TaxonomyName {

    private int taxId;               // the id of node associated with this name
    private String nameTxt;          // name itself
    private String uniqueName;       // the unique variant of this name if name not unique
    private String nameClass;        // (synonym, common name, ...)

    // Default constructor
    public TaxonomyName() {
    }

    // Parameterized constructor
    public TaxonomyName(int taxId, String nameTxt, String uniqueName, String nameClass) {
        this.taxId = taxId;
        this.nameTxt = nameTxt;
        this.uniqueName = uniqueName;
        this.nameClass = nameClass;
    }

    // Getters and Setters
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public String getNameTxt() {
        return nameTxt;
    }

    public void setNameTxt(String nameTxt) {
        this.nameTxt = nameTxt;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    // Override toString() method for better readability
    @Override
    public String toString() {
        return "TaxonomyName{" +
                "taxId=" + taxId +
                ", nameTxt='" + nameTxt + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", nameClass='" + nameClass + '\'' +
                '}';
    }
}