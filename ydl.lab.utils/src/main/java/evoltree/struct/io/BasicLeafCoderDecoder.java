package evoltree.struct.io;

import evoltree.struct.ArrayBasedNode;

public class BasicLeafCoderDecoder<T extends ArrayBasedNode> extends AbstractNodeCoderDecoder<T> {

	private StringBuilder sbuilder = new StringBuilder();

	
	@Override
	public void parseNode(T node, String str) {
		// be aware of split!
		if (str.isEmpty()) {
			node.setName("");
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
		sbuilder.setLength(0);
		sbuilder.append(node.getName());
		sbuilder.append(":");
		sbuilder.append(convertRate2Decimal(node.getLength()));

		return sbuilder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		return (T) new ArrayBasedNode();
	}

}
