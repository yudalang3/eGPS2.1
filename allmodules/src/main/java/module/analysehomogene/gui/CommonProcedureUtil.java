package module.analysehomogene.gui;

import org.apache.commons.io.FileUtils;

import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CommonProcedureUtil {

    public static final EvolNode getRootNode(String pathOfPhylogeneticTree) throws Exception {
        final String defaultRootNodeName = "Root";

        String nwkString = FileUtils.readFileToString(new File(pathOfPhylogeneticTree), StandardCharsets.US_ASCII);
        TreeDecoder decode = new TreeDecoder();
        EvolNode root = decode.decode(nwkString);
        if (root.getName() == null) {
            root.setName(defaultRootNodeName);
        }
        return root;
    }
}
