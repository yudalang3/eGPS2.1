package module.mutationpre.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

import module.mutationpre.model.DrawConnectingLineElement;
import module.mutationpre.model.DrawMutationElement;
import module.mutationpre.model.DrawPropertiesMutation;
import module.mutationpre.model.DrawVariantsProperty;

public class MutationPainter {

	DrawPropertiesMutation property;

	public void paint(Graphics2D g2d) {
		
		if (property == null) {
			return;
		}
		
		// =========== For debug
//		g2d.setColor(Color.magenta);
//		g2d.fillRect(property.leftMostX, 390, property.drawWidth, 5);
		// =========== For debug

		g2d.setFont(property.mutationFont);
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int height = fontMetrics.getMaxAscent();
		float halfFontHeight = (float) (0.5 * height);

		List<DrawConnectingLineElement> listOfMutationElement = property.listofConnectingLineElements;
		int lastIndex = listOfMutationElement.size() - 1;
		List<DrawVariantsProperty> listOfDrawVariants = property.listOfDrawVariants;

		boolean isFirstVariant = true;
		int variantInterval = 3;
		
		Iterator<DrawVariantsProperty> iterator = listOfDrawVariants.iterator();
		while (iterator.hasNext()) {
			double degree = property.degree;
			if (isFirstVariant && property.showPositionCoordinate) {
				degree = property.positionCoordinateDegree;
			}
			boolean ifDegreeZero = degree == 0;
			
			DrawVariantsProperty drawVariantsProperty = iterator.next();
			List<DrawMutationElement> elements = drawVariantsProperty.listOfMutationElement;
			
			// 得到一个Varinat里面最长的
			int novelVariantInterval = 0;
			// 用来在绘制Variant的名字的辅助标记
			int index = 0;
			for (DrawMutationElement mutationElement : elements) {
				DrawConnectingLineElement drawMutationElement = listOfMutationElement.get(index);

				Point rotationPoint = mutationElement.rotationPoint;
				rotationPoint.x = drawMutationElement.locationInMutationArea.x;
				rotationPoint.y = drawMutationElement.locationInMutationArea.y + variantInterval;
				Point locationInMutationArea = rotationPoint;

				
				int stringWidth = fontMetrics.stringWidth(mutationElement.name);
				if (stringWidth > novelVariantInterval) {
					novelVariantInterval = stringWidth;
				}

				int x = locationInMutationArea.x - stringWidth;
				if (ifDegreeZero) {
					x += (0.5 * stringWidth + 2);
				}
				int y = (int) (locationInMutationArea.y + halfFontHeight);

				g2d.setColor(mutationElement.color);
				
				g2d.rotate(degree, locationInMutationArea.x, locationInMutationArea.y);

				if (!mutationElement.name.isEmpty()) {
					g2d.drawString(mutationElement.name, x, y);
				}

				Rectangle rectangle = mutationElement.rectangle;
				rectangle.setFrame(x - 3, locationInMutationArea.y - halfFontHeight, stringWidth + 7, height + 5);
				if (mutationElement.selected) {
					g2d.setColor(Color.blue);
					g2d.draw(rectangle);
				}
				if (mutationElement.highlight == -1) {
					g2d.setColor(new Color(123, 123, 0, 40));
					g2d.fill(rectangle);
				}
				g2d.rotate(-degree, locationInMutationArea.x, locationInMutationArea.y);

				// =========== For debug
//				g2d.setColor(Color.darkGray);
//				g2d.fillOval(locationInMutationArea.x, locationInMutationArea.y, 2, 2);
				// =========== For debug

				if (index == lastIndex) {
					g2d.setColor(Color.black);
					String variantName = drawVariantsProperty.variantName;

					if (ifDegreeZero) {
						g2d.drawString(variantName, property.leftMostX - fontMetrics.stringWidth(variantName) - 30,
								(int) (locationInMutationArea.y + halfFontHeight));
					} else {

						g2d.drawString(variantName, property.leftMostX - fontMetrics.stringWidth(variantName) - 30,
								(int) (locationInMutationArea.y + height));
					}
				}
				index++;
			}

			// 应该再增加一点距离
			variantInterval += (novelVariantInterval * Math.sin(- degree) + 12);
			novelVariantInterval = 0;

			isFirstVariant = false;
		}
		
		// 留给开发者，如果想高亮某一篇区域
//		g2d.setColor(new Color(0, 0, 255 , 30));
//		g2d.fillRect(500, 280, 420, 450);
	}

	public void calculate(int width, int height) {
		// TODO Auto-generated method stub

	}

	public void setProperty(DrawPropertiesMutation property) {
		this.property = property;
	}
}
