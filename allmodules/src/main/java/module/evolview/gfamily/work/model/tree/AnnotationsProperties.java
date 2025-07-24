package module.evolview.gfamily.work.model.tree;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNode2LeafAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropInternalNodeInsituAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropLeafNameAnno;
import module.evolview.gfamily.work.gui.tree.annotation.DrawPropOutterSidewardAnno;
import module.evolview.gfamily.work.gui.tree.annotation.SidewardNodeAnnotation;

public class AnnotationsProperties {

	protected final List<DrawPropInternalNode2LeafAnno> internalNode2LeafAnnos = new ArrayList<>();
	protected final List<DrawPropInternalNodeInsituAnno> internalNode2LeafInsituAnnos = new ArrayList<>();
	protected final List<DrawPropOutterSidewardAnno> outterSidewardAnnos = new ArrayList<>();

	protected List<DrawPropLeafNameAnno> leafNameAnnos = new ArrayList<>();
	private final Map<Integer, Color> leafNameRenderMap = new HashMap<>();
	private Map<String, Color> categ2colorMap = new HashMap<>();

	
	public AnnotationsProperties clone() {
		AnnotationsProperties clone = new AnnotationsProperties();
		
		clone.internalNode2LeafAnnos.addAll(internalNode2LeafAnnos);
		clone.internalNode2LeafInsituAnnos.addAll(internalNode2LeafInsituAnnos);
		clone.outterSidewardAnnos.addAll(outterSidewardAnnos);
		clone.leafNameAnnos.addAll(leafNameAnnos);
		
		
		
		return clone;
	}
	/**
	 * @return the internalNode2LeafAnnos
	 */
	public List<DrawPropInternalNode2LeafAnno> getInternalNode2LeafAnnos() {
		return internalNode2LeafAnnos;
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
	public List<DrawPropLeafNameAnno> getLeafNameAnnos() {
		return leafNameAnnos;
	}

	/**
	 * @return the outterSidewardAnnos
	 */
	public List<DrawPropOutterSidewardAnno> getOutterSidewardAnnos() {
		return outterSidewardAnnos;
	}

	public void configurateLeafNamesAnnotaion(boolean is4linageType) {
		leafNameRenderMap.clear();
		
		categ2colorMap.clear();
		
		for (DrawPropLeafNameAnno anno : leafNameAnnos) {
			if (anno.shouldConfigurateAndPaint()) {
				Color color = anno.getColor();
				List<GraphicsNode> leaves = anno.getLeaves();
				
				for (GraphicsNode graphicsNode : leaves) {
					leafNameRenderMap.put(graphicsNode.getID(), color);
				}
				
				//categ2colorMap.put(anno.get, value)
			}
		}
	}

	public Map<Integer, Color> getLeafNameRenderMap() {
		return leafNameRenderMap;
	}

	public void clearAllAnnotation() {
		internalNode2LeafAnnos.clear();
		internalNode2LeafInsituAnnos.clear();
		outterSidewardAnnos.clear();
		leafNameAnnos.clear();
		leafNameRenderMap.clear();
	}

	public void clearSelectedAnnotation(Set<GraphicsNode> nodeSet) {
		internalNode2LeafAnnos.removeIf(e -> {
			return nodeSet.contains(e.getCurrentGraphicsNode());
		});
		internalNode2LeafInsituAnnos.removeIf(e -> {
			return nodeSet.contains(e.getCurrentGraphicsNode());
		});
		outterSidewardAnnos.removeIf(e -> {
			return nodeSet.contains(e.getCurrentGraphicsNode());
		});
		leafNameAnnos.removeIf(e -> {
			return nodeSet.contains(e.getCurrentGraphicsNode());
		});
	}

	public boolean hasAnnotation() {

		boolean empty = internalNode2LeafAnnos.isEmpty() && internalNode2LeafInsituAnnos.isEmpty()
				&& outterSidewardAnnos.isEmpty() && leafNameAnnos.isEmpty();
		return !empty;

	}

	public void addAnnotationProperties(AnnotationsProperties currentAnnotationsProperties, GraphicsNode newNode) {

		addInternalNode2LeafAnnosProp(currentAnnotationsProperties.getInternalNode2LeafAnnos(), newNode);
		addInternalNode2LeafInsituAnnosProp(currentAnnotationsProperties.getInternalNode2LeafInsituAnnos(), newNode);
		addOutterSidewardAnnosProp(currentAnnotationsProperties.getOutterSidewardAnnos(), newNode);
		addLeafNameAnnosProp(currentAnnotationsProperties.getLeafNameAnnos(), newNode);
	}

	public void addLeafNameAnnosProp(List<DrawPropLeafNameAnno> list, GraphicsNode newNode) {
		for (DrawPropLeafNameAnno drawPropLeafNameAnno : list) {
			DrawPropLeafNameAnno tt = new DrawPropLeafNameAnno(drawPropLeafNameAnno.getColor(), newNode);
			this.getLeafNameAnnos().add(tt);
		}

	}

	public void addOutterSidewardAnnosProp(List<DrawPropOutterSidewardAnno> list, GraphicsNode newNode) {
		for (DrawPropOutterSidewardAnno tt : list) {
			SidewardNodeAnnotation sidewardNodeAnnotation = tt.getSidewardNodeAnnotation();
			DrawPropOutterSidewardAnno drawPropOutterSidewardAnno = new DrawPropOutterSidewardAnno(
					sidewardNodeAnnotation,newNode);
			
			drawPropOutterSidewardAnno.setJust4annotationNot4lineageTypeTrue();

			this.getOutterSidewardAnnos().add(drawPropOutterSidewardAnno);
		}

	}

	public void addInternalNode2LeafInsituAnnosProp(List<DrawPropInternalNodeInsituAnno> list, GraphicsNode newNode) {
		for (DrawPropInternalNodeInsituAnno tt : list) {
			this.getInternalNode2LeafInsituAnnos().add(tt.clone(newNode));
		}

	}

	public void addInternalNode2LeafAnnosProp(List<DrawPropInternalNode2LeafAnno> list, GraphicsNode newNode) {
		for (DrawPropInternalNode2LeafAnno tt : list) {
			this.getInternalNode2LeafAnnos().add(new DrawPropInternalNode2LeafAnno(tt.getColor(), newNode));
		}

	}
	
	public void setShouldNotConfigurateIfNoGraphicsNode() {
		List<DrawPropInternalNode2LeafAnno> internalNode2LeafAnnos = getInternalNode2LeafAnnos();
		for (DrawPropInternalNode2LeafAnno tt : internalNode2LeafAnnos) {
			tt.setShouldNotConfigurate();
		}
		List<DrawPropOutterSidewardAnno> outterSidewardAnnos = getOutterSidewardAnnos();
		for (DrawPropOutterSidewardAnno tt : outterSidewardAnnos) {
			tt.setShouldNotConfigurate();
		}
		List<DrawPropInternalNodeInsituAnno> internalNode2LeafInsituAnnos = getInternalNode2LeafInsituAnnos();
		for (DrawPropInternalNodeInsituAnno tt : internalNode2LeafInsituAnnos) {
			tt.setShouldNotConfigurate();
		}
		List<DrawPropLeafNameAnno> leafNameAnnos = getLeafNameAnnos();
		for (DrawPropLeafNameAnno tt : leafNameAnnos) {
			tt.setShouldNotConfigurate();
		}
	}
	
	public void assignNullIfShouldNotConfig4all() {
		List<DrawPropInternalNode2LeafAnno> internalNode2LeafAnnos = getInternalNode2LeafAnnos();
		for (DrawPropInternalNode2LeafAnno tt : internalNode2LeafAnnos) {
			tt.assignNullIfShouldNotConfig();
		}
		List<DrawPropOutterSidewardAnno> outterSidewardAnnos = getOutterSidewardAnnos();
		for (DrawPropOutterSidewardAnno tt : outterSidewardAnnos) {
			tt.assignNullIfShouldNotConfig();
		}
		List<DrawPropInternalNodeInsituAnno> internalNode2LeafInsituAnnos = getInternalNode2LeafInsituAnnos();
		for (DrawPropInternalNodeInsituAnno tt : internalNode2LeafInsituAnnos) {
			tt.assignNullIfShouldNotConfig();
		}
		List<DrawPropLeafNameAnno> leafNameAnnos = getLeafNameAnnos();
		for (DrawPropLeafNameAnno tt : leafNameAnnos) {
			tt.assignNullIfShouldNotConfig();
		}
	}

}
