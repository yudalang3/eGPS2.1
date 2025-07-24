package module.remnant.datapanel.data;

import java.io.File;
import java.util.List;

public interface IDataCenter {
	/**
	 * 测试能否载入文件，如果载入成功就返回true；否则返回false！
	 * @param files
	 * @return
	 */
	boolean loadingFiles(List<File> files);
	
	/**
	 * 根据输入文件，删除传入的文件引用。
	 * 重要: 如果文件全部都删除光了，应该有这样一个过程：
	 * set Status To Empty！
	 * 例如现在自带的 data center 是这样的！
	 * 
	 * public void setStatusToEmpty() {
	 * 	this.currentDataFormat = DataFormat.NO_FOMAT;
	 *	this.currentDataType = DataType.NO_DATA;
	 * }
	 * @param filesNeedRemove
	 */
	void removeAllExistedFiles(List<File> filesNeedRemove);
	/**
	 * 得到当前的数据格式！
	 * @return
	 */
	int getCurrentDataFormat();
	/**
	 * 得到当前的数据类型！
	 * @return
	 */
	int getCurrentDataType();

	/**
	 * 得到输入在里面的文件！
	 * @return 这个返回值永远不要返回空！
	 */
	List<File> getInputFiles();

	/**
	 * 得到错误类型的索引,如果载入文件有问题！
	 * @return
	 */
	int getInputFileErrorIndex();
	/**
	 * 从data format index得到名称
	 * @param dataFormat
	 * @return
	 */
	String getDataFormatNameFromDataFormat(int dataFormat);
	
	/**
	 * 从data type index得到名称
	 * @param dataType
	 * @return
	 */
	String getDataTypeNameFromDataType(int dataType);
	
	/**
	 * 从data format index 到data type index
	 * @param dataFormat
	 * @return
	 */
	default int getDataTypeFromDataFormat(int dataFormat) {
		return ((int) (dataFormat / 100)) * 100;
	}
}
