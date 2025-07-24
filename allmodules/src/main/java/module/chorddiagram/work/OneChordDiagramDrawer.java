package module.chorddiagram.work;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import graphic.engine.drawer.SectorRingDrawer;
import graphic.engine.guicalculator.GuiCalculator;

public class OneChordDiagramDrawer {
	// Define the gap between entities as a percentage of 360 degrees
	private final double ENTITY_GAP_PERCENTAGE = 0.02; // 2% of the circle
	private final double LABEL_OFFSET = 50; // Offset for entity labels
	private final float DEFAULT_LINE_WIDTH = 2.0f; // Default line width for chords
	private double drawStartDegre = 0;

	private SectorRingDrawer sectorRingDrawer = new SectorRingDrawer();

	public void draw(Graphics2D g2d, ChordDiagram4WntBean chordDiagram, Rectangle rectangle) {
		if (chordDiagram == null || chordDiagram.getEntities() == null || chordDiagram.getInteractionList() == null) {
			return;
		}

		List<BioEntity> entities = chordDiagram.getEntities();
		List<BioInteraction> interactions = chordDiagram.getInteractionList();
		int size = entities.size();

		// Calculate the total length of all entities for proportion calculation,
		// including gaps
		int totalLength = entities.stream().mapToInt(BioEntity::getLength).sum();
		double totalGapDegrees = ENTITY_GAP_PERCENTAGE * 360;

		double gapDegree = totalGapDegrees / size;
		double availableAngle = 360 - totalGapDegrees; // Subtract gaps from 360

		// Define a circle for the chord diagram and thickness of entities
		double outerRadius = Math.min(rectangle.width, rectangle.height) / 2 - 10; // leave some padding
		double innerRadius = outerRadius - 20; // Thickness of 20 pixels
		double centerX = rectangle.x + rectangle.width / 2;
		double centerY = rectangle.y + rectangle.height / 2;

		// Draw arcs for each entity with gaps and labels
		double startAngle = drawStartDegre;

//		{
//			GeneralPath secter = sectorRingDrawer.getSectorRingGeneralPath(g2d, centerX, centerY, innerRadius,
//					outerRadius, startAngle, 30);
//			g2d.fill(secter);
//		}
//
//		if (true) {
//			return;
//		}

		for (int i = 0; i < size; i++) {
			BioEntity entity = entities.get(i);
			double angle = availableAngle * entity.getLength() / totalLength;

			GeneralPath secter = sectorRingDrawer.getSectorRingGeneralPath(g2d, centerX, centerY, innerRadius,
					outerRadius, startAngle, angle);

			g2d.setColor(entity.getColor());
			g2d.fill(secter);

			// Calculate the midpoint angle for the label
			double midAngle = startAngle + 0.5 * angle;
			Point2D labelPoint = pointOnCircle(centerX, centerY, outerRadius + 20, midAngle);

			// Draw the label
			String label = entity.getName();
			FontMetrics metrics = g2d.getFontMetrics();
			Rectangle2D stringBounds = metrics.getStringBounds(label, g2d);
			double labelX = labelPoint.getX() - stringBounds.getWidth() / 2;
			double labelY = labelPoint.getY() + stringBounds.getHeight() / 4;
			g2d.setColor(Color.BLACK); // Label color
			g2d.drawString(label, (float) labelX, (float) labelY);

			// Store the start angle for this entity
			entity.setStartAngle(startAngle);
			entity.setAvaliableAngle(angle);

			// Move start angle to the beginning of the next gap
			startAngle += (angle + gapDegree);
		}

		// Draw chords for each interaction
		for (BioInteraction interaction : interactions) {
			BioEntity source = interaction.getSource();
			BioEntity target = interaction.getTarget();

			// Calculate angles for source and target positions, considering gaps
			double sourceStartAngle = calculatePositionAngle(source, interaction.getSourceStartPosition());
			double sourceEndAngle = calculatePositionAngle(source, interaction.getSourceEndPosition());
			double targetStartAngle = calculatePositionAngle(target, interaction.getTargetStartPosition());
			double targetEndAngle = calculatePositionAngle(target, interaction.getTargetEndPosition());

			// Calculate points on the circle for the start and end of the chords
			Point2D sourceStartPoint = pointOnCircle(centerX, centerY, innerRadius, sourceStartAngle);
			Point2D sourceMiddlePoint = pointOnCircle(centerX, centerY, innerRadius,
					0.5 * (sourceStartAngle + sourceEndAngle));
			Point2D sourceEndPoint = pointOnCircle(centerX, centerY, innerRadius, sourceEndAngle);
			Point2D targetStartPoint = pointOnCircle(centerX, centerY, innerRadius, targetStartAngle);
			Point2D targetEndPoint = pointOnCircle(centerX, centerY, innerRadius, targetEndAngle);
			Point2D targetMiddlePoint = pointOnCircle(centerX, centerY, innerRadius,
					0.5 * (targetStartAngle + targetEndAngle));

			Stroke originalStroke = g2d.getStroke();
			Paint originalPaint = g2d.getPaint();
			g2d.setStroke(new BasicStroke(DEFAULT_LINE_WIDTH));

			GeneralPath path = new GeneralPath();

			{

				Arc2D arc2 = new Arc2D.Double(centerX - innerRadius, centerY - innerRadius, 2 * innerRadius,
						2 * innerRadius, sourceStartAngle, sourceEndAngle - sourceStartAngle, Arc2D.OPEN);
				path.append(arc2, true);

				QuadCurve2D chord = new QuadCurve2D.Double(sourceEndPoint.getX(), sourceEndPoint.getY(), centerX,
						centerY, targetStartPoint.getX(), targetStartPoint.getY());

				path.append(chord, true);



			}
			{
				Arc2D arc2 = new Arc2D.Double(centerX - innerRadius, centerY - innerRadius, 2 * innerRadius,
						2 * innerRadius, targetStartAngle, targetEndAngle - targetStartAngle, Arc2D.OPEN);

				path.append(arc2, true);

				QuadCurve2D chord = new QuadCurve2D.Double(targetEndPoint.getX(), targetEndPoint.getY(), centerX,
						centerY, sourceStartPoint.getX(), sourceStartPoint.getY());
				path.append(chord, true);


			}
			path.closePath();


			g2d.setColor(Color.BLACK);
			g2d.draw(path);

			Color startColor = source.getColor(); // Replace with your start color
			Color endColor = target.getColor(); // Replace with your end color

			// Define a gradient paint
			// Assuming the gradient runs horizontally or vertically
			LinearGradientPaint gradientPaint = new LinearGradientPaint(
					sourceMiddlePoint, // Gradient start point
					targetMiddlePoint, // Gradient end point
					new float[] { 0.0f, 1.0f }, // Fractions for gradient transition
					new Color[] { startColor, endColor } // Gradient colors
			);

			// Set the gradient paint
			g2d.setPaint(gradientPaint);
			g2d.fill(path);
			// Restore the original stroke
			g2d.setStroke(originalStroke);
			g2d.setPaint(originalPaint);
		}
	}

	private double calculatePositionAngle(BioEntity entity, int position) {
		double startAngle = entity.getStartAngle();
		double avaliableAngle = entity.getAvaliableAngle();
		return startAngle + position / (double) entity.getLength() * avaliableAngle;
	}

// Helper method to get a point on the circle at a given angle
	private Point2D pointOnCircle(double centerX, double centerY, double radius, double angle) {
		return GuiCalculator.calculateCircularLocation(angle, radius, centerX, centerY);
	}

}
