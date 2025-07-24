/**
 *
 */
package module.analysehomogene.gui;

import java.util.HashSet;
import java.util.List;

import egps2.UnifiedAccessPoint;
import egps2.builtin.modules.voice.fastmodvoice.DockableTabModuleFaceOfVoice;
import egps2.frame.ComputationalModuleFace;
import module.parsimonytre.CLI;
import egps2.builtin.modules.voice.bean.AbstractParamsAssignerAndParser4VOICE;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class InferringInternalNodeStatesPanel extends DockableTabModuleFaceOfVoice {

    public InferringInternalNodeStatesPanel(ComputationalModuleFace cmf) {
        super(cmf);
    }

    @Override
    protected void setParameter(AbstractParamsAssignerAndParser4VOICE mapProducer) {
		mapProducer.addKeyValueEntryBean("@", "Analyse homogeneous with species tree: InferringInternalNodeStatesPanel",
				"");
        mapProducer.addKeyValueEntryBean("phylo.tree.path", "", "For more info. refer to https://www.yuque.com/u21499046/egpsdoc/pr5qx32gnxa53p36\n\nPath to the phylogenetic tree file. Necessary");
        mapProducer.addKeyValueEntryBean("leafStates.path", "", "Path to the leaf states TSV file. Necessary");
		mapProducer.addKeyValueEntryBean("leaf.identifier", "leafName",
				"The ID of the leaf name/specie name, i.e. the column name of the $leafStates.path, Default is leafName");
        mapProducer.addKeyValueEntryBean("preferStateOfRoot.path", "", "Optional. Preferred state of root when multiple states have the same score. Input tsv file similar to the $leafStates.path.");
        mapProducer.addKeyValueEntryBean("output.file.path", "", "The output file path.");
        mapProducer.addKeyValueEntryBean("exclude.states.file.columns", "", "Example: a;b;c . The columns to excludes for inference, default none: assume beside the leafID, these are positions.");
		mapProducer.addKeyValueEntryBean("^", "", "");
        mapProducer.addKeyValueEntryBean("display.results", "F", "Whether display the results in the GUI.");
    }

    @Override
    protected void execute(OrganizedParameterGetter o) throws Exception {
		String inputTree = o.getSimplifiedString("phylo.tree.path");
		String inputLeafStates = o.getSimplifiedString("leafStates.path");
		String outputFilePath = o.getSimplifiedString("output.file.path");
		boolean simplifiedValueExist = o.isSimplifiedValueExist("preferStateOfRoot.path");
		String preferStateOfRootPath = null;
		if (simplifiedValueExist){
			preferStateOfRootPath = o.getSimplifiedString("preferStateOfRoot.path");
		}
        boolean displayResults = o.getSimplifiedBool("display.results");
        List<String> excludeColNames = o.getEntriesAfterEqualChar("exclude.states.file.columns");
        UnifiedAccessPoint.promoteAtBottom(computationalModuleFace, "Inferring Internal Node States with Parsimony");

        long startTime = System.currentTimeMillis();
        // help me to masseuse the running time
        String simplifiedString = o.getSimplifiedString("leaf.identifier");
		List<String> run = CLI.run(inputTree, inputLeafStates, outputFilePath, preferStateOfRootPath,
                displayResults, simplifiedString, new HashSet<>(excludeColNames));

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        String timeMessage = "It takes time of " + elapsedTime + " millisecond";
        run.add(timeMessage);
        setText4Console(run);
    }




    @Override
    public String getShortDescription() {
        return null;
    }

    @Override
    public String getTabName() {
        return "1. Inferring Internal Node States with Parsimony";
    }
}
