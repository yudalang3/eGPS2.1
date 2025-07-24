package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Color;
import java.util.List;

/**
 * 绘制引物的信息
 */
public class DrawingPropertyPrimers {

    private List<DrawingPropertyPrimersNucleotide> forwardPrimersNucleotides;//Nucleotide信息
    private String institution;
    private String gene;
    private int index;
    private int fStart;
    private int fEnd;
    private int rStart;
    private int rEnd;
    private String forward;
    private String reverse;
    private Color primersColor;//短带颜色
    private int usedTwice;
    private boolean referencePrimers;
	public List<DrawingPropertyPrimersNucleotide> getForwardPrimersNucleotides() {
		return forwardPrimersNucleotides;
	}
	public void setForwardPrimersNucleotides(List<DrawingPropertyPrimersNucleotide> forwardPrimersNucleotides) {
		this.forwardPrimersNucleotides = forwardPrimersNucleotides;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getFStart() {
		return fStart;
	}
	public void setFStart(int fStart) {
		this.fStart = fStart;
	}
	public int getFEnd() {
		return fEnd;
	}
	public void setFEnd(int fEnd) {
		this.fEnd = fEnd;
	}
	public int getRStart() {
		return rStart;
	}
	public void setRStart(int rStart) {
		this.rStart = rStart;
	}
	public int getREnd() {
		return rEnd;
	}
	public void setREnd(int rEnd) {
		this.rEnd = rEnd;
	}
	public String getForward() {
		return forward;
	}
	public void setForward(String forward) {
		this.forward = forward;
	}
	public String getReverse() {
		return reverse;
	}
	public void setReverse(String reverse) {
		this.reverse = reverse;
	}
	public Color getPrimersColor() {
		return primersColor;
	}
	public void setPrimersColor(Color primersColor) {
		this.primersColor = primersColor;
	}
	public int getUsedTwice() {
		return usedTwice;
	}
	public void setUsedTwice(int usedTwice) {
		this.usedTwice = usedTwice;
	}
	public boolean isReferencePrimers() {
		return referencePrimers;
	}
	public void setReferencePrimers(boolean referencePrimers) {
		this.referencePrimers = referencePrimers;
	}


    
}
