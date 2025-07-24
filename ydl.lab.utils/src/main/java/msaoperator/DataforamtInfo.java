package msaoperator;

import java.util.Optional;

public class DataforamtInfo {
	
	private boolean isSuccess;
	/**
	 * If not success, the this variable will be error code!
	 */
	private int dataFormatCode;
	
	private DataForamtPrivateInfor dataForamtPrivateInfor;

	public DataforamtInfo(boolean isSuccess, int dataFormatCode, DataForamtPrivateInfor dataForamtPrivateInfor) {
		this.isSuccess = isSuccess;
		this.dataFormatCode = dataFormatCode;
		this.dataForamtPrivateInfor = dataForamtPrivateInfor;
	}

	public DataforamtInfo(boolean b, int filetypeerror) {
		this(b, filetypeerror, null);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public int getDataFormatCode() {
		return dataFormatCode;
	}

	public Optional<DataForamtPrivateInfor> getDataForamtPrivateInfor() {
		return Optional.ofNullable(dataForamtPrivateInfor);
	}
	
	public void setDataForamtPrivateInfor(DataForamtPrivateInfor dataForamtPrivateInfor) {
		this.dataForamtPrivateInfor = dataForamtPrivateInfor;
	}

}
