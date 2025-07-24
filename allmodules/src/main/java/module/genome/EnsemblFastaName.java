package module.genome;

import java.util.Optional;
import java.util.StringJoiner;

import utils.string.EGPSStringUtil;

public class EnsemblFastaName {

	String name;
	String type;
	String info;
	String sequenceType;

	public EnsemblFastaName(String str) {
		String[] splits = EGPSStringUtil.split(str, ' ');
		setName(splits[0]);
		setType(splits[1]);
		setInfo(splits[2]);

		if (splits.length == 4) {
			setSequenceType(splits[3]);
		}

	}

	@Override
	public String toString() {
		StringJoiner stringJoiner = new StringJoiner("\t");
		stringJoiner.add(name).add(type).add(info).add(sequenceType);
		return stringJoiner.toString();
	}

	public EnsemblFastaName() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Optional<String> getSequenceType() {
		return Optional.ofNullable(sequenceType);
	}

	public void setSequenceType(String sequenceType) {
		this.sequenceType = sequenceType;
	}

}
