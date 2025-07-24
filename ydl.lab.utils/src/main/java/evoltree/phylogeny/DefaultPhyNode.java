package evoltree.phylogeny;

import evoltree.struct.ArrayBasedNode;

/**
 * Represents a default implementation of a phylogenetic tree node.
 *
 * This class extends ArrayBasedNode and adds support for storing bootstrap values
 * commonly used in phylogenetic tree analysis.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class DefaultPhyNode extends ArrayBasedNode {
	
	protected double bootstrapValue;
	
	
    /**
     * Gets the bootstrap value of the node.
     * 
     * @return the bootstrap value
     */
    public double getBootstrapValue() {
		return bootstrapValue;
	}
	
    /**
     * Sets the bootstrap value of the node.
     * 
     * @param bootstrapValue the bootstrap value to set
     */
    public void setBootstrapValue(double bootstrapValue) {
		this.bootstrapValue = bootstrapValue;
	}

}
