package module.evoltre.mutation;

import static phylo.msa.util.EvolutionaryProperties.GAP_CHAR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import phylo.msa.util.EvolutionaryProperties;
import module.parsimonytre.algo.StateAfterMutation;

/**
 * 
 * A collection of methods to deal with Mutations.
 * 
 * @title MutationOperator
 * @createdDate 2021-03-02 10:13
 * @lastModifiedDate 2021-03-02 10:13
 * @author yudalang
 * @since 1.7
 *
 */

public class MutationOperator {

	private static HashMap<StateAfterMutation, StateAfterMutation> stateAfterMutationFactory = new HashMap<StateAfterMutation, StateAfterMutation>(
			1 << 10);

	private MutationOperator() {

	}

	public static void clear() {
		stateAfterMutationFactory = null;
	}

	/**
	 * 
	 * 这个方法的作用：将一个比对好的双序列联配，转换成坐标。 对于多序列的联配，我想到是可以像计算进化距离那样批量联配的。所以说我们可以对
	 * Tracing进行优化。
	 * 
	 * 注意，这里要处理序列比对之后，首尾两种情况。
	 * 
	 * <pre>
	 *               100 号位置在这里
	 * 如果头部是 ----------ATG 
	 * 
	 *                                              29800,那么要注意这里就不是deletion了。
	 * 或者尾部  ATCAGTAGCATCGATCAGCATCGATCAGT-------------------
	 * </pre>
	 * 
	 * @title getPairwiseSeqMutations
	 * @createdDate 2020-10-29 15:11
	 * @lastModifiedDate 2020-10-29 15:11
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param alignedRef
	 * @param newString
	 * @param formOneBase : one-based start position
	 * @param toOneBase   : one-based end position
	 */
	public static List<IMutation4Rec> getPairwiseSeqMutationsGui(String refOri, String alignedRef, String newString,
			int formOneBase, int toOneBase) {
		int size = alignedRef.length();

		if (newString.length() != size) {
			throw new IllegalArgumentException("You should input aligned strings.");
		}

//		下面这个暂时不需要它报告出来
//		if (alignedRef.charAt(0) == '-') {
//			System.err.println("Warnning: The reference aligned sequence start with - !");
//		}

		if (formOneBase > 1 || toOneBase < refOri.length()) {
			int countableFromOneBase = 0, countableToOneBase = 0;

			int index = 0;
			for (int i = 0; i < size; i++) {
				char refChar = alignedRef.charAt(i);
				char stringChar = newString.charAt(i);

				if (refChar != GAP_CHAR) {
					index++;
				}

				if (stringChar != GAP_CHAR) {
					countableFromOneBase = index;
					break;
				}
			}

			index = refOri.length();
			for (int i = size - 1; i > 0; i--) {
				char refChar = alignedRef.charAt(i);
				char stringChar = newString.charAt(i);

				if (stringChar != GAP_CHAR) {
					countableToOneBase = index;
					break;
				}
				if (refChar != GAP_CHAR) {
					index--;
				}
			}

			if (formOneBase < countableFromOneBase) {
				formOneBase = countableFromOneBase;
			}

			if (toOneBase > countableToOneBase) {
				toOneBase = countableToOneBase;
			}
		}

		// 下面是正式地处理
		List<IMutation4Rec> list = new LinkedList<>();

		int index = 0;
		for (int i = 0; i < size; i++) {
			char refChar = alignedRef.charAt(i);
			char stringChar = newString.charAt(i);

			if (refChar != GAP_CHAR) {
				index++;
			}

			if (refChar != stringChar) {
				if (index >= formOneBase && index <= toOneBase) {
					list.add(new Mutation0302(index, String.valueOf(refChar), String.valueOf(stringChar)));
				}
			}
		}
		/**
		 * 这里需要有一个Check 的步骤以处理一个复杂的情况,这种情况需要报告出来，是比对的质量不好
		 * 
		 * <pre>
		 * 
		 * String alignedRefGenomeString = "AT--GT--T--C--";
		 * String newString = "ATCCG----G-CTT";
		 * 
		 * </pre>
		 */
		checkAwfulMutations(list);

		// Than we merge!

		Iterator<IMutation4Rec> iterator = list.iterator();

		IMutation4Rec prev = iterator.hasNext() ? iterator.next() : null;

		if (prev == null) {
			return list;
		}

		// 如果第一个是insertion 需要特殊处理。
		if (isFirstCharIsGap(prev.getAncestralState())) {
			// should insert insute state
			String ori = String.valueOf(refOri.charAt(prev.getPosition() - 1));
			prev.setAncestralState(ori);
			prev.setDerivedState(ori.concat(prev.getDerivedState()));

		}

		while (iterator.hasNext()) {
			IMutation4Rec curr = iterator.next();

			if (isFirstCharIsGap(curr.getAncestralState())) {
				// Insertion
				if (isFirstCharIsGap(prev.getAncestralState())) {
					prev.setDerivedState(prev.getDerivedState().concat(curr.getDerivedState()));
					iterator.remove();
					continue;
				} else {
					// This is the first insertion
					if (prev.getPosition() == curr.getPosition()) {
						// should merge
						prev.setDerivedState(prev.getDerivedState().concat(curr.getDerivedState()));
						iterator.remove();
						continue;
					} else {
						// should insert insute state
						String ori = String.valueOf(refOri.charAt(curr.getPosition() - 1));
						curr.setAncestralState(ori);
						curr.setDerivedState(ori.concat(curr.getDerivedState()));
					}
				}
			} else if (isFirstCharIsGap(curr.getDerivedState())) {
				// Deletion
				if (isFirstCharIsGap(prev.getDerivedState())) {
					if (prev.getPosition() + prev.getAncestralState().length() == curr.getPosition()) {
						prev.setAncestralState(prev.getAncestralState().concat(curr.getAncestralState()));
						iterator.remove();
						continue;
					}
				}
			}
			prev = curr;
		}

		return list;
	}

