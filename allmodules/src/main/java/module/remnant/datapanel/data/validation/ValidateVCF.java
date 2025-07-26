package module.remnant.datapanel.data.validation;//package egps.remnant.datapanel.data.validation;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.List;
//
//import egps2.module.data.datapanel.remnant.DataCenter;
//import egps2.module.data.datapanel.remnant.DataFormat;
//import egps2.module.data.datapanel.remnant.DataFormatError;
//import egps2.module.data.datapanel.remnant.IDataCenter;
//import egps.remnant.vcfoperator.calculator.popCal.VCFFileFormatBasicInfo;
//import egps.remnant.vcfoperator.calculator.popCal.VCFReader;
//import htsjdk.samtools.util.BlockCompressedInputStream;
//
///**
// * 
// * @author: mhl,yudalang
// */
//public class ValidateVCF implements FormatDetect,CommonValidate {
//	public static final String STANDARD_INDEX_EXTENSION = ".tbi";
//	private File inputFile;
//
//	public ValidateVCF(File file) {
//		this.inputFile = file;
//	}
//
//	public ValidateVCF() {}
//
//	private static List<String> currentCompleteSampleIndividualIDs;
//	private IDataCenter dataCenter = new DataCenter();
//
//	@Override
//	public DataforamtInfo getFileFormat() {
//
//		VCFReader vcfReader = new VCFReader();
//		vcfReader.setVcfFile(inputFile.getAbsolutePath());
//
//		List<String> annotations = vcfReader.getStringAnnotations();
//	
//		// If there is no comment in the file. eg: vcf.gz.tbi File
//		if (annotations.size() == 0) {
//			return new DataforamtInfo(false,DataFormatError.VCF_NOANNOTATIONSERROR);
//		}
//		
//		if (!judegeVCFContentSize(inputFile.getAbsolutePath())) {
//			return new DataforamtInfo(false,DataFormatError.VCF_NOCONTENT_ERROR);
//		}
//		
//		// See the first line!
//		if (!annotations.get(0).contains("VCF")) {
//			return new DataforamtInfo(false,DataFormatError.VCF_HEADERERROR);
//		}
//		// See the GT location
//		if (!judegeVcfGTLocation(vcfReader)) {
//			return new DataforamtInfo(false,DataFormatError.VCF_GTERROR);
//		}
//
//		// See the sampleIDs
//		if (!isCompatibleWithOthers(vcfReader)) {
//			return new DataforamtInfo(false,DataFormatError.VCF_UNSAMPLEIDSERROR);
//		}
//		// Determine if the format of the file is the same <br>
//		// Data format eg: .vcf; .gz(GZIP compressionand BZIP compression)
//		DataforamtInfo judegeVcfFormat = judegeVcfFormat();
//		List<String> completeIndividualIDs = vcfReader.getCompleteIndividualIDs();
//		VCFPrivateDataInfor vcfPrivateDataInfor = new VCFPrivateDataInfor(completeIndividualIDs.toArray(new String[0]));
//		judegeVcfFormat.setDataForamtPrivateInfor(vcfPrivateDataInfor);
//		return judegeVcfFormat;
//	}
//
//	private boolean judegeVCFContentSize(String absolutePath) {
//		VCFReader reader = new VCFReader();
//		reader.setVcfFile(inputFile.getAbsolutePath());
//
//		String vcfLine;
//		try {
//			while ((vcfLine = reader.getLine()) != null) {
//				if (vcfLine.trim().length() > 0) {
//					return true;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return false;
//	}
//
//	private DataforamtInfo judegeVcfFormat() {
//		if (!inputFile.getName().contains(".gz")) {
//			return new DataforamtInfo(true,DataFormat.VCF_UNCOMPRESS);
//		}
//
//		BlockCompressedInputStream inputStream = null;
//		try {
//			inputStream = new BlockCompressedInputStream(new FileInputStream(inputFile));
//			inputStream.readLine();
//
//		} catch (Exception e) {
//			return new DataforamtInfo(true,DataFormat.VCF_GZIP);
//		} finally {
//			try {
//				inputStream.close();
//			} catch (IOException e) {
//				throw new RuntimeException("Close the failure");
//			}
//		}
//		return new DataforamtInfo(true,DataFormat.VCF_BZIP);
//	}
//
//	// See the GT location
//	private boolean judegeVcfGTLocation(VCFReader vcfReader) {
//		try {
//			String snpRecord = vcfReader.getLine();
//			String[] elements = snpRecord.split("\t");
//			if (!elements[VCFFileFormatBasicInfo.colIndexOfFORMAT].startsWith("GT")) {
//				return false;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return true;
//	}
//
//	// See the sampleIDs
//	public boolean isCompatibleWithOthers(VCFReader vcfReader) {
//		List<File> inputFiles = dataCenter.getInputFiles();
//		if (inputFiles.size() == 0) {
//			currentCompleteSampleIndividualIDs = null;
//		}
//		
//		List<String> completeSampleIndivIDs = vcfReader.getCompleteIndividualIDs();
//		if (currentCompleteSampleIndividualIDs != null) {
//			for (int i = 0; i < completeSampleIndivIDs.size(); i++) {
//				if (completeSampleIndivIDs.size() != currentCompleteSampleIndividualIDs.size()
//						|| !completeSampleIndivIDs.get(i).equalsIgnoreCase(currentCompleteSampleIndividualIDs.get(i))) {
//
//					return false;
//				}
//			}
//
//		} else {
//			currentCompleteSampleIndividualIDs = completeSampleIndivIDs;
//		}
//		return true;
//	}
//
//	/**
//	 * @Title: isTbiFileExist
//	 * @Description:Check that the directory contains the corresponding tbi files
//	 * @param filepath
//	 *            A pathname string
//	 * @return boolean
//	 */
//	public boolean isTbiFileExist(String filePath) {
//
//		boolean checkFile = false;
//		File file = new File(filePath);
//		String path = file.getAbsolutePath();
//		String tbiPath = path + STANDARD_INDEX_EXTENSION;
//		String fileName = file.getName();
//		String directoryName = path.substring(0, path.indexOf(fileName));
//		File fileDirectory = new File(directoryName);
//		String[] filelist = fileDirectory.list();
//		for (int i = 0; i < filelist.length; i++) {
//			File readFile = new File(directoryName + "/" + filelist[i]);
//			if (readFile.getAbsolutePath().equals(tbiPath)) {
//				checkFile = true;
//				break;
//			}
//		}
//
//		return checkFile;
//	}
//
//	@Override
//	public boolean detectFormat(List<String> strings) {
//		if (strings.get(0).startsWith("##fileformat=VCF")) {
//			return true;
//		}
//		return false;
//	}
//
//}