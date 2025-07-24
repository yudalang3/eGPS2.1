package module.bedmerger.calculate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class BedFileManager {
	
	boolean keepOriginalBedOrder = false;
	
	public List<BedRecord> mergeBedRecords(List<BedRecord> inputBedRecords){
		
		Map<String, List<BedRecord>> map = new HashMap<>(8192);
		List<String> keys = new ArrayList<String>(8196);
		
		// step1: 转载bed记录
		for (BedRecord bedRecord : inputBedRecords) {
			List<BedRecord> list = map.get(bedRecord.chromosome);
			
			if (list == null) {
				//List<BedRecord> arrayList = new ArrayList<>(8196);
				List<BedRecord> arrayList = new LinkedList<>();
				arrayList.add(bedRecord);
				map.put(bedRecord.chromosome, arrayList);
				
				keys.add(bedRecord.chromosome);
			}else {
				list.add(bedRecord);
			}
		}
		
		//处理同一染色体下的记录
		for (Entry<String, List<BedRecord>> entry : map.entrySet()) {
			List<BedRecord> value = entry.getValue();
			
			value.sort(new Comparator<BedRecord>() {
				@Override
				public int compare(BedRecord o1, BedRecord o2) {
					return o1.start - o2.start;
				}
			});
			
			if (value.size() < 2) {
				continue;
			}
			Iterator<BedRecord> iterator = value.iterator();
			
			BedRecord prev = iterator.next();
			
			while (iterator.hasNext()) {
				
				BedRecord current = iterator.next();
				
				if (current.start <= prev.end) {
					if (current.end < prev.end) {
						//nothing to do 
					}else {
						prev.end = current.end;
					}
					iterator.remove();
				}
				prev = current;
			}
		}
		
		if (!keepOriginalBedOrder) {
			Collections.sort(keys);
		}
		
		List<BedRecord> ret = new ArrayList<BedRecord>(32728);
		for (String kk : keys) {
			List<BedRecord> list = map.get(kk);
			ret.addAll(list);
		}
		
		return ret;
	}
	public List<BedRecord> mergeBedRecords(String filePath) throws IOException{
		return mergeBedRecords(getBedRecordsFromFile(filePath));
	}
	
	public void writeBedRecords2file(File outFile,List<BedRecord> listOfRecodBedRecords) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFile));
		for (BedRecord bedRecord : listOfRecodBedRecords) {
			bufferedWriter.write(bedRecord.toString());
			bufferedWriter.write("\n");
		}
		
		bufferedWriter.close();
	}
	
	public List<BedRecord> getBedRecordsFromFile(String filePath) throws IOException {
		List<String> readLines = FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8);
		return getBedRecordsFromLines(readLines);
	}
	
	
	public List<BedRecord> getBedRecordsFromLines(List<String> readLines) {
		
		List<BedRecord> listofBedRecords = new ArrayList<BedRecord>();
		
		for (String string : readLines) {
			
			string = string.trim();
			
			if (string.startsWith("track") || string.startsWith("browser")) {
				continue;
			}
			String[] split = string.split("\\s+");
			
			if (split.length < 3) {
				throw new IllegalArgumentException("Not a bed format file, it has at least three columns!");
			}
			
			BedRecord bedRecord = new BedRecord();
			
			bedRecord.chromosome = split[0];
			bedRecord.start = Integer.parseInt(split[1]);
			bedRecord.end = Integer.parseInt(split[2]);
			
			listofBedRecords.add(bedRecord);
		}
		
		return listofBedRecords;
	}

	List<BedRecord> listOfBedRecords;

	public static void main(String[] args) throws IOException {
		 BedFileManager bedFileManager = new BedFileManager();
		List<String> lines = Arrays.asList("chr1	10	20",
				"chr1	25	30",
				"chr1	27	35",
				"chr2	10	20",
				"chr2	25	30",
				"chr2	27	35");
		
		List<BedRecord> bedRecordsFromLines = bedFileManager.getBedRecordsFromLines(lines);
		
		List<BedRecord> mergeBedRecords = bedFileManager.mergeBedRecords(bedRecordsFromLines);
	
		System.out.println(mergeBedRecords);
		
//		bedFileManager.writeBedRecords2file(new File(pathname), listOfRecodBedRecords);
	
		
	}
}
