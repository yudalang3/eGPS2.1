package module.remnant.treeoperator.io;


import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import module.remnant.treeoperator.DefaultBranch;
import module.remnant.treeoperator.DefaultNode;
import module.remnant.treeoperator.NodeEGPSv1;

//import com.ibm.math.*;   // import BigDecimal and MathContext

/**
 * 
* <p>Title: TreeDecoder</p>  
* <p>Description: annotated by yudalang
* 	 Purpose: Parse Strings( tree information or tree-format file) to Node instances.
*    Note: this is only for the nwk format tree!!!
* </p>  
* <p>Example:  
* ((7:0.1582:1,(3:0.08635:2,(((1:0.00755:0,5:0.00755:0):2.0E-4:0,10:0.00775:0):0.00115:0,(4:0.0077:0,(8:0.00695:0,6:0.00695:0):7.5E-4:0):0.0012:0):0.07745:0):0.07185:0):0.2401:1,(9:0.0634:0,2:0.0634:2):0.3349:5):0.0:0
* Added by GF: ((raccoon:19.19959,bear:6.80041)50:0.84600,((sea_lion:11.99700,
* seal:12.00300)100:7.52973,((monkey:100.85930,cat:47.14069)80:20.59201,
* weasel:18.87953)75:2.09460)50:3.87382,dog:25.46154);");
* </p>
* 
* @author haipeng,yudalang 
* @date 2018-7-23
* @date 2024-04-30 yudalang
 */
public class TreeDecoder {
	
	protected DefaultNode[] nodes;
	protected DefaultBranch[] branchs;
	private boolean bsOnly = false;

	/** Creates new TreeReader */
	public TreeDecoder() {}

	/**
	* ElegantJTable function parse a string like example to create a Node.
	* @author yudalang
	* @date 2018-7-26
	* @param oneNode is a String like example
	 */
	protected NodeEGPSv1 createNode(String oneNode) {
		String speciesName = null;
		NodeEGPSv1 node = new DefaultNode();
		//System.out.println(getClass()+"\t"+node.getNameID());
		char firstCh = oneNode.charAt(0);
		String name = null;
		
		if (firstCh == '(') {// String not just a leaf!
			int nextRelatedBracket = nextRelatedBracket(oneNode, 0);
			if (nextRelatedBracket + 1 < oneNode.length()) {
				// bs means bootstrap value
				int bsCommaIndex = oneNode.indexOf(":", nextRelatedBracket);
				if (bsCommaIndex != -1) {
					
					String bsStr = oneNode.substring(nextRelatedBracket + 1, bsCommaIndex);
					if (bsStr.length() != 0) { // bootstrap value is not null
						node.setBs(Integer.parseInt(bsStr));
					}

					oneNode = oneNode.substring(bsCommaIndex + 1);
				} else { // deal with only branch length or bootstrap value
					
					String bsStr = oneNode.substring(nextRelatedBracket + 1, oneNode.length()).trim();
					if (bsStr.length() != 0) { // deal with only bootstrap value
						// "[value]" or directly "value"
						if (bsStr.contains("[")) {
							int start = bsStr.indexOf("[");
							int end = bsStr.indexOf("]");
							String str = bsStr.substring(start + 1, end);
							if (isDouble(str)) {
								node.setBs(Integer.parseInt(str));
							}
							/*
							 * int start = bsStr.indexOf("["); int end = bsStr.indexOf("]");
							 * node.setBs(Integer.parseInt(bsStr.substring(start + 1, end)));
							 */
						} else {
							node.setBs(Integer.parseInt(bsStr));
						}
						bsOnly = true;
					} else {
						// deal with only branch length
						oneNode = oneNode.substring(nextRelatedBracket + 2); // Only the length and mutation information
																				// left.
					}
				}

			} else {
				// yudalang: because we will recursively call the createNode function
				oneNode = "";
			}

		} else { // That's a leaf, with a name or without name
			int semicolonIndex = oneNode.indexOf(":");
			String namePart = oneNode;
			// if ";" match the namePart
			if (semicolonIndex != -1)
				namePart = oneNode.substring(0, semicolonIndex);

			namePart = namePart.trim();
			if (namePart.length() > 0)
				name = namePart;
			if (semicolonIndex == -1) {
				oneNode = "";
			} else {
				oneNode = oneNode.substring(oneNode.indexOf(":") + 1); // Only the length and mutation information left.
			}
		}
		if (oneNode.contains("&&NHX")) {
			int indexOf = oneNode.indexOf(":", oneNode.indexOf(":S=") + 3);
			if (indexOf != -1) {
				speciesName = oneNode.substring(oneNode.indexOf(":S=") + 3, indexOf);
			} else {
				speciesName = oneNode.substring(oneNode.indexOf(":S=") + 3, oneNode.indexOf("]"));
			}
			oneNode = oneNode.replace(oneNode.substring(oneNode.indexOf("[")), "");
		}

		oneNode = oneNode.replace(':', ' ');

		if (!bsOnly) {
			if (oneNode.contains("[")) { // deal with bootstrap value like this: (ElegantJTable:1.2,B:2.3):6.9[98]
				oneNode = bootstrapBehind(oneNode, node);
			}
			StringTokenizer tokenizer = new StringTokenizer(oneNode);
			if (tokenizer.hasMoreTokens()) {
				String nextToken = tokenizer.nextToken();

				double len = getDoubleValue(nextToken);
				if (len >= 0.0) {
					node.getBranch().setLength(len);
				} else {
					node.getBranch().setLength(0.0);
				}

			}
		}

		if (name != null) {
			if (speciesName != null) {
				node.setLeafName(speciesName + " " + name); // for only leaf
			} else {
				node.setLeafName(name); // for only leaf
			}
		} else {
			if (speciesName != null) {
				node.setLeafName(speciesName); // for only leaf
			}
		}
		return node;
	}
	/**
	 * Give a string like "xxxxx[bootstrapValue]", set node' bs and return "xxxxx"; 
	* @author yudalang
	* @date 2018-7-26
	 */
	private String bootstrapBehind(String str, NodeEGPSv1 node) {
		
		int start = str.indexOf("[");
		int end = str.indexOf("]");
		String substring = str.substring(start + 1, end);
		node.setBs(Double.parseDouble(substring));
		// node.setLeafName("human");
		str = str.substring(0, start);
		return str;
	}

