package module.remnant.treeoperator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 */
public class ETreeDecoder {
	
	protected BasicNode[] nodes;
	protected BasicBranch[] branchs;
	private boolean bsOnly = false;
	
	private int globalIndex = 0;
	
	private HashMap<String, String> translationMaps = null;

	/** Creates new TreeReader */
	public ETreeDecoder() {}

	/**
	 * Note only read first line!
	 * @param file
	 * @return
	 */
	public BasicNode loadTreeFromPhylogeneticFile(File file) {
		GNode ret = null;
		
		String treeInforString = null;
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) {
					continue;
				}
				if (line.length()>0) {
					treeInforString = line;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (treeInforString.indexOf("xml") != -1 && treeInforString.indexOf("ItreeV") != -1) {
			throw new UnsupportedOperationException();
		}else if (treeInforString.indexOf("#NEXUS") != -1) {
			List<GNode> returnNode = new ArrayList<GNode>();
			translationMaps = new HashMap<String, String>();
			parseNexusTree(treeInforString, returnNode);
			ret = returnNode.get(0);
		}else if (treeInforString.indexOf("NHX") != -1) {
			ret = parseNHXTree(treeInforString);
		}else {
			// parse newick tree
			ret = parseNewickTree(treeInforString);
		}

		return (BasicNode) ret;
	}
	
