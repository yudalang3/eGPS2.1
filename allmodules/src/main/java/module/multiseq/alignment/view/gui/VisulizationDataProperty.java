package module.multiseq.alignment.view.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.LinkedList;
import java.util.List;

import egps2.UnifiedAccessPoint;
import msaoperator.alignment.sequence.SequenceI;
import module.multiseq.alignment.view.model.SequenceDataForAViewer;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @author mhl
 * 
 * @Date Created on:2019-07-16 17:13
 * 
 */
public class VisulizationDataProperty {

	/** The horizontal and vertical starting positions of the Seq. in the current displayed view! */
	private int startSeq;
	private int endSeq;
	
	/**
	 * Nucleotide residues, amino acid residues
	 */
	private int startResidue;
	private int endResidue;

	// Width and height of each char
	private int displayedCharWidth;
	private int displayedCharHeight;

	private Font font;

	private Color newColor;

	private SequenceDataForAViewer sequenceData;

	private final List<UserSelectedViewElement> selectionElements = new LinkedList<>();

	public VisulizationDataProperty(SequenceDataForAViewer sequenceData) {

		this.sequenceData = sequenceData;
		setFont(UnifiedAccessPoint.getLaunchProperty().getDefaultFont());
	}

	public List<UserSelectedViewElement> getSelectionElements() {
		return selectionElements;
	}
	
	public SequenceDataForAViewer getSequenceData() {
		return sequenceData;
	}

	public void setSequenceData(SequenceDataForAViewer sequenceData) {
		this.sequenceData = sequenceData;
	}

	public int getStartSeq() {
		return startSeq;
	}

	public void setStartSeq(int startSeq) {
		this.startSeq = startSeq;
	}

	public int getEndSeq() {
		return endSeq;
	}

	public void setEndSeq(int endSeq) {
		int sequenceCount = sequenceData.getTotalSequenceCount();
		if (endSeq > sequenceCount) {
			this.endSeq = sequenceCount;
		} else {
			this.endSeq = endSeq;
		}
	}

	public int getStartRes() {
		return startResidue;
	}

	public void setStartRes(int startRes) {
		this.startResidue = startRes;
	}

	public int getEndRes() {
		return endResidue;
	}

	public void setEndRes(int endRes) {
		List<SequenceI> dataSequences = sequenceData.getDataSequences();
		int maxSeqLength = -1;
		for (SequenceI sequenceI : dataSequences) {
			if (sequenceI.getLength() > maxSeqLength) {
				maxSeqLength = sequenceI.getLength();
			}
		}
		if (endRes > maxSeqLength) {
			this.endResidue = maxSeqLength;
		} else {
			this.endResidue = endRes;
		}
	}

	public int getCharWidth() {
		return displayedCharWidth;
	}

	public void setCharWidth(int charWidth) {
		this.displayedCharWidth = charWidth;
	}

	public int getCharHeight() {
		return displayedCharHeight;
	}

	public void setCharHeight(int charHeight) {
		this.displayedCharHeight = charHeight;
	}

	public void setFont(Font font) {
		this.font = font;
		Container c = new Container();
		FontMetrics fm = c.getFontMetrics(font);
		int ww = fm.charWidth('W');
		setCharHeight(fm.getHeight());
		setCharWidth(ww);
	}

	public Font getFont() {
		return font;
	}

	/**
	 * The length of the current page display data
	 */
	public int getLength() {
		if (startResidue >= endResidue) {
			return 0;
		}
		return endResidue - startResidue;
	}

	/**
	 *
	 * The number of horizontal displays on the current interface
	 * 
	 * @author mhl
	 *
	 * @Date Created on:2019-07-16 17:14
	 */
	public int getSize() {
		if (startSeq >= endSeq) {
			return 0;
		}
		return endSeq - startSeq;
	}

	/**
	 * 
	 *
	 * @see {@link egps.remnant.multiple.seq.alignment.viewer.model.SequenceDataForAViewer#getTotalSequenceCount()}
	 *
	 * @Date Created on:2019-07-16 17:19
	 */
	public int getTotalSequenceCount() {

		return sequenceData.getTotalSequenceCount();
	}

	public int getTotalSequenceLength() {

		return sequenceData.getLength();
	}

	public void setColor(Color newColor) {
		this.newColor = newColor;
	}

	public Color getColor() {
		return newColor;
	}

	public int getBaseNameLenght() {

		List<SequenceI> dataSequences = sequenceData.getDataSequences();

		if (dataSequences.size() == 0) {
			return 0;
		}
		Container c = new Container();

		FontMetrics fm = c.getFontMetrics(getFont());

		int maxBaseNameLenght = Integer.MIN_VALUE;

		for (SequenceI sequenceI : dataSequences) {

			String seqName = sequenceI.getSeqName();

			int stringWidth = fm.stringWidth(seqName);
			if (stringWidth > maxBaseNameLenght) {
				maxBaseNameLenght = stringWidth;
			}
		}
		return maxBaseNameLenght;
	}

	public void clearUserSelections() {
		selectionElements.clear();
	}

}
