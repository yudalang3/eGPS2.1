package module.genome;

import java.util.Objects;

public class Range implements Comparable<Range> {

	public int start;
	public int end;

	@Override
	public int hashCode() {
		return Objects.hash(end, start);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Range other = (Range) obj;
		return end == other.end && start == other.start;
	}

	public static Range fromBio(int bioStart, int bioEnd) {
		Range range = new Range();
		range.start = bioStart - 1;
		range.end = bioEnd;
		return range;
	}

	public int length() {
		return this.end - this.start;
	}

	public int bioStart() {
		return this.start + 1;
	}

	public int bioEnd() {
		return this.end;
	}

	@Override
	public int compareTo(Range o) {
		int i = this.start - o.start;
		if (i == 0) {
			i = this.end - o.end;
		}
		return i;
	}

	@Override
	public String toString() {
		// 使用 String.format 格式化输出
		String formattedOutput = String.format("%,d-%,d", start, end);
		return formattedOutput;
	}

	public static void main(String[] args) {
		String string = Range.fromBio(100000, 400000000).toString();

		System.out.println(string);
	}

}
