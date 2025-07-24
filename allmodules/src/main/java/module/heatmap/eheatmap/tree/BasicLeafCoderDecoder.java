package module.heatmap.eheatmap.tree;

import evoltree.struct.io.AbstractNodeCoderDecoder;

public class BasicLeafCoderDecoder<T extends GraphcsNode> extends AbstractNodeCoderDecoder<T> {

	
	@Override
	public void parseNode(T node, String str) {
		// be aware of split!

		if (str.isEmpty()) {
			node.setName(str);
		} else {
			String[] split = str.split(":", -1);
			node.setName(split[0]);
			if (split.length > 1) {
				if (split[1].length() > 0) {
					node.setLength(Double.parseDouble(split[1]));
				}
			}
		}

	}

	@Override
	public String codeNode(T node) {
		return node.getName() + ":" + node.getLength();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		return (T) new GraphcsNode(1);
	}

}
