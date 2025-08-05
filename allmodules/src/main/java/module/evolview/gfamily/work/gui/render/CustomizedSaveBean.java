package module.evolview.gfamily.work.gui.render;

import java.util.ArrayList;
import java.util.List;

import module.evolview.model.enums.ColorScheme;

public class CustomizedSaveBean {
	
	int beforeRenderingIndex = ColorScheme.COUNTRIESREGIONS.getIndex();
	
	List<CustomizedRenderRecord> records = new ArrayList<>();

	/**
	 * @return the beforeRenderingIndex
	 */
	public int getBeforeRenderingIndex() {
		return beforeRenderingIndex;
	}

	/**
	 * @param beforeRenderingIndex the beforeRenderingIndex to set
	 */
	public void setBeforeRenderingIndex(int beforeRenderingIndex) {
		this.beforeRenderingIndex = beforeRenderingIndex;
	}

	/**
	 * @return the records
	 */
	public List<CustomizedRenderRecord> getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(List<CustomizedRenderRecord> records) {
		this.records = records;
	}
	
	

}
