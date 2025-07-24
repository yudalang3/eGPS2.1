package module.evolview.phylotree.visualization;

import top.signature.IModuleSignature;

/**
 * basic.swing is for the quick and convenient way to visualize the phylogenetic tree.
 * large.interactive is designed for scripted-Java developers
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Visualize the phylogenetic tree with the convenient Java scripts, not only graphics but also for the texts.";
    }

    @Override
    public String getTabName() {
        return "phylogenetic tree visualization on 20250317, this is for the more general tree display data structure.";
    }
}
