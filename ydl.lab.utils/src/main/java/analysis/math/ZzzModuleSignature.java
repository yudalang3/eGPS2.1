package analysis.math;

import top.signature.IModuleSignature;

/**
 * Module signature implementation for the math utilities package.
 *
 * Provides metadata about the math utilities module for the eGPS system.
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
     * @return description of the math utilities module
     */
    @Override
    public String getShortDescription() {
        return "Math utility for Double List, Random generator...";
    }

    /**
     * Returns the tab name for this module in the user interface
     * 
     * @return tab name for the math utilities module
     */
    @Override
    public String getTabName() {
        return "Math Utility";
    }

}
