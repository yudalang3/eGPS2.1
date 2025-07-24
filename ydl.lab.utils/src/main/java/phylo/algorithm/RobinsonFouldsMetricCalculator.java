package phylo.algorithm;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;
import evoltree.struct.util.EvolNodeUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 没空管实现是否高效了，有用就行。 Robinson-Foulds Metric的计算，用于快速进行两棵进化树的比较。
 */
public class RobinsonFouldsMetricCalculator {

    public int countDiff(EvolNode tree1, EvolNode tree2) {

        List<String> internalNodeElements1 = Lists.newArrayList();
        Set<String> internalNodeElements2 = Sets.newHashSet();

        EvolNodeUtil.recursiveIterateTreeIF(tree1, node -> {
            if (node.getParentCount() == 0) {
                return;
            }
            if (node.getChildCount() == 0) {
                return;
            }
            List<EvolNode> leaves = EvolNodeUtil.getLeaves(node);
            List<String> stringsOfNode1 = Lists.newLinkedList();
            for (EvolNode set : leaves) {
                stringsOfNode1.add(set.getName());
            }
            internalNodeElements1.add(setToSortedString(stringsOfNode1));
        });

        EvolNodeUtil.recursiveIterateTreeIF(tree2, node -> {
            if (node.getParentCount() == 0) {
                return;
            }
            if (node.getChildCount() == 0) {
                return;
            }
            List<EvolNode> leaves = EvolNodeUtil.getLeaves(node);
            List<String> stringsOfNode1 = Lists.newLinkedList();
            for (EvolNode set : leaves) {
                stringsOfNode1.add(set.getName());
            }
            internalNodeElements2.add(setToSortedString(stringsOfNode1));
        });

        int ret = 0;

        for (String string : internalNodeElements1) {
            if (!internalNodeElements2.contains(string)) {
                ret++;
            }
        }

        // 不要忘记乘以2
        return ret + ret;
    }

    private String setToSortedString(List<String> sortedList) {
        Collections.sort(sortedList);
        StringBuilder sb = new StringBuilder();
        for (String s : sortedList) {
            sb.append(s).append(",");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        TreeDecoder treeDecoder = new TreeDecoder();

        {
            String line1 = "(A,(B,(H,(D,(J,(((G,E),(F,I)),C))))));".replaceAll(" ", "");
            String line2 = "(A,(B,(D,((J, H),(((G, E),(F,I)),C)))));".replaceAll(" ", "");

            EvolNode root1 = treeDecoder.decode(line1);
            EvolNode root2 = treeDecoder.decode(line2);

            RobinsonFouldsMetricCalculator cal = new RobinsonFouldsMetricCalculator();
            int countDiff = cal.countDiff(root1, root2);
            System.out.println(countDiff == 4);
        }

        {
            String line1 = "(A,(B,(D,(H,(J,(((G,E),(F,I)), C))))));".replaceAll(" ", "");
            String line2 = "(A,(B,(E,(G,((F,I),((J,(H, D)), C))))));".replaceAll(" ", "");

            EvolNode root1 = treeDecoder.decode(line1);
            EvolNode root2 = treeDecoder.decode(line2);

            RobinsonFouldsMetricCalculator cal = new RobinsonFouldsMetricCalculator();
            int countDiff = cal.countDiff(root1, root2);
            System.out.println(countDiff == 10);
        }

        {
            String line1 = "(A,(B,(E,(G,((F,I),(((J, H), D), C))))));".replaceAll(" ", "");
            String line2 = "(A,(B,(E,((F,I),(G,((J,(H, D)), C))))));".replaceAll(" ", "");

            EvolNode root1 = treeDecoder.decode(line1);
            EvolNode root2 = treeDecoder.decode(line2);

            RobinsonFouldsMetricCalculator cal = new RobinsonFouldsMetricCalculator();
            int countDiff = cal.countDiff(root1, root2);
            System.out.println(countDiff == 4);
        }
    }

}
