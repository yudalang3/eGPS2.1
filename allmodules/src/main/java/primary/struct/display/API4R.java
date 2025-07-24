package primary.struct.display;

import primary.struct.display.data.GeneData;
import primary.struct.display.drawer.DraggableRectangleDrawer;

import javax.swing.JFrame;

public class API4R {

	/**
	 * 
	 * @param jsonString
	 * @param w
	 * @param h
	 */
	public void draw_multiple_genes(String jsonString, int w, int h, int geneHeight) {

		try {
			// 解析JSON字符串为GeneData对象
			GeneData geneData = new GeneData(jsonString);
			DraggableRectangleDrawer panel = new DraggableRectangleDrawer(geneData,geneHeight);
			JFrame frame = new JFrame("Draggable Rectangle Drawer");
			frame.add(panel);
			frame.setSize(w, h);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

	}
}
