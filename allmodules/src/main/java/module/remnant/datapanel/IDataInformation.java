package module.remnant.datapanel;

import java.io.File;

public interface IDataInformation {

	/**
	 * ydl: 当左侧选中的文件改变，那么就调用这个方法，从新调用这个方法！
	 * 但是除了vcf与maf文件，这个不需要变。而其它的文件都需要变更。
	 * @param file
	 */
	void loadingInformation(File file);
	
	/**
	 * ydl: 返回每个data type各自的参数！
	 * 现在需要调用的有：1. vcf 2. maf 3.matrix table
	 * 
	 * @return 返回最顶层的Object对象！
	 */
	Object getInputParameters();
}
