package msaoperator;


import org.apache.commons.lang3.tuple.Pair;

public class DefaultDataFormatPrivateInfor implements DataForamtPrivateInfor{

	@Override
	public Pair<Boolean, String> isCompatiable(DataForamtPrivateInfor anotherInfor) {
		return Pair.of(true, null);
	}

	@Override
	public String getFormatName() {
		return "default data format private information ";
	}

}
