package module.heatmap.eheatmap.cirpainter;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import egps2.utils.common.model.datatransfer.ThreeTuple;
import graphic.engine.GradientColorHolder;
import module.heatmap.eheatmap.model.AnnotaionParaObj;
import module.heatmap.eheatmap.model.CircularParameters;
import module.heatmap.eheatmap.model.DataModel;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.model.selection.AnnotationSelection;
import module.heatmap.eheatmap.model.selection.CellSelection;
import module.heatmap.eheatmap.util.CellHandler;
import module.heatmap.eheatmap.util.CellLocationHandler;
import module.heatmap.eheatmap.util.CellShapeFactory;
import module.heatmap.eheatmap.util.FactorCalculator;
import module.heatmap.eheatmap.util.FactorHandler;

public class CirMapPainter extends CellLocationHandler {

	private final SectorDrawer sectorDrawer = new SectorDrawer();
	private final double oneDegRadians = Math.PI / 180;

	@Override
	protected void twoDimIterateProcess(CellHandler cellHandler, PaintingLocationParas p, ParameterModel paraModel,
			DataModel dataModel) {

	}

	public void painting(Graphics2D g2d, PaintingLocationParas p, DataModel dataModel, GradientColorHolder gColorHolder,
			ParameterModel paraModel) {

		double[][] dataMatrix = dataModel.getDataMatrix();
		int[] rowOrderMaping = dataModel.getRowOrderMaping();
		int[] colOrderMaping = dataModel.getColOrderMaping();
		String[] rowNames = dataModel.getRowNames();
		String[] colNames = dataModel.getColNames();
		numOfRows = p.getNumOfRows();
		numOfCols = p.getNumOfCols();
		double maxMinusMix = dataModel.getMaxValue() - dataModel.getMinValue();

		Set<Integer> xSet = new HashSet<Integer>();
		Set<Integer> ySet = new HashSet<Integer>();

		CellSelection oneSelection = null;
		if (paraModel.isCellOneMouseHasClick()) {
			List<CellSelection> cellSelections = paraModel.getCellSelections();
			if (cellSelections.size() > 0) {
				oneSelection = cellSelections.get(0);
			}
		} else if (paraModel.isHasMouseDragEvent()) {
			List<CellSelection> cellSelections = paraModel.getCellSelections();
			for (CellSelection cellSelection : cellSelections) {
				xSet.add(cellSelection.getIi());
				ySet.add(cellSelection.getJj());
			}

		}

		int[] hGapLocations = paraModel.gethGapLocations();
		int gapSize = paraModel.getGapSize();
		FactorHandler hfactorHandler = new FactorHandler(0, hGapLocations, hGapLocations.length);
		hfactors = new int[numOfCols];
		for (int i = 0; i < numOfCols; i++) {
			hfactors[i] = FactorCalculator.getIncreasedAddingFactor(hfactorHandler, i);
		}
		int[] vGapLocations = paraModel.getvGapLocations();
		FactorHandler vfactorHandler = new FactorHandler(0, vGapLocations, vGapLocations.length);
		int[] vfactors = new int[numOfRows];
		for (int i = 0; i < numOfRows; i++) {
			vfactors[i] = FactorCalculator.getIncreasedAddingFactor(vfactorHandler, i);
		}

		CircularParameters cPara = paraModel.getCircularParameters();
		
		Point centerPoint = p.getCenterPoint();
		int x0 = centerPoint.x;
		int y0 = centerPoint.y;

		g2d.setColor(paraModel.getRowNameColor());
		g2d.setFont(paraModel.getRowNameFont());
		FontMetrics metrics = g2d.getFontMetrics();
		int hgt = metrics.getHeight();

		double r2 = 0;
		double eachDeg = (cPara.getTotalEextendDeg() - vGapLocations.length * gapSize * cPara.getRowAnnGapFactor())
				/ numOfRows;
		CirShapePainter cirShapePainter = CellShapeFactory.obtainCirLayoutCellShapePainter(
				paraModel.getCellShapePainter(), eachDeg * cPara.getInnerRadius(), cPara.getRingThickness());
		// draw ring by ring
		for (int i = 0; i < numOfCols; i++) {
			double r1 = cPara.getInnerRadius() + i * cPara.getRingThickness() + hfactors[i] * gapSize;
			r2 = r1 + cPara.getRingThickness();
			double sD = cPara.getTotalSstartDeg();
			for (int j = 0; j < numOfRows; j++) {
				double startDeg = sD + vfactors[j] * gapSize * cPara.getRowAnnGapFactor();
				cirShapePainter.setProperty(r1, r2, startDeg, eachDeg);

				double d = dataMatrix[rowOrderMaping[j]][colOrderMaping[i]];
				double value = (d - dataModel.getMinValue()) / maxMinusMix;
				if (Double.isNaN(value)) {
					g2d.setColor(Color.gray);
				} else {
					g2d.setColor(gColorHolder.getColorFromPallet(value));
				}

				Shape shape = cirShapePainter.getShape(x0, y0, d, value);
				g2d.fill(shape);
				if (paraModel.isIfShowBorder()) {
					g2d.setColor(paraModel.getBorderColor());
					g2d.draw(shape);
				}

				sD += eachDeg;
				// paint row names!
				if (paraModel.isIfPaintRowNames() && j == 0) {
					
					String string = colNames[colOrderMaping[i]];
					drawOnceRowName(g2d, paraModel, cPara, r1, startDeg, x0, y0, string);
				}
			}

			if (paraModel.isIfPaintColAnnotation()) {
				AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
				ThreeTuple<List<String>, List<Color[]>, List<byte[]>> colAnnoParas = annotaionParaObj.getColAnnoParas();
				List<Color[]> colAnnColors = colAnnoParas.second;
				List<byte[]> colAnnotaionIndexes = colAnnoParas.third;
				List<String> colAnnotationNames = colAnnoParas.first;

				int numOfAnnotations = colAnnotaionIndexes.size();
//				double allAvaiheight = h / numOfAnnotations;
//				double height = allAvaiheight - paraModel.annoLocationDivider;

				List<AnnotationSelection> annotationSelections = paraModel.getAnnotationSelections();

				for (int k = 0; k < numOfAnnotations; k++) {
					byte[] bs = colAnnotaionIndexes.get(k);
					Color[] colors = colAnnColors.get(k);
					double xx = 0;

					g2d.setColor(colors[bs[colOrderMaping[i]]]);
					// for the contol panel!
					int degre = cPara.getColEachAnnotationDegree();
					sectorDrawer.setProperty(r1, r2, k * degre + sD + cPara.getColEachAnnotationDegreeGap(), degre);
//					if(k==1) {
//					System.err.println("fefe"+(k * degre + sD + cPara.getColEachAnnotationDegreeGap()+degre));
//					}
					sectorDrawer.drawSector(g2d, x0, y0, paraModel);

				}
			}

		}

		// 绘制 col Names
		r2 += 5;

		if (paraModel.isIfPaintColNames()) {
			g2d.setColor(paraModel.getColNameColor());
			for (int j = 0; j < numOfRows; j++) {
				double startDeg = (j + 0.5) * eachDeg + cPara.getTotalSstartDeg();
				startDeg +=  vfactors[j] * gapSize * cPara.getRowAnnGapFactor();
				
				Point2D.Double p1 = new Point2D.Double(x0 + r2 * Math.cos(startDeg * oneDegRadians),
						y0 - r2 * Math.sin(startDeg * oneDegRadians));

				//g2d.fillOval((int)(p1.x -2), (int)(p1.y-2), 4, 4);
				String string = rowNames[rowOrderMaping[j]];
				
				g2d.rotate(-Math.toRadians(startDeg), p1.getX(), p1.getY());
				g2d.drawString(string, (float) p1.getX(), (float) p1.getY());
				g2d.rotate(Math.toRadians(startDeg), p1.getX(), p1.getY());

//				if (j == 0) {
//					break;
//				}
			}
		}

		// draw row annotion
		AnnotaionParaObj annotaionParaObj = paraModel.getAnnotaionParaObj();
		ThreeTuple<List<String>, List<Color[]>, List<byte[]>> rowAnnoParas = annotaionParaObj.getRowAnnoParas();

		List<Color[]> rowAnnColors = rowAnnoParas.second;
		List<byte[]> rowAnnotaionIndexes = rowAnnoParas.third;
		List<String> rowAnnotationNames = rowAnnoParas.first;

		int numOfAnnotations = rowAnnotaionIndexes.size();
		double allAvaiWidth = (cPara.getInnerRadius() - cPara.getRowAnnotationRadius()) / numOfAnnotations;
		double width = allAvaiWidth - paraModel.annoLocationDivider;

		List<AnnotationSelection> annotationSelections = paraModel.getAnnotationSelections();

		for (int i = 0; i < numOfAnnotations; i++) {
			Color[] colors = rowAnnColors.get(i);
			byte[] bs = rowAnnotaionIndexes.get(i);

			double r1 = cPara.getRowAnnotationRadius() + i * cPara.getRowAnnotationEachThickness();
			r2 = r1 + cPara.getRowAnnotationEachThickness() * 0.65;

			for (int j = 0; j < numOfRows; j++) {
				double sD = j * eachDeg + cPara.getTotalSstartDeg();

				sectorDrawer.setProperty(r1, r2, sD + vfactors[j] * gapSize * cPara.getRowAnnGapFactor(), eachDeg);

				g2d.setColor(colors[bs[rowOrderMaping[j]]]);

				Point2D.Double p1 = sectorDrawer.drawSector(g2d, x0, y0, paraModel);

			}

		}

		// paint values

		// 下面不用去看

//		CellHandler cellHandler = new CellHandler(g2d, oneSelection) {
//			public void handleProcess(Double r, int i, int j, double x, double y) {
//
////				x / hmapHeight * ;
////				y / hmapWeidth
//
//				if (paraModel.isCellOneMouseHasClick()) {
//					double value = (dataMatrix[rowOrderMaping[i]][colOrderMaping[j]] - dataModel.getMinValue())
//							/ maxMinusMix;
//					if (java.lang.Double.isNaN(value)) {
//						g2d.setColor(Color.gray);
//					} else {
//						g2d.setColor(gColorHolder.getColorFromPallet(value));
//					}
//
//					g2d.fill(r);
//
//					if (oSelection != null && oSelection.equals(i, j)) {
//						g2d.setColor(selectBorderColor);
//						Stroke oriStroke = g2d.getStroke();
//						g2d.setStroke(new BasicStroke(3));
//						g2d.draw(r);
//						g2d.setStroke(oriStroke);
//					}
//				} else if (paraModel.isHasMouseDragEvent()) {
//					if (xSet.contains(i) && ySet.contains(j)) {
//						g2d.setColor(selectCellColor);
//						g2d.fill(r);
//					} else {
//						double value = (dataMatrix[rowOrderMaping[i]][colOrderMaping[j]] - dataModel.getMinValue())
//								/ maxMinusMix;
//						if (java.lang.Double.isNaN(value)) {
//							g2d.setColor(Color.gray);
//						} else {
//							g2d.setColor(gColorHolder.getColorFromPallet(value));
//						}
//						g2d.fill(r);
//					}
//				} else {
//					double d = dataMatrix[rowOrderMaping[i]][colOrderMaping[j]];
//					double value = (d - dataModel.getMinValue()) / maxMinusMix;
//					if (java.lang.Double.isNaN(value)) {
//						g2d.setColor(Color.gray);
//					} else {
//						// g2d.setColor(gColorHolder.getColorFromPallet(value));
//						g2d.setColor(Color.yellow);
//					}
//					g2d.fill(r);
//				}
//
//			};
//		};

	}

