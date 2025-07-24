package pfam.parse;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for hmmer and hmmer extension tools results parser";
    }

    @Override
    public String getTabName() {
        return "Hmmer Parser";
    }

}
