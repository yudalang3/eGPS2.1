package module.evolview.phylotree.visualization.primary.swing;

import java.util.Map;

import com.google.common.collect.Maps;
import module.evolview.phylotree.visualization.graphics.struct.CollapseProperty;

public class FastGraphicsProperties {

	public int verticalBlankLength = 40;
	public int leftHorizontalBlankLength = 100;
	public int rightHorizontalBlankLength = 180;

	public Map<String, CollapseProperty> collapsePropertyMaps = Maps.newHashMap();

}
