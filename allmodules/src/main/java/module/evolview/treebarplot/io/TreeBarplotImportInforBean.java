package module.evolview.treebarplot.io;

import module.evolview.moderntreeviewer.io.MTVImportInforBean;

public class TreeBarplotImportInforBean extends MTVImportInforBean {
    protected int rightBlank = 40;
    protected int treeRightBlankSpaceDivider = 3;

    protected String barPlotDataFile;

    public TreeBarplotImportInforBean() {
        super();
    }

    public TreeBarplotImportInforBean(MTVImportInforBean object) {

        this.setInput_nwk_path(object.getInput_nwk_path());
        this.setShowLeafLabel(object.isShowLeafLabel());
        this.setShouldLeafNameRightAlign(object.isShouldLeafNameRightAlign());
        this.setNeedReverseAxisBar(object.isNeedReverseAxisBar());
        this.setWhetherHeightScaleOnMouseWheel(object.isWhetherHeightScaleOnMouseWheel());
        this.setWhetherWidthScaleOnMouseWheel(object.isWhetherWidthScaleOnMouseWheel());

        this.blank_space = object.getBlank_space();
        this.titleString = object.getTitleString();
        this.setRemoveWhitespace(object.isRemoveWhitespace());

        branchLengthUnit = object.getBranchLengthUnit();

        this.defaultFont = object.getDefaultFont();
        this.defaultTitleFont = object.getDefaultTitleFont();


    }

    public int getRightBlank() {
        return rightBlank;
    }

    public void setRightBlank(int rightBlank) {
        this.rightBlank = rightBlank;
    }

    public int getTreeRightBlankSpaceDivider() {
        return treeRightBlankSpaceDivider;
    }

    public void setTreeRightBlankSpaceDivider(int treeRightBlankSpaceDivider) {
        this.treeRightBlankSpaceDivider = treeRightBlankSpaceDivider;
    }


    public String getBarPlotDataFile() {
        return barPlotDataFile;
    }

    public void setBarPlotDataFile(String barPlotDataFile) {
        this.barPlotDataFile = barPlotDataFile;
    }


}
