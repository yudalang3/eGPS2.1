package module.remnant.datapanel.data;

import static module.remnant.datapanel.data.DataFormatError.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import egps2.panels.dialog.SwingDialog;
import egps2.utils.common.model.datatransfer.TwoTuple;
import msaoperator.DataForamtPrivateInfor;
import msaoperator.DataforamtInfo;
import msaoperator.DefaultDataFormatPrivateInfor;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 
 * Copyright (c) 2018 Chinese Academy of Sciences. All rights reserved.
 * 
 * <pre>
 * This is the Data Center contains all the data type and data format!
 * Users need to identify all the features of following conceptions:
 * 1. data type name
 * 2. data format name/short name
 * 3. default file suffix name
 * 4. file suffix name regular expression (for multple suffix are permitted!)
 * 5. full name / description (don't need for coding,no place will invoke!)
 * 6. data type index (an integer )
 * 7. data format index (an integer )
 * 
 * </pre>
 * 
 * @author mhl,ydl
 * @version V1.0
 * @Date Created on: 2018-05-23 10:58
 * 
 */
public class DataCenter implements DataFormat,IDataCenter {

	private int inputFileERROR;

	private int currentDataType = DataType.NO_DATA;
	private int currentDataFormat = DataFormat.NO_FOMAT;

	/** All input files for eGPS */
	private List<File> inputFiles = new ArrayList<File>(20);
	
	public DataCenter() {
		
	}

	/**
	 * Ying yong mo shi
	 */
	DataTypeParser dataTypeParser = new DataTypeParser();

	/** return the input file error index */
	@Override
	public int getInputFileErrorIndex() {
		return inputFileERROR;
	}

	/** return current data format */
	@Override
	public int getCurrentDataFormat() {
		return currentDataFormat;
	}

	/** return current data format */
	@Override
	public int getCurrentDataType() {
		return currentDataType;
	}

	/** set data format and data type to No status ! */
	private void setStatusToEmpty() {
		this.currentDataFormat = DataFormat.NO_FOMAT;
		this.currentDataType = DataType.NO_DATA;
	}

	/**
	 * remove files<br>
	 * We don't have clear() method, cause remove() will only be invoked in
	 * Controller
	 * {@link egps.controller.ControllerDataFile#removeAllSecletedFiles()} <br>
	 * If you want to remove all file:<br>
	 * <code>dataCenter.removeAllExistedFiles(dataCenter.getInputFiles());</code>
	 */
	@Override
	public void removeAllExistedFiles(List<File> filesNeedRemove) {
		inputFiles.removeAll(filesNeedRemove);
		if (inputFiles.size() == 0) {
			setStatusToEmpty();
		}
	}

	/** TestExample method to get input files */
	@Override
	public List<File> getInputFiles() {
		return inputFiles;
	}


	/**
	 * determine if input file compatible with current files <br>
	 * some situations you need to consider: 1. if file already existed ! 2. data
	 * type and data format compatible with previous states !
	 * 
	 * @return true : compatible, new file can be included! else can't !
	 */
//	public boolean isCompatible(File file) {
//
//		if (ifSameWithExistFiles(file)) {
//			return false;
//		}
//
//		if (!judgeDateType(file)) {
//			return false;
//		}
//
//		if (!judegeDataFormat(file)) {
//			return false;
//		}
//
//		return true;
//	}

	/**
	 * Loading files and show the dialog if errors happen!
	 * 
	 * @param files
	 * @return whether success to load data.
	 */
	@Override
	public boolean loadingFiles(List<File> files) {
		if (getIfContainsData()) {
			return dealWithFilesWhenExistData(files);
		} else {
			return dealWtihMultiFileNoData(files);
		}

	}

	/**
	 * Note: In the method DT and DF correct means the file it self is correct! The
	 * way to judge weather a file it self is correct is:<br>
	 * Firstly, get data type, next get data format; finally, data type != NO_DATA
	 * and {@link #getDataTypeFromDataFormat(int data format)} = data type
	 * 
	 * @param files
	 * @return if we can infer correct DT and DF from input data, also get the
	 *         correct files!
	 */
	private boolean dealWtihMultiFileNoData(List<File> files) {
		List<JudgedFileStatusBean> status = new ArrayList<JudgedFileStatusBean>();
		// The first step is just judge the input files
		for (File file : files) {
			JudgedFileStatusBean ss = new JudgedFileStatusBean();
			ss.setFileName(file.getName());
			ss.setIfRepeat(false);

			// DT
			TwoTuple<Integer, Integer> firstLightJudgeData = dataTypeParser.firstLightJudgeData(file);
			int dataType = firstLightJudgeData.first;
			ss.setDataTypeCode(dataType);
			if (dataType != DataType.NO_DATA) {
				ss.setIfDataTypeCorrect(true);
				DataforamtInfo newDataFormat = dataTypeParser.getDataFormat(file, firstLightJudgeData);
				ss.setDataFormatCode(newDataFormat.getDataFormatCode());
				int tt = getDataTypeFromDataFormat(newDataFormat.getDataFormatCode());
				//设置私有域
				ss.setPrivateData(newDataFormat.getDataForamtPrivateInfor().orElse(new DefaultDataFormatPrivateInfor()));
				
				if (tt == dataType) {
					ss.setIfDataFormatCorrect(true);
				} else {
					//YDL: may be this file is General text file, and have data format
					if (dataType == DataType.GENERAL_TEXT && tt != DataType.NO_DATA) {
						ss.setIfDataFormatCorrect(true);
						ss.setDataTypeCode(tt);
					}else {
						ss.setIfDataFormatCorrect(false);
					}
				}
			} else {
				ss.setIfDataTypeCorrect(false);
			}

			status.add(ss);
		}

		// make decision when there exists file not correct
		// find first file contains correct data type and data format
		boolean ifContainsCorrectDTDF = false;
		DataForamtPrivateInfor currentPrivateData = null;
		for (JudgedFileStatusBean ss : status) {
			if (ss.isIfDataTypeCorrect() && ss.isIfDataFormatCorrect()) {
				ifContainsCorrectDTDF = true;
				currentDataType = ss.getDataTypeCode();
				currentDataFormat = ss.getDataFormatCode();
				currentPrivateData = ss.getPrivateData();
				break;
			}
		}
		if (ifContainsCorrectDTDF) {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("New input files contain data format supported by eGPS:")
					.append("\nWe recognized first supported data type is ")
					.append(getDataTypeNameFromDataType(currentDataType)).append(",data format is ")
					.append(getDataFormatNameFromDataFormat(currentDataFormat))
					.append("\nBut some files contain follwing error:");

			boolean ifAllFilesCorrect = true;
			for (int i = 0; i < status.size(); i++) {
				JudgedFileStatusBean ss = status.get(i);
				Pair<Boolean, String> compatiable = null;
				
				if (ss.getDataTypeCode() == currentDataType && ss.getDataFormatCode() == currentDataFormat) {
					compatiable = currentPrivateData.isCompatiable(ss.getPrivateData());
					if (compatiable.getKey()) {
						inputFiles.add(files.get(i));
						continue;
					}
				}
				ifAllFilesCorrect = false;

				sBuilder.append("\nFile: ");
				// first judge the data type and data format

				if (!ss.isIfDataTypeCorrect()) {
					int dataTypeCode = ss.getDataTypeCode();

					if (dataTypeCode == DataType.NO_DATA) {
						sBuilder.append(ss.getFileName()).append(" is not supported by eGPS!");
					} else {
						sBuilder.append(ss.getFileName()).append(" is not compatible with current data type ")
								.append(getDataTypeNameFromDataType(dataTypeCode));
					}

				} else if (ss.getDataTypeCode() != currentDataType) {
					sBuilder.append(ss.getFileName()).append(" not compatible with current data type!");
				} else if (!ss.isIfDataFormatCorrect()) {
					sBuilder.append(ss.getFileName()).append(" ").append(getInputFileERRORAlertString(ss.getDataFormatCode()));
				} else if (ss.getDataFormatCode() != currentDataFormat) {
					sBuilder.append(ss.getFileName()).append(" not compatible with current data format!");
				} else if (!compatiable.getKey()) {
					sBuilder.append(ss.getFileName()).append(" "+ compatiable.getValue() +"!");
				} else {
					// unknow error
					sBuilder.append(ss.getFileName())
							.append(" contains unknow error!Please feel free to contact developer.");
				}
				
			}

			if (!ifAllFilesCorrect) {
				SwingDialog.showErrorMSGDialog("Date error", sBuilder.toString());
			}

		} else {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("None of your input data are supported by eGPS!");
			for (JudgedFileStatusBean ss : status) {
				sBuilder.append("\nFile: ");
				if (!ss.isIfDataTypeCorrect()) {
					sBuilder.append(ss.getFileName()).append(" is not supported by eGPS!");
				} else if (!ss.isIfDataFormatCorrect()) {
					sBuilder.append(ss.getFileName()).append(" recognized is ")
							.append(getDataTypeNameFromDataType(ss.getDataTypeCode()))
							.append(",but data format is not correct!")
					        .append(" ").append(getInputFileERRORAlertString(ss.getDataFormatCode()));
				} else {
					// unknow error
					sBuilder.append(ss.getFileName())
							.append(" contains unknow error!Please feel free to contact developer.");
				}

			}
			SwingDialog.showErrorMSGDialog("Date error", sBuilder.toString());
			return false;
		}

		return true;
	}

	private boolean dealWithFilesWhenExistData(List<File> files) {
		List<JudgedFileStatusBean> status = new ArrayList<JudgedFileStatusBean>();
		int numOfCorrectFiles = 0;

		/**
		 * 第一阶段：判断文件的内容，生成对应的 Judged file status bean
		 */
		for (File file : files) {
			JudgedFileStatusBean jfsBean = new JudgedFileStatusBean();
			jfsBean.setFileName(file.getName());
			// if repeat!
			jfsBean.setIfRepeat(ifFileExisted(file));
			// DT
			TwoTuple<Integer, Integer> firstLightJudgeData = dataTypeParser.firstLightJudgeData(file);
			int dataType = firstLightJudgeData.first;
			jfsBean.setDataTypeCode(dataType);
			if (dataType == currentDataType) {
				jfsBean.setIfDataTypeCorrect(true);
				DataforamtInfo newDataFormat = dataTypeParser.getDataFormat(file, firstLightJudgeData);
				jfsBean.setDataFormatCode(newDataFormat.getDataFormatCode());
				int tt = getDataTypeFromDataFormat(newDataFormat.getDataFormatCode());
				if (tt == dataType) {
					if (newDataFormat.getDataFormatCode() == currentDataFormat) {
						// DT正确，DF正确，又不是重复文件，可以加入进来！
						if (!jfsBean.isIfRepeat()) {
							inputFiles.add(file);
							jfsBean.setIfDataFormatCorrect(true);
							jfsBean.setAvilable(true);
							numOfCorrectFiles++;
						}
					}
				}
			}
			// NOTE: YDL: the default status is false, so the else block don't need to set
			// false

			status.add(jfsBean);
		}

		/**
		 * 第二阶段：根据状态的Beans来总结！有问题弹出Dialog! make decision when there exists file not
		 * correct
		 */
		if (numOfCorrectFiles != files.size()) {

			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("New input files contain follwing error:");

			for (JudgedFileStatusBean ss : status) {
				if (ss.isAvilable()) {
					continue;
				}
				sBuilder.append("\nFile: ");
				// unAvilable then we need to output information!
				if (ss.isIfRepeat()) {
					sBuilder.append(ss.getFileName()).append(" is already exists!");
				} else if (!ss.isIfDataTypeCorrect()) {
					int dataTypeCode = ss.getDataTypeCode();

					if (dataTypeCode == DataType.NO_DATA) {
						sBuilder.append(ss.getFileName()).append(" is not supported by eGPS!");
					} else {
						sBuilder.append(ss.getFileName()).append(" is not compatible with current data type!")
								.append("New input type is ").append(getDataTypeNameFromDataType(dataTypeCode));
					}

				} else if (!ss.isIfDataFormatCorrect()) {
					if (getDataTypeFromDataFormat(ss.getDataFormatCode()) == currentDataType) {
						sBuilder.append(ss.getFileName()).append(" is not compatible with current data format!");
					} else {
						sBuilder.append(ss.getFileName()).append(" contains data format error!")
								.append(getInputFileERRORAlertString(ss.getDataFormatCode()));
					}
				} else {
					// unknow error
					sBuilder.append(ss.getFileName())
							.append(" contains unknow error!Please feel free to contact developer.");
				}
			}

			SwingDialog.showErrorMSGDialog("Date type incompatiable", sBuilder.toString());
			return false;
		}

		return true;
	}
	
	private boolean ifFileExisted(File file) {
		boolean ifExist = false;
		for (File everyIFile : inputFiles) {
			boolean equalsIgnoreCase = everyIFile.getAbsolutePath().equalsIgnoreCase(file.getAbsolutePath());
			if (equalsIgnoreCase) {
				ifExist = true;
				break;
			}
		}
		return ifExist;
	}

	/**
	 * When file input from external dirs, the input file will contain errors!
	 * These error will have an error code!
	 * This error stored in the DataCenter, because data will first passed to DataCenter!
	 * 
	 * The error categories include:
	 * 
	 */
	private String getInputFileERRORAlertString(int inputFileERROR) {
		
		String ret = "The input file does not match the defined format!";

		switch (inputFileERROR) {

		case VCF_HEADERERROR:
			ret = "First annotation line miss VCF format word!";
			break;
		case VCF_GTERROR:
			ret = "GT not locate in the first part of FORMAT field!";
			break;
		case VCF_UNSAMPLEIDSERROR:
			ret = "New file's individul IDs are not identical to current individul IDs!";
			break;
		case VCF_NOANNOTATIONSERROR:
			ret = "The provide VCF file is not complete. You need to provide meta infromation!";
			break;
		case VCF_NOCONTENT_ERROR:
			ret = "The provide VCF file is not complete. You need to provide at least one variant!";
			break;
		case NEXUS_FILE_HEADERERROR:
			ret = "The first line of NEXUS format should be '#NEXUS' !";
			break;
		case NEXUS_FILE_CONTENTERROR:
			ret = "This file don't contain any validate information: data or trees !";
			break;

		default:
			break;
		}
		return ret;
	}

	/**
	 * Determine if the file exists.<br>
	 * inputFileERROR will be assigned if file already exist!
	 * 
	 */
//	private boolean ifSameWithExistFiles(File file) {
//		List<File> existInputFiles = getInputFiles();
//		if (existInputFiles.size() != 0) {
//			for (File existInputFile : existInputFiles) {
//				if (existInputFile.equals(file)) {
//					inputFileERROR = DataFormatError.SAMEFILEERROR;
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	/**
	 * Determine file compatibility! <br>
	 * {@link #currentDataType} will be assigned if correctly judged!
	 * 
	 * @return true : correctly judge file's data type
	 */
//	private boolean judgeDateType(File file) {
//
//		int newImportedFileDataType = DataTypeParser.getDataType(file);
//
//		if (newImportedFileDataType == DataType.NO_DATA) {
//			inputFileERROR = DataFormatError.FILETYPEERROR;
//			return false;
//		}
//
//		if (currentDataType == DataType.NO_DATA || getInputFiles().size() == 0) {
//			// When the home page does not have a file,to assign a value
//			currentDataType = newImportedFileDataType;
//
//		} else {
//			// New imported file data type is different
//			if (newImportedFileDataType != currentDataType) {
//				inputFileERROR = DataFormatError.FILETYPEERROR;
//				return false;
//			}
//		}
//		return true;
//	}

	// Judge file format
//	private boolean judegeDataFormat(File file) {
//		int newDataFormat = DataTypeParser.getDataFormat(file, currentDataType);
//
//		int dataType = getDataTypeFromDataFormat(newDataFormat);
//		if (dataType != currentDataType) {
//			inputFileERROR = newDataFormat;
//			return false;
//		}
//
//		if (currentDataFormat != DataFormat.NO_FOMAT) {
//			if (newDataFormat != currentDataFormat) {
//				inputFileERROR = newDataFormat;
//				return false;
//			}
//		}
//		currentDataFormat = newDataFormat;
//		return true;
//
//	}

	public String getDataFormatNameFromDataFormat(int dataFormat) {
		String stringOfdataFormat = "eGPS";
		switch (dataFormat) {
		case VCF_UNCOMPRESS:
			stringOfdataFormat = "UnCompressVCF";
			break;
		case VCF_BZIP:
			stringOfdataFormat = "BgzipCompressVCF";
			break;
		case VCF_GZIP:
			stringOfdataFormat = "GzipCompressVCF";
			break;
		case TREE_NHX:
			stringOfdataFormat = "Nhx tree";
			break;
		case TREE_NH:
			stringOfdataFormat = "Newick tree";
			break;
		case TREE_ETREE:
			stringOfdataFormat = "eGPS tree";
			break;
		case CIRCUS_RNA:
			stringOfdataFormat = "eGPS CircusRNA";
			break;
		case ALIGNED_EMBL:
			stringOfdataFormat = "Aligned EMBL";
			break;
		case ALIGNED_STOCKHOLM:
			stringOfdataFormat = "Aligned stockholm";
			break;
		case NEXML_TREE:
			stringOfdataFormat = "Nexml tree";
			break;
		case NEXML_SEQ:
			stringOfdataFormat = "Nexml seqs";
			break;
		case NEXML_BOTH:
			stringOfdataFormat = "Nexml tree/seqs";
			break;
		case NEXUS_TREE:
			stringOfdataFormat = "Nexus tree";
			break;
		case NEXUS_SEQ:
			stringOfdataFormat = "Nexus seqs";
			break;
		case NEXUS_BOTH:
			stringOfdataFormat = "Nexus tree/seqs";
			break;
		case ALIGNED_PIRNBRF:
			stringOfdataFormat = "Aligned PIR/NBRF";
			break;
		case ALIGNED_PHYLIP:
			stringOfdataFormat = "Phylip MSA";
			break;
		case ALIGNED_MEGA:
			stringOfdataFormat = "MEGA MSA";
			break;
		case ALIGNED_PAML:
			stringOfdataFormat = "PAML MSA";
			break;
		case ALIGNED_CLUSTALW:
			stringOfdataFormat = "ClustalW MSA";
			break;
		case ALIGNED_GCGMSF:
			stringOfdataFormat = "GCG(MSF) MSA";
			break;
		case ALIGNED_FASTA:
			stringOfdataFormat = "Aligned fasta";
			break;
		case UNALIGNED_GENBANK:
			stringOfdataFormat = "GenBank";
			break;
		case UNALIGNED_EMBL:
			stringOfdataFormat = "Unaligned EMBL";
			break;
		case UNALIGNED_PIRNBRF:
			stringOfdataFormat = "Unaligned PIR";
			break;
		case UNALIGNED_FASTA:
			stringOfdataFormat = "Unaligned fasta";
			break;
		case SIMULATOR:
			stringOfdataFormat = "Simu";
			break;
		case MAF_UNCOMPRESS:
			stringOfdataFormat = "Maf";
			break;
		case MAF_COMPRESSED:
			stringOfdataFormat = "Maf";
			break;
		case GENETIC_DIST:
			stringOfdataFormat = "Genetic distance";
			break;
		case EHEATMAP:
			stringOfdataFormat = "eGPS heatmap";
			break;
		case GENERAL_TEXT_FORMAT:
			stringOfdataFormat = "General text";
			break;
		case MATRIX_TABLE:
			stringOfdataFormat = "Matrix table";
			break;
		default:
			break;
		}

		return stringOfdataFormat;
	}

	public String getDataTypeNameFromDataType(int dataType) {
		String dataTypeName = null;
		switch (dataType) {
		case DataType.VCF:
			dataTypeName = "VCF";
			break;
		case DataType.TREE:
			dataTypeName = "TREE";
			break;
		case DataType.CIRCUSRNA:
			dataTypeName = "CIRCUSRNA";
			break;
		case DataType.SIMULATOR:
			dataTypeName = "SIMULATOR";
			break;
		case DataType.MAF:
			dataTypeName = "MAF";
			break;
		case DataType.MULTIPLE_SEQS:
			dataTypeName = "Multiple sequences";
			break;
		case DataType.GENETIC_DIST:
			dataTypeName = "Genetic Distance";
			break;
		case DataType.NEXUS:
			dataTypeName = "Nexus";
			break;
		case DataType.GENERAL_TEXT:
			dataTypeName = "General text file";
			break;
		default:
			dataTypeName = "NO_DATA";
			break;
		}
		return dataTypeName;
	}

	/** Get the index of this file in all input files! Not exists returns -1 */
	public int getFilePosition(File file) {
		int num = -1;
		for (int i = 0; i < inputFiles.size(); i++) {
			if (file.getAbsolutePath().equals(inputFiles.get(i).getAbsolutePath())) {
				num = i + 1;
				break;
			}
		}

		return num;
	}

	private boolean getIfContainsData() {
		return currentDataType != DataType.NO_DATA;
	}
}
