package module.heatmap.eheatmap.model;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import egps2.utils.common.model.datatransfer.FourTuple;
import module.heatmap.eheatmap.model.selection.AnnotationSelection;

public class AnnotaionParaObj implements Cloneable{

	List<byte[]> colAnnotaionIndexes = new ArrayList<byte[]>();// zero based!
	List<Color[]> colAnnColors = new ArrayList<Color[]>();
	List<String> colAnnotationNames = new ArrayList<String>();
	List<String[]> colAnnotationCaseNames = new ArrayList<>();
	
	
	List<byte[]> rowAnnotaionIndexes = new ArrayList<byte[]>();// zero based!
	List<Color[]> rowAnnColors = new ArrayList<Color[]>();
	List<String> rowAnnotationNames = new ArrayList<String>();
	List<String[]> rowAnnotationCaseNames = new ArrayList<>();
	
	Font font = new Font("Arial", Font.PLAIN, 16);
	
	Random random = new Random();
	
	@Override
	protected AnnotaionParaObj clone() throws CloneNotSupportedException {
		AnnotaionParaObj ret = new AnnotaionParaObj();
		// ret.colAnnotaionIndexes.add
		ret.colAnnotaionIndexes.addAll(this.colAnnotaionIndexes);
		ret.colAnnColors.addAll(this.colAnnColors);
		ret.colAnnotationNames.addAll(this.colAnnotationNames);
		ret.colAnnotationCaseNames.addAll(this.colAnnotationCaseNames);
		
		ret.rowAnnotaionIndexes.addAll(this.rowAnnotaionIndexes);
		ret.rowAnnColors.addAll(this.rowAnnColors);
		ret.rowAnnotationNames.addAll(this.rowAnnotationNames);
		ret.rowAnnotationCaseNames.addAll(this.rowAnnotationCaseNames);
		
		ret.font = this.font;
		ret.random = this.random;

		return ret;
	}
	 
	public int getNumOfColAnnotaions() {
		if(colAnnotaionIndexes == null) {
			return 0;
		}
		return colAnnotaionIndexes.size();
	}
	
	public int getNumOfRowAnnotaions() {
		if(rowAnnotaionIndexes == null) {
			return 0;
		}
		return rowAnnotaionIndexes.size();
	}
	
	public void clear() {
		  clearColAnnotations();
		  clearRowAnnotations();
	}

	public void clearRowAnnotations() {
		rowAnnotaionIndexes.clear();
		rowAnnColors.clear();
		rowAnnotationNames.clear();
		rowAnnotationCaseNames.clear();
	}

	public void clearColAnnotations() {
		colAnnColors.clear();
		colAnnotaionIndexes.clear();
		colAnnotationNames.clear();
		colAnnotationCaseNames.clear();
	}
	
	public boolean ifRowAnnotationEmpty() {
		return rowAnnotationNames.isEmpty();
	}
	public boolean ifColAnnotationEmpty() {
		return colAnnotationNames.isEmpty();
	}
	
	public void addARowAnnotation(String name,Color[] colors,byte[] indexes,String[] caseNames) {
		rowAnnotationNames.add(name);
		rowAnnColors.add(colors);
		rowAnnotaionIndexes.add(indexes);
		rowAnnotationCaseNames.add(caseNames);
	}
	
	public void addAColAnnotation(String name,Color[] colors,byte[] indexes,String[] caseNames) {
		colAnnotationNames.add(name);
		colAnnColors.add(colors);
		colAnnotaionIndexes.add(indexes);
		colAnnotationCaseNames.add(caseNames);
	}
	public void addARowAnnotation(String name,Color[] colors,byte[] indexes) {
		rowAnnotationNames.add(name);
		rowAnnColors.add(colors);
		rowAnnotaionIndexes.add(indexes);
		rowAnnotationCaseNames.add(generateCaseNames(indexes,true));
	}
	
	public void addAColAnnotation(String name,Color[] colors,byte[] indexes) {
		colAnnotationNames.add(name);
		colAnnColors.add(colors);
		colAnnotaionIndexes.add(indexes);
		colAnnotationCaseNames.add(generateCaseNames(indexes,false));
	}
	
	public void addARowAnnotation(String name,byte[] indexes) {
		rowAnnotationNames.add(name);
		Color[] colors = generateColors(indexes,rowAnnColors);
		rowAnnColors.add(colors);
		rowAnnotaionIndexes.add(indexes);
		rowAnnotationCaseNames.add(generateCaseNames(indexes,true));
	}
	
