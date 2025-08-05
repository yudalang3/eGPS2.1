package module.evolview.model.tree;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import egps2.UnifiedAccessPoint;
import module.evolview.gfamily.work.gui.tree.annotation.CommonShape;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNodeInsituAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropLeafNameAnno4LinageType;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropOutterSidewardAnno4LinageType;
import module.evolview.gfamily.work.gui.tree.annotation.OutterSidewardLocation;
import module.evolview.gfamily.work.gui.tree.annotation.SidewardNodeAnnotation;

public class AnnotationsProperties4LinageType {

	protected final List<DrawPropInternalNodeInsituAnno> internalNode2LeafInsituAnnos = new ArrayList<>();

	protected final List<DrawPropLeafNameAnno4LinageType> leafNameAnnos = new ArrayList<>();
	private Map<Integer, Color> leafNameRenderMap = new HashMap<>();
	private Map<String, Color> categ2colorMap = new HashMap<>();

	private Map<Integer, String> nodeID2categlory = new HashMap<>();
	private Map<String, SidewardNodeAnnotation> categlory2SidewardNodeAnnotation = new HashMap<>();
	protected final List<DrawPropOutterSidewardAnno4LinageType> sidewardAnno4LinageTypes = new ArrayList<>();

	protected final List<DrawPropOutterSidewardAnno4LinageType> internalNode2leafAnno4LinageTypes = new ArrayList<>();

	boolean isShowSidewardAnno = false;
	boolean isShowInternalNode2leafAnno = false;
	boolean isShowInsituAnno = false;


	public AnnotationsProperties4LinageType clone() {
		AnnotationsProperties4LinageType clone = new AnnotationsProperties4LinageType();
		clone.isShowSidewardAnno = this.isShowSidewardAnno;
		clone.isShowInternalNode2leafAnno = this.isShowInternalNode2leafAnno;
		clone.isShowInsituAnno = this.isShowInsituAnno;
		
		clone.categ2colorMap = this.categ2colorMap;
		clone.nodeID2categlory = this.nodeID2categlory;
		
		clone.internalNode2LeafInsituAnnos.addAll(this.internalNode2LeafInsituAnnos);
		clone.leafNameAnnos.addAll(leafNameAnnos);
		clone.leafNameRenderMap = this.leafNameRenderMap;
		clone.categlory2SidewardNodeAnnotation = this.categlory2SidewardNodeAnnotation;
		
		clone.sidewardAnno4LinageTypes.addAll(sidewardAnno4LinageTypes);
		clone.internalNode2leafAnno4LinageTypes.addAll(internalNode2leafAnno4LinageTypes);
		return clone;
	}
	/**
	 * @return the internalNode2LeafInsituAnnos
	 */
	public List<DrawPropInternalNodeInsituAnno> getInternalNode2LeafInsituAnnos() {
		return internalNode2LeafInsituAnnos;
	}

	/**
	 * @return the leafNameAnnos
	 */
	public List<DrawPropLeafNameAnno4LinageType> getLeafNameAnnos() {
		return leafNameAnnos;
	}

	public void clearAllInsituAnnos() {
		internalNode2LeafInsituAnnos.clear();
	}
	public void configurateOneInsituAnno(int id, double x, double y) {
		String string = nodeID2categlory.get(id);
		
		DrawPropInternalNodeInsituAnno drawPropInternalNodeInsituAnno = new DrawPropInternalNodeInsituAnno(null);
		drawPropInternalNodeInsituAnno.setShapParameter(20, CommonShape.ELLIPSE, new Color(255, 0, 0, 100));
		
		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont();
		drawPropInternalNodeInsituAnno.setFontParameter(defaultFont, string, Color.black);
		drawPropInternalNodeInsituAnno.configurate4lineageType(x, y);
		internalNode2LeafInsituAnnos.add(drawPropInternalNodeInsituAnno);
		
	}

	public void configurateLeafNamesAnnotaion() {
		if (leafNameAnnos.size() > 0) {

			leafNameRenderMap.clear();

			categ2colorMap.clear();
			for (DrawPropLeafNameAnno4LinageType anno : leafNameAnnos) {
				Color color = anno.getColor();
				categ2colorMap.put(anno.getSubTypeName(), color);

			}
			for (Entry<Integer, String> tt : nodeID2categlory.entrySet()) {
				Color value = categ2colorMap.get(tt.getValue());
				if (value == null) {
					System.err.println(getClass() + "  Internal error please call programmer!");
					value = Color.red;
				}
				leafNameRenderMap.put(tt.getKey(), value);
			}
		}
	}

	public Map<Integer, Color> getLeafNameRenderMap() {
		return leafNameRenderMap;
	}
	
	public Map<String, Color> getCateg2colorMap() {
		return categ2colorMap;
	}

	public void clearAllAnnotation() {
		internalNode2LeafInsituAnnos.clear();
		
		leafNameAnnos.clear();
		leafNameRenderMap.clear();

		nodeID2categlory.clear();
		sidewardAnno4LinageTypes.clear();
		categlory2SidewardNodeAnnotation.clear();

		internalNode2leafAnno4LinageTypes.clear();
		internalNode2LeafInsituAnnos.clear();
		
		isShowSidewardAnno = false;
		isShowInternalNode2leafAnno = false;
		isShowInsituAnno = false;

	}

