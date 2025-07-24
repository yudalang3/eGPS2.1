package module.evolview.gfamily.work;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.phylogeny.DefaultPhyNode;
import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;
import module.evolview.gfamily.work.model.tree.GraphicsNode;

public class NodeUtils {
	public static GraphicsNode convertBasicNode2graphicsNode(EvolNode node) {
		Function<EvolNode, GraphicsNode> generateNewRoot = inputNode -> {
			GraphicsNode newroot = new GraphicsNode();
			newroot.setID(inputNode.getID());

			newroot.setName(inputNode.getName());
			newroot.setLength(inputNode.getLength());
			newroot.setRealBranchLength(inputNode.getLength());

			newroot.setSize(inputNode.getSize());
			return newroot;
		};

		GraphicsNode convertNode = EvolNodeUtil.convertNode(node, generateNewRoot);

		return convertNode;
	}

	/**
	 * Format is : intutiveTree(itp)
	 * 
	 * <pre>
	 * # header line
	 * # tab element separator
	 * # , is the node separator
	 * # : is the feature separator like nwk format
	 * # parent children attachment
	 * a	b,c
	 * b	d,e
	 * c	f,g
	 * d	
	 * e
	 * f
	 * g
	 * # another example
	 * a:1	b:2,c:3
	 * b	d:1,e:3
	 * c	f:1,g:3
	 * # The parent node properties can be omitted
	 * </pre>
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static EvolNode parseTableTree(String path) throws IOException {
		List<String> readLines = FileUtils.readLines(new File(path), StandardCharsets.UTF_8);
		return parseTableTree(readLines);

	}

	public static EvolNode parseTableTree(List<String> readLines) throws IOException {
		HashMap<String, ArrayBasedNode> name2nodeMap = new HashMap<>();
		ArrayBasedNode rootNode = null;

		for (String string : readLines) {
			String[] split = string.split("\t", -1);

			ArrayBasedNode parent = parseOneNode(split[0]);
			{
				ArrayBasedNode existNode = name2nodeMap.get(parent.getName());
				if (existNode == null) {
					name2nodeMap.put(parent.getName(), existNode);
				} else {
					parent = existNode;
				}
			}

			// 第一个节点就是根
			if (rootNode == null) {
				rootNode = parent;
			}

			String childrenStr = split[1];
			if (childrenStr.isEmpty()) {
				continue;
			}
			String[] split2 = childrenStr.split(",", -1);
			for (String string2 : split2) {
				ArrayBasedNode child = parseOneNode(string2);

				name2nodeMap.put(child.getName(), child);

				parent.addChild(child);
			}

		}

		return rootNode;

	}
	
	public static String codeTableTree(EvolNode root) {
		
		// 这里没有check，因为传入的root肯定不可能是一个节点
		StringBuilder sBuilder = new StringBuilder();
		codeWorker(root,sBuilder);
		return sBuilder.toString();
	}

	/**
	 * 这个方法适合以Table读进去的树，因为它通过getName来定位名字
	 * @param node
	 * @param sBuilder
	 */
	private static void codeWorker(EvolNode node, StringBuilder sBuilder) {
		int childCount = node.getChildCount();
		
		if (childCount == 0) {
			return;
		}
		
		sBuilder.append(node.getName());
		sBuilder.append(':');
		sBuilder.append(node.getLength());
		sBuilder.append("\t");
		
		EvolNode firstChild = node.getFirstChild();
		sBuilder.append(firstChild.getName());
		sBuilder.append(':');
		sBuilder.append(firstChild.getLength());
		for (int i = 1; i < childCount; i++) {
			EvolNode child = node.getChildAt(i);
			
			sBuilder.append(',');
			sBuilder.append(child.getName());
			sBuilder.append(':');
			sBuilder.append(child.getLength());
		}
		sBuilder.append("\n");
		
		for (int i = 0; i < childCount; i++) {
			EvolNode child = node.getChildAt(i);
			codeWorker(child, sBuilder);
		}
	}

	private static ArrayBasedNode parseOneNode(String str) {
		DefaultPhyNode node = new DefaultPhyNode();
		if (str.length() > 0) {
			String[] split = str.split(":", -1);
			node.setName(split[0]);
			if (split.length > 1) {
				node.setLength(Double.parseDouble(split[1]));
			} else {
				node.setLength(1);
			}
		} else {
			throw new IllegalArgumentException("The str is length 0.");
		}

		return node;

	}

}
