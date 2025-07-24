package primary.struct.display.data;

import com.google.common.collect.Lists;

import java.util.List;

public class GeneV2 {
    private String geneName;
    private int length;
    private List<DomainV2> domains = Lists.newArrayList();


    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public List<DomainV2> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainV2> domains) {
        this.domains = domains;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
