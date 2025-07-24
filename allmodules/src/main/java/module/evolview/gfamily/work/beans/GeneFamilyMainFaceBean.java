package module.evolview.gfamily.work.beans;

public class GeneFamilyMainFaceBean {

	double savedPerOfTreeAndBrowser = 0.5;
	int savedLocationOfMainSplitPanel = 420;

	public GeneFamilyMainFaceBean() {
	}

	public GeneFamilyMainFaceBean(double savedPerOfTreeAndBrowser, int savedLocationOfMainSplitPanel) {
		super();
		this.savedPerOfTreeAndBrowser = savedPerOfTreeAndBrowser;
		this.savedLocationOfMainSplitPanel = savedLocationOfMainSplitPanel;
	}

	public double getSavedPerOfTreeAndBrowser() {
		return savedPerOfTreeAndBrowser;
	}

	public void setSavedPerOfTreeAndBrowser(double savedPerOfTreeAndBrowser) {
		this.savedPerOfTreeAndBrowser = savedPerOfTreeAndBrowser;
	}

	public int getSavedLocationOfMainSplitPanel() {
		return savedLocationOfMainSplitPanel;
	}

	public void setSavedLocationOfMainSplitPanel(int savedLocationOfMainSplitPanel) {
		this.savedLocationOfMainSplitPanel = savedLocationOfMainSplitPanel;
	}
	
	
}
