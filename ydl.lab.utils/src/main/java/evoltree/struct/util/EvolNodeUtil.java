package evoltree.struct.util;

import com.google.common.base.Strings;
import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeCoder;
import evoltree.struct.io.PrimaryNodeTreeDecoder;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 *
 * <h1>目的</h1>
 * <p>
 * 这个目的是对树进行一些便捷的操作，注意点一定要看！！！！！
 * </p>
 *
 * <h1>输入/输出</h1>
 *
 * <p>
 * 大部分方法都是直接输入一个根结点
 * </p>
 *
 * <h1>使用方法</h1>
 *
 * <blockquote> 直接调用静态方法 </blockquote>
 *
 * <h1>注意点</h1>
 * <ol>
 * <li>有些方法放在
 * edu.sinh.beauty.unisoft.remnant.gfamily.hias.ydl.NodeUtil这里了。因为这个类还不能改
 * 这是一个最通用的类</li>
 * <li>这个注释必看</li>
 * </ol>
 * @lastModifiedDate  2024-07-25
 *
 * @implSpec 非常偏IT的类
 *
 * @author yudal
 *
 *
 */
public class EvolNodeUtil {

    private static final Comparator<EvolNode> sizeComparator = new Comparator<>() {
        @Override
        public int compare(EvolNode o1, EvolNode o2) {
            if (ladderizeDirection) {
                int ret = o2.getSize() - o1.getSize();
                if (ret == 0) {
                    ret = (int) Math.signum(o2.getLength() - o1.getLength());
                }

                return ret;
            } else {
                int ret = o1.getSize() - o2.getSize();
                if (ret == 0) {
                    ret = (int) Math.signum(o1.getLength() - o2.getLength());
                }
                return ret;
            }
        }
    };

    private static boolean ladderizeDirection = false;

    private EvolNodeUtil() {

    }

