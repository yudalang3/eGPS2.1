package module.vennplot.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

public class VennPlotImportBean {
	
	@JSONField(ordinal = 3)
	private Map<String, List<String>> content = new HashMap<>();
	
	@JSONField(ordinal = 2)
	private boolean ifGenomicRegion = false;
	
	@JSONField(ordinal = 1)
	private String visualizationStyle = "classic venn";

	public Map<String, List<String>> getContent() {
		return content;
	}

	public void setContent(Map<String, List<String>> content) {
		this.content = content;
	}

	public boolean isIfGenomicRegion() {
		return ifGenomicRegion;
	}

	public void setIfGenomicRegion(boolean ifGenomicRegion) {
		this.ifGenomicRegion = ifGenomicRegion;
	}

	public String getVisualizationStyle() {
		return visualizationStyle;
	}

	public void setVisualizationStyle(String visualizationStyle) {
		this.visualizationStyle = visualizationStyle;
	}
	
	

}
