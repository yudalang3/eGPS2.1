package module.evolview.pathwaybrowser;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.Maps;
import com.jidesoft.swing.JideTabbedPane;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.util.SaveUtil;
import egps2.EGPSProperties;
import tsv.io.TSVReader;
import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.GeneFamilyMainFace;
import egps2.builtin.modules.IconObtainer;
import module.evolview.gfamily.work.gui.browser.BrowserPanel;
import module.evolview.gfamily.work.gui.tree.PhylogeneticTreePanel;
import module.evolview.gfamily.work.listener.TreeListener;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.model.tree.TreeLayoutProperties;
import module.evolview.pathwaybrowser.gui.EvoSelectionPressurePanel;
import module.evolview.pathwaybrowser.gui.PathwayDetailsPanel;
import module.evolview.pathwaybrowser.gui.PathwayStatisticsPanel;
import module.evolview.pathwaybrowser.io.PathwayFamilyBrowserImportInfoBean;
import module.evolview.pathwaybrowser.io.Voice4pathwayFamilyBrowser;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class PathwayFamilyMainFace extends GeneFamilyMainFace {


    private final Voice4pathwayFamilyBrowser voiceDataHandler = new Voice4pathwayFamilyBrowser(this);

    protected PathwayFamilyMainFace(IModuleLoader moduleLoader) {
        super(moduleLoader);
    }

	@Override
	protected String getSavedPersistentPath() {
		final String PERSISTENT_STORE_PATH = EGPSProperties.JSON_DIR.concat("/pathwayFamilyFace.json");
		return PERSISTENT_STORE_PATH;
	}


    @Override
    public boolean canImport() {
        return true;
    }

    @Override
    public void importData() {
        voiceDataHandler.doUserImportAction();
    }

    @Override
    public boolean canExport() {
        return true;
    }

    @Override
    public void exportData() {
        PhylogeneticTreePanel selectedPhylogeneticTreePanel = getSelectedPhylogeneticTreePanel();

        if (selectedPhylogeneticTreePanel == null) {
            SwingDialog.showErrorMSGDialog(UnifiedAccessPoint.getInstanceFrame(), "Export error",
                    "There are no data in the remnant,\nPlease input data first.");
        } else {
			JideTabbedPane tabbedAnalysisPanel = getTabbedAnalysisPanel();
            int tabCount = tabbedAnalysisPanel.getTabCount();
            if (tabCount > 0) {
                BrowserPanel selectedBrowserPanel = getSelectedBrowserPanel();
                SaveUtil saveUtil = new SaveUtil();
				saveUtil.saveData(selectedBrowserPanel.getContextPanel(), getClass());
				Optional<File> userSelectedFile = saveUtil.getUserSelectedFile();
				if (userSelectedFile.isPresent()) {
					File file = userSelectedFile.get();
					File parentFile = file.getParentFile();

				}
            }
        }
    }

    @Override
    public String[] getFeatureNames() {
		return new String[]{"Gene family visualization"};
    }

	@Override
	public void initializeGraphics() {
		super.initializeGraphics();
	}

	@Override
	public void initializeTheModule() {
		super.initializeTheModule();
    }

    /**
     * The main frame consist of following elements:
     *
     * <pre>
     *
     * 首先是垂直上的构造
     *
     *
     * Shell Panel
     *
     * ControlPanel
     *     |---------------------------------------|
     *     |                         |             | TreePanel
     *     |                         |             |
     *     |                         |             |
     *     |                         |             |
     *     |                         | ----------- |
     *     |                         |             | BrowserPanel
     *     |                         |             |
     *     |                         |             |
     *     |---------------------------------------|
     *
     *
     *
     * </pre>
     */
	public void initializeTheModule(PathwayFamilyBrowserImportInfoBean geneData,
			TreeLayoutProperties treeLayoutProperties) {

        // 这个导入过程，如果更改需要注意三处：VOICE4MTV类的execute()；GeneFamilyMainFace类的initialize和MTreeViewMainFace的initializeGraphics()

        GraphicsNode root = treeLayoutProperties.getOriginalRootNode();
        PhylogeneticTreePanel phylogeneticTreePanel = new PhylogeneticTreePanel(treeLayoutProperties, root, null, null);
        controller.setGlobalPhylogeneticTreePanel(phylogeneticTreePanel);

        controller.setTreeLayoutProperties(treeLayoutProperties);

        ImageIcon imageIcon = IconObtainer.get("global.png");

        JScrollPane jScrollPane = new JScrollPane(phylogeneticTreePanel);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());

		List<PhylogeneticTreePanel> existedPhylogeneticTreePanels = getExistedPhylogeneticTreePanels();

		JideTabbedPane tabbedAnalysisPanel = getTabbedAnalysisPanel();
		int tabCount = existedPhylogeneticTreePanels.size();
        if (tabCount > 0) {
			getTabbedPhylogeneticTreePane().removeAll();
            tabbedAnalysisPanel.removeAll();
        }
		getTabbedPhylogeneticTreePane().addTab("Phylogeny", imageIcon, jScrollPane, "The referenced phylogentic tree.");

		TreeListener treeListener = phylogeneticTreePanel.getTreeListener();
		List<Consumer<GraphicsNode>> tree2AnalyzingPanelInteractions = treeListener
				.getTree2AnalyzingPanelInteractions();

		String componentsInfoPath = geneData.getComponentsInfoPath();

		String geneColumnName = geneData.getGeneColumnName();
		String categoryColumnName = geneData.getCategoryColumnName();
		Map<String, List<String>> tableAsKey2ListMap = null;
		try {
			tableAsKey2ListMap = TSVReader.readAsKey2ListMap(componentsInfoPath);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Map<String, List<Short>> species2geneCountMap = Maps.newHashMap();
		List<String> geneList = tableAsKey2ListMap.remove(geneColumnName);
		List<String> categoryList = tableAsKey2ListMap.remove(categoryColumnName);
		{
			
			for (Entry<String, List<String>> entry : tableAsKey2ListMap.entrySet()) {
				List<String> value = entry.getValue();
				List<Short> array = Lists.newArrayList();
				for (String string : value) {
					array.add(Short.valueOf(string));
				}
				species2geneCountMap.put(entry.getKey(), array);
			}
		}
		

		{
			PathwayDetailsPanel wntPathwayPanel = new PathwayDetailsPanel(geneData.getPathwayDetailsFigure(),
					geneData.getGeneNameSeparater().charAt(0));
			wntPathwayPanel.setSpecies2CompMaps(geneList, species2geneCountMap);
			tree2AnalyzingPanelInteractions.add(n -> {
				wntPathwayPanel.clickToSpecies(n.getName());
			});
			JScrollPane jScrollPane2 = new JScrollPane(wntPathwayPanel);
			jScrollPane2.setBorder(null);
			tabbedAnalysisPanel.addTab("Pathway Details", IconObtainer.get("dna.png"),jScrollPane2 ,
					"The Wnt signalling pathway regulation details");

		}
		{
			PathwayStatisticsPanel wntPathwayPanel = new PathwayStatisticsPanel(geneData.getPathwayStatisticsFigure(),
					categoryColumnName);
			wntPathwayPanel.setSpeciesCategory2CompMaps(categoryList, species2geneCountMap);
			tree2AnalyzingPanelInteractions.add(n -> {
				wntPathwayPanel.clickToSpecies(n.getName());
			});

			JScrollPane jScrollPane2 = new JScrollPane(wntPathwayPanel);
			jScrollPane2.setBorder(null);
			tabbedAnalysisPanel.addTab("Pathway Statistics", IconObtainer.get("dna.png"),
					jScrollPane2,
					"The Wnt pathway components counts");

		}
		{
			EvoSelectionPressurePanel wntPathwayPanel = new EvoSelectionPressurePanel(
					geneData.getEvolutionarySelectionFigurePath());
			JScrollPane jScrollPane2 = new JScrollPane(wntPathwayPanel);
			tabbedAnalysisPanel.addTab("Evolutionary selection", IconObtainer.get("dna.png"), jScrollPane2,
					"The evolutionary selection presseure on the pathway on the genome");

		}
        SwingUtilities.invokeLater(() -> phylogeneticTreePanel.initializeLeftPanel());

        // ==========================================================================================================

    }


    public BrowserPanel getSelectedBrowserPanel() {
		JideTabbedPane tabbedAnalysisPanel = getTabbedAnalysisPanel();
        Component selectedComponent = tabbedAnalysisPanel.getSelectedComponent();

        if (selectedComponent == null) {
            return null;
        }
        BrowserPanel phylogeneticTreePanel = (BrowserPanel) selectedComponent;
        return phylogeneticTreePanel;
    }



    @Override
    public IInformation getModuleInfo() {
        IInformation iInformation = new IInformation() {

            @Override
            public String getWhatDataInvoked() {
                return "The data is loading from the import dialog.";
            }

            @Override
            public String getSummaryOfResults() {
				return "The phylogenetic tree with pathway info. is generated by the eGPS software.";
            }
        };
        return iInformation;
    }

}
