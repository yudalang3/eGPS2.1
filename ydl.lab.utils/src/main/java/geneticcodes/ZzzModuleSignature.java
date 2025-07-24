package geneticcodes;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Translate the mRNA into protein sequence";
    }

    @Override
    public String getTabName() {
        return "Genetic Code";
    }

}
