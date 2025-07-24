package module.evolview.moderntreeviewer.io;

import java.awt.Font;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.base.Strings;

import egps2.utils.common.util.EGPSFonts;
import utils.string.EGPSStringUtil;
import egps2.UnifiedAccessPoint;
import module.evoltreio.ParamsAssignerAndParser4PhyloTree;
import graphic.engine.guicalculator.BlankArea;
import egps2.builtin.modules.voice.fastmodvoice.OrganizedParameterGetter;

public class ParamsAssignerAndParser4ModernTreeView extends ParamsAssignerAndParser4PhyloTree {

	public ParamsAssignerAndParser4ModernTreeView() {
		super();

		Font defaultFont = UnifiedAccessPoint.getLaunchProperty().getDefaultFont().deriveFont(9f);
		Font defaultTitleFont = UnifiedAccessPoint.getLaunchProperty().getDefaultTitleFont();
		String codeFont = EGPSFonts.codeFont(defaultFont);
		String codeTitleFont = EGPSFonts.codeFont(defaultTitleFont);
		addKeyValueEntryBean("tree.global.font", codeFont,
				"The font for global, current the leaf label names employ it.");
		addKeyValueEntryBean("bottom.title.font", codeTitleFont, "The font for bottom title.");
		addKeyValueEntryBean("blank.space", "20,20,80,20", "The blank area of top,left,bottom,right .");
		addKeyValueEntryBean("height.scale.on", "T", "Whether mouse wheel scale on for the height, default is True.");
		addKeyValueEntryBean("width.scale.on", "T", "Whether mouse wheel scale on for the width, default is True");
		addKeyValueEntryBean("show.leaf.label", "T", "Whether display the leaf label on the tree.");
		addKeyValueEntryBean("leaf.label.right.align", "T", "Whether right align the leaf labels.");
		addKeyValueEntryBean("need.reverse.axis.bar", "F",
				"Whether reverse the axis bar. In some cases, the time is years ago.");
		addKeyValueEntryBean("bottom.title.string", "The phylogenetic tree with {0} high-quality sequenced samples.",
				"The statement that displayed in the bottom of the tree view. {0} will be replace with number of leaf.");
		addKeyValueEntryBean("branch.length.unit", "",
				"The unit of branch, for example mya(million years ago) or the evolutionary rate. Default, none.");
		addKeyValueEntryBean("node.visual.config.path", "",
				"Node specific visual annotations, current supported(Note, only fixed symbols will be recognized) :name, fillColor, circleRadius, lineThickness, displayName.\n"
						+ "# Please input a tsv file with header. For e.g (remove # in real file):\n"
						+ "#name\tfillColor\tcircleRadius\tlineThickness\tdisplayName\n#leaf1\t#FF00FF\t2\t2\tT\n#leaf2\t12,00,34\t1\t2\tF");

	}

	@Override
	public MTVImportInforBean generateTreeFromKeyValue(OrganizedParameterGetter input) {
		MTVImportInforBean ret = new MTVImportInforBean();
		super.assignValues(input, ret);

		String string = input.getSimplifiedStringWithDefault("$blank.space");
		if (!string.isEmpty()) {
			String[] split = EGPSStringUtil.split(string, ',');
			int top = Integer.parseInt(split[0]);
			int left = Integer.parseInt(split[1]);
			int bottom = Integer.parseInt(split[2]);
			int right = Integer.parseInt(split[3]);
			BlankArea eggsInsets = new BlankArea(top, left, bottom, right);
			ret.setBlank_space(eggsInsets);
		}

		string = input.getSimplifiedStringWithDefault("$bottom.title.string");
		if (!string.isEmpty()) {
			ret.setTitleString(string);
		}

		string = input.getSimplifiedStringWithDefault("$height.scale.on");
		if (!string.isEmpty()) {
			boolean boolean1 = BooleanUtils.toBoolean(string);
			ret.setWhetherHeightScaleOnMouseWheel(boolean1);
		}
		string = input.getSimplifiedStringWithDefault("$width.scale.on");
		if (!string.isEmpty()) {
			boolean boolean1 = BooleanUtils.toBoolean(string);
			ret.setWhetherWidthScaleOnMouseWheel(boolean1);
		}
		string = input.getSimplifiedStringWithDefault("$show.leaf.label");
		if (!string.isEmpty()) {
			boolean boolean1 = BooleanUtils.toBoolean(string);
			ret.setShowLeafLabel(boolean1);
		}
		string = input.getSimplifiedStringWithDefault("$need.reverse.axis.bar");
		if (!string.isEmpty()) {
			boolean boolean1 = BooleanUtils.toBoolean(string);
			ret.setNeedReverseAxisBar(boolean1);
		}

		string = input.getSimplifiedStringWithDefault("$leaf.label.right.align");
		if (!string.isEmpty()) {
			boolean boolean1 = BooleanUtils.toBoolean(string);
			ret.setShouldLeafNameRightAlign(boolean1);
		}

		string = input.getSimplifiedStringWithDefault("$tree.global.font");
		if (!string.isEmpty()) {
			Font font = EGPSFonts.parseFont(string);
			ret.setDefaultFont(font);
		}

		string = input.getSimplifiedStringWithDefault("$bottom.title.font");
		if (!string.isEmpty()) {
			Font font = EGPSFonts.parseFont(string);
			ret.setDefaultTitleFont(font);
		}
		string = input.getSimplifiedStringWithDefault("$branch.length.unit");
		if (!string.isEmpty()) {
			ret.setBranchLengthUnit(string);
		}
		string = input.getSimplifiedStringWithDefault("$node.visual.config.path");
		if (!Strings.isNullOrEmpty(string)) {
			ret.setNodeVisualAnnotationConfigFilePath(string);
		}

		return ret;
	}

}
