package evoltree.struct.io;

import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Stack;

/**
 * Decode the phylogenetic tree by the stack data structure. This algo. can
 * make the tree for one iteration loop. Not integrated into the pipeline, cause
 * the comprehensive tests need more time.
 * <p>
 * Generally speaking the parsing speed can increase double. Means the 10s seconds will decrease to 5s seconds if You use this implementation.
 * compared to the TreeDecoderWithMap.
 *
 * @author yudalang
 * Caution: it is not comprehensive tested, use by test.
 *
 * <p>
 * &#064;date:  2024-12-21
 * &#064;date:  2025-01-20
 *
 * @param <T>
 */
public class PrimaryNodeTreeDecoder<T extends EvolNode> {

	public final static byte LEFT_BRACKET_BYTE = '(';
	public final static byte RIGHT_BRACKET_BYTE = ')';
	public final static byte COMMA_BYTE = ',';
	public final static byte SEMICOLON_BYTE = ';';

	final AbstractNodeCoderDecoder<T> leafNodeParse;
	final AbstractNodeCoderDecoder<T> internalNodeParse;

	@SuppressWarnings("unchecked")
	public PrimaryNodeTreeDecoder() {
		leafNodeParse = (AbstractNodeCoderDecoder<T>) new BasicLeafCoderDecoder<ArrayBasedNode>();
		internalNodeParse = (AbstractNodeCoderDecoder<T>) new BasicInternalCoderDecoder<ArrayBasedNode>();
	}

	/***
	 *
	 * @title TreeDecoderLessMemory
	 * @createdDate 2020-11-22 10:03
	 * @lastModifiedDate 2024-12-18 10:03
	 * @author yudalang
	 *
	 * @param p1          : leaf node parser
	 * @param p2          : internal node parser
	 */
	public PrimaryNodeTreeDecoder(AbstractNodeCoderDecoder<T> p1, AbstractNodeCoderDecoder<T> p2) {
		leafNodeParse = p1;
		internalNodeParse = p2;
	}

	public T decode(Path filename) throws Exception {
		byte[] line;
		try (FileChannel fileChannel = FileChannel.open(filename)) {
			long size = fileChannel.size();
			MappedByteBuffer mappedByteBuffer = fileChannel.map(MapMode.READ_ONLY, 0, size);
			line = new byte[(int) size];
			mappedByteBuffer.get(line);
		}

		return decode(line);
	}
	/**
	 * Parser nwk-like string to a tree!
     */
	public T decode(String line) {
		return decode(line, false);
	}
	public T decode(String line, boolean removeWhitespace) {
		String ret;
		if (removeWhitespace) {
			StringBuilder buffer = new StringBuilder();
			int length = line.length();
			for (int i = 0; i < length; i++) {
				char c = line.charAt(i);
				if (!Character.isWhitespace(c)) {
					buffer.append(c);
				}
			}
			ret = buffer.toString();
		}else {
			ret = line.trim();
		}
		return decode(ret.getBytes());
	}

	public T decode(byte[] nwkLine) {
		T root = null;
		Stack<T> stack = new Stack<>();
		int totalLength = nwkLine.length;
		// If it comes with a ";", then remove this character. In some cases, ";" may be used as a separator.
		if (nwkLine[totalLength - 1] == SEMICOLON_BYTE) {
			totalLength--;
		}
		int currentIndex = 0;
		while (currentIndex < totalLength) {
			byte currentByte = nwkLine[currentIndex];
			switch (currentByte) {
			case LEFT_BRACKET_BYTE:
				T internalNode = internalNodeParse.createNode();
				// the codes also could be added to the
				if (stack.isEmpty()) {
					root = internalNode;
				} else {
					stack.peek().addChild(internalNode);
				}
				stack.push(internalNode);
				currentIndex++;
				break;
			case COMMA_BYTE:
				// , char, do nothing
				currentIndex++;
				break;
			case RIGHT_BRACKET_BYTE:
				int searchStartIndex = currentIndex + 1;
				int nextCommaOrLeftBracketIndex1 = getNextStopCharIndex(nwkLine, searchStartIndex, totalLength);
				String string4InternalNode = new String(nwkLine, searchStartIndex,
						nextCommaOrLeftBracketIndex1 - searchStartIndex, StandardCharsets.US_ASCII);
				T pop = stack.pop();
				internalNodeParse.parseNode(pop, string4InternalNode);
				currentIndex = nextCommaOrLeftBracketIndex1;

				break;
			default:
				// The leaf name
				T leafNode = leafNodeParse.createNode();

				int nextCommaOrLeftBracketIndex2 = getNextStopCharIndex(nwkLine, currentIndex + 1, totalLength);
				String string4externalNode = new String(nwkLine, currentIndex,
						nextCommaOrLeftBracketIndex2 - currentIndex, StandardCharsets.US_ASCII);
				leafNodeParse.parseNode(leafNode, string4externalNode);
				stack.peek().addChild(leafNode);
				currentIndex = nextCommaOrLeftBracketIndex2;
				break;
			}
		}
		return root;
	}

	/**
	 * Stop char is <code>,)(</code>
	 *
	 * Note the return value is the index of the next stop char.
	 * And the parse method will not repeat iterate the byte[] array!
	 */
	private int getNextStopCharIndex(byte[] line, int currentIndex, int totalLength) {
		int ret = currentIndex;
		while (ret < totalLength) {
			byte currChar = line[ret];
			if (currChar == COMMA_BYTE || currChar == RIGHT_BRACKET_BYTE || currChar == LEFT_BRACKET_BYTE) {
				return ret;
			}
			ret++;
		}
		return ret;
	}



}
