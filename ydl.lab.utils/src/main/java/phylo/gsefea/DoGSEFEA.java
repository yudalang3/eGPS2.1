package phylo.gsefea;

import analysis.AbstractAnalysisAction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import evoltree.struct.EvolNode;
import evoltree.struct.io.PrimaryNodeTreeDecoder;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.util.EvolTreeOperator;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsv.io.KitTable;
import tsv.io.TSVReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class DoGSEFEA extends AbstractAnalysisAction {

    private static final Logger log = LoggerFactory.getLogger(DoGSEFEA.class);
    String treeFilePath;
    String nodeGeneFormationCountFilePath;
    String outputFilePath;

    public DoGSEFEA(){

    }
    public DoGSEFEA(String treeFilePath, String nodeGeneFormationCountFilePath, String outputFilePath) {
        this.treeFilePath = treeFilePath;
        this.nodeGeneFormationCountFilePath = nodeGeneFormationCountFilePath;
        this.outputFilePath = outputFilePath;
    }

    public List<String> perform(String treeFilePath, String resultSummaryByNodesPath, String outputFilePath) throws Exception {
        String treeContents = Files.readString(Path.of(treeFilePath));
        final String ROOT_NAME = "Root";
        PrimaryNodeTreeDecoder primaryNodeTreeDecoder = new PrimaryNodeTreeDecoder();
        EvolNode root = primaryNodeTreeDecoder.decode(treeContents);
        root.setName(ROOT_NAME);
        root.setLength(0);

        List<String> outputs4console = Lists.newArrayList();
        double treeLength = EvolTreeOperator.getTreeLength(root);

        Map<String, Integer> nodeName2FormationCountsMap = getStringIntegerMap(resultSummaryByNodesPath);

        // Use the stream API to get the total counts
        int totalCounts = nodeName2FormationCountsMap.values().stream().mapToInt(Integer::intValue).sum();
        double evolutionaryRate = totalCounts / treeLength;
        outputs4console.add("# The evolutionary rate is " + evolutionaryRate);

        List<String> outputResults = Lists.newLinkedList();
        outputResults.addAll(outputs4console);
        outputResults.add("nodeName\tbranchLength\tobservedCounts\texpectedCounts\tpValue");

        EvolNodeUtil.recursiveIterateTreeIF(root, currNode -> {
            String nodeName = currNode.getName();

            if (Objects.equals(nodeName, ROOT_NAME)){
                return;
            }

            int geneGainOrLossCount = 0;
            Integer counts = nodeName2FormationCountsMap.get(nodeName);
            if (counts == null){
                log.warn("Node: " + nodeName + " is not found in the nodeGeneFormationCountFilePath file, assume it is 0.");
            }else {
                geneGainOrLossCount = counts;
            }


            double expectedCounts = currNode.getLength() * evolutionaryRate;
            if (expectedCounts <= 0) {
                outputResults.add(nodeName + "\t" + currNode.getLength() + "\t" + geneGainOrLossCount + "\t" + expectedCounts + "\t0.0");
            } else {
                double observedCounts = geneGainOrLossCount;
                // Help me use passion distribution to calculate the p-value using the apache commons math or else lib
                // for k >= observed counts
                // 使用 Apache Commons Math 库计算 p-value
                PoissonDistribution poissonDist = new PoissonDistribution(expectedCounts);
                double pValue = 1 - poissonDist.cumulativeProbability((int) observedCounts - 1);
                currNode.setDoubleVariable(pValue);

                StringJoiner sJ = new StringJoiner("\t");
                sJ.add(nodeName).add(String.valueOf(currNode.getLength()));
                sJ.add(String.valueOf(observedCounts)).add(String.valueOf(expectedCounts));
                sJ.add(String.valueOf(pValue));
                outputResults.add(sJ.toString());
            }

        });
        Files.write(Path.of(outputFilePath), outputResults);
        return outputs4console;
    }

    private static Map<String, Integer> getStringIntegerMap(String resultSummaryByNodesPath) throws IOException {
        KitTable kitTable = TSVReader.readTsvTextFile(resultSummaryByNodesPath);
        List<List<String>> contents = kitTable.getContents();
        //NodeName	counts	events
        Map<String, Integer> nodeName2Counts = Maps.newLinkedHashMap();
        for (List<String> content : contents) {
            int value = Integer.parseInt(content.get(1));
            String key = content.get(0);
            nodeName2Counts.put(key, value);
        }
        return nodeName2Counts;
    }

    @Override
    public void doIt() throws Exception {
        List<String> perform = perform(treeFilePath, nodeGeneFormationCountFilePath, outputFilePath);
        for (String s : perform){
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws Exception {
        String treeFilePath = args[0];
        String nodeGeneFormationCountFilePath = args[1];
        String outputFilePath = args[2];

        DoGSEFEA doGSEFEA = new DoGSEFEA(treeFilePath, nodeGeneFormationCountFilePath, outputFilePath);

        doGSEFEA.runIt();
    }
}
