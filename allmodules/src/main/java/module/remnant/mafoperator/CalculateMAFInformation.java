package module.remnant.mafoperator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import egps2.utils.common.model.datatransfer.TwoTuple;
import module.remnant.mafoperator.mafParser.MafFastaParser;

/**
* @author YFQ,yudalang
* @date Nov 21, 2018 3:05:29 PM  
*/
public class CalculateMAFInformation {
	
	List<String> seqNames = null;
	
	public CalculateMAFInformation() {
		
	}
	
	/**
	 * 输入一个文件，然后查看这个文件所在的目录是否有一个“maf.config” 主文件！
	 * @param file
	 * @return 如果有这个配置文件，返回true与相应的名称；否则就返回false
	 */
	public TwoTuple<Boolean, List<String>> checkMainConfigFile(File file) {
		boolean ifConfigFileExist = false;

		String filePath = file.getParent();
		File configFile = new File(filePath + "/maf.config");
		if (configFile.exists()) {
			seqNames = readerFile(configFile);
			ifConfigFileExist = true;
		} 
		
		return new TwoTuple<Boolean, List<String>>(ifConfigFileExist, seqNames);
	}
	
	public void checkAndProduceFileSpecificConfigFile(File file) {
		boolean flag = ifConfigFileExists(file);
		if (!flag) {
			List<String> names = findSpecies(file);
			createFile(file.getAbsolutePath(), names);
		}
	}
	
	
	/*
	 * Read configuration file.
	 */
	private List<String> readerFile(File file) {
		
		List<String> names = new ArrayList<>();
		
        BufferedReader reader = null;
        
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;    

            while ((tempString = reader.readLine()) != null) {
            	if (tempString.indexOf("#") < 0 && tempString.indexOf("Total size") < 0) {
            		names.add(tempString); 
				} 
            }  
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }
        
        return names;
        
	}
	
	/*
	 * Find all species names in the file.
	 */
	private List<String> findSpecies(File file) {
		
		MafFastaParser mfp = new MafFastaParser(file);
        int num = 0;
        HashSet<String> nameSet = new HashSet<>();
        
        while (mfp.parseBlocks()) {
        	if (mfp.getBlock().getSeqNames().size() > num) {
				List<String> names = mfp.getBlock().getSeqNames();
				nameSet.addAll(names);
			}
        }

        
        mfp.close();
        
        return new ArrayList<>(nameSet);
	}
	
	/*
	 * Create the configuration file corresponding to the file.
	 */
	private void createFile(String filePath, List<String> seqNames) {

		try {
			
			FileOutputStream out = new FileOutputStream(filePath+".config");
			PrintStream p = new PrintStream(out);
			
			p.println("##eGPS multiple alignment file configure file format(MAFconfig) version=1.0 totalSize="+seqNames.size());
			p.println("# list of genomes");
			
			for (int i = 0; i < seqNames.size(); i++) { 
				p.println(seqNames.get(i));
			}
			
			p.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * Determine whether the configuration file corresponding to the incoming file exists.
	 */
	private boolean ifConfigFileExists(File file) {
		String path = file.getAbsolutePath();
		File configFile = new File(path+".config");
		if (configFile.exists()) {
    		return true;
    	} else {
    		return false;
		}
	}
	
	/*
	 * Merge species names in all files.
	 */
	public void unionSepices(List<File> files) {
		if (seqNames == null) {
			HashSet<String> nameSet = new HashSet<>();
			
			for (int i = 0; i < files.size(); i++) {
				String path = files.get(i).getAbsolutePath();
				File configFile = new File(path+".config");
				nameSet.addAll(readerFile(configFile));
			}
			
			List<String> list = new ArrayList<String>(nameSet);
			
			seqNames = list;
		}
	}
	
	public List<String> getSeqList() {
		return seqNames;
	}
}
