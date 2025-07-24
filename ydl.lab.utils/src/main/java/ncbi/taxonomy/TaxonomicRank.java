package ncbi.taxonomy;

public class TaxonomicRank {
    private int taxId;           // 节点ID (tax_id)
    private String taxName;      // 生物体的学名 (tax_name)
    private String species;      // 物种名称 (species)
    private String genus;        // 属名称 (genus)
    private String family;       // 科名称 (family)
    private String order;        // 目名称 (order)
    private String clazz;        // 纲名称 (class) - 使用 clazz 避免与关键字冲突
    private String phylum;       // 门名称 (phylum)
    private String kingdom;      // 界名称 (kingdom)
    private String superkingdom; // 超界名称 (superkingdom)

    // 无参构造函数
    public TaxonomicRank() {
    }

    // 带参构造函数
    public TaxonomicRank(int taxId, String taxName, String species, String genus, String family, String order, String clazz, String phylum, String kingdom, String superkingdom) {
        this.taxId = taxId;
        this.taxName = taxName;
        this.species = species;
        this.genus = genus;
        this.family = family;
        this.order = order;
        this.clazz = clazz;
        this.phylum = phylum;
        this.kingdom = kingdom;
        this.superkingdom = superkingdom;
    }

    // Getter 和 Setter 方法
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getPhylum() {
        return phylum;
    }

    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getSuperkingdom() {
        return superkingdom;
    }

    public void setSuperkingdom(String superkingdom) {
        this.superkingdom = superkingdom;
    }

    // toString 方法，用于打印对象信息
    @Override
    public String toString() {
        return "TaxonomicRank{" +
                "taxId=" + taxId +
                ", taxName='" + taxName + '\'' +
                ", species='" + species + '\'' +
                ", genus='" + genus + '\'' +
                ", family='" + family + '\'' +
                ", order='" + order + '\'' +
                ", clazz='" + clazz + '\'' +
                ", phylum='" + phylum + '\'' +
                ", kingdom='" + kingdom + '\'' +
                ", superkingdom='" + superkingdom + '\'' +
                '}';
    }
}