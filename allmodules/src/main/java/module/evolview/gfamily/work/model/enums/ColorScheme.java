package module.evolview.gfamily.work.model.enums;

public enum ColorScheme {

	NO_COLOR(1), COUNTRIESREGIONS(2), GENDER(3), PATIENTAGE(4), COLLECTIONDATE(5), CUSTOMIZED(0);

	private int index;

	private ColorScheme(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static ColorScheme getColorSchemeAccording2index(int index) {
		
		switch (index) {
		case 1:
			return NO_COLOR;
		case 2:
			return COUNTRIESREGIONS;
		case 3:
			return GENDER;
		case 4:
			return PATIENTAGE;
		case 5:
			return COLLECTIONDATE;
		default:
			return CUSTOMIZED;
		}
	}

}
