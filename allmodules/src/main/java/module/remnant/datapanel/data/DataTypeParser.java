package module.remnant.datapanel.data;


import static module.remnant.datapanel.data.DataFileSuffixNames.CIRCUSRNA_FILE_SUFFIX;
import static module.remnant.datapanel.data.DataFileSuffixNames.CLUSTALW_FILE_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.EHEATMAP_FILE_SUFFIX;
import static module.remnant.datapanel.data.DataFileSuffixNames.FASTA_FILE_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.GCGMSF_FILE_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.GENETIC_DIST_FILE_SUFFIX;
import static module.remnant.datapanel.data.DataFileSuffixNames.MAF_FILE_SUFFIX;
import static module.remnant.datapanel.data.DataFileSuffixNames.MEGA_FILE_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.NEXUS_FILE_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.PAML_FILE_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.PHYLIP_FILE_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.SIMULATOR_FILE_SUFFIX;
import static module.remnant.datapanel.data.DataFileSuffixNames.TREE_FILE_ETREE_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.TREE_FILE_NHX_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.TREE_FILE_NWK_SUFFIX_REG;
import static module.remnant.datapanel.data.DataFileSuffixNames.VCF_FILE_SUFFIX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import egps2.utils.common.model.datatransfer.TwoTuple;
import module.remnant.datapanel.data.validation.CommonValidate;
import msaoperator.DataforamtInfo;
import module.remnant.datapanel.data.validation.ValidateDistance;
import module.remnant.datapanel.data.validation.ValidateMAF;
import module.remnant.treeoperator.io.ValidateNEXUS;
import msaoperator.io.seqFormat.AbstractParser;
import msaoperator.io.seqFormat.parser.ClustalWParser;
import msaoperator.io.seqFormat.parser.FastaParser;
import msaoperator.io.seqFormat.parser.GCGMSFParser;
import msaoperator.io.seqFormat.parser.MEGAParser;
import msaoperator.io.seqFormat.parser.PAMLParser;
import msaoperator.io.seqFormat.parser.PHYParser;


/**
 * @author: mhl,yudalang
 */
public class DataTypeParser {
	final TwoTuple<Integer, Integer> firstLightJudgeData(File file) {

		String fileName = file.getName();
		String ext = getFileExtension(fileName);
		int dataType = 0,dataFormat = 0;
		
		if (ext.matches(VCF_FILE_SUFFIX)) {
			dataType =  DataType.VCF;
		} else if (ext.matches(CIRCUSRNA_FILE_SUFFIX)) {
			dataType = DataType.CIRCUSRNA;
		} else if (ext.matches(TREE_FILE_ETREE_SUFFIX_REG)) {
			dataType = DataType.TREE;dataFormat = DataFormat.TREE_ETREE;
		} else if (ext.matches(TREE_FILE_NWK_SUFFIX_REG)) {
			dataType = DataType.TREE;dataFormat = DataFormat.TREE_NH;
		} else if (ext.matches(TREE_FILE_NHX_SUFFIX_REG)) {
			dataType = DataType.TREE;dataFormat = DataFormat.TREE_NHX;
//		} else if (ext.matches(GENBANK_FILE_SUFFIX_REG)) {
//			return DataType.MULTIPLE_SEQS;
//		} else if (ext.matches(EMBL_FILE_SUFFIX_REG)) {
//			return DataType.MULTIPLE_SEQS;
//		} else if (ext.matches(STOCKHOLM_FILE_SUFFIX_REG)) {
//			return DataType.MULTIPLE_SEQS;
//		} else if (ext.matches(NEXML_FILE_SUFFIX_REG)) {
//			return DataType.NEXML;
//		} else if (ext.matches(PIRNBRF_FILE_SUFFIX_REG)) {
//			return DataType.MULTIPLE_SEQS;
		} else if (ext.matches(NEXUS_FILE_SUFFIX_REG)) {
			dataType = DataType.NEXUS;
		} else if (ext.matches(PHYLIP_FILE_SUFFIX_REG)) {
			dataType = DataType.MULTIPLE_SEQS;dataFormat =DataFormat.ALIGNED_PHYLIP;
		} else if (ext.matches(MEGA_FILE_SUFFIX_REG)) {
			dataType = DataType.MULTIPLE_SEQS;dataFormat = DataFormat.ALIGNED_MEGA;
		} else if (ext.matches(PAML_FILE_SUFFIX_REG)) {
			dataType = DataType.MULTIPLE_SEQS;dataFormat = DataFormat.ALIGNED_PAML;
		} else if (ext.matches(CLUSTALW_FILE_SUFFIX_REG)) {
			dataType = DataType.MULTIPLE_SEQS;dataFormat = DataFormat.ALIGNED_CLUSTALW;
		} else if (ext.matches(GCGMSF_FILE_SUFFIX_REG)) {
			dataType = DataType.MULTIPLE_SEQS;dataFormat = DataFormat.ALIGNED_GCGMSF;
		} else if (ext.matches(FASTA_FILE_SUFFIX_REG)) {
			dataType = DataType.MULTIPLE_SEQS;dataFormat = DataFormat.ALIGNED_FASTA;
		}else if (ext.matches(SIMULATOR_FILE_SUFFIX)) {
			dataType = DataType.SIMULATOR;
		}else if (ext.matches(MAF_FILE_SUFFIX)) {
			dataType = DataType.MAF;
		} else if (ext.matches(GENETIC_DIST_FILE_SUFFIX)) {
			dataType = DataType.GENETIC_DIST;
		}else if (ext.matches(EHEATMAP_FILE_SUFFIX)) {
			dataType = DataType.EHEATMAP;
		} else {
			boolean binaryFile = false;
			try {
				binaryFile = isBinaryFile(file);
			} catch (IOException e) {
				dataType = DataType.NO_DATA;
			}
			//如果是二进制文件就返回识别有误
			if (binaryFile) {
				dataType = DataType.NO_DATA;
			} else {
				dataType = DataType.GENERAL_TEXT;
			}
		}
		
		return new TwoTuple<Integer, Integer>(dataType, dataFormat);
	}
	