    /**
     * 一个快捷的Util工具，用来遍历我们的树结构！然后对每个节点做一些操作！例如赋值 这个是internal node first的遍历！
     * IF是internal node first的缩写！
     */
    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> void recursiveIterateTreeIF(T rootNode, Consumer<T> func) {
        if (rootNode == null) {
            throw new IllegalArgumentException("Null happend!");
        }

        func.accept(rootNode);

        int childCount = rootNode.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                recursiveIterateTreeIF((T) rootNode.getChildAt(i), func);
            }
        }
    }

    /**
     *
     * 指定深度的遍历树，达到指定深度后就不再继续遍历子节点了
     *
     * @title iterateTreeWithDepth
     * @createdDate 2023-12-05 15:31
     * @lastModifiedDate 2023-12-05 15:31
     * @author yudalang, yjn
     * @since 1.7
     *
     * @param iterateDepth the depth of the tree
     * @param func the action to perform
     */
    public static <T extends EvolNode> void iterateTreeWithDepth(T rootNode, int iterateDepth, Consumer<T> func) {
        func.accept(rootNode);
        if (iterateDepth <= 0) {
            // 深度达到了，不用再遍历了。
            return;
        }
        // 继续遍历子节点
        int depth = iterateDepth - 1;
        int childCount = rootNode.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                iterateTreeWithDepth((T) rootNode.getChildAt(i), depth, func);
            }
        }
    }

    /**
     * <pre>
     *
     * 一是internal node first的遍历！
     * IF是internal node first的缩写！
     * 但是有时候遍历过程中可能会对树结构做一些操作，比如生成midNode。
     * 这时候childCount就变化了，需要调用这个方法才行，否则会产生空指针。
     * </pre>
     *
     * @param rootNode
     * @param func
     */
    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> void recursiveIterateTreeIFWithUpdatedChild(T rootNode, Consumer<T> func) {
        func.accept(rootNode);

        int childCount = rootNode.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                T childT = (T) rootNode.getChildAt(i);
                if (childT != null) {
                    recursiveIterateTreeIFWithUpdatedChild(childT, func);
                }
                childCount = rootNode.getChildCount();
            }
        }
    }

    /**
     * 一个快捷的Util工具，用来遍历我们的树结构！然后对每个节点做一些操作！例如赋值 这个是internal node first的遍历！
     * IF是internal node first的缩写！
     *
     * 这个方法可以根据传入的返回值停止。
     *
     * @param rootNode
     * @param func:    返回true时候不会再继续递归
     */
    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> void recursiveIterateTreeIFWithStop(T rootNode, Predicate<T> func) {
        boolean test = func.test(rootNode);

        if (test) {
            return;
        }

        int childCount = rootNode.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                recursiveIterateTreeIFWithStop((T) rootNode.getChildAt(i), func);
            }
        }
    }

    /**
     * 一个快捷的Util工具，用来遍历我们的树结构！然后对每个节点做一些操作！例如赋值 这个是leaf first的遍历！ LF是leaf first的缩写！
     *
     * @param rootNode
     * @param func
     */
    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> void recursiveIterateTreeLF(T rootNode, Consumer<T> func) {
        int childCount = rootNode.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                recursiveIterateTreeLF((T) rootNode.getChildAt(i), func);
            }
        }

        func.accept(rootNode);
    }

    public static <T extends EvolNode> void recursiveIterateTreeLFWithUpdatedChild(T rootNode, Consumer<T> func) {
        int childCount = rootNode.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                T childT = (T) rootNode.getChildAt(i);
                if (childT != null) {
                    recursiveIterateTreeIFWithUpdatedChild(childT, func);
                }
                childCount = rootNode.getChildCount();
            }
        }

        func.accept(rootNode);
    }

    /**
     * 字面意思
     */
    public static double getLengthFromNode2root(EvolNode node, EvolNode root) {
        double ret = node.getLength();

        EvolNode tempEvolNode = node;
        while (tempEvolNode.getParent() != root) {
            tempEvolNode = tempEvolNode.getParent();
            ret += tempEvolNode.getLength();
        }

        return ret;
    }

    /**
     * 将一个Node转化成另外一个Node, 注意这个过程包括创建一个对象。
     * @createdDate 2021-03-04 20:22
     * @lastModifiedDate 2021-03-04 20:22
     * @lastModifiedDate 2025-01-17 20:22
     * @author yudalang
     *
     * @param node
     * @param generateNewRootFunc
     * @return
     * @param <R>
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <R extends EvolNode, T extends EvolNode> R convertNode(T node, Function<T, R> generateNewRootFunc) {

        R ret = generateNewRootFunc.apply(node);

        int childCount = node.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                R convertNode = convertNode((T) node.getChildAt(i), generateNewRootFunc);
                ret.addChild(convertNode);
            }
        }
        return ret;
    }

    /**
     *
     * 初始化节点的size属性. 有时候这个size属性对于计算是很有帮助的。
     *
     * @title initializeSize
     * @createdDate 2021-03-16 10:27
     * @lastModifiedDate 2021-03-16 10:27
     * @author yudalang
     * @since 1.7
     *
     * @param root
     */
    public static void initializeSize(EvolNode root) {
        int childCount = root.getChildCount();

        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                EvolNode childAt = root.getChildAt(i);
                initializeSize(childAt);
            }
        }

        if (childCount == 0) {
            root.setSize(1);
        } else {
            int size = 0;
            for (int i = 0; i < childCount; i++) {
                EvolNode child = root.getChildAt(i);
                size += child.getSize();
            }

            root.setSize(size);
        }

    }

    public static <T extends EvolNode> List<T> getLeaves(T root) {
//		把EvolNodeUtil中的 getLeaves方法从默认的getLeavesByStackIteration 改为了 getLeavesByRecursive
        // 因为两者得出的 leaves顺序是不同的，可视化的时候经常用 getLeaves by recursive 而不是 stack
        // by stack就是层度优先遍历，而 recursive是深度优先遍历
//		return getLeavesByStackIteration(root);
        return getLeavesByRecursive(root);

    }

    public static <T extends EvolNode> Optional<T> getLeafByName(T root, String name) {
        T ret = null;
        List<T> leaves = getLeavesByRecursive(root);
        for (T t : leaves) {
            if (Objects.equals(t.getName(), name)) {
                ret = t;
                break;
            }
        }
        Optional<T> leavesByRecursive = Optional.ofNullable(ret);
        return leavesByRecursive;

    }

    /**
     * 利用递归的方式去遍历树，然后得到叶子。
     */
    public static <T extends EvolNode> List<T> getLeavesByRecursive(T rootNode) {
        List<T> leaves = new ArrayList<>(8192);
        recursive2getLeafNumber(rootNode, leaves);
        return leaves;
    }

    @SuppressWarnings("unchecked")
    private static <T extends EvolNode> void recursive2getLeafNumber(T node, List<T> leaves) {
        int childCount2 = node.getChildCount();
        if (childCount2 == 0) {
            leaves.add(node);
        } else {
            int childCount = node.getChildCount();
            for (int j = 0; j < childCount; j++) {
                recursive2getLeafNumber((T) node.getChildAt(j), leaves);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> List<T> getChildren(T node) {
        int childCount = node.getChildCount();
        List<T> ret = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            ret.add((T) node.getChildAt(i));
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> T[] getChildrenArray(T node) {
        int newLength = node.getChildCount();
        Class<? extends EvolNode> newType = node.getClass();
        T[] ret = (T[]) Array.newInstance(newType, newLength);
        for (int i = 0; i < newLength; i++) {
            ret[i] = (T) node.getChildAt(i);
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> T getChildrenAt(T node, int j) {
        return (T) node.getChildAt(j);
    }

    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> List<T> getParents(T node) {
        int parentCount = node.getParentCount();
        List<T> ret = new ArrayList<>();
        for (int i = 0; i < parentCount; i++) {
            ret.add((T) node.getParentAt(i));
        }
        return ret;
    }

    /**
     *
     * 很多时候我们都只有一个parent所以这里返回的是一个值。
     *
     *
     * @title getParent
     * @createdDate 2021-03-16 11:01
     * @lastModifiedDate 2021-03-16 11:01
     * @author yudalang
     * @since 1.7
     *
     * @param <T>
     * @param node
     * @return
     * @return Optional<T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> Optional<T> getParent(T node) {
        T ret = (T) node.getParent();
        return Optional.ofNullable(ret);
    }

    /**
     * 字面意思
     */
    public static <T extends EvolNode> List<T> getLeavesByStackIteration(T rootNode) {
        List<T> leaves = new ArrayList<>(8192);
        iterateByLevelTraverse(rootNode, node -> {
            if (node.getChildCount() == 0) {
                leaves.add(node);
            }
        });
        return leaves;
    }

    /**
     * 这个方式是非递归的，有个好处就是：可以中途直接退出！
     *
     * @param rootNode
     * @param func
     */
    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> void iterateByLevelTraverse(T rootNode, Consumer<T> func) {
        LinkedList<T> queue = new LinkedList<>();
        queue.offer(rootNode);
        while (!queue.isEmpty()) {
            T node = queue.poll();

            func.accept(node);

            int childCount = node.getChildCount();
            for (int i = 0; i < childCount; i++) {
                T child = (T) node.getChildAt(i);
                queue.offer(child);
            }
        }
    }

    /**
     *
     * 得到满足条件的节点，然后返回，并不再循环！
     */
    public static Optional<EvolNode> iterateByLevelTraverseWithBreakReturn(EvolNode rootNode,
                                                                           Predicate<EvolNode> func) {
        LinkedList<EvolNode> queue = new LinkedList<>();
        queue.offer(rootNode);
        while (!queue.isEmpty()) {
            EvolNode node = queue.poll();

            boolean test = func.test(node);
            if (test) {
                return Optional.of(node);
            }

            int childCount = node.getChildCount();
            for (int i = 0; i < childCount; i++) {
                EvolNode child = node.getChildAt(i);
                queue.offer(child);
            }
        }

        return Optional.empty();
    }

    /**
     * 字面意思: 得到第一个满足条件的节点
     */
    public static Optional<EvolNode> getFirstSpecificNodeAccording2Name(EvolNode root, String name) {
        if (Objects.isNull(name)) {
            return Optional.empty();
        }

        Predicate<EvolNode> predicate = new Predicate<EvolNode>() {
            @Override
            public boolean test(EvolNode t) {
                // 这样不用非空判断！
                if (name.equals(t.getName())) {
                    return true;
                }
                return false;
            }

        };

        return iterateByLevelTraverseWithBreakReturn(root, predicate);
    }

    /**
     * 不知道这个方法，还有没有用，这个complement指的是，先意会一下吧
     *
     * <pre>
     *
     * </pre>
     *
     * @param graphicsNode
     * @return
     */
    public static List<EvolNode> getComplementNodes(EvolNode graphicsNode) {
        List<EvolNode> ret = new ArrayList<>();
        iterate2getComplementNodes(ret, graphicsNode);
        return ret;
    }

    private static void iterate2getComplementNodes(List<EvolNode> ret, EvolNode graphicsNode) {
        List<EvolNode> siblings = getSiblings(graphicsNode);
        for (EvolNode tt : siblings) {
            ret.add(tt);
        }

        if (graphicsNode.getParentCount() != 0) {
            iterate2getComplementNodes(ret, graphicsNode.getParent());
        }
    }

    /**
     * 字面意思,没有考虑多个parent，不过大部分情况都不需要！
     */
    public static List<EvolNode> getSiblings(EvolNode node) {
        List<EvolNode> ret = new ArrayList<>();

        if (node.getParentCount() == 0) {
            return ret;
        }

        EvolNode parent = node.getParent();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            EvolNode child = parent.getChildAt(i);
            if (child != node) {
                ret.add(child);
            }
        }
        return ret;
    }

    /**
     *
     * 读取代表拓扑结构的基本树。注意如果想要读取自定义的树，那么就要实现如何读取的类，自己用Decoder去解析！
     * 这里的是最基本的nwk格式字符串，只有名字和枝长。内节点放名字无效，只有叶子有效。
     * ((name1:2,name2:3):3,(name3:4,name4:5):2):0
     */
    public static EvolNode readBasicTreeFromFile(String nwkString) throws Exception {
        return new PrimaryNodeTreeDecoder<>().decode(nwkString);
    }

    /**
     * 入代表拓扑结构的基本树。注意如果想要读取自定义的树，那么就要实现如何读取的类，自己用Decoder去解析！
     * @createdDate 2020-10-20 15:43
     * @lastModifiedDate 2020-10-20 15:43
     * @lastModifiedDate 2025-01-17
     * @author "yudalang"
     *
     * @param root
     * @return the nwk string
     */
    public static String writeBasicTreeFromRoot(EvolNode root) {
        return new TreeCoder<EvolNode>().code(root);
    }

    public static void main(String[] args) throws Exception {
//		EvolNode root = readBasicTreeFromFile("((name1:2,name2:3):3,name3:3,(name4:4,name5:5):2):0");
        ArrayBasedNode root = (ArrayBasedNode) readBasicTreeFromFile("((name1:2,name2:3):3,name3:3,(name4:4,name5:5):2):0");

//		System.err.println(writeBasicTreeFromRoot(root));
//		
//		initializeSize(root);
//		
//		displayNodesWithoutGuiByTable(root, 100, node -> {
//			return node.getLength() + "";
//		});

//		System.out.println("");
//		displayNodesWithoutGuiByTextLines(root, 60, 20, true);
//		
//		ladderizeNodeAccording2size(root, true);
//		displayNodesWithoutGuiByTextLines(root, 60, 20, true);

//		DefaultNode[] childrenArray = getChildrenArray(root);
//		for (DefaultNode defaultNode : childrenArray) {
//			System.out.println(defaultNode.getName());
//		}

    }

    /**
     * 不同于用线条来显示这一棵树，这里用表格的形式来显示。
     *
     * @title displayNodesWithoutGui
     * @createdDate 2020-10-20 20:46
     * @lastModifiedDate 2020-10-20 20:46
     * @author "yudalang"
     *
     * @param tree
     * @return void
     */
    public static <T extends EvolNode> void displayNodesWithoutGuiByTable(T tree, int depth) {
		System.out.printf("No. of OTU is %d, No. of depth is %d.\n", getLeaves(tree).size(), EvolTreeOperator.getTreeDepth(tree));

        displayNodesWithoutGuiByTable(tree, depth, node -> {
            return "";
        });
    }

    /**
     * 递归地按表格形式显示节点信息，不使用GUI
     * 此方法用于以文本形式展示树形结构的节点信息，适用于调试和日志记录
     *
     * @param tree 泛型参数为T的树节点，T继承了EvolNode
     * @param depth 树的深度，用于控制递归的层数
     * @param func 一个函数接口，用于对树节点应用以获取额外信息
     */
    public static <T extends EvolNode> void displayNodesWithoutGuiByTable(T tree, int depth, Function<T, String> func) {
        if (depth < 1) {
            return;
        }
        int childCount = tree.getChildCount();

        StringJoiner stringBuilder = new StringJoiner("\t");
        stringBuilder.add(String.valueOf(tree.getID())).add(tree.getName());
		// add the length
		stringBuilder.add(String.valueOf(tree.getLength()));
        String apply = func.apply(tree);

        if (!Strings.isNullOrEmpty(apply)) {
            stringBuilder.add(apply);
        }
        stringBuilder.add("||");
        for (int i = 0; i < childCount; i++) {
            EvolNode child = tree.getChildAt(i);
            stringBuilder.add(String.valueOf(child.getID()));
            stringBuilder.add(child.getName()).add(";");
        }

        System.out.println(stringBuilder.toString());

        for (int i = 0; i < childCount; i++) {
            T child = getChildrenAt(tree, i);
            int newDepth = depth - 1;
            displayNodesWithoutGuiByTable(child, newDepth, func);
        }
    }



    /**
     * 用于快速搜索满足条件的节点 得到满足条件的节点，然后返回，并不再循环！
     */
    public static <T extends EvolNode> Optional<T> searchNodeWithReturn(T rootNode, Predicate<T> func) {
        LinkedList<T> queue = new LinkedList<>();
        queue.offer(rootNode);
        while (!queue.isEmpty()) {
            T node = queue.poll();

            boolean test = func.test(node);
            if (test) {
                return Optional.of(node);
            }

            int childCount = node.getChildCount();
            for (int i = 0; i < childCount; i++) {
                @SuppressWarnings("unchecked")
                T child = (T) node.getChildAt(i);
                queue.offer(child);
            }
        }

        return Optional.empty();
    }

    /**
     *
     * 得到第一个叶子，如果传入的点是叶子，那么就返回其本身。
     *
     * @title getFirstLeaf
     * @createdDate 2021-03-16 14:50
     * @lastModifiedDate 2021-03-16 14:50
     * @author yudalang
     * @since 1.7
     *
     * @param node
     * @return
     * @return EvolNode
     */
    public static EvolNode getFirstLeaf(EvolNode node) {
        EvolNode temp = node;

        while (temp.getChildCount() > 0) {
            temp = temp.getFirstChild();
        }
        return temp;
    }

    public static EvolNode getLastLeaf(EvolNode node) {
        EvolNode temp = node;

        while (temp.getChildCount() > 0) {
            temp = temp.getLastChild();
        }
        return temp;
    }

    /**
     *
     * 是否两个节点是继承的关系。
     *
     * <pre>
     *     |----
     * ----|ancestor
     *     |---------descendant
     *
     *     |------
     * ----|a   |------
     *     |----|
     *          |-------descendant
     * </pre>
     *
     * 第一个例子里面，应该返回1； 第二个例子里面，应该返回2
     *
     * @title isExtensionRelationship
     * @createdDate 2021-03-24 18:22
     * @lastModifiedDate 2021-03-24 18:22
     * @author yudalang
     * @since 1.7
     *
     * @param ancestor
     * @param descendant
     * @return int -1表示不是继承关系；否则返回隔了几代。
     */
    public static int isExtensionRelationship(EvolNode ancestor, EvolNode descendant) {
        int ret = -1;
        EvolNode temp = descendant;

        while (temp.getParentCount() > 0) {
            ret++;
            if (temp == ancestor) {
                return ret;
            }

            temp = temp.getParent();
        }

        return -1;
    }

    /**
     *
     * 获取node的sibling
     *
     * @title getSibling
     * @createdDate 2023-08-21 16:41
     * @lastModifiedDate 2023-08-21 16:41
     * @author yjn
     * @since 1.7
     *
     * @param <T>
     * @param node
     * @return
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> List<T> getSibling(T node) {
        List<T> ret = new ArrayList<>();
        EvolNode par = node.getParent();
        int currChildIndex = par.indexOfChild(node);
        int childCount = par.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i != currChildIndex) {
                ret.add((T) par.getChildAt(i));
            }
        }
        return ret;
    }

    /**
     *
     * @Title: swapNode
     * @Description: swap 一个节点下面的子节点；注意不单单是二叉树。
     *
     * @param: find
     * @return: void
     */
    public static void swapNodeWithSibling(EvolNode find) {
        EvolNode par = find.getParent();
        int currChildIndex = par.indexOfChild(find);
        int cc = par.getChildCount();
        if (cc > 2) {
            // nodes is the copy of par
            List<EvolNode> allChildren = new ArrayList<>(cc);
            for (int x = 0; x < cc; x++) {
                allChildren.add(par.getChildAt(x));
            }
            EvolNode child1 = par.getChildAt(currChildIndex);
            EvolNode child2 = null;
            if ((currChildIndex + 1) < cc) {
                child2 = par.getChildAt(currChildIndex + 1);
                par.removeAllChild();
                for (int n = 0; n < cc; n++) {
                    if (n == currChildIndex) {
                        par.addChild(child2);
                        par.addChild(child1);
                        n = n + 1;
                    } else {
                        par.addChild(allChildren.get(n));
                    }
                }
            } else {
                // this means swap the last node and the first node
                child2 = par.getChildAt(0);
                par.removeAllChild();
                par.addChild(child1);
                for (int n = 1; n < cc - 1; n++) {
                    par.addChild(allChildren.get(n));
                }
                par.addChild(child2);
            }
        } else {
            EvolNode child1 = par.getChildAt(0);
            EvolNode child2 = par.getChildAt(1);
            par.removeAllChild();
            par.addChild(child2);
            par.addChild(child1);
        }

    }

    /**
     * 注意，这个方法比较老了，用其它的方法。
     * @param currNode: 用户点击时的节点！
     * @param b         : true for up; false for down
     */
    public static void ladderizeNode(EvolNode currNode, boolean b) {
        TreeMap<Integer, List<EvolNode>> mapOfNodesInSameDeepth = new TreeMap<Integer, List<EvolNode>>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer obj1, Integer obj2) {
                        int factor = b ? 1 : -1;
                        return factor * obj1.compareTo(obj2);
                    }
                });

        ladderizeNode(currNode, mapOfNodesInSameDeepth);

    }

    private static void ladderizeNode(EvolNode currentNode, Map<Integer, List<EvolNode>> mapOfNodesInSameDeepth) {
        mapOfNodesInSameDeepth.clear();
        int childCount = currentNode.getChildCount();
        // If current node is leaf or all children is leaf,don't need to ladderize!
        if (currentNode.getChildCount() == 0 || isAllNodesAreLeaf(currentNode)) {
            return;
        }
        EvolNode tempNode = null;
        // if two or more child have same num of leaves, then add them to one List<Node>
        for (int i = 0; i < childCount; i++) {
            tempNode = currentNode.getChildAt(i);

            int leafNum = getLeaves(tempNode).size();

            if (mapOfNodesInSameDeepth.containsKey(leafNum)) {
                List<EvolNode> cnodes = mapOfNodesInSameDeepth.get(leafNum);
                cnodes.add(tempNode);
                mapOfNodesInSameDeepth.put(leafNum, cnodes);
            } else {
                List<EvolNode> cnodes = new ArrayList<>(3);
                cnodes.add(tempNode);
                mapOfNodesInSameDeepth.put(leafNum, cnodes);
            }
        }

        currentNode.removeAllChild();

        Iterator<Entry<Integer, List<EvolNode>>> mapIterator = mapOfNodesInSameDeepth.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Entry<Integer, List<EvolNode>> cnEntry = mapIterator.next();
            List<EvolNode> childrenOfCurrentNode = cnEntry.getValue();
            for (int m = 0; m < childrenOfCurrentNode.size(); m++) {
                EvolNode cnTempNode = childrenOfCurrentNode.get(m);
                cnTempNode.removeAllParent();
                currentNode.addChild(cnTempNode);
            }
        }
        for (int i = 0; i < childCount; i++) {
            ladderizeNode(currentNode.getChildAt(i), mapOfNodesInSameDeepth);
        }
    }

    private static boolean isAllNodesAreLeaf(EvolNode node) {
        boolean allIsLeaf = true;
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChildAt(i).getChildCount() != 0) {
                allIsLeaf = false;
                break;
            }
        }
        return allIsLeaf;
    }

    /**
     * 注意，用之前要初始化size属性
     *
     * @param root
     * @param upOrDwon
     */
    public static void ladderizeNodeAccording2size(EvolNode root, boolean upOrDwon) {
        ladderizeNodeAccording2sizeAndLength(root, upOrDwon);
    }

    /**
     * 注意，用之前要初始化size属性。当然还有length属性。
     *
     * @param root
     * @param upOrDwon
     */
    public static void ladderizeNodeAccording2sizeAndLength(EvolNode root, boolean upOrDwon) {
        List<EvolNode> children = getChildren(root);

        if (children.size() == 0) {
            return;
        }

        ladderizeDirection = upOrDwon;
        Collections.sort(children, sizeComparator);

        root.removeAllChild();
        for (EvolNode evolNode : children) {
            root.addChild(evolNode);
        }

        for (EvolNode evolNode : children) {
            ladderizeNodeAccording2sizeAndLength(evolNode, upOrDwon);
        }
    }

    public static <T extends EvolNode> List<T> getLeavesAccording2depth(T root, int neededDepth) {
        List<T> ret = new ArrayList<>();
        iterateInitializeTree(root, neededDepth, ret);
        return ret;
    }

    private static <T extends EvolNode> void iterateInitializeTree(T node, int depth, List<T> ret) {
        int childCount = node.getChildCount();

        if (childCount == 0 || depth == 0) {
            ret.add(node);
            return;
        }

        for (int i = 0; i < childCount; i++) {
            T child = getChildrenAt(node, i);
            int newDepth = depth - 1;
            iterateInitializeTree(child, newDepth, ret);
        }
    }

    /**
     * Not include node itself. Note: the order is from leaf to root.
     *
     */
    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> List<T> getAllParentsInTheLineage(T node) {
        List<T> ret = new ArrayList<>();

        T parent = (T) node.getParent();

        while (parent != null) {
            ret.add(parent);
            parent = (T) parent.getParent();
        }

        return ret;
    }

    /**
     * Get the path from specific node to node! <b>Note:</b> The parameter root
     * should be the ancestor of child!
     *
     * @param root  the start node, should be ancestor of child!
     * @param child the end node
     * @return an List of nodes from root to child( include root and child)!
     * @author yudalang
     */
    public static ArrayList<EvolNode> getPath(EvolNode root, EvolNode child) {
        ArrayList<EvolNode> arrayList = new ArrayList<EvolNode>();
        EvolNode current = child;
        while (current != root) {
            arrayList.add(current);
            current = current.getParent();
        }

        arrayList.add(root);
        return arrayList;
    }

}
