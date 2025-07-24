package module.heatmap.images;

import java.net.URL;

import javax.swing.ImageIcon;

public class HeatmapImageObtainer {
	
	/**
	 * Load an icon as a resource from the "images" directory.
	 */
	public static ImageIcon get(String name) {
		return get(name, null);
	}

	public static ImageIcon get(String name, String description) {

		URL resource = HeatmapImageObtainer.class.getResource(name);
		
		ImageIcon imageIcon = null;
		if (description == null) {
			imageIcon = new ImageIcon(resource);
		} else {
			imageIcon = new ImageIcon(resource, description);
		}

		return imageIcon;
	}

}
