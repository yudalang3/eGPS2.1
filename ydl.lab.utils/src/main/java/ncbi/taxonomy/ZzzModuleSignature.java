package ncbi.taxonomy;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for Parsing NCBI Taxon";
    }

    @Override
    public String getTabName() {
        return "NCBI Taxon Parser";
    }

}
