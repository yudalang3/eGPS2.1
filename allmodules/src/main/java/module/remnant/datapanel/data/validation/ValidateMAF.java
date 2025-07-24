package module.remnant.datapanel.data.validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import module.remnant.datapanel.data.DataAlignmentFormat;
import module.remnant.datapanel.data.DataFormat;
import module.remnant.datapanel.data.DataFormatError;
import module.remnant.mafoperator.mafParser.MafLine;
import msaoperator.DataforamtInfo;

/**
 * 
 * Copyright © 2018 Chinese Academy of Sciences. All rights reserved.
 *
 * 
 * 
 * @Title: ValidateProteomics.java
 * 
 * @Prject: eGPS_beta_v1.0
 * 
 * @Package: egps.data
 * 
 * @author: mhl
 * 
 * @date: 2018/05/22 14:45:51
 * 
 * @version: V1.0
 */
public class ValidateMAF implements FormatDetect,CommonValidate {
	private File inputFile;

	public ValidateMAF(File file) {
		this.inputFile = file;
	}

	@Override
	public DataforamtInfo getFileFormat() {

		if (inputFile.getAbsolutePath().endsWith(".maf.gz")) {
			return isMafAlignmentGz();
		}

		return isMafAlignment();
	}

	public DataforamtInfo isMafAlignment() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			String line = null;
			line = reader.readLine();
			// Determine if the maf file starts with ##maf
			if (!line.startsWith("##maf")) {
				reader.close();
				return new DataforamtInfo(false,DataFormatError.MAF_HEADERERROR);
			}

			while ((line = reader.readLine()) != null) {
				if (line.trim().equals("")) {
					break;
				}
				
				//System.out.println(line.length());

				if (line.startsWith("a")) {
					// yudalang removed
//					String[] blocks = line.split("\\s+");
//					for (int i = 0; i < blocks.length; i++) {
//						String singleBlock = blocks[i];
//						if (i > 0) {
//							if (!singleBlock.startsWith("score") && !singleBlock.startsWith("pass")) {
//								reader.close();
//								return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
//							}
//						}
//					}
				} else if (line.startsWith("s")) {
					String[] blocks = line.split("\\s+");
					// If the sequence is at the beginning of s, the sequence index length is 7
					// yudalang removed
//					if (blocks.length != 7) {
//						reader.close();
//						return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
//					}
					for (int i = 2; i < blocks.length; i++) {
						String singleBlock = blocks[i];
						// Determine whether the start and total length of the index are positive
						// integers
						if (i == MafLine.COL_START || i == MafLine.COL_SIZE || i == MafLine.COL_SRCSIZE) {
							try {
								int parseInt = Integer.parseInt(singleBlock);
							} catch (Exception e) {
								return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
							}
						}
						// Either "+" or "-". If "-", then the alignment is to the reverse-complemented
						// source.Can only contain "+" or "-".
						else if (i == MafLine.COL_STRAND) {

							if (!singleBlock.equals("-") && !singleBlock.equals("+")) {
								return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
							}
						}
						// Check the nucleotide (or amino acid) in the alignment and any insertions
						// (dash).
						else if (i == MafLine.COL_TEXT) {
							char[] charArray = singleBlock.toCharArray();
							for (char c : charArray) {
								String valueOf = String.valueOf(c);
								if (!DataAlignmentFormat.ifAlignmentFormat(valueOf)) {
									return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
								}
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new DataforamtInfo(true,DataFormat.MAF_UNCOMPRESS);
		
	}

	public DataforamtInfo isMafAlignmentGz() {
//		try {
//			BlockCompressedInputStream reader = new BlockCompressedInputStream(new FileInputStream(inputFile));
//
//			String line = null;
//			line = reader.readLine();
//			// Determine if the maf file starts with ##maf
//			if (!line.startsWith("##maf")) {
//				reader.close();
//				return new DataforamtInfo(false,DataFormatError.MAF_HEADERERROR);
//			}
//			// Count the number of rows starting with a(totalEmpty++) and the number of rows
//			// ending with </br>(totalEmpty--)
//			while ((line = reader.readLine()) != null) {
//				if (line.trim().equals("")) {
//					break;
//				}
//
//				if (line.startsWith("a")) {
//					String[] blocks = line.split("\\s+");
//					for (int i = 0; i < blocks.length; i++) {
//						String singleBlock = blocks[i];
//						if (i > 0) {
//							if (!singleBlock.startsWith("score") && !singleBlock.startsWith("pass")) {
//								System.out.println("2");
//								return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
//							}
//						}
//					}
//				} else if (line.startsWith("s")) {
//					String[] blocks = line.split("\\s+");
//					// If the sequence is at the beginning of s, the sequence index length is 7
//					if (blocks.length != 7) {
//						return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
//					}
//					for (int i = 2; i < blocks.length; i++) {
//						String singleBlock = blocks[i];
//						// Determine whether the start and total length of the index are positive
//						// integers
//						if (i == MafLine.COL_START || i == MafLine.COL_SIZE || i == MafLine.COL_SRCSIZE) {
//							try {
//								@SuppressWarnings("unused")
//								int parseInt = Integer.parseInt(singleBlock);
//
//							} catch (Exception e) {
//								System.out.println("4");
//								return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
//								
//							}
//						}
//						// Either "+" or "-". If "-", then the alignment is to the reverse-complemented
//						// source.Can only contain "+" or "-".
//						else if (i == MafLine.COL_STRAND) {
//
//							if (!singleBlock.equals("-") && !singleBlock.equals("+")) {
//								System.out.println("5");
//								return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
//							}
//						}
//						// Check the nucleotide (or amino acid) in the alignment and any insertions
//						// (dash).
//						else if (i == MafLine.COL_TEXT) {
//							char[] charArray = singleBlock.toCharArray();
//							for (char c : charArray) {
//								String valueOf = String.valueOf(c);
//								if (!DataAlignmentFormat.ifAlignmentFormat(valueOf)) {
//									return new DataforamtInfo(false,DataFormatError.MAF_BLOCKERROR);
//								}
//							}
//						}
//					}
//				}
//			}
//
//		} catch (IOException e) {
//			return new DataforamtInfo(false,DataFormatError.FILETYPEERROR);
//		}

		return new DataforamtInfo(true,DataFormat.MAF_COMPRESSED);
	}

	@Override
	public boolean detectFormat(List<String> strings) {
		if (strings.get(0).startsWith("##maf")) {
			return true;
		}
		return false;
	}
}
