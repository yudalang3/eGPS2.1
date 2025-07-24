package module.mutationpre.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class VariantInfo {
	
	String variantName;

	List<InputMutationInfor> variantsList = new ArrayList<>();
}

class InputMutationInfor implements Comparable<InputMutationInfor>{
	int position;

	Color color = Color.black;

	String annotationString;
	
	@Override
	public int compareTo(InputMutationInfor o) {
		return this.position - o.position;
	}
}
