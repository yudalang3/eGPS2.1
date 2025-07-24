package module.evoltre.enumeration.violence;

import java.util.ArrayList;
import java.util.List;

import utils.EGPSUtil;
import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;


public class Enumeration {

	/**
	 * 获取给定节点的枚举数量
	 * bifurcating, labeled, rooted trees, for 2, 3, and 4 tips.
	 *
	 * @param numOfOUT 节点的个数
	 * @return 枚举数量
	 */
	public static long getEnumerateCount(int numOfOUT) {
		if (numOfOUT < 2) {
			throw new IllegalArgumentException("The number of OUT should be greater than 2");
		}
		long ret = 1;
		int upBound = 2 * numOfOUT - 3;
		int currOddNum = 3;
		while (currOddNum <= upBound){
			ret *= currOddNum;
			currOddNum += 2;
		}

		return ret;
	}

	/**
	 * 枚举给定节点的所有可能组合，设定默认的长度为1
	 *
	 * 注意：这个程序有问题，结果是不对的。
	 *
	 * @param nodes 输入的节点列表
	 * @return 枚举后的节点列表
	 */
	public static List<EvolNode> enumerate(List<EvolNode> nodes) {
		int size = nodes.size();
		
		int pow = (int) Math.pow(2, size);
		List<EvolNode> list = new ArrayList<>( pow - 1);

		if (size > 2) {
			for (int i = 0; i < size; i++) {
				List<EvolNode> arrayList = new ArrayList<>(nodes);
				EvolNode remove = arrayList.remove(i);

				List<EvolNode> enumerates = enumerate(arrayList);

				
				for (EvolNode evolNode : enumerates) {
					ArrayBasedNode defaultNode = new ArrayBasedNode();
					defaultNode.setLength(1);
					defaultNode.addChild(remove);
					defaultNode.addChild(evolNode);
					list.add(defaultNode);
				}
				
			}
		} else if (size == 2) {
			ArrayBasedNode defaultNode = new ArrayBasedNode();
			defaultNode.setLength(1);
			for (EvolNode evolNode : nodes) {
				defaultNode.addChild(evolNode);
			}
			list.add(defaultNode);
		}
		
		return list;
	}

	public static void main(String[] args) {
		List<EvolNode> list = new ArrayList<>();

		for (int i = 3; i < 13; i++) {
			list.clear();
			getTree(i, list);
//			long enumerateCount = getEnumerateCount(i);
//			System.out.println("Num. of enumerates is : " + enumerateCount + "\tNum. of OTU :" + i);
		}

	}

	private static void getTree(int i, List<EvolNode> list) {
		for (int j = 0; j < i; j++) {
			ArrayBasedNode node = new ArrayBasedNode();
			node.setLength(1);
			list.add(node);
		}

		long startTimeMillis = System.currentTimeMillis();
		List<EvolNode> enumerate = Enumeration.enumerate(list);
		System.out.print("Num. of enumerates is : " + enumerate.size());
		System.out.println("\tNum. of OTU :" + i);


		EGPSUtil.printTimeSinceStartAndPrintUsedMemory(startTimeMillis);
	}


}