	private static boolean checkAwfulMutations(List<IMutation4Rec> list) {
		Iterator<IMutation4Rec> iterator = list.iterator();

		if (iterator.hasNext()) {
			IMutation4Rec prev = iterator.next();
			while (iterator.hasNext()) {
				IMutation4Rec curr = iterator.next();
				// System.out.println(curr);
				if (prev.getDerivedState().charAt(0) == GAP_CHAR) {
					if (curr.getAncestralState().charAt(0) == GAP_CHAR) {
						if (prev.getPosition() == curr.getPosition()) {
							throw new InputMismatchException(
									"Sorry, the sequence alignment is not reasonable in position: "
											+ curr.getPosition());
						}
					}
				}
				prev = curr;
			}
		}

		return false;
	}

	private static boolean isFirstCharIsGap(String str) {
		return GAP_CHAR == str.charAt(0);
	}

	/**
	 * 直接得到 从1 到 refOri的length的突变
	 * 
	 * @param refOri
	 * @param alignedRef
	 * @param newString
	 * @return
	 */
	public static List<IMutation4Rec> getPairwiseSeqMutationsGui(String refOri, String alignedRef, String newString) {

		return getPairwiseSeqMutationsGui(refOri, alignedRef, newString, 1, refOri.length());
	}

	public static List<IMutation> imutation4Rect2Imutation(List<IMutation4Rec> input) {
		List<IMutation> mutations = new ArrayList<>();
		for (IMutation iMutation : input) {
			mutations.add(iMutation);
		}
		return mutations;

	}