	private void drawOnceRowName(Graphics2D g2d, ParameterModel paraModel,CircularParameters cPara,
			double r,double curangle,int x0,int y0,String st) {
		//st ="I love eGPS!";
		//double r = cPara.getInnerRadius(); // first line should be this!
		// first angle
//		double curangle = oneDegRadians *(90 -cPara.getTotalSstartDeg() + 5);
		curangle = oneDegRadians *(90 -curangle +4);
		
		g2d.setColor(paraModel.getRowNameColor());
		g2d.setFont(paraModel.getRowNameFont());
		char ch[] = st.toCharArray();
		FontMetrics fm = g2d.getFontMetrics();
		r = r + 0.5 * cPara.getRingThickness() - 0.45 *fm.getHeight();

		int length = ch.length;
		for (int i = 0; i < length; i++) {
			double cwid = (double) (getWidth(ch[i], fm));
			if (!(ch[i] == ' ' || Character.isSpaceChar(ch[i]))) {
				cwid = (double) (fm.charWidth(ch[i]));
				g2d.rotate(curangle, x0, y0);
				String chstr = new String(ch, i, 1);
				//g2d.setTransform(cxform);
				float xx = (float) (-0.5 * cwid + x0);
				float yy = (float) (-r + y0);
				g2d.drawString(chstr, xx, yy); 
				g2d.rotate(-curangle, x0, y0);
			}
			// compute advance of angle assuming cwid<<radius
			if (i < (length - 1)) {
				double adv = 0.5 * cwid + fm.getLeading() + 0.5 * getWidth(ch[i + 1], fm);
				// Use of atan() suggested by Michael Moradzadeh
				curangle += Math.atan(adv / r);
				// Original code was: // 
				//curangle += Math.sin(adv / r);
			}
		}
	}
	/**
	 * Get the width of a given character under the specified FontMetrics,
	 * interpreting all spaces as en-spaces.
	 */
	int getWidth(char c, FontMetrics fm) {
		if (c == ' ' || Character.isSpaceChar(c)) {
			return fm.charWidth('n');
		} else {
			return fm.charWidth(c);
		}
	}

}
