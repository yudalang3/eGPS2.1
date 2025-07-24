package analysis;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Run helper for analysis script";
    }

    @Override
    public String getTabName() {
        return "Executing Helper";
    }

}
