package module.vennplot.model;

import java.util.ArrayList;
import java.util.List;

public class SingleRegionDataModel {
	
	List<SingleRegionElement> eles = new ArrayList<SingleRegionElement>();
	int higestNumber;
	int expendHigestNumber = 500 ;
	String title = "Set size";
	
	/**
	 * 注意：这个eles会按照count数从小到大进行排序，所以这个indexes会变化
	 */
	int[] indexes;
	
	public void setIndexes(int[] indexes) {
		this.indexes = indexes;
	}
	
	public int[] getIndexes() {
		return indexes;
	}
	
	public List<SingleRegionElement> getEles() {
		return eles;
	}
	public int getHigestNumber() {
		return higestNumber;
	}
	public void setHigestNumber(int higestNumber) {
		this.higestNumber = higestNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getExpendHigestNumber() {
		return expendHigestNumber;
	}
	public void setExpendHigestNumber(int expendHigestNumber) {
		this.expendHigestNumber = expendHigestNumber;
	}

	public byte[] getBooleansAfterSort(byte[] booleans) {
		int length = booleans.length;
		byte[] ret = new byte[length];
		for (int i = 0; i < length; i++) {
			ret[i] = booleans[indexes[i]];
		}
		return ret;
	}
	
	

}
