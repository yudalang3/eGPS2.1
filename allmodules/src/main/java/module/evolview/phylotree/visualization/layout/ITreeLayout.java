package module.evolview.phylotree.visualization.layout;

import java.awt.Graphics2D;

/**
 * The top interface for the tree layout.
 */
public interface ITreeLayout {
	
	/**
	 * The top-level layout method that needs to be implemented, which takes width and height as parameters and calculates the required positions.
	 *  
	 * @title calculateForPainting
	 * @createdDate 2020-11-05 16:39
	 * @lastModifiedDate 2020-11-05 16:39
	 * @author yudalang
	 * @since 1.7
	 *   
	 * @param width the width of the layout area
	 * @param height the height of the layout area
	 */
	void calculateForPainting(int width, int height);
	
	/**
	 * The top-level layout method that needs to be implemented, which directly paints based on the positions calculated by the above method.
	 *  
	 * @title paintGraphics
	 * @createdDate 2020-11-05 16:39
	 * @lastModifiedDate 2020-11-05 16:39
	 * @author yudalang
	 * @since 1.7
	 *   
	 * @param g2d the Graphics2D object to paint on
	 */
	void paintGraphics(Graphics2D g2d);
}