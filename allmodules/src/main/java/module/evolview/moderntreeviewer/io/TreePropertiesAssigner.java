package module.evolview.moderntreeviewer.io;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import tsv.io.TSVReader;
import egps2.UnifiedAccessPoint;
import egps2.frame.MyFrame;
import evoltree.struct.util.EvolNodeUtil;
import module.evolview.model.tree.GraphicsNode;
import module.evolview.phylotree.visualization.graphics.struct.ShowLeafPropertiesInfo;
import evoltree.txtdisplay.TreeDrawUnit;
import module.evolview.model.tree.TreeLayoutProperties;
import graphic.engine.colors.EGPSColors;

public class TreePropertiesAssigner {

	public void assign(TreeLayoutProperties treeLayoutProperties, MTVImportInforBean object) {
		treeLayoutProperties.setBlankArea(object.getBlank_space());
		
		if(!Strings.isNullOrEmpty(object.getTitleString())) {
			treeLayoutProperties.setTitleString(object.getTitleString());
		}
		
		treeLayoutProperties.setWhetherHeightScaleOnMouseWheel( object.isWhetherHeightScaleOnMouseWheel());
		treeLayoutProperties.setWhetherWidthScaleOnMouseWheel( object.isWhetherWidthScaleOnMouseWheel());
		
		treeLayoutProperties.setTitleString(object.getTitleString());

		ShowLeafPropertiesInfo showLeafPropertiesInfo = treeLayoutProperties.getShowLeafPropertiesInfo();
		showLeafPropertiesInfo.setShowLeafLabel(object.isShowLeafLabel());

		if (object.isShowLeafLabel()) {
			showLeafPropertiesInfo.setNeedChange4showLabel(true);
			showLeafPropertiesInfo.setNeedChange4hideLabel(false);

			List<GraphicsNode> leaves = treeLayoutProperties.getLeaves();
			for (GraphicsNode node : leaves) {
				node.getDrawUnit().setDrawName(true);
			}
		} else {
			showLeafPropertiesInfo.setNeedChange4showLabel(false);
			showLeafPropertiesInfo.setNeedChange4hideLabel(false);
		}

		treeLayoutProperties.setGlobalFont(object.getDefaultFont());
		treeLayoutProperties.setNeedReverseAxisBar(object.isNeedReverseAxisBar());
		treeLayoutProperties.setShouldLeafNameRightAlign(object.isShouldLeafNameRightAlign());
		treeLayoutProperties.setBranchLengthUnit(object.getBranchLengthUnit());
	
	
		treeLayoutProperties.setGlobalFont(object.getDefaultFont());
		treeLayoutProperties.setTitleFont(object.getDefaultTitleFont());
	}

	public void assignGraphicsNodeEffects(GraphicsNode rootNode, MTVImportInforBean evolTreeImportInfoBean)
			throws IOException {
		String nodeVisualAnnotationConfigFilePath = evolTreeImportInfoBean.getNodeVisualAnnotationConfigFilePath();
		if (nodeVisualAnnotationConfigFilePath == null) {
			return;
		}

		// name fillColor, circleRadius, lineThickness
		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap(nodeVisualAnnotationConfigFilePath);
		List<String> nameList = asKey2ListMap.get("name");
		if (nameList == null) {
			throw new InputMismatchException("The node graphic effects configuration file most has name column");
		}
		
		Map<String, GraphicsNode> name2nodeMap = new HashMap<>();
		EvolNodeUtil.recursiveIterateTreeIF(rootNode, node -> {
			String name = node.getName();
			if (!Strings.isNullOrEmpty(name)) {
				name2nodeMap.put(name, node);
			}
		});

		List<String> lists4tips = Lists.newLinkedList();
		StringBuilder lists4buttomBars = new StringBuilder();

		List<String> list = asKey2ListMap.get("fillColor");
		if (list != null) {

			Iterator<String> iterator = list.iterator();
			int renderedCount = 0;
			for (String string : nameList) {
				String next = iterator.next();
				GraphicsNode graphicsNode = name2nodeMap.get(string);
				if (graphicsNode != null) {
					Color color = EGPSColors.parseColor(next);
					TreeDrawUnit drawUnit = graphicsNode.getDrawUnit();
					drawUnit.setLineColor(color);
					renderedCount++;
				} else {
					lists4tips.add(string + " is not found.");
				}
			}

			lists4buttomBars.append("Total need to render is: ");
			lists4buttomBars.append(list.size());
			lists4buttomBars.append("; ");
			lists4buttomBars.append("Total rendered count is: ");
			lists4buttomBars.append(renderedCount);
			lists4buttomBars.append(".");

		}

		list = asKey2ListMap.get("circleRadius");
		if (list != null) {
			Iterator<String> iterator = list.iterator();
			for (String string : nameList) {
				String next = iterator.next();
				GraphicsNode graphicsNode = name2nodeMap.get(string);
				if (graphicsNode != null) {
					int int1 = Integer.parseInt(next);
					TreeDrawUnit drawUnit = graphicsNode.getDrawUnit();
					drawUnit.setCircleRadius(int1);
				}
			}
		}

		list = asKey2ListMap.get("lineThickness");
		if (list != null) {
			Iterator<String> iterator = list.iterator();
			for (String string : nameList) {
				String next = iterator.next();
				GraphicsNode graphicsNode = name2nodeMap.get(string);
				if (graphicsNode != null) {
					int int1 = Integer.parseInt(next);
					TreeDrawUnit drawUnit = graphicsNode.getDrawUnit();
					drawUnit.setStrokeSize(int1);
				}
			}
		}
		list = asKey2ListMap.get("displayName");
		if (list != null) {
			Iterator<String> iterator = list.iterator();
			for (String string : nameList) {
				String next = iterator.next();
				GraphicsNode graphicsNode = name2nodeMap.get(string);
				if (graphicsNode != null) {
					boolean boolean1 = BooleanUtils.toBoolean(next);
					TreeDrawUnit drawUnit = graphicsNode.getDrawUnit();
					drawUnit.setDrawName(boolean1);
				}
			}
		}

		if (UnifiedAccessPoint.isGULaunched()) {
			MyFrame instanceFrame = UnifiedAccessPoint.getInstanceFrame();
			instanceFrame.showTipsOnBottomStatusBar(lists4buttomBars.toString());
			for (String string : lists4tips) {
				instanceFrame.prompt(string);
			}
		}

	}

}
