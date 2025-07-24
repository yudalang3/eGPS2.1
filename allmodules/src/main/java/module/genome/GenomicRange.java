package module.genome;

import java.util.Objects;
import java.util.StringJoiner;

public class GenomicRange extends Range {

	public char strand = '+';

	public static GenomicRange fromBio(int bioStart, int bioEnd, char strand) {
		GenomicRange ret = new GenomicRange();
		ret.start = bioStart - 1;
		ret.end = bioEnd;

		ret.strand = strand;
		return ret;
	}

	@Override
	public int hashCode() {
		return Objects.hash(end, start, strand);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenomicRange other = (GenomicRange) obj;
		return end == other.end && start == other.start && strand == other.strand;
	}

	public char bioStrand() {
		return strand;
	}

	public boolean isSameStrand(GenomicRange location) {
		return this.strand == location.strand;
	}

	@Override
	public String toString() {
		StringJoiner stringJoiner = new StringJoiner("\t");
		stringJoiner.add(String.valueOf(bioStrand()));
		stringJoiner.add(String.valueOf(bioStart()));
		stringJoiner.add(String.valueOf(bioEnd()));

		return stringJoiner.toString();
	}

}
