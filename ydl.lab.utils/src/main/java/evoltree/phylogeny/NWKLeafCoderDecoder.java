package evoltree.phylogeny;

import evoltree.struct.io.BasicLeafCoderDecoder;

/**
 * 
 *
 * @Description: Same as its super class!
 *
 *
 * @createdDate Sep 25, 2020
 * @lastModifiedDate Sep 25, 2020
 * @author "yudalang"
 */
public class NWKLeafCoderDecoder<T extends DefaultPhyNode> extends BasicLeafCoderDecoder<T> {

	
	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		return (T) new DefaultPhyNode();
	}
}
