package module.remnant.datapanel;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.swing.SwingUtilities;

import egps2.utils.common.model.datatransfer.TwoTuple;
import module.remnant.datapanel.data.DataCenter;
import module.remnant.datapanel.data.DataType;
import module.remnant.datapanel.data.IDataCenter;
import module.remnant.datapanel.informationArea.InformationAreaMAF;
import module.remnant.datapanel.informationArea.InformationAreaMultipleSeqs;
import module.remnant.datapanel.informationArea.InformationAreaNexus;
import module.remnant.datapanel.informationArea.InformationAreaTxt;
import module.remnant.mafoperator.CalculateMAFInformation;
import egps2.modulei.IModuleLoader;

/**
 * The main entry panel management class!
 * 
 * @author Yudalang
 * @date 2018-03-22;2019-12-30
 * @version 1.0
 * @category management
 */
public class MyDataEventManager {

	private final DefaultPanel defaultPanel;
	private final IDataCenter dataCenter;

	private LeftDataInputArea leftArea;
	private MiddleInformationArea middleArea;
	private RightSuitableMethodsArea rightArea;

	private Logger log = Logger.getGlobal();

	/**
	 * 这个类将会在DefaultPanel实例化！
	 * 
	 * @param defaultPanel
	 */
	MyDataEventManager(DefaultPanel defaultPanel) {
		this.defaultPanel = defaultPanel;
		this.dataCenter = new DataCenter();
		defaultPanel.setDataCenter(dataCenter);
	}

	public IDataCenter getDataCenter() {
		return dataCenter;
	}

	public void setLeftDataInputArea(LeftDataInputArea leftArea) {
		this.leftArea = leftArea;
	}

	public void setRightSuitableMethodsArea(RightSuitableMethodsArea controller_AvailableMethodsView) {
		this.rightArea = controller_AvailableMethodsView;
	}

	public void setMiddleInformationArea(MiddleInformationArea middleInformationArea) {
		this.middleArea = middleInformationArea;

	}

	/**
	 * 导入数据都先放到这里来，然后让这个类去调用data center来执行这个判定！
	 * 
	 * @param draggedFiles
	 */
	public void handleFiles(List<File> draggedFiles) {
		if (draggedFiles == null || draggedFiles.size() == 0) {
			return;
		}

		log.info(draggedFiles.toString());
		boolean ifSuccess = dataCenter.loadingFiles(draggedFiles);
		if (!ifSuccess) {
			// Log dragged files error information, YDL:
			log.warning("Input File error code: " + dataCenter.getInputFileErrorIndex());
		}

		int numOfTotalInputFiles = dataCenter.getInputFiles().size();
		// 当拖入一个文件,并且是错误文件时,不需要继续向下执行
		if (numOfTotalInputFiles <= 0) {
			return;
		}
		// 导入新的文件
		int startFileIndex = leftArea.getNumberOfFileLinks();
		List<File> inputFiles = dataCenter.getInputFiles();
		List<File> subList = inputFiles.subList(startFileIndex, inputFiles.size());
		letLeftToLoadingLinks(subList);
		// Recent file actions
		// BioMainFrame.getInstance().addMoreFilesForRecentFileActions(subList);
		if (startFileIndex == 0) {
			// 从头导入文件
			letLeftFirstButtonToBeSelectAll();
			/**
			 * 注意，右边的按钮会被disable，因为要建索引！所以先加载按钮！
			 */
			letRightToLoadingMethods();
			letMiddleToLoadingDataInformation(draggedFiles.get(0));

		}

	}

	/**
	 * 删除数据从这里开始，也需要与data center进行交互！ 注意data center也要清空一下！
	 * 
	 * @param files
	 */
	public void removeFiles(List<File> files) {
		dataCenter.removeAllExistedFiles(files);
		if (dataCenter.getInputFiles().size() == 0) {
			clearRightArea();
			clearMiddleArea();
		} else {
//			tailInWorksForVCF();
		}
	}

	/**
	 * @category left data area
	 */
	void letLeftClearRedBorderAndUpdateMiddleInformation(File inputFile) {
		leftArea.clearRedBorder();
		middleArea.loadingInformation(inputFile);
	}

	/**
	 * @category left data area
	 */
	void letLeftToLoadingLinks(List<File> files) {
		leftArea.addValidateFiles(files);
	}

	/**
	 * @category left data area
	 */
	void letLeftStartBusyStatus(int[] indexes) {
		leftArea.letLeftStartBusyStatus(indexes);

	}

	/**
	 * @category left data area
	 */
	void letLeftBusyStatusShutDown(int[] indexes) {
		leftArea.letLeftBusyStatusShutDown(indexes);
	}

	/**
	 * @category left data area
	 */
	void letLeftFirstButtonToBeSelectAll() {
		leftArea.letFirstButtonToBeSelectAll();
	}

