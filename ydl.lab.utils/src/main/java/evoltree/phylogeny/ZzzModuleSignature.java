package evoltree.phylogeny;

import top.signature.IModuleSignature;

/**
 * This remnant should none business with the graphics remnant
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for the NWK file format parsing infrastructure extensions";
    }

    @Override
    public String getTabName() {
        return "Nwk Parser";
    }
}
