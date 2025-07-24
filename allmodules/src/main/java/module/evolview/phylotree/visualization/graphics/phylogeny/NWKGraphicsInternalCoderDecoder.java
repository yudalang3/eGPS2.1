package module.evolview.phylotree.visualization.graphics.phylogeny;

import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import evoltree.struct.io.AbstractNodeCoderDecoder;
import utils.string.EGPSStringUtil;

import java.util.InputMismatchException;

/**
 * 内节点可以有三个属性。
 * 
 * <pre>
 * 我们这里定义，如果三个属性都有，那么输出为 名字:bootstrap:枝长 
 * 如果只有一个枝长，那么 :枝长 
 * 如果有两个值有可能是
 * 名字:枝长 或者 bootstrap:枝长
 * </pre>
 * 
 * 所以一定至少有两个值。
 */
public class NWKGraphicsInternalCoderDecoder<T extends DefaultGraphicsPhyloNode> extends AbstractNodeCoderDecoder<T> {

	private StringBuilder sBuilder = new StringBuilder();

	@Override
	public void parseNode(T node, String str) {

		if (str.isEmpty()) {
			return;
		}
		String[] split = EGPSStringUtil.split(str, ':');

		if (split.length < 2) {
			throw new InputMismatchException("Sorry, please check your nwk format: ".concat(str));
		}

		if (split.length == 3) {
			node.setName(split[0]);
			node.setBootstrapValue(Double.parseDouble(split[1]));
			node.setLength(Double.parseDouble(split[2]));
		}

		// has bootstrap
		String firstStr = split[0];
		Double tryParse = Doubles.tryParse(firstStr);
		if (tryParse == null) {
			node.setName(firstStr);
		} else {
			node.setBootstrapValue(tryParse);
		}

		node.setLength(Double.parseDouble(split[1]));

	}

	@Override
	public String codeNode(T node) {

		sBuilder.setLength(0);

		if (!Strings.isNullOrEmpty(node.getName())) {
			sBuilder.append(node.getName());
		}

		if (node.getBootstrapValue() != 0) {
			if (sBuilder.length() > 0) {
				sBuilder.append(COLON);
			}
			sBuilder.append(Double.toString(node.getLength()));
		}

		sBuilder.append(COLON);
		sBuilder.append(convertRate2Decimal(node.getLength()));

		return sBuilder.toString();
	}

	@Override
	public T createNode() {
		DefaultGraphicsPhyloNode defaultPhyNode = new DefaultGraphicsPhyloNode();
		return (T) defaultPhyNode;
	}

}