	/**
	 * 
	 * 将这个字符串转化成 IMutation4Rec 实例。 使用示例：
	 * 
	 * "GGT6-" IMutation4Rec parseMutation = MutationOperator.parseMutation("GGT6-",
	 * true); 得到 GGT 6 -
	 * 
	 * 如果 IMutation4Rec parseMutation = MutationOperator.parseMutation("GGT6-",
	 * false); 得到 GGT 5 -
	 * 
	 * @title parseMutation
	 * @createdDate 2020-10-30 15:12
	 * @lastModifiedDate 2020-10-30 15:12
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param str
	 * @param oneBased
	 * @return IMutation4Rec
	 */
	public static IMutation4Rec parseMutation(String str, boolean oneBased) {
		int strLength = str.length();

		int firstDigitIndex = -1; // 0-based, inclusive
		for (int i = 0; i < strLength; i++) {
			char c = str.charAt(i);
			if (Character.isDigit(c)) {
				firstDigitIndex = i;
				break;
			}
		}

		int lastDigitIndex = -1; // 0-based, exclusive
		for (int i = firstDigitIndex + 1; i < strLength; i++) {
			char c = str.charAt(i);
			if (!Character.isDigit(c)) {
				lastDigitIndex = i;
				break;
			}
		}
		/**
		 * 前面的lastDigitIndex其实只是数字后第一个不是数字的char，但是后面也可能出现非数字字符。
		 * 因为搜索也调用了此方法，所以可能传入“ON710682.1”"Canada/ON-PHL-21-19569/2021"这样的字符串。
		 * 所以需要额外判定lastDigitIndex后面是否又一次出现数字，如果出现则也不是突变
		 */
		for (int i = lastDigitIndex + 1; i < strLength; i++) {
			char c = str.charAt(i);
			if (Character.isDigit(c)) {
				return null;
			}
		}

		String ancestralAllele = str.substring(0, firstDigitIndex);
		String derivedAllele = str.substring(lastDigitIndex);
		int lastPositionInsertLength = 0;
		if (firstDigitIndex == 1 && ancestralAllele.charAt(0) == EvolutionaryProperties.GAP_CHAR) {
			// 这个突变是insertion
			int leftBracketIndex = -1;
			for (int i = lastDigitIndex + 1; i < strLength; i++) {
				char c = str.charAt(i);
				if (c == IMutation.LAST_POSITION_INSERTION_SPLIT_CHAR) {
					leftBracketIndex = i;
					break;
				}
			}

			if (leftBracketIndex > 0) {
				derivedAllele = derivedAllele.replace("`", "");
				lastPositionInsertLength = strLength - leftBracketIndex - 1;
			}
		}

		int pos = -1;
		if (oneBased) {
			pos = Integer.parseInt(str.substring(firstDigitIndex, lastDigitIndex));
		} else {
			pos = Integer.parseInt(str.substring(firstDigitIndex, lastDigitIndex)) - 1; // Transited as 0-based.
		}

		IMutation4Rec mut = new Mutation0302(pos, ancestralAllele, derivedAllele);
		mut.setLastPositionInsertLength(lastPositionInsertLength);

		return mut;
	}

	/**
	 * Input mutation strings to mutation instance list.
	 * 
	 * "-28728TTTCA,-28731C"; String will be two mutation list
	 * 
	 * @param mutString
	 * @return
	 */
	public static List<IMutation> parseHumanReadableMutationStringsToList(String mutString) {
		mutString = mutString.replace(" ", "");
		List<IMutation> exp = new ArrayList<>();
		for (String ss : mutString.split(",")) {
			IMutation4Rec parseMutation = MutationOperator.parseMutation(ss, true);
			exp.add(parseMutation);
		}

		return exp;
	}

//	/**
//	 * 输入目标的 Mutations，然后这个方法就会去寻找带有累计这些突变的内节点。 也有可能找不到这个内节点。
//	 * 
//	 * @param root
//	 * @param mutations
//	 * @return
//	 */
//	public static List<GraphicsNode>  findTargetInternalNode4mutations(GraphicsNode root, IMutation4Rec[] mutations) {
//
//		List<GraphicsNode> ret = new ArrayList<>();
//		List<IMutation4Rec> targetMutations = Arrays.asList(mutations);
//		iterate4findTargetInternalNode4mutations(root, new ArrayList<IMutation4Rec>(), targetMutations, ret);
//		return ret;
//	}
//
//	private static void iterate4findTargetInternalNode4mutations(GraphicsNode node, List<IMutation4Rec> calculators,
//			List<IMutation4Rec> targetMutations, List<GraphicsNode> candidates) {
//
//		ArrayList<IMutation4Rec> arrayList = new ArrayList<>(calculators);
//		IMutation4Rec[] mm = node.getMutation();
//		if (mm != null) {
//			for (IMutation4Rec m : mm) {
//				arrayList.add(m);
//			}
//		}
//
//		if (!node.getDate().isEmpty()) {
//			if (arrayList.containsAll(targetMutations)) {
//				candidates.add(node);
//				return;
//			}
//		}
//
//		int childCount = node.getChildCount();
//		if (childCount != 0) {
//			for (int i = 0; i < childCount; i++) {
//				iterate4findTargetInternalNode4mutations(node.getChildAt(i), arrayList, targetMutations, candidates);
//			}
//		}
//	}

