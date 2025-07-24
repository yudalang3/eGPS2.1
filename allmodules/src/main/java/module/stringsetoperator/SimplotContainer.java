package module.stringsetoperator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import module.evolview.gfamily.work.gui.DrawUtil;

public class SimplotContainer extends JPanel{
	
	DrawUtil drawUtil = new DrawUtil();
	private List<Point2D> oneLinePaintingData;
	
	final int totalNumberOfPoints = 100;
	final int interval = 10;
	
	private GeneralPath generalPath = new GeneralPath();
	private Image image;
	
	public SimplotContainer() {
		setBorder(new EmptyBorder(20, 20, 20, 20));
		setBackground(Color.WHITE);
		
		image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("hero.gif"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel textLabel = new JLabel("Tutorial video");
		textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(textLabel);
		
		Component component = Box.createVerticalStrut(15);
		add(component);
		
		JLabel jLabel = new JLabel("");
		jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		jLabel.setIcon(new ImageIcon(image));
		add(jLabel);
		
		
	}
	
//	@Override
//	public void paint(Graphics g) {
//		Graphics2D g2d = (Graphics2D) g;
		
//		int width = getWidth();
//		int height = getHeight();
//		
//		double maxXValue = totalNumberOfPoints * interval + 1;
//		
//		double ratio = width / maxXValue;
//		
//		int size = oneLinePaintingData.size();
//		for (int i = 0; i < size; i++) {
//			
//			Point2D point2d = oneLinePaintingData.get(i);
//			if (i == 0) {
//				generalPath.reset();
//				generalPath.moveTo(point2d.getX() * ratio, height - height * point2d.getY() );
//			}else {
//				generalPath.lineTo(point2d.getX() * ratio, height - height * point2d.getY() );
//			}
//		}
//		
//		g2d.setColor(Color.red);
//		g2d.draw(generalPath);
//		
//		g2d.drawImage(image, 50, 20, null);
		
//		g2d.dispose();
//	}

}
