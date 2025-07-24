package cli.tools;

import top.signature.IModuleSignature;

/**
 * Module signature implementation for the command line utilities package.
 *
 * Provides metadata about the CLI utilities module for the eGPS system.
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
     * @return description of the CLI utilities module
     */
    @Override
    public String getShortDescription() {
        return "Convenient tools for command line operating utilities: List/Count/Clipboard ......";
    }

    /**
     * Returns the tab name for this module in the user interface
     * 
     * @return tab name for the CLI utilities module
     */
    @Override
    public String getTabName() {
        return "CMD Utility";
    }

}