	/**
	 * 得到从根到叶子的所有累计的突变。 然后把互补的突变都去掉。
	 * 
	 * C20451T, T20451C, T...G21765-, C20451T 测试一下有问题，竟然把整个 20451 突变删掉了。 (问题已修复)
	 * 
	 * @param mutationsFromEarly2late
	 */
	public static void eraseMutations(List<IMutation> mutationsFromEarly2late) {
		getEraseMutations(mutationsFromEarly2late);
	}

	/**
	 * 
	 * 对传入的List<IMutation>完成eraseMutation的同时返回erase的列表
	 * 
	 * 注意: 这个对于没有很多回复突变的简单顺序是可以正确解析的，但是复杂突变需要注意，例如下面的例子：
	 * 
	 * <pre>
	 * 解析前：
	 * C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,TTA21991-,C23604A,G24914C,C23709T,C5388A,C5986T,T24506G,C3267T,T6954C,G28048T,C23271A,T16176C,C15279T,C913T,A28111G,C14676T,C28977T,T28282A,A28281T,G28280C,C27972T,A23063T,TACATG21765-,TCTGGTTTT11288-,A28271-,A28095T,C14120T,C2110T,-21991TTA,-21765TACATC,CATCTC21767-,TTA21991-,A29183G,G7936T,C5628T,-21771TC,TA21765-,C23170T,A12234G,C29281T,A3472C,G28109T,-21765TACATG
	 * 解析后：
	 * C3037T,A23403G,C14408T,C241T,G28881A,G28882A,G28883C,          C23604A,G24914C,C23709T,C5388A,C5986T,T24506G,C3267T,T6954C,G28048T,C23271A,T16176C,C15279T,C913T,A28111G,C14676T,C28977T,T28282A,A28281T,G28280C,C27972T,A23063T,             TCTGGTTTT11288-,A28271-,A28095T,C14120T,C2110T,          -21765TACATC,CATCTC21767-,TTA21991-,A29183G,G7936T,C5628T,-21771TC,TA21765-,C23170T,A12234G,C29281T,A3472C,G28109T
	 * </pre>
	 * 
	 * @title getEraseMutations
	 * @createdDate 2021-04-01 10:33
	 * @lastModifiedDate 2021-04-01 10:33
	 * @author
	 * @since 1.7
	 * 
	 * @param mutationsFromEarly2late
	 * @return
	 * @return List<IMutation>
	 */
	public static List<IMutation> getEraseMutations(List<IMutation> mutationsFromEarly2late) {
		List<IMutation> eraseMutations = new ArrayList<IMutation>();
		Map<Integer, List<IMutation>> location2listOfMutation = new HashMap<>();
		for (IMutation mutation : mutationsFromEarly2late) {
			int position = mutation.getPosition();
			List<IMutation> list = location2listOfMutation.get(position);
			if (list == null) {
				List<IMutation> listOfMutation = new ArrayList<>();
				listOfMutation.add(mutation);
				location2listOfMutation.put(position, listOfMutation);
			} else {
				list.add(mutation);
			}
		}

		List<IMutation> list2remove = new ArrayList<>();
		for (Entry<Integer, List<IMutation>> entry : location2listOfMutation.entrySet()) {
			list2remove.clear();

			List<IMutation> value = entry.getValue();
			int size = value.size();
			if (size < 2) {
				continue;
			}
			int index = 0;
			IMutation currMutation = null;
			while (index < size) {
				IMutation mutation = value.get(index);

				if (currMutation == null) {
					currMutation = mutation;
				} else {
					if (isBackMutation(currMutation, mutation)) {
						list2remove.add(currMutation);
						list2remove.add(mutation);
						currMutation = null;
					}
				}

				index++;
			}

			if (!list2remove.isEmpty()) {
				for (IMutation mutation4remove : list2remove) {
					Iterator<IMutation> iterator = mutationsFromEarly2late.iterator();
					while (iterator.hasNext()) {
						IMutation curr = iterator.next();
						if (curr.equals(mutation4remove)) {
							iterator.remove();
							eraseMutations.add(curr);
							break;
						}
					}
				}
			}
		}

		return eraseMutations;
	}

