package module.remnant.datapanel.data;

import msaoperator.DataForamtPrivateInfor;

/**
 * 一个类用来存储每个文件判定之后的情况；为后续总结提供基础！
 * @author yudalang
 */
public class JudgedFileStatusBean {
	
	//判断变量
	//每个文件都有一个data type与data format code
	private String fileName;
	//文件是否有效，就是说文件的data type/data format都符合，并且内容都符合。优先根据后缀名来判断！
	private boolean available;
	// 没有文件就是NO_DATA
	private int dataTypeCode;
	// 文件format不对的话这里存储的是error code
	//对的话就是dataForamtCode
	private int dataFormatCode;
	//一个data format的私有域
	private DataForamtPrivateInfor privateData;


	//总结变量
	//下面的这些总结时候被赋值的
	private boolean avilable = false;
	private boolean ifRepeat = false;
	//ydl：科研一般输入的都是英文，默认是utf-8与anssic
	//private boolean ifUTF8 = false;
	private boolean ifDataTypeCorrect = false;
	private boolean ifDataFormatCorrect = false;
	private boolean ifInforamtionConsistant = false;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public int getDataTypeCode() {
		return dataTypeCode;
	}
	public void setDataTypeCode(int dataTypeCode) {
		this.dataTypeCode = dataTypeCode;
	}
	public int getDataFormatCode() {
		return dataFormatCode;
	}
	public void setDataFormatCode(int dataFormatCode) {
		this.dataFormatCode = dataFormatCode;
	}
	public DataForamtPrivateInfor getPrivateData() {
		return privateData;
	}
	public void setPrivateData(DataForamtPrivateInfor privateData) {
		this.privateData = privateData;
	}
	public boolean isAvilable() {
		return avilable;
	}
	public void setAvilable(boolean avilable) {
		this.avilable = avilable;
	}
	public boolean isIfRepeat() {
		return ifRepeat;
	}
	public void setIfRepeat(boolean ifRepeat) {
		this.ifRepeat = ifRepeat;
	}
	public boolean isIfDataTypeCorrect() {
		return ifDataTypeCorrect;
	}
	public void setIfDataTypeCorrect(boolean ifDataTypeCorrect) {
		this.ifDataTypeCorrect = ifDataTypeCorrect;
	}
	public boolean isIfDataFormatCorrect() {
		return ifDataFormatCorrect;
	}
	public void setIfDataFormatCorrect(boolean ifDataFormatCorrect) {
		this.ifDataFormatCorrect = ifDataFormatCorrect;
	}
	public boolean isIfInforamtionConsistant() {
		return ifInforamtionConsistant;
	}
	public void setIfInforamtionConsistant(boolean ifInforamtionConsistant) {
		this.ifInforamtionConsistant = ifInforamtionConsistant;
	}
	
	
	

}
