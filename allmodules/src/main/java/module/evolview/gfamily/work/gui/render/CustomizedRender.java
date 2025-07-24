package module.evolview.gfamily.work.gui.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import module.evolview.gfamily.GeneFamilyController;
import module.evolview.gfamily.work.gui.tree.TreeOperationUtil;
import module.evolview.gfamily.work.model.enums.ColorScheme;
import module.evolview.gfamily.work.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.util.GraphicTreePropertyCalculator;
import graphic.engine.colors.EGPSColors;

/**
 * 进行自定义颜色渲染的类
 * 
 *
 */
public class CustomizedRender implements ColorRander {

	private static int INDEX_OF_NAME = 0;
	private static int INDEX_OF_TYPE = 1;
	private static int INDEX_OF_COLOR = 2;

	private static final String TYPE_SELF = "self";
	private static final String TYPE_ALL = "all";
	private static final String BEFORE_RENDER_HEADER = "##RENDER_SCHEME=";

	private GeneFamilyController controller;
	/**
	 * 1，No color rendering ; 2 Countries & regions; 3 Gender; 4 Patient age; 5
	 * Collection date
	 */
	private int beforeRenderIndex;
	private List<String> availableStrings;


	public CustomizedRender(GeneFamilyController controller) {
		this.controller = controller;
		
		CustomizedSaveBean customizedSaveBean = controller.getCustomizedSaveBean();
		beforeRenderIndex = customizedSaveBean.getBeforeRenderingIndex();
		
	}

	public CustomizedRender(String text, GeneFamilyController controller2) {

		this.controller = controller2;

		String[] lineStrings = text.split("\n");

		beforeRenderIndex = 1;

		availableStrings = new ArrayList<String>();
		for (String renderString : lineStrings) {

			String trim = renderString.trim();
			if (trim.length() == 0 || renderString.startsWith("#")) {

				int indexOf = trim.indexOf(BEFORE_RENDER_HEADER);
				if (indexOf != -1) {
					String substring = trim.substring(BEFORE_RENDER_HEADER.length());
					beforeRenderIndex = Integer.parseInt(substring);
				}

				continue;
			}

			availableStrings.add(trim);

		}

	}

	@Override
	public void renderNodes(GraphicsNode rootNode) {
		
		//添加一步验证，如果有问题的话，提前就抛出异常
		if (availableStrings != null) {
			for (String string : availableStrings) {
				String[] eleStrings = string.split("\\s+");
				if (eleStrings.length != 3) {
					throw new IllegalArgumentException("The input format of your string error: "+string);
				}
			}
		}

		switch (beforeRenderIndex) {
		case 2:
			controller.changeColorScheme(ColorScheme.COUNTRIESREGIONS);
			break;
		case 3:
			controller.changeColorScheme(ColorScheme.GENDER);
			break;
		case 4:
			controller.changeColorScheme(ColorScheme.PATIENTAGE);
			break;
		case 5:
			controller.changeColorScheme(ColorScheme.COLLECTIONDATE);
			break;
		default:
			controller.changeColorScheme(ColorScheme.NO_COLOR);
			break;
		}

		// outer use
		if (availableStrings != null) {
			CustomizedSaveBean customizedSaveBean = controller.getCustomizedSaveBean();
			List<CustomizedRenderRecord> records = customizedSaveBean.getRecords();
			records.clear();
			
			Map<String, Color> selfNodeMap = new HashMap<>(128);
			for (String string : availableStrings) {
				String[] eleStrings = string.split("\\s+");

				String name = eleStrings[INDEX_OF_NAME];
				String type = eleStrings[INDEX_OF_TYPE];
				String color = eleStrings[INDEX_OF_COLOR];

				String[] leafNames = name.split(",");

				Color decode = Color.decode(color);
				if (leafNames.length > 1) {
					GraphicsNode corespondingInnerNode = getCorespondingInnerNode(rootNode, leafNames);
					if (TYPE_ALL.equalsIgnoreCase(type)) {
						TreeOperationUtil.recursiveIterateTreeIF(corespondingInnerNode, node -> {
							node.getDrawUnit().setLineColor(decode);
						});

						CustomizedRenderRecord customizedRenderRecord = new CustomizedRenderRecord();
						customizedRenderRecord.setSelf(false);
						customizedRenderRecord.setHexFormatColor(color);
						customizedRenderRecord.setNodeID(corespondingInnerNode.getID());
						records.add(customizedRenderRecord);
					} else {
						corespondingInnerNode.getDrawUnit().setLineColor(decode);
						
						CustomizedRenderRecord customizedRenderRecord = new CustomizedRenderRecord();
						customizedRenderRecord.setSelf(true);
						customizedRenderRecord.setHexFormatColor(color);
						customizedRenderRecord.setNodeID(corespondingInnerNode.getID());
						records.add(customizedRenderRecord);
					}
				} else {
					selfNodeMap.put(name, decode);
				}
			}
			
			// self rendering
			TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
				Color cc = selfNodeMap.get(node.getName());
				if (cc != null) {
					node.getDrawUnit().setLineColor(cc);
					
					CustomizedRenderRecord customizedRenderRecord = new CustomizedRenderRecord();
					customizedRenderRecord.setSelf(true);
					customizedRenderRecord.setHexFormatColor(EGPSColors.toHexFormColor(cc));
					customizedRenderRecord.setNodeID(node.getID());
					records.add(customizedRenderRecord);
				}
			});
			
			
		}else {
			// internal use
			Map<Integer, Color> selfNodeMap = new HashMap<>(128);
			Map<Integer, Color> allNodeMap = new HashMap<>(128);
			
			CustomizedSaveBean customizedSaveBean = controller.getCustomizedSaveBean();
			List<CustomizedRenderRecord> records = customizedSaveBean.getRecords();
			for (CustomizedRenderRecord record : records) {
				if (record.isSelf()) {
					selfNodeMap.put(record.getNodeID(), Color.decode(record.getHexFormatColor()));
				}else {
					allNodeMap.put(record.getNodeID(), Color.decode(record.getHexFormatColor()));
				}
			}
			
			// all color rendering first!
			TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
				Color cc = allNodeMap.get(node.getID());
				if (cc != null) {
					TreeOperationUtil.recursiveIterateTreeIF(node, t -> {
						t.getDrawUnit().setLineColor(cc);
					});
				}
			});
			TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
				Color cc = selfNodeMap.get(node.getID());
				if (cc != null) {
					node.getDrawUnit().setLineColor(cc);
				}
			});
			
		}
		
		

	}

	private GraphicsNode getCorespondingInnerNode(GraphicsNode rootNode, String[] leafNames) {
		Set<String> hashSet = new HashSet<>(Arrays.asList(leafNames));
		Set<GraphicsNode> nodeSet = new HashSet<>();
		TreeOperationUtil.recursiveIterateTreeIF(rootNode, node -> {
			if (hashSet.contains(node.getName())) {
				nodeSet.add(node);
			}
		});
		GraphicsNode[] array = nodeSet.toArray(new GraphicsNode[1]);
		GraphicsNode innerNode = array[0];
		for (int i = 1; i < array.length; i++) {
			GraphicsNode graphicsNode = array[i];
			innerNode = GraphicTreePropertyCalculator.getMostRecentCommonAnsester(innerNode, graphicsNode);
		}
		return innerNode;
	}

}
