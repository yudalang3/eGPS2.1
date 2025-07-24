package module.evolview.pathwaybrowser.io;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import egps2.EGPSProperties;
import module.evolview.moderntreeviewer.io.MTVImportInforBean;
import module.evolview.moderntreeviewer.io.ParamsAssignerAndParser4ModernTreeView;
import egps2.builtin.modules.voice.bean.VoiceValueParameterBean;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class ParamsAssignerAndParser4pathwayFamBrowser extends ParamsAssignerAndParser4ModernTreeView {

	public ParamsAssignerAndParser4pathwayFamBrowser() {
		// 去掉一些输入方式
		requiredParams.remove("input.nwk.string");
		requiredParams.remove("input.tableLike.path");

		// 需要重新排列Entries的顺序
		List<Entry<String, VoiceValueParameterBean>> alreadyMapBean = new LinkedList<>(requiredParams.entrySet());

		requiredParams.clear();
		String propertiesDir = EGPSProperties.PROPERTIES_DIR;
		addKeyValueEntryBean("input.nwk.path",
				propertiesDir + "/bioData/example/9_model_species_evolution.nwk",
				"Input the nwk file path.");
		addKeyValueEntryBean("components.info.path",
				propertiesDir + "/bioData/pathwayBrowser/components.info.tsv",
				"Input the file path of the components, tsv format. Must has the gene column and category column.");
		addKeyValueEntryBean("gene.column.name", "name", "The column name of the gene symbol or ids");
		addKeyValueEntryBean("category.column.name", "category",
				"The column name of the category in the tsv file.");
		addKeyValueEntryBean("pathway.details.figure",
				propertiesDir + "/bioData/pathwayBrowser/wnt_pathway2.pptx",
				"Input the file path of the components, format is the pptx and the shape id is same as the gene name.");
		addKeyValueEntryBean("pathway.statistics.figure",
				propertiesDir + "/bioData/pathwayBrowser/pathway_counts.pptx",
				"Input the file path of the category counts, format is the pptx and the shape id is same as the category name.");
		addKeyValueEntryBean("evolutionary.selection.figure",
				propertiesDir + "/bioData/pathwayBrowser/demo_selection.pptx",
				"Input the file path of the evolutionary selection pressure, the figure is pre-culculated, format is the pptx.");

		addKeyValueEntryBean("^", "", "");
		addKeyValueEntryBean("@", "The Pathway Family Browser", "");
		addKeyValueEntryBean("gene.name.separater", "_",
				"The gene symbol or ids separater, default is _. For different components like arrow, textbox and shape.");
		addKeyValueEntryBean("blank.space", "20,20,100,20",
				"The blank area of top,left,bottom,right. Note: right will take effects if divider = 0");
		addKeyValueEntryBean("need.reverse.axis.bar", "T",
				"Whether reverse the axis bar. In some cases, the time is years ago.");
		addKeyValueEntryBean("bottom.title.string", "The phylogenetic tree with {0} high-quality sequenced samples.",
				"The statement that displayed in the bottom of the tree view. {0} will be replace with number of leaf.");

		for (Entry<String, VoiceValueParameterBean> entry : alreadyMapBean) {
			if (requiredParams.get(entry.getKey()) != null) {
				// 这一句一定要有，否则被覆盖
				continue;
			}
			requiredParams.put(entry.getKey(), entry.getValue());
		}

	}

	public PathwayFamilyBrowserImportInfoBean getImportBeanInfo(OrganizedParameterGetter str) {
		// 得到进化树的一些设置属性
		MTVImportInforBean object = this.generateTreeFromKeyValue(str);
		PathwayFamilyBrowserImportInfoBean bean = new PathwayFamilyBrowserImportInfoBean(object);

		bean.geneNameSeparater = str.getSimplifiedString("gene.name.separater");
		bean.componentsInfoPath = str.getSimplifiedString("components.info.path");
		bean.geneColumnName = str.getSimplifiedString("gene.column.name");
		bean.categoryColumnName = str.getSimplifiedString("category.column.name");
		bean.pathwayDetailsFigure = str.getSimplifiedString("pathway.details.figure");
		bean.pathwayStatisticsFigure = str.getSimplifiedString("pathway.statistics.figure");
		bean.evolutionarySelectionFigurePath = str.getSimplifiedString("evolutionary.selection.figure");

		return bean;
	}
}
