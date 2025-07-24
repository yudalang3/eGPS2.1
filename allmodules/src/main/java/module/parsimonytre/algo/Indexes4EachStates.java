package module.parsimonytre.algo;

import java.util.List;

class Indexes4EachStates {
	private final List<Integer>[] chosenOfChildren;

	public Indexes4EachStates(List<Integer>[] chosenOfChildren) {
		this.chosenOfChildren = (List<Integer>[]) chosenOfChildren;
	}

	public List<Integer>[] getChosenOfChildren() {
		return this.chosenOfChildren;
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		byte b;
		int i;
		List<Integer>[] arrayOfList;
		for (i = (arrayOfList = this.chosenOfChildren).length, b = 0; b < i;) {
			List<Integer> list = arrayOfList[b];
			stringBuilder.append(list.toString()).append("&");
			b++;
		}

		return stringBuilder.toString();
	}
}

