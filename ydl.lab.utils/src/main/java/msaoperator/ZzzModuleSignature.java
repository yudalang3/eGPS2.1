package msaoperator;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "The multiple sequence alignment";
    }

    @Override
    public String getTabName() {
        return "MSA Operator";
    }

}