	/**
	* A function parse a string like example to create a Node.
	* @author yudalang
	* @date 2018-7-26
	* @param oneNode is a String like example
	 */
	protected GNode createNode(String oneNode) {
		String speciesName = null;
		BasicNode node = new BasicNode();
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
						node.setBootstrapValue(Double.parseDouble(bsStr));
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
								node.setBootstrapValue(Double.parseDouble(str));
							}
							/*
							 * int start = bsStr.indexOf("["); int end = bsStr.indexOf("]");
							 * node.setBs(Integer.parseInt(bsStr.substring(start + 1, end)));
							 */
						} else {
							node.setBootstrapValue(Double.parseDouble(bsStr));
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
			node.setOrignalOrderInMatrix(globalIndex++);
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
			if (oneNode.contains("[")) { // deal with bootstrap value like this: (A:1.2,B:2.3):6.9[98]
				oneNode = bootstrapBehind(oneNode, node);
			}
			StringTokenizer tokenizer = new StringTokenizer(oneNode);
			if (tokenizer.hasMoreTokens()) {
				String nextToken = tokenizer.nextToken();

				double len = getDoubleValue(nextToken);
				if (len >= 0.0) {
					node.getBranch(0).setLength(len);
				} else {
					node.getBranch(0).setLength(0.0);
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
	private String bootstrapBehind(String str, GNode node) {
		
		int start = str.indexOf("[");
		int end = str.indexOf("]");
		node.setBootstrapValue(Double.parseDouble(str.substring(start + 1, end)));
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
	public GNode decode(String line) {
		
		// Remove ; symbol in the nwk-like String
		// line = line.replace(";", "");
		int lenMiunsOne = line.length() - 1;
		char lastChar = line.charAt(lenMiunsOne);
		if (lastChar == ';') {
			line = line.substring(0, lenMiunsOne);
		}
		
		GNode root = createNode(line);
		nextLevel(root, line);
		return root;
	}


	private boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		// System.out.println("pattern.matcher(str).matches()1 ="
		// +pattern.matcher(str).matches());
		return pattern.matcher(str).matches();
	}

	protected void nextLevel(GNode parent, String parentString) {
		int beginIndex = 1;
		while (true) {
			int endOfNode = endOfNode(parentString, beginIndex);
			String childString = parentString.substring(beginIndex, endOfNode);
			GNode child = createNode(childString);
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
		try {
			BigDecimal bd = new BigDecimal(word);
			return bd.doubleValue();
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Wrong digit format for " + word);
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

	/**
	 * parse the newick format tree
	 * 
	 * @param newickTree ((raccoon:19.19959,bear:6.80041)50:0.84600,((sea_lion:11.99700,
	 *                   seal:12.00300)100:7.52973,((monkey:100.85930,cat:47.14069)80:20.59201,
	 *                   weasel:18.87953)75:2.09460)50:3.87382,dog:25.46154);
	 * @return
	 */
	private GNode parseNewickTree(String treeStr) {
		globalIndex = 0;
		return decode(treeStr);
	}
	
	private String removeNexusComments(String s) {
		Pattern commentFinder = createPattern("(\\[.*?\\])");
		Matcher m = commentFinder.matcher(s);
		return m.replaceAll("");
	}

	private Pattern createPattern(String pattern) {
		return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	}
	
	private GNode parseNHXTree(String treeStr) {
		Pattern bracketFinder = createPattern("(\\[.*?\\])");
		Matcher m = bracketFinder.matcher(treeStr);
		while (m.find()) {
			int index = 0;
			String s = m.group(1);
			if ((index = s.indexOf(":B=")) != -1) {
				String bs = null;
				int bracketEnd = s.indexOf("]", index + 1);
				int commaEnd = s.indexOf(":", index + 1);
				if (commaEnd < 0) {
					bs = s.substring(index + 3, bracketEnd);
				} else if (commaEnd < bracketEnd) {
					bs = s.substring(index + 3, commaEnd);
				}
				treeStr = treeStr.replace(s, "[" + bs + "]");
			} else {
				treeStr = treeStr.replace(s, "");
			}

		}
		return decode(treeStr);
	}
	
	
	private void parseNexusTree(String treeStr, List<GNode> returnNode) {
		String tree = null;
		// remove comments
		tree = removeNexusComments(treeStr);
		tree = getTreesBlockFromNexus(tree);

		getTranslationMap(tree);

		List<String> treeBs = getTreeFromTreesBlock(tree);
		for (int i = 0; i < treeBs.size(); i++) {
			tree = treeBs.get(i);

			tree = tree.substring(tree.indexOf("("));
			tree = replaceBlank(tree);
			returnNode.add( decode(tree, translationMaps) );
			break;
		}
	}
	
	/**
	 * The main function for us to use!
	 * 
	* @author yudalang-annotated 
	* @date 2018-7-23
	* @return Node instance
	 */
	public GNode decode(String line, HashMap<String, String> translationMaps) {
		
		// Remove ; symbol in the nwk-like String
		// line = line.replace(";", "");
		int lenMiunsOne = line.length() - 1;
		char lastChar = line.charAt(lenMiunsOne);
		if (lastChar == ';') {
			line = line.substring(0, lenMiunsOne);
		}
		
		GNode root = null;
		if ((translationMaps == null) || (translationMaps.size() == 0)) {
			root = createNode(line);
			nextLevel(root, line);
		} else {
			root = createNode(line, translationMaps);
			nextLevel(root, line, translationMaps);
		}
		return root;
	}
	
	protected void nextLevel(GNode parent, String parentString, HashMap<String, String> translationMaps) {
		int beginIndex = 1;
		while (true) {
			int endOfNode = endOfNode(parentString, beginIndex);
			String childString = parentString.substring(beginIndex, endOfNode);
			GNode child = createNode(childString, translationMaps);
			parent.addChild(child);
			if (childString.charAt(0) == '(')
				nextLevel(child, childString, translationMaps);

			beginIndex = endOfNode + 1;
			if (parentString.charAt(endOfNode) == ')')
				break;
		}
	}
	
	
	protected GNode createNode(String oneNode, HashMap<String, String> translationMaps) {
		GNode node = new BasicNode();
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
						node.setBootstrapValue(Double.parseDouble(bsStr));
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
								node.setBootstrapValue(Double.parseDouble(str));
							}
						} else {
							node.setBootstrapValue(Double.parseDouble(bsStr));
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
			if (oneNode.contains("[")) { // deal with bootstrap value like this: (A:1.2,B:2.3):6.9[98]
				oneNode = bootstrapBehind(oneNode, node);
			}
			StringTokenizer tokenizer = new StringTokenizer(oneNode);
			if (tokenizer.hasMoreTokens()) {

				double length = getDoubleValue(tokenizer.nextToken());
				if (length >= 0.0) {
					node.getBranch(0).setLength(length);
				} else {
					node.getBranch(0).setLength(0.0);
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
	
	/**
	 * From ... to ... "fa f fe fe\tiii" --> "faffefeiii"
	 */
	private String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	private List<String> getTreeFromTreesBlock(String treesBlock) {
		List<String> treeBlocks = new ArrayList<String>(7);
		Pattern p = createPattern("tree(.*?);");
		Matcher m = p.matcher(treesBlock);
		while (m.find()) {
			treeBlocks.add(m.group());
		}
		return treeBlocks;
	}
	
	private String getTreesBlockFromNexus(String s) {
		return matchGroup(s, "begin trees;(.*)end;", 1);
	}
	
	private void getTranslationMap(String treesBlock) {
		String trans = getTranslateBlock(treesBlock);
		if ((trans != null) && (trans.length() > 0)) {
			String[] pairs = trans.split(",");
			int size = pairs.length;
			String pair = null;
			for (int i = 0; i < size; i++) {
				pair = pairs[i].trim();
				if (pair.length() < 1)
					continue;
				String[] keyValue = pair.split("[\\s]+");
				String key = keyValue[0].trim();
				String value = keyValue[1].trim();
				translationMaps.put(key, value);
			}
		}
	}
	private String matchGroup(String s, String pattern, int groupNumber) {
		Pattern p = createPattern(pattern);
		Matcher m = p.matcher(s);
		if (m.find()) {
			return m.group(groupNumber);
		} else {
			return null;
		}
	}
	
	private String getTranslateBlock(String s) {
		return matchGroup(s, "translate(.*?);", 1);
	}
	
	public static void main(String[] args) {
		 ETreeDecoder decoder = new ETreeDecoder();
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
		GNode root = decoder.decode("(((a:8.5,b:8.5):2.5,e:11.0):5.5,(c:14.0,d:14.0):2.5):0.0;");
		
		System.out.println(new ETreeCoder().code(root));
	}
}
