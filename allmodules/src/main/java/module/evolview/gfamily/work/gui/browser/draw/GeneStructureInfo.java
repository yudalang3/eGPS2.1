package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import module.evolview.gfamily.work.beans.GeneMetaInfo;
import module.evolview.gfamily.work.beans.SequenceElementInfo;

/**
 * 用于基因的每个block位置信息封装,
 * 所有的坐标都是1-based
 */
public class GeneStructureInfo {

	String[] sequenceElementNames;
	Integer[] startPoses; // inclusive
	Integer[] endPoses; // inclusive
	Integer[] listOfNucleotideUsedTwice; // 1-based
	Color[] colors;
	
	public int geneLength;

	String geneName;
	String sequenceElementName;
	
	private static final Color DEFAULT_COLOR = new Color(88,193,255,80);

	public GeneStructureInfo(GeneMetaInfo tt) {
		if (tt != null) {
			this.geneLength = tt.getGeneLength();
			
			geneName = tt.getGeneName();
			sequenceElementName = tt.getSequenceElementName();

			SequenceElementInfo[] orfInformations = tt.getSequenceElements();
			ArrayList<String> geneNames = new ArrayList<>();
			ArrayList<Integer> startPoses = new ArrayList<>();
			ArrayList<Integer> endPoses = new ArrayList<>();
			ArrayList<Color> colors = new ArrayList<>();
			ArrayList<Integer> listOfNucleotideUsedTwice = new ArrayList<>();
			int tempIndex = -1;
			int preEndPos = -1;
			for (SequenceElementInfo orfInformation : orfInformations) {
				geneNames.add(orfInformation.getGeneName());
				int startPos = orfInformation.getStartPos();
				startPoses.add(startPos);
				int endPos = orfInformation.getEndPos();
				endPoses.add(endPos);
				colors.add(orfInformation.getColor());
				if (preEndPos > startPos) {
					listOfNucleotideUsedTwice.add(startPos);
					preEndPos = endPos;
					continue;
				}
				listOfNucleotideUsedTwice.add(tempIndex);
				preEndPos = endPos;
			}
			this.sequenceElementNames = geneNames.toArray(new String[] {});
			this.startPoses = startPoses.toArray(new Integer[] {});
			this.endPoses = endPoses.toArray(new Integer[] {});
			this.colors = colors.toArray(new Color[] {});
			this.listOfNucleotideUsedTwice = listOfNucleotideUsedTwice.toArray(new Integer[] {});

		}
	}

	/**
	 * 通过位置获取改区域的颜色信息
	 */
	public Color getColorByPosition(int pos) {
		for (int i = 0; i < startPoses.length; i++) {
			int start = startPoses[i];
			int end = endPoses[i];
			if (pos >= start && pos <= end) {
				return colors[i];
			}
		}
		return DEFAULT_COLOR;
	}

	private ArrayList<Integer[]> blanks;
	/**
	 * 计算排除block位置之外的信息
	 */
	public List<Integer[]> calculateBlank() {
		if (blanks == null) {
			if (geneLength < endPoses[endPoses.length - 1]) {
				throw new IllegalArgumentException("The gene length less than last end position.");
			}
			RangeSet<Integer> numberRangeSet = TreeRangeSet.create();
			for (int i = 0; i < startPoses.length; i++) {
				// 这里用了 canonical form 它相当于是一个防御措施，可以把紧挨着的range merge起来
				Range<Integer> canonical = Range.closed(startPoses[i], endPoses[i]).canonical(DiscreteDomain.integers());
				numberRangeSet.add(canonical);
			}
			
//			logger.info("The span range is {} to {}", experienceSpan.lowerEndpoint(),experienceSpan.upperEndpoint() );

			// 直接调用了 GUAVA的代码，哈哈偷懒一下
			//注意取补集之后，变成了左开右开
			RangeSet<Integer> numberRangeComplementSet
		      = numberRangeSet.complement();
			
			Set<Range<Integer>> asRanges = numberRangeComplementSet.asRanges();
			// 这时是一个 canonical form 左闭右开
			
//			logger.info(numberRangeComplementSet.toString());
			ArrayList<Integer> blankStartPos = new ArrayList<>();
			ArrayList<Integer> blankEndPos = new ArrayList<>();
			// 一定要注意，现在是左闭右开，因为我输入的时候是 canonical form
			for (Range<Integer> range : asRanges) {
				if (!range.hasLowerBound()) {
					// first element
					//右开
					int intValue = range.upperEndpoint().intValue();
					if (intValue > 1) {
						blankStartPos.add(1);
						blankEndPos.add(intValue - 1);
					}
					continue;
				}
				if (!range.hasUpperBound()) {
					// last element
					int intValue = range.lowerEndpoint().intValue();
					// 左闭
					if (intValue <= geneLength) {
						blankStartPos.add(intValue);
						blankEndPos.add(geneLength);
					}
					
					continue;
				}
				
				// 处理的时候注意，左闭右开
				blankStartPos.add(range.lowerEndpoint());
				blankEndPos.add(range.upperEndpoint() - 1);

			}
			blanks = new ArrayList<>();
			
			Integer[] blankStartIntegers = blankStartPos.toArray(new Integer[blankStartPos.size()]);
			Integer[] blankendIntegers = blankEndPos.toArray(new Integer[blankStartPos.size()]);

			blanks.add(blankStartIntegers);
			blanks.add(blankendIntegers);
		}
		

		return blanks;
	}


	public Integer[] getStartPoses() {
		return startPoses;
	}

	public void setStartPoses(Integer[] startPoses) {
		this.startPoses = startPoses;
	}

	public Integer[] getEndPoses() {
		return endPoses;
	}

	public void setEndPoses(Integer[] endPoses) {
		this.endPoses = endPoses;
	}

	public Integer[] getListOfNucleotideUsedTwice() {
		return listOfNucleotideUsedTwice;
	}

	public void setListOfNucleotideUsedTwice(Integer[] listOfNucleotideUsedTwice) {
		this.listOfNucleotideUsedTwice = listOfNucleotideUsedTwice;
	}

	public Color[] getColors() {
		return colors;
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}
	
	public int getGeneLength() {
		return geneLength;
	}
	
	public void setGeneLength(int geneLength) {
		this.geneLength = geneLength;
	}

	public String[] getSequenceElementNames() {
		return sequenceElementNames;
	}

	public void setSequenceElementNames(String[] sequenceElementNames) {
		this.sequenceElementNames = sequenceElementNames;
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