	public boolean hasAnnotation() {

		boolean empty = leafNameAnnos.isEmpty() && nodeID2categlory.isEmpty();
		return !empty;

	}

	public void setNodeID2categlory(Map<Integer, String> nodeID2categlory) {
		this.nodeID2categlory = nodeID2categlory;
	}

	public void setCateglory2SidewardNodeAnnotation(
			Map<String, SidewardNodeAnnotation> categlory2SidewardNodeAnnotation) {
		this.categlory2SidewardNodeAnnotation = categlory2SidewardNodeAnnotation;
	}
	
	public Map<String, SidewardNodeAnnotation> getCateglory2SidewardNodeAnnotation() {
		return categlory2SidewardNodeAnnotation;
	}

	/**
	 * 
	 * @param annoIndex  : 0 sideward; 1 internal node to leaf ; 2 in-situ
	 * @param leaves
	 * @param shapeAdder
	 */
	public void configurateDrawPropOutterSidewardAnno4LinageTypes(int annoIndex, GraphicsNode[] leaves,
			Function<LinkedList<GraphicsNode>, OutterSidewardLocation> shapeAdder) {

		if (nodeID2categlory.isEmpty()) {
			return;
		}

		if (annoIndex == 0) {
			sidewardAnno4LinageTypes.clear();
		} else if (annoIndex == 1) {
			internalNode2leafAnno4LinageTypes.clear();
		} else {
			// in-situ
		}

		LinkedList<GraphicsNode> linkedList = new LinkedList<>();
		String prevCateglory = "";

		for (GraphicsNode leaf : leaves) {
			String string = nodeID2categlory.get(leaf.getID());

			if (string == null) {
				if (linkedList.size() > 0) {
					configOneAnnotater(annoIndex, linkedList, prevCateglory, shapeAdder);
				}

				prevCateglory = "";
			} else {
				if (!prevCateglory.equals(string)) {
					if (linkedList.size() > 0) {
						configOneAnnotater(annoIndex, linkedList, prevCateglory, shapeAdder);
					}
				}
				linkedList.add(leaf);
				prevCateglory = string;
			}
		}

		if (linkedList.size() > 0) {
			configOneAnnotater(annoIndex, linkedList, prevCateglory, shapeAdder);
		}

	}

	/**
	 * 
	 * @param linkedList
	 * @param prevCateglory
	 * @param shapeAdder
	 */
	private void configOneAnnotater(int annoIndex, LinkedList<GraphicsNode> linkedList, String prevCateglory,
			Function<LinkedList<GraphicsNode>, OutterSidewardLocation> shapeAdder) {

		OutterSidewardLocation apply = shapeAdder.apply(linkedList);

		if (annoIndex != 2) {
			SidewardNodeAnnotation sidewardNodeAnnotation = categlory2SidewardNodeAnnotation.get(prevCateglory);
			if (sidewardNodeAnnotation == null) {
				System.err.println("This is sideward or node 2 leaf annotaion. key is: "+prevCateglory);
				throw new InternalError(getClass() + "  Please tell developer, this is an internal error!");
			}
			DrawPropOutterSidewardAnno4LinageType e = new DrawPropOutterSidewardAnno4LinageType(sidewardNodeAnnotation);
			
			e.setTextLocationAndShape(apply.getTextX(), apply.getTextY(), apply.getShape());

			if (annoIndex == 0) {
				e.setIs4sideward(true);
				sidewardAnno4LinageTypes.add(e);
			} else if (annoIndex == 1) {
				e.setIs4sideward(false);
				internalNode2leafAnno4LinageTypes.add(e);
			}
		} else {
			// in-situ
			// base layout配置的时候会赋值
		}

		linkedList.clear();
	}

	public List<DrawPropOutterSidewardAnno4LinageType> getOutterSidewardAnnos() {
		return sidewardAnno4LinageTypes;
	}

	public List<DrawPropOutterSidewardAnno4LinageType> getInternalNode2leafAnno4LinageTypes() {
		return internalNode2leafAnno4LinageTypes;
	}

	/**
	 * @return the isShowSidewardAnno
	 */
	public boolean isShowSidewardAnno() {
		return isShowSidewardAnno;
	}

	/**
	 * @param isShowSidewardAnno the isShowSidewardAnno to set
	 */
	public void setShowSidewardAnno(boolean isShowSidewardAnno) {
		this.isShowSidewardAnno = isShowSidewardAnno;
	}

	/**
	 * @return the isShowInternalNode2leafAnno
	 */
	public boolean isShowInternalNode2leafAnno() {
		return isShowInternalNode2leafAnno;
	}

	/**
	 * @param isShowInternalNode2leafAnno the isShowInternalNode2leafAnno to set
	 */
	public void setShowInternalNode2leafAnno(boolean isShowInternalNode2leafAnno) {
		this.isShowInternalNode2leafAnno = isShowInternalNode2leafAnno;
	}

	/**
	 * @return the isShowInsituAnno
	 */
	public boolean isShowInsituAnno() {
		return isShowInsituAnno;
	}

	/**
	 * @param isShowInsituAnno the isShowInsituAnno to set
	 */
	public void setShowInsituAnno(boolean isShowInsituAnno) {
		this.isShowInsituAnno = isShowInsituAnno;
	}

	
}
