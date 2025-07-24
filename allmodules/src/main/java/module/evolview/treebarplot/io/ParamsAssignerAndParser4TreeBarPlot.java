package module.evolview.treebarplot.io;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import egps2.EGPSProperties;
import module.evolview.moderntreeviewer.io.MTVImportInforBean;
import module.evolview.moderntreeviewer.io.ParamsAssignerAndParser4ModernTreeView;
import egps2.builtin.modules.voice.bean.VoiceValueParameterBean;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class ParamsAssignerAndParser4TreeBarPlot extends ParamsAssignerAndParser4ModernTreeView {

	public ParamsAssignerAndParser4TreeBarPlot() {
		// 去掉一些输入方式
		requiredParams.remove("input.nwk.string");
		requiredParams.remove("input.tableLike.path");

		// 需要重新排列Entries的顺序
		List<Entry<String, VoiceValueParameterBean>> alreadyMapBean = new LinkedList<>(requiredParams.entrySet());

		requiredParams.clear();

		String propertiesDir = EGPSProperties.PROPERTIES_DIR;
		addKeyValueEntryBean("input.nwk.path", propertiesDir + "/bioData/example/treeBarplot/01.speci.nwk",
				"Input the nwk file path.");
		addKeyValueEntryBean("bar.plot.data.file",
				propertiesDir + "/bioData/example/treeBarplot/02_species_meta_info.txt",
				"The file with tsv format contains header line.");
		addKeyValueEntryBean("blank.space", "20,20,200,20",
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

	public TreeBarplotImportInforBean getImportBeanInfo(OrganizedParameterGetter str) {

		// 得到进化树的一些设置属性
		MTVImportInforBean object = this.generateTreeFromKeyValue(str);

		TreeBarplotImportInforBean treeBarplotImportInforBean = new TreeBarplotImportInforBean(object);

		treeBarplotImportInforBean.setBarPlotDataFile(str.getSimplifiedString("bar.plot.data.file"));

		return treeBarplotImportInforBean;
	}
}
