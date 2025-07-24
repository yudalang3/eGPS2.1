package evoltree.struct.util;

import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;

public class PhyloTreeGenerator {

    public EvolNode getTheTree() {
        EvolNode root = createNode();
        root.setName("Cellular organisms");
        configureTree(root, 27);

        return root;
    }

    public EvolNode getTheTree(int depth) {
        EvolNode root = createNode();
        root.setName("Tree with depth " + depth);
        configureTree(root, depth);

        return root;
    }

    private void configureTree(EvolNode root, int depth) {
        if (depth <= 0) {
            return; // 如果达到指定的深度，则停止递归
        }

        // 创建左子节点
        EvolNode leftChild = createNode();
        leftChild.setName("L_" + depth);
        root.addChild(leftChild); // 添加左子节点


        EvolNode rightChild = createNode();
        rightChild.setName("R_" + depth);
        root.addChild(rightChild); // 添加右子节点

        configureTree(rightChild, depth - 1); // 递归配置左子树

    }

    private static EvolNode createNode() {
        ArrayBasedNode node = new ArrayBasedNode();
        node.setLength(1.0);
        return node;

    }


}

