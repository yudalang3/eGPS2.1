package blast.parse;

import top.signature.IModuleSignature;

/**
 * Module signature implementation for the BLAST parser utilities package.
 *
 * Provides metadata about the BLAST parser module for the eGPS system.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class ZzzModuleSignature implements IModuleSignature {
    /**
     * Returns a short description of the module functionality
     * 
     * @return description of the BLAST parser module
     */
    @Override
    public String getShortDescription() {
        return "Convenient tools for blast results parser";
    }

    /**
     * Returns the tab name for this module in the user interface
     * 
     * @return tab name for the BLAST parser module
     */
    @Override
    public String getTabName() {
        return "Blast Parser";
    }

}
