package module.remnant.datapanel.data.validation;

import msaoperator.DataforamtInfo;

public interface CommonValidate {
	
	/**
	 * First element indicate weather success!
	 * Second is the integer indicate the error code(for fail) or file format code(for success)!
	 * Third element is the private inforamtion!
	 */
	DataforamtInfo getFileFormat();
}
