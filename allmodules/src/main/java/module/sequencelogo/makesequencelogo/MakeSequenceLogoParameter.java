package module.sequencelogo.makesequencelogo;

import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.tuple.Triple;

public class MakeSequenceLogoParameter {

	// 当通过MEMEChipOut生成sequence logo图时最多展示的图的数量
	public int numOfLabelsDisplayed4MEMEChipOut = 5;
	// 坐标轴刻度线的长度
	protected int tickLength = 20;
	// sequence logo字体的类型（除Q以外）
	protected Font seqLogoFontType;
	// sequence logo中Q的字体的类型，因为Q比较特殊，所以单独设置了一种字体
	protected Font seqLogoFontTypeOfQ;
	// numOfseqAndfrequenceOfBasesPerPositionAndBitsOfBasesPerPosition = NFB;
	protected Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> NFB;
	// True:类型为bits;Flase:类型为频率图
	protected boolean seqLogoType;
	// 0代表是DNA或者RNA序列，1代表是蛋白质序列
	protected int sequenceType;
	// True:代表横坐标的角度是竖直的；False:代表横坐标的角度是水平的
	protected boolean abscissaAngle;
	// 选择sequence logo的字体大小时是否选择了默认字体
	protected boolean isSeqLogoFontSizeDefault;
	// 画板的宽度
	protected double drawingBoardWidth;
	// 画板的高度
	protected double drawingBoardHeight;
	// sequence logo的字体大小
	protected int seqLogoFontSize;
	// 在sequence logo的字体大小一定时，字母在画板上画出时的实际宽度
	protected double seqLogoFontWidth;
	// 在sequence logo的字体大小一定时，字母在画板上画出时的实际高度
	protected double seqLogoFontHeight;
	// 坐标轴的字体类型
	protected Font coordinateAxisFontType = new Font("Serif", Font.BOLD, 30);
	// 初始横坐标
	protected double initialAbscissa;
	// 初始纵坐标
	protected double initialOrdinate;
	// 每一个字母需要调整的位置，例如‘G’，如果绘制的坐标是（0，0），
	//但实际上‘G’的下边界会超出一小部分（y坐标为2），所以需要把这超出的一小部分
	//消除，实际绘制‘G’的时候坐标设置为（0，-2）就会消除这个影响
	protected HashMap<Character, Double> adjustedPositionOfEachLetter;

	public void setNFB(
			Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> NFB) {
		this.NFB = NFB;
	}

	public void setSeqLogoType(boolean seqLogoType) {
		this.seqLogoType = seqLogoType;
	}

	public void setSequenceType(int sequenceType) {
		this.sequenceType = sequenceType;
	}

	public void setAbscissaAngle(boolean abscissaAngle) {
		this.abscissaAngle = abscissaAngle;
	}

	public void setDrawingBoardWidth(double drawingBoardWidth) {
		this.drawingBoardWidth = drawingBoardWidth;
	}

	public void setdrawingBoardHeight(double drawingBoardHeight) {
		this.drawingBoardHeight = drawingBoardHeight;
	}

	public void setIsSeqLogoFontSizeDefault(Boolean isSeqLogoFontSizeDefault) {
		this.isSeqLogoFontSizeDefault = isSeqLogoFontSizeDefault;
	}

	public void setSeqLogoFontSize(int seqLogoFontSize) {
		this.seqLogoFontSize = seqLogoFontSize;
	}

	public void setSeqLogoFontWidth(double seqLogoFontWidth) {
		this.seqLogoFontWidth = seqLogoFontWidth;
	}

	public void setSeqLogoFontHeight(double seqLogoFontHeight) {
		this.seqLogoFontHeight = seqLogoFontHeight;
	}

	public void setSeqLogoFontType() {
		this.seqLogoFontType = new Font("Consolas", Font.BOLD, seqLogoFontSize);
	}

	public void setSeqLogoFontTypeOfQ() {
		//this.seqLogoFontTypeOfQ = new Font("Source Code Pro", Font.BOLD, seqLogoFontSize);
		this.seqLogoFontTypeOfQ = new Font("Consolas", Font.BOLD, seqLogoFontSize);
	}

	public void setCoordinateAxisFontType(Font coordinateAxisFontType) {
		this.coordinateAxisFontType = coordinateAxisFontType;
	}

	// 一定要在画的高度设置完之后在进行设置
	public void setInitialCoordinate() {
		this.initialAbscissa = 150.0;
		this.initialOrdinate = drawingBoardHeight - 200;
	}
	
	public void setAdjustedPositionOfEachLetter(HashMap<Character, Double> adjustedPositionOfEachLetter) {
		this.adjustedPositionOfEachLetter = adjustedPositionOfEachLetter;
	}
	
	public Triple<Integer, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>, LinkedHashMap<Integer, LinkedHashMap<Character, Double>>> getNFB() {
		return NFB;
	}

	public boolean getSeqLogoType() {
		return seqLogoType;
	}

	public int getSequenceType() {
		return sequenceType;
	}

	public boolean getAbscissaAngle() {
		return abscissaAngle;
	}

	public double getDrawingBoardWidth() {
		return drawingBoardWidth;
	}

	public double getdrawingBoardHeight() {
		return drawingBoardHeight;
	}

	public boolean getIsSeqLogoFontSizeDefault() {
		return isSeqLogoFontSizeDefault;
	}

	public int getSeqLogoFontSize() {
		return seqLogoFontSize;
	}

	public double getSeqLogoFontWidth() {
		return seqLogoFontWidth;
	}

	public double getSeqLogoFontHeight() {
		return seqLogoFontHeight;
	}

	public Font getSeqLogoFontType() {
		return seqLogoFontType;
	}

	public Font getSeqLogoFontTypeOfQ() {
		return seqLogoFontTypeOfQ;
	}

	public Font getCoordinateAxisFontType() {
		return coordinateAxisFontType;
	}
	
	public double getInitialAbscissa() {
		return initialAbscissa;
	}
	
	public double getInitialOrdinate() {
		return initialOrdinate;
	}
	
	public HashMap<Character, Double> getAdjustedPositionOfEachLetter() {
		return adjustedPositionOfEachLetter;
	}
}
