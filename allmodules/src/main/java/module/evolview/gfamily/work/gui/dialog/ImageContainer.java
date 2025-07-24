package module.evolview.gfamily.work.gui.dialog;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import egps2.builtin.modules.IconObtainer;

public class ImageContainer extends JPanel {

	private Image image;
	
	private String pathString = "annotation.png";
	
	public ImageContainer(String pathString) {
		this.pathString = pathString;
	}

	@Override
	protected void paintComponent(Graphics g) {
		int x = getWidth() / 2;
		int y = getHeight() / 2;
		Image image2 = getImage();
		int width2 = image2.getWidth(this) / 2;
		int height2 = image2.getHeight(this) / 2;
		g.drawImage(image2, x - width2 , y - height2, this);
	}

	public Image getImage() {
		if (image == null) {
			ImageIcon imageIcon = IconObtainer.get(pathString);
			image = imageIcon.getImage();
//			image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		}
		return image;
	}
}
