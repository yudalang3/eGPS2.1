package module.evolview.gfamily.work.io;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import egps2.EGPSProperties;
import module.evolview.genebrowser.io.MapperOfGeneBrowser;
import module.evolview.gfamily.work.beans.RequiredGeneData;
import module.evolview.moderntreeviewer.io.MTVImportInforBean;
import module.evolview.moderntreeviewer.io.ParamsAssignerAndParser4ModernTreeView;
import egps2.builtin.modules.voice.bean.VoiceValueParameterBean;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class ParamsAssignerAndParser4GeneFamilyBrowser extends ParamsAssignerAndParser4ModernTreeView {

	private MTVImportInforBean mtvImportInforBean;

	private RequiredGeneData requiredGeneData;

	private MapperOfGeneBrowser mapperOfGeneBrowser = new MapperOfGeneBrowser();

	public ParamsAssignerAndParser4GeneFamilyBrowser() {
		super();

		// 去掉一些输入方式
		requiredParams.remove("input.nwk.string");
		requiredParams.remove("input.tableLike.path");

		// 需要重新排列Entries的顺序
		List<Entry<String, VoiceValueParameterBean>> alreadyMapBean = new LinkedList<>(requiredParams.entrySet());

		requiredParams.clear();
		String propertiesDir = EGPSProperties.PROPERTIES_DIR;
		addKeyValueEntryBean("input.nwk.path", propertiesDir + "/bioData/gfamily/input.tree.nwk",
				"Input the nwk file path.");

		// 假如 gBrowser的参数
		Map<String, VoiceValueParameterBean> parserMapBean2 = mapperOfGeneBrowser.getRequiredParams();
		VoiceValueParameterBean voiceValueParameterBean = parserMapBean2.get("browser.struct.annotation.path");
		voiceValueParameterBean.setValue(propertiesDir + "/bioData/gfamily/refGenomeInfor.txt");
		voiceValueParameterBean = parserMapBean2.get("alignment.file.path");
		voiceValueParameterBean.setValue(propertiesDir + "/bioData/gfamily/aligned.seq.fas");
		voiceValueParameterBean = parserMapBean2.get("browser.input.type");
		voiceValueParameterBean.setValue("1");

		VoiceValueParameterBean bean = new VoiceValueParameterBean();
		bean.setValue("Modern Tree View");
		requiredParams.put("@", bean);
		requiredParams.putAll(parserMapBean2);

		addKeyValueEntryBean("remove.whitespace", "F",
				"Whether remove whitespace (spaces, tabs, carriage returns, and linefeeds)");
		addKeyValueEntryBean("leaf.label.right.align", "F", "Whther right align the leaf labels.");

		addKeyValueEntryBean("blank.space", "20,20,80,20",
				"The blank area of top,left,bottom,right. Note: right will take effects if divider = 0");
		addKeyValueEntryBean("bottom.title.string", "The phylogenetic tree with {0} high-quality sequenced samples.",
				"The statement that displayed in the bottom of the tree view. {0} will be replace with numebr of leaf.");

		
		
		for (Entry<String, VoiceValueParameterBean> entry : alreadyMapBean) {
			if (requiredParams.get(entry.getKey()) != null) {
				// 这一句一定要有，否则被覆盖
				continue;
			}
			requiredParams.put(entry.getKey(), entry.getValue());
		}

	}

	public void obtainData(OrganizedParameterGetter str) {
		mtvImportInforBean = generateTreeFromKeyValue(str);
		requiredGeneData = mapperOfGeneBrowser.parseImportedData(str);
	}

	public MTVImportInforBean getMTVImportInfo() {
		return mtvImportInforBean;
	}

	public RequiredGeneData getRequiredGeneData() {
		return requiredGeneData;
	}

}
