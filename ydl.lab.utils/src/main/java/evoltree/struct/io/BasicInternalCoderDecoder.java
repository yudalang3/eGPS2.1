package evoltree.struct.io;

import evoltree.struct.ArrayBasedNode;

public class BasicInternalCoderDecoder<T extends ArrayBasedNode> extends AbstractNodeCoderDecoder<T> {

	@Override
	public void parseNode(T node, String str) {
		if (str.length() > 0) {
			String[] split = str.split(":", -1);
			node.setName(split[0]);
			if (split.length > 1) {
				node.setLength(Double.parseDouble(split[1]));
			}
		}

	}

	@Override
	public String codeNode(T node) {
		String name = node.getName();
		
		if (name == null) {
			return ":" + node.getLength();
		} else {
			return name + ":" + node.getLength();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		return (T) new ArrayBasedNode();
	}

}