	/**
	 * @category middle information area
	 */
	void letMiddleToLoadingDataInformation(File file) {
		int dataType = dataCenter.getCurrentDataType();
		File firstFileInDataCenter = dataCenter.getInputFiles().get(0);
		switch (dataType) {

//		case DataType.VCF:
//			InformationAreaVCF vcfInformationArea = new InformationAreaVCF();
//			middleArea.addNewInformationArea(vcfInformationArea);
//			SwingUtilities.invokeLater(() -> {
//				vcfInformationArea.loadingInformationsOnce(firstFileInDataCenter);
//			});
//			tailInWorksForVCF();
//			break;
		case DataType.MAF:
			InformationAreaMAF mafInformationArea = new InformationAreaMAF();
			middleArea.addNewInformationArea(mafInformationArea);
			tailInWorksForMAF(mafInformationArea);

			break;
//		case DataType.EHEATMAP:
//			middleArea.addNewInformationArea(new InformationAreaEHeatmap());
//			middleArea.loadingInformation(firstFileInDataCenter);
//			break;
//		case DataType.MATRIX_TABLE:
//			middleArea.addNewInformationArea(new InformationAreaMatrix());
//			middleArea.loadingInformation(firstFileInDataCenter);
//			break;

		case DataType.MULTIPLE_SEQS:
			middleArea.addNewInformationArea(new InformationAreaMultipleSeqs());
			middleArea.loadingInformation(firstFileInDataCenter);
			break;
//		case DataType.GENETIC_DIST:
//		    middleArea.addNewInformationArea(new InformationAreaDist());
//			middleArea.loadingInformation(firstFileInDataCenter);
//		    break;
		case DataType.NEXUS:
			middleArea.addNewInformationArea(new InformationAreaNexus());
			middleArea.loadingInformation(firstFileInDataCenter);
			break;
//		case DataType.SIMULATOR:
//			middleArea.addNewInformationArea(new InformationAreaSimu());
//			middleArea.loadingInformation(firstFileInDataCenter);
//			break;
//		case DataType.TREE:
//			middleArea.addNewInformationArea(new InformationAreaTree());
//			middleArea.loadingInformation(firstFileInDataCenter);
//			break;
		case DataType.GENERAL_TEXT:
			middleArea.addNewInformationArea(new InformationAreaTxt());
			middleArea.loadingInformation(firstFileInDataCenter);
			break;

		default:
			log.info("Not information are for the input data! " + getClass());
		}

		middleArea.revalidate();
	}

//	private void tailInWorksForVCF() {
//		new Thread(() -> {
//			if (dataCenter.getCurrentDataFormat() == DataFormat.VCF_BZIP) {
//				List<File> inputFiles = dataCenter.getInputFiles();
//				if (inputFiles.size() > 0) {
//					File firstFile = inputFiles.get(0);
//					File tbi = new File(firstFile.getAbsolutePath() + TabixUtils.STANDARD_INDEX_EXTENSION);
//					if (!tbi.exists()) {
//						enableSpecificMethodAButton(MethodsForVCFSnapShot.class, false);
//						letLeftStartBusyStatus(new int[] { 0 });
//
//						new VCFIndexUtil().buildTabixIndex(firstFile);
//
//						enableSpecificMethodAButton(MethodsForVCFSnapShot.class, true);
//						letLeftBusyStatusShutDown(new int[] { 0 });
//					}
//				}
//			}
//		}).start();
//	}

	private void tailInWorksForMAF(InformationAreaMAF mafInformationArea) {

		List<File> inputFiles = dataCenter.getInputFiles();
		CalculateMAFInformation mafInformation = new CalculateMAFInformation();

		File firstFileInDataCenter = inputFiles.get(0);
		TwoTuple<Boolean, List<String>> twoTuple = mafInformation.checkMainConfigFile(firstFileInDataCenter);

		if (twoTuple.first) {
			SwingUtilities.invokeLater(() -> {
				mafInformationArea.loadingInformationsOnce(twoTuple.second);
			});
		} else {
			// 先产生左边的繁忙状态，再逐个建立索引，最终取消状态
			new Thread(() -> {
//				enableSpecificMethodAButton(MethodsForMAF2TreeViewer.class, false);
				int[] array = IntStream.range(0, inputFiles.size()).toArray();
				letLeftStartBusyStatus(array);

				for (int i = 0; i < array.length; i++) {
					mafInformation.checkAndProduceFileSpecificConfigFile(inputFiles.get(i));
					letLeftBusyStatusShutDown(new int[] { i });
				}
				mafInformation.unionSepices(inputFiles);
				SwingUtilities.invokeLater(() -> {
					mafInformationArea.loadingInformationsOnce(mafInformation.getSeqList());
				});
//				enableSpecificMethodAButton(MethodsForMAF2TreeViewer.class, true);
			}).start();

		}

	}

	/**
	 * @category middle information area
	 */
	void clearMiddleArea() {
		// Not like rightPane, the center pane need to be reset!
		middleArea.removeAll();
		defaultPanel.repaint();
	}

	/**
	 * @category right method area
	 */
	void letRightToLoadingMethods() {
		rightArea.suitableMethods(dataCenter.getCurrentDataFormat());
		rightArea.revalidate();
	}

	/**
	 * @category right method area
	 */
	void clearRightArea() {
		rightArea.clearButtons();
	}

	/**
	 * 
	 * @param clz
	 * @param enable
	 * @category right method area
	 */
	void enableSpecificMethodAButton(Class<? extends IModuleLoader> clz, boolean enable) {
		try {
			rightArea.getMethoButton(clz).setEnable(enable);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public IDataInformation getDataInformationPanel() {
		return middleArea.getDataInfoamtion();
	}

	public DefaultPanel getDefaultPanel() {
		return defaultPanel;
	}
}