	/**
	 * The main function for us to use!
	 * 
	* @author yudalang-annotated 
	* @date 2018-7-23
	* @return Node instance
	 */
	public NodeEGPSv1 decode(String line, HashMap<String, String> translationMaps) {
		
		// Remove ; symbol in the nwk-like String
		// line = line.replace(";", "");
		int lenMiunsOne = line.length() - 1;
		char lastChar = line.charAt(lenMiunsOne);
		if (lastChar == ';') {
			line = line.substring(0, lenMiunsOne);
		}
		
		NodeEGPSv1 root = null;
		if ((translationMaps == null) || (translationMaps.size() == 0)) {
			root = createNode(line);
			nextLevel(root, line);
		} else {
			root = createNode(line, translationMaps);
			nextLevel(root, line, translationMaps);
		}
		return root;
	}

	protected NodeEGPSv1 createNode(String oneNode, HashMap<String, String> translationMaps) {
		NodeEGPSv1 node = new DefaultNode();
		String  speciesName = null;
		char firstCh = oneNode.charAt(0);
		String name = null;
		if (firstCh == '(') {
			int nextRelatedBracket = nextRelatedBracket(oneNode, 0);
			if (nextRelatedBracket + 1 < oneNode.length()) {
				int bsCommaIndex = oneNode.indexOf(":", nextRelatedBracket);
				if (bsCommaIndex != -1) {
					String bsStr = oneNode.substring(nextRelatedBracket + 1, bsCommaIndex);
					if (bsStr.length() != 0) { // bootstrap value is not null
						node.setBs(Integer.parseInt(bsStr));
					}
					oneNode = oneNode.substring(bsCommaIndex + 1);
				} else { // deal with only branch length or bootstrap value
					String bsStr = oneNode.substring(nextRelatedBracket + 1, oneNode.length()).trim();
					if (bsStr.length() != 0) { // deal with only bootstrap value
						if (bsStr.contains("[")) {

							int start = bsStr.indexOf("[");
							int end = bsStr.indexOf("]");
							String str = bsStr.substring(start + 1, end);
							if (isDouble(str)) {

								node.setBs(Integer.parseInt(str));
							}
						} else {
							node.setBs(Integer.parseInt(bsStr));
						}
						bsOnly = true;
					} else {
						// deal with only branch length
						oneNode = oneNode.substring(nextRelatedBracket + 2); // Only the length and mutation information
																				// left.
					}
				}
			} else {
				oneNode = "";
			}
		} else { // That's a leaf, with a name or without name
			int index = oneNode.indexOf(":");
			String namePart = oneNode;
			if (index != -1)
				namePart = oneNode.substring(0, oneNode.indexOf(":"));

			namePart = namePart.trim();
			if (namePart.length() > 0)
				name = namePart;
			if (index == -1) {
				oneNode = "";
			} else {
				oneNode = oneNode.substring(oneNode.indexOf(":") + 1); // Only the length and mutation information left.
			}
		}

		if (oneNode.contains("&&NHX")) {
			int indexOf = oneNode.indexOf(":", oneNode.indexOf(":S=") + 3);
			 if (indexOf != -1) {
				 speciesName = oneNode.substring(oneNode.indexOf(":S=") + 3, indexOf);
			} else {
				speciesName = oneNode.substring(oneNode.indexOf(":S=") + 3, oneNode.indexOf("]"));
			}
			oneNode = oneNode.replace(oneNode.substring(oneNode.indexOf("[")), "");
		}

		oneNode = oneNode.replace(':', ' ');

		if (!bsOnly) {
			if (oneNode.contains("[")) { // deal with bootstrap value like this: (ElegantJTable:1.2,B:2.3):6.9[98]
				oneNode = bootstrapBehind(oneNode, node);
			}
			StringTokenizer tokenizer = new StringTokenizer(oneNode);
			if (tokenizer.hasMoreTokens()) {

				double length = getDoubleValue(tokenizer.nextToken());
				if (length >= 0.0) {
					node.getBranch().setLength(length);
				} else {
					node.getBranch().setLength(0.0);
				}
			}
		}

		if (name != null) {
			if (translationMaps.get(name) == null) {
				node.setLeafName(name);
			} else {
				node.setLeafName(translationMaps.get(name));
			}
		}
		return node;
	}

