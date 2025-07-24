package module.evolview.gfamily.work.beans;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import egps2.builtin.modules.voice.fastmodvoice.VoiceParameterParser;

/**
 * 从网页中获取数据,同时进行封装
 */
public class GeneMetaInfo {

    private SequenceElementInfo[] sequenceElements;
    private int geneLength = 29903;
    private String geneName = "I am gene";
    private String sequenceElementName = "I am sequence element";


    
    public GeneMetaInfo() {
		super();
	}

    public static GeneMetaInfo obtainTheGeneMetaInfo(File input) throws IOException {
        List<SequenceElementInfo> list = new ArrayList<SequenceElementInfo>();
        
        List<String> parameterLines = new ArrayList<>();
        List<String> readLines = FileUtils.readLines(input,StandardCharsets.US_ASCII);
        Iterator<String> iterator = readLines.iterator();
        while (iterator.hasNext()) {
			String next = iterator.next();
			if (next.startsWith("#")) {
				iterator.remove();
			}else if (next.startsWith("$")) {
				iterator.remove();
				parameterLines.add(next);
			}
		}
        
        List<String> referenceGemoneInfor = readLines;
        
        iterator = referenceGemoneInfor.iterator();
        //第一行不用
        iterator.next();
        while (iterator.hasNext()) {
            String next = iterator.next();
			String[] splitStrings = next.split("\t", -1);
			
			if (splitStrings.length < 4) {
				throw new InputMismatchException("Sorry the input is not have four elements.");
			}
            SequenceElementInfo geneInfor = new SequenceElementInfo();
            geneInfor.setGeneName(splitStrings[0]);
            geneInfor.setStartPos(Integer.parseInt(splitStrings[1]));
            geneInfor.setEndPos(Integer.parseInt(splitStrings[2]));
            Color color = getColor(splitStrings[3]);
			geneInfor.setColor(color);
            list.add(geneInfor);
        }
        
        GeneMetaInfo genomeInformation = new GeneMetaInfo();
        parseParameters(parameterLines, genomeInformation);
        
        // 如果什么都没有则填充一个
        if (list.isEmpty()) {
        	 SequenceElementInfo geneInfor = new SequenceElementInfo();
             geneInfor.setGeneName("");
             geneInfor.setStartPos(1);
             geneInfor.setEndPos(genomeInformation.geneLength);
             Color color = Color.gray;
 			geneInfor.setColor(color);
             list.add(geneInfor);
		}
        
        SequenceElementInfo[] results = list.toArray(new SequenceElementInfo[]{});
       
        genomeInformation.sequenceElements = results;
        
        
        
        return genomeInformation;
    }

	private static void parseParameters(List<String> parameterLines, GeneMetaInfo genomeInformation) {
		VoiceParameterParser parser = new VoiceParameterParser();
        Map<String, LinkedList<String>> parameters = parser.parseInputString4organization(parameterLines);
        
		{
			LinkedList<String> linkedList = parameters.get("$geneLength");
			if (linkedList != null) {

				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				int value = Integer.parseInt(stringAfterEqualStr);
				genomeInformation.geneLength = value;
			}
		}
		
		{
			LinkedList<String> linkedList = parameters.get("$geneName");
			if (linkedList != null) {

				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				genomeInformation.geneName = stringAfterEqualStr;
			}
		}
		{
			LinkedList<String> linkedList = parameters.get("$sequenceElementName");
			if (linkedList != null) {
				
				String stringAfterEqualStr = parser.getStringAfterEqualStr(linkedList.get(0));
				genomeInformation.sequenceElementName = stringAfterEqualStr;
			}
		}
	}



    private static Color getColor(String string) {
    	Color decode = Color.decode(string);
        return decode;
    }

	public int getGeneLength() {
		return geneLength;
	}

	public void setGeneLength(int geneLength) {
		this.geneLength = geneLength;
	}

	public SequenceElementInfo[] getSequenceElements() {
		return sequenceElements;
	}
	
	public void setSequenceElements(SequenceElementInfo[] sequenceElements) {
		this.sequenceElements = sequenceElements;
	}

	public String getGeneName() {
		return geneName;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	public String getSequenceElementName() {
		return sequenceElementName;
	}

	public void setSequenceElementName(String sequenceElementName) {
		this.sequenceElementName = sequenceElementName;
	}
	
	

}
