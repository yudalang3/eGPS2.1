package rest;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for making web query to the ensembl rest service.";
    }

    @Override
    public String getTabName() {
        return "EnsemblREST Request";
    }

}
