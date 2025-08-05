package module.evolview.model.tree;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 这里的 displayDoubles应该是 work space 中的 长度，减少可视化的计算
 * @author yudal
 *
 */
public class ButtomScaleBarDrawProperty {

	private List<String> displayedStrings = Lists.newArrayList();
	private List<Double> displayeDoubles = Lists.newArrayList();

	/**
	 * @return the displayedStrings
	 */
	public List<String> getDisplayedStrings() {
		return displayedStrings;
	}

	/**
	 * @return the displayeDoubles
	 */
	public List<Double> getDisplayeDoubles() {
		return displayeDoubles;
	}
	
	public void clear() {
		displayedStrings.clear();
		displayeDoubles.clear();
	}
	
	public void addElement(String str,double value) {
		displayedStrings.add(str);
		displayeDoubles.add(value);
	}
	
}
