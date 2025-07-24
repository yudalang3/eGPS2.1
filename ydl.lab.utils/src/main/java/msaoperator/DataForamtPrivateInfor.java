package msaoperator;

import org.apache.commons.lang3.tuple.Pair;

public interface DataForamtPrivateInfor {
	
	/**
	 * 传入另一个 DataForamtPrivateInfor，判断两者是否兼容！
	 * @param anotherInfor
	 * @return 第一个是否兼容；若不兼容第二个返回描述错误的String!
	 */
	Pair<Boolean, String> isCompatiable(DataForamtPrivateInfor anotherInfor);

	String getFormatName();
}
