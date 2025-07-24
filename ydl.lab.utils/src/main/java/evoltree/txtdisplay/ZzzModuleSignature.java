package evoltree.txtdisplay;

import top.signature.IModuleSignature;

public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Text display the phylogenetic tree";
    }

    @Override
    public String getTabName() {
        return "Text Displayer";
    }
}
