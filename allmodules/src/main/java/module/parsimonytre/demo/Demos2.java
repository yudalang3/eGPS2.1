package module.parsimonytre.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;
import module.parsimonytre.algo.Node4SankoffAlgo;
import module.parsimonytre.algo.SankoffAlgoRunner;
import module.parsimonytre.algo.StateAfterMutation;

/**
 * @author yudalang
 * 
 *         作用: 当输入一个位点 site 的所有状态时，再读入nwk格式编码的树文件。然后根据拓扑结构推断突变的构成。
 */
public class Demos2 extends Demos1 {

	@Override
	public void demo() throws Exception {
		TreeDecoder decode = new TreeDecoder();

//		String string = "(((1:1,2:1):1,(3:1,4:1):1),(5:1,6:1):1):0";
		String string = "(1:5,((2:2,(3:1,4:1)7:1)9:1),((5:1,6:1)8:2)10:1)11:0";
		EvolNode root = decode.decode(string);

		Map<String, Character> name2stateMap = new HashMap<>();
//		name2stateMap.put("1", 'E');
//		name2stateMap.put("2", 'A');
//		name2stateMap.put("3", 'E');
//		name2stateMap.put("4", 'E');
//		name2stateMap.put("5", 'B');
//		name2stateMap.put("6", 'B');

		name2stateMap.put("1", 'A');
		name2stateMap.put("2", 'A');
		name2stateMap.put("3", 'T');
		name2stateMap.put("4", 'G');
		name2stateMap.put("5", 'T');
		name2stateMap.put("6", 'C');

		HashMap<Integer, StateAfterMutation> leaf2stateMap = new HashMap<Integer, StateAfterMutation>();
		List<EvolNode> leaves = EvolNodeUtil.getLeaves(root);
		for (EvolNode evolNode : leaves) {
			String name = evolNode.getName();

			Character character = name2stateMap.get(name);
			StateAfterMutation value = new StateAfterMutation(character, 1);

			leaf2stateMap.put(evolNode.getID(), value);
		}

		Node4SankoffAlgo rootRet = SankoffAlgoRunner.quickRunAlgo(root, leaf2stateMap, new StateAfterMutation('T', 1));
		
		displayTheNode(rootRet);
		
		System.out.println(Arrays.toString(rootRet.getMinParsimonyScore()));
	}

	public static void main(String[] args) throws Exception {
		new Demos2().demo();
	}

}
