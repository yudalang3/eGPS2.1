package module.alignedpro2cds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import fasta.io.FastaReader;
import org.apache.commons.io.FileUtils;

public class AlignedProt2AlignedCDS {
	
	public static void makeTheConversion(File alignedProtFile,File unAlignedCDSFile,File outputFile) throws IOException {
		
		//这里应该有一系列的 check
		// 1. 蛋白质的长度应该是 CDS的 1/3 。 要考虑有些蛋白质可能最后一位是 *
		// 2. 两个fasta的序列的数量应该相等
		// 3. 用户输入的蛋白质序列应该和CDS序列一一对应，否则要出错
		
		LinkedHashMap<String, String> protSeqs = FastaReader.readFastaDNASequence(alignedProtFile);
		LinkedHashMap<String, String> cdsSeqs = FastaReader.readFastaDNASequence(unAlignedCDSFile);
		
		
		List<Entry<String, String>> protSeqSet = new ArrayList<>( protSeqs.entrySet() );
		List<Entry<String, String>> cdsSeqSet = new ArrayList<>( cdsSeqs.entrySet() );
		
		int size = protSeqSet.size();
		
		List<String> output = new ArrayList<>();
		
		StringBuilder sBuilder = new StringBuilder();
		
		for (int i = 0; i < size; i++) {
			String protSeq = protSeqSet.get(i).getValue();
			Entry<String, String> entry = cdsSeqSet.get(i);
			String cdsSeq = entry.getValue();
			
			char[] charArray = protSeq.toCharArray();
			
			int indexOfAA = 0;
			sBuilder.setLength(0);
			for (char c : charArray) {
				if (c == '-') {
					sBuilder.append("---");
				}else {
					int fromIndex = indexOfAA * 3;
					String substring = cdsSeq.substring(fromIndex, fromIndex + 3);
					sBuilder.append(substring);
					
					indexOfAA ++;
				}
			}
			
			output.add(">".concat(entry.getKey()));
			output.add(sBuilder.toString());
			
			// for check
			System.out.print(protSeq.replaceAll("-", "").length() * 3 + "\t" );
			System.out.println(sBuilder.toString().replaceAll("-", "").length());
		}
		
		FileUtils.writeLines(outputFile, output);
		
		
	}

}