	/**
	 * 两个突变是否是互补的突变。这个互补很大情况下是由重组造成的 20221024起，我们运用了新的位点标记体系，在这个体系中,
	 * 插入突变有可能最后一个基因组位置有一个插入序列。
	 * 
	 * @param currMutation
	 * @param mutation
	 * @return
	 */
	public static boolean isBackMutation(IMutation currMutation, IMutation mutation) {

		if (mutation.getPosition() != currMutation.getPosition()) {
			return false;
		}
		if (currMutation.getAncestralState().equals(mutation.getDerivedState())
				&& currMutation.getDerivedState().equals(mutation.getAncestralState())) {
			return true;
		}
		return false;
	}

	public static boolean strictEquals(IMutation currMutation, IMutation mutation) {

		if (mutation.getPosition() != currMutation.getPosition()) {
			return false;
		}
		if (currMutation.getAncestralState().equals(mutation.getAncestralState())
				&& currMutation.getDerivedState().equals(mutation.getDerivedState())) {
			if (currMutation.getLastPositionInsertLength() == mutation.getLastPositionInsertLength()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMultipleHit(IMutation currMutation, IMutation mutation) {
		return mutation.getPosition() == currMutation.getPosition();
	}

	/**
	 * 两个突变是否是互补的突变。这个互补很大情况下是由重组造成的
	 * 
	 * @param currMutation
	 * @return
	 */
	public static boolean isComplementary(IMutation4Rec currMutation, List<IMutation4Rec> mutations) {
		for (IMutation4Rec mutation : mutations) {
			if (isBackMutation(mutation, currMutation)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isDeletion(IMutation mutation) {
		String chars = mutation.getDerivedState();
		return mutation.getAncestralState().length() > 0 && chars.charAt(0) == GAP_CHAR;
	}

	/**
	 * 
	 * 用之前这个输出的突变需要sort一下！ Only merge sequential Deletions. 注意insertion也要考虑
	 * 
	 * 它的作用是：将 C10-,G11- 合并成 CG11-
	 * 
	 * @title mergeListOfMutation
	 * @createdDate 2020-12-01 12:51
	 * @lastModifiedDate 2020-12-01 12:51
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @return List<IMutation4Rec>
	 */
	public static void mergeListOfMutation(List<? extends IMutation> input) {
		Iterator<? extends IMutation> iterator = input.iterator();

		boolean hasNext = iterator.hasNext();

		IMutation prev = null;

		if (hasNext) {
			prev = iterator.next();
			int prevPos = prev.getPosition();
			while (iterator.hasNext()) {
				IMutation curr = iterator.next();
				if (prevPos + 1 == curr.getPosition() && isDeletion(prev) && isDeletion(curr)) {
					iterator.remove();

					prev.setAncestralState(prev.getAncestralState().concat(curr.getAncestralState()));
				} else {
					prev = curr;

				}
				prevPos = curr.getPosition();
			}
		}

		iterator = input.iterator();

		hasNext = iterator.hasNext();

		if (hasNext) {
			prev = iterator.next();
			int prevPos = prev.getPosition();
			while (iterator.hasNext()) {
				IMutation curr = iterator.next();
				if (prevPos + 1 == curr.getPosition() && isStrictInsertion(prev) && isStrictInsertion(curr)) {
					iterator.remove();

					prev.setDerivedState(prev.getDerivedState().concat(curr.getDerivedState()));
				} else {
					prev = curr;

				}
				prevPos = curr.getPosition();
			}
		}

	}

	/**
	 * 
	 * 相较于mergeListOfMutation 这个方法 对于如下的转换是有区别的:
	 * 
	 * <pre>
	 * Ref    T    T    T    T    T
	 * Pos    1    2    3    4    5
	 * Seq1   -    -    -    -    -
	 * Seq2   T    TGG  T    T    TAA
	 * </pre>
	 * 
	 * 那么 Seq1 -> Seq2 的突变不是像之前一样是 -1TTGGTTTAA，而是 -1TTGG, -3TTTAA
	 * 
	 * @title mergeListOfMutationIndelRestorable
	 * @createdDate 2020-12-01 12:51
	 * @lastModifiedDate 2020-12-01 12:51
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @return List<IMutation4Rec>
	 */
	public static void mergeListOfMutationIndelRestorable(List<? extends IMutation> input) {
		Iterator<? extends IMutation> iterator = input.iterator();

		boolean hasNext = iterator.hasNext();

		IMutation prev = null;
		boolean previousIs = false;
		// Deletion
		if (hasNext) {
			prev = iterator.next();
			int prevPos = prev.getPosition();
			previousIs = isDeletion(prev);
			while (iterator.hasNext()) {
				IMutation curr = iterator.next();
				boolean currIsDeletion = isDeletion(curr);
				if (prevPos + 1 == curr.getPosition() && previousIs && currIsDeletion) {
					iterator.remove();

					prev.setAncestralState(prev.getAncestralState().concat(curr.getAncestralState()));
				} else {
					prev = curr;

				}
				prevPos = curr.getPosition();
				previousIs = currIsDeletion;
			}
		}

		// Insertion
		iterator = input.iterator();
		hasNext = iterator.hasNext();

		if (hasNext) {
			prev = iterator.next();
			int prevPos = prev.getPosition();
			previousIs = isStrictInsertion(prev);

			int derivedStateLength = prev.getDerivedState().length();
			if (previousIs && derivedStateLength > 1) {
				prev.setLastPositionInsertLength(derivedStateLength - 1);
				previousIs = false;
			}

			while (iterator.hasNext()) {
				IMutation curr = iterator.next();
				boolean currStrictInsertion = isStrictInsertion(curr);
				if (prevPos + 1 == curr.getPosition() && previousIs && currStrictInsertion) {
					iterator.remove();

					prev.setDerivedState(prev.getDerivedState().concat(curr.getDerivedState()));
				} else {
					prev = curr;

				}
				prevPos = curr.getPosition();

				derivedStateLength = curr.getDerivedState().length();
				if (currStrictInsertion && derivedStateLength > 1) {
					prev.setLastPositionInsertLength(derivedStateLength - 1);
					previousIs = false;
				} else {
					previousIs = currStrictInsertion;
				}
			}
		}

	}

	private static boolean isStrictInsertion(IMutation mutation) {
		String ancestralState = mutation.getAncestralState();
		if (ancestralState.length() == 1 && GAP_CHAR == ancestralState.charAt(0)) {
			return true;
		}
		return false;
	}

	public static String mutationList2AbbrString(List<IMutation> list) {

		StringBuilder stringBuilder = new StringBuilder();
		for (IMutation mutation : list) {
			stringBuilder.append(mutation.toString()).append(",");
		}

		int index = stringBuilder.length() - 1;
		if (index > -1) {
			stringBuilder.deleteCharAt(index);
		}
		return stringBuilder.toString();
	}

	public static String mutation4RecList2FullString(List<IMutation4Rec> list) {

		StringBuilder stringBuilder = new StringBuilder();
		for (IMutation4Rec mutation : list) {
			stringBuilder.append(mutation.getFullInformation()).append(",");
		}

		int index = stringBuilder.length() - 1;
		if (index > -1) {
			stringBuilder.deleteCharAt(index);
		}
		return stringBuilder.toString();
	}

	/**
	 * 
	 * 这个和mutation4RecList2FullString的区别是一个输入是List<IMutation4Rec>，一个是List<IMutation>。
	 * 用两个方法是为了节省不同输入时，list间转换的内存消耗
	 * 
	 * @title mutationList2FullString
	 * @createdDate 2021-11-15 20:32
	 * @lastModifiedDate 2021-11-15 20:32
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param list
	 * @return
	 * @return String
	 */
	public static String mutationList2FullString(List<? extends IMutation> list) {

		StringBuilder stringBuilder = new StringBuilder();
		for (IMutation mutation : list) {
			stringBuilder.append(mutation.getFullInformation()).append(",");
		}

		int index = stringBuilder.length() - 1;
		if (index > -1) {
			stringBuilder.deleteCharAt(index);
		}
		return stringBuilder.toString();
	}

	public static List<StateAfterMutation> deepCloneListOfStateAfterMutation(
			List<StateAfterMutation> listOfStateAfterMutations) {
		List<StateAfterMutation> ret = new ArrayList<>();

		for (StateAfterMutation stateAfterMutation : listOfStateAfterMutations) {
			ret.add(stateAfterMutation.clone());
		}
		return ret;
	}

	public static boolean isPointMutation(IMutation targetMutation) {
		String origin = targetMutation.getAncestralState();
		String destination = targetMutation.getDerivedState();

		if (origin.length() == 1 && destination.length() == 1) {
			if (origin.charAt(0) != GAP_CHAR && destination.charAt(0) != GAP_CHAR) {
				return true;
			}
		}
		return false;
	}

	public static List<IMutation4Rec> reverseMutations(List<IMutation4Rec> inputMutations) {
		List<IMutation4Rec> ret = new ArrayList<>();
		int size = inputMutations.size();
		for (int i = size - 1; i >= 0; i--) {
			IMutation4Rec mutation = inputMutations.get(i);
			ret.add(reverseOneMutation(mutation));
		}

		return ret;
	}

	private static IMutation4Rec reverseOneMutation(IMutation4Rec mutation) {
		return new Mutation0302(mutation.getPosition(), mutation.getDerivedState(), mutation.getAncestralState());
	}

	public static List<IMutation4Rec> castMutationList4Reconbination(List<IMutation> input) {
		List<IMutation4Rec> ret = new ArrayList<>();
		for (IMutation ele : input) {
			if (ele instanceof IMutation4Rec) {
				ret.add((IMutation4Rec) ele);
			} else {
				throw new InputMismatchException("Can not cast IMutation to IMutation4Rec!");
			}
		}

		return ret;
	}

	public static IMutation4Rec cloneOneMutation(IMutation4Rec targetMut) {
		return new Mutation0302(targetMut.getPosition(), targetMut.getAncestralState(), targetMut.getDerivedState());
	}

	public static List<IMutation4Rec> cloneListOfMutations(List<IMutation4Rec> inputs) {

		List<IMutation4Rec> ret = new ArrayList<>();
		for (IMutation4Rec iMutation4Rec : inputs) {
			ret.add(cloneOneMutation(iMutation4Rec));
		}
		return ret;
	}

	public static HashMap<StateAfterMutation, StateAfterMutation> getStateAfterMutationFactory() {
		return stateAfterMutationFactory;
	}

	/**
	 * 
	 * 该方法通过StateAfterMutation的部分享元，来降低内存消耗
	 * 
	 * @title useStateAfterMutationFactoryClone
	 * @createdDate 2021-08-17 18:38
	 * @lastModifiedDate 2021-08-17 18:38
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param states
	 * @return
	 * @return List<StateAfterMutation>
	 */
	public static List<StateAfterMutation> useStateAfterMutationFactoryClone(List<StateAfterMutation> states) {
		List<StateAfterMutation> fromFactory = new ArrayList<>();

		for (StateAfterMutation sm : states) {
			StateAfterMutation getState = stateAfterMutationFactory.get(sm);
			if (getState != null) {
				fromFactory.add(getState);
			} else {
				stateAfterMutationFactory.put(sm, sm);
				fromFactory.add(sm);
			}
		}

		return fromFactory;

	}

	public static char getInsituStateOfSpecificPostion(List<StateAfterMutation> listOfStateAfterMutations, int position,
			String genomeString) {
		for (StateAfterMutation stateAfterMutation : listOfStateAfterMutations) {
			if (stateAfterMutation.getPosition() == position) {
				return stateAfterMutation.getInsutePlaceState();
			}
		}
		return genomeString.charAt(position - 1);
	}

	public static StateAfterMutation getStateOfSpecificPostion(List<StateAfterMutation> listOfStateAfterMutations,
			int position, String genomeString) {
		for (StateAfterMutation stateAfterMutation : listOfStateAfterMutations) {
			if (stateAfterMutation.getPosition() == position) {
				return stateAfterMutation;
			}
		}
		return new StateAfterMutation(genomeString.charAt(position - 1), position);
	}

	private static final int charIndexOfA = 'A';
	private static final int charIndexOfZ = 'Z';
	private static final int charIndexOf0 = '0';
	private static final int charIndexOf9 = '9';

	/**
	 * 输入一个字符串，判断其是否是一个突变的表示形式。 例如 A23406T，也可以是氨基酸的突变形式。 注意氨基酸也有ATCG四个字符。 具体的判定形式：
	 * 不能小写 前面一段是A-Z还有-; 中间是 0-9 ; 后面是 A-Z还有-
	 * 
	 * yudalang: 20221018，这个方法和下面的parse Mutation的方法重复了。
	 * 
	 * @param str
	 * @return mutation: 验证如果通过也就直接生成突变了，节省资源，而且快。
	 */
	private static IMutation isMutationStr(String str) {
		int length = str.length() - 1;
		if (length < 2) {
			return null;
		}
		if (!isA2Z(str.charAt(0))) {
			return null;
		}

		int indexOfDigitStart = 1;
		while (indexOfDigitStart < length) {
			if (is0To9(str.charAt(indexOfDigitStart))) {
				break;
			}
			indexOfDigitStart++;
		}

		if (indexOfDigitStart == length) {
			return null;
		}

		if (!isA2Z(str.charAt(length))) {
			return null;
		}

		int indexOfDigitEnd = length - 1;
		while (indexOfDigitEnd >= indexOfDigitStart) {
			if (is0To9(str.charAt(indexOfDigitStart))) {
				break;
			}
			indexOfDigitEnd--;
		}

		int temp = indexOfDigitEnd + 1;
		int pos = Integer.parseInt(str.substring(indexOfDigitStart, temp));

		String ancestor = str.substring(0, indexOfDigitStart);
		String substring = str.substring(temp);
		return new Mutation0302(pos, ancestor, substring);
	}

	private static boolean isA2Z(char c) {
		if (c == GAP_CHAR) {
			return true;
		}

		if (c < charIndexOfA || c > charIndexOfZ) {
			return false;
		}
		return true;
	}

	private static boolean is0To9(char c) {
		if (c < charIndexOf0 || c > charIndexOf9) {
			return false;
		}
		return true;
	}

	public static List<IMutation4Rec> parseMutationFromCommaSplitString(String string) {
		
		if (string == null || string.isEmpty()) {
			return Collections.emptyList();
		}
		String[] split = string.split(",");
		List<IMutation4Rec> ret = new ArrayList<>();
		for (String str : split) {
			IMutation4Rec parseMutation = parseMutation(str, true);
			ret.add(parseMutation);
		}

		return ret;
	}

	public static IMutation isMutationString(String string) {
		IMutation4Rec ret = null;

		try {
			ret = parseMutation(string, true);
		} catch (Exception e) {
			// null will not be returned.
		}

		return ret;
	}

	public static int getNumOfINDEL(List<IMutation4Rec> mutations) {
		int ret = 0;
		for (IMutation4Rec mut : mutations) {
			if (mut.getAncestralState().charAt(0) == GAP_CHAR || mut.getDerivedState().charAt(0) == GAP_CHAR) {
				ret ++;
			}
		}
		return ret;
	}
	public static List<IMutation4Rec> getINDELMutations(List<IMutation4Rec> mutations) {
		List<IMutation4Rec> ret = new ArrayList<>();
		for (IMutation4Rec mut : mutations) {
			if (mut.getAncestralState().charAt(0) == GAP_CHAR || mut.getDerivedState().charAt(0) == GAP_CHAR) {
				ret.add(mut);
			}
		}
		return ret;
	}

}
