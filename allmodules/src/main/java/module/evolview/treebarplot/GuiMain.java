package module.evolview.treebarplot;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;

import utils.string.EGPSStringUtil;
import egps2.utils.common.util.SaveUtil;
import egps2.frame.ModuleFace;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.gfamily.work.model.tree.TreeLayoutProperties;
import graphic.engine.guicalculator.BlankArea;
import module.evolview.moderntreeviewer.io.TreeParser4MTV;
import module.evolview.moderntreeviewer.io.TreePropertiesAssigner;
import module.evolview.treebarplot.gui.PaintingPanel;
import module.evolview.treebarplot.io.TreeBarplotImportInforBean;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;

@SuppressWarnings("serial")
public class GuiMain extends ModuleFace {

	private VOICM4TreeBarPlot voicm4TreeLeafInforObtainer;

	PaintingPanel paintingPanel;
	TreeBarplotImportInforBean evolTreeImportInfoBean;

	private JScrollPane jScollPanel;

	protected GuiMain(IModuleLoader moduleLoader) {
		super(moduleLoader);
		setLayout(new BorderLayout());
		jScollPanel = new JScrollPane(paintingPanel);
		jScollPanel.setBorder(null);
		add(jScollPanel, BorderLayout.CENTER);

		voicm4TreeLeafInforObtainer = new VOICM4TreeBarPlot(this);
	}

	@Override
	public boolean closeTab() {
		return false;
	}

	@Override
	public void changeToThisTab() {

	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		voicm4TreeLeafInforObtainer.doUserImportAction();
	}

	public VOICM4TreeBarPlot getVoicm4TreeLeafInforObtainer() {
		return voicm4TreeLeafInforObtainer;
	}

	@Override
	public boolean canExport() {
		return true;
	}

	@Override
	public void exportData() {
		new SaveUtil().saveData(paintingPanel, getClass());

	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Painting the tree and bar plot" };
	}

	public void recordTheFunctionInvoke() {
		recordFeatureUsed4user("Painting the tree and bar plot");
	}

	@Override
	public void initializeGraphics() {
		if (evolTreeImportInfoBean == null) {
			// 第一次打开模块
			importData();
			return;
		}
		
		TreeParser4MTV treeParser4MTV = new TreeParser4MTV();
		Optional<GraphicsNode> tree = Optional.empty();
		try {
			tree = treeParser4MTV.parseTree(evolTreeImportInfoBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!tree.isPresent()) {
			throw new InputMismatchException("The phylogentic tree file is not correct, pleace check.");
		}
		
		GraphicsNode rootNode = tree.get();
		TreeLayoutProperties treeLayoutProperties = new TreeLayoutProperties(rootNode);
		//初始化设置信息
		int width2 = getWidth();
		
		BlankArea blank_space = evolTreeImportInfoBean.getBlank_space();
		if (evolTreeImportInfoBean.getTreeRightBlankSpaceDivider() > 0) {
			int rightSpace = width2  / evolTreeImportInfoBean.getTreeRightBlankSpaceDivider();
			blank_space.setRight( rightSpace );
		}
		
		treeLayoutProperties.setBlankArea(blank_space);
		
		TreePropertiesAssigner treePropertiesAssigner = new TreePropertiesAssigner();
		treePropertiesAssigner.assign(treeLayoutProperties, evolTreeImportInfoBean);
		try {
			treePropertiesAssigner.assignGraphicsNodeEffects(rootNode, evolTreeImportInfoBean);
		} catch (IOException e) {
			e.printStackTrace();
		}

		paintingPanel = new PaintingPanel(treeLayoutProperties);
		paintingPanel.setRightBlank(evolTreeImportInfoBean.getRightBlank());
		
		
		final List<String> titles = new ArrayList<>();
		final List<double[]> dataValues = new ArrayList<>();
		
		importTheBarplotData(treeLayoutProperties, titles, dataValues);

		paintingPanel.setValues(dataValues);
		paintingPanel.setTitles(titles);
		
		
		jScollPanel.setViewportView(paintingPanel);
		
		SwingUtilities.invokeLater(()->{
			paintingPanel.initializeLeftPanel();
		});

	}

	protected void importTheBarplotData(TreeLayoutProperties treeLayoutProperties, final List<String> titles,
			final List<double[]> dataValues) {
		try {
			String barPlotDataFile = evolTreeImportInfoBean.getBarPlotDataFile();
			File file = new File(barPlotDataFile);
			List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
			Iterator<String> iterator = lines.iterator();
			String next = iterator.next();
			String[] headerSplits = EGPSStringUtil.split(next, '\t');
			for (int i = 1; i < headerSplits.length; i++) {
				titles.add(headerSplits[i]);
			}
			//我们用最笨的方法来 match 叶子名就行了，不需要用高效的编程方法
			HashMap<String, String[]> hashMap = new HashMap<>();
			while (iterator.hasNext()) {
				String next2 = iterator.next();
				String[] splits = EGPSStringUtil.split(next2, '\t');
				hashMap.put(splits[0], splits);
			}
			
			List<List<Double>> doubleLists = new ArrayList<>();

			// 初始化数组中的每个List<Double>
			for (int i = 0; i < titles.size(); i++) {
				doubleLists.add(new ArrayList<>());
			}
			
			List<GraphicsNode> leaves = treeLayoutProperties.getLeaves();
			for (GraphicsNode node : leaves) {
				String name = node.getName();
				String[] strings = hashMap.get(name);
				
				Objects.requireNonNull(strings, name.concat("\t string has not bar values, pleace check."));

				for (int i = 1; i < strings.length; i++) {
					int indexOfDoubleList = i - 1;
					List<Double> list = doubleLists.get(indexOfDoubleList);
					
					String str = strings[i];
					if ("Nan".equalsIgnoreCase(str)) {
						// yudalang:
						// 这里先暂时处理成 0
						Double double1 = 0.0;
						list.add(double1);
					}else {
						Double double1 = Double.parseDouble(str);
						list.add(double1);
					}
					
				}
			}
			
			//转成数组
			for (List<Double> values : doubleLists) {
				double[] doubleArray = values.stream().mapToDouble(Double::doubleValue).toArray();
				dataValues.add(doubleArray);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new InputMismatchException("The bar data file is not correct, pleace check.");
		}
	}

	public JScrollPane getjScollPanel() {
		return jScollPanel;
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
				return "The functionality is powered by the eGPS software.";
			}
		};
		return iInformation;
	}

}
