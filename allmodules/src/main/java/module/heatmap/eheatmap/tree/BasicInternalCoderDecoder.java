package module.heatmap.eheatmap.tree;


import evoltree.struct.io.AbstractNodeCoderDecoder;

public class BasicInternalCoderDecoder<T extends GraphcsNode> extends AbstractNodeCoderDecoder<T> {

	@Override
	public void parseNode(GraphcsNode node, String str) {
		if (str.length() > 0) {
			String[] split = str.split(":", -1);
			node.setName(split[0]);
			if (split.length > 1) {
				node.setLength(Double.parseDouble(split[1]));
			}
		}

	}

	@Override
	public String codeNode(GraphcsNode node) {
		String name = node.getName();
		
		if (name == null) {
			return ":" + Double.toString(node.getLength());
		} else {
			return name + ":" + Double.toString(node.getLength());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		return (T) new GraphcsNode(1);
	}


}
