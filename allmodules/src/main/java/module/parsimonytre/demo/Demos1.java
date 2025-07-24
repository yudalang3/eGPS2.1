package module.parsimonytre.demo;

import java.awt.Dimension;
import java.util.Arrays;

import evoltree.struct.util.EvolNodeUtil;
import module.evolview.phylotree.visualization.primary.swing.FastSingleTreeVisualizer;
import evoltree.swingvis.OneNodeDrawer;
import module.parsimonytre.algo.ComplexSnakoffAlgo;
import module.parsimonytre.algo.Node4SankoffAlgo;
import module.parsimonytre.algo.PrepareFactor;
import module.parsimonytre.algo.StateAfterMutation;

/**
 * @author yudalang
 * 
 *         作用: 当输入一个位点 site 的所有状态时，根据拓扑结构推断突变的构成。
 */
public class Demos1 {

	protected int maxValue = 10000;

	protected int totalSiteCount;

	protected PrepareFactor prepareFactor;

	public Demos1() {

		StateAfterMutation specialSS = new StateAfterMutation('A', 1);
		specialSS.setInsertionState("AT");

		StateAfterMutation[] ss = { 
				specialSS, 
				new StateAfterMutation('A', 1), 
				new StateAfterMutation('B', 1),
				new StateAfterMutation('C', 1), 
				new StateAfterMutation('D', 1), 
				new StateAfterMutation('E', 1),
				new StateAfterMutation('F', 1), 
				new StateAfterMutation('G', 1), 
				new StateAfterMutation('H', 1),
				new StateAfterMutation('I', 1), 
				new StateAfterMutation('J', 1), };

		prepareFactor = new PrepareFactor(ss);

		totalSiteCount = prepareFactor.getTotalStates();

	}

	public void demo() throws Exception {
		ComplexSnakoffAlgo snakoffAlgo = new ComplexSnakoffAlgo(prepareFactor);

		Node4SankoffAlgo root = new Node4SankoffAlgo(snakoffAlgo);
		Node4SankoffAlgo level1left = new Node4SankoffAlgo(snakoffAlgo);
		Node4SankoffAlgo level1leftleft = new Node4SankoffAlgo(snakoffAlgo);
		Node4SankoffAlgo level1leftright = new Node4SankoffAlgo(snakoffAlgo);
		Node4SankoffAlgo level1right = new Node4SankoffAlgo(snakoffAlgo);
		Node4SankoffAlgo level1rightleft = new Node4SankoffAlgo(snakoffAlgo);
		Node4SankoffAlgo level1rightright = new Node4SankoffAlgo(snakoffAlgo);

		root.addChild(level1left);
		root.addChild(level1right);

		level1left.addChild(level1leftleft);
		level1leftleft.setMinParsimonyScore(getScoreArray(1));
		level1left.addChild(level1leftright);
		level1leftright.setMinParsimonyScore(getScoreArray(1));

		level1right.addChild(level1rightleft);
		level1rightleft.setMinParsimonyScore(getScoreArray(2));
		level1right.addChild(level1rightright);
		level1rightright.setMinParsimonyScore(getScoreArray(2));

		snakoffAlgo.calculate(root);

		displayTheNode(root);
		
		System.out.println(Arrays.toString(root.getMinParsimonyScore()));

	}

	public void displayTheNode(Node4SankoffAlgo root) {
		EvolNodeUtil.iterateByLevelTraverse(root, node -> {
			node.setLength(1);
		});
		OneNodeDrawer<Node4SankoffAlgo> drawer = (g2d, grNode) -> {
			int xSelf = (int) grNode.getXSelf();
			int ySelf = (int) grNode.getYSelf();
			g2d.fillRect(xSelf, ySelf, 2, 2);
			Node4SankoffAlgo reflectNode = grNode.getReflectNode();
			g2d.drawString(reflectNode.getChosenChar().getStringOfOneStateAfterMutation(), xSelf + 5, ySelf);
		};
		FastSingleTreeVisualizer fastSingleTreeVisualizer = new FastSingleTreeVisualizer();
		fastSingleTreeVisualizer.alwaysOnTop = false;
		fastSingleTreeVisualizer.plotTree(root, "Title", new Dimension(1200, 600), drawer);
	}

	private int[] getScoreArray(int i) {
		int[] ret = new int[totalSiteCount];

		for (int j = 0; j < totalSiteCount; j++) {
			if (j == i) {
				ret[j] = 0;
			} else {
				ret[j] = maxValue;
			}
		}
		return ret;
	}

	public static void main(String[] args) throws Exception {
		new Demos1().demo();
	}

}
