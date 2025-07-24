package fasta.stat;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for fasta statistical summary";
    }

    @Override
    public String getTabName() {
        return "Fasta File Statistics";
    }

}
