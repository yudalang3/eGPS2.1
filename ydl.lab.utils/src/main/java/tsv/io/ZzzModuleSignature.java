package tsv.io;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for quick operate tsv format files";
    }

    @Override
    public String getTabName() {
        return "TSVFile Parser";
    }

}
