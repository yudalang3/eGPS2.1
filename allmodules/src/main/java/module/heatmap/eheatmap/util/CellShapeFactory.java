package module.heatmap.eheatmap.util;

import module.heatmap.eheatmap.cirpainter.CirShapePainter;
import module.heatmap.eheatmap.cirpainter.shape.CirCircleWithAnglePainter;
import module.heatmap.eheatmap.cirpainter.shape.CirCircleWithSizePainter;
import module.heatmap.eheatmap.cirpainter.shape.CirDiamondPainter;
import module.heatmap.eheatmap.cirpainter.shape.CirDoubleTrianglePainter;
import module.heatmap.eheatmap.cirpainter.shape.CirEllipsePainter;
import module.heatmap.eheatmap.cirpainter.shape.CirPlainCirclePainter;
import module.heatmap.eheatmap.cirpainter.shape.CirRectPainter;
import module.heatmap.eheatmap.enums.CellShape;
import module.heatmap.eheatmap.rectpainter.RectShapePainter;
import module.heatmap.eheatmap.rectpainter.shape.RectCircleWithAnglePainter;
import module.heatmap.eheatmap.rectpainter.shape.RectCircleWithSizePainter;
import module.heatmap.eheatmap.rectpainter.shape.RectDiamondPainter;
import module.heatmap.eheatmap.rectpainter.shape.RectDoubleTrianglePainter;
import module.heatmap.eheatmap.rectpainter.shape.RectEllipsePainter;
import module.heatmap.eheatmap.rectpainter.shape.RectPlainCirclePainter;
import module.heatmap.eheatmap.rectpainter.shape.RectRectPainter;

public class CellShapeFactory {

	public static RectShapePainter obtainRectLayoutCellShapePainter(CellShape cellShape,double w,double h) {
		RectShapePainter ret =null;
		switch (cellShape) {
		case Ellipse:
			ret = new RectEllipsePainter(w, h);
			break;
		case PlainCircle:
			ret = new RectPlainCirclePainter(w, h);
			break;
		case CircleWithSize:
			ret = new RectCircleWithSizePainter(w, h);
			break;
		case CircleWithPointer:
			ret = new RectCircleWithAnglePainter(w, h);
			break;
		case Diamond:
			ret = new RectDiamondPainter(w, h);
			break;
		case DoubleTriangle:
			ret = new RectDoubleTrianglePainter(w, h);
			break;
		default:
			ret = new RectRectPainter(w, h);
			break;
		}
		
		return ret;
	}
	public static CirShapePainter obtainCirLayoutCellShapePainter(CellShape cellShape,double w,double h) {
		CirShapePainter ret = null;
		switch (cellShape) {
		case Ellipse:
			ret = new CirEllipsePainter(w, h);
			break;
		case PlainCircle:
			ret = new CirPlainCirclePainter(w, h);
			break;
		case CircleWithSize:
			ret = new CirCircleWithSizePainter(w, h);
			break;
		case CircleWithPointer:
			ret = new CirCircleWithAnglePainter(w, h);
			break;
		case Diamond:
			ret = new CirDiamondPainter(w, h);
			break;
		case DoubleTriangle:
			ret = new CirDoubleTrianglePainter(w, h);
			break;
		default:
			ret = new CirRectPainter(w, h);
			break;
		}
		
		return ret;
	}
}
