package module.tablelikeview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.google.common.collect.Lists;

import egps2.panels.dialog.SwingDialog;
import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;
import egps2.frame.ComputationalModuleFace;
import module.tablelikeview.gui.CustomTablePanel;
import module.tablelikeview.gui.NavigationPanel;
import module.tablelikeview.io.VOICM4TableLikeView;
import module.tablelikeview.io.WorkImportInfoBean;
import egps2.modulei.IInformation;
import egps2.modulei.IModuleLoader;
import egps2.modulei.RunningTask;

@SuppressWarnings("serial")
public class MainGUI extends ComputationalModuleFace implements RunningTask {
	private JSplitPane jSplitPane;
	private CustomTablePanel rightJPanel = new CustomTablePanel();
	private VOICM4TableLikeView voicm4TableLikeView;
	private WorkImportInfoBean importBean;
	private SimpleLeftControlPanel simpleLeftControlPanel;

	private NavigationPanel navigationPanel;

	protected MainGUI(IModuleLoader moduleLoader) {
		super(moduleLoader);

		setLayout(new BorderLayout());

		jSplitPane = new JSplitPane();

		simpleLeftControlPanel = new SimpleLeftControlPanel(this);

		jSplitPane.setLeftComponent(simpleLeftControlPanel);

		/**
		 * 请帮我写一个JTable，它第一列的内容是行号。输入的内容 是一个List<List<String>> input 的数据结构，这个内容是不包括行号的。
		 * 要求这个Jtable美观。这个input内容输入的过程单独写一个 loading方法，可以动态变换数据。
		 */
		jSplitPane.setRightComponent(rightJPanel);

		add(jSplitPane, BorderLayout.CENTER);

	}

	public JPanel getRightJPanel() {
		return rightJPanel;
	}

	public Dimension getPaintingPanelDim() {
		return rightJPanel.getSize();
	}

	@Override
	public boolean closeTab() {
		return false;
	}

	@Override
	public void changeToThisTab() {

	}

	@Override
	public boolean canImport() {
		return true;
	}

	@Override
	public void importData() {
		if (voicm4TableLikeView == null) {
			voicm4TableLikeView = new VOICM4TableLikeView(this);
		}
		voicm4TableLikeView.doUserImportAction();
	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void exportData() {

	}

	@Override
	public String[] getFeatureNames() {
		return new String[] { "Watch the table-like contents." };
	}

	@Override
	protected void initializeGraphics() {
		importData();
	}

	@Override
	public IInformation getModuleInfo() {
		IInformation iInformation = new IInformation() {

			@Override
			public String getWhatDataInvoked() {
				return "The data is loading from the import dialog.";
			}

			@Override
			public String getSummaryOfResults() {
				return "The functionality is powered by the eGPS software.";
			}
		};
		return iInformation;
	}

	public void loadingData(WorkImportInfoBean importBean) {
		this.importBean = importBean;
		registerRunningTask(this);

	}

	@Override
	public boolean isTimeCanEstimate() {
		return false;
	}

	@Override
	public int processNext() throws Exception {
		int currentLineNumber = importBean.getCurrentLineNumber();
		int tableContentLineNumber = importBean.getTableContentLineNumber();

		InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(importBean.getFilePath());

		char seperator = importBean.getSeperator();
		if (importBean.isFirstTime()) {
			importBean.setFirstTime(false);
			importBean.setCurrentLineNumber(1);
			this.navigationPanel.initializeTotalLineNumber(importBean);
		}
		List<List<String>> ret = new LinkedList<>();

		int lineIndex = 0;
		boolean hasHeader = importBean.isHasHeader();
		List<String> headerLine = null;
		int sz = 1 << 30;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8), sz)) {
			String readLine = null;
			int count = 0;
			while ((readLine = br.readLine()) != null) {
				if (hasHeader) {
					hasHeader = false;
					String[] split = EGPSStringUtil.split(readLine, seperator);
					headerLine = Lists.asList("Row", split);
					continue;
				}

				lineIndex++;

				if (lineIndex >= currentLineNumber && passFilter(importBean, readLine)) {
					count++;
					String[] split = EGPSStringUtil.split(readLine, seperator);
					List<String> asList = Lists.asList(String.valueOf(lineIndex), split);

					ret.add(asList);
					if (count > tableContentLineNumber) {
						break;
					}
				}

			}
		}

		if (headerLine == null) {
			headerLine = new ArrayList<>();
			headerLine.add("Row");

			int size = ret.get(0).size();

			for (int i = 0; i < size; i++) {
				String str = "Column " + (i + 1);
				headerLine.add(str);
			}
		}

		rightJPanel.loadData(ret, headerLine);

		navigationPanel.initializePanel(importBean);

		return PROGRESS_FINSHED;
	}

	private boolean passFilter(WorkImportInfoBean importBean2, String readLine) {
		Optional<String> searchString = importBean2.getSearchString();
		if (searchString.isPresent()) {
			return readLine.contains(searchString.get());
		} else {
			return true;
		}

	}

	public void setNavigationPanel(NavigationPanel navigationPanel) {
		this.navigationPanel = navigationPanel;
	}

	public void nextPage() {
		int currentLineNumber = importBean.getCurrentLineNumber();
		int tableContentLineNumber = importBean.getTableContentLineNumber();
		importBean.setCurrentLineNumber(currentLineNumber + tableContentLineNumber);

		registerRunningTask(this);
	}

	public void previousPage() {
		int currentLineNumber = importBean.getCurrentLineNumber();
		int tableContentLineNumber = importBean.getTableContentLineNumber();
		int currentLineNumber2 = currentLineNumber - tableContentLineNumber;
		if (currentLineNumber2 < 1) {
			SwingDialog.showErrorMSGDialog("Error", "No Previous line information.");
		} else {
			importBean.setCurrentLineNumber(currentLineNumber2);
			registerRunningTask(this);
		}

	}

	public void gotoLineOf(int int1) {
		importBean.setCurrentLineNumber(int1);
		registerRunningTask(this);
	}

	public void searchString(String text) {
		if (text.isEmpty()) {
			SwingDialog.showInfoMSGDialog("Enter", "Please enter the search context first.");
			return;
		}

		importBean.setSearchString(text);
		registerRunningTask(this);
	}

	public void clearSearch() {
		importBean.setSearchString(null);
		registerRunningTask(this);
	}

	public void changeJTableResizeMode(int autoResizeAllColumns) {
		rightJPanel.changeJTableResizeMode(autoResizeAllColumns);
	}

	public void setPreferenceWidth(int width) {
		rightJPanel.setPrefColumnWidth(width);
	}
}