	private boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		// System.out.println("pattern.matcher(str).matches()1 ="
		// +pattern.matcher(str).matches());
		return pattern.matcher(str).matches();
	}

	protected void nextLevel(NodeEGPSv1 parent, String parentString, HashMap<String, String> translationMaps) {
		int beginIndex = 1;
		while (true) {
			int endOfNode = endOfNode(parentString, beginIndex);
			String childString = parentString.substring(beginIndex, endOfNode);
			NodeEGPSv1 child = createNode(childString, translationMaps);
			parent.addChild(child);
			if (childString.charAt(0) == '(')
				nextLevel(child, childString, translationMaps);

			beginIndex = endOfNode + 1;
			if (parentString.charAt(endOfNode) == ')')
				break;
		}
	}

	protected void nextLevel(NodeEGPSv1 parent, String parentString) {
		int beginIndex = 1;
		while (true) {
			int endOfNode = endOfNode(parentString, beginIndex);
			String childString = parentString.substring(beginIndex, endOfNode);
			NodeEGPSv1 child = createNode(childString);
			parent.addChild(child);
			if (childString.charAt(0) == '(')
				nextLevel(child, childString);

			beginIndex = endOfNode + 1;
			if (parentString.charAt(endOfNode) == ')')
				break;
		}
	}

	// The character of returned index should not be included in the node.
	protected int endOfNode(String line, int beginIndex) {
		char beginCh = line.charAt(beginIndex);
		int endOfNode = beginIndex;
		if (beginCh == '(') {
			endOfNode = nextRelatedBracket(line, beginIndex);
		}
		int endOfComma = nextComma(line, endOfNode + 1);
		int endOfEndBracket = nextEndOfBracket(line, endOfNode + 1);
		if (endOfComma == -1 && endOfEndBracket == -1) {
			endOfNode = line.length();
		} else {
			if (endOfComma != -1 && endOfEndBracket == -1) {
				endOfNode = endOfComma;
			} else {
				if (endOfComma == -1 && endOfEndBracket != -1) {
					endOfNode = endOfEndBracket;
				} else { // endOfComma != -1 && endOfEndBracket != -1
					endOfNode = (endOfComma < endOfEndBracket ? endOfComma : endOfEndBracket);
				}
			}
		}
		return endOfNode;
	}

	private double getDoubleValue(String word) throws IllegalArgumentException {
//		try {
//			BigDecimal bd = new BigDecimal(word);
//			return bd.doubleValue();
//		} catch (NumberFormatException e) {
//			throw new IllegalArgumentException("Wrong digit format for " + word);
//		}
		
		return Double.parseDouble(word);
	}

	private int getIntValue(String number) throws IllegalArgumentException {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Wrong digit format for " + number);
		}
	}

	protected int valueCharByBracket(char c) {
		int value = 0;
		if (c == '(') {
			value = 1;
		} else {
			if (c == ')') {
				value = -1;
			}
		}
		return value;
	}

	/**
	 * To get index of the next related bracket. For example: give a String like "(qwert)" , find where is ")".
	* @author yudalang
	* @date 2018-7-26
	 */
	protected int nextRelatedBracket(String line, int beginIndex) throws IllegalArgumentException {
		char beginCh = line.charAt(beginIndex);
		int index = -1;
		if (beginCh == '(') {
			int value = 1; // Wait until it reaches 0.
			for (int i = beginIndex + 1; i < line.length(); i++) {
				value += valueCharByBracket(line.charAt(i));
				if (value == 0) {
					index = i;
					break;
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
		return index;
	}

	protected int nextComma(String line, int beginIndex) {
		return line.indexOf(',', beginIndex);
	}

	protected int nextEndOfBracket(String line, int beginIndex) {
		return line.indexOf(')', beginIndex);
	}

	public static void main(String[] args) {
		 TreeDecoder decoder = new TreeDecoder();
		// Node root =
		// decoder.decode("((7:0.1582:1,(3:0.08635:2,(((1:0.00755:0,5:0.00755:0):2.0E-4:0,10:0.00775:0):0.00115:0,(4:0.0077:0,(8:0.00695:0,6:0.00695:0):7.5E-4:0):0.0012:0):0.07745:0):0.07185:0):0.2401:1,(9:0.0634:0,2:0.0634:2):0.3349:5):0.0:0");
		// Node root =
		// decoder.decode("((((CAS1_PIG:1.0,(CAS1_SHEEP:1.0,CAS1_BOVIN:1.0):1.0):1.0,CAS1_MOUSE:1.0):1.0,CAS1_RABIT:1.0):1.0,CAS1_HUMAN:1.0);");
		// // Node root =
		// decoder.decode("((((CAS1_PIG:1.0,(CAS1_SHEEP,CAS1_BOVIN)),CAS1_MOUSE:0.2:3),CAS1_RABIT),CAS1_HUMAN);");
		// Node root =
		// decoder.decode("((raccoon:19.19959,bear:6.80041)50:0.84600,((sea_lion:11.99700,
		// seal:12.00300)100:7.52973,((monkey:100.85930,cat:47.14069)80:20.59201,
		// weasel:18.87953)75:2.09460)50:3.87382,dog:25.46154);");
		// Node root =
		// decoder.decode("((raccoon:19.19959,bear:6.80041):0.84600,((sea_lion:11.99700,
		// seal:12.00300):7.52973,((monkey:100.85930,cat:47.14069):20.59201,
		// weasel:18.87953):2.09460)50:3.87382,dog:25.46154);");

		// System.out.println(TreeCoder.code(root, false));
		
		// Node root = decoder.decode("((raccoon:19.19959,bear:6.80041):0.84600,((sea_lion:11.99700, seal:12.00300):7.52973,((monkey:100.85930,cat:47.14069):20.59201, weasel:18.87953):2.09460)50:3.87382,dog:25.46154);", null);
		NodeEGPSv1 root = decoder.decode("(((a:8.5,b:8.5):2.5,e:11.0):5.5,(c:14.0,d:14.0):2.5):0.0;", null);
		
		System.out.println(TreeCoder.code(root));
	}
	
}
