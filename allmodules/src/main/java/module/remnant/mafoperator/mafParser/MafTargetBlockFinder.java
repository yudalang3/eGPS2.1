package module.remnant.mafoperator.mafParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MafTargetBlockFinder {
	
	final static char GAP_CHR = '-';
	
	public static void main(String[] args) {
		MafTargetBlockFinder mafPositionFinder = new MafTargetBlockFinder();
		mafPositionFinder.runProcess(args);
	}

	public void runProcess(String[] args) {
		if (args == null || args.length < 3) {
			System.err.println("Usage:\n"
					+ "java -cp maf_postion_converter.jar egps.core.popGen.mafParser.MafTargetBlockFinder maf_file location_file chr_file");
		}
		
		File mafFile = new File(args[0]);
		File inputLocation = new File(args[1]);
		File chrFile = new File(args[2]);
		
		boolean verbose = false;
		if (args.length == 4 && "-v".equals(args[3])) {
			verbose = true;
		}
		
		List<QueryRecord> queryRecords = getInputQueryMap(inputLocation,chrFile);
		
		Map<String, GenomicRegion2MafBlockHandler> queryChrMap = new HashMap<>();
		List<ResultRecord> resultRecords = new ArrayList<ResultRecord>();

		for (QueryRecord queryRecord : queryRecords) {
			String chr = queryRecord.getChr();
			GenomicRegion2MafBlockHandler chromosomePositionHandler = queryChrMap.get(chr);
			if (chromosomePositionHandler == null) {
				chromosomePositionHandler = new GenomicRegion2MafBlockHandler(chr, resultRecords);
				ArrayList<QueryRecord> arrayList = new ArrayList<>();
				arrayList.add(queryRecord.clone());
				chromosomePositionHandler.setQueryGenomicRegions(arrayList);
				queryChrMap.put(chr, chromosomePositionHandler);
			} else {
				chromosomePositionHandler.getQueryGenomicRegions().add(queryRecord.clone());
			}
		}

		MafFastaParser mfp = new MafFastaParser(mafFile);

		int numOfLines = 0;

		while (mfp.parseBlocks()) {
			MafBlock block = mfp.getBlock();
			List<MafLine> lines = block.getLines();

			for (MafLine mafLine : lines) {
				GenomicRegion2MafBlockHandler chromosomePositionHandler = queryChrMap.get(mafLine.getRefSrc());
				if (chromosomePositionHandler != null) {
					chromosomePositionHandler.handleOneMafBlock(mafLine,lines);
				}
			}

//        	numOfLines ++;
//        	
//        	if (numOfLines > 2000 ) {
//				break;
//			}

		}
		mfp.close();
		
		Map<QueryRecord, List<ResultRecord>> mapResults = new HashMap<>();
		for (ResultRecord resultRecord : resultRecords) {
			QueryRecord queryRecord = resultRecord.getQueryRecord();
			
			List<ResultRecord> list = mapResults.get(queryRecord);
			if (list == null) {
				list = new ArrayList<ResultRecord>();
				list.add(resultRecord);
				mapResults.put(queryRecord, list);
			}else {
				list.add(resultRecord);
			}
			
		}
		
		for (QueryRecord qq : queryRecords) {
			List<ResultRecord> resultRecords1 = mapResults.get(qq);
			if (resultRecords1 == null) {
				System.out.println("## "+qq.getChr() +"\t"+qq.getStrand()+ "\t" + (qq.getStartPos()+1) + "\t"+(qq.getStartPos()+qq.getLength())+"\t Null");
			} else {
				System.out.println("## "+qq.getChr() + "\t"+qq.getStrand()+ "\t" + (qq.getStartPos()+1) + "\t"+(qq.getStartPos()+qq.getLength())+"    find block is:");
				int size = resultRecords1.size();
				for (int i = 0; i < size; i++) {
					System.out.println("block " + (i + 1) +" is:");
					ResultRecord tmpResultRecord = resultRecords1.get(i);
					
					List<MafLine> mafLines = tmpResultRecord.getMafLines();
					anchorStartOrEndLocation(qq,mafLines);
					for (MafLine resultRecord : tmpResultRecord.getMafLines()) {
						System.out.println(resultRecord.toString(verbose));
					}
				}
			}

		}
		
	}
	
	private void anchorStartOrEndLocation(QueryRecord qq, List<MafLine> mafLines) {
		int startPos = qq.getStartPos();
		int endPos = qq.getStartPos() + qq.getLength();
		String chr = qq.getChr();
		
		boolean startAnchored = false;
		int startAnchoredPositonInvokeGaps = 0;
		boolean endAnchored = false;
		int endAnchoredPositonInvokeGaps = 0;
		
		for (MafLine mafLine : mafLines) {
			if (mafLine.getRefSrc().equals(chr)) {
				int mafStart = mafLine.getStart();
				int mafEnd = mafStart + mafLine.getSize();
				if (startPos >= mafStart && startPos < mafEnd) {
					startAnchored = true;
					startAnchoredPositonInvokeGaps = getPositionInvokeGaps(mafLine.getSequence(),startPos - mafStart);
				}
				
				if (endPos >= mafStart && endPos < mafEnd) {
					endAnchored = true;
					endAnchoredPositonInvokeGaps = getPositionInvokeGaps(mafLine.getSequence(),endPos - mafStart);
				}
			}
		}
		
		//System.out.println(startAnchoredPositonInvokeGaps+"    "+endAnchoredPositonInvokeGaps);
		if (startAnchored) {
			for (MafLine mafLine : mafLines) {
				StringBuffer seqBuffer = mafLine.getSeqBuffer();
				seqBuffer.insert(startAnchoredPositonInvokeGaps, "^");
			}
		}
		if (endAnchored) {
			for (MafLine mafLine : mafLines) {
				StringBuffer seqBuffer = mafLine.getSeqBuffer();
				int tt = startAnchored ? 1 : 0;
				seqBuffer.insert(endAnchoredPositonInvokeGaps + tt, "$");
			}
		}
		
	}

	private int getPositionInvokeGaps(String sequence, int passedChars) {
		int ret = 0;
		int length = sequence.length();
		for (int i = 0; i < length; i++) {
			char cc = sequence.charAt(i);
			if (cc != GAP_CHR) {
				if (ret == passedChars) {
					break;
				}
				ret ++;
			}
		}
		
		return ret;
	}

	private List<QueryRecord> getInputQueryMap(File inputLocation, File chrFile) {
		List<QueryRecord> ret = new ArrayList<QueryRecord>();
		Map<String, Integer> chr2sizeMap = new HashMap<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(chrFile));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.trim().split("\t");
				chr2sizeMap.put(split[0], Integer.parseInt(split[1]));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		

		try (BufferedReader br = new BufferedReader(new FileReader(inputLocation));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				String[] split = line.trim().split("\t");
				String mafFormatSrc = split[0];
				QueryRecord queryRecord = new QueryRecord();
				queryRecord.setChr(mafFormatSrc);
				queryRecord.setStrand('+');
				// 1 based to 0 based!
				int startOneBased = Integer.parseInt(split[1]);
				int endOneBased = Integer.parseInt(split[2]);
				queryRecord.setStartPos(startOneBased - 1);
				int length = endOneBased - startOneBased + 1;
				queryRecord.setLength(length);
				
				QueryRecord queryRecordReverse = queryRecord.clone();
				queryRecordReverse.setStrand('-');
				int indexOfDot = mafFormatSrc.indexOf(".") ;
				
				
				ret.add(queryRecord);
				
				if (indexOfDot != -1) {
					String chrKey = mafFormatSrc.substring(indexOfDot + 1);
					Integer chromSize = chr2sizeMap.get(chrKey);
					if (chromSize != null) {
						int ss = chromSize - endOneBased;
						//System.out.println((ss+ 1)+"   "+(ss+length));
						queryRecordReverse.setStartPos(ss );
						queryRecordReverse.setLength(length);
						ret.add(queryRecordReverse);
					}else {
						System.err.println(chrKey +"  Not contained in your chr file!");
					}
				}else {
					System.err.println(mafFormatSrc +"  Not a MAF format field!");
				}
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

}

class GenomicRegion2MafBlockHandler{
	
	private List<ResultRecord> resultRecords;
	private String queryChr;
	
	private List<QueryRecord> queryGenomicRegions;
	
	
	public GenomicRegion2MafBlockHandler(String queryChr, List<ResultRecord> resultRecords) {
		super();
		this.resultRecords = resultRecords;
		this.queryChr = queryChr;
	}
	public void handleOneMafBlock(MafLine mafLineWithQueryOverlap, List<MafLine> lines) {
		int start = mafLineWithQueryOverlap.getStart();
		int size = mafLineWithQueryOverlap.getSize();
		for (QueryRecord queryRecord : queryGenomicRegions) {
			boolean isOverlap = isOverlaped(queryRecord.getStartPos(),queryRecord.getLength(),start,size);
			
			if (isOverlap ) {
				//System.out.println(queryRecord.getStrand()+"  "+mafLineWithQueryOverlap.getStrand());
				if (queryRecord.getStrand() == mafLineWithQueryOverlap.getStrand()) {
					ResultRecord resultRecord = new ResultRecord();
					resultRecord.setMafLines(lines);
					resultRecord.setQueryRecord(queryRecord);
					resultRecords.add(resultRecord);
				}
				
			}
		}
		
	}
	private boolean isOverlaped(int queryStart, int queryLen, int targetStart, int targetLen) {
		//System.out.println(queryStart+" "+queryLen+" "+targetStart+" "+targetLen);
		if (queryStart< targetStart) {
			return targetStart < (queryStart + queryLen);
		}else {
			return queryStart < (targetStart + targetLen);
		}
		
	}
	public void setQueryGenomicRegions(ArrayList<QueryRecord> arrayList) {
		this.queryGenomicRegions = arrayList;
		
	}
	
	
	public List<QueryRecord> getQueryGenomicRegions() {
		return queryGenomicRegions;
	}
	
	
}
