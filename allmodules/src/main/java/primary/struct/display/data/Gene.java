package primary.struct.display.data;

import java.util.List;

public class Gene {
	private List<Integer> length;
	private List<Integer> start;
	private List<Integer> end;
	private List<String> color;

	// Getters and setters
	public List<Integer> getLength() {
		return length;
	}

	public void setLength(List<Integer> length) {
		this.length = length;
	}

	public List<Integer> getStart() {
		return start;
	}

	public void setStart(List<Integer> start) {
		this.start = start;
	}

	public List<Integer> getEnd() {
		return end;
	}

	public void setEnd(List<Integer> end) {
		this.end = end;
	}

	public List<String> getColor() {
		return color;
	}

	public void setColor(List<String> color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "GeneData{" + "length=" + length + ", start=" + start + ", end=" + end + ", color=" + color + '}';
	}
}