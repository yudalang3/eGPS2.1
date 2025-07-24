package fasta.io;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for the fasta I/O, most case for reading.";
    }

    @Override
    public String getTabName() {
        return "Fasta File IO";
    }

}
