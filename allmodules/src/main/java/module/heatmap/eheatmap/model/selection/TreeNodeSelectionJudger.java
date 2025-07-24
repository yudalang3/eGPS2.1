package module.heatmap.eheatmap.model.selection;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Double;

import evoltree.struct.EvolNode;
import module.heatmap.eheatmap.model.PaintingLocationParas;
import module.heatmap.eheatmap.model.ParameterModel;
import module.heatmap.eheatmap.tree.GraphcsNode;
import module.heatmap.eheatmap.tree.LineObj;


public class TreeNodeSelectionJudger {
	private GraphcsNode selectedGraphcsNode;
	private Double rect;
	private boolean globalBooleanFlag;
	private boolean ifRowSelected;
	
	public GraphcsNode getGraphcsNode() {
		return selectedGraphcsNode;
	}

	public boolean getAndJudgeSelectionForRegion(PaintingLocationParas locationParas, ParameterModel paraModel,
			Double dragRect) {
		
		this.rect = dragRect;
		GraphcsNode rowTreeRootNode = paraModel.getRowTreeRootNode();
		globalBooleanFlag = false;
		if (paraModel.isIfPaintRowTree()) {
			iterateToGetIfNodeSelected(rowTreeRootNode,true);
		}
		if (globalBooleanFlag) {
			ifRowSelected = true;
			return true;
		}
		GraphcsNode colTreeRootNode = paraModel.getColTreeRootNode();
		globalBooleanFlag = false;
		if (paraModel.isIfPaintColTree()) {
			iterateToGetIfNodeSelected(colTreeRootNode,false);
		}
		if (globalBooleanFlag) {
			ifRowSelected = false;
			return true;
		}
		return false;
	}

	public boolean getAndJudgeSelectionForOneClick(PaintingLocationParas locationParas, ParameterModel paraModel,
			Point2D point) {
		Double dd = new Double(point.getX() - 10, point.getY() - 10, 20, 20);
		return getAndJudgeSelectionForRegion(locationParas,paraModel,dd);
	}

	/**
	 * The first level root nodes don't need to iterate!
	 * @param node : the first time is root node.
	 * @param ifRow : if row
	 */
	private void iterateToGetIfNodeSelected(GraphcsNode node,boolean ifRow) {
		/**
		 * The case there is no root GraphcsNode. when you don't selected node!
		 */
		if (node == null) {
			return;
		}
		/**
		 * The case this is leaf, the Node will be judged by parent!
		 */
		int len = node.getChildCount();
		if (len == 0) {
			return;
		}
		/**
		 * The case When some Node is judged by others, No need to repeat judge!
		 */
		if (globalBooleanFlag) {
			return;
		}
		
		for (int i = 0; i < len; i++) {
			EvolNode gg = node.getChildAt(i);
			LineObj lineObj = null;
			GraphcsNode graphcsNode = (GraphcsNode) gg;
			
			if (ifRow) {
				lineObj = graphcsNode.getHorizontalLineObj();
				
			}else {
				lineObj = graphcsNode.getVerticaLineObj();
			}
			boolean tt = rect.intersectsLine(lineObj.getX1(), lineObj.getY1(),lineObj.getX2(), lineObj.getY2());
			 
			if (tt) {
				selectedGraphcsNode = graphcsNode;
				graphcsNode.setSelectedByUser(true);
				//DecimalFormat df = new DecimalFormat("##.##");
				globalBooleanFlag = true;
				//System.out.println(df.format(lineObj.getX2())+" "+df.format(lineObj.getY2())+" "+rect.toString()+" "+tt+" "+getClass());
				return ;
			}
			iterateToGetIfNodeSelected(graphcsNode,ifRow);
		}
		
	}

	public boolean getIfRowSelected() {
		return ifRowSelected;
	}
}