	/**
	 * YDL: this is from the stack over flow
	 * https://stackoverflow.com/questions/620993/determining-binary-text-file-type-in-java
	 * You should know there are no guaranteed way!!
	 * @param f : the input file
	 * @return
	 */
	boolean isBinaryFile3(File f) {
        String type = null;
		try {
			type = Files.probeContentType(f.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (type == null) {
            //type couldn't be determined, assume binary
            return true;
        } else if (type.contains("text")) {
            return false;
        } else {
            //type isn't text
            return true;
        }
    }
	/**
	 * YDL: this is from the stack over flow
	 * https://stackoverflow.com/questions/620993/determining-binary-text-file-type-in-java
	 * You should know there are no guaranteed way!!
	 * @param f : the input file
	 * @return
	 */

	public static boolean isBinaryFile(File f) throws IOException {
	    
		FileInputStream in = new FileInputStream(f);
	    int size = in.available();
	    if(size > 1024) size = 1024;
	    byte[] data = new byte[size];
	    in.read(data);
	    in.close();

	    int ascii = 0;
	    int other = 0;

	    for(int i = 0; i < data.length; i++) {
	        byte b = data[i];
	        if( b < 0x09 ) return true;

	        if( b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D ) ascii++;
	        else if( b >= 0x20  &&  b <= 0x7E ) ascii++;
	        else other++;
	    }

	    if( other == 0 ) return false;

	    return 100 * other / (ascii + other) > 95;
	}

	/**
	 * 返回一个文件的后缀名，有时候windows用户不知道怎么显示后缀名！
	 * 故一开始会特殊处理！
	 */
	private final String getFileExtension(String fileName) {
		// Windows 系统有时候用户不知道怎么样显示后缀名！
		if (fileName.endsWith("txt")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}

		String ext = null;
		if (fileName.endsWith("gz")) {
			int boundIndex = fileName.lastIndexOf('.');
			String tmp = fileName.substring(0, boundIndex);
			boundIndex = tmp.lastIndexOf('.');

			ext = fileName.substring(boundIndex + 1);

		} else {
			ext = fileName.substring(fileName.lastIndexOf('.') + 1);
		}

		return ext;
	}

	/** 
	 * Gets the current file format
	 * @return DataFormat index, else return the error code
	 */
	final DataforamtInfo getDataFormat(File file, TwoTuple<Integer, Integer> dt) {

		int currentDataType = dt.first;
		int currentDataFormat = dt.second;
		CommonValidate commonValidate = null;
		
		switch (currentDataType) {
//		case DataType.VCF:
//			commonValidate = new ValidateVCF(file);
//			break;
//		case DataType.CIRCUSRNA:
//			commonValidate = new ValidateCircusRNA(file);
//			break;
		case DataType.TREE:
			switch (currentDataFormat) {
			case DataFormat.TREE_ETREE:
//				commonValidate = new ValidatorETREE(file);
//				break;
//			case DataFormat.TREE_NHX:
//				commonValidate = new ValidatorNHX(file);
//				break;
			default:
//				commonValidate = new ValidatorNWK(file);
				break;
			}
			break;
		case DataType.MULTIPLE_SEQS:
//			commonValidate = packagingSeqValidate(file,currentDataFormat);
			break;
//		case DataType.SIMULATOR:
//			commonValidate = new ValidateSimulator(file);
//			break;
		case DataType.MAF:
			commonValidate = new ValidateMAF(file);
			break;
		case DataType.GENETIC_DIST:
			commonValidate = new ValidateDistance(file);
			break;
		case DataType.NEXML:
			// 这个以后再弄吧！
			break;
		case DataType.NEXUS:
			commonValidate = new ValidateNEXUS(file);
			break;
//		case DataType.EHEATMAP:
//			commonValidate = new ValidateEHEATMAP(file);
//			break;
		case DataType.GENERAL_TEXT:
			/**
			 * YDL : 
			 * in this case we need to judge whether a text file is the right format that we allowed!
			 */
			return reValidateFileFormatForGeneralTextFile(file);
		default:
			// in case of input data Type not include in our software!
			return new DataforamtInfo(false,DataFormatError.FILETYPEERROR);
		}

		throw new RuntimeException("Not implemented yet!");

//		if (commonValidate == null) {
//			return new DataforamtInfo(false,DataFormatError.FILETYPEERROR);
//
//		}else {
//			return commonValidate.getFileFormat();
//		}
	}

	private DataforamtInfo reValidateFileFormatForGeneralTextFile(File file) {

		List<AbstractParser> validates = new ArrayList<>();
		// Multiple sequence 多重的序列格式，fasta包含未比对的！
		validates.add(new ClustalWParser(file));
		validates.add(new FastaParser(file));
		validates.add(new GCGMSFParser(file));
		validates.add(new MEGAParser(file));
		validates.add(new PAMLParser(file));
		validates.add(new PHYParser(file));
		
		//Genetic distance(DIST)
//		validates.add(new ValidateDistance(file));
		//Phylogenetic tree(TREE)
//		validates.add(new ValidatorNWK(file));
//		validates.add(new ValidatorNHX(file));
//		validates.add(new ValidatorETREE(file));
		//NEXUS
//		validates.add(new ValidateNEXUS(file));
		//whole genome multiple alignment format (MAF)
//		validates.add(new ValidateMAF(file));
		//Variant Call Format (VCF)
//		validates.add(new ValidateVCF(file));
		//Matrix table (Table)
//		validates.add(new ValidateMatrixTable(file));
		//Population history model (SIMU)
//		validates.add(new ValidateSimulator(file));
		//EGPS heatmap (eheatmap)
//		validates.add(new ValidateEHEATMAP(file));
		
		//下面这些都要去掉
//		validates.add(new ValidateProteomics(file));
//		validates.add(new ValidateRnaExp(file));
//		validates.add(new ValidateCircusRNA(file));
		List<String> severalStrings = obtainFirstLines(file,5);
//		for (AbstractParser commonValidate : validates) {
//			if (commonValidate.detectFormat(severalStrings)) {
//				DataforamtInfo fileFormat = commonValidate.getFileFormat();
//				if (fileFormat.isSuccess()) {
//					return new DataforamtInfo(true, fileFormat.getDataFormatCode());
//				}
//			}
//		}
		//都不符合时还是返回，非二进制文件！
		return new DataforamtInfo(true,DataFormat.GENERAL_TEXT_FORMAT);
	}

	private List<String> obtainFirstLines(File file, int numOfLines) {
		List<String> ret = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String line = null;int index = 0;
			while ((line = br.readLine()) != null) {
				ret.add(line);
				//System.out.println(line);
				index ++;
				if (index == numOfLines) {
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("This is class: " + getClass());
//		System.out.println(Arrays.toString(ret));
		return ret;
	}

//	private final CommonValidate packagingSeqValidate(File file, int currentDataFormat) {
//		CommonValidate ret = null;
//
//		switch (currentDataFormat) {
//		case DataFormat.ALIGNED_PHYLIP:
//			ret = new PHYParser(file);
//			break;
//		case DataFormat.ALIGNED_MEGA:
//			ret = new MEGAParser(file);
//			break;
//		case DataFormat.ALIGNED_PAML:
//			ret = new PAMLParser(file);
//			break;
//		case DataFormat.ALIGNED_CLUSTALW:
//			ret = new ClustalWParser(file);
//			break;
//		case DataFormat.ALIGNED_GCGMSF:
//			ret = new GCGMSFParser(file);
//			break;
//		case DataFormat.ALIGNED_FASTA:
//			ret = new FastaParser(file);
//			break;
//		default:
//			ret = new FastaParser(file);
//			break;
//		}
//		return ret;
//
//	}
	

}
