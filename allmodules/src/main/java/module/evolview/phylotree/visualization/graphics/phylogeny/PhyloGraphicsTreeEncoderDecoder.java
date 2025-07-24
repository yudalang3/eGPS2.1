package module.evolview.phylotree.visualization.graphics.phylogeny;

import evoltree.struct.io.BasicLeafCoderDecoder;
import evoltree.struct.TreeCoder;
import evoltree.struct.io.PrimaryNodeTreeDecoder;

public class PhyloGraphicsTreeEncoderDecoder {
    class NWKGraphicsLeafCoderDecoder<T extends DefaultGraphicsPhyloNode> extends BasicLeafCoderDecoder<T> {


        @SuppressWarnings("unchecked")
        @Override
        public T createNode() {
            return (T) new DefaultGraphicsPhyloNode();
        }
    }

    public String encode(DefaultGraphicsPhyloNode tree) {
        TreeCoder<DefaultGraphicsPhyloNode> coder = new TreeCoder<>(new NWKGraphicsInternalCoderDecoder<>(), new NWKGraphicsLeafCoderDecoder<>());
        return coder.code(tree);
    }

    public DefaultGraphicsPhyloNode decode(String line) throws Exception {
        PrimaryNodeTreeDecoder<DefaultGraphicsPhyloNode> decoder = new PrimaryNodeTreeDecoder<>(new NWKGraphicsInternalCoderDecoder<>(), new NWKGraphicsLeafCoderDecoder<>());
        return decoder.decode(line);
    }
}


