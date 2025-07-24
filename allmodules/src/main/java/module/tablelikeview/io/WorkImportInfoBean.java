package module.tablelikeview.io;

import java.util.Optional;

import module.tablelikeview.gui.TSVImportInfoBean;

public class WorkImportInfoBean extends TSVImportInfoBean {

	protected int currentLineNumber = 1;
	protected boolean isFirstTime = true;

	protected Optional<String> searchString = Optional.empty();

	public int getCurrentLineNumber() {
		return currentLineNumber;
	}

	public void setCurrentLineNumber(int currentLineNumber) {
		this.currentLineNumber = currentLineNumber;
	}

	public boolean isFirstTime() {
		return isFirstTime;
	}

	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	public Optional<String> getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = Optional.ofNullable(searchString);
	}
}
