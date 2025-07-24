package module.vennplot.io;

import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import com.alibaba.fastjson.JSONObject;

import module.vennplot.VennPlotDataImportLoader;
import module.vennplot.model.Element;
import module.vennplot.model.SetItem;
import egps2.builtin.modules.voice.VersatileOpenInputClickAbstractGuiBase;

public class VOICM4VennPlot extends VersatileOpenInputClickAbstractGuiBase {
	
	private int index = 0;
	private VennPlotDataImportLoader vennPlotDataImportLoader;
	
	public VOICM4VennPlot(VennPlotDataImportLoader vennPlotDataImportLoader) {
		this.vennPlotDataImportLoader = vennPlotDataImportLoader;
	}

	@Override
	protected int getNumberOfExamples() {
		return 2;
	}

	@Override
	public String getExampleText() {
		VennPlotImportBean bean = new VennPlotImportBean();
		
		if (index == 0) {
			Map<String, List<String>> content = new HashMap<>();
			content.put("Set1", Arrays.asList("a","b","c"));
			content.put("Set2", Arrays.asList("a","b","d"));
			content.put("Set3", Arrays.asList("a","g","h"));
			
			bean.setContent(content);
			bean.setIfGenomicRegion(false);
		}else {
			
			Map<String, List<String>> content = new HashMap<>();
			content.put("Set1", Arrays.asList("chr1,100,201","chr1,200,301","chr2,301,401","chr2,501,600"));
			content.put("Set2", Arrays.asList("chr2,301,401","chr2,501,600","chr3,300,401","chr4,450,500"));
			content.put("Set3", Arrays.asList("chr2,301,401","chr4,450,500"));
			
			bean.setContent(content);
			bean.setIfGenomicRegion(true);
			
			index = -1;
		}
		
		String jsonString = JSONObject.toJSONString(bean, true);
		
		index ++;
		return jsonString;
	}

	@Override
	public void execute(String inputs) {
		VennPlotImportBean object = JSONObject.parseObject(inputs, VennPlotImportBean.class);
		
		boolean ifGenomicRegion = object.isIfGenomicRegion();
		Map<String, List<String>> content = object.getContent();
		int numOfSets = content.size();
		
		
		SetItem[] setItems = new SetItem[numOfSets];
		int index = 0;
		for (Entry<String, List<String>> entry : content.entrySet()) {
			SetItem setItem = new SetItem();
			setItem.setName(entry.getKey());
			List<String> value = entry.getValue();
			int length = value.size();
			Element[] elements = new Element[length];
			setItem.setSetElements(elements);
			setItems[index] = setItem;
			for (int j = 0; j < length; j++) {
				String line = value.get(j);
				Element element = new Element(line);
				element.setIfGenomicRegion(ifGenomicRegion);
				elements[j]  = element;
			}
			
			index ++;

		}
		
		vennPlotDataImportLoader.loadData4thisModule(setItems);
		
		
		Component root = SwingUtilities.getRoot(vennPlotDataImportLoader);
		if (root instanceof JDialog) {
			((JDialog) root).dispose();
		}

	}

}