	public void addAColAnnotation(String name,byte[] indexes) {
		colAnnotationNames.add(name);
		
		Color[] colors = generateColors(indexes,colAnnColors);
		colAnnColors.add(colors);
		colAnnotaionIndexes.add(indexes);
		colAnnotationCaseNames.add(generateCaseNames(indexes,false));
	}
	
	public void updateAnnotations(boolean ifRow,int index,Color newColor,String text,byte[] ba) {
		if (ifRow) {
			Color[] colors = rowAnnColors.get(index);
			int len = colors.length;
			Color[] newColors = new Color[ len+ 1];
			System.arraycopy(colors, 0, newColors, 0, len);
			newColors[len] = newColor;
			rowAnnColors.set(index, newColors);
			
			String[] strings = rowAnnotationCaseNames.get(index);
			len = strings.length;
			String[] newStrings = new String[ len+ 1];
			System.arraycopy(strings, 0, newStrings, 0, len);
			newStrings[len] = text;
			rowAnnotationCaseNames.set(index, newStrings);
			
			rowAnnotaionIndexes.set(index, ba);
		}else {
			Color[] colors = colAnnColors.get(index);
			int len = colors.length;
			Color[] newColors = new Color[ len+ 1];
			System.arraycopy(colors, 0, newColors, 0, len);
			newColors[len] = newColor;
			colAnnColors.set(index, newColors);
			
			String[] strings = colAnnotationCaseNames.get(index);
			len = strings.length;
			String[] newStrings = new String[ len+ 1];
			System.arraycopy(strings, 0, newStrings, 0, len);
			newStrings[len] = text;
			colAnnotationCaseNames.set(index, newStrings);
			
			colAnnotaionIndexes.set(index, ba);
		}
	}
	
	private String[] generateCaseNames(byte[] indexes, boolean ifRowName) {
		int needNum = getNeededNumber(indexes);
		List<String> ret = new ArrayList<String>();
		
		for (int i = 0; i < needNum; i++) {
			if (ifRowName) {
				ret.add("RowCluster"+ (i+1));
			}else {
				ret.add("ColCluster"+ (i+1));
			}
		}
		
		return ret.toArray(new String[0]);
	}

	private int getNeededNumber(byte[] indexes) {
		int max = 1;
		for (byte b : indexes) {
			if (b > max) {
				max  = b;
			}
		}
		return max + 1;
	}

	public FourTuple<List<String>, List<Color[]>, List<byte[]>,List<String[]>> getRowAnnoParas() {
		return new FourTuple<List<String>, List<Color[]>, List<byte[]>,List<String[]>>(
				rowAnnotationNames, rowAnnColors, rowAnnotaionIndexes,rowAnnotationCaseNames);
	}
	public FourTuple<List<String>, List<Color[]>, List<byte[]>,List<String[]>> getColAnnoParas() {
		return new FourTuple<List<String>, List<Color[]>, List<byte[]>,List<String[]>>(
				colAnnotationNames, colAnnColors, colAnnotaionIndexes,colAnnotationCaseNames);
	}

	private Color[] generateColors(byte[] indexes, List<Color[]> lc) {
		int needNumOfCols = getNeededNumber(indexes);

		Color[] colors = new Color[needNumOfCols];
		for (int i = 0; i < needNumOfCols; i++) {
			colors[i] = randomColor();
		}
		return colors;
	}
	
	private Color randomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void clearAnnotations(List<AnnotationSelection> annotationSelections) {
		for (AnnotationSelection annotationSelection : annotationSelections) {
			boolean ifRow = annotationSelection.isIfRow();
			int theStorageIndex = annotationSelection.getTheStorageIndex();
			if (ifRow) {
				rowAnnotaionIndexes.remove(theStorageIndex);
				rowAnnColors.remove(theStorageIndex);
				rowAnnotationNames.remove(theStorageIndex);
				rowAnnotationCaseNames.remove(theStorageIndex);
			}else {
				colAnnColors.remove(theStorageIndex);
				colAnnotaionIndexes.remove(theStorageIndex);
				colAnnotationNames.remove(theStorageIndex);
				colAnnotationCaseNames.remove(theStorageIndex);
			}
		}
		
	}
	
}
