package module.evolview.model.tree;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class HighlightProperties {

	private Map<CGBID, Color> nodes2highlight = new HashMap<>();

	public Map<CGBID, Color> getNodes2highlight() {
		return nodes2highlight;
	}
}
